package com.daocaowu.itelligentprofile.DBHelper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.daocaowu.itelligentprofile.DBHelper.DBHelper.TABLE;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.LocationColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.NoteColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.ProfileColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.TaskColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.WifiLocationColumns;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.bean.Note;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.bean.WifiLocation;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class DBManager {

	private static final String TAG = "DBManager";
	
	private static DBHelper mDbHelper;
	private static SQLiteDatabase db;
	public static Context context = null;
	
	public DBManager() {
		super();
	}
	
	
	/**
	 * 插入单个实体Profile到数据库中
	 * @param object
	 */
	public static int insertProfileIntoDB(Profile object){
		
		ContentValues cv = new ContentValues();
		cv.put(ProfileColumns.ATUOADJUSTMENT, object.getAutoAdjustment());
		cv.put(ProfileColumns.AUTOROTATE, object.getAutoRotate());
		cv.put(ProfileColumns.BELL_VOLUME, object.getBellVolume());
		cv.put(ProfileColumns.BLUETOOTH, object.getBluetooth());
		cv.put(ProfileColumns.BRIGHTNESS, object.getBrightness());
		cv.put(ProfileColumns.CLOCK_ALARM, object.getClockAlarm());
		cv.put(ProfileColumns.CLOCK_VOLUME, object.getClockVolume());
		cv.put(ProfileColumns.CLOCKMUSIC, object.getClockMusic());
		cv.put(ProfileColumns.ENDTIME, object.getEndTime());
		cv.put(ProfileColumns.GPS, object.getGps());
		cv.put(ProfileColumns.VIBREATE, object.getVibrate());
		cv.put(ProfileColumns.INFORM_VOLUME, object.getInformVolume());
		cv.put(ProfileColumns.LABEL, object.getLabel());
		cv.put(ProfileColumns.MEDIA_VOLUME, object.getMediaVolume());
		cv.put(ProfileColumns.NAME, object.getProfileName());
		cv.put(ProfileColumns.NETWORK, object.getNetwork());
		cv.put(ProfileColumns.NETWORK_MODEL, object.getNetworkModel());
		cv.put(ProfileColumns.SETTINGID, object.getSettingId());
		cv.put(ProfileColumns.STARTTIME, object.getStartTime());
		cv.put(ProfileColumns.TASKID, object.getTaskId());
		cv.put(ProfileColumns.TELEPHONE_SIGNAL, object.getTelephoneSignal());
		cv.put(ProfileColumns.WIFI, object.getWifi());
		cv.put(ProfileColumns.WIFI_HOSTPOT, object.getWifiHospot());
		cv.put(ProfileColumns.ATUOADJUSTMENT, object.getAutoAdjustment());
		cv.put(ProfileColumns.IS_DEFAULT, object.getIsdefault());
		cv.put(ProfileColumns.TYPE, object.getType());
		cv.put(ProfileColumns.POWER_VALUE, object.getPowerValue());
		
		
		int updateId = update(TYPE.PROFILE, cv, ProfileColumns.ID+"=?", new String[]{String.valueOf(object.getProfileId())});
		Log.v(TAG + "insertProfileIntoDB", "updateId="+updateId);
		if (updateId == 0) {
			return (int)insert(TYPE.PROFILE, cv);
		}else {
			ContentValues updateCV = new ContentValues();
			updateCV.put(ProfileColumns.NAME, object.getProfileName());
			update(TYPE.TASK, updateCV, TaskColumns.PROFILE_ID+"=?", new String[]{String.valueOf(object.getProfileId())});
			update(TYPE.WIFILOCATION, updateCV, WifiLocationColumns.PROFILE_ID+"=?", new String[]{String.valueOf(object.getProfileId())});
			update(TYPE.LOCATION, updateCV, LocationColumns.PROFILE_ID+"=?", new String[]{String.valueOf(object.getProfileId())});
			return object.getProfileId();
		}
		
//		Cursor queryCursor = query(TYPE.PROFILE, new String[]{ProfileColumns.ID}, ProfileColumns.ID+"=?", new String[]{String.valueOf(object.getProfileId())}, null);
//		if (queryCursor != null) {
//			int updateId = update(TYPE.PROFILE, cv, ProfileColumns.ID+"=?", new String[]{String.valueOf(object.getProfileId())});
//			Log.v(TAG + "insertProfileIntoDB", "updateId="+updateId);
//		}else {
//			insert(TYPE.PROFILE, cv);
//		}
		
		
	}

	/*********************** 实体数据的储存 ***************************************/
	
	/**
	 * 插入单个实体Task到数据库中
	 * @param object
	 */
	public static int insertTaskIntoDB(Task object) {

		ContentValues cv = new ContentValues();
        cv.put(TaskColumns.CONTENT, object.getContent());
        cv.put(TaskColumns.DURATION_TIME, object.getDurationTime());
        cv.put(TaskColumns.END_TIME, object.getEndTime());
        cv.put(TaskColumns.NAME,object.getTaskName());
        cv.put(TaskColumns.NOTE_ID, object.getNoteId());
        cv.put(TaskColumns.PROFILE_ID, object.getProfileId());
        cv.put(TaskColumns.REMIND_TIME, object.getRemindTime());
        cv.put(TaskColumns.SETTING_TYPE, object.getSettingType());
        cv.put(TaskColumns.START_TIME, object.getStartTime());
        cv.put(TaskColumns.TITILE, object.getTitle());
        cv.put(TaskColumns.USER_ID, object.getUserId());
        cv.put(TaskColumns.PROFILE_NAME, object.getProfileName());
        cv.put(TaskColumns.DAY_OF_WEEK, object.getDayofWeek());
        cv.put(TaskColumns.ENABLE, object.getEnable());

        int updateId = update(TYPE.TASK, cv, TaskColumns.ID+"=?", new String[]{String.valueOf(object.getTaskId())});
		Log.v(TAG + "insertTaskIntoDB", "updateId="+updateId);
		if (updateId == 0) {
			return (int) insert(TYPE.TASK, cv);
		}else {
			return object.getTaskId();
		}
	}

	/**
	 * 插入单个实体Note到数据库中
	 * @param object
	 */
	public static int insertNoteIntoDB(Note object) {

		ContentValues cv = new ContentValues();
		cv.put(NoteColumns.CONTENT, object.getContent());
		cv.put(NoteColumns.NAME, object.getNoteName());
		cv.put(NoteColumns.RECORD, object.getRecord());
		cv.put(NoteColumns.RECORD_LENGTH, object.getRecordLength());
		cv.put(NoteColumns.RECORD_NAME, object.getRecordName());
		cv.put(NoteColumns.DATE, object.getDate());
		cv.put(NoteColumns.ENABLE, object.getEnable());
		if (object.getMsgType()) {
			cv.put(NoteColumns.IS_COM_MEG, 1);
		}else {
			cv.put(NoteColumns.IS_COM_MEG, 0);
		}
		cv.put(NoteColumns.IS_RECORD, object.getNoteType());
		int updateId = update(TYPE.NOTE, cv, NoteColumns.ID+"=?", new String[]{String.valueOf(object.getNoteId())});
		Log.v(TAG + "insertNoteIntoDB", "updateId=" + updateId);
		if (updateId == 0) {
			return (int)insert(TYPE.NOTE, cv);
		}else{
			return object.getNoteId();
		}
	}

	/**
	 * 插入单个实体Location到数据库中
	 * @param object
	 */
	public static int insertLocationIntoDB(Location object) {

		ContentValues cv = new ContentValues();
		cv.put(LocationColumns.NAME, object.getLocationName());
		cv.put(LocationColumns.PROFILE_ID, object.getProfileId());
		cv.put(LocationColumns.LATITUDE, object.getLatitude());
		cv.put(LocationColumns.LONGITUDE, object.getLongitude());
		cv.put(LocationColumns.RADIUS, object.getRadius());
		cv.put(LocationColumns.RANGESQUARE, object.getRangeSquare());
		cv.put(LocationColumns.SET_POSITION, object.getSetPosition());
		cv.put(LocationColumns.PROFILE_NAME, object.getProfileName());
		cv.put(LocationColumns.ADDRESS, object.getAddress());

		int updateId = update(TYPE.LOCATION, cv, LocationColumns.ID+"=?", new String[]{String.valueOf(object.getLocationId())});
		Log.v(TAG + "insertLocationIntoDB", "updateId=" + updateId);
		if (updateId == 0) {
			return (int)insert(TYPE.LOCATION, cv);
		}else {
			return object.getLocationId();
		}
	}
	
	/**
	 * 插入单个实体WifiLocation到数据库中
	 * @param object
	 */
	public static int insertWifiLocationIntoDB(WifiLocation object) {

		ContentValues cv = new ContentValues();
		cv.put(WifiLocationColumns.NAME, object.getWifiLocationName());
		cv.put(WifiLocationColumns.SSID, object.getSsid());
		cv.put(WifiLocationColumns.BSSID, object.getBssid());
		cv.put(WifiLocationColumns.UPDATETIME, object.getUpdateTime());
		cv.put(WifiLocationColumns.PROFILE_ID, object.getProfileId());
		cv.put(WifiLocationColumns.PROFILE_NAME, object.getProfileName());
		cv.put(WifiLocationColumns.PRIORITY_LEVEL, object.getPriorityLevel());

		int updateId = update(TYPE.WIFILOCATION, cv, LocationColumns.ID+"=?", new String[]{String.valueOf(object.getWifiLocationId())});
		Log.v(TAG + "insertWifiLocationIntoDB", "updateId=" + updateId);
		if (updateId == 0) {
			return (int)insert(TYPE.WIFILOCATION, cv);
		}else {
			return object.getWifiLocationId();
		}
	}
	
	
	
	
	
	
	/*********************** 单个实体数据的查找 ***************************************/
	/**
	 * 从数据库中查找Profile的基本信息，构造一个新的实体
	 * @return
	 */
	public static Profile getProfile(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		Profile object = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			//把cursor里面的每个字段的值用来初始化实体
			object = getProfileFromCursor(cursor);
		}
		cursor.close();
		return object;
	}
	
