package beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import repository.generics.Entity;

public class Apartment implements Entity<Integer> {

	private String name;
	private ApartmentType apartmentType;
	private Integer numberOfRooms;
	private Integer numberOfGuests;
	private Location location;
	private Collection<Date> datesForRenting;
	private Collection<Date> availableDates;
	private Collection<String> imageKeys;
	private Double pricePerNight;
	@JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private Date checkInTime;
	@JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
	private Date checkOutTime;
	private ApartmentStatus status;
	private Collection<Amenity> amenities;
	@JsonIgnore
	private Collection<Comment> comments;
	@JsonIgnore
	private Collection<Reservation> reservations;
	private Host host;
	private Integer id;

	public Apartment() {
		super();
		datesForRenting = new ArrayList<>();
		availableDates = new ArrayList<>();
		imageKeys = new ArrayList<>();
		amenities = new ArrayList<>();
		comments = new ArrayList<>();
		reservations = new ArrayList<>();
		location = new Location();
		status = ApartmentStatus.INACIVE;
	}

	public Apartment(String name, ApartmentType apartmentType, Integer numberOfRooms, Integer numberOfGuests,
			Location location, Double pricePerNight, Date checkInTime, Date checkOutTime, ApartmentStatus status,
			Host host) {
		super();
		this.name = name;
		this.apartmentType = apartmentType;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.pricePerNight = pricePerNight;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.status = status;
		this.host = host;
		datesForRenting = new ArrayList<>();
		availableDates = new ArrayList<>();
		imageKeys = new ArrayList<>();
		amenities = new ArrayList<>();
		comments = new ArrayList<>();
		reservations = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApartmentType getApartmentType() {
		return apartmentType;
	}

	public void setApartmentType(ApartmentType apartmentType) {
		this.apartmentType = apartmentType;
	}

	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Collection<Date> getDatesForRenting() {
		return datesForRenting;
	}

	public void setDatesForRenting(Collection<Date> datesForRenting) {
		this.datesForRenting = datesForRenting;
	}

	public Collection<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(Collection<Date> availableDates) {
		this.availableDates = availableDates;
	}

	public Collection<String> getImageKeys() {
		return imageKeys;
	}

	public void setImageKeys(Collection<String> imageKeys) {
		this.imageKeys = imageKeys;
	}

	public Double getPricePerNight() {
		return pricePerNight;
	}

	public void setPricePerNight(Double pricePerNight) {
		this.pricePerNight = pricePerNight;
	}

	public Date getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Date getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(Date checkOutTime) {
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

	@Override
	public Integer getID() {
		return id;
	}

	@Override
	public void setID(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Apartment other = (Apartment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}