package com.bdayapp;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
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
		setContentView(R.layout.main);
		boolean disableAlarm = true;
		ListView mContactList;
		mContactList = (ListView) findViewById(R.id.contact_list);
		contactList = ContactListUtil.getContactList(this);

		BdayListAdapter m_Adapter = new BdayListAdapter(this, contactList);
		mContactList.setAdapter(m_Adapter);
		if (disableAlarm != true)
		{
			// get a Calendar object with current time
			Calendar cal = Calendar.getInstance();
			// add 5 minutes to the calendar object
			cal.add(Calendar.MINUTE, 5);
			Intent intent = new Intent(this, AlarmReceiver.class);
			intent.putExtra("alarm_message", "O'Doyle Rules!");
			// In reality, you would want to have a static variable for the request code instead of 192837
			PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// Get the AlarmManager service
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		}
		mContactList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			      // When clicked, show a toast with the TextView text
				clickPosition = position;
				Intent i = new Intent(BdayNotifier.this, ContactPage.class);
				startActivity(i);
			    }
			});
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
}
