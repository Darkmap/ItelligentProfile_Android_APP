package com.daocaowu.itelligentprofile.view;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.utils.ConstData;
import com.daocaowu.itelligentprofile.utils.DateUtil;
import com.daocaowu.itelligentprofile.utils.SystemUtil;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	//点击刷新
		private static final int TAP_TO_REFRESH = 1;
		//拉到恢复
		private static final int PULL_TO_REFRESH = 2;
		//释放到恢复
		private static final int RELEASE_TO_REFRESH = 3;
		//刷新
		private static final int REFRESHING = 4;

		private static final String TAG = "PullToRefreshListView";

		private OnRefreshListener mOnRefreshListener;

		/**
		 * Listener that will receive notifications every time the list scrolls.
		 */
		private OnScrollListener mOnScrollListener;
		private LayoutInflater mInflater;

		private RelativeLayout mRefreshView;
		private TextView mRefreshViewText;
		private ImageView mRefreshViewImage;
		private ProgressBar mRefreshViewProgress;
		private TextView mRefreshViewLastUpdated;

		private int mCurrentScrollState;
		private int mRefreshState;

		private RotateAnimation mFlipAnimation;
		private RotateAnimation mReverseFlipAnimation;

		private int mRefreshViewHeight;
		private int mRefreshOriginalTopPadding;
		private int mLastMotionY;
		private SharedPreferences mSettings;
		private boolean mBounceHack;
		private String mLastRefreshTime;
		private String mKey;

		private Context mContext;
		private LinearLayout loadLayout;

		public PullToRefreshListView(Context context) {
			super(context);
			init(context);
		}

		public PullToRefreshListView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init(context);
		}

		public PullToRefreshListView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init(context);
		}
		

		private void init(Context context) {
			// Load all of the animations we need in code rather than through XML
			mContext = context;
			mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
			mKey = ConstData.LAST_REFRESH_TIME + mContext.getClass().getFields();
			mLastRefreshTime = mSettings.getString(mKey,
					DateUtil.dateToStrLong(new Date()));
			Log.v(TAG, "mLastRefreshTime:" + mLastRefreshTime);
			mFlipAnimation = new RotateAnimation(0, -180,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			mFlipAnimation.setInterpolator(new LinearInterpolator());
			mFlipAnimation.setDuration(250);
			mFlipAnimation.setFillAfter(true);
			mReverseFlipAnimation = new RotateAnimation(-180, 0,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
			mReverseFlipAnimation.setDuration(250);
			mReverseFlipAnimation.setFillAfter(true);

			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			mRefreshView = (RelativeLayout) mInflater.inflate(
					R.layout.pull_to_refresh_header, this, false);
			mRefreshViewText = (TextView) mRefreshView
					.findViewById(R.id.pull_to_refresh_text);
			mRefreshViewImage = (ImageView) mRefreshView
					.findViewById(R.id.pull_to_refresh_image);
			mRefreshViewProgress = (ProgressBar) mRefreshView
					.findViewById(R.id.pull_to_refresh_progress);
			mRefreshViewLastUpdated = (TextView) mRefreshView
					.findViewById(R.id.pull_to_refresh_updated_at);

			mRefreshViewImage.setMinimumHeight(50);
			mRefreshView.setOnClickListener(new OnClickRefreshListener());
			mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();

			mRefreshState = TAP_TO_REFRESH;

			addHeaderView(mRefreshView);

			super.setOnScrollListener(this);

			measureView(mRefreshView);
			mRefreshViewHeight = mRefreshView.getMeasuredHeight();
			btnColor = getResources().getColorStateList(R.color.white);
			
			Log.i("Sports", "mRefreshState == " + mRefreshState);
		}
		
		
		/**
		 * 重新加载listview
		 * @param colorId 更多按钮的背景
		 */
		public void reAddFooterBtn(int resourceId){
			if (resourceId != 0) {
				reBtnResourceId = resourceId;
			}
			addFooterBtn();
		}

		//用于存放【更多】按钮的背景
		private int reBtnResourceId;
		LinearLayout footerLayout;
		public Button loadBtn;
		ProgressBar progressBar;
		TextView loadText;
		private ColorStateList btnColor;
		private int btnResourceId;

		private void addFooterBtn() {
			if (footerLayout != null) {
				removeFooterView(footerLayout);
			}
			
			if (footerLayout == null) {
				// 创建 更多 按钮
				footerLayout = new LinearLayout(mContext);
				loadBtn = new Button(mContext);
				loadBtn.setText("更多");
				loadBtn.setTextSize(16f);
				loadBtn.setTextColor(btnColor);
				
				
				//如果传了背景就使用传的
				if (0 != reBtnResourceId) {
					loadBtn.setBackgroundResource(reBtnResourceId);
					loadBtn.setTextColor(Color.BLACK);
					loadBtn.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							55));
				}else{
					loadBtn.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.MATCH_PARENT));
				}
				loadBtn.setOnClickListener(new LoadFooterOnclick(footerLayout));
				footerLayout.addView(loadBtn);
			}
			addFooterView(footerLayout);
			if (loadLayout != null) {
				removeFooterView(loadLayout);
			}

		}

		private void addFooterLoadBar() {
			if (loadLayout == null) {
				// 创建加载条
				loadLayout = new LinearLayout(mContext);
				loadLayout.setPadding(0, 4, 0, 8);
				loadLayout.setGravity(Gravity.CENTER);
				progressBar = new ProgressBar(mContext);
				progressBar.setLayoutParams(new LayoutParams(40, 40));
				progressBar
						.setScrollBarStyle(android.R.attr.progressBarStyleSmallInverse);
				loadText = new TextView(mContext);
				loadText.setTextSize(16f);
				loadText.setText("数据正在加载...");
				loadLayout.addView(progressBar);
				loadLayout.addView(loadText);
			}

			removeFooterView(footerLayout);
			addFooterView(loadLayout);
		}

		/**
		 * 尾部
		 * 
		 * @param context
		 */
		public void initFooterView(Context context) {
//			if (getCount() >= 10 ) {
//				if (SystemUtil.isConnectInternet(mContext)) {
//					addFooterBtn();
//				}
//			}

			addFooterBtn();
		}

		class LoadFooterOnclick implements OnClickListener {
			public LoadFooterOnclick(LinearLayout footerLayout) {

			}

			@Override
			public void onClick(View v) {
				addFooterLoadBar();
				footerLayout = null;
				if (mOnRefreshListener != null) {
					if (SystemUtil.isConnectInternet(mContext))
						mOnRefreshListener.loadMoreData();
				}
			}

		}

		/**
		 * 外部调用加载foot
		 */
		public void initFoot() {
			if (SystemUtil.isConnectInternet(mContext)) {
				PullToRefreshListView.this.removeFooterView(loadLayout);
				PullToRefreshListView.this.initFooterView(mContext);
			}
			
		}
		
		public void initFoot(int color) {
			if (SystemUtil.isConnectInternet(mContext)) {
				PullToRefreshListView.this.removeFooterView(loadLayout);
				PullToRefreshListView.this.reAddFooterBtn(color);
			}
			
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
			setSelection(1);
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			super.setAdapter(adapter);

			setSelection(1);
		}

		/**
		 * Set the listener that will receive notifications every time the list
		 * scrolls.
		 * 
		 * @param l
		 *            The scroll listener.
		 */
		@Override
		public void setOnScrollListener(AbsListView.OnScrollListener l) {
			mOnScrollListener = l;
		}

		/**
		 * Register a callback to be invoked when this list should be refreshed.
		 * 
		 * @param onRefreshListener
		 *            The callback to run.
		 */
		public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
			mOnRefreshListener = onRefreshListener;
		}

		/**
		 * Set a text to represent when the list was last updated.
		 * 
		 * @param lastUpdated
		 *            Last updated at.
		 */
		public void setLastUpdated(CharSequence lastUpdated) {
			if (lastUpdated != null) {
				mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
				mRefreshViewLastUpdated.setText(lastUpdated);
			} else {
				mRefreshViewLastUpdated.setVisibility(View.GONE);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			final int y = (int) event.getY();
			mBounceHack = false;

			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				if (!isVerticalScrollBarEnabled()) {
					setVerticalScrollBarEnabled(true);
				}
				
//				Log.i("Sports", "getFirstVisiblePosition == " + getFirstVisiblePosition());
//				Log.i("Sports", "mRefreshView.getBottom() == " + mRefreshView.getBottom());
//				Log.i("Sports", "mRefreshViewHeight == " + mRefreshViewHeight);
//				Log.i("Sports", "mRefreshView.getTop() == " + mRefreshView.getTop());
				
				
				
				if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
					if ((mRefreshView.getBottom() >= mRefreshViewHeight || mRefreshView
							.getTop() >= 0) && mRefreshState == RELEASE_TO_REFRESH) {
						// Initiate the refresh
						if (SystemUtil.isConnectInternet(mContext)) {
							mRefreshState = REFRESHING;
							prepareForRefresh();
							onRefresh();
							mLastRefreshTime = DateUtil.dateToStrLong(new Date());
							mSettings.edit().putString(mKey, mLastRefreshTime)
									.commit();
							
							
						}
					} else if (mRefreshView.getBottom() < mRefreshViewHeight
							|| mRefreshView.getTop() <= 0) {
						// Abort refresh and scroll down below the refresh view
						resetHeader();
						setSelection(1);
					}
				}
				else if(getFirstVisiblePosition() <= 1 && getFirstVisiblePosition() > 0){
					setSelection(1);
				}
				break;
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				applyHeaderPadding(event);
				break;
			}
			return super.onTouchEvent(event);
		}

		private void applyHeaderPadding(MotionEvent ev) {
			// getHistorySize has been available since API 1
			int pointerCount = ev.getHistorySize();

			for (int p = 0; p < pointerCount; p++) {
				if (mRefreshState == RELEASE_TO_REFRESH) {
					if (isVerticalFadingEdgeEnabled()) {
						setVerticalScrollBarEnabled(false);
					}

					int historicalY = (int) ev.getHistoricalY(p);

					// Calculate the padding to apply, we divide by 1.7 to
					// simulate a more resistant effect during pull.
					int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);

					mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
							topPadding, mRefreshView.getPaddingRight(),
							mRefreshView.getPaddingBottom());
				}
			}
		}

		/**
		 * Sets the header padding back to original size.
		 */
		private void resetHeaderPadding() {
			
//			Log.i("Sports", "设置Padding  getPaddingLeft " + mRefreshView.getPaddingLeft());
//			Log.i("Sports", "设置Padding  mRefreshOriginalTopPadding " + mRefreshOriginalTopPadding);
//			Log.i("Sports", "设置Padding  getPaddingRight " + mRefreshView.getPaddingRight());
//			Log.i("Sports", "设置Padding  getPaddingBottom " + mRefreshView.getPaddingBottom());
			
			mRefreshView.setPadding(mRefreshView.getPaddingLeft(),
					mRefreshOriginalTopPadding, mRefreshView.getPaddingRight(),
					mRefreshView.getPaddingBottom());
		}

		/**
		 * 显示【点击刷新】
		 */
		private void resetHeader() {
			Log.i("Sports", "刷新后：mRefreshState == " + mRefreshState);
			if (mRefreshState != TAP_TO_REFRESH) {
				mRefreshState = TAP_TO_REFRESH;

				resetHeaderPadding();

				// Set refresh view text to the pull label
				mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
				// Replace refresh drawable with arrow drawable
				mRefreshViewImage
						.setImageResource(R.drawable.ic_pulltorefresh_arrow);
				// Clear the full rotation animation
				mRefreshViewImage.clearAnimation();
				// Hide progress bar and arrow.
				mRefreshViewImage.setVisibility(View.GONE);
				mRefreshViewProgress.setVisibility(View.GONE);
			}
		}

		private void measureView(View child) {
			ViewGroup.LayoutParams p = child.getLayoutParams();
			if (p == null) {
				p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}

			int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
			int lpHeight = p.height;
			int childHeightSpec;
			if (lpHeight > 0) {
				childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
						MeasureSpec.EXACTLY);
			} else {
				childHeightSpec = MeasureSpec.makeMeasureSpec(0,
						MeasureSpec.UNSPECIFIED);
			}
			child.measure(childWidthSpec, childHeightSpec);
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// When the refresh view is completely visible, change the text to say
			// "Release to refresh..." and flip the arrow drawable.
			if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
					&& mRefreshState != REFRESHING) {
				if (firstVisibleItem == 0) {
					mRefreshViewImage.setVisibility(View.VISIBLE);
					if ((mRefreshView.getBottom() >= mRefreshViewHeight + 20 || mRefreshView
							.getTop() >= 0) && mRefreshState != RELEASE_TO_REFRESH) {
						// mRefreshViewText.setText(String.format(mContext
						// .getString(R.string.pull_to_refresh_release_label),
						// mLastRefreshTime));
						mRefreshViewText.setText(mContext
								.getString(R.string.pull_to_refresh_release_label));
						setLastUpdated(String.format(mContext
								.getString(R.string.pull_to_refresh_last_time),
								mLastRefreshTime));
						mRefreshViewImage.clearAnimation();
						mRefreshViewImage.startAnimation(mFlipAnimation);
						mRefreshState = RELEASE_TO_REFRESH;
					} else if (mRefreshView.getBottom() < mRefreshViewHeight + 20
							&& mRefreshState != PULL_TO_REFRESH) {
						mRefreshViewText
								.setText(R.string.pull_to_refresh_pull_label);
						setLastUpdated("");
						if (mRefreshState != TAP_TO_REFRESH) {
							mRefreshViewImage.clearAnimation();
							mRefreshViewImage.startAnimation(mReverseFlipAnimation);
						}
						mRefreshState = PULL_TO_REFRESH;
					}
				} else {
					mRefreshViewImage.setVisibility(View.GONE);
					resetHeader();
				}
			} else if (mCurrentScrollState == SCROLL_STATE_FLING
					&& firstVisibleItem == 0 && mRefreshState != REFRESHING) {
				setSelection(1);
				mBounceHack = true;
			} else if (mBounceHack && mCurrentScrollState == SCROLL_STATE_FLING) {
				setSelection(1);
			}

			if (mOnScrollListener != null) {
				mOnScrollListener.onScroll(view, firstVisibleItem,
						visibleItemCount, totalItemCount);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			mCurrentScrollState = scrollState;

			if (mCurrentScrollState == SCROLL_STATE_IDLE) {
				mBounceHack = false;
			}

			if (mOnScrollListener != null) {
				mOnScrollListener.onScrollStateChanged(view, scrollState);
			}
		}

		/**
		 * 刷新中
		 */
		public void prepareForRefresh() {
			resetHeaderPadding();

			mRefreshViewImage.setVisibility(View.GONE);
			// We need this hack, otherwise it will keep the previous drawable.
			mRefreshViewImage.setImageDrawable(null);
			mRefreshViewProgress.setVisibility(View.VISIBLE);

			// Set refresh view text to the refreshing label
			mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);

			mRefreshState = REFRESHING;
		}

		public void onRefresh() {
			Log.d(TAG, "onRefresh");

			if (mOnRefreshListener != null) {
				mOnRefreshListener.onRefresh();
			}
		}

		/**
		 * Resets the list to a normal state after a refresh.
		 * 
		 * @param lastUpdated
		 *            Last updated at.
		 */
		public void onRefreshComplete(CharSequence lastUpdated) {
			setLastUpdated(lastUpdated);
			onRefreshComplete();
		}

		/**
		 * 调用上部刷新
		 */
		public void onRefreshComplete() {
			Log.d(TAG, "onRefreshComplete");

			resetHeader();

			// If refresh view is visible when loading completes, scroll down to
			// the next item.

			//Log.i("Sports", "PullListView  >>  getCount == " + getCount());
			if (getCount() >= 10 && footerLayout == null) {
				if (SystemUtil.isConnectInternet(mContext)) {
					addFooterBtn();
				}
			}

			Log.i("Sports", "点击后：mRefreshState == " + mRefreshState);
			if (mRefreshView.getBottom() > 0) {
				invalidateViews();
				setSelection(1);
				
				if (SystemUtil.isConnectInternet(mContext)) {
					if (mRefreshState == TAP_TO_REFRESH   && getCount() == 1 ) {
						//SystemUtil.showToast(mContext, "没有数据");
					}else{
						//SystemUtil.showToast(mContext, "有数据");
						//mRefreshView.setVisibility(GONE);
						//setSelection(2);
					}
				}
			}
//			else{
//				mRefreshView.setVisibility(VISIBLE);
//			}
		}

		/**
		 * Invoked when the refresh view is clicked on. This is mainly used when
		 * there's only a few items in the list and it's not possible to drag the
		 * list.
		 */
		private class OnClickRefreshListener implements OnClickListener {

			@Override
			public void onClick(View v) {
				if (mRefreshState != REFRESHING) {
					
					prepareForRefresh();
					onRefresh();
				}
			}

		}

		/**
		 * Interface definition for a callback to be invoked when list should be
		 * refreshed.
		 */
		public interface OnRefreshListener {
			/**
			 * Called when the list should be refreshed.
			 * <p>
			 * A call to {@link PullToRefreshListView #onRefreshComplete()} is
			 * expected to indicate that the refresh has completed.
			 */
			public void onRefresh();

			public void loadMoreData();
		}

		public String getLastRefreshTime() {
			return mLastRefreshTime;
		}
}
