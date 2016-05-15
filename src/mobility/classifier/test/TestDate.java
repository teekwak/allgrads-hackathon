package mobility.classifier.test;

import static org.junit.Assert.*;
import junit.framework.Assert;
import mobility.classifier.Classifier;

import org.junit.Test;

public class TestDate {

	@Test
	public void testAtWork() {
		
		String line = "40985,	1,1441123206,	40.9983973,	-73.6859592"; //16h, not at home				
		Classifier classifier = new Classifier();
		classifier.setTrim(false);
		assertFalse(classifier.checkAtHome(line));
	}
	
	@Test
	public void testAtHome() {
		
		String line = "40985,	1,1441047941,	40.9983973,	-73.6859592"; //4h, not at home
		Classifier classifier = new Classifier();
		classifier.setTrim(false);
		assertTrue(classifier.checkAtHome(line));
	}
}
