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

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdayapp.contacts.ContactInfo;
import com.bdayapp.contacts.ContactListUtil;

public class BdayListAdapter extends BaseAdapter {

	private ArrayList<ContactInfo> elements;
	private Context ctx;
	private Drawable defaultDrawable;

	public BdayListAdapter(Context cxt, ArrayList<ContactInfo> list) {
		elements = list;
		ctx = cxt;
		defaultDrawable = ctx.getResources().getDrawable(R.drawable.stock_contact_photo);
	}

	public int getCount() {
		return elements.size();
	}

	public Object getItem(int position) {
		return elements.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(ctx, R.layout.list_item, null);
		}

		ContactInfo contactInfo = elements.get(position);

		// ContactName
		TextView textView = (TextView) convertView.findViewById(R.id.contact_name);
		textView.setText(contactInfo.getContactName());

		// ContactDOB
		textView = (TextView) convertView.findViewById(R.id.date_of_birth);
		textView.setText(Utils.format(contactInfo.getDateOfBirth()));

		// DaysToBday
		textView = (TextView) convertView.findViewById(R.id.next_bday);
		textView.setText(ContactListUtil.getNextBdayText(contactInfo));
		textView.setTextColor(ColorStateList.valueOf(0xFFFF0000));

		// ContactPhoto
		Bitmap bitmap = null;
		try {
			bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), contactInfo.getContactPhotoUri());
		} catch (Exception e) {
			// nothing to do, we didn't get the image :(
		}

		ImageView imageView = (ImageView) convertView.findViewById(R.id.contact_image);

		if (bitmap == null) {
			imageView.setImageDrawable(defaultDrawable);
		} else {
			imageView.setImageBitmap(bitmap);
		}

		return convertView;
	}
}
