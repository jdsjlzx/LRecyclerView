package com.lzx.demo.util;

import android.content.Context;
import android.widget.Toast;

public class AppToast {

	protected static final String TAG = "AppToast";
	public static Toast toast;
	/**
	 * 信息提示
	 * 
	 * @param context
	 * @param content
	 */
	public static void makeToast(Context context, String content) {
		if(context==null)return;
		if(toast != null)
			toast.cancel();
		toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
		toast.show();
	}

	public static void makeShortToast(Context context, String content) {
		if(context==null)return;
		if(toast != null)
			toast.cancel();
		toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void showShortText(Context context, int resId) {
		try {
			if(context==null)return;
			if(toast != null)
				toast.cancel();
			toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
			toast.show();
		} catch (Exception e) {
		}
	}

	public static void showShortText(Context context, CharSequence text) {
		if(context==null)return;
		if(toast != null)
			toast.cancel();
		toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void showLongText(Context context, int resId) {
		try {
			if(context==null)return;
			if(toast != null)
				toast.cancel();
			toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
			toast.show();
			
		} catch (Exception e) {
		}
	}


}
