package Agentes;

import java.util.Vector;

import sajas.core.behaviours.ParallelBehaviour;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.core.behaviours.SimpleBehaviour;

public class MySequentialBehaviour extends SequentialBehaviour
{
	Vector<SimpleBehaviour> lista;
	AgenteTrabalhador agente;
	
	public int onEnd()
	{
		return 0;
		
	}
	
	public MySequentialBehaviour(AgenteTrabalhador ag, Vector<SimpleBehaviour> tarefas)
	{
		lista = tarefas;
		agente = ag;
		
		for(SimpleBehaviour b: lista){
			addSubBehaviour(b);
		}
	}	
}
