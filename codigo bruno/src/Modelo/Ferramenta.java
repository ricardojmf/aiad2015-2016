package Modelo;

public class Ferramenta
{
	public String nome;
	
	public Ferramenta(String arg)
	{
		nome = arg;
	}
	
	public int compareTo(Ferramenta obj)
	{
		return ( this.nome.compareTo(obj.nome) );
	}
}
