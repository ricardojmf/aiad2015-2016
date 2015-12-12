package Agentes;

import sajas.core.behaviours.CyclicBehaviour;

import java.util.ArrayList;

import jade.lang.acl.ACLMessage;

public class SocialBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	//private AgenteTrabalhador worker;
	private String workerName;

	private ArrayList<ACLMessage> pendingProposeMessages;
	private ArrayList<ACLMessage> pendingReplyMessages;


	public SocialBehaviour(AgenteTrabalhador worker) {
		//this.worker = worker;
		this.workerName = worker.getLocalName();
		this.pendingProposeMessages = new ArrayList<ACLMessage>();
		this.pendingReplyMessages = new ArrayList<ACLMessage>();
	}

	@Override
	public void action() {
		System.out.println("[" + workerName + "] Listening... ");

		ACLMessage aclMessage = myAgent.receive();
		
		if(aclMessage != null)
		{
			addPendingMsg(aclMessage);
			System.out.println("[" + workerName + "] Recebeu uma mensagem de [" + aclMessage.getSender().getLocalName() + "] dizendo: " + aclMessage.getContent());
		}
		else {
			block();
		}
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
	
	public boolean haveProposePendingMsgs() {
		return !pendingProposeMessages.isEmpty();
	}
	
	public boolean haveReplyPendingMsgs() {
		return !pendingReplyMessages.isEmpty();
	}
	
	public ArrayList<ACLMessage> getReplyPendingMsgs() {
		return pendingReplyMessages;
	}
	
	public ACLMessage get1stProposePendingMsg() {
		if(!pendingProposeMessages.isEmpty()) {
			ACLMessage msg = pendingProposeMessages.get(0);
			remove1stProposePendingMsg();
			return msg;
		}
		return null;
	}
	
	private void remove1stProposePendingMsg() {
		if(!pendingProposeMessages.isEmpty())
			pendingProposeMessages.remove(0);
	}
}