package Logica;

import java.util.Date;

import jade.util.leap.Serializable;

public class TarefaLeiloada implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public TrabalhoLeiloado tb;
	public Date tempoAceite;
	
	public TarefaLeiloada(TrabalhoLeiloado t)
	{
		tb = t;
		tempoAceite = new Date(System.currentTimeMillis());
	}
	
	public int estadoTarefa()
	{
		Date data = new Date(System.currentTimeMillis());
		
		int x = data.compareTo(tb.tempoLimite);
		return x;
	}
}
