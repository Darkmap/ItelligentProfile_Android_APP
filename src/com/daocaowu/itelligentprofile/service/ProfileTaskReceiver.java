package com.daocaowu.itelligentprofile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/*提醒用户当前已经设置的背景*/
public class ProfileTaskReceiver extends BroadcastReceiver {

	private static final String TAG = "ProfileBroadcastReceiver";
	private static final String TASK_TYPE = "TaskType";
	private static final String PROFILE = "Profile";
	
	public static final String PROFILE_TASk_RECEIVER = "com.daocaowu.itelligentprofile.service.PROFILE_TASK_RECEIVER";
	
	public static final int REQUEST_CODE = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
//		if(intent != null){  
//            Bundle bundle = intent.getExtras();  
//            Intent it = new Intent(context, ProfileTaskService.class);    // call service for MusicReceiverService.class  
//            it.putExtras(bundle);  
//            if(bundle != null){  
//                String op = bundle.getString(TASK_TYPE); 
//                //从数据库中读取ID要处理的任务ID和情景模式ID
//                if(!PROFILE.equals(op)){  
//                    context.stopService(it);        // stopService  
//                }else{  
//                    context.startService(it);       // startService  
//                }  
//            }  
//        }  
//		
		/* start another activity - MyAlarm to display the alarm */  
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//        intent.setClass(context, ProfileTaskService.class); 
//        context.startService(intent);
//		Log.v(TAG+"-onReceive",	"onReceive");
//		//跳到通知通知栏的Activity
//		Intent setIntent = new Intent(context, null);
//		
//		Bundle bundle = new Bundle();
//		bundle.putString(SET_PROFILE, "1");
//		setIntent.putExtras(bundle);
//		setIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(setIntent);

	}

}
