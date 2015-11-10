package Consola;

import java.util.Vector;

import Logica.*;

public class Main
{
	public void mostrarMatriz(Vector<Ponto> vc)
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
	
	static public void main(String[] args)
	{
		Mundo x = new Mundo("mapa.txt", "objectos.txt");
		x.verLojas();
		x.verArmazens();
		
		
		Trabalhador tr = new Trabalhador("Bruno", 1);
		//tr.ver();
		
		x.comprar(tr, x.lojas.elementAt(0), 0, 2);
		x.comprar(tr, x.lojas.elementAt(0), 1, 3);
		
		//tr.ver();
		x.comprar(tr, x.lojas.elementAt(0), 1, 3);
	}
}
