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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.bdayapp.PreferenceUtil.NotificationTime;



public class SettingsPage extends ListActivity{

	private static final int TIME_DIALOG_ID = 0;
	private static final int LED_DIALOG_ID = 1;
	private static final int NOTIFY_TYPE_DIALOG_ID = 2;

	AlertDialog alert_led_color;
	AlertDialog alert_notify_type;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] settings = getResources().getStringArray(R.array.settings_array);
		ListView lv = getListView();
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.settings_list_item, settings));

		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		    	switch (position)
		    	{
		    	case 0:
		    		showDialog(TIME_DIALOG_ID);
		    		break;
		    	case 1:
		    		showDialog(NOTIFY_TYPE_DIALOG_ID);
		    		break;
		    	case 2:
		    		showDialog(LED_DIALOG_ID);
		    		break;
		    	}
		    }
		  });
		lv.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK))
				finish();
				return true;
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case TIME_DIALOG_ID:
	    	NotificationTime time = PreferenceUtil.getNotifierTime(this);
    	    int hour = time.getHour();
    	    int minutes = time.getMinutes();
	        return new TimePickerDialog(this,
	                mTimeSetListener, hour, minutes, false);
	    case LED_DIALOG_ID:
    		AlertDialog.Builder ledColorBuilder = new AlertDialog.Builder(SettingsPage.this);
    		ledColorBuilder.setTitle("Pick a color");
    		final String[] colors = PreferenceUtil.COLOR_STRINGS;
    		int colorIndex = PreferenceUtil.getSelectedColorIndex(this);
    		ledColorBuilder.setSingleChoiceItems(colors, colorIndex, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    		        Log.w("SettingsPage", "ledColor  = " + colors[item]);
    		        PreferenceUtil.setNotificationLedColor(SettingsPage.this, colors[item]);
    		        dialog.dismiss();
    		    }
    		});
    		alert_led_color = ledColorBuilder.create();
	    	return alert_led_color;
	    case NOTIFY_TYPE_DIALOG_ID:
    		AlertDialog.Builder builder = new AlertDialog.Builder(SettingsPage.this);
    		builder.setTitle("Notification Type");
    		String[] notify_type = getResources().getStringArray(R.array.notify_type_array);
    		int selectedIndex = notificationSelectedIndex(this);
    		builder.setSingleChoiceItems(notify_type, selectedIndex, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    		    	String[] notify_type = getResources().getStringArray(R.array.notify_type_array);
    		        Log.w("SettingsPage", "Notification Type  = " + notify_type[item]);
    		        PreferenceUtil.setAlertType(SettingsPage.this, notify_type[item]);
    		        dialog.dismiss();
    		    }
    		});
    		alert_notify_type = builder.create();
	    	return alert_notify_type;
	    }
	    return null;
	}
	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	        	PreferenceUtil.storeNotifierTime(SettingsPage.this, hourOfDay, minute);
	    	    Log.w("SettingsPage", "Time hours = " + hourOfDay);
	    	    Log.w("SettingsPage", "Time Minutes = " + minute);
	        }
	    };

	private static int notificationSelectedIndex(Context ctx)
	{
		boolean sound = PreferenceUtil.soundNotificationEnabled(ctx);
		boolean vibrate = PreferenceUtil.vibrateNotificationEnabled(ctx);

		if (sound && vibrate)
		{
			return 3;
		}

		if (sound)
		{
			return 1;
		}

		if (vibrate)
		{
			return 2;
		}

		return 0;
	}
}
