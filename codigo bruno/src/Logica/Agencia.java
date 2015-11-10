package Logica;

import java.util.Vector;

public class Agencia extends Local
{
	Vector<Trabalho> trabalhos;
	
	public Agencia(int li, int col)
	{
		super(li, col, 'A');
		trabalhos = new Vector<Trabalho>();
	}
	
	public Agencia(int li, int col, String nome)
	{
		super(li, col, 'A', nome);
		trabalhos = new Vector<Trabalho>();
	}
}
