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

import java.util.Date;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdayapp.contacts.ContactListUtil;

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
			final String[] phoneNum = ContactListUtil.getContactPhoneNums(this, contactID);

			TextView tview_cname = (TextView)findViewById(R.id.contact_name_1);
	        tview_cname.setText(contactName);

	        TextView tview_dob = (TextView)findViewById(R.id.date_of_birth_1);
	        tview_dob.setText(Utils.format(dateOfBirth));

	        TextView tview_next = (TextView)findViewById(R.id.next_bday_1);
	        tview_next.setText(String.valueOf(numOfDaysLeft) + " days to Go");
	        tview_next.setTextColor(ColorStateList.valueOf(0xFFFF0000));

	        Bitmap bmap = null;
	        try {
				bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contactPhoto);
			} catch (Exception e) {
			}
			ImageView iview_photo = (ImageView)findViewById(R.id.contact_image_1);

			if (bmap != null)
			{
				iview_photo.setImageBitmap(bmap);
			}
			else
			{
				iview_photo.setImageDrawable(this.getResources().getDrawable(R.drawable.stock_contact_photo));
			}

			TextView tviewPhNum = (TextView)findViewById(R.id.contac_number);
			tviewPhNum.setText(phoneNum[0]);

			View v = View.inflate(this, R.layout.contact_page, null);
			v.setOnKeyListener(new OnKeyListener() {

				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK))
						finish();
					return true;
				}
			});

			Button callButton = (Button)findViewById(R.id.call_button);
			callButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + phoneNum[0]));
					startActivity(intent);
				}
			});

			Button textButton = (Button)findViewById(R.id.message_button);
			textButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					SmsManager smsManger = SmsManager.getDefault();
					smsManger.sendTextMessage(phoneNum[0], null, "Test message ignore", null, null);
				}
			});
			if (contactID.contentEquals("414"))
			{
				Utils.setNotification(this, contactID, contactName, Notification.FLAG_AUTO_CANCEL);
			}
		}
}
