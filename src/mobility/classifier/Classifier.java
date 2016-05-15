package mobility.classifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Categorizes each line as commuting, work, or home.
 * 
 * @author adrianoc
 *
 */
public class Classifier {

	private Double precision = 0.0; // the maximum distance to consider at same place. This value is setup in the properties file.
	private Double businessHoursStart = 8.0;
	private Double businessHoursEnd = 17.0;
	private ArrayList<String> fileList=new ArrayList<String>();
	private String filesPath=".";

	public static void main(String args[]){
		Classifier classifier = new Classifier();
		classifier.run();
	}

	public void run(){
		this.loadParameters();
		for(String file:fileList){
			ArrayList<String> buffer = ReadWriteFileBuffer.readToBuffer(this.filesPath+"//"+file);
			buffer = categorizeCommuting(buffer);
			ReadWriteFileBuffer.writeBackToBuffer(buffer,this.filesPath+ "//", "new_"+file);
		}
	}

	
	private void loadParameters(){
		PropertyManager manager = new PropertyManager();
		manager.initialize();
		this.filesPath = manager.FILES_PATH;
		this.fileList = manager.deviceFileList;
		this.precision = manager.locationPrecision;
		this.businessHoursEnd = manager.businessHoursEnd;
		this.businessHoursStart = manager.businessHoursStart;
	}
	
	private ArrayList<String> categorizeCommuting(ArrayList<String> buffer) {

		//DEVICE ID, HOUR
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

		for(String line : buffer){
			String[] lineTokens = line.split(",");
			String deviceID = lineTokens[0];
			String hourIndex = lineTokens[1];

			String hashKey = deviceID+"_"+hourIndex;

			if(map.containsKey(hashKey)){
				ArrayList<String> sameDeviceSameHourLines = map.get(hashKey);
				sameDeviceSameHourLines.add(line);
				map.put(hashKey, sameDeviceSameHourLines);
			}
			else{
				ArrayList<String> sameDeviceSameHourLines = new ArrayList<String>();
				sameDeviceSameHourLines.add(line);
				map.put(hashKey, sameDeviceSameHourLines);
			}

		}

		return null;
	}

	private void checkMoving(HashMap<String, ArrayList<String>> map){

		HashMap<String, ArrayList<String>> taggedMap = new HashMap<String, ArrayList<String>>();

		for(Map.Entry<String, ArrayList<String>> entry : map.entrySet()){
			ArrayList<String> list = entry.getValue();
			if(list!=null && list.size()>0){
				String mobilityFlag="";
				if(hasMovement(list)){
					mobilityFlag = "Commuting";
				}
				else{
					if(checkAtHome(list.get(0))){
						mobilityFlag = "Home";
					}
					else{
						mobilityFlag = "Work";
					}
				}
				list = this.fillMobilityFields(list, mobilityFlag);

				String hashKey = entry.getKey();
				taggedMap.put(hashKey, list);
			}
		}
	}


	public boolean hasMovement( ArrayList<String> lineList) {

		Double sourceLatitude = extractField(3,lineList.get(0));
		Double sourceLongitude = extractField(4,lineList.get(0));

		boolean movementFound = false;
		int index = 1;
		while(index<lineList.size()){
			String line = lineList.get(index);
			Double latitute = extractField(3,line);
			Double longitude = extractField(4,line);
			if((sourceLatitude - latitute != this.precision) || 
					(sourceLongitude - longitude != this.precision))
			{//moving!
				movementFound=true;
				break;
			}
			index++;
		}	
		System.out.println("movementFound: "+movementFound);
		return movementFound;
	}


	private Double extractField(int i, String sourceLine) {

		String[] tokens = sourceLine.split(",");
		return new Double(tokens[i]);
	}

	public boolean checkAtHome(String line){

		Integer timeStamp = extractField(2,line).intValue();
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(timeStamp *1000);
		calendar.setTime(date);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY); //Military 24h day.
		Integer AmPm = calendar.get(Calendar.AM_PM);

		//System.out.println("hour:"+ hour+", AM/PM:"+AmPm);

		if(hour>this.businessHoursEnd || hour<this.businessHoursStart)
			return true;
		else
			return false;
	}

	private ArrayList<String> fillMobilityFields(ArrayList<String>lineList, String mobilityTag){

		ArrayList<String> taggedLineList = new ArrayList<String>();

		for(String line: lineList){
			line = line.concat(", "+mobilityTag);
			taggedLineList.add(line);
		}
		return taggedLineList;
	}

}
