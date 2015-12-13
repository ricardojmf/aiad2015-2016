package Agentes;

import java.util.Iterator;

import Agentes.AgenteTrabalhador.WorkerState;
import Logica.Ponto;
import Logica.Producto;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.core.behaviours.Behaviour;

public class SimpleRequestBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;

	public enum RequestJobBehaviourState {
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, RECEIVING_CONFIRMATION, WAITING_FOR_WORKER,
		GO_TO_DELIVERY_POINT, MOVING_TO_DELIVERY_POINT, REWARDING_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}

	private RequestJobBehaviourState behaviourState;

	private AgenteTrabalhador worker;
	private String conversationID;
	private DFAgentDescription[] possibleWorkers;
	private Service requestedService;
	private AID assignedWorker;
	private Ponto deliveryPoint;
	//private String workerName;
	//private boolean requestedServiceDone;
	//private Object[] productsReceived;

	public SimpleRequestBehaviour(AgenteTrabalhador worker, Service requestedService)
	{
		super(worker);
		this.worker = worker;
		this.conversationID = System.currentTimeMillis()+"";
		this.requestedService = requestedService;
		this.behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
		this.deliveryPoint = null;
		//this.workerName = worker.getLocalName();
		//this.requestedServiceDone = false;
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
		case GO_TO_DELIVERY_POINT:
			goToDeleveryPoint();;
			break;
		case MOVING_TO_DELIVERY_POINT:
			movingToDeleveryPoint();
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
			worker.debug("Proposta de trabalho a preco fixo em (" + requestedService.getName() + ") concluida.");
			return true;
		}
		else return false;
	}

	private void findWorkersWithService()
	{
		possibleWorkers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers.length != 0){
			assignedWorker = possibleWorkers[0].getName(); 
			behaviourState = RequestJobBehaviourState.ASKING_TO_WORK;
		}else
			behaviourState = RequestJobBehaviourState.NO_WORKERS_FOUND;	
	}

	private void noWorkersFound()
	{
		worker.debug("No worker found for the requested service! Trying again in 15 seconds!");
		block(1500);
	}

	private void askingToWork()
	{
		worker.socializer.sendObject(ACLMessage.PROPOSE, assignedWorker, conversationID, "DO WORK ON FOR-" + requestedService.getName().toUpperCase(), requestedService);
		worker.debug("Enviou proposta de trabalho em (" + requestedService.getName() + ") para ["+ assignedWorker.getLocalName() + "], ");
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
							worker.debug("Recebeu aceitacao de [" + assignedWorker.getLocalName() + "] para trabalhar em (" + requestedService.getName() + ")");
							behaviourState = RequestJobBehaviourState.WAITING_FOR_WORKER;
							break;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							worker.debug("Recebeu negacao de [" + assignedWorker.getLocalName() + "] para trabalhar em (" + requestedService.getName() + ")");
							behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
							break;
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
							worker.debug("Recebeu de [" + assignedWorker.getLocalName() + "] trabalho em (" + requestedService.getName() + ") concluido ");
							if(msg.getContent().contains("LOCAL")) {
								String[] local = msg.getContent().split("-")[2].split(" ");
								deliveryPoint = new Ponto(Integer.parseInt(local[0]), Integer.parseInt(local[1]));
								behaviourState = RequestJobBehaviourState.GO_TO_DELIVERY_POINT;
							}
							else
								behaviourState = RequestJobBehaviourState.REWARDING_WORKER;
							msgItem.remove();
							break;
						}
						else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
							msgItem.remove();
							worker.debug("Recebeu de [" + assignedWorker.getLocalName() + "] trabalho em (" + requestedService.getName() + ") cancelado ");
							behaviourState = RequestJobBehaviourState.FINDING_AVAILABLE_WORKERS;
							break;
						}
					}
				}
			}
		}
	}

	public void goToDeleveryPoint() {
		if(deliveryPoint != null) {
			if(worker.state != WorkerState.MOVING) {
				worker.movimentar(deliveryPoint);
				behaviourState = RequestJobBehaviourState.MOVING_TO_DELIVERY_POINT;
			}
		}
		else
			behaviourState = RequestJobBehaviourState.REWARDING_WORKER;
	}

	public void movingToDeleveryPoint() {
		if(deliveryPoint != null) {
			if(worker.state == WorkerState.NOT_MOVING)
				if(worker.tr.pontoToString().equals(deliveryPoint.pontoToString())) {
					worker.debug("Chegou ao destino");
					behaviourState = RequestJobBehaviourState.REWARDING_WORKER;
				}
		}
		else
			behaviourState = RequestJobBehaviourState.REWARDING_WORKER;
	}	

	public void rewardingWorkers() // SEND
	{
		// ------------ Paga o agente que concluido trabalho ----------------------

		int reward = requestedService.getValue();
		if(requestedService.getValue() > worker.tr.riqueza)
			reward = worker.tr.riqueza;
		worker.tr.riqueza = worker.tr.riqueza - reward;

		worker.socializer.send(ACLMessage.CONFIRM, assignedWorker, conversationID, "REWARD-" + reward);
		worker.debug("Enviou reconpensa " + reward + " a [" + assignedWorker.getLocalName() + "] do trabalho em (" + requestedService.getName() + ")");

		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					msgItem.remove();
				}
			}
		}

		if(requestedService.isEnvolveProducts())
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
							try {
								TransferedObjects objects = (TransferedObjects) msg.getContentObject();
								for(Producto produto: objects.getObjects()) {
									worker.tr.adicionarContentor(produto, Integer.parseInt(requestedService.getType())  );
								}
								worker.tr.verContentor();
							} catch (UnreadableException e) {
								worker.debug("ERRO AO DESERIALIZAR O OBJETO A RECEBER DE [" + assignedWorker.getLocalName() + "]");
							}
							msgItem.remove();
							worker.debug("Recebeu Produto de [" + assignedWorker.getLocalName() + "] do trabalho em (" + requestedService.getName() + ")");
							behaviourState = RequestJobBehaviourState.DONE;
							break;
						}
					}
				}
			}
		}
	}
}