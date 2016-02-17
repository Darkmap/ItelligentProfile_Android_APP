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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.adapter.GpsMapSearchListAdapter;
import com.daocaowu.itelligentprofile.bean.Location;
import com.daocaowu.itelligentprofile.service.LocationService;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.SystemUtil;
import com.daocaowu.itelligentprofile.utils.ToastFactory;
import com.daocaowu.itelligentprofile.view.NewMapView;

public class GPSMapActivity extends Activity {
	
	final private GeoPoint GEOPOINT = new GeoPoint(23057113, 113407869);

	private static final String TAG = "GPSMapFragment";
	private BMapManager mBMapManager = null;
	// 扩大按钮 缩小按钮 保存按钮 定位按钮
	private Button enlargeButton, diminishButton, saveButton, locationButton,
			OfflineMapButton;
//	TextView TextView01, TextView02;
	private SeekBar radiusSeekBar;
	private EditText editName,searchCityEdit;
	private ListView searchList_view;
	private Button searchButton ;
	private String locationName = "";

	private NewMapView mMapView = null;

	public static MapController mMapController = null;
//	public static MapController mStaticMapController = null;

	public MKMapViewListener mMapListener = null;
	private FrameLayout mMapViewContainer = null;

	private LocationData mySelectData;
	// 定位相关
	private LocationClient mLocationClient;
	//
	// public NotifyLister mNotifyer = null;// 还未用上

	// MyLocationOverlay myLocationOverlay = null;
	private MyLocationOverlay mySelectOverlay = null;//蓝色的点圈
	private LocationData mLocationData = null;
	private Location mySelectLocation = null;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(GPSMapActivity.this, "定位成功!",
					Toast.LENGTH_SHORT).show();
		};
	};
	
	//搜索相关
	private MKSearch mMKSearch = null;
	private GpsMapSearchListAdapter mSearchListAdapter = null;
	private LinearLayout mapLayout = null;
	private LinearLayout searchListLayout ;
	private RelativeLayout returnMapLayout;
	private ArrayList<MKSuggestionInfo> mMKSuggestionInfoList  = null;
	private ArrayList<MKPoiInfo> mMKPoiInfolist = null;//搜索结果列表
	private int NUMofMKSuggestion = 0;
	private int mMKSuggestionInfoIndex = 0;
	private boolean isSearchEditChanged = false;
