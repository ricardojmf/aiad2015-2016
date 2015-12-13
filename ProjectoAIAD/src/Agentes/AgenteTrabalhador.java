package Agentes;

import java.util.Vector;

import Logica.*;
import Repast.Sprite;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;



public class AgenteTrabalhador extends Agent implements Drawable
{
	public Trabalhador tr;
	
	static final Sprite carro = new Sprite(Auxiliar.folder + "carro.png");
	static final Sprite mota = new Sprite(Auxiliar.folder + "mota.png");
	static final Sprite drone = new Sprite(Auxiliar.folder + "drone_parede.png");
	static final Sprite camiao = new Sprite(Auxiliar.folder + "camiao.png");
	
	Object2DGrid espaco;
	public enum WorkerState {
		WAITING_FOR_JOB, PREPARE_TO_WORK, WORKING, CHARGING_BATTERY, MOVING, NOT_MOVING
	}
	
	MySequentialBehaviour listaComportamentos;
	
	public Mundo mundo;
	protected WorkerState state;
	protected String nome;
	protected String workerType;
	protected ServiceManager serviceManager;
	protected SocialBehaviour socializer;
	protected ProcessManager processManager;
	
	public AgenteTrabalhador(String nome, int tipoTransporte, Object2DGrid space, Mundo mundo)
	{
		super();
		espaco = space;
		this.nome = nome;
		state = WorkerState.WAITING_FOR_JOB;
		this.mundo = mundo;
		
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
		
		//Service service = new Service(nomeServico, "", 1000, null, null, "DO WORK ON FOR");
		Service service = new Service(nomeServico, "", 1000, null, null, "DO WORK ON FOR");
		
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
			arg0.drawImage(drone.value());
			break;
		case 4:
			arg0.drawImage(camiao.value());
			break;
		case 1:
			arg0.drawImage(carro.value());
			break;
		case 3:
			arg0.drawImage(mota.value());
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
	
	public void movimentar(int linha1, int coluna1, int linha2, int coluna2)
	{
		tr.set(linha1, coluna1);
		Ponto destino = new Ponto(linha2, coluna2);
		movimentar(destino);
	}
	
	public void movimentar(int linha1, int coluna1, Vector<Ponto> destinos)
	{
		tr.set(linha1, coluna1);
		movimentar(destinos);
	}
		
	public void movimentar(Ponto destino)
	{
		Vector<Behaviour> vec = new Vector<Behaviour>();
		Ponto actual = tr.obterLocalizacao();
		
		MovingBehaviour mb = new MovingBehaviour(this, actual, destino);
		vec.addElement(mb);
		
		listaComportamentos = new MySequentialBehaviour(this, vec);
		
		this.addBehaviour(listaComportamentos);
	}
	
	public void movimentar(Vector<Ponto> destinos)
	{
		Vector<Behaviour> vec = new Vector<Behaviour>();
		
		Ponto actual = tr.obterLocalizacao();
		
		MovingBehaviour mb = new MovingBehaviour(this, actual, destinos.elementAt(0));
		vec.addElement(mb);
		
		for(int index = 0; index < (destinos.size() - 1); index++)
		{
			Ponto origem = destinos.elementAt(index);
			Ponto destino = destinos.elementAt(index + 1);
			
			mb = new MovingBehaviour(this, origem, destino);
			vec.addElement(mb);
		}
		
		listaComportamentos = new MySequentialBehaviour(this, vec);
		
		this.addBehaviour(listaComportamentos);
	}
	
	public void adquirir(Producto producto, int quantidade)
	{
		adquirir(new Ranhura(producto, quantidade) );
	}
	
	public void adquirir(Ranhura productos)
	{
		Vector<Armazem> armazensComProducto = mundo.obterArmazensProducto(tr, productos.producto);
		
		if(armazensComProducto.size() != 0) //se houver armazens com o producto, ir ate la
		{
			// trajectoria ate armazem
			Vector<Ponto> percursoPerto =
				Ponto.percursoCurtoArmazens(mundo.cidade.matriz, tr.meioTransporte,
					tr.obterLocalizacao(), armazensComProducto);
			
			Armazem ar = mundo.obterArmazem(percursoPerto.elementAt(percursoPerto.size() - 1));
			
			ContentorArmazem ca = ar.obterContentor(tr);
			if(ca == null)
			{
				comprar(productos);
				return;
			}
			
			ProductoArmazenado pa = ca.existeProducto(productos.producto);
			
			if(pa == null)
			{
				comprar(productos);
				return;
			}
			
			if(pa.quantidade < productos.quantidade)
			{
				comprar(productos);
				return;
			}
			
			Vector<Behaviour> vec = new Vector<Behaviour>();
			Ponto actual = tr.obterLocalizacao();
			
			Ponto destino = percursoPerto.elementAt(percursoPerto.size() - 1);
			
			MovingBehaviour mb1 = new MovingBehaviour(this, actual, destino);
			
			// armazenar
			PickupSimpleBehaviour mb2 = new PickupSimpleBehaviour(this, mundo.obterArmazem(destino), productos);
			
			vec.addElement(mb1);
			vec.addElement(mb2);
			
			listaComportamentos = new MySequentialBehaviour(this, vec);
			this.addBehaviour(listaComportamentos);
		}
		else //senao houver, vai comprar
		{
			comprar(productos);
		}
	}
	
	public void comprar(Producto producto, int quantidade)
	{
		Ranhura productos = new Ranhura(producto, quantidade);
		comprar(productos);
	}
	
	public void comprar(Ranhura productos)
	{
		int totalPeso = productos.obterTamanhoTotal();
		int totalPreco = productos.obterPrecoTotal();
		
		if(totalPreco <= tr.riqueza)
		{
			Vector<Behaviour> vec = new Vector<Behaviour>();
			Ponto actual = tr.obterLocalizacao();
			
			if( (totalPeso + tr.carga ) <= tr.cargaMax) // caso possa cargar os productos
			{
				Vector<Loja> lojasComProducto = mundo.obterLojasProducto(productos.producto);
				Vector<Ponto> percursoPerto =
					Ponto.percursoCurtoLojas(mundo.cidade.matriz, tr.meioTransporte,
							tr.obterLocalizacao(), lojasComProducto);
				
				Ponto destino = percursoPerto.elementAt(percursoPerto.size() - 1);
				
				// trajecto loja
				MovingBehaviour mb1 = new MovingBehaviour(this, actual, destino);
				
				// comprar
				BuyingSimpleBehaviour mb2 =
					new BuyingSimpleBehaviour(this, mundo.obterLoja(destino), productos);
				
				// adicionar os behaviours
				vec.addElement(mb1);
				vec.addElement(mb2);
			}
			else // ir ao armazem mais proximo despejar
			{
				// trajecto ate armazem para despejar
				Vector<Armazem> armazensComProducto = mundo.obterArmazensProducto(tr, productos.producto);
				
				Vector<Ponto> percursoPerto = null;
				if(armazensComProducto.size() == 0) //caso n tenha armazens com producto
				{
					percursoPerto = Ponto.percursoCurtoArmazens(mundo.cidade.matriz, tr.meioTransporte,
								tr.obterLocalizacao(), mundo.armazens);
				}
				else
				{
					percursoPerto =
						Ponto.percursoCurtoArmazens(mundo.cidade.matriz, tr.meioTransporte,
								tr.obterLocalizacao(), armazensComProducto);
				}				
				
				Ponto destino = percursoPerto.elementAt(percursoPerto.size() - 1);
				
				MovingBehaviour mb1 = new MovingBehaviour(this, actual, destino);
				
				// armazenar productos aleatoriamente
				StoreRandomSimpleBehaviour mb2 = new StoreRandomSimpleBehaviour(this, mundo.obterArmazem(destino), productos);	
				
				// trajecto loja
				Vector<Loja> lojasComProducto = mundo.obterLojasProducto(productos.producto);
				Vector<Ponto> percursoPerto2 =
					Ponto.percursoCurtoLojas(mundo.cidade.matriz, tr.meioTransporte,
							destino, lojasComProducto);
				
				Ponto destino2 = percursoPerto2.elementAt(percursoPerto2.size() - 1);
				
				MovingBehaviour mb3 = new MovingBehaviour(this, destino, destino2);
				
				// comprar
				BuyingSimpleBehaviour mb4 =
					new BuyingSimpleBehaviour(this, mundo.obterLoja(destino2), productos);
				
				// adicionar os behaviours
				vec.addElement(mb1);
				vec.addElement(mb2);
				vec.addElement(mb3);
				vec.addElement(mb4);
			}
			
			listaComportamentos = new MySequentialBehaviour(this, vec);
			this.addBehaviour(listaComportamentos);
		}
		else
		{
			// vender productos ?
		}
	}
	
	
	public void debug(String msg) {
		System.out.println("[" + nome + "]: -> " + msg);
	}
}
