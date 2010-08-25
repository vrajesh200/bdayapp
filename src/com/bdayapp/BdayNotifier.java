package com.bdayapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		ListView mContactList;
		mContactList = (ListView) findViewById(R.id.contact_list);
		contactList = ContactListUtil.getContactList(this);

		BdayListAdapter m_Adapter = new BdayListAdapter(this, contactList);
		mContactList.setAdapter(m_Adapter);
		
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

}
