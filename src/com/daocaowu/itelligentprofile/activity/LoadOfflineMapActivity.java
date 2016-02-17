package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.adapter.OffMapCityAdapter;
import com.daocaowu.itelligentprofile.adapter.UpdateInfoCityAdapter;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;
import com.daocaowu.itelligentprofile.utils.SystemUtil;

//TODO 检测当前网络状态
public class LoadOfflineMapActivity extends Activity {

	public static MKOfflineMap mMKOfflineMap = null;
	public MYMKOfflineMapListener myMKOfflineMapListener = null;
	private final static String TAG = "MainActivty";
	EditText searchCityEdit ;//搜索框
	TextView loadInfoTextView;//下载信息显示
	RelativeLayout citySelectLayout ;//切换框
	TextView citySelectTextView ;//切换信息显示
	ListView cityList_view;//城市列表
	OffMapCityAdapter offlineMapCityadapter;//离线地图城市
	UpdateInfoCityAdapter updateCityadapter;//
	ArrayList<MKOLSearchRecord> offlineCityList,searchOfflineCityList ;//离线地图 列表
	ArrayList<MKOLUpdateElement> updateCityList,searchAllUpdateCityList ;//有下载痕迹的地图列表
	
	//记录当前的listview状态的
	private final int LOOKloadLIST= 1;//下载列表状态
	private final int LOOKallLIST= 0;//所有列表状态
	private int listViewStatus = LOOKallLIST;
	private boolean isLoading = false;
	
	private ArrayList<MKOLSearchRecord> offlineProvinceList;//省份列表
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_offline_map);
		// 启动activity时不自动弹软键盘
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setUnCatchException();
		//布局组件的获取
        searchCityEdit = (EditText)findViewById(R.id.searchCityEdit);
		loadInfoTextView = (TextView)findViewById(R.id.loadInfoTextView);
		citySelectLayout = (RelativeLayout)findViewById(R.id.citySelectLayout);
		citySelectTextView = (TextView)findViewById(R.id.citySelectTextView);
		cityList_view = (ListView) findViewById(R.id.cityList_view);
        
		initMKOfflineMap();
        //获取支持离线地图城市列表
		offlineCityList = getOfflineCityList();
        if (offlineCityList == null) {
			offlineCityList = new ArrayList<MKOLSearchRecord>();
		}
        if (offlineMapCityadapter == null) {
        	//初始化 离线地图 适配器        cityList_view绑定该适配器
            offlineMapCityadapter = new OffMapCityAdapter(offlineCityList, LoadOfflineMapActivity.this);
    		cityList_view.setAdapter(offlineMapCityadapter);
		}else {
			offlineMapCityadapter.notifyDataSetChanged();
		}
        
        
        //获取各城市离线地图更新信息
		updateCityList = getUpdateCityList();
        if (updateCityList == null) {
        	updateCityList = new ArrayList<MKOLUpdateElement>();
        }
        if (updateCityadapter == null) {
        	//初始化 离线地图更新信息 适配器 
            updateCityadapter = new UpdateInfoCityAdapter(updateCityList, LoadOfflineMapActivity.this);
		}else {
			updateCityadapter.notifyDataSetChanged();
		}
        
        //获取各省份离线地图
        offlineProvinceList = getOfflineProvinceList();
        
      //监听searchCityEdit的变化  有变化就时时搜索
		searchCityEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(listViewStatus == LOOKallLIST)
					searchOfflineCity(s.toString());
				else
					searchUpdateInfoCity(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		//监听citySelectLayout被按下就进行城市列表切换
		citySelectLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listViewStatus == LOOKallLIST)//如果是显示所有城市状态
				{
					citySelectTextView.setText("查看所有城市列表");
					listViewStatus = LOOKloadLIST;
					cityList_view.setAdapter(updateCityadapter);
					refUpdateCityList();
					searchUpdateInfoCity(searchCityEdit.getText().toString());
				}
				else
				{
					citySelectTextView.setText("查看下载列表");
					listViewStatus = LOOKallLIST;
					cityList_view.setAdapter(offlineMapCityadapter);
					searchOfflineCity(searchCityEdit.getText().toString());
				}
			}
		});
		
		//监听cityList_view是否有item被click
		        //长按
