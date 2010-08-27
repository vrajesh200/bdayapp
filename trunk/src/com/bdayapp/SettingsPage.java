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
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsPage extends Activity implements OnClickListener {

	private NotificationManager mManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);

		Button button = (Button) findViewById(R.id.contact_list);

		button.setOnClickListener(this);

		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void onClick(View v) {

		Intent i = new Intent(this, BdayNotifier.class);

		Notification note = new Notification(R.drawable.icon, "hehe", System.currentTimeMillis());

		note.setLatestEventInfo(this, "title", "text",
				PendingIntent.getActivity(this.getBaseContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT));

		mManager.notify(0, note);

	}
}
