package com.daocaowu.itelligentprofile.bean;

public class Location {

	private int locationId; // 位置Id
	private String locationName="默认"; // 位置名称
	private double latitude;//纬度
	private double longitude;//经度
	private double Radius;//半径
	private double setPosition; // 设置位置
	private double rangeSquare; // 范围面积
	private String address;//地址信息
	private int profileId=-1; // 情景模式Id
	private String profileName=""; // 情景模式名称

	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public double getRadius() {
		return Radius;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setRadius(double radius) {
		Radius = radius;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileName() {
		return profileName;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getSetPosition() {
		return setPosition;
	}

	public void setSetPosition(double setPosition) {
		this.setPosition = setPosition;
	}

	public double getRangeSquare() {
		return rangeSquare;
	}

	public void setRangeSquare(double rangeSquare) {
		this.rangeSquare = rangeSquare;
	}

	public int getProfileId() {
		return profileId;
	}

	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}

}
