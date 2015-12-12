package Messages;

import jade.lang.acl.ACLMessage;

public class AcceptProposalMessage extends AgentMessage {
	
	public AcceptProposalMessage(String messageSender, String messageInfo) {
		super(messageSender, messageInfo);
		setMessageType(ACLMessage.ACCEPT_PROPOSAL);
	}
}
