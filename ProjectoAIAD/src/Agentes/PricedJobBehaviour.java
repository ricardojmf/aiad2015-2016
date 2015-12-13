package Agentes;

import sajas.core.AID;

import java.util.Iterator;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class PricedJobBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	public enum PricedJobBehaviourState {
		SENDING_CONFIRMATION, RECEIVING_CONFIRMATION, WORKING, SENDING_JOB_DONE, WAITING_FOR_REWARD,
		GIVING_PRODUCTS, DONE
	}
	private PricedJobBehaviourState behaviourState;

	private AID bossAgent;

	private String conversationID;

	private AgenteTrabalhador worker;
	private String workerName;
	private ACLMessage msgToSend;
	private Service requestedJob;

	public PricedJobBehaviour(AgenteTrabalhador worker, Service job, String conversationID, AID bossAgent)
	{
		//super(worker);
		this.worker = worker;
		this.workerName = worker.getLocalName();
		this.requestedJob = job;
		this.conversationID = conversationID;
		this.bossAgent = bossAgent;
		this.behaviourState = PricedJobBehaviourState.SENDING_CONFIRMATION;
	}

	@Override
	public void action() {
		switch (behaviourState) {
		case SENDING_CONFIRMATION:
			sendingConfirmation();		// SEND
			break;
		case RECEIVING_CONFIRMATION:
			receivingConfirmation(); 	// RECEIVE
			break;
		case WORKING:
			working(); 					// RECEIVE
			break;
		case SENDING_JOB_DONE:
			sendingJobDone(); 			// SEND
			break;
		case WAITING_FOR_REWARD:
			waitingForReward(); 		// RECEIVE
			break;
		case GIVING_PRODUCTS:
			givingProducts(); 			// SEND
			break;
		default:
			break;
		}
	}

	@Override
	public boolean done() {
		if(behaviourState == PricedJobBehaviourState.DONE)
		{
			System.out.println("[" + workerName + "] trabalho a preco fixo concluido");
			return true;
		} else return false;
	}

	public void sendingConfirmation()
	{
		msgToSend = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msgToSend.setConversationId(conversationID);
		msgToSend.addReceiver(bossAgent);

		if(true) {
			msgToSend.setContent("OK DO JOB-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			System.out.println("[" + workerName + "] Aceitou trabalho a preco fixo a: " + worker.getLocalName());
			behaviourState = PricedJobBehaviourState.RECEIVING_CONFIRMATION;
		}
		else {
			msgToSend.setPerformative(ACLMessage.REJECT_PROPOSAL);
			msgToSend.setContent("NO DO JOB-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			System.out.println("[" + workerName + "] Recusou trabalho a preco fixo a: " + worker.getLocalName());
			behaviourState = PricedJobBehaviourState.DONE;
		}
	}

	public void receivingConfirmation()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(bossAgent.getName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							behaviourState = PricedJobBehaviourState.WORKING;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							behaviourState = PricedJobBehaviourState.DONE;
						}
					}
				}
			}
		}
	}

	public void working()
	{

	}

	public void sendingJobDone()
	{
		msgToSend = new ACLMessage(ACLMessage.CONFIRM);
		msgToSend.setConversationId(conversationID);
		msgToSend.addReceiver(bossAgent);
		if(true) {
			msgToSend.setContent("JOB DONE-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			System.out.println("[" + workerName + "] Concluiu trabalho a preco fixo a: " + worker.getLocalName());
			behaviourState = PricedJobBehaviourState.WAITING_FOR_REWARD;
		}
		else {
			msgToSend.setPerformative(ACLMessage.REJECT_PROPOSAL);
			msgToSend.setContent("JOB NOT DONE-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			System.out.println("[" + workerName + "] Nao acabou trabalho a preco fixo a: " + worker.getLocalName());
			behaviourState = PricedJobBehaviourState.DONE;
		}

	}

	public void waitingForReward()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(bossAgent.getName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							behaviourState = PricedJobBehaviourState.GIVING_PRODUCTS;
						}
						else if(msg.getPerformative() == ACLMessage.CANCEL) {
							msgItem.remove();
							behaviourState = PricedJobBehaviourState.DONE;
						}
					}
				}
			}
		}
	}

	public void givingProducts()
	{
		msgToSend = new ACLMessage(ACLMessage.CONFIRM);
		msgToSend.setConversationId(conversationID);
		msgToSend.addReceiver(bossAgent);
		msgToSend.setContent("PRODUCT-" + requestedJob.getName().toUpperCase());
		myAgent.send(msgToSend);
		System.out.println("[" + workerName + "] Concluiu trabalho a preco fixo a: " + worker.getLocalName());
		behaviourState = PricedJobBehaviourState.DONE;
	}

}