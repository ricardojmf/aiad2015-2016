package Agentes;

import java.util.ArrayList;
import java.util.Iterator;

import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import sajas.domain.DFService;

public class ServiceManager {

	private AgenteTrabalhador worker;
	//private String workerName;
	private ArrayList<Service> offeredServices;
	private ArrayList<Service> requestedServices;
	private ArrayList<Service> jobsToDo;

	public ServiceManager(AgenteTrabalhador worker) {
		this.worker = worker;
		//this.workerName = worker.getLocalName();
		this.offeredServices = new ArrayList<Service>();
		this.requestedServices = new ArrayList<Service>();
		this.jobsToDo = new ArrayList<Service>();
	}

	// ======================================  Requested Jobs To Do  =======================================

	public void addJobToDo(Service service) {
		jobsToDo.add(service);
	}
	
	public boolean haveJobsToDo() {
		return !jobsToDo.isEmpty();
	}
	
	public Service get1stJobToDo() {
		if(!jobsToDo.isEmpty())
			return jobsToDo.get(0);
		return null;
	}
	
	public void remove1stJobToDo() {
		if(!jobsToDo.isEmpty())
			jobsToDo.remove(0);
	}

	// ======================================  Requested Services  =======================================
	
	public void requestService(Service service) {
		requestedServices.add(service);
	}
	
	public boolean wantToRequestService() {
		return !requestedServices.isEmpty();
	}
	
	public Service get1stRequestedService() {
		if(!requestedServices.isEmpty())
			return requestedServices.get(0);
		return null;
	}
	
	public void remove1stRequestedService() {
		if(!requestedServices.isEmpty())
			requestedServices.remove(0);
	}
	
	// ======================================  Offered Services  =======================================
	
	public Boolean canDoService(Service service) {
		Boolean haveService = false;
		for(Iterator<Service> item = offeredServices.iterator(); item.hasNext(); ) {
			Service servico = item.next();
			if(servico.equals(service)) {
				haveService = true;
				break;
			}
		}
		return haveService;
	}

	public String oferedServicesToString() {
		String result = "";
		for(Iterator<Service> item = offeredServices.iterator(); item.hasNext(); ) {
			Service servico = item.next();
			result = result + servico.getName() + ", ";
		}
		return result;
	}

	public Boolean offerService(Service newService) {
		Boolean alreadyExisting = false;
		for(Iterator<Service> item = offeredServices.iterator(); item.hasNext(); ) {
			Service servico = item.next();
			if(servico.equals(newService))
				alreadyExisting = true;
		}
		if(!alreadyExisting) {
			offeredServices.add(newService);
			deRegistaTodosServicos();
			return registaServicos(offeredServices);
		}
		return true;
	}

	public void removeOfferedService(Service oldService)
	{
		for(Iterator<Service> item = offeredServices.iterator(); item.hasNext(); ) {
			Service servico = item.next();
			if(servico.equals(oldService)) {
				item.remove();
				break;
			}
		}
		if(offeredServices.size() != 0) {
			deRegistaTodosServicos();
			registaServicos(offeredServices);
		}
		else
			deRegistaTodosServicos();
	}

	public void removeAllOfferedServices()
	{	
		deRegistaTodosServicos();
	}
	
	// ======================================  FIPA Services DF Operators  =======================================

	protected DFAgentDescription[] procuraServico(Service servico)
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setName(servico.getName());
		sd.setType(servico.getType());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);

		try {
			DFAgentDescription[] resultado = DFService.search(worker, dfd);
			if(resultado.length != 0)
			{
				worker.debug("Encontrou " + resultado.length + " agente(s) que oferece(m) " + servico.getName());
				return resultado;
			}
			else
			{
				worker.debug("Nao encontrou nenhum agente que oferece " + servico.getName());
				return null;
			}
		} catch (FIPAException e) {
			worker.debug("Não consegui pesquisar agentes que oferecem " + servico.getName());
		}
		return null;
	}

	private Boolean registaServicos(ArrayList<Service> oferedServices)
	{
		DFAgentDescription dfd = new DFAgentDescription();

		for(Iterator<Service> item = oferedServices.iterator(); item.hasNext(); ) {
			Service servico = item.next();
			ServiceDescription sd = new ServiceDescription();
			sd.setName(servico.getName());
			sd.setType(servico.getType());
			dfd.addServices(sd);
		}

		try {
			DFService.register(worker, dfd);
			worker.debug("Registou-se para oferecer " + oferedServicesToString());
			return true;
		} catch (FIPAException e) {
			worker.debug("Não consegui registar-se para oferecer " + oferedServicesToString());
		}
		return false;
	}

	private void deRegistaTodosServicos()
	{
		try {
			DFService.deregister(worker);
		} catch (FIPAException e) {
			worker.debug("Ainda não estava registado para oferecer qualquer servico");
		}
	}
}
