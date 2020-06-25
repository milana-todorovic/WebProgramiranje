package beans;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Apartment {

	private ApartmentType apartmentType;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private Collection<LocalDate> datesForRenting;
	@JsonIgnore
	private Collection<LocalDate> availableDates;
	private Collection<String> imageKeys;
	private double pricePerNight;
	private LocalTime checkInTime;
	private LocalTime checkOutTime;
	private ApartmentStatus status;
	private Collection<Amenity> amenities;
	@JsonIgnore
	private Collection<Comment> comments;
	@JsonIgnore
	private Collection<Reservation> reservations;
	private Host host;
	private int id;

	public Apartment() {
		super();
		datesForRenting = new ArrayList<>();
		availableDates = new ArrayList<>();
		imageKeys = new ArrayList<>();
		amenities = new ArrayList<>();
		comments = new ArrayList<>();
		reservations = new ArrayList<>();
		checkInTime = LocalTime.of(14, 0);
		checkOutTime = LocalTime.of(10, 0);
		status = ApartmentStatus.INACIVE;
	}

	public ApartmentType getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(ApartmentType apartmentType) {
		this.apartmentType = apartmentType;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Collection<LocalDate> getDatesForRenting() {
		return datesForRenting;
	}

	public void setDatesForRenting(Collection<LocalDate> datesForRenting) {
		this.datesForRenting = datesForRenting;
	}

	public Collection<LocalDate> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(Collection<LocalDate> availableDates) {
		this.availableDates = availableDates;
	}

	public Collection<String> getImageKeys() {
		return imageKeys;
	}

	public void setImageKeys(Collection<String> imageKeys) {
		this.imageKeys = imageKeys;
	}

	public double getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(double pricePerNight) {
		this.pricePerNight = pricePerNight;
	}

	public LocalTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(LocalTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public LocalTime getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(LocalTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public ApartmentStatus getStatus() {
		return status;
	}

	public void setStatus(ApartmentStatus status) {
		this.status = status;
	}

	public Collection<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(Collection<Amenity> amenities) {
		this.amenities = amenities;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	public Collection<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}