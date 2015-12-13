package Logica;

import java.util.Date;
import java.util.Vector;

import jade.util.leap.Serializable;

public class TrabalhoPreco implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int recompensa;
	public String detalhes;
	public boolean realizado;
	
	public Vector<Ranhura> pedido; // a entregar, obter
	public Ponto destino; // lugar a entregar, devolver obtido
	
	private void inicializar()
	{
		realizado = false;
	}
	
	public void definirObjectivo(Vector<Ranhura> request, Ponto dst)
	{
		pedido = request;
		destino = dst;
	}
	
	public TrabalhoPreco(int rec,  Date tl)
	{
		recompensa = rec;
		detalhes = "";
		
		inicializar();
	}
	
	public TrabalhoPreco(int rec, String det, int h, int m, int s, int dia, int mes, int ano)
	{
		recompensa = rec;
		
		detalhes = det;
		realizado = false;
	}
	
	public TrabalhoPreco(int rec, int h, int m, int s, int dia, int mes, int ano)
	{
		recompensa = rec;
		detalhes = "";
		
		inicializar();
	}
	
	public void terminar()
	{
		realizado = true;
	}
	
	public int compareTo(TrabalhoPreco obj)
	{
		if (recompensa == obj.recompensa)
		{
			if (realizado == obj.realizado)
			{
				return (0);
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
		System.out.println("Recompensa: " + recompensa + "€ (" + (realizado ? "Finalizado" : "Em execucao")+ ")");
		System.out.println("-------");
		System.out.println("Detalhes: \"" + detalhes + "\"");
	}
}
