package com.daocaowu.itelligentprofile.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.util.Log;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.activity.InitActivity;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class TaskReceiver extends BroadcastReceiver {

	private static final String TAG = TaskReceiver.class.getSimpleName();
	/**
	 * 如果闹钟时间比系统固定时间还要小，则忽略它. 这个时间可能因为时间和时区的改变而改变
	 */
	private final static int STALE_WINDOW = 60 * 30;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "onReceive()");
		if (TaskService.TASK_KILLED.equals(intent.getAction())) {
			Log.v(TAG, "TaskService.TASK_KILLED");
			return;
		}

		//启动电池监控服务  
//		context.startService(new Intent(TaskReceiverService.instance.getApplicationContext(), MonitorService.class));
	    
		if (TaskService.TASK_ALERT_ACTION.equals(intent.getAction())) {
			Log.v(TAG, "TaskService.TASK_ALERT_ACTION");
			doTaskServiceAction(context, intent);
			return;
		} else {
			if (TaskService.POWER_SAVING_TASK_ACTION.equals(intent.getAction())) {
				Log.v(TAG, "TaskService.POWER_SAVING_TASK_ACTION");
				Intent setProfileOfPowerSaving = new Intent(TaskService.POWER_SAVING_TASK_ACTION);
				context.startService(setProfileOfPowerSaving);
				return;
			}
			
			if (TaskService.GPS_TASK_ACTION.equals(intent.getAction())) {
				Log.v(TAG, "TaskService.GPS_TASK_ACTION");
				doGPSServiceAction(context);
				return;
			}

			if (TaskService.WIFI_TASK_ACTION.equals(intent.getAction())
					|| WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())
					|| WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
				Log.v(TAG, "TaskService.WIFI_TASK_ACTION");
				if (context != null) {
					doWifiServiceAction(context);
				}
				return;
			}
		}

	}
	
	private void doTaskServiceAction(Context context, Intent intent){
		Task task = null;

		final byte[] data = intent
				.getByteArrayExtra(TaskService.TASK_RAW_DATA);

		if (data != null) {
			Parcel in = Parcel.obtain();
			in.unmarshall(data, 0, data.length);
			in.setDataPosition(0);
			task = Task.CREATOR.createFromParcel(in);
		}

		if (task == null) {
			Log.v(TAG,
					"TaskReceiver failed to parse the alarm from the intent");
			return;
		}

		Log.v(TAG, "task.getRemindTime() " + task.getRemindTime()
				+ " now " + System.currentTimeMillis());
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS aaa");
		Log.v(TAG, "TaskReceiver.onReceive() id " + task.getTaskId()
				+ " setFor " + sdf.format(new Date(task.getRemindTime())));

		// if (now > task.getRemindTime() + STALE_WINDOW * 1000) {
		// Log.v(TAG,"TaskReceiver ignoring stale alarm");
		// return;
		// }

		TaskReceiverService.task = task;
		if (context == null) {
			Intent intent2 = new Intent(TaskReceiverService.instance,
					InitActivity.class);
			intent2.putExtra(TaskService.TASK_INTENT_EXTRA, task);
			TaskReceiverService.instance.startActivity(intent2);
		} else {
			DBManager.openDB(context);
			// TODO
			TaskService.setNextAlert(context);
			if (TaskReceiverService.instance == null) {
				Intent setProfileOfGPS = new Intent(context, TaskReceiverService.class);
				setProfileOfGPS.setAction(TaskService.TASK_ALERT_ACTION);
				context.startService(setProfileOfGPS);
			}else {
				Intent setProfileOfTask = new Intent(TaskService.TASK_ALERT_ACTION);
				setProfileOfTask.putExtra(TaskService.TASK_INTENT_EXTRA, task);
				context.startService(setProfileOfTask);
			}
		}
	}
	
	
	
	private void doGPSServiceAction(Context context) {
		if (DataApplication.isGPSOpen) {
			if (GPSService.instance == null) {
				Intent setProfileOfGPS = new Intent(context, GPSService.class);
				setProfileOfGPS.setAction(TaskService.GPS_TASK_ACTION);
				context.startService(setProfileOfGPS);
			} else {
				if (LocationService.mLocationClient == null) {
					LocationService.initLocation(context, 10);
				}
				if (!LocationService.mLocationClient.isStarted()) {
					Log.v(TAG, "!isStarted()---------->isStarted()");
					// LocationService.startLocation(context,
					// DataApplication.isGPSOpen,
					// DataApplication.repeatTimeOfGPS);
					Intent setProfileOfGPS = new Intent(
							TaskService.GPS_TASK_ACTION);
					context.startService(setProfileOfGPS);
				} else {
					LocationService.mLocationClient.requestLocation();
				}
			}
		} else {
			if (LocationService.mLocationClient != null) {
				LocationService.mLocationClient.stop();
			}
			// TaskService.setNextAlert(context);
		}
	}
	
	private void doWifiServiceAction(Context context){
		Log.e(TAG, "正在检查网络状态");
	    if (DataApplication.isWIFIOpen) {
	    	//获得网络连接服务   
		    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
		    // State state = connManager.getActiveNetworkInfo().getState();   
		    // 获取WIFI网络连接状态  
		    int state = wifiManager.getWifiState();
		    Log.v(TAG+"-State.CONNECTED", ""+State.CONNECTED);
		    // 判断是否正在使用WIFI网络   
			if (WifiManager.WIFI_STATE_ENABLED == state
					|| WifiManager.WIFI_STATE_ENABLING == state
					|| WifiManager.WIFI_STATE_DISABLING == state) {
				DBManager.openDB(context);
				Intent setProfileOfWIFI = new Intent(TaskService.WIFI_TASK_ACTION);
				context.startService(setProfileOfWIFI);
			}
		}
	    
//		TaskService.setNextAlert(context);
		
	}

	/**
	 * 发送通知栏通知
	 * 
	 * @param context
	 * @param task
	 */
	private void sendNotification(Context context, Task task) {
		if (task.getTaskId() == 0) {
			Profile profile = ProfileService.getProfile(task);
			task.setProfileName(profile.getProfileName());
			task.setProfileId(profile.getProfileId());
		}
		Intent notify = new Intent(context, MainPad.class);
		notify.putExtra(TaskService.TASK_INTENT_EXTRA, task);
		PendingIntent pendingNotify = PendingIntent.getActivity(context,
				task.getProfileId(), null, 0);

		// 下面是设置通知栏通知的
		String label = task.getProfileName();
		Notification n = new Notification(R.drawable.ic_launcher, label,
				task.getRemindTime());
		n.setLatestEventInfo(context, label, task.getProfileName() + " 启用了",
				pendingNotify);
		n.flags |= Notification.FLAG_SHOW_LIGHTS
				| Notification.FLAG_ONGOING_EVENT;
		n.defaults |= Notification.DEFAULT_LIGHTS;

		// 用TaskId来发送通知，容易正确取消相应通知
		NotificationManager nm = getNotificationManager(context);
		nm.notify(task.getTaskId(), n);
	}

	/**
	 * 获得系统通知服务
	 * 
	 * @param context
	 * @return
	 */
	private NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

}
