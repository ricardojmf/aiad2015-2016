package Messages;

public class AgentMessage{

	private int messageType;
	private String messageSender;
	private String messageInfo;
	
	public AgentMessage(String messageSender, String messageInfo) {
		this.messageSender = messageSender;
		this.messageInfo = messageInfo;		
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(String messageSender) {
		this.messageSender = messageSender;
	}

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	
	public String toString(){
		return messageType + "-" + messageSender + "-" + messageInfo;
	}
}