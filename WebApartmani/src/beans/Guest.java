package beans;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import repository.generics.Entity;

public class Guest extends User implements Entity<Integer> {

	@JsonIgnore
	private Collection<Reservation> reservations;
	@JsonIgnore
	public Collection<Apartment> rentedApartments;

	public Guest() {
		super();
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