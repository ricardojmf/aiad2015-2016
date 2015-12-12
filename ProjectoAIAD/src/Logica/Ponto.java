package Logica;

import java.util.Vector;

public class Ponto
{
	public int linha;
	public int coluna;
	
	public Ponto(int li, int col)
	{
		linha = li;
		coluna = col;
	}
	
	public Ponto obterLocalizacao()
	{
		return( new Ponto(this.linha, this.coluna) );
	}
	
	public Ponto obterTransposta()
	{
		return( new Ponto(this.coluna, this.linha) );
	}
	
	public String pontoToString()
	{
		return( linha + " " + coluna );
	}
	
	public boolean mesmaPosicao(Ponto p)
	{
		return( this.linha == p.linha && this.coluna == p.coluna );
	}
	
	public void set(int l, int c)
	{
		linha = l;
		coluna = c;
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
	
	public static Vector<Ponto> percursoCurtoLojas(Vector<String> mapa, boolean ar, Ponto origem, Vector<Loja> pontosDestinos)
	{
		Vector<Ponto> percursoMinimo1 = null;
		for(Loja re: pontosDestinos)
		{
			Vector<Ponto> percurso = null;
			
			if(ar)
			{
				percurso = Auxiliar.linhaRecta(origem.linha, origem.coluna,
						re.linha, re.coluna);
			}
			else
			{
				percurso = Auxiliar.caminhoCurto(mapa,
						origem.coluna, origem.linha,
						re.coluna, re.linha);
			}
			
			if (percursoMinimo1 == null)
			{
				percursoMinimo1 = new Vector<Ponto>();
				percursoMinimo1 = percurso;
			}
			else
			{
				if( percurso.size() < percursoMinimo1.size())
				{
					percursoMinimo1 = percurso;
				}
			}
		}
		
		return percursoMinimo1;
	}
	
	public static Vector<Ponto> percursoCurtoArmazens(Vector<String> mapa, boolean ar, Ponto origem, Vector<Armazem> pontosDestinos)
	{
		Vector<Ponto> percursoMinimo1 = null;
		for(Armazem re: pontosDestinos)
		{
			Vector<Ponto> percurso = null;
			
			if(ar)
			{
				percurso = Auxiliar.linhaRecta(origem.linha, origem.coluna,
						re.linha, re.coluna);
			}
			else
			{
				percurso = Auxiliar.caminhoCurto(mapa,
						origem.coluna, origem.linha,
						re.coluna, re.linha);
			}
			
			if (percursoMinimo1 == null)
			{
				percursoMinimo1 = new Vector<Ponto>();
				percursoMinimo1 = percurso;
			}
			else
			{
				if( percurso.size() < percursoMinimo1.size())
				{
					percursoMinimo1 = percurso;
				}
			}
		}
		
		return percursoMinimo1;
	}
	
	public static Vector<Ponto> percursoCurtoEstacoes(Vector<String> mapa, boolean ar, Ponto origem, Vector<Recarga> pontosDestinos)
	{
		Vector<Ponto> percursoMinimo1 = null;
		for(Recarga re: pontosDestinos)
		{
			Vector<Ponto> percurso = null;
			
			if(ar)
			{
				percurso = Auxiliar.linhaRecta(origem.linha, origem.coluna,
						re.linha, re.coluna);
			}
			else
			{
				percurso = Auxiliar.caminhoCurto(mapa,
						origem.coluna, origem.linha,
						re.coluna, re.linha);
			}
			
			if (percursoMinimo1 == null)
			{
				percursoMinimo1 = new Vector<Ponto>();
				percursoMinimo1 = percurso;
			}
			else
			{
				if( percurso.size() < percursoMinimo1.size())
				{
					percursoMinimo1 = percurso;
				}
			}
		}
		
		return percursoMinimo1;
	}

	public static Vector<Ponto> percursoCurtoDirecto(Vector<String> mapa, boolean ar, Ponto origem, Ponto destino)
	{
		Vector<Ponto> percursoDestino = null;
		if(ar)
		{
			percursoDestino = Auxiliar.linhaRecta(origem.linha, origem.coluna,
					destino.linha, destino.coluna);
		}
		else
		{
			percursoDestino = Auxiliar.caminhoCurto(mapa,
					origem.coluna, origem.linha,
					destino.coluna, destino.linha);
		}
		return percursoDestino;
	}	

	public static void verVector(Vector<Ponto> lista)
	{
		for(Ponto p: lista)
		{
			System.out.println(p.pontoToString());
		}
	}
}
