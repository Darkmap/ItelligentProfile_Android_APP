package com.daocaowu.itelligentprofile.utils;

import java.io.FileNotFoundException;
import java.security.MessageDigest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Gallery;

public class SystemUtil {

	/**
	 * 
	 * �ж������Ƿ����
	 * 
	 * @param pContext
	 * @return true: �������; false: ���粻����
	 */

	public static boolean isConnectInternet(Context pContext) {
		System.out.println("pContext:" + pContext);
		ConnectivityManager conManager = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * ��dip��λת����px
	 * 
	 * @param context
	 * @param dpValue
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * pxװ����dp
	 * 
	 * @param context
	 * @param pxValue
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
//	/**
//	 * 
//	 * Toast��ʾ
//	 * 
//	 * @param pContext
//	 * @param pString
//	 */
//
//	public static void showToast(Context pContext, String pString) {
//		Toast.makeText(pContext, pString, Toast.LENGTH_SHORT).show();
//	}
	
	/**
	 * ��uri��Ӧ��ͼƬת����bitmap����
	 */
	public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
	
	
	/**
	 * gallery�����
	 */
	public static  void alignGalleryToLeft(Context context, Gallery gallery,
			int itemWidth, int spacing) {
		int galleryWidth = DataApplication.getScreenWidth();
		// if (viewGroup != null)
		// galleryWidth = viewGroup.getWidth();
		int offset = 0;
		if (galleryWidth <= itemWidth) {
			offset = galleryWidth / 2 - itemWidth / 2 - spacing;
		} else {
			offset = galleryWidth - itemWidth - 2 * spacing;
		}

		MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
		mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
				mlp.bottomMargin);
	}
	
	//MD5���ܣ�32λ   
    public static  String MD5(String str)  
    {  
        MessageDigest md5 = null ;  
        try   
        {  
            md5 = MessageDigest.getInstance("MD5" );  
        }catch (Exception e)  
        {  
            e.printStackTrace();  
            return   "" ;  
        }  
          
        char [] charArray = str.toCharArray();  
        byte [] byteArray =  new   byte [charArray.length];  
          
        for ( int  i =  0 ; i < charArray.length; i++)  
        {  
            byteArray[i] = (byte )charArray[i];  
        }  
        byte [] md5Bytes = md5.digest(byteArray);  
          
        StringBuffer hexValue = new  StringBuffer();  
        for (  int  i =  0 ; i < md5Bytes.length; i++)  
        {  
            int  val = (md5Bytes[i])& 0xff ;  
            if (val <  16 )  
            {  
                hexValue.append("0" );  
            }  
            hexValue.append(Integer.toHexString(val));  
        }  
        return  hexValue.toString();  
    }  
    
    public static String getIMEI(Context context) {
		// ����ϵͳ���ܻ�ȡIMEI
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}
    
    
    
	public static void getGeography(Context context, double latitude,
			double longitude) {
		// ��ȡ��γ��
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude(); // ����
				longitude = location.getLongitude(); // γ��
			}
		}
	}
}
