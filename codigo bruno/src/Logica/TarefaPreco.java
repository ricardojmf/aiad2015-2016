package Logica;

import java.util.Date;

public class TarefaPreco 
{
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
