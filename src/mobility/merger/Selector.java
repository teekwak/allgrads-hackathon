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
	boolean trim=true;
	
	public ArrayList<String> select(String filePath,int dateFieldPosition, Long lowerBoundDate, Long upperBoundDate){
		
		ArrayList<String> selectedLines =  new ArrayList<String>();
		
		//Readfile to String
		ArrayList<String> buffer = ReadWriteFileBuffer.readToBuffer(filePath);
		
		for(String line: buffer){
			String[] tokens = line.split(",");
			String lineTimeStamp = tokens[dateFieldPosition];
			String field = this.trimDoubleQuotes(lineTimeStamp);
			Long lineTimeStampLong = new Long(field);
			if(lineTimeStampLong<upperBoundDate && lineTimeStampLong>lowerBoundDate){
				line = line.concat(","+lineTimeStampLong.toString());
				selectedLines.add(line);
			}
		}
		return selectedLines;
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
