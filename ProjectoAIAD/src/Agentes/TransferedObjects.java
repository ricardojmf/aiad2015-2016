package Agentes;

import java.util.ArrayList;

import Logica.Producto;
import jade.util.leap.Serializable;

public class TransferedObjects implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<Producto> objects;
	
	public TransferedObjects() {
		objects = new ArrayList<Producto>();
	}
	
	public void addObject(Producto object) {
		objects.add(object);
	}
	
	public ArrayList<Producto> getObjects() {
		return objects;
	}
} 