package Jade;

import jade.core.behaviours.ParallelBehaviour;

public class BasicWorkerBehaviour extends ParallelBehaviour {

	private static final long serialVersionUID = 1L;
	private Worker worker;
	private String workerName;

	
	public BasicWorkerBehaviour(Worker worker) {
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