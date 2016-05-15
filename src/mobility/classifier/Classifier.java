package mobility.classifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mobility.util.ClassifierPropertyManager;
import mobility.util.ReadWriteFileBuffer;

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
	private String readPath=".";
	private String writePath=".";
	
	private boolean trim=true;

	public static void main(String args[]){
		Classifier classifier = new Classifier();
		classifier.run();
	}

	public void run(){
		this.loadParameters();
		for(String file:this.fileList){
			ArrayList<String> buffer = ReadWriteFileBuffer.readToBuffer(this.readPath+"//"+file);
			HashMap<String, ArrayList<String>> map = buildMap(buffer);
			buffer = categorize(map);
			ReadWriteFileBuffer.writeBackToBuffer(buffer,this.writePath+ "//", "new_00025_"+file);
		}
	}

	
	private void loadParameters(){
		ClassifierPropertyManager manager = new ClassifierPropertyManager();
		manager.initialize();
		this.readPath = manager.READ_PATH;
		this.fileList = manager.deviceFileList;
		this.writePath = manager.WRITE_PATH;
		this.precision = manager.locationPrecision;
		this.businessHoursEnd = manager.businessHoursEnd;
		this.businessHoursStart = manager.businessHoursStart;
	}
	
	private HashMap<String, ArrayList<String>> buildMap(ArrayList<String> buffer) {

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
		return map;
	}

	private  ArrayList<String> categorize(HashMap<String, ArrayList<String>> map){

		ArrayList<String> categorizedList = new  ArrayList<String>();

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
				categorizedList.addAll(list);
			}
		}
		return categorizedList;
	}


	public boolean hasMovement( ArrayList<String> lineList) {

		Double sourceLatitude = extractGeoField(3,lineList.get(0));
		Double sourceLongitude = extractGeoField(4,lineList.get(0));

		boolean movementFound = false;
		int index = 1;
		while(index<lineList.size()){
			String line = lineList.get(index);
			Double latitute = extractGeoField(3,line);
			Double longitude = extractGeoField(4,line);
			if((Math.abs(sourceLatitude - latitute) > this.precision) || 
					Math.abs(sourceLongitude - longitude) > this.precision)
			{//moving!
				movementFound=true;
				break;
			}
			index++;
		}	
		//System.out.println("movementFound: "+movementFound);
		return movementFound;
	}


	private Long extractTimeField(int i, String sourceLine) {

		String[] tokens = sourceLine.split(",");
		String field = this.trimDoubleQuotes(tokens[i]);
	  //  System.out.println(field);
		return new Long(field);
	}
	
	private Double extractGeoField(int i, String sourceLine) {

		String[] tokens = sourceLine.split(",");
		String field = this.trimDoubleQuotes(tokens[i]);
	   // System.out.println(field);
		return new Double(field);
	}

	public boolean checkAtHome(String line){

		Long timeStamp = extractTimeField(2,line);
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(timeStamp *1000);
		calendar.setTime(date);
		Integer hour = calendar.get(Calendar.HOUR); //Military 24h day.
		Integer AmPm = calendar.get(Calendar.AM_PM);

	//	System.out.println("hour:"+ hour+", AM/PM:"+AmPm);

		if(hour>this.businessHoursEnd || hour<this.businessHoursStart){
		//	System.out.println("home hour:"+ hour+", AM/PM:"+AmPm);
			return true;
		}
		else{
			//System.out.println("work hour:"+ hour+", AM/PM:"+AmPm);
			return false;
		}
	}

	private ArrayList<String> fillMobilityFields(ArrayList<String>lineList, String mobilityTag){

		ArrayList<String> taggedLineList = new ArrayList<String>();

		for(String line: lineList){
			line = line.concat(", "+mobilityTag);
			taggedLineList.add(line);
		}
		return taggedLineList;
	}
	
	private String trimDoubleQuotes(String original){
		if(this.trim){
			return original = original.substring(1, original.length()-1);
		}
		else
			return original;
	}

	public void setTrim(boolean b) {
		this.trim=b;
	}
	
}
