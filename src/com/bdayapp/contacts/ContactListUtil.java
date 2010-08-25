package com.bdayapp.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Data;
import android.util.Log;

import com.bdayapp.Utils;

public class ContactListUtil {

	private static Cursor getContactCursor(Activity activity) {
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] BdayList = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + "1" + "'";
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
		String[] selectionArgs = null;
		return activity.managedQuery(uri, BdayList, selection, selectionArgs, sortOrder);
	}

	public static ArrayList<ContactInfo> getContactList(Activity activity) {
		Cursor contactCursor = getContactCursor(activity);
		int rowNum = contactCursor.getCount();
		contactCursor.moveToFirst();

		ArrayList<ContactInfo> contact_list = new ArrayList<ContactInfo>();
		for (int i = 0; i < rowNum; i++) {
			contactCursor.moveToPosition(i);
			// Log.w("BdayNotifier", "Name = " + contactCursor.getString(1));
			Date dob = ContactListUtil.getDobOfContact(activity, contactCursor.getString(0));
			// Log.w("BDayNOtifier", "DOB = " + Dob);
			if (dob != null) {
				Log.w("BdayNotifier", "Date = " + dob.toGMTString());
				ContactInfo cinfo = new ContactInfo();
				cinfo.setContactName(contactCursor.getString(1));
				cinfo.setDateOfBirth(dob);
				cinfo.setContactId(contactCursor.getString(0));
				cinfo.setContactPhotoUri(ContactListUtil.getContactPhoto(activity, cinfo.getContactId()));
				contact_list.add(cinfo);
			}
		}
		Collections.sort(contact_list);
		Log.w("BdayNotifier", "Num of items in contact_list = " + contact_list.size());

		return contact_list;
	}

	public static Date getDobOfContact(Context ctx, String ContactID) {
		String dateOfBirth = "";
		Cursor c = ctx.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Event.DATA },
				Data.CONTACT_ID + "=" + ContactID + " AND " + Data.MIMETYPE + "= '" + Event.CONTENT_ITEM_TYPE
						+ "' AND " + Event.TYPE + "=" + Event.TYPE_BIRTHDAY, null, Data.DISPLAY_NAME);
		if (c != null) {
			c.moveToFirst();
			if (c.getCount() != 0) {
				dateOfBirth = c.getString(0);
			}
			c.close();
		}
		return Utils.parse(dateOfBirth);
	}

	public static Uri getContactPhoto(Context ctx, String contactID) {
		Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactID));
		Uri photo = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

		Log.w("getContactPhoto", "photo Uri = " + photo.toString());
		return photo;
	}

}
