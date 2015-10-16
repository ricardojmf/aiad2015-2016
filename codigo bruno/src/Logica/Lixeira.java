package Logica;

import java.util.Vector;

public class Lixeira extends Edificio
{
	Vector<ProductoLoja> productos;
	
	public Lixeira(int li, int col)
	{
		super(li, col, 'S');
		productos = new Vector<ProductoLoja>();
	}
	
	public Lixeira(int li, int col, String nome)
	{
		super(li, col, 'S', nome);
		productos = new Vector<ProductoLoja>();
	}
	
}
