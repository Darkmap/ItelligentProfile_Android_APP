package com.daocaowu.itelligentprofile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TYPE;

public class TaskInitReceiver extends BroadcastReceiver {

	private static final String TAG = TaskService.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.v(TAG, "TaskInitReceiver-Action：" + action);
		
		if (DBManager.context == null) {
			DBManager.openDB(context);
		}
		
		if (DBManager.getTasks(TYPE.TASK, null, null, null) == null) {
			Log.v(TAG, "AlarmInitReceiver: FAILURE unable to get content resolver.  Tasks inactive.");
            return;
		}
		
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.v(TAG, "ACTION_BOOT_COMPLETED\t"+Intent.ACTION_BOOT_COMPLETED);
        }
		TaskService.setNextAlert(context);
		context.startService(new Intent(context, GPSService.class));
		//启动电池监控服务
		context.startService(new Intent(context, MonitorService.class));
	}

}
