package Logica;

public class ProductoArmazenado
{
	public Producto producto;
	public Agente agente;
	
	public ProductoArmazenado(Producto arg1, Agente arg2)
	{
		producto = arg1;
		agente = arg2;
	}
	
	public int compareTo(ProductoArmazenado obj)
	{
		return( agente.nome.compareTo(obj.agente.nome) );
	}
}
