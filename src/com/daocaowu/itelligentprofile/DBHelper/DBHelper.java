package com.daocaowu.itelligentprofile.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daocaowu.itelligentprofile.DBHelper.TableColumns.LocationColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.NoteColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.ProfileColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.TaskColumns;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.WifiLocationColumns;

public class DBHelper extends SQLiteOpenHelper{

	private static final String TAG = "ProfileDatabaseHelper";
	
	private static final String DB_NAME = "profile.db";

    private static final int DB_VERSION = 1;
	
	public interface TABLE{
		public static final String PROFILE = "profile";
		
		public static final String TASK = "task";
		
		public static final String NOTE = "note";
		
		public static final String LOCATION = "location";
		
		public static final String WIFILOCATION = "wifiLocation";
	}
	
	static DBHelper mDbHelper;
	
	private static final String CREATE_PROFILE_TABLE_SQL = 
			"CREATE TABLE IF NOT EXISTS " + TABLE.PROFILE + "(" +
	         ProfileColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
	         ProfileColumns.NAME + " INT NULL," +
	         ProfileColumns.STARTTIME + " VARCHAR NULL," +
	         ProfileColumns.ENDTIME + " VARCHAR NULL," +
	         ProfileColumns.LABEL + " VARCHAR," +
	         ProfileColumns.TASKID + " INT NULL," +
	         ProfileColumns.IS_DEFAULT + " INT NULL," +
	         ProfileColumns.TYPE + " INT NULL," +
	         ProfileColumns.POWER_VALUE + " INT NULL," +
	         ProfileColumns.SETTINGID + " INT NULL," +
	         ProfileColumns.CLOCK_VOLUME + " INT NULL," +
	         ProfileColumns.CLOCK_ALARM + " VARCHAR," +
	         ProfileColumns.CLOCKMUSIC + " VARCHAR," +
	         ProfileColumns.MEDIA_VOLUME + " INT NULL," +
	         ProfileColumns.BELL_VOLUME + " INT NULL," +
	         ProfileColumns.INFORM_VOLUME + " INT NULL," +
	         ProfileColumns.BRIGHTNESS + " INT NULL," +
	         ProfileColumns.ATUOADJUSTMENT + " INT NULL," +
	         ProfileColumns.AUTOROTATE + " INT NULL," +
	         ProfileColumns.BLUETOOTH + " INT NULL," +
	         ProfileColumns.GPS + " INT NULL," +
	         ProfileColumns.WIFI + " INT NULL," +
	         ProfileColumns.WIFI_HOSTPOT + " INT NULL," +
	         ProfileColumns.VIBREATE + " INT NULL," +
	         ProfileColumns.NETWORK + " INT NULL," +
	         ProfileColumns.NETWORK_MODEL + " INT NULL," +
	         ProfileColumns.TELEPHONE_SIGNAL + " INT NULL" +
			")";
	
	
	private static final String CREATE_TASK_TABLE_SQL = 
			"CREATE TABLE IF NOT EXISTS " + TABLE.TASK + "(" +
	         TaskColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
	         TaskColumns.NAME + " VARCHAR NULL," +
	         TaskColumns.START_TIME + " VARCHAR NULL," +
	         TaskColumns.TITILE + " VARCHAR NULL," +
	         TaskColumns.REMIND_TIME + " VARCHAR NULL," +
	         TaskColumns.DURATION_TIME + " VARCHAR NULL," +
	         TaskColumns.END_TIME + " VARCHAR NULL," +
	         TaskColumns.CONTENT + " VARCHAR NULL," +
	         TaskColumns.DAY_OF_WEEK + " INT NULL," +
	         TaskColumns.SETTING_TYPE + " VARCHAR," +
	         TaskColumns.USER_ID + " INT NULL," +
	         TaskColumns.PROFILE_ID + " INT NULL," +
	         TaskColumns.PROFILE_NAME + " VARCHAR NULL," +
	         TaskColumns.NOTE_ID + " INT NULL," +
	         TaskColumns.ENABLE + " INT NULL" +
			")";
	
