package Repast;

import java.awt.Color;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.ImageIcon;

import Logica.Auxiliar;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

public class Quadro implements Drawable
{
	public Vector<String> matriz;
	
	public int COMPRIMENTO = 0;
	public int LARGURA = 0;
	
	static final Sprite parede = new Sprite(Auxiliar.folder + "parede.png");
	static final Sprite estrada = new Sprite(Auxiliar.folder + "estrada.png");
	static final Sprite loja = new Sprite(Auxiliar.folder + "loja.png");
	static final Sprite armazem = new Sprite(Auxiliar.folder + "armazem.png");
	static final Sprite lixeira = new Sprite(Auxiliar.folder + "lixeira.png");
	static final Sprite recarga = new Sprite(Auxiliar.folder + "recarga.png");
	
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
				case Auxiliar.letraParede: // parede
					//arg0.drawFastRect(Color.black);
					arg0.drawImage(parede.value());
					break;
				case Auxiliar.letraEstrada: // estrada
					arg0.drawImage(estrada.value());
					break;
				case Auxiliar.letraLoja: // parede
					arg0.drawImage(loja.value());
					break;
				case Auxiliar.letraArmazem: // estrada
					arg0.drawImage(armazem.value());
					break;
				case Auxiliar.letraLixeira: // parede
					arg0.drawImage(lixeira.value());
					break;
				case Auxiliar.letraRecarga: // estrada
					arg0.drawImage(recarga.value());
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