//		cityList_view.setOnItemLongClickListener(new OnItemLongClickListener() {
//			
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//				// TODO Auto-generated method stub
//				TextView hasNewTV=(TextView) view.findViewById(R.id.hasNewTV);
//				if(listViewStatus == LOOKloadLIST)//如果是离线地图下载信息列表
//				{
//					MKOLUpdateElement myListItem = (MKOLUpdateElement) updateCityadapter.getItem(position);
//					removeLoadMap(myListItem.cityID);
////					loadInfoTV.setText("已经删除 "+myListItem.cityName);
//					updateCityList.remove(position);
////					searchCityEdit.setText("");
//					refUpdateCityList();
//					Log.v("已经删除","```");
//					updateCityadapter.notifyDataSetChanged();
//				}
//				return true;		
//			}
//		});
		       //轻轻点击
		cityList_view.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stu
				openDialog(position);
			}
		});
	}
	/**
	 *  初始化 MKOfflineMap
	 */
	private void initMKOfflineMap() {
		if (myMKOfflineMapListener == null) {
			myMKOfflineMapListener = new MYMKOfflineMapListener();
		}
		if (mMKOfflineMap == null) {
			mMKOfflineMap = new MKOfflineMap();
//			mMKOfflineMap.init(GPSMapActivity.mMapController,
//					myMKOfflineMapListener);
		}
		mMKOfflineMap.init(GPSMapActivity.mMapController,
				myMKOfflineMapListener);
	}

	/**
	 * 返回离线地图城市（包括省份和全国）列表
	 */
	public ArrayList<MKOLSearchRecord> getOfflineCityAndProvinceList() {
		ArrayList<MKOLSearchRecord> list = new ArrayList<MKOLSearchRecord>();
		ArrayList<MKOLSearchRecord> ocl = mMKOfflineMap.getOfflineCityList();
		if(ocl==null)
			return list;
		for (MKOLSearchRecord r : mMKOfflineMap.getOfflineCityList())
		{
			list.add(r);
			if (r.cityType == 1)
				for (MKOLSearchRecord o : r.childCities)
				{
					list.add(o);
					Log.v(o.cityName, o.cityID+" ");
				}
		}
		return list;
	}
	/**
	 * 返回离线地图（只有省份）列表
	 */
	public ArrayList<MKOLSearchRecord> getOfflineProvinceList() {
		ArrayList<MKOLSearchRecord> list = new ArrayList<MKOLSearchRecord>();
		ArrayList<MKOLSearchRecord> ocl = mMKOfflineMap.getOfflineCityList();
		if(ocl==null)
			return list;
		for (MKOLSearchRecord r : mMKOfflineMap.getOfflineCityList())
		{
			if (r.cityType == 1)
				list.add(r);
		}
		return list;
	}
	
	/**
	 * 返回离线地图城市（没有包括省份，有全国）列表
	 */
	public ArrayList<MKOLSearchRecord> getOfflineCityList() {
		ArrayList<MKOLSearchRecord> list = new ArrayList<MKOLSearchRecord>();
		ArrayList<MKOLSearchRecord> ocl = mMKOfflineMap.getOfflineCityList();
		if(ocl==null)
			return list;
		for (MKOLSearchRecord r : ocl)
		{
			if (r.cityType == 1)
				for (MKOLSearchRecord o : r.childCities)
				{
					list.add(o);
//					Log.v(o.cityName, o.cityID+" ");
				}
			else
				list.add(r);
		}
		Log.v("OfflineCityList", "城市加载完毕");
		return list;
	}
	
	/**
	 * 返回各城市离线地图更新信息
	 */
	public ArrayList<MKOLUpdateElement> getUpdateCityList()
	{
		ArrayList<MKOLUpdateElement> mmm = mMKOfflineMap.getAllUpdateInfo();
		if(mmm==null)
			return new ArrayList<MKOLUpdateElement>();
		for (MKOLUpdateElement m: mmm)
		{
			if(m.size==0&&m.status!=MKOLUpdateElement.DOWNLOADING&&m.status!=MKOLUpdateElement.WAITING)  
				//如果大小为0 或者不是下载或者等待下载状态    就删除它
				mMKOfflineMap.remove(m.cityID);
		}
		return mMKOfflineMap.getAllUpdateInfo();
	}
	
	/**
	 * 搜索离线地图城市
	 */
	public void searchOfflineCity(String city) {
		if(city.equals(""))
		{
			offlineMapCityadapter.setListData(offlineCityList);
		}
		else
		{
			if (searchOfflineCityList == null) {
				searchOfflineCityList = new ArrayList<MKOLSearchRecord>();
			}else {
				searchOfflineCityList.clear();
			}
			if(city.length()<4&&city.length()>1)
			{
				for (MKOLSearchRecord p: offlineProvinceList)
				{
					if( p.cityName.startsWith(city) )
					{
						for (MKOLSearchRecord c: p.childCities)
							searchOfflineCityList.add(c);
						break;//中国所有省份的前2个字都不相同
					}	
				}
			}
			for (MKOLSearchRecord r : offlineCityList) {
				if(r.cityName.startsWith(city))
					searchOfflineCityList.add(r);
			}
			offlineMapCityadapter.setListData(searchOfflineCityList);
		}
		offlineMapCityadapter.notifyDataSetChanged();//刷新offlineMapCityadapter适配器
	}
	
	/**
	 * 设置离线地图城市List
	 */
	public void setOfflineCityListData() {
		offlineMapCityadapter.setListData(offlineCityList);
	}
	public void setOfflineCityListData(ArrayList<MKOLSearchRecord> CityList) {
		offlineMapCityadapter.setListData(CityList);
	}
	/**
	 * 搜索已经下载地图城市
	 */
	public void searchUpdateInfoCity(String city) {
		if(city.equals(""))
		{
			updateCityadapter.setListData(updateCityList);
		}
		else
		{
			if (searchAllUpdateCityList == null) {
				searchAllUpdateCityList = new ArrayList<MKOLUpdateElement>();
			}else {
				searchAllUpdateCityList.clear();
			}
			if(city.length()<4&&city.length()>1)
			{
				MKOLUpdateElement upEle;
				for (MKOLSearchRecord p: offlineProvinceList)
				{
					if( p.cityName.startsWith(city) )
					{
						for (MKOLSearchRecord c: p.childCities)
						{
							upEle=mMKOfflineMap.getUpdateInfo(c.cityID);
							if(upEle !=null)
								searchAllUpdateCityList.add(upEle);
						}
						break;//中国所有省份的前2个字都不相同
					}	
				}
			}
			for (MKOLUpdateElement r : updateCityList) {
				if(r.cityName.startsWith(city))
					searchAllUpdateCityList.add(r);
			}
			updateCityadapter.setListData(searchAllUpdateCityList);
		}
		updateCityadapter.notifyDataSetChanged();//刷新updateCityadapter适配器
	}
	
	/**
	 * 刷新已经下载地图城市
	 */
	public void refUpdateCityList() {
		updateCityList = getUpdateCityList();
		updateCityadapter.setListData(updateCityList);
	}
	/**
	 * 设置已经下载地图城市List
	 */
	public void setUpdateInfoCityListData() {
		updateCityadapter.setListData(getUpdateCityList());
	}
	public void setUpdateInfoCityListData(ArrayList<MKOLUpdateElement> CityList) {
		updateCityadapter.setListData(CityList);
	}
	
	/**
	 * 开始下载离线地图
	 */
	public void startLoadMap(int cityID) {
		mMKOfflineMap.start(cityID);
	}
	/**
	 * 暂停下载离线地图
	 */
	public void pauseLoadMap(int cityID) {
		mMKOfflineMap.pause(cityID);
	}
	/**
	 * 删除下载好的离线地图
	 */
	public void removeLoadMap(int cityID) {
		mMKOfflineMap.remove(cityID);
	}
	/**
	 * 导入下载好的离线地图  返回导入个数
	 */
	public int scanLoadMap() {
		//true: 导入失败时将删除离线地图数据。 false: 导入失败时保留离线地图数据。
		return mMKOfflineMap.scan(false);
	}
	

