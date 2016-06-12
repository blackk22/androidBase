package com.tyl.framework.util.net;

import java.lang.reflect.Method;

import com.tyl.framework.TTLog;
import com.tyl.framework.log.L;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @Title NetWorkUtil
 * @Package com.firstte.util.netstate
 * @Description 是检测网络的一个工具包
 * @version V1.0
 */
public class NetWorkUtil {
	private static final String TAG = NetWorkUtil.class.getSimpleName();
	
	/**
	 * wifi 无线网络, CMNET net方式移动网络, CMWAP wap方式移动网络, noneNet没有网络
	 * 
	 * @Description 是网络类型枚举
	 * @version V1.0
	 * 
	 */
	public static enum netType {
		wifi, CMNET, CMWAP, noneNet
	}
	
	/**
	 * 获取ConnectivityManager
	 */
	public static ConnectivityManager getConnManager(Context context) {
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 网络是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo[] info = getConnManager(context).getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			NetworkInfo mNetworkInfo = getConnManager(context).getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			NetworkInfo mWiFiNetworkInfo = getConnManager(context)
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断有无网络正在连接中（查找网络、校验、获取IP等）。
	 * @param context
	 * @return boolean 不管wifi，还是mobile net，只有当前在连接状态（可有效传输数据）才返回true,反之false。
	 */
	public static boolean isConnectedOrConnecting(Context context) {
		NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
		if (nets != null) {
			for (NetworkInfo net : nets) {
				if (net.isConnectedOrConnecting()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			NetworkInfo mMobileNetworkInfo = getConnManager(context)
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return android.net.ConnectivityManager.TYPE_XXXX
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			NetworkInfo mNetworkInfo = getConnManager(context)
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static netType getAPNType(Context context) {
		if(context!=null){
			NetworkInfo networkInfo = getConnManager(context).getActiveNetworkInfo();
			if (networkInfo == null) {
				return netType.noneNet;
			}
			int nType = networkInfo.getType();
	
			if (nType == ConnectivityManager.TYPE_MOBILE) {
				if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
					return netType.CMNET;
				}
	
				else {
					return netType.CMWAP;
				}
			} else if (nType == ConnectivityManager.TYPE_WIFI) {
				return netType.wifi;
			}
		}
		return netType.noneNet;

	}
	/**
	 * 判断是否有可用状态的Wifi，以下情况返回false：
	 *  1. 设备wifi开关关掉;
	 *  2. 已经打开飞行模式；
	 *  3. 设备所在区域没有信号覆盖；
	 *  4. 设备在漫游区域，且关闭了网络漫游。
	 *  
	 * @param context
	 * @return boolean wifi为可用状态（不一定成功连接，即Connected）即返回ture
	 */
	public static boolean isWifiAvailable(Context context) {
		NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
		if (nets != null) {
			for (NetworkInfo net : nets) {
				if (net.getType() == ConnectivityManager.TYPE_WIFI) { return net.isAvailable(); }
			}
		}
		return false;
	}

	/**
	 * 判断有无可用状态的移动网络，注意关掉设备移动网络直接不影响此函数。
	 * 也就是即使关掉移动网络，那么移动网络也可能是可用的(彩信等服务)，即返回true。
	 * 以下情况它是不可用的，将返回false：
	 *  1. 设备打开飞行模式；
	 *  2. 设备所在区域没有信号覆盖；
	 *  3. 设备在漫游区域，且关闭了网络漫游。
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobileAvailable(Context context) {
		NetworkInfo[] nets = getConnManager(context).getAllNetworkInfo();
		if (nets != null) {
			for (NetworkInfo net : nets) {
				if (net.getType() == ConnectivityManager.TYPE_MOBILE) { return net.isAvailable(); }
			}
		}
		return false;
	}

	/**
	 * 设备是否打开移动网络开关
	 * @param context
	 * @return boolean 打开移动网络返回true，反之false
	 */
	public static boolean isMobileEnabled(Context context) {
		try {
			Method getMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
			getMobileDataEnabledMethod.setAccessible(true);
			return (Boolean) getMobileDataEnabledMethod.invoke(getConnManager(context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 反射失败，默认开启
		return true;
	}

	/**
	 * 打印当前各种网络状态
	 * @param context
	 * @return boolean
	 */
	public static void printNetworkInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo in = connectivity.getActiveNetworkInfo();
			TTLog.i(TAG, "-------------$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$-------------");
			TTLog.i(TAG, "getActiveNetworkInfo: " + in);
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					// if (info[i].getType() == ConnectivityManager.TYPE_WIFI) {
					TTLog.i(TAG, "NetworkInfo[" + i + "]isAvailable : " + info[i].isAvailable());
					TTLog.i(TAG, "NetworkInfo[" + i + "]isConnected : " + info[i].isConnected());
					TTLog.i(TAG, "NetworkInfo[" + i + "]isConnectedOrConnecting : " + info[i].isConnectedOrConnecting());
					TTLog.i(TAG, "NetworkInfo[" + i + "]: " + info[i]);
					// }
				}
				TTLog.i(TAG, "\n");
			} else {
				TTLog.i(TAG, "getAllNetworkInfo is null");
			}
		}
	}
	
	/**
	 * 获得连接wifi信息
	 * @param context
	 * @return
	 */
	public static WifiInfo getWifiInfo(Context context) {
		if(context==null){
			return null;
		}
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo();
    }
	
	/**
	 * 获得wifi mac地址
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		if(context==null){
			return null;
		}
        WifiInfo info = getWifiInfo(context);
        if(info!=null){
	        String mac = info.getMacAddress();
	        L.i(TAG, " MAC：" + mac);
	        return mac;
        }
        return null;
    }
	
	/**
	 * 获得wifi mac地址 BSSID形式
	 * @param context
	 * @return
	 */
	public static String getBSSID(Context context) {
		if(context==null){
			return null;
		}
        WifiInfo info = getWifiInfo(context);
        if(info!=null){
	        String bssid = info.getBSSID();
	        L.i(TAG, " bssid：" + bssid);
	        return bssid;
        }
        return null;
    }
	
	/**
	 * 获得wifi SSID
	 * @param context
	 * @return
	 */
	public static String getSSID(Context context) {
		if(context==null){
			return null;
		}
        WifiInfo info = getWifiInfo(context);
        if(info!=null){
	        String ssid = info.getSSID();
	        L.i(TAG, " SSID：" + ssid);
	        return ssid;
        }
        return null;
    }
}
