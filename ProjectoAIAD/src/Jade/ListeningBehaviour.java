package Jade;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ListeningBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private Worker worker;
	private ACLMessage aclMessage;
	private String workerName;
	
	
	public ListeningBehaviour(Worker worker) {
		this.worker = worker;
		this.workerName = worker.getLocalName();
	}

	@Override
	public void action() {
		System.out.println("[" + workerName + "] Listening... ");

		aclMessage = myAgent.receive();

		if(aclMessage != null)
		{
			String receivedMsg = aclMessage.getContent().toString();
			System.out.println("[" + workerName + "] Recebeu uma mensagem de [" + aclMessage.getSender().getLocalName() + "] dizendo: " + receivedMsg);
						
			parseReceivedMsg(receivedMsg.toUpperCase());
		}
		else {
			block();
		}
	}
	
	public void parseReceivedMsg(String receivedMsg) {
		
		if(receivedMsg.contains("DO JOB")) {
			
			String[] parts = receivedMsg.split("-");
			
			String requestedServiceName = parts[1];
			String requestedServiceType = "";
			Service job = new Service(requestedServiceName, requestedServiceType);

			if(worker.serviceManager.canDoService(job))
			{
				System.out.println("[" + workerName + "] Recebeu ordem de trabalho de " + aclMessage.getSender().getLocalName() + " para servico " + job.getName());
				
				worker.serviceManager.addJobToDo(job);
			}			
		}		
	}	
}