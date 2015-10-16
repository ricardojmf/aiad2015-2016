package Logica;

public class Producto
{
	public String nome;
	public String detalhes;
	public int peso;
	
	public Producto(String arg1, int arg2)
	{
		nome = arg1;
		detalhes = "";
		peso = arg2;
	}
	
	public Producto(String arg1, String arg2, int arg3)
	{
		nome = arg1;
		detalhes = arg2;
		peso = arg3;
	}
	
	public int compareTo(Producto obj)
	{
		if (obj.nome == this.nome)
		{
			return 0;
		}
		else
		{
			if (obj.peso == this.peso)
			{
				return 0;
			}
			else
			{
				if (this.peso < obj.peso)
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
		}
	}
}
