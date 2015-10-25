package Modelo;

public class Identidade extends Ponto
{
	public char letra;

	public Identidade(int li, int col)
	{
		super(li, col);
		letra = ' ';
	}
	
	public Identidade(int li, int col, char let)
	{
		super(li, col);
		letra = let;
	}
	
	public int compareTo(Identidade obj) // posicao
	{
		if (this.linha == obj.linha)
		{
			if (this.coluna == obj.coluna)
			{
				return 0;
			}
			else
			{
				return ( (this.coluna < obj.coluna) ? -1:1 );
			}
		}
		else
		{
			return ( (this.linha < obj.linha) ? -1:1 );
		}
	}
	
	public int compareToAbsolute(Identidade obj)
	{
		if (this.letra == obj.letra)
		{
			return( compareTo(obj) );
		}
		else
		{
			return ( (this.letra < obj.letra) ? -1:1 );
		}
	}
}
