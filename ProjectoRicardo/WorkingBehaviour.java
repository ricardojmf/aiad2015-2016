package jade;

import messages.*;
import messages.AgentMessage.messageType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class WorkingBehaviour extends Behaviour{
	
	public enum WorkingBehaviourState{
		WORKING, RECEIVING_CONFIRMATION, FINNISH_COMUNICATION, DELIVERING_PRODUCTS, DONE, WAITING_FOR_WORK, FIRST_COMUNICATION
	}
	
	private WorkingBehaviourState workingState;
	private Job job;
	private AID requestWorker;
	private Object[] products;
	private boolean makingProducts;
	
	public WorkingBehaviour(Agent myAgent, boolean makingProducts) {
		super(myAgent);
		this.workingState = WorkingBehaviourState.WAITING_FOR_WORK;
		this.job = new Job("Transport", "50::Wood::WarehouseOne");
		this.makingProducts = makingProducts;
		registWork(job);
	}
	
	@Override
	public void action() {
		switch (workingState) {
		case WAITING_FOR_WORK:
			waitingForWork();
			break;			
		case WORKING:
			working();
			break;
		case FINNISH_COMUNICATION:
			finishComunication();
			break;
		case DELIVERING_PRODUCTS:
			deliveringProducts();
			break;
		case RECEIVING_CONFIRMATION:
			receivingConfirmation();
			break;
		default:
			break;
		}
	}
	
	private void waitingForWork(){
		ACLMessage message = myAgent.receive();
		if(message != null)
		{
			System.out.println("RECEIVE: " + message);		
			String messageParts[] = message.getContent().split("-");			
			if(messageParts[0].equals("REQUEST_JOB") && 
			   messageParts[1].equals(job.jobType))
			{
				sendReply();
				requestWorker = message.getSender();
				workingState = WorkingBehaviourState.WORKING;
			}
		} else block();
	}
	
	private void sendReply()
	{
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(requestWorker);
		message.setContent(new ReplyMessage("YES-JOB").toString());
		myAgent.send(message);
	}
	
	private void registWork(Job job)
	{
		ServiceDescription service = new ServiceDescription();
		service.setType(job.jobType);
		service.setName(myAgent.getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(service);
		try {
			DFService.register(myAgent, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	private void working(){
		if(job.work(this))
			workingState = WorkingBehaviourState.FINNISH_COMUNICATION;
	}
	
	private void finishComunication(){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(requestWorker);
		message.setContent(new ReplyMessage("DONE-JOB").toString());
		myAgent.send(message);
		if(makingProducts)
			workingState = WorkingBehaviourState.DELIVERING_PRODUCTS;
		else workingState = WorkingBehaviourState.DONE;
	}
	
	private void deliveringProducts(){
		// COMPLETAR DEPOIS
	}
	
	private void receivingConfirmation(){
		ACLMessage message = myAgent.receive();
		if(message != null)
		{
			System.out.println("RECEIVE: " + message);		
			String messageParts[] = message.getContent().split("-");			
			if(messageParts[0].equals("REPLY_MESSAGE") && 
			   message.getSender().getLocalName().equals(requestWorker.getLocalName()) && 
			   messageParts[1].equals("YES"))
				workingState = WorkingBehaviourState.DONE;	
		} else block();
	}

	@Override
	public boolean done() {
		if(workingState.equals(WorkingBehaviourState.DONE))
		{
			System.out.println("Finished Job");
			return true;
		}
		else return false;
	}
}
