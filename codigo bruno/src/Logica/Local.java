package Logica;

public class Local extends Identidade
{
	public String nome;
	
	public Local(int li, int col, char let)
	{
		super(li, col, let);
		nome = "";
	}
	
	public Local(int li, int col, char let, String no)
	{
		super(li, col, let);
		nome = no;
	}
	
	public int compareTo(Local obj)
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
