package com.daocaowu.itelligentprofile.bean;

public class WifiLocation {

	private int wifiLocationId;            //λ��Id
	private String wifiLocationName;       //λ������(�û��ı�ע��)
	private String ssid="";                  //ssid
	private String bssid="";                //bssid��mac��ַ��
	private String updateTime;          //����ѵ���wifi��ʱ��
	private int profileId=-1;             //�龰ģʽId
	private String profileName="";          //�龰ģʽ����
	private int  priorityLevel = 1;        //WIFI���ȼ�
	
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
