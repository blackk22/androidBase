package com.tyl.framework.util.app;

import java.io.File;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * @Title VersionUtils
 * @Package com.firstte.util.extend.app
 * @Description VersionUtils app 应用程序包相关信息类、安装、卸载
 * @version V1.0
 */
public class VersionUtils {

	/**
	 * 获得当前应用程序包名
	 * @param context
	 * @return
	 */
	public static String getCurrentPackageName(Context context) {
		return context.getPackageName();
	}

	/**
	 * 获取当前程序版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurrentVersionCode(Context context) {
		int versioncode = -1;
		try {
			versioncode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versioncode;
	}

	

	/**
	 * 获取指定程序版本号
	 * @param context
	 * @param packageName 应用程序包名称
	 * @return
	 */
	public static int getVersionCode(Context context,String packageName) {
		int versioncode = -1;
		try {
			versioncode = context.getPackageManager().getPackageInfo(
					packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versioncode;
	}
	
	
	/**
	 * 获取当前程序版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersionName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
	 * 获取指定程序版本名称
	 * @param context
	 * @param packageName 应用程序包名称
	 * @return
	 */
	public static String getVersionName(Context context,String packageName) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	
	/**
	 * 获得应用程序的icon图标
	 * @param context
	 * @return
	 */
	public static Drawable getCurrentIcon(Context context){
		Drawable drawable=null;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(context.getPackageName(), 0);
			ApplicationInfo appinfo=pinfo.applicationInfo;
			drawable=appinfo.loadIcon(pm);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return drawable;
	}
	
	
	/**
	 * 获得当前应用程序的 标签名称
	 * @param context
	 * @return
	 */
	public static String getCurrentLabelName(Context context){
		String labelname=null;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(context.getPackageName(), 0);
			ApplicationInfo appinfo=pinfo.applicationInfo;
			labelname=appinfo.loadLabel(pm).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return labelname;
	}
	
	/**
	 * 获得指定应用程序包名称 应用程序的 标签名称
	 * @param context
	 * @return
	 */
	public static String getLabelName(Context context,String packagename){
		String labelname=null;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(packagename, 0);
			ApplicationInfo appinfo=pinfo.applicationInfo;
			labelname=appinfo.loadLabel(pm).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return labelname;
	}
	
	/**
	 * 获得当前应用程序的 安装时间
	 * @param context
	 * @return
	 */
	public static long getCurrentInstallTime(Context context){
		long time=0;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(context.getPackageName(), 0);
			time=pinfo.firstInstallTime;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	/**
	 * 获得某个应用程序的 安装时间
	 * @param context
	 * @return
	 */
	public static long getInstallTime(Context context,String packagename){
		long time=0;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(packagename, 0);
			time=pinfo.firstInstallTime;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	
	/**
	 * 获得当前应用程序的 最新更新时间
	 * @param context
	 * @return
	 */
	public static long getCurrentUpdateTime(Context context){
		long time=0;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(context.getPackageName(), 0);
			time=pinfo.lastUpdateTime;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	/**
	 * 获得某个应用程序的  最新更新时间
	 * @param context
	 * @return
	 */
	public static long getUpdateTime(Context context,String packagename){
		long time=0;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(packagename, 0);
			time=pinfo.lastUpdateTime;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	
	/**
	 * 获得应用程序的icon图标
	 * @param context
	 * @return
	 */
	public static Drawable getIcon(Context context,String packagename){
		Drawable drawable=null;
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo pinfo=pm.getPackageInfo(packagename, 0);
			ApplicationInfo appinfo=pinfo.applicationInfo;
			drawable=appinfo.loadIcon(pm);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return drawable;
	}
	
	/**
	 * 获得指定apk文件的 包信息对象
	 * @param context
	 * @param fpath
	 * @return
	 */
	public static PackageInfo getApkPackageInfo(Context context,String fpath){
		PackageInfo packageInfo=null;
		if(fpath==null||new File(fpath).exists()==false){
			return packageInfo;
		}
		
		PackageManager pm=context.getPackageManager();
		packageInfo=pm.getPackageArchiveInfo(fpath, PackageManager.GET_ACTIVITIES);
		return packageInfo;
	}
	
	/**
	 * 获得指定apk文件的 包名称
	 * @param context
	 * @param fpath
	 * @return
	 */
	public static String getApkPackageName(Context context,String fpath){

		PackageInfo packageInfo=getApkPackageInfo(context,fpath);
		if(packageInfo!=null){
			return packageInfo.packageName;
		}
		return "";
	}
	
	/**
	 * 获得指定apk文件的 版本号
	 * @param context
	 * @param fpath
	 * @return
	 */
	public static int getApkVersionCode(Context context,String fpath){

		PackageInfo packageInfo=getApkPackageInfo(context,fpath);
		if(packageInfo!=null){
			return packageInfo.versionCode;
		}
		return -1;
	}
	
	/**
	 * 获得指定apk文件的 版本名称
	 * @param context
	 * @param fpath
	 * @return
	 */
	public static String getApkVersionName(Context context,String fpath){

		PackageInfo packageInfo=getApkPackageInfo(context,fpath);
		if(packageInfo!=null){
			return packageInfo.versionName;
		}
		return "";
	}
	
	/**
	 * 获得指定apk文件的 icon图标
	 * @param context
	 * @param fpath
	 * @return
	 */
	public static Drawable getApkIcon(Context context,String fpath){

		PackageInfo packageInfo=getApkPackageInfo(context,fpath);
		ApplicationInfo appinfo=packageInfo.applicationInfo;
		if(packageInfo!=null){
			return appinfo.loadIcon(context.getPackageManager());
		}
		return null;
	}
	
	/**
	 * 获得指定apk文件的 标签名称
	 * @param context
	 * @param fpath
	 * @return
	 */
	public static String getApkLabelName(Context context,String fpath){
		String labelName="";
		PackageInfo packageInfo=getApkPackageInfo(context,fpath);
		ApplicationInfo appinfo=packageInfo.applicationInfo;
		if(packageInfo!=null){
			labelName=appinfo.loadLabel(context.getPackageManager()).toString();
		}
		return labelName;
	}
}
