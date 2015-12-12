package Messages;

import jade.lang.acl.ACLMessage;

public class RefuseMessage extends AgentMessage{	
	public RefuseMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.REFUSE);
	}
}
