package com.bdayapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bdayapp.contacts.ContactInfo;
import com.bdayapp.contacts.ContactListUtil;

public class BdayNotifier extends Activity
{
	ArrayAdapter<String> mdArrayAdapter = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView mContactList;
        mContactList = (ListView)findViewById(R.id.contact_list);
        ArrayList<ContactInfo> contactList = ContactListUtil.getContactList(this);

        BDayListAdapter m_Adapter = new BDayListAdapter(this, contactList);
        mContactList.setAdapter(m_Adapter);

    }


}
