package com.tyl.framework.cache;

import java.io.File;

import com.tyl.framework.log.L;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

/**
 * 缓冲目录类
 * @author LaoYing
 *
1.	目的
     在项目开发过程中，为了有效方便管理项目中产生的文件，进行了项目中本地文件目录存放结构。
2.	适用范围
     适用于公司采用Android编程语言开发的所有软件项目。
3.	若没有SDCrad的情况下
   若没有SDCrad存在的情况下，项目中需要的本地文件保存目录为/data/data/com.firstte.xx(包名)/cache/
     /data/data/com.firstte.xx(包名)/cache/ft_log   日志文件保存目录
     /data/data/com.firstte.xx(包名)/cache/ft_apk  相关APK文件保存目录
     /data/data/com.firstte.xx(包名)/cache/ft_cache 缓存文件保存目录
     /data/data/com.firstte.xx(包名)/cache/ft_pic   图片文件保存目录
     /data/data/com.firstte.xx(包名)/cache/ft_video  视频文件保存目录
     /data/data/com.firstte.xx(包名)/cache/ft_music  音频文件保存目录
     /data/data/com.firstte.xx(包名)/cache/ft_temp  临时文件保存目录
      /data/data/com.firstte.xx(包名)/cache/ft_download  下载文件保存目录
      /data/data/com.firstte.xx(包名)/cache/ft_http  http请求缓冲目录
      /data/data/com.firstte.xx(包名)/cache/ft_update  app自身更新的下载目录
       /data/data/com.firstte.xx(包名)/cache/ft_exception  app异常闪退日志目录目录
4.	若存在SDCard的情况
  若SDCrad存在的情况，项目中需要的本地文件保存目录为SDCard/com.firstte.xx(包名)/cache/
    SDCard/data/com.firstte.xx(包名)/cache/ft_log   日志文件保存目录
    SDCard/com.firstte.xx(包名)/cache/ft_apk  相关APK文件保存目录
     SDCard/com.firstte.xx(包名)/cache/ft_cache 缓存文件保存目录
     SDCard/com.firstte.xx(包名)/cache/ft_pic   图片文件保存目录
     SDCard/com.firstte.xx(包名)/cache/ft_video  视频文件保存目录
     SDCard/com.firstte.xx(包名)/cache/ft_music  音频文件保存目录
     SDCard/com.firstte.xx(包名)/cache/ft_temp  临时文件保存目录
     SDCard/com.firstte.xx(包名)/cache/ft_download  下载文件保存目录
      SDCard/com.firstte.xx(包名)/cache/ft_http  http请求缓冲目录
      SDCard/com.firstte.xx(包名)/cache/ft_update  app自身更新的下载目录
      SDCard/com.firstte.xx(包名)/cache/ft_exception  app异常闪退日志目录目录
 */
public class CacheDirectory {

	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
	
	/**
	 * 判断是否存在外部存储设备
	 * 
	 * @return 如果不存在返回false
	 */
	public static boolean hasExternalStorage() {
		Boolean externalStorage = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		return externalStorage;
	}
	
	/**
	 * 判断是否有权限操作外部存储卡
	 * @param context 上下文
	 * @return 如果没有权限返回false
	 */
	public static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	
	/**
	 * 创建并返回指定名称的缓冲目录路径
	 * @param context 上下文
	 * @param cacheName 缓冲目录名称
	 * @return 缓冲目录的路径
	 */
	public static  String getCacheDir(Context context,String cacheName){
		String dir=null;
		boolean flag=false;
		//判断是否有sdkcard,sdcard读写权限
		if(hasExternalStorage()&&hasExternalStoragePermission(context)){
			 L.d(CacheDirectory.class.getSimpleName(), "加载sdcard文件："+cacheName);
			 String cacheDir = "/"+context.getPackageName()+ "/cache/"+cacheName;
			 File file=new File(Environment.getExternalStorageDirectory().getPath()+ cacheDir);
			 flag=true;
			 if(!file.exists()){
				 flag=file.mkdirs();
				 L.d(CacheDirectory.class.getSimpleName(), "创建sdcard文件："+cacheName+":"+flag);
			 }
			 dir=file.getAbsolutePath();
		}

		if(!flag){
			File file=new File(context.getCacheDir(), cacheName);
			L.d(CacheDirectory.class.getSimpleName(), "加载缓冲文件："+cacheName);
			 if(!file.exists()){
				 boolean b=file.mkdirs();
				 L.d(CacheDirectory.class.getSimpleName(), "创建缓冲文件："+cacheName+":"+b);
			 }
			 dir=file.getAbsolutePath();
			
		}
		return dir;
	}
	
	/**
	 * 获得http请求缓冲的目录
	 * @return
	 */
	public static  String getHTTPDir(Context context){
		return getCacheDir(context,"ft_http");
	}
	
	/**
	 * 获得下载文件的目录
	 * @return
	 */
	public static  String getDownLoadDir(Context context){
		return getCacheDir(context,"ft_download");
	}
	
	/**
	 * 获得日志的目录
	 * @return
	 */
	public static  String getLogDir(Context context){
		return getCacheDir(context,"ft_log");
	}
	
	/**
	 * 获得apk目录
	 * @return
	 */
	public static  String getApkDir(Context context){
		return getCacheDir(context,"ft_apk");
	}
	
	/**
	 * 获得缓冲目录
	 * @return
	 */
	public static  String getCacheDir(Context context){
		return getCacheDir(context,"ft_cache");
	}
	
	/**
	 * 获得图像目录
	 * @return
	 */
	public static  String getPicDir(Context context){
		return getCacheDir(context,"ft_pic");
	}
	
	/**
	 * 获得视频目录
	 * @return
	 */
	public static  String getVideoDir(Context context){
		return getCacheDir(context,"ft_video");
	}
	
	/**
	 * 获得音乐目录
	 * @return
	 */
	public static  String getMusicDir(Context context){
		return getCacheDir(context,"ft_music");
	}
	
	/**
	 * 获得临时文件目录
	 * @return
	 */
	public static  String getTempDir(Context context){
		return getCacheDir(context,"ft_temp");
	}
	
	/**
	 * 获得更新下载目录
	 * @param context
	 * @return
	 */
	public  static String getUpdateDir(Context context){
		return getCacheDir(context,"ft_update");
	}
	
	/**
	 * 获得闪退日志目录
	 * @param context
	 * @return
	 */
	public static String getExceptionDir(Context context){
		return getCacheDir(context, "ft_exception");
	}
}
