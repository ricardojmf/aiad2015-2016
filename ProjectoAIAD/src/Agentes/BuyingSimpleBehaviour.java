package Agentes;

import Logica.*;
import sajas.core.behaviours.Behaviour;

public class BuyingSimpleBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;
	
	AgenteTrabalhador agente;
	boolean terminado;
	
	Loja loja;
	Ranhura productos;
	
	public BuyingSimpleBehaviour(AgenteTrabalhador at, Loja l, Producto p, int q)
	{
		agente = at;
		terminado = false;
		
		productos = new Ranhura(p, q);
	}
	
	public BuyingSimpleBehaviour(AgenteTrabalhador at, Loja l, Ranhura ra)
	{
		agente = at;
		terminado = false;
		
		productos = ra;
	}
	
	public void action()
	{
		if(agente.tr.mesmaPosicao(loja))
		{
			//agente.tr.comprar(loja, productos.producto, productos.quantidade);
			
			terminado = true;
		}	
	}

	public boolean done()
	{
		
		return terminado;
	}

}
