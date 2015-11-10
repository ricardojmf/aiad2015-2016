package Logica;

import java.util.Vector;

public class Armazem extends Local
{
	public Vector<ContentorArmazem> clientes;
	
	public Armazem(int li, int col)
	{
		super(li, col, 'W');
		clientes = new Vector<ContentorArmazem>();
	}
	
	public Armazem(int li, int col, String nome)
	{
		super(li, col, 'W', nome);
		clientes = new Vector<ContentorArmazem>();
	}

}
