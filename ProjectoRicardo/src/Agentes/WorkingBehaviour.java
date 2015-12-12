package Agentes;

import Agentes.AgenteTrabalhador.WorkingState;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.SimpleBehaviour;

public class WorkingBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1L;

	protected Service actualService;

	private AgenteTrabalhador worker;
	private String workerName;
	
	int qnt = 0;
	long delay = 1000;


	public WorkingBehaviour(AgenteTrabalhador worker, Service service) {
		//super(worker);
		this.actualService = service;
		this.worker = worker;
		this.workerName = worker.getLocalName();
		
		System.out.println("[" + workerName + "] Vai começar a trabalhar em " + actualService.getName());
	}

	@Override
	public void action() {
		//block(delay);
		System.out.print("[" + workerName + "] Estou a trabalhar em " + actualService.getName() + "! ");
		qnt = qnt + 1;
		System.out.println("Qnt: " + qnt);		
	}
	
	@Override
	public boolean done() {
		if(qnt == 5)
		{
			System.out.println("[" + workerName + "] Acabei a minha tarefa em " + actualService.getName());
			qnt = 0;
			worker.state = WorkingState.WAITING_FOR_JOB;
			worker.serviceManager.offerService(new Service("pedras", ""));
			actualService = null;
			return true;
		} else return false;
	}
}