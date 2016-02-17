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
	// ����һ��NotificationManager
	NotificationManager notificationManager;
	//����һ��ProfileTask������
	ProfileTaskStrategy profileTaskStrategy;
	
	public static final String PROFILE_TASk_SERVICE = "com.daocaowu.itelligentprofile.service.PROFILE_TASK_SERVICE";
	
	/**
	 * ѡ��������һ���龰ģʽ
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
		
		notificationManager.cancelAll();// �Ƴ�����֪ͨ
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
				//��ȡ�û���ǰ��Ĭ���龰ģʽ
	        	Profile defaultProfile = getDefaultProfile();
	        	if (defaultProfile == null) {
					defaultProfile = new Profile();
					Log.v(TAG, "defaultProfile == null");
				}
	        	SwitchProfileUtil.setProfile(context, defaultProfile);
	        	showNotification(defaultProfile.getProfileName(), "�����龰,Ϊ���ṩ���ʷ���");
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
				showNotification(newProfile.getProfileName(), "�����龰,Ϊ���ṩ���ʷ���");
				Intent intent1 = new Intent(ProfileTaskService.this,ProfileTaskReceiver.class);
	        	sendBroadcast(intent1);
				break;

			default:
//				//��ȡ�û���ǰ��Ĭ���龰ģʽ
//	        	Profile defaultProfileCase = getDefaultProfile();
//	        	SwitchProfileUtil.setProfile(context, defaultProfileCase);
//	        	showNotification(defaultProfileCase.getProfileName(), "�����龰,Ϊ���ṩ���ʷ���");
//	        	Intent intentdDefault = new Intent(ProfileTaskService.this,ProfileTaskReceiver.class);
//	        	sendBroadcast(intentdDefault);
				break;
			}
		}
		
		/**
		 * �����Ƿ�ִ�в���
		 * @param isRegisited
		 */
		private void doStrategy(boolean isRegisited){
			if (getContext() == null) {
				setContext(DBManager.context);
			}
	          
	        if(isRegisited){
	        	// ��� AlarmManager��ʵ��   
	            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
	            // ����һ�����ݹ㲥��ɫ��PendingIntent  
	            Intent intent = new Intent(DBManager.context,ProfileTaskReceiver.class);//�Զ����Action�����Զ���Ĺ㲥�߽���
	            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ProfileTaskReceiver.REQUEST_CODE, intent, 0); 
	        	//��ȡ�����һ������
	        	Task lastTask = new Task();
	        	
	        	if (lastTask != null) {
	        		//��������Profile������һ��Profile
	        		Profile lastProfile = ProfileService.getProfileByTask(lastTask);
	        		//�����ǰʱ����Taskʱ��μ䣬����Task
					if (DateUtil.getmillisecond(lastTask.getStartTime())>= System.currentTimeMillis() && DateUtil.getmillisecond(lastTask.getStartTime()) < System.currentTimeMillis()) {
						//����һ����ʱ����
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, DateUtil.getmillisecond(lastProfile.getStartTime()), pendingIntent); 
					}else {
						//����һ����ʱ����
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent); 
					}
	        		
	        		//����Ϊ��ǰ��Task
	        		DataApplication.currentTaskId = lastTask.getTaskId();
	        		//������ݵ�ǰTaskId������Profile
	        		switchProfile = 1;
	        		
				}else {//�Ҳ�����һ��Task��
					if (curProfileEndTime != null && !"".equals(curProfileEndTime)) {
						//����һ����ʱ����
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, DateUtil.getmillisecond(curProfileEndTime), pendingIntent);
					}else {
						//����һ����ʱ����
		        		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
		        		//���õ���û������
		        		DataApplication.currentTaskId = 0;
					}
	        		if (DataApplication.currentTaskId == 0) {
	        			switchProfile = -1;
					}else {
						//���������û�Ĭ���龰ģʽ
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
		 * ���Ĭ���龰ģʽ
		 */
		private Profile getDefaultProfile(){
			return ProfileService.getDefaultProfile();
		}
		
		/**
		 * ��ȡ���������ڼ�
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
     * ��״̬����ʾ֪ͨ
     */
    private void showNotification(CharSequence contentTitle, CharSequence contentText ){
         
        // ����Notification�ĸ�������   
        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "�����龰";
        notification.when = System.currentTimeMillis();
        
        //FLAG_AUTO_CANCEL   ��֪ͨ�ܱ�״̬���������ť�������
        //FLAG_NO_CLEAR      ��֪ͨ���ܱ�״̬���������ť�������
        //FLAG_ONGOING_EVENT ֪ͨ��������������
        //FLAG_INSISTENT     �Ƿ�һֱ���У���������һֱ���ţ�֪���û���Ӧ
//        notification.flags |= Notification.FLAG_ONGOING_EVENT; // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����   
//        notification.flags |= Notification.FLAG_NO_CLEAR; // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��   
//        notification.flags |= Notification.FLAG_SHOW_LIGHTS;   
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // ��֪ͨ�ܱ�״̬���������ť�������   
        notification.flags |= Notification.FLAG_SHOW_LIGHTS; 
        
        //DEFAULT_ALL     ʹ������Ĭ��ֵ�������������𶯣������ȵ�
        //DEFAULT_LIGHTS  ʹ��Ĭ��������ʾ
        //DEFAULT_SOUNDS  ʹ��Ĭ����ʾ����
        //DEFAULT_VIBRATE ʹ��Ĭ���ֻ��𶯣������<uses-permission android:name="android.permission.VIBRATE" />Ȩ��
        notification.defaults = Notification.DEFAULT_LIGHTS; 
        //����Ч������
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;   
        notification.ledOnMS =5000; //����ʱ�䣬����
        
        // ����֪ͨ���¼���Ϣ   
//        CharSequence contentTitle ="�����龰����"; // ֪ͨ������   
//        CharSequence contentText ="�����龰����"; // ֪ͨ������   
        Intent notificationIntent =new Intent(DBManager.context, ProfileTaskService.class); // �����֪ͨ��Ҫ��ת��Activity   
        PendingIntent contentItent = PendingIntent.getActivity(this, 0, notificationIntent, 0);   
        notification.setLatestEventInfo(DBManager.context, contentTitle, contentText, contentItent);
         
        // ��Notification���ݸ�NotificationManager   
        notificationManager.notify(0, notification);
//        notificationManager.notify();
    }
    
    //ɾ��֪ͨ    
    private void clearNotification(){
        // ������ɾ��֮ǰ���Ƕ����֪ͨ   
        NotificationManager notificationManager = (NotificationManager) this
        		.getSystemService(NOTIFICATION_SERVICE);   
        notificationManager.cancel(0);  
 
    }
    
   

}
