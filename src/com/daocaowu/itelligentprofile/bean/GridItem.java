package com.daocaowu.itelligentprofile.bean;

public class GridItem {
	
	private int imageID;
	private String itemname;
	
	
	
	
	
	public GridItem(int imageID, String itemname) {
		super();
		this.imageID = imageID;
		this.itemname = itemname;
	}
	public int getImageID() {
		return imageID;
	}
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	
	
	
}
