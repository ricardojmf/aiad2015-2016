package Logica;

import jade.util.leap.Serializable;

public class Producto implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String nome;
	public int preco;
	public int peso;
	
	public Producto(String nome, int peso)
	{
		this.nome = nome;
		this.preco = 0;
		this.peso = peso;
	}
	
	public Producto(String nome, int preco, int peso)
	{
		this.nome = nome;
		this.preco = preco;
		this.peso = peso;
	}
	
	public String toString()
	{
		return( nome + " " + peso + "kg " + preco + "€");
	}
	
	public int compareTo(Producto obj)
	{
		if (obj.nome.compareTo(this.nome) != 0)
		{
			return obj.nome.compareTo(this.nome);
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
