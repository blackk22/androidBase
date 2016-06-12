package com.tyl.framework.download;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.text.TextUtils;

import com.tyl.framework.http.AsyncHttpClient;
import com.tyl.framework.http.AsyncHttpResponseHandler;
import com.tyl.framework.http.FileHttpResponseHandler;
import com.tyl.framework.util.text.StringUtils;

/**
 * @Title DownloadManager
 * @Package com.firstte.util.download
 * @Description 下载管理器线程  继承Thread类
 * @version V1.0
 */
public class DownloadManager extends Thread {

	/**
	 * 最大操作数
	 */
	private static final int MAX_handler_COUNT = 100;
	/**
	 * 最大下载线程数
	 */
	private static final int MAX_DOWNLOAD_THREAD_COUNT = 3;
	/**
	 * 响应操作对象队列
	 */
	private handlerQueue mhandlerQueue;
	/**
	 * 下载状态的响应操作对象集合
	 */
	private List<AsyncHttpResponseHandler> mDownloadinghandlers;
	/**
	 * 暂停状态下载的响应操作对象集合
	 */
	private List<AsyncHttpResponseHandler> mPausinghandlers;
	
	/**
	 * 异步操作httpclient
	 */
	private AsyncHttpClient syncHttpClient;
	/**
	 * 是否在运行
	 */
	private Boolean isRunning = false;
	/**
	 * 存储目录
	 */
	//private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static  String FILE_ROOT;
	
	/**
	 * 下载回调
	 */
	private DownLoadCallback mDownLoadCallback;
	/**
	 * 
	 *根路径
	 */
	private String rootPath = "";
	/**
	 * 下载管理器线程
	 */
	private static DownloadManager downloadManager;

	public static DownloadManager getDownloadManager(String rootPath) {

		if (downloadManager == null) {
			downloadManager = new DownloadManager(rootPath);
		}
		return downloadManager;
	}

	private DownloadManager() {
		this(FILE_ROOT);
	}

	private DownloadManager(String rootPath) {
		this.rootPath = rootPath;
		mhandlerQueue = new handlerQueue();
		mDownloadinghandlers = new ArrayList<AsyncHttpResponseHandler>();
		mPausinghandlers = new ArrayList<AsyncHttpResponseHandler>();
		syncHttpClient = new AsyncHttpClient();
		if (!StringUtils.isEmpty(rootPath)) {
			File rootFile = new File(rootPath);
			if (!rootFile.exists()) {
				rootFile.mkdir();
			}
		}
	}

	/**
	 * 返回下载存储根路径，如果默认为空，就默认sdcard目录下面的firstte目录
	 * @return
	 */
	public String getRootPath() {
		if (StringUtils.isEmpty(rootPath)) {
			rootPath = FILE_ROOT;
		}
		return rootPath;
	}

	/**
	 * 设置回调
	 * @param downLoadCallback
	 */
	public void setDownLoadCallback(DownLoadCallback downLoadCallback) {
		this.mDownLoadCallback = downLoadCallback;
	}

	/**
	 * 开始管理
	 */
	public void startManage() {

		isRunning = true;
		this.start();
		if (mDownLoadCallback != null) {
			mDownLoadCallback.sendStartMessage();
		}
		// checkUncompletehandlers();
	}

	/**
	 * 关闭下载
	 */
	public void close() {

		isRunning = false;
		pauseAllHandler();
		if (mDownLoadCallback != null) {
			mDownLoadCallback.sendStopMessage();
		}
		//this.stop();
	}

	/**
	 * 返回是否在运行
	 * @return
	 */
	public boolean isRunning() {

		return isRunning;
	}

	@Override
	public void run() {

		super.run();
		while (isRunning) {
			FileHttpResponseHandler handler = (FileHttpResponseHandler) mhandlerQueue.poll();
			if (handler != null) {
				mDownloadinghandlers.add(handler);
				handler.setInterrupt(false);
				syncHttpClient.download(handler.getUrl(), handler);
			}
		}
	}

