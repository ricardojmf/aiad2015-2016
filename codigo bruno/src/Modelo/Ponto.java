package Modelo;

public class Ponto
{
	public int linha;
	public int coluna;
	
	public Ponto(int li, int col)
	{
		linha = li;
		coluna = col;
	}
	
	public int compareTo(Ponto obj) // posicao
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
}
