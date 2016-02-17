package com.daocaowu.itelligentprofile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBootReciver extends BroadcastReceiver {

	private static final String TAG = "MyBootReciver";
	private static final String SET_PROFILE = "setProfile";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		Intent myBootIntent = new Intent(context, ProfileTaskService.class);
//
////		Bundle bundle = new Bundle();
////		bundle.putString(SET_PROFILE, "1");
////		myBootIntent.putExtras(bundle);
//		myBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(myBootIntent);
		
		 

	}

}
