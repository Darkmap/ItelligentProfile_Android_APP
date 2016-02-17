package com.daocaowu.itelligentprofile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ProfileTaskService2 extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
//		Toast.makeText(this, "show onCreate() Service", Toast.LENGTH_SHORT).show();
//		Log.v("onCreate- s", "show onCreate() Service");
//		Intent i = new Intent(this,ProfileTaskReceiver.class);
//		sendBroadcast(i);
	}

	@Override
	public void onDestroy() {
//		Toast.makeText(this, "onDestroy  fdffdd", Toast.LENGTH_SHORT).show();
//		Log.v("onDestroy- s", "show onCreate() Service");
	}

	@Override
	public void onStart(Intent intent, int startId) {
//		Toast.makeText(this, "onStart gfgfg ", Toast.LENGTH_SHORT).show();
//		Log.v("onStart- s", "show onCreate() Service");
//		Intent i = new Intent(this,ProfileTaskReceiver.class);
//		sendBroadcast(i);
//		Log.v("onStart- s", "show onCreate() Service2");
	}

}
