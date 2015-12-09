package Agentes;

import sajas.core.behaviours.ParallelBehaviour;

public class BasicWorkerBehaviour extends ParallelBehaviour {

	private static final long serialVersionUID = 1L;
	private AgenteTrabalhador worker;
	private String workerName;

	
	public BasicWorkerBehaviour(AgenteTrabalhador worker) {
		this.worker = worker;
		this.workerName = worker.getLocalName();
		
		this.addSubBehaviour(new ListeningBehaviour(this.worker));
		this.addSubBehaviour(new ProcessManager(this.worker));
	}

	public int onEnd() {
		System.out.println("[" + workerName + "] Comportamento Composto Finalizado com Sucesso!");
		return 0;
	}
}