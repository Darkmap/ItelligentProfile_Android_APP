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
		//TODO ����д�ü���
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
		 * ��ʼ��ϵͳ����
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
		 * �����龰ģʽ�����ݿ���
		 */
		public void saveProfile(){
			localService = new ProfileService();
			localService.add(profile);
		}
		
		/**
		 * �����ݿ��л�ȡ�����龰ģʽ��Ϣ
		 * @param objectId profileID
		 * @return �����龰ģʽʵ��
		 */
		public Profile checkSingleProfile(int objectId){
			return (Profile) localService.check(objectId);
		}
		
		/**
		 * �����ݿ��л�ȡ�龰ģʽ�б�����
		 * @return
		 */
		public List<Profile> checkProfiles(){
			return localService.check();
		}
		
		/**
		 * ɾ�����ݿ��е���Profile������
		 * @param objectId
		 */
		public void deleteProfile(int objectId){
			localService = new ProfileService();
			localService.delete(objectId);
		}
		
		/**
		 * ����Ҫ������ʵ��
		 * @param profile
		 */
		public void setProfile(Profile profile) {
			this.profile = profile;
		}
		
		/**
		 * ��������ϵͳ���龰ģʽ
		 */
		private void setSysProfile() {
			

		}
		
		/**
		 * ��������
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

		private boolean getBluetoothStatus() {
			return mBluetoothAdapter.isEnabled();
		}
		
		/**
		 * ����GPS
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
			// �ֶ����ô�
		}

		private void setGPSClose() {
			// TODO Auto-generated method stub
			Intent myIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(myIntent);
			// �ֶ����ùر�
		}

		private boolean getGPSStatus() {
			return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
					&& mLocationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}
		
		/**
		 * ����WLAN
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

		private boolean getWlanStatus() {
			return mWifiManager.isWifiEnabled();
		}

		/**
		 * ����GPRS
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
		 * ����WifiAP
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
		 * ����ScreenBrightness
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
			// ���õ�ǰ���������ֵ
			getWindow().setAttributes(lp2);
			// ����ϵͳ������ֵ
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
//			mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);//����
//			mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);//֪ͨ
//			mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);//����
//			mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress, 0);//˫����Ƶ�绰��MIUI֧�֣�
//			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);//����
//			mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);//ϵͳ
//			mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, 0);//ͨ����MIUI��֧�֣�
		}
		private int getAudioVolume(int streamType) {
			// TODO Auto-generated method stub
			return mAudioManager.getStreamVolume(streamType);
//			mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//����
//			mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
//			mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//����
//			mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
//			mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//����
//			mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
//			mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
		}
		private int getAudioMaxVolume(int streamType) {
			// TODO Auto-generated method stub
			return mAudioManager.getStreamMaxVolume(streamType);
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//����
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//����
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//����
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
//			mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
		}
		
		/**
		 * ��������ģʽ
		 * @param ringerMode
		 */
		private void setRingerMode(int ringerMode) {
			switch (ringerMode) {
			case 0:
				mAudioManager.setRingerMode(ringerMode);
				break;
			case 1:
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//����
				break;
			case 2:
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//����
				break;
			case 3:
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//��
				break;

			default:
				break;
			}
//			mAudioManager.setRingerMode(ringerMode);
//			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//����
//			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);//����
//			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);//��
		}
		
		private int getRingerMode() {
			return mAudioManager.getRingerMode();
//			AudioManager.RINGER_MODE_SILENT����
//			AudioManager.RINGER_MODE_NORMAL����
//			AudioManager.RINGER_MODE_VIBRATE��
		}
		
		//AIRPLANE_MODE
		private void setAirplaneModeOn(boolean enabling) {
			try {
				android.provider.Settings.System.putString(getContentResolver(),
						android.provider.Settings.System.AIRPLANE_MODE_ON, enabling ?"1":"0");
//				android.provider.Settings.System.putString(getContentResolver(),
//						android.provider.Settings.Global.AIRPLANE_MODE_ON, enabling ?"1":"0");//���°汾��android
				Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
				intent.putExtra("state", enabling);
				sendBroadcast(intent);
			} catch (Exception e) {
				// e.printStackTrace();
				Toast.makeText(getBaseContext(), "���÷���ģʽ״̬ʧ��", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getBaseContext(), "���ȡ�÷���ģʽ״̬ʧ��", Toast.LENGTH_SHORT).show();
			}
			return (isAirplaneMode == 1) ? true : false;
		}

		private Profile getCurrentSetting(){
			Profile profile = new Profile();
			
			return profile;
		}
		
		
	}
	
	
}
