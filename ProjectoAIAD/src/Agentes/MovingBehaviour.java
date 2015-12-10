package Agentes;

import java.util.Vector;

import Logica.Ponto;
import sajas.core.behaviours.SimpleBehaviour;

public class MovingBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1L;
	private static final int frequencia = 5;
	private static final int tempoEspera = 5;
	
	private AgenteTrabalhador worker;
	Vector<Vector<Ponto>> percursos;
	
	Vector<Ponto> percurso;
	int contador;
	boolean temQueRecargar;
	boolean recarregando;
	boolean recargado;
	
	public MovingBehaviour(AgenteTrabalhador worker, Vector<Vector<Ponto>> novoPercurso, boolean irRecarga)
	{
		this.worker = worker;
		percursos = novoPercurso;
		
		percurso = percursos.elementAt(0);
		percursos.removeElementAt(0);
		
		contador = 0;
		
		temQueRecargar = irRecarga;
		recarregando = false;
		recargado = false;
		
		verBateriaMovimento();
	}
	
	@Override
	public void action()
	{		
		if (temQueRecargar && recarregando && !recargado)
		{
			System.out.println("RECARREGANDO 1");
			if (recarregando)
			{
				System.out.println("RECARREGANDO 2 com contador = " + contador);
				contador++;
				if (contador == (tempoEspera*20))
				{
					System.out.println("RECARREGANDO 3");
					recarregando = false;
					recargado = true;
					contador = 0;
					
					worker.tr.bateria = worker.tr.bateriaMax;
				}
			}			
		}
		else // se nao estiver a recarregar
		{
			if (percurso.size() == 0)
			{
				percurso = percursos.elementAt(0);
				percursos.removeElementAt(0);
				
				if (!recargado)
				{
					recarregando = true;
				}
				
				//contador = 0;
				
				verBateriaMovimento();
			}
			
			if(contador == frequencia*(6 - worker.tr.velocidade) && worker.tr.bateria > 0)
			{
				worker.tr.bateria--;
				contador = 0;
				
				Ponto p = percurso.elementAt(0);
				percurso.removeElementAt(0);
				
				worker.espaco.putObjectAt(worker.tr.coluna, worker.tr.linha, null);
				worker.tr.linha = p.linha;
				worker.tr.coluna = p.coluna;
				worker.espaco.putObjectAt(worker.tr.coluna, worker.tr.linha, this);
			}
			else
			{
				contador++;
			}
			
			if(done())
			{
				verBateriaFim();
			}
		}
	}

	@Override
	public boolean done()
	{
		return ( (percurso.size() == 0 && percursos.size() == 0) || worker.tr.bateria == 0);
	}
	
	public void verBateriaMovimento()
	{
		System.out.println(worker.tr.nome + " iniciou novo percurso com bateria: " + worker.tr.bateria + "/" + worker.tr.bateriaMax);
	}
	
	public void verBateriaFim()
	{
		System.out.println(worker.tr.nome + " terminou com bateria: " + worker.tr.bateria + "/" + worker.tr.bateriaMax);
	}
}
