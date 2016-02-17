package com.daocaowu.itelligentprofile.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.adapter.WifiAdapter;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.WifiLocation;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.service.WifiLocationService;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.ToastFactory;

public class WifiProfileFragment extends Fragment {

	private static final String TAG = "WifiProfileFragment";

	MainPad mainPadActivity = null;
	View contentView = null;
	
	private CheckBox check;
	
	private ListView mListView;
	private WifiAdapter mAdapter;
	
	private ImageView addimg;
	private String mScanResult;
	private WifiAdmin mWifiAdmin;

	private  Handler myHandler = new Handler() {
        @Override
		public void handleMessage(Message msg) {
             switch (msg.what) {
                  case 0:
                	  initData();
                	  mAdapter.notifyDataSetChanged();
                       break;
             }
             super.handleMessage(msg);
        }
   };
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainPadActivity = (MainPad) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.wifilistview, container, false);
//		Button btn_add_location = (Button)contentView.findViewById(R.id.add_wifi);
//		btn_add_location.setOnClickListener(new OnClickListener() {
		check = (CheckBox)contentView.findViewById(R.id.start_location);
		
		initData();
		
		SharedPreferences sp = MainPad.instance.getSharedPreferences("SP", 0);
		 DataApplication.isWIFIOpen = sp.getBoolean("wifi", false);
		
		check.setChecked(DataApplication.isWIFIOpen);
		
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){   
            @Override   
            public void onCheckedChanged(CompoundButton buttonView,   
                    boolean isChecked) {
                if(isChecked){   
                    DataApplication.isWIFIOpen=true;
                    Editor sharedata = MainPad.instance.getSharedPreferences("SP", 0).edit();  
                    sharedata.putBoolean("wifi",isChecked); 
//                    Intent intent = new Intent(TaskService.WIFI_TASK_ACTION);
//                    mainPadActivity.instance.sendBroadcast(intent);
                    WifiLocationService.enableWifiLocaiton(MainPad.instance, 25*1000);
                    sharedata.commit();
                }else{   
                	DataApplication.isWIFIOpen=false;
                	Editor sharedata = MainPad.instance.getSharedPreferences("SP", 0).edit();  
                    sharedata.putBoolean("wifi",isChecked);  
//                    Intent intent = new Intent(TaskService.WIFI_TASK_ACTION);
//                    mainPadActivity.instance.sendBroadcast(intent);
                    WifiLocationService.disableLocation(MainPad.instance);
                    sharedata.commit();
                }   
            }   
        });  
		
		mListView = (ListView) contentView.findViewById(R.id.wifi_listview);
		mAdapter = new WifiAdapter(DataApplication.wifilist,MainPad.instance);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
				String[] act ={"设置对应情景模式","设置优先级"};
				final int index = arg2;
				new AlertDialog.Builder(mainPadActivity.instance).setTitle("选择操作")
				.setItems(act, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						switch (which) {
						case 0:
							AbstractLocalService localService = new ProfileService();
							if(DataApplication.profilelist==null)
								DataApplication.profilelist = new ArrayList<Profile>();
							else
								DataApplication.profilelist.clear();
							List<Profile> profiles = localService.check();
							if (profiles != null) {
								DataApplication.profilelist.addAll(profiles);
							}
							
							
							ArrayList<String> name = new ArrayList<String>();
							int length = DataApplication.profilelist.size();
							for (int i = 0; i < length; i++) {
								Profile profile = DataApplication.profilelist.get(i);
								name.add(profile.getProfileName());
							}

							final int size = name.size();
							final String[] p_names = name.toArray(new String[size]);

							
							final int loc = index;
							
							new AlertDialog.Builder(MainPad.instance).setTitle("选择情景模式")
									.setItems(p_names, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
											
											
											
											int id = DataApplication.profilelist.get(which).getProfileId();
											String profileName = p_names[which];
											DataApplication.wifilist.get(loc).setProfileName(profileName);
											DataApplication.wifilist.get(loc).setProfileId(id);
											WifiLocationService wifiService = new WifiLocationService(MainPad.instance);
											wifiService.add(DataApplication.wifilist.get(loc));
											
											DataApplication.lastProfileId = -1;
											
											Message message = new Message();   
									        message.what = 0; 
									        myHandler.sendMessage(message);
										}
									}).show();
							break;
						case 1:
							String[] act ={"1(最低)","2","3","4","5","6(最高)"};
							new AlertDialog.Builder(mainPadActivity.instance).setTitle("设定优先级")
							.setItems(act, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										DataApplication.wifilist.get(index).setPriorityLevel(1);
										break;
									case 1:
										DataApplication.wifilist.get(index).setPriorityLevel(2);
										break;
									case 2:
										DataApplication.wifilist.get(index).setPriorityLevel(3);
										break;
									case 3:
										DataApplication.wifilist.get(index).setPriorityLevel(4);
										break;
									case 4:
										DataApplication.wifilist.get(index).setPriorityLevel(5);
										break;
									case 5:
										DataApplication.wifilist.get(index).setPriorityLevel(6);
										break;
									default:
										break;
									}
									WifiLocationService wifiService = new WifiLocationService(MainPad.instance);
									wifiService.add(DataApplication.wifilist.get(index));
									
									DataApplication.lastProfileId = -1;
									
									Message message = new Message();   
							        message.what = 0; 
							        myHandler.sendMessage(message);
								}
							}).show();
							
							break;
							
							
						default:
							break;
						}
					}
				}).show();
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
		});
		
		
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				final int index = arg2;
				
				final WifiLocationService wifiService = new WifiLocationService(MainPad.instance);
				
				new AlertDialog.Builder(MainPad.instance)
				.setTitle("确认")
				.setMessage("确定删除该设定吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
								wifiService.delete(DataApplication.wifilist.get(index).getWifiLocationId());
			                	
								Message message = new Message();   
						        message.what = 0; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
		
		addimg = (ImageView) contentView.findViewById(R.id.addimg);
		
		addimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showlist();
			}
		});
		
		
		return contentView;
	}

	private void initData() {
		
		if(DataApplication.wifilist==null){
			DataApplication.wifilist = new ArrayList<WifiLocation>();
		}
		else
			DataApplication.wifilist.clear();
		
		WifiLocationService wifiService = new WifiLocationService(MainPad.instance);
		List<WifiLocation> wifiLocations = wifiService.check();
		if (wifiLocations != null) {
			DataApplication.wifilist.addAll(wifiLocations);
		}
		
	}
	
	
	private List<WifiLocation> getwifilist() {
		
		mWifiAdmin = new WifiAdmin(MainPad.instance);
		
		return mWifiAdmin.getwifilocationlist();

	}
	
	
	private void showlist(){
		Log.v("showlist", "showlist被调用");
		
		final List<WifiLocation> thistimelist = getwifilist();
		
		ArrayList<String> name = new ArrayList<String>();
		int length = thistimelist.size();
		for (int i = 0; i < length; i++) {
			WifiLocation profile = thistimelist.get(i);
			name.add(profile.getSsid());
		}

		final int size = name.size();
		final String[] p_names = name.toArray(new String[size]);

		new AlertDialog.Builder(MainPad.instance).setTitle("选择WIFI")
				.setItems(p_names, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						String temp = thistimelist.get(which).getSsid();
						int length = DataApplication.wifilist.size();
						boolean has = false;
						for(int i=0; i<length; i++){
							if(temp.equals(DataApplication.wifilist.get(i).getSsid())){
								has = true;
								break;
							}
						}
						
						if(!has){
							WifiLocationService wifiService = new WifiLocationService(MainPad.instance);
							if(thistimelist.get(which)==null)Log.v("thistimelist.get(which)","为null");
							wifiService.add(thistimelist.get(which));
							
							ToastFactory.showToast(mainPadActivity.instance, "添加成功，请点击项设定情景模式及优先级");
							
						}else{
							ToastFactory.showToast(MainPad.instance, "已经存在该wifi");
						}
						
						
						Message message = new Message();   
				        message.what = 0; 
				        myHandler.sendMessage(message);
					}
				}).show();
	}

}











