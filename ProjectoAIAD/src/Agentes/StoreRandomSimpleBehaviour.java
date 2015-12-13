package Agentes;

import java.util.Vector;

import Logica.*;
import sajas.core.behaviours.Behaviour;

public class StoreRandomSimpleBehaviour extends Behaviour
{
	private static final long serialVersionUID = 1L;
	
	AgenteTrabalhador agente;
	Armazem armazem;
	Ranhura productos;
	
	boolean terminado;
	
	public StoreRandomSimpleBehaviour(AgenteTrabalhador at, Armazem ar, Producto p, int quantidade)
	{
		agente = at;
		armazem = ar;
		
		terminado = false;
		productos = new Ranhura(p, quantidade);
	}
	
	public StoreRandomSimpleBehaviour(AgenteTrabalhador at, Armazem ar, Ranhura ra)
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
			Vector<Ranhura> todosProductosNaoGuardar = new Vector<Ranhura>();
			todosProductosNaoGuardar.addElement(productos);
			
			agente.tr.armazenarAleatoriamente(armazem, todosProductosNaoGuardar);
			
			terminado = true;
		}
	}

	@Override
	public boolean done()
	{
		return terminado;
	}
	
}
