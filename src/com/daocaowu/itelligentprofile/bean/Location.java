package com.daocaowu.itelligentprofile.bean;

public class Location {

	private int locationId; // λ��Id
	private String locationName="Ĭ��"; // λ������
	private double latitude;//γ��
	private double longitude;//����
	private double Radius;//�뾶
	private double setPosition; // ����λ��
	private double rangeSquare; // ��Χ���
	private String address;//��ַ��Ϣ
	private int profileId=-1; // �龰ģʽId
	private String profileName=""; // �龰ģʽ����

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
