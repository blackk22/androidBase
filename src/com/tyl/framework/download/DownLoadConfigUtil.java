package com.tyl.framework.download;

import java.util.ArrayList;
import java.util.List;

import com.tyl.framework.TTApplication;
import com.tyl.framework.util.text.StringUtils;


/**
 * @Title DownLoadConfigUtil
 * @Package com.firstte.util.download
 * @Description 下载配置
 * @version V1.0
 */
public class DownLoadConfigUtil {
	/**
	 * 下载配置名称
	 */
	public static final String PREFERENCE_NAME = "com.firstte.download";
	/**
	 * url总数
	 */
	public static final int URL_COUNT = 3;
	/**
	 * 配置key的名称
	 */
	public static final String KEY_URL = "url";

	/**
	 * 存储url 通过索引
	 * @param index
	 * @param url
	 */
	public static void storeURL(int index, String url) {
		TTApplication.getApplication().getCurrentConfig().setString(KEY_URL + index, url);
	}

	/**
	 * 删除url 通过索引
	 * @param index
	 */
	public static void clearURL(int index) {
		TTApplication.getApplication().getCurrentConfig()
				.remove(KEY_URL + index);
	}

	/**
	 * 获得url，通过索引
	 */
	public static String getURL(int index) {
		return TTApplication.getApplication().getCurrentConfig()
				.getString(KEY_URL + index, "");
	}

	/**
	 * 获得所有url
	 * @return
	 */
	public static List<String> getURLArray() {
		List<String> urlList = new ArrayList<String>();
		for (int i = 0; i < URL_COUNT; i++) {
			if (!StringUtils.isEmpty(getURL(i))) {
				urlList.add(getString(KEY_URL + i));
			}
		}
		return urlList;
	}

	/**
	 * 通过key获得url
	 * @param key
	 * @return
	 */
	private static String getString(String key) {
		return TTApplication.getApplication().getCurrentConfig()
				.getString(key, "");
	}

}
