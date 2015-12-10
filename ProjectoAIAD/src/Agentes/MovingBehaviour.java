package Agentes;

import java.util.Vector;

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
	
	Mundo mundo;
	Ponto origem;
	Ponto destino;
	
	public MovingBehaviour(AgenteTrabalhador worker, Mundo mundo, Ponto origem, Ponto destino)
	{
		this.worker = worker;		
		this.mundo = mundo;
		this.destino = destino;
		this.origem = origem;
		
		superContador = 0;
	}
	
	public void calcularCenas()
	{
		worker.tr.set(origem.linha, origem.coluna);
		
		// percurso entre origem e destino
		Vector<Ponto> percursoDestino =
			Ponto.percursoCurtoDirecto(mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), destino);

		// percurso minimo para verificar se ha bateria ate ao percurso
		Vector<Ponto> percursoMinimo1 =
			Ponto.percursoCurtoEstacoes(mundo.cidade.matriz, worker.tr.meioTransporte, worker.tr.obterLocalizacao(), mundo.estacoes);
		
		// percurso minimo para verificar se quando chegar ao destino, tem suficiente para ir a uma estacao recarga
		Vector<Ponto> percursoMinimo2 =
			Ponto.percursoCurtoEstacoes(mundo.cidade.matriz, worker.tr.meioTransporte, destino, mundo.estacoes);
		
		// percurso minimo da estacao mais curta da origem ate ao destino
		Vector<Ponto> percursoMinimo3 =
			Ponto.percursoCurtoDirecto(mundo.cidade.matriz, worker.tr.meioTransporte, percursoMinimo1.elementAt(percursoMinimo1.size() - 1), destino);
				
		int distOrigemDestino = percursoDestino.size();
		int distOrigemRecargaX = (percursoMinimo1 != null ? percursoMinimo1.size() : -1);
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
			calcularCenas();
		}
		else
		{
			if(superContador < 5)//if(true)
			{
				if (temQueRecargar && recarregando && !recargado)
				{
					//System.out.println("RECARREGANDO 1");
					if (recarregando)
					{
						//System.out.println("RECARREGANDO 2 com contador = " + contador);
						contador++;
						if (contador == (tempoEspera*20))
						{
							//System.out.println("RECARREGANDO 3");
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
					
					if(percursos.size() == 0 && percurso.size() == 0)
					{
						//verBateriaFim();
						
						superContador++;
						
						if(superContador < 5)
						{
							calcularCenas();
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
			calcularCenas();
		}
		else
		{
			if (temQueRecargar && recarregando && !recargado)
			{
				//System.out.println("RECARREGANDO 1");
				if (recarregando)
				{
					//System.out.println("RECARREGANDO 2 com contador = " + contador);
					contador++;
					if (contador == (tempoEspera*20))
					{
						//System.out.println("RECARREGANDO 3");
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
		
	}
	
	@Override
	public void action()
	{
		nrepetir();	
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
