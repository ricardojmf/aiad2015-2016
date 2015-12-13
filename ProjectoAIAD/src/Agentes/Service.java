package Agentes;

import Logica.TarefaLeiloada;
import Logica.TarefaPreco;
import jade.util.leap.Serializable;

public class Service implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String type;
	private boolean envolveProducts;
	private int value;
	private TarefaLeiloada tarefaLeiloada;
	private TarefaPreco tarefaPreco;
	private String msg;
	
	
	public Service(String name, String type, int value, TarefaLeiloada tarefaLeiloada, TarefaPreco tarefaPreco, String msg) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
		this.envolveProducts = true;
		this.tarefaLeiloada = tarefaLeiloada;
		this.tarefaPreco = tarefaPreco;
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEnvolveProducts() {
		return envolveProducts;
	}

	public void setEnvolveProducts(boolean envolveProducts) {
		this.envolveProducts = envolveProducts;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public Boolean equals(Service service) {
		return this.name.equalsIgnoreCase(service.name) && this.type.equalsIgnoreCase(service.type);
	}

	public TarefaLeiloada getTarefaLeiloada() {
		return tarefaLeiloada;
	}

	public void setTarefaLeiloada(TarefaLeiloada tarefaLeiloada) {
		this.tarefaLeiloada = tarefaLeiloada;
	}

	public TarefaPreco getTarefaPreco() {
		return tarefaPreco;
	}

	public void setTarefaPreco(TarefaPreco tarefaPreco) {
		this.tarefaPreco = tarefaPreco;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
