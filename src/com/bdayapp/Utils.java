/*
 * Copyright (C) 2010 Venkata Rajesh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//=====================================================================================================================
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Utils {
	private static int sHourOfDay = 1;
	private static int sMinute = 0;
	private static int sLedColor = 0x00FF0000;
	private static boolean bEnableSound = false;
	private static boolean bEnableVibrate = false;
	
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
	
	public static void setAlarm(Context ctx, int hourOfDay, int minute) {
		if ((hourOfDay == -1) && (minute == -1))
		{
			hourOfDay = sHourOfDay;
			minute = sMinute;
		}
		// get a Calendar object with current time
		Calendar cal = Calendar.getInstance();
		Log.w("setAlarm", "DAY_OF_YEAR = " + cal.get(Calendar.DAY_OF_YEAR));
		if (hourOfDay < cal.get(Calendar.HOUR_OF_DAY)) 
		{
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		else if ((hourOfDay == cal.get(Calendar.HOUR_OF_DAY)) && (minute <= cal.get(Calendar.MINUTE)))
		{
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		Log.w("setAlarm", "DAY_OF_YEAR = " + cal.get(Calendar.DAY_OF_YEAR));
		Log.w("setAlarm", "Hour = " + hourOfDay + "minutest = " + minute);
		Date dt = cal.getTime();
		dt.setHours(hourOfDay);
		dt.setMinutes(minute);
		dt.setSeconds(0);
		Log.w("setAlarm", "AlarmTime = " + dt.getHours() + dt.getMinutes());
		cal.setTime(dt);

		Intent intent = new Intent(ctx, AlarmReceiver.class);
		// In reality, you would want to have a static variable for the request code instead of 192837
		PendingIntent sender = PendingIntent.getBroadcast(ctx, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
	}
	
	public static void setNotification(Context ctx, int position, String contactName, int flags) {
		Intent configIntent = new Intent(ctx, ContactPage.class);
		configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		configIntent.putExtra("IndexInList", position);
		Notification note = new Notification(R.drawable.icon_notify, contactName, System.currentTimeMillis());
		note.flags = flags;
		note.flags |= Notification.FLAG_SHOW_LIGHTS;
		note.ledARGB = sLedColor;
		if (bEnableSound)
		{
			note.defaults |= Notification.DEFAULT_SOUND;
		}
		if (bEnableVibrate)
		{
			note.defaults |= Notification.DEFAULT_VIBRATE;
		}
		note.setLatestEventInfo(ctx, "Bday Notification", contactName +"'s Bday",
				PendingIntent.getActivity(ctx, 0, configIntent, PendingIntent.FLAG_CANCEL_CURRENT));
		NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, note);
	}
	
	public static void setAlarmRptTime(Context ctx, int hour, int minute)
	{
		sHourOfDay = hour;
		sMinute = minute;
		Utils.setAlarm(ctx, sHourOfDay, sMinute);
	}
	
	public static void setLedColor(String color)
	{
		if (color.contentEquals("White"))
		{
			sLedColor = 0xFFFFFF;
		}
		else if (color.contentEquals("Red"))
		{
			sLedColor = 0xFF0000;
		}
		else if (color.contentEquals("Green"))
		{
			sLedColor = 0x00FF00;
		}
		else if (color.contentEquals("Blue"))
		{
			sLedColor = 0x0000FF;
		}
		else
		{
			sLedColor = 0xFFFFFF;
		}
	}
	
	public static void setAlertType(String alertType)
	{
		if (alertType.contentEquals("Sound"))
		{
			bEnableSound = true;
			bEnableVibrate = false;
		}
		else if (alertType.contentEquals("Vibrate"))
		{
			bEnableSound = false;
			bEnableVibrate = true;
		}
		else if (alertType.contentEquals("Sound + Vibrate"))
		{
			bEnableSound = true;
			bEnableVibrate = true;
		}
		else
		{
			bEnableSound = false;
			bEnableVibrate = false;
		}
	}
}
