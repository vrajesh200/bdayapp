package com.bdayapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.bdayapp.contacts.ContactInfo;
import com.bdayapp.contacts.ContactListUtil;

public class BdayNotifier extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView mContactList;
		mContactList = (ListView) findViewById(R.id.contact_list);
		ArrayList<ContactInfo> contactList = ContactListUtil.getContactList(this);

		BdayListAdapter m_Adapter = new BdayListAdapter(this, contactList);
		mContactList.setAdapter(m_Adapter);

	}

}
