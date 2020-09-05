package beans;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Host extends User {
	@JsonIgnore
	private Collection<Apartment> apartments;

	public Host() {
		super();
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