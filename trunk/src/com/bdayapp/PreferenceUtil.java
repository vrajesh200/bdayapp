/*
 * Copyright (C) 2010 Srinivas Paladugu.
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

package com.bdayapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtil {
	private static final String PREF_NAME = "prefFile";
	private static final int PREF_MODE = Context.MODE_PRIVATE;

	private static final String NOTIFICATION_TIME_HOUR		= "notificationHour";
	private static final String NOTIFICATION_TIME_MIN		= "notificationMinute";
	private static final String NOTIFICATION_SOUND			= "notificationSound";
	private static final String NOTIFICATION_VIBRATE		= "notificationVibrate";
	private static final String NOTIFICATION_LED_COLOR		= "notificationLedColor";

	public static final String[] COLOR_STRINGS	= {"Red", "Green", "Blue", "White"};
	private static final int[] COLOR_INTS		= {0xFF0000, 0x00FF00, 0x0000FF, 0xFFFFFF};


	private static SharedPreferences getPrefs(Context ctx)
	{
		return ctx.getApplicationContext().getSharedPreferences(PREF_NAME,PREF_MODE);
	}

	public static void storeNotifierTime(Context ctx, int hour, int minutes)
	{
		Editor edit = getPrefs(ctx).edit();
		edit.putInt(NOTIFICATION_TIME_HOUR, hour);
		edit.putInt(NOTIFICATION_TIME_MIN, minutes);
		edit.commit();
	}

	public static NotificationTime getNotifierTime(Context ctx)
	{
		SharedPreferences prefs = getPrefs(ctx);
		return new NotificationTime(prefs.getInt(NOTIFICATION_TIME_HOUR, 0), prefs.getInt(NOTIFICATION_TIME_MIN, 0));
	}

	public static void enableSoundNotification(Context ctx)
	{
		Editor edit = getPrefs(ctx).edit();
		edit.putBoolean(NOTIFICATION_SOUND, true);
		edit.commit();
	}

	public static void disableSoundNotification(Context ctx)
	{
		Editor edit = getPrefs(ctx).edit();
		edit.putBoolean(NOTIFICATION_SOUND, false);
		edit.commit();
	}

	public static boolean soundNotificationEnabled(Context ctx)
	{
		return getPrefs(ctx).getBoolean(NOTIFICATION_SOUND, false);
	}

	public static void enableVibrateNotification(Context ctx)
	{
		Editor edit = getPrefs(ctx).edit();
		edit.putBoolean(NOTIFICATION_VIBRATE, true);
		edit.commit();
	}

	public static void disableVibrateNotification(Context ctx)
	{
		Editor edit = getPrefs(ctx).edit();
		edit.putBoolean(NOTIFICATION_VIBRATE, false);
		edit.commit();
	}

	public static boolean vibrateNotificationEnabled(Context ctx)
	{
		return getPrefs(ctx).getBoolean(NOTIFICATION_VIBRATE, false);
	}

	public static void setNotificationLedColor(Context ctx, int color)
	{
		Editor edit = getPrefs(ctx).edit();
		edit.putInt(NOTIFICATION_LED_COLOR, color);
		edit.commit();
	}

	public static int getNotificationLedColor(Context ctx)
	{
		return getPrefs(ctx).getInt(NOTIFICATION_LED_COLOR, 0xFFFFFF);
	}

	public static void setAlertType(Context ctx, String alertType)
	{
		if (alertType.contentEquals("Sound"))
		{
			enableSoundNotification(ctx);
			disableVibrateNotification(ctx);
		}
		else if (alertType.contentEquals("Vibrate"))
		{
			disableSoundNotification(ctx);
			enableVibrateNotification(ctx);
		}
		else if (alertType.contentEquals("Sound + Vibrate"))
		{
			enableSoundNotification(ctx);
			enableVibrateNotification(ctx);
		}
		else
		{
			disableSoundNotification(ctx);
			disableVibrateNotification(ctx);
		}
	}

	public static void setNotificationLedColor(Context ctx, String color)
	{
		setNotificationLedColor(ctx, colorToInt(color));
	}


	public static int getSelectedColorIndex(Context ctx)
	{
		int color = getNotificationLedColor(ctx);

		for (int i = 0; i < COLOR_INTS.length; i++)
		{
			if (COLOR_INTS[i] == color)
			{
				return i;
			}
		}
		return 3;

	}

	private static int colorToInt(String color)
	{
		for (int i = 0; i < COLOR_STRINGS.length; i++)
		{
			if (COLOR_STRINGS[i].equals(color))
			{
				return COLOR_INTS[i];
			}
		}
		return COLOR_INTS[3];
	}



	public static class NotificationTime {
		private int hour;
		private int minutes;

		public NotificationTime(int hour, int minutes)
		{
			this.hour = hour;
			this.minutes = minutes;
		}

		public int getHour()
		{
			return hour;
		}

		public int getMinutes()
		{
			return minutes;
		}
	}

}
