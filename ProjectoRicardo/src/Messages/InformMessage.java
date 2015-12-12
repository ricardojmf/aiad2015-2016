package Messages;

import jade.lang.acl.ACLMessage;

public class InformMessage extends AgentMessage{
	
	public InformMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.INFORM);
	}
}
