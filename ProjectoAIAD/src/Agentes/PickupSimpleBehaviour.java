package Agentes;

import Logica.*;
import sajas.core.behaviours.Behaviour;

public class PickupSimpleBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;
	
	AgenteTrabalhador agente;
	Armazem armazem;
	Ranhura productos;
	
	boolean terminado;
	
	public PickupSimpleBehaviour(AgenteTrabalhador at, Armazem ar, Producto p, int quantidade)
	{
		agente = at;
		armazem = ar;
		
		terminado = false;
		productos = new Ranhura(p, quantidade);
	}
	
	public PickupSimpleBehaviour(AgenteTrabalhador at, Armazem ar, Ranhura ra)
	{
		
		agente = at;
		armazem = ar;
		
		terminado = false;
		productos = ra;
	}
	
	@Override
	public void action()
	{
		if(agente.tr.mesmaPosicao(armazem))
		{
			boolean estado = agente.tr.removerArmazem(armazem, productos);
			
			terminado = true;
		}
	}

	@Override
	public boolean done()
	{
		armazem.verDetalhes();
		agente.tr.verContentor();
		return terminado;
	}
	
}
