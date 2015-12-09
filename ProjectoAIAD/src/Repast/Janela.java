package Repast;

import Agentes.AgenteTrabalhador;
import Logica.Auxiliar;
import Logica.Mundo;

import java.util.ArrayList;
import java.util.Vector;

import jade.core.Profile;
import jade.core.ProfileImpl;

import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

public class Janela extends Repast3Launcher
{
	// CLASSES PROPRIAS
	Quadro quadro;
	
	private Mundo mundo;
	
	// CLASSES REPAST
	private ContainerController contentorPrincipal;
	private ContainerController contentorAgente;

	private DisplaySurface dsurf;
	private Object2DGrid espaco;
	private ArrayList<Object> listaDesenhar;
	
	private AgenteTrabalhador ag1;
	private AgenteTrabalhador ag2;
	private AgenteTrabalhador ag3;
	private AgenteTrabalhador ag4;
	
	Vector<AgenteTrabalhador> agentes;
	
	public static final boolean SEPARADOS = false;
	public static final int NUM_AGENTS = 3;
	
	/*********************
			FUNCOES 
	*********************/	
	@Override
	public String[] getInitParam()
	{
		
		String[] params = { "comprimento", "largura" };
		return params;
	}

	@Override
	public String getName() {
		return "Transportes";
	}
	
	@Override
	protected void launchJADE()
	{
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		contentorPrincipal = rt.createMainContainer(p1);

		if (SEPARADOS)
		{
			Profile p2 = new ProfileImpl();
			contentorAgente = rt.createAgentContainer(p2);
		} else {
			contentorAgente = contentorPrincipal;
		}
		
		mundo = new Mundo();
		agentes = new Vector<AgenteTrabalhador>();
		// CORRER OS AGENTES
		try
		{
			quadro = new Quadro(mundo.cidade.matriz);
			mundo.mostrarMundo();
			
			espaco = new Object2DGrid(mundo.cidade.colunas, mundo.cidade.linhas);
			
			ag1 = new AgenteTrabalhador("Agente1", 1, espaco);
			ag2 = new AgenteTrabalhador("Agente2", 4, espaco);
			ag3 = new AgenteTrabalhador("Agente3", 3, espaco);
			ag4 = new AgenteTrabalhador("Agente4", 2, espaco);
			
			System.out.println("1");
			
			ag1.setArguments(new String[] { "ping" });
			ag2.setArguments(new String[] { "pong" });
			ag3.setArguments(new String[] { "pong" });
			ag4.setArguments(new String[] { "pung" });
			
			System.out.println("2");
			
			ag1.executarMovimento(Auxiliar.caminhoCurto(quadro.matriz, 11, 35, 22, 27));
			ag2.executarMovimento(Auxiliar.caminhoCurto(quadro.matriz, 1, 1, 22, 24));
			ag3.executarMovimento(Auxiliar.caminhoCurto(quadro.matriz, 1, 9, 22, 24));
			ag4.executarMovimento(Auxiliar.caminhoCurto(quadro.matriz, 22, 27, 2, 4));
			
			System.out.println("3");
			
			agentes.addElement(ag1);
			agentes.addElement(ag2);
			agentes.addElement(ag3);
			agentes.addElement(ag4);
			
			for(AgenteTrabalhador at: agentes)
			{
				contentorAgente.acceptNewAgent(at.tr.nome, at);
			}
			
			System.out.println("4");
			
			for(AgenteTrabalhador at: agentes)
			{
				scheduleAgent(at);
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getStackTrace());
		}
	}

	@Override
	public void begin() { 
		super.begin();
		
		dsurf = new DisplaySurface(this, "AIAD");
		registerDisplaySurface("AIAD", dsurf);
		
		// construir o display
		listaDesenhar = new ArrayList<Object>();
		listaDesenhar.add(quadro);

		Object2DDisplay ecraAgentes = new Object2DDisplay(espaco);
		ecraAgentes.setObjectList(listaDesenhar);
		
		dsurf.addDisplayableProbeable(ecraAgentes, "Agentes");
		addSimEventListener(dsurf);
		
		dsurf.display();
		
		getSchedule().scheduleActionBeginning(1, this, "step");
		
		// contruir o modelo
		for(AgenteTrabalhador at: agentes)
		{
			espaco.putObjectAt(at.tr.coluna, at.tr.linha, at);
		}
		
		for(AgenteTrabalhador at: agentes)
		{
			listaDesenhar.add(at);
		}
	}

	public void step() {
		dsurf.updateDisplay();
	}
	
	public static void main(String[] args)
	{
		SimInit init = new SimInit();
		init.setNumRuns(10);
		init.loadModel(new Janela(), null, false);
	}
}
