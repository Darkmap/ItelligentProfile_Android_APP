package com.daocaowu.itelligentprofile.service;

import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.activity.BaseActivity;
import com.daocaowu.itelligentprofile.bean.Profile;

public class ProfileService2 extends BaseActivity {

	private BluetoothAdapter mBluetoothAdapter;
	private LocationManager mLocationManager;
	private WifiManager mWifiManager;
	private ConnectivityManager mConnectivityManager;
	private WifiManager mWifiManager02;

	private AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

	/**********************************************************************************/

	/**
	 * 
	 */
	public ProfileService2() {
		// TODO Auto-generated constructor stub
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mLocationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		mWifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiManager02 = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
	}

	/************************ Bluetooth���� **********************************************************/

	private void setBluetoothOpen() {
		// ���Bluetooth�Ѿ��ر��Ҳ��ڴ�״̬�оʹ�Bluetooth
		if (!mBluetoothAdapter.isEnabled()
				&& !(mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)) {
			// Intent intent = new Intent
			// ////��������豸�����õĻ�,����һ��intent����,�ö�����������һ��Activity,��ʾ�û���������������
			// (BluetoothAdapter.ACTION_REQUEST_ENABLE);startActivity(intent);
			mBluetoothAdapter.enable();
		}
	}

	private void setBluetoothClose() {
		// ���Bluetooth�Ѿ����� �͹ر�Bluetooth
		if (mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
		}
	}

	private void setBluetooth(int isOpen) {
		if (isOpen == 1)
			setBluetoothOpen();
		else
			setBluetoothClose();
	}

	private int getBluetoothStatus() {
		return mBluetoothAdapter.isEnabled() ? 1 : 0;
	}

	/********** TODO**************** GPS ���Ƕ�λ ********************************************************/

	private void setGPSOpen() {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(myIntent);
		// �ֶ����ô�
	}

	private void setGPSClose() {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(myIntent);
		// �ֶ����ùر�
	}

	private void setGPS(int isOpen) {
		if (isOpen == 1)
			setGPSOpen();
		else
			setGPSClose();
	}