//	private RouteOverlay routeOverlay = null;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*初始化BMapManager*/
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(getApplicationContext());
			mBMapManager.init("D27A0E94CE28C67204904596DDF88003B836EA7B", null); // 家颖的Key
		}
		setContentView(R.layout.gps_map_layout);
		if (mMapView == null) {
			mMapView = (NewMapView) findViewById(R.id.bmapsView);
		}
		setUnCatchException();
		/*初始化布局组件*/
		enlargeButton = (Button) findViewById(R.id.enlargeButton);
		diminishButton = (Button) findViewById(R.id.diminishButton);
		saveButton = (Button) findViewById(R.id.saveButton);
		locationButton = (Button) findViewById(R.id.locationButton);
		OfflineMapButton =  (Button) findViewById(R.id.OfflineMapButton);
        //		TextView01 = (TextView) findViewById(R.id.TextView01);
        //		TextView02 = (TextView) findViewById(R.id.TextView02);
		radiusSeekBar = (SeekBar) findViewById(R.id.radiusSeekBar);
		editName = (EditText) findViewById(R.id.editName);
		searchList_view = (ListView) findViewById(R.id.searchList_view);
		searchButton = (Button) findViewById(R.id.searchButton);
		searchCityEdit = (EditText)findViewById(R.id.searchCityEdit);
		mapLayout = (LinearLayout)findViewById(R.id.mapLayout);
		returnMapLayout = (RelativeLayout)findViewById(R.id.returnMapLayout);
		searchListLayout = (LinearLayout)findViewById(R.id.searchListLayout);
		searchListLayout.setVisibility(8);//不可见
		mapLayout.setVisibility(0);//可见
		
		searchCityEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				isSearchEditChanged=true;
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {	
			}
		});
		searchButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(searchCityEdit.getText().length()>0)//如果 不为空 就搜索
				{
					if(!SystemUtil.isConnectInternet(getApplicationContext()))
					{
						Toast.makeText(GPSMapActivity.this, "网络未连接，请检查网络设置后重试",
								Toast.LENGTH_SHORT).show();	
						Log.v("searchButton", "网络未连接");
					}
					else
					{
						if(isSearchEditChanged==true)//如果 输入值 改变了就重新搜索
						{
							NUMofMKSuggestion=0;
							mMKSuggestionInfoIndex = 0;
							if(mMKPoiInfolist!=null)
								mMKPoiInfolist.clear();//清空上一次的数据
							mSearchListAdapter.setListData(mMKPoiInfolist);
							mSearchListAdapter.notifyDataSetChanged();
							mMKSearch.suggestionSearch(searchCityEdit.getText().toString());
							Toast.makeText(GPSMapActivity.this, "搜索中···",
									Toast.LENGTH_SHORT).show();	
							isSearchEditChanged = false;
						}
						else//如果输入值没有变 就拿之前的值
						{
							if(mMKPoiInfolist.size()==0)
							{
								Toast.makeText(GPSMapActivity.this, "未找到相关信息",
										Toast.LENGTH_SHORT).show();	
							}
							else
							{
								searchListLayout.setVisibility(0);
								mapLayout.setVisibility(8);
							}	
						}
					}
					
						
				}
				Log.v("searchButton", "被摸了");
			}
		});
		
		/*初始化数据成员*/
		mySelectData = new LocationData();
		initMapView();
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(new MyLocationListenner());

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);//设置定时定位的时间间隔。单位ms （小于1000时只运行一次）
		option.disableCache(true);//禁止使用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		// if (mMapController == null) {
		mMapController = mMapView.getController();
		// }
		mMapController.setCenter(GEOPOINT);
		mMapController.setZoom(14);//地图的级数
		mMapController.enableClick(true);
	

		// myLocationOverlay = new MyLocationOverlay(mMapView);
		mySelectOverlay = new MyLocationOverlay(mMapView);
		mySelectOverlay.enableCompass();//打开指南针
		// mLocationData = new LocationData();
		// myLocationOverlay.setData(mLocationData);
		// mMapView.getOverlays().add(myLocationOverlay);
		mMapView.getOverlays().add(mySelectOverlay);
		// myLocationOverlay.enableCompass();
		
		mMapView.setOnTouchListener(new OnTouchListener() {
			//获取触控点 经纬度
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mySelectData.latitude = DataApplication.x/1e6;//1E6是浮点数
				mySelectData.longitude = DataApplication.y/1e6;
				mySelectData.direction = -1;//这样可以是触控的的蓝色图标是圆的
				mySelectOverlay.setData(mySelectData);
				mMKSearch.reverseGeocode(getSelectGeoPoint());
				mMapView.refresh();
				Log.v("mMapView", "被摸了");
				return false;
			}
		});
		
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {		//手触控完成		
			// mySelectData.latitude =
			// mMapView.getMapCenter().getLatitudeE6()/1e6;
			// mySelectData.longitude =
			// mMapView.getMapCenter().getLongitudeE6()/1e6;
			// mySelectData.direction = -1;
			// mySelectOverlay.setData(mySelectData);
			// mMapView.refresh();
			// Log.v("我靠"+mySelectData.longitude,"我靠");
			}
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(GPSMapActivity.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onGetCurrentMap(Bitmap b) {
				// 截图

			}
			@Override
			public void onMapAnimationFinish() {
				// 动画结束时调用

			}
		};
		mMapView.regMapViewListener(mBMapManager, mMapListener);
				

		editName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				locationName= s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		enlargeButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mySelectData.accuracy +=100;
				mySelectOverlay.setData(mySelectData);
				radiusSeekBar.setProgress((int)mySelectData.accuracy);
				mMapView.refresh();
				Log.v("扩大半径",mySelectData.accuracy+"");
			}
		});
		diminishButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mySelectData.accuracy -= 100;
				if(mySelectData.accuracy<0)
					mySelectData.accuracy=0;
				radiusSeekBar.setProgress((int)mySelectData.accuracy);
				mySelectOverlay.setData(mySelectData);
				mMapView.refresh();
				Log.v("缩小半径",mySelectData.accuracy+"");
			}
		});
		saveButton.setOnClickListener(new Button.OnClickListener(){
			@Override//TODO
			public void onClick(View v) {
//				if(getSelectGeoPoint()!=null)
//					mMKSearch.reverseGeocode(getSelectGeoPoint());
				
				// TODO Auto-generated method stub
//				GeoPoint point = mMapView.getMapCenter();
//				TextView01.setText(point.getLatitudeE6() + " "
//						+ point.getLongitudeE6() + " "
//						+ mMapView.getLatitudeSpan() + " "
//						+ mMapView.getLongitudeSpan());
				if(getSelectGeoPoint()!=null)
				{
					if (locationName.length() == 0)// 如果用户命名为空
					{
						final EditText et = new EditText(GPSMapActivity.this);
						AlertDialog.Builder builder = new Builder(
								GPSMapActivity.this);

						builder.setTitle("请输入该定位名称");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setView(et);
						builder.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								locationName = et.getText().toString();
								if (locationName.length() > 0) {
									Location tempLocation = getSelectLocation();
									if (tempLocation != null) {
										LocationService mLocationService = new LocationService();
										mLocationService.add(tempLocation);
										ToastFactory.showToast(GPSMapActivity.this,
												"位置添加成功，请点击位置设置相应情景模式");
										Log.v("保存位置成功", mySelectData.accuracy + "");
										DataApplication.lastProfileId = -1;
										Log.v(TAG, "DataApplication.lastProfileId = -1");
										dialog.dismiss();
										finish();
									} else {
										ToastFactory.showToast(GPSMapActivity.this,
												"位置添加失败");
										Log.v("保存位置失败", mySelectData.accuracy + "");
										dialog.dismiss();
									}
								}
								else
									ToastFactory.showToast(GPSMapActivity.this,"位置添加失败，请输入位置名称");
								dialog.dismiss();
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
					else
					{
						Location tempLocation = getSelectLocation();
						if (tempLocation != null) {
							LocationService mLocationService = new LocationService();
							mLocationService.add(tempLocation);
							ToastFactory.showToast(GPSMapActivity.this, "位置添加成功，请点击位置设置相应情景模式");
							Log.v("保存位置成功",mySelectData.accuracy+"");
							finish();
						}
						else
						{
							ToastFactory.showToast(GPSMapActivity.this, "位置添加失败");
							Log.v("保存位置失败",mySelectData.accuracy+"");
						}
						
					}
				}
				else
				{
					ToastFactory.showToast(GPSMapActivity.this, "地图没有位置被标记");
					Log.v("地图没有位置被标记","getSelectGeoPoint()==null");
				}
				
				
			}
		});
		OfflineMapButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GPSMapActivity.this, LoadOfflineMapActivity.class);
				startActivity(intent);
			}
		});
		locationButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
