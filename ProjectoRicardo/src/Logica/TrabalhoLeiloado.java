package Logica;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

public class TrabalhoLeiloado
{
	public int recompensa;
	public String detalhes;
	public boolean realizado;
	public Date tempoLimite;
	
	public Date tempoLeilao; // data para terminar leilao
	
	public Vector<Ranhura> pedido; // a entregar, obter
	public Ponto destino; // lugar a entregar, devolver obtido
	
	public Trabalhador pretendente;
	
	private static DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	
	public void definirObjectivo(Vector<Ranhura> request, Ponto dst)
	{
		pedido = request;
		destino = dst;
	}
	
	public void initTempoRealizacao(int h, int m, int s, int dia, int mes, int ano)
	{
		Calendar sl = new GregorianCalendar(ano, mes - 1, dia, h, m, s);
		tempoLimite = (Date) sl.getTime();
	}
	
	public void initTempoLeilao(int h, int m, int s, int dia, int mes, int ano)
	{
		Calendar sl = new GregorianCalendar(ano, mes - 1, dia, h, m, s);
		tempoLeilao = (Date) sl.getTime();
	}
	
	public void comecarTrabalho(int h, int m, int s, int dia, int mes, int ano)
	{
		initTempoRealizacao(h,m,s,dia,mes,ano);
		tempoLeilao = null;
		realizado = false;
	}
	
	public TrabalhoLeiloado(int rec,  Date tl)
	{
		recompensa = rec;
		detalhes = "";
		tempoLimite = tl;
		
		realizado = false;
	}
	
	public TrabalhoLeiloado(int rec, String det)
	{
		recompensa = rec;
		
		detalhes = det;
		realizado = false;
	}
	
	public TrabalhoLeiloado(int rec, String det, int h, int m, int s, int dia, int mes, int ano)
	{
		recompensa = rec;
		initTempoRealizacao(h,m,s,dia,mes,ano);
		
		detalhes = det;
		realizado = false;
	}
	
	public TrabalhoLeiloado(int rec, int h, int m, int s, int dia, int mes, int ano)
	{
		recompensa = rec;
		detalhes = "";
		initTempoRealizacao(h,m,s,dia,mes,ano);
		
		realizado = false;
	}
	
	public void novoTrabalhador(Trabalhador tr, int proposta)
	{
		if(proposta < recompensa)
		{
			recompensa = proposta;
			pretendente = tr;
		}
	}
	
	public void terminar()
	{
		realizado = true;
	}
	
	public int compareTo(TrabalhoLeiloado obj)
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
