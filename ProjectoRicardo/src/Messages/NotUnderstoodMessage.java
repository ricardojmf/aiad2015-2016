package Messages;

import jade.lang.acl.ACLMessage;

public class NotUnderstoodMessage extends AgentMessage{
	public NotUnderstoodMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.NOT_UNDERSTOOD);
	}
}
