package Logica;

public class ProductoArmazenado
{
	public Producto producto;
	public Trabalhador trabalhador;
	
	public ProductoArmazenado(Producto arg1, Trabalhador arg2)
	{
		producto = arg1;
		trabalhador = arg2;
	}
	
	public int compareTo(ProductoArmazenado obj)
	{
		return( trabalhador.nome.compareTo(obj.trabalhador.nome) );
	}
}
