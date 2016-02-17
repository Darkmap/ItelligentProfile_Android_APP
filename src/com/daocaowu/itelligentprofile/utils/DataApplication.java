package com.daocaowu.itelligentprofile.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.bean.WifiLocation;
import com.daocaowu.itelligentprofile.service.TaskReceiverService;
import com.daocaowu.itelligentprofile.utils.ManagerReceiver.ReceiverInterface;

public class DataApplication extends Application {

	private static int screenHeight = 0;// 屏幕高度
	private static int screenWidth = 0;// 屏幕宽度
	private static int screenStatusbar = 0;// 状态栏高度

	public static int x;
	public static int y;
	
	
	private ManagerReceiver mBroadReceiver;

	public static Context mContext;
	public static Task defaultTask;
	
	public static boolean isGPSOpen = false;
	public static long repeatTimeOfGPS = Long.MAX_VALUE; 
	
	public static boolean isWIFIOpen = false;
	public static long repeatTimeOfWIFI = Long.MAX_VALUE;
	
	public static boolean isPowerSaving = false;

	public static int lastProfileId = -1;
	
	/**
	 * 当前应用的Task的Id
	 */
	public static int currentTaskId = 0;

	/**
	 * 下一个要应用的Task的Id
	 */
	public static int nextTaskId = 0;

	// view0中使用的情景模式list
	public static List<Profile> profilelist = new ArrayList<Profile>();

	// 所有日程list
	public static List<Task> tasklist = new ArrayList<Task>();
	// view1-7中使用的日程list
	public static List<Task> mondaylist = new ArrayList<Task>();
	public static List<Task> tuesdaylist = new ArrayList<Task>();
	public static List<Task> wednesdaylist = new ArrayList<Task>();
	public static List<Task> thursdaylist = new ArrayList<Task>();
	public static List<Task> fridaylist = new ArrayList<Task>();
	public static List<Task> saturdaylist = new ArrayList<Task>();
	public static List<Task> sundaylist = new ArrayList<Task>();

	public static List<WifiLocation> wifilist = new ArrayList<WifiLocation>();
	
	public static List<Location> locationlist = new ArrayList<Location>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		DBManager.openDB(this);
		mBroadReceiver = new ManagerReceiver(new MyInterface());
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(ConstData.INTENT_EXIT);
		registerReceiver(mBroadReceiver, filter);
//		LocationService.initLocation(getContext(), 3);
	}

	private class MyInterface implements ReceiverInterface {

		@Override
		public void refresh(Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
				// 检查Service状态
				boolean isServiceRunning = false;
				ActivityManager manager = (ActivityManager) DataApplication
						.getContext()
						.getSystemService(Context.ACTIVITY_SERVICE);
				for (RunningServiceInfo service : manager
						.getRunningServices(Integer.MAX_VALUE)) {
					// Service的类名
					if ("com.daocaowu.itelligentprofile.service.TaskReceiverService"
							.equals(service.service.getClassName())) {
						isServiceRunning = true;
					}

				}
				if (!isServiceRunning) {
					Intent i = new Intent(getApplicationContext(),
							TaskReceiverService.class);
					DataApplication.getContext().startService(i);
				}
			}
		}
	}

	public static Context getContext() {
		return mContext;
	}

	/**
	 * get the screen height
	 */
	public static int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * get the screen width
	 */
	public static int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * get the status bar height
	 */
	public static int getScreenStatusBar() {
		return screenStatusbar;
	}

	/**
	 * get the screen height
	 */
	public static void setScreenHeight(int height) {
		screenHeight = height;
	}

	/**
	 * get the screen width
	 */
	public static void setScreenWidth(int width) {
		screenWidth = width;
	}

	/**
	 * get the status bar height
	 */
	public static void setScreenStatusBar(int statusbar) {
		screenStatusbar = statusbar;
	}
}
