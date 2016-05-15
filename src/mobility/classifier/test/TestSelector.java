package mobility.classifier.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import mobility.merger.Selector;
import mobility.util.MergerPropertyManager;

import org.junit.Test;

public class TestSelector {

	@Test
	public void testAppLogSelector() {
		Selector selector = new Selector();
		MergerPropertyManager manager = new MergerPropertyManager();
		manager.initialize();
		
		String filePath = manager.READ_APP_LOG_PATH+"//"+manager.septemberFileList.get(2);
		int dateFieldPosition = 2;
		long lowerBoundDate = 1441094400;
		long upperBoundDate = 1441094400 + 3600;
		System.out.println("before");
		ArrayList<String> selectedList = selector.select(filePath, dateFieldPosition, lowerBoundDate, upperBoundDate);
		
		System.out.println("Size: "+selectedList.size());
	}

}
