package Jade;

import java.util.ArrayList;

import Jade.Worker.WorkingSate;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class RequestingBehaviour extends OneShotBehaviour
{
	
	protected WorkingSate wstate;

	private ArrayList<Service> oferedServices;
	private Service actualService;

	ACLMessage msg;

	private Worker requester;
	private String workerName;
	
	public RequestingBehaviour(Worker agent, WorkingSate wstate, String job) {
		super(agent);
		this.wstate = wstate;
		this.requester = agent;
		this.workerName = agent.getLocalName();
		this.actualService = new Service(job, "");
	}
	
	@Override
	public void action() {

		DFAgentDescription[] workers = requester.serviceManager.procuraServico(actualService);
		
		if(workers != null) {
			DFAgentDescription worker = workers[0];
			
			System.out.println("[" + workerName + "] Encontrou " + worker.getName().getLocalName());
			
			msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(worker.getName());
			msg.setContent(actualService.getName());
			myAgent.send(msg);
			
			System.out.println("[" + workerName + "] Enviou ordem de trabalho a " + worker.getName().getLocalName());
		}
	}
}