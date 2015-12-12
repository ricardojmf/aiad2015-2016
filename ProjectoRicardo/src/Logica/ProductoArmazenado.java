package Logica;

public class ProductoArmazenado
{
	public Producto producto;
	public int quantidade;
	
	public ProductoArmazenado(Producto arg1, int arg2)
	{
		producto = arg1;
		quantidade = arg2;
	}
	
	public String productoArmazeadoToString()
	{
		String s = producto.nome + " " + quantidade;
		return s;
	}
}