	private int getGPSStatus() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& mLocationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? 1
				: 0;
	}

	/**************************** Wlan ���߾����� ******************************************************/

	private void setWlanOpen() {
		// ���wifi�Ѿ����� �͹ر�wifi
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	private void setWlanClose() {
		// ���wifi�Ѿ��ر��Ҳ��ڴ�״̬�оʹ�wifi
		if (!mWifiManager.isWifiEnabled()
				&& mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	private void setWlan(int isOpen) {
		if (isOpen == 1)
			setWlanOpen();
		else
			setWlanClose();
	}

	private int getWlanStatus() {
		return mWifiManager.isWifiEnabled() ? 1 : 0;
	}

	/****************************** GPRS �ƶ����� ****************************************************/

	private void setGprsOpen() {
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

	private void setGprsClose() {
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

	private void setGprs(int isOpen) {
		if (isOpen == 1)
			setGprsOpen();
		else
			setGprsClose();
	}

	private int geGprsStatus() {
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

	private void setWifiAPOpen() {
		// TODO Auto-generated method stub

	}

	private void setWifiAPClose() {
		// TODO Auto-generated method stub

	}

	private void setWifiAP(int isOpen) {
		if (isOpen == 1)
			setWifiAPOpen();
		else
			setWifiAPClose();
	}

	private int getWifiAPStatus() {
		// TODO Auto-generated method stub
		return true ? 1 : 0;
	}

	/************************ ScreenBrightness ��Ļ���� **********************************************************/

	private void setScreenBrightnessAUTOMATIC() {
		try {
			android.provider.Settings.System
					.putInt(getContentResolver(),
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	private void setScreenBrightnessMANUAL() {
		try {
			android.provider.Settings.System
					.putInt(getContentResolver(),
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
							android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	private void setScreenBrightness(int isAuto) {
		if (isAuto == 1)
			setScreenBrightnessAUTOMATIC();
		else
			setScreenBrightnessMANUAL();
	}

	private int getScreenBrightnessMode() throws SettingNotFoundException {
		return android.provider.Settings.System.getInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC ? 1
				: 0;
	}

	private int getScreenBrightnessValues() {
		int value = 0;
		ContentResolver cr = getContentResolver();
		try {
			value = android.provider.Settings.System.getInt(cr,
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {

		}
		return value;
	}

	private void setScreenBrightnessValues(int progress) {
		WindowManager.LayoutParams lp2 = getWindow().getAttributes();
		if (progress == 0 || progress == 1) {
			lp2.screenBrightness = 2 / 255f;
			progress = 2;
		} else {
			lp2.screenBrightness = progress / 255f;
			getWindow().setAttributes(lp2);
		}
		// ���õ�ǰ���������ֵ
		getWindow().setAttributes(lp2);
		// ����ϵͳ������ֵ
		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
	}

	/*********************** ScreenRotation ��Ļ��ת ***********************************************************/

	private void setScreenRotationAUTOMATIC() {
		Settings.System.putInt(getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 1);
	}

	private void setScreenRotationMANUAL() {
		Settings.System.putInt(getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
	}

	private void setScreenRotation(int isAuto) {
		if (isAuto == 1)
			setScreenRotationAUTOMATIC();
		else
			setScreenRotationMANUAL();
	}

	private int getScreenRotationMode() throws SettingNotFoundException {
		return android.provider.Settings.System.getInt(getContentResolver(),
				android.provider.Settings.System.ACCELEROMETER_ROTATION);
	}

	/*** TODO************************Audio ���� *******************************************************/

	private void setAudioVolume(int streamType, int index) {
		mAudioManager.setStreamVolume(streamType, index, 0);
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

	private int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamVolume(streamType);
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
	}

	private int getAudioMaxVolume(int streamType) {
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

	private void setRingerMode(int ringerMode) {
		mAudioManager.setRingerMode(ringerMode);
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//����
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//����
		// mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//��
	}

	private int getRingerMode() {
		return mAudioManager.getRingerMode();
		// AudioManager.RINGER_MODE_SILENT����
		// AudioManager.RINGER_MODE_NORMAL����
		// AudioManager.RINGER_MODE_VIBRATE��
	}

	/************************* AIRPLANE_MODE ����ģʽ *********************************************************/

	private void setAirplaneMode(int isOpen) {
		try {
			android.provider.Settings.System.putString(getContentResolver(),
					android.provider.Settings.System.AIRPLANE_MODE_ON,
					isOpen == 1 ? "1" : "0");
			// android.provider.Settings.System.putString(getContentResolver(),
			// android.provider.Settings.Global.AIRPLANE_MODE_ON, enabling
			// ?"1":"0");//���°汾��androidAPI
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", isOpen);
			this.sendBroadcast(intent);
		} catch (Exception e) {
			// e.printStackTrace();
			Toast.makeText(this, "���÷���ģʽ״̬ʧ��", Toast.LENGTH_SHORT).show();
		}
	}

	private int getAirplaneMode() {
		int isAirplaneMode = 0;
		try {
			isAirplaneMode = android.provider.Settings.System.getInt(
					getContentResolver(),
					android.provider.Settings.System.AIRPLANE_MODE_ON, 0);
			return (isAirplaneMode == 1) ? 1 : 0;
		} catch (Exception e) {
			Toast.makeText(this, "���ȡ�÷���ģʽ״̬ʧ��", Toast.LENGTH_SHORT).show();
		}
		return (isAirplaneMode == 1) ? 1 : 0;
	}

	/************************* Vibrate �� *********************************************************/

	private void setVibrate(int isVibrate) {
		// 4.1.2��֧����
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

	/************************* ������ *********************************************************/

	private void setProfile(int isBluetoothOpen, int isGPSOpen, int isWlanOpen,
			int isGprsOpen, int isWifiAPOpen, int isScreenBrightnessAuto,
			int screenBrightnessValues, int isScreenAuto, int ringerMode,
			int ringVolume, int notificationVolume, int musicVolume,
			int alarmVolume, int isAirplaneModeOpen, int isVibrate) {
		setBluetooth(isBluetoothOpen);
		setGPS(isGPSOpen);
		setWlan(isWlanOpen);
		setGprs(isGprsOpen);
		setWifiAP(isWifiAPOpen);
		setScreenBrightness(isScreenBrightnessAuto);
		setScreenBrightnessValues(screenBrightnessValues);
		setScreenRotation(isScreenAuto);
		setRingerMode(ringerMode);
		setAudioVolume(AudioManager.STREAM_RING, ringVolume);
		setAudioVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume);
		setAudioVolume(AudioManager.STREAM_SYSTEM, notificationVolume);// ϵͳ������ʱ��Ϊ֪ͨ��������
		setAudioVolume(AudioManager.STREAM_MUSIC, musicVolume);
		setAudioVolume(AudioManager.STREAM_ALARM, alarmVolume);
		setAirplaneMode(isAirplaneModeOpen);
		setVibrate(isVibrate);
	}

	public void setProfile(Profile profile) {
		setProfile(profile.getBluetooth(), profile.getGps(), profile.getWifi(),
				profile.getNetwork(), profile.getWifiHospot(),
				profile.getAutoAdjustment(), profile.getBrightness(),
				profile.getAutoRotate(),
				AudioManager.RINGER_MODE_NORMAL/* ringerMode */,
				profile.getBellVolume(), profile.getInformVolume(),
				profile.getMediaVolume(), profile.getClockVolume(),
				profile.getTelephoneSignal(), profile.getVibrate());
	}
	// ��ӱд��
	// private Profile getCurrentSetting(){
	// Profile profile = new Profile();
	//
	// return profile;
	// }
	//
	// private void saveProfileIntoDB(){
	//
	// }
	//
	// private Profile getProfileFromDB() {
	// return DBManager.getProfile();
	// }
}
