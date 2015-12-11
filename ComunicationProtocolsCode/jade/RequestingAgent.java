package jade;

import jade.core.Agent;


public class RequestingAgent extends Agent{
	
	
	@Override
	protected void setup() {	
		
		addBehaviour(new RequestingJobBehaviour(this, false));	
		
	}
}

