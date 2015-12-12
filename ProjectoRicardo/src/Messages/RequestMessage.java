package Messages;

import jade.lang.acl.ACLMessage;

public class RequestMessage extends AgentMessage{	
	public RequestMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.REQUEST);
	}
}
