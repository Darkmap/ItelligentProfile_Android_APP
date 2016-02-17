package com.daocaowu.itelligentprofile.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.DBHelper.TableColumns.ProfileColumns;

public class ProfileWidgetProvider_4x extends ProfileWidgetProvider {

	private static final String TAG = "ProfileWidgetProvider_4x";
	
	@Override  
    public void onReceive(Context context, Intent intent) {  
		if (intent !=  null) {
			if (UPDATE.equals(intent.getAction())) {
        		RemoteViews appWidgetView = new RemoteViews(context.getPackageName(), getLayoutId());
				if (intent.getStringExtra(ENABLE_TYPE) != null && ENABLE_BY_USER.contains(intent.getStringExtra(ENABLE_TYPE))) {
					StringBuffer sb = new StringBuffer();
//					sb.append(intent.getIntExtra(ProfileColumns.ID, 0) + "--");
					sb.append(intent.getStringExtra(ProfileColumns.NAME) + "--");
//					sb.append(intent.getStringExtra(ProfileColumns.STARTTIME) + "--");
					appWidgetView.setTextViewText(R.id.widget_text, sb.toString());
					Log.e(TAG, sb.toString());
				}else if (intent.getStringExtra(ENABLE_TYPE) != null && ENABLE_BY_TASK.contains(intent.getStringExtra(ENABLE_TYPE))) {
					StringBuffer sb = new StringBuffer();
//					sb.append(intent.getIntExtra(ProfileColumns.ID, 0) + "--");
					sb.append(intent.getStringExtra(ProfileColumns.NAME) + "--");
//					sb.append(intent.getStringExtra(ProfileColumns.STARTTIME) + "--");
					appWidgetView.setTextViewText(R.id.widget_text, sb.toString());
					Log.e(TAG, sb.toString());
				}else if (intent.getStringExtra(ENABLE_TYPE) != null && ENABLE_BY_LOCATION.contains(intent.getStringExtra(ENABLE_TYPE))) {
					StringBuffer sb = new StringBuffer();
//					sb.append(intent.getIntExtra(ProfileColumns.ID, 0) + "--");
					sb.append(intent.getStringExtra(ProfileColumns.NAME) + "--");
//					sb.append(intent.getStringExtra(ProfileColumns.STARTTIME) + "--");
					appWidgetView.setTextViewText(R.id.widget_text, sb.toString());
					Log.e(TAG, sb.toString());
				}else if (intent.getStringExtra(ENABLE_TYPE) != null && ENABLE_BY_WIFI.contains(intent.getStringExtra(ENABLE_TYPE))) {
					StringBuffer sb = new StringBuffer();
//					sb.append(intent.getIntExtra(ProfileColumns.ID, 0) + "--");
					sb.append(intent.getStringExtra(ProfileColumns.NAME) + "--");
//					sb.append(intent.getStringExtra(ProfileColumns.STARTTIME) + "--");
					appWidgetView.setTextViewText(R.id.widget_text, sb.toString());
					Log.e(TAG, sb.toString());
				}else if (intent.getStringExtra(ENABLE_TYPE) != null && ENABLE_BY_POWER_SAVING.contains(intent.getStringExtra(ENABLE_TYPE))) {
					StringBuffer sb = new StringBuffer();
//					sb.append(intent.getIntExtra(ProfileColumns.ID, 0) + "--");
					sb.append(intent.getStringExtra(ProfileColumns.NAME) + "--");
//					sb.append(intent.getStringExtra(ProfileColumns.STARTTIME) + "--");
					appWidgetView.setTextViewText(R.id.widget_text, sb.toString());
					Log.e(TAG, sb.toString());
				}
//				AppWidgetManager.getInstance(context).updateAppWidget(appWidgetView.getLayoutId(), appWidgetView);
				AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context,ProfileWidgetProvider_2x.class), appWidgetView);
			}
		}
        super.onReceive(context, intent);  
        Log.e(TAG, "onReceive,Action:" + intent.getAction());  
    }
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.widget_4x;
	}

	@Override
	protected int getWidgetType() {
		// TODO Auto-generated method stub
		return 0;
	}

}
