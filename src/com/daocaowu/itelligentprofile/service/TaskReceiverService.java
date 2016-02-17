package com.daocaowu.itelligentprofile.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.ProfileColumns;
import com.daocaowu.itelligentprofile.activity.MainActivity;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.bean.WifiLocation;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.DateUtil;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;
import com.daocaowu.itelligentprofile.widget.ProfileWidgetProvider;

/**
 * 管理任务设置,作为一种后台服务.
 * @author CaoRen
 */
public class TaskReceiverService extends Service {

	private static final String TAG = TaskReceiverService.class.getSimpleName();

	private MyBinder mBinder;
	private BatteryChangedReceiver batteryChangedReceiver = null;

	public static Task task;
	public static TaskReceiverService instance = null;
	private WifiAdmin mWifiAdmin;
	public static List<WifiLocation> currentWLlist;
	WifiLocationService wifiLocationService;
    List<WifiLocation> dbWifiLocations;
//    public static int lastProfileId = -1;

	private static final int KILLER = 1000;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case KILLER:
				Log.v(TAG, "*********** Alarm killer triggered ***********");
				// sendKillBroadcast((Task) msg.obj);
				stopSelf();
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		if (mBinder == null) {
			mBinder = new MyBinder();
		}
		return mBinder;
	}

	@Override
	public void onCreate() {
		Log.v(TAG, "start onCreate~~~");
		super.onCreate();
		instance = this;
		if (task == null) {
			TaskService.initDefaultTask();
			task = DataApplication.defaultTask;
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "start onStart~~~");
		super.onStart(intent, startId);
		instance = this;
		// setTask(task);
		// sendNotification(TaskReceiverService.this, task);
		Log.v(TAG, "start task.startTime~~~" + task.getStartTime());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v(TAG, "start onStartCommand~~~");
		if (intent == null) {
			stopSelf();
			Log.v(TAG, "start onStartCommand~~~stopSelf()");
			return START_STICKY;
		}

		if (TaskService.TASK_ALERT_ACTION.equals(intent.getAction())) {
			Log.v(TAG, "TaskService.TASK_ALERT_ACTION");
			final Task receiveTask = intent
					.getParcelableExtra(TaskService.TASK_ALERT_ACTION);
			if (task == null) {
				task = receiveTask;
				Log.v(TAG, "task == null   task = receiveTask");
			}
			if (task == null) {
				TaskService.initDefaultTask();
				task = DataApplication.defaultTask;
				Log.v(TAG, "task == null   task = DataApplication.defaultTask");
			}
			if (task == null) {
				Log.v(TAG,
						"TaskReceiverService failed to parse the task from the intent");
				stopSelf();
				Log.v(TAG, "start onStartCommand~~~~task == null~~~stopSelf()");
				return START_STICKY;
			}
			DBManager.openDB(getApplicationContext());
			setTask(task);
			
			// TaskService.setNextAlert(TaskReceiverService.this);
			if (task != null) {
				Log.v(TAG, "start task.startTime~~~" + task.getStartTime());
			} else {
				Log.v(TAG, "start tas == null~~~");
			}
		} else if (TaskService.WIFI_TASK_ACTION.equals(intent.getAction())) {
			Log.v(TAG, "TaskService.WIFI_TASK_ACTION");
			return setWifiProfile();
		} else if (TaskService.POWER_SAVING_TASK_ACTION.equals(intent.getAction())) {
			Log.v(TAG, "TaskService.POWER_SAVING_TASK_ACTION");
			if (DataApplication.isPowerSaving) {
				Log.v("BatteryChangedReceiver", "isPowerSavingOpen :" + DataApplication.isPowerSaving);
				// 定义电池电量更新广播的过滤器,只接受带有ACTION_BATTERRY_CHANGED事件的Intent
				IntentFilter batteryChangedReceiverFilter = new IntentFilter();
				batteryChangedReceiverFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
				if (batteryChangedReceiver == null) {
					Log.v("BatteryChangedReceiver", "batteryChangedReceiver == null , atteryChangedReceiver() ");
					batteryChangedReceiver = new BatteryChangedReceiver();
				}
				// 向系统注册batteryChangedReceiver接收器，本接收器的实现见代码字段处
				registerReceiver(batteryChangedReceiver, batteryChangedReceiverFilter);
				/*// 由于初始化本服务时系统可能没有发出ACTION_BATTERY_CHANGED广播，那么刚才注册的那个接收器将不会在本服务启动时被激活，这种情况下就无法显示当前电量，因此在这里添加一个匿名广播接收器。
				new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						Log.v("BatteryChangedReceiver", "onReceive()");
						doPowerSavingAction(context, intent);
					}
					
					private void doPowerSavingAction(Context context, Intent intent){
						Log.v("BatteryChangedReceiver", "doPowerSavingAction()");
						int level = intent.getIntExtra("level", 0);
						int scale = intent.getIntExtra("scale", 100);
						int status = intent.getIntExtra("status", 0);

						// 若正在充电
						if (status == BatteryManager.BATTERY_STATUS_CHARGING){
							Log.v("BatteryChangedReceiver", "若正在充电,不执行省电模式");
						}else {
							Log.v("new BroadcastReceiver()", "若非正在充电状态,检查或并执行省电模式");
							setPowerSavingProfile(level * 100 / scale);
						}
					}
					
					private void setPowerSavingProfile(int batteryHealth){
						Log.v("BatteryChangedReceiver", "setPowerSavingProfile()");
						Profile profile = ProfileService.getPowerSavingProfile();
						if (profile == null) {
							Log.v("BatteryChangedReceiver", "profile == null");
							return;
						}
						if (lastProfileId != profile.getProfileId()) {
							Log.v("BatteryChangedReceiver", "lastProfileId != profile.getProfileId() :"+lastProfileId);
							if (batteryHealth <= profile.getPowerValue()) {
								TaskReceiverService.sendMsgToWidget(TaskReceiverService.instance.getApplicationContext(), profile, 4);
								SwitchProfileUtil.setProfile(TaskReceiverService.instance.getApplicationContext(), profile);
								sendNotificationByProfile(getApplicationContext(), profile, System.currentTimeMillis());
								lastProfileId = profile.getProfileId();
								Log.v("BatteryChangedReceiver", "SwitchProfileUtil.setProfile");
							}
						}
					}
				};*/
			}else {
				if (batteryChangedReceiver != null) {
					unregisterReceiver(batteryChangedReceiver);
					batteryChangedReceiver = null;
				}
			}
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "start onDestroy~~~");
		// super.onDestroy();
		setForeground(true);
		Intent localIntent = new Intent();
		localIntent.setClass(this, TaskReceiverService.class); // 销毁时重新启动Service
		this.startService(localIntent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.v(TAG, "start onUnbind~~~");
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder {
		public TaskReceiverService getService() {
			return TaskReceiverService.this;
		}
	}
	
	/**
	 * 接受电池信息更新的广播
	 */
	private class BatteryChangedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("BatteryChangedReceiver", "onReceive()");
			doPowerSavingAction(context, intent);
		}
		
		private void doPowerSavingAction(Context context, Intent intent){
			Log.v("BatteryChangedReceiver", "doPowerSavingAction()");
			int level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 100);
			int status = intent.getIntExtra("status", 0);

			// 若正在充电
			if (status == BatteryManager.BATTERY_STATUS_CHARGING){
				Log.v("BatteryChangedReceiver", "若正在充电,不执行省电模式");
//				setPowerSavingProfile(level * 100 / scale);
			}else {
				Log.v("BatteryChangedReceiver", "若非正在充电状态,检查或并执行省电模式");
				setPowerSavingProfile(level * 100 / scale);
			}
		}
		
		private void setPowerSavingProfile(int batteryHealth){
			Log.v("BatteryChangedReceiver", "batteryHealth： "+batteryHealth);
			Log.v("BatteryChangedReceiver", "setPowerSavingProfile()");
			Profile profile = ProfileService.getPowerSavingProfile();
			if (profile == null) {
				Log.v("BatteryChangedReceiver", "profile == null");
				return;
			}
			if (DataApplication.lastProfileId != profile.getProfileId()) {
				Log.v("BatteryChangedReceiver", "lastProfileId != profile.getProfileId() :"+DataApplication.lastProfileId);
				if (batteryHealth <= profile.getPowerValue()) {
					sendMsgToWidget(TaskReceiverService.instance.getApplicationContext(), profile, 4);
					SwitchProfileUtil.setProfile(TaskReceiverService.instance.getApplicationContext(), profile);
					sendNotificationByProfile(getApplicationContext(), profile, System.currentTimeMillis(), 3);
//					DataApplication.lastProfileId = profile.getProfileId();
					Log.v("BatteryChangedReceiver", "SwitchProfileUtil.setProfile");
				}
			}else {
				Log.v("BatteryChangedReceiver", "lastProfileId == profile.getProfileId() :"+DataApplication.lastProfileId);
			}
		}
	};
	
	/**
	 * 设置WIFILocation的Profile
	 */
	private int setWifiProfile(){
		if (mWifiAdmin == null) {
			mWifiAdmin = new WifiAdmin(TaskReceiverService.this);
		}
		DBManager.openDB(getApplicationContext());
	    currentWLlist = mWifiAdmin.getwifilocationlist();
	    wifiLocationService = new WifiLocationService(getApplicationContext());
	    dbWifiLocations = wifiLocationService.checkWithProfile();
		if (currentWLlist != null) {
			Log.v(TAG, "currentWLlist != null"+"  size() "+currentWLlist.size());
			if (dbWifiLocations != null) {
				int maxPriorityLevel = 0;
				int indexOfMax = -1; 
				Log.v(TAG, "dbWifiLocations != null"+"  size() "+dbWifiLocations.size());
				for (int i = 0; i < dbWifiLocations.size(); i++) {
					for (int j = 0; j < currentWLlist.size(); j++) {
						if (dbWifiLocations.get(i).getBssid().equals(currentWLlist.get(j).getBssid())) {
							Log.v(TAG, dbWifiLocations.get(i).getBssid()+"  equals  "+currentWLlist.get(j).getBssid());
							if (dbWifiLocations.get(i).getPriorityLevel() > maxPriorityLevel) {
								maxPriorityLevel = dbWifiLocations.get(i).getPriorityLevel();
								indexOfMax = i;
							}
						}
					}
				}
				Log.v(TAG, maxPriorityLevel + " ==== " + indexOfMax);
				
				if (indexOfMax > -1) {
					Log.v(TAG, "indexOfMax > -1");
//					WifiLocation wifiLocation = WifiLocationService
//							.check(dbWifiLocations.get(indexOfMax).getBssid());
					AbstractLocalService wifiLocalService = new WifiLocationService(getApplicationContext());
					WifiLocation wifiLocation = (WifiLocation) wifiLocalService.check(dbWifiLocations.get(indexOfMax).getWifiLocationId());
					
					if (wifiLocation != null) {
						Log.v(TAG, "wifiLocation != null");
						Log.v(TAG, "wifiLocation.getWifiLocationId()"+wifiLocation.getWifiLocationId() +"  wifiLocation.getProfileId()  "+"wifiLocation.getProfileId()");
						AbstractLocalService profileService = new ProfileService();
						Profile profile = (Profile) profileService.check(wifiLocation.getProfileId());
						if (profile != null) {
							Log.v(TAG, "profile != null");
							List<Task> tasks = TaskService.checkByProfileId(profile.getProfileId());
							if (tasks != null) {
								Log.v(TAG, "tasks != null");
								boolean isExist = false;
								for (Task task : tasks) {
									Log.v(TAG, ""+task.getDayofWeek());
									Log.v(TAG, ""+Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
									if (task.getDayofWeek() == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
										if (DateUtil.getmillisecond(task.getStartTime()) <= System.currentTimeMillis() && System.currentTimeMillis() <= DateUtil.getmillisecond(task.getEndTime())) {
											Log.v(TAG, ""+task.getDayofWeek());
											Log.v(TAG, ""+DateUtil.getmillisecond(task.getStartTime())+"<="+System.currentTimeMillis()+"<="+DateUtil.getmillisecond(task.getEndTime()));
											isExist = true;
										}
									}
									
								}
								if (!isExist) {
									Log.v(TAG, "!isExist");
									if (DataApplication.lastProfileId != profile.getProfileId()) {
										sendMsgToWidget(getApplicationContext(), profile, 3);
										SwitchProfileUtil.setProfile(getApplicationContext(), profile);
										sendNotificationByProfile(getApplicationContext(), profile, System.currentTimeMillis(), 2);
//										DataApplication.lastProfileId = profile.getProfileId();
										Log.v(TAG, "SwitchProfileUtil.setProfile");
									}
									
									return START_STICKY;
								}
							}else {
								Log.v(TAG, "tasks == null");
								if (DataApplication.lastProfileId != profile.getProfileId()) {
									sendMsgToWidget(getApplicationContext(), profile, 3);
									SwitchProfileUtil.setProfile(getApplicationContext(), profile);
									sendNotificationByProfile(getApplicationContext(), profile, System.currentTimeMillis(), 2);
//									DataApplication.lastProfileId = profile.getProfileId();
									Log.v(TAG, "SwitchProfileUtil.setProfile");
								}
								return START_STICKY;
							}
							
						}else {
							Log.v(TAG, "profile == null");
						}
					}else {
						Log.v(TAG, "wifiLocation == null");
					}
				}
			}
		}
		return START_NOT_STICKY;
	}

	/**
	 * 最终设置情景模式
	 * @param task
	 */
	private void setTask(Task task) {
		Profile profile = ProfileService.getProfile(task);
		sendMsgToWidget(getApplicationContext(), profile, 1);
		SwitchProfileUtil.setProfile(getApplicationContext(), profile);
		sendNotificationByTask(getApplicationContext(), task);
		Log.v(TAG, "setTask(Task task)~~id:" + task.getTaskId());
	}
	
	/**
	 * 更新桌面控件的当前设置的情景模式信息
	 * @param context
	 * @param profile
	 * @param enableType
	 */
	public static void sendMsgToWidget(Context context, Profile profile, int enableType){
		Intent intent = new Intent(ProfileWidgetProvider.UPDATE);
		switch (enableType) {
		case 0:
			intent.putExtra(ProfileWidgetProvider.ENABLE_TYPE, ProfileWidgetProvider.ENABLE_BY_USER);
			break;
		case 1:
			intent.putExtra(ProfileWidgetProvider.ENABLE_TYPE, ProfileWidgetProvider.ENABLE_BY_TASK);
			break;
		case 2:
			intent.putExtra(ProfileWidgetProvider.ENABLE_TYPE, ProfileWidgetProvider.ENABLE_BY_LOCATION);
			break;
		case 3:
			intent.putExtra(ProfileWidgetProvider.ENABLE_TYPE, ProfileWidgetProvider.ENABLE_BY_WIFI);
			break;
		case 4:
			intent.putExtra(ProfileWidgetProvider.ENABLE_TYPE, ProfileWidgetProvider.ENABLE_BY_POWER_SAVING);
			break;
		default:
			break;
		}
		intent.putExtra(ProfileColumns.ID, profile.getProfileId());
		intent.putExtra(ProfileColumns.NAME, profile.getProfileName());
		Date date = DateUtil.getLongDate(System.currentTimeMillis());
		if (date != null) {
			intent.putExtra(ProfileColumns.STARTTIME, date.toString());
		}
		context.sendBroadcast(intent);
	}
	
	/**
	 * 发送通知栏通知
	 * @param context
	 * @param task
	 */
	private void sendNotificationByTask(Context context, Task task) {
		if (task == null) {
			Log.v(TAG, "task == null");
			return;
		}
		Profile profile = ProfileService.getProfile(task);
		task.setProfileName(profile.getProfileName());
		task.setProfileId(profile.getProfileId());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS aaa");
		Log.v(TAG, "TaskReceiver.onReceive() id " + task.getTaskId()
				+ " setFor " + sdf.format(new Date(task.getRemindTime())));
		sendNotificationByProfile(context, profile, System.currentTimeMillis(), 0);
	}

	/**
	 * 发送通知栏通知
	 * @param context
	 * @param task
	 */
	private void sendNotificationByProfile(Context context, Profile profile, long remindTime, int type) {
		
		if (TaskReceiverService.this == null) {
			return;
		}
		Intent notify = new Intent(TaskReceiverService.this, MainActivity.class);
		notify.putExtra(TaskService.TASK_INTENT_EXTRA, task);
		PendingIntent pendingNotify = PendingIntent.getActivity(
				TaskReceiverService.this, task.getProfileId(), notify, 0);

		// 下面是设置通知栏通知的
		// String label = task.getProfileName();
		String detail = "来自【智能情景模式 】温馨提示";
		String label = "";
		switch (type) {
		case 0:
			label = "任务情景："+"【" + profile.getProfileName()+ "】" + "已经启用";
			break;
		case 1:
			label = "GPS情景："+"【" + profile.getProfileName()+ "】" + "已经启用";
			break;
		case 2:
			label = "WIFI情景："+"【" + profile.getProfileName()+ "】" + "已经启用";
			break;
		case 3:
			label = "省电情景："+"【" + profile.getProfileName()+ "】" + "已经启用";
			break;
		default:
			label = "【" + profile.getProfileName()+ "】" + "已经启用";
			break;
		}
		Notification n = new Notification(R.drawable.ic_launcher, label,
				remindTime);
		n.setLatestEventInfo(context, label, detail, pendingNotify);
		n.flags |= Notification.FLAG_AUTO_CANCEL; // 该通知能被状态栏的清除按钮给清除掉
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.defaults |= Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_VIBRATE;

		// 用TaskId来发送通知，容易正确取消相应通知
		NotificationManager nm = getNotificationManager(context);
		nm.notify(0, n);
	}

	/**
	 * 获得系统通知服务
	 * @param context
	 * @return
	 */
	private NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	class WifiAdmin {
		private final static String TAG = "WifiAdmin";
		private StringBuffer mStringBuffer = new StringBuffer();
		private List<ScanResult> listResult;
		private ScanResult mScanResult;
		// 定义WifiManager对象
		private WifiManager mWifiManager;
		// 定义WifiInfo对象
		private WifiInfo mWifiInfo;
		// 网络连接列表
		private List<WifiConfiguration> mWifiConfiguration;
		// 定义一个WifiLock
		WifiLock mWifiLock;

		/**
		 * 构造方法
		 */
		public WifiAdmin(Context context) {
			mWifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			mWifiInfo = mWifiManager.getConnectionInfo();
		}

		/**
		 * 打开Wifi网卡
		 */
		public void openNetCard() {
			if (!mWifiManager.isWifiEnabled()) {
				mWifiManager.setWifiEnabled(true);
			}
		}

		/**
		 * 关闭Wifi网卡
		 */
		public void closeNetCard() {
			if (mWifiManager.isWifiEnabled()) {
				mWifiManager.setWifiEnabled(false);
			}
		}

		/**
		 * 检查当前Wifi网卡状态
		 */
		public void checkNetCardState() {
			if (mWifiManager.getWifiState() == 0) {
				Log.i(TAG, "网卡正在关闭");
			} else if (mWifiManager.getWifiState() == 1) {
				Log.i(TAG, "网卡已经关闭");
			} else if (mWifiManager.getWifiState() == 2) {
				Log.i(TAG, "网卡正在打开");
			} else if (mWifiManager.getWifiState() == 3) {
				Log.i(TAG, "网卡已经打开");
			} else {
				Log.i(TAG, "---_---晕......没有获取到状态---_---");
			}
		}

		/**
		 * 扫描周边网络
		 */
		public void scan() {
			mWifiManager.startScan();
			listResult = mWifiManager.getScanResults();
			if (listResult != null) {
				Log.i(TAG, "当前区域存在无线网络，请查看扫描结果");
			} else {
				Log.i(TAG, "当前区域没有无线网络");
			}
		}

		/**
		 * 扫描获得WifiLocation列表
		 * 
		 * @return
		 */
		public List<WifiLocation> getwifilocationlist() {

			List<WifiLocation> list = new ArrayList<WifiLocation>();

			Time t = new Time();
			t.setToNow(); // 取得系统时间。
			String timeNow = t.year + "." + t.month + "." + t.monthDay + "."
					+ t.hour + ":" + t.minute + ":" + t.second;

			// 每次点击扫描之前清空上一次的扫描结果
			if (mStringBuffer != null) {
				mStringBuffer = new StringBuffer();
			}
			// 开始扫描网络
			scan();
			// listResult = mWifiManager.getScanResults();
			if (listResult != null) {
				for (int i = 0; i < listResult.size(); i++) {
					mScanResult = listResult.get(i);
					WifiLocation temp = new WifiLocation();
					temp.setSsid(mScanResult.SSID);
					temp.setBssid(mScanResult.BSSID);
					list.add(temp);
				}
			}
			return list;
		}

		/**
		 * 得到扫描结果
		 */
		public String getScanResult() {

			Time t = new Time();
			t.setToNow(); // 取得系统时间。
			String timeNow = t.year + "." + t.month + "." + t.monthDay + "."
					+ t.hour + ":" + t.minute + ":" + t.second;

			// 每次点击扫描之前清空上一次的扫描结果
			if (mStringBuffer != null) {
				mStringBuffer = new StringBuffer();
			}
			// 开始扫描网络
			scan();
			listResult = mWifiManager.getScanResults();
			if (listResult != null) {
				for (int i = 0; i < listResult.size(); i++) {
					mScanResult = listResult.get(i);
					mStringBuffer = mStringBuffer.append("NO.").append(i + 1)
							.append(" :").append(mScanResult.SSID).append("->")
							.append(mScanResult.BSSID).append("->")
							.append(mScanResult.capabilities).append("->")
							.append(mScanResult.frequency).append("->")
							.append(mScanResult.level).append("->")
							.append(mScanResult.describeContents())
							.append("->").append(timeNow).append("\n\n");
				}
			}
			Log.i(TAG, mStringBuffer.toString());
			return mStringBuffer.toString();
		}

		/**
		 * 连接指定网络
		 */
		public void connect() {
			mWifiInfo = mWifiManager.getConnectionInfo();

		}

		/**
		 * 断开当前连接的网络
		 */
		public void disconnectWifi() {
			int netId = getNetworkId();
			mWifiManager.disableNetwork(netId);
			mWifiManager.disconnect();
			mWifiInfo = null;
		}

		/**
		 * 检查当前网络状态
		 * 
		 * @return String
		 */
		public void checkNetWorkState() {
			if (mWifiInfo != null) {
				Log.i(TAG, "网络正常工作");
			} else {
				Log.i(TAG, "网络已断开");
			}
		}

		/**
		 * 得到连接的ID
		 */
		public int getNetworkId() {
			return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
		}

		/**
		 * 得到IP地址
		 */
		public int getIPAddress() {
			return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
		}

		// 锁定WifiLock
		public void acquireWifiLock() {
			mWifiLock.acquire();
		}

		// 解锁WifiLock
		public void releaseWifiLock() {
			// 判断时候锁定
			if (mWifiLock.isHeld()) {
				mWifiLock.acquire();
			}
		}

		// 创建一个WifiLock
		public void creatWifiLock() {
			mWifiLock = mWifiManager.createWifiLock("Test");
		}

		// 得到配置好的网络
		public List<WifiConfiguration> getConfiguration() {
			return mWifiConfiguration;
		}

		// 指定配置好的网络进行连接
		public void connectConfiguration(int index) {
			// 索引大于配置好的网络索引返回
			if (index >= mWifiConfiguration.size()) {
				return;
			}
			// 连接配置好的指定ID的网络
			mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
					true);
		}

		// 得到MAC地址
		public String getMacAddress() {
			return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
		}

		// 得到接入点的BSSID
		public String getBSSID() {
			return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
		}

		// 得到WifiInfo的所有信息包
		public String getWifiInfo() {
			return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
		}

		// 添加一个网络并连接
		public int addNetwork(WifiConfiguration wcg) {
			int wcgID = mWifiManager.addNetwork(mWifiConfiguration.get(3));
			mWifiManager.enableNetwork(wcgID, true);
			return wcgID;
		}
	}
}