package Logica;

import java.util.Date;

public class TarefaLeiloada
{
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
