package com.tyl.framework.log;

import com.tyl.framework.TTApplication;
import com.tyl.framework.log.Logger;
import com.tyl.framework.log.LoggerConfig;

/**
 * 用于框架中日志操作
 * @author LaoYing
 *
 */
public class L {

	/**
	 * 一般提示性的日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void i(Object tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.i(tag,str);
		}
	}

	/**
	 * 一般提示性的日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void i(String tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.i(tag,str);
		}

	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param str
	 */
	public static void s(Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.s(str);
		}

	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param tag
	 * @param str
	 */
	public static void s(Object tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.i(tag,str);
		}
	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param tag
	 * @param str
	 */
	public static void s(String tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.s(tag,str);
		}
	}

	/**
	 * debug调试日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void d(Object tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.d(tag,str);
		}
	}

	/**
	 * debug调试日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void d(String tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.d(tag,str);
		}
	}

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void v(Object tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.v(tag,str);
		}
	}

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void v(String tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.v(tag,str);
		}
	}

	/**
	 * warning警告日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void w(Object tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.w(tag,str);
		}
	}

	/**
	 * warning警告日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void w(String tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.w(tag,str);
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void e(Object tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.e(tag,str);
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void e(String tag, Object str) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			
			Logger.e(tag,str);
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	public static void e(Object tag, Exception ex) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			Logger.e(tag,ex);
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	public static void e(String tag, Exception ex) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			Logger.e(tag,ex);
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param log
	 * @param tr
	 */
	public static void e(Object tag, String message, Exception ex) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			Logger.e(tag, message,ex);
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param log
	 * @param tr
	 */
	public static void e(String tag, String message, Exception ex) {
		if(LoggerConfig.FRAMEWORK_DEBUG){
			Logger.e(tag, message,ex);
		}
	}

	
}
