package Agentes;

import Agentes.AgenteTrabalhador.WorkingState;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.core.AID;
import sajas.core.behaviours.CyclicBehaviour;

public class ProcessManager extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private AgenteTrabalhador worker;
	//private String workerName;


	public ProcessManager(AgenteTrabalhador worker) {
		//super(worker);
		this.worker = worker;
		//this.workerName = worker.getLocalName();
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

			if(requestedService.getMsg().contains("I ORDER WORK"))
				myAgent.addBehaviour(new RequestingBehaviour(worker, requestedService));
			else if(requestedService.getMsg().contains("DO WORK ON FOR"))
				myAgent.addBehaviour(new SimpleRequestBehaviour(worker, requestedService));
			else if(requestedService.getMsg().contains("WANT TO WORK ON FOR"))
				myAgent.addBehaviour(new PricedRequestBehaviour(worker, requestedService));
			//else if(requestedService.getMsg().contains("WANT TO WORK ON BID"))
			//myAgent.addBehaviour(new BidRequestBehaviour(worker, requestedService));
		}
	}

	protected void checkForPendingMessages()
	{
		if (worker.socializer.haveProposePendingMsgs())
		{
			parseReceivedMsg(worker.socializer.get1stProposePendingMsg());
		}
	}

	protected void waitingForJob()
	{
		//System.out.println("[" + workerName + "] Esta a espera de emprego");

		if(worker.serviceManager.haveJobsToDo())
		{
			worker.debug("A preparar para trabalhar em " + worker.serviceManager.get1stJobToDo().getName());
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
		worker.debug("Parsing mensagem de [" + aclMessage.getSender().getLocalName() + "]");

		Service job = null;
		try {
			job = (Service) aclMessage.getContentObject();
			worker.debug("Servico de [" + aclMessage.getSender().getLocalName() + "] pedindo: " + job.getName());
		} catch (UnreadableException e) {
			worker.debug("ERRO AO DESERIALIZAR O SERVICO A RECEBER DE [" + aclMessage.getSender().getLocalName() + "]");
		}

		if(job != null) {
			if(job.getMsg().contains("DO WORK ON FOR"))		// proposta de encomenda
			{
				if(worker.serviceManager.canDoService(job))
				{
					worker.debug("Recebeu ordem de encomenda de [" + aclMessage.getSender().getLocalName() + "] para servico (" + job.getName() + ")");
					worker.addBehaviour(new SimpleWorkBehaviour(worker, job, aclMessage.getConversationId(), aclMessage.getSender()));
					//worker.serviceManager.addJobToDo(job);
				}
				else
					worker.socializer.send(ACLMessage.REJECT_PROPOSAL, aclMessage.getSender(), aclMessage.getConversationId(), "CANT DO SERVICE");			
			}
			else if(job.getMsg().contains("WANT TO WORK ON FOR"))		// proposta a preço fixo
			{
				if(worker.serviceManager.canDoService(job))
				{
					worker.debug("Recebeu proposta de trabalho a preco fixo de [" + aclMessage.getSender().getLocalName() + "] para servico (" + job.getName() + ")");
					worker.addBehaviour(new PricedWorkBehaviour(worker, job, aclMessage.getConversationId(), aclMessage.getSender()));
					//worker.serviceManager.addJobToDo(job);
				}
				else
					worker.socializer.send(ACLMessage.REJECT_PROPOSAL, aclMessage.getSender(), aclMessage.getConversationId(), "CANT DO SERVICE");
			}
			else if(job.getMsg().contains("WANT TO WORK ON BID"))		// proposta a leilao
			{
				if(worker.serviceManager.canDoService(job))
				{
					worker.debug("Recebeu proposta de trabalho a leilao de [" + aclMessage.getSender().getLocalName() + "] para servico (" + job.getName() + ")");
					//worker.addBehaviour(new BidedWorkBehaviour(worker, job, aclMessage.getConversationId(), aclMessage.getSender()));
					//worker.serviceManager.addJobToDo(job);
				}
				else
					worker.socializer.send(ACLMessage.REJECT_PROPOSAL, aclMessage.getSender(), aclMessage.getConversationId(), "CANT DO SERVICE");
			}
			else
				worker.debug("Mensagen de [" + aclMessage.getSender().getLocalName() + "] nao entendida");
		}
	}
}