package com.bdayapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

	private static final ThreadLocal<DateFormat> parser = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};

	private static final ThreadLocal<DateFormat> formatter = new ThreadLocal<DateFormat>() {
		protected DateFormat initialValue() {
			return DateFormat.getDateInstance();
		};
	};

	private static final int[] DAYS_OF_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static final int[] DAYS_OF_MONTH_LEAP = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public static int numberOfDaysToBday(Date dob) {
		return numberOfDaysToBday(dob, new Date());
	}

	public static int numberOfDaysToBday(Date dob, Date from) {
		Calendar fromCal = Calendar.getInstance();
		Calendar dobCal = Calendar.getInstance();

		dobCal.setTime(dob);
		fromCal.setTime(from);

		dobCal.set(Calendar.YEAR, fromCal.get(Calendar.YEAR));
		dobCal.get(Calendar.DAY_OF_YEAR);

		int compare = fromCal.compareTo(dobCal);

		if (sameMonth(fromCal, dobCal) && sameDayOfMonth(fromCal, dobCal)) {
			return 0;
		}

		if (compare == -1 && sameMonth(dobCal, fromCal)) // In this month only
		{
			return dobCal.get(Calendar.DAY_OF_MONTH) - fromCal.get(Calendar.DAY_OF_MONTH);
		}

		int days = 0;

		days += daysInMonth(fromCal) - fromCal.get(Calendar.DAY_OF_MONTH);

		fromCal.add(Calendar.MONTH, 1);

		while (!sameMonth(fromCal, dobCal)) {
			days += daysInMonth(fromCal);

			fromCal.add(Calendar.MONTH, 1);
		}

		days += dobCal.get(Calendar.DAY_OF_MONTH);

		return days;
	}

	private static boolean sameDayOfMonth(Calendar one, Calendar two) {
		return one.get(Calendar.DAY_OF_MONTH) == two.get(Calendar.DAY_OF_MONTH);
	}

	private static boolean sameMonth(Calendar one, Calendar two) {
		return one.get(Calendar.MONTH) == two.get(Calendar.MONTH);
	}

	private static int daysInMonth(Calendar cal) {
		int month = cal.get(Calendar.MONTH);
		if (month != Calendar.FEBRUARY) {
			return DAYS_OF_MONTH[month];
		}

		int year = cal.get(Calendar.YEAR);

		if ((year % 4) == 0) {
			return DAYS_OF_MONTH_LEAP[Calendar.FEBRUARY];
		} else {
			return DAYS_OF_MONTH[Calendar.FEBRUARY];
		}
	}

	public static Date parse(String date) {
		try {
			return parser.get().parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String format(Date date) {
		return formatter.get().format(date);
	}

	public static void main(String[] args) {
		Date dob = parse("2011-08-15");
		Date from = parse("2011-08-25");
		System.out.println(numberOfDaysToBday(dob, from));
	}
}
