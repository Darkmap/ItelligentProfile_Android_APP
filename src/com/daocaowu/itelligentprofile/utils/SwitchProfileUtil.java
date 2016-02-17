package com.daocaowu.itelligentprofile.utils;

import java.lang.reflect.Method;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.daocaowu.itelligentprofile.bean.Profile;

public class SwitchProfileUtil {

	private static BluetoothAdapter mBluetoothAdapter;
	private static LocationManager mLocationManager;
	private static WifiManager mWifiManager;
	private static ConnectivityManager mConnectivityManager;
	private static WifiManager mWifiManager02;
	private static AudioManager mAudioManager;
	private static Context thisContext;

	public static void init(Context context) {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		mWifiManager02 = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
	}

	/**********************************************************************************/


	/************************ Bluetooth蓝牙 **********************************************************/

	public static void setBluetoothOpen() {
		// 如果Bluetooth已经关闭且不在打开状态中就打开Bluetooth
		if (!mBluetoothAdapter.isEnabled()
				&& !(mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)) {
			// Intent intent = new Intent
			// ////如果蓝牙设备不可用的话,创建一个intent对象,该对象用于启动一个Activity,提示用户启动蓝牙适配器
			// (BluetoothAdapter.ACTION_REQUEST_ENABLE);startActivity(intent);
			mBluetoothAdapter.enable();
		}
	}

	public static void setBluetoothClose() {
		// 如果Bluetooth已经打开了 就关闭Bluetooth
		if (mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
		}
	}

	public static void setBluetooth(int isOpen) {
		if (isOpen == 1)
			setBluetoothOpen();
		else
			setBluetoothClose();
	}

	public static int getBluetoothStatus() {
		return mBluetoothAdapter.isEnabled() ? 1 : 0;
	}

	/********** TODO**************** GPS 卫星定位 ********************************************************/

