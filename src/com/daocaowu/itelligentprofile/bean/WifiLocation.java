package com.daocaowu.itelligentprofile.bean;

public class WifiLocation {

	private int wifiLocationId;            //位置Id
	private String wifiLocationName;       //位置名称(用户的备注名)
	private String ssid="";                  //ssid
	private String bssid="";                //bssid（mac地址）
	private String updateTime;          //最近搜到此wifi的时间
	private int profileId=-1;             //情景模式Id
	private String profileName="";          //情景模式名称
	private int  priorityLevel = 1;        //WIFI优先级
	
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public String getProfileName() {
		return profileName;
	}
	
	public int getWifiLocationId() {
		return wifiLocationId;
	}
	public void setWifiLocationId(int wifiLocationId) {
		this.wifiLocationId = wifiLocationId;
	}
	public String getWifiLocationName() {
		return wifiLocationName;
	}
	public void setWifiLocationName(String wifiLocationName) {
		this.wifiLocationName = wifiLocationName;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	public int getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(int priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
}
