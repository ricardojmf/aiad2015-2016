package Agentes;

import jade.core.AID;

import java.util.Iterator;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class PricedJobBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	public enum PricedJobBehaviourState {
		SENDING_CONFIRMATION, RECEIVING_CONFIRMATION, WORKING, SENDING_JOB_DONE, WAITING_FOR_REWARD, GIVING_PRODUCTS, DONE
	}
	private PricedJobBehaviourState behaviourState;

	private AID bossAgent;

	private String conversationID;

	private AgenteTrabalhador worker;
	//private String workerName;
	private ACLMessage msgToSend;
	private Service requestedJob;
	private boolean jobOnTime;
	private int debugJobCounter;

	public PricedJobBehaviour(AgenteTrabalhador worker, Service job, String conversationID, AID bossAgent)
	{
		//super(worker);
		this.worker = worker;
		//this.workerName = worker.getLocalName();
		this.requestedJob = job;
		this.conversationID = conversationID;
		this.bossAgent = bossAgent;
		this.jobOnTime = true;
		this.debugJobCounter = 5;
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
			if (jobOnTime)
				worker.debug("Trabalho a preco fixo em (" + requestedJob.getName() + ") concluido para [" + bossAgent.getLocalName() + "]");
			else
				worker.debug("Trabalho a preco fixo em (" + requestedJob.getName() + ") nao concluido a tempo para [" + bossAgent.getLocalName() + "]");
			return true;
		} else return false;
	}

	public void sendingConfirmation()
	{
		msgToSend = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		msgToSend.setConversationId(conversationID);
		msgToSend.addReceiver(bossAgent);

		if(true) {
			msgToSend.setContent("OK I DO JOB-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			worker.debug("Enviou aceitacao do trabalho a preco fixo em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedJobBehaviourState.RECEIVING_CONFIRMATION;
		}
		else {
			msgToSend.setPerformative(ACLMessage.REJECT_PROPOSAL);
			msgToSend.setContent("I DONT DO JOB-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			worker.debug("Enviou recusacao do trabalho a preco fixo em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedJobBehaviourState.DONE;
		}
	}

	public void receivingConfirmation()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(bossAgent.equals(msg.getSender())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							worker.debug("Recebeu confirmacao de [" + bossAgent.getLocalName() + "] para trabalhar a preco fixo em (" + requestedJob.getName() + ")");
							behaviourState = PricedJobBehaviourState.WORKING;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar a preco fixo em (" + requestedJob.getName() + ")");
							behaviourState = PricedJobBehaviourState.DONE;
						}
					}
				}
			}
		}
	}

	public void working()
	{
		// worker.addBehaviour(TASKS_OF_THE_JOB_TODO)
		
		worker.debug("Trabalhando a preco fixo em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
		
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(bossAgent.equals(msg.getSender())) {
						if(msg.getPerformative() == ACLMessage.CANCEL) {
							msgItem.remove();
							jobOnTime = false;
							worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar a preco fixo em (" + requestedJob.getName() + ") porque ja foi concluido por outro");
							behaviourState = PricedJobBehaviourState.DONE;
						}
					}
				}
			}
		}
		
		if(debugJobCounter <= 0)
			behaviourState = PricedJobBehaviourState.SENDING_JOB_DONE;
		debugJobCounter--;
	}

	public void sendingJobDone()
	{
		msgToSend = new ACLMessage(ACLMessage.CONFIRM);
		msgToSend.setConversationId(conversationID);
		msgToSend.addReceiver(bossAgent);
		if(true) {
			msgToSend.setContent("JOB DONE-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			worker.debug("Enviou Conclusao do trabalho a preco fixo em (" + requestedJob.getName() + ") avisando [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedJobBehaviourState.WAITING_FOR_REWARD;
		}
		else {
			msgToSend.setPerformative(ACLMessage.REJECT_PROPOSAL);
			msgToSend.setContent("JOB NOT DONE-" + requestedJob.getName().toUpperCase());
			myAgent.send(msgToSend);
			worker.debug("Enviou nao conclusao do trabalho a preco fixo em (" + requestedJob.getName() + ") avisando [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedJobBehaviourState.DONE;
		}

	}

	public void waitingForReward()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(bossAgent.getLocalName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							worker.debug("Recebeu reconpensa de [" + bossAgent.getLocalName() + "] do o trabalho a preco fixo em (" + requestedJob.getName() + ")");
							behaviourState = PricedJobBehaviourState.GIVING_PRODUCTS;
						}
						else if(msg.getPerformative() == ACLMessage.CANCEL) {
							msgItem.remove();
							jobOnTime = false;
							worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar a preco fixo em (" + requestedJob.getName() + ") porque ja foi concluido por outro");
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
		worker.debug("Enviou produto do trabalho a preco fixo em (" + requestedJob.getName() + ") a [" + bossAgent.getLocalName() + "]");
		behaviourState = PricedJobBehaviourState.DONE;
	}

}