package com.daocaowu.itelligentprofile.service;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.strategy.IStrategy;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.DateUtil;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;

public class ProfileTaskService extends Service {

	private static final String TAG = "ProfileTaskService";
	private MyBinder mBinder;
	// 创建一个NotificationManager
	NotificationManager notificationManager;
	//创建一个ProfileTask策略者
	ProfileTaskStrategy profileTaskStrategy;
	
	public static final String PROFILE_TASk_SERVICE = "com.daocaowu.itelligentprofile.service.PROFILE_TASK_SERVICE";
	
	/**
	 * 选择设置哪一种情景模式
	 * */
	private static int switchProfile;
	
	private static String curProfileEndTime;
	
	private int intCounter = 0;

	private Handler mHandler = new Handler();
	
	private Runnable mTasks = new Runnable(){
		@Override
		public void run() {
			intCounter++;
			
			if (intCounter == 10) {
				if (profileTaskStrategy.context != null) {
	        		Log.v(TAG, profileTaskStrategy.context.toString());
				}else {
					Log.v(TAG, "profileTaskStrategy.context==null");
				}
//				profileTaskStrategy.execute();
				Log.v(TAG+"-intCounter", ""+intCounter);
				intCounter = 0;
				Log.v(TAG+"-intCounter", ""+intCounter);
			}
//			mHandler.postDelayed(mTasks, 1000);
		}
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		if (mBinder == null) {
			mBinder = new MyBinder();
		}
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		Log.v(TAG, "start onCreate~~~");
		super.onCreate();
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		profileTaskStrategy = new ProfileTaskStrategy();
		profileTaskStrategy.setContext(DBManager.context);
		if (profileTaskStrategy.context != null) {
    		Log.v(TAG, profileTaskStrategy.context.toString());
		}else {
			Log.v(TAG, "profileTaskStrategy.context==null");
		}
//		profileTaskStrategy.execute();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.v(TAG, "start onStart~~~");
		super.onStart(intent, startId);
//		mHandler.postDelayed(mTasks, 1000);
		if (profileTaskStrategy.context != null) {
    		Log.v(TAG, profileTaskStrategy.context.toString());
		}else {
			Log.v(TAG, "profileTaskStrategy.context==null");
		}
//		profileTaskStrategy.execute();
//		showNotification();
//		profileTaskStrategy.execute();
	}
	
	@Override
	public void onDestroy() {
		Log.v(TAG, "start onDestroy~~~");
		super.onDestroy();
		
		notificationManager.cancelAll();// 移出所有通知
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.v(TAG, "start onUnbind~~~");
		return super.onUnbind(intent);
	}
	
	public class MyBinder extends Binder {
		public ProfileTaskService getService() {
			return ProfileTaskService.this;
		}
	}
	
	public void setProfileTaskStrategy(ProfileTaskStrategy profileTaskStrategy) {
		this.profileTaskStrategy = profileTaskStrategy;
	}
	
	private class ProfileTaskStrategy implements IStrategy {

		private Context context;
		
		@Override
		public void execute() {
			doStrategy(true);
			setProfile(switchProfile);
		}

		@Override
		public void doAysnTask() {
			new Thread(){
				@Override
				public void run() {
					execute();
				};
			}.start();

		}
		
		private void setProfile(int which){
			switch (which) {
			case 0:
				//获取用户当前的默认情景模式
	        	Profile defaultProfile = getDefaultProfile();
	        	if (defaultProfile == null) {
					defaultProfile = new Profile();
					Log.v(TAG, "defaultProfile == null");
				}
	        	SwitchProfileUtil.setProfile(context, defaultProfile);
	        	showNotification(defaultProfile.getProfileName(), "智能情景,为您提供优质服务");
	        	Intent intent0 = new Intent(ProfileTaskService.this,ProfileTaskReceiver.class);
	        	sendBroadcast(intent0);
				break;
			case 1:
				AbstractLocalService localService = new TaskService(ProfileTaskService.this);
				Task task = (Task) localService.check(DataApplication.currentTaskId);
				Profile newProfile = null;
				if (task != null) {
					newProfile = ProfileService.getProfileByTask(task);
					curProfileEndTime = task.getEndTime();
				}else {
					newProfile = getDefaultProfile();
				}
				SwitchProfileUtil.setProfile(context, newProfile);
				showNotification(newProfile.getProfileName(), "智能情景,为您提供优质服务");
				Intent intent1 = new Intent(ProfileTaskService.this,ProfileTaskReceiver.class);
	        	sendBroadcast(intent1);
				break;

			default:
//				//获取用户当前的默认情景模式
//	        	Profile defaultProfileCase = getDefaultProfile();
//	        	SwitchProfileUtil.setProfile(context, defaultProfileCase);
//	        	showNotification(defaultProfileCase.getProfileName(), "智能情景,为您提供优质服务");
//	        	Intent intentdDefault = new Intent(ProfileTaskService.this,ProfileTaskReceiver.class);
//	        	sendBroadcast(intentdDefault);
				break;
			}
		}
		
