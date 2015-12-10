package Agentes;

import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;

import Logica.Auxiliar;
import Logica.Mundo;
import Logica.Ponto;
import Logica.Trabalhador;

import sajas.core.Agent;

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
		String nomeServico = "";
		String empregador = "";
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			nomeServico = (String) args[0];
			empregador = (String) args[1];
		} else {
			System.out.println("Nao especificou o tipo");
		}
		
		Service service = new Service(nomeServico, "");
		
		
		if(empregador.equals("P"))
		{
			serviceManager.requestService(service);
		}
		else
		{
			serviceManager.offerService(service);
		}
		
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
		Ponto destino = new Ponto(linha2, coluna2);
		movimentar(mundo, destino);
	}
	
	public void movimentar(Mundo mundo, Ponto destino)
	{
		// percurso entre origem e destino
		Vector<Ponto> percursoDestino =
			Ponto.percursoCurtoDirecto(mundo.cidade.matriz, tr.meioTransporte, tr.obterLocalizacao(), destino);

		// percurso minimo para verificar se ha bateria ate ao percurso
		Vector<Ponto> percursoMinimo1 =
			Ponto.percursoCurtoEstacoes(mundo.cidade.matriz, tr.meioTransporte, tr.obterLocalizacao(), mundo.estacoes);
		
		// percurso minimo para verificar se quando chegar ao destino, tem suficiente para ir a uma estacao recarga
		Vector<Ponto> percursoMinimo2 =
			Ponto.percursoCurtoEstacoes(mundo.cidade.matriz, tr.meioTransporte, destino, mundo.estacoes);
		
		// percurso minimo da estacao mais curta da origem ate ao destino
		Vector<Ponto> percursoMinimo3 =
			Ponto.percursoCurtoDirecto(mundo.cidade.matriz, tr.meioTransporte, percursoMinimo1.elementAt(percursoMinimo1.size() - 1), destino);
				
		int distOrigemDestino = percursoDestino.size();
		int distOrigemRecargaX = (percursoMinimo1 != null ? percursoMinimo1.size() : -1);
		int distDestinoRecargaY = (percursoMinimo2 != null ? percursoMinimo2.size() : -1);
		int distRecargaXDestino = percursoMinimo3.size();
		
		//System.out.println("Trabalhador   : " + tr.nome);
		//System.out.println("Bateria       : " + tr.bateria);
		//System.out.println("OrigemDestino : " + distOrigemDestino);
		//System.out.println("OrigemRecargaX : " + distOrigemRecargaX);
		//System.out.println("RecargaXDestino: " + distRecargaXDestino);
		//System.out.println("DestinoRecargaY: " + distDestinoRecargaY);
		
		Vector<Vector<Ponto>> percursos = new Vector<Vector<Ponto>>();
		
		if( (distOrigemDestino + distDestinoRecargaY) <= tr.bateria )
		{
			percursos.addElement(percursoDestino);
			
			executarMovimento(percursos, false);
		}
		else if( (distRecargaXDestino + distDestinoRecargaY) <= tr.bateria )
		{
			percursos.addElement(percursoMinimo1);
			percursos.addElement(percursoMinimo3);
			executarMovimento(percursos, true);
		}
	}
	
	public void executarMovimento(Vector<Vector<Ponto>> percursos, boolean temQueRecargar)
	{
		addBehaviour(new MovingBehaviour(this, percursos, temQueRecargar));
	}
}
