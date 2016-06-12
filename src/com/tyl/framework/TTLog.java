package com.tyl.framework;

import com.tyl.framework.log.Logger;

/**
 * 用于工程中日志操作，使用规范：
 * 1.调试开发只能用d
 * 2.异常错误只能用e
 * 3.一般信息只能用i
 * @author LaoYing
 *
 */
public class TTLog {

	/**
	 * 一般提示性的日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void i(Object tag, Object str) {
			Logger.i(tag,str);
	}

	/**
	 * 一般提示性的日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void i(String tag, Object str) {
			Logger.i(tag,str);
	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param str
	 */
	public static void s(Object str) {
			Logger.s(str);
	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param tag
	 * @param str
	 */
	public static void s(Object tag, Object str) {
			Logger.i(tag,str);
	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param tag
	 * @param str
	 */
	public static void s(String tag, Object str) {
			Logger.s(tag,str);
	}

	/**
	 * debug调试日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void d(Object tag, Object str) {
			Logger.d(tag,str);
	}

	/**
	 * debug调试日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void d(String tag, Object str) {
			Logger.d(tag,str);
	}

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void v(Object tag, Object str) {
			Logger.v(tag,str);
	}

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void v(String tag, Object str) {
			Logger.v(tag,str);
	}

	/**
	 * warning警告日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void w(Object tag, Object str) {
			Logger.w(tag,str);
	}

	/**
	 * warning警告日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void w(String tag, Object str) {
			Logger.w(tag,str);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void e(Object tag, Object str) {
			Logger.e(tag, str);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void e(String tag, Object str) {
			Logger.e(tag, str);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	public static void e(Object tag, Exception ex) {
			Logger.e(tag,ex);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	public static void e(String tag, Exception ex) {
			Logger.e(tag,ex);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param log
	 * @param tr
	 */
	public static void e(Object tag, String message, Exception ex) {
			Logger.e(tag, message,ex);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param log
	 * @param tr
	 */
	public static void e(String tag, String message, Exception ex) {
			Logger.e(tag, message,ex);
	}

	
}
