package Modelo;

import java.util.Date;

public class Tarefa 
{
	public Trabalho tb;
	public Date tempoAceite;
	
	public Tarefa(Trabalho t)
	{
		tb = t;
	}
	
	public void initTempoAceitacao2()
	{
		tempoAceite = new Date(System.currentTimeMillis());
	}
	
	public int estadoTarefa()
	{
		Date data = new Date(System.currentTimeMillis());
		
		int x = data.compareTo(tb.tempoLimite);
		return x;
	}
	
	
}
