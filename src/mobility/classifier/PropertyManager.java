package mobility.classifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class PropertyManager {

	private String propertiesFileName="classifier.properties";
	
	private String propertiesPath="C://Users//adrianoc//Documents//GitHub//allgrads-hackathon//src//";
	
	public String ORIGINAL_DEVICE_FILES =""; //Default value
	
	public String FILES_PATH ="";
	
	public ArrayList<String> deviceFileList;
	
	public Double locationPrecision = 0.0; //Default value

	public Double businessHoursEnd = 17.0; //Default value
	
	public Double businessHoursStart = 8.0; //Default value

	
	public void initialize(){
		
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(propertiesPath+propertiesFileName));
			this.FILES_PATH = properties.getProperty("FILES_PATH");
			this.ORIGINAL_DEVICE_FILES = properties.getProperty("ORIGINAL_DEVICE_FILES");
			this.deviceFileList = extractFileNames(this.ORIGINAL_DEVICE_FILES);
			this.locationPrecision = new Double(properties.getProperty("LOCATION_PRECISION"));
			this.businessHoursEnd =  new Double(properties.getProperty("BUSINESS_HOUR_END"));
			this.businessHoursStart =  new Double(properties.getProperty("BUSINESS_HOUR_START"));

		} 
		catch (IOException e) {
			System.out.println("Could not load properties. Please be sure that the property file is located at: "+propertiesPath);
			System.out.println("Using defaut value: "+ORIGINAL_DEVICE_FILES);
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

