package Messages;

import jade.lang.acl.ACLMessage;

public class ProposeMessage extends AgentMessage{	
	public ProposeMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.PROPOSE);
	}
}
