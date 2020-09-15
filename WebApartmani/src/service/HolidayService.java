package service;

import java.util.Collection;
import java.util.Date;

import beans.Holiday;
import repository.interfaces.HolidayRepository;

public class HolidayService {
	// možda promjeniti kako su generalno modelovani praznici, ovako je malo glupo

	private HolidayRepository holidayRepository;

	public HolidayService(HolidayRepository holidayRepository) {
		super();
		this.holidayRepository = holidayRepository;
	}

	public Collection<Holiday> getAll() {
		// TODO
		return null;
	}
	
	public Holiday create(Holiday holiday) {
		// TODO
		return null;
	}
	
	public void delete(Integer id) {
		// TODO
	}
	
	public Boolean isHoldiay(Date date) {
		// TODO
		return false;
	}

}
