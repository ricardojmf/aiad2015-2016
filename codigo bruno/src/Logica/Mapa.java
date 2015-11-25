package Logica;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class Mapa
{
	public Vector<String> matriz;
	
	private static char letraParede = 'O';
	private static char letraEstrada = ' ';
	
	private void iniciarMapa()
	{
		matriz = new Vector<String>();
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
					if (!(data == letraParede || data == letraEstrada ))
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
	}
}
