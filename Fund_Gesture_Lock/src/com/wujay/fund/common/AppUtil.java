package com.wujay.fund.common;

import java.security.MessageDigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class AppUtil {
    
	/**
     * 获取屏幕分辨率
     * @param context
     */
    public static int[] getScreenDispaly(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; // 手机屏幕的宽度
		int height = dm.heightPixels; // 手机屏幕的高度
		int display[] = {width, height};
		return display;
	}
    
    
    public static String md5Code(String str) {
    	try {
    		byte[]  strBytes = str.getBytes("UTF-8");
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(strBytes);
			StringBuilder buff = new StringBuilder();
			int length = bytes.length;
			int digital;
			for (int i = 0; i < length; i++) {
				digital = bytes[i];
				if(digital < 0) {
					digital += 256;
				}
				if(digital < 16) {
					buff.append("0");
				}
				buff.append(Integer.toHexString(digital));
			}
			return buff.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		} 
    }
    
    public static void putValue(Context context, String value) {
    	SharedPreferences spf = context.getSharedPreferences("md5", Context.MODE_PRIVATE);
    	Editor edit = spf.edit();
    	edit.putString("sha", value);
    	edit.commit();
	}
    public static String getValue(Context context) {
    	SharedPreferences spf = context.getSharedPreferences("md5", Context.MODE_PRIVATE);
    	return spf.getString("sha", "");
    }

}
