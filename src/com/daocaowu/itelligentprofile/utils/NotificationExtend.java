package com.daocaowu.itelligentprofile.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import com.daocaowu.itelligentprofile.R;

public class NotificationExtend {

	private Activity context;

	public NotificationExtend(Activity context) {
		this.context = context;

	}

	// ��ʾNotification

	public void showNotification() {

		// ����һ��NotificationManager������
		NotificationManager notificationManager = (
		NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		// ����Notification�ĸ�������

		Notification notification = new Notification(

		R.drawable.icon, "�����龰",

		System.currentTimeMillis());

		// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����

		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�Զ������

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		notification.defaults = Notification.DEFAULT_LIGHTS;

		notification.ledARGB = Color.BLUE;

		notification.ledOnMS = 5000;

		// ����֪ͨ���¼���Ϣ

		CharSequence contentTitle = "�龰ģʽ��ʾ��Ϣ"; // ֪ͨ������

		CharSequence contentText = "��鿴����"; // ֪ͨ������

		Intent notificationIntent = new Intent(context, context.getClass());

		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		// ��Notification���ݸ�NotificationManager
		notificationManager.notify(0, notification);

	}

	// ȡ��֪ͨ
	public void cancelNotification() {

		NotificationManager notificationManager = (

		NotificationManager) context.getSystemService(

		android.content.Context.NOTIFICATION_SERVICE);

		notificationManager.cancel(0);

	}

}
