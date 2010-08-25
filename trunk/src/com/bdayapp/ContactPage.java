package com.bdayapp;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdayapp.contacts.ContactInfo;

public class ContactPage extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.contact_page);
			Log.w("ContactPage", "Activity Created");
			TextView tview_cname;
			TextView tview_dob;
			boolean contactPhotoFound = true;
			ContactInfo cinfo = BdayNotifier.contactList.get(BdayNotifier.clickPosition);
			Log.w("ContactPage", "Activity Created");
	        tview_cname = (TextView)findViewById(R.id.contact_name_1);
	        Log.w("ContactPage", "Activity Created");
	        if (tview_cname != null)
	        {
	        	tview_cname.setText(cinfo.getContactName());
	        }
	        Log.w("ContactPage", "Activity Created");
	        tview_dob = (TextView)findViewById(R.id.date_of_birth_1);
	        Log.w("ContactPage", "Activity Created");
	        tview_dob.setText(Utils.format(cinfo.getDateOfBirth()));
	        Log.w("ContactPage", "Activity Created");
	        TextView tview_next = (TextView)findViewById(R.id.next_bday_1);
	        tview_next.setText(String.valueOf(cinfo.getNumOfDaysToNextBday()) + " days to Go");
	        tview_next.setTextColor(ColorStateList.valueOf(0xFFFF0000));
	        Bitmap bmap = null;
	        Log.w("ContactPage", "Activity Created");
	        //Log.w("getView", "Contact name = " + (elements.get(position)).contactName.toString());
	        try {
				bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cinfo.getContactPhotoUri());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Log.w("getView", "File not found for" + (cinfo).getContactName());
				contactPhotoFound = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageView iview_photo = (ImageView)findViewById(R.id.contact_image);
			if (contactPhotoFound)
			{
				iview_photo.setImageBitmap(bmap);
				Log.w("getView", "Image dimension = " + bmap.getHeight() + "x" + bmap.getWidth());
			}
			else
			{
				iview_photo.setImageDrawable(this.getResources().getDrawable(R.drawable.stock_contact_photo));
			}
			Log.w("ContactPage", "Name = " + cinfo.getContactName());
			Log.w("ContactPage", "Dob = " + cinfo.getDateOfBirth());
			
			View v = View.inflate(this, R.layout.contact_page, null);
			v.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK))
						finish();
					return true;
				}
			});
		}
}
