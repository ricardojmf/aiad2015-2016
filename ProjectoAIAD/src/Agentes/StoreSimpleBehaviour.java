package Agentes;

import Logica.*;
import sajas.core.behaviours.Behaviour;

public class StoreSimpleBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;
	
	AgenteTrabalhador agente;
	Armazem armazem;
	Ranhura productos;
	
	boolean terminado;
	
	public StoreSimpleBehaviour(AgenteTrabalhador at, Armazem ar, Producto p, int quantidade)
	{
		agente = at;
		armazem = ar;
		
		terminado = false;
		productos = new Ranhura(p, quantidade);
	}
	
	public StoreSimpleBehaviour(AgenteTrabalhador at, Armazem ar, Ranhura ra)
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
			//agente.tr.armazenar(armazem, productos.producto, productos.quantidade);
			
			terminado = true;
		}
	}

	@Override
	public boolean done()
	{
		return terminado;
	}
	
}
