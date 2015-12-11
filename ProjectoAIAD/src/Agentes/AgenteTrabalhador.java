package Agentes;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import Logica.Auxiliar;
import Logica.Mundo;
import Logica.Ponto;
import Logica.Trabalhador;

import sajas.core.Agent;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.core.behaviours.SimpleBehaviour;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;



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
	
	MySequentialBehaviour listaComportamentos;

	protected WorkingState state;
	protected String workerType;
	protected ServiceManager serviceManager;
	protected SocialBehaviour socializer;
	protected ProcessManager processManager;
	
	public AgenteTrabalhador(String nome, int tipoTransporte, Object2DGrid space)
	{
		super();
		espaco = space;
		state = WorkingState.WAITING_FOR_JOB;
		
		tr = new Trabalhador(nome, tipoTransporte);
	}

	/************************************************************/
	/************************* FUNCOES **************************/
	/************************************************************/
	protected void setup()
	{
		String nomeServico = "";
		String empregador = "";
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			nomeServico = (String) args[0];
			empregador = (String) args[1];
		} else {
			System.out.println("Nao especificou o tipo");
		}

		this.serviceManager = new ServiceManager(this);
		
		Service service = new Service(nomeServico, "");
		
		if(empregador.equals("P"))
		{
			serviceManager.requestService(service);
		}
		else
		{
			serviceManager.offerService(service);
		}
		
		processManager = new ProcessManager(this);
		socializer = new SocialBehaviour(this);
		
		addBehaviour(processManager);
		addBehaviour(socializer);
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
		Ponto destino = new Ponto(linha2, coluna2);
		movimentar(mundo, destino);
	}
	
	public void movimentar(Mundo mundo, int linha1, int coluna1, Vector<Ponto> destinos)
	{
		tr.set(linha1, coluna1);
		movimentar(mundo, destinos);
	}
		
	public void movimentar(Mundo mundo, Ponto destino)
	{
		Vector<SimpleBehaviour> vec = new Vector<SimpleBehaviour>();
		Ponto actual = tr.obterLocalizacao();
		
		MovingBehaviour mb = new MovingBehaviour(this, mundo, actual, destino);
		vec.addElement(mb);
		
		listaComportamentos = new MySequentialBehaviour(this, vec);
		
		this.addBehaviour(listaComportamentos);
	}
	
	public void movimentar(Mundo mundo, Vector<Ponto> destinos)
	{
		Vector<SimpleBehaviour> vec = new Vector<SimpleBehaviour>();
		
		Ponto actual = tr.obterLocalizacao();
		
		MovingBehaviour mb = new MovingBehaviour(this, mundo, actual, destinos.elementAt(0));
		vec.addElement(mb);
		
		for(int index = 0; index < (destinos.size() - 1); index++)
		{
			Ponto origem = destinos.elementAt(index);
			Ponto destino = destinos.elementAt(index + 1);
			
			mb = new MovingBehaviour(this, mundo, origem, destino);
			vec.addElement(mb);
		}
		
		listaComportamentos = new MySequentialBehaviour(this, vec);
		
		this.addBehaviour(listaComportamentos);
	}
}
