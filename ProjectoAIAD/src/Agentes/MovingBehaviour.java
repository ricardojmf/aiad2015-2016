package Agentes;

import java.util.Vector;

import Agentes.AgenteTrabalhador.MovingState;
import Logica.Mundo;
import Logica.Ponto;
import sajas.core.behaviours.SimpleBehaviour;

public class MovingBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = 1L;
	private static final int frequencia = 1; // 5
	private static final int tempoEspera = 5;
	
	private AgenteTrabalhador worker;
	Vector<Vector<Ponto>> percursos;
	
	Vector<Ponto> percurso;
	int contador;
	boolean temQueRecargar;
	boolean recarregando;
	boolean recargado;
	
	int superContador;
	
	public Ponto origem;
	public Ponto destino;
	
	private enum Pesquisa {
		DESTINO, LOJA, ARMAZEM, RECARGA
	};
	
	Pesquisa pes;
	
	public MovingBehaviour(AgenteTrabalhador worker, Ponto origem, Ponto destino)
	{
		worker.movingState = MovingState.MOVING;
		this.worker = worker;	
		this.destino = destino;
		this.origem = origem;
		
		pes = Pesquisa.DESTINO;
		
		superContador = 0;
	}
	
	public MovingBehaviour(AgenteTrabalhador worker, Ponto origem, int tipoPesquisa)
	{
		worker.movingState = MovingState.MOVING;
		this.worker = worker;
		this.destino = null;
		this.origem = origem;
		
		switch(tipoPesquisa)
		{
		case 1:
			pes = Pesquisa.LOJA;
			break;
		case 2:
			pes = Pesquisa.ARMAZEM;
			break;
		case 3:
			pes = Pesquisa.RECARGA;
			break;
		}
		
		superContador = 0;
	}
	
	public void calcularPercursos()
	{
		worker.tr.set(origem.linha, origem.coluna);
		
		// percurso entre origem e destino
		Vector<Ponto> percursoDestino = null;
		
		// percurso minimo para verificar se ha bateria ate ao percurso
		Vector<Ponto> percursoMinimo1 = null;
		
		// percurso minimo para verificar se quando chegar ao destino, tem suficiente para ir a uma estacao recarga
		Vector<Ponto> percursoMinimo2 = null;
		
		// percurso minimo da estacao mais curta da origem ate ao destino
		Vector<Ponto> percursoMinimo3 = null;
		
		if(pes == Pesquisa.LOJA)
		{
			percursoDestino =
				Ponto.percursoCurtoLojas(worker.mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), worker.mundo.lojas);
		
			destino = percursoDestino.elementAt(percursoDestino.size() - 1);
		}
		else if(pes == Pesquisa.ARMAZEM)
		{
			percursoDestino =
				Ponto.percursoCurtoArmazens(worker.mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), worker.mundo.armazens);
		
			destino = percursoDestino.elementAt(percursoDestino.size() - 1);
		}
		else if(pes == Pesquisa.RECARGA)
		{
			percursoDestino =
				Ponto.percursoCurtoEstacoes(worker.mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), worker.mundo.estacoes);
		
			destino = percursoDestino.elementAt(percursoDestino.size() - 1);
		}
		else
		{
			percursoDestino =
					Ponto.percursoCurtoDirecto(worker.mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), destino);
		}
		
		percursoMinimo1 =
			Ponto.percursoCurtoEstacoes(worker.mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), worker.mundo.estacoes);
		
		percursoMinimo2 =
			Ponto.percursoCurtoEstacoes(worker.mundo.cidade.matriz, worker.tr.meioTransporte, destino, worker.mundo.estacoes);
		
		percursoMinimo3 =
			Ponto.percursoCurtoDirecto(worker.mundo.cidade.matriz, worker.tr.meioTransporte, percursoMinimo1.elementAt(percursoMinimo1.size() - 1), destino);
				
		int distOrigemDestino = percursoDestino.size();
		//int distOrigemRecargaX = (percursoMinimo1 != null ? percursoMinimo1.size() : -1);
		int distDestinoRecargaY = (percursoMinimo2 != null ? percursoMinimo2.size() : -1);
		int distRecargaXDestino = percursoMinimo3.size();
		
		//System.out.println("Trabalhador   : " + worker.tr.nome);
		//System.out.println("Bateria       : " + worker.tr.bateria);
		//System.out.println("OrigemDestino : " + distOrigemDestino);
		//System.out.println("OrigemRecargaX : " + distOrigemRecargaX);
		//System.out.println("RecargaXDestino: " + distRecargaXDestino);
		//System.out.println("DestinoRecargaY: " + distDestinoRecargaY);
		
		Vector<Vector<Ponto>> percursoss = new Vector<Vector<Ponto>>();
		
		if( (distOrigemDestino + distDestinoRecargaY) <= worker.tr.bateria )
		{
			percursoss.addElement(percursoDestino);
			
			temQueRecargar = false;
		}
		else if( (distRecargaXDestino + distDestinoRecargaY) <= worker.tr.bateria )
		{
			percursoss.addElement(percursoMinimo1);
			percursoss.addElement(percursoMinimo3);
			
			temQueRecargar = true;
		}
		
		this.percursos = new Vector<Vector<Ponto>>();
		this.percurso = new Vector<Ponto>();
		
		this.percursos = percursoss;
		
		this.percurso = percursos.elementAt(0);
		this.percursos.removeElementAt(0);
		
		this.contador = 0;
		
		this.recarregando = false;
		this.recargado = false;
	}
	
	private void repetir()
	{
		if(percursos == null)
		{
			calcularPercursos();
		}
		else
		{
			if(superContador < 5)//if(true)
			{
				if (temQueRecargar && recarregando && !recargado)
				{
					if (recarregando)
					{
						contador++;
						if (contador == (tempoEspera*20))
						{
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
					
					if(percursos.size() == 0 && percurso.size() == 0)
					{
						superContador++;
						
						if(superContador < 5)
						{
							calcularPercursos();
						}
					}
				}
			}	
		}		
	}
	
	private void nrepetir()
	{
		if(percursos == null)
		{
			calcularPercursos();
		}
		else
		{
			if (temQueRecargar && recarregando && !recargado)
			{
				if (recarregando)
				{
					contador++;
					if (contador == (tempoEspera*20))
					{
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
		
	}
	
	@Override
	public void action()
	{
		nrepetir();	
	}

	@Override
	public boolean done()
	{
		if ( (percurso.size() == 0 && percursos.size() == 0) || worker.tr.bateria == 0) {
			worker.movingState = MovingState.NOT_MOVING;
			return true;
		}
		else
			return false;
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
