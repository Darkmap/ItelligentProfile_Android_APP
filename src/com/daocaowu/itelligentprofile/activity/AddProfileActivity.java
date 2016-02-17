package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.adapter.ProfileSettingAdapter;
import com.daocaowu.itelligentprofile.bean.GridItem;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class AddProfileActivity extends BaseActivity {

	private ProfileSettingAdapter adapter;
	private GridView settingGrid;
	private List<GridItem> gridlist;
	
	
	public static Profile currentprofile;
	
	private EditText profileName;
	
	private LinearLayout amountsetlayout;
	private Button ok;
	private Button cancel;
	private SeekBar seekbar;
	
	
	//标记是否修改过
	private boolean changed =false;
	private String oldname;
	
	//标记是什么内容（0为add，1为修改）
	private int action=0;
	//修改的index
	private int index=-1;
	private static final String TAG = "AddProfileActivity";
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
		setContentView(R.layout.addprofile);
		// 启动activity时不自动弹出软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setUnCatchException();
		
		
		
		
		profileName = (EditText) findViewById(R.id.nameedit);
		amountsetlayout = (LinearLayout) findViewById(R.id.amountsetlayout);
		ok = (Button) findViewById(R.id.ok_btn);
		cancel = (Button) findViewById(R.id.cancel_btn);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		seekbar.setMax(100);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				amountsetlayout.setVisibility(View.INVISIBLE);
			}
		});
		
		
		amountsetlayout.setVisibility(View.INVISIBLE);
		
		Intent intent = getIntent();
		action = intent.getIntExtra("action", 0);
		index = intent.getIntExtra("index", 0);
		
		gridlist = new ArrayList<GridItem>();
		
		init();
	}
	
	private void setUnCatchException(){
		Intent intent = getIntent();
		int code = intent.getIntExtra(RESTART_INTENT_KEY, 0);
		if (CRASHED_CODE == code) {
			/** You can do something here. */
			Log.d(TAG, "So sorry that the application crashed.");
		}
		intent.putExtra(RESTART_INTENT_KEY, CRASHED_CODE);
		m_restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Thread.setDefaultUncaughtExceptionHandler(m_handler);
	}

	
	
	private void init() {
		
		//根据ADD/Modify调整策略
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
		
		

		
		
		settingGrid = (GridView) findViewById(R.id.profilegrid);
		adapter = new ProfileSettingAdapter(this, gridlist);
		settingGrid.setAdapter(adapter);
		
		settingGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				final int index = position;
				switch (position) {
				case 0:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(currentprofile.getClockVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//标记已修改
							changed = true;
							
							currentprofile.setClockVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(currentprofile.getClockVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 1:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(currentprofile.getMediaVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//标记已修改
							changed = true;
							
							currentprofile.setMediaVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(currentprofile.getMediaVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
							
						}
					});
					break;
				case 2:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(currentprofile.getBellVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//标记已修改
							changed = true;
							
							currentprofile.setBellVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(currentprofile.getBellVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 3:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(currentprofile.getInformVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//标记已修改
							changed = true;
							
							currentprofile.setInformVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(currentprofile.getInformVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 4:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(currentprofile.getBrightness());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//标记已修改
							changed = true;
							
							currentprofile.setBrightness(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(currentprofile.getBrightness()<=15) gridlist.get(index).setImageID(R.drawable.moon_icon);
							else if(currentprofile.getBrightness()<=50)gridlist.get(index).setImageID(R.drawable.cloud_icon);
							else gridlist.get(index).setImageID(R.drawable.luminance_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 5:
					//标记已修改
					changed = true;
					
					if(currentprofile.getAutoAdjustment()==0) {
						currentprofile.setAutoAdjustment(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						Log.v("自动亮度1",String.valueOf(currentprofile.getAutoAdjustment()));
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getAutoAdjustment()==1) {
						currentprofile.setAutoAdjustment(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						Log.v("自动亮度2",String.valueOf(currentprofile.getAutoAdjustment()));
						adapter.notifyDataSetChanged();
					}
					break;
				case 6:
					//标记已修改
					changed = true;
					
					if(currentprofile.getBluetooth()==0) {
						currentprofile.setBluetooth(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getBluetooth()==1) {
						currentprofile.setBluetooth(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				
				case 7:
					//标记已修改
					changed = true;
					
					if(currentprofile.getGps()==0) {
						currentprofile.setGps(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getGps()==1) {
						currentprofile.setGps(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				case 8:
					//标记已修改
					changed = true;
					
					if(currentprofile.getWifi()==0) {
						currentprofile.setWifi(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getWifi()==1) {
						currentprofile.setWifi(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				case 9:
					//标记已修改
					changed = true;
					
					if(currentprofile.getVibrate()==0) {
						currentprofile.setVibrate(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getVibrate()==1) {
						currentprofile.setVibrate(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				case 10:
					//标记已修改
					changed = true;
					
					if(currentprofile.getNetwork()==0) {
						currentprofile.setNetwork(1);
						gridlist.get(position).setImageID(R.drawable.network_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getNetwork()==1) {
						currentprofile.setNetwork(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				
				case 11:
					//标记已修改
					changed = true;
					
					if(currentprofile.getTelephoneSignal()==0) {
						currentprofile.setTelephoneSignal(1);
						gridlist.get(position).setImageID(R.drawable.fly_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(currentprofile.getTelephoneSignal()==1) {
						currentprofile.setTelephoneSignal(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
					
				default:
					break;
				}
				
			}
		});
		
		
	}

	private void initModify() {
		
		if(currentprofile.getClockVolume()==0)gridlist.add(new GridItem(R.drawable.silence_icon, "闹钟音量"));			//0
		else gridlist.add(new GridItem(R.drawable.volume_icon, "闹钟音量"));			//0
		
		if(currentprofile.getMediaVolume()==0) gridlist.add(new GridItem(R.drawable.silence_icon, "媒体音量"));			//1
		else gridlist.add(new GridItem(R.drawable.volume_icon, "媒体音量"));			//1
		
		if(currentprofile.getBellVolume()==0) gridlist.add(new GridItem(R.drawable.silence_icon, "来电音量"));			//1
		else gridlist.add(new GridItem(R.drawable.volume_icon, "来电音量"));			//2
		
		if(currentprofile.getInformVolume()==0) gridlist.add(new GridItem(R.drawable.silence_icon, "通知音量"));			//3
		else gridlist.add(new GridItem(R.drawable.volume_icon, "通知音量"));			//3
		
		if(currentprofile.getBrightness()<=15) gridlist.add(new GridItem(R.drawable.moon_icon, "亮度大小"));		//4
		else if(currentprofile.getBrightness()<=50) gridlist.add(new GridItem(R.drawable.cloud_icon, "亮度大小"));		//4
		else gridlist.add(new GridItem(R.drawable.luminance_icon, "亮度大小"));		//4
		
		
		if(currentprofile.getAutoAdjustment()==1)gridlist.add(new GridItem(R.drawable.ok_icon, "自动亮度"));						//5
		else gridlist.add(new GridItem(R.drawable.no_icon, "自动亮度"));
		
		if(currentprofile.getBluetooth()==0)gridlist.add(new GridItem(R.drawable.no_icon, "蓝牙"));							//6
		else gridlist.add(new GridItem(R.drawable.ok_icon, "蓝牙"));							//6
		
		if(currentprofile.getGps()==1)gridlist.add(new GridItem(R.drawable.gps_icon, "GPS"));								//7
		else gridlist.add(new GridItem(R.drawable.no_icon, "GPS"));								//7
		
		if(currentprofile.getWifi()==0)gridlist.add(new GridItem(R.drawable.no_icon, "WIFI"));							//8
		else gridlist.add(new GridItem(R.drawable.ok_icon, "WIFI"));							//8
		
		if(currentprofile.getVibrate()==1){
			Log.v("震动", "为1");
			gridlist.add(new GridItem(R.drawable.ok_icon, "震动"));					//9
		}
		else {
			Log.v("震动", "为0");
			gridlist.add(new GridItem(R.drawable.no_icon, "震动"));					//9
		}
		
		if(currentprofile.getNetwork()==1)gridlist.add(new GridItem(R.drawable.network_icon, "移动网络"));			//10
		else gridlist.add(new GridItem(R.drawable.no_icon, "移动网络"));			//10
		
		if(currentprofile.getTelephoneSignal()==0)gridlist.add(new GridItem(R.drawable.no_icon, "飞行模式"));						//11
		else gridlist.add(new GridItem(R.drawable.fly_icon, "飞行模式"));						//11
		
		
		oldname = currentprofile.getProfileName();
		profileName.setText(oldname);
	}

	private void initAdd() {
		
		gridlist.add(new GridItem(R.drawable.volume_icon, "闹钟音量"));			//0
		gridlist.add(new GridItem(R.drawable.volume_icon, "媒体音量"));			//1
		gridlist.add(new GridItem(R.drawable.volume_icon, "来电音量"));			//2
		gridlist.add(new GridItem(R.drawable.volume_icon, "通知音量"));			//3
		gridlist.add(new GridItem(R.drawable.luminance_icon, "亮度大小"));		//4
		gridlist.add(new GridItem(R.drawable.ok_icon, "自动亮度"));						//5
		gridlist.add(new GridItem(R.drawable.no_icon, "蓝牙"));							//6
		gridlist.add(new GridItem(R.drawable.ok_icon, "GPS"));								//7
		gridlist.add(new GridItem(R.drawable.no_icon, "WIFI"));							//8
		gridlist.add(new GridItem(R.drawable.ok_icon, "震动"));					//9
		gridlist.add(new GridItem(R.drawable.network_icon, "移动网络"));			//10
		gridlist.add(new GridItem(R.drawable.no_icon, "飞行模式"));						//11
		
		currentprofile = new Profile();
		Log.v("position", "运行至initAdd()");
		
		oldname = "新建情景模式";
		profileName.setText(oldname);
	}
	
	
	
	
	
	
	
	
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	
        	//判断名字是否修改过
        	if(!oldname.equals(profileName.getText().toString())) changed=true;
        	
        	if(changed)savedialog();
        	else finish();
            return false; 
        } 
        return false;
	}



	/**
	 * 返回键以后，出现对话框，提示是否保存该设置
	 */
	private void savedialog() {
		new AlertDialog.Builder(AddProfileActivity.this)
		.setTitle("确认")
		.setMessage("确定保存吗？")
		.setPositiveButton("是",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						currentprofile.setProfileName(profileName.getText().toString());
						switch (action) {
						case 0:
//							DataApplication.profilelist.add(currentprofile);
							saveProfile();
							break;
						case 1:
//							DataApplication.profilelist.set(index, currentprofile);
							saveProfile();
							break;
						default:
							break;
						}
			        	finish();
					}
				}).setNegativeButton("否", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}).show();
		
	}
	
	
	
	/**
	 * 存储 currentprofile进入数据库（可能是新增，也可能是修改，以ID为标记）
	 */
	private void saveProfile() {
		new Runnable() {
			@Override
			public void run() {
				AbstractLocalService localService = new ProfileService();
				localService.add(currentprofile);
				
				Log.v("当前设置震动为", String.valueOf(currentprofile.getVibrate()));
				if(DataApplication.profilelist==null)
					DataApplication.profilelist = new ArrayList<Profile>();
				else
					DataApplication.profilelist.clear();
				List<Profile> profiles = localService.check();
				if (profiles != null) {
					DataApplication.profilelist.addAll(profiles);
				}
				profiles = null;
				Log.v("来自save线程", String.valueOf(DataApplication.profilelist.size()));
			}
		}.run();
		
	}
	
	
}
