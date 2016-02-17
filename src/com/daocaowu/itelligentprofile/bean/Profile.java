package com.daocaowu.itelligentprofile.bean;

import android.util.Log;

public class Profile{

	private int profileId;           //情景模式Id
	private String profileName = "新情景模式";      //情景模式名
	private String startTime;        //开始时间
	private String endTime;          //结束时间
	private String label;            //标签 0为普通,1为默认使用
	private int taskId;              //任务Id//TODO 到底是列表好呢，还是这样呢
	
	private int isdefault=0;			//是否是默认情景模式，0不是，1是
	private int type = 0;               //情景模式类型，用来区分是定时模式还是省电模式
	private int powerValue = 0;         //启用省电模式的电量阈值
	
	private int clockVolume=50;      //闹钟音量：数值 100比
	private String clockAlarm;       //闹钟铃声：路径
	private String clockMusic;       //闹钟音乐：路径
	private int mediaVolume=50;      //媒体音量：数值 100比
	private int bellVolume=50;       //来电音量：数值 100比
	private int informVolume=50;        //通知音量：数值 100比
	private int brightness=50;          //亮度大小 100比
	private int autoAdjustment=1;      //自动调节亮度：1开启 0关闭
	private int autoRotate=0;          //自动旋转：开启 关闭
	private int bluetooth=0;           //蓝牙：1开启 0关闭
	private int gps=1;                 //GPS：1开启 0关闭
	private int wifi=0;                //WIFI：1开启 0关闭
	private int wifiHospot=0;          //WIFI热点：1开启 0关闭
	
	private int vibrate=1;             //震动：1开启 0关闭
	
	private int network=1;             //移动网络：1开启 0关闭
	private int telephoneSignal=0;     //飞行模式，0为有信号，1为无信号
	private int networkModel=2;        //网络模式：2G、3G、2G/3G
	
	
	private String settingId;        //设置的ID号
	
	
	
	
	
	
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
