package com.daocaowu.itelligentprofile.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.adapter.ProfileSettingAdapter;
import com.daocaowu.itelligentprofile.bean.GridItem;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.service.TaskService;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class PowerSavingFragment extends Fragment {

	private static final String TAG = "PowerSavingFragment";

	MainPad mainPadActivity = null;
	View contentView = null;
	private CheckBox check;
	private GridView settingGrid;
	private List<GridItem> gridlist;
	private ProfileSettingAdapter adapter;
	
	private LinearLayout amountsetlayout;
	private Button ok;
	private Button cancel;
	private SeekBar seekbar;
	
	private SeekBar savingbar;
	TextView savingamount;
	
	public static Profile powerSavingProfile;
	// ����Ƿ��޸Ĺ�
	private boolean changed =false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainPadActivity = (MainPad) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.set_power_saving_mode_layout, container, false);
		check = (CheckBox)contentView.findViewById(R.id.start_power_saving_mode);
		
		
		savingbar = (SeekBar)contentView.findViewById(R.id.savingseekBar);
		savingamount = (TextView)contentView.findViewById(R.id.percent);
		
		
		
		SharedPreferences sp = MainPad.instance.getSharedPreferences("SP", 0);
		DataApplication.isPowerSaving = sp.getBoolean("power", false);
		 
		savingbar.setMax(100);
		savingbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				savingamount.setText(progress+"%");
			}
		});
		
		check.setChecked(DataApplication.isPowerSaving);
		
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){   
            @Override   
            public void onCheckedChanged(CompoundButton buttonView,   
                    boolean isChecked) {
                if(isChecked){
//                	savingamountlayout.setVisibility(View.VISIBLE);
                    DataApplication.isPowerSaving=true;
                    Editor sharedata = MainPad.instance.getSharedPreferences("SP", 0).edit();  
                    sharedata.putBoolean("power",isChecked); 
//                    WifiLocationService.enableWifiLocaiton(MainPad.instance, 25*1000);
                    sharedata.commit();
                    
                    powerSavingProfile.setPowerValue(savingbar.getProgress());
                    powerSavingProfile.setProfileName("ʡ��ģʽ");
                    powerSavingProfile.setType(3);
                    
                    ProfileService localService = new ProfileService();
                    int insertId = localService.add(powerSavingProfile);
                    powerSavingProfile.setProfileId(insertId);
                    Log.v(TAG, "insertId == "+insertId);
                    Intent intent = new Intent(TaskService.POWER_SAVING_TASK_ACTION);
                    mainPadActivity.instance.sendBroadcast(intent);
                    
                }else{
//                	savingamountlayout.setVisibility(View.GONE);
                	DataApplication.isPowerSaving=false;
                	Editor sharedata = MainPad.instance.getSharedPreferences("SP", 0).edit();  
                    sharedata.putBoolean("power",isChecked);  
//                    WifiLocationService.disableLocation(MainPad.instance);
                    sharedata.commit();
                    Intent intent = new Intent(TaskService.POWER_SAVING_TASK_ACTION);
                    mainPadActivity.instance.sendBroadcast(intent);
                }   
            }   
        });  
		
		
		
		
		
		
		amountsetlayout = (LinearLayout) contentView.findViewById(R.id.amountsetlayout_power_saving);
		ok = (Button) contentView.findViewById(R.id.ok_btn_power_saving);
		cancel = (Button) contentView.findViewById(R.id.cancel_btn_power_saving);
		seekbar = (SeekBar) contentView.findViewById(R.id.seekBar_power_saving);
		seekbar.setMax(100);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				amountsetlayout.setVisibility(View.INVISIBLE);
			}
		});
		
		amountsetlayout.setVisibility(View.INVISIBLE);
		
		gridlist = new ArrayList<GridItem>();
		
		init();
		return contentView;
	}
	
	
	
	private void init() {
		
		ProfileService localService = new ProfileService();
		powerSavingProfile = localService.getPowerSavingProfile();
		if(powerSavingProfile == null)powerSavingProfile = new Profile();
		
		
		initModify();
		
		savingbar.setProgress(powerSavingProfile.getPowerValue());
		
		settingGrid = (GridView) contentView.findViewById(R.id.profilegrid_power_saving);
		adapter = new ProfileSettingAdapter(mainPadActivity.instance, gridlist);
		settingGrid.setAdapter(adapter);
		setGirdViewListener();
	}

	private void initModify() {

		if (powerSavingProfile.getClockVolume() == 0)
			gridlist.add(new GridItem(R.drawable.silence_icon, "��������")); // 0
		else
			gridlist.add(new GridItem(R.drawable.volume_icon, "��������")); // 0

		if (powerSavingProfile.getMediaVolume() == 0)
			gridlist.add(new GridItem(R.drawable.silence_icon, "ý������")); // 1
		else
			gridlist.add(new GridItem(R.drawable.volume_icon, "ý������")); // 1

		if (powerSavingProfile.getBellVolume() == 0)
			gridlist.add(new GridItem(R.drawable.silence_icon, "��������")); // 1
		else
			gridlist.add(new GridItem(R.drawable.volume_icon, "��������")); // 2

		if (powerSavingProfile.getInformVolume() == 0)
			gridlist.add(new GridItem(R.drawable.silence_icon, "֪ͨ����")); // 3
		else
			gridlist.add(new GridItem(R.drawable.volume_icon, "֪ͨ����")); // 3

		if (powerSavingProfile.getBrightness() <= 15)
			gridlist.add(new GridItem(R.drawable.moon_icon, "���ȴ�С")); // 4
		else if (powerSavingProfile.getBrightness() <= 50)
			gridlist.add(new GridItem(R.drawable.cloud_icon, "���ȴ�С")); // 4
		else
			gridlist.add(new GridItem(R.drawable.luminance_icon, "���ȴ�С")); // 4

		if (powerSavingProfile.getAutoAdjustment() == 1)
			gridlist.add(new GridItem(R.drawable.ok_icon, "�Զ�����")); // 5
		else
			gridlist.add(new GridItem(R.drawable.no_icon, "�Զ�����"));

		if (powerSavingProfile.getBluetooth() == 0)
			gridlist.add(new GridItem(R.drawable.no_icon, "����")); // 6
		else
			gridlist.add(new GridItem(R.drawable.ok_icon, "����")); // 6

		if (powerSavingProfile.getGps() == 1)
			gridlist.add(new GridItem(R.drawable.gps_icon, "GPS")); // 7
		else
			gridlist.add(new GridItem(R.drawable.no_icon, "GPS")); // 7

		if (powerSavingProfile.getWifi() == 0)
			gridlist.add(new GridItem(R.drawable.no_icon, "WIFI")); // 8
		else
			gridlist.add(new GridItem(R.drawable.ok_icon, "WIFI")); // 8

		if (powerSavingProfile.getVibrate() == 1) {
			Log.v("��", "Ϊ1");
			gridlist.add(new GridItem(R.drawable.ok_icon, "��")); // 9
		} else {
			Log.v("��", "Ϊ0");
			gridlist.add(new GridItem(R.drawable.no_icon, "��")); // 9
		}

		if (powerSavingProfile.getNetwork() == 1)
			gridlist.add(new GridItem(R.drawable.network_icon, "�ƶ�����")); // 10
		else
			gridlist.add(new GridItem(R.drawable.no_icon, "�ƶ�����")); // 10

		if (powerSavingProfile.getTelephoneSignal() == 0)
			gridlist.add(new GridItem(R.drawable.no_icon, "����ģʽ")); // 11
		else
			gridlist.add(new GridItem(R.drawable.fly_icon, "����ģʽ")); // 11

	}

	private void initPowerSavingProfileView() {

		gridlist.add(new GridItem(R.drawable.volume_icon, "��������")); // 0
		gridlist.add(new GridItem(R.drawable.volume_icon, "ý������")); // 1
		gridlist.add(new GridItem(R.drawable.volume_icon, "��������")); // 2
		gridlist.add(new GridItem(R.drawable.volume_icon, "֪ͨ����")); // 3
		gridlist.add(new GridItem(R.drawable.luminance_icon, "���ȴ�С")); // 4
		gridlist.add(new GridItem(R.drawable.ok_icon, "�Զ�����")); // 5
		gridlist.add(new GridItem(R.drawable.no_icon, "����")); // 6
		gridlist.add(new GridItem(R.drawable.ok_icon, "GPS")); // 7
		gridlist.add(new GridItem(R.drawable.no_icon, "WIFI")); // 8
		gridlist.add(new GridItem(R.drawable.ok_icon, "��")); // 9
		gridlist.add(new GridItem(R.drawable.network_icon, "�ƶ�����")); // 10
		gridlist.add(new GridItem(R.drawable.no_icon, "����ģʽ")); // 11

//		powerSavingProfile = new Profile();
		Log.v("position", "������initPowerSavingProfileView()");
	}
	
	private void setGirdViewListener(){
		settingGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				final int index = position;
				switch (position) {
				case 0:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(powerSavingProfile.getClockVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//������޸�
							changed = true;
							
							powerSavingProfile.setClockVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(powerSavingProfile.getClockVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 1:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(powerSavingProfile.getMediaVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//������޸�
							changed = true;
							
							powerSavingProfile.setMediaVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(powerSavingProfile.getMediaVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
							
						}
					});
					break;
				case 2:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(powerSavingProfile.getBellVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//������޸�
							changed = true;
							
							powerSavingProfile.setBellVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(powerSavingProfile.getBellVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 3:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(powerSavingProfile.getInformVolume());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//������޸�
							changed = true;
							
							powerSavingProfile.setInformVolume(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(powerSavingProfile.getInformVolume()==0) gridlist.get(index).setImageID(R.drawable.silence_icon);
							else gridlist.get(index).setImageID(R.drawable.volume_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 4:
					amountsetlayout.setVisibility(View.VISIBLE);
					seekbar.setProgress(powerSavingProfile.getBrightness());
					ok.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							//������޸�
							changed = true;
							
							powerSavingProfile.setBrightness(seekbar.getProgress());
							amountsetlayout.setVisibility(View.INVISIBLE);
							
							if(powerSavingProfile.getBrightness()<=15) gridlist.get(index).setImageID(R.drawable.moon_icon);
							else if(powerSavingProfile.getBrightness()<=50)gridlist.get(index).setImageID(R.drawable.cloud_icon);
							else gridlist.get(index).setImageID(R.drawable.luminance_icon);
							adapter.notifyDataSetChanged();
						}
					});
					break;
				case 5:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getAutoAdjustment()==0) {
						powerSavingProfile.setAutoAdjustment(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						Log.v("�Զ�����1",String.valueOf(powerSavingProfile.getAutoAdjustment()));
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getAutoAdjustment()==1) {
						powerSavingProfile.setAutoAdjustment(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						Log.v("�Զ�����2",String.valueOf(powerSavingProfile.getAutoAdjustment()));
						adapter.notifyDataSetChanged();
					}
					break;
				case 6:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getBluetooth()==0) {
						powerSavingProfile.setBluetooth(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getBluetooth()==1) {
						powerSavingProfile.setBluetooth(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				
				case 7:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getGps()==0) {
						powerSavingProfile.setGps(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getGps()==1) {
						powerSavingProfile.setGps(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				case 8:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getWifi()==0) {
						powerSavingProfile.setWifi(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getWifi()==1) {
						powerSavingProfile.setWifi(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				case 9:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getVibrate()==0) {
						powerSavingProfile.setVibrate(1);
						gridlist.get(position).setImageID(R.drawable.ok_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getVibrate()==1) {
						powerSavingProfile.setVibrate(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				case 10:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getNetwork()==0) {
						powerSavingProfile.setNetwork(1);
						gridlist.get(position).setImageID(R.drawable.network_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getNetwork()==1) {
						powerSavingProfile.setNetwork(0);
						gridlist.get(position).setImageID(R.drawable.no_icon);
						
						adapter.notifyDataSetChanged();
					}
					break;
				
				case 11:
					//������޸�
					changed = true;
					
					if(powerSavingProfile.getTelephoneSignal()==0) {
						powerSavingProfile.setTelephoneSignal(1);
						gridlist.get(position).setImageID(R.drawable.fly_icon);
						
						adapter.notifyDataSetChanged();
					}
					else if(powerSavingProfile.getTelephoneSignal()==1) {
						powerSavingProfile.setTelephoneSignal(0);
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
}
