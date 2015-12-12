package Messages;

import jade.lang.acl.ACLMessage;

public class ConfirmMessage extends AgentMessage {	
	public ConfirmMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.CONFIRM);
	}
}
