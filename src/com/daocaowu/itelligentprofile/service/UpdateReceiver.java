package com.daocaowu.itelligentprofile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.utils.AlarmManagerUtil;

//�������ݿ�Ĺ㲥������
public class UpdateReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "���±ȷ�����", Toast.LENGTH_LONG).show();

		// ����ȫ�ֶ�ʱ��(����) 60����ٷ��㲥֪ͨ���㲥����������ִ��.
		// ���ַ�ʽ����JavaScript�е� setTimeout(xxx,60000)
		AlarmManagerUtil.sendUpdateBroadcast(context);
	}
}
