package com.daocaowu.itelligentprofile.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ClassName: ToastFactory <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可�?). <br/>
 * date: 2013-3-23 上午10:14:13 <br/>
 * 
 * @author Liang YongJian
 * @version
 * @since JDK 1.6
 * 
 *        解决toast频繁显示用户体验效果不佳的问�?
 */
public class ToastFactory {
	private static Context context;
	private static Toast toast;

	public static void showToast(Context context, String title) {
		if (context == null)
			context = DataApplication.getContext();
		if (ToastFactory.context == context) {
			toast.setText(title);// 重新设置内容
		} else {
			if (toast != null)// 清空之前显示的toast
				toast.cancel();
			ToastFactory.context = context;
			toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
			toast.setText(title);
		}

		toast.show();
	}

	public static void showToast(String title) {
		Context context = DataApplication.getContext();
		if (ToastFactory.context == context) {
			toast.setText(title);// 重新设置内容
		} else {
			if (toast != null)// 清空之前显示的toast
				toast.cancel();
			ToastFactory.context = context;
			toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
			toast.setText(title);
		}

		toast.show();
	}
}
