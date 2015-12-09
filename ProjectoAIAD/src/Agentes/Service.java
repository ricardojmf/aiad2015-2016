package Agentes;

public class Service {
	
	private String name;
	private String type;
	
	
	public Service(String name, String type) {
		super();
		this.name = name;
		this.type = type;
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
	
	public Boolean equals(Service service) {
		return this.name.equalsIgnoreCase(service.name) && this.type.equalsIgnoreCase(service.type);
	}
		

}
