package Agentes;

import sajas.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class RequestingBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;

	private AgenteTrabalhador worker;
	//private String workerName;
	private ACLMessage msg;
	private DFAgentDescription[] possibleWorkers;
	private Service requestedService;
	private boolean requestedServiceDone;


	public RequestingBehaviour(AgenteTrabalhador worker, Service requestedService) {
		//super(worker);
		this.worker = worker;
		//this.workerName = worker.getLocalName();
		this.requestedService = requestedService;
		this.requestedServiceDone = false;
	}

	@Override
	public void action() {

		possibleWorkers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers != null) {
			DFAgentDescription possibleWorker = possibleWorkers[0];

			worker.debug("Encontrou " + possibleWorker.getName().getLocalName() + " para servico " + requestedService.getName());

			msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.addReceiver(possibleWorker.getName());
			msg.setContent("DO JOB-" + requestedService.getName().toUpperCase());
			myAgent.send(msg);

			worker.debug("Enviou ordem de trabalho a " + possibleWorker.getName().getLocalName());

			requestedServiceDone = true;
		}
		block(1500);
	}
	
	@Override
	public boolean done() {
		if(requestedServiceDone)
		{
			worker.debug("ordem de trabalho enviada");
			return true;
		} else return false;
	}
}