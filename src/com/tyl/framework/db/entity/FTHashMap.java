package com.tyl.framework.db.entity;

import java.util.Date;
import java.util.HashMap;

/**
 * @Title FTHashMap NameValuePair数组
 * @Package com.firstte.util.db.entity
 * @Description FTHashMap 名称值对Map集合操作类
 * @version V1.0
 */
public class FTHashMap<T extends Object> extends HashMap<String, T> {
	private static final long serialVersionUID = 1L;

	/**
	 * 添加key、value
	 */
	public T put(String key, T value) {
		if (hasValue(value)) {
			return super.put(key, value);
		} else {
			return null;
		}
	};

	/*
	 * 判断是否有值
	 */
	public boolean hasValue(Object value) {
		if (value != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过key获得值
	 */
	@Override
	public T get(Object key) {
		return super.get(key);
	}

	/**
	 * 通过key获得字符串形式值
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return String.valueOf(get(key));
	}

	/**
	 * 通过key获得int形式值
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return Integer.valueOf(getString(key));
	}

	/**
	 * 通过key获得boolean形式值
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		return Boolean.valueOf(getString(key));
	}

	/**
	 * 通过key获得double形式值
	 * @param key
	 * @return
	 */
	public double getDouble(String key) {
		return Double.valueOf(getString(key));
	}

	/**
	 * 通过key获得float形式值
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return Float.valueOf(getString(key));
	}

	/**
	 * 通过key获得long形式值
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return Long.valueOf(getString(key));
	}

	/**
	 * 获取时间
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getDate(String key) {
		return new Date(getString(key));
	}

	/**
	 * 通过key获得char形式值
	 * @param key
	 * @return
	 */
	public char getChar(String key) {
		return getString(key).trim().toCharArray()[0];
	}

	/**
	 * 通过key获得blod形式值
	 * @param key
	 * @return
	 */
	public byte[] getBlob(String key) {
		return getString(key).getBytes();
	}

	/**
	 * 通过key获得short形式值
	 * @param key
	 * @return
	 */
	public short getShort(String key) {
		return Short.valueOf(getString(key));
	}

}
