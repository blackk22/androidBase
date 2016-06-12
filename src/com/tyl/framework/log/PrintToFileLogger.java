package com.tyl.framework.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.tyl.framework.TTApplication;
import com.tyl.framework.cache.CacheDirectory;

/**
 * @Title PrintToFileLogger 单例设计模式 每种日志输出到当天日期的日志文件中
 * @Package com.firstte.util.log
 * @Description PrintToFileLogger是第一特框架中打印到sdcard上面的日志类
 * @version V1.0
 */
public class PrintToFileLogger implements ILogger {

	private String mPath;
	private Writer mWriter;

	private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ",Locale.getDefault());
	private static final SimpleDateFormat TIMESTAMP_FMT_FILE = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
	private String basePath = "";
	private String logDir;

	private static ILogger iLogger;

	public static ILogger getInstance() {
		if (iLogger == null) {
			iLogger = new PrintToFileLogger();
		}
		return iLogger;
	}

	private PrintToFileLogger() {
		logDir=CacheDirectory.getLogDir(TTApplication.getApplication());
	}

	@Override
	public void d(String tag, String message) {
		println(DEBUG, tag, message);
	}

	@Override
	public void e(String tag, String message) {
		println(ERROR, tag, message);
	}

	@Override
	public void i(String tag, String message) {
		println(INFO, tag, message);
	}

	@Override
	public void v(String tag, String message) {
		println(VERBOSE, tag, message);
	}

	@Override
	public void w(String tag, String message) {
		println(WARN, tag, message);
	}

	@Override
	public void e(String tag, Exception ex) {
		println(ERROR, tag, ex.toString());
	}

	@Override
	public void e(String tag, String message, Exception ex) {
		println(ERROR, tag, message + "-->" + ex.toString());
	}

	@Override
	public void s(String tag, String message) {
		println(OUT, tag, message);
	}

	private void logOpen(String tag) {

		basePath = logDir + "/" + getCurrentTimeString()+ "-" + tag + "-logger.log";
		try {
			File file = new File(basePath);
			mPath = file.getAbsolutePath();
			//System.out.println("日志文件存储位置：" + mPath);
			mWriter = new BufferedWriter(new FileWriter(mPath, true), 2048);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void println(int priority, String tag, String message) {
		switch (priority) {
		case VERBOSE:
			logOpen("verbose");
			break;
		case DEBUG:
			logOpen("debug");
			break;
		case INFO:
			logOpen("info");
			break;
		case WARN:
			logOpen("warn");
			break;
		case ERROR:
			logOpen("error");
			break;
		case OUT:
			logOpen("out");
		default:
			break;
		}
		println(message, tag);
		logClose();
	}

	private void println(String message, String tag) {
		try {
			mWriter.write(TIMESTAMP_FMT.format(new Date()));
			mWriter.write('\t');
			mWriter.write(tag);
			mWriter.write('\n');
			mWriter.write(message);
			mWriter.write('\n');
			mWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void logClose() {
		try {
			mWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getCurrentTimeString() {
		return TIMESTAMP_FMT_FILE.format(new Date());
	}

}
