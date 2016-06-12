package com.tyl.framework.widget;

import com.tyl.framework.TTApplication;

import android.content.Context;
import android.widget.Toast;

public class TTSingletonToast {
	private static TTSingletonToast instance;
	private  Toast  mToast;
    private Context context;

    private TTSingletonToast() {
        this.context = TTApplication.getApplication().getApplicationContext();
    }

    public static TTSingletonToast getInstance(){
    	if(instance==null){
    		instance=new TTSingletonToast();
    	}
    	return instance;
    }
    
    public Toast getSingletonToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        }else{
            mToast.setText(resId);
        }
        return mToast;
    }

    public Toast getSingletonToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }else{
            mToast.setText(text);
        }
        return mToast;
    }


    public void showSingletonToast(int resId) {
        getSingletonToast(resId).show();
    }

    public void showSingletonToast(String text) {
        getSingletonToast(text).show();
    }

 

}
