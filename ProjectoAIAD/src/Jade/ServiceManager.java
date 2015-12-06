package Jade;

import java.util.ArrayList;
import java.util.Iterator;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ServiceManager {
	
	protected ArrayList<Service> oferedServices;
	protected Worker worker;
	protected String workerName;
	
	public ServiceManager(Worker agent) {
		this.worker = agent;
		this.workerName = agent.getLocalName();
		this.oferedServices = agent.oferedServices;
	}

	// ======================================  Services  =======================================

		public String oferedServicesToString() {
			String result = "";
			for(Iterator<Service> item = oferedServices.iterator(); item.hasNext(); ) {
				Service servico = item.next();
				result = result + servico.getName() + ", ";
			}
			return result;
		}

		public Boolean addService(Service newService) {
			Boolean alreadyExisting = false;
			for(Iterator<Service> item = oferedServices.iterator(); item.hasNext(); ) {
				Service servico = item.next();
				if(servico.equals(newService))
					alreadyExisting = true;
			}
			if(!alreadyExisting) {
				oferedServices.add(newService);
				deRegistaTodosServicos();
				return registaServicos(oferedServices);
			}
			return true;
		}

		public void removeService(Service oldService)
		{
			for(Iterator<Service> item = oferedServices.iterator(); item.hasNext(); ) {
				Service servico = item.next();
				if(servico.equals(oldService)) {
					item.remove();
					break;
				}
			}
			if(oferedServices.size() != 0) {
				deRegistaTodosServicos();
				registaServicos(oferedServices);
			}
			else
				deRegistaTodosServicos();
		}

		public void removeAllServices()
		{	
			deRegistaTodosServicos();
		}

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
					System.out.println("[" + workerName + "] Encontrou " + resultado.length + " agente(s) que oferece(m) " + servico.getName());
					return resultado;
				}
				else
				{
					System.out.println("[" + workerName + "] Nao encontrou nenhum agente que oferece " + servico.getName());
					return null;
				}
			} catch (FIPAException e) {
				System.err.println("[" + workerName + "] Não consegui pesquisar agentes que oferecem " + servico.getName());
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
				System.out.println("[" + workerName + "] Registou-se para oferecer " + oferedServicesToString());
				return true;
			} catch (FIPAException e) {
				System.err.println("[" + workerName + "] Não consegui registar-se para oferecer " + oferedServicesToString());
			}
			return false;
		}

		private void deRegistaTodosServicos()
		{
			try {
				DFService.deregister(worker);
			} catch (FIPAException e) {
				System.err.println("[" + workerName + "] Ainda não estava registado para oferecer qualquer servico");
			}
		}
}
