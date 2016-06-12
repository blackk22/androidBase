package com.tyl.framework.widget;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tyl.framework.TTActivity;

public class TTAppExitToast {
	
	private static boolean isExit = false;
	private static boolean isExitGo = false;
	private static TTAppExitToast appExitToast;
	public static  TTAppExitToast getInstance(){
		if(appExitToast==null){
			appExitToast=new TTAppExitToast();
		}
		return appExitToast;
	}

	  // 定义一个变量，来标识是否退出
   
    private Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    isExit = false;
            }
    };
    
    
    public  void exit(TTActivity activity,String msg) {
        if (!isExit) {
                isExit = true;
                Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
        	if(isExitGo==false){
        		isExitGo=true;
        		activity.getFTApplication().exitApp(false);
        	}
        }
    }
    
    public  void exit(TTActivity activity,int  resid) {
        if (!isExit) {
                isExit = true;
                Toast.makeText(activity,resid, Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
        	if(isExitGo==false){
        		isExitGo=true;
        		activity.getFTApplication().exitApp(false);
        	}
        }
    }
}
