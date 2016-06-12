package com.tyl.framework.download;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import android.text.TextUtils;

import com.tyl.framework.TTApplication;
import com.tyl.framework.cache.CacheDirectory;
import com.tyl.framework.http.AsyncHttpClient;
import com.tyl.framework.http.AsyncHttpResponseHandler;
import com.tyl.framework.http.DirectFileHttpResponseHandler;
import com.tyl.framework.log.L;
import com.tyl.framework.util.text.StringUtils;

/**
 * @Title DownloadManager
 * @Package com.firstte.util.download
 * @Description 下载管理器线程  继承Thread类  多线程下载，但是不支持断点续传
 * @version V1.0
 */
public class DirectDownloadManager extends Thread {

	private static final String TAG="DirectDownloadManager";
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
	 * 异步操作httpclient  其中的线程池的的最大线程是5个
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
	public static final String FILE_ROOT;
	
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
	private static DirectDownloadManager downloadManager;
	
	static{
		FILE_ROOT=CacheDirectory.getDownLoadDir(TTApplication.getApplication());
		//System.out.println("file_root:"+FILE_ROOT);
	}

	public static DirectDownloadManager getDownloadManager() {
		if (downloadManager == null) {
			downloadManager = new DirectDownloadManager(FILE_ROOT);
		}
		return downloadManager;
	}

	public static DirectDownloadManager getDownloadManager(String rootPath) {

		if (downloadManager == null) {
			downloadManager = new DirectDownloadManager(rootPath);
		}
		return downloadManager;
	}

	private DirectDownloadManager() {
		this(FILE_ROOT);
	}

	private DirectDownloadManager(String rootPath) {
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
	@SuppressWarnings("deprecation")
	public void close() {

		isRunning = false;
		deleteHandler();
		if (mDownLoadCallback != null) {
			mDownLoadCallback.sendStopMessage();
		}
		this.stop();
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
			//出列操，
			L.d(TAG, "当前下载数量排队的："+mhandlerQueue.size()+",下载线程："+mDownloadinghandlers.size()+",最大值:"+MAX_DOWNLOAD_THREAD_COUNT);
			
			DirectFileHttpResponseHandler handler = (DirectFileHttpResponseHandler) mhandlerQueue.poll();
			if (handler != null) {
				mDownloadinghandlers.add(handler);
				handler.setInterrupt(false);
				syncHttpClient.download(handler.getUrl(), handler);
			}
		}
		L.e(TAG, "下载管理器退出");
	}

