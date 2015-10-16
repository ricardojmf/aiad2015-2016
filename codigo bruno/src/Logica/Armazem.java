package Logica;

import java.util.Vector;

public class Armazem extends Edificio
{
	public Vector<ProductoArmazenado> productos;
	
	public Armazem(int li, int col)
	{
		super(li, col, 'S');
		productos = new Vector<ProductoArmazenado>();
	}
	
	public Armazem(int li, int col, String nome)
	{
		super(li, col, 'S', nome);
		productos = new Vector<ProductoArmazenado>();
	}

}
