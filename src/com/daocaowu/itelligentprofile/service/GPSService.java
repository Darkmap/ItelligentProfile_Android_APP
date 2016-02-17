package com.daocaowu.itelligentprofile.service;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.LocationData;
import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.activity.MainActivity;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.DateUtil;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;

public class GPSService extends Service {

	// 错误标记
	private static String TAG = "GPSService";
	private final IBinder mBinder = new GPSServiceBinder();
	public static GPSService instance;
	AbstractLocalService gpsLocationService = new LocationService();
	public static List<Location> dbLocations;
	public static MyLocationListenner myListener = null;
//	private int lastProfileId = -1;

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "GPSService onCreate.");
		instance = this;
		LocationService.initLocation(this, 3);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.v(TAG, "GPSService onStart.");
		instance = this;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return START_NOT_STICKY;
		}
		if (TaskService.GPS_TASK_ACTION.equals(intent.getAction())) {
			Log.v(TAG, "TaskService.GPS_TASK_ACTION");
			return setGPSProfile();
		}
		return START_STICKY;
	}

	private int setGPSProfile() {
		if (LocationService.mLocationClient == null) {
			LocationService.initLocation(getApplicationContext(), 3);
		}
		if (!LocationService.mLocationClient.isStarted()) {
			LocationService.mLocationClient.start();
			Log.v(TAG, "!isStarted()---------->isStarted()");
		}
		LocationService.mLocationClient.requestLocation();
//		LocationData locationData = LocationService.getLocationData();
		
		return START_NOT_STICKY;
	}
	
	
	public static int doGPSAction(LocationData locationData){
		if (locationData != null) {
			Log.v(TAG, "LocationService.getLocationData() != null");
			DBManager.openDB(GPSService.instance.getApplicationContext());
			dbLocations = LocationService.getLocationsWithProfile();
			if (dbLocations != null) {
				Log.v(TAG, "dbLocations != null");
				Log.v(TAG, "dbLocations size():" + dbLocations.size());
				Location profileLocation = getClosestLocation(dbLocations);
				if (profileLocation != null) {
					AbstractLocalService profileService = new ProfileService();
					Profile profile = (Profile) profileService
							.check(profileLocation.getProfileId());
					if (profile != null) {
						Log.v(TAG, "profile != null");
						List<Task> tasks = TaskService.checkByProfileId(profile
								.getProfileId());
						if (tasks != null) {
							Log.v(TAG, "tasks != null");
							boolean isExist = false;
							for (Task task : tasks) {
								if (DateUtil
										.getmillisecond(task.getStartTime()) <= System
										.currentTimeMillis()
										&& DateUtil.getmillisecond(task
												.getEndTime()) >= System
												.currentTimeMillis()) {
									isExist = true;
								}
							}
							if (!isExist) {
								Log.v(TAG, "!isExist");
								Log.v(TAG, "DataApplication.lastProfileId :"+DataApplication.lastProfileId);
								Log.v(TAG, "profile.getProfileId() :"+profile.getProfileId());
								if (DataApplication.lastProfileId != profile.getProfileId()) {
									Log.v(TAG, "DataApplication.lastProfileId != profile.getProfileId()");
									TaskReceiverService.sendMsgToWidget(GPSService.instance.getApplicationContext(), profile, 2);
									SwitchProfileUtil.setProfile(
											GPSService.instance.getApplicationContext(), profile);
									sendNotificationByProfile(
											GPSService.instance.getApplicationContext(), profile,
											System.currentTimeMillis());
//									DataApplication.lastProfileId = profile.getProfileId();
									Log.v(TAG, "SwitchProfileUtil.setProfile");
								}else {
									Log.v(TAG, "DataApplication.lastProfileId == profile.getProfileId()");
								}

								return START_STICKY;
							}
						} else {
							Log.v(TAG, "tasks == null");
							Log.v(TAG, "isExist");
							Log.v(TAG, "DataApplication.lastProfileId :"+DataApplication.lastProfileId);
							Log.v(TAG, "profile.getProfileId() :"+profile.getProfileId());
							if (DataApplication.lastProfileId != profile.getProfileId()) {
								Log.v(TAG, "DataApplication.lastProfileId != profile.getProfileId()");
								TaskReceiverService.sendMsgToWidget(GPSService.instance.getApplicationContext(), profile, 2);
								SwitchProfileUtil.setProfile(
										GPSService.instance.getApplicationContext(), profile);
								sendNotificationByProfile(
										GPSService.instance.getApplicationContext(), profile,
										System.currentTimeMillis());
//								DataApplication.lastProfileId = profile.getProfileId();
								Log.v(TAG, "SwitchProfileUtil.setProfile");
							}else {
								Log.v(TAG, "DataApplication.lastProfileId == profile.getProfileId()");
							}
							return START_STICKY;
						}

					} else {
						Log.v(TAG, "profile == null");
					}
				}
			}

		}
		return START_NOT_STICKY;
	}

	private static Location getClosestLocation(List<Location> locations) {
		int indexOfMax = -1;
		double maxDouble = Double.MAX_VALUE;
		if (LocationService.getLocationData() == null) {
			Log.v(TAG, "getLocationData() == null");
			LocationService.mLocationClient.requestLocation();
		}
		for (int i = 0; i < locations.size(); i++) {
			double distance = GetDistance(locations.get(i).getLatitude(),
					locations.get(i).getLongitude(),
					LocationService.mLocationData.latitude,
					LocationService.mLocationData.longitude);
			if (distance < maxDouble && distance < locations.get(i).getRadius()) {
				Log.v(TAG,"distance:"+distance+ "---getRadius():"+locations.get(i).getRadius());
				indexOfMax = i;
				maxDouble = distance;
			}
		}
		if (indexOfMax == -1) {
			Log.v(TAG, "indexOfMax == -1");
			return null;
		}else {
			Log.v(TAG, "indexOfMax == "+indexOfMax);
		}
		return locations.get(indexOfMax);
	}

	/********************* 求两个坐标的距离 ****************************/
	private final static double EARTH_RADIUS = 6378137;//验算过了  这个是正确的
