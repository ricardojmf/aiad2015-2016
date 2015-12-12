package Consola;

import java.util.Vector;

import Logica.*;

public class Main
{
	public static void mostrarMatriz(Vector<Ponto> vc)
	{
		char[][] mat;
		mat = new char[10][10];
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				mat[i][j] = ' ';
			}
		}
		
		mat[vc.elementAt(0).linha][vc.elementAt(0).coluna] = 'X';
		mat[vc.elementAt(vc.size() - 1).linha][vc.elementAt(vc.size() - 1).coluna] = 'Y';
		
		int ppp = 65;
		for(Ponto p: vc)
		{
			mat[p.linha][p.coluna] = (char)ppp;
			ppp++;
		}
		
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				System.out.print(mat[i][j]);
			}
			System.out.println();
		}
	}
	
	public void executarLinhaRecta()
	{
		int linha1 = 7;
		int coluna1 = 7;
		int linha2 = 2;
		int coluna2 = 1;
		
		for(int k = 0; k < 10; k++)
		{
			linha1 = Auxiliar.gerarInteiro(10);
			coluna1 = Auxiliar.gerarInteiro(10);
			linha2 = Auxiliar.gerarInteiro(10);
			coluna2 = Auxiliar.gerarInteiro(10);
			
			Auxiliar.writeln("(" + linha1 +", " + coluna1 + ")->(" + linha2 +", " + coluna2 + ")");
			
			Vector<Ponto> vc = Auxiliar.linhaRecta(linha1, coluna1, linha2, coluna2);
			mostrarMatriz(vc);
		}
	}
	
	public void executarCaminhoCurto(Mapa mp)
	{
		int linha1 = 2;
		int coluna1 = 2;
		int linha2 = 3;
		int coluna2 = 7;
		
		Auxiliar.writeln("(" + linha1 +", " + coluna1 + ")->(" + linha2 +", " + coluna2 + ")");
		Vector<Ponto> r = Auxiliar.caminhoCurto(mp.matriz, linha1, coluna1, linha2, coluna2);
		mostrarMatriz(r);
	}
	
	public static void movimento()
	{
		Mundo mundo = new Mundo("mapa.txt", "objectos.txt");
		
		Trabalhador tr1 = new Trabalhador("Bruno's Car", 1);
		tr1.adicionarContentor(mundo.productos.elementAt(2), 5);
		tr1.adicionarContentor(mundo.productos.elementAt(3), 4);
		
		Vector<Ponto> vec = Auxiliar.caminhoCurto(mundo.cidade.matriz, 11, 35, 22, 27);
		
		int index = 0;
		
		while(true)
		{
			Ponto actual = null;
			
			mundo.mostrarMundo();
			if (index < vec.size())
			{
				actual = vec.elementAt(index++);
			}
			else
			{
				actual = vec.elementAt(vec.size() - 1);
			}
			
			mundo.cidade.substituir(actual.linha, actual.coluna, 'A');
			
			mundo.cidade.ver();
			
			mundo.ocultarMundo();
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void aaa()
	{
		Mundo mundo = new Mundo("mapa.txt", "objectos.txt");
		
		Trabalhador tr1 = new Trabalhador("Bruno's Car", 1);
		tr1.adicionarContentor(mundo.productos.elementAt(2), 5);
		tr1.adicionarContentor(mundo.productos.elementAt(3), 4);
		
		Trabalhador tr2 = new Trabalhador("Moreira's Car", 2);
		tr2.adicionarContentor(mundo.productos.elementAt(4), 3);
		tr2.adicionarContentor(mundo.productos.elementAt(5), 7);
		
		Armazem ar = new Armazem(2, 1);
		tr1.armazenar(ar, 0, 1);
		tr2.armazenar(ar, 1, 2);
		
		ar.ver();
		
		String s = ar.stringEnviarArmazem();
		Auxiliar.writeln(s);
	}
	
	public static void testeCompra()
	{
		Mundo mundo = new Mundo("mapa.txt", "objectos.txt");
		
		Trabalhador tr1 = new Trabalhador("Bruno's Car", 1);
		tr1.adicionarContentor(mundo.productos.elementAt(2), 5);
		tr1.adicionarContentor(mundo.productos.elementAt(3), 4);
		
		Loja lj = new Loja(1, 1);
		lj.productos.addElement(mundo.productos.elementAt(0));
		
		mundo.lojas.addElement(lj);
		
		tr1.ver();
		tr1.comprar(lj, mundo.productos.elementAt(0), 3);
		tr1.ver();
	}
	
	static public void main(String[] args)
	{
		testeCompra();
	}
}
