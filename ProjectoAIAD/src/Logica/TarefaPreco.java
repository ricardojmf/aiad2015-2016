package Logica;

import java.util.Date;

import jade.util.leap.Serializable;

public class TarefaPreco implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public TrabalhoPreco tb;
	public Date tempoAceite;
	
	public TarefaPreco(TrabalhoPreco t)
	{
		tb = t;
		tempoAceite = new Date(System.currentTimeMillis());
	}
	
	public int estadoTarefa()
	{
		int x = (tb.realizado) ? 1 : 0;
		return x;
	}
	
	
}
