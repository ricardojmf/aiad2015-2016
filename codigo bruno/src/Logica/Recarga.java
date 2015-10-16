package Logica;

import java.util.Vector;

public class Recarga extends Edificio
{
	Vector<ProductoLoja> productos;
	
	public Recarga(int li, int col)
	{
		super(li, col, 'R');
		productos = new Vector<ProductoLoja>();
	}
	
	public Recarga(int li, int col, String nome)
	{
		super(li, col, 'S', nome);
		productos = new Vector<ProductoLoja>();
	}
	
}
