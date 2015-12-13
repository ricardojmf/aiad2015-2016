package Agentes;

import Logica.Produzir;
import sajas.core.behaviours.SimpleBehaviour;

public class ProduceBehaviour extends SimpleBehaviour
{
	AgenteTrabalhador at;
	Produzir producao;
	
	boolean terminado;
	
	public ProduceBehaviour(AgenteTrabalhador agente, Produzir pro)
	{
		at = agente;
		producao = pro;
		
		terminado = false;
	}
	
	@Override
	public void action()
	{
		if(at.tr.produzir(producao))
		{
			terminado = true;
		}
	}

	@Override
	public boolean done()
	{
		return ( terminado );
	}
	
}
