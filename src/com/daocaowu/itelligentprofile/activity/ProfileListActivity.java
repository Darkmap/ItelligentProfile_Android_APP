package com.daocaowu.itelligentprofile.activity;

import java.lang.reflect.Method;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.action.AbstractAction;
import com.daocaowu.itelligentprofile.adapter.ProfileListAdapter;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.ProfileService;

public class ProfileListActivity extends BaseActivity {

	private ListView mListView = null;
	private ProfileListAdapter mProfileListAdapter = null;
	private ProfileSetAction profileSetAction;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		profileSetAction = new ProfileSetAction();
		//TODO 界面写好即可
		// setContentView(R.layout.activity_main);
		// mListView = (ListView) findViewById(R.id.listview);
		// mProfileListAdapter = new ProfileListAdapter(this,
		// R.layout.activity_profile);
		// mListView.setAdapter(mProfileListAdapter);
	}
	
	
	private class ProfileSetAction extends AbstractAction{

		private Profile profile;
		private AbstractLocalService localService;
		
		private BluetoothAdapter mBluetoothAdapter;
		private LocationManager mLocationManager;
		private WifiManager mWifiManager;
		private ConnectivityManager mConnectivityManager;
		private WifiManager mWifiManager02;
		private AudioManager mAudioManager;
		
		public ProfileSetAction() {
			initSysServic();
		}
		
		/**
		 * 初始化系统服务
		 */
		private void initSysServic(){
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			mWifiManager02 = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		}
		

		
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			
		}
		
		/**
		 * 保存情景模式到数据库中
		 */
		public void saveProfile(){
			localService = new ProfileService();
			localService.add(profile);
		}
		
		/**
		 * 从数据库中获取单个情景模式信息
		 * @param objectId profileID
		 * @return 单个情景模式实体
		 */
		public Profile checkSingleProfile(int objectId){
			return (Profile) localService.check(objectId);
		}
		
		/**
		 * 从数据库中获取情景模式列表数据
		 * @return
		 */
		public List<Profile> checkProfiles(){
			return localService.check();
		}
		
		/**
		 * 删除数据库中单个Profile的数据
		 * @param objectId
		 */
		public void deleteProfile(int objectId){
			localService = new ProfileService();
			localService.delete(objectId);
		}
		
		/**
		 * 设置要操作的实体
		 * @param profile
		 */
		public void setProfile(Profile profile) {
			this.profile = profile;
		}
		
		/**
		 * 设置整个系统的情景模式
		 */
		private void setSysProfile() {
			

		}
		
		/**
		 * 设置蓝牙
		 * @param state
		 */
		private void setBluetooth(int state){
			if (state == 0) {
				setBluetoothClose();
			}else if (state == 1) {
				setBluetoothOpen();
			}
		}

		// ********* Bluetooth
		private void setBluetoothOpen() {
			// 如果Bluetooth已经关闭且不在打开状态中就打开Bluetooth
			if (!mBluetoothAdapter.isEnabled()
					&& !(mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON)) {
				// Intent intent = new Intent
				// ////如果蓝牙设备不可用的话,创建一个intent对象,该对象用于启动一个Activity,提示用户启动蓝牙适配器
				// (BluetoothAdapter.ACTION_REQUEST_ENABLE);startActivity(intent);
				mBluetoothAdapter.enable();
			}
		}

		private void setBluetoothClose() {
			// 如果Bluetooth已经打开了 就关闭Bluetooth
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.disable();
			}
		}

		private boolean getBluetoothStatus() {
			return mBluetoothAdapter.isEnabled();
		}
		
		/**
		 * 设置GPS
		 * @param state
		 */
		private void setGPS(int state){
			if (state == 0) {
				setGPSClose();
			}else if (state == 1) {
				setGPSOpen();
			}
		}

		// GPS
		private void setGPSOpen() {
			// TODO Auto-generated method stub
			Intent myIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
			// 手动设置打开
		}

		private void setGPSClose() {
			// TODO Auto-generated method stub
			Intent myIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
			// 手动设置关闭
		}

		private boolean getGPSStatus() {
			return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
					&& mLocationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		
		/**
		 * 设置WLAN
		 * @param state
		 */
		private void setWlan(int state){
			if (state == 0) {
				setWlanClose();
			}else if (state == 1) {
				setWlanOpen();
			}
		}

		// Wlan
		private void setWlanOpen() {
			// 如果wifi已经打开了 就关闭wifi
			if (mWifiManager.isWifiEnabled()) {
				mWifiManager.setWifiEnabled(false);
			}
		}

		private void setWlanClose() {
			// 如果wifi已经关闭且不在打开状态中就打开wifi
			if (!mWifiManager.isWifiEnabled()
					&& mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
				mWifiManager.setWifiEnabled(true);
			}
		}

		private boolean getWlanStatus() {
			return mWifiManager.isWifiEnabled();
		}

		/**
		 * 设置GPRS
		 * @param state
		 */
		private void setGPRS(int state){
			if (state == 0) {
				setGPRSClose();
			}else if (state == 1) {
				setGPRSOpen();
			}
		}

		
		// GPRS
		private void setGPRSOpen() {
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

		private void setGPRSClose() {
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

		private boolean geGPRSStatus() {
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

			return isOpen;
		}

		/**
		 * 设置WifiAP
		 * @param state
		 */
		private void setWifiAP(int state){
			if (state == 0) {
				setWifiAPClose();
			}else if (state == 1) {
				setWifiAPOpen();
			}
		}
		
		// WifiAP
		private void setWifiAPOpen() {
			// TODO Auto-generated method stub

		}

		private void setWifiAPClose() {
			// TODO Auto-generated method stub

		}

		private boolean getWifiAPStatus() {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * 设置ScreenBrightness
		 * @param state
		 */
		private void setScreenBrightness(int state){
			if (state == 0) {
				setWifiAPClose();
			}else if (state == 1) {
				setWifiAPOpen();
			}
		}
		// BrightnessAuto
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

		private boolean getScreenBrightnessMode()
				throws SettingNotFoundException {
			return android.provider.Settings.System.getInt(getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		}

		
		//ScreenBrightness
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
			// 设置当前程序的亮度值
			getWindow().setAttributes(lp2);
			// 设置系统的亮度值
			android.provider.Settings.System.putInt(getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
		}

		// ScreenRotation

		private void setScreenRotationAUTOMATIC() {
			Settings.System.putInt(getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 1);
		}

		private void setScreenRotationMANUAL() {
			Settings.System.putInt(getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 0);
		}
		
		private int getScreenRotationMode() throws SettingNotFoundException {
			return android.provider.Settings.System.getInt(getContentResolver(),
					android.provider.Settings.System.ACCELEROMETER_ROTATION);
		}
		
		//Audio
		
		private void setAudioVolume(int streamType,int index) {
			mAudioManager.setStreamVolume(streamType, index, 0);
//			mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);//铃声
//			mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);//通知
//			mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);//闹铃
//			mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress, 0);//双音多频电话（MIUI支持）
//			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);//音乐
//			mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);//系统
//			mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, 0);//通话（MIUI不支持）
		}
		private int getAudioVolume(int streamType) {
			// TODO Auto-generated method stub
			return mAudioManager.getStreamVolume(streamType);
//			mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//铃声
//			mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//通知
//			mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//闹铃
//			mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//双音多频电话
//			mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//音乐
//			mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//系统
//			mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//通话
		}
		private int getAudioMaxVolume(int streamType) {
			// TODO Auto-generated method stub
			return mAudioManager.getStreamMaxVolume(streamType);
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//铃声
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//通知
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//闹铃
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//双音多频电话
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//音乐
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//系统
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//通话
		}
		
		/**
		 * 设置铃声模式
		 * @param ringerMode
		 */
		private void setRingerMode(int ringerMode) {
			switch (ringerMode) {
			case 0:
				mAudioManager.setRingerMode(ringerMode);
				break;
			case 1:
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//静音
				break;
			case 2:
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//常规
				break;
			case 3:
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//振动
				break;

			default:
				break;
			}
//			mAudioManager.setRingerMode(ringerMode);
//			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//静音
//			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//常规
//			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//振动
		}
		
		private int getRingerMode() {
			return mAudioManager.getRingerMode();
//			AudioManager.RINGER_MODE_SILENT静音
//			AudioManager.RINGER_MODE_NORMAL常规
//			AudioManager.RINGER_MODE_VIBRATE振动
		}
		
		//AIRPLANE_MODE
		private void setAirplaneModeOn(boolean enabling) {
			try {
				android.provider.Settings.System.putString(getContentResolver(),
						android.provider.Settings.System.AIRPLANE_MODE_ON, enabling ?"1":"0");
//				android.provider.Settings.System.putString(getContentResolver(),
//						android.provider.Settings.Global.AIRPLANE_MODE_ON, enabling ?"1":"0");//最新版本的android
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", enabling);
				sendBroadcast(intent);
			} catch (Exception e) {
				// e.printStackTrace();
				Toast.makeText(getBaseContext(), "设置飞行模式状态失败", Toast.LENGTH_SHORT).show();
			}
		}
		
		private boolean getAirplaneMode() {
			int isAirplaneMode = 0;
			try {
				isAirplaneMode = android.provider.Settings.System.getInt(
						getContentResolver(),
						android.provider.Settings.System.AIRPLANE_MODE_ON, 0);
				return (isAirplaneMode == 1) ? true : false;
			} catch (Exception e) {
				Toast.makeText(getBaseContext(), "设获取置飞行模式状态失败", Toast.LENGTH_SHORT).show();
			}
			return (isAirplaneMode == 1) ? true : false;
		}

		private Profile getCurrentSetting(){
			Profile profile = new Profile();
			
			return profile;
		}
		
		
	}
	
	
}
