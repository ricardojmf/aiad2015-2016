package Logica;

import java.util.Vector;

public class Lixeira extends Local
{	
	public Lixeira(int li, int col)
	{
		super(li, col, 'G');
	}
	
	public Lixeira(int li, int col, String nome)
	{
		super(li, col, 'G', nome);
	}
	
}