//	private boolean isLoading = false;//保存当前状态
	/**
	 * 离线地图mMKOfflineMap的监听类
	 */
	public class MYMKOfflineMapListener implements MKOfflineMapListener {

		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				isLoading = true;
				Log.v("TYPE_DOWNLOAD_UPDATE", "下载中");
				// Log.d("OfflineDemo",
				// String.format("cityid:%d update",state));
				MKOLUpdateElement update = mMKOfflineMap.getUpdateInfo(state);
				if(update == null)
					update = new MKOLUpdateElement();
//				Log.v(""+update.status," ```");
				if (update.ratio == 0) {
					loadInfoTextView.setText(String.format("%s : 等待中", update.cityName) );
				} else {
					loadInfoTextView.setText(String.format("%s : %d%%", update.cityName, update.ratio));
//					isOneLoading = 1;
				}
//				if(update.ratio==100)
//					isLoading = false;
//				else
//					isLoading = true;
//				nowCityId = update.cityID;
//				Log.v("isOneLoading", ""+isOneLoading);
//				Log.v("update.ratio", ""+update.ratio);
//				Log.v("update.status", ""+update.status);
				if(update.ratio >= 100)
				{
//					isOneLoading = 0;
					loadInfoTextView.setText(String.format("%s : %d%%"+" 已经完成下载",
							update.cityName, update.ratio));
					isLoading = false;
					Log.v("TYPE_DOWNLOAD_UPDATE", "下载完毕");
//					Log.v("update", ""+mMKOfflineMap.getUpdateInfo(state).update);
					scanLoadMap();
				}
				//对数据进行刷新
				if(listViewStatus == LOOKallLIST)
				{
					searchOfflineCity(searchCityEdit.getText().toString());
				}
				else
				{
					refUpdateCityList();
					searchUpdateInfoCity(searchCityEdit.getText().toString());			
				}
				switch (update.status) {//异常就删除它
				case MKOLUpdateElement.eOLDSIOError:
					loadInfoTextView.setText(update.cityName + " : 读写异常"+"已经删除");
					removeLoadMap(update.cityID);
					break;
				case MKOLUpdateElement.eOLDSMd5Error:
					loadInfoTextView.setText(update.cityName + " : 效验失败"+"已经删除");
					removeLoadMap(update.cityID);
					break;
				case MKOLUpdateElement.eOLDSMissData:
					loadInfoTextView.setText(update.cityName + " : 数据丢失"+"已经删除");
					removeLoadMap(update.cityID);
					break;
				case MKOLUpdateElement.eOLDSWifiError:
					loadInfoTextView.setText(update.cityName + " : 网络异常");
					break;
				case MKOLUpdateElement.eOLDSNetError:
					loadInfoTextView.setText(update.cityName + " : wifi网络异常");
					break;
				default:
					break;
				}
//				Log.v("update", ""+mMKOfflineMap.getUpdateInfo(state).update);
			}
			break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				loadInfoTextView.setText("已经完成安装");
				Log.v("TYPE_NEW_OFFLINE","新安装%d个离线地图"+state);
