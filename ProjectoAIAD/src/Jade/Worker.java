package Jade;

import java.util.ArrayList;

import jade.core.Agent;

public class Worker extends Agent
{
	public enum WorkingSate{
		WAITING_FOR_JOB, PREPARE_TO_WORK, WORKING, CHARGING_BATTERY
	}

	protected WorkingSate wstate;
	protected String workerType, job, workerName;
	protected ServiceManager serviceManager;
	
	protected ArrayList<Service> oferedServices;

	@Override
	protected void setup() {

		this.oferedServices = new ArrayList<Service>();
		this.serviceManager = new ServiceManager(this);

		Object[] args = getArguments();
		workerName = this.getLocalName();

		if(args != null && args.length > 0)
		{
			String arguments = (String)args[0];
			String[] parts = arguments.split("-");

			this.workerType = (String) parts[0];
			this.job = (String) parts[1];
			
			Service service = new Service(job, "");

			switch (workerType)
			{
			case "w":
				wstate = WorkingSate.WAITING_FOR_JOB;
				serviceManager.addService(service);
				addBehaviour(new WorkingBehaviour(this, wstate, job));
				break;

			case "b":
				addBehaviour(new RequestingBehaviour(this, wstate, job));
				break;

			default:
				break;
			}
		}
		else
			System.exit(0);
	}

	
}