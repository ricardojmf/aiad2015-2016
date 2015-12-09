package messages;

import messages.AgentMessage.messageType;

public class ReplyMessage extends AgentMessage{
	
	public String messageInfo;
	
	public ReplyMessage(String agentName, String messageInfo) {
		super(agentName);
		this.messageInfo = messageInfo;
	}
	
	public String toString(){
		return messageType.REPLY_MESSAGE + "-" + agentName + "-" + messageInfo;
	}
}
