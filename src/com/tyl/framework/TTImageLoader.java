package com.tyl.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.tyl.framework.R;
import com.tyl.framework.bitmap.cache.disc.DiskCache;
import com.tyl.framework.bitmap.cache.disc.naming.Md5FileNameGenerator;
import com.tyl.framework.bitmap.cache.memory.MemoryCache;
import com.tyl.framework.bitmap.core.DisplayImageOptions;
import com.tyl.framework.bitmap.core.ImageLoader;
import com.tyl.framework.bitmap.core.ImageLoaderConfiguration;
import com.tyl.framework.bitmap.core.assist.ImageSize;
import com.tyl.framework.bitmap.core.assist.QueueProcessingType;
import com.tyl.framework.bitmap.core.imageaware.ImageAware;
import com.tyl.framework.bitmap.core.listener.ImageLoadingListener;
import com.tyl.framework.bitmap.core.listener.ImageLoadingProgressListener;

public class TTImageLoader {
	private  static ImageLoader instance;
	
	static{
		instance=ImageLoader.getInstance();
	}
	
	/**
	 * 初始化图像加载默认配置
	 */
	public static void init(Context context){
		
		//图像显示选项
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		//图像缓冲配置
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.defaultDisplayImageOptions(options)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(50 * 1024 * 1024) // 50 Mb
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.build();
		
		//初始化配置
		instance.init(config); //初始化
	}
	
	/**
	 * 初始化图像加载配置
	 * @param configuration
	 */
	public static void init(ImageLoaderConfiguration configuration) {
		instance.init(configuration);
	}
	
	/**
	 * 判断是否初始化配置
	 * @return
	 */
	public static boolean isInited() {
		return instance.isInited();
	}
	
	/**
	 * 显示图像
	 * @param uri  图像地址
	 * @param imageAware 
	 */
	public static void displayImage(String uri, ImageAware imageAware) {
		instance.displayImage(uri, imageAware);
	}
	
	/**
	 * 显示图像
	 * @param uri
	 * @param imageAware
	 * @param listener
	 */
	public static void displayImage(String uri, ImageAware imageAware, ImageLoadingListener listener) {
		instance.displayImage(uri, imageAware,listener);
	}
	
