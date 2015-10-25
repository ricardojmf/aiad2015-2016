package Modelo;

import java.util.Vector;

public class Produzir // accao
{
	public Vector<Producto> pedido;
	public Producto recompensa;
	public int quantidade;
	public Vector<String> ferramentas;
	
	public Produzir(Vector<Producto> request, Vector<String> fer, Producto rec, int q)
	{
		pedido = request;
		fer = ferramentas;
		recompensa = rec;
		quantidade = q;
	}
	
	public boolean pronto(Trabalhador trabalhador)
	{
		return false;
	}
}
