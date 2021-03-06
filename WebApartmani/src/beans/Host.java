package beans;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import repository.generics.Entity;

public class Host extends User implements Entity<Integer> {
	@JsonIgnore
	private Collection<Apartment> apartments;

	public Host() {
		super();
		role = UserRole.HOST;
		apartments = new ArrayList<Apartment>();
	}

	public Host(String username, String password, String name, String surname, String gender) {
		super(username, password, name, surname, gender);
		role = UserRole.HOST;
		apartments = new ArrayList<Apartment>();
	}

	public Collection<Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(Collection<Apartment> apartments) {
		this.apartments = apartments;
	}

}