	/**
	 * 添加下载url
	 * @param url
	 */
	public void addHandler(String url) {
		if (getTotalhandlerCount() >= MAX_handler_COUNT) {
			if (mDownLoadCallback != null) {
				mDownLoadCallback.sendFailureMessage(url, "任务列表已满");
			}
			return;
		}
		
		if (TextUtils.isEmpty(url) || hasHandler(url)) {
			// 任务中存在这个任务,或者任务不满足要求
			return;
		}
		
		try {
			addHandler(newAsyncHttpResponseHandler(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加异步请求响应操作者
	 * @param handler
	 */
	private void addHandler(AsyncHttpResponseHandler handler) {
		broadcastAddHandler(((FileHttpResponseHandler) handler).getUrl());
		mhandlerQueue.offer(handler);

		if (!this.isAlive()) {
			this.startManage();
		}
	}

	/**
	 * 广播回调下载
	 * @param url
	 */
	private void broadcastAddHandler(String url)

	{
		broadcastAddHandler(url, false);
	}

	/**
	 * 广播回调下载
	 * @param url
	 * @param isInterrupt
	 */
	private void broadcastAddHandler(String url, boolean isInterrupt) {

		if (mDownLoadCallback != null) {
			mDownLoadCallback.sendAddMessage(url, false);
		}

	}

	/**
	 * 所有的广播回调下载
	 */
	public void reBroadcastAddAllhandler() {

		FileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mDownloadinghandlers.get(i);
			broadcastAddHandler(handler.getUrl(), handler.isInterrupt());
		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (FileHttpResponseHandler) mhandlerQueue.get(i);
			broadcastAddHandler(handler.getUrl());
		}
		for (int i = 0; i < mPausinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mPausinghandlers.get(i);
			broadcastAddHandler(handler.getUrl());
		}
	}

	/**
	 * 判断对应的url有没有操作对象
	 * @param url
	 * @return
	 */
	public boolean hasHandler(String url) {

		FileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mDownloadinghandlers.get(i);
			if (handler.getUrl().equals(url)) {
				return true;
			}
		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (FileHttpResponseHandler) mhandlerQueue.get(i);
			if (handler.getUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 通过url获得FileHttpResponseHandler
	 * @param url
	 * @return
	 */
	public FileHttpResponseHandler getHandler(String url) {
		FileHttpResponseHandler handler = null;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mDownloadinghandlers.get(i);

		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (FileHttpResponseHandler) mhandlerQueue.get(i);
		}
		return handler;
	}

	/**
	 * 通过位置获得 AsyncHttpResponseHandler
	 * 
	 * @param position
	 * @return
	 */
	public AsyncHttpResponseHandler gethandler(int position) {

		if (position >= mDownloadinghandlers.size()) {
			return mhandlerQueue.get(position - mDownloadinghandlers.size());
		} else {
			return mDownloadinghandlers.get(position);
		}
	}

	/**
	 * 获得队列个数
	 * @return
	 */
	public int getQueuehandlerCount() {

		return mhandlerQueue.size();
	}

	/**
	 * 获得下载队列个数
	 * @return
	 */
	public int getDownloadinghandlerCount() {

		return mDownloadinghandlers.size();
	}

	/**
	 * 获得暂停队列个数
	 * @return
	 */
	public int getPausinghandlerCount() {

		return mPausinghandlers.size();
	}

	/**
	 * 获得总队列个数
	 * @return
	 */
	public int getTotalhandlerCount() {

		return getQueuehandlerCount() + getDownloadinghandlerCount()
				+ getPausinghandlerCount();
	}

	/**
	 * 检查未完成的操作
	 */
	public void checkUncompletehandlers() {

		List<String> urlList = DownLoadConfigUtil.getURLArray();
		if (urlList.size() >= 0) {
			for (int i = 0; i < urlList.size(); i++) {
				addHandler(urlList.get(i));
			}
		}
	}

	/**
	 * 暂停某个url下载
	 * @param url
	 */
	public synchronized void pauseHandler(String url) {

		FileHttpResponseHandler handler;
		
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (FileHttpResponseHandler)mhandlerQueue.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				mhandlerQueue.remove(handler);
				mPausinghandlers.add(handler);
				if (mDownLoadCallback != null) {
					mDownLoadCallback.sendPauseMessage(url);
				}
			}
			
		}
		
		
		
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mDownloadinghandlers.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				pausehandler(handler);
			}
		}
		
	}

