package com.tyl.framework.db.entity;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tyl.framework.util.text.StringUtils;

/**
 * @Title TAArrayList NameValuePair数组
 * @Package com.firstte.util.db.entity
 * @Description FTArrayList 名称值对数组集合操作类
 * @version V1.0
 */
public class FTArrayList extends ArrayList<NameValuePair> {

	private static final long serialVersionUID = 1L;

	/**
	 * 添加一个名称值对象
	 */
	@Override
	public boolean add(NameValuePair nameValuePair) {
		if (!StringUtils.isEmpty(nameValuePair.getValue())) {
			return super.add(nameValuePair);
		} else {
			return false;
		}
	}

	/**
	 * 添加数据key与value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, String value) {
		return add(new BasicNameValuePair(key, value));
	}

}
