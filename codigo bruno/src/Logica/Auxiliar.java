package Logica;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Auxiliar
{
	public static String folder = "/usr/users2/mieic2012/ei12012/Documents/workspace/ProjectoAIAD/";
	//public static String folder = "C:\\Users\\Miguel Moreira\\Documents\\Eclipse Mars x64\\projects\\ProjectoAIAD\\";
		
	
	/*	MELHORAMENTO AO INPUT/OUTPUT	*/
	
	// INPUT
	public static int lerInteiro()
	{
		Scanner s = new Scanner(System.in);
		int r = s.nextInt();
		
		s.close();
		return r;
	}
	
	public static float lerReal()
	{
		Scanner s = new Scanner(System.in);
		float r = s.nextFloat();
		
		s.close();
		return r;
	}
	
	public static String lerString()
	{
		Scanner s = new Scanner(System.in);
		String r = s.nextLine();
		
		s.close();
		return r;
	}
	
	public static char lerChar()
	{
		Scanner s = new Scanner(System.in);
		String r1 = s.nextLine();
		char r = r1.charAt(0);
		
		s.close();
		return r;
	}
	
	// OUTPUT
	public static void writeln(String arg)
	{
		System.out.println(arg);
	}
	
	public static void writeln(int arg)
	{
		System.out.println(arg);
	}
	
	public static void writeln(double arg)
	{
		System.out.println(arg);
	}
	
	public static void writeln(float arg)
	{
		System.out.println(arg);
	}
	
	public static void writeln(char arg)
	{
		System.out.println(arg);
	}
	
	public static void write(String arg)
	{
		System.out.print(arg);
	}
	
	public static void write(int arg)
	{
		System.out.print(arg);
	}
	
	public static void write(double arg)
	{
		System.out.print(arg);
	}
	
	public static void write(float arg)
	{
		System.out.print(arg);
	}
	
	public static void write(char arg)
	{
		System.out.print(arg);
	}
	
	/*	FUNCOES RELACIONADAS COM STRINGS	*/
	
	// SUBSTITUI SRC[INDEX] POR 'VALUE'
	public static String substituir(String src, int index, char value)
	{
		String r = "";
		
		if (index >= 0 && index < src.length())
		{
			String antes = src.substring(0,index);
			String depois = src.substring(index+1, src.length());
			
			r = antes + value + depois;
		}
		
		return r;
	}
	
	// SUBSTITUI TODOS CARACTERES 'CHAVE' POR 'NOVO'
	public static String substituir(String src, char chave, char novo)
	{
		String r = "";
		for (int i = 0; i < src.length(); i++)
		{
			char aux = src.charAt(i);
			if(aux != chave)
			{
				r = r + aux;
			}
			else
			{
				r = r + novo;
			}
		}
		
		return r;
	}
	
	// ELIMINA O CARACTER SRC[VALUE]
	public static String eliminar(String src, int index)
	{
		String r = "";
		
		if (index >= 0 && index < src.length())
		{
			String antes = src.substring(0,index);
			String depois = src.substring(index+1, src.length());
			
			r = antes + depois;
		}
				
		return r;
	}
	
	// ELIMINA TODOS CARACTERES 'VALUE'
	public static String eliminar(String src, char value)
	{
		String r = "";
		for (int i = 0; i < src.length(); i++)
		{
			char aux = src.charAt(i);
			if(aux != value)
			{
				r = r + aux;
			}
		}
		
		return r;
	}
	
	/*	FUNCOES RELACIONADAS COM NUMEROS	*/
	
	public static int gerarInteiro(int max)
	{
	    return gerarInteiroVersao2(0, max);
	}
	
	// GERA NUMERO ENTRE [min, max]
	public static int gerarInteiroVersao1(int min, int max)
	{
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	// GERA NUMERO ENTRE [min, max[
	public static int gerarInteiroVersao2(int min, int max)
	{
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min)) + min;

	    return randomNum;
	}
	
	// GERA NUMERO ENTRE ]min, max]
	public static int gerarInteiroVersao3(int min, int max)
	{
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min)) + min + 1;

	    return randomNum;
	}
	
	private static int SIGN(double x)
	{
		return (x < 0 ? -1 : (x > 0 ? 1 : 0));
	}
	
	private static int SIGN(int x)
	{
		return (x < 0 ? -1 : (x > 0 ? 1 : 0));
	}
	
	public static Vector<Ponto> linhaRecta(int x1, int y1, int x2, int y2)
	{
		Vector<Ponto> r = new Vector<Ponto>();
		
		int	dx,dy,sx,sy;
		int	accum;
	 
		dx = x2 - x1;
		dy = y2 - y1;
	 
		sx = SIGN(dx);
		sy = SIGN(dy);
	 
		dx = Math.abs(dx);
		dy = Math.abs(dy);
	 
		x2 += sx;
		y2 += sy;
	 
		if(dx > dy)
		{
			accum = dx >> 1;
			do{
				r.addElement(new Ponto(x1, y1));
	 
				accum -= dy;
				if(accum < 0)
				{
					accum += dx;
					y1 += sy;
				}
	 
				x1 += sx;
			}while(x1 != x2);
		}else{
			accum = dy >> 1;
			do{
				r.addElement(new Ponto(x1, y1));
	 
				accum -= dx;
				if(accum < 0)
				{
					accum += dy;
					x1 += sx;
				}
	 
				y1 += sy;
			}while(y1 != y2);
		}
		return r;
	}
	
	private static class NodoPesquisa implements Comparable
	{
		public int valor;
		public int linha;
		public int coluna;
		
		public NodoPesquisa(int v, int l, int c)
		{
			valor = v;
			linha = l;
			coluna = c;
		}
		
		public NodoPesquisa(int l, int c)
		{
			valor = 0;
			linha = l;
			coluna = c;
		}
		
		public void ver()
		{
			writeln(linha + " , " + coluna + " (" + valor + ")");
		}

		@Override
		public int compareTo(Object o)
		{
			NodoPesquisa np = (NodoPesquisa) o;
			if (valor == np.valor)
			{
				if (linha == np.linha)
				{
					if (coluna == np.coluna)
					{
						return 0;
					}
					else
					{
						return ( (coluna < np.coluna) ? -1 : 1 );
					}
				}
				else
				{
					return ( (coluna < np.coluna) ? -1 : 1 );
				}
			}
			else
			{
				return ( (valor < np.valor) ? -1 : 1 );
			}
		}
	}
	
	public static Vector<Ponto> caminhoCurto(Vector<String> mapa, int x1, int y1, int x2, int y2)
	{
		Vector<Ponto> r = new Vector<Ponto>();
		
		char obstaculo = 'O';
		char estrada = ' ';
		
		int maxLinha = mapa.size();
		int maxColuna = mapa.elementAt(0).length();
		
		Vector<Vector<NodoPesquisa> > nodos = new Vector<Vector<NodoPesquisa> >();
		
		for (int linha = 0; linha < mapa.size(); linha ++)
		{
			String s = mapa.elementAt(linha);
			
			Vector<NodoPesquisa> nd = new Vector<NodoPesquisa>();
			for(int coluna = 0; coluna < s.length(); coluna++)
			{
				NodoPesquisa np = new NodoPesquisa(linha, coluna);
				
				char c = s.charAt(coluna);
				if (c == obstaculo)
				{
					np.valor = -2;
				}
				else
				{
					np.valor = -1;
				}
				
				nd.addElement(np);
			}
			nodos.addElement(nd);
		}
		
		PriorityQueue<NodoPesquisa> queue = new PriorityQueue<NodoPesquisa>();
		NodoPesquisa inicial = nodos.elementAt(x1).elementAt(y1);
		inicial.valor = 0;
		
		queue.add(inicial);		
		NodoPesquisa agora = inicial;
		
		boolean condicaoParagem = true;
		while(!queue.isEmpty() && condicaoParagem)
		{
			agora = queue.poll();
			
			int linha = agora.linha;
			int coluna = agora.coluna;
			
			Vector<NodoPesquisa> nxt = new Vector<NodoPesquisa>();
			
			// cima
			if ( (linha + 1) < maxLinha)
			{
				NodoPesquisa tmp = nodos.elementAt(linha + 1).elementAt(coluna);
				if (tmp.valor == -1 || tmp.valor > agora.valor)
				{
					nxt.addElement(tmp);
				}
			}
			
			//baixo
			if ( (linha - 1) >= 0)
			{
				NodoPesquisa tmp = nodos.elementAt(linha - 1).elementAt(coluna);
				if (tmp.valor == -1 || tmp.valor > agora.valor)
				{
					nxt.addElement(tmp);
				}
			}
			
			// direita
			if ( (coluna + 1)  < maxColuna)
			{
				NodoPesquisa tmp = nodos.elementAt(linha).elementAt(coluna + 1);
				if (tmp.valor == -1 || tmp.valor > agora.valor)
				{
					nxt.addElement(tmp);
				}
			}
			
			// esquerda
			if ( (coluna - 1) >= 0)
			{
				NodoPesquisa tmp = nodos.elementAt(linha).elementAt(coluna - 1);
				if (tmp.valor == -1 || tmp.valor > agora.valor)
				{
					nxt.addElement(tmp);
				}
			}
			
			for(NodoPesquisa np: nxt)
			{
				np.valor = agora.valor + 1;
				if(np.coluna == y2 && np.linha == x2)
				{
					condicaoParagem = false;
					agora = np;
				}
				else
				{
					queue.add(np);
				}
			}
		}
		
		//writeln("--------------------------------------------");
		
//		for (Vector<NodoPesquisa> np: nodos)
//		{
//			for(NodoPesquisa nd: np)
//			{
//				if (nd.valor == -2)
//				{
//					Auxiliar.write('O');
//				}
//				else if (nd.valor == -1)
//				{
//					Auxiliar.write(' ');
//				}
//				else
//				{
//					Auxiliar.write(nd.valor);
//				}
//				
//			} Auxiliar.writeln("");
//		}
		
		r.addElement(new Ponto(agora.linha, agora.coluna));
		while(!(agora.linha == x1 && agora.coluna == y1))
		{
			Vector<NodoPesquisa> nxt = new Vector<NodoPesquisa>();
			int linha = agora.linha;
			int coluna = agora.coluna;
			
			// cima
			if ( (linha + 1) < maxLinha)
			{
				NodoPesquisa tmp = nodos.elementAt(linha + 1).elementAt(coluna);
				if (tmp.valor == (agora.valor - 1))
				{
					nxt.addElement(tmp);
				}
			}
			
			//baixo
			if ( (linha - 1) >= 0)
			{
				NodoPesquisa tmp = nodos.elementAt(linha - 1).elementAt(coluna);
				if (tmp.valor == (agora.valor - 1))
				{
					nxt.addElement(tmp);
				}
			}
			
			// direita
			if ( (coluna + 1)  < maxColuna)
			{
				NodoPesquisa tmp = nodos.elementAt(linha).elementAt(coluna + 1);
				if (tmp.valor == (agora.valor - 1))
				{
					nxt.addElement(tmp);
				}
			}
			
			// esquerda
			if ( (coluna - 1) >= 0)
			{
				NodoPesquisa tmp = nodos.elementAt(linha).elementAt(coluna - 1);
				if (tmp.valor == (agora.valor - 1))
				{
					nxt.addElement(tmp);
				}
			}
			
			agora = nxt.elementAt(0);
			r.addElement(new Ponto(agora.linha, agora.coluna));
		}
		
		Vector<Ponto> rr = new Vector<Ponto>();
		for(int i = r.size() - 1; i >= 0; i--)
		{
			rr.addElement(r.elementAt(i));
		}
		
		return rr;
	}
}
