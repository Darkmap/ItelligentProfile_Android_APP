package com.daocaowu.itelligentprofile.service;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcel;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;

import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.DBHelper.TYPE;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.TaskColumns;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.DateUtil;

public class TaskService extends AbstractLocalService{

	private static final String TAG = TaskService.class.getSimpleName();
	
	private Context context;
	
	public static final String PREFERENCES = "AlarmClock";
	
	final static String PREF_SNOOZE_ID = "snooze_id";
    final static String PREF_SNOOZE_TIME = "snooze_time";
	
    public static final int TASK_STATUSBAR_CANCEL = 0;//取消
	public static final int TASK_STATUSBAR_NOTIFY = 1;//通知
	public static final int TASK_STATUSBAR_INVARIANT = 2;//不变
	
	public static final long ONE_WEEK_TIME = 7*24*60*60*1000;
	
	/**
	 * 这个Action不仅用来触发AlarmReceiver也用来触发解锁事件
	 * 这是用于 响应来自于alarm Manager的Alarm Broadcasts
	 */
	public static final String POWER_SAVING_TASK_ACTION = "com.daocaowu.itelligentprofile.service.POWER_SAVING_TASK";
	
	public static final String GPS_TASK_ACTION = "com.daocaowu.itelligentprofile.service.GPS_TASK";
	
	public static final String WIFI_TASK_ACTION = "com.daocaowu.itelligentprofile.service.WIFI_TASK";
	
	public static final String TASK_ALERT_ACTION = "com.daocaowu.itelligentprofile.service.TASK_ALERT";
	
	public static final String TASK_DONE_ACTION = "com.daocaowu.itelligentprofile.service.TASK_DONE";

	public static final String TASK_SNOOZE_ACTION = "com.daocaowu.itelligentprofile.service.TASK_SNOOZE";

	public static final String TASK_DISMISS_ACTION = "com.daocaowu.itelligentprofile.service.TASK_DISMISS";

	public static final String TASK_KILLED = "task_killed";
	
	public static final String TASK_WIFI_ACTION = "com.daocaowu.itelligentprofile.service.TASK_WIFI";
	
	public static final String TASK_GPS_ACTION = "com.daocaowu.itelligentprofile.service.TASK_GPS";
	 
	/**
	 * 这个String变量是用来在Intent中传递一个任务实体用的
	 */
	public static final String TASK_INTENT_EXTRA = "intent.extra.task";
	
	public static final String TASK_RAW_DATA = "intent.extra.task_raw";
	/**
	 * 这个String变量是用来在Intent中传递一个任务ID用的
	 */
    public static final String TASK_ID = "task_id";
    
    private final static String DM12 = "E h:mm aa";
    private final static String DM24 = "E k:mm";

    private final static String M12 = "h:mm aa";
    // Shared with DigitalClock
    final static String M24 = "kk:mm";
    
    public TaskService(Context context) {
    	this.context = context;
	}
    
    public static Task currentTask;
    
    public static void initDefaultTask(){
    	if (DataApplication.defaultTask == null) {
    		Log.v(TAG, "initDefaultTask()");
			DataApplication.defaultTask = new Task();
			Log.v(TAG, "DataApplication.defaultTask = new Task()");
			DataApplication.defaultTask.setTaskId(0);
			DataApplication.defaultTask.setDayofWeek(Calendar.WEEK_OF_YEAR);
	    	DataApplication.defaultTask.setEnable(1);
		}
    }
    
    /**
     * 把一个任务添加进入数据库，同时设置一个任务定时器
     */
	@Override
	public int add(Object object) {
//		((Task)object).setDayofWeek((((Task)object).getDayofWeek()+1)%7);
		int count1 = countTasks();//获得现在的任务个数
		//获得要保存任务的定时时间
		long timeInMillis = claculateAlarmTime((Task)object);
		//设置要保存任务的定时时间(也叫提醒时间)
		((Task)object).setRemindTime(timeInMillis);
		// 获得保存任务的ID，包括修改的任务的ID
		int insertTaskId = DBManager.insertTaskIntoDB((Task) object);
		//获得保存任务后的个数
		int count2 = countTasks();
		
		Log.v(TAG, "intsetTaskId:"+insertTaskId+" -count1:"+count1+"-count2:"+count2);
		
		//TODO 如果任务数不变，就要把原来的定时取消掉再重新设置定时任务，任务数增加了，就直接设置定时任务
		
		if (((Task)object).getEnable() == 1) {
			clearSnoozeIfNeeded(context, timeInMillis);
		}
		
		if (count1 < count2) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		setNextAlert(context);
		return insertTaskId;
	}
	
