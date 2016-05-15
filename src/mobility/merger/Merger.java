package mobility.merger;

import java.util.ArrayList;

import mobility.util.MergerPropertyManager;
import mobility.util.ReadWriteFileBuffer;

/** 
 * Generates the monthly dataset files that result from joining the mobilityFiles and the appLogFiles.
 * 
 * Steps for each month:
 * 1- Generate time intervals for the month
 * 2- Select records in mobility and appLogfiles that are within the each time interval
 * 2.1 This involves merging two half-months of data for the mobility Files.
 * 3- Join each selection from mobility and app
 * 4- Append the join to the monthly file 
 * 
 * @author adrianoc
 *
 */
public class Merger {

	MergerPropertyManager manager;
	private ArrayList<String> monthMerged;
	
	public static void main(String args[]){
		Merger merger = new Merger();
		merger.runSeptember();
		merger.runOctober();
		merger.runNovember();
	}
	
	public void runSeptember(){
		this.loadParameters();
		String writePath = this.manager.WRITE_PATH;
		
		//September run
		String startMonthDate = this.manager.SEPTEMBER_START_DATE;
		int daysInMonth=30;
	
		String appFilePath = manager.READ_APP_LOG_PATH+"//"+this.manager.septemberFileList.get(0); //apps
		int appDatePosition = 1; 
		
		String mobilityFilePath = manager.READ_DEVICE_MOBILITY_PATH+"//"+this.manager.septemberFileList.get(1);//mobility
		int mobilityDatePosition = 2;
		
		this.monthMerged = new ArrayList<String>();
		run(startMonthDate,daysInMonth,mobilityFilePath,mobilityDatePosition,appFilePath, appDatePosition);
		
		String mergedFileName = "mergedSeptember.csv"; 
		ReadWriteFileBuffer.writeBackToBuffer(this.monthMerged, writePath, mergedFileName);
	}
	
	public void runOctober(){
		this.loadParameters();
		String writePath = this.manager.WRITE_PATH;
		
		//September run
		String startMonthDate = this.manager.OCTOBER_START_DATE;
		int daysInMonth=31;
	
		String appFilePath = manager.READ_APP_LOG_PATH+"//"+this.manager.octoberFileList.get(0); //apps
		int appDatePosition = 1; 
		
		String mobilityFilePath = manager.READ_DEVICE_MOBILITY_PATH+"//"+this.manager.octoberFileList.get(1);//mobility
		int mobilityDatePosition = 2;
		
		this.monthMerged = new ArrayList<String>();
		
		run(startMonthDate,daysInMonth,mobilityFilePath,mobilityDatePosition,appFilePath, appDatePosition);
		
		String mergedFileName = "mergedOctober.csv"; 
		ReadWriteFileBuffer.writeBackToBuffer(this.monthMerged, writePath, mergedFileName);
	}
	
	public void runNovember(){
		this.loadParameters();
		String writePath = this.manager.WRITE_PATH;
		
		//September run
		String startMonthDate = this.manager.NOVEMBER_START_DATE;
		int daysInMonth=30;
	
		String appFilePath = manager.READ_APP_LOG_PATH+"//"+this.manager.novemberFileList.get(0); //apps
		int appDatePosition = 1; 
		
		String mobilityFilePath = manager.READ_DEVICE_MOBILITY_PATH+"//"+this.manager.novemberFileList.get(1);//mobility
		int mobilityDatePosition = 2;
				
		this.monthMerged = new ArrayList<String>();
		
		run(startMonthDate,daysInMonth,mobilityFilePath,mobilityDatePosition,appFilePath, appDatePosition);
		
		String mergedFileName = "mergedNovember.csv"; 
		ReadWriteFileBuffer.writeBackToBuffer(this.monthMerged, writePath, mergedFileName);	
	}
	

	private void loadParameters(){
		this.manager = new MergerPropertyManager();
		manager.initialize();
	}
	
	public void run(String startMonthDate, int daysInMonth, String mobilityFilePath,
			int mobilityDatePosition, String appFilePath, int appDatePosition){
		TimeIntervalGenerator generator = new TimeIntervalGenerator();
		generator.generate(startMonthDate, daysInMonth);
		
		for(Long lowerBoundDate: generator.timeIntervalList){
			
			Selector selectorMobility = new Selector();
			selectorMobility.setTrim(true);
			ArrayList<String> mobilityList = selectorMobility.select(mobilityFilePath, mobilityDatePosition, lowerBoundDate, lowerBoundDate+3600);//MOBILITY
			
			Selector selectorApps = new Selector();
			selectorApps.setTrim(false);
			ArrayList<String> appLogList =selectorApps.select(appFilePath, appDatePosition, lowerBoundDate, lowerBoundDate+3600);//APP
					
			System.out.println("size mobilityList"+ mobilityList.size());
			System.out.println("size appLogList"+ appLogList.size());
			Joiner joiner = new Joiner();
			ArrayList<String> joinedIntervalList = joiner.join(mobilityList, appLogList);
			merge(joinedIntervalList);
		}
	}

	private void merge(ArrayList<String> joinedIntervalList) {
		System.out.println("size joined"+ joinedIntervalList.size());
		this.monthMerged.addAll(joinedIntervalList);
	}
	
}
