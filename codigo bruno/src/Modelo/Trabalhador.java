package Modelo;

import java.util.Vector;

public class Trabalhador extends Identidade
{
	public String nome;
	public int riqueza;
	
	public int velocidade;
	public int bateriaMax;
	public int bateria;
	public int cargaMax;
	public int carga;
	
	public int transporte; // 0 ate 4
	public boolean meioTransporte; // false: estradas ; true: ar
	public Vector<String> ferramentas;
	public Vector<Tarefa> tarefas;
	public Vector<Ranhura> contentor;
	
	private static int riquezaInicio = 1500;
	
	private void iniciarTransporte()
	{
		switch(transporte)
		{
		case (1):
			letra = 'C';
			velocidade = 3;
			bateriaMax = 500;
			cargaMax = 550;
			meioTransporte = false;
			ferramentas.addElement("f1");
			ferramentas.addElement("f2");
			break;
		case (2):
			letra = 'H';
			velocidade = 5;
			bateriaMax = 250;
			cargaMax = 100;
			meioTransporte = true;
			ferramentas.addElement("f1");
			break;
		case (3):
			letra = 'M';
			velocidade = 4;
			bateriaMax = 350;
			cargaMax = 300;
			meioTransporte = false;
			ferramentas.addElement("f3");
			ferramentas.addElement("tool1");
			break;
		case (4):
			letra = 'T';
			velocidade = 1;
			bateriaMax = 3000;
			cargaMax = 1000;
			meioTransporte = false;
			ferramentas.addElement("f2");
			ferramentas.addElement("f3");
			break;
		}
		
		bateria = bateriaMax;
		carga = 0;
	}
	
	public Trabalhador(String no, int tipoAgente)
	{
		super(0, 0);
		
		nome = no;
		riqueza = riquezaInicio;
		
		transporte = tipoAgente;
		ferramentas = new Vector<String>();
		iniciarTransporte();
		
		tarefas = new Vector<Tarefa>();
		contentor = new Vector<Ranhura>();
	}
	
	public Trabalhador(String no, int tipoAgente, int li, int col)
	{
		super(li, col);
		
		nome = no;
		riqueza = riquezaInicio;
		
		transporte = tipoAgente;
		ferramentas = new Vector<String>();
		iniciarTransporte();
		
		tarefas = new Vector<Tarefa>();
		contentor = new Vector<Ranhura>();
	}
	
	public int compareTo(Trabalhador obj)
	{
		return (nome.compareTo(obj.nome));
	}

	
}
