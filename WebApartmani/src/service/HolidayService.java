package service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import beans.Holidays;
import repository.interfaces.HolidayRepository;
import util.DateUtil;

public class HolidayService {

	private HolidayRepository holidayRepository;

	public HolidayService(HolidayRepository holidayRepository) {
		super();
		this.holidayRepository = holidayRepository;

		if (holidayRepository.simpleGetByID(0) == null) {
			Holidays holidays = new Holidays();
			holidays.setID(0);
			holidayRepository.create(holidays);
		}
	}

	public Collection<Date> getAll() {
		Holidays holidays = holidayRepository.simpleGetByID(0);
		if (holidays == null)
			return new ArrayList<Date>();
		else
			return holidays.getDates();
	}

	public Collection<Date> update(Collection<Date> newHolidays) {
		Holidays holidays = holidayRepository.simpleGetByID(0);
		if (holidays == null) {
			holidays = new Holidays();
			holidays.setID(0);
			holidayRepository.create(holidays);
		}
		holidays.setDates(DateUtil.removeDuplicateDates(newHolidays));
		holidays = holidayRepository.update(holidays);
		if (holidays == null)
			return new ArrayList<Date>();
		else
			return holidays.getDates();
	}

	public Boolean isHoliday(Date date) {
		Holidays holidays = holidayRepository.simpleGetByID(0);
		if (holidays == null)
			return false;
		else
			return holidays.getDates().contains(DateUtil.stripDate(date));
	}

}
