package Repast;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Sprite
{
	Image imagem;
	ImageIcon fundo;
	
	public Sprite(String src)
	{
		fundo = new ImageIcon(src);
		imagem = fundo.getImage();
	}
	
	public Image value()
	{
		return imagem;
	}
}