	public static int add(Context context, Object object){
		int count1 = countTasks();//获得现在的任务个数
		//获得要保存任务的定时时间
		long timeInMillis = claculateAlarmTime((Task)object);
		//设置要保存任务的定时时间(也叫提醒时间)
		((Task)object).setRemindTime(timeInMillis);
		// 获得保存任务的ID，包括修改的任务的ID
		int insertTaskId = DBManager.insertTaskIntoDB((Task) object);
		//获得保存任务后的个数
		int count2 = countTasks();
		
		Log.v(TAG, "intsetTaskId:"+insertTaskId+" -count1:"+count1+"-count2:"+count2);
		
		//TODO 如果任务数不变，就要把原来的定时取消掉再重新设置定时任务，任务数增加了，就直接设置定时任务
		
		if (((Task)object).getEnable() == 1) {
			clearSnoozeIfNeeded(context, timeInMillis);
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		setNextAlert(context);
		return insertTaskId;
	}

	@Override
	public void delete(int objectId) {
		DBManager.delete(TYPE.TASK, TaskColumns.ID+"=?", new String[]{String.valueOf(objectId)});
		setNextAlert(context);
	}

	@Override
	public Object check(int objectId) {
		return DBManager.getTask(TYPE.TASK, null, TaskColumns.ID+"=?", new String[]{String.valueOf(objectId)});
		
	}

	@Override
	public List check() {
		return DBManager.getTasks(TYPE.TASK, null, null, null);
		
	}

	@Override
	public void update(Object object) {
		DBManager.insertTaskIntoDB((Task) object);

	}
	
	public static int countTasks(){
		return DBManager.getQuantity(TYPE.TASK, null, TaskColumns.ENABLE+"=?", new String[]{String.valueOf(1)});
	}
	
	public static void deleteByProfileId(int profileId){
		DBManager.delete(TYPE.TASK, TaskColumns.PROFILE_ID+"=?", new String[]{String.valueOf(profileId)});
		Log.v("deleteByProfileId", profileId+"");
	}
	
	public static List<Task> checkByProfileId(int profileId){
		return DBManager.getTasks(TYPE.TASK, null, TaskColumns.PROFILE_ID+"=?", new String[]{String.valueOf(profileId)});
	}
	
	/**
	 * 在系统开机的时候，时间和时区变化的时候或者是用户修改任务的设置的时候 调用
	 * 如果小睡模式设置了，就激活它，否则加载所有任务，激活下一个任务
	 * @param context
	 */
	public static void setNextAlert(final Context context) {
		initDefaultTask();
		//获得离现在时间最近的Task
		Task task = calculateNextTask(context);
		if (task != null) {
			//设置任务
			enableTask(context, task, claculateAlarmTime(task));
//			TaskReceiverService.task = task;
		} else {
			//取消任务
			disableTask(context);
		}
	}
	
	/**
	 * 找出下一个最近的哪个Task
	 * @param context
	 * @return
	 */
	public static Task calculateNextTask(final Context context){
		Task task = null;
		long minTime = Long.MAX_VALUE;
		long now = System.currentTimeMillis();
		Cursor cursor = DBManager.getObjectsCursor(TYPE.TASK, null, TaskColumns.ENABLE+"=1", null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					
					//把cursor里面的每个字段的值用来初始化实体
					Task t = DBManager.getTaskFromCursor(cursor);
//					Log.v(TAG+"-calculateNextTask","t.getRemindTime()-:"+ t.getRemindTime());
					long tTime = 0;
					tTime = claculateAlarmTime(t);
//					Log.v(TAG+"-calculateNextTask","tTime-:"+ tTime);
//					Log.v(TAG+"-calculateNextTask","nowTime-:"+ now);
					//TODO 
					t.setRemindTime(tTime);
					//要获得的是离现在时间最近的任务
					if (tTime < now) {
//						Log.v(TAG+"-calculateNextTask","tTime < now");
						continue;
					}
					
					//找出时间最近的那个
					if (tTime < minTime) {
						minTime = tTime;
						task = t;
//						Log.v(TAG+"-calculateNextTask", "tTime < minTime");
					}
					Log.v(TAG+"-calculateNextTask", "t: id="+t.getTaskId());
					
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		
		//下面是选择DefaultTask和数据库中查出来的Task，哪个作为NextTask的逻辑
		//比较DefaultTask和NextTask的开始时间
		long t = claculateAlarmTime(task);
		long dt;
		if (DataApplication.defaultTask.getStartTime() == null || "".equals(DataApplication.defaultTask.getStartTime())) {
			dt = Long.MAX_VALUE;
		}else {
			if (DataApplication.defaultTask.getDayofWeek() == 0) {
				DataApplication.defaultTask.setDayofWeek(Calendar.DAY_OF_WEEK);
			}
			dt = claculateAlarmTime(DataApplication.defaultTask);
		}

		if (dt < t) {
			Profile profile = ProfileService.getDefaultProfile();
			DataApplication.defaultTask.setProfileId(profile.getProfileId());
			DataApplication.defaultTask.setProfileName(profile.getProfileName());
			Log.v(TAG, "calculateNextTask : DataApplication.defaultTask : defaultTask: id="+DataApplication.defaultTask.getTaskId());
			return DataApplication.defaultTask;
		}else {
			if (task != null) {
				DataApplication.defaultTask.setStartTime(task.getEndTime());
				//这句语句有待斟酌
				DataApplication.defaultTask.setDayofWeek(task.getDayofWeek());
				Log.v(TAG, "calculateNextTask : task: id="+task.getTaskId());
			}
			return task;
			
		}
		
	}
	
	/**
	 * 清除打盹模式
	 * @param context
	 * @param alarmTime
	 */
	private static void clearSnoozeIfNeeded(Context context, long alarmTime) {
        // 如果新任务和下一个snooze任务冲突，则清除snooze任务，启用新任务
        SharedPreferences prefs =
                context.getSharedPreferences(TaskService.PREFERENCES, 0);
        long snoozeTime = prefs.getLong(PREF_SNOOZE_TIME, 0);
        if (alarmTime < snoozeTime) {
            clearSnoozePreference(context, prefs);
        }
    }
	
	/**
	 * 清除打盹配置，同时清除它的通知用ID
	 * @param context
	 * @param prefs
	 */
	private static void clearSnoozePreference(final Context context,
			final SharedPreferences prefs) {
		//TODO这里的alarmId可能需要更改，改成TaskID，从数据库中读入
		final int alarmId = prefs.getInt(PREF_SNOOZE_ID, -1);
		if (alarmId != -1) {
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(alarmId);
		}

		final SharedPreferences.Editor ed = prefs.edit();
		ed.remove(PREF_SNOOZE_ID);
		ed.remove(PREF_SNOOZE_TIME);
		ed.commit();
	};
	
	/**
	 * 使定时任务生效
	 * @param context
	 * @param task
	 * @param atTimeInMillis
	 */
	private static void enableTask(Context context, final Task task, final long atTimeInMillis) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Log.v(TAG, "**setTask id "+task.getTaskId() + " atTime "+atTimeInMillis+"---"+DateUtil.getLongDate(atTimeInMillis));
		Log.v(TAG, "**nowTime : "+ System.currentTimeMillis()+"---"+DateUtil.getLongDate(System.currentTimeMillis()));
		//设置Action
		Intent intent = new Intent(TASK_ALERT_ACTION);
//		Intent intent = new Intent(DBManager.context, TaskReceiver.class);
		//把任务实体传过去
		Parcel out  = Parcel.obtain();
		task.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(TASK_RAW_DATA, out.marshall());
		//
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
		Log.v(TAG, "atTimeInMillis:"+atTimeInMillis);
		Log.v(TAG, "am.set()");
//		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+40000, sender);
//		am.setRepeating(AlarmManager.RTC_WAKEUP, atTimeInMillis, ONE_WEEK_TIME, sender);//这个每个星期重复的
		
//		//后来加的
//		context.sendBroadcast(intent);
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(atTimeInMillis);
		String timeString = formatDayAndTime(context, c);
		//TODO 保存定时执行的任务
		saveNextTask(context, timeString);
		Log.v(TAG, "enableTask\task.id:"+task.getTaskId());
		
	}
	
	/**
	 * 使定时任务失效
	 * @param context
	 */
	public static void disableTask(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TASK_ALERT_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
		saveNextTask(context, "");
	}
	
	/**
	 * 使定时任务生效
	 * @param context
	 * @param profileId
	 * @param repeatTimeMillis
	 */
	public static void enableWifiTask(Context context, int profileId, long repeatTimeMillis){
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(TASK_WIFI_ACTION);
		intent.putExtra("profileId", profileId);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTimeMillis, sender);//这个每个星期重复的
		
	}
	
