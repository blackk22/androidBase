package com.tyl.framework.util.meta;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.tyl.framework.log.L;

public class MetaDataUtil {
	private final static String TAG="MetaDataUtil";
	private static ApplicationInfo info=null;

	private static Bundle getbBundle(Context context){
		if(info==null){
			try {
				info = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
			} catch (NameNotFoundException e) {
				L.e(TAG,e);
			}
		}
		return info.metaData;
	}
	/**
	 * 读取配置  返回boolean  如果没有找到值返回默认值false
	 * <meta-data android:name="com.firstte.logger.file"  android:value="true" />
	 */
   public static boolean readMetaDataBoolean(Context context,String name){
		Bundle bundle=getbBundle(context);
		if(bundle!=null){
			return bundle.getBoolean(name,false);
		}
		return false;
		
	}
   
   /**
	 * 读取配置  返回boolean
	 * <meta-data android:name="com.firstte.logger.file"  android:value="true" />
	 */
   public static boolean readMetaDataBoolean(Context context,String name,boolean defaultValue){
	   Bundle bundle=getbBundle(context);
		if(bundle!=null){
			return bundle.getBoolean(name,defaultValue);
		}
		return defaultValue;
	}
   
   
   /**
	 * 读取配置  返回Int 如果没有找到值返回默认值0
	 * <meta-data android:name="com.firstte.logger.file"  android:value="2" />
	 */
  public static int readMetaDataInt(Context context,String name){
		Bundle bundle=getbBundle(context);
		if(bundle!=null){
			return bundle.getInt(name,0);
		}
		return 0;
		
	}
  
  /**
	 * 读取配置  返回Int
	 * <meta-data android:name="com.firstte.logger.file"  android:value="1" />
	 */
  public static int readMetaDataInt(Context context,String name,int defaultValue){
	   Bundle bundle=getbBundle(context);
		if(bundle!=null){
			return bundle.getInt(name,defaultValue);
		}
		return defaultValue;
	}
   
  
  /**
 	 * 读取配置  返回Int 如果没有找到值返回默认值null
 	 * <meta-data android:name="com.firstte.logger.file"  android:value="adb" />
 	 */
   public static String readMetaDataString(Context context,String name){
 		Bundle bundle=getbBundle(context);
 		if(bundle!=null){
 			return bundle.getString(name);
 		}
 		return null;
 		
 	}
   
   /**
 	 * 读取配置  返回Int
 	 * <meta-data android:name="com.firstte.logger.file"  android:value="anb" />
 	 */
   public static String readMetaDataString(Context context,String name,String defaultValue){
 	   Bundle bundle=getbBundle(context);
 	  String value=null;
 		if(bundle!=null){
 			value= bundle.getString(name);
 		}
 		if(value!=null){
 			return value;
 		}
 		return defaultValue;
 	}
   
   /**
	 * 读取配置  返回double 如果没有找到值返回默认值0.0
	 * <meta-data android:name="com.firstte.logger.file"  android:value="0.0" />
	 */
   public static double readMetaDataDouble(Context context,String name){
		Bundle bundle=getbBundle(context);
		if(bundle!=null){
			return bundle.getDouble(name,0.0);
		}
		return 0;
		
	}
 
 /**
	 * 读取配置  返回double
	 * <meta-data android:name="com.firstte.logger.file"  android:value="0.0" />
	 */
   public static double readMetaDataDouble(Context context,String name,double defaultValue){
	   Bundle bundle=getbBundle(context);
		if(bundle!=null){
			return bundle.getDouble(name,defaultValue);
		}
		return defaultValue;
	}
}
