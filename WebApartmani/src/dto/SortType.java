package dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SortType {
	ASCENDING("Rastuće"), DESCENDING("Opadajuće");

	private String keyword;

	private SortType(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}
