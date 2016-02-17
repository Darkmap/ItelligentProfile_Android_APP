package com.daocaowu.itelligentprofile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainActivity;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.slidingmenu.lib2.SlidingMenu;

public class MenuFragment extends Fragment implements OnClickListener {

	int index = -1;
	private ViewPager mViewPager = null;
	private FrameLayout mFrameLayout = null;
	private MainPad mainPadActivity = null;
	private View contextView = null;
	private FragmentManager fragmentManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		// 初始化Activity和Fragment
		mainPadActivity = (MainPad) getActivity();
		if (MainPad.instance == null) {
			Intent restart = new Intent(mainPadActivity, MainActivity.class);
			startActivity(restart);
		}else {
			mFrameLayout = (FrameLayout) mainPadActivity.findViewById(R.id.f_content);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 初始化菜单界面和布局
		contextView = inflater.inflate(R.layout.list_menu, container, false);
		LinearLayout layout = (LinearLayout) contextView
				.findViewById(R.id.listMenu_layout);
		// mFrameLayout.addView(mViewPager);
		// 初始化监听事件
		for (int i = 0; i < layout.getChildCount(); i++) {
			layout.getChildAt(i).setOnClickListener(this);
		}
		
		return contextView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.listMenu_1:// 菜单1事件
			if (index == 1) {
				mainPadActivity.getSlidingMenu().toggle();
				return;
			}
			index = 1;
			fragmentManager = mainPadActivity.getSupportFragmentManager();
			ViewPagerFragment viewPagerFragment = (ViewPagerFragment) fragmentManager
					.findFragmentByTag("Indicator");
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.f_content,
							viewPagerFragment == null ? new ViewPagerFragment()
									: viewPagerFragment, "Indicator").commit();
			break;
		case R.id.listMenu_2:// 菜单2事件

			if (index == 2) {
				mainPadActivity.getSlidingMenu().toggle();
				return;
			}
			index = 2;
			mainPadActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);
			if (mFrameLayout != null) {
				mFrameLayout.setVisibility(View.VISIBLE);
			}
			FragmentManager fragmentManager = mainPadActivity
					.getSupportFragmentManager();
			NoteFragment noteFragment = (NoteFragment) fragmentManager
					.findFragmentByTag("note");
			fragmentManager
					.beginTransaction()
					.replace(
							R.id.f_content,
							noteFragment == null ? new NoteFragment() : noteFragment,
							"note").commit();
			
			break;
		case R.id.listMenu_3:// 菜单三 事件
			if (index == 3) {
				mainPadActivity.getSlidingMenu().toggle();
				return;
			}
			index = 3;
			mainPadActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);
			if (mFrameLayout != null) {
				mFrameLayout.setVisibility(View.VISIBLE);
			}

			fragmentManager = mainPadActivity.getSupportFragmentManager();
			GPSMapFragment gpsMapFragment = (GPSMapFragment) fragmentManager
					.findFragmentByTag("gpsMap");

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.f_content,
							gpsMapFragment == null ? new GPSMapFragment()
									: gpsMapFragment, "gpsMap").commit();

			break;
		case R.id.listMenu_4:// 菜单三 事件
			if (index == 4) {
				mainPadActivity.getSlidingMenu().toggle();
				return;
			}
			index = 4;
			mainPadActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);
			if (mFrameLayout != null) {
				mFrameLayout.setVisibility(View.VISIBLE);
			}

			fragmentManager = mainPadActivity.getSupportFragmentManager();
			WifiProfileFragment wifiProfileFragment = (WifiProfileFragment) fragmentManager
					.findFragmentByTag("wifiProfile");

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.f_content,
							wifiProfileFragment == null ? new WifiProfileFragment()
									: wifiProfileFragment, "wifiProfile").commit();

			break;
		case R.id.listMenu_5:// 菜单三 事件
			if (index == 5) {
				mainPadActivity.getSlidingMenu().toggle();
				return;
			}
			index = 5;
			mainPadActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);
			if (mFrameLayout != null) {
				mFrameLayout.setVisibility(View.VISIBLE);
			}

			fragmentManager = mainPadActivity.getSupportFragmentManager();
			PowerSavingFragment powerSavingFragment = (PowerSavingFragment) fragmentManager
					.findFragmentByTag("powerSaving");

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.f_content,
							powerSavingFragment == null ? new PowerSavingFragment()
									: powerSavingFragment, "powerSaving").commit();

			break;
		case R.id.listMenu_6:// 菜单三 事件
			if (index == 6) {
				mainPadActivity.getSlidingMenu().toggle();
				return;
			}
			index = 6;
			mainPadActivity.getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);
			if (mFrameLayout != null) {
				mFrameLayout.setVisibility(View.VISIBLE);
			}

			fragmentManager = mainPadActivity.getSupportFragmentManager();
			IntroductionFragment introductionFragment = (IntroductionFragment) fragmentManager
					.findFragmentByTag("introduction");

			fragmentManager
					.beginTransaction()
					.replace(
							R.id.f_content,
							introductionFragment == null ? new IntroductionFragment()
									: introductionFragment, "introduction").commit();

			break;
		case R.id.listMenu_exit:// 菜单退出 事件
			MainPad.instance.moveTaskToBack(true); 
//			Intent intent = new Intent(Intent.ACTION_MAIN);  
//		    intent.addCategory(Intent.CATEGORY_HOME);  
//		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置  
//		    startActivity(intent);
			
			mainPadActivity.finish();
			break;
		default:
			break;
		}
		mainPadActivity.getSlidingMenu().toggle();
	}


}
