package jade;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import messages.RequestJobMessage;

public class RequestingJobBehaviour extends Behaviour{
	
	public enum RequestJobBehaviourState{
		FINDING_WORKERS, WAITING_FIRST_REPLY, CONFIRMATION_REPLY
	}
	
	private int numWorkersAvailable;
	private RequestJobBehaviourState behaviourState;
	private Job job;
	private RequestJobMessage requestJobMessage;
	private GivingAgent[] givingAgents;
	
	public RequestingJobBehaviour(Agent myAgent, RequestJobMessage requestJobMessage) {
		super(myAgent);
		this.behaviourState = RequestJobBehaviourState.FINDING_WORKERS;
		this.requestJobMessage = requestJobMessage;
	}

	@Override
	public void action() {
		
		switch (behaviourState) {
		case FINDING_WORKERS:
			
			ACLMessage message = myAgent.receive();
			
			
			break;
		case WAITING_FIRST_REPLY:			
			break;
		case CONFIRMATION_REPLY:			
			break;
		default:
			break;
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
