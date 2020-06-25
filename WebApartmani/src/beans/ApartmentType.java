package beans;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApartmentType {
	ROOM("Soba"), FULL_APARTMENT("Ceo apartman");

	private String keyword;

	private ApartmentType(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}