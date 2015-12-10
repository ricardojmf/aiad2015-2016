package messages;

import messages.AgentMessage.messageType;

public class ReplyMessage extends AgentMessage{
	
	public String messageInfo;
	
	public ReplyMessage(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	
	public String toString(){
		return messageType.REPLY_MESSAGE + "-" + messageInfo;
	}
}
