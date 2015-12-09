package Agentes;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import Logica.Auxiliar;
import Logica.Ponto;
import Logica.Trabalhador;
import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;



public class AgenteTrabalhador extends Agent implements Drawable
{
	public Trabalhador tr;
	
	public enum WorkingState{
		WAITING_FOR_JOB, PREPARE_TO_WORK, WORKING, CHARGING_BATTERY, MOVING
	}

	protected WorkingState state;
	protected String workerType;
	protected ServiceManager serviceManager;
	
	static ImageIcon iconCarro = new ImageIcon(Auxiliar.folder + "carro.png");
	static ImageIcon iconMota = new ImageIcon(Auxiliar.folder + "mota.png");
	static ImageIcon iconDrone = new ImageIcon(Auxiliar.folder + "drone_estrada.png");
	static ImageIcon iconCamiao = new ImageIcon(Auxiliar.folder + "camiao.png");
	
	static Image carro = iconCarro.getImage();
	static Image mota = iconMota.getImage();
	static Image drone = iconDrone.getImage();
	static Image camiao = iconCamiao.getImage();
	
	public AgenteTrabalhador(String nome, int tipoTransporte, Object2DGrid space)
	{
		super();
		espaco = space;
		
		state = WorkingState.WAITING_FOR_JOB;
		this.serviceManager = new ServiceManager(this);
		
		tr = new Trabalhador(nome, tipoTransporte);
	}

	Object2DGrid espaco;
	
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
		case 1:
			arg0.drawImage(carro);
			break;
		case 2:
			arg0.drawImage(mota);	
			break;
		case 3:
			arg0.drawImage(drone);
			break;
		case 4:
			arg0.drawImage(camiao);
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
	
	public void executarMovimento(Vector<Ponto> percurso)
	{
		addBehaviour(new MovingBehaviour(this, percurso));
	}
}
