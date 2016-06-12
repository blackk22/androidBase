package com.tyl.framework;

import java.io.File;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;

import com.tyl.framework.http.AsyncHttpClient;
import com.tyl.framework.http.AsyncHttpResponseHandler;
import com.tyl.framework.http.RequestParams;

public class TTAsyncHttpClient  {

	/**异步HttpClient*/
	private static AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
	
	/**
	 * 构建gson对象
	 */
	private static void buildAsyncHttpClient(){
		if(asyncHttpClient==null){
			asyncHttpClient=new AsyncHttpClient();
		}
	}
	
	/**
	 * 获得异步http请求客户端对象
	 * @return
	 */
	public static AsyncHttpClient getAsyncHttpClient(){
		buildAsyncHttpClient();
		return asyncHttpClient;
	}
	
	
	
	public static void setCookieStore(CookieStore cookieStore) {
		buildAsyncHttpClient();
		asyncHttpClient.setCookieStore(cookieStore);
	}
	
	
	
	public static void setUserAgent(String userAgent) {
		buildAsyncHttpClient();
		asyncHttpClient.setUserAgent(userAgent);
	}
	
	
	public static void setTimeout(int timeout) {
		buildAsyncHttpClient();
		asyncHttpClient.setTimeout(timeout);
	}
	
	
	public static void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		buildAsyncHttpClient();
		asyncHttpClient.setSSLSocketFactory(sslSocketFactory);
	}
	
	
	public static void addHeader(String header, String value) {
		buildAsyncHttpClient();
		asyncHttpClient.addHeader(header, value);
	}
	
	
	public static void setBasicAuth(String user, String pass) {
		buildAsyncHttpClient();
		asyncHttpClient.setBasicAuth(user, pass);
	}
	
	
	public static void setBasicAuth(String user, String pass, AuthScope scope) {
		buildAsyncHttpClient();
		asyncHttpClient.setBasicAuth(user, pass, scope);
	}
	
	
	public static void cancelRequests(Context context, boolean mayInterruptIfRunning) {
		buildAsyncHttpClient();
		asyncHttpClient.cancelRequests(context, mayInterruptIfRunning);
	}
	
	
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.get(url, responseHandler);
	}
	
	
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.get(url, params, responseHandler);
	}
	
	
	public static void get(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.get(context, url, responseHandler);
	}
	
	
	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.get(context, url, params, responseHandler);
	}
	
	
	public static void download(String url, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.download(url, responseHandler);
	}
	
	
	public static void download(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.download(url, params, responseHandler);
	}
	
	
	public static void download(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.download(context, url, responseHandler);
	}
	
	
	public static void download(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.download(context, url, params, responseHandler);
	}
	
	
	public static void get(Context context, String url, Header[] headers,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.get(context, url, headers, params, responseHandler);
	}
	
	
	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.post(url, responseHandler);
	}
	
	
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.post(url, params, responseHandler);
	}
	
	
	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.post(context, url, params, responseHandler);
	}
	
	
	public static void post(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.post(context, url, entity, contentType, responseHandler);
	}
	
	
	public static void post(Context context, String url, Header[] headers,
			RequestParams params, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.post(context, url, headers, params, contentType, responseHandler);
	}
	

	
	public static void post(Context context, String url, Header[] headers,
			HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.post(context, url, headers, entity, contentType, responseHandler);
	}
	
	
	public static void sendBody(Context context, String url, String content,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.sendBody(context, url, content, responseHandler);
	}
	
	
	public static void upload(Context context, String url, String filename, File file,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.upload(context, url, filename, file, responseHandler);
	}
	
	
	public static void upload(Context context, String url, String filename,
			List<File> files, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.upload(context, url, filename, files, responseHandler);
	}
	
	public static void sendBody(String url, String content,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.sendBody(url, content, responseHandler);
	}
	
	
	public static void upload(String url, String filename, File file,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.upload(url, filename, file, responseHandler);
	}
	
	
	public static void upload(String url, String filename,
			List<File> files, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.upload(url, filename, files, responseHandler);
	}
	
	public static void put(String url, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.put(url, responseHandler);
	}
	
	
	public static void put(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.put(url, params, responseHandler);
	}
	
	
	public static void put(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.put(context, url, params, responseHandler);
	}
	
	
	public static void put(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.put(context, url, entity, contentType, responseHandler);
	}
	
	
	public static void put(Context context, String url, Header[] headers,
			HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.put(context, url, headers, entity, contentType, responseHandler);
	}
	
	
	public static void delete(String url, AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.delete(url, responseHandler);
	}
	
	
	public static void delete(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.delete(context, url, responseHandler);
	}
	
	
	
	public static void delete(Context context, String url, Header[] headers,
			AsyncHttpResponseHandler responseHandler) {
		buildAsyncHttpClient();
		asyncHttpClient.delete(context, url, headers, responseHandler);
	}

	
	
	
}
