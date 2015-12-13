package Agentes;

import java.util.Iterator;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class SimpleWorkBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;

	public enum WorkingBehaviourState {
		SENDING_CONFIRMATION, WORKING, SENDING_JOB_DONE, WAITING_FOR_REWARD, DELIVERING_PRODUCTS, DONE
	}

	private WorkingBehaviourState behaviourState;
	private Service requestedJob;
	private AID bossAgent;
	private String conversationID;
	private AgenteTrabalhador worker;
	private int debugJobCounter;
	private boolean makingProducts;
	private TransferedObjects products;
	//private String workerName;


	public SimpleWorkBehaviour(AgenteTrabalhador worker, Service job, String conversationID, AID bossAgent)
	{
		super(worker);
		this.makingProducts = job.isEnvolveProducts();
		this.worker = worker;
		this.requestedJob = job;
		this.conversationID = conversationID;
		this.bossAgent = bossAgent;
		this.debugJobCounter = 5;
		this.products = new TransferedObjects();
		this.behaviourState = WorkingBehaviourState.SENDING_CONFIRMATION;
		//DeregistWork(job);
		//this.workerName = worker.getLocalName();
		//this.job = new Job("Transport", "50::Wood::WarehouseOne");
	}

	@Override
	public void action()
	{
		switch (behaviourState) {
		case SENDING_CONFIRMATION:
			sendingConfirmation();		// SEND
			break;
		case WORKING:
			working(); 					// RECEIVE
			break;
		case SENDING_JOB_DONE:
			sendingJobDone();			// SEND
			break;
		case WAITING_FOR_REWARD:
			waitingForReward(); 		// RECEIVE
			break;
		case DELIVERING_PRODUCTS:
			givingProducts(); 			// SEND
			break;
		default:
			break;
		}
	}

	@Override
	public boolean done()
	{
		if(behaviourState.equals(WorkingBehaviourState.DONE))
		{
			worker.debug("Trabalho em (" + requestedJob.getName() + ") concluido para [" + bossAgent.getLocalName() + "]");
			return true;
		}
		else return false;
	}

	public void sendingConfirmation()
	{
		if(true) {
			worker.socializer.send(ACLMessage.ACCEPT_PROPOSAL, bossAgent, conversationID, "OK I DO JOB-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou aceitacao do trabalho em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
			behaviourState = WorkingBehaviourState.WORKING;
		}
		else {
			worker.socializer.send(ACLMessage.REJECT_PROPOSAL, bossAgent, conversationID, "I DONT DO JOB-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou recusacao do trabalho em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");
			behaviourState = WorkingBehaviourState.DONE;
		}
	}

	private void working()
	{
		// worker.addBehaviour(TASKS_OF_THE_JOB_TODO)

		worker.debug("Trabalhando em (" + requestedJob.getName() + ") para [" + bossAgent.getLocalName() + "]");

		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(bossAgent.equals(msg.getSender())) {
						if(msg.getPerformative() == ACLMessage.CANCEL) {
							msgItem.remove();
							worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar em (" + requestedJob.getName() + ")");
							behaviourState = WorkingBehaviourState.DONE;
							break;
						}
					}
				}
			}
		}

		if(debugJobCounter <= 0)
			behaviourState = WorkingBehaviourState.SENDING_JOB_DONE;
		debugJobCounter--;
	}

	public void sendingJobDone()
	{
		if(true) {
			worker.socializer.send(ACLMessage.CONFIRM, bossAgent, conversationID, "JOB DONE-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou Conclusao do trabalho em (" + requestedJob.getName() + ") avisando [" + bossAgent.getLocalName() + "]");
			behaviourState = WorkingBehaviourState.WAITING_FOR_REWARD;
		}
		else {
			worker.socializer.send(ACLMessage.REJECT_PROPOSAL, bossAgent, conversationID, "JOB NOT DONE-" + requestedJob.getName().toUpperCase());
			worker.debug("Enviou nao conclusao do trabalho em (" + requestedJob.getName() + ") avisando [" + bossAgent.getLocalName() + "]");
			behaviourState = WorkingBehaviourState.DONE;
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
							worker.debug("Recebeu reconpensa de [" + bossAgent.getLocalName() + "] do o trabalho em (" + requestedJob.getName() + ")");

							if(makingProducts)
								behaviourState = WorkingBehaviourState.DELIVERING_PRODUCTS;
							else
								behaviourState = WorkingBehaviourState.DONE;
							break;
						}
					}
					else if(msg.getPerformative() == ACLMessage.CANCEL) {
						msgItem.remove();
						worker.debug("Recebeu cancelamento de [" + bossAgent.getLocalName() + "] para trabalhar em (" + requestedJob.getName() + ")");
						behaviourState = WorkingBehaviourState.DONE;
						break;
					}
				}
			}
		}
	}

	public void givingProducts()
	{
		worker.socializer.sendObject(ACLMessage.CONFIRM, bossAgent, conversationID, "PRODUCT", products);
		worker.debug("Enviou produto do trabalho em (" + requestedJob.getName() + ") a [" + bossAgent.getLocalName() + "]");
		behaviourState = WorkingBehaviourState.DONE;
	}
}