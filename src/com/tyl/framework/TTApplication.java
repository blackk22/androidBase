package com.tyl.framework;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Process;
import android.text.format.Formatter;

import com.tyl.framework.config.ConfigType;
import com.tyl.framework.config.IConfig;
import com.tyl.framework.config.PreferenceConfig;
import com.tyl.framework.config.PropertiesConfig;
import com.tyl.framework.db.SQLiteDatabasePool;
import com.tyl.framework.db.TTSQLiteDatabase.DBUpdateListener;
import com.tyl.framework.exception.CrashExceptionHandler;
import com.tyl.framework.log.LoggerConfig;
import com.tyl.framework.update.VersionInfo;
import com.tyl.framework.util.meta.MetaDataUtil;


public class TTApplication extends Application{

	/**应用程序对象*/
	private static TTApplication application;
	
	/**Activity管理对象*/
	private  ActivityManager activityManager;
	
	/** 配置器 */
	private IConfig mCurrentConfig;
	
	/** FirstTe数据库链接池 */
	private SQLiteDatabasePool mSQLiteDatabasePool;
	
	/**
	 * 升级监听器
	 */
	private DBUpdateListener mTadbUpdateListener;
	
	/** FirstTe 应用程序运行Activity管理器 */
	private TTAppManager mAppManager;
	
	/**当前是否在更新*/
	private  boolean updatestatus=false;
	/**服务器版本信息*/
	private  VersionInfo versionInfo;
	/**应用程序的 icon图标*/
	private int icon=-1;
	
	@Override
	public void onCreate() {
		super.onCreate();
		TTLog.d("framework",this.getClass().getSimpleName()+ "===========onCreate===========");
		TTApplication.application = this;
		activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		
		//初始化图片加载
		initImageLoader();
		
		//读取配置，是否开启日志功能
		LoggerConfig.getLoggerConfig(this);
		
		//闪退异常退出捕捉初始化，读取配置CrashException 值
		if(MetaDataUtil.readMetaDataBoolean(this, "CrashException", false)){
			CrashExceptionHandler.getInstance().init(this);
		}
	}
	
	/**
	 * 获取Application
	 * 
	 * @return
	 */
	public static TTApplication getApplication() {
		return application;
	}
	
	/**
	 * 获得ActivityManager
	 * @return
	 */
	public ActivityManager getActivityManager() {
		return activityManager;
	}
	
	/**
	 * 返回当前内存情况，数组形式，
	 * memory[0] 手机可用内存
	 * memory[1] 当前应用占用内存
	 * memory[2] 当前应用分配总内存
	 * memory[3] 当前应用可分配最大内存
	 * memory[4] 当前应用空隙内存
	 * @return
	 */
	public long[] getMemoryInfo(){
		long[] memory=new long[5];
		MemoryInfo memoryInfo=new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		memory[0]=memoryInfo.availMem;
		memory[1]=activityManager.getProcessMemoryInfo(new int[]{Process.myPid()})[0].dalvikPrivateDirty*1024;
		memory[2]=Runtime.getRuntime().totalMemory();
		memory[3]=Runtime.getRuntime().maxMemory();
		memory[4]=Runtime.getRuntime().freeMemory();
		return memory;
	}
	
	/**
	 * 格式化显示内存情况
	 * @return
	 */
	public  String  getMemoryInfoString() {
		long[] memory=getMemoryInfo();
		return "可用内存:"+Formatter.formatFileSize(this, memory[0])+",当前应用占用内存："+Formatter.formatFileSize(this, memory[1])+",总内存:"+Formatter.formatFileSize(this, memory[2])+",最大内存："+Formatter.formatFileSize(this, memory[3])+",空闲内存："+Formatter.formatFileSize(this, memory[4]);
	}

	/**
	 * 获得配置对象
	 */
	public IConfig initConfig(ConfigType confingType) {
		IConfig myconfig=null;
		if (confingType == ConfigType.PREFERENCE) {
			myconfig = PreferenceConfig.getPreferenceConfig(this);

		} else if (confingType == ConfigType.PROPERTIES) {
			myconfig = PropertiesConfig.getPropertiesConfig(this);
		} else {
			myconfig = PropertiesConfig.getPropertiesConfig(this);
		}
		
		if (!myconfig.isLoadConfig()) {
			myconfig.loadConfig();
		}
		return myconfig;
	}

	/**
	 * 获得当前设置配置对象
	 * @return
	 */
	public IConfig getCurrentConfig() {
		if (mCurrentConfig == null) {
			mCurrentConfig=initConfig(ConfigType.PREFERENCE);
		}
		return mCurrentConfig;
	}
	
	/**
	 * 获得当前更新状态
	 * @return
	 */
	public boolean isUpdatestatus() {
		return updatestatus;
	}

	/**
	 * 设置更新状态
	 * @param updatestatus
	 */
	public void setUpdatestatus(boolean updatestatus) {
		this.updatestatus = updatestatus;
	}

	/**
	 * 获得服务器版本
	 * @return
	 */
	public VersionInfo getVersionInfo() {
		return versionInfo;
	}

	/**
	 * 设置服务器版本
	 * @param versionInfo
	 */
	public void setVersionInfo(VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	/**
	 * 获得应用图标
	 * @return
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * 设置应用图标
	 * @param icon
	 */
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	/**
	 * 获得sqlite数据库池
	 * @param mTadbUpdateListener 数据库升级监听接口
	 * @return
	 */
	public SQLiteDatabasePool getSQLiteDatabasePool(DBUpdateListener mTadbUpdateListener) {
		this.mTadbUpdateListener = mTadbUpdateListener;
		
		if (mSQLiteDatabasePool == null) {
			mSQLiteDatabasePool = SQLiteDatabasePool.getInstance(this);
			mSQLiteDatabasePool.setOnDbUpdateListener(mTadbUpdateListener);
			mSQLiteDatabasePool.createPool();
		}
		return mSQLiteDatabasePool;
	}
	
	/**
	 * 设置sqlite数据库池
	 * @param sqliteDatabasePool
	 */
	public void setSQLiteDatabasePool(SQLiteDatabasePool sqliteDatabasePool) {
		this.mSQLiteDatabasePool = sqliteDatabasePool;
	}
	

	/**
	 * 获得应用程序管理器
	 * @return
	 */
	public TTAppManager getAppManager() {
		if (mAppManager == null) {
			mAppManager = TTAppManager.getAppManager();
		}
		return mAppManager;
	}
	
	/**
	 * 退出应用程序
	 * 
	 * @param isBackground
	 *            是否开开启后台运行,如果为true则为后台运行
	 */
	public void exitApp(Boolean isBackground) {
		//取消所有通知
		((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
		mAppManager.AppExit(this, isBackground);
	}
	
	/**
	 * 在子类中重写，初始化图像加载自定义配置
	 */
	public void initImageLoader() {
		TTImageLoader.init(this);
	}
}
