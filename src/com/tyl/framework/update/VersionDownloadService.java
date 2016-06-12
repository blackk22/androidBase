package com.tyl.framework.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.tyl.framework.TTApplication;
import com.tyl.framework.R;
import com.tyl.framework.TTResource;
import com.tyl.framework.cache.CacheDirectory;
import com.tyl.framework.http.AsyncHttpClient;
import com.tyl.framework.http.FileHttpResponseHandler;
import com.tyl.framework.log.L;
import com.tyl.framework.util.app.PackageUtils;
import com.tyl.framework.util.text.StringUtils;

public class VersionDownloadService extends Service {

	private String DOWNLOAD_DIR;
	private NotificationManager notifyManager;
	private Notification notification;
	private int predownloadPercen=0;
	private Handler handler=new Handler();
	private TTApplication ftapp=TTApplication.getApplication();

	
	// 创建异步httpclient
	private AsyncHttpClient syncHttpClient = new AsyncHttpClient();

	// 文件响应handler
	class MyFileHttpResponseHandler extends FileHttpResponseHandler {

		public MyFileHttpResponseHandler(String filePath) {
			super(filePath);
		}

		@Override
		public void onProgress(long totalSize, long currentSize, long speed) {
			super.onProgress(totalSize, currentSize, speed);
			L.i("MyFileHttpResponseHandler", "总字节："+totalSize);
			int downloadPercent = (int)(currentSize * 100 / totalSize);
			if(predownloadPercen==0){
				//notification.contentView.setProgressBar(R.id.progressBar, (int)totalSize,0, false);
				notification.contentView.setProgressBar(TTResource.getIdByName(getApplicationContext(), "id", "progressBar"), (int)totalSize,0, false);
				//notification.contentView.setTextViewText(com.tyl.framework.R.id.text, "0%");
				notification.contentView.setTextViewText(TTResource.getIdByName(getApplicationContext(), "id", "text"), "0%");
				notifyManager.notify(1031, notification);
				
			}
			if(predownloadPercen<downloadPercent){
//				System.out.println("下载百分比："+downloadPercent+"%");
				predownloadPercen=downloadPercent;
				//notification.icon = R.drawable.ic_launcher;//通知图标
				notification.tickerText = "下载通知";//通知提示
				notification.when = System.currentTimeMillis();
				//notification.contentView.setTextViewText(R.id.text, "下载百分比："+downloadPercent+"%");
				notification.contentView.setTextViewText(TTResource.getIdByName(getApplicationContext(), "id", "text"), "下载百分比："+downloadPercent+"%");
				//notification.contentView.setProgressBar(R.id.progressBar, (int)totalSize,(int)currentSize, false);
				notification.contentView.setProgressBar(TTResource.getIdByName(getApplicationContext(), "id", "progressBar"), (int)totalSize,(int)currentSize, false);
				notifyManager.notify(1031, notification);
			}
			 
		}

		@Override
		public void onFailure(Throwable error) {
			super.onFailure(error);
			ftapp.setUpdatestatus(false);
			VersionDownloadService.this.stopSelf();
		}

		@Override
		public void onSuccess(byte[] binaryData) {
			super.onSuccess(binaryData);
			ftapp.setUpdatestatus(false);
			L.i("VersionDownloadService", "下载文件："+getFile().getAbsolutePath());
			
			//notification.contentView.setTextViewText(R.id.text, "下载百分比：100%，进行安装中...");
			notification.contentView.setTextViewText(TTResource.getIdByName(getApplicationContext(), "id", "text"), "下载百分比：100%，进行安装中...");
			//notification.contentView.setProgressBar(R.id.progressBar, 100,100, false);
			notification.contentView.setProgressBar(TTResource.getIdByName(getApplicationContext(), "id", "progressBar"), 100,100, false);
			notifyManager.notify(1031, notification);
			
			//进行安装操作
			final boolean installstatus= PackageUtils.installNormal(VersionDownloadService.this, getFile().getAbsolutePath());
			notifyManager.cancel(1031);
			
			/*
			//安装错误时候，会执行
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					L.i("install", "安装情况："+installstatus);
					
					VersionDownloadService.this.stopSelf();
				}
			},500);		
			*/
		}

	};
	
	public void notification(int totalSize, int currentSize, int speed) {// 重写onReceive方法

		
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L.i("VersionDownloadService", ftapp.isUpdatestatus());
		if (!ftapp.isUpdatestatus()) {
			predownloadPercen=0;
			if (ftapp.getVersionInfo() != null
					&& !StringUtils.isEmpty(ftapp.getVersionInfo().getDownurl())) {
				ftapp.setUpdatestatus(true);
				
				DOWNLOAD_DIR = CacheDirectory.getUpdateDir(this)+"/"+System.currentTimeMillis() + ".apk";
						//ExternalOverFroyoUtils.getDiskCacheDir(this,System.currentTimeMillis() + ".apk").getAbsolutePath();
				
				notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				
				//Bitmap large =BitMapUtility.drawableToBitmap(VersionUtils.getCurrentIcon(this));
				
				Intent resultIntent = new Intent();
				//resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				PendingIntent resultPendingIntent =PendingIntent.getActivity(this,0, resultIntent, 0);

				notification= new Notification();
				if(ftapp.getIcon()!=-1){
					notification.icon=ftapp.getIcon();
				}else{
					//notification.icon=R.drawable.firstdownload;
					notification.icon=TTResource.getIdByName(getApplicationContext(), "drawable", "firstdownload");
				}
				
				notification.flags=Notification.FLAG_ONGOING_EVENT;
				//notification.contentView = new RemoteViews(this.getPackageName(),R.layout.show_progress);
				notification.contentView = new RemoteViews(this.getPackageName(),TTResource.getIdByName(getApplicationContext(), "layout", "show_progress"));
				//notification.contentView.setImageViewResource(R.id.imageView1, notification.icon);
				notification.contentView.setImageViewResource(TTResource.getIdByName(getApplicationContext(), "id", "imageView1"), notification.icon);
				notification.contentIntent = resultPendingIntent;
				
				syncHttpClient.download(ftapp.getVersionInfo().getDownurl(),new MyFileHttpResponseHandler(DOWNLOAD_DIR));
			}

		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		L.i("VersionDownloadService", "onDestroy");
		super.onDestroy();
		
	}
}
