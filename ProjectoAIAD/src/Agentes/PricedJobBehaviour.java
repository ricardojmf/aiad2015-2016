package Agentes;

import sajas.core.AID;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class PricedJobBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;

	public enum PricedJobBehaviourState {
		SENDING_CONFIRMATION, 
		WAITING_FOR_WORKER, REWARDING_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
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
	}

	@Override
	public void action() {
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}