package beans;

import java.util.Date;

import repository.generics.Entity;

public class Holiday implements Entity<Integer> {

	private Integer id;
	private Date date;

	public Integer getID() {
		return id;
	}

	public void setID(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
