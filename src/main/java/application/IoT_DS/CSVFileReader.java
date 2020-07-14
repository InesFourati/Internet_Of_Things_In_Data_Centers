package application.IoT_DS;

import com.opencsv.CSVReader;

import java.io.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//////////////////// !!!!!!!!! //////////////////////////

//TODO: Set ClassPath to Resource, for testing please change the path ! (for now)

////////////////////!!!!!!!!! //////////////////////////
public class CSVFileReader {
	
	public static List<Topic> getTopicsFromFile (){
		
		List<Topic> topics = new ArrayList<Topic>();

		String topicsFile = "/Users/iness/eclipse-workspace/IoT_DS/lib/topics.csv"; //path to CSV Data

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(topicsFile));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
            	Topic tempTopic = new Topic();
            	
            	tempTopic.setTopicName(line[0]);
            	tempTopic.setTopicPriority(Integer.parseInt(line[1]));
            	tempTopic.setTopicQoS(Integer.parseInt(line[2]));
            	tempTopic.setTopicFrequency(Integer.parseInt(line[3]));
            	
                topics.add(tempTopic);   
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return topics;
	}
	
	public static List<String[]> getServerURLfromFile() {
		
		List<String[]> serversList = new ArrayList<String[]>();
		String serversFile = "/Users/iness/eclipse-workspace/IoT_DS/lib/serversURL.csv"; //path to CSV Data
		
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(serversFile));
			String[] line;
			
			while ((line = reader.readNext()) != null) {
				String[] urlAndID = new String[2];
				urlAndID[0]=line[0];
				urlAndID[1]=line[1];
				serversList.add(urlAndID);
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	return serversList;
	
}
	
}
