package Agentes;

import Agentes.AgenteTrabalhador.WorkingState;
import sajas.core.behaviours.CyclicBehaviour;

public class ProcessManager extends CyclicBehaviour {
	
	private static final long serialVersionUID = 1L;
	private AgenteTrabalhador worker;
	private String workerName;


	public ProcessManager(AgenteTrabalhador worker) {
		//super(worker);
		this.worker = worker;
		this.workerName = worker.getLocalName();
	}

	@Override
	public void action() {
		
		switch (worker.state) {
		case WAITING_FOR_JOB:
			break;
		case PREPARE_TO_WORK:
			prepareToWork();
			break;
		case WORKING:
			break;
		case CHARGING_BATTERY:
			break;
		case MOVING:
			break;
		default:
			break;
		}

		waitingForJob();
		checkForRequestingServices();
		block(1000);
	}
	
	protected void waitingForJob()
	{
		System.out.println("[" + workerName + "] Esta a espera de emprego");
		
		if(worker.serviceManager.haveJobsToDo())
		{
			System.out.println("[" + workerName + "] A preparar para trabalhar em " + worker.serviceManager.get1stJobToDo().getName());
			worker.state = WorkingState.PREPARE_TO_WORK;
		}
	}
	
	protected void prepareToWork()
	{
		Service jobService = worker.serviceManager.get1stJobToDo();
		worker.serviceManager.remove1stJobToDo();
		worker.state = WorkingState.WORKING;
		myAgent.addBehaviour(new WorkingBehaviour(worker, jobService));
	}
	
	protected void checkForRequestingServices()
	{
		if (worker.serviceManager.wantToRequestService())
		{
			Service requestedService = worker.serviceManager.get1stRequestedService();
			worker.serviceManager.remove1stRequestedService();
			myAgent.addBehaviour(new RequestingBehaviour(worker, requestedService));
		}
	}
}