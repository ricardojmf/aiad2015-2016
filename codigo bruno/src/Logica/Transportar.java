package Logica;

import java.util.Date;
import java.util.Vector;


public class Transportar extends Trabalho
{
	public Vector<Ranhura> pedido; // a entregar
	public Ponto destino;
	
	public Transportar(int rec, Date tl, Ponto p)
	{
		super(rec, tl);
		pedido = new Vector<Ranhura>();
		destino = p;
	}
	
}
