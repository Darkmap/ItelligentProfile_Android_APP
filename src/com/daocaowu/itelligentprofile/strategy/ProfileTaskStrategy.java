package com.daocaowu.itelligentprofile.strategy;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.service.ProfileTaskReceiver;

public class ProfileTaskStrategy implements IStrategy {

	public static Profile defaultProfile;
	private Context context;
	
	public ProfileTaskStrategy(Context context) {
		this.context = context;
	}
	
	
	
	@Override
	public void execute() {
		

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
	
	
	/**
	 * 设置是否执行策略
	 * @param isRegisited
	 */
	private void doStrategy(boolean isRegisited){
		 
          
        if(isRegisited){  
        	// get the AlarmManager instance   
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
            // create a PendingIntent that will perform a broadcast  
            Intent intent = new Intent(ProfileTaskReceiver.PROFILE_TASk_RECEIVER);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ProfileTaskReceiver.REQUEST_CODE, intent, 0); 
        	
            int dayOfWeek = getDayOfWeek();
        	Profile defaultProfile = getDefaultProfile();
//        	
//        	long currentTime = System.currentTimeMillis();
//        	Profile lastProfile = ProfileService.getLastProfile(DateUtil.getHHmmString(), dayOfWeek);
//        	if (lastProfile != null) {
//        		SwitchProfileUtil.setProfile(context, lastProfile);
//        		// schedule an alarm
//            	alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime+10000, pendingIntent); 
//			}else {
//				
//			}
        	
             
        }  
        else{  
        	
        	// get the AlarmManager instance   
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
            // create a PendingIntent that will perform a broadcast  
            Intent intent = new Intent(ProfileTaskReceiver.PROFILE_TASk_RECEIVER);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ProfileTaskReceiver.REQUEST_CODE, intent, 0); 
        	
            int dayOfWeek = getDayOfWeek();
        	Profile defaultProfile = getDefaultProfile();
//        	
//        	long currentTime = System.currentTimeMillis();
//        	Profile lastProfile = ProfileService.getLastProfile(DateUtil.getHHmmString(), dayOfWeek);
//        	if (lastProfile != null) {
//        		SwitchProfileUtil.setProfile(context, lastProfile);
//        		// schedule an alarm
//            	alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime+10000, pendingIntent); 
//			}else {
//				
//			}
            // cancel current alarm  
//        	alarmManager.cancel(pendingIntent);
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

}