//	public static Profile getLastProfile(){
//		Profile
//	}
	
	/**
	 * 从数据库中查找Task的基本信息，构造一个新的实体
	 * @return
	 */
	public static Task getTask(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		Task object = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			//把cursor里面的每个字段的值用来初始化实体
			object = getTaskFromCursor(cursor);
		}
		cursor.close();
		return object;
	}
	
	/**
	 * 从数据库中查找Note的基本信息，构造一个新的实体
	 * @return
	 */
	public static Note getNote(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		Note object = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			//把cursor里面的每个字段的值用来初始化实体
			object = getNoteFromCursor(cursor);
		}
		cursor.close();
		return object;
	}
	
	/**
	 * 从数据库中查找Location的基本信息，构造一个新的实体
	 * @return
	 */
	public static Location getLocation(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		Location object = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			//把cursor里面的每个字段的值用来初始化实体
			object = getLocationFromCursor(cursor);
		}
		cursor.close();
		return object;
	}
	
	/**
	 * 从数据库中查找WifeLocation的基本信息，构造一个新的实体
	 * @return TODO 、、还可以去掉TYPE entityType
	 */
	public static WifiLocation getWifiLocation(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		WifiLocation object = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			//把cursor里面的每个字段的值用来初始化实体
			object = getWifiLocationFromCursor(cursor);
		}
		cursor.close();
		return object;
	}
	
	/*********************** 多个实体数据的查找 ***************************************/
	
	/**
	 * 从数据库中查找profile的基本信息，构造一个新的实体列表
	 * @return
	 */
	public static List<Profile> getProfiles(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		List<Profile> objects = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor != null) {
			objects = new ArrayList<Profile>();
			while (cursor.moveToNext()) {
				//把cursor里面的每个字段的值用来初始化实体
				objects.add(getProfileFromCursor(cursor));
			}
			cursor.close();
		}
		
		return objects;
	}
	
	/**
	 * 从数据库中查找Task的基本信息，构造一个新的实体列表
	 * @return
	 */
	public static List<Task> getTasks(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		List<Task> objects = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor != null) {
			objects = new ArrayList<Task>();
			while (cursor.moveToNext()) {
				//把cursor里面的每个字段的值用来初始化实体
				objects.add(getTaskFromCursor(cursor));
			}
			cursor.close();
		}
		return objects;
	}
	
	/**
	 * 从数据库中查找Object
	 * @return Cursor
	 */
	public static Cursor getObjectsCursor(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs, String orderBy){
		return query(entityType, projection, selection, selectionArgs, orderBy);
	}
	
	public static int getQuantity(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		int count = 0;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				count++;
			}
			cursor.close();
		}
		return count;
	}
	
	
	/**
	 * 从数据库中查找Note的基本信息，构造一个新的实体列表
	 * @return
	 */
	public static List<Note> getNotes(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		List<Note> objects = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor != null) {
			objects = new ArrayList<Note>();
			while (cursor.moveToNext()) {
				//把cursor里面的每个字段的值用来初始化实体
				objects.add(getNoteFromCursor(cursor));
			}
			cursor.close();
		}
		return objects;
	}
	
	/**
	 * 从数据库中查找Location的基本信息，构造一个新的实体列表
	 * @return
	 */
	public static List<Location> getLocations(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		List<Location> objects = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor != null) {
			objects = new ArrayList<Location>();
			while (cursor.moveToNext()) {
				//把cursor里面的每个字段的值用来初始化实体
				objects.add(getLocationFromCursor(cursor));
			}
			cursor.close();
		}
		return objects;
	}
	
	/**
	 * 从数据库中查找WifiLocation的基本信息，构造一个新的实体列表
	 * @return
	 */
	public static List<WifiLocation> getWifiLocations(TYPE entityType, String[] projection,
			String selection, String[] selectionArgs){
		ArrayList<WifiLocation> objects = null;
		Cursor cursor = query(entityType, projection, selection, selectionArgs, null);
		if (cursor != null) {
			objects = new ArrayList<WifiLocation>();
			while (cursor.moveToNext()) {
				//把cursor里面的每个字段的值用来初始化实体
				objects.add(getWifiLocationFromCursor(cursor));
			}
			cursor.close();
		}
		return objects;
	}
	
	/*************************下面是转换函数*************************/
	/**
	 * 
	 * 传入指针cursor返回一个profile实体
	 * @return
	 */
	public static Profile getProfileFromCursor(Cursor cursor){
		Profile object = null;
		if(cursor != null){
			object = new Profile();
			object.setAutoAdjustment(cursor.getInt(cursor.getColumnIndex(ProfileColumns.ATUOADJUSTMENT)));
			object.setAutoRotate(cursor.getInt(cursor.getColumnIndex(ProfileColumns.AUTOROTATE)));
			object.setBellVolume(cursor.getInt(cursor.getColumnIndex(ProfileColumns.BELL_VOLUME)));
			object.setBluetooth(cursor.getInt(cursor.getColumnIndex(ProfileColumns.BLUETOOTH)));
			object.setBrightness(cursor.getInt(cursor.getColumnIndex(ProfileColumns.BRIGHTNESS)));
			object.setClockAlarm(cursor.getString(cursor.getColumnIndex(ProfileColumns.CLOCK_ALARM)));
			object.setClockMusic(cursor.getString(cursor.getColumnIndex(ProfileColumns.CLOCKMUSIC)));
			object.setClockVolume(cursor.getInt(cursor.getColumnIndex(ProfileColumns.CLOCK_VOLUME)));
			object.setEndTime(cursor.getString(cursor.getColumnIndex(ProfileColumns.ENDTIME)));
			object.setGps(cursor.getInt(cursor.getColumnIndex(ProfileColumns.GPS)));
			object.setVibrate(cursor.getInt(cursor.getColumnIndex(ProfileColumns.VIBREATE)));
			object.setInformVolume(cursor.getInt(cursor.getColumnIndex(ProfileColumns.INFORM_VOLUME)));
			object.setLabel(cursor.getString(cursor.getColumnIndex(ProfileColumns.LABEL)));
			object.setMediaVolume(cursor.getInt(cursor.getColumnIndex(ProfileColumns.MEDIA_VOLUME)));
			object.setNetwork(cursor.getInt(cursor.getColumnIndex(ProfileColumns.NETWORK)));
			object.setNetworkModel(cursor.getInt(cursor.getColumnIndex(ProfileColumns.NETWORK_MODEL)));
			object.setProfileId(cursor.getInt(cursor.getColumnIndex(ProfileColumns.ID)));
			object.setProfileName(cursor.getString(cursor.getColumnIndex(ProfileColumns.NAME)));
			object.setSettingId(cursor.getString(cursor.getColumnIndex(ProfileColumns.SETTINGID)));
			object.setStartTime(cursor.getString(cursor.getColumnIndex(ProfileColumns.STARTTIME)));
			object.setTaskId(cursor.getInt(cursor.getColumnIndex(ProfileColumns.TASKID)));
			object.setTelephoneSignal(cursor.getInt(cursor.getColumnIndex(ProfileColumns.TELEPHONE_SIGNAL)));
			object.setWifi(cursor.getInt(cursor.getColumnIndex(ProfileColumns.WIFI)));
			object.setWifiHospot(cursor.getInt(cursor.getColumnIndex(ProfileColumns.WIFI_HOSTPOT)));
			object.setIsdefault(cursor.getInt(cursor.getColumnIndex(ProfileColumns.IS_DEFAULT)));
			object.setType(cursor.getInt(cursor.getColumnIndex(ProfileColumns.TYPE)));
			object.setPowerValue(cursor.getInt(cursor.getColumnIndex(ProfileColumns.POWER_VALUE)));
		}
		return object;
	}
	
	/**
	 * 
	 * 传入指针cursor返回一个task实体
	 * @return
	 */
	public static Task getTaskFromCursor(Cursor cursor){
		Task object = null;
		if (cursor != null) {
			object = new Task();
			object.setContent(cursor.getString(cursor.getColumnIndex(TaskColumns.CONTENT)));
			object.setDurationTime(cursor.getString(cursor.getColumnIndex(TaskColumns.DURATION_TIME)));
			object.setEndTime(cursor.getString(cursor.getColumnIndex(TaskColumns.END_TIME)));
			object.setNoteId(cursor.getInt(cursor.getColumnIndex(TaskColumns.NOTE_ID)));
			object.setProfileId(cursor.getInt(cursor.getColumnIndex(TaskColumns.PROFILE_ID)));
			object.setRemindTime(cursor.getInt(cursor.getColumnIndex(TaskColumns.REMIND_TIME)));
			object.setSettingType(cursor.getString(cursor.getColumnIndex(TaskColumns.SETTING_TYPE)));
			object.setStartTime(cursor.getString(cursor.getColumnIndex(TaskColumns.START_TIME)));
			object.setTaskId(cursor.getInt(cursor.getColumnIndex(TaskColumns.ID)));
			object.setTaskName(cursor.getString(cursor.getColumnIndex(TaskColumns.NAME)));
			object.setTitle(cursor.getString(cursor.getColumnIndex(TaskColumns.TITILE)));
			object.setUserId(cursor.getInt(cursor.getColumnIndex(TaskColumns.USER_ID)));
			object.setProfileName(cursor.getString(cursor.getColumnIndex(TaskColumns.PROFILE_NAME)));
			object.setDayofWeek(cursor.getInt(cursor.getColumnIndex(TaskColumns.DAY_OF_WEEK)));
			object.setEnable(cursor.getInt(cursor.getColumnIndex(TaskColumns.ENABLE)));
		}
		return object;
		
	}
	
	/**
	 * 
	 * 传入指针curse返回一个Note实体
	 * @return
	 */
	public static Note getNoteFromCursor(Cursor cursor){
		Note object = null;
		if(cursor != null){
			object = new Note();
			object.setContent(cursor.getString(cursor.getColumnIndex(NoteColumns.CONTENT)));
			object.setNoteId(cursor.getInt(cursor.getColumnIndex(NoteColumns.ID)));
			object.setNoteName(cursor.getString(cursor.getColumnIndex(NoteColumns.NAME)));
			object.setRecord(cursor.getString(cursor.getColumnIndex(NoteColumns.RECORD)));
			object.setRecordLength(cursor.getString(cursor.getColumnIndex(NoteColumns.RECORD_LENGTH)));
			object.setRecordName(cursor.getString(cursor.getColumnIndex(NoteColumns.RECORD_NAME)));
			object.setDate(cursor.getString(cursor.getColumnIndex(NoteColumns.DATE)));
			object.setNoteType(cursor.getInt(cursor.getColumnIndex(NoteColumns.IS_RECORD)));
			if (cursor.getInt(cursor.getColumnIndex(NoteColumns.IS_COM_MEG))==1) {
				object.setMsgType(true);
			}else {
				object.setMsgType(false);
			}
			object.setEnable(cursor.getInt(cursor.getColumnIndex(NoteColumns.ENABLE)));
		}
		return object;
	}
	
	/**
	 * 
	 * 传入指针返回一个Location实体
	 * @return
	 */
	public static Location getLocationFromCursor(Cursor cursor){
		Location object = null;
		if(cursor != null){
			object = new Location();
			object.setLocationId(cursor.getInt(cursor.getColumnIndex(LocationColumns.ID)));
			object.setLocationName(cursor.getString(cursor.getColumnIndex(LocationColumns.NAME)));
			object.setProfileId(cursor.getInt(cursor.getColumnIndex(LocationColumns.PROFILE_ID)));
			object.setRangeSquare(cursor.getDouble(cursor.getColumnIndex(LocationColumns.RANGESQUARE)));
			object.setSetPosition(cursor.getDouble(cursor.getColumnIndex(LocationColumns.SET_POSITION)));
			object.setLatitude(cursor.getDouble(cursor.getColumnIndex(LocationColumns.LATITUDE)));
			object.setLongitude(cursor.getDouble(cursor.getColumnIndex(LocationColumns.LONGITUDE)));
			object.setRadius(cursor.getDouble(cursor.getColumnIndex(LocationColumns.RADIUS)));
			object.setProfileName(cursor.getString(cursor.getColumnIndex(LocationColumns.PROFILE_NAME)));
			object.setAddress(cursor.getString(cursor.getColumnIndex(LocationColumns.ADDRESS)));
		}
		return object;
	}
	
	
	/**
	 * 
	 * 传入指针返回一个Location实体
	 * @return
	 */
	public static WifiLocation getWifiLocationFromCursor(Cursor cursor){
		WifiLocation object = null;
		if(cursor != null){
			object = new WifiLocation();
			object.setWifiLocationId(cursor.getInt(cursor.getColumnIndex(WifiLocationColumns.ID)));
			object.setWifiLocationName(cursor.getString(cursor.getColumnIndex(WifiLocationColumns.NAME)));
			object.setSsid(cursor.getString(cursor.getColumnIndex(WifiLocationColumns.SSID)));
			object.setBssid(cursor.getString(cursor.getColumnIndex(WifiLocationColumns.BSSID)));
			object.setUpdateTime(cursor.getString(cursor.getColumnIndex(WifiLocationColumns.UPDATETIME)));
			object.setProfileId(cursor.getInt(cursor.getColumnIndex(WifiLocationColumns.PROFILE_ID)));
			object.setProfileName(cursor.getString(cursor.getColumnIndex(WifiLocationColumns.PROFILE_NAME)));
			object.setPriorityLevel(cursor.getInt(cursor.getColumnIndex(WifiLocationColumns.PRIORITY_LEVEL)));

		}
		return object;
	}
	
	/**
	 * 把字符串数组转换成SQL的过滤语句
	 * 
	 * @param filtersColumns
	 *            字符串数组
	 * @return SQL的过滤语句：String类型
	 */
	public static String changeStringArrayToSQLFilter(String[] filtersColumns) {
		StringBuffer SQLFilter = null;
		if (filtersColumns != null) {
			SQLFilter = new StringBuffer();
			int length = filtersColumns.length;
			for (int i = 0; i < (length - 1); i++) {
				SQLFilter.append(filtersColumns[i] + "=? AND ");
			}
			SQLFilter.append(filtersColumns[length - 1] + "=?");
		}
		return SQLFilter.toString();
	}
	
	/**************************************************************************************
	 * 下面是数据库的一些基本操作，具体操作将在实体调用的函数中实现逻辑细节 By ADao12          *
	 **************************************************************************************/
	
	
	/**
	 * Convenience method for query rows in the database.
	 * 
	 * @param type
	 *            The table name to compile the query against.
	 * @param projection
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is discouraged to prevent reading data from
	 *            storage that isn't going to be used.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param sortOrder
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered
	 * @return A Cursor object, which is positioned before the first entry. Note
	 *         that Cursors are not synchronized, see the documentation for more
	 *         details.
	 * @author CaoRen
	 */
	public static Cursor query(TYPE type, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if (mDbHelper == null) {
			DBManager.openDB(DataApplication.getContext());
		}
		if (mDbHelper == null) {
			return null;
		}
		if (db == null) {
			db = mDbHelper.getWritableDatabase();
		}
		switch (type) {
		case PROFILE:
			cursor = db.query(TABLE.PROFILE, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case TASK:
			cursor = db.query(TABLE.TASK, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case NOTE:
			cursor = db.query(TABLE.NOTE, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case LOCATION:
			cursor = db.query(TABLE.LOCATION, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case WIFILOCATION:
			cursor = db.query(TABLE.WIFILOCATION, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown TYPE " + type);
		}

		return cursor;
	}
	
	/**
	 * Convenience method for insert rows in the database.
	 * 
	 * @param type
	 *            the table to insert the row into
	 * @param contentValues
	 *            this map contains the initial column values for the row. The
	 *            keys should be the column names and the values the column
	 *            values
	 * @param selection
	 *            the optional WHERE clause to apply when deleting.
	 * @param selectionArgs
	 *            Passing null will update all rows.
	 * @return the number of rows affected
	 * @author CaoRen
	 */
	public static long insert(TYPE type, ContentValues contentValues) {
		long insertedId = 0;
		if (mDbHelper == null) {
			DBManager.openDB(DataApplication.getContext());
		}
		if (mDbHelper == null) {
			return -1;
		}
		if (db == null) {
			db = mDbHelper.getWritableDatabase();
		}
		switch (type) {
		case PROFILE:
			insertedId = db.insert(TABLE.PROFILE, null, contentValues);
			break;
		case TASK:
			insertedId = db.insert(TABLE.TASK, null, contentValues);
			break;
		case NOTE:
			insertedId = db.insert(TABLE.NOTE, null, contentValues);
			break;
		case LOCATION:
			insertedId = db.insert(TABLE.LOCATION, null, contentValues);
			break;
		case WIFILOCATION:
			insertedId = db.insert(TABLE.WIFILOCATION, null, contentValues);
			break;
		default:
			throw new IllegalArgumentException("Unknown TYPE " + type);
		}

		return insertedId;
	}
	
	
	/**
	 * Convenience method for delete rows in the database.
	 * 
	 * @param type
	 *            the table to delete from
	 * @param contentValues
	 *            a map from column names to new column values. null is a valid
	 *            value that will be translated to NULL.
	 * @param selection
	 *            the optional WHERE clause to apply when deleting.
	 * @param selectionArgs
	 *            Passing null will update all rows.
	 * @return the number of rows affected
	 * @author CaoRen
	 */
	public static int delete(TYPE type, String selection, String[] selectionArgs) {
		if (mDbHelper == null) {
			DBManager.openDB(DataApplication.getContext());
		}
		if (mDbHelper == null) {
			return -1;
		}
		if (db == null) {
			db = mDbHelper.getWritableDatabase();
		}
		int deleteId = 0;
		switch (type) {
		case PROFILE:
			deleteId = db.delete(TABLE.PROFILE, selection, selectionArgs);
			break;
		case TASK:
			deleteId = db.delete(TABLE.TASK, selection, selectionArgs);
			break;
		case NOTE:
			deleteId = db.delete(TABLE.NOTE, selection, selectionArgs);
			break;
		case LOCATION:
			deleteId = db.delete(TABLE.LOCATION, selection, selectionArgs);
			break;
		case WIFILOCATION:
			deleteId = db.delete(TABLE.WIFILOCATION, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown TYPE " + type);
		}

		return deleteId;

	}
	
	
	
	
	
	/**
	 * Convenience method for updating rows in the database.
	 * 
	 * @param type
	 *            the table to update in
	 * @param contentValues
	 *            a map from column names to new column values. null is a valid
	 *            value that will be translated to NULL.
	 * @param selection
	 *            the optional WHERE clause to apply when updating.
	 * @param selectionArgs
	 *            Passing null will update all rows.
	 * @return the number of rows affected
	 * @author CaoRen
	 */
	public static int update(TYPE type, ContentValues contentValues,
			String selection, String[] selectionArgs) {
		if (mDbHelper == null) {
			DBManager.openDB(DataApplication.getContext());
		}
		if (mDbHelper == null) {
			return -1;
		}
		if (db == null) {
			db = mDbHelper.getWritableDatabase();
		}
		int updateId = 0;
		switch (type) {
		case PROFILE:
			updateId = db.update(TABLE.PROFILE, contentValues, selection,
					selectionArgs);
			break;
		case TASK:
			updateId = db.update(TABLE.TASK, contentValues, selection,
					selectionArgs);
			break;
		case NOTE:
			updateId = db.update(TABLE.NOTE, contentValues, selection,
					selectionArgs);
			break;
		case LOCATION:
			updateId = db.update(TABLE.LOCATION, contentValues, selection,
					selectionArgs);
			break;
		case WIFILOCATION:
			updateId = db.update(TABLE.WIFILOCATION, contentValues, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown TYPE " + type);
		}

		return updateId;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 打开数据库
	 */
	public static Boolean openDB(Context context) {
		if (db == null) {
			if (mDbHelper != null) {
				db = mDbHelper.getReadableDatabase();
			}else {
				if (context != null) {
					DBManager.context = context;
					mDbHelper = DBHelper.getInstance(context);
					db = mDbHelper.getReadableDatabase();
				}
			}
			
		}
		return db == null;
	}
	
	/**
	 * 关闭数据库
	 */
	public static void closeDB(){
		if (db != null) {
			db.close();
		}
		mDbHelper = null;
	}
	
	public static SQLiteDatabase getDb() {
		return db;
	}
	
	/*
	 * 删除所有数据表
	 */
	public static void deleteAllTable(){
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE.PROFILE);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE.NOTE);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE.LOCATION);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE.TASK);
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE.WIFILOCATION);
	}
}
