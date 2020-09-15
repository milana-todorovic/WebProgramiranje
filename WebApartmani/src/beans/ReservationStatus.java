package beans;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
	CREATED("Kreirana"), REJECTED("Odbijena"), CANCELLED("Otkazana"), ACCEPTED("Prihvaćena"), FINISHED("Završena");

	private String keyword;

	private ReservationStatus(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}