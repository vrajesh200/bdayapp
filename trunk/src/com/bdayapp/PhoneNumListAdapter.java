package com.bdayapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PhoneNumListAdapter extends BaseAdapter {

	private final String[] phoneList;
	private Context ctx;
	public PhoneNumListAdapter(Context context, String[] phoneNumList) {
		phoneList = phoneNumList;
		ctx = context;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return phoneList.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return phoneList[position];
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
		// Set phonenum String
		TextView phNumTv = (TextView)convertView.findViewById(R.id.phonenum_text);
		phNumTv.setText(phoneList[position]);
		
		Button callButton = (Button)convertView.findViewById(R.id.call_button);
		callButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + phoneList[index]));
				v.getContext().startActivity(intent);
			}
		});

		Button textButton = (Button)convertView.findViewById(R.id.message_button);
		textButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				SmsManager smsManger = SmsManager.getDefault();
				smsManger.sendTextMessage(phoneList[index], null, "Test message ignore", null, null);
			}
		});
		
		return convertView;
	}

}
