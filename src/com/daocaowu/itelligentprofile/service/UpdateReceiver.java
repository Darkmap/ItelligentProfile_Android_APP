package com.daocaowu.itelligentprofile.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.utils.AlarmManagerUtil;

//更新数据库的广播接收器
public class UpdateReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "更新比分数据", Toast.LENGTH_LONG).show();

		// 设置全局定时器(闹钟) 60秒后再发广播通知本广播接收器触发执行.
		// 这种方式很像JavaScript中的 setTimeout(xxx,60000)
		AlarmManagerUtil.sendUpdateBroadcast(context);
	}
}
