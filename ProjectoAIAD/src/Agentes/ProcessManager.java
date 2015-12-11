package Agentes;

import Agentes.AgenteTrabalhador.WorkingState;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
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
		checkForPendingMessages();
		//block(1000);
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
	
	protected void checkForPendingMessages()
	{
		if (worker.socializer.havePendingProposeMsgs())
		{
			parseReceivedMsg(worker.socializer.get1stPendingMsg());
		}
	}
	
	protected void waitingForJob()
	{
		//System.out.println("[" + workerName + "] Esta a espera de emprego");
		
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
	
	public void parseReceivedMsg(ACLMessage aclMessage)
	{		
		String receivedMsg = aclMessage.getContent().toString();
		System.out.println("[" + workerName + "] Parsing mensagem de [" + aclMessage.getSender().getLocalName() + "] dizendo: " + receivedMsg);

		if(receivedMsg.contains("DO JOB"))		// proposta de encomenda
		{	
			String[] parts = receivedMsg.split("-");

			String requestedServiceName = parts[1];
			String requestedServiceType = "";
			Service job = new Service(requestedServiceName, requestedServiceType);

			if(worker.serviceManager.canDoService(job))
			{
				System.out.println("[" + workerName + "] Recebeu ordem de encomenda de " + aclMessage.getSender().getLocalName() + " para servico " + job.getName());

				replyOK(aclMessage.getSender(), receivedMsg);
				worker.serviceManager.addJobToDo(job);
			}
			else
				replyNO(aclMessage.getSender(), receivedMsg);
		}
		else if(receivedMsg.contains("WANT TO WORK ON FOR")) {		// proposta a preço fixo
			String[] parts = receivedMsg.split("-");

			String requestedServiceName = parts[1];
			String requestedServiceType = "";
			Service job = new Service(requestedServiceName, requestedServiceType);

			if(worker.serviceManager.canDoService(job))
			{
				System.out.println("[" + workerName + "] Recebeu proposta de trabalho a preco fixo de " + aclMessage.getSender().getLocalName() + " para servico " + job.getName());

				replyOK(aclMessage.getSender(), receivedMsg);
				worker.serviceManager.addJobToDo(job);
			}
			else
				replyNO(aclMessage.getSender(), receivedMsg);
		}
		else if(receivedMsg.contains("WANT TO WORK ON BID")) {		// proposta a leilao
			String[] parts = receivedMsg.split("-");

			String requestedServiceName = parts[1];
			String requestedServiceType = "";
			Service job = new Service(requestedServiceName, requestedServiceType);

			if(worker.serviceManager.canDoService(job))
			{
				System.out.println("[" + workerName + "] Recebeu proposta de trabalho a leilao de " + aclMessage.getSender().getLocalName() + " para servico " + job.getName());

				replyOK(aclMessage.getSender(), receivedMsg);
				worker.serviceManager.addJobToDo(job);
			}
			else
				replyNO(aclMessage.getSender(), receivedMsg);
		}
	}

	public void replyOK(AID agent, String replyMsg) {
		ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
		msg.addReceiver(agent);
		msg.setContent(replyMsg);
		myAgent.send(msg);
	}

	public void replyNO(AID agent, String replyMsg) {
		ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
		msg.addReceiver(agent);
		msg.setContent(replyMsg);
		myAgent.send(msg);
	}

	public void replyInform(AID agent, String replyMsg) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(agent);
		msg.setContent(replyMsg);
		myAgent.send(msg);
	}
}