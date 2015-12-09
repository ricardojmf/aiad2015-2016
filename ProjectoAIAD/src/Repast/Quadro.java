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
	
	static ImageIcon iconParede = new ImageIcon(Auxiliar.folder + "parede.png");
	static ImageIcon iconEstrada = new ImageIcon(Auxiliar.folder + "estrada.png");
	static ImageIcon iconLoja = new ImageIcon(Auxiliar.folder + "loja.png");
	static ImageIcon iconArmazem = new ImageIcon(Auxiliar.folder + "armazem.png");
	static ImageIcon iconLixeira = new ImageIcon(Auxiliar.folder + "lixeira.png");
	static ImageIcon iconRecarga = new ImageIcon(Auxiliar.folder + "recarga.png");
	
	static Image parede = iconParede.getImage();
	static Image estrada = iconEstrada.getImage();
	static Image loja = iconLoja.getImage();
	static Image armazem = iconArmazem.getImage();
	static Image lixeira = iconLixeira.getImage();
	static Image recarga = iconRecarga.getImage();
	
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
					arg0.drawImage(parede);
					break;
				case Auxiliar.letraEstrada: // estrada
					arg0.drawImage(estrada);
					break;
				case Auxiliar.letraLoja: // parede
					arg0.drawImage(loja);
					break;
				case Auxiliar.letraArmazem: // estrada
					arg0.drawImage(armazem);
					break;
				case Auxiliar.letraLixeira: // parede
					arg0.drawImage(lixeira);
					break;
				case Auxiliar.letraRecarga: // estrada
					arg0.drawImage(recarga);
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
