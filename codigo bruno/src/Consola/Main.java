package Consola;

import java.util.Vector;

import Logica.*;

public class Main
{
	public void executarLinhaRecta()
	{
		int linha1 = 2;
		int coluna1 = 1;
		int linha2 = 7;
		int coluna2 = 7;
		
		//Vector<Ponto> vc = Auxiliar.linhaRecta(linha1, coluna1, linha2, coluna2);
		Vector<Ponto> vc = Auxiliar.caminho(linha1, coluna1, linha2, coluna2);
		
		char[][] mat;
		mat = new char[10][10];
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				mat[i][j] = '_';
			}
		}
		
		mat[linha1][coluna1] = 'X';
		mat[linha2][coluna2] = 'Y';
		
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
	
	private void limpar()
	{
		for(int i = 0; i < 6; i++)
		{
			Auxiliar.writeln("");
		}
	}
	
	public void executar()
	{	
		limpar();
		
		//executarLinhaRecta();
		Producto p1 = new Producto("Agua", 25);
		Producto p2 = new Producto("Vinho Tinto", 25);
		Producto p3 = new Producto("Fanta Laranja", 25);
		
		Mapa mp = new Mapa("mapa.txt");
		mp.ver();
	}
	
	static public void main(String[] args)
	{
		Main x = new Main();
		x.executar();
	}
}
