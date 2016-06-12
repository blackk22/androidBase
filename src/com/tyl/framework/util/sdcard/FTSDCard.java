package com.tyl.framework.util.sdcard;

import java.io.File;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.os.EnvironmentCompat;

public class FTSDCard {

	/**
	 * 检测设备SD卡状态
	 * 
	 * @return 正常返回true 不正常返回false
	 */
	public static boolean checkDeviceSDEnable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED) ? true : false;
	}

	/**
	 * 检测设备SD卡剩余空间
	 * 
	 * @return 单位MB
	 */
	public static long getSDFreeSize() {
		if(!checkDeviceSDEnable()){
			return 0;
		}
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 检测设备SD卡总容量
	 * 
	 * @return 单位MB
	 */
	public static long getSDAllSize() {
		if(!checkDeviceSDEnable()){
			return 0;
		}
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}
	
	/**
	 * sdcard公共语音目录
	 * @return
	 */
	public static String getExternalStoragePublicVoiceDirectory(){
		if(!checkDeviceSDEnable()){
			return null;
		}
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();		
	}
	
	/**
	 * sdcard公共目录
	 * @return
	 */
	public static String getExternalStoragePublicDCIMDirectory(){
		if(!checkDeviceSDEnable()){
			return null;
		}
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();		
	}
}
