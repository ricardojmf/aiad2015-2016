package Logica;

import java.util.Vector;

public class Loja extends Local
{
	public Vector<Producto> productos;
	
	public Loja(int li, int col)
	{
		super(li, col, 'S');
		productos = new Vector<Producto>();
	}
	
	public Loja(int li, int col, String nome)
	{
		super(li, col, 'S', nome);
		productos = new Vector<Producto>();
	}
	
}
