package com.tyl.framework.log;

import android.util.Log;

/**
 * @Title TAPrintToLogCatLogger 单例设计模式
 * @Package com.firstte.core.log
 * @Description PrintToLogCatLogger是第一特框架中打印到LogCat上面的日志类
 * @version V1.0
 */
public class PrintToLogCatLogger implements ILogger {
	private static ILogger iLogger;

	public static ILogger getInstance() {
		if (iLogger == null) {
			iLogger = new PrintToLogCatLogger();
		}
		return iLogger;
	}

	private PrintToLogCatLogger() {

	}

	@Override
	public void d(String tag, String message) {
		Log.d(tag, message);
	}

	@Override
	public void e(String tag, String message) {
		Log.e(tag, message);
	}

	@Override
	public void i(String tag, String message) {
		Log.i(tag, message);
	}

	@Override
	public void v(String tag, String message) {
		Log.v(tag, message);
	}

	@Override
	public void w(String tag, String message) {
		Log.w(tag, message);
	}

	@Override
	public void e(String tag, Exception ex) {
		Log.e(tag, "error", ex);

	}

	@Override
	public void e(String tag, String message, Exception ex) {
		Log.e(tag, message, ex);

	}

	@Override
	public void s(String tag, String message) {
		System.out.println(tag + ":" + message);

	}

}
