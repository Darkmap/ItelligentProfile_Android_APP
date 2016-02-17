package com.daocaowu.itelligentprofile.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.daocaowu.itelligentprofile.R;
import com.daocaowu.itelligentprofile.activity.MainPad;
import com.daocaowu.itelligentprofile.bean.Profile;
import com.daocaowu.itelligentprofile.service.AbstractLocalService;
import com.daocaowu.itelligentprofile.service.ProfileService;
import com.daocaowu.itelligentprofile.service.TaskReceiverService;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public abstract class ProfileWidgetProvider extends AppWidgetProvider {

	private static final String TAG = "NoteWidgetProvider";
	
	public static final String DELETE = "android.appwidget.action.APPWIDGET_DELETED";
	
	public static final String UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
	public static final String ENABLE_TYPE = "enable_type";
	public static final String ENABLE_BY_USER = "enableByUser";
	public static final String ENABLE_BY_TASK = "enableByTask";
	public static final String ENABLE_BY_LOCATION = "enableByLocation";
	public static final String ENABLE_BY_WIFI = "enableByWifi";
	public static final String ENABLE_BY_POWER_SAVING = "enableByPowerSaving";
	
	@Override  
    public void onDeleted(Context context, int[] appWidgetIds) {  
        super.onDeleted(context, appWidgetIds);  
        Log.e(TAG, "onDeleted");
    }  
  
    @Override  
    public void onDisabled(Context context) {  
        super.onDisabled(context);  
        Log.e(TAG, "onDisabled");  
    }  
  
    @Override  
    public void onEnabled(Context context) {  
        super.onEnabled(context);  
        Log.e(TAG, "onEnabled");  
    }  
  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        super.onReceive(context, intent);  
        Log.e(TAG, "onReceive,Action:" + intent.getAction());  
    }
  
    @Override  
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {  
        // TODO Auto-generated method stub  
        Log.e(TAG, "onUpdate,Count:" + appWidgetIds.length);  
        
        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.widget_2x);
		Profile profile = null;
		if (DataApplication.lastProfileId <= 0) {
			profile = ProfileService.getDefaultProfile();
			TaskReceiverService.sendMsgToWidget(context, profile, 0);
		}else {
			AbstractLocalService profileService = new ProfileService();
			profile = (Profile) profileService.check(DataApplication.lastProfileId);
		}
		if (profile != null) {
			
			StringBuffer sb = new StringBuffer();
//			sb.append(profile.getProfileId() + "--");
			sb.append(profile.getProfileName());
			appWidgetViews.setTextViewText(R.id.widget_text, sb.toString());
		}
		Intent intent = new Intent(context, MainPad.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context, 0, intent, 0);
		appWidgetViews.setOnClickPendingIntent(R.id.widget_text,
				pendingIntent);
		appWidgetManager.updateAppWidget(new ComponentName(context,ProfileWidgetProvider.class), appWidgetViews);
        
//        for (int i = 0; i < appWidgetIds.length; i++) {
//			if (appWidgetIds[i] != AppWidgetManager.INVALID_APPWIDGET_ID) {
//				
//			}
//		}
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    
    protected abstract int getLayoutId();

    protected abstract int getWidgetType();
}
