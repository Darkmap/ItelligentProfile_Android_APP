package com.daocaowu.itelligentprofile.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.DBManager;
import com.daocaowu.itelligentprofile.activity.AddProfileActivity;
import com.daocaowu.itelligentprofile.activity.AddTask;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.adapter.CalendarListAdapter;
import com.daocaowu.itelligentprofile.adapter.ProfileListAdapter;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.bean.Task;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.service.TaskReceiver;
import com.daocaowu.itelligentprofile.service.TaskReceiverService;
import com.daocaowu.itelligentprofile.service.TaskService;
import com.daocaowu.itelligentprofile.utils.DataApplication;
import com.daocaowu.itelligentprofile.utils.SwitchProfileUtil;
import com.daocaowu.itelligentprofile.utils.ToastFactory;
import com.slidingmenu.lib2.SlidingMenu;

public class ViewPagerFragment extends Fragment {

	private View contextView = null;
	public static ViewPager mTabPager = null;
//	public static LinePageIndicator mIndicator;
	FragmentListAdapter adapter = null;
	MainPad mainPadActivity = null;

	private ImageView mTab0,mTab1, mTab2, mTab3, mTab4, mTab5,mTab6, mTab7;
	public static int currIndex = 0;// 当前页卡编号
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private int four;
	private int five;
	private int six;
	private int seven;
	
	//8个滑动的View
	View view0;
	View view1;
	View view2;
	View view3;
	View view4;
	View view5;
	View view6;
	View view7;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		adapter = new FragmentListAdapter(getFragmentManager(), fragmentList);
		mainPadActivity = (MainPad)getActivity();
		
	}
	
	@Override()
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.homepad, container, false);
		mTabPager = (ViewPager) contextView.findViewById(R.id.tabpager);
//		 mIndicator = (LinePageIndicator)contextView.findViewById(R.id.indicator);
//		mTabPager.setAdapter(adapter);
//		mTabPager.setOnPageChangeListener(onPageChangeListener);
		
		taskLocalService = new TaskService(DBManager.context);
		mTaskReceiver = new TaskReceiver();
//		if (mainPadActivity == null) {
//			Intent i = getBaseContext().getPackageManager()  
//			        .getLaunchIntentForPackage(getBaseContext().getPackageName());  
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//			startActivity(i);
//		}
		
		initList();
		
		
		if (DataApplication.profilelist == null) {
			setDefaultProfile();
		}else if (DataApplication.profilelist.size() == 0) {
			setDefaultProfile();
		}
		AddProfileActivity.currentprofile = null;
		initList();
		
