package Modelo;

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
		int auxiliar = producto.compareTo(obj.producto);
		
		if (auxiliar == 0)
		{
			return ( (quantidade < obj.quantidade) ? -1 : 1);
		}
		
		return auxiliar;
	}
}
