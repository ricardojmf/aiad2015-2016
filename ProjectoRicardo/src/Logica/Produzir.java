package Logica;

import java.util.Vector;

public class Produzir // accao
{
	public Vector<Ranhura> pedido;
	public Producto recompensa;
	public int quantidade;
	public Vector<String> ferramentas;
	
	public Produzir(Vector<Ranhura> request, Vector<String> fer, Producto rec, int q)
	{
		pedido = request;
		ferramentas = fer;
		recompensa = rec;
		quantidade = q;
	}
	
	public void ver()
	{
		System.out.println("Produzir");
		System.out.println("---------------------------------");
		for(Ranhura ra: pedido)
		{
			Auxiliar.writeln(ra.producto.nome + " x" + ra.quantidade );
		}
		
		for(String s: ferramentas)
		{
			Auxiliar.writeln("Ferramenta: " + s);
		}
		
		Auxiliar.writeln("Recompensa: " + recompensa.nome + " x" + quantidade );
	}
}
