package dto;

import java.util.Collection;

import beans.ReservationStatus;

public class ReservationSearchDTO {

	private SortType sort;

	private Collection<ReservationStatus> status;
	private String guestUsername;

	public SortType getSort() {
		return sort;
	}

	public void setSort(SortType sort) {
		this.sort = sort;
	}

	public Collection<ReservationStatus> getStatus() {
		return status;
	}

	public void setStatus(Collection<ReservationStatus> status) {
		this.status = status;
	}

	public String getGuestUsername() {
		return guestUsername;
	}

	public void setGuestUsername(String guestUsername) {
		this.guestUsername = guestUsername;
	}

}
