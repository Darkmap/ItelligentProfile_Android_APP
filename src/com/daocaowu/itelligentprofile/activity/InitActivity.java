package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.service.TaskReceiverService;
import com.daocaowu.itelligentprofile.service.TaskService;

public class InitActivity extends BaseActivity {

	private static final String TAG = InitActivity.class.getSimpleName();
	
	PendingIntent m_restartIntent;
	String RESTART_INTENT_KEY = "restart";
	int CRASHED_CODE = -1;
	private UncaughtExceptionHandler m_handler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.d(TAG, "uncaught exception is catched!");
			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
					m_restartIntent);
			System.exit(2);
		}
	};
	
	private void setUnCatchException(){
		Intent intent = getIntent();
		int code = intent.getIntExtra(RESTART_INTENT_KEY, 0);
		if (CRASHED_CODE == code) {
			/** You can do something here. */
			Log.d(TAG, "So sorry that the application crashed.");
		}
		intent.putExtra(RESTART_INTENT_KEY, CRASHED_CODE);
		m_restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Thread.setDefaultUncaughtExceptionHandler(m_handler);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setUnCatchException();
		DBManager.openDB(getApplicationContext());

		Task task = null;
		
		final byte[] data = getIntent().getByteArrayExtra(TaskService.TASK_RAW_DATA);

		if (data != null) {
			Parcel in = Parcel.obtain();
			in.unmarshall(data, 0, data.length);
			in.setDataPosition(0);
			task = Task.CREATOR.createFromParcel(in);
		}
		
		if (task == null) {
			Log.v(TAG," failed to parse the alarm from the intent");
			return ;
		}
		
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS aaa");
		Log.v(TAG, ".onReceive() id " + task.getTaskId() + " setFor "
				+ sdf.format(new Date(task.getRemindTime())));

		TaskReceiverService.task = task;
		
		TaskService.setNextAlert(getApplicationContext());
		
		Intent setProfileOfTask = new Intent(TaskService.TASK_ALERT_ACTION);
		setProfileOfTask.putExtra(TaskService.TASK_INTENT_EXTRA, task);
		startService(setProfileOfTask);
	}
}
