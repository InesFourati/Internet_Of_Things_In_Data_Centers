package application.IoT_DS;


public class Topic {
	String topicName;
	int topicPriority; 
	int topicQoS;
	int topicFrequency; //in seconds

//get and set TopicName
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

//get and set Topic Priority
	public int getTopicPriority() {
		return topicPriority;
	}
	public void setTopicPriority(int topicPriority) {
		this.topicPriority = topicPriority;
	}

//get and set TopicQoS
	public int getTopicQoS() {
		return topicQoS;
	}
	public void setTopicQoS(int topicQoS) {
		this.topicQoS = topicQoS;
	}
	
///////////// Relevant only for prio1 messages ////////////////
	
//get and set interval	
	public int getTopicFrequency() {
		return topicFrequency; //in seconds
	}
	
	public void setTopicFrequency(int topicInterval) {
		this.topicFrequency = topicInterval; //in seconds
	}


}
