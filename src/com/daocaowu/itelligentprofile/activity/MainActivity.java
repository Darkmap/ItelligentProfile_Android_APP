package com.daocaowu.itelligentprofile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.service.LocationService;
import com.daocaowu.itelligentprofile.service.TaskReceiverService;
import com.daocaowu.itelligentprofile.service.TaskService;

public class MainActivity extends BaseActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static long DALEYMILLIS = 1500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DBManager.openDB(getApplicationContext());
		TaskService.setNextAlert(getApplicationContext());
		LocationService.initLocation(getApplicationContext(), 3);
		//启动电池监控服务  
//	    startService(new Intent(this, MonitorService.class));
	    startService(new Intent(this, TaskReceiverService.class));
//		initTask();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				toMainpad();
			}
		}, DALEYMILLIS);

	}

	private void toMainpad() {
		Intent intent = new Intent(getApplicationContext(), MainPad.class);
		startActivity(intent);
		MainActivity.this.finish();
		overridePendingTransition(R.anim.myanim_in, R.anim.myanim_out);
	}

}
