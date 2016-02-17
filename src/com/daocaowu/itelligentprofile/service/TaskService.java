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
	
    public static final int TASK_STATUSBAR_CANCEL = 0;//ȡ��
	public static final int TASK_STATUSBAR_NOTIFY = 1;//֪ͨ
	public static final int TASK_STATUSBAR_INVARIANT = 2;//����
	
	public static final long ONE_WEEK_TIME = 7*24*60*60*1000;
	
	/**
	 * ���Action������������AlarmReceiverҲ�������������¼�
	 * �������� ��Ӧ������alarm Manager��Alarm Broadcasts
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
	 * ���String������������Intent�д���һ������ʵ���õ�
	 */
	public static final String TASK_INTENT_EXTRA = "intent.extra.task";
	
	public static final String TASK_RAW_DATA = "intent.extra.task_raw";
	/**
	 * ���String������������Intent�д���һ������ID�õ�
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
     * ��һ��������ӽ������ݿ⣬ͬʱ����һ������ʱ��
     */
	@Override
	public int add(Object object) {
//		((Task)object).setDayofWeek((((Task)object).getDayofWeek()+1)%7);
		int count1 = countTasks();//������ڵ��������
		//���Ҫ��������Ķ�ʱʱ��
		long timeInMillis = claculateAlarmTime((Task)object);
		//����Ҫ��������Ķ�ʱʱ��(Ҳ������ʱ��)
		((Task)object).setRemindTime(timeInMillis);
		// ��ñ��������ID�������޸ĵ������ID
		int insertTaskId = DBManager.insertTaskIntoDB((Task) object);
		//��ñ��������ĸ���
		int count2 = countTasks();
		
		Log.v(TAG, "intsetTaskId:"+insertTaskId+" -count1:"+count1+"-count2:"+count2);
		
		//TODO ������������䣬��Ҫ��ԭ���Ķ�ʱȡ�������������ö�ʱ���������������ˣ���ֱ�����ö�ʱ����
		
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
		int count1 = countTasks();//������ڵ��������
		//���Ҫ��������Ķ�ʱʱ��
		long timeInMillis = claculateAlarmTime((Task)object);
		//����Ҫ��������Ķ�ʱʱ��(Ҳ������ʱ��)
		((Task)object).setRemindTime(timeInMillis);
		// ��ñ��������ID�������޸ĵ������ID
		int insertTaskId = DBManager.insertTaskIntoDB((Task) object);
		//��ñ��������ĸ���
		int count2 = countTasks();
		
		Log.v(TAG, "intsetTaskId:"+insertTaskId+" -count1:"+count1+"-count2:"+count2);
		
		//TODO ������������䣬��Ҫ��ԭ���Ķ�ʱȡ�������������ö�ʱ���������������ˣ���ֱ�����ö�ʱ����
		
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
	 * ��ϵͳ������ʱ��ʱ���ʱ���仯��ʱ��������û��޸���������õ�ʱ�� ����
	 * ���С˯ģʽ�����ˣ��ͼ���������������������񣬼�����һ������
	 * @param context
	 */
	public static void setNextAlert(final Context context) {
		initDefaultTask();
		//���������ʱ�������Task
		Task task = calculateNextTask(context);
		if (task != null) {
			//��������
			enableTask(context, task, claculateAlarmTime(task));
//			TaskReceiverService.task = task;
		} else {
			//ȡ������
			disableTask(context);
		}
	}
	
	/**
	 * �ҳ���һ��������ĸ�Task
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
					
					//��cursor�����ÿ���ֶε�ֵ������ʼ��ʵ��
					Task t = DBManager.getTaskFromCursor(cursor);
//					Log.v(TAG+"-calculateNextTask","t.getRemindTime()-:"+ t.getRemindTime());
					long tTime = 0;
					tTime = claculateAlarmTime(t);
//					Log.v(TAG+"-calculateNextTask","tTime-:"+ tTime);
//					Log.v(TAG+"-calculateNextTask","nowTime-:"+ now);
					//TODO 
					t.setRemindTime(tTime);
					//Ҫ��õ���������ʱ�����������
					if (tTime < now) {
//						Log.v(TAG+"-calculateNextTask","tTime < now");
						continue;
					}
					
					//�ҳ�ʱ��������Ǹ�
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
		
		//������ѡ��DefaultTask�����ݿ��в������Task���ĸ���ΪNextTask���߼�
		//�Ƚ�DefaultTask��NextTask�Ŀ�ʼʱ��
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
				//�������д�����
				DataApplication.defaultTask.setDayofWeek(task.getDayofWeek());
				Log.v(TAG, "calculateNextTask : task: id="+task.getTaskId());
			}
			return task;
			
		}
		
	}
	
	/**
	 * �������ģʽ
	 * @param context
	 * @param alarmTime
	 */
	private static void clearSnoozeIfNeeded(Context context, long alarmTime) {
        // ������������һ��snooze�����ͻ�������snooze��������������
        SharedPreferences prefs =
                context.getSharedPreferences(TaskService.PREFERENCES, 0);
        long snoozeTime = prefs.getLong(PREF_SNOOZE_TIME, 0);
        if (alarmTime < snoozeTime) {
            clearSnoozePreference(context, prefs);
        }
    }
	
	/**
	 * ����������ã�ͬʱ�������֪ͨ��ID
	 * @param context
	 * @param prefs
	 */
	private static void clearSnoozePreference(final Context context,
			final SharedPreferences prefs) {
		//TODO�����alarmId������Ҫ���ģ��ĳ�TaskID�������ݿ��ж���
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
	 * ʹ��ʱ������Ч
	 * @param context
	 * @param task
	 * @param atTimeInMillis
	 */
	private static void enableTask(Context context, final Task task, final long atTimeInMillis) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Log.v(TAG, "**setTask id "+task.getTaskId() + " atTime "+atTimeInMillis+"---"+DateUtil.getLongDate(atTimeInMillis));
		Log.v(TAG, "**nowTime : "+ System.currentTimeMillis()+"---"+DateUtil.getLongDate(System.currentTimeMillis()));
		//����Action
		Intent intent = new Intent(TASK_ALERT_ACTION);
//		Intent intent = new Intent(DBManager.context, TaskReceiver.class);
		//������ʵ�崫��ȥ
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
//		am.setRepeating(AlarmManager.RTC_WAKEUP, atTimeInMillis, ONE_WEEK_TIME, sender);//���ÿ�������ظ���
		
//		//�����ӵ�
//		context.sendBroadcast(intent);
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(atTimeInMillis);
		String timeString = formatDayAndTime(context, c);
		//TODO ���涨ʱִ�е�����
		saveNextTask(context, timeString);
		Log.v(TAG, "enableTask\task.id:"+task.getTaskId());
		
	}
	
	/**
	 * ʹ��ʱ����ʧЧ
	 * @param context
	 */
	public static void disableTask(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TASK_ALERT_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
		saveNextTask(context, "");
	}
	
	/**
	 * ʹ��ʱ������Ч
	 * @param context
	 * @param profileId
	 * @param repeatTimeMillis
	 */
	public static void enableWifiTask(Context context, int profileId, long repeatTimeMillis){
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(TASK_WIFI_ACTION);
		intent.putExtra("profileId", profileId);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTimeMillis, sender);//���ÿ�������ظ���
		
	}
	
	/**
	 * ʹ��ʱ����ʧЧ
	 * @param context
	 */
	public static void disableWifiTask(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TASK_WIFI_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
	
	/**
	 * ʹ��ʱ������Ч
	 * @param context
	 * @param profileId
	 * @param repeatTimeMillis
	 */
	public static void enableGPSTask(Context context, int profileId, long repeatTimeMillis){
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(TASK_GPS_ACTION);
		intent.putExtra("profileId", profileId);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), repeatTimeMillis, sender);//���ÿ�������ظ���
	}
	
	/**
	 * ʹ��ʱ����ʧЧ
	 * @param context
	 */
	public static void disableGPSTask(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, new Intent(TASK_GPS_ACTION), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}
	
	/**
     * ������һ�������ʱ�䵽ϵͳ�����У��õ���formatted:String����ʽ��
     * �Ա��������Ҫ��ʱ���õ�
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
     * �������ں�ʱ�� -- ��������������
     */
    private static String formatDayAndTime(final Context context, Calendar c) {
        String format = get24HourMode(context) ? DM24 : DM12;
        return (c == null) ? "" : (String)DateFormat.format(format, c);
    }
    
    /**
     * @return true ���ʱ�䱻�ĳ�24Сʱģʽ
     */
    static boolean get24HourMode(final Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }
	
	
	/**
	 * ��ø��������һ��ִ�еĺ��뼶ʱ��
	 * @param task Ҫִ�е�����
	 * @return ���뼶ʱ��
	 */
	public static long claculateAlarmTime(Task task){
		if (task != null) {
			return calculateCalendar(task.getStartTime(), task.getDayofWeek()).getTimeInMillis();
		}else {
			return Long.MAX_VALUE;
		}
		
		
	}
	
	/**
	 * �����һ��ִ�����������
	 * @param time ����ִ�еĿ�ʼʱ��
	 * @param dayOfWeek �������ڵ����ڼ�
	 * @return ����
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
			Log.v(TAG, "taskDayOfWeek < nowDayOfWeek ����7��");
		} else if (taskDayOfWeek == nowDayOfWeek) {
			// �������ʼʱ��С������ʱ�䣬�Ͱ�ʱ���ƺ�7��
			if (taskHour < nowHour || 
					taskHour == nowHour && taskMinute <= nowMinute) {
				c.add(Calendar.DAY_OF_YEAR, 7);
				Log.v(TAG, "taskDayOfWeek == nowDayOfWeek ����7��");
			}
		}
        
        //���������ڼ�ִ������
        //TODO��dayOfWeek����ΪCalendar.DAY_OF_WEEK����ʽ����������Ϊ��һ�죬����һΪ�ڶ���
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
