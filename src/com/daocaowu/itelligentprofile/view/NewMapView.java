package com.daocaowu.itelligentprofile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.daocaowu.itelligentprofile.activity.GPSMapActivity;
import com.daocaowu.itelligentprofile.utils.DataApplication;

public class NewMapView extends MapView {

	public NewMapView(Context context)
	{
		super(context);
	}
	
	public NewMapView(Context context, AttributeSet set)
	{
		super(context, set);
	}
	
	public NewMapView(Context context, AttributeSet set, int i)
	{
		super(context, set, i);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		int x = (int)arg0.getX();
		int y = (int)arg0.getY();
		Projection mProjection = this.getProjection();  
        GeoPoint pt = mProjection.fromPixels(x,y);
        if(pt==null) Log.v("GeoPoint", "GeoPointΪnull");
        else{
    		int yy = pt.getLongitudeE6();
    		int xx = pt.getLatitudeE6();
    		DataApplication.x = xx;
    		DataApplication.y = yy;
    		Log.d("xxxxxxxxxxx", Integer.toString(xx));
    		Log.d("yyyyyyyyyyy", Integer.toString(yy));
        }
//        GPSMapActivity.mMapController.animateTo(pt, null);
//		int xx = geoPoint.getLongitudeE6();
//		int yy = geoPoint.getLatitudeE6();
//		Log.d("xxxxxxxxxx", x+"");
//		Log.d("yyyyyyyyyy", y+"");

		return super.onTouchEvent(arg0);
	}
}