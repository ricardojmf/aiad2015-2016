package Agentes;

import java.util.Vector;

import sajas.core.behaviours.ParallelBehaviour;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.core.behaviours.Behaviour;

public class MySequentialBehaviour extends SequentialBehaviour
{
	Vector<Behaviour> lista;
	AgenteTrabalhador agente;
	
	public int onEnd()
	{
		return 0;
		
	}
	
	public MySequentialBehaviour(AgenteTrabalhador ag, Vector<Behaviour> tarefas)
	{
		lista = tarefas;
		agente = ag;
		
		for(Behaviour b: lista){
			addSubBehaviour(b);
		}
	}	
}
