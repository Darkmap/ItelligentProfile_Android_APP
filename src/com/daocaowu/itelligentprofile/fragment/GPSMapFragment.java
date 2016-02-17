package com.daocaowu.itelligentprofile.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.GPSMapActivity;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.adapter.GpsAdaptet;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.LocationService;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class GPSMapFragment extends Fragment{

	private static final String TAG = "GPSMapFragment";
	
	MainPad mainPadActivity = null;
	View contentView = null;
	
	ListView gpslistview;
	GpsAdaptet madapter;
	
	private CheckBox check;
	
	List<Location> gpslist = new ArrayList<Location>();
	private  Handler myHandler = new Handler() {
        @Override
		public void handleMessage(Message msg) {
             switch (msg.what) {
                  case 0:
                	  initData();
                	  madapter.notifyDataSetChanged();
                       break;
             }
             super.handleMessage(msg);
        }
   };
	
   private void initData() {
		
		if(DataApplication.locationlist==null){
			DataApplication.locationlist = new ArrayList<Location>();
		}
		else
			DataApplication.locationlist.clear();
		
		LocationService locService = new LocationService();
		List<Location> locations = locService.check();
		if (locations != null) {
			DataApplication.locationlist.addAll(locations);
		}
		
		
//		Location a = new Location();
//		a.setLatitude(23.12313213);
//		a.setLongitude(159.123123123);
//		a.setLocationName("家");
//		DataApplication.locationlist.add(a);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainPadActivity = (MainPad)getActivity();
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Message message = new Message();   
        message.what = 0; 
        myHandler.sendMessage(message);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			contentView = inflater.inflate(R.layout.set_location_layout, container, false);
			Button btn_add_location = (Button)contentView.findViewById(R.id.add_location);
			check = (CheckBox)contentView.findViewById(R.id.start_location);
			
			initData();
			
			 SharedPreferences sp = MainPad.instance.getSharedPreferences("SP", 0);
			 DataApplication.isGPSOpen = sp.getBoolean("gps", false);
			
			 check.setChecked(DataApplication.isGPSOpen);
			
			
			check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){   
	            @Override   
	            public void onCheckedChanged(CompoundButton buttonView,   
	                    boolean isChecked) {
	                if(isChecked){   
	                    DataApplication.isGPSOpen=true;
	                    Editor sharedata = MainPad.instance.getSharedPreferences("SP", 0).edit();  
	                    sharedata.putBoolean("gps",isChecked); 
//	                    Intent intent = new Intent(TaskService.GPS_TASK_ACTION);
//	                    mainPadActivity.instance.sendBroadcast(intent);
	                    LocationService.enableLocation(MainPad.instance, 10*1000);
	                    sharedata.commit();
	                }else{   
	                	DataApplication.isGPSOpen=false;
	                	Editor sharedata = MainPad.instance.getSharedPreferences("SP", 0).edit();  
	                    sharedata.putBoolean("gps",isChecked);  
//	                    Intent intent = new Intent(TaskService.GPS_TASK_ACTION);
//	                    mainPadActivity.instance.sendBroadcast(intent);
	                    LocationService.disableLocation(MainPad.instance);
	                    sharedata.commit();
	                }   
	            }   
	        });  
			
			gpslistview = (ListView)contentView.findViewById(R.id.gpslist);
			madapter = new GpsAdaptet(DataApplication.locationlist,MainPad.instance);
			gpslistview.setAdapter(madapter);
			
			gpslistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
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

					final int loc = arg2;
					
					new AlertDialog.Builder(MainPad.instance).setTitle("选择情景模式")
							.setItems(p_names, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									
									
									int id = DataApplication.profilelist.get(which).getProfileId();
									String profileName = p_names[which];
									DataApplication.locationlist.get(loc).setProfileName(profileName);
									DataApplication.locationlist.get(loc).setProfileId(id);
									LocationService gpsService = new LocationService();
									gpsService.add(DataApplication.locationlist.get(loc));
									
									DataApplication.lastProfileId = -1;
									Log.v(TAG, "DataApplication.lastProfileId = "+DataApplication.lastProfileId);
									
									Message message = new Message();   
							        message.what = 0; 
							        myHandler.sendMessage(message);
								}
							}).show();
					
				}
			});
			
			
			
			gpslistview.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					final int index = arg2;
					
					final LocationService locService = new LocationService();
					
					new AlertDialog.Builder(MainPad.instance)
					.setTitle("确认")
					.setMessage("确定删除该设定吗？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
//									AbstractLocalService localService = new TaskService(mainPadActivity.instance);
									locService.delete(DataApplication.locationlist.get(index).getLocationId());
				                	
									DataApplication.lastProfileId = -1;
									Log.v(TAG, "DataApplication.lastProfileId = "+DataApplication.lastProfileId);
									
									Message message = new Message();   
							        message.what = 0; 
							        myHandler.sendMessage(message);
								}
							}).setNegativeButton("否", null).show();
					
					return false;
				}
			});
			
			
			btn_add_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mainPadActivity, GPSMapActivity.class);
				startActivity(intent);
				
				
			}
		});
	    
		return contentView;
	}
	
}
