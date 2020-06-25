package beans;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
	HOST("Domaćin"), GUEST("Gost"), ADMIN("Administrator");

	private String keyword;

	private UserRole(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}