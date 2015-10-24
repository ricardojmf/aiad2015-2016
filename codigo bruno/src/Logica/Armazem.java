package Logica;

import java.util.Vector;

public class Armazem extends Local
{
	public Vector<ProductoArmazenado> productos;
	
	public Armazem(int li, int col)
	{
		super(li, col, 'W');
		productos = new Vector<ProductoArmazenado>();
	}
	
	public Armazem(int li, int col, String nome)
	{
		super(li, col, 'W', nome);
		productos = new Vector<ProductoArmazenado>();
	}

}
