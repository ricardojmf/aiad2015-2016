package Repast;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import Logica.Auxiliar;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public class Quadro implements Drawable
{
	public Vector<String> matriz;
	
	public int COMPRIMENTO = 0;
	public int LARGURA = 0;
	
	private static char letraParede = 'O';
	private static char letraEstrada = ' ';
	
	public Quadro(Vector<String> map)
	{
		matriz = map;
		COMPRIMENTO = matriz.elementAt(0).length();
		LARGURA = matriz.size();
	}
	
	@Override
	public void draw(SimGraphics arg0)
	{		
		for(int linha = 0; linha < matriz.size(); linha++)
		{
			String LINHA = matriz.elementAt(linha);
			for(int coluna = 0; coluna < LINHA.length(); coluna++)
			{
				char COLUNA = LINHA.charAt(coluna);
				
				arg0.setDrawingCoordinates(coluna * arg0.getCurWidth(), linha * arg0.getCurHeight(), 0);
				
				switch(COLUNA)
				{
				case 'O':
					arg0.drawFastRect(Color.red);
					break;
				case ' ':
					arg0.drawFastRect(Color.white);
					break;
				}
			}
		}
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}
}
