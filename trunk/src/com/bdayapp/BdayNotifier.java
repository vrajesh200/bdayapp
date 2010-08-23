package com.bdayapp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


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
        Cursor contactCursor = getContactList();
        int rowNum = contactCursor.getCount();
        contactCursor.moveToFirst();
        mdArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        for (int i = 0; i < rowNum; i++)
        {
        	contactCursor.moveToPosition(i);
        	Log.w("BdayNotifier", "Name = " + contactCursor.getString(1));
        	String Dob = GetDobOfContact(contactCursor.getString(0));
        	Log.w("BDayNOtifier", "DOB = " + Dob);
        	if (!Dob.equalsIgnoreCase(""))
        	{
        		mdArrayAdapter.add(contactCursor.getString(1) + "\n" + Dob);
        	}
        }
        mContactList.setAdapter(mdArrayAdapter);
    }
    
    private Cursor getContactList()
    {
    	Uri uri = ContactsContract.Contacts.CONTENT_URI;
    	String[] BdayList = new String[] {
    			ContactsContract.Contacts._ID,
    			ContactsContract.Contacts.DISPLAY_NAME,
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
        "1" + "'";
    	String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
    	String[] selectionArgs = null;
    	return managedQuery(uri, BdayList, selection, selectionArgs, sortOrder);
    }
    
    private String GetDobOfContact(String ContactID)
    {
    	String Dob = "";
        Cursor c = this.getContentResolver().query(
     	       Data.CONTENT_URI,
     	       new String[] { Event.DATA },
     	       Data.CONTACT_ID + "=" + ContactID + " AND "
     	         + Data.MIMETYPE + "= '"
     	         + Event.CONTENT_ITEM_TYPE + "' AND "
     	         + Event.TYPE + "=" + Event.TYPE_BIRTHDAY,
     	       null, Data.DISPLAY_NAME);
        if (c != null)
        {
        	c.moveToFirst();
        	if (c.getCount() != 0)
        	{
        		Dob = c.getString(0);
        	}
        	c.close();
        }
        return Dob;
    }
}