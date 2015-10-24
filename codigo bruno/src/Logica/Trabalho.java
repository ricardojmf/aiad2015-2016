package Logica;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Trabalho
{
	public int recompensa;
	public String detalhes;
	public boolean realizado;
	public Date tempoLimite;
	
	public Vector<Ranhura> pedido; // a entregar, obter
	public Ponto destino; // lugar a entregar, devolver obtido
	
	private static DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	
	private void inicializar()
	{
		detalhes = "";
		realizado = false;
	}
	
	public void initTempoRealizacao(int h, int m, int s, int dia, int mes, int ano)
	{
		Calendar sl = new GregorianCalendar(ano, mes - 1, dia, h, m, s);
		tempoLimite = (Date) sl.getTime();
	}
	
	public Trabalho(int rec, Vector<Ranhura> request, Ponto dst, Date tl)
	{
		recompensa = rec;
		tempoLimite = tl;
		inicializar();
		pedido = request;
		destino = dst;
	}
	
	public Trabalho(int rec, String det, Vector<Ranhura> request, Ponto dst, int h, int m, int s, int dia, int mes, int ano)
	{
		recompensa = rec;
		initTempoRealizacao(h,m,s,dia,mes,ano);
		
		detalhes = det;
		realizado = false;
		pedido = request;
		destino = dst;
	}
	
	public Trabalho(int rec, Vector<Ranhura> request, Ponto dst, int h, int m, int s, int dia, int mes, int ano)
	{
		recompensa = rec;
		initTempoRealizacao(h,m,s,dia,mes,ano);
		
		inicializar();
		pedido = request;
		destino = dst;
	}
	
	public void terminar()
	{
		realizado = true;
	}
	
	public int compareTo(Trabalho obj)
	{
		if (recompensa == obj.recompensa)
		{
			if (realizado == obj.realizado)
			{
				return (tempoLimite.compareTo(obj.tempoLimite));
			}
			else
			{
				return ((!realizado) ? -1:1);
			}
		}
		else
		{
			return ( (recompensa < obj.recompensa) ? -1:1 );
		}
	}
	
	public void verInformacao()
	{
		System.out.println("Data finalizar trabalho: " + df.format(tempoLimite));
		System.out.println("Recompensa: " + recompensa + "€ (" + (realizado ? "Finalizado" : "Em execucao")+ ")");
		System.out.println("-------");
		System.out.println("Detalhes: \"" + detalhes + "\"");
	}
}
