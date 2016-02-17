package com.daocaowu.itelligentprofile.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.daocaowu.itelligentprofile.service.UpdateReceiver;

public class AlarmManagerUtil {

	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * 指定时间后进行执行(有如闹钟的设置) 注意: Receiver记得在manifest.xml中注册
	 * 
	 * @param context
	 */
	public static void sendUpdateBroadcast(Context context) {
		Log.i("score", "send to start update broadcase,delay time :" + 60000);

		AlarmManager am = getAlarmManager(context);
		// 60秒后将产生广播,触发UpdateReceiver的执行,这个方法才是真正的更新数据的操作主要代码
		Intent i = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
		am.set(AlarmManager.RTC, System.currentTimeMillis() + 60000,
				pendingIntent);
	}

	/**
	 * 取消定时执行(有如闹钟的取消)
	 * 
	 * @param context
	 */
	public static void cancelUpdateBroadcast1(Context context) {
		AlarmManager am = getAlarmManager(context);
		Intent i = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
		am.cancel(pendingIntent);
	}

	// （2）周期性的执行某项操作
	public static void sendUpdateBroadcastRepeat(Context context) {
		Intent intent = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);

		// 开始时间
		long firstime = SystemClock.elapsedRealtime();

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// 60秒一个周期，不停的发送广播
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
				60 * 1000, pendingIntent);
	}

	// 取消定时器(闹钟)
	/**
	 * 取消定时执行(有如闹钟的取消)
	 * 
	 * @param context
	 */
	public static void cancelUpdateBroadcast2(Context context) {
		AlarmManager am = getAlarmManager(context);
		// 取消时注意UpdateReceiver.class必须与设置时一致,这样才要正确取消
		Intent i = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
		am.cancel(pendingIntent);
	}
}
