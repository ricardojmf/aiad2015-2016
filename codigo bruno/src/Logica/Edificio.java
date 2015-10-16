package Logica;

public class Edificio extends Identidade
{
	public String nome;
	
	public Edificio(int li, int col, char let)
	{
		super(li, col, let);
		nome = "";
	}
	
	public Edificio(int li, int col, char let, String no)
	{
		super(li, col, let);
		nome = no;
	}
	
	public int compareTo(Edificio obj)
	{
		if (this.nome == obj.nome)
		{
			return(super.compareTo(obj));
		}
		else
		{
			return ( this.nome.compareTo(obj.nome) );
		}
	}
}
