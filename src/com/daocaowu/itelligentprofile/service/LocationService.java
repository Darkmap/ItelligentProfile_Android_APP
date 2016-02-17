package com.daocaowu.itelligentprofile.service;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TYPE;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.LocationColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.ProfileColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.TaskColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.WifiLocationColumns;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;

public class LocationService extends AbstractLocalService {

	private static final String TAG = "LocationService";
	
	@Override
	public int add(Object object) {
//		if (((Location)object).getProfileId() > 0) {
//			addAlarmLoction((Location)object);
//		}
		return DBManager.insertLocationIntoDB((Location)object);
	}

	@Override
	public void delete(int objectId) {
		Location location = (Location) check(objectId);
		NotifyLister mNotifyer = new NotifyLister();
		mNotifyer.setLocation(location);
		mNotifyer.SetNotifyLocation(location.getLatitude(),location.getLongitude(),(float)location.getRadius(),"bd09ll");
		mLocationClient.removeNotifyEvent(mNotifyer);
		DBManager.delete(TYPE.LOCATION, LocationColumns.ID+"=?", new String[]{String.valueOf(objectId)});

	}

	@Override
	public Object check(int objectId) {
		return DBManager.getLocation(TYPE.LOCATION, null, LocationColumns.ID+"=?", new String[]{String.valueOf(objectId)});
		
	}
	
	public static Location check(double latitude, double longitude, float distance){
		return DBManager.getLocation(TYPE.LOCATION, null, LocationColumns.LATITUDE+"="+latitude+" AND "+LocationColumns.LONGITUDE+"="+longitude+" AND "+LocationColumns.RADIUS+">="+distance, null);
	}
	
	public static List<Location> getLocationsWithProfile(){
		return DBManager.getLocations(TYPE.LOCATION, null, LocationColumns.PROFILE_ID+">0", null);
	}
	
	public static List<Location> getLocationsWithoutProfile(){
		return DBManager.getLocations(TYPE.LOCATION, null, LocationColumns.PROFILE_ID+"<=0", null);
	}

	@Override
	public List check() {
		return DBManager.getLocations(TYPE.LOCATION, null, null, null);
		
	}

	@Override
	public void update(Object object) {
		DBManager.insertLocationIntoDB((Location) object);
	}
	
