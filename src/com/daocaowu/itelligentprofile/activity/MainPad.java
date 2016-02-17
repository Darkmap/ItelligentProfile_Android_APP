package com.daocaowu.itelligentprofile.activity;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.fragment.MenuFragment;
import com.daocaowu.itelligentprofile.fragment.ViewPagerFragment;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;
import com.slidingmenu.lib.app2.SlidingFragmentActivity;
import com.slidingmenu.lib2.SlidingMenu;

public class MainPad extends SlidingFragmentActivity {
	public static MainPad instance = null;
	private static final String TAG = "MainPad";
	
//	public static LocationClient mLocationClient = null;
	public SlidingMenu slidingMenu = null;
	public int whatOfShowing = 1;
	
	PendingIntent m_restartIntent;
	String RESTART_INTENT_KEY = "restart";
	int CRASHED_CODE = -1;
	private UncaughtExceptionHandler m_handler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.d(TAG, "uncaught exception is catched!");
			AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2500,
					m_restartIntent);
			System.exit(2);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.homepad);
		instance = this;
		setUnCatchException();
//		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//	    mLocationClient.registerLocationListener( new MyLocationListener() );    //注册监听函数
		
//		taskLocalService = new TaskService(this);
//		mTaskReceiver = new TaskReceiver();
//		initList();
//		
//		// 启动activity时不自动弹出软键盘
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		DataApplication.profilelist.add(new Profile());
		//初始化所有界面
//		initViews();
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.frame_content);
//		setContentView(R.layout.homepad);
     // set the Behind View
        setBehindContentView(R.layout.frame_menu);
