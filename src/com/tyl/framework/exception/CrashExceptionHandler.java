package com.tyl.framework.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.tyl.framework.cache.CacheDirectory;
import com.tyl.framework.log.L;

import android.content.Context;
import android.os.Environment;



/**
 * 
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告
 *
 */
public class CrashExceptionHandler implements UncaughtExceptionHandler{
	private final static String TAG = "UncaughtException";
    private static CrashExceptionHandler mUncaughtException;
    private Context context;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.getDefault());
    private static final SimpleDateFormat TIMESTAMP_FMT_FILE = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    // 用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();  
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private CrashExceptionHandler() {
       
    }

    /**
     * 同步方法，以免单例多线程环境下出现异常
     * 
     * @return
     */
    public synchronized static CrashExceptionHandler getInstance() {
        if (mUncaughtException == null) {
            mUncaughtException = new CrashExceptionHandler();
        }
        return mUncaughtException;
    }

    /**
     * 初始化，把当前对象设置成UncaughtExceptionHandler处理器
     */
    public void init(Context context) {
    	L.i(TAG, "初始化闪退异常捕捉");
    	this.context=context;
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtException);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
    	System.out.println("闪退捕捉成功");
    	ex.printStackTrace();
    	//处理异常,我们还可以把异常信息写入文件，以供后来分析。
       String filename=saveCrashInfo2File(ex);
       L.i(this, "异常信息存储位置："+filename);
       System.exit(1);
  
    }
    
    /** 
     * 保存错误信息到文件中 
     *  
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    private String saveCrashInfo2File(Throwable ex) {  
        StringBuffer sb = new StringBuffer(); 
       
        String time = formatter.format(new Date()); 
        sb.append("\n"+time+"----");
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
  
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
  
        String result = writer.toString();  
        sb.append(result); 
        //写入文件
        try {
            String fileName = CacheDirectory.getExceptionDir(context) +"/ex_"+TIMESTAMP_FMT_FILE.format(new Date())+".log";  
            FileOutputStream fos = new FileOutputStream(fileName,true);  
            fos.write(sb.toString().getBytes());  
            fos.close();
            return fileName;  
        } catch (Exception e) {  
           e.printStackTrace();
        }  
  
        return null;  
    }  
	
}