class WifiAdmin {
	private final static String TAG = "WifiAdmin";
	private StringBuffer mStringBuffer = new StringBuffer();
	private List<ScanResult> listResult;
	private ScanResult mScanResult;
	// 定义WifiManager对象
	private WifiManager mWifiManager;
	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfiguration;
	// 定义一个WifiLock
	WifiLock mWifiLock;

	/**
	 * 构造方法
	 */
	public WifiAdmin(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * 打开Wifi网卡
	 */
	public void openNetCard() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 关闭Wifi网卡
	 */
	public void closeNetCard() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * 检查当前Wifi网卡状态
	 */
	public void checkNetCardState() {
		if (mWifiManager.getWifiState() == 0) {
			Log.i(TAG, "网卡正在关闭");
		} else if (mWifiManager.getWifiState() == 1) {
			Log.i(TAG, "网卡已经关闭");
		} else if (mWifiManager.getWifiState() == 2) {
			Log.i(TAG, "网卡正在打开");
		} else if (mWifiManager.getWifiState() == 3) {
			Log.i(TAG, "网卡已经打开");
		} else {
			Log.i(TAG, "---_---晕......没有获取到状态---_---");
		}
	}

	/**
	 * 扫描周边网络
	 */
	public void scan() {
		mWifiManager.startScan();
		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			Log.i(TAG, "当前区域存在无线网络，请查看扫描结果");
		} else {
			Log.i(TAG, "当前区域没有无线网络");
		}
	}

