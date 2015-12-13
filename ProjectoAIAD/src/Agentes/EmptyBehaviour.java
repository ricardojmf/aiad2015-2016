package Agentes;

import sajas.core.behaviours.Behaviour;

public class EmptyBehaviour extends Behaviour
{
	boolean terminado;
	
	public EmptyBehaviour()
	{
		terminado = false;
	}
	@Override
	public void action() {
		terminado = true;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return terminado;
	}

}
