package com.daocaowu.itelligentprofile.DBHelper;

public enum TYPE {

	PROFILE(1),
	TASK(2),
	NOTE(3),
	LOCATION(4),
	WIFILOCATION(5);
	

	private final int type;
	
	private TYPE(int type){
		this.type = type;
	}
}
