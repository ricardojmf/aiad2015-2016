package Jade;
import java.util.ArrayList;
import java.util.Iterator;

import Jade.Worker.WorkingSate;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

public class WorkingBehaviour extends CyclicBehaviour {

	protected WorkingSate wstate;

	protected ArrayList<Service> oferedServices;
	protected Service actualService;

	ACLMessage msg;

	protected Worker worker;
	protected String workerName;


	public WorkingBehaviour(Worker agent, WorkingSate wstate, String job) {
		super(agent);
		this.wstate = wstate;
		this.oferedServices = agent.oferedServices;
		this.actualService = null;
		this.worker = agent;
		this.workerName = worker.getLocalName();
	}

	@Override
	public void action() {

		switch (wstate) {
		case WAITING_FOR_JOB:
			waitingForJob();
			break;
		case PREPARE_TO_WORK:
			working();
		case WORKING:
			break;
		default:
			break;
		}
	}

	protected void waitingForJob()
	{
		System.out.println("[" + workerName + "] Estou a espera de emprego ");

		msg = myAgent.receive();

		if(msg != null)
		{
			System.out.print("[" + workerName + "] Recebi uma mensagem de [" + msg.getSender().getLocalName() + "] dizendo: ");
			System.out.println(msg.getContent().toString());
			
			String requestedServiceName = msg.getContent().toString();
			String requestedServiceType = "";
			Service requestedService = new Service(requestedServiceName, requestedServiceType);
			
			Boolean job = false;
			for(Iterator<Service> item = oferedServices.iterator(); item.hasNext(); ) {
				Service servico = item.next();
				if(servico.equals(requestedService)) {
					job = true;
					break;
				}
			}

			if(job)
			{
				System.out.println("[" + workerName + "] Recebi ordem de trabalho de " + msg.getSender().getLocalName() + " para servico " + requestedService.getName());
				actualService = requestedService;
				this.wstate = WorkingSate.PREPARE_TO_WORK;
			}
		} else {
			block();
		}
	}

	protected void working()
	{
		System.out.println("[" + workerName + "] Vou começar a trabalhar");

		this.wstate = WorkingSate.WORKING;

		myAgent.addBehaviour(new Behaviour(myAgent) {

			int qnt = 0;
			long delay = 1000;

			@Override
			public boolean done() {
				if(qnt == 5)
				{
					System.out.println("[" + workerName + "] Acabei a minha tarefa!");
					qnt = 0;
					wstate = WorkingSate.WAITING_FOR_JOB;
					worker.serviceManager.addService(new Service("pedras", ""));
					actualService = null;
					return true;
				} else return false;
			}

			@Override
			public void action() {
				block(delay);
				System.out.print("[" + workerName + "] Estou a servir em " + actualService.getName() + "! ");
				qnt = qnt + 1;
				System.out.println("Qnt: " + qnt);
			}
		});		
	}
}