		/**
		 * 设置是否执行策略
		 * @param isRegisited
		 */
		private void doStrategy(boolean isRegisited){
			if (getContext() == null) {
				setContext(DBManager.context);
			}
	          
	        if(isRegisited){
	        	// 获得 AlarmManager的实例   
	            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
	            // 创建一个办演广播角色的PendingIntent  
	            Intent intent = new Intent(DBManager.context,ProfileTaskReceiver.class);//自定义的Action，由自定义的广播者接收
	            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ProfileTaskReceiver.REQUEST_CODE, intent, 0); 
	        	//获取最近的一个任务
	        	Task lastTask = new Task();
	        	
	        	if (lastTask != null) {
	        		//获得最近的Profile，即下一个Profile
	        		Profile lastProfile = ProfileService.getProfileByTask(lastTask);
	        		//如果当前时间在Task时间段间，返回Task
					if (DateUtil.getmillisecond(lastTask.getStartTime())>= System.currentTimeMillis() && DateUtil.getmillisecond(lastTask.getStartTime()) < System.currentTimeMillis()) {
						//设置一个定时任务
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, DateUtil.getmillisecond(lastProfile.getStartTime()), pendingIntent); 
					}else {
						//设置一个即时任务
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent); 
					}
	        		
	        		//设置为当前的Task
	        		DataApplication.currentTaskId = lastTask.getTaskId();
	        		//将会根据当前TaskId来设置Profile
	        		switchProfile = 1;
	        		
				}else {//找不到下一个Task了
					if (curProfileEndTime != null && !"".equals(curProfileEndTime)) {
						//设置一个定时任务
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, DateUtil.getmillisecond(curProfileEndTime), pendingIntent);
					}else {
						//设置一个即时任务
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
		        		//设置当天没有任务
		        		DataApplication.currentTaskId = 0;
					}
	        		if (DataApplication.currentTaskId == 0) {
	        			switchProfile = -1;
					}else {
						//将会设置用户默认情景模式
		        		switchProfile = 0;
					}
	        		
				}
	        	
	        }  
	        else{  
	            // cancel current alarm  
//	        	alarmManager.cancel(pendingIntent);
	        }
	        
	        
		}
		
		/**
		 * 获得默认情景模式
		 */
		private Profile getDefaultProfile(){
			return ProfileService.getDefaultProfile();
		}
		
		/**
		 * 获取当天是星期几
		 * @return
		 */
		private int getDayOfWeek(){
			Calendar calendar = Calendar.getInstance();
			return calendar.get(Calendar.DAY_OF_WEEK)-1;
		}

		public void setContext(Context context) {
			this.context = context;
		}
		
		public Context getContext() {
			return context;
		}
	}
	
	/**
     * 在状态栏显示通知
     */
    private void showNotification(CharSequence contentTitle, CharSequence contentText ){
         
        // 定义Notification的各种属性   
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "智能情景";
        notification.when = System.currentTimeMillis();
        
        //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
        //FLAG_ONGOING_EVENT 通知放置在正在运行
        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
//        notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中   
//        notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用   
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 该通知能被状态栏的清除按钮给清除掉   
        notification.flags |= Notification.FLAG_SHOW_LIGHTS; 
        
        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
        //DEFAULT_LIGHTS  使用默认闪光提示
        //DEFAULT_SOUNDS  使用默认提示声音
        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
        notification.defaults = Notification.DEFAULT_LIGHTS; 
        //叠加效果常量
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;   
        notification.ledOnMS =5000; //闪光时间，毫秒
        
        // 设置通知的事件消息   
//        CharSequence contentTitle ="智能情景标题"; // 通知栏标题   
//        CharSequence contentText ="智能情景内容"; // 通知栏内容   
        Intent notificationIntent =new Intent(DBManager.context, ProfileTaskService.class); // 点击该通知后要跳转的Activity   
        PendingIntent contentItent = PendingIntent.getActivity(this, 0, notificationIntent, 0);   
        notification.setLatestEventInfo(DBManager.context, contentTitle, contentText, contentItent);
         
        // 把Notification传递给NotificationManager   
        notificationManager.notify(0, notification);
//        notificationManager.notify();
    }
    
    //删除通知    
    private void clearNotification(){
        // 启动后删除之前我们定义的通知   
        NotificationManager notificationManager = (NotificationManager) this
        		.getSystemService(NOTIFICATION_SERVICE);   
        notificationManager.cancel(0);  
 
    }
    
   

}
