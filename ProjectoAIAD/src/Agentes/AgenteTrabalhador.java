package Agentes;

import java.awt.Color;
import java.util.Vector;

import Logica.Auxiliar;
import Logica.Ponto;
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
	
	public AgenteTrabalhador(String nome, int tipoTransporte, Object2DGrid space)
	{
		super();
		espaco = space;
		
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
		
		//Auxiliar.writeln("AgenteTrabalhador setup() 1");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		sd.setType("Agente " + tipo);
		dfd.addServices(sd);
		
		
		//Auxiliar.writeln("AgenteTrabalhador setup() 2");
		
		try
		{
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		//Auxiliar.writeln("AgenteTrabalhador setup() 3");
	}
	
	@Override
	public void draw(SimGraphics arg0) {
		arg0.setDrawingCoordinates(getX() * arg0.getCurWidth(), getY() * arg0.getCurHeight(), 0);
		arg0.drawFastRect(Color.red);
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
		addBehaviour(new ComportamentoMovimento(percurso));
	}
	
	public class ComportamentoMovimento extends SimpleBehaviour
	{
		private static final long serialVersionUID = 1L;
		
		Vector<Ponto> percurso;
		int contador;
		
		public ComportamentoMovimento(Vector<Ponto> novoPercurso)
		{
			percurso = novoPercurso;
			contador = 0;
		}
		
		@Override
		public void action()
		{
			if(contador == tr.velocidade && tr.bateria > 0)
			{
				tr.bateria--;
				contador = 0;
				
				Ponto p = percurso.elementAt(0);
				percurso.removeElementAt(0);
				
				espaco.putObjectAt(tr.linha, tr.coluna, null);
				tr.linha = p.linha;
				tr.coluna = p.coluna;
				espaco.putObjectAt(tr.linha, tr.coluna, this);
			}
			else
			{
				contador++;
			}
		}

		@Override
		public boolean done()
		{
			return ( percurso.size() == 0 || tr.bateria == 0);
		}
	}
	
	class ComportamentoDefault extends SimpleBehaviour {

		private int n = 0;

		public ComportamentoDefault(Agent a)
		{
			super(a);
		}

		public void action()
		{
			ACLMessage msg = blockingReceive();
			if (msg.getPerformative() == ACLMessage.INFORM) {
				System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
				// cria resposta
				ACLMessage reply = msg.createReply();
				// preenche conteudo da mensagem
				if (msg.getContent().equals("ping"))
					reply.setContent("pong");
				else
					reply.setContent("ping");
				// envia mensagem
				send(reply);
			}
		}

		public boolean done() {
			return n == 10;
		}
	}

	
	
}
