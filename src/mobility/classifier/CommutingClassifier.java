package mobility.classifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommutingClassifier {

	private Double precision = 0.0; // the maximum distance to consider at same place. This value is setup in the properties file.
	private Double businessHoursStart = 8.0;
	private Double businessHoursEnd = 17.0;
	
	public static void main(String args[]){
		
		CommutingClassifier classifier = new CommutingClassifier();
		classifier.run();
		
	}
	
	private void loadParameters(){
		PropertyManager manager = new PropertyManager();
		this.precision = manager.locationPrecision;
		this.businessHoursEnd = manager.businessHoursEnd;
		this.businessHoursStart = manager.businessHoursStart;
	}
	
	public void run(){
		PropertyManager manager = new PropertyManager();
		ArrayList<String> fileList = manager.deviceFileList;
	
		
		for(String file:fileList){
			ArrayList<String> buffer = ReadWriteFileBuffer.readToBuffer(manager.FILES_PATH+"//"+file);
			buffer = categorizeCommuting(buffer);
			ReadWriteFileBuffer.writeBackToBuffer(buffer, manager.FILES_PATH+ "//", "new_"+file);
		}
		
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
			String mobilityFlag="";
			if(checkMovement(list)){
				mobilityFlag = "Commuting";
			}
			else{
				if(checkAtHome(list)){
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
	
	
	private boolean checkMovement( ArrayList<String> lineList) {
		
		Double sourceLatitude = extractField(3,lineList.get(0));
		Double sourceLongitude = extractField(4,lineList.get(0));
				
		boolean noMovementFound = true;
		int index = 1;
		while(noMovementFound || index<lineList.size()){
			String line = lineList.get(index);
			Double latitute = extractField(3,line);
			Double longitude = extractField(4,line);
			if((sourceLatitude - latitute > this.precision) || 
					(sourceLongitude - longitude > this.precision))
			{//moving!
				noMovementFound=false;
			}
			index++;
		}	
		return noMovementFound;
	}

	
	private Double extractField(int i, String sourceLine) {
		
		String[] tokens = sourceLine.split(",");
		return new Double(tokens[i]);
	}
	
	private boolean checkAtHome(ArrayList<String> lineList){
		
		Integer timeStamp = extractField(2,lineList.get(0)).intValue();
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(timeStamp *1000);
		calendar.setTime(date);
		Integer hour = calendar.get(Calendar.HOUR);
		Integer AmPm = calendar.get(Calendar.AM_PM);
		
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
