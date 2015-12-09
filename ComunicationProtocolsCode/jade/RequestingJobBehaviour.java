package jade;
import messages.*;
import messages.AgentMessage.messageType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import messages.RequestJobMessage;

public class RequestingJobBehaviour extends Behaviour{

	public enum RequestJobBehaviourState{
		FINDING_WORKERS_WITH_SERVICE, FIND_WORKERS_AVAILABLE, 
		WAITING_FIRST_REPLY, CONFIRMATION_REPLY, NO_WORKERS_AVAILABLE, 
		WAITING_FOR_COMPlETITION, DONE, 
	}

	private int numWorkersAvailable;
	private RequestJobBehaviourState behaviourState;
	private Job job;
	private RequestJobMessage requestJobMessage;
	private ArrayList<DFAgentDescription> foundWorkers;
	private Agent assignedWorker;
	private ServiceDescription serviceDescription;
	private DFAgentDescription dfd;


	public RequestingJobBehaviour(Agent myAgent, Job job) {
		super(myAgent);
		this.behaviourState = RequestJobBehaviourState.FINDING_WORKERS_WITH_SERVICE;
		this.job = job;
		this.requestJobMessage = new RequestJobMessage(myAgent.getLocalName(), job);
		this.serviceDescription = new ServiceDescription();
		this.serviceDescription.setType(job.jobType);
		this.dfd = new DFAgentDescription();
		this.dfd.addServices(this.serviceDescription);
	}

	@Override
	public void action() {

		switch (behaviourState) {
		case FINDING_WORKERS_WITH_SERVICE:
			findWorkersWithService();			
			break;
		case FIND_WORKERS_AVAILABLE:
			findWorkerAvailable();
			break;
		case WAITING_FIRST_REPLY:
			waitingFirstreplpy();
			break;
		case CONFIRMATION_REPLY:		
			break;
		default:
			break;
		}
	}
	
	/*
	 * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 * :: A PARTE DAS MENDAGENS DESTAS FUNÇÕES VAO SER IMPLEMENTADAS NO BEHAVIOUR DA COMUNICAÇÃO ::
	 * ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	 */
	
	private void waitingFirstreplpy(){
		ACLMessage message = myAgent.receive();
		if(message != null)
		{
			System.out.println("RECEIVE: " + message);
			
			String messageParts[] = message.getContent().split("-");
			
			if(messageParts[0].equals(messageType.REPLY_MESSAGE) && messageParts[1].equals(assignedWorker.getLocalName()) && messageParts[2].equals("YES"))
				behaviourState = RequestJobBehaviourState.WAITING_FOR_COMPlETITION;
			else{
				foundWorkers.remove(0);
			}			
		}
	}

	private void findWorkerAvailable(){		
		
		if(foundWorkers.size() != 0)
		{
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.addReceiver(foundWorkers.get(0).getName());
			message.setContent(requestJobMessage.toString());
			myAgent.send(message);
			behaviourState = RequestJobBehaviourState.WAITING_FIRST_REPLY;
		}else behaviourState = RequestJobBehaviourState.NO_WORKERS_AVAILABLE;
	}

	private void findWorkersWithService(){

		try {
			DFAgentDescription[] result = DFService.search(myAgent, dfd);
			foundWorkers = new ArrayList<DFAgentDescription>(Arrays.asList(result));
			behaviourState = RequestJobBehaviourState.FIND_WORKERS_AVAILABLE;
		} catch (FIPAException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
