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
	// ����ť ��С��ť ���水ť ��λ��ť
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
	// ��λ���
	private LocationClient mLocationClient;
	//
	// public NotifyLister mNotifyer = null;// ��δ����

	// MyLocationOverlay myLocationOverlay = null;
	private MyLocationOverlay mySelectOverlay = null;//��ɫ�ĵ�Ȧ
	private LocationData mLocationData = null;
	private Location mySelectLocation = null;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(GPSMapActivity.this, "��λ�ɹ�!",
					Toast.LENGTH_SHORT).show();
		};
	};
	
	//�������
	private MKSearch mMKSearch = null;
	private GpsMapSearchListAdapter mSearchListAdapter = null;
	private LinearLayout mapLayout = null;
	private LinearLayout searchListLayout ;
	private RelativeLayout returnMapLayout;
	private ArrayList<MKSuggestionInfo> mMKSuggestionInfoList  = null;
	private ArrayList<MKPoiInfo> mMKPoiInfolist = null;//��������б�
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
		/*��ʼ��BMapManager*/
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(getApplicationContext());
			mBMapManager.init("D27A0E94CE28C67204904596DDF88003B836EA7B", null); // ��ӱ��Key
		}
		setContentView(R.layout.gps_map_layout);
		if (mMapView == null) {
			mMapView = (NewMapView) findViewById(R.id.bmapsView);
		}
		setUnCatchException();
		/*��ʼ���������*/
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
		searchListLayout.setVisibility(8);//���ɼ�
		mapLayout.setVisibility(0);//�ɼ�
		
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
				if(searchCityEdit.getText().length()>0)//��� ��Ϊ�� ������
				{
					if(!SystemUtil.isConnectInternet(getApplicationContext()))
					{
						Toast.makeText(GPSMapActivity.this, "����δ���ӣ������������ú�����",
								Toast.LENGTH_SHORT).show();	
						Log.v("searchButton", "����δ����");
					}
					else
					{
						if(isSearchEditChanged==true)//��� ����ֵ �ı��˾���������
						{
							NUMofMKSuggestion=0;
							mMKSuggestionInfoIndex = 0;
							if(mMKPoiInfolist!=null)
								mMKPoiInfolist.clear();//�����һ�ε�����
							mSearchListAdapter.setListData(mMKPoiInfolist);
							mSearchListAdapter.notifyDataSetChanged();
							mMKSearch.suggestionSearch(searchCityEdit.getText().toString());
							Toast.makeText(GPSMapActivity.this, "�����С�����",
									Toast.LENGTH_SHORT).show();	
							isSearchEditChanged = false;
						}
						else//�������ֵû�б� ����֮ǰ��ֵ
						{
							if(mMKPoiInfolist.size()==0)
							{
								Toast.makeText(GPSMapActivity.this, "δ�ҵ������Ϣ",
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
				Log.v("searchButton", "������");
			}
		});
		
		/*��ʼ�����ݳ�Ա*/
		mySelectData = new LocationData();
		initMapView();
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(new MyLocationListenner());

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(0);//���ö�ʱ��λ��ʱ��������λms ��С��1000ʱֻ����һ�Σ�
		option.disableCache(true);//��ֹʹ�û��涨λ
		option.setPoiNumber(5); // ��෵��POI����
		option.setPoiDistance(1000); // poi��ѯ����
		option.setPoiExtraInfo(true); // �Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ
		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		// if (mMapController == null) {
		mMapController = mMapView.getController();
		// }
		mMapController.setCenter(GEOPOINT);
		mMapController.setZoom(14);//��ͼ�ļ���
		mMapController.enableClick(true);
	

		// myLocationOverlay = new MyLocationOverlay(mMapView);
		mySelectOverlay = new MyLocationOverlay(mMapView);
		mySelectOverlay.enableCompass();//��ָ����
		// mLocationData = new LocationData();
		// myLocationOverlay.setData(mLocationData);
		// mMapView.getOverlays().add(myLocationOverlay);
		mMapView.getOverlays().add(mySelectOverlay);
		// myLocationOverlay.enableCompass();
		
		mMapView.setOnTouchListener(new OnTouchListener() {
			//��ȡ���ص� ��γ��
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mySelectData.latitude = DataApplication.x/1e6;//1E6�Ǹ�����
				mySelectData.longitude = DataApplication.y/1e6;
				mySelectData.direction = -1;//���������Ǵ��صĵ���ɫͼ����Բ��
				mySelectOverlay.setData(mySelectData);
				mMKSearch.reverseGeocode(getSelectGeoPoint());
				mMapView.refresh();
				Log.v("mMapView", "������");
				return false;
			}
		});
		
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {		//�ִ������		
			// mySelectData.latitude =
			// mMapView.getMapCenter().getLatitudeE6()/1e6;
			// mySelectData.longitude =
			// mMapView.getMapCenter().getLongitudeE6()/1e6;
			// mySelectData.direction = -1;
			// mySelectOverlay.setData(mySelectData);
			// mMapView.refresh();
			// Log.v("�ҿ�"+mySelectData.longitude,"�ҿ�");
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
				// ��ͼ

			}
			@Override
			public void onMapAnimationFinish() {
				// ��������ʱ����

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
				Log.v("����뾶",mySelectData.accuracy+"");
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
				Log.v("��С�뾶",mySelectData.accuracy+"");
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
					if (locationName.length() == 0)// ����û�����Ϊ��
					{
						final EditText et = new EditText(GPSMapActivity.this);
						AlertDialog.Builder builder = new Builder(
								GPSMapActivity.this);

						builder.setTitle("������ö�λ����");
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setView(et);
						builder.setPositiveButton("ȷ��", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								locationName = et.getText().toString();
								if (locationName.length() > 0) {
									Location tempLocation = getSelectLocation();
									if (tempLocation != null) {
										LocationService mLocationService = new LocationService();
										mLocationService.add(tempLocation);
										ToastFactory.showToast(GPSMapActivity.this,
												"λ����ӳɹ�������λ��������Ӧ�龰ģʽ");
										Log.v("����λ�óɹ�", mySelectData.accuracy + "");
										DataApplication.lastProfileId = -1;
										Log.v(TAG, "DataApplication.lastProfileId = -1");
										dialog.dismiss();
										finish();
									} else {
										ToastFactory.showToast(GPSMapActivity.this,
												"λ�����ʧ��");
										Log.v("����λ��ʧ��", mySelectData.accuracy + "");
										dialog.dismiss();
									}
								}
								else
									ToastFactory.showToast(GPSMapActivity.this,"λ�����ʧ�ܣ�������λ������");
								dialog.dismiss();
							}
						});
						builder.setNegativeButton("ȡ��", new OnClickListener() {

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
							ToastFactory.showToast(GPSMapActivity.this, "λ����ӳɹ�������λ��������Ӧ�龰ģʽ");
							Log.v("����λ�óɹ�",mySelectData.accuracy+"");
							finish();
						}
						else
						{
							ToastFactory.showToast(GPSMapActivity.this, "λ�����ʧ��");
							Log.v("����λ��ʧ��",mySelectData.accuracy+"");
						}
						
					}
				}
				else
				{
					ToastFactory.showToast(GPSMapActivity.this, "��ͼû��λ�ñ����");
					Log.v("��ͼû��λ�ñ����","getSelectGeoPoint()==null");
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
//				Log.v("MAP "+mGpsService.getmLocationData().latitude, "�ҿ�");
				mLocationClient.requestLocation();
//				GeoPoint point = mMapView.getMapCenter();
//				TextView02.setText(point.getLatitudeE6() + " "
//						+ point.getLongitudeE6());
			}
			
		});
		radiusSeekBar.setProgress((int)(mySelectData.accuracy)); // �����϶����ĵ�ǰֵ

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
				Log.v("�ı�뾶",mySelectData.accuracy+"");
				
			}
		});
		mMapView.refresh();
		
		/*��������*/
		mySelectLocation = new Location();//��ѡ��ĵ���λ��
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
				Log.v("onGetSuggestionResult", "����");
				if(error==0)
				{
					mMKSuggestionInfoList = result.getAllSuggestions();
					if(mMKSuggestionInfoList!=null)
					{
						NUMofMKSuggestion = result.getSuggestionNum();//��Ϣ������
						for(int i=0;i<NUMofMKSuggestion;i++)
						{
							//ֱ���ҵ�һ��city��Ϊ�յ�Ϊֹ
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
						if(isFind==false)//���û���ҵ�
						{
							mMKSearch.suggestionSearch(mMKSuggestionInfo.key);
							Log.v("mMKSuggestionInfo.key","��������");
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
						Toast.makeText(GPSMapActivity.this, "δ�ҵ������Ϣ",
								Toast.LENGTH_SHORT).show();	
						Log.v("onGetSuggestionResult", "δ�ҵ������Ϣ");
					}	
				}
			}
			
			@Override
			public void onGetPoiResult(MKPoiResult result,int type,int iError) {
				// TODO Auto-generated method stub
				Log.v("onGetPoiResult", "����");
				if (result.getCurrentNumPois() > 0)
					mMKPoiInfolist.addAll(result.getAllPoi());
				if (mMKPoiInfolist == null&&mMKSuggestionInfoIndex>=NUMofMKSuggestion)//�����������ǿյ�
				{
					Log.v("onGetPoiResult", "δ�ҵ������Ϣ");
					Toast.makeText(GPSMapActivity.this, "δ�ҵ������Ϣ",Toast.LENGTH_SHORT).show();
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
							// ֱ���ҵ�һ��city��Ϊ�յ�Ϊֹ
							MKSuggestionInfo mMKSuggestionInfo = mMKSuggestionInfoList
									.get(mMKSuggestionInfoIndex++);
							if (mMKSuggestionInfo.city.length() > 0)
							{
								Log.v(mMKSuggestionInfo.city+"", mMKSuggestionInfo.key+"");
								if(searchListLayout.getVisibility()==View.VISIBLE)//��� �б�ɼ��ͼ�������
									mMKSearch.poiSearchInCity(mMKSuggestionInfo.city,
										mMKSuggestionInfo.key);
								else
								{
									Log.v("onGetPoiResult", "������ǰ����");
									return;
								}
								break;
							}	
						};
					} 
					else {
						NUMofMKSuggestion = 0;
						mMKSuggestionInfoIndex = 0;
						Toast.makeText(GPSMapActivity.this, "�������",
								Toast.LENGTH_SHORT).show();	
						Log.v("onGetPoiResult", "�������");
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
					Toast.makeText(GPSMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
		        }

//				routeOverlay = new RouteOverlay(GPSMapActivity.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
				
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
				Toast.makeText(GPSMapActivity.this, "�Ѿ��ƶ��� "+result.getBusName()+" ��ʼ��վ",
						Toast.LENGTH_LONG).show();	
				mySelectLocation.setAddress(result.getBusName()+"ʼ��վ");
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
					Toast.makeText(GPSMapActivity.this, "�Ѿ��ƶ��� "+mMKPoiInfo.city+mMKPoiInfo.name,
							Toast.LENGTH_LONG).show();
					mySelectLocation.setAddress(mMKPoiInfo.city+mMKPoiInfo.name+"");
				}
				else
				{
					if ( 2 == mMKPoiInfo.ePoiType ||4 == mMKPoiInfo.ePoiType ) {
                        /** poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·**/
                        mMKSearch.busLineSearch(mMKPoiInfo.city , mMKPoiInfo.uid);
                    }
//					Toast.makeText(GPSMapActivity.this, "������·��������ʾ",
//							Toast.LENGTH_SHORT).show();	
//					Log.v("����·��", "δ��ʾ");
				}
					
				
				mapLayout.setVisibility(0);
				searchListLayout.setVisibility(8);
			}
		});

	}

	/**
	 * ����λ�õ�����
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
	 * ����λ��
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
	 * ��ͼ�ĳ�ʼ��
	 */
	private void initMapView() {
		mMapView.setLongClickable(true);
		mMapView.setBuiltInZoomControls(true);
		// mMapController.setMapClickEnable(true);
		// mMapView.setSatellite(false);
	}

	/**
	 * ��������������λ�õ�ʱ�򣬸�ʽ�����ַ������������Ļ��
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
			// ����һ����TAG
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

	public class NotifyLister extends BDNotifyListener {// TODO ��δ����
		@Override
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}
	
	/**
	 * ��ط��ؼ�
	 * @param position
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(searchListLayout.getVisibility()==View.VISIBLE)//��������ɼ�
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
