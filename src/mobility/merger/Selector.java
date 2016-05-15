package mobility.merger;

import java.util.ArrayList;
import java.util.HashMap;

import mobility.util.ClassifierPropertyManager;
import mobility.util.MergerPropertyManager;
import mobility.util.ReadWriteFileBuffer;

/**
 *  Selects the tuples which are within a certain time range (typically, 1h).
 * 
 * @author adrianoc
 *
 */
public class Selector {

	String writePath=".";
	ArrayList<String> mobiliyList;
	ArrayList<String> appLogList;
	
	public ArrayList<String> select(String filePath,int dateFieldPosition, Long lowerBoundDate, Long upperBoundDate){
		
		ArrayList<String> selectedLines =  new ArrayList<String>();
		
		//Readfile to String
		ArrayList<String> buffer = ReadWriteFileBuffer.readToBuffer(filePath);
		
		for(String line: buffer){
			String[] tokens = line.split(",");
			String lineTimeStamp = tokens[dateFieldPosition];
			Long lineTimeStampLong = new Long(lineTimeStamp.substring(1, lineTimeStamp.length()-1));
			if(lineTimeStampLong<upperBoundDate && lineTimeStampLong>lowerBoundDate){
				line = line.concat(","+lineTimeStampLong.toString());
				selectedLines.add(line);
			}
		}
		return selectedLines;
	}
}
