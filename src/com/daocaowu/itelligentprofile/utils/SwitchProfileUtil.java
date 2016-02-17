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


	/************************ Bluetooth���� **********************************************************/

	public static void setBluetoothOpen() {
		// ���Bluetooth�Ѿ��ر��Ҳ��ڴ�״̬�оʹ�Bluetooth
		if (!mBluetoothAdapter.isEnabled()
				&& !(mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)) {
			// Intent intent = new Intent
			// ////��������豸�����õĻ�,����һ��intent����,�ö�����������һ��Activity,��ʾ�û���������������
			// (BluetoothAdapter.ACTION_REQUEST_ENABLE);startActivity(intent);
			mBluetoothAdapter.enable();
		}
	}

	public static void setBluetoothClose() {
		// ���Bluetooth�Ѿ����� �͹ر�Bluetooth
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

	/********** TODO**************** GPS ���Ƕ�λ ********************************************************/

	public static void setGPSOpen() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// �ֶ����ô�
		toggleGPS();
	}

	public static void setGPSClose() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// �ֶ����ùر�
		toggleGPS();
	}

	// �򿪻��߹ر�GPS
	private static void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");// ����java���书�ܣ����͹㲥��
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		// ����ǹؼ���GPS�Ĵ���Ϊ��3��ֻҪ�����ֵ��һ�¾Ϳ���ʵ�������ļ������ܡ�
		// ����WIFI��0������߶ȣ�1��ͬ�����ݣ�2��GPS��3��������4
		try {
			PendingIntent.getBroadcast(thisContext, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
		
//		Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		//�ֶ����ô�
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

	/**************************** Wlan ���߾����� ******************************************************/

	public static void setWlanOpen() {
		// ���wifi�Ѿ��ر��Ҳ��ڴ�״̬�оʹ�wifi
				if (!mWifiManager.isWifiEnabled()
						&& mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
					mWifiManager.setWifiEnabled(true);
				}
		
	}

	public static void setWlanClose() {
		// ���wifi�Ѿ����� �͹ر�wifi
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

	/****************************** GPRS �ƶ����� ****************************************************/

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

	/********* TODO******************WifiAP ��Яʽwlani�ȵ� *******************************************************/

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

	/************************ ScreenBrightness ��Ļ���� **********************************************************/

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
//		// ���õ�ǰ���������ֵ
//		getWindow().setAttributes(lp2);
		// ����ϵͳ������ֵ
		android.provider.Settings.System.putInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
	}

	/*********************** ScreenRotation ��Ļ��ת ***********************************************************/

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

	/*** TODO************************Audio ���� *******************************************************/

	public static void setAudioVolume(int streamType, int index) {
		mAudioManager.setStreamVolume(streamType, (int)(index/100.0*getAudioMaxVolume(streamType)), 0);
		// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
		// progress, 0);//֪ͨ
		// mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress,
		// 0);//˫����Ƶ�绰��MIUI֧�֣�
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
		// 0);//ϵͳ
		// mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
		// progress, 0);//ͨ����MIUI��֧�֣�
	}

	public static int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return (int)(mAudioManager.getStreamVolume(streamType)*100.0/getAudioMaxVolume(streamType));
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
	}

	public static int getAudioMaxVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamMaxVolume(streamType);
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
	}

	public static void setRingerMode(int ringerMode) {
		mAudioManager.setRingerMode(ringerMode);
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//����
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//����
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//��
	}

	public static int getRingerMode() {
		return mAudioManager.getRingerMode();
		// AudioManager.RINGER_MODE_SILENT����
		// AudioManager.RINGER_MODE_NORMAL����
		// AudioManager.RINGER_MODE_VIBRATE��
	}

	/************************* AIRPLANE_MODE ����ģʽ *********************************************************/

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
//			Toast.makeText(context, "���÷���ģʽ״̬ʧ��", Toast.LENGTH_SHORT).show();
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
//			Toast.makeText(context, "���ȡ�÷���ģʽ״̬ʧ��", Toast.LENGTH_SHORT).show();
//		}
//		return (isAirplaneMode == 1) ? 1 : 0;
//	}

	/************************* Vibrate �� *********************************************************/

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

	/************************* ������ *********************************************************/

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
		if(android.os.Build.BRAND.equals("Xiaomi"))//�����С���ֻ�
		{
			setAudioVolume(AudioManager.STREAM_NOTIFICATION, ringVolume);
			setAudioVolume(AudioManager.STREAM_SYSTEM, ringVolume);// ϵͳ���� ֪ͨ���� ����������ͬ
		}
		else//�������С���ֻ�
		{
			setAudioVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume);
			setAudioVolume(AudioManager.STREAM_SYSTEM, notificationVolume);// ϵͳ������Ϊ֪ͨ��������
			setAudioVolume(AudioManager.STREAM_RING, ringVolume);//�����MIUI���治׼ʹ��  ������һ���жϻ���
		}
		
		setAirplaneMode(context,isAirplaneModeOpen);
		
		setVibrate(isVibrate);
		//���������Ϊ0�� �ͻ��Ϊ����ģʽ  
		   //����ǿ�������  �Ǿ�תΪ��ģʽ
		if( getRingerMode()==AudioManager.RINGER_MODE_SILENT && isVibrate==1 )
			setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		   //����ǹر�����  �Ǿ�תΪ����ģʽ
		else if( getRingerMode()==AudioManager.RINGER_MODE_VIBRATE && isVibrate==0 )
			setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	/*
	 * ����ϵͳ��ǰ�龰ģʽ
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
	 * ��ȡϵͳ��ǰ�龰ģʽ
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
