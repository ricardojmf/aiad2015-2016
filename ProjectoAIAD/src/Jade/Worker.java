package Jade;

import jade.core.Agent;

public class Worker extends Agent
{
	private static final long serialVersionUID = 1L;

	public enum WorkingState{
		WAITING_FOR_JOB, PREPARE_TO_WORK, WORKING, CHARGING_BATTERY, MOVING
	}

	protected WorkingState state;
	protected String workerType;
	protected ServiceManager serviceManager;

	@Override
	protected void setup() {

		this.serviceManager = new ServiceManager(this);

		Object[] args = getArguments();

		if(args != null && args.length > 0)
		{
			String arguments = (String)args[0];
			String[] parts = arguments.split("-");

			this.workerType = (String) parts[0];
			String job = (String) parts[1];
			
			Service service = new Service(job, "");
			
			switch (workerType)
			{
			case "w":
				serviceManager.offerService(service);
				state = WorkingState.WAITING_FOR_JOB;
				//addBehaviour(new WorkingBehaviour(this, state, job));
				break;

			case "b":
				serviceManager.requestService(service);
				state = WorkingState.WAITING_FOR_JOB;
				//addBehaviour(new RequestingBehaviour(this, state, job));
				break;

			default:
				break;
			}

			addBehaviour(new BasicWorkerBehaviour(this));
		}
		else
			System.exit(0);
	}
}