	/**
	 * 添加下载url 到队列尾部
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
			L.i(TAG, "添加下载："+url);
			addHandler(newAsyncHttpResponseHandler(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加下载url 到队列头部
	 * @param url
	 */
	public void addHandlerHeader(String url) {
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
			L.i(TAG, "添加下载："+url);
			addHandlerHeader(newAsyncHttpResponseHandler(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加异步请求响应操作者  队尾添加
	 * @param handler
	 */
	private void addHandler(AsyncHttpResponseHandler handler) {
		//广播下载
		broadcastAddHandler(((DirectFileHttpResponseHandler) handler).getUrl());
		mhandlerQueue.offer(handler);
		if (!this.isAlive()) {
			this.startManage();
		}
	}
	
	/**
	 * 添加异步请求响应操作者 队头添加
	 * @param handler
	 */
	private void addHandlerHeader(AsyncHttpResponseHandler handler) {
		//广播下载
		broadcastAddHandler(((DirectFileHttpResponseHandler) handler).getUrl());
		mhandlerQueue.offerHeader(handler);
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

		DirectFileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mDownloadinghandlers.get(i);
			broadcastAddHandler(handler.getUrl(), handler.isInterrupt());
		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mhandlerQueue.get(i);
			broadcastAddHandler(handler.getUrl());
		}
		for (int i = 0; i < mPausinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mPausinghandlers.get(i);
			broadcastAddHandler(handler.getUrl());
		}
	}

	/**
	 * 判断对应的url有没有操作对象
	 * @param url
	 * @return
	 */
	public boolean hasHandler(String url) {

		DirectFileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mDownloadinghandlers.get(i);
			if (handler.getUrl().equals(url)) {
				return true;
			}
		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mhandlerQueue.get(i);
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
	public DirectFileHttpResponseHandler getHandler(String url) {
		DirectFileHttpResponseHandler handler = null;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mDownloadinghandlers.get(i);

		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mhandlerQueue.get(i);
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
	 * 通过url 删除所有排队(暂停、等待)中下载操作，进行下载中不下载
	 * @param url
	 */
	public synchronized void deleteQueueHandler() {

		DirectFileHttpResponseHandler handler;		
		for (int i = mhandlerQueue.size()-1; i>=0 ; i--) {
			handler = (DirectFileHttpResponseHandler) mhandlerQueue.get(i);
				mhandlerQueue.remove(handler);
		}
		
		for (int i = mPausinghandlers.size()-1; i >=0 ; i--) {
			handler = (DirectFileHttpResponseHandler) mPausinghandlers.get(i);
				mPausinghandlers.remove(handler);
		}
		//System.out.println("排队中："+mhandlerQueue.size());
		//System.out.println("暂停中："+mPausinghandlers.size());
	}
	

	/**
	 * 通过url 删除所有下载操作
	 * @param url
	 */
	public synchronized void deleteHandler() {

		DirectFileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mDownloadinghandlers.get(i);
			if (handler != null) {
				File file = handler.getFile();
				if (file!=null&&file.exists())
					file.delete();
				File tempFile = handler.getTempFile();
				if (tempFile!=null&&tempFile.exists()) {
					tempFile.delete();
				}
				handler.setInterrupt(true);
				completehandler(handler);
			}
		}
		
		for (int i = mhandlerQueue.size()-1; i>=0 ; i--) {
			handler = (DirectFileHttpResponseHandler) mhandlerQueue.get(i);
				mhandlerQueue.remove(handler);
		}
		
		for (int i = mPausinghandlers.size()-1; i >=0 ; i--) {
			handler = (DirectFileHttpResponseHandler) mPausinghandlers.get(i);
				mPausinghandlers.remove(handler);
		}
	}
	
	/**
	 * 通过url 删除下载操作
	 * @param url
	 */
	public synchronized void deleteHandler(String url) {

		DirectFileHttpResponseHandler handler;
		for (int i = 0; i < mDownloadinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mDownloadinghandlers.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				File file = handler.getFile();
				if (file.exists())
					file.delete();
				File tempFile = handler.getTempFile();
				if (tempFile.exists()) {
					tempFile.delete();
				}
				handler.setInterrupt(true);
				completehandler(handler);
				return;
			}
		}
		for (int i = 0; i < mhandlerQueue.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mhandlerQueue.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				mhandlerQueue.remove(handler);
			}
		}
		for (int i = 0; i < mPausinghandlers.size(); i++) {
			handler = (DirectFileHttpResponseHandler) mPausinghandlers.get(i);
			if (handler != null && handler.getUrl().equals(url)) {
				mPausinghandlers.remove(handler);
			}
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
						.sendFinishMessage(((DirectFileHttpResponseHandler) handler).getUrl());
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
		
		DirectFileHttpResponseHandler handler = new DirectFileHttpResponseHandler(url,rootPath, StringUtils.getFileNameFromUrl(url)) {

			@Override
			public void onProgress(long totalSize, long currentSize, long speed) {
				super.onProgress(totalSize, currentSize, speed);
				if(mDownLoadCallback != null) {
					mDownLoadCallback.sendLoadMessage(this.getUrl(), totalSize,
							currentSize, speed);
				}
			}

			@Override
			public void onSuccess(String content) {
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
				L.d(TAG, "下载失败："+getUrl());
				if (error != null) {

					if (mDownLoadCallback != null) {
						mDownLoadCallback.sendFailureMessage(this.getUrl(),error.getMessage());
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
		private Deque<AsyncHttpResponseHandler> handlerQueue;

		public handlerQueue() {
			handlerQueue = new LinkedList<AsyncHttpResponseHandler>();
		}

		/**
		 * 进队列尾部
		 * @param handler
		 */
		public void offer(AsyncHttpResponseHandler handler) {

			handlerQueue.offer(handler);
		}
		
		
		/**
		 * 进队列头部
		 * @param handler
		 */
		public void offerHeader(AsyncHttpResponseHandler handler) {

			handlerQueue.offerFirst(handler);
		}
		
		
		/**
		 * 出队列
		 * @return
		 */
		public AsyncHttpResponseHandler poll() {

			AsyncHttpResponseHandler handler = null;
			
			
			//当前下载数量大于约束值  、队列中没有排队的
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
			return ((LinkedList<AsyncHttpResponseHandler>) handlerQueue).get(position);
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
