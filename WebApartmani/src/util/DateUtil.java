package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	public static Date stripDate(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date stripTime(Date date) {
		Calendar timeWithDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		timeWithDate.setTime(date);
		Calendar time = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		time.set(Calendar.HOUR_OF_DAY, timeWithDate.get(Calendar.HOUR_OF_DAY));
		time.set(Calendar.MINUTE, timeWithDate.get(Calendar.MINUTE));
		time.set(Calendar.SECOND, timeWithDate.get(Calendar.SECOND));
		time.set(Calendar.MILLISECOND, timeWithDate.get(Calendar.MILLISECOND));
		return time.getTime();
	}
	
	public static Date addDays(Date date, Integer numberOfDays) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
		return calendar.getTime();
	}

	public static Date nextDay(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static Collection<Date> removeDuplicateDates(Collection<Date> dates) {
		Collection<Date> dateSet = new ArrayList<Date>();
		for (Date date : dates) {
			if (!dateSet.contains(DateUtil.stripDate(date)))
				dateSet.add(DateUtil.stripDate(date));
		}
		return dateSet;
	}

	public static Collection<Date> makeList(Date startDate, int numberOfDays) {
		Collection<Date> dates = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		for (int i = 0; i < numberOfDays; i++) {
			calendar.setTime(stripDate(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, i);
			dates.add(calendar.getTime());
		}
		return dates;
	}

	public static Collection<Date> makeList(Date startDate, Date endDate) {
		Collection<Date> dates = new ArrayList<Date>();
		Date currentDate = stripDate((Date) startDate.clone());
		while (currentDate.before(stripDate(endDate))) {
			dates.add(currentDate);
		}
		return dates;
	}

	public static Boolean isWeekend(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		Integer day = calendar.get(Calendar.DAY_OF_WEEK);
		return day.equals(Calendar.FRIDAY) || day.equals(Calendar.SATURDAY) || day.equals(Calendar.SUNDAY);
	}

	public static Boolean isBeforeToday(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		Date today = stripDate(calendar.getTime());
		return stripDate(date).before(today);
	}
	
	public static Boolean isAfterToday(Date date) {
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		Date today = stripDate(calendar.getTime());
		return stripDate(date).after(today);
	}

}
