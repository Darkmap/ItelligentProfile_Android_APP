package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.daocaowu.itelligentprofile.utils.ConstData;
import com.daocaowu.itelligentprofile.utils.ManagerReceiver;
import com.daocaowu.itelligentprofile.utils.ManagerReceiver.ReceiverInterface;

public class BaseActivity extends Activity{

	private static final String TAG = "BaseActivity";
	
	private ManagerReceiver mBroadReceiver;
//	public static BaseActivity instance;
	
//	public LocationClient mLocationClient = null;
	
//	private TaskReceiver mTaskReceiver;

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

		Log.v(getLocalClassName(), "onCreate");

//		setUnCatchException();
//		instance = this;
		mBroadReceiver = new ManagerReceiver(new MyInterface());
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstData.INTENT_EXIT);
		registerReceiver(mBroadReceiver, filter);
		
//		mLocationClient = new LocationClient(getApplicationContext());     //ÉùÃ÷LocationClientÀà
//	    mLocationClient.registerLocationListener( new MyLocationListener() );    //×¢²á¼àÌýº¯Êý
		
//		mTaskReceiver = new TaskReceiver();
//		IntentFilter taskFilter = new IntentFilter();
//		taskFilter.addAction(TaskService.TASK_ALERT_ACTION);
//		taskFilter.addAction(TaskService.TASK_DISMISS_ACTION);
//		taskFilter.addAction(TaskService.TASK_DONE_ACTION);
//		taskFilter.addAction(TaskService.TASK_INTENT_EXTRA);
//		taskFilter.addAction(TaskService.TASK_KILLED);
//		taskFilter.addAction(TaskService.TASK_RAW_DATA);
//		taskFilter.addAction(TaskService.TASK_SNOOZE_ACTION);
//		registerReceiver(mTaskReceiver, taskFilter);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBroadReceiver != null)
			unregisterReceiver(mBroadReceiver);
//		if (mTaskReceiver != null) {
//			unregisterReceiver(mTaskReceiver);
//		}
	}

	private class MyInterface implements ReceiverInterface {

		@Override
		public void refresh(Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals(ConstData.INTENT_EXIT)) {
				finish();
			}
		}
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
	 
			Log.v(TAG, sb.toString());
		}
	@Override
	public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Poi time : ");
				sb.append(poiLocation.getTime());
				sb.append("\nerror code : ");
				sb.append(poiLocation.getLocType());
				sb.append("\nlatitude : ");
				sb.append(poiLocation.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(poiLocation.getLongitude());
				sb.append("\nradius : ");
				sb.append(poiLocation.getRadius());
				if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\naddr : ");
					sb.append(poiLocation.getAddrStr());
				} 
				if(poiLocation.hasPoi()){
					sb.append("\nPoi:");
					sb.append(poiLocation.getPoi());
				}else{				
					sb.append("noPoi information");
				}
				Log.v(TAG, sb.toString());
			}
	}
	
}
