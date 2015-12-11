package jade;

import jade.core.Agent;


public class WorkingAgent extends Agent{
	
	@Override
	protected void setup() {	
		addBehaviour(new WorkingBehaviour(this, false));
	}

}
