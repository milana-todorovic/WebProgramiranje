/***********************************************************************
 * Module:  ReservationStatus.java
 * Author:  Lana
 * Purpose: Defines the Class ReservationStatus
 ***********************************************************************/

package beans;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
	CREATED("Kreirana"), REJECTED("Odbijena"), CANCELLED("Odustanak"), ACCEPTED("Prihvaćena"), FINISHED("Završena");

	private String keyword;

	private ReservationStatus(String keyword) {
		this.keyword = keyword;
	}

	@JsonValue
	public String getKeyword() {
		return keyword;
	}
}