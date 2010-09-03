/*
 * Copyright (C) 2010 Venkata Rajesh.
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

//=====================================================================================================================
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp.contacts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import com.bdayapp.Utils;

public class ContactListUtil {

	private static Cursor getContactCursor(Context ctx) {
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] BdayList = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + "1" + "'";
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
		String[] selectionArgs = null;
		return ctx.getContentResolver().query(uri, BdayList, selection, selectionArgs, sortOrder);
	}

	public static ArrayList<ContactInfo> getContactList(Context ctx) {
		Cursor contactCursor = getContactCursor(ctx);
		int rowNum = contactCursor.getCount();
		contactCursor.moveToFirst();

		ArrayList<ContactInfo> contact_list = new ArrayList<ContactInfo>();
		for (int i = 0; i < rowNum; i++) {
			contactCursor.moveToPosition(i);
			// Log.w("BdayNotifier", "Name = " + contactCursor.getString(1));
			Date dob = ContactListUtil.getContactDob(ctx, contactCursor.getString(0));
			// Log.w("BDayNOtifier", "DOB = " + Dob);
			if (dob != null) {
				Log.w("BdayNotifier", "Date = " + dob.toGMTString());
				ContactInfo cinfo = new ContactInfo();
				cinfo.setContactName(contactCursor.getString(1));
				cinfo.setDateOfBirth(dob);
				cinfo.setContactId(contactCursor.getString(0));
				cinfo.setContactPhotoUri(ContactListUtil.getContactPhoto(ctx, cinfo.getContactId()));

				String phoneNum = getContactPhoneNum(ctx, contactCursor.getString(0));
				Log.w("ContactListUtil", cinfo.getContactName() + " = " + phoneNum);
				cinfo.setContactPhoneNumber(phoneNum);
				contact_list.add(cinfo);
			}
		}
		Collections.sort(contact_list);
		Log.w("BdayNotifier", "Num of items in contact_list = " + contact_list.size());

		return contact_list;
	}

	public static Date getContactDob(Context ctx, String ContactID) {
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

	public static String getContactPhoneNum(Context ctx, String ContactID) {
		String PhoneNum = "";
		Cursor c = ctx.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Phone.NUMBER },
				Data.CONTACT_ID + "=" + ContactID + " AND " + Data.MIMETYPE + "= '" + Phone.CONTENT_ITEM_TYPE
						+ "' AND " + Phone.TYPE + "=" + Phone.TYPE, null, Data.DISPLAY_NAME);
		if (c != null) {
			c.moveToFirst();

			Log.w("getContactPhoneNum", "Num of Columns = " + c.getColumnCount());
			for (int i = 0; i < c.getColumnCount(); i++)
			{
				Log.w("getContactPhoneNum", "Column[" +i +"] = " + c.getColumnName(i));
			}
			Log.w("getContactPhoneNum", "Num of phone numbers  = " + c.getCount());
			if (c.getCount() != 0) {
				PhoneNum = c.getString(0);
			}
			c.close();
		}
		return PhoneNum;
	}

	public static String getNextBdayText(ContactInfo info)
	{
		Calendar cal = Calendar.getInstance();
		int curYear = cal.get(Calendar.YEAR);

		cal.setTime((Date)info.getDateOfBirth().clone());

		int numOfDays = info.getNumOfDaysToNextBday();

		cal.add(Calendar.DAY_OF_MONTH, -numOfDays);

		int diff = curYear - cal.get(Calendar.YEAR);

		if (numOfDays == 0)
		{
			return "Turned " + diff + " Today";
		}

		if (numOfDays == 1)
		{
			return "Turns " + diff + " Tomorrow";
		}

		return "Turns " + diff + " in " + numOfDays + " days";
	}
}