//		初始化所有界面
		initViews();
		return contextView;
	}
	
	private void setDefaultProfile(){
		try {
			AddProfileActivity.currentprofile = SwitchProfileUtil.getProfile(mainPadActivity.instance);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		if(AddProfileActivity.currentprofile!=null){
			new Runnable() {
				public void run() {
					AbstractLocalService localService = new ProfileService();
					AddProfileActivity.currentprofile.setIsdefault(1);
					AddProfileActivity.currentprofile.setProfileName("默认情景");
					localService.add(AddProfileActivity.currentprofile);
					
					Log.v("当前设置震动为", String.valueOf(AddProfileActivity.currentprofile.getVibrate()));
					
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
	
	@Override
	public void onResume() {
		super.onResume();
		Message message = new Message();
        message.what = 8;
        myHandler.sendMessage(message);
	}
	
	/***************************View0组件********************************************/
	private GridView profilelist_view;
	private ProfileListAdapter profileadapter;
	
	/***************************View1-7组件********************************************/
	
	private ImageView dayofweek_1;
	private RelativeLayout cover1;
	private ListView calendarlist_view_1;
	private CalendarListAdapter calendaradapter_1;
	
	private ImageView dayofweek_2;
	private RelativeLayout cover2;
	private ListView calendarlist_view_2;
	private CalendarListAdapter calendaradapter_2;
	
	private ImageView dayofweek_3;
	private RelativeLayout cover3;
	private ListView calendarlist_view_3;
	private CalendarListAdapter calendaradapter_3;
	
	private ImageView dayofweek_4;
	private RelativeLayout cover4;
	private ListView calendarlist_view_4;
	private CalendarListAdapter calendaradapter_4;
	
	private ImageView dayofweek_5;
	private RelativeLayout cover5;
	private ListView calendarlist_view_5;
	private CalendarListAdapter calendaradapter_5;
	
	private ImageView dayofweek_6;
	private RelativeLayout cover6;
	private ListView calendarlist_view_6;
	private CalendarListAdapter calendaradapter_6;
	
	private ImageView dayofweek_7;
	private RelativeLayout cover7;
	private ListView calendarlist_view_7;
	private CalendarListAdapter calendaradapter_7;
	
	
	
	private AbstractLocalService taskLocalService;
	private TaskReceiver mTaskReceiver;
	
	private  Handler myHandler = new Handler() {
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
   
   /**
	 * View0的初始化
	 */
	private void initView0() {
		profilelist_view = (GridView) view0.findViewById(R.id.profile_list); 
		profileadapter = new ProfileListAdapter(DataApplication.profilelist, mainPadActivity.instance);
		profilelist_view.setAdapter(profileadapter);
		
		profilelist_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int index = arg2;
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定切换情景模式吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Profile profile = DataApplication.profilelist.get(index);
								TaskReceiverService.sendMsgToWidget(mainPadActivity.instance, profile, 0);
								SwitchProfileUtil.setProfile(mainPadActivity.instance, profile);
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
				new AlertDialog.Builder(mainPadActivity.instance).setTitle("选择操作")
				.setItems(act, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(mainPadActivity.instance, AddProfileActivity.class);
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
								List<Profile> profiles = localService.check();
								if (profiles != null) {
									DataApplication.profilelist.addAll(profiles);
								}
								profiles = null;
								
								Message message = new Message();   
						        message.what = 8; 
						        myHandler.sendMessage(message);
							}
							else ToastFactory.showToast(mainPadActivity.instance,"不能删除默认情景模式");
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
							List<Profile> profiles = localService2.check();
							if (profiles != null) {
								DataApplication.profilelist.addAll(profiles);
							}
							profiles = null;
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

	/**
	 * View1初始化
	 */
	private void initView1() {
		
		
		cover1 = (RelativeLayout) view1.findViewById(R.id.cover);
		cover1.setBackgroundResource(R.color.mon);
		dayofweek_1 = (ImageView) view1.findViewById(R.id.dayimage);
		dayofweek_1.setImageDrawable(getResources().getDrawable(
				R.drawable.mon));
		
		calendarlist_view_1 = (ListView) view1.findViewById(R.id.calender_list);
		calendaradapter_1 = new CalendarListAdapter(DataApplication.mondaylist, mainPadActivity.instance);
		calendarlist_view_1.setAdapter(calendaradapter_1);
		
		calendarlist_view_1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.mondaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance.getApplicationContext(), AddTask.class);
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
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Log.v("来自calendarlist_view_1,mondaylist", String.valueOf(index));
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
	
	
	
	
	/**
	 * View2初始化
	 */
	private void initView2() {
		
		cover2 = (RelativeLayout) view2.findViewById(R.id.cover);
		cover2.setBackgroundResource(R.color.tue);
		dayofweek_2 = (ImageView) view2.findViewById(R.id.dayimage);
		dayofweek_2.setImageDrawable(getResources().getDrawable(
				R.drawable.tue));
		
		calendarlist_view_2 = (ListView) view2.findViewById(R.id.calender_list);
		calendaradapter_2 = new CalendarListAdapter(DataApplication.tuesdaylist, mainPadActivity.instance);
		calendarlist_view_2.setAdapter(calendaradapter_2);
		
		calendarlist_view_2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.tuesdaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance.getApplicationContext(), AddTask.class);
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
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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

	
	
	
	/**
	 * View3初始化
	 */
	private void initView3() {
		
		cover3 = (RelativeLayout) view3.findViewById(R.id.cover);
		cover3.setBackgroundResource(R.color.wed);
		dayofweek_3 = (ImageView) view3.findViewById(R.id.dayimage);
		dayofweek_3.setImageDrawable(getResources().getDrawable(
				R.drawable.wed));
		
		calendarlist_view_3 = (ListView) view3.findViewById(R.id.calender_list);
		calendaradapter_3 = new CalendarListAdapter(DataApplication.wednesdaylist, mainPadActivity.instance);
		calendarlist_view_3.setAdapter(calendaradapter_3);
		
		calendarlist_view_3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.wednesdaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance, AddTask.class);
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
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
	
	
	
	/**
	 * View4初始化
	 */
	private void initView4() {
		
		cover4 = (RelativeLayout) view4.findViewById(R.id.cover);
		cover4.setBackgroundResource(R.color.thur);
		dayofweek_4 = (ImageView) view4.findViewById(R.id.dayimage);
		dayofweek_4.setImageDrawable(getResources().getDrawable(
				R.drawable.thur));
		
		calendarlist_view_4 = (ListView) view4.findViewById(R.id.calender_list);
		calendaradapter_4 = new CalendarListAdapter(DataApplication.thursdaylist, mainPadActivity.instance);
		calendarlist_view_4.setAdapter(calendaradapter_4);
		
		calendarlist_view_4.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.thursdaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance, AddTask.class);
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
				
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
	
	
	
	
	/**
	 * View5初始化
	 */
	private void initView5() {
		
		cover5 = (RelativeLayout) view5.findViewById(R.id.cover);
		cover5.setBackgroundResource(R.color.fri);
		dayofweek_5 = (ImageView) view5.findViewById(R.id.dayimage);
		dayofweek_5.setImageDrawable(getResources().getDrawable(
				R.drawable.fri));
		
		
		calendarlist_view_5 = (ListView) view5.findViewById(R.id.calender_list);
		calendaradapter_5 = new CalendarListAdapter(DataApplication.fridaylist, mainPadActivity.instance);
		calendarlist_view_5.setAdapter(calendaradapter_5);
		
		calendarlist_view_5.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.fridaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance, AddTask.class);
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
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
	
	
	
	/**
	 * View6初始化
	 */
	private void initView6() {
		
		cover6 = (RelativeLayout) view6.findViewById(R.id.cover);
		cover6.setBackgroundResource(R.color.sat);
		dayofweek_6 = (ImageView) view6.findViewById(R.id.dayimage);
		dayofweek_6.setImageDrawable(getResources().getDrawable(
				R.drawable.sat));
		
		calendarlist_view_6 = (ListView) view6.findViewById(R.id.calender_list);
		calendaradapter_6 = new CalendarListAdapter(DataApplication.saturdaylist, mainPadActivity.instance);
		calendarlist_view_6.setAdapter(calendaradapter_6);
		
		calendarlist_view_6.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.saturdaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance, AddTask.class);
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
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
	
	
	
	
	/**
	 * View7初始化
	 */
	private void initView7() {
		
		cover7 = (RelativeLayout) view7.findViewById(R.id.cover);
		cover7.setBackgroundResource(R.color.sun);
		dayofweek_7 = (ImageView) view7.findViewById(R.id.dayimage);
		dayofweek_7.setImageDrawable(getResources().getDrawable(
				R.drawable.sun));
		
		calendarlist_view_7 = (ListView) view7.findViewById(R.id.calender_list);
		calendaradapter_7 = new CalendarListAdapter(DataApplication.sundaylist, mainPadActivity.instance);
		calendarlist_view_7.setAdapter(calendaradapter_7);
		
		calendarlist_view_7.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				AddTask.currentTask = new Task(DataApplication.sundaylist.get(arg2));
				
				Intent intent = new Intent(mainPadActivity.instance, AddTask.class);
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
				new AlertDialog.Builder(mainPadActivity.instance)
				.setTitle("确认")
				.setMessage("确定删除该日程吗？")
				.setPositiveButton("是",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
//								AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
   
	/**
	 * 控件的初始化
	 */
	private void initPageViews() {
//		mTabPager = (ViewPager) mainPadActivity.findViewById(R.id.tabpager);
		mTabPager.setOnPageChangeListener(onPageChangeListener);

		mTab0 = (ImageView) contextView.findViewById(R.id.profile_setting);
		mTab1 = (ImageView) contextView.findViewById(R.id.Monday);
		mTab2 = (ImageView) contextView.findViewById(R.id.Tuesday);
		mTab3 = (ImageView) contextView.findViewById(R.id.Wednesday);
		mTab4 = (ImageView) contextView.findViewById(R.id.Thursday);
		mTab5 = (ImageView) contextView.findViewById(R.id.Friday);
		mTab6 = (ImageView) contextView.findViewById(R.id.Saturday);
		mTab7 = (ImageView) contextView.findViewById(R.id.Sunday);

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
		LayoutInflater mLi = LayoutInflater.from(mainPadActivity.instance);
		
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
//		mIndicator.setViewPager(mTabPager);
	}

	
	
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
	
	ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
		
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_FULLSCREEN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
				break;
			default:
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
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
	
	/**初始化所有List
	 * 
	 */
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
		List<Profile> profiles = localService.check();
		if (profiles != null) {
			DataApplication.profilelist.addAll(profiles);
		}
		profiles = null;
	}
	
	
	private void inittaskList() {
//		AbstractLocalService localService = new TaskService(mainPadActivity.instance);
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
		
		List<Task> tasks = taskLocalService.check();
		if (tasks != null) {
			//TODO
			DataApplication.tasklist.addAll(tasks);
		}
		
		
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
	}
	
	public static int getCurrIndex() {
		return currIndex;
	}
	
	public static void setCurrIndex(int currIndex) {
		ViewPagerFragment.currIndex = currIndex;
	}
	
	public static ViewPager getmTabPager() {
		return mTabPager;
	}
	
//	public void addtask(View v) {
//		Intent intent = new Intent(mainPadActivity.instance, AddTask.class);
//		intent.putExtra("action", 0);
//		intent.putExtra("dayofweek", currIndex);
//		startActivity(intent);
//		Log.v("星期", String.valueOf(currIndex));
//	}
}
