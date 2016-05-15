package mobility.merger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import mobility.util.MergerPropertyManager;

/**
 * Generates a list of time intervals of 1h duration each starting on September 1st, 2015, at midnight
 * 
 * @author adrianoc
 *
 */
public class TimeIntervalGenerator {

	/** Lowerbound time for each interval */
	public ArrayList<Long> timeIntervalList = new ArrayList<Long>(); 

	/** Typically 1h in seconds */
	public long interval = 3600; 

	/** Typically  September 1st, 2015, at midnight */
	public Date startDate;

	private Long startDateSeconds;
	
	public static void main(String args[]){
		TimeIntervalGenerator generator = new TimeIntervalGenerator();
		generator.generate("Tue, 1 Sep 2015 00:00:00 -0800", 30);
	
		//for(Integer interval: generator.timeIntervalList){
			//System.out.println(interval.toString());
	//	}
		System.out.println("Intervals for September: "+ generator.timeIntervalList.size());
	}

	
	
	public void generate(String startDateStr, int days){
		this.loadParameters();
		initializeDate(startDateStr);
		int intervals = days *24;
		Long lowerBoundIntervalDate=this.startDateSeconds;
		
		for(int i=0;i<intervals;i++){
			lowerBoundIntervalDate = new Long(lowerBoundIntervalDate.intValue() +this.interval);
			this.timeIntervalList.add(lowerBoundIntervalDate);
		}
		
	}

	private void loadParameters(){
		MergerPropertyManager manager = new MergerPropertyManager();
		manager.initialize();
	}
	
	public void initializeDate(String startDateStr){
		// Tue, 1 Sep 2015 00:00:00 -0800
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

		try {
			this.startDate = df.parse(startDateStr);
			System.out.println(this.startDate.getTime());
			this.startDateSeconds = this.startDate.getTime() / 1000;
			System.out.println(this.startDateSeconds);
		} catch (ParseException e) {
			System.out.println("Date: "+startDateStr);
			e.printStackTrace();
		}
	}


}
