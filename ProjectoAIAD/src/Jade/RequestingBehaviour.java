package Jade;

import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class RequestingBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;

	private Worker worker;
	private String workerName;
	private ACLMessage msg;
	private DFAgentDescription[] possibleWorkers;
	private DFAgentDescription selectedWorker;
	private Service requestedService;
	private boolean requestedServiceDone;


	public RequestingBehaviour(Worker worker, Service requestedService) {
		super(worker);
		this.worker = worker;
		this.workerName = worker.getLocalName();
		this.requestedService = requestedService;
		this.requestedServiceDone = false;
	}

	@Override
	public void action() {

		possibleWorkers = worker.serviceManager.procuraServico(requestedService);

		if(possibleWorkers != null) {
			selectedWorker = possibleWorkers[0];

			System.out.println("[" + workerName + "] Encontrou " + selectedWorker.getName().getLocalName() + " para servico " + requestedService.getName());

			msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(selectedWorker.getName());
			msg.setContent("DO JOB-" + requestedService.getName().toUpperCase());
			myAgent.send(msg);

			System.out.println("[" + workerName + "] Enviou ordem de trabalho a " + selectedWorker.getName().getLocalName());

			requestedServiceDone = true;
		}
		block(1500);
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