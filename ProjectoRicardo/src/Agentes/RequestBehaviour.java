package Agentes;

import Messages.*;

import sajas.core.behaviours.Behaviour;

import java.util.Iterator;

import com.sun.media.rtsp.protocol.RequestMessage;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;


public class RequestBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;

	public enum RequestJobBehaviourState{
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, RECEIVING_CONFIRMATION, 
		WAITING_FOR_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}

	private RequestJobBehaviourState behaviourState;

	private AgenteTrabalhador worker;
	private String conversationID;
	private String workerName;
	private ACLMessage msg;	
	private DFAgentDescription[] possibleWorkers;
	private Service requestedService;
	private boolean requestedServiceDone;
	private boolean receivingProducts;
	private Object[] productsReceived;
	private AID assignedWorker;

	public RequestBehaviour(AgenteTrabalhador worker, Service requestedService) {
		super(worker);
		this.behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
		this.worker = worker;
		this.workerName = worker.getLocalName();
		this.conversationID = System.currentTimeMillis()+"";
		this.requestedService = requestedService;
		this.requestedServiceDone = false;
	}

	@Override
	public void action() {

		switch (behaviourState) {
		case FINDING_AVAILABLE_WORKERS:
			findWorkersWithService();
			break;
		case NO_WORKERS_FOUND:
			noWorkersFound();
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

		possibleWorkers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers.length != 0){
			worker.printToConsole("Worker Founded: " + possibleWorkers[0].getName().getLocalName());
			assignedWorker = possibleWorkers[0].getName(); 
			behaviourState = RequestJobBehaviourState.ASKING_TO_WORK;
		}else
			behaviourState = RequestJobBehaviourState.NO_WORKERS_FOUND;	
	}

	private void noWorkersFound(){
		worker.printToConsole("No worker found for the requested service! Trying again in 15 seconds!");
		block(1500);
	}

	private void askingToWork(){	
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(assignedWorker);
		message.setContent(new Messages.ProposeMessage(myAgent.getLocalName(), requestedService.getType()).toString());
		myAgent.send(message);
		behaviourState = RequestJobBehaviourState.RECEIVING_CONFIRMATION;
	}

	private void receivingConfirmation(){

		if(worker.socializer.haveConfirmPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getConfirmPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(assignedWorker.getName().equals(msg.getSender().getLocalName())) {
						if(msg.getContent().contains("YES"))	
						behaviourState = RequestJobBehaviourState.WAITING_FOR_WORKER;
					}
				}
			}
		}
	}

	private void waitingForWorker(){
		
		if(worker.socializer.haveConfirmPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getConfirmPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(assignedWorker.getName().equals(msg.getSender().getLocalName())) {
						if(msg.getContent().contains("DONE"))
							if(receivingProducts)
								behaviourState = RequestJobBehaviourState.RECEIVING_PRODUCTS;
							else
								behaviourState = RequestJobBehaviourState.DONE;
					}
				}
			}
		}		
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