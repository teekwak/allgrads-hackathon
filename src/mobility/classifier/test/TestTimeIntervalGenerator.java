package mobility.classifier.test;

import static org.junit.Assert.*;
import mobility.merger.TimeIntervalGenerator;

import org.junit.Test;

public class TestTimeIntervalGenerator {

	@Test
	public void test() {
		TimeIntervalGenerator generator = new TimeIntervalGenerator();
		generator.generate("Tue, 1 Sep 2015 00:00:00 -0700", 30);
	
		for(Long interval: generator.timeIntervalList){
			System.out.println(interval.toString());
		}
		System.out.println("Intervals for September: "+ generator.timeIntervalList.size());
	}

}
