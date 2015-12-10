package Agentes;

import Logica.Produzir;
import sajas.core.behaviours.SimpleBehaviour;

public class ProduceBehaviour extends SimpleBehaviour
{
	AgenteTrabalhador at;
	Produzir producao;
	
	private static enum Estado {
		INICIO, FIM, PRODUZINDO
	}
	
	Estado estadoProducao;
	
	int contador;
	private static final int tempoEspera = 5;
	
	public ProduceBehaviour(AgenteTrabalhador agente, Produzir pro)
	{
		at = agente;
		producao = pro;
		
		estadoProducao = Estado.INICIO;
	}
	
	@Override
	public void action()
	{
		if( estadoProducao == Estado.PRODUZINDO )
		{
			if (contador == tempoEspera)
			{
				contador = 0;
				estadoProducao = Estado.FIM;
			}
			else
			{
				contador++;
			}
		}
		else
		{
			if(at.tr.produzir(producao))
			{
				estadoProducao = Estado.PRODUZINDO;
			}
		}
	}

	@Override
	public boolean done()
	{
		return ( estadoProducao == Estado.FIM );
	}
	
}
