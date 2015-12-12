package Agentes;

import Logica.Producto;
import sajas.core.behaviours.Behaviour;

public class BuyingBehaviour extends Behaviour
{
	AgenteTrabalhador agente;
	
	public BuyingBehaviour(AgenteTrabalhador at, Producto p, int quantidade)
	{
		agente = at;
	}
	
	@Override
	public void action() {
		
	}

	@Override
	public boolean done() {
		
		return false;
	}

}
