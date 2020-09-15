package beans;

import repository.generics.Entity;

public class Comment implements Entity<Integer> {

	private String text;
	private Integer rating;
	private Boolean showing;
	private Guest guest;
	private Apartment apartment;
	private Integer id;
	private Boolean deleted;

	public Comment() {
		super();
		showing = false;
		deleted = false;
	}

	public Comment(String text, Integer rating, Boolean showing, Guest guest, Apartment apartment, Boolean deleted) {
		super();
		this.text = text;
		this.rating = rating;
		this.showing = showing;
		this.guest = guest;
		this.apartment = apartment;
		this.deleted = deleted;
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}