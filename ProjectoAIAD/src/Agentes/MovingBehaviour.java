package Agentes;

import java.util.Vector;

import Logica.Ponto;
import sajas.core.behaviours.SimpleBehaviour;

public class MovingBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1L;
	private AgenteTrabalhador worker;
	Vector<Ponto> percurso;
	int contador;
	
	public MovingBehaviour(AgenteTrabalhador worker, Vector<Ponto> novoPercurso)
	{
		this.worker = worker;
		percurso = novoPercurso;
		contador = 0;
	}
	
	@Override
	public void action()
	{
		int frequencia = 5;
		
		if(contador == frequencia*(6 - worker.tr.velocidade) && worker.tr.bateria > 0)
		{
			worker.tr.bateria--;
			contador = 0;
			
			Ponto p = percurso.elementAt(0);
			percurso.removeElementAt(0);
			
			worker.espaco.putObjectAt(worker.tr.linha, worker.tr.coluna, null);
			worker.tr.linha = p.linha;
			worker.tr.coluna = p.coluna;
			worker.espaco.putObjectAt(worker.tr.linha, worker.tr.coluna, this);
		}
		else
		{
			contador++;
		}
	}

	@Override
	public boolean done()
	{
		return ( percurso.size() == 0 || worker.tr.bateria == 0);
	}
}
