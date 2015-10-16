package Logica;

import java.util.Date;
import java.util.Vector;


public class Obter extends Trabalho
{
	public Vector<Ranhura> pedido; // a entregar
	
	public Obter(int rec, Date tl, Ponto p)
	{
		super(rec, tl);
		pedido = new Vector<Ranhura>();
	}
}
