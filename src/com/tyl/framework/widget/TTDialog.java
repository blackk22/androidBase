package com.tyl.framework.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @author LaoYing
 * 
 * 对话框，7秒钟内是不可以手动按返回键或者点击屏幕取消的。只有手动调用cancelProgress取消或者7秒以后，可以按返回键、点击屏幕取消
 *
 */
public class TTDialog {

	private AlertDialog myProgressDialog;
	private View view;
	private static final int DIALOG_LOCK_TIME = 7000;

	public TTDialog(Activity context) {
		myProgressDialog = new AlertDialog.Builder(context).create();
		

	}
	
	public TTDialog(Activity context, String msg) {
		myProgressDialog =new AlertDialog.Builder(context).create();
		myProgressDialog.setMessage(msg);
	}
	
	
	public TTDialog(Activity context, View view) {
		myProgressDialog = new AlertDialog.Builder(context).create();
		this.view=view;
		
	}
	
	/**
	 * 
	 * @param context
	 * @param viewid  布局的资源id
	 * @param Textviewid 布局中文本显示id
	 * @param msgid 文本显示的文字资源
	 */
	public TTDialog(Activity context,int viewid,int  Textviewid,int msgid) {
		myProgressDialog = new AlertDialog.Builder(context).create();
		View view=LayoutInflater.from(context).inflate(viewid, null);
		((TextView)view.findViewById(Textviewid)).setText(msgid);
		this.view=view;
		
	}
	
	public void setMessage(String msg) {
		myProgressDialog.setMessage(msg);
	}
	
	public boolean isShow() {
		return myProgressDialog.isShowing();
	}

	public void showProgress() {
		mHandler.sendEmptyMessage(0);
	}

	public void cancelProgress() {
		mHandler.sendEmptyMessage(1);
	}

	public void hideProgress() {
		mHandler.sendEmptyMessage(3);
	}

	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				myProgressDialog.show();
				myProgressDialog.setCancelable(false);
				if(view!=null){
					myProgressDialog.getWindow().setContentView(view);
				}

				new Thread() {
					@Override
					public void run() {
						try {
							sleep(DIALOG_LOCK_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						mHandler.sendEmptyMessage(2);
					}
				}.start();
				break;
			case 1:
				//if (myProgressDialog.isShowing()){
//					System.out.println("关闭进度对话框");
					//myProgressDialog.dismiss();
					myProgressDialog.cancel();
				//}
				break;
			case 2:
				myProgressDialog.setCancelable(true);
				break;
			case 3:
				myProgressDialog.hide();
				break;
			default:
				break;
			}
		}

	};
}
