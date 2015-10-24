package Logica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Mapa
{
	public Vector<String> matriz;
	//private static String folder = "/usr/users2/mieic2012/ei12012/Documents/workspace/ProjectoAIAD/";
	private static String folder = "C:\\Users\\Miguel Moreira\\Documents\\Eclipse Mars x64\\projects\\ProjectoAIAD\\";
	
	public static char letraParede = 'O';
	public static char letraEstrada = ' ';
	
	private void iniciarMapa()
	{
		matriz = new Vector<String>();
	}
	
	private void lerFicheiro(String ficheiro)
	{
		try
		{
			InputStream inputstream = new FileInputStream(folder + ficheiro);
			
			int data;
			String linha = "";
			
			data = inputstream.read();
			while (data != -1)
			{
				if (data == '\n')
				{
					matriz.addElement(linha);
					linha = "";
				}
				else
				{
					if (!(data == letraParede || data == letraEstrada))
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
			inputstream.close();
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
	}
}
