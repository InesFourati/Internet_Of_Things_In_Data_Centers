package application.IoT_DS;

import java.util.List;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {
/////////////////// GLOBAL VARIABLES ////////////////////

	//Read the CSV file containing all the Topics. topicsList is a GLOBAL variable
	static List <Topic> topicsList = CSVFileReader.getTopicsFromFile(); 
	static MqttClient myCloud;
	public static void main(String[] args) throws MqttException, InterruptedException {
		
/////////////////// DATABASE ///////////////////////
		
			//Connect to the Database
			Database.connectDatabase();


////////////////// MQTT SETTINGS ///////////////////

			//TODO: delete this !
			final List <String[]> serversURLandID = CSVFileReader.getServerURLfromFile();
			
			MqttClient myClient;

			// MQTT Connection Options
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			MqttConnectOptions options2 = new MqttConnectOptions();
			options2.setCleanSession(true);
			//options.setAutomaticReconnect(true); 
			
			//Memory Persistence of the Connection
			MemoryPersistence persistenceClient = new MemoryPersistence();
			MemoryPersistence persistenceCloud = new MemoryPersistence();		
			
		try {
			System.out.println("Connecting to the broker ...");
			//Localhost Cloud node broker
			myCloud= new MqttClient("tcp://192.168.56.1:1883","cloud-pc",persistenceCloud);
			myCloud.connect(options2);
			
			//Localhost Edge node broker
			myClient = new MqttClient("tcp://192.168.1.58:1883", "virtual-server",persistenceClient);
			
			Callback myCallback = new Callback();
			myClient.setCallback(myCallback);
			System.out.println("Callback set");

		// @override callbacks here in main 
		myClient.connect(options);
		
		

			//Subscribe to all the Topics
			for (Topic t : topicsList) {
				for (String[] serverID : serversURLandID) {
					String topic = "telegraf/"+serverID[1]+"/"+t.getTopicName();
					System.out.println("subscribing to "+topic+" ...");
					myClient.subscribe(topic,t.getTopicQoS());
					System.out.println("subscribed to "+topic);

				}
			}

		} catch (Exception me) {
			System.out.println("msg: " + me.getMessage());
			System.out.println("loc: " + me.getLocalizedMessage());
			System.out.println("cause: " + me.getCause());
			System.out.println("excep: " + me);
			me.printStackTrace();
			System.exit(0);
			
		}
		System.out.println("connected to the broker !");
	}


	public static void sendMessageToCloud(String topic, MqttMessage message) {
		try {

			myCloud.publish("telegraf/cloud",message);
			//prints  message on console
			System.out.println(topic+","+ message.toString());
		} catch (MqttException e) {
			System.out.println("reason: " + e.getReasonCode());
			System.out.println("message: " + e.getMessage());
			System.out.println("localization: " + e.getLocalizedMessage());
			System.out.println("cause: " + e.getCause());
			System.out.println("exception: " + e);
			e.printStackTrace();
		}
		}
	

}