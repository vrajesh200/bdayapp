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

//=====================================================================================================================
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.bdayapp.contacts.ContactInfo;
import com.bdayapp.contacts.ContactListUtil;

public class BdayNotifier extends Activity {

	public static ArrayList<ContactInfo> contactList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		boolean alarmWakeup = false;
		if (bundle != null)
		{
			alarmWakeup = bundle.getBoolean("IsAlarmWakeUp");
		}
		if (!alarmWakeup)
		{
			new LoadTask().execute(this);
		}
		else
		{
			contactList = getContactList(this);
			if (contactList.get(0).getNumOfDaysToNextBday() == 0)
			{
				setNotification(0);
			}
			setAlarm();
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater inflater = getMenuInflater();
		  inflater.inflate(R.menu.main_menu, menu);
		  return true;
		}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	   if (item.getItemId() == R.id.preferences)
	   {
		   Intent prefIntent = new Intent(BdayNotifier.this, SettingsPage.class);
		   startActivity(prefIntent);
	   }
	   return true;
	}


	private class LoadTask extends AsyncTask<Activity, Void, ArrayList<ContactInfo>>{

		@Override
		protected ArrayList<ContactInfo> doInBackground(Activity... params) {
			// perform long running operation operation
			ArrayList<ContactInfo> mcontactList = getContactList(params[0]);

			return mcontactList;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(ArrayList<ContactInfo> result) {
			// execution of result of Long time consuming operation
			setContentView(R.layout.main);
			ListView mContactList;
			mContactList = (ListView) findViewById(R.id.contact_list);
			BdayListAdapter m_Adapter = new BdayListAdapter(BdayNotifier.this, result);
			BdayNotifier.contactList = result;
			mContactList.setAdapter(m_Adapter);
			mContactList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
				        int position, long id) {
					Intent i = new Intent(BdayNotifier.this, ContactPage.class);
					i.putExtra("IndexInList", position);
					startActivity(i);
				    }
				});
			if (contactList.get(0).getNumOfDaysToNextBday() == 0)
			{
				setNotification(0);
			}
			setAlarm();
			Log.w("LoadTask", "Post Execute done");

		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
		// Things to be done before execution of long running operation. For example showing ProgessDialog
			setContentView(R.layout.splashscreen);
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
	      // Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
		 }
	}
	
	private void setAlarm() {
		// get a Calendar object with current time
		Calendar cal = Calendar.getInstance();
		// add 5 minutes to the calendar object
		cal.add(Calendar.MINUTE, 5);
		Date dt = cal.getTime();
		Log.w("setAlarm", "Time = " + dt.getHours() + dt.getMinutes());
		/*dt.setHours(1);
		dt.setMinutes(0);
		dt.setSeconds(0);
		cal.setTime(dt);
		Log.w("setAlarm", "Time = " + dt.getHours() + dt.getMinutes());*/
		Intent intent = new Intent(this, AlarmReceiver.class);
		// In reality, you would want to have a static variable for the request code instead of 192837
		PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
	}
	
	private void setNotification(int position) {
		Intent configIntent = new Intent(BdayNotifier.this, ContactPage.class);
		configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		configIntent.putExtra("IndexInList", 0);
		Notification note = new Notification(R.drawable.icon, contactList.get(position).getContactName(), System.currentTimeMillis());
		note.flags = Notification.FLAG_AUTO_CANCEL;
		note.setLatestEventInfo(BdayNotifier.this, "Bday Notification", contactList.get(position).getContactName() +"'s Bday",
				PendingIntent.getActivity(BdayNotifier.this.getBaseContext(), 0, configIntent, PendingIntent.FLAG_CANCEL_CURRENT));
			
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, note);
	}
	
	private ArrayList<ContactInfo> getContactList(Activity activity) {
		return ContactListUtil.getContactList(activity);
	}
}
