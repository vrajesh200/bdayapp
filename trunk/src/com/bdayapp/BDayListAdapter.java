package com.bdayapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdayapp.contacts.ContactInfo;

public class BDayListAdapter extends BaseAdapter {

	private ArrayList<ContactInfo> elements;
	private Context c;

	public BDayListAdapter (Context cxt, ArrayList<ContactInfo> list)
	{
		elements = list;
		c = cxt;
	}

	public int getCount() {
		return elements.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return elements.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tview_cname;
		TextView tview_dob;
		View v = convertView;
		boolean contactPhotoFound = true;
        if (v == null) {
        	v = View.inflate(c, R.layout.list_item, null);
        }
        tview_cname = (TextView)v.findViewById(R.id.contact_name);
        tview_cname.setText((elements.get(position)).getContactName());
        tview_dob = (TextView)v.findViewById(R.id.date_of_birth);
        tview_dob.setText(Utils.format(elements.get(position).getDateOfBirth()));

        TextView tview_next = (TextView)v.findViewById(R.id.next_bday);
        tview_next.setText(String.valueOf(elements.get(position).getNumOfDaysToNextBday()) + " days to Go");
        tview_next.setTextColor(ColorStateList.valueOf(0xFFFF0000));
        Bitmap bmap = null;
        //Log.w("getView", "Contact name = " + (elements.get(position)).contactName.toString());
        try {
			bmap = MediaStore.Images.Media.getBitmap(c.getContentResolver(), elements.get(position).getContactPhotoUri());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.w("getView", "File not found for" + (elements.get(position)).getContactName());
			contactPhotoFound = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView iview_photo = (ImageView)v.findViewById(R.id.contact_image);
		if (contactPhotoFound)
		{
			iview_photo.setImageBitmap(bmap);
			Log.w("getView", "Image dimension = " + bmap.getHeight() + "x" + bmap.getWidth());
		}
		else
		{
			iview_photo.setImageDrawable(c.getResources().getDrawable(R.drawable.stock_contact_photo));
		}
        return v;
	}

}
