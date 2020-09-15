package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static Date stripDate(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date stripTime(Date date) {
		Calendar timeWithDate = new GregorianCalendar();
		timeWithDate.setTime(date);
		Calendar time = new GregorianCalendar();
		time.set(Calendar.HOUR_OF_DAY, timeWithDate.get(Calendar.HOUR_OF_DAY));
		time.set(Calendar.MINUTE, timeWithDate.get(Calendar.MINUTE));
		time.set(Calendar.SECOND, timeWithDate.get(Calendar.SECOND));
		time.set(Calendar.MILLISECOND, timeWithDate.get(Calendar.MILLISECOND));
		return time.getTime();
	}

	public static Date nextDay(Date date) {
		Calendar calendar = new GregorianCalendar();
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
		Calendar calendar = new GregorianCalendar();
		for (int i = 0; i < numberOfDays; i++) {
			calendar.setTime(startDate);
			calendar.add(Calendar.DAY_OF_MONTH, i);
			dates.add(calendar.getTime());
		}
		return dates;
	}

	public static Collection<Date> makeList(Date startDate, Date endDate) {
		Collection<Date> dates = new ArrayList<Date>();
		Date currentDate = (Date) startDate.clone();
		while (currentDate.compareTo(endDate) <= 0) {
			dates.add(currentDate);
		}
		return dates;
	}

}
