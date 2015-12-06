package Jade;

import java.util.Vector;

import Logica.Ponto;
import Logica.TarefaPreco;
import Logica.Trabalhador;

import jade.core.*;

public class Agente extends Agent
{
	private static final long serialVersionUID = 1L;

	public AgenteJade ag;
	
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
	
	
}