	/**
	 * 暂停所有的操作
	 */
	public synchronized void pauseAllHandler() {

		AsyncHttpResponseHandler handler;

		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = mhandlerQueue.get(i);
			mhandlerQueue.remove(handler);
			mPausinghandlers.add(handler);
		}

		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = mDownloadinghandlers.get(i);
			if (handler != null) {
				pausehandler(handler);
			}
		}
	}

	/**
	 * 通过url 删除下载操作
	 * @param url
	 */
	public synchronized void deleteHandler(String url) {

		FileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mDownloadinghandlers.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				File file = handler.getFile();
				if (file.exists()){
					file.delete();
				}
				
				handler.setInterrupt(true);
				completehandler(handler);
				return;
			}
		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (FileHttpResponseHandler) mhandlerQueue.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				mhandlerQueue.remove(handler);
			}
		}
		for (int i = 0; i < mPausinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mPausinghandlers.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				mPausinghandlers.remove(handler);
			}
		}
	}

	/**
	 * 继续下载
	 * @param url
	 */
	public synchronized void continueHandler(String url) {

		FileHttpResponseHandler handler;
		for (int i = 0; i < mPausinghandlers.size(); i++) {
			handler = (FileHttpResponseHandler) mPausinghandlers.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				continuehandler(handler);
			}

		}
	}

	/**
	 * 暂停下载
	 * @param handler
	 */
	public synchronized void pausehandler(AsyncHttpResponseHandler handler) {

		FileHttpResponseHandler fileHttpResponseHandler = (FileHttpResponseHandler) handler;
		if (handler != null) {
			fileHttpResponseHandler.setInterrupt(true);
			// move to pausing list
			String url = fileHttpResponseHandler.getUrl();
			try {
				mDownloadinghandlers.remove(handler);
				handler = newAsyncHttpResponseHandler(url);
				mPausinghandlers.add(handler);
				if (mDownLoadCallback != null) {
					mDownLoadCallback.sendPauseMessage(url);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 继续下载
	 * @param handler
	 */
	public synchronized void continuehandler(AsyncHttpResponseHandler handler) {

		if (handler != null) {
			mPausinghandlers.remove(handler);
			mhandlerQueue.offer(handler);
		}
	}

	/**
	 * 完成下载
	 * @param handler
	 */
	public synchronized void completehandler(AsyncHttpResponseHandler handler) {

		if (mDownloadinghandlers.contains(handler)) {
			DownLoadConfigUtil.clearURL(mDownloadinghandlers.indexOf(handler));
			mDownloadinghandlers.remove(handler);

			if (mDownLoadCallback != null) {
				mDownLoadCallback
						.sendFinishMessage(((FileHttpResponseHandler) handler)
								.getUrl());
			}
		}
	}

	/**
	 * 新的下载操作
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	private AsyncHttpResponseHandler newAsyncHttpResponseHandler(String url)
			throws MalformedURLException {
		
		FileHttpResponseHandler handler = new FileHttpResponseHandler(url,
				rootPath, StringUtils.getFileNameFromUrl(url)) {

			@Override
			public void onProgress(long totalSize, long currentSize, long speed) {
				super.onProgress(totalSize, currentSize, speed);
				if (mDownLoadCallback != null) {
					mDownLoadCallback.sendLoadMessage(this.getUrl(), totalSize,
							currentSize, speed);
				}
			}

			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				if (mDownLoadCallback != null) {
					mDownLoadCallback.sendSuccessMessage(this.getUrl());
				}
			}

			public void onFinish() {
				completehandler(this);
			}

			public void onStart() {
				DownLoadConfigUtil.storeURL(mDownloadinghandlers.indexOf(this),
						getUrl());
			}

			@Override
			public void onFailure(Throwable error) {
				//Logger.d(DownloadManager.this, "Throwable");
				if (error != null) {

					if (mDownLoadCallback != null) {
						mDownLoadCallback.sendFailureMessage(this.getUrl(),
								error.getMessage());
					}
				}
				// completehandler(this);
			}
			
		};

		return handler;
	}

	/**
	 * AsyncHttpResponseHandler 队列
	 * 
	 *
	 */
	private class handlerQueue {
		//异步响应对象的队列
		private Queue<AsyncHttpResponseHandler> handlerQueue;

		public handlerQueue() {
			handlerQueue = new LinkedList<AsyncHttpResponseHandler>();
		}

		/**
		 * 进队列
		 * @param handler
		 */
		public void offer(AsyncHttpResponseHandler handler) {

			handlerQueue.offer(handler);
		}

		/**
		 * 出队列
		 * @return
		 */
		public AsyncHttpResponseHandler poll() {

			AsyncHttpResponseHandler handler = null;
			while (mDownloadinghandlers.size() >= MAX_DOWNLOAD_THREAD_COUNT|| (handler = handlerQueue.poll()) == null) {
				try {
					Thread.sleep(1000); // sleep
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return handler;
		}

		/**
		 * 通过位置获得异步http响应对象
		 * @param position
		 * @return
		 */
		public AsyncHttpResponseHandler get(int position) {

			if (position >= size()) {
				return null;
			}
			return ((LinkedList<AsyncHttpResponseHandler>) handlerQueue)
					.get(position);
		}

		/**
		 * 发挥队列个数
		 * @return
		 */
		public int size() {

			return handlerQueue.size();
		}

		/**
		 * 通过位置移除
		 * @param position
		 * @return
		 */
		@SuppressWarnings("unused")
		public boolean remove(int position) {

			return handlerQueue.remove(get(position));
		}


		/**
		 * 通过异步响应操作对象移除
		 * @param position
		 * @return
		 */
		public boolean remove(AsyncHttpResponseHandler handler) {

			return handlerQueue.remove(handler);
		}
	}

}
