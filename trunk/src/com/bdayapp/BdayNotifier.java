package com.bdayapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentUris;
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
        TimeZone tz = TimeZone.getDefault();
        Calendar presentCal = new GregorianCalendar(tz);
        for (int i = 0; i < rowNum; i++)
        {	
        	contactCursor.moveToPosition(i);
        	//Log.w("BdayNotifier", "Name = " + contactCursor.getString(1));
        	String Dob = GetDobOfContact(this, contactCursor.getString(0));
        	//Log.w("BDayNOtifier", "DOB = " + Dob);
        	if (!Dob.equalsIgnoreCase(""))
        	{	
        		Calendar dobCal = new GregorianCalendar(tz);
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		Date date = new Date();
				try {
					date = (Date)formatter.parse(Dob);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.w("BdayNotifier", "Date = " + date.toGMTString());
        		contactInfo cinfo = new contactInfo();
        		cinfo.contactName = contactCursor.getString(1);
        		cinfo.dateOfBirth = date;
        		cinfo.contactID = contactCursor.getString(0);
        		dobCal.setTime(date);
        		int pres = 0, dob = 0, diff;
        		pres = presentCal.get(Calendar.DAY_OF_YEAR);
        		dob = dobCal.get(Calendar.DAY_OF_YEAR);
        		diff = pres - dob;
        		if (diff < 0)
        		{
        			cinfo.numOfDaysToNextBday = Math.abs(diff);
        		}
        		else
        		{
        			cinfo.numOfDaysToNextBday = 365 - diff;
        		}	
        		contact_list.add(cinfo);
        		cinfo.contactPhotoUri = getContactPhoto(this, cinfo.contactID);
        	}
        }
        Collections.sort(contact_list, new contactInfoComparator());
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
   private Uri getContactPhoto(Context ctx, String contactID)
   {
       Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
               .parseLong(contactID));
       Uri photo = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

       Log.w("getContactPhoto", "photo Uri = " + photo.toString());
       return photo;
   }
}
class contactInfoComparator implements Comparator<Object>
{
	public int compare(Object obj1, Object obj2)
	{
		contactInfo c1 = (contactInfo) obj1;
		contactInfo c2 = (contactInfo) obj2;
		
		return (c1.numOfDaysToNextBday - c2.numOfDaysToNextBday);
	}

}