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

package com.bdayapp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Notification;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bdayapp.contacts.ContactListUtil;
import com.bdayapp.contacts.PhoneNumInfo;

public class ContactPage extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.contact_page);
			Bundle bundle = this.getIntent().getExtras();
			String contactID = bundle.getString("ContactID");

			String contactName = ContactListUtil.getContactName(this, contactID);
			Date dateOfBirth = ContactListUtil.getContactDob(this, contactID);
			int numOfDaysLeft = Utils.numberOfDaysToBday(dateOfBirth) ;
			Uri contactPhoto = ContactListUtil.getContactPhoto(this, contactID);
			final ArrayList<PhoneNumInfo> phoneNum = ContactListUtil.getContactPhoneNums(this, contactID);

			// Set Contact Name
			TextView tview_cname = (TextView)findViewById(R.id.contact_name_1);
	        tview_cname.setText(contactName);

	        // Set Contact DoB	        
	        TextView tview_dob = (TextView)findViewById(R.id.date_of_birth_1);
	        tview_dob.setText(Utils.format(dateOfBirth));

	        // Set DaysToGo
	        TextView tview_next = (TextView)findViewById(R.id.next_bday_1);
	        tview_next.setText(String.valueOf(numOfDaysLeft) + " days to Go");
	        tview_next.setTextColor(ColorStateList.valueOf(0xFFFF0000));

	        Bitmap bmap = null;
	        try {
				bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contactPhoto);
			} catch (Exception e) {
			}
			ImageView iview_photo = (ImageView)findViewById(R.id.contact_image_1);

			// Set Contact Photo
			if (bmap != null)
			{
				iview_photo.setImageBitmap(bmap);
			}
			else
			{
				iview_photo.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_contact_picture));
			}

			ListView phoneNumList = (ListView)findViewById(R.id.phonenum_list);
			PhoneNumListAdapter mPhAdapter = new PhoneNumListAdapter(this, phoneNum);
			phoneNumList.setAdapter(mPhAdapter);
			// Set Contact Phone Number

			View v = View.inflate(this, R.layout.contact_page, null);
			v.setOnKeyListener(new OnKeyListener() {

				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK))
						finish();
					return true;
				}
			});


			// Test Code: Delete later
			if (contactID.contentEquals("414"))
			{
				Utils.setNotification(this, contactID, contactName, Notification.FLAG_AUTO_CANCEL);
			}
		}
}
