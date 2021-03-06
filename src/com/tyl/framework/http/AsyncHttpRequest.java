package com.tyl.framework.http;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.tyl.framework.log.L;


/**
 * @Title AsyncHttpRequest
 * @Package com.firstte
 * @Description AsyncHttpRequest是异步Http请求线程对象
 * @version V1.0
 */
public class AsyncHttpRequest implements Runnable {
	private static final String tag="AsyncHttpRequest";
	
	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest request;
	private final AsyncHttpResponseHandler responseHandler;
	private boolean isBinaryRequest;
	private int executionCount;

	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, AsyncHttpResponseHandler responseHandler) {
		this.client = client;
		this.context = context;
		this.request = request;
		this.responseHandler = responseHandler;
		initAsyncHttpRequest();
	}

	public AsyncHttpResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public HttpUriRequest getRequest() {
		return request;
	}

	/**
	 * 初始化异步请求
	 */
	protected void  initAsyncHttpRequest() {
		if (responseHandler instanceof BinaryHttpResponseHandler) {
			this.isBinaryRequest = true;
		} else if (responseHandler instanceof FileHttpResponseHandler) {
			
			FileHttpResponseHandler fileHttpResponseHandler = (FileHttpResponseHandler) responseHandler;
			
			File tempFile = fileHttpResponseHandler.getFile();

			if (tempFile.exists()) {
				long previousFileSize = tempFile.length();
				
				L.i("FileHttpResponseHandler","tempFile:"+previousFileSize);
				//设置之后下载多少
				fileHttpResponseHandler.setPreviousFileSize(previousFileSize);
				setRequestHeader("range", "bytes=" + previousFileSize+ "-");
				
				L.i("FileHttpResponseHandler","RANGE:"+request.getHeaders("RANGE")[0].getValue());
			}

		}
	}
	
	protected  void  setRequestHeader(String key,String value) {
		this.request.addHeader(key,value);
		
	}
	
	
	@Override
	public void run() {
		try {
			if (responseHandler != null) {
				responseHandler.sendStartMessage();
			}

			makeRequestWithRetries();
			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
			}

		} catch (IOException e) {
			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
				if (this.isBinaryRequest) {
					responseHandler.sendFailureMessage(e, (byte[]) null);
				} else {
					responseHandler.sendFailureMessage(e, (String) null);
				}
			}
		}
	}

	/**
	 * 执行http请求
	 * @throws IOException
	 */
	private void makeRequest() throws IOException {
		if (!Thread.currentThread().isInterrupted()) {
			try {
				//执行请求，获得响应对象
				L.i(tag, "执行请求，获得响应对象:"+request.getURI().toURL().toString());
				HttpResponse response = client.execute(request, context);
				if (!Thread.currentThread().isInterrupted()) {
					if (responseHandler != null) {
						responseHandler.sendResponseMessage(response);
					}
				} else {
					// TODO: should raise InterruptedException? this block is
					// reached whenever the request is cancelled before its
					// response is received
				}
			} catch (IOException e) {
				L.e(tag, e);
				if (!Thread.currentThread().isInterrupted()) {
					throw e;
				}
			}
		}
	}

	/**
	 * 尝试请求
	 * @throws ConnectException
	 *  This is an additional layer of retry logic lifted from droid-fu
	 * See:https://github.com/kaeppler/droid-fu/blob/master/src/main/java/com/github/droidfu/http/BetterHttpRequestBase.java
	 */
	private void makeRequestWithRetries() throws ConnectException {
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client
				.getHttpRequestRetryHandler();
		while (retry) {
			try {
				makeRequest();
				return;
			} catch (UnknownHostException e) {
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			} catch (SocketException e) {
				// Added to detect host unreachable
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "can't resolve host");
				}
				return;
			} catch (SocketTimeoutException e) {
				if (responseHandler != null) {
					responseHandler.sendFailureMessage(e, "socket time out");
				}
				return;
			} catch (IOException e) {
				
				cause = e;
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
			} catch (NullPointerException e) {
				// there's a bug in HttpClient 4.0.x that on some occasions
				// causes
				// DefaultRequestExecutor to throw an NPE, see
				// http://code.google.com/p/android/issues/detail?id=5255
				cause = new IOException("NPE in HttpClient" + e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
			}
		}

		// no retries left, crap out with exception
		ConnectException ex = new ConnectException();
		ex.initCause(cause);
		throw ex;
	}
}
