package Agentes;

import java.awt.Color;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import Logica.Auxiliar;
import Logica.Mundo;
import Logica.Ponto;
import Logica.Recarga;
import Logica.Trabalhador;

import sajas.core.Agent;
import sajas.core.behaviours.SimpleBehaviour;
import sajas.domain.DFService;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;



public class AgenteTrabalhador extends Agent implements Drawable
{
	public Trabalhador tr;
	
	static final ImageIcon iconCarro = new ImageIcon(Auxiliar.folder + "carro.png");
	static final ImageIcon iconMota = new ImageIcon(Auxiliar.folder + "mota.png");
	static final ImageIcon iconDrone_Estrada = new ImageIcon(Auxiliar.folder + "drone_estrada.png");
	static final ImageIcon iconDrone_Parede = new ImageIcon(Auxiliar.folder + "drone_parede.png");
	static final ImageIcon iconCamiao = new ImageIcon(Auxiliar.folder + "camiao.png");
	
	static final Image carro = iconCarro.getImage();
	static final Image mota = iconMota.getImage();
	static final Image drone_estrada = iconDrone_Estrada.getImage();
	static final Image drone_parede = iconDrone_Parede.getImage();
	static final Image camiao = iconCamiao.getImage();
	
	Object2DGrid espaco;
	public enum WorkingState{
		WAITING_FOR_JOB, PREPARE_TO_WORK, WORKING, CHARGING_BATTERY, MOVING
	}

	protected WorkingState state;
	protected String workerType;
	protected ServiceManager serviceManager;
	
	public AgenteTrabalhador(String nome, int tipoTransporte, Object2DGrid space)
	{
		super();
		espaco = space;
		state = WorkingState.WAITING_FOR_JOB;
		this.serviceManager = new ServiceManager(this);
		
		tr = new Trabalhador(nome, tipoTransporte);
	}

	/************************************************************/
	/************************* FUNCOES **************************/
	/************************************************************/
	protected void setup()
	{
		String tipo = "";
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			tipo = (String) args[0];
		} else {
			System.out.println("Nao especificou o tipo");
		}

		Service service = new Service(tipo, "");
		serviceManager.offerService(service);
		
		addBehaviour(new BasicWorkerBehaviour(this));
	}
	
	@Override
	public void draw(SimGraphics arg0) {
		arg0.setDrawingCoordinates(getX() * arg0.getCurWidth(), getY() * arg0.getCurHeight(), 0);
		
		switch(tr.transporte)
		{
		case 2:
			arg0.drawImage(drone_estrada);
			break;
		case 4:
			arg0.drawImage(camiao);
			break;
		case 1:
			arg0.drawImage(carro);
			break;
		case 3:
			arg0.drawImage(mota);
			break;
		}
	}

	@Override
	public int getX() {
		return( tr.coluna );
	}

	@Override
	public int getY() {
		return( tr.linha );
	}
	
	public void movimentar(Mundo mundo, int linha1, int coluna1, int linha2, int coluna2)
	{
		tr.set(linha1, coluna1);
		movimentar(mundo, linha2, coluna2);
	}
	
	public void movimentar(Mundo mundo, Ponto destino)
	{
		movimentar(mundo, destino.linha, destino.coluna);
	}
	
	public void movimentar(Mundo mundo, int linha2, int coluna2)
	{
		// percurso minimo para verificar se ha bateria ate ao percurso
		Vector<Ponto> percursoMinimo1 = null;
		for(Recarga re: mundo.estacoes)
		{
			Vector<Ponto> percurso = null;
			
			if(tr.meioTransporte)
			{
				percurso = Auxiliar.linhaRecta(tr.linha, tr.coluna,
						re.linha, re.coluna);
			}
			else
			{
				percurso = Auxiliar.caminhoCurto(mundo.cidade.matriz,
						tr.coluna, tr.linha,
						re.coluna, re.linha);
			}
			
			if (percursoMinimo1 == null)
			{
				percursoMinimo1 = new Vector<Ponto>();
				percursoMinimo1 = percurso;
			}
			else
			{
				if( percurso.size() < percursoMinimo1.size())
				{
					percursoMinimo1 = percurso;
				}
			}
		}
		
		// percurso entre origem e destino
		Vector<Ponto> percursoDestino = null;
		if(tr.meioTransporte)
		{
			percursoDestino = Auxiliar.linhaRecta(tr.linha, tr.coluna,
					linha2, coluna2);
		}
		else
		{
			percursoDestino = Auxiliar.caminhoCurto(mundo.cidade.matriz,
					tr.coluna, tr.linha,
					coluna2, linha2);
		}
				
		// percurso minimo para verificar se quando chegar ao destino, tem suficiente para ir a uma estacao recarga
		Vector<Ponto> percursoMinimo2 = null;
		for(Recarga re: mundo.estacoes)
		{
			Vector<Ponto> percurso = null;
			
			if(tr.meioTransporte)
			{
				percurso = Auxiliar.linhaRecta(linha2, coluna2,
						re.linha, re.coluna);
			}
			else
			{
				percurso = Auxiliar.caminhoCurto(mundo.cidade.matriz,
						coluna2, linha2,
						re.coluna, re.linha);
			}
			
			if (percursoMinimo2 == null)
			{
				percursoMinimo2 = new Vector<Ponto>();
				percursoMinimo2 = percurso;
			}
			else
			{
				if( percurso.size() < percursoMinimo2.size())
				{
					percursoMinimo2 = percurso;
				}
			}
		}
		
		int distOrigemRecarga = (percursoMinimo1 != null ? percursoMinimo1.size() : -1);
		int distOrigemDestino = percursoDestino.size();
		int distDestinoRecarga = (percursoMinimo2 != null ? percursoMinimo2.size() : -1);
		
		//System.out.println("bateria: " + tr.bateria);
		//System.out.println("OrigemRecarga: " + distOrigemRecarga);
		//System.out.println("OrigemDestino: " + distOrigemDestino);
		//System.out.println("DestinoRecarga: " + distDestinoRecarga);
		
		if( (tr.bateria - distOrigemDestino) >= (distDestinoRecarga) )
		{
			executarMovimento(percursoDestino);
		}
		else
		{
			
		}
	}
	
	public void executarMovimento(Vector<Ponto> percurso)
	{
		addBehaviour(new MovingBehaviour(this, percurso));
	}
}
