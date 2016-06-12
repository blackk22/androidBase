package com.tyl.framework.log;

/**
 * @Title Logger
 * @Package com.firstte.util.log
 * @Description Logger是一个日志的接口
 * @version V1.0
 */
public interface ILogger {
	/**
	 * VERBOSE 类型调试信息，verbose啰嗦的意思
	 */
	public static final int VERBOSE = 2;

	/**
	 * DEBUG 类型调试信息, debug调试信息
	 */
	public static final int DEBUG = 3;

	/**
	 * INFO 类型调试信息, 一般提示性的消息information
	 */
	public static final int INFO = 4;

	/**
	 * WARN 类型调试信息，warning警告类型信息
	 */
	public static final int WARN = 5;

	/**
	 * ERROR 类型调试信息，错误信息
	 */
	public static final int ERROR = 6;

	/**
	 * ASSERT 类型调试信息,断言信息
	 */
	public static final int ASSERT = 7;

	/**
	 * System.out 类型调试信息,控制台信息
	 */
	public static final int OUT = 8;

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param message
	 */
	void v(String tag, String message);

	/**
	 * 调试日志
	 * 
	 * @param tag
	 * @param message
	 */
	void d(String tag, String message);

	/**
	 * 提示日志
	 * 
	 * @param tag
	 * @param message
	 */
	void i(String tag, String message);

	/**
	 * 警告日志
	 * 
	 * @param tag
	 * @param message
	 */
	void w(String tag, String message);

	/**
	 * 错误日志
	 * 
	 * @param tag
	 * @param message
	 */
	void e(String tag, String message);

	/**
	 * 错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	void e(String tag, Exception ex);

	/**
	 * 错误日志
	 * 
	 * @param tag
	 * @param message
	 * @param ex
	 */
	void e(String tag, String message, Exception ex);

	/**
	 * 控制台日志
	 * 
	 * @param message
	 */
	void s(String tag, String message);
}
