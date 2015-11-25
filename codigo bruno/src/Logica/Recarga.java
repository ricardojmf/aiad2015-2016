package Logica;

public class Recarga extends Local
{
	public int preco;
	
	public Recarga(int li, int col, int p)
	{
		super(li, col, 'R');
		preco = p;
	}
	
	public Recarga(int li, int col, String nome, int p)
	{
		super(li, col, 'S', nome);
		preco = p;
	}
	
}
