package com.bdayapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
        ArrayList<contactInfo> contact_list = new ArrayList<contactInfo>();
        for (int i = 0; i < rowNum; i++)
        {
        	contactCursor.moveToPosition(i);
        	Log.w("BdayNotifier", "Name = " + contactCursor.getString(1));
        	String Dob = GetDobOfContact(this, contactCursor.getString(0));
        	Log.w("BDayNOtifier", "DOB = " + Dob);
        	if (!Dob.equalsIgnoreCase(""))
        	{
        		contactInfo cinfo = new contactInfo();
        		cinfo.contactName = contactCursor.getString(1);
        		cinfo.dateOfBirth = Dob;
        		contact_list.add(cinfo);
        	}
        }
        Log.w("BdayNotifier", "Num of items in contact_list = " + contact_list.size());
        bdayList m_Adapter = new bdayList(this, contact_list);
        mContactList.setAdapter(m_Adapter);
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
    
    public String GetDobOfContact(Context ctx, String ContactID)
    {
    	String Dob = "";
        Cursor c = ctx.getContentResolver().query(
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