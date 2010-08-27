//=====================================================================================================================
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp;

import android.app.Activity;
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

import com.bdayapp.contacts.ContactInfo;

public class ContactPage extends Activity {
		public ContactInfo cinfo = null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.contact_page);
			Bundle bundle = this.getIntent().getExtras();
			int position = bundle.getInt("IndexInList");
			cinfo = BdayNotifier.contactList.get(position);
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

			Button callButton = (Button)findViewById(R.id.call_button);
			callButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + cinfo.getContactPhoneNumber()));
					startActivity(intent);
				}
			});

			Button textButton = (Button)findViewById(R.id.message_button);
			textButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					SmsManager smsManger = SmsManager.getDefault();
					smsManger.sendTextMessage(cinfo.getContactPhoneNumber(), null, "Test message ignore", null, null);
				}
			});
		}
}
