package Logica;

import java.util.Vector;

public class ContentorArmazem
{
	public Vector<ProductoArmazenado> contentor;
	public Trabalhador trabalhador;
	
	public ContentorArmazem(Trabalhador arg)
	{
		contentor = new Vector<ProductoArmazenado>();
		trabalhador = arg;
	}
	
	public int compareTo(ContentorArmazem obj)
	{
		return( trabalhador.nome.compareTo(obj.trabalhador.nome) );
	}
	
	public ProductoArmazenado existeProducto(Producto p)
	{
		for(ProductoArmazenado pa: contentor)
		{
			if(pa.producto.nome.equals(p.nome))
			{
				return pa;
			}
		}
		return null;
	}
}
