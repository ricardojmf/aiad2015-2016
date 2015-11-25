package Logica;

import java.util.Vector;

public class Agencia extends Local
{
	Vector<TrabalhoPreco> trabalhosPrecos;
	Vector<TrabalhoLeiloado> trabalhosLeiloados;
	
	public Agencia(int li, int col)
	{
		super(li, col, 'A');
		trabalhosPrecos = new Vector<TrabalhoPreco>();
		trabalhosLeiloados = new Vector<TrabalhoLeiloado>();
	}
	
	public Agencia(int li, int col, String nome)
	{
		super(li, col, 'A', nome);
		trabalhosPrecos = new Vector<TrabalhoPreco>();
		trabalhosLeiloados = new Vector<TrabalhoLeiloado>();
	}
}
