package dto;

import java.util.Collection;
import java.util.Date;

import beans.ApartmentType;

public class ApartmentSearchDTO {

	private SortType sort;

	private Collection<Integer> amenities;
	private Collection<ApartmentType> types;

	private Date startDate;
	private Date endDate;
	private Double minimumPrice;
	private Double maximumPrice;
	private Integer minimumNumberOfRooms;
	private Integer maximumNumberOfRooms;
	private Integer minimumNumberOfGuests;
	private Integer maximumNumberOfGuests;
	private String city;
	private String country;

	public SortType getSort() {
		return sort;
	}

	public void setSort(SortType sort) {
		this.sort = sort;
	}

	public Collection<Integer> getAmenities() {
		return amenities;
	}

	public void setAmenities(Collection<Integer> amenities) {
		this.amenities = amenities;
	}

	public Collection<ApartmentType> getTypes() {
		return types;
	}

	public void setTypes(Collection<ApartmentType> types) {
		this.types = types;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(Double minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public Double getMaximumPrice() {
		return maximumPrice;
	}

	public void setMaximumPrice(Double maximumPrice) {
		this.maximumPrice = maximumPrice;
	}

	public Integer getMinimumNumberOfRooms() {
		return minimumNumberOfRooms;
	}

	public void setMinimumNumberOfRooms(Integer minimumNumberOfRooms) {
		this.minimumNumberOfRooms = minimumNumberOfRooms;
	}

	public Integer getMaximumNumberOfRooms() {
		return maximumNumberOfRooms;
	}

	public void setMaximumNumberOfRooms(Integer maximumNumberOfRooms) {
		this.maximumNumberOfRooms = maximumNumberOfRooms;
	}

	public Integer getMinimumNumberOfGuests() {
		return minimumNumberOfGuests;
	}

	public void setMinimumNumberOfGuests(Integer minimumNumberOfGuests) {
		this.minimumNumberOfGuests = minimumNumberOfGuests;
	}

	public Integer getMaximumNumberOfGuests() {
		return maximumNumberOfGuests;
	}

	public void setMaximumNumberOfGuests(Integer maximumNumberOfGuests) {
		this.maximumNumberOfGuests = maximumNumberOfGuests;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
