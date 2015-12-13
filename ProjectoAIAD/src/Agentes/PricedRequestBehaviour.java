package Agentes;

import java.util.ArrayList;
import java.util.Iterator;

import Agentes.AgenteTrabalhador.WorkerState;
import Logica.Ponto;
import Logica.Producto;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.core.behaviours.Behaviour;

public class PricedRequestBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	public enum RequestPricedJobBehaviourState {
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, CONFIRM_TO_WORK, RECEIVING_CONFIRMATIONS, WAITING_FOR_WORKERS,
		GO_TO_DELIVERY_POINT, MOVING_TO_DELIVERY_POINT, REWARDING_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}
	private RequestPricedJobBehaviourState behaviourState;

	private String conversationID;

	private AgenteTrabalhador worker;
	private String workerName;
	private ArrayList<AID> possibleWorkers;
	private ArrayList<AID> confirmedWorkers;
	private AID winnerWorker;
	private Service requestedService;
	private Ponto deliveryPoint;


	public PricedRequestBehaviour(AgenteTrabalhador worker, Service requestedService)
	{
		//super(worker);
		this.worker = worker;
		this.workerName = worker.getLocalName();
		this.requestedService = requestedService;
		this.conversationID = System.currentTimeMillis()+"";
		this.possibleWorkers = new ArrayList<AID>();
		this.confirmedWorkers = new ArrayList<AID>();
		this.winnerWorker = null;
		this.deliveryPoint = null;
		this.behaviourState = RequestPricedJobBehaviourState.FINDING_AVAILABLE_WORKERS;
	}

	@Override
	public void action()
	{
		switch (behaviourState) {
		case FINDING_AVAILABLE_WORKERS:
			findPossibleWorkers();		// QUERY
			break;
		case ASKING_TO_WORK:
			askingToWork(); 			// SEND - Begin Process
			break;
		case RECEIVING_CONFIRMATIONS:
			receivingConfirmations(); 	// RECEIVE
			break;
		case CONFIRM_TO_WORK:
			confirmToWork(); 			// SEND
			break;
		case WAITING_FOR_WORKERS:
			waitingForWorkers(); 		// RECEIVE
			break;
		case GO_TO_DELIVERY_POINT:
			goToDeleveryPoint();;
			break;
		case MOVING_TO_DELIVERY_POINT:
			movingToDeleveryPoint();
			break;
		case REWARDING_WORKER:
			rewardingWorkers(); 		// SEND
			break;
		case RECEIVING_PRODUCTS:
			receivingProducts(); 		// RECEIVE
			break;
		default:
			break;
		}
	}

	@Override
	public boolean done()
	{
		if(behaviourState == RequestPricedJobBehaviourState.DONE)
		{
			worker.debug("Proposta de trabalho a preco fixo em (" + requestedService.getName() + ") concluida.");
			return true;
		} else return false;
	}

	private void findPossibleWorkers()
	{
		DFAgentDescription[] workers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers != null) {
			for(DFAgentDescription worker: workers)
				possibleWorkers.add(worker.getName());
			behaviourState = RequestPricedJobBehaviourState.ASKING_TO_WORK;
		}
	}

	private void askingToWork() // SEND
	{
		worker.socializer.sendObject(ACLMessage.PROPOSE, possibleWorkers, conversationID, "WANT TO WORK ON FOR-" + requestedService.getName().toUpperCase(), requestedService);
		System.out.print("[" + workerName + "]: -> Enviou proposta de trabalho a preco fixo em (" + requestedService.getName() + ") a: ");
		for(AID worker: possibleWorkers) {
			System.out.print("[" + (worker.getLocalName()) + "], ");
		}
		System.out.println();
		behaviourState = RequestPricedJobBehaviourState.RECEIVING_CONFIRMATIONS;
	}

	private void receivingConfirmations() // RECEIVE
	{
		if(!possibleWorkers.isEmpty()) {
			if(worker.socializer.haveReplyPendingMsgs()) {
				for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
					ACLMessage msg = msgItem.next();
					if(msg.getConversationId().equals(conversationID)) {
						for(Iterator<AID> workerItem =  possibleWorkers.iterator(); workerItem.hasNext();) {
							AID possibleWorker = workerItem.next();
							if(possibleWorker.getLocalName().equals(msg.getSender().getLocalName())) {
								if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
									confirmedWorkers.add(msg.getSender());
									msgItem.remove();
									workerItem.remove();
									worker.debug("Recebeu aceitacao de [" + possibleWorker.getLocalName() + "] para trabalhar a preco fixo em (" + requestedService.getName() + ")");
								}
								else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
									msgItem.remove();
									workerItem.remove();
									worker.debug("Recebeu negacao de [" + possibleWorker.getLocalName() + "] para trabalhar a preco fixo em (" + requestedService.getName() + ")");
								}
							}
						}
					}
				}
			}
		}
		else
			behaviourState = RequestPricedJobBehaviourState.CONFIRM_TO_WORK;
	}

	private void confirmToWork() // SEND
	{
		worker.socializer.send(ACLMessage.CONFIRM, confirmedWorkers, conversationID, "SO DO JOB-" + requestedService.getName().toUpperCase());
		System.out.print("[" + workerName + "]: -> Enviou confirmacao para trabalhar a preco fixo em (" + requestedService.getName() + ") a ");
		for(AID worker: confirmedWorkers) {
			System.out.print("[" + (worker.getLocalName() + "], "));
		}
		System.out.println();

		behaviourState = RequestPricedJobBehaviourState.WAITING_FOR_WORKERS;
	}

	private void waitingForWorkers() // RECEIVE
	{
		if(winnerWorker == null) {
			if(worker.socializer.haveReplyPendingMsgs()) {
				for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
					ACLMessage msg = msgItem.next();
					if(msg.getConversationId().equals(conversationID)) {
						for(Iterator<AID> workerItem =  confirmedWorkers.iterator(); workerItem.hasNext();) {
							AID confirmedWorker = workerItem.next();
							if(confirmedWorker.getLocalName().equals(msg.getSender().getLocalName())) {
								if(msg.getPerformative() == ACLMessage.CONFIRM) {
									winnerWorker = msg.getSender();
									worker.debug("Recebeu de [" + confirmedWorker.getLocalName() + "] trabalho a preco fixo em (" + requestedService.getName() + ") concluido ");

									if(msg.getContent().contains("LOCAL")) {
										String[] local = msg.getContent().split("-")[2].split(" ");
										deliveryPoint = new Ponto(Integer.parseInt(local[0]), Integer.parseInt(local[1]));
										behaviourState = RequestPricedJobBehaviourState.GO_TO_DELIVERY_POINT;
									}
									else
										behaviourState = RequestPricedJobBehaviourState.GO_TO_DELIVERY_POINT; //REWARDING_WORKER;
									workerItem.remove();
									msgItem.remove();
									break;
								}
								else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
									msgItem.remove();
									workerItem.remove();
									worker.debug("Recebeu de [" + confirmedWorker.getLocalName() + "] trabalho a preco fixo em (" + requestedService.getName() + ") cancelado ");
								}
							}
						}
					}
				}
			}
		}
		else
			behaviourState = RequestPricedJobBehaviourState.GO_TO_DELIVERY_POINT; //REWARDING_WORKER;
	}

	public void goToDeleveryPoint() {
		if(deliveryPoint != null) {
			if(worker.state != WorkerState.MOVING) {
				worker.movimentar(deliveryPoint);
				behaviourState = RequestPricedJobBehaviourState.MOVING_TO_DELIVERY_POINT;
			}
		}
		else
			behaviourState = RequestPricedJobBehaviourState.REWARDING_WORKER;

		// ------------ Avisa outro agentes que o trabalho foi concluido ----------------------

		if(!confirmedWorkers.isEmpty()) {
			worker.socializer.send(ACLMessage.CANCEL, confirmedWorkers, conversationID, "JOB ALLREADY DONE-" + 0);
			System.out.print("[" + workerName + "]: -> Enviou cancelamento para trabalhar a preco fixo em (" + requestedService.getName() + ") a ");
			for(Iterator<AID> workerItem =  confirmedWorkers.iterator(); workerItem.hasNext();) {
				AID workerToCancel = workerItem.next();
				System.out.print("[" + (workerToCancel.getLocalName() + "], "));
				workerItem.remove();
			}
			System.out.println(" porque já foi concluido");
		}

		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					msgItem.remove();
				}
			}
		}
	}

	public void movingToDeleveryPoint() {
		if(deliveryPoint != null) {
			if(worker.state == WorkerState.NOT_MOVING)
				if(worker.tr.pontoToString().equals(deliveryPoint.pontoToString())) {
					worker.debug("Chegou ao destino");
					behaviourState = RequestPricedJobBehaviourState.REWARDING_WORKER;
				}
		}
		else
			behaviourState = RequestPricedJobBehaviourState.REWARDING_WORKER;
	}	

	public void rewardingWorkers() // SEND
	{
		// ------------ Paga o 1º agente que concluido trabalho ----------------------

		int reward = requestedService.getValue();
		if(requestedService.getValue() > worker.tr.riqueza)
			reward = worker.tr.riqueza;
		worker.tr.riqueza = worker.tr.riqueza - reward;

		worker.socializer.send(ACLMessage.CONFIRM, winnerWorker, conversationID, "REWARD-" + reward);
		worker.debug("Enviou reconpensa " + reward + " a [" + winnerWorker.getLocalName() + "] do trabalho a preco fixo em (" + requestedService.getName() + ")");



		if(requestedService.isEnvolveProducts())
			behaviourState = RequestPricedJobBehaviourState.RECEIVING_PRODUCTS;
		else
			behaviourState = RequestPricedJobBehaviourState.DONE;
	}

	private void receivingProducts()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {					
					if(winnerWorker.getLocalName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							try {
								TransferedObjects objects = (TransferedObjects) msg.getContentObject();
								for(Producto produto: objects.getObjects()) {
									worker.tr.adicionarContentor(produto, 1);
								}
							} catch (UnreadableException e) {
								worker.debug("ERRO AO DESERIALIZAR O OBJETO A RECEBER DE [" + winnerWorker.getLocalName() + "]");
							}
							msgItem.remove();
							worker.debug("Recebeu Produto de [" + winnerWorker.getLocalName() + "] do o trabalho a preco fixo em (" + requestedService.getName() + ")");
							behaviourState = RequestPricedJobBehaviourState.DONE;
							break;
						}
					}
				}
			}
		}
	}
}