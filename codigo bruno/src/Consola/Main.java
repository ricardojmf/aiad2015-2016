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
	
	public static void testeProduzir()
	{
		Mundo mundo = new Mundo("mapa.txt", "objectos.txt");
		
		Trabalhador tr = new Trabalhador("Bruno's Car", 1);
		mundo.adicionarContentor(tr, mundo.productos.elementAt(2), 5);
		mundo.adicionarContentor(tr, mundo.productos.elementAt(3), 4);
		
		Vector<String> ferra = new Vector<String>();
		ferra.addElement("f1");
		ferra.addElement("f2");
		
		Ranhura ra1 = new Ranhura(mundo.productos.elementAt(2), 4);
		Ranhura ra2 = new Ranhura(mundo.productos.elementAt(3), 4);
		
		Vector<Ranhura> request = new Vector<Ranhura>();
		request.addElement(ra1);
		request.addElement(ra2);
		
		Produzir pd = new Produzir(request, ferra, mundo.productos.elementAt(4), 3);
		
		tr.ver();
		pd.ver();
		
		boolean estado = mundo.produzir(tr, pd);
		
		System.out.println(estado);
		
		tr.verContentor();
	}
	
	public static void testeTrocarProductos()
	{
		Mundo mundo = new Mundo("mapa.txt", "objectos.txt");
		
		Trabalhador tr1 = new Trabalhador("Bruno's Car", 1);
		mundo.adicionarContentor(tr1, mundo.productos.elementAt(2), 5);
		mundo.adicionarContentor(tr1, mundo.productos.elementAt(3), 4);
		
		Trabalhador tr2 = new Trabalhador("Moreira's Car", 1);
		mundo.adicionarContentor(tr2, mundo.productos.elementAt(5), 2);
		mundo.adicionarContentor(tr2, mundo.productos.elementAt(6), 3);
		
		mundo.trocarProductos(tr1, tr1.contentor.elementAt(0).producto, 4, tr2, tr2.contentor.elementAt(1).producto, 1);
		
		tr1.verContentor();
		tr2.verContentor();
	}
	
	public static void testeTrocarProductoDinheiro()
	{
		Mundo mundo = new Mundo("mapa.txt", "objectos.txt");
		
		Trabalhador tr1 = new Trabalhador("Bruno's Car", 1);
		mundo.adicionarContentor(tr1, mundo.productos.elementAt(2), 5);
		mundo.adicionarContentor(tr1, mundo.productos.elementAt(3), 4);
		
		Trabalhador tr2 = new Trabalhador("Moreira's Car", 1);
		tr2.riqueza = 30000;
		
		mundo.trocarProductoDinheiro(tr1, tr1.contentor.elementAt(0).producto, 4, tr2, 11700);
		
		tr1.verActual();
		tr1.verContentor();
		tr2.verActual();
		tr2.verContentor();
	}
	
	static public void main(String[] args)
	{
		//testeProduzir();
		testeTrocarProductoDinheiro();
	}
}
