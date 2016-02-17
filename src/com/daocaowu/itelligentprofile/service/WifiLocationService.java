package com.daocaowu.itelligentprofile.service;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TYPE;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.LocationColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.WifiLocationColumns;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.WifiLocation;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class WifiLocationService extends AbstractLocalService{

	private static final String TAG = "WifiLocationService";
	
	private Context context;
	
	public WifiLocationService(Context context) {
		this.context = context;
	}
	
	@Override
	public int add(Object object) {
		
		return DBManager.insertWifiLocationIntoDB((WifiLocation)object);

	}
	@Override
	public void delete(int objectId) {
		DBManager.delete(TYPE.WIFILOCATION, LocationColumns.ID+"=?", new String[]{String.valueOf(objectId)});

	}
	@Override
	public Object check(int objectId) {
		return DBManager.getWifiLocation(TYPE.WIFILOCATION, null, WifiLocationColumns.ID+"=?", new String[]{String.valueOf(objectId)});
		
	}

	/**
	 * ����ȫ����
	 */
	@Override
	public List check() {
		return DBManager.getWifiLocations(TYPE.WIFILOCATION, null, null, null);
	}
	/**
	 * �������龰��
	 */
	public List checkWithProfile() {
		return DBManager.getWifiLocations(TYPE.WIFILOCATION, null, WifiLocationColumns.PROFILE_ID+"!=0", null);
		
	}
	/**
	 * ����û���龰��
	 */
	public List checkWithoutProfile() {
		return DBManager.getWifiLocations(TYPE.WIFILOCATION, null, WifiLocationColumns.PROFILE_ID+"=0", null);
		
	}
	
	public static WifiLocation check(String bssid) {
		return DBManager.getWifiLocation(
				TYPE.WIFILOCATION,
				null,
				DBManager.changeStringArrayToSQLFilter(new String[] {
						WifiLocationColumns.BSSID,
						WifiLocationColumns.PROFILE_ID }), new String[] {
						bssid, "!=-1" });
	}
	
	@Override
	public void update(Object object) {
		DBManager.insertWifiLocationIntoDB((WifiLocation)object);

	}
	
	public static void initWifiLocation(Context context){
		
	}
	
	public static void setWifiLocation(Context context, long repeatTimeMillis){
		if (repeatTimeMillis != 0) {
			enableWifiLocaiton(context, DataApplication.repeatTimeOfWIFI);
		}else {
			disableLocation(context);
		}
		
	}
	
	/**
	 * ����WIFI��λ
	 */
	public static void enableWifiLocaiton(Context context, long repeatTimeMillis){
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//		Log.v(TAG, "**location id "+wifiLocation.getWifiLocationId());
		// ����Action
		Intent intent = new Intent(TaskService.WIFI_TASK_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTimeMillis, sender);
	}
	
	/**
	 * �ر�WIFI��λ
	 * @param context
	 */
	public static void disableLocation(Context context){
		DataApplication.lastProfileId = -1;
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TaskService.WIFI_TASK_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
	
	/**
	 * ��ȡ��ǰ�����������������龰ģʽ��WiFiList
	 */
	public static List<WifiLocation> getCurrentWifiLocations(List<WifiLocation> wifiLocations){
		//TODO �����ݿ��л�ȡ�����������龰ģʽ��WiFiList
		return wifiLocations;
	}
	
	/**
	 * ������������õ�һ��WifiLocation
	 * @param wifiLocations
	 * @return
	 */
	public static WifiLocation getAccurateWifiLocation(List<WifiLocation> wifiLocations){
		WifiLocation wifiLocation = null;
		//TODO
		return wifiLocation;
	}
	
	/**
	 * ��ý�Ҫ���õ�Profile
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
	 * ����bssid�����Ѿ��趨��Profile��WifiLocationʵ��
	 * @param bssid
	 * @return
	 */
	public static WifiLocation hasBssid(String bssid) {
		return DBManager.getWifiLocation(TYPE.WIFILOCATION, null, WifiLocationColumns.BSSID+"="+bssid+" AND "+WifiLocationColumns.PROFILE_ID+"!=-1", null);
	};
	
}
