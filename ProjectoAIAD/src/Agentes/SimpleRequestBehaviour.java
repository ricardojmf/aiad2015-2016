package Agentes;

import java.util.Iterator;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class SimpleRequestBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;

	public enum RequestJobBehaviourState{
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, RECEIVING_CONFIRMATION, 
		WAITING_FOR_WORKER, REWARDING_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}

	private RequestJobBehaviourState behaviourState;

	private AgenteTrabalhador worker;
	private String conversationID;
	private String workerName;
	private ACLMessage msgToSend;
	private DFAgentDescription[] possibleWorkers;
	private Service requestedService;
	//private boolean requestedServiceDone;
	private boolean receivingProducts;
	//private Object[] productsReceived;
	private AID assignedWorker;

	public SimpleRequestBehaviour(AgenteTrabalhador worker, Service requestedService)
	{
		super(worker);
		this.worker = worker;
		this.workerName = worker.getLocalName();
		this.conversationID = System.currentTimeMillis()+"";
		this.requestedService = requestedService;
		this.receivingProducts = requestedService.isEnvolveProducts();
		//this.requestedServiceDone = false;
		this.behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
	}

	@Override
	public void action()
	{
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
		case REWARDING_WORKER:
			rewardingWorkers();
		case RECEIVING_PRODUCTS:
			receivingProducts();
		default:
			break;
		}
	}

	@Override
	public boolean done()
	{
		if(behaviourState.equals(RequestJobBehaviourState.DONE) || behaviourState.equals(RequestJobBehaviourState.DONE)) {
			System.out.println("[" + workerName + "] Proposta de trabalho a preco fixo em (" + requestedService.getName() + ") concluida.");
			return true;
		}
		else return false;
	}

	private void findWorkersWithService()
	{
		possibleWorkers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers.length != 0){
			//worker.printToConsole("Worker Founded: " + possibleWorkers[0].getName().getLocalName());
			assignedWorker = possibleWorkers[0].getName(); 
			behaviourState = RequestJobBehaviourState.ASKING_TO_WORK;
		}else
			behaviourState = RequestJobBehaviourState.NO_WORKERS_FOUND;	
	}

	private void noWorkersFound()
	{
		//worker.printToConsole("No worker found for the requested service! Trying again in 15 seconds!");
		System.out.println("No worker found for the requested service! Trying again in 15 seconds!");
		block(1500);
	}

	private void askingToWork()
	{	
		msgToSend = new ACLMessage(ACLMessage.PROPOSE);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("DO WORK ON FOR-" + requestedService.getName().toUpperCase());

		System.out.println("[" + workerName + "] Enviou proposta de trabalho em (" + requestedService.getName() + ") para ["+ assignedWorker.getLocalName() + "], ");
		msgToSend.addReceiver(assignedWorker);
		myAgent.send(msgToSend);
		behaviourState = RequestJobBehaviourState.RECEIVING_CONFIRMATION;
	}

	private void receivingConfirmation()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(assignedWorker.getLocalName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
							msgItem.remove();
							System.out.println("[" + workerName + "] Recebeu aceitacao de [" + assignedWorker.getLocalName() + "] para trabalhar em (" + requestedService.getName() + ")");
							behaviourState = RequestJobBehaviourState.WAITING_FOR_WORKER;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							System.out.println("[" + workerName + "] Recebeu negacao de [" + assignedWorker.getLocalName() + "] para trabalhar em (" + requestedService.getName() + ")");
							behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
						}
					}
				}
			}
		}
	}

	private void waitingForWorker()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					if(assignedWorker.getLocalName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							System.out.println("[" + workerName + "] Recebeu de [" + assignedWorker.getLocalName() + "] trabalho em (" + requestedService.getName() + ") concluido ");
							behaviourState = RequestJobBehaviourState.REWARDING_WORKER;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							System.out.println("[" + workerName + "] Recebeu de [" + assignedWorker.getLocalName() + "] trabalho em (" + requestedService.getName() + ") cancelado ");
							behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
						}
					}
				}
			}
		}
	}

	public void rewardingWorkers() // SEND
	{
		// ------------ Paga o agente que concluido trabalho ----------------------
		msgToSend = new ACLMessage(ACLMessage.CONFIRM);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("REWARD-" + 1000);

		System.out.println("[" + workerName + "] Enviou reconpensa a [" + assignedWorker.getLocalName() + "] do trabalho em (" + requestedService.getName() + ")");
		msgToSend.addReceiver(assignedWorker);
		myAgent.send(msgToSend);

		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					msgItem.remove();
				}
			}
		}

		if(receivingProducts)
			behaviourState = RequestJobBehaviourState.RECEIVING_PRODUCTS;
		else
			behaviourState = RequestJobBehaviourState.DONE;
	}

	private void receivingProducts()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {					
					if(assignedWorker.getLocalName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							System.out.println("[" + workerName + "] Recebeu Produto de [" + assignedWorker.getLocalName() + "] do trabalho em (" + requestedService.getName() + ")");
							behaviourState = RequestJobBehaviourState.DONE;
						}
					}
				}
			}
		}
	}
}