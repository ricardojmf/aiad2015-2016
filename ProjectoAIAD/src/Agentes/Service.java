package Agentes;

public class Service {
	
	private String name;
	private String type;
	private boolean envolveProducts;
	private int value;
	
	public Service(String name, String type, int value) {
		super();
		this.name = name;
		this.type = type;
		this.value = value;
		this.envolveProducts = true;
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
}
