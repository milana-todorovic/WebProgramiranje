package beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import repository.generics.Entity;

public class Holidays implements Entity<Integer> {

	private Integer id;
	private Collection<Date> dates;

	public Holidays() {
		super();
		this.dates = new ArrayList<Date>();
	}

	public Integer getID() {
		return id;
	}

	public void setID(Integer id) {
		this.id = id;
	}

	public Collection<Date> getDates() {
		return dates;
	}

	public void setDates(Collection<Date> dates) {
		this.dates = dates;
	}

}
