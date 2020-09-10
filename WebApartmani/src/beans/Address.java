package beans;

public class Address {

	private String street;
	private Integer number;
	private String city;
	private String country;
	private String postalCode;

	public Address() {
		super();
	}

	public Address(String street, Integer number, String city, String country, String postalCode) {
		super();
		this.street = street;
		this.number = number;
		this.city = city;
		this.country = country;
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}