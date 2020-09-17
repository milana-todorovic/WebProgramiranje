package dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SortType {
	ASCENDING("Rastu\u0107e"), DESCENDING("Opadaju\u0107e");

	private String keyword;

	private SortType(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}