	/**
	 * 显示图像
	 * @param uri
	 * @param imageAware
	 * @param options
	 */
	public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options) {
		instance.displayImage(uri, imageAware,options);
	}
	
	/**
	 * 显示图像
	 * @param uri
	 * @param imageAware
	 * @param options
	 * @param listener
	 */
	public  static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
			ImageLoadingListener listener) {
		instance.displayImage(uri, imageAware,options,listener);
	}
	
	/**
	 * 显示图像
	 * @param uri
	 * @param imageAware
	 * @param options
	 * @param listener
	 * @param progressListener
	 */
	public static void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		instance.displayImage(uri, imageAware,options,listener,progressListener);
	}
	
	/**
	 * 显示图像
	 * @param uri
	 * @param imageView
	 */
	public static void displayImage(String uri, ImageView imageView) {
		instance.displayImage(uri, imageView);
	}
	
	/**
	 * 显示图像
	 * @param uri
	 * @param imageView
	 * @param options
	 */
	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		instance.displayImage(uri, imageView, options);
	}

	/**
	 * 显示图像
	 * @param uri
	 * @param imageView
	 * @param listener
	 */
	public static void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
		instance.displayImage(uri, imageView, listener);
	}

	/**
	 * 显示图像
	 * @param uri
	 * @param imageView
	 * @param options
	 * @param listener
	 */
	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener) {
		instance.displayImage(uri, imageView, options, listener);
	}

	/**
	 * 显示图像
	 * @param uri
	 * @param imageView
	 * @param options
	 * @param listener
	 * @param progressListener
	 */
	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		instance.displayImage(uri, imageView, options, listener, progressListener);
	}

	/**
	 * 加载图像
	 * @param uri
	 * @param listener
	 */
	public static void loadImage(String uri, ImageLoadingListener listener) {
		instance.loadImage(uri, listener);
	}
	
	/**
	 * 加载图像
	 * @param uri
	 * @param targetImageSize
	 * @param listener
	 */
	public static void loadImage(String uri, ImageSize targetImageSize, ImageLoadingListener listener) {
		instance.loadImage(uri, targetImageSize, listener);
	}

	/**
	 * 加载图像
	 * @param uri
	 * @param options
	 * @param listener
	 */
	public static void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
		instance.loadImage(uri, options, listener);
	}

	/**
	 * 加载图像
	 * @param uri
	 * @param targetImageSize
	 * @param options
	 * @param listener
	 */
	public static void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
			ImageLoadingListener listener) {
		instance.loadImage(uri, targetImageSize, options, listener);
	}

	/**
	 * 加载图像
	 * @param uri
	 * @param targetImageSize
	 * @param options
	 * @param listener
	 * @param progressListener
	 */
	public static void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		instance.loadImage(uri,targetImageSize,options,listener,progressListener);
	}

	/**
	 * 同步加载图像
	 * @param uri
	 * @return
	 */
	public static Bitmap loadImageSync(String uri) {
		return instance.loadImageSync(uri);
	}

	/**
	 * 同步加载图像
	 * @param uri
	 * @param options
	 * @return
	 */
	public static Bitmap loadImageSync(String uri, DisplayImageOptions options) {
		return instance.loadImageSync(uri, options);
	}
	
	/**
	 * 同步加载图像
	 * @param uri
	 * @param targetImageSize
	 * @return
	 */
	public static Bitmap loadImageSync(String uri, ImageSize targetImageSize) {
		return instance.loadImageSync(uri, targetImageSize);
	}

	/**
	 * 同步加载图像
	 * @param uri
	 * @param targetImageSize
	 * @param options
	 * @return
	 */
	public static Bitmap loadImageSync(String uri, ImageSize targetImageSize, DisplayImageOptions options) {
		return instance.loadImageSync(uri,targetImageSize,options);
	}

	/**
	 * 获得内存缓冲
	 * @return
	 */
	public static MemoryCache getMemoryCache() {
		return instance.getMemoryCache();
	}

	/**
	 * 清除内存缓冲
	 */
	public static void clearMemoryCache() {
		instance.clearMemoryCache();
	}


	/**
	 * 获得磁盘缓冲
	 * @return
	 */
	public static DiskCache getDiskCache() {
		return instance.getDiskCache();
	}

	/**
	 * 清除磁盘缓冲
	 */
	public static void clearDiskCache() {
		instance.clearDiskCache();
	}

	/**
	 * 通过ImageAware获得加载的图像地址
	 * @param imageAware
	 * @return
	 */
	public static String getLoadingUriForView(ImageAware imageAware) {
		return instance.getLoadingUriForView(imageAware);
	}

	/**
	 * 通过ImageView获得加载的图像地址
	 * @param imageView
	 * @return
	 */
	public static String getLoadingUriForView(ImageView imageView) {
		return instance.getLoadingUriForView(imageView);
	}

	/**
	 * 取消图像显示任务
	 * @param imageAware
	 */
	public static void cancelDisplayTask(ImageAware imageAware) {
		instance.cancelDisplayTask(imageAware);
	}

	/**
	 * 取消图像显示任务
	 * @param imageView
	 */
	public static void cancelDisplayTask(ImageView imageView) {
		instance.cancelDisplayTask(imageView);
	}

	/**
	 * 是否取消网络工作
	 * @param denyNetworkDownloads
	 */
	public static void denyNetworkDownloads(boolean denyNetworkDownloads) {
		instance.denyNetworkDownloads(denyNetworkDownloads);
	}

	/**
	 * 减慢网络
	 * @param handleSlowNetwork
	 */
	public  static void handleSlowNetwork(boolean handleSlowNetwork) {
		instance.handleSlowNetwork(handleSlowNetwork);
	}


	/**
	 * 暂停
	 */
	public static void pause() {
		instance.pause();
	}

	/**
	 * 重启
	 */
	public static void resume() {
		instance.resume();
	}

	/**
	 * 停止
	 */
	public static void stop() {
		instance.stop();
	}

	/**
	 * 销毁
	 */
	public static void destroy() {
		instance.destroy();
	}
}
