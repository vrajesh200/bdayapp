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

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bdayapp.contacts.ContactInfo;
import com.bdayapp.contacts.ContactListUtil;
import com.bdayapp.contacts.IContactListProvider;

public class BdayNotifier extends Activity implements IContactListProvider {

	private ArrayList<ContactInfo> contactList;

	public ArrayList<ContactInfo> getContactList() {
		return contactList;
	}

	public void setContactList(ArrayList<ContactInfo> contactList) {
		this.contactList = contactList;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new LoadTask(this).execute(this);

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
		if (item.getItemId() == R.id.preferences) {
			Intent prefIntent = new Intent(BdayNotifier.this, SettingsPage.class);
			startActivity(prefIntent);
		}
		return true;
	}

	private class LoadTask extends AsyncTask<Context, Void, ArrayList<ContactInfo>> {

		private IContactListProvider contactListProvider;

		public LoadTask(IContactListProvider contactListProvider) {
			this.contactListProvider = contactListProvider;
		}

		@Override
		protected ArrayList<ContactInfo> doInBackground(Context... params) {
			// perform long running operation operation
			return ContactListUtil.getContactList(params[0]);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(ArrayList<ContactInfo> result) {

			contactListProvider.setContactList(result);

			// execution of result of Long time consuming operation
			setContentView(R.layout.main);

			ListView listView = (ListView) findViewById(R.id.contact_list);
			listView.setAdapter(new BdayListAdapter(BdayNotifier.this, result));

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent i = new Intent(BdayNotifier.this, ContactPage.class);
					i.putExtra("ContactID", getContactList().get(position).getContactId());
					startActivity(i);
				}
			});

			ContactInfo firstContactInfo = getContactList().get(0);

			if (firstContactInfo.getNumOfDaysToNextBday() == 0) {
				Utils.setNotification(BdayNotifier.this, firstContactInfo.getContactId(),
						firstContactInfo.getContactName(), Notification.FLAG_AUTO_CANCEL);
			}

			Log.w("LoadTask", "Setting Alarm");
			Utils.setAlarm(BdayNotifier.this);
			Log.w("LoadTask", "Post Execute done");

		}

		private ArrayList<ContactInfo> getContactList() {
			return contactListProvider.getContactList();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation. For
			// example showing ProgessDialog
			setContentView(R.layout.splashscreen);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}
}
