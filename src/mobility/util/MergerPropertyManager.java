package mobility.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class MergerPropertyManager {
	
	private String propertiesFileName="merger.properties";
	
	private String propertiesPath="C://Users//adrianoc//Documents//GitHub//allgrads-hackathon//src//";
	
	public String SEPTEMBER_FILES =""; //Default value
	public String OCTOBER_FILES =""; //Default value
	public String NOVEMBER_FILES =""; //Default value
	
	public String READ_DEVICE_MOBILITY_PATH ="";
	public String READ_APP_LOG_PATH ="";
	public String WRITE_PATH ="";
	
	public ArrayList<String> septemberFileList;
	public ArrayList<String> octoberberFileList;
	public ArrayList<String> novemberFileList;
	
	public String defaultStartDate="";
	
	public void initialize(){
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(propertiesPath+propertiesFileName));
			this.READ_DEVICE_MOBILITY_PATH = properties.getProperty("READ_DEVICE_MOBILITY_PATH");
			this.READ_APP_LOG_PATH = properties.getProperty("READ_APP_LOG_PATH");
			this.WRITE_PATH = properties.getProperty("WRITE_PATH");
			
			this.SEPTEMBER_FILES = properties.getProperty("SEPTEMBER_FILES");
			this.septemberFileList = extractFileNames(this.SEPTEMBER_FILES);
			
			this.OCTOBER_FILES = properties.getProperty("OCTOBER_FILES");
			this.octoberberFileList = extractFileNames(this.OCTOBER_FILES);
			
			this.NOVEMBER_FILES = properties.getProperty("NOVEMBER_FILES");
			this.novemberFileList = extractFileNames(this.NOVEMBER_FILES);
			
			this.defaultStartDate = properties.getProperty("DEFAULT_START_DATE");		
		} 
		catch (IOException e) {
			System.out.println("Could not load properties. Please be sure that the property file is located at: "+propertiesPath);
			System.out.println("Using defaut values!!!!! ");
		}
	}
	
	
	private ArrayList<String> extractFileNames(String fileNames){
		ArrayList<String> list =  new ArrayList<String>();
		
		String[] fileNameTokens= fileNames.split(",");
		for(String name : fileNameTokens){
			list.add(name.trim());
		}
		return list;
	}
	


}

