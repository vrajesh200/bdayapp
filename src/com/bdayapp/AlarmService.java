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

//=====================================================================================================================
// $HeadURL: https://bdayapp.googlecode.com/svn/trunk/src/com/bdayapp/AlarmReceiver.java $
// Checked in by: $Author: psrinivas $
// $Date: 2010-08-29 02:26:41 +0530 (Sun, 29 Aug 2010) $
// $Revision: 37 $
//=====================================================================================================================

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