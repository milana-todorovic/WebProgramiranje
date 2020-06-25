package beans;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApartmentStatus {
	ACTIVE("Aktivan"), INACIVE("Neaktivan");

	private String keyword;

	private ApartmentStatus(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}