package Modelo;

import java.util.Vector;

public class Lixeira extends Local
{
	Vector<ProductoLoja> productos;
	
	public Lixeira(int li, int col)
	{
		super(li, col, 'G');
		productos = new Vector<ProductoLoja>();
	}
	
	public Lixeira(int li, int col, String nome)
	{
		super(li, col, 'G', nome);
		productos = new Vector<ProductoLoja>();
	}
	
}
