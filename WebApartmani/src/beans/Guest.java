package beans;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import repository.generics.Entity;

public class Guest extends User implements Entity<Integer> {

	@JsonIgnore
	private Collection<Reservation> reservations;
	@JsonIgnore
	private Collection<Apartment> rentedApartments;

	public Guest() {
		super();
		role = UserRole.GUEST;
		reservations = new ArrayList<Reservation>();
		rentedApartments = new ArrayList<Apartment>();
	}

	public Guest(String username, String password, String name, String surname, String gender) {
		super(username, password, name, surname, gender);
		role = UserRole.GUEST;
		reservations = new ArrayList<Reservation>();
		rentedApartments = new ArrayList<Apartment>();
	}

	public Collection<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Collection<Apartment> getRentedApartments() {
		return rentedApartments;
	}

	public void setRentedApartments(Collection<Apartment> rentedApartments) {
		this.rentedApartments = rentedApartments;
	}

}