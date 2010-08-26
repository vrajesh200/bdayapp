//=====================================================================================================================
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
	public static int clickPosition = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean disableAlarm = true;
		new LoadTask().execute(this);

		if (disableAlarm != true)
		{
			// get a Calendar object with current time
			Calendar cal = Calendar.getInstance();
			// add 5 minutes to the calendar object
			cal.add(Calendar.MINUTE, 5);
			Intent intent = new Intent(this, AlarmReceiver.class);			// In reality, you would want to have a static variable for the request code instead of 192837
			PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// Get the AlarmManager service
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
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
			ArrayList<ContactInfo> mcontactList = ContactListUtil.getContactList(params[0]);

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
			mContactList.setAdapter(m_Adapter);
			BdayNotifier.contactList = result;
			mContactList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
				        int position, long id) {
				      // When clicked, show a toast with the TextView text
					BdayNotifier.clickPosition = position;
					Intent i = new Intent(BdayNotifier.this, ContactPage.class);
					startActivity(i);
				    }
				});


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

}
