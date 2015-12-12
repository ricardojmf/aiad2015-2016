package Logica;

public class Ranhura
{
	public Producto producto;
	public int quantidade;
	
	public Ranhura(Producto p, int q)
	{
		producto = p;
		quantidade = q;
	}
	
	public Ranhura(Producto p)
	{
		producto = p;
		quantidade = 0;
	}
	
	public int compareTo(Ranhura obj)
	{
		if (producto.nome.compareTo(obj.producto.nome) == 0)
		{
			if (quantidade == obj.quantidade)
			{
				return 0;
			}
			else
			{
				return( (quantidade < obj.quantidade) ? -1 : 1 );
			}
		}
		else
		{
			return( producto.nome.compareTo(obj.producto.nome) );
		}
	}
	
	public int obterPrecoTotal()
	{
		return producto.preco*quantidade ;
	}
	
	public int obterTamanhoTotal()
	{
		return producto.peso*quantidade ;
	}
	
	public String toString()
	{
		String s = producto.nome + " x" + quantidade;
		return s;
	}
	
	public String ranhuraToString()
	{
		String s = producto.nome + " " + quantidade;
		return s;
	}
}