//				loadInfoTextView.setText(String.format("新安装%d个离线地图", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				MKOLUpdateElement e = mMKOfflineMap.getUpdateInfo(state);
				if (e != null) {
					loadInfoTextView.setText(String.format("%s 有离线地图更新", e.cityName));
					Log.v("TYPE_VER_UPDATE",e.cityName+"有离线地图更新");
				}
				break;
			}
		}
	}
	
	/**
	 * 点击listview 打开对话框
	 * @param position
	 */
	public void openDialog(int position) {
		AlertDialog.Builder builder = new Builder(LoadOfflineMapActivity.this);
		String message1="",message2="",message3="",queren="确认";
		final MKOLUpdateElement mMKOLUpdateElement;
		final MKOLSearchRecord mMKOLSearchRecord ;
		if(SwitchProfileUtil.getWlanStatus(this)==0)
			message2 = "未开启WLAN";
		if(SystemUtil.isConnectInternet(this)==false)
			message2 = "未连接网络";
		if(listViewStatus == LOOKallLIST)
		{
			mMKOLSearchRecord = (MKOLSearchRecord) offlineMapCityadapter.getItem(position);
			Log.v("cityName"+mMKOLSearchRecord.cityName, "cityID"+mMKOLSearchRecord.cityID);
			mMKOLUpdateElement = mMKOfflineMap.getUpdateInfo(mMKOLSearchRecord.cityID);
			Log.v("MKOLUpdateElement",mMKOLUpdateElement+"");
			loadInfoTextView.setText(mMKOLSearchRecord.cityName+"");
			
			
			if(mMKOLUpdateElement == null)
				message1 = "确定下载吗？";
			else
				switch(mMKOLUpdateElement.status)
				{
				case MKOLUpdateElement.SUSPENDED:
					message1 = "确定继续下载吗？";
					break;
				case MKOLUpdateElement.DOWNLOADING:
					message1 = "确定暂停吗？";
					break;
				case MKOLUpdateElement.WAITING:
					message1 = "确定暂停吗？";
					break;
				case MKOLUpdateElement.FINISHED:
					message1 = "已经完成 无须下载";
					break;
				default:
					message1 = "error";
					break;
				
				}
		}
		else
		{
			message1 = "确认删除？";
			mMKOLUpdateElement = (MKOLUpdateElement) updateCityadapter.getItem(position);
			mMKOLSearchRecord = null;
			switch(mMKOLUpdateElement.status)
			{
			case MKOLUpdateElement.SUSPENDED:
				message1 = "删除or继续？";
				message3 = "继续";
				queren="删除";
				break;
			case MKOLUpdateElement.DOWNLOADING:
				message1 = "删除or暂停？";
				message3 = "暂停";
				queren="删除";
				break;
			case MKOLUpdateElement.WAITING:
				message1 = "删除or暂停？";
				message3 = "暂停";
				queren="删除";
				break;
			}
		}
		
		if(message3.length()>0)
			builder.setNeutralButton(message3, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(mMKOLUpdateElement.status)
					{
					case MKOLUpdateElement.SUSPENDED:
						mMKOfflineMap.start(mMKOLUpdateElement.cityID);
						break;
					case MKOLUpdateElement.DOWNLOADING:
						loadInfoTextView.setText(loadInfoTextView.getText() + " 已暂停");
						mMKOfflineMap.pause(mMKOLUpdateElement.cityID);
						break;
					case MKOLUpdateElement.WAITING:
						loadInfoTextView.setText(loadInfoTextView.getText() + " 已暂停");
						mMKOfflineMap.pause(mMKOLUpdateElement.cityID);
						break;
					}
					offlineMapCityadapter.notifyDataSetChanged();
					refUpdateCityList();
					updateCityadapter.notifyDataSetChanged();
					dialog.dismiss();
				}
			});
		
		builder.setMessage(message1);
		if(message2.length()>0)
		{
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle(message2);
		}
		builder.setPositiveButton(queren, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(listViewStatus == LOOKallLIST)//如果是离线地图列表
				{
					
					if(mMKOLUpdateElement == null || mMKOLUpdateElement.status == MKOLUpdateElement.SUSPENDED)//从未下载过  就开始下载
					{
						Log.v("status","null or SUSPENDED");
						mMKOfflineMap.start(mMKOLSearchRecord.cityID);
					}
					else if(mMKOLUpdateElement.status == MKOLUpdateElement.DOWNLOADING)//下载中
					{
						loadInfoTextView.setText(loadInfoTextView.getText() + " 已暂停");
						Log.v("status","DOWNLOADING");
						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
					}
					else if(mMKOLUpdateElement.status == MKOLUpdateElement.WAITING)//等待中
					{
						loadInfoTextView.setText(loadInfoTextView.getText() + " 已暂停");
						Log.v("status","WAITING");
						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
					}
					else if(mMKOLUpdateElement.status == MKOLUpdateElement.FINISHED)//下载完成
					{
						loadInfoTextView.setText(loadInfoTextView.getText() + " 已完成");
						Log.v("status","FINISHED");
					}
					else
					{
						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
						Log.v("status",""+mMKOLUpdateElement.status);
//						mMKOfflineMap.pause(mMKOLSearchRecord.cityID);
					}
				}
				else//如果是离线地图列表
				{
					removeLoadMap(mMKOLUpdateElement.cityID);
					loadInfoTextView.setText(mMKOLUpdateElement.cityName + " : 已删除");
//					updateCityList.remove(position);
//					searchCityEdit.setText("");
					Log.v("已删除","```");
				}
				offlineMapCityadapter.notifyDataSetChanged();
				refUpdateCityList();
				updateCityadapter.notifyDataSetChanged();
				dialog.dismiss();
//				LoadOfflineMapActivity.this.finish();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	/**
	 * 监控返回键
	 * @param position
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(isLoading){
				AlertDialog.Builder build=new AlertDialog.Builder(this);
				build.setTitle("退出会暂停下载")
				        .setIcon(android.R.drawable.ic_dialog_info)
						.setMessage("确定要退出吗？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								finish();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						})
						.show();
			}
			else
				finish();
			break;
		default:
			break;
		}
		return false;
		//return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
//		mOffline.pause(cityid);
		// mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mMKOfflineMap != null) {
			mMKOfflineMap.destroy();
		}
//		mMapView.destroy();
//		DemoApplication app = (DemoApplication) this.getApplication();
//		if (app.mBMapManager != null) {
//			app.mBMapManager.destroy();
//			app.mBMapManager = null;
//		}
		super.onDestroy();
	}
	
	

}
