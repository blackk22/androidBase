package com.tyl.framework.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * @Title LoggerConfig
 * @Package com.firstte.util.log
 * @Description LoggerConfig是一个日志是否开启配置
 * @version V1.0
 */
public class LoggerConfig {
	/**
	 * 控制日志输出到文件，true 表示开启、 false 表示关闭
	 */
	public static boolean FILE_DEBUG = false;

	/**
	 * 控制日志输出到logcat，true 表示开启、 false 表示关闭
	 */
	public static boolean LOGCAT_DEBUG = false;
	
	/**
	 * 控制日志输出时候是否带线程信息，比较耗性能,建议关闭，true 表示开启、 false 表示关闭
	 */
	public static boolean LOGCAT_THREAD = false;
	
	/**
	 * 用于控制框架中的日志是否输出
	 */
	public static boolean FRAMEWORK_DEBUG=true;
	
	/**
	 * 读取配置
	 *  <!-- 日志是否开启 输出文件 -->
        <meta-data
            android:name="logger.file"
            android:value="false" />
        <!-- 日志是否开启 输出logcat -->
        <meta-data
            android:name="logger.logcat"
            android:value="true" />
        <!-- 日志是包含线程信息 -->
        <meta-data
            android:name="logger.thread"
            android:value="true" />
        <!-- 框架日志是否输出 -->
        <meta-data
            android:name="logger.framework"
            android:value="true" />
	 */
   public static void getLoggerConfig(Context context){
		ApplicationInfo info=null;
		try {
			info = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(info!=null){
			Bundle bundle=info.metaData;
			if(bundle!=null){
				FILE_DEBUG=bundle.getBoolean("logger.file");
				LOGCAT_DEBUG=bundle.getBoolean("logger.logcat");
				LOGCAT_THREAD=bundle.getBoolean("logger.thread");
				FRAMEWORK_DEBUG=bundle.getBoolean("logger.framework");
				System.out.println("FILE_DEBUG:"+FILE_DEBUG);
				System.out.println("LOGCAT_DEBUG:"+LOGCAT_DEBUG);
				System.out.println("LOGCAT_THREAD:"+LOGCAT_THREAD);
				System.out.println("FRAMEWORK_DEBUG:"+FRAMEWORK_DEBUG);
				
			}
		}
		
	}
}