//				GPSService mGpsService = new GPSService(GPSMapActivity.this);
//				mGpsService.locationClientStart();
//				TextView02.setText(mGpsService.getmLocationData().latitude+" "+mGpsService.getmLocationData().longitude);
//				Log.v("MAP "+mGpsService.getmLocationData().latitude, "我靠");
				mLocationClient.requestLocation();
//				GeoPoint point = mMapView.getMapCenter();
//				TextView02.setText(point.getLatitudeE6() + " "
//						+ point.getLongitudeE6());
			}
			
		});
		radiusSeekBar.setProgress((int)(mySelectData.accuracy)); // 设置拖动条的当前值

		radiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
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
				// TODO Auto-generated method stub
				mySelectData.accuracy = progress;
				mySelectOverlay.setData(mySelectData);
				mMapView.refresh();
				Log.v("改变半径",mySelectData.accuracy+"");
				
			}
		});
		mMapView.refresh();
		
		/*搜索服务*/
		mySelectLocation = new Location();//我选择的地理位置
		mMKSearch = new MKSearch();
		mSearchListAdapter = new GpsMapSearchListAdapter(null, GPSMapActivity.this);
		searchList_view.setAdapter(mSearchListAdapter);
		mMKSuggestionInfoList = new ArrayList<MKSuggestionInfo>();
		mMKPoiInfolist = new ArrayList<MKPoiInfo>();
