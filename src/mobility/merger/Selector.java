package mobility.merger;

import java.util.ArrayList;
import java.util.HashMap;

import mobility.util.ClassifierPropertyManager;
import mobility.util.ReadWriteFileBuffer;

/**
 *  Selects the tuples which are within a certain time range (typically, 1h).
 * 
 * @author adrianoc
 *
 */
public class Selector {

	String writePath=".";
	ArrayList<String> mobiliyList;
	ArrayList<String> appLogList;
	
	public static void main (String args[]){
		Selector selector = new Selector();
		selector.run();
	}

	public void run(){
		this.loadParameters();
		for(String file:this.fileList){
			ArrayList<String> buffer = ReadWriteFileBuffer.readToBuffer(this.readPath+"//"+file);
			HashMap<String, ArrayList<String>> map = buildMap(buffer);
			buffer = categorize(map);
			ReadWriteFileBuffer.writeBackToBuffer(buffer,this.writePath+ "//", "new_00025_"+file);
		}
	}

	
	private void loadParameters(){
		ClassifierPropertyManager manager = new ClassifierPropertyManager();
		manager.initialize();
		this. = manager.READ_PATH;
		this.fileList = manager.deviceFileList;
		this.writePath = manager.WRITE_PATH;
	}

	
}
