package com.daocaowu.itelligentprofile.DBHelper;

public class TableColumns {

	public interface ProfileColumns {

		public static final String ID = "_id";

		public static final String NAME = "profileName";

		public static final String STARTTIME = "startTime";

		public static final String ENDTIME = "endTime";

		public static final String LABEL = "label";

		public static final String SETTINGID = "settingId";

		public static final String TASKID = "taskId";
		
		public static final String TYPE = "type";
		
		public static final String POWER_VALUE = "powerValue";

		public static final String IS_DEFAULT = "isDefualt";
		
		public static final String CLOCK_VOLUME = "clockVolume";

		public static final String CLOCK_ALARM = "clockAlarm";

		public static final String CLOCKMUSIC = "clockMusic";

		public static final String MEDIA_VOLUME = "mediaVolume";

		public static final String BELL_VOLUME = "bellVolume";

		public static final String INFORM_VOLUME = "informVolume";

		public static final String BRIGHTNESS = "brightness";

		public static final String ATUOADJUSTMENT = "autoAdjustment";

		public static final String AUTOROTATE = "autoRotate";

		public static final String BLUETOOTH = "bluetooth";

		public static final String GPS = "gps";

		public static final String WIFI = "wifi";
		
		public static final String VIBREATE = "vibrate";

		public static final String WIFI_HOSTPOT = "wifiHospot";

		public static final String NETWORK = "network";

		public static final String TELEPHONE_SIGNAL = "telephoneSignal";

		public static final String NETWORK_MODEL = "networkModel";

	}


	public interface TaskColumns {

		public static final String ID = "_id";

		public static final String NAME = "taskName";

		public static final String TITILE = "title";

		public static final String START_TIME = "startTime";

		public static final String REMIND_TIME = "remindTime";
		
		public static final String DAY_OF_WEEK = "dayOfWeek";

		public static final String DURATION_TIME = "durationTime";

		public static final String END_TIME = "endTime";

		public static final String CONTENT = "content";

		public static final String SETTING_TYPE = "settingType";

		public static final String USER_ID = "userId";

		public static final String PROFILE_ID = "profileId";
		
		public static final String PROFILE_NAME = "profileName";

		public static final String NOTE_ID = "noteId";
		
		public static final String ENABLE = "enable";

	}
	
	public interface NoteColumns{
		
		public static final String ID = "_id";
		
		public static final String NAME = "noteName";

		public static final String CONTENT = "content";
		
		public static final String RECORD = "record";
		
		public static final String RECORD_LENGTH = "recordLength";
		
		public static final String RECORD_NAME = "recordName";
		
		public static final String DATE = "date";

		public static final String IS_COM_MEG = "isComMeg";

		public static final String IS_RECORD = "isRecord";
		
		public static final String ENABLE = "enable";
	}
	
	public interface LocationColumns {

		public static final String ID = "_id";

		public static final String NAME = "locationName";
		
		public static final String LATITUDE = "latitude";

		public static final String LONGITUDE = "longitude";

		public static final String RADIUS = "radius";

		public static final String SET_POSITION = "setPosition";

		public static final String RANGESQUARE = "rangeSquare";

		public static final String PROFILE_ID = "profileId";
		
		public static final String PROFILE_NAME = "profileName";
		
		public static final String ADDRESS = "address";

	}
	
	public interface WifiLocationColumns {
		
		public static final String ID = "_id";
		
		public static final String NAME = "name";//用户的备注名

		public static final String SSID = "ssid";

		public static final String BSSID = "bssid";

		public static final String UPDATETIME = "updateTime";

		public static final String PROFILE_ID = "profileId";
		
		public static final String PROFILE_NAME = "profileName";
		
		public static final String PRIORITY_LEVEL = "priorityLevel";
	}
	
	public interface AlarmColumns{
		public static final String ID = "_id";
		
	}
	
}