	/**
	 * 扫描获得WifiLocation列表
	 * @return
	 */
	public List<WifiLocation> getwifilocationlist(){
		
		List<WifiLocation> list = new ArrayList<WifiLocation>();
		
		Time t=new Time();
		t.setToNow(); // 取得系统时间。
		String timeNow =t.year+"."+t.month+"."+t.monthDay+"."+t.hour+":"+t.minute+":"+t.second;

		// 每次点击扫描之前清空上一次的扫描结果
		if (mStringBuffer != null) {
			mStringBuffer = new StringBuffer();
		}
		// 开始扫描网络
		scan();
//		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			for (int i = 0; i < listResult.size(); i++) {
				mScanResult = listResult.get(i);
				WifiLocation temp = new WifiLocation();
				temp.setSsid(mScanResult.SSID);
				temp.setBssid(mScanResult.BSSID);
				list.add(temp);
			}
		}
		return list;
	}
	
	
	/**
	 * 得到扫描结果
	 */
	public String getScanResult() {
		
		Time t=new Time();
		t.setToNow(); // 取得系统时间。
		String timeNow =t.year+"."+t.month+"."+t.monthDay+"."+t.hour+":"+t.minute+":"+t.second;

		// 每次点击扫描之前清空上一次的扫描结果
		if (mStringBuffer != null) {
			mStringBuffer = new StringBuffer();
		}
		// 开始扫描网络
		scan();
		listResult = mWifiManager.getScanResults();
		if (listResult != null) {
			for (int i = 0; i < listResult.size(); i++) {
				mScanResult = listResult.get(i);
				mStringBuffer = mStringBuffer.append("NO.").append(i + 1)
						.append(" :").append(mScanResult.SSID).append("->")
						.append(mScanResult.BSSID).append("->")
						.append(mScanResult.capabilities).append("->")
						.append(mScanResult.frequency).append("->")
						.append(mScanResult.level).append("->")
						.append(mScanResult.describeContents()).append("->")
						.append(timeNow).append("\n\n");
			}
		}
		Log.i(TAG, mStringBuffer.toString());
		return mStringBuffer.toString();
	}

	/**
	 * 连接指定网络
	 */
	public void connect() {
		mWifiInfo = mWifiManager.getConnectionInfo();
		
	}

	/**
	 * 断开当前连接的网络
	 */
	public void disconnectWifi() {
		int netId = getNetworkId();
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
		mWifiInfo = null;
	}

	/**
	 * 检查当前网络状态
	 * 
	 * @return String
	 */
	public void checkNetWorkState() {
		if (mWifiInfo != null) {
			Log.i(TAG, "网络正常工作");
		} else {
			Log.i(TAG, "网络已断开");
		}
	}

	/**
	 * 得到连接的ID
	 */
	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * 得到IP地址
	 */
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 锁定WifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// 解锁WifiLock
	public void releaseWifiLock() {
		// 判断时候锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	// 创建一个WifiLock
	public void creatWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("Test");
	}

	// 得到配置好的网络
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfiguration;
	}

	// 指定配置好的网络进行连接
	public void connectConfiguration(int index) {
		// 索引大于配置好的网络索引返回
		if (index >= mWifiConfiguration.size()) {
			return;
		}
		// 连接配置好的指定ID的网络
		mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
				true);
	}

	// 得到MAC地址
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// 得到接入点的BSSID
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// 得到WifiInfo的所有信息包
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	// 添加一个网络并连接
	public int addNetwork(WifiConfiguration wcg) {
		int wcgID = mWifiManager.addNetwork(mWifiConfiguration.get(3));
		mWifiManager.enableNetwork(wcgID, true);
		return wcgID;
	}
}
