package application.IoT_DS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Callback implements MqttCallback{
static 	List <String> alreadyScheduled = new ArrayList <String> ();
	
	@Override
	public void connectionLost(Throwable cause) {
		System.out.println(cause.getMessage());
		//TODO: Close Database when connection lost
		System.out.println("3AN BOUK LKALB!");
	}

	@Override
	public void messageArrived(String topicReceived, MqttMessage msgReceived) throws Exception {
		
		String[] topicReceivedModified = topicReceived.split("/");
		//Sorting incoming messages after the topic priority
		for (Topic topic : App.topicsList) {
			if (topic.getTopicName().equals(topicReceivedModified[2]) && topic.getTopicPriority()==0) {
				//prio0 messages
				App.sendMessageToCloud(topicReceived, msgReceived);
				break; //to have a good time-complexity
				
			} 
			if (topic.getTopicName().equals(topicReceivedModified[2]) && topic.getTopicPriority()==1) {
				String mp = new String (msgReceived.getPayload());
				String [] mpSplit = mp.split(",");
				
				//Incoming MQTT message in converted byte[] to an ArrayList for an easier processing
				List <String> payloadList = new ArrayList <> (Arrays.asList(mpSplit));
				
				//Correcting Telegraf's payload bugs
				//PAyload had space " "
				for (int i = 0; i < payloadList.size(); i++) {
					if (payloadList.get(i).contains(" ")) {
						String[] correction = payloadList.get(i).split(" ");
						payloadList.remove(i);
						payloadList.add(i, correction[0]);
						payloadList.add(i+1, correction[1]);
					}
				}
				payloadList.remove(0);
				payloadList.remove(payloadList.size()-1);
				
				//separation of the keys and values in payloadList and storing them separately in Lists
				List <String> tableColumns = new ArrayList <String> ();
				List <String> tableValues = new ArrayList <String> ();
				
				//List <String> columnType = new ArrayList <String> ();
				String hostName = "server1";
				for (int i = 1; i < payloadList.size(); i++) {
					String[] keyAndValueSplit = payloadList.get(i).split("=");
					if (keyAndValueSplit[1].indexOf("i") == keyAndValueSplit[1].length()-1) {
						keyAndValueSplit[1]=keyAndValueSplit[1].substring(0, keyAndValueSplit[1].length()-1);
					}
					if (keyAndValueSplit[0].equals("host")) {
						hostName = keyAndValueSplit[1];
					}
					try {
						Double.parseDouble(keyAndValueSplit[1]);
						tableColumns.add(keyAndValueSplit[0]);
						tableValues.add(keyAndValueSplit[1]);		
					} catch (Exception e) {
						//do nothing
					}
				}
				
				//FORMAT: INSERT INTO tableName (tableColumn1, ...) VALUES ('value1', ...);
				for (int i = 0; i < tableColumns.size(); i++) {
					
					//SQL does not support "-" in Table name: We change it to "_"
					if (tableColumns.get(i).contains("-")) {
						String s = tableColumns.get(i).replace("-", "_");
						tableColumns.remove(i);
						tableColumns.add(i,s);
					}
					
					String sqlCreate = "CREATE TABLE IF NOT EXISTS "+topicReceivedModified[2]+"_"+tableColumns.get(i)+" (\n"
			                + "host text,\n"
			                + "value real\n"
			                + ");";
					Database.updateTable(sqlCreate);
					

					String sqlQuery = "INSERT INTO "+topicReceivedModified[2]+"_"+tableColumns.get(i)+" (host,value) VALUES (\n"
							+"'"+hostName+"',"+"'"+tableValues.get(i)+"');";
					
					//Update the corresponding Table with the payload values
					Database.updateTable(sqlQuery);

					}
				//Set the topic timer
				for (int i = 0; i < tableColumns.size(); i++) {
				if (!alreadyScheduled.contains(tableColumns.get(i))) {
					alreadyScheduled.add(tableColumns.get(i));
					
					MessagingScheduler.scheduleTopic(topic,topicReceivedModified[2]+"_"+tableColumns.get(i));
				}}
				break; //to have a good time-complexity
				}
				
			}
		}	

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		
	}

}
