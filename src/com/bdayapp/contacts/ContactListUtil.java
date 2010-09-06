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
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

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

		ArrayList<ContactInfo> contactList = new ArrayList<ContactInfo>();

		for (int i = 0; i < rowNum; i++) {
			contactCursor.moveToPosition(i);
			Date dob = ContactListUtil.getContactDob(ctx, contactCursor.getString(0));

			if (dob != null) {
				ContactInfo cInfo = new ContactInfo();
				cInfo.setContactName(contactCursor.getString(1));
				cInfo.setDateOfBirth(dob);
				cInfo.setContactId(contactCursor.getString(0));
				cInfo.setContactPhotoUri(ContactListUtil.getContactPhoto(ctx, cInfo.getContactId()));
				cInfo.setContactPhoneNumber(getContactPhoneNum(ctx, cInfo.getContactId()));
				contactList.add(cInfo);
			}
		}
		contactCursor.close();
		Collections.sort(contactList);

		return contactList;
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
		return photo;
	}

	public static String getContactPhoneNum(Context ctx, String contactId) {
		String[] nums = getContactPhoneNums(ctx, contactId);

		if (nums.length == 0)
		{
			return "";
		}
		else
		{
			return nums[0];
		}
	}

	public static String[] getContactPhoneNums(Context ctx, String contactId)
	{
		Cursor c = ctx.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Phone.NUMBER },
				Data.CONTACT_ID + "=" + contactId + " AND " + Data.MIMETYPE + "= '" + Phone.CONTENT_ITEM_TYPE
						+ "' AND " + Phone.TYPE + "=" + Phone.TYPE, null, Data.DISPLAY_NAME);

		String[] numbers = new String[0];

		if (c != null) {
			c.moveToFirst();

			int count = c.getCount();

			numbers = new String[count];

			for (int i = 0; i < count; i++)
			{
				c.moveToPosition(i);

				numbers[i] = c.getString(0);
			}

			c.close();
		}
		return numbers;
	}

	public static String getNextBdayText(ContactInfo info)
	{
		Calendar cal = Calendar.getInstance();
		int curYear = cal.get(Calendar.YEAR);

		cal.setTime((Date)info.getDateOfBirth().clone());

		int numOfDays = info.getNumOfDaysToNextBday();
		cal.add(Calendar.DAY_OF_MONTH, -numOfDays);

		int yearsCompleted = curYear - cal.get(Calendar.YEAR);

		if (numOfDays == 0)
		{
			return "Turned " + yearsCompleted + " Today";
		}

		if (numOfDays == 1)
		{
			return "Turns " + yearsCompleted + " Tomorrow";
		}

		return "Turns " + yearsCompleted + " in " + numOfDays + " days";
	}
}
