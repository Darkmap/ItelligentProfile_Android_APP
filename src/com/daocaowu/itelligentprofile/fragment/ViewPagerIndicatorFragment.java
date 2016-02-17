package com.daocaowu.itelligentprofile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.slidingmenu.lib2.SlidingMenu;

/**
 * 注释：这个类已经弃用
 * @author CaoRen
 *
 */
public class ViewPagerIndicatorFragment extends Fragment {

	private static final String[] dayOfWeek = new String[]{
		"情景模式","星期一","星期二","星期三","星期四","星期五","星期六","星期天"
	};
	private View contextView = null;
	FragmentPagerAdapter adapter = null;
	MainPad mainPadActivity = null;
//	TabPageIndicator indicator = null;
	ViewPager mTabPager = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ProfileAdapter(getFragmentManager());
		mainPadActivity = (MainPad)getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.homepad, container, false);
		mTabPager = (ViewPager) contextView.findViewById(R.id.tabpager);
		mTabPager.setAdapter(adapter);
		mTabPager.setOnPageChangeListener(onPageChangeListener);
//		indicator = (TabPageIndicator) contextView.findViewById(R.id.indicator);
//		indicator.setViewPager(mTabPager);
//		indicator.setOnPageChangeListener(onPageChangeListener);
		return contextView;
	}
	
	private class ProfileAdapter extends FragmentPagerAdapter {

		public ProfileAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new ContentFragment("dong ViewPagerIndicator代替actionBar "
					+ ""/*dayOfWeek[position % dayOfWeek.length]*/);
		}
		
//		@Override
//		public CharSequence getPageTitle(int position) {
//			return dayOfWeek[position % dayOfWeek.length].toUpperCase();
//		}

		@Override
		public int getCount() {
			return dayOfWeek.length;
		}
		
	}
	
	ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_FULLSCREEN);
				break;
			default:
				mainPadActivity.getSlidingMenu().setTouchModeAbove(
						SlidingMenu.TOUCHMODE_MARGIN);
				break;
			}
		}
	};
	
}
