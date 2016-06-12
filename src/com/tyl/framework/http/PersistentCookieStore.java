package com.tyl.framework.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @Title PersistentCookieStore
 * @Package com.firstte.util.http
 * @Description PersistentCookieStore是对会话cookie是否存储，对CookieStore的实现
 * @version V1.0
 */
public class PersistentCookieStore implements CookieStore {
	/**
	 * cookie配置名称
	 */
	private static final String COOKIE_PREFS = "CookiePrefsFile";
	/**
	 * 存储的名字
	 */
	private static final String COOKIE_NAME_STORE = "names";
	/**
	 * cookie的前缀
	 */
	private static final String COOKIE_NAME_PREFIX = "cookie_";

	/**
	 * cookie的map集合
	 */
	private final ConcurrentHashMap<String, Cookie> cookies;
	
	/**
	 * 配置对象
	 */
	private final SharedPreferences cookiePrefs;

	/**
	 * Construct a persistent cookie store.
	 */
	public PersistentCookieStore(Context context) {
		cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
		cookies = new ConcurrentHashMap<String, Cookie>();

		// Load any previously stored cookies into the store
		String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE,null);
		if (storedCookieNames != null) {
			String[] cookieNames = TextUtils.split(storedCookieNames, ",");
			for (String name : cookieNames) {
				String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX
						+ name, null);
				if (encodedCookie != null) {
					Cookie decodedCookie = decodeCookie(encodedCookie);
					if (decodedCookie != null) {
						cookies.put(name, decodedCookie);
					}
				}
			}

			// Clear out expired cookies
			clearExpired(new Date());
		}
	}

	/**
	 * 添加cookie
	 */
	@Override
	public void addCookie(Cookie cookie) {
		String name = cookie.getName() + cookie.getDomain();

		// Save cookie into local store, or remove if expired
		if (!cookie.isExpired(new Date())) {
			cookies.put(name, cookie);
		} else {
			cookies.remove(name);
		}

		// Save cookie into persistent store
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		prefsWriter.putString(COOKIE_NAME_STORE,
				TextUtils.join(",", cookies.keySet()));
		prefsWriter.putString(COOKIE_NAME_PREFIX + name,
				encodeCookie(new SerializableCookie(cookie)));
		prefsWriter.commit();
	}

	/**
	 * 请求所有cookie
	 */
	@Override
	public void clear() {
		// Clear cookies from local store
		cookies.clear();

		// Clear cookies from persistent store
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
		for (String name : cookies.keySet()) {
			prefsWriter.remove(COOKIE_NAME_PREFIX + name);
		}
		prefsWriter.remove(COOKIE_NAME_STORE);
		prefsWriter.commit();
	}

	/**
	 * 清除指定时间的cookie
	 */
	@Override
	public boolean clearExpired(Date date) {
		boolean clearedAny = false;
		SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

		for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet()) {
			String name = entry.getKey();
			Cookie cookie = entry.getValue();
			if (cookie.isExpired(date)) {
				// Clear cookies from local store
				cookies.remove(name);

				// Clear cookies from persistent store
				prefsWriter.remove(COOKIE_NAME_PREFIX + name);

				// We've cleared at least one
				clearedAny = true;
			}
		}

		// Update names in persistent store
		if (clearedAny) {
			prefsWriter.putString(COOKIE_NAME_STORE,
					TextUtils.join(",", cookies.keySet()));
		}
		prefsWriter.commit();

		return clearedAny;
	}

	/**
	 * 获得所有的cookie
	 */
	@Override
	public List<Cookie> getCookies() {
		return new ArrayList<Cookie>(cookies.values());
	}

	/**
	 * 编码cookie
	 * @param cookie
	 * @return
	 */
	protected String encodeCookie(SerializableCookie cookie) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(os);
			outputStream.writeObject(cookie);
		} catch (Exception e) {
			return null;
		}

		return byteArrayToHexString(os.toByteArray());
	}

	/**
	 * 解码cookie
	 * @param cookieStr
	 * @return
	 */
	protected Cookie decodeCookie(String cookieStr) {
		byte[] bytes = hexStringToByteArray(cookieStr);
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		Cookie cookie = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			cookie = ((SerializableCookie) ois.readObject()).getCookie();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cookie;
	}

	

	/**
	 * 字节数组转换为16进制字符串
	 * @param b
	 * @return
	 */
	protected String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (byte element : b) {
			int v = element & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 16进制字符串转换为字节
	 * @param s
	 * @return
	 */
	protected byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}