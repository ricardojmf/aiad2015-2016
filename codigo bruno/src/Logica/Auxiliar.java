package Logica;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Auxiliar
{
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
	
	public static Vector<Ponto> linhaRecta(int x1, int y1, int x2, int y2)
	{
		Vector<Ponto> r = new Vector<Ponto>();
		
		int xi = x1;
		int yi = y1;
		int xf = x2;
		int yf = y2;
		
		int a, b, d, inc1, inc2, y, yIncrease;
		boolean slope = false;
		a = Math.abs(xf - xi);
		b = Math.abs(yf - yi);
		yIncrease = 1;
		
		if(b > a) // first point is on the right.
		{
			int tmp = xi;
			xi = yi;
			yi = tmp;
			
			tmp = yf;
			yf = xf;
			xf = tmp;
			
			tmp = a;
			a = b;
			b = tmp;			
			
			slope = true;
		}
		if(xi > xf)
		{
			int tmp = xi;
			xi = xf;
			xf = tmp;
			
			tmp = yi;
			yi = yf;
			yf = tmp;
		}
		if(yi > yf) // first point is higher than the second
		{
			yIncrease = -1;
		}
		
		inc2 = 2*b;
		d = inc2 - a;
		inc1 = d - a;
		y = yi;
		
		for(int x = xi; x <= xf; x++)
		{
			if(d <= 0)
			{
				d += inc2;
			}
			else
			{
				d += inc1;
				y += yIncrease;
			}
			
			if(slope)
			{
				r.addElement(new Ponto(y, x));
			}
			else
			{
				r.addElement(new Ponto(x, y));
			}
		}
		
		if (r.size() > 1)
		{
			//r.removeElementAt(0);
			//r.removeElementAt(r.size() - 1);
		}
		return r;
	}
	
	// mapa: vazio ( )
	public static Vector<Ponto> caminho(int x1, int y1, int x2, int y2)
	{
		Vector<Ponto> r = new Vector<Ponto>();
		
		int dx = x2 - x1;
		int dy = y2 - y1;
		
		double m = dy / dx;
		
		if (dx > 0)
		{
			for(int x = x1, y = y1; x <= x2; x++, y+=m)
			{
				writeln(x + " " + y);
				r.addElement(new Ponto(x, y));
			}
		}
		else
		{
			for(int x = x2, y = y1; x <= x1; x--, y+=m)
			{
				writeln(x + " " + y);
				r.addElement(new Ponto(x, y));
			}
		}
		
		
		
		return r;
	}
}
