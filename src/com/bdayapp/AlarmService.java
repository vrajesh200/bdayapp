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

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;

import com.bdayapp.contacts.ContactInfo;
import com.bdayapp.contacts.ContactListUtil;

public class AlarmService extends IntentService {

	 public AlarmService() {
		 super("Update Notification service");
	    }
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		ArrayList<ContactInfo> contactList = ContactListUtil.getContactList(this);
		if (contactList.get(0).getNumOfDaysToNextBday() == 0)
		{
			Utils.setNotification(this, contactList.get(0).getContactId(),
					contactList.get(0).getContactName(), Notification.FLAG_AUTO_CANCEL );
		}
		Log.w("AlarmService", "Setting Alarm");
		Utils.setAlarm(this);
	}

}
