package com.bdayapp;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
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

			ContactInfo cinfo = BdayNotifier.contactList.get(BdayNotifier.clickPosition);
			TextView tview_cname = (TextView)findViewById(R.id.contact_name_1);
	        tview_cname.setText(cinfo.getContactName());
	        TextView tview_dob = (TextView)findViewById(R.id.date_of_birth_1);
	        tview_dob.setText(Utils.format(cinfo.getDateOfBirth()));

	        TextView tview_next = (TextView)findViewById(R.id.next_bday_1);
	        tview_next.setText(String.valueOf(cinfo.getNumOfDaysToNextBday()) + " days to Go");
	        tview_next.setTextColor(ColorStateList.valueOf(0xFFFF0000));

	        Bitmap bmap = null;
	        try {
				bmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cinfo.getContactPhotoUri());
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
			tviewPhNum.setText(cinfo.getContactPhoneNumber());
			
			View v = View.inflate(this, R.layout.contact_page, null);
			v.setOnKeyListener(new OnKeyListener() {

				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_BACK))
						finish();
					return true;
				}
			});


			// sample notification
			/*
			Intent configIntent = new Intent(this, Configuration.class);

			Notification note = new Notification(R.drawable.icon, cinfo.getContactName(), System.currentTimeMillis());
			note.setLatestEventInfo(this, "Bday Notification", cinfo.getContactName() +"'s Bday",
					PendingIntent.getActivity(this.getBaseContext(), 0, configIntent, PendingIntent.FLAG_CANCEL_CURRENT));

			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(0, note);
			*/
		}
}