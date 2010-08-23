package com.bdayapp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class BdayNotifier extends Activity
{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView mContactList;
        mContactList = (ListView)findViewById(R.id.contact_list);
        Cursor contactCursor = getContactList();
        String[] columnNames = contactCursor.getColumnNames();
        int columnNum = contactCursor.getColumnCount();
        int rowNum = contactCursor.getCount();
        for (int i = 0; i < columnNum; i++)
        {
        	Log.w("BdayNotifier", columnNames[i].toString());
        	Log.w("BdayNotifier", "Num of records = " + rowNum);
        }
        contactCursor.moveToPosition(2);
        String[] contactId = new String[]{
        		contactCursor.getString(0)		
        };
        Log.w("BdayNotifier", "ID = " + contactId[0]);
        Log.w("BdayNotifier", "Name = " + contactCursor.getString(1));
        String[] fields = new String[] {
                ContactsContract.Data.DISPLAY_NAME               
        };

        String Dob =  GetDobOfContact(contactCursor.getString(0));
        
        Log.w("BdayNotifier", "DOB = " + Dob);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, contactCursor,
                fields, new int[] {R.id.contact_name_});
        mContactList.setAdapter(adapter);
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
    
    private String GetDobOfContact(String ID)
    {
        Cursor c = this.getContentResolver().query(
     	       Data.CONTENT_URI,
     	       new String[] { Event.DATA },
     	       Data.CONTACT_ID + "=" + ID + " AND "
     	         + Data.MIMETYPE + "= '"
     	         + Event.CONTENT_ITEM_TYPE + "' AND "
     	         + Event.TYPE + "=" + Event.TYPE_BIRTHDAY,
     	       null, Data.DISPLAY_NAME);
        if (c == null)
        {
        	return "c==NULL";
        }
        c.moveToFirst();
        String Dob = c.getString(0);
        c.close();
        return Dob;
    }
}