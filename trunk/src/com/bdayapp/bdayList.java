package com.bdayapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class bdayList extends BaseAdapter {

	private ArrayList<contactInfo> elements;
	private Context c;

	public bdayList (Context cxt, ArrayList<contactInfo> list)
	{
		elements = list;
		c = cxt;
	}
	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return elements.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tview_cname;
		TextView tview_dob;
		View v = convertView;
		
        if (v == null) {
        	v = View.inflate(c, R.layout.list_item, null);
        }
        
        tview_cname = (TextView)v.findViewById(R.id.contact_name);
        tview_cname.setText((elements.get(position)).contactName.toString());
        
        tview_dob = (TextView)v.findViewById(R.id.date_of_birth);
        tview_dob.setText((elements.get(position)).dateOfBirth.toString());
        return v;
	}

}