//	private final static double EARTH_RADIUS = 6378.137;
	//TODO 这里好像有问题  好像地球半径的影响很小
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		/**
		 * 百度地图的方法
		 */
//		GeoPoint point1 = new GeoPoint((int)(lat1*1E6), (int)(lng1*1E6));
//		GeoPoint point2 = new GeoPoint((int)(lat2*1E6), (int)(lng2*1E6));
//		DistanceUtil mDistanceUtil = new DistanceUtil();
//		return mDistanceUtil.getDistance(point1, point2);
		/**
		 * 下面的是自己的方法（贺智超的方法） 精确度更高
		 */
		double dlong = rad(lng2) - rad(lng1);
		double dlat = rad(lat2) - rad(lat1);
		double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(rad(lat1))
				* Math.cos(rad(lat1)) * Math.pow(Math.sin(dlong / 2D), 2D);
		double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
		double d = EARTH_RADIUS * c;
		Log.v("半径 啊", d + "");
	    return d;
		/**
		 * 下面的是自己的方法（程家颖的方法）
		 */
//		double radLat1 = rad(lat1);
//		double radLat2 = rad(lat2);
//		double a = radLat1 - radLat2;
//		double b = rad(lng1) - rad(lng2);
//		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
//				+ Math.cos(radLat1) * Math.cos(radLat2)
//				* Math.pow(Math.sin(b / 2), 2)));
//		s = s * EARTH_RADIUS;
//		s = Math.round(s * 10000) / 10000;
//		return s;
	}

	/*********************** 求两个坐标的距离 **************************/

	
	
	
	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public static class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			LocationService.mLocationData.latitude = location.getLatitude();
			LocationService.mLocationData.longitude = location.getLongitude();
			LocationService.mLocationData.accuracy = location.getRadius();
			LocationService.mLocationData.direction = location.getDerect();
			
			doGPSAction(LocationService.mLocationData);
			
			//以下一下是TAG
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
			sb.append("\nderect : ");
			sb.append(location.getDerect());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			Log.v(TAG, sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
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
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			Log.v(TAG, sb.toString());
		}
		
	}
	
	
	
	
	/**
	 * 发送通知栏通知
	 * 
	 * @param context
	 * @param task
	 */
	private static void sendNotificationByProfile(Context context, Profile profile,
			long remindTime) {

		if (GPSService.instance == null) {
			return;
		}
		Intent notify = new Intent(GPSService.instance, MainActivity.class);
		PendingIntent pendingNotify = PendingIntent.getActivity(
				GPSService.instance, profile.getProfileId(), notify, 0);

		// 下面是设置通知栏通知的
		// String label = task.getProfileName();
		String detail = "来自【智能情景模式 】温馨提示";
		String label = "【" + profile.getProfileName() + "】" + "已经启用";
		Notification n = new Notification(R.drawable.ic_launcher, label,
				remindTime);
		n.setLatestEventInfo(context, label, detail, pendingNotify);
		n.flags |= Notification.FLAG_AUTO_CANCEL; // 该通知能被状态栏的清除按钮给清除掉
		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.defaults |= Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_VIBRATE;

		// 用0来发送通知，容易正确取消相应通知
		NotificationManager nm = getNotificationManager(GPSService.instance.getApplicationContext());
		nm.notify(0, n);
	}

	/**
	 * 获得系统通知服务
	 * 
	 * @param context
	 * @return
	 */
	private static NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "GPSService Ended.");
		LocationService.mLocationClient.stop();
	}

	public class GPSServiceBinder extends Binder {
		GPSService getService() {
			return GPSService.this;
		}
	}

}