	public static void setGPSOpen() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// 手动设置打开
		toggleGPS();
	}

	public static void setGPSClose() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// 手动设置关闭
		toggleGPS();
	}

	// 打开或者关闭GPS
	private static void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");// 利用java反射功能，发送广播：
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		// 这句是关键，GPS的代码为：3，只要把这个值改一下就可以实现其它的几个功能。
		// 其中WIFI：0；背光高度：1；同步数据：2；GPS：3；蓝牙：4
		try {
			PendingIntent.getBroadcast(thisContext, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
		
//		Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		//手动设置打开
	}
	public static void setGPS(int isOpen) {
		if (isOpen == 1)
			setGPSOpen();
		else
			setGPSClose();
	}

	public static int getGPSStatus() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& mLocationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? 1
				: 0;
	}

	/**************************** Wlan 无线局域网 ******************************************************/

	public static void setWlanOpen() {
		// 如果wifi已经关闭且不在打开状态中就打开wifi
				if (!mWifiManager.isWifiEnabled()
						&& mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
					mWifiManager.setWifiEnabled(true);
				}
		
	}

	public static void setWlanClose() {
		// 如果wifi已经打开了 就关闭wifi
				if (mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(false);
				}
	}

	public static void setWlan(int isOpen) {
		if (isOpen == 1)
			setWlanOpen();
		else
			setWlanClose();
	}

	public static int getWlanStatus() {
		return mWifiManager.isWifiEnabled() ? 1 : 0;
	}
	
	public static int getWlanStatus(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return mWifiManager.isWifiEnabled() ? 1 : 0;
	}

	/****************************** GPRS 移动数据 ****************************************************/

	public static void setGprsOpen() {
		Class cmClass = mConnectivityManager.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod("setMobileDataEnabled",
					argClasses);
			method.invoke(mConnectivityManager, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setGprsClose() {
		Class cmClass = mConnectivityManager.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod("setMobileDataEnabled",
					argClasses);
			method.invoke(mConnectivityManager, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setGprs(int isOpen) {
		if (isOpen == 1)
			setGprsOpen();
		else
			setGprsClose();
	}

	public static int geGprsStatus() {
		Class cmClass = mConnectivityManager.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod("getMobileDataEnabled",
					argClasses);
			isOpen = (Boolean) method.invoke(mConnectivityManager, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen ? 1 : 0;
	}

	/********* TODO******************WifiAP 便携式wlani热点 *******************************************************/

	public static void setWifiAPOpen() {
		// TODO Auto-generated method stub

	}

	public static void setWifiAPClose() {
		// TODO Auto-generated method stub

	}

	public static void setWifiAP(int isOpen) {
		if (isOpen == 1)
			setWifiAPOpen();
		else
			setWifiAPClose();
	}

	public static int getWifiAPStatus() {
		// TODO Auto-generated method stub
		return true ? 1 : 0;
	}

	/************************ ScreenBrightness 屏幕亮度 **********************************************************/

	public static void setScreenBrightnessAUTOMATIC(Context context) {
		try {
			android.provider.Settings.System
					.putInt(context.getContentResolver(),
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static void setScreenBrightnessMANUAL(Context context) {
		try {
			android.provider.Settings.System
					.putInt(context.getContentResolver(),
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static void setScreenBrightnessMode(Context context,int isAutoScreenBrightness) {
		if (isAutoScreenBrightness == 1)
			setScreenBrightnessAUTOMATIC(context);
		else
			setScreenBrightnessMANUAL(context);
	}

	public static int getScreenBrightnessMode(Context context) throws SettingNotFoundException {
		return android.provider.Settings.System.getInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE);
	}

	public static int getScreenBrightnessValues(Context context) {
		int value = 0;
		ContentResolver cr = context.getContentResolver();
		try {
			value = android.provider.Settings.System.getInt(cr,
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {

		}
		return value;
	}

	public static void setScreenBrightnessValues(Context context,int progress) {
//		WindowManager.LayoutParams lp2 = context.getWindow().getAttributes();
		if (progress == 0 || progress == 1) {
//			lp2.screenBrightness = 2 / 255f;
			progress = 2;
		}
//		else {
//			lp2.screenBrightness = progress / 255f;
//			getWindow().setAttributes(lp2);
//		}
//		// 设置当前程序的亮度值
//		getWindow().setAttributes(lp2);
		// 设置系统的亮度值
		android.provider.Settings.System.putInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
	}

	/*********************** ScreenRotation 屏幕旋转 ***********************************************************/

	public static void setScreenRotationAUTOMATIC(Context context) {
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 1);
	}

	public static void setScreenRotationMANUAL(Context context) {
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
	}

	public static void setScreenRotation(Context context,int isScreenRotationAuto) {
		if (isScreenRotationAuto == 1)
			setScreenRotationAUTOMATIC(context);
		else
			setScreenRotationMANUAL(context);
	}

	public static int getScreenRotationMode(Context context) throws SettingNotFoundException {
		return android.provider.Settings.System.getInt(context.getContentResolver(),
				android.provider.Settings.System.ACCELEROMETER_ROTATION);
	}

	/*** TODO************************Audio 音量 *******************************************************/

	public static void setAudioVolume(int streamType, int index) {
		mAudioManager.setStreamVolume(streamType, (int)(index/100.0*getAudioMaxVolume(streamType)), 0);
		// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress,
		// 0);//铃声
		// mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
		// progress, 0);//通知
		// mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
		// 0);//闹铃
		// mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress,
		// 0);//双音多频电话（MIUI支持）
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
		// 0);//音乐
		// mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
		// 0);//系统
		// mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
		// progress, 0);//通话（MIUI不支持）
	}

	public static int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return (int)(mAudioManager.getStreamVolume(streamType)*100.0/getAudioMaxVolume(streamType));
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//铃声
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//通知
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//闹铃
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//双音多频电话
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//音乐
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//系统
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//通话
	}

	public static int getAudioMaxVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamMaxVolume(streamType);
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//铃声
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//通知
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//闹铃
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//双音多频电话
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//音乐
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//系统
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//通话
	}

	public static void setRingerMode(int ringerMode) {
		mAudioManager.setRingerMode(ringerMode);
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//静音
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//常规
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//振动
	}

	public static int getRingerMode() {
		return mAudioManager.getRingerMode();
		// AudioManager.RINGER_MODE_SILENT静音
		// AudioManager.RINGER_MODE_NORMAL常规
		// AudioManager.RINGER_MODE_VIBRATE振动
	}

	/************************* AIRPLANE_MODE 飞行模式 *********************************************************/

	public static void setAirplaneMode(Context context,int isAirplaneModeOpen) {
		if(isAirplaneModeOpen==1){
			if(getAirplaneMode(context)!=1){
				android.provider.Settings.System.putString(context.getContentResolver(),
						android.provider.Settings.System.AIRPLANE_MODE_ON,"1");
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", true);
				context.sendBroadcast(intent);
			}
		}
		else if(isAirplaneModeOpen==0){
			if(getAirplaneMode(context)==1){
				android.provider.Settings.System.putString(context.getContentResolver(),
						android.provider.Settings.System.AIRPLANE_MODE_ON,"0");
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", false);
				context.sendBroadcast(intent);
			}
			
		}
		
//		try {
//			android.provider.Settings.System.putString(context.getContentResolver(),
//					android.provider.Settings.System.AIRPLANE_MODE_ON,
//					isOpen == 1 ? "1" : "0");
//			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//			intent.putExtra("state", isOpen);
//			context.sendBroadcast(intent);
//		} catch (Exception e) {
//			// e.printStackTrace();
//			Toast.makeText(context, "设置飞行模式状态失败", Toast.LENGTH_SHORT).show();
//		}
	}
	public static int getAirplaneMode(Context context) {  
        return Settings.System.getInt(context.getContentResolver(),  
        Settings.System.AIRPLANE_MODE_ON, 0);  
  }
//
//	public static int getAirplaneMode(Context context) {
//		int isAirplaneMode = 0;
//		try {
//			isAirplaneMode = android.provider.Settings.System.getInt(
//					context.getContentResolver(),
//					android.provider.Settings.System.AIRPLANE_MODE_ON, 0);
//			return (isAirplaneMode == 1) ? 1 : 0;
//		} catch (Exception e) {
//			Toast.makeText(context, "设获取置飞行模式状态失败", Toast.LENGTH_SHORT).show();
//		}
//		return (isAirplaneMode == 1) ? 1 : 0;
//	}

	/************************* Vibrate 振动 *********************************************************/

	public static void setVibrate(int isVibrate) {
		if (isVibrate == 1) {
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_ON);
			mAudioManager.setVibrateSetting(
					AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_ON);
		} else {
			mAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
					AudioManager.VIBRATE_SETTING_OFF);
			mAudioManager.setVibrateSetting(
					AudioManager.VIBRATE_TYPE_NOTIFICATION,
					AudioManager.VIBRATE_SETTING_OFF);
		}
	}
	
	public static int getVibrateStatus() {
		return mAudioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
	}

	/************************* 总设置 *********************************************************/

	public static void setProfile(Context context,int isBluetoothOpen, int isGPSOpen,
			int isWlanOpen, int isGprsOpen, int isWifiAPOpen,
			int isScreenBrightnessAuto, int screenBrightnessValues,
			int isScreenAuto, int ringerMode, int ringVolume,
			int notificationVolume, int musicVolume, int alarmVolume,
			int isAirplaneModeOpen, int isVibrate) {
		thisContext = context;
		setBluetooth(isBluetoothOpen);
		setGPS(isGPSOpen);
		setWlan(isWlanOpen);
		setGprs(isGprsOpen);
//		setWifiAP(isWifiAPOpen);
		setScreenBrightnessMode(context,isScreenBrightnessAuto);
		setScreenBrightnessValues(context,screenBrightnessValues);
//		setScreenRotation(context,isScreenAuto);
//		setRingerMode(ringerMode);
		
		setAudioVolume(AudioManager.STREAM_MUSIC, musicVolume);
		setAudioVolume(AudioManager.STREAM_ALARM, alarmVolume);
		if(android.os.Build.BRAND.equals("Xiaomi"))//如果是小米手机
		{
			setAudioVolume(AudioManager.STREAM_NOTIFICATION, ringVolume);
			setAudioVolume(AudioManager.STREAM_SYSTEM, ringVolume);// 系统音量 通知音量 铃声音量相同
		}
		else//如果不是小米手机
		{
			setAudioVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume);
			setAudioVolume(AudioManager.STREAM_SYSTEM, notificationVolume);// 系统音量归为通知音量里面
			setAudioVolume(AudioManager.STREAM_RING, ringVolume);//这个在MIUI里面不准使用  增加了一个判断机制
		}
		
		setAirplaneMode(context,isAirplaneModeOpen);
		
		setVibrate(isVibrate);
		//如果铃声变为0了 就会变为静音模式  
		   //如果是开启了振动  那就转为振动模式
		if( getRingerMode()==AudioManager.RINGER_MODE_SILENT && isVibrate==1 )
			setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		   //如果是关闭了振动  那就转为静音模式
		else if( getRingerMode()==AudioManager.RINGER_MODE_VIBRATE && isVibrate==0 )
			setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	/*
	 * 设置系统当前情景模式
	 */
	public static void setProfile(Context context, Profile profile) {
		if (profile != null) {
			init(context);
			init(context);
			setProfile(context,
					profile.getBluetooth(), 
					profile.getGps(), 
					profile.getWifi(),
					profile.getNetwork(),
					profile.getWifiHospot(),
					profile.getAutoAdjustment(), 
					profile.getBrightness(),
					profile.getAutoRotate(),
					AudioManager.RINGER_MODE_NORMAL/* ringerMode */,
					profile.getBellVolume(),
					profile.getInformVolume(),
					profile.getMediaVolume(),
					profile.getClockVolume(),
					profile.getTelephoneSignal(),
					profile.getVibrate());
			DataApplication.lastProfileId = profile.getProfileId();
		}
	}
	
	/*
	 * 获取系统当前情景模式
	 */
	public static Profile getProfile(Context context) throws SettingNotFoundException  {
		init(context);
		Profile nowProfile = new Profile();
		nowProfile.setBluetooth(getBluetoothStatus());
		nowProfile.setGps(getGPSStatus());
		nowProfile.setWifi(getWlanStatus());
		nowProfile.setNetwork(geGprsStatus());
		nowProfile.setWifiHospot(getWifiAPStatus());
		nowProfile.setAutoAdjustment(getScreenBrightnessMode(context));
		nowProfile.setBrightness(getScreenBrightnessValues(context));
		nowProfile.setAutoRotate(getScreenRotationMode(context));
		/* ringerMode */;
		nowProfile.setBellVolume(getAudioVolume(AudioManager.STREAM_RING));
		nowProfile.setInformVolume(getAudioVolume(AudioManager.STREAM_NOTIFICATION));
		nowProfile.setMediaVolume(getAudioVolume(AudioManager.STREAM_MUSIC));
		nowProfile.setClockVolume(getAudioVolume(AudioManager.STREAM_ALARM));
		nowProfile.setTelephoneSignal(getAirplaneMode(context)); 
		nowProfile.setVibrate(getVibrateStatus());
		return nowProfile;
	}
}