	private static final String CREAT_PROFILE_UPDATE_TIGGER = 
			"CREATE TRIGGER PROFILE_UPDATE"
			+ " AFTER UPDATE ON " + TABLE.PROFILE
			+ " BEGIN "
			+ " FOR EACH ROW BEGIN "
			+ " SELECT RAISE(ROLLBACK,'No this classId in jokeClass')"
			+ " WHERE (SELECT classId FROM jokeClass WHERE classId = NEW.classId) IS NULL; "
			+ " END; ";
	
	private static final String CREATE_NOTE_TABLE_SQL = 
			"CREATE TABLE IF NOT EXISTS " + TABLE.NOTE + "(" +
	         NoteColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
	         NoteColumns.NAME + " VARCHAR," +
	         NoteColumns.CONTENT + " VARCHAR," +
	         NoteColumns.RECORD + " VARCHAR," +
	         NoteColumns.RECORD_LENGTH + " VARCHAR," +
	         NoteColumns.RECORD_NAME + " VARCHAR," +
	         NoteColumns.IS_COM_MEG + " INT," +
			 NoteColumns.DATE + " VARCHAR," +
			 NoteColumns.IS_RECORD + " INT," +
			 NoteColumns.ENABLE + " INT" +
			")";
	
	private static final String CREATE_LOCATION_TABLE_SQL = 
			"CREATE TABLE IF NOT EXISTS " + TABLE.LOCATION + "(" +
	         LocationColumns.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
	         LocationColumns.NAME + " VARCHAR," +
	         LocationColumns.LATITUDE + " DOUBLE NULL," +
	         LocationColumns.LONGITUDE + " DOUBLE NULL," +
	         LocationColumns.RADIUS + " DOUBLE NULL," +
	         LocationColumns.SET_POSITION + " DOUBLE NULL," +
	         LocationColumns.RANGESQUARE + " DOUBLE NULL," +
	         LocationColumns.PROFILE_ID + " INT NULL," +
	         LocationColumns.PROFILE_NAME + " VARCHAR NULL," +
	         LocationColumns.ADDRESS + " VARCHAR NULL"+
			")";
	
	private static final String CREATE_WIFILOCATION_TABLE_SQL = 
		    "CREATE TABLE IF NOT EXISTS " + TABLE.WIFILOCATION + "(" +
		    WifiLocationColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		    WifiLocationColumns.NAME + " VARCHAR," +
		    WifiLocationColumns.SSID + " VARCHAR NULL," +
		    WifiLocationColumns.BSSID + " VARCHAR NULL," +
		    WifiLocationColumns.UPDATETIME + " VARCHAR NULL," +
		    WifiLocationColumns.PROFILE_ID + " INT NULL," +
		    WifiLocationColumns.PROFILE_NAME + " VARCHAR NULL," +
		    WifiLocationColumns.PRIORITY_LEVEL + " INT NULL" +
		   ")";
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

    public void creatProfileTables(SQLiteDatabase db){
		db.execSQL(CREATE_PROFILE_TABLE_SQL);
		Log.d(TAG, "tables has been created");
		db.execSQL(CREATE_TASK_TABLE_SQL);
		db.execSQL(CREATE_NOTE_TABLE_SQL);
		db.execSQL(CREATE_LOCATION_TABLE_SQL);
		db.execSQL(CREATE_WIFILOCATION_TABLE_SQL);
		Log.d(TAG, "tables has been created");
	}
    
    static synchronized DBHelper getInstance(Context context){
    	if(mDbHelper == null){
    		mDbHelper = new DBHelper(context);
    	}
    	return mDbHelper;
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建数据库
		creatProfileTables(db);
		Log.i("sqlite", "creatProfileTables(db)!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//更改数据库版本的操作
		db.execSQL("DROP TABLE IF EXISTS "+TABLE.PROFILE); 
		db.execSQL("DROP TABLE IF EXISTS "+TABLE.TASK);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE.NOTE);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE.LOCATION);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE.WIFILOCATION);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		Log.i("sqlite", "DataBase is opened!");
		System.out.println("DataBase is opened!");
	}
}
