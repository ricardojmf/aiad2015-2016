package Agentes;

import sajas.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.ArrayList;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Serializable;

public class SocialBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private AgenteTrabalhador worker;
	//private String workerName;

	private ArrayList<ACLMessage> pendingProposeMessages;
	private ArrayList<ACLMessage> pendingReplyMessages;

	private ArrayList<ACLMessage> pendingAcceptProposalMessages;
	private ArrayList<ACLMessage> pendingConfirmMessages;
	private ArrayList<ACLMessage> pendingInformMessages;
	private ArrayList<ACLMessage> pendingNotUnderstoodMessages;
	private ArrayList<ACLMessage> pendingRefuseMessages;


	public SocialBehaviour(AgenteTrabalhador worker) {
		this.worker = worker;
		//this.workerName = worker.getLocalName();

		this.pendingProposeMessages = new ArrayList<ACLMessage>();
		this.pendingReplyMessages = new ArrayList<ACLMessage>();

		this.pendingAcceptProposalMessages = new ArrayList<ACLMessage>();
		this.pendingConfirmMessages = new ArrayList<ACLMessage>();
		this.pendingInformMessages = new ArrayList<ACLMessage>();
		this.pendingNotUnderstoodMessages = new ArrayList<ACLMessage>();
		this.pendingRefuseMessages = new ArrayList<ACLMessage>();
	}

	// ======================================  Listening for all Messages  =======================================

	@Override
	public void action() {
		//worker.debug("Listening... ");

		ACLMessage receivedMsg = myAgent.receive();

		if(receivedMsg != null)
		{
			addPendingMsg(receivedMsg);
			//worker.debug("Recebeu uma mensagem de [" + aclMessage.getSender().getLocalName() + "] dizendo: " + receivedMessage.getContent() + " id: " + aclMessage.getConversationId());
		}
		else {
			block();
		}
	}

	// ======================================  Sending Messages  =======================================

	public void send(int performative, ArrayList<AID> receivers, String conversationID, String msgContent)
	{
		ACLMessage msgToSend = new ACLMessage(performative);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent(msgContent);

		for(AID receiver: receivers)
			msgToSend.addReceiver(receiver);

		myAgent.send(msgToSend);
	}

	public void send(int performative, AID receiver, String conversationID, String msgContent)
	{
		ACLMessage msgToSend = new ACLMessage(performative);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent(msgContent);
		msgToSend.addReceiver(receiver);
		myAgent.send(msgToSend);
	}

	public void sendObject(int performative, AID receiver, String conversationID, String msgContent, Serializable object)
	{
		ACLMessage msgToSend = new ACLMessage(performative);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("bla");
		msgToSend.addReceiver(receiver);

		try {
			msgToSend.setContentObject(object);
			myAgent.send(msgToSend);
		} catch (IOException e) {
			worker.debug("ERRO AO SERIALIZAR O OBJETO A ENTREGAR A [" + receiver.getLocalName() + "]");
		}		
	}

	public void sendObject(int performative, ArrayList<AID> receivers, String conversationID, String msgContent, Serializable object)
	{
		ACLMessage msgToSend = new ACLMessage(performative);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("bla");

		for(AID receiver: receivers)
			msgToSend.addReceiver(receiver);

		try {
			msgToSend.setContentObject(object);
			myAgent.send(msgToSend);
		} catch (IOException e) {
			worker.debug("ERRO AO SERIALIZAR O OBJETO A ENTREGAR A [varios agentes]");
		}		
	}

	public void sendObjects(int performative, AID receiver, String conversationID, String msgContent, ArrayList<Serializable> object)
	{
		ACLMessage msgToSend = new ACLMessage(performative);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent(msgContent);
		msgToSend.addReceiver(receiver);

		try {
			msgToSend.setContentObject(object);
			myAgent.send(msgToSend);
		} catch (IOException e) {
			worker.debug("ERRO AO SERIALIZAR O OBJETO A ENTREGAR A [" + receiver.getLocalName() + "]");
		}		
	}

	public void replyOK(AID agent, String replyMsg) {
		ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
		msg.addReceiver(agent);
		msg.setContent(replyMsg);
		myAgent.send(msg);
	}

	public void replyNO(AID agent, String replyMsg) {
		ACLMessage msg = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
		msg.addReceiver(agent);
		msg.setContent(replyMsg);
		myAgent.send(msg);
	}

	public void replyInform(AID agent, String replyMsg) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(agent);
		msg.setContent(replyMsg);
		myAgent.send(msg);
	}

	// ======================================  Pending Messages  =======================================

	public void addPendingMsg(ACLMessage msg) {
		int perform = msg.getPerformative();
		if(perform == ACLMessage.PROPOSE)
			pendingProposeMessages.add(msg);
		else if(perform == ACLMessage.ACCEPT_PROPOSAL || perform == ACLMessage.REJECT_PROPOSAL || perform == ACLMessage.CONFIRM
				|| perform == ACLMessage.CANCEL)
			pendingReplyMessages.add(msg);
	}	

	public void addPendingMsgFilter(ACLMessage msg){
		int perform = msg.getPerformative();
		switch (perform) {
		case ACLMessage.ACCEPT_PROPOSAL:
			pendingAcceptProposalMessages.add(msg);
			break;
		case ACLMessage.CONFIRM:
			pendingConfirmMessages.add(msg);
			break;
		case ACLMessage.PROPOSE:
			pendingProposeMessages.add(msg);
			break;
		case ACLMessage.NOT_UNDERSTOOD:
			pendingNotUnderstoodMessages.add(msg);
			break;
		case ACLMessage.REFUSE:
			pendingRefuseMessages.add(msg);
			break;
		case ACLMessage.INFORM:
			pendingInformMessages.add(msg);
			break;
		default:
			break;
		}
	}	

	public boolean haveReplyPendingMsgs() {
		return !pendingReplyMessages.isEmpty();
	}

	public ArrayList<ACLMessage> getReplyPendingMsgs() {
		return pendingReplyMessages;
	}

	public boolean haveAcceptProposalPendingMsgs() {
		return !pendingAcceptProposalMessages.isEmpty();
	}

	public boolean haveProposePendingMsgs() {
		return !pendingProposeMessages.isEmpty();
	}

	public boolean haveRefusePendingMsgs() {
		return !pendingRefuseMessages.isEmpty();
	}

	public boolean haveInformPendingMsgs() {
		return !pendingInformMessages.isEmpty();
	}

	public boolean haveNotUnderstoodPendingMsgs() {
		return !pendingNotUnderstoodMessages.isEmpty();
	}

	public boolean haveConfirmPendingMsgs() {
		return !pendingConfirmMessages.isEmpty();
	}

	public ArrayList<ACLMessage> getAcceptProposalPendingMsgs() {
		return pendingAcceptProposalMessages;
	}

	public ArrayList<ACLMessage> getProposePendingMsgs() {
		return pendingProposeMessages;
	}

	public ArrayList<ACLMessage> getRefusePendingMsgs() {
		return pendingRefuseMessages;
	}

	public ArrayList<ACLMessage> getInformPendingMsgs() {
		return pendingInformMessages;
	}

	public ArrayList<ACLMessage> getNotUnderstoodPendingMsgs() {
		return pendingNotUnderstoodMessages;
	}

	public ArrayList<ACLMessage> getConfirmPendingMsgs() {
		return pendingConfirmMessages;
	}

	public ACLMessage get1stAcceptProposalPendingMsg() {
		if(!pendingAcceptProposalMessages.isEmpty()) {
			ACLMessage msg = pendingAcceptProposalMessages.get(0);
			remove1stAcceptProposalPendingMsg();
			return msg;
		}
		return null;
	}

	public ACLMessage get1stProposePendingMsg() {
		if(!pendingProposeMessages.isEmpty()) {
			ACLMessage msg = pendingProposeMessages.get(0);
			remove1stProposePendingMsg();
			return msg;
		}
		return null;
	}

	public ACLMessage get1stRefusePendingMsg() {
		if(!pendingRefuseMessages.isEmpty()) {
			ACLMessage msg = pendingRefuseMessages.get(0);
			remove1stRefusePendingMsg();
			return msg;
		}
		return null;
	}

	public ACLMessage get1stInformPendingMsg() {
		if(!pendingInformMessages.isEmpty()) {
			ACLMessage msg = pendingInformMessages.get(0);
			remove1stInformPendingMsg();
			return msg;
		}
		return null;
	}

	public ACLMessage get1stNotUnderstoodPendingMsg() {
		if(!pendingNotUnderstoodMessages.isEmpty()) {
			ACLMessage msg = pendingNotUnderstoodMessages.get(0);
			remove1stNotUnderstoodPendingMsg();
			return msg;
		}
		return null;
	}

	public ACLMessage get1stConfirmPendingMsg() {
		if(!pendingConfirmMessages.isEmpty()) {
			ACLMessage msg = pendingConfirmMessages.get(0);
			remove1stConfirmPendingMsg();
			return msg;
		}
		return null;
	}	

	private void remove1stAcceptProposalPendingMsg() {
		if(!pendingAcceptProposalMessages.isEmpty())
			pendingAcceptProposalMessages.remove(0);
	}

	private void remove1stProposePendingMsg() {
		if(!pendingProposeMessages.isEmpty())
			pendingProposeMessages.remove(0);
	}

	private void remove1stRefusePendingMsg() {
		if(!pendingRefuseMessages.isEmpty())
			pendingRefuseMessages.remove(0);
	}

	private void remove1stInformPendingMsg() {
		if(!pendingInformMessages.isEmpty())
			pendingInformMessages.remove(0);
	}

	private void remove1stNotUnderstoodPendingMsg() {
		if(!pendingNotUnderstoodMessages.isEmpty())
			pendingNotUnderstoodMessages.remove(0);
	}

	private void remove1stConfirmPendingMsg() {
		if(!pendingConfirmMessages.isEmpty())
			pendingConfirmMessages.remove(0);
	}
}