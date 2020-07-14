package application.IoT_DS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessagingScheduler {
	/* I will use a thread pool instead of a thread queue 
	 * A thread pool reuses previously created threads to execute current tasks
	 * and offers a solution to the problem of thread cycle overhead and resource thrashing. 
	 * */
	public static void scheduleTopic (Topic topic, final String tableName) {
		
		final String topicName=topic.getTopicName();
		
		///////////// SQLite Queries ////////////////////
		final String querySQL = "SELECT host,AVG(value) AS avg ,MIN(value) AS min,MAX(value) AS max\n"
								+"FROM "+tableName+"\n"
								+"GROUP BY host";
		
		final String deleteSQL ="DELETE FROM "+tableName+";";

		
		//////////////// Output messages Scheduling ///////////////////////////////
		//TODO: ASK here about the number of threads. Here default value = 1
		 ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		 
		 //tasks to execute periodically
		 Runnable sendMQTTMsg = new Runnable() {
			 
		       public void run() { 
		    	   String payloadToCloud=Database.queryPayload(querySQL, tableName);
		    	   if(payloadToCloud!=null) {
		    		   
		    		   //delete the values used for the resultset
		    		   Database.updateTable(deleteSQL);
		    		   
		    		   //send aggregated message to the Cloud
		    		   MqttMessage m =new MqttMessage(payloadToCloud.getBytes());
		    		   App.sendMessageToCloud(topicName,m);
		    	   }else {System.out.println("empty MSG");}
		       }};
		     
		     //Frequency of the topic
		     scheduler.scheduleAtFixedRate(sendMQTTMsg, topic.getTopicFrequency(), topic.getTopicFrequency(),TimeUnit.SECONDS);
		}
	
}
