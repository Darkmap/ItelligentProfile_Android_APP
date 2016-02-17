package com.daocaowu.itelligentprofile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainActivity;

public class MonitorService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		boolean isNotified = false;
		if (isNotified) {
			// 定义电池电量更新广播的过滤器,只接受带有ACTION_BATTERRY_CHANGED事件的Intent
			IntentFilter batteryChangedReceiverFilter = new IntentFilter();
			batteryChangedReceiverFilter
					.addAction(Intent.ACTION_BATTERY_CHANGED);

			// 向系统注册batteryChangedReceiver接收器，本接收器的实现见代码字段处
			registerReceiver(batteryChangedReceiver, batteryChangedReceiverFilter);

			// 实例化Notification通知的管理器，即字段notification manager
			notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			// 由于初始化本服务时系统可能没有发出ACTION_BATTERY_CHANGED广播，那么刚才注册的那个接收器将不会在本服务启动时被激活，这种情况下就无法显示当前电量，因此在这里添加一个匿名广播接收器。
			new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					int level = intent.getIntExtra("level", 0); // 电池电量等级
					int scale = intent.getIntExtra("scale", 100); // 电池满时百分比
					int status = intent.getIntExtra("status", 0); // 电池状态

					// 若正在充电
					if (status == BatteryManager.BATTERY_STATUS_CHARGING)
						notification = getNotification(getChargingIcon(level
								* 100 / scale), "电池监控",
								System.currentTimeMillis(), "智能情景模式", "正在充电");
					else
						notification = getNotification(getStateIcon(level * 100
								/ scale), "电池监控", System.currentTimeMillis(),
								"智能情景模式", "");

					// 向系统发送此通知，为方便起见，通知ID简单设为0
					notifyManager.notify(1, notification);

				}
			};
		}else {
			onDestroy();
		}
	}

	@Override
	public void onDestroy() {
		// 当本服务终止时，通过通知ID注销左上角的通知
		if (notifyManager != null) {
			notifyManager.cancel(1);
		}
		super.onDestroy();
	}

	// --------------------------------------私有方法------------------------------
	// 获得一个notification
	private Notification getNotification(int notificationIcon,
			String tickerText, long when, String contentTitle,
			String contentText) {
		// 定义该notification的扩展文本和intent
		Notification notification = new Notification(notificationIcon,
				tickerText, when);
		Context context = getApplicationContext();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// 设置本状态出现在“正在进行”而非“通知”栏目，不允许用户清除状态图标
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		return notification;
	}

	// 获得对应的电池状态图标
	private int getStateIcon(int batteryHealth) {
		if (batteryHealth >= 0 || batteryHealth <= 100)
			return batteryStateIcons[batteryHealth]; // 这里是为了方便起见而硬编码的资源数组，直接从R.java文件中提取的十六进制资源ID
		return R.drawable.stat_sys_battery_unknown;
	}

	// 获得对应的充电状态图标
	private int getChargingIcon(int batteryHealth) {
		if (batteryHealth >= 0 && batteryHealth < 5)
			return R.drawable.stat_sys_battery_charge_anim0;
		if (batteryHealth >= 5 && batteryHealth < 10)
			return R.drawable.stat_sys_battery_charge_anim01;
		if (batteryHealth >= 10 && batteryHealth < 15)
			return R.drawable.stat_sys_battery_charge_anim02;
		if (batteryHealth >= 15 && batteryHealth < 20)
			return R.drawable.stat_sys_battery_charge_anim03;
		if (batteryHealth >= 20 && batteryHealth < 25)
			return R.drawable.stat_sys_battery_charge_anim04;
		if (batteryHealth >= 25 && batteryHealth < 30)
			return R.drawable.stat_sys_battery_charge_anim05;
		if (batteryHealth >= 30 && batteryHealth < 35)
			return R.drawable.stat_sys_battery_charge_anim06;
		if (batteryHealth >= 35 && batteryHealth < 40)
			return R.drawable.stat_sys_battery_charge_anim07;
		if (batteryHealth >= 40 && batteryHealth < 45)
			return R.drawable.stat_sys_battery_charge_anim08;
		if (batteryHealth >= 45 && batteryHealth < 50)
			return R.drawable.stat_sys_battery_charge_anim09;
		if (batteryHealth >= 50 && batteryHealth < 55)
			return R.drawable.stat_sys_battery_charge_anim10;
		if (batteryHealth >= 55 && batteryHealth < 60)
			return R.drawable.stat_sys_battery_charge_anim11;
		if (batteryHealth >= 60 && batteryHealth < 65)
			return R.drawable.stat_sys_battery_charge_anim12;
		if (batteryHealth >= 65 && batteryHealth < 70)
			return R.drawable.stat_sys_battery_charge_anim13;
		if (batteryHealth >= 70 && batteryHealth < 75)
			return R.drawable.stat_sys_battery_charge_anim14;
		if (batteryHealth >= 75 && batteryHealth < 80)
			return R.drawable.stat_sys_battery_charge_anim15;
		if (batteryHealth >= 80 && batteryHealth < 85)
			return R.drawable.stat_sys_battery_charge_anim16;
		if (batteryHealth >= 85 && batteryHealth < 90)
			return R.drawable.stat_sys_battery_charge_anim17;
		if (batteryHealth >= 90 && batteryHealth < 95)
			return R.drawable.stat_sys_battery_charge_anim18;
		if (batteryHealth >= 95 && batteryHealth < 100)
			return R.drawable.stat_sys_battery_charge_anim19;
		if (batteryHealth == 100)
			return R.drawable.stat_sys_battery_charge_animfull;
		return R.drawable.stat_sys_battery_unknown;
	}

	// -------------------------------私有字段--------------------------------------
	private NotificationManager notifyManager = null;
	private Notification notification = null;

	// 这里是为了方便起见而硬编码的icon资源数组，直接从R.java文件中提取的十六进制资源ID，本数组的0号元素0x7f020002表示资源stat_sys_battery_0.png（见附件），以后累加。不同配置的ID可能不同，请读者自行修改。
	private int batteryStateIcons[] = { 0x7f020070, 0x7f020071, 0x7f020072,
			0x7f020073, 0x7f020074, 0x7f020075, 0x7f020076, 0x7f020077,
			0x7f020078, 0x7f020079, 0x7f02007a, 0x7f02007b, 0x7f02007c,
			0x7f02007d, 0x7f02007e, 0x7f02007f, 0x7f020080, 0x7f020081,
			0x7f020082, 0x7f020083, 0x7f020084, 0x7f020085, 0x7f020086,
			0x7f020087, 0x7f020088, 0x7f020089, 0x7f02008a, 0x7f02008b,
			0x7f02008c, 0x7f02008d, 0x7f02008e, 0x7f02008f, 0x7f020090,
			0x7f020091, 0x7f020092, 0x7f020093, 0x7f020094, 0x7f020095,
			0x7f020096, 0x7f020097, 0x7f020098, 0x7f020099, 0x7f02009a,
			0x7f02009b, 0x7f02009c, 0x7f02009d, 0x7f02009e, 0x7f02009f,
			0x7f0200a0, 0x7f0200a1, 0x7f0200a2, 0x7f0200a3, 0x7f0200a4,
			0x7f0200a5, 0x7f0200a6, 0x7f0200a7, 0x7f0200a8, 0x7f0200a9,
			0x7f0200aa, 0x7f0200ab, 0x7f0200ac, 0x7f0200ad, 0x7f0200ae,
			0x7f0200af, 0x7f0200b0, 0x7f0200b1, 0x7f0200b2, 0x7f0200b3,
			0x7f0200b4, 0x7f0200b5, 0x7f0200b6, 0x7f0200b7, 0x7f0200b8,
			0x7f0200b9, 0x7f0200ba, 0x7f0200bb, 0x7f0200bc, 0x7f0200bd,
			0x7f0200be, 0x7f0200bf, 0x7f0200c0, 0x7f0200c1, 0x7f0200c2,
			0x7f0200c3, 0x7f0200c4, 0x7f0200c5, 0x7f0200c6, 0x7f0200c7,
			0x7f0200c8, 0x7f0200c9, 0x7f0200ca, 0x7f0200cb, 0x7f0200cc,
			0x7f0200cd, 0x7f0200ce, 0x7f0200cf, 0x7f0200d0, 0x7f0200d1,
			0x7f0200d2, 0x7f0200e8 };

	// 接受电池信息更新的广播
	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 100);
			int status = intent.getIntExtra("status", 0);

			// 若正在充电
			if (status == BatteryManager.BATTERY_STATUS_CHARGING)
				notification = getNotification(getChargingIcon(level * 100
						/ scale), "Battery Monitor",
						System.currentTimeMillis(), "智能情景模式", "正在充电");
			else
				notification = getNotification(
						getStateIcon(level * 100 / scale), "Battery Monitor",
						System.currentTimeMillis(), "智能情景模式", "");
			notifyManager.notify(1, notification);
		}
	};
}
