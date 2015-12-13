package Agentes;

import jade.core.AID;

import java.util.Iterator;

import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class PricedWorkBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	public enum PricedWorkBehaviourState {
		SENDING_CONFIRMATION, RECEIVING_CONFIRMATION, WORKING, SENDING_JOB_DONE, WAITING_FOR_REWARD, GIVING_PRODUCTS, DONE
	}
	private PricedWorkBehaviourState behaviourState;

	private AID bossAgent;

	private String conversationID;

	private AgenteTrabalhador worker;
	//private String workerName;
	private Service requestedJob;
	private boolean jobOnTime;
	private TransferedObjects products;
	private int debugJobCounter;

	public PricedWorkBehaviour(AgenteTrabalhador worker, Service job, String conversationID, AID bossAgent)
	{
		//super(worker);
		this.worker = worker;
		//this.workerName = worker.getLocalName();
		this.requestedJob = job;
		this.conversationID = conversationID;
		this.bossAgent = bossAgent;
		this.jobOnTime = true;
		this.products = new TransferedObjects();
		this.debugJobCounter = 5;
		this.behaviourState = PricedWorkBehaviourState.SENDING_CONFIRMATION;
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
		if(behaviourState == PricedWorkBehaviourState.DONE)
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
		if(true) {
			worker.socializer.send(ACLMessage.ACCEPT_PROPOSAL, bossAgent, conversationID, "OK I DO JOB-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou aceitacao do trabalho a preco fixo em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedWorkBehaviourState.RECEIVING_CONFIRMATION;
		}
		else {
			worker.socializer.send(ACLMessage.REJECT_PROPOSAL, bossAgent, conversationID, "I DONT DO JOB-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou recusacao do trabalho a preco fixo em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedWorkBehaviourState.DONE;
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
							behaviourState = PricedWorkBehaviourState.WORKING;
							break;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar a preco fixo em (" + requestedJob.getName() + ")");
							behaviourState = PricedWorkBehaviourState.DONE;
							break;
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
							behaviourState = PricedWorkBehaviourState.DONE;
							break;
						}
					}
				}
			}
		}
		
		if(debugJobCounter <= 0)
			behaviourState = PricedWorkBehaviourState.SENDING_JOB_DONE;
		debugJobCounter--;
	}

	public void sendingJobDone()
	{
		if(true) {
			worker.socializer.send(ACLMessage.CONFIRM, bossAgent, conversationID, "JOB DONE-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou Conclusao do trabalho a preco fixo em (" + requestedJob.getName() + ") avisando [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedWorkBehaviourState.WAITING_FOR_REWARD;
		}
		else {
			worker.socializer.send(ACLMessage.REJECT_PROPOSAL, bossAgent, conversationID, "JOB NOT DONE-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou nao conclusao do trabalho a preco fixo em (" + requestedJob.getName() + ") avisando [" + bossAgent.getLocalName() + "]");
			behaviourState = PricedWorkBehaviourState.DONE;
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
							behaviourState = PricedWorkBehaviourState.GIVING_PRODUCTS;
							break;
						}
						else if(msg.getPerformative() == ACLMessage.CANCEL) {
							msgItem.remove();
							jobOnTime = false;
							worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar a preco fixo em (" + requestedJob.getName() + ") porque ja foi concluido por outro");
							behaviourState = PricedWorkBehaviourState.DONE;
							break;
						}
					}
				}
			}
		}
	}

	public void givingProducts()
	{
		worker.socializer.sendObject(ACLMessage.CONFIRM, bossAgent, conversationID, "PRODUCT", products);
		worker.debug("Enviou produto do trabalho a preco fixo em (" + requestedJob.getName() + ") a [" + bossAgent.getLocalName() + "]");
		behaviourState = PricedWorkBehaviourState.DONE;
	}

}