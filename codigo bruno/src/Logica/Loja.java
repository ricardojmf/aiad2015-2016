package Logica;

import java.util.Vector;

public class Loja extends Local
{
	Vector<ProductoLoja> productos;
	
	public Loja(int li, int col)
	{
		super(li, col, 'S');
		productos = new Vector<ProductoLoja>();
	}
	
	public Loja(int li, int col, String nome)
	{
		super(li, col, 'S', nome);
		productos = new Vector<ProductoLoja>();
	}
	
}
