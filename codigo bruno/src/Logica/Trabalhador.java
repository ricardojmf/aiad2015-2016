package Logica;

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
	
	private void inicializarTransporte()
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
	
	private void inicializar(String no, int tipoAgente)
	{
		nome = no;
		riqueza = riquezaInicio;
		
		transporte = tipoAgente;
		
		ferramentas = new Vector<String>();
		inicializarTransporte();
		
		tarefas = new Vector<Tarefa>();
		contentor = new Vector<Ranhura>();
	}
	
	public Trabalhador(String no, int tipoAgente)
	{
		super(0, 0);
		
		inicializar(no, tipoAgente);
	}
	
	public Trabalhador(String no, int tipoAgente, int li, int col)
	{
		super(li, col);
		
		inicializar(no, tipoAgente);
	}
	
	public int compareTo(Trabalhador obj)
	{
		return (nome.compareTo(obj.nome));
	}

	public void verDetalhesTransporte()
	{
		Auxiliar.writeln("Detalhes Transporte");
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Velocidade: " + velocidade);
		Auxiliar.writeln("Bateria Max: " + bateriaMax);
		Auxiliar.writeln("Carga Max: " + cargaMax);
		Auxiliar.writeln("Tipo de Transporte: " + transporte);
		Auxiliar.writeln("Meio de Transporte: " + meioTransporte);
		
		for(String s: ferramentas)
		{
			Auxiliar.writeln("Ferramenta: " + s);
		}
		
	}
	
	public void verContentor()
	{
		Auxiliar.writeln("Contentor");
		Auxiliar.writeln("--------------------------------------");
		for(Ranhura ra: contentor)
		{
			Auxiliar.writeln(ra.producto.nome + " x" + ra.quantidade );
		}
	}
	
	public void verActual()
	{
		Auxiliar.writeln("Actual");
		Auxiliar.writeln("--------------------------------------");
		Auxiliar.writeln("Coordenadas: (" + this.linha + " , " + this.coluna + ")");
		Auxiliar.writeln("Riqueza: " + riqueza);
		Auxiliar.writeln("Carga: " + carga);
		Auxiliar.writeln("Bateria: " + bateria);
	}
	
	public void ver()
	{
		verDetalhesTransporte();
		verActual();
		verContentor();
	}
}
