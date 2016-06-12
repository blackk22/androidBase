package com.tyl.framework.util.device;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceUitls {

    /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null){
            mResources = Resources.getSystem();
        }else{
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }
    
    /**
	 * 获取设备号 TODO(这里用一句话描述这个方法的作用)
	 * 
	 * @author gdpancheng@gmail.com 2013-10-15 下午10:31:48
	 * @return
	 * @return String
	 */
	public static String getDeviceId(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}
	
	/**
	 * 获得手机的IMEI号，需要权限：android.permission.READ_PHONE_STATE
	 * @param context 上下文
	 * @return 手机号码
	 */
	public static String getIMEI(Context context) {
		if(context==null){
			return null;
		}
		
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }
	
	/**
	 * 获得手机的电话号，需要权限：android.permission.READ_PHONE_STATE
	 * @param context 上下文
	 * @return 手机号码
	 */
	public static String getTelephoneNumber(Context context){
		if(context==null){
			return null;
		}
		
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phone = telephonyManager.getLine1Number();
        return phone;
	}
	
	/**
	 * 获得wifi mac地址
	 * @param context 上下文
	 * @return mac地址
	 */
	public static String getMacAddress(Context context) {
		if(context==null){
			return null;
		}
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if(info!=null){
	        return info.getMacAddress();
        }
        return null;
    }
	/**
	 * 设备型号
	 * @param context
	 * @return
	 */
	public static String getDeviceType(Context context){
		return android.os.Build.MODEL;
	}
}