	/******************************************定位相关*****************************************************/ 
	public static LocationClient mLocationClient;
//	public static MyLocationListenner myListener = null;
	public static LocationData mLocationData = null;
	/**
	 * 初始化定位SDK必需字段
	 * @param context
	 */
	public static void initLocation(Context context, int timeInSecend){
		mLocationClient = new LocationClient(context);
		GPSService.myListener = new GPSService.MyLocationListenner();
		mLocationData = new LocationData();
		mLocationClient.registerLocationListener(GPSService.myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(timeInSecend);//设置定时定位的时间间隔。单位ms （小于1000时只运行一次）
		option.disableCache(true);//禁止使用缓存定位
		mLocationClient.setLocOption(option);
	}
	
	/**
	 * 获得将要设置的Profile
	 * @param profileId
	 * @return
	 */
	public static Profile getProfile(int profileId){
		AbstractLocalService  profileService = new ProfileService();
		Profile profile = (Profile) profileService.check(profileId);
		if (profile == null) {
			profile = ProfileService.getDefaultProfile();
		}
		return profile;
	}
	
	
	/**
	 * 开始定位
	 * @param context
	 * @param isOpen
	 * @param repeatTimeMillis
	 */
	public static void startLocation(Context context, boolean isOpen, long repeatTimeMillis){
		if (isOpen) {
			enableLocation(context, repeatTimeMillis);
		}else {
			disableLocation(context);
		}
	}
	
	/**
	 * 启用GPS定位
	 */
	public static void enableLocation(Context context, long repeatTimeMillis){
//		mLocationClient.start();
//		mLocationClient.requestLocation();
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// 设置Action
		Log.v(TAG, "enableLocation()");
		Intent intent = new Intent(TaskService.GPS_TASK_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTimeMillis, sender);
	}
	
	/**
	 * 关闭GPS定位
	 * @param context
	 */
	public static void disableLocation(Context context){
		mLocationClient.stop();
		DataApplication.lastProfileId = -1;
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TaskService.GPS_TASK_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
	
	public static LocationData getLocationData(){
		return mLocationData;
	}
	
	/**
	 * 从数据库中读取有情景模式的GPS定位信息
	 * @param context
	 * @return
	 */
	public static List<Location> checkLocationWithProfile(Context context){
		return DBManager.getLocations(TYPE.LOCATION, null, LocationColumns.PROFILE_ID+"!=0", null);
	}
	
	/**
	 * 在数据库中查找符合当前位置的坐标点
	 * @return
	 */
	public static int checkLocationByDB(Context context, Location location){
//		String IS_EXIST_SQL = LocationColumns.LATITUDE+"="+location.getLatitude()+LocationColumns.LONGITUDE+LocationColumns.RADIUS;
//		return DBManager.query(TYPE.LOCATION, null, null, null, null);
		return -1;
	}
	
	public static void addAlarmLoction(Location location){
		if (location != null) {
			//位置提醒相关代码
			NotifyLister mNotifyer = new NotifyLister();
			mNotifyer.setLocation(location);
			mNotifyer.SetNotifyLocation(location.getLatitude(),location.getLongitude(),(float)location.getRadius(),"bd09ll");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
			mLocationClient.registerNotify(mNotifyer);
			//注册位置提醒监听事件后，可以通过SetNotifyLocation 来修改位置提醒设置，修改后立刻生效。
		}
	}
	
	/**
	 * 位置提醒相关代码
	 * @param locations
	 */
	public static void setAlarmLocation(List<Location> locations){//TODO
		if (locations != null) {
			for (Location location : locations) {
				//位置提醒相关代码
				NotifyLister mNotifyer = new NotifyLister();
				mNotifyer.setLocation(location);
				mNotifyer.SetNotifyLocation(location.getLatitude(),location.getLongitude(),(float)location.getRadius(),"bd09ll");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
				mLocationClient.registerNotify(mNotifyer);
				//注册位置提醒监听事件后，可以通过SetNotifyLocation 来修改位置提醒设置，修改后立刻生效。
			}
		}
	}
	
	//BDNotifyListner实现
		public static class NotifyLister extends BDNotifyListener{
			private NotifyLister notifyLister;
			private Location mLocation;

			public void setNotifyLister(NotifyLister notifyLister) {
				this.notifyLister = notifyLister;
			}
			
			public void setLocation(Location location) {
				this.mLocation = location;
			}
			int step = 0;
			
		    @Override
			public void onNotify(BDLocation mlocation, float distance){
//			mVibrator01.vibrate(1000);//振动提醒已到设定位置附近
		    	Log.v(TAG, mlocation.getLatitude()+" -- "+mlocation.getLongitude()+" -- "+mlocation.getRadius()+" -- "+distance);
		    	
		    	if (distance <= this.mLocation.getRadius()) {//用户输入的半径
		    		step++;
		    		AbstractLocalService locationService  = new LocationService();
		    		if (this.mLocation.getLocationId() != 0) {
		    			
						Location tempLocation = (Location) locationService.check(this.mLocation.getLocationId());
						if (tempLocation.getProfileId() != 0) {
							AbstractLocalService profileService = new ProfileService();
							Profile profile = (Profile) profileService.check(tempLocation.getProfileId());
							if (profile != null) {
								if (profile.getProfileId() == 0) {
									SwitchProfileUtil.setProfile(GPSService.instance.getApplicationContext(), ProfileService.getDefaultProfile());
								}else {
									SwitchProfileUtil.setProfile(GPSService.instance.getApplicationContext(), profile);
								}
							}
						}
					}else {
						Location tempLocation = LocationService.check(mlocation.getLatitude(), mLocation.getLongitude(), distance);
						if (tempLocation.getProfileId() != 0) {
							AbstractLocalService profileService = new ProfileService();
							Profile profile = (Profile) profileService.check(tempLocation.getProfileId());
							if (profile != null) {
								if (profile.getProfileId() == 0) {
									SwitchProfileUtil.setProfile(GPSService.instance.getApplicationContext(), ProfileService.getDefaultProfile());
								}else {
									SwitchProfileUtil.setProfile(GPSService.instance.getApplicationContext(), profile);
								}
							}
						}
					}
				}else {
					step = 0;
				}
		    }
		    public void remove() {
		    	mLocationClient.removeNotifyEvent(notifyLister);
			}
		}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Location calculateleCorrespondLocation(Context context, LocationData locationData){
		//TODO 计算坐标是否符合规则设定
		Location location = null;
		return location;
	}
	
	public static long claculateAlarmTime(){
		long nextAlarmTime  = 123;
		return nextAlarmTime;
	}
	
	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
//	public static class MyLocationListenner implements BDLocationListener {
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			if (location == null)
//				return;
////			mySelectData.latitude = location.getLatitude();
////			mySelectData.longitude = location.getLongitude();
////			mySelectData.accuracy = location.getRadius();
////			mySelectData.direction = location.getDerect();
//			mLocationData.latitude = location.getLatitude();
//			mLocationData.longitude = location.getLongitude();
//			mLocationData.accuracy = location.getRadius();
//			mLocationData.direction = location.getDerect();
//////			myLocationOverlay.setData(mLocationData);
////			mySelectOverlay.setData(mySelectData);
////			mMapView.refresh();
////			mMapController.animateTo(new GeoPoint((int) (mySelectData.latitude * 1e6),
////							(int) (mySelectData.longitude * 1e6)), mHandler
////							.obtainMessage(1));
////			TextView02.setText((int) (mySelectData.latitude * 1e6)+" "+(int) (mySelectData.longitude * 1e6));
//			//以下一下是TAG
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			sb.append("\nderect : ");
//			sb.append(location.getDerect());
//			if (location.getLocType() == BDLocation.TypeGpsLocation) {
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//			}
//			Log.v(TAG, sb.toString());
//		}
//
//		@Override
//		public void onReceivePoi(BDLocation poiLocation) {
//			if (poiLocation == null) {
//				return;
//			}
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("Poi time : ");
//			sb.append(poiLocation.getTime());
//			sb.append("\nerror code : ");
//			sb.append(poiLocation.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(poiLocation.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(poiLocation.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(poiLocation.getRadius());
//			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
//				sb.append("\naddr : ");
//				sb.append(poiLocation.getAddrStr());
//			}
//			if (poiLocation.hasPoi()) {
//				sb.append("\nPoi:");
//				sb.append(poiLocation.getPoi());
//			} else {
//				sb.append("noPoi information");
//			}
//			Log.v(TAG, sb.toString());
//		}
//	}
	

}