//        textView=(TextView)findViewById(R.id.titleText);
//        textView.setText("SlidingMenu Demo");
//        textView.setOnClickListener(this);
		initFragments();
	}
	
	private void setUnCatchException(){
		Intent intent = getIntent();
		int code = intent.getIntExtra(RESTART_INTENT_KEY, 0);
		if (CRASHED_CODE == code) {
			/** You can do something here. */
			finish();
			Log.d(TAG, "So sorry that the application crashed.");
		}
		intent.putExtra(RESTART_INTENT_KEY, CRASHED_CODE);
		m_restartIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		Thread.setDefaultUncaughtExceptionHandler(m_handler);
	}
	

	/**
	 * 初始化Fragment
	 */
	private void initFragments(){
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		MenuFragment menuFragment = new MenuFragment();
		fragmentTransaction.replace(R.id.menu, menuFragment);
//		ContentFragment contentFragment = new ContentFragment("wweecclloome");
//		fragmentTransaction.replace(R.id.f_content, contentFragment, "Welcome");
		ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
		fragmentTransaction.replace(R.id.f_content, viewPagerFragment);
//		ContentFragment contentFragment = new ContentFragment("wweecclloome");
//		fragmentTransaction.replace(R.id.content, contentFragment, "Welcome");
		fragmentTransaction.commit();
		
		initSlidingMenu();
	}
	
	/**
	 * 初始化SlidingMenu
	 */
	private void initSlidingMenu(){
		slidingMenu = getSlidingMenu();
		slidingMenu.setShadowWidth(50);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffset(80);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setBehindScrollScale(0);
		//设置slding menu的几种手势模式
        //TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
        //TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,需要在屏幕边缘滑动才可以打开slding menu
        //TOUCHMODE_NONE 不能通过手势打开
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		/*//设置是左滑还是右滑，还是左右都可以滑
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		//设置阴影宽度
		slidingMenu.setShadowWidth(getWindowManager().getDefaultDisplay().getWidth() / 40);
		//设置左菜单阴影图片
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		//设置右菜单阴影图片
		slidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);
		//设置菜单占屏幕的比例
		slidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 5);
		//设置滑动时菜单的是否淡入淡出
		slidingMenu.setFadeEnabled(true);
		//设置淡入淡出的比例
		slidingMenu.setFadeDegree(0.4f);
		//设置滑动时拖拽效果
		slidingMenu.setBehindScrollScale(0);
		//设置要使菜单滑动，触碰屏幕的范围
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);*/
	}
	
	/************************************设置各个view上的响应函数*******************************************/
	// 设置每个view上的按键的函数
	public void addprofile(View v) {
		Log.v("addprofile-mainPan.this", "OK");
		Intent intent = new Intent(MainPad.this, AddProfileActivity.class);
		/****智超要求改的******/
		intent.putExtra("action", 1);
		try {
			AddProfileActivity.currentprofile = SwitchProfileUtil.getProfile(this);
			if(AddProfileActivity.currentprofile!=null)
				startActivity(intent);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**添加一个Task
	 * @param v
	 */
	public void addtask(View v) {
		Intent intent = new Intent(MainPad.this, AddTask.class);
		intent.putExtra("action", 0);
		intent.putExtra("dayofweek", ViewPagerFragment.getCurrIndex());
		startActivity(intent);
		Log.v("星期", String.valueOf(ViewPagerFragment.getCurrIndex()));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 获取
			if (ViewPagerFragment.getCurrIndex() != 0) {
				ViewPagerFragment.getmTabPager().setCurrentItem(0);
				ViewPagerFragment.setCurrIndex(0);
				return true;
			}
		}
		// 按下HOME键
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			moveTaskToBack(true);
			return true;
		}
    	return false;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && getSlidingMenu().isMenuShowing()) {
			showContent();
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_BACK && !getSlidingMenu().isMenuShowing()) {
			showMenu();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
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
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
	 
			Log.v(TAG, sb.toString());
		}
	@Override
	public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ;
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
				if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\naddr : ");
					sb.append(poiLocation.getAddrStr());
				} 
				if(poiLocation.hasPoi()){
					sb.append("\nPoi:");
					sb.append(poiLocation.getPoi());
				}else{				
					sb.append("noPoi information");
				}
				Log.v(TAG, sb.toString());
			}
	}
	
	
	/***************************PageView组件********************************************/
	/*private ViewPager mTabPager;
	private ImageView mTab0,mTab1, mTab2, mTab3, mTab4, mTab5,mTab6, mTab7;
	private int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private int four;
	private int five;
	private int six;
	private int seven;

	private long quitTime = 0;
	
	//8个滑动的View
	View view0;
	View view1;
	View view2;
	View view3;
	View view4;
	View view5;
	View view6;
	View view7;
	
	*//***************************View0组件********************************************//*
	private GridView profilelist_view;
	private ProfileListAdapter profileadapter;
	
	*//***************************View1-7组件********************************************//*
	
	private ImageView dayofweek_1;
	private ListView calendarlist_view_1;
	private CalendarListAdapter calendaradapter_1;
	
	private ImageView dayofweek_2;
	private ListView calendarlist_view_2;
	private CalendarListAdapter calendaradapter_2;
	
	private ImageView dayofweek_3;
	private ListView calendarlist_view_3;
	private CalendarListAdapter calendaradapter_3;
	
	private ImageView dayofweek_4;
	private ListView calendarlist_view_4;
	private CalendarListAdapter calendaradapter_4;
	
	private ImageView dayofweek_5;
	private ListView calendarlist_view_5;
	private CalendarListAdapter calendaradapter_5;
	
	private ImageView dayofweek_6;
	private ListView calendarlist_view_6;
	private CalendarListAdapter calendaradapter_6;
	
	private ImageView dayofweek_7;
	private ListView calendarlist_view_7;
	private CalendarListAdapter calendaradapter_7;
	
	
	
	private AbstractLocalService taskLocalService;
	private TaskReceiver mTaskReceiver;
	
	private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
             switch (msg.what) {
                  case 0:   
                	  profileadapter.notifyDataSetChanged();
                       break;
                  case 1:
                	  Log.v("HANDLE", "收到1");
                	  inittaskList();
                	  if (calendaradapter_1 != null) {
                		  calendaradapter_1.notifyDataSetChanged();
					  }
                	  if (calendaradapter_2 != null) {
                		  calendaradapter_2.notifyDataSetChanged();
					  }
                	  if (calendaradapter_3 != null) {
                		  calendaradapter_3.notifyDataSetChanged();
					  }
                	  if (calendaradapter_4 != null) {
                		  calendaradapter_4.notifyDataSetChanged();
					  }
                	  if (calendaradapter_5 != null) {
                		  calendaradapter_5.notifyDataSetChanged();
					  }
                	  if (calendaradapter_6 != null) {
                		  calendaradapter_6.notifyDataSetChanged();
					  }
                	  if (calendaradapter_7 != null) {
                		  calendaradapter_7.notifyDataSetChanged();
					  }
                       break;
                   //case 8:是全部刷新
                  case 8:
                	  //TODO 
                	  inittaskList();
                	  if (profileadapter != null) {
                		  profileadapter.notifyDataSetChanged();
					  }
                	  if (calendaradapter_1 != null) {
                		  calendaradapter_1.notifyDataSetChanged();
					  }
                	  if (calendaradapter_2 != null) {
                		  calendaradapter_2.notifyDataSetChanged();
					  }
                	  if (calendaradapter_3 != null) {
                		  calendaradapter_3.notifyDataSetChanged();
					  }
                	  if (calendaradapter_4 != null) {
                		  calendaradapter_4.notifyDataSetChanged();
					  }
                	  if (calendaradapter_5 != null) {
                		  calendaradapter_5.notifyDataSetChanged();
					  }
                	  if (calendaradapter_6 != null) {
                		  calendaradapter_6.notifyDataSetChanged();
					  }
                	  if (calendaradapter_7 != null) {
                		  calendaradapter_7.notifyDataSetChanged();
					  }
                       break;  
             }   
             super.handleMessage(msg);   
        }
   };
*/	

	
	
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//		Message message = new Message();   
//        message.what = 8;
//        myHandler.sendMessage(message);
//	}

	
	/*
	private void initViews() {
		initPageViews();
		initView0();
		initView1();
		initView2();
		initView3();
		initView4();
		initView5();
		initView6();
		initView7();
	}



	*//**
	 * View0的初始化
	 *//*
	private void initView0() {
		profilelist_view = (GridView) view0.findViewById(R.id.profile_list); 
		profileadapter = new ProfileListAdapter(DataApplication.profilelist, this);
		profilelist_view.setAdapter(profileadapter);
		
//		view0.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				toggle();
//			}
//		});
		
		profilelist_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定切换情景模式吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SwitchProfileUtil.setProfile(getApplicationContext(),DataApplication.profilelist.get(index));
							}
						}).setNegativeButton("否", null).show();
				
			}
		});
		
		profilelist_view.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String[] act ={"编辑","删除","设为默认"};
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this).setTitle("选择操作")
				.setItems(act, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(MainPad.this, AddProfileActivity.class);
							intent.putExtra("action", 1);
							intent.putExtra("index", index);
							//深复制
							AddProfileActivity.currentprofile = new Profile(DataApplication.profilelist.get(index));
							if(AddProfileActivity.currentprofile!=null)
								Log.v("list中getVibrate", DataApplication.profilelist.get(index).getVibrate()+"");
								Log.v("静态对象中getVibrate", AddProfileActivity.currentprofile.getVibrate()+"");
								startActivity(intent);
							break;
						case 1:
//							DataApplication.profilelist.remove(index);
							if(DataApplication.profilelist.get(index).getIsdefault()==0){
								AbstractLocalService localService = new ProfileService();
								localService.delete(DataApplication.profilelist.get(index).getProfileId());
								
								TaskService.deleteByProfileId(DataApplication.profilelist.get(index).getProfileId());
								
								if(DataApplication.profilelist==null)
									DataApplication.profilelist = new ArrayList<Profile>();
								else
									DataApplication.profilelist.clear();
								DataApplication.profilelist.addAll(localService.check());
								
								Message message = new Message();   
						        message.what = 8; 
						        myHandler.sendMessage(message);
							}
							else ToastFactory.showToast(getApplicationContext(),"不能删除默认情景模式");
							break;
							
						case 2:
							
							//找出原来那个默认的设置为0
							AbstractLocalService localService2 = new ProfileService();
							int length = DataApplication.profilelist.size();
							for(int i=0;i<length;i++){
								Profile old = DataApplication.profilelist.get(i);
								if(old.getIsdefault()==1){
									old.setIsdefault(0);
									localService2.add(old);
								}
							}
							//把新的默认模式标志设置为1
							Profile thisprofile = DataApplication.profilelist.get(index);
							thisprofile.setIsdefault(1);
							localService2.add(thisprofile);
							
							if(DataApplication.profilelist==null)
								DataApplication.profilelist = new ArrayList<Profile>();
							else
								DataApplication.profilelist.clear();
							DataApplication.profilelist.addAll(localService2.check());
							Log.v("默认模式修改", String.valueOf(DataApplication.profilelist.size()));
							
							Message message2 = new Message();
					        message2.what = 0; 
					        myHandler.sendMessage(message2);
							
							break;
							
						default:
							break;
						}
					}
				}).show();
				
				
				return false;
			}
		});
		
	}

	
	
	
	*//**
	 * View1初始化
	 *//*
	private void initView1() {
		
		
		dayofweek_1 = (ImageView) view1.findViewById(R.id.cover);
		dayofweek_1.setImageDrawable(getResources().getDrawable(
				R.drawable.monday));
		
		
		calendarlist_view_1 = (ListView) view1.findViewById(R.id.calender_list);
		calendaradapter_1 = new CalendarListAdapter(DataApplication.mondaylist, this);
		calendarlist_view_1.setAdapter(calendaradapter_1);
		
		calendarlist_view_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.mondaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_1.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Log.v("来自calendarlist_view_1,mondaylist", String.valueOf(index));
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.mondaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 8; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}
	
	
	
	
	*//**
	 * View2初始化
	 *//*
	private void initView2() {
		
		dayofweek_2 = (ImageView) view2.findViewById(R.id.cover);
		dayofweek_2.setImageDrawable(getResources().getDrawable(
				R.drawable.tuesday));
		
		calendarlist_view_2 = (ListView) view2.findViewById(R.id.calender_list);
		calendaradapter_2 = new CalendarListAdapter(DataApplication.tuesdaylist, this);
		calendarlist_view_2.setAdapter(calendaradapter_2);
		
		calendarlist_view_2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.tuesdaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_2.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.tuesdaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 1; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}

	
	
	
	*//**
	 * View3初始化
	 *//*
	private void initView3() {
		
		dayofweek_3 = (ImageView) view3.findViewById(R.id.cover);
		dayofweek_3.setImageDrawable(getResources().getDrawable(
				R.drawable.wednesday));
		
		calendarlist_view_3 = (ListView) view3.findViewById(R.id.calender_list);
		calendaradapter_3 = new CalendarListAdapter(DataApplication.wednesdaylist, this);
		calendarlist_view_3.setAdapter(calendaradapter_3);
		
		calendarlist_view_3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.wednesdaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_3.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.wednesdaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 1; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}
	
	
	
	*//**
	 * View4初始化
	 *//*
	private void initView4() {
		
		dayofweek_4 = (ImageView) view4.findViewById(R.id.cover);
		dayofweek_4.setImageDrawable(getResources().getDrawable(
				R.drawable.thursday));
		
		calendarlist_view_4 = (ListView) view4.findViewById(R.id.calender_list);
		calendaradapter_4 = new CalendarListAdapter(DataApplication.thursdaylist, this);
		calendarlist_view_4.setAdapter(calendaradapter_4);
		
		calendarlist_view_4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.thursdaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_4.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.thursdaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 1; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}
	
	
	
	
	*//**
	 * View5初始化
	 *//*
	private void initView5() {
		
		dayofweek_5 = (ImageView) view5.findViewById(R.id.cover);
		dayofweek_5.setImageDrawable(getResources().getDrawable(
				R.drawable.friday));
		
		
		calendarlist_view_5 = (ListView) view5.findViewById(R.id.calender_list);
		calendaradapter_5 = new CalendarListAdapter(DataApplication.fridaylist, this);
		calendarlist_view_5.setAdapter(calendaradapter_5);
		
		calendarlist_view_5.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.fridaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_5.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.fridaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 1; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}
	
	
	
	*//**
	 * View6初始化
	 *//*
	private void initView6() {
		
		dayofweek_6 = (ImageView) view6.findViewById(R.id.cover);
		dayofweek_6.setImageDrawable(getResources().getDrawable(
				R.drawable.saturday));
		
		calendarlist_view_6 = (ListView) view6.findViewById(R.id.calender_list);
		calendaradapter_6 = new CalendarListAdapter(DataApplication.saturdaylist, this);
		calendarlist_view_6.setAdapter(calendaradapter_6);
		
		calendarlist_view_6.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.saturdaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_6.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.saturdaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 1; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}
	
	
	
	
	*//**
	 * View7初始化
	 *//*
	private void initView7() {
		
		dayofweek_7 = (ImageView) view7.findViewById(R.id.cover);
		dayofweek_7.setImageDrawable(getResources().getDrawable(
				R.drawable.sunday));
		
		calendarlist_view_7 = (ListView) view7.findViewById(R.id.calender_list);
		calendaradapter_7 = new CalendarListAdapter(DataApplication.sundaylist, this);
		calendarlist_view_7.setAdapter(calendaradapter_7);
		
		calendarlist_view_7.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.sundaylist.get(arg2));
				
				Intent intent = new Intent(MainPad.this, AddTask.class);
				intent.putExtra("action", 1);
				intent.putExtra("dayofweek", currIndex);
				startActivity(intent);
				Log.v("星期", String.valueOf(currIndex));
			}
		});
		
		calendarlist_view_7.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final int index = arg2;
				new AlertDialog.Builder(MainPad.this)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(MainPad.this);
								taskLocalService.delete(DataApplication.sundaylist.get(index).getTaskId());
			                	
								Message message = new Message();   
						        message.what = 1; 
						        myHandler.sendMessage(message);
							}
						}).setNegativeButton("否", null).show();
				
				return false;
			}
		});
		
	}
	
	
	
	
	
	

	*//**
	 * 控件的初始化
	 *//*
	private void initPageViews() {
		mTabPager = (ViewPager) findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mTab0 = (ImageView) findViewById(R.id.profile_setting);
		mTab1 = (ImageView) findViewById(R.id.Monday);
		mTab2 = (ImageView) findViewById(R.id.Tuesday);
		mTab3 = (ImageView) findViewById(R.id.Wednesday);
		mTab4 = (ImageView) findViewById(R.id.Thursday);
		mTab5 = (ImageView) findViewById(R.id.Friday);
		mTab6 = (ImageView) findViewById(R.id.Saturday);
		mTab7 = (ImageView) findViewById(R.id.Sunday);

		mTab0.setOnClickListener(myClickListener);
		mTab1.setOnClickListener(myClickListener);
		mTab2.setOnClickListener(myClickListener);
		mTab3.setOnClickListener(myClickListener);
		mTab4.setOnClickListener(myClickListener);
		mTab5.setOnClickListener(myClickListener);
		mTab6.setOnClickListener(myClickListener);
		mTab7.setOnClickListener(myClickListener);

		one = DataApplication.getScreenWidth() / 8; // 设置水平动画平移大小
		two = one * 2;
		three = one * 3;
		four = one * 4;
		five = one * 5;
		six = one * 6;
		seven = one * 7;
		
		// Log.i("info", "获取的屏幕分辨率为" + one + two + three + "X" + displayHeight);

		// InitImageView();//使用动画
		// 将要分页显示的View装入数组中
		LayoutInflater mLi = LayoutInflater.from(this);
		
		view0 = mLi.inflate(R.layout.setting_view, null);
		view1 = mLi.inflate(R.layout.calenderview, null);
		view2 = mLi.inflate(R.layout.calenderview, null);
		view3 = mLi.inflate(R.layout.calenderview, null);
		view4 = mLi.inflate(R.layout.calenderview, null);
		view5 = mLi.inflate(R.layout.calenderview, null);
		view6 = mLi.inflate(R.layout.calenderview, null);
		view7 = mLi.inflate(R.layout.calenderview, null);

		// 每个页面的view数据
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view0);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		views.add(view6);
		views.add(view7);

		// 填充ViewPager的数据适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		
		mTabPager.setAdapter(mPagerAdapter);
		
		

	}
	

	
	
	
	
	*//**************************ViewPager相关***********************************************************//*
	
	private OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.profile_setting:
				mTabPager.setCurrentItem(0);
				break;
			case R.id.Monday:
				mTabPager.setCurrentItem(1);
				break;
			case R.id.Tuesday:
				mTabPager.setCurrentItem(2);
				break;
			case R.id.Wednesday:
				mTabPager.setCurrentItem(3);
				break;
			case R.id.Thursday:
				mTabPager.setCurrentItem(4);
				break;
			case R.id.Friday:
				mTabPager.setCurrentItem(5);
				break;
			case R.id.Saturday:
				mTabPager.setCurrentItem(6);
				break;
			case R.id.Sunday:
				mTabPager.setCurrentItem(7);
				break;

			default:
				break;
			}
		}
	};
	
	private void setImgAndAnimation(ImageView targetImageView,
			ImageView lastImageView, int targetDrawable, int lastDrawable,
			int last, int target) {
		lastImageView.setImageDrawable(getResources().getDrawable(
				lastDrawable));
		targetImageView.setImageDrawable(getResources().getDrawable(
				targetDrawable));
		Animation animation = new TranslateAnimation(last, target, 0, 0);

		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(150);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int location) {
			switch (location) {
			case 0:
				if (currIndex == 1) {
					setImgAndAnimation(mTab0, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, 0);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab0, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, 0);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab0, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, 0);
				} else if (currIndex == 4) {
					setImgAndAnimation(mTab0, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, 0);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab0, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, 0);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab0, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, 0);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab0, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, 0);
					Log.v("路径", "从7-0");
				}
				break;
			case 1:
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab1, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, one);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab1, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, one);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab1, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, one);
				} else if (currIndex == 4) {
					setImgAndAnimation(mTab1, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, one);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab1, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, one);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab1, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, one);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab1, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, one);
				}
				break;
			case 2:

				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab2, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, two);
				} else if (currIndex == 1) {
					setImgAndAnimation(mTab2, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, two);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab2, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, two);
				} else if (currIndex == 4) {
					setImgAndAnimation(mTab2, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, two);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab2, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, two);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab2, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, two);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab2, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, two);
				}
				break;
			case 3:
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab3, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, three);
				} else if (currIndex == 1) {
					setImgAndAnimation(mTab3, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, three);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab3, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, three);
				} else if (currIndex == 4) {
					setImgAndAnimation(mTab3, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, three);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab3, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, three);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab3, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, three);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab3, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, three);
				}
				break;
			case 4:
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab4, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, four);
				} else if (currIndex == 1) {
					setImgAndAnimation(mTab4, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, four);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab4, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, four);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab4, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, four);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab4, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, four);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab4, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, four);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab4, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, four);
				}
				break;
			case 5:
				mTab5.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab5, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, five);
				} else if (currIndex == 1) {
					setImgAndAnimation(mTab5, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, five);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab5, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, five);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab5, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, five);
				}else if (currIndex == 4) {
					setImgAndAnimation(mTab5, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, five);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab5, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, five);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab5, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, five);
				}
				break;
			case 6:
				mTab6.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab6, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, six);
				} else if (currIndex == 1) {
					setImgAndAnimation(mTab6, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, six);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab6, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, six);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab6, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, six);
				}else if (currIndex == 4) {
					setImgAndAnimation(mTab6, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, six);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab6, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, six);
				}else if (currIndex == 7) {
					setImgAndAnimation(mTab6, mTab7, R.drawable.pad_point_on,
							R.drawable.pad_point_off, seven, six);
				}
				break;
			case 7:
				mTab7.setImageDrawable(getResources().getDrawable(
						R.drawable.pad_point_on));
				if (currIndex == 0) {
					setImgAndAnimation(mTab7, mTab0, R.drawable.pad_point_on,
							R.drawable.pad_point_off, 0, seven);
				} else if (currIndex == 1) {
					setImgAndAnimation(mTab7, mTab1, R.drawable.pad_point_on,
							R.drawable.pad_point_off, one, seven);
				} else if (currIndex == 2) {
					setImgAndAnimation(mTab7, mTab2, R.drawable.pad_point_on,
							R.drawable.pad_point_off, two, seven);
				} else if (currIndex == 3) {
					setImgAndAnimation(mTab7, mTab3, R.drawable.pad_point_on,
							R.drawable.pad_point_off, three, seven);
				}else if (currIndex == 4) {
					setImgAndAnimation(mTab7, mTab4, R.drawable.pad_point_on,
							R.drawable.pad_point_off, four, seven);
				}else if (currIndex == 5) {
					setImgAndAnimation(mTab7, mTab5, R.drawable.pad_point_on,
							R.drawable.pad_point_off, five, seven);
				}else if (currIndex == 6) {
					setImgAndAnimation(mTab7, mTab6, R.drawable.pad_point_on,
							R.drawable.pad_point_off, six, seven);
				}
				break;
			}
			currIndex = location;
			Log.v("currIndex", String.valueOf(currIndex));
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	*/
	
	
	/************************************设置每个view上的按键的函数*******************************************//*
	
	
	*//**
	 * 两次返回键后退出
	 *//*
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - quitTime >= 2000) {//如果在两秒钟内连续按再次返回键则退出
				ToastFactory.showToast(getBaseContext(), "再按一次退出");
				quitTime = currentTime;
			}else {
				finish();
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	};
	
	
	
	
	
	
	
	*//**初始化所有List
	 * 
	 *//*
	private void initList(){
		initprofileList();
		inittaskList();
	}


	private void initprofileList() {
		AbstractLocalService localService = new ProfileService();
		if(DataApplication.profilelist==null)
			DataApplication.profilelist = new ArrayList<Profile>();
		else
			DataApplication.profilelist.clear();
		DataApplication.profilelist.addAll(localService.check());
	}
	
	private void inittaskList() {
//		AbstractLocalService localService = new TaskService(MainPad.this);
		if(DataApplication.tasklist==null)
			DataApplication.tasklist = new ArrayList<Task>();
		else
			DataApplication.tasklist.clear();
		
		
		if(DataApplication.mondaylist==null)
			DataApplication.mondaylist = new ArrayList<Task>();
		else
			DataApplication.mondaylist.clear();
		
		if(DataApplication.tuesdaylist==null)
			DataApplication.tuesdaylist = new ArrayList<Task>();
		else
			DataApplication.tuesdaylist.clear();
		
		if(DataApplication.wednesdaylist==null)
			DataApplication.wednesdaylist = new ArrayList<Task>();
		else
			DataApplication.wednesdaylist.clear();
		
		if(DataApplication.thursdaylist==null)
			DataApplication.thursdaylist = new ArrayList<Task>();
		else
			DataApplication.thursdaylist.clear();
		
		if(DataApplication.fridaylist==null)
			DataApplication.fridaylist = new ArrayList<Task>();
		else
			DataApplication.fridaylist.clear();
		
		if(DataApplication.saturdaylist==null)
			DataApplication.saturdaylist = new ArrayList<Task>();
		else
			DataApplication.saturdaylist.clear();
		
		if(DataApplication.sundaylist==null)
			DataApplication.sundaylist = new ArrayList<Task>();
		else
			DataApplication.sundaylist.clear();
		
		
		
		
		//TODO
		DataApplication.tasklist.addAll(taskLocalService.check());
		
		int length = DataApplication.tasklist.size();
		for(int i=0;i<length;i++){
			Task task = DataApplication.tasklist.get(i);
			switch (task.getDayofWeek()) {
			case 2:
				DataApplication.mondaylist.add(task);
				break;
			case 3:
				DataApplication.tuesdaylist.add(task);
				break;
			case 4:
				DataApplication.wednesdaylist.add(task);
				break;
			case 5:
				DataApplication.thursdaylist.add(task);
				break;
			case 6:
				DataApplication.fridaylist.add(task);
				break;
			case 7:
				DataApplication.saturdaylist.add(task);
				break;
			case 1:
				DataApplication.sundaylist.add(task);
				break;

			default:
				break;
			}
		}
		
		
	}*/
	
	
	
	

}















