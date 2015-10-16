package Logica;

import java.util.Vector;

public class Agente extends Identidade
{
	String nome;
	int riqueza;
	
	int velocidade;
	int bateriaMax;
	int bateria;
	int cargaMax;
	int carga;
	
	int transporte; // 0 ate 4
	boolean meioTransporte; // false: estradas ; true: ar
	Vector<String> ferramentas;
	Vector<Tarefa> tarefas;
	Vector<Ranhura> contentor;
	
	private static int riquezaInicio = 1500;
	
	private void iniciarTransporte()
	{
		switch(transporte)
		{
		case (1):
			velocidade = 3;
			bateriaMax = 500;
			cargaMax = 550;
			meioTransporte = false;
			ferramentas.addElement("f1");
			ferramentas.addElement("f2");
			break;
		case (2):
			velocidade = 5;
			bateriaMax = 250;
			cargaMax = 100;
			meioTransporte = true;
			ferramentas.addElement("f1");
			break;
		case (3):
			velocidade = 4;
			bateriaMax = 350;
			cargaMax = 300;
			meioTransporte = false;
			ferramentas.addElement("f3");
			ferramentas.addElement("tool1");
			break;
		case (4):
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
	
	public Agente(String no, int tipoAgente, char let)
	{
		super(0, 0, let);
		
		nome = no;
		riqueza = riquezaInicio;
		
		transporte = tipoAgente;
		ferramentas = new Vector<String>();
		iniciarTransporte();
		
		tarefas = new Vector<Tarefa>();
		contentor = new Vector<Ranhura>();
	}
	
	public Agente(String no, int tipoAgente, int li, int col, char let)
	{
		super(li, col, let);
		
		nome = no;
		riqueza = riquezaInicio;
		
		transporte = tipoAgente;
		ferramentas = new Vector<String>();
		iniciarTransporte();
		
		tarefas = new Vector<Tarefa>();
		contentor = new Vector<Ranhura>();
	}
	
	public int compareTo(Agente obj)
	{
		return (nome.compareTo(obj.nome));
	}
}
