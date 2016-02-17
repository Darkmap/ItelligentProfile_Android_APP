package com.daocaowu.itelligentprofile.bean;

import android.util.Log;

public class Profile{

	private int profileId;           //�龰ģʽId
	private String profileName = "���龰ģʽ";      //�龰ģʽ��
	private String startTime;        //��ʼʱ��
	private String endTime;          //����ʱ��
	private String label;            //��ǩ 0Ϊ��ͨ,1ΪĬ��ʹ��
	private int taskId;              //����Id//TODO �������б���أ�����������
	
	private int isdefault=0;			//�Ƿ���Ĭ���龰ģʽ��0���ǣ�1��
	private int type = 0;               //�龰ģʽ���ͣ����������Ƕ�ʱģʽ����ʡ��ģʽ
	private int powerValue = 0;         //����ʡ��ģʽ�ĵ�����ֵ
	
	private int clockVolume=50;      //������������ֵ 100��
	private String clockAlarm;       //����������·��
	private String clockMusic;       //�������֣�·��
	private int mediaVolume=50;      //ý����������ֵ 100��
	private int bellVolume=50;       //������������ֵ 100��
	private int informVolume=50;        //֪ͨ��������ֵ 100��
	private int brightness=50;          //���ȴ�С 100��
	private int autoAdjustment=1;      //�Զ��������ȣ�1���� 0�ر�
	private int autoRotate=0;          //�Զ���ת������ �ر�
	private int bluetooth=0;           //������1���� 0�ر�
	private int gps=1;                 //GPS��1���� 0�ر�
	private int wifi=0;                //WIFI��1���� 0�ر�
	private int wifiHospot=0;          //WIFI�ȵ㣺1���� 0�ر�
	
	private int vibrate=1;             //�𶯣�1���� 0�ر�
	
	private int network=1;             //�ƶ����磺1���� 0�ر�
	private int telephoneSignal=0;     //����ģʽ��0Ϊ���źţ�1Ϊ���ź�
	private int networkModel=2;        //����ģʽ��2G��3G��2G/3G
	
	
	private String settingId;        //���õ�ID��
	
	
	
	
	
	
	public Profile() {
		super();
	}
	public Profile(Profile p) {
		super();
		
		this.profileId = p.getProfileId();
		this.profileName = p.getProfileName();
		this.startTime = p.getStartTime();
		this.endTime = p.getEndTime();
		this.label = p.getLabel();
		this.taskId = p.getTaskId();
		this.type = p.getType();
		this.powerValue = p.getPowerValue();
		this.clockVolume = p.getClockVolume();
		this.clockAlarm = p.getClockAlarm();
		this.clockMusic = p.getClockMusic();
		this.mediaVolume = p.getMediaVolume();
		this.bellVolume = p.getBellVolume();
		this.informVolume = p.getInformVolume();
		this.brightness = p.getBrightness();
		this.autoAdjustment = p.getAutoAdjustment();
		this.autoRotate = p.getAutoRotate();
		this.bluetooth = p.getBluetooth();
		this.gps = p.getGps();
		this.wifi = p.getWifi();
		this.wifiHospot = p.getWifiHospot();
		this.vibrate = p.getVibrate();
		Log.v("getVibrate", this.vibrate+"");
		this.network = p.getNetwork();
		this.telephoneSignal = p.getTelephoneSignal();
		this.networkModel = p.getNetworkModel();
		this.settingId = p.getSettingId();
		this.isdefault = p.getIsdefault();
	}
	
	
	
	public int getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}
	public int getVibrate() {
		return vibrate;
	}
	public void setVibrate(int vibrate) {
		this.vibrate = vibrate;
	}
	public String getSettingId(){
		return settingId;
	}
	public void setSettingId(String settingId){
		this.settingId = settingId;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPowerValue() {
		return powerValue;
	}
	public void setPowerValue(int powerValue) {
		this.powerValue = powerValue;
	}
	public int getClockVolume() {
		return clockVolume;
	}
	public void setClockVolume(int clockVolume) {
		this.clockVolume = clockVolume;
	}
	public String getClockAlarm() {
		return clockAlarm;
	}
	public void setClockAlarm(String clockAlarm) {
		this.clockAlarm = clockAlarm;
	}
	public String getClockMusic() {
		return clockMusic;
	}
	public void setClockMusic(String clockMusic) {
		this.clockMusic = clockMusic;
	}
	public int getMediaVolume() {
		return mediaVolume;
	}
	public void setMediaVolume(int mediaVolume) {
		this.mediaVolume = mediaVolume;
	}
	public int getBellVolume() {
		return bellVolume;
	}
	public void setBellVolume(int bellVolume) {
		this.bellVolume = bellVolume;
	}
	public int getInformVolume() {
		return informVolume;
	}
	public void setInformVolume(int informVolume) {
		this.informVolume = informVolume;
	}
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	public int getAutoAdjustment() {
		return autoAdjustment;
	}
	public void setAutoAdjustment(int autoAdjustment) {
		this.autoAdjustment = autoAdjustment;
	}
	public int getAutoRotate() {
		return autoRotate;
	}
	public void setAutoRotate(int autoRotate) {
		this.autoRotate = autoRotate;
	}
	public int getBluetooth() {
		return bluetooth;
	}
	public void setBluetooth(int bluetooth) {
		this.bluetooth = bluetooth;
	}
	public int getGps() {
		return gps;
	}
	public void setGps(int gps) {
		this.gps = gps;
	}
	public int getWifi() {
		return wifi;
	}
	public void setWifi(int wifi) {
		this.wifi = wifi;
	}
	public int getWifiHospot() {
		return wifiHospot;
	}
	public void setWifiHospot(int wifiHospot) {
		this.wifiHospot = wifiHospot;
	}
	public int getNetwork() {
		return network;
	}
	public void setNetwork(int network) {
		this.network = network;
	}
	public int getTelephoneSignal() {
		return telephoneSignal;
	}
	public void setTelephoneSignal(int telephoneSignal) {
		this.telephoneSignal = telephoneSignal;
	}
	public int getNetworkModel() {
		return networkModel;
	}
	public void setNetworkModel(int networkModel) {
		this.networkModel = networkModel;
	}
	
}
