package beans;

import repository.generics.Entity;

public class Amenity implements Entity<Integer>{

	private Integer id;
	private String name;

	@Override
	public Integer getID() {
		return id;
	}

	@Override
	public void setID(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}