package jade;

import messages.*;
import messages.AgentMessage.messageType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class RequestingJobBehaviour extends Behaviour{

	public enum RequestJobBehaviourState{
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, RECEIVING_CONFIRMATION, 
		WAITING_FOR_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}

	private RequestJobBehaviourState behaviourState;
	private Job job;	
	private AID assignedWorker;
	private boolean receivingProducts;
	private Object[] productsReceived;

	public RequestingJobBehaviour(Agent myAgent, Job job, boolean receivingProducts) {
		super(myAgent);
		behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
		this.job = job;
		this.receivingProducts = receivingProducts;
	}

	@Override
	public void action() {

		switch (behaviourState) {
		case FINDING_AVAILABLE_WORKERS:
			findWorkersWithService();
			break;
		case ASKING_TO_WORK:
			askingToWork();
			break;
		case RECEIVING_CONFIRMATION:
			receivingConfirmation();
			break;
		case WAITING_FOR_WORKER:
			waitingForWorker();
			break;
		case RECEIVING_PRODUCTS:
			System.out.println("Received Products"); //Efeitos de teste
			behaviourState = RequestJobBehaviourState.DONE;
		default:
			break;
		}
	}
	
	private void findWorkersWithService(){

		try {			
			ServiceDescription sd = new ServiceDescription();
			sd.setType(job.jobType);
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.addServices(sd);			
			DFAgentDescription[] result = DFService.search(myAgent, dfd);
			
			if(result.length != 0){
				assignedWorker = result[0].getName();
				behaviourState = RequestJobBehaviourState.ASKING_TO_WORK;
			}else behaviourState = RequestJobBehaviourState.NO_WORKERS_FOUND;
			
		} catch (FIPAException e) {
			e.printStackTrace();
		}		
	}
	
	private void askingToWork(){		
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(assignedWorker);
		message.setContent(new RequestJobMessage(job).toString());
		myAgent.send(message);
		behaviourState = RequestJobBehaviourState.RECEIVING_CONFIRMATION;
	}
	
	private void receivingConfirmation(){
		
		ACLMessage message = myAgent.receive();
		if(message != null)
		{
			System.out.println("RECEIVE: " + message);		
			String messageParts[] = message.getContent().split("-");			
			if(messageParts[0].equals(messageType.REPLY_MESSAGE) && 
			   message.getSender().getLocalName().equals(assignedWorker.getLocalName()) && 
			   messageParts[2].equals("YES-JOB"))
				behaviourState = RequestJobBehaviourState.WAITING_FOR_WORKER;	
		} else block();
	}

	private void waitingForWorker(){
		
		ACLMessage message = myAgent.receive();
		if(message != null)
		{
			System.out.println("RECEIVE: " + message);		
			String messageParts[] = message.getContent().split("-");			
			if(messageParts[0].equals(messageType.REPLY_MESSAGE) && 
			   message.getSender().getLocalName().equals(assignedWorker.getLocalName()) && 
			   messageParts[2].equals("DONE-JOB"))
				if(receivingProducts)
					behaviourState = RequestJobBehaviourState.RECEIVING_PRODUCTS;
				else
					behaviourState = RequestJobBehaviourState.DONE;
		} else block();		
	}

	private void receivingProducts()
	{
		//POR COMPLETAR
	}
	
	@Override
	public boolean done() {
		if(behaviourState.equals(RequestJobBehaviourState.DONE) || behaviourState.equals(RequestJobBehaviourState.DONE))
			return true;
		else return false;
	}
}
