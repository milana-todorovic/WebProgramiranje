package beans;

import repository.generics.Entity;

public class Comment implements Entity<Integer> {

	private String text;
	private Integer rating;
	private Boolean showing;
	private Guest guest;
	private Apartment apartment;
	private Integer id;

	public Comment() {
		super();
		showing = false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Boolean isShowing() {
		return showing;
	}

	public void setShowing(Boolean showing) {
		this.showing = showing;
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	@Override
	public Integer getID() {
		return id;
	}

	@Override
	public void setID(Integer id) {
		this.id = id;
	}

}