package mobility.merger;

import java.util.ArrayList;
import java.util.HashMap;

/** 
 * Joins two tables by the DeviceID indexes
 * 
 * @author adrianoc
 *
 */
public class Joiner {

	ArrayList<String> appLogList;
	private boolean trim = true;

	public ArrayList<String>  join(ArrayList<String> mobilityList, ArrayList<String> appLogList){

		this.appLogList = appLogList;

		ArrayList<String> joinedList = new ArrayList<String>();
		HashMap<String, String> processedKeys = new HashMap<String,String>();

		for(String mobilityLine : mobilityList){
			String[] mobilityTokens = mobilityLine.split(",");
			String mobilityKey = trimDoubleQuotes(mobilityTokens[0]) ;

			//Save key, so we don't try to process it once more.
			if(!processedKeys.containsKey(mobilityKey)){
				processedKeys.put(mobilityKey, mobilityKey);

				ArrayList<String> extractedAppLogList = extractAppLogList(mobilityKey);
				if(extractedAppLogList.size()>0){
					String latitude = trimDoubleQuotes(mobilityTokens[3]);
					String longitude = trimDoubleQuotes(mobilityTokens[4]);
					String mobility = trimDoubleQuotes(mobilityTokens[5]);
					extractedAppLogList = appendMobilityFields(extractedAppLogList, latitude,longitude,mobility);
					joinedList.addAll(extractedAppLogList);
				}
			}
		}
		return joinedList;
	}

	private String trimDoubleQuotes(String original){
		if(this.trim){
			return original = original.substring(1, original.length()-1);
		}
		else
			return original;
	}


	/** Lookup lines in the AppLogList, remove them from the AppLogList, and return them */
	private ArrayList<String> extractAppLogList(String mobilityKey){
		
		ArrayList<String> extractedList = new ArrayList<String>();
		ArrayList<Integer> linesToRemove = new ArrayList<Integer>();
		
		for(int i=0; i<this.appLogList.size();i++){
			String appLogLine = this.appLogList.get(i);
			String[] appTokens = appLogLine.split(",");
			String appKey = trimDoubleQuotes(appTokens[0]);
			if(mobilityKey.matches(appKey)){
				extractedList.add(appLogLine);
				linesToRemove.add(i);
			}
		}
		removeLines(linesToRemove);
		return extractedList;
	}
	
	private void removeLines(ArrayList<Integer> lineList){
		
		int shift=0;
		for(Integer index:lineList){
			this.appLogList.remove(index-shift);
			shift++;
		}
	}

	private ArrayList<String> appendMobilityFields(ArrayList<String> list, String latitude, String longitude,String mobility) {

		ArrayList<String> resultList = new ArrayList<String>();
		for(String line: list){
			line = line.concat(","+latitude);
			line = line.concat(","+longitude);
			line = line.concat(","+mobility);	
			resultList.add(line);
		}
		return resultList;
	}

	public void setTrim(boolean b) {
		this.trim=b;
	}


}
