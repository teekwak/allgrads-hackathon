package mobility.classifier.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import mobility.classifier.Classifier;

import org.junit.Test;

public class TestMovement {

	public ArrayList<String> setupMovementPoints(){
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("84756,4,1441103847,40.9049362,-73.8577978");
		list.add("84756,4,1441103847,40.9049362,-73.8577978");
		list.add("84756,4,1441103847,40.9049362,-73.8577978");
		list.add("84756,4,1441103847,40.9049362,-73.8577978");
		list.add("84756,4,1441103847,40.9049362,-73.8577978");
		list.add("84756,4,1441103850,40.9136577,-73.8230737");
		list.add("84756,4,1441103850,40.9136577,-73.8230737");
		list.add("84756,4,1441103850,40.9136577,-73.8230737");
		list.add("84756,4,1441103850,40.9136577,-73.8230737");
		list.add("84756,4,1441103850,40.9136577,-73.8230737");
		list.add("84756,4,1441103850,40.9136577,-73.8230737");
		return list;
	}
	

	public ArrayList<String> setupRestingPoints(){
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("40985,	1,	1441091141,	40.9983973,	-73.6859592");
		list.add("40985,	1,	1441091141,	40.9983973,	-73.6859592");
		list.add("40985,	1,	1441091141,	40.9983973,	-73.6859592");
		list.add("40985,	1,	1441091141,	40.9983973,	-73.6859592");
		list.add("40985,	1,	1441091141,	40.9983973,	-73.6859592");
		return list;
	}

	
	
	@Test
	public void testMovement() {
		
		ArrayList<String> list = this.setupMovementPoints();
		Classifier classifier = new Classifier();
		assertTrue(classifier.hasMovement(list));
	}
	
	@Test
	public void testNoMovement() {
		ArrayList<String> list = this.setupRestingPoints();
		Classifier classifier = new Classifier();
		assertFalse(classifier.hasMovement(list));
		
	}
	

}
