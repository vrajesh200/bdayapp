package com.bdayapp;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import com.bdayapp.contacts.ContactListUtil;

public class AlarmService extends IntentService {

	 public AlarmService() {
		 super("Update Notification service");
	    }
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (BdayNotifier.contactList == null)
		{
			BdayNotifier.contactList = ContactListUtil.getContactList(this);
		}
		if (BdayNotifier.contactList.get(0).getNumOfDaysToNextBday() == 0)
		{
			Utils.setNotification(this, 0,BdayNotifier.contactList.get(0).getContactName(), Notification.FLAG_AUTO_CANCEL );
		}
		Utils.setAlarm(this);
	}

}
