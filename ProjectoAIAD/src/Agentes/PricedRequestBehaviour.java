package Agentes;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

public class PricedRequestBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;

	public enum RequestPricedJobBehaviourState{
		FINDING_AVAILABLE_WORKERS, ASKING_TO_WORK, CONFIRM_TO_WORK, RECEIVING_CONFIRMATION, 
		WAITING_FOR_WORKER, REWARDING_WORKER, RECEIVING_PRODUCTS, DONE, NO_WORKERS_FOUND
	}
	private RequestPricedJobBehaviourState behaviourState;

	private String conversationID;

	private AgenteTrabalhador worker;
	private String workerName;
	private ACLMessage msg;
	private ArrayList<AID> possibleWorkers;
	private ArrayList<AID> confirmedWorkers;
	private AID winnerWorker;
	private Service requestedService;
	private boolean requestedServiceDone;

	public PricedRequestBehaviour(AgenteTrabalhador worker, Service requestedService) {
		//super(worker);
		this.worker = worker;
		this.workerName = worker.getLocalName();
		this.requestedService = requestedService;
		this.requestedServiceDone = false;
		this.conversationID = System.currentTimeMillis()+"";
		this.possibleWorkers = new ArrayList<AID>();
		this.confirmedWorkers = new ArrayList<AID>();
	}

	private void findPossibleWorkers() {
		DFAgentDescription[] workers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers != null) {
			for(DFAgentDescription worker: workers)
				possibleWorkers.add(worker.getName());
			behaviourState = RequestPricedJobBehaviourState.ASKING_TO_WORK;
		}
	}

	private void askingToWork() {
		msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.setConversationId(conversationID);
		msg.setContent("DO JOB-" + requestedService.getName().toUpperCase());

		System.out.println("[" + workerName + "] Enviou proposta de trabalho a preco fixo a: ");
		for(AID worker: possibleWorkers) {
			System.out.print((worker.getName()) + ", ");
			msg.addReceiver(worker);
		}
		System.out.println();
		myAgent.send(msg);
	}

	private void receivingConfirmations() {
		if(!possibleWorkers.isEmpty()) {
			if(worker.socializer.havePendingReplyMsgs()) {
				for(Iterator<ACLMessage> msgItem =  worker.socializer.getPendingReplyMsgs().iterator(); msgItem.hasNext();) {
					ACLMessage msg = msgItem.next();
					if(msg.getConversationId().equals(conversationID)) {
						for(Iterator<AID> workerItem =  possibleWorkers.iterator(); workerItem.hasNext();) {
							AID possibleWorker = workerItem.next();
							if(possibleWorker.getName().equals(msg.getSender().getLocalName())) {
								if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
									confirmedWorkers.add(msg.getSender());
									msgItem.remove();
									workerItem.remove();
								}
								else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
									msgItem.remove();
									workerItem.remove();
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

	private void confirmToWork() {
		msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setConversationId(conversationID);
		msg.setContent("DO JOB-" + requestedService.getName().toUpperCase());

		System.out.println("[" + workerName + "] Enviou confirmação de trabalho a preco fixo a: ");
		for(AID worker: confirmedWorkers) {
			System.out.print((worker.getLocalName() + ", "));
			msg.addReceiver(worker);
		}
		System.out.println();
		myAgent.send(msg);
	}

	private void waitingForWorkers() {
		if(worker.socializer.havePendingReplyMsgs()) {
			for(Iterator<ACLMessage> msgItem =  worker.socializer.getPendingReplyMsgs().iterator(); msgItem.hasNext();) {
				ACLMessage msg = msgItem.next();
				if(msg.getConversationId().equals(conversationID)) {
					for(Iterator<AID> workerItem =  confirmedWorkers.iterator(); workerItem.hasNext();) {
						AID confirmedWorker = workerItem.next();
						if(confirmedWorker.getName().equals(msg.getSender().getLocalName())) {
							if(msg.getPerformative() == ACLMessage.CONFIRM) {
								winnerWorker = msg.getSender();
								msgItem.remove();
								workerItem.remove();
							}
							else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
								msgItem.remove();
								workerItem.remove();
							}
						}
					}
				}
			}
		}
		else
			behaviourState = RequestPricedJobBehaviourState.CONFIRM_TO_WORK;
	}
	
	public void RewardingWorkers() {
		msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.setConversationId(conversationID);
		msg.setContent("REWARD-" + 1000);

		System.out.println("[" + workerName + "] Enviou reconpenssa a: " + winnerWorker.getLocalName());
		msg.addReceiver(winnerWorker);
		myAgent.send(msg);
	}

	@Override
	public void action() {

		switch (behaviourState) {
		case FINDING_AVAILABLE_WORKERS:
			findPossibleWorkers();
			break;
		case ASKING_TO_WORK:
			askingToWork();
			break;
		case RECEIVING_CONFIRMATION:
			receivingConfirmations();
			break;
		case CONFIRM_TO_WORK:
			confirmToWork();
			break;
		case WAITING_FOR_WORKER:
			waitingForWorkers();
			break;
		case REWARDING_WORKER:
			RewardingWorkers();
			break;
		case RECEIVING_PRODUCTS:
			System.out.println("Received Products"); //Efeitos de teste
			behaviourState = RequestPricedJobBehaviourState.DONE;
		default:
			break;
		}
	}

	@Override
	public boolean done() {
		if(requestedServiceDone)
		{
			System.out.println("[" + workerName + "] ordem de trabalho enviada");
			return true;
		} else return false;
	}

}