	/**
	 * 使定时任务失效
	 * @param context
	 */
	public static void disableWifiTask(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TASK_WIFI_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
	
	/**
	 * 使定时任务生效
	 * @param context
	 * @param profileId
	 * @param repeatTimeMillis
	 */
	public static void enableGPSTask(Context context, int profileId, long repeatTimeMillis){
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(TASK_GPS_ACTION);
		intent.putExtra("profileId", profileId);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTimeMillis, sender);//这个每个星期重复的
	}
	
	/**
	 * 使定时任务失效
	 * @param context
	 */
	public static void disableGPSTask(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TASK_GPS_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
	
	/**
     * 保存下一个任务的时间到系统设置中，用的是formatted:String的形式，
     * 以便可以在需要的时候用到
     */
    static void saveNextTask(final Context context, String timeString) {
        Settings.System.putString(context.getContentResolver(),
                                  Settings.System.NEXT_ALARM_FORMATTED,
                                  timeString);
    }
	
    static String formatTime(final Context context, String time, int daysOfWeek) {
        Calendar c = calculateCalendar(time, daysOfWeek);
        return formatTime(context, c);
    }

    /* used by AlarmAlert */
    public static String formatTime(final Context context, Calendar c) {
        String format = get24HourMode(context) ? M24 : M12;
        return (c == null) ? "" : (String)DateFormat.format(format, c);
    }
	
	/**
     * 显天日期和时间 -- 用在锁屏界面上
     */
    private static String formatDayAndTime(final Context context, Calendar c) {
        String format = get24HourMode(context) ? DM24 : DM12;
        return (c == null) ? "" : (String)DateFormat.format(format, c);
    }
    
    /**
     * @return true 如果时间被改成24小时模式
     */
    static boolean get24HourMode(final Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }
	
	
	/**
	 * 获得该任务的下一次执行的毫秒级时间
	 * @param task 要执行的任务
	 * @return 毫秒级时间
	 */
	public static long claculateAlarmTime(Task task){
		if (task != null) {
			return calculateCalendar(task.getStartTime(), task.getDayofWeek()).getTimeInMillis();
		}else {
			return Long.MAX_VALUE;
		}
		
		
	}
	
	/**
	 * 获得下一次执行任务的日历
	 * @param time 任务执行的开始时间
	 * @param dayOfWeek 任务所在的星期几
	 * @return 日历
	 */
	private static Calendar calculateCalendar(String time, int dayOfWeek){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
//		Log.v(TAG, "-taskDayOfWeek:"+dayOfWeek+"-startTime:"+time);
		int taskDayOfWeek = dayOfWeek;
		int taskHour = DateUtil.parseHoursFromHHMMTime(time);
		int taskMinute = DateUtil.parseMinutesFromHHMMTime(time);
		Log.v(TAG, "-taskDayOfWeek:"+taskDayOfWeek+"-taskHour:"+taskHour+"-taskMinute:"+taskMinute);
		int nowDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMinute = c.get(Calendar.MINUTE);
        Log.v(TAG, "-nowDayOfWeek:"+nowDayOfWeek+"-nowHour:"+nowHour+"-nowMinute:"+nowMinute);
		if (taskDayOfWeek < nowDayOfWeek) {
			c.add(Calendar.DAY_OF_YEAR, 7);
			Log.v(TAG, "taskDayOfWeek < nowDayOfWeek 增加7天");
		} else if (taskDayOfWeek == nowDayOfWeek) {
			// 如果任务开始时间小于现在时间，就把时间推后7天
			if (taskHour < nowHour || 
					taskHour == nowHour && taskMinute <= nowMinute) {
				c.add(Calendar.DAY_OF_YEAR, 7);
				Log.v(TAG, "taskDayOfWeek == nowDayOfWeek 增加7天");
			}
		}
        
        //设置是星期几执行任务
        //TODO把dayOfWeek定义为Calendar.DAY_OF_WEEK的形式，即星期天为第一天，星期一为第二天
        c.set(Calendar.DAY_OF_WEEK, taskDayOfWeek);
        c.set(Calendar.HOUR_OF_DAY, taskHour);
        c.set(Calendar.MINUTE, taskMinute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
//        Log.v(TAG, "-Time():"+c.getTime());
//        Log.v(TAG, "-getTimeInMillis():"+c.getTimeInMillis());
//        Log.v(TAG, "-System.currentTimeMillis():"+System.currentTimeMillis());
//        Log.v(TAG, "-DAY_OF_WEEK:"+c.get(Calendar.DAY_OF_WEEK)+"-HOUR_OF_DAY:"+c.get(Calendar.HOUR_OF_DAY)+"-MINUTE:"+c.get(Calendar.MINUTE));
//        Log.v(TAG, "-Month:"+c.get(Calendar.MONTH)+"-Day:"+c.get(Calendar.DAY_OF_MONTH));
		return c;
	}
	
	
	

}
