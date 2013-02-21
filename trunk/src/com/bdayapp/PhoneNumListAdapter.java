/*
 * Copyright (C) 2010 Srinivas Paladugu.
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

import com.bdayapp.contacts.PhoneNumInfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneNumListAdapter extends BaseAdapter {

	private final ArrayList<PhoneNumInfo> phoneList;
	private Context ctx;
	public PhoneNumListAdapter(Context context, ArrayList<PhoneNumInfo> phoneNumList) {
		phoneList = phoneNumList;
		ctx = context;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return phoneList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return phoneList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		if (convertView == null)
		{
			convertView = View.inflate(ctx, R.layout.phonenum_list, null);
		}
		// Set phone number
		TextView phNumTv = (TextView)convertView.findViewById(R.id.phonenum_text);
		phNumTv.setText(phoneList.get(position).getPhoneNum());
		
		// Set phone type
		TextView phTypeTv = (TextView)convertView.findViewById(R.id.phonenum_type);
		phTypeTv.setText(phoneList.get(position).getPhoneType());
		
		ImageView callButton = (ImageView)convertView.findViewById(R.id.call_button);
		callButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + (phoneList.get(index).getPhoneNum())));
				v.getContext().startActivity(intent);
			}
		});

		ImageView textButton = (ImageView)convertView.findViewById(R.id.message_button);
		textButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				SmsManager smsManger = SmsManager.getDefault();
				smsManger.sendTextMessage(phoneList.get(index).getPhoneNum(), null, "Test message ignore", null, null);
			}
		});
		
		return convertView;
	}

}