//		routeOverlay = new RouteOverlay(GPSMapActivity.this, mMapView);
		
		mMKSearch.init(mBMapManager, new MKSearchListener() {
			
			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetSuggestionResult(MKSuggestionResult result, int error) {
				// TODO Auto-generated method stub
				boolean isFind =false;
				if(mMKSuggestionInfoList!=null)
					mMKSuggestionInfoList.clear();
				MKSuggestionInfo mMKSuggestionInfo = new MKSuggestionInfo();
				Log.v("onGetSuggestionResult", "启动");
				if(error==0)
				{
					mMKSuggestionInfoList = result.getAllSuggestions();
					if(mMKSuggestionInfoList!=null)
					{
						NUMofMKSuggestion = result.getSuggestionNum();//信息的数量
						for(int i=0;i<NUMofMKSuggestion;i++)
						{
							//直到找到一个city不为空的为止
							mMKSuggestionInfo = result.getSuggestion(mMKSuggestionInfoIndex);
							mMKSuggestionInfoIndex++;
							if(mMKSuggestionInfo.city.length()>0)
							{
								mapLayout.setVisibility(8);
								searchListLayout.setVisibility(0);
								Log.v(mMKSuggestionInfo.city+"", mMKSuggestionInfo.key+"");
								mMKSearch.poiSearchInCity(mMKSuggestionInfo.city, mMKSuggestionInfo.key);
								isFind = true;
								break;
							}
						}
						if(isFind==false)//如果没有找到
						{
							mMKSearch.suggestionSearch(mMKSuggestionInfo.key);
							Log.v("mMKSuggestionInfo.key","重新搜索");
							mMKSuggestionInfoIndex = 0;
							NUMofMKSuggestion = 0;
						}
//						for(int i=0;i<NUMofMKSuggestion;i++)
//						{
//							mMKSuggestionInfo = result.getSuggestion(i);
//							if(mMKSuggestionInfo!=null)
//							{
//								if(mMKSuggestionInfo.city.length()==0)
//									;//mMKSearch.suggestionSearch(mMKSuggestionInfo.key);
//								else
//									;//mMKSearch.poiSearchInCity(mMKSuggestionInfo.city, mMKSuggestionInfo.key);
//								Log.v(mMKSuggestionInfo.city+"", mMKSuggestionInfo.key+"");
//							}
//						}
					}
					else
					{
						mapLayout.setVisibility(0);
						searchListLayout.setVisibility(8);
						Toast.makeText(GPSMapActivity.this, "未找到相关信息",
								Toast.LENGTH_SHORT).show();	
						Log.v("onGetSuggestionResult", "未找到相关信息");
					}	
				}
			}
			
			@Override
			public void onGetPoiResult(MKPoiResult result,int type,int iError) {
				// TODO Auto-generated method stub
				Log.v("onGetPoiResult", "启动");
				if (result.getCurrentNumPois() > 0)
					mMKPoiInfolist.addAll(result.getAllPoi());
				if (mMKPoiInfolist == null&&mMKSuggestionInfoIndex>=NUMofMKSuggestion)//如果到了最后还是空的
				{
					Log.v("onGetPoiResult", "未找到相关信息");
					Toast.makeText(GPSMapActivity.this, "未找到相关信息",Toast.LENGTH_SHORT).show();
					mapLayout.setVisibility(0);
					searchListLayout.setVisibility(8);
					return;
				}	
				else
				{
					mSearchListAdapter.setListData(mMKPoiInfolist);
					mSearchListAdapter.notifyDataSetChanged();
					Log.v(mMKSuggestionInfoIndex + ":" + result.getPoi(0).name + "",result.getPoi(0).address + "");
					if (mMKSuggestionInfoIndex < NUMofMKSuggestion) {
						while(mMKSuggestionInfoIndex< NUMofMKSuggestion) {
							// 直到找到一个city不为空的为止
							MKSuggestionInfo mMKSuggestionInfo = mMKSuggestionInfoList
									.get(mMKSuggestionInfoIndex++);
							if (mMKSuggestionInfo.city.length() > 0)
							{
								Log.v(mMKSuggestionInfo.city+"", mMKSuggestionInfo.key+"");
								if(searchListLayout.getVisibility()==View.VISIBLE)//如果 列表可见就继续搜索
									mMKSearch.poiSearchInCity(mMKSuggestionInfo.city,
										mMKSuggestionInfo.key);
								else
								{
									Log.v("onGetPoiResult", "搜索提前结束");
									return;
								}
								break;
							}	
						};
					} 
					else {
						NUMofMKSuggestion = 0;
						mMKSuggestionInfoIndex = 0;
						Toast.makeText(GPSMapActivity.this, "搜索完成",
								Toast.LENGTH_SHORT).show();	
						Log.v("onGetPoiResult", "搜索完成");
					}
				}
			}
			
			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				// TODO Auto-generated method stub
				if (iError != 0 || result == null) {
					Toast.makeText(GPSMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
		        }

//				routeOverlay = new RouteOverlay(GPSMapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
				
//			    routeOverlay.setData(result.getBusRoute());
//			    mMapView.getOverlays().clear();
//			    mMapView.getOverlays().add(routeOverlay);
//			    mMapView.refresh();
				
				GeoPoint tempGeoPoint = result.getBusRoute().getStart();
			    mMapController.animateTo(tempGeoPoint);
			    LocationData tempLocationData = new LocationData();
				tempLocationData.accuracy = mySelectData.accuracy;
				tempLocationData.direction = -1;
				if(tempGeoPoint!=null)
				{
//					mMapView.getOverlays().remove(routeOverlay);
					tempLocationData.latitude = tempGeoPoint.getLatitudeE6()/1E6;
					tempLocationData.longitude = tempGeoPoint.getLongitudeE6()/1E6;
					mySelectOverlay.setData(tempLocationData);
				}
				mMapView.refresh();
				Toast.makeText(GPSMapActivity.this, "已经移动至 "+result.getBusName()+" 的始发站",
						Toast.LENGTH_LONG).show();	
				mySelectLocation.setAddress(result.getBusName()+"始发站");
			}
			
			@Override
			public void onGetAddrResult(MKAddrInfo mMKAddrInfo, int arg1) {
				// TODO Auto-generated method stub
				mySelectLocation.setAddress(mMKAddrInfo.strAddr);
				ToastFactory.showToast(GPSMapActivity.this, mySelectLocation.getAddress());
			}
		});
		returnMapLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mapLayout.setVisibility(0);
				searchListLayout.setVisibility(8);
			}
			
		});
		searchList_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				MKPoiInfo mMKPoiInfo = (MKPoiInfo)mSearchListAdapter.getItem(position);
				LocationData tempLocationData = new LocationData();
				tempLocationData.accuracy = mySelectData.accuracy;
				tempLocationData.direction = -1;
				if(mMKPoiInfo.pt!=null)
				{
//					mMapView.getOverlays().remove(routeOverlay);
					tempLocationData.latitude = mMKPoiInfo.pt.getLatitudeE6()/1E6;
					tempLocationData.longitude = mMKPoiInfo.pt.getLongitudeE6()/1E6;
					mySelectOverlay.setData(tempLocationData);
//					while(mMapView.getZoomLevel()<mMapView.getMaxZoomLevel()-2)
//					{
//						mMapController.zoomIn();
//					}
					mMapController.animateTo(mMKPoiInfo.pt, null);
					mMapView.refresh();
					Toast.makeText(GPSMapActivity.this, "已经移动至 "+mMKPoiInfo.city+mMKPoiInfo.name,
							Toast.LENGTH_LONG).show();
					mySelectLocation.setAddress(mMKPoiInfo.city+mMKPoiInfo.name+"");
				}
				else
				{
					if ( 2 == mMKPoiInfo.ePoiType ||4 == mMKPoiInfo.ePoiType ) {
                        /** poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路**/
                        mMKSearch.busLineSearch(mMKPoiInfo.city , mMKPoiInfo.uid);
                    }
//					Toast.makeText(GPSMapActivity.this, "公交线路：不予显示",
//							Toast.LENGTH_SHORT).show();	
//					Log.v("公交路线", "未显示");
				}
					
				
				mapLayout.setVisibility(0);
				searchListLayout.setVisibility(8);
			}
		});

	}

	/**
	 * 返回位置的数据
	 */
	private LocationData getSelectData() {
		// TODO Auto-generated method stub
		return mySelectOverlay.getMyLocation();
		// latitude
		// longitude
		// accuracy
		// direction
	}

	private GeoPoint getSelectGeoPoint() {
		// TODO Auto-generated method stub
		if(mySelectOverlay.getMyLocation()!=null)
		{
			GeoPoint aGeoPoint = new GeoPoint(
					(int) (mySelectOverlay.getMyLocation().latitude * 1E6),
					(int) (mySelectOverlay.getMyLocation().longitude * 1E6));
			return aGeoPoint;
		}
		return null;
	}
	
	/*
	 * 返回位置
	 */
	private Location getSelectLocation() {
		// TODO Auto-generated method stub
		
		if (mySelectOverlay.getMyLocation() != null) {
			mySelectLocation.setLatitude(mySelectOverlay.getMyLocation().latitude);
			mySelectLocation.setLongitude(mySelectOverlay.getMyLocation().longitude);
			mySelectLocation.setRadius(mySelectOverlay.getMyLocation().accuracy);
			mySelectLocation.setLocationName(locationName);
//			mySelectLocation.setAddress(this.address); 
		}
		return mySelectLocation;
	}

	@Override
	protected void onPause(){
        mMapView.onPause();
//        if(mBMapManager!=null){
//        	mBMapManager.stop();
//        }
        super.onPause();
}

	@Override
	protected void onResume(){
        mMapView.onResume();
//        if(mBMapManager!=null){
//        	mBMapManager.start();
//        }
        super.onResume();
}
	@Override
	protected void onDestroy() {
		if (mLocationClient != null)
			mLocationClient.stop();
		mMapView.destroy();
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 地图的初始化
	 */
	private void initMapView() {
		mMapView.setLongClickable(true);
		mMapView.setBuiltInZoomControls(true);
		// mMapController.setMapClickEnable(true);
		// mMapView.setSatellite(false);
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			mySelectData.latitude = location.getLatitude();
			mySelectData.longitude = location.getLongitude();
			mySelectData.accuracy = location.getRadius();
			mySelectData.direction = location.getDerect();
			// mLocationData.latitude = location.getLatitude();
			// mLocationData.longitude = location.getLongitude();
			// mLocationData.accuracy = location.getRadius();
			// mLocationData.direction = location.getDerect();
			// myLocationOverlay.setData(mLocationData);
			radiusSeekBar.setProgress((int)mySelectData.accuracy);
			mySelectOverlay.setData(mySelectData);
			mMapView.refresh();
			if(getSelectGeoPoint()!=null)
				mMKSearch.reverseGeocode(getSelectGeoPoint());
//			mySelectLocation.setAddress(location.getAddrStr());
//			if(location.getAddrStr()!=null)
//			{
//				Log.v("location.getAddrStr()", location.getAddrStr()+"");
//				Toast.makeText(GPSMapActivity.this, location.getAddrStr()+"",
//						Toast.LENGTH_SHORT).show();	
//			}
			mMapController.animateTo(getSelectGeoPoint(), mHandler
					.obtainMessage(1));
//			TextView02.setText((int) (mySelectData.latitude * 1e6) + " "
//					+ (int) (mySelectData.longitude * 1e6));
			// 以下一下是TAG
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			sb.append("\nderect : ");
			sb.append(location.getDerect());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			Log.v(TAG, sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			Log.v(TAG, sb.toString());
		}
	}

	public class NotifyLister extends BDNotifyListener {// TODO 还未用上
		@Override
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}
	
	/**
	 * 监控返回键
	 * @param position
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(searchListLayout.getVisibility()==View.VISIBLE)//搜索结果可见
			{
				searchListLayout.setVisibility(8);
				mapLayout.setVisibility(0);
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
	
}
