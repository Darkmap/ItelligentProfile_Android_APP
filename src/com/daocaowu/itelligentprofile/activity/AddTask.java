package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.service.TaskService;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.DateUtil;
import com.daocaowu.itelligentprofile.utils.ToastFactory;

public class AddTask extends BaseActivity {

	private EditText taskNameView;
	private EditText taskContentView;
	private TextView profileNameView;
	private TimePicker startTimePicker;
	private TimePicker endTimePicker;
	
	public static Task currentTask;
	
	
	//标记是否已修改
	private boolean changed = false;
	
	private String oldname="";
	private String oldcontent="";
	private String oldstart="";
	private String oldend="";
	
	private int action;
	

	/**
	 * 标定当前周几
	 */
	private int dayofweek;
	
	private static final String TAG = "AddTask";
	PendingIntent m_restartIntent;
	String RESTART_INTENT_KEY = "restart";
	int CRASHED_CODE = -1;
	private UncaughtExceptionHandler m_handler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.d(TAG, "uncaught exception is catched!");
			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
					m_restartIntent);
			System.exit(2);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setUnCatchException();
		
		Intent intent = getIntent();
		action = intent.getIntExtra("action", 0);
		dayofweek = intent.getIntExtra("dayofweek", 0);
		
		
		init();
	}
	
	private void setUnCatchException(){
		Intent intent = getIntent();
		int code = intent.getIntExtra(RESTART_INTENT_KEY, 0);
		Log.d(TAG, "code:"+code);
		if (CRASHED_CODE == code) {
			/** You can do something here. */
			Toast.makeText(getApplicationContext(), "数据异常，请重启应用", Toast.LENGTH_LONG);
			
			Log.d(TAG, "So sorry that the application crashed.");
		}
		intent.putExtra(RESTART_INTENT_KEY, CRASHED_CODE);
		m_restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Thread.setDefaultUncaughtExceptionHandler(m_handler);
	}

	private void init() {
		taskNameView = (EditText) findViewById(R.id.taskName);
		taskContentView = (EditText) findViewById(R.id.content);
		profileNameView = (TextView) findViewById(R.id.profileName);
		startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
		endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
		
		switch (action) {
		case 0:
			initAdd();
			break;
		case 1:
			initModify();
			break;
		default:
			break;
		}
	}

	private void initModify() {
		
		oldname = currentTask.getTaskName();
		taskNameView.setText(oldname);
		
		oldcontent = currentTask.getContent();
		taskContentView.setText(oldcontent);
		
		profileNameView.setText(currentTask.getProfileName());
		
		oldstart = currentTask.getStartTime();
		oldend = currentTask.getEndTime();
		String start[] = oldstart.split(":");
		String end[] = oldend.split(":");
		
		startTimePicker.setCurrentHour(Integer.valueOf(start[0]));
		startTimePicker.setCurrentMinute(Integer.valueOf(start[1]));
		endTimePicker.setCurrentHour(Integer.valueOf(end[0]));
		endTimePicker.setCurrentMinute(Integer.valueOf(end[1]));
	}

	private void initAdd() {
		currentTask = new Task();
		
		//添加的时候，默认认为是需要询问的
		changed = true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	
        	if(!oldname.equals(taskNameView.getText().toString())){
        		changed = true;
        		Log.v("变化了", "oldname");
        	}
        	else if(!oldcontent.equals(taskContentView.getText().toString())){
        		changed = true;
        		Log.v("变化了", "oldcontent");
        	}
        	
        	else if(!oldstart.equals(DateUtil.getStringbyHourandMinute(startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute()))){
        		changed = true;
        		Log.v("变化了", "oldstart");
        	}
        	else if(!oldend.equals(DateUtil.getStringbyHourandMinute(endTimePicker.getCurrentHour(), endTimePicker.getCurrentMinute()))){
        		changed = true;
        		Log.v("变化了", "oldend");
        	}
        	
        	if(changed){
        		savedialog();
        	}else finish();
        		
            return false; 
        } 
        return false;
	}

	private void savedialog() {
		new AlertDialog.Builder(AddTask.this)
		.setTitle("确认")
		.setMessage("确定保存吗？")
		.setPositiveButton("是",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						currentTask.setDayofWeek(dayofweek==6? 7:((dayofweek+1)%7));
						currentTask.setTaskName(taskNameView.getText().toString());
						currentTask.setContent(taskContentView.getText().toString());
						Integer hour = startTimePicker.getCurrentHour();
						Integer minute = startTimePicker.getCurrentMinute();
						String start = DateUtil.getStringbyHourandMinute(hour, minute);
						currentTask.setStartTime(start);
						
						Integer hour1 = endTimePicker.getCurrentHour();
						Integer minute1 = endTimePicker.getCurrentMinute();
						String end = DateUtil.getStringbyHourandMinute(hour1, minute1);
						currentTask.setEndTime(end);
						
						Log.v("STARTTIME", currentTask.getStartTime());
						Log.v("ENDTIME", currentTask.getEndTime());
						if(currentTask.getProfileId()!=0){
							if(start.compareTo(end)<0){
								
								
								boolean duplicate=false;
								int length = DataApplication.tasklist.size();
								
								for(int i=0;i<length;i++){
									if(currentTask.getTaskId()!=DataApplication.tasklist.get(i).getTaskId()&&currentTask.duplicate(DataApplication.tasklist.get(i))){
										duplicate = true;
										break;
									}
								}
								
								if(!duplicate){
									saveTask();
									finish();
								}
								else ToastFactory.showToast(getApplicationContext(), "不能与已设置日程时间重叠！");
								
							}
							else ToastFactory.showToast(getApplicationContext(), "开始时间必须在结束时间之前！");
						}
						else ToastFactory.showToast(getApplicationContext(), "必须选择情景模式！");
						
			        	
					}

				}).setNegativeButton("否", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).show();
	}
	
	
	public void selectProfile(View v) {

		changed = true;
		
		AbstractLocalService localService = new ProfileService();
		if(DataApplication.profilelist==null)
			DataApplication.profilelist = new ArrayList<Profile>();
		else
			DataApplication.profilelist.clear();
		DataApplication.profilelist.addAll(localService.check());
		
		
		ArrayList<String> name = new ArrayList<String>();
		int length = DataApplication.profilelist.size();
		for (int i = 0; i < length; i++) {
			Profile profile = DataApplication.profilelist.get(i);
			name.add(profile.getProfileName());
		}

		final int size = name.size();
		final String[] p_names = name.toArray(new String[size]);

		new AlertDialog.Builder(AddTask.this).setTitle("选择情景模式")
				.setItems(p_names, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						int id = DataApplication.profilelist.get(which).getProfileId();
						String profileName = p_names[which];
						currentTask.setProfileName(profileName);
						currentTask.setProfileId(id);
						profileNameView.setText(profileName);
					}
				}).show();
	}
	
	
	private void saveTask() {
		new Runnable() {
			@Override
			public void run() {
				Log.v("addTaskwhichDay", dayofweek+"");
				AbstractLocalService localService = new TaskService(AddTask.this);
				currentTask.setDayofWeek(dayofweek==6? 7:((dayofweek+1)%7));
				localService.add(currentTask);
			}
		}.run();
		
	}
	
	
}
