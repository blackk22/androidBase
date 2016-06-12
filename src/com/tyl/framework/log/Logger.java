package com.tyl.framework.log;

import com.tyl.framework.TTApplication;
import com.tyl.framework.TTLog;


/**
 * @Title Logger 日志信息打印静态方法的类
 * @Package com.firstte.util.Logger
 * @Description Logger是第一特框架中打印日志类
 * @version V1.0
 */
public class Logger {
	
	/**
	 * 一般提示性的日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void i(Object tag,Object str) {
		i(tag.getClass().getSimpleName(),str);
	}

	/**
	 * 一般提示性的日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void i(String tag,Object str) {
		String name=null;
		if ((LoggerConfig.LOGCAT_DEBUG||LoggerConfig.FILE_DEBUG)&&LoggerConfig.LOGCAT_THREAD){
			name=getFunctionName();
		}
		
		if (LoggerConfig.LOGCAT_DEBUG) {
			if (name != null) {
				PrintToLogCatLogger.getInstance().i(tag,"\n------------------------------------------------------------------------------\n"
										+ name
										+ "\n"
										+ str
										+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToLogCatLogger.getInstance().i(tag,"\n------------------------------------------------------------------------------\n"
										+ str
										+ "\n------------------------------------------------------------------------------");
			}
		}
		
		if (LoggerConfig.FILE_DEBUG) {
			if (name != null) {
				PrintToFileLogger.getInstance().i(tag,"\n------------------------------------------------------------------------------\n"
										+ name
										+ "\n"
										+ str
										+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToFileLogger.getInstance().i(tag,"\n------------------------------------------------------------------------------\n"
										+ str
										+ "\n------------------------------------------------------------------------------");
			}
		}

	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param str
	 */
	public static void s(Object  str) {
		s("System.out", str);

	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param tag
	 * @param str
	 */
	public static void s(Object tag, Object str) {
		i(tag.getClass().getSimpleName(), str);
	}

	/**
	 * 控制台日志 System.out
	 * 
	 * @param tag
	 * @param str
	 */
	public static void s(String tag, Object str) {
		String name=null;
		if ((LoggerConfig.LOGCAT_DEBUG||LoggerConfig.FILE_DEBUG)&&LoggerConfig.LOGCAT_THREAD){
			name=getFunctionName();
		}
		if (LoggerConfig.LOGCAT_DEBUG) {
			if (name != null) {
				System.out.println(tag
								+ ":\t"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				System.out.println(tag
								+ ":\t"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}
		if (LoggerConfig.FILE_DEBUG) {
			if (name != null) {
				PrintToFileLogger.getInstance().s(tag,"\n------------------------------------------------------------------------------\n"
										+ name
										+ "\n"
										+ str
										+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToFileLogger.getInstance().s(tag,"\n------------------------------------------------------------------------------\n"
										+str
										+ "\n------------------------------------------------------------------------------");
			}
		}
	}

	/**
	 * debug调试日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void d(Object tag,Object str) {
		d(tag.getClass().getSimpleName(), str);
	}

	/**
	 * debug调试日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void d(String tag,Object str) {
		String name=null;
		if ((LoggerConfig.LOGCAT_DEBUG||LoggerConfig.FILE_DEBUG)&&LoggerConfig.LOGCAT_THREAD){
			name=getFunctionName();
		}
		if (LoggerConfig.LOGCAT_DEBUG) {
			if (name != null) {
				PrintToLogCatLogger
						.getInstance()
						.d(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToLogCatLogger
						.getInstance()
						.d(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}

		if (LoggerConfig.FILE_DEBUG) {
			if (name != null) {
				PrintToFileLogger
						.getInstance()
						.d(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToFileLogger
						.getInstance()
						.d(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}
	}

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void v(Object tag, Object str) {
		v(tag.getClass().getSimpleName(), str);
	}

	/**
	 * verbose啰嗦日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void v(String tag, Object str) {
		String name=null;
		if ((LoggerConfig.LOGCAT_DEBUG||LoggerConfig.FILE_DEBUG)&&LoggerConfig.LOGCAT_THREAD){
			name=getFunctionName();
		}
		if (LoggerConfig.LOGCAT_DEBUG) {
			if (name != null) {
				PrintToLogCatLogger
						.getInstance()
						.v(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToLogCatLogger
						.getInstance()
						.v(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}

		if (LoggerConfig.FILE_DEBUG) {
			if (name != null) {
				PrintToFileLogger
						.getInstance()
						.v(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToFileLogger
						.getInstance()
						.v(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}
	}

	/**
	 * warning警告日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void w(Object tag, Object str) {
		w(tag.getClass().getSimpleName(), str);
	}

	/**
	 * warning警告日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void w(String tag, Object str) {
		String name=null;
		if ((LoggerConfig.LOGCAT_DEBUG||LoggerConfig.FILE_DEBUG)&&LoggerConfig.LOGCAT_THREAD){
			name=getFunctionName();
		}
		if (LoggerConfig.LOGCAT_DEBUG) {
			if (name != null) {
				PrintToLogCatLogger
						.getInstance()
						.w(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToLogCatLogger
						.getInstance()
						.w(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}

		if (LoggerConfig.FILE_DEBUG) {
			if (name != null) {
				PrintToFileLogger
						.getInstance()
						.w(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToFileLogger
						.getInstance()
						.w(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void e(Object tag, Object str) {
		e(tag.getClass().getSimpleName(), str);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param str
	 */
	public static void e(String tag, Object str) {
		String name=null;
		if ((LoggerConfig.LOGCAT_DEBUG||LoggerConfig.FILE_DEBUG)&&LoggerConfig.LOGCAT_THREAD){
			name=getFunctionName();
		}
		if (LoggerConfig.LOGCAT_DEBUG) {
			if (name != null) {
				PrintToLogCatLogger
						.getInstance()
						.e(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToLogCatLogger
						.getInstance()
						.e(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}

		if (LoggerConfig.FILE_DEBUG) {
			if (name != null) {
				PrintToFileLogger
						.getInstance()
						.e(tag,"\n------------------------------------------------------------------------------\n"
								+ name
								+ "\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			} else {
				PrintToFileLogger
						.getInstance()
						.e(tag,"\n------------------------------------------------------------------------------\n"
								+ str
								+ "\n------------------------------------------------------------------------------");
			}
		}
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	public static void e(Object tag, Exception ex) {
		e(tag.getClass().getSimpleName(), ex);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param ex
	 */
	public static void e(String tag, Exception ex) {
		if (LoggerConfig.LOGCAT_DEBUG) {
			PrintToLogCatLogger.getInstance().e(tag, "error", ex);
		}

		if (LoggerConfig.FILE_DEBUG) {
			PrintToFileLogger
					.getInstance()
					.e(tag,"\n------------------------------------------------------------------------------\n"
							+ ex.toString()
							+ "\n------------------------------------------------------------------------------");
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
		e(tag.getClass().getSimpleName(), message, ex);
	}

	/**
	 * error错误日志
	 * 
	 * @param tag
	 * @param log
	 * @param tr
	 */
	public static void e(String tag, String message, Exception ex) {
		if (LoggerConfig.LOGCAT_DEBUG) {
			PrintToLogCatLogger.getInstance().e(tag, message + "\n", ex);
		}

		if (LoggerConfig.FILE_DEBUG) {
			PrintToFileLogger
					.getInstance()
					.e(tag,
							"\n------------------------------------------------------------------------------\n"
							+ message
							+ "--->"
							+ ex.toString()
							+ "\n------------------------------------------------------------------------------");
		}
	}

	
	/**
	 * 获得当前运行代码的相关信息
	 * 
	 * @return
	 */
	private static String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return "内存信息-->"+TTApplication.getApplication().getMemoryInfoString();
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(TTLog.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(L.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(Logger.class.getName())) {
				continue;
			}
			
			return "基本信息-->线程名:" + Thread.currentThread().getName() + ",文件名:"
					+ st.getFileName() + ",行号:" + st.getLineNumber() + ",方法名:"
					+ st.getMethodName() + 
					"\n" 
					+"内存信息-->"+TTApplication.getApplication().getMemoryInfoString();
		}
		return "内存信息-->"+TTApplication.getApplication().getMemoryInfoString();
	}
	

}
