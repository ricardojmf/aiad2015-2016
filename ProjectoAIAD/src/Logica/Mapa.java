package Logica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Mapa
{
	public Vector<String> matriz;
	public int linhas;
	public int colunas;
	
	private void iniciarMapa()
	{
		matriz = new Vector<String>();
	}
	
	public void substituir(int linha, int coluna, char letra)
	{
		String linhaa = matriz.elementAt(linha);
		String novo = Auxiliar.substituir(linhaa, coluna, letra);
		matriz.set(linha, novo);
	}
	
	private void lerFicheiro(String ficheiro)
	{
		try
		{
			InputStream inputstream = new FileInputStream(Auxiliar.folder + ficheiro);
			
			int data;
			String linha = "";
			
			data = inputstream.read();
			while (data != -1)
			{
				if (data == 10 || data == 13)
				{
					matriz.addElement(linha);
					linha = "";
				}
				else
				{					
					if (!(data == Auxiliar.letraParede || data == Auxiliar.letraEstrada ))
					{
						System.exit(0);
					}
					else
					{
						linha += ((char)data);
					}
				}
				data = inputstream.read();
			}
			matriz.addElement(linha);
			
			inputstream.close();
			
			int i = 0;
			while(i < matriz.size())
			{
				if (matriz.elementAt(i).length() == 0)
					matriz.removeElementAt(i);
				else
					i++;
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void ver()
	{
		for(String s: matriz)
		{
			Auxiliar.writeln(s);
		}
	}
	
	public Mapa(String ficheiro)
	{
		iniciarMapa();
		lerFicheiro(ficheiro);
		
		linhas = matriz.size();
		colunas = matriz.elementAt(0).length();
	}
}
