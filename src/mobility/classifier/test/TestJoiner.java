package mobility.classifier.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import mobility.merger.Joiner;

import org.junit.Test;

public class TestJoiner {

	public ArrayList<String> mobilityList;
	public ArrayList<String> appLogList;
	
	public long lowerBoundDate = 1441897200;
	public long upperBoundDate = lowerBoundDate + 3600;

	
	public void setupAppLogList(){
		appLogList=new ArrayList<String>();
		appLogList.add("84756,1441897203,com.twitter.android,twitter,social,1441897200");
		
		appLogList.add("84755,1441897483,com.pandora.android,pandora,audio,1441897200");
		appLogList.add("84755,1441897483,com.spotify,spotify,audio,1441897200");
		appLogList.add("84755,1441897283,com.google.android.youtube,youtube,video,1441897200");
		appLogList.add("39493,1441897323,com.facebook.katana,facebook,social,1441897200");
		appLogList.add("39493,1441897543,com.ebay.mobile,ebay,ecommerce,1441897200");
		appLogList.add("39493,1441897503,com.android.chrome,chrome,browser,1441897200");
	}
	
	public void setupMobilityList(){
		mobilityList=new ArrayList<String>();
	    mobilityList.add("84756,226,1441897203,40.9141337,-73.7904915,Commuting,1441897200");
	
		mobilityList.add("84755,417,1441897483,40.9097037,-73.7935624,Home,1441897200");
		mobilityList.add("84755,417,1441897483,40.9097307,-73.793482,Home,1441897200");
		mobilityList.add("84755,569,1441897283,40.9096467,-73.7935583,Home,1441897200");
		mobilityList.add("39493,219,1441897323,40.9799689,-73.8133869,Work,1441897200");
		mobilityList.add("39493,374,1441897543,40.9769685,-73.815129,Commuting,1441897200");
		mobilityList.add("39493,374,1441897503,40.9769685,-73.815129,Commuting,1441897200");
	}
	
	
	@Test
	public void testJoiner() {
		this.setupAppLogList();
		this.setupMobilityList();
		Joiner joiner = new Joiner();
		joiner.setTrim(false);
		ArrayList<String> joinedList = joiner.join(mobilityList, appLogList);
		for(String line : joinedList){
			System.out.println(line);
		}
		
	}

}
