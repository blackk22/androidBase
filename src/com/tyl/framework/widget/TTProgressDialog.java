package com.tyl.framework.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class TTProgressDialog {

	private ProgressDialog mProgressDialog;
	private static final int DIALOG_LOCK_TIME = 7000;

	public TTProgressDialog(Activity context) {
		mProgressDialog = new ProgressDialog(context);

	}
	
	public TTProgressDialog(Activity context, String msg) {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(msg);
	}
	
	
	public void setMessage(String msg) {
		mProgressDialog.setMessage(msg);
	}
	
	public boolean isShow() {
		return mProgressDialog.isShowing();
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
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();

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
				//if (mProgressDialog.isShowing()){
					mProgressDialog.dismiss();
				//}
				break;
			case 2:
				mProgressDialog.setCancelable(true);
				break;
			case 3:
				mProgressDialog.hide();
				break;

			default:
				break;
			}
		}

	};
}
