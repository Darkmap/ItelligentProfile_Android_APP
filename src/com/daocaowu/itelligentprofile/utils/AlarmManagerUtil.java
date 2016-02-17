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
	 * ָ��ʱ������ִ��(�������ӵ�����) ע��: Receiver�ǵ���manifest.xml��ע��
	 * 
	 * @param context
	 */
	public static void sendUpdateBroadcast(Context context) {
		Log.i("score", "send to start update broadcase,delay time :" + 60000);

		AlarmManager am = getAlarmManager(context);
		// 60��󽫲����㲥,����UpdateReceiver��ִ��,����������������ĸ������ݵĲ�����Ҫ����
		Intent i = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
		am.set(AlarmManager.RTC, System.currentTimeMillis() + 60000,
				pendingIntent);
	}

	/**
	 * ȡ����ʱִ��(�������ӵ�ȡ��)
	 * 
	 * @param context
	 */
	public static void cancelUpdateBroadcast1(Context context) {
		AlarmManager am = getAlarmManager(context);
		Intent i = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
		am.cancel(pendingIntent);
	}

	// ��2�������Ե�ִ��ĳ�����
	public static void sendUpdateBroadcastRepeat(Context context) {
		Intent intent = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);

		// ��ʼʱ��
		long firstime = SystemClock.elapsedRealtime();

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// 60��һ�����ڣ���ͣ�ķ��͹㲥
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,
				60 * 1000, pendingIntent);
	}

	// ȡ����ʱ��(����)
	/**
	 * ȡ����ʱִ��(�������ӵ�ȡ��)
	 * 
	 * @param context
	 */
	public static void cancelUpdateBroadcast2(Context context) {
		AlarmManager am = getAlarmManager(context);
		// ȡ��ʱע��UpdateReceiver.class����������ʱһ��,������Ҫ��ȷȡ��
		Intent i = new Intent(context, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
		am.cancel(pendingIntent);
	}
}
