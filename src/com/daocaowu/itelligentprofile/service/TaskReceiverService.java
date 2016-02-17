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
 * ������������,��Ϊһ�ֺ�̨����.
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
				// �����ص������¹㲥�Ĺ�����,ֻ���ܴ���ACTION_BATTERRY_CHANGED�¼���Intent
				IntentFilter batteryChangedReceiverFilter = new IntentFilter();
				batteryChangedReceiverFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
				if (batteryChangedReceiver == null) {
					Log.v("BatteryChangedReceiver", "batteryChangedReceiver == null , atteryChangedReceiver() ");
					batteryChangedReceiver = new BatteryChangedReceiver();
				}
				// ��ϵͳע��batteryChangedReceiver������������������ʵ�ּ������ֶδ�
				registerReceiver(batteryChangedReceiver, batteryChangedReceiverFilter);
				/*// ���ڳ�ʼ��������ʱϵͳ����û�з���ACTION_BATTERY_CHANGED�㲥����ô�ղ�ע����Ǹ��������������ڱ���������ʱ�������������¾��޷���ʾ��ǰ������������������һ�������㲥��������
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

						// �����ڳ��
						if (status == BatteryManager.BATTERY_STATUS_CHARGING){
							Log.v("BatteryChangedReceiver", "�����ڳ��,��ִ��ʡ��ģʽ");
						}else {
							Log.v("new BroadcastReceiver()", "�������ڳ��״̬,����ִ��ʡ��ģʽ");
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
		localIntent.setClass(this, TaskReceiverService.class); // ����ʱ��������Service
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
	 * ���ܵ����Ϣ���µĹ㲥
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

			// �����ڳ��
			if (status == BatteryManager.BATTERY_STATUS_CHARGING){
				Log.v("BatteryChangedReceiver", "�����ڳ��,��ִ��ʡ��ģʽ");
//				setPowerSavingProfile(level * 100 / scale);
			}else {
				Log.v("BatteryChangedReceiver", "�������ڳ��״̬,����ִ��ʡ��ģʽ");
				setPowerSavingProfile(level * 100 / scale);
			}
		}
		
		private void setPowerSavingProfile(int batteryHealth){
			Log.v("BatteryChangedReceiver", "batteryHealth�� "+batteryHealth);
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
	 * ����WIFILocation��Profile
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
	 * ���������龰ģʽ
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
	 * ��������ؼ��ĵ�ǰ���õ��龰ģʽ��Ϣ
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
	 * ����֪ͨ��֪ͨ
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
	 * ����֪ͨ��֪ͨ
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

		// ����������֪ͨ��֪ͨ��
		// String label = task.getProfileName();
		String detail = "���ԡ������龰ģʽ ����ܰ��ʾ";
		String label = "";
		switch (type) {
		case 0:
			label = "�����龰��"+"��" + profile.getProfileName()+ "��" + "�Ѿ�����";
			break;
		case 1:
			label = "GPS�龰��"+"��" + profile.getProfileName()+ "��" + "�Ѿ�����";
			break;
		case 2:
			label = "WIFI�龰��"+"��" + profile.getProfileName()+ "��" + "�Ѿ�����";
			break;
		case 3:
			label = "ʡ���龰��"+"��" + profile.getProfileName()+ "��" + "�Ѿ�����";
			break;
		default:
			label = "��" + profile.getProfileName()+ "��" + "�Ѿ�����";
			break;
		}
		Notification n = new Notification(R.drawable.ic_launcher, label,
				remindTime);
		n.setLatestEventInfo(context, label, detail, pendingNotify);
		n.flags |= Notification.FLAG_AUTO_CANCEL; // ��֪ͨ�ܱ�״̬���������ť�������
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.defaults |= Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_VIBRATE;

		// ��TaskId������֪ͨ��������ȷȡ����Ӧ֪ͨ
		NotificationManager nm = getNotificationManager(context);
		nm.notify(0, n);
	}

	/**
	 * ���ϵͳ֪ͨ����
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
		// ����WifiManager����
		private WifiManager mWifiManager;
		// ����WifiInfo����
		private WifiInfo mWifiInfo;
		// ���������б�
		private List<WifiConfiguration> mWifiConfiguration;
		// ����һ��WifiLock
		WifiLock mWifiLock;

		/**
		 * ���췽��
		 */
		public WifiAdmin(Context context) {
			mWifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			mWifiInfo = mWifiManager.getConnectionInfo();
		}

		/**
		 * ��Wifi����
		 */
		public void openNetCard() {
			if (!mWifiManager.isWifiEnabled()) {
				mWifiManager.setWifiEnabled(true);
			}
		}

		/**
		 * �ر�Wifi����
		 */
		public void closeNetCard() {
			if (mWifiManager.isWifiEnabled()) {
				mWifiManager.setWifiEnabled(false);
			}
		}

		/**
		 * ��鵱ǰWifi����״̬
		 */
		public void checkNetCardState() {
			if (mWifiManager.getWifiState() == 0) {
				Log.i(TAG, "�������ڹر�");
			} else if (mWifiManager.getWifiState() == 1) {
				Log.i(TAG, "�����Ѿ��ر�");
			} else if (mWifiManager.getWifiState() == 2) {
				Log.i(TAG, "�������ڴ�");
			} else if (mWifiManager.getWifiState() == 3) {
				Log.i(TAG, "�����Ѿ���");
			} else {
				Log.i(TAG, "---_---��......û�л�ȡ��״̬---_---");
			}
		}

		/**
		 * ɨ���ܱ�����
		 */
		public void scan() {
			mWifiManager.startScan();
			listResult = mWifiManager.getScanResults();
			if (listResult != null) {
				Log.i(TAG, "��ǰ��������������磬��鿴ɨ����");
			} else {
				Log.i(TAG, "��ǰ����û����������");
			}
		}

		/**
		 * ɨ����WifiLocation�б�
		 * 
		 * @return
		 */
		public List<WifiLocation> getwifilocationlist() {

			List<WifiLocation> list = new ArrayList<WifiLocation>();

			Time t = new Time();
			t.setToNow(); // ȡ��ϵͳʱ�䡣
			String timeNow = t.year + "." + t.month + "." + t.monthDay + "."
					+ t.hour + ":" + t.minute + ":" + t.second;

			// ÿ�ε��ɨ��֮ǰ�����һ�ε�ɨ����
			if (mStringBuffer != null) {
				mStringBuffer = new StringBuffer();
			}
			// ��ʼɨ������
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
		 * �õ�ɨ����
		 */
		public String getScanResult() {

			Time t = new Time();
			t.setToNow(); // ȡ��ϵͳʱ�䡣
			String timeNow = t.year + "." + t.month + "." + t.monthDay + "."
					+ t.hour + ":" + t.minute + ":" + t.second;

			// ÿ�ε��ɨ��֮ǰ�����һ�ε�ɨ����
			if (mStringBuffer != null) {
				mStringBuffer = new StringBuffer();
			}
			// ��ʼɨ������
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
		 * ����ָ������
		 */
		public void connect() {
			mWifiInfo = mWifiManager.getConnectionInfo();

		}

		/**
		 * �Ͽ���ǰ���ӵ�����
		 */
		public void disconnectWifi() {
			int netId = getNetworkId();
			mWifiManager.disableNetwork(netId);
			mWifiManager.disconnect();
			mWifiInfo = null;
		}

		/**
		 * ��鵱ǰ����״̬
		 * 
		 * @return String
		 */
		public void checkNetWorkState() {
			if (mWifiInfo != null) {
				Log.i(TAG, "������������");
			} else {
				Log.i(TAG, "�����ѶϿ�");
			}
		}

		/**
		 * �õ����ӵ�ID
		 */
		public int getNetworkId() {
			return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
		}

		/**
		 * �õ�IP��ַ
		 */
		public int getIPAddress() {
			return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
		}

		// ����WifiLock
		public void acquireWifiLock() {
			mWifiLock.acquire();
		}

		// ����WifiLock
		public void releaseWifiLock() {
			// �ж�ʱ������
			if (mWifiLock.isHeld()) {
				mWifiLock.acquire();
			}
		}

		// ����һ��WifiLock
		public void creatWifiLock() {
			mWifiLock = mWifiManager.createWifiLock("Test");
		}

		// �õ����úõ�����
		public List<WifiConfiguration> getConfiguration() {
			return mWifiConfiguration;
		}

		// ָ�����úõ������������
		public void connectConfiguration(int index) {
			// �����������úõ�������������
			if (index >= mWifiConfiguration.size()) {
				return;
			}
			// �������úõ�ָ��ID������
			mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
					true);
		}

		// �õ�MAC��ַ
		public String getMacAddress() {
			return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
		}

		// �õ�������BSSID
		public String getBSSID() {
			return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
		}

		// �õ�WifiInfo��������Ϣ��
		public String getWifiInfo() {
			return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
		}

		// ���һ�����粢����
		public int addNetwork(WifiConfiguration wcg) {
			int wcgID = mWifiManager.addNetwork(mWifiConfiguration.get(3));
			mWifiManager.enableNetwork(wcgID, true);
			return wcgID;
		}
	}
}