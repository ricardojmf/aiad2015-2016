package Jade;

import java.util.Vector;

import Logica.Auxiliar;
import Logica.Ponto;
import Logica.TarefaPreco;
import Logica.Trabalhador;

public class Agente
{
	public Trabalhador trabalhador;
	
	Vector<Ponto> percurso;
	int pontosMover;
	TarefaPreco tarefaActual;
	
	public Agente(String no, int tipoAgente)
	{
		trabalhador = new Trabalhador(no, tipoAgente);
		
		percurso = null;
		pontosMover = 0;
		tarefaActual = null;
	}
	
	public void comportamentoCalculoPercurso(TarefaPreco ta)
	{
		tarefaActual = ta;
		Ponto src = new Ponto(trabalhador.linha, trabalhador.coluna);
		Ponto dst = tarefaActual.tb.destino;
		
		if (trabalhador.meioTransporte) // DRONE
		{
			percurso = Auxiliar.linhaRecta(src.linha, src.coluna, dst.linha, dst.coluna);
		}
		else
		{
			
		}
	}
	
	public void comportamentoMover()
	{
		if (pontosMover < trabalhador.velocidade)
		{
			pontosMover++;
		}
		else
		{
			pontosMover = 0;
			if (percurso.size() > 1)
			{
				percurso.remove(0);
			}
		}
	}
	
	/* CONDICOES */
	public boolean condicaoA()
	{
		return false;
	}
	
	/* COMPORTAMENTOS */
	public void comportamentoA()
	{
		
	}
	
	public void comportamento()
	{
		if (condicaoA())
		{
			comportamentoA();
		}
	}
}
