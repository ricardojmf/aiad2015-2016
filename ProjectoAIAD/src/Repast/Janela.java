package Repast;

import Agentes.AgenteTrabalhador;
import Logica.Auxiliar;
import Logica.Mundo;

import java.util.ArrayList;
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
	private int comprimento = 100;
	private int largura = 100;
	public static final int NUM_AGENTS = 2;
	
	Mundo cidade;
	
	// CLASSES REPAST
	private ContainerController contentorPrincipal;
	private ContainerController contentorAgente;

	DisplaySurface dsurf;
	Object2DGrid espaco;
	ArrayList<Object> listaDesenhar;
	
	AgenteTrabalhador ag1;
	AgenteTrabalhador ag2;
	
	public static final boolean SEPARADOS = false;
	
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
		
		cidade = new Mundo();
		
		// CORRER OS AGENTES
		try
		{
			quadro = new Quadro(cidade.cidade.matriz);
			
			comprimento = quadro.COMPRIMENTO;
			largura = quadro.LARGURA;
			
			espaco = new Object2DGrid(largura, comprimento);
			
			ag1 = new AgenteTrabalhador("Bruno", 1, espaco);
			ag1.executarMovimento(Auxiliar.caminhoCurto(quadro.matriz, 11, 35, 22, 27));
			ag1.setArguments(new String[] { "ping" });
			
			ag2 = new AgenteTrabalhador("Moreira", 2, espaco);
			ag2.executarMovimento(Auxiliar.caminhoCurto(quadro.matriz, 1, 1, 22, 24));
			ag2.setArguments(new String[] { "pong" });
			
			contentorAgente.acceptNewAgent(ag1.tr.nome, ag1);
			contentorAgente.acceptNewAgent(ag2.tr.nome, ag2);
			
			scheduleAgent(ag1);
			scheduleAgent(ag2);
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
		
		espaco.putObjectAt(ag1.tr.coluna, ag1.tr.linha, ag1);
		listaDesenhar.add(ag1);
		espaco.putObjectAt(ag2.tr.coluna, ag2.tr.linha, ag2);
		listaDesenhar.add(ag2);
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
