package com.tyl.framework;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.tyl.framework.log.L;

/**
 * @Title FTAppManager
 * @Package com.firstte
 * @Description FTAppManager 是Activity的管理器
 * @version V1.0
 */
public class TTAppManager {
	private final static String TAG="FTAppManager";
	private static Stack<Activity> activityStack;
	private static TTAppManager instance;

	private TTAppManager() {

	}

	/**
	 * 单一实例
	 */
	public static TTAppManager getAppManager() {
		if (instance == null) {
			instance = new TTAppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	

	/**
	 * 退出应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param isBackground
	 *            是否开开启后台运行
	 */
	public void AppExit(final Context context,final Boolean isBackground) {
		
		//发送退出广播
		context.sendBroadcast(new Intent(context.getPackageName()+".exit"));
		TTLog.e(TAG, "退出系统======activityStack.size()："+activityStack.size());
		//1秒钟之后执行退出操作
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					//关闭所有activity
					finishAllActivity();
					ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
					activityMgr.killBackgroundProcesses(context.getPackageName());
				} catch (Exception e) {
					L.e(TAG, e);
				} finally {
					// 注意，如果您有后台程序运行，请不要支持此句子
					if (!isBackground) {
						System.exit(0);
					}
				}
				
			}
		}, 1000);
		
		
	}
}