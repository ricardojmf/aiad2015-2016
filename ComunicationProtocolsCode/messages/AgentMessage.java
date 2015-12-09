package messages;

public class AgentMessage {
	
	public enum messageType{
		REQUEST_JOB, REPLY_MESSAGE
	}
	
	String agentName;
	String messageInfo;
	
	public AgentMessage(String agentName) {
		this.agentName = agentName;
	}
	
	public String getAgentName() {
		return agentName;
	}
}