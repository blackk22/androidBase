package com.tyl.framework.http;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import android.os.Message;

/**
 * @Title BinaryHttpResponseHandler  用于图像下载
 * @Package com.firstte.util.http
 * @Description BinaryHttpResponseHandler是AsyncHttpClient的请求响应，响应的内容为字节数组 对AsyncHttpResponseHandler的扩充
 * @version V1.0
 */
public class BinaryHttpResponseHandler extends AsyncHttpResponseHandler {
	/**
	 * 默认允许图像
	 */
	private static String[] mAllowedContentTypes = new String[] { "image/jpeg","image/png" };

	/**
	 * 创建一个新的BinaryHttpResponseHandler
	 */
	public BinaryHttpResponseHandler() {
		super();
	}

	/**
	 * Creates a new BinaryHttpResponseHandler, and overrides the default
	 * allowed content types with passed String array (hopefully) of content
	 * types.
	 */
	public BinaryHttpResponseHandler(String[] allowedContentTypes) {
		this();
		mAllowedContentTypes = allowedContentTypes;
	}


	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param binaryData
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(byte[] binaryData) {
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param statusCode
	 *            the status code of the response
	 * @param binaryData
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(int statusCode, byte[] binaryData) {
		onSuccess(binaryData);
	}

	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 * @param binaryData
	 *            the response body, if any
	 * @deprecated
	 */
	@Deprecated
	public void onFailure(Throwable error, byte[] binaryData) {
		// By default, call the deprecated onFailure(Throwable) for
		// compatibility
		onFailure(error);
	}

	//
	// Pre-processing of messages (executes in background threadpool thread)
	//

	protected void sendSuccessMessage(int statusCode, byte[] responseBody) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] { statusCode,
				responseBody }));
	}

	@Override
	protected void sendFailureMessage(Throwable e, byte[] responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody }));
	}

	//
	// Pre-processing of messages (in original calling thread, typically the UI
	// thread)
	//

	protected void handleSuccessMessage(int statusCode, byte[] responseBody) {
		onSuccess(statusCode, responseBody);
	}

	protected void handleFailureMessage(Throwable e, byte[] responseBody) {
		onFailure(e, responseBody);
	}

	// Methods which emulate android's Handler and Message methods
	@Override
	protected void handleMessage(Message msg) {
		Object[] response;
		switch (msg.what) {
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(byte[]) response[1]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			handleFailureMessage((Throwable) response[0],
					response[1].toString());
			break;
		default:
			super.handleMessage(msg);
			break;
		}
	}

	// Interface to AsyncHttpRequest
	@Override
	protected void sendResponseMessage(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		Header[] contentTypeHeaders = response.getHeaders("Content-Type");
		byte[] responseBody = null;
		if (contentTypeHeaders.length != 1) {
			// malformed/ambiguous HTTP Header, ABORT!
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(),
					"None, or more than one, Content-Type Header found!"),
					responseBody);
			return;
		}
		Header contentTypeHeader = contentTypeHeaders[0];
		boolean foundAllowedContentType = false;
		for (String anAllowedContentType : mAllowedContentTypes) {
			if (Pattern.matches(anAllowedContentType,
					contentTypeHeader.getValue())) {
				foundAllowedContentType = true;
			}
		}
		if (!foundAllowedContentType) {
			// Content-Type not in allowed list, ABORT!
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), "Content-Type not allowed!"),
					responseBody);
			return;
		}
		try {
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				entity = new BufferedHttpEntity(temp);
			}
			responseBody = EntityUtils.toByteArray(entity);
		} catch (IOException e) {
			sendFailureMessage(e, (byte[]) null);
		}

		if (status.getStatusCode() >= 300) {
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), status.getReasonPhrase()),
					responseBody);
		} else {
			sendSuccessMessage(status.getStatusCode(), responseBody);
		}
	}
}