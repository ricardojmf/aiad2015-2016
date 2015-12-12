package Agentes;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class PricedRequestBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	public enum RequestPricedJobBehaviourState {
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, CONFIRM_TO_WORK, RECEIVING_CONFIRMATIONS, 
		WAITING_FOR_WORKERS, REWARDING_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}
	private RequestPricedJobBehaviourState behaviourState;

	private String conversationID;

	private AgenteTrabalhador worker;
	private String workerName;
	private ACLMessage msgToSend;
	private ArrayList<AID> possibleWorkers;
	private ArrayList<AID> confirmedWorkers;
	private AID winnerWorker;
	private Service requestedService;


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
			System.out.println("[" + workerName + "] Proposta de trabalho a preco fixo em (" + requestedService.getName() + ") concluida.");
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
		msgToSend = new ACLMessage(ACLMessage.PROPOSE);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("WANT TO WORK ON FOR-" + requestedService.getName().toUpperCase());

		System.out.print("[" + workerName + "] Enviou proposta de trabalho a preco fixo em (" + requestedService.getName() + ") a: ");
		for(AID worker: possibleWorkers) {
			System.out.print("[" + (worker.getLocalName()) + "], ");
			msgToSend.addReceiver(worker);
		}
		System.out.println();
		myAgent.send(msgToSend);
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
									System.out.println("[" + workerName + "] Recebeu aceitacao de [" + possibleWorker.getLocalName() + "] para trabalhar a preco fixo em (" + requestedService.getName() + ")");
								}
								else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
									msgItem.remove();
									workerItem.remove();
									System.out.println("[" + workerName + "] Recebeu negacao de [" + possibleWorker.getLocalName() + "] para trabalhar a preco fixo em (" + requestedService.getName() + ")");
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
		msgToSend = new ACLMessage(ACLMessage.CONFIRM);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("SO DO JOB-" + requestedService.getName().toUpperCase());

		System.out.print("[" + workerName + "] Enviou confirmacao para trabalhar a preco fixo em (" + requestedService.getName() + ") a ");
		for(AID worker: confirmedWorkers) {
			System.out.print("[" + (worker.getLocalName() + "], "));
			msgToSend.addReceiver(worker);
		}
		System.out.println();
		myAgent.send(msgToSend);

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
									msgItem.remove();
									workerItem.remove();
									System.out.println("[" + workerName + "] Recebeu de [" + confirmedWorker.getLocalName() + "] trabalho a preco fixo em (" + requestedService.getName() + ") concluido ");
									break;
								}
								else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
									msgItem.remove();
									workerItem.remove();
									System.out.println("[" + workerName + "] Recebeu de [" + confirmedWorker.getLocalName() + "] trabalho a preco fixo em (" + requestedService.getName() + ") cancelado ");
								}
							}
						}
					}
				}
			}
		}
		else
			behaviourState = RequestPricedJobBehaviourState.REWARDING_WORKER;
	}

	public void rewardingWorkers() // SEND
	{
		// ------------ Paga o 1º agente que concluido trabalho ----------------------
		msgToSend = new ACLMessage(ACLMessage.CONFIRM);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("REWARD-" + 1000);

		System.out.println("[" + workerName + "] Enviou reconpensa a [" + winnerWorker.getLocalName() + "] do trabalho a preco fixo em (" + requestedService.getName() + ")");
		msgToSend.addReceiver(winnerWorker);
		myAgent.send(msgToSend);

		// ------------ Avisa outro agentes que o trabalho foi concluido ----------------------
		msgToSend = new ACLMessage(ACLMessage.CANCEL);
		msgToSend.setConversationId(conversationID);
		msgToSend.setContent("JOB ALLREADY DONE-" + 0);

		boolean cancelOthersWorkers = false;

		for(Iterator<AID> workerItem =  confirmedWorkers.iterator(); workerItem.hasNext();) {
			AID confirmedWorker = workerItem.next();
			msgToSend.addReceiver(confirmedWorker);
			cancelOthersWorkers = true;
		}

		if(cancelOthersWorkers) {
			myAgent.send(msgToSend);
			System.out.print("[" + workerName + "] Enviou cancelamento para trabalhar a preco fixo em (" + requestedService.getName() + ") a ");
			for(Iterator<AID> workerItem =  confirmedWorkers.iterator(); workerItem.hasNext();) {
				AID workerToCancel = workerItem.next();
				System.out.print("[" + (workerToCancel.getLocalName() + "], "));
				msgToSend.addReceiver(workerToCancel);
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

		behaviourState = RequestPricedJobBehaviourState.RECEIVING_PRODUCTS;
	}

	private void receivingProducts()
	{
		if(worker.socializer.haveReplyPendingMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getReplyPendingMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {					
					if(winnerWorker.getLocalName().equals(msg.getSender().getLocalName())) {
						if(msg.getPerformative() == ACLMessage.CONFIRM) {
							msgItem.remove();
							System.out.println("[" + workerName + "] Recebeu Produto de [" + winnerWorker.getLocalName() + "] do o trabalho a preco fixo em (" + requestedService.getName() + ")");
							behaviourState = RequestPricedJobBehaviourState.DONE;
						}
					}
				}
			}
		}
	}
}