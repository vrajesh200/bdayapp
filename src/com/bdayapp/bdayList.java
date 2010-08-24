package com.bdayapp;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.ColorStateList;

import java.text.DateFormat;
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
        if (v == null) {
        	v = View.inflate(c, R.layout.list_item, null);
        }
        tview_cname = (TextView)v.findViewById(R.id.contact_name);
        tview_cname.setText((elements.get(position)).contactName.toString());
        tview_dob = (TextView)v.findViewById(R.id.date_of_birth);
        tview_dob.setText(DateFormat.getDateInstance().format(elements.get(position).dateOfBirth));
        
        TextView tview_next = (TextView)v.findViewById(R.id.next_bday);
        tview_next.setText(String.valueOf(elements.get(position).numOfDaysToNextBday) + " days to Go");
        tview_next.setTextColor(ColorStateList.valueOf(0xFFFF0000));
        return v;
	}

}
