package beans;

import repository.generics.Entity;

public class Base64Image implements Entity<String> {

	private String id;
	private String data;

	public Base64Image() {
		super();
	}

	public Base64Image(String data) {
		super();
		this.data = data;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
