package com.tyl.framework.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * @Title AsyncHttpResponseHandler
 * @Package com.firstte.util.http
 * @Description AsyncHttpResponseHandler是异步Http响应句柄
 * @version V1.0
 */
public class AsyncHttpResponseHandler {
	/**
	 * 成功
	 */
	protected static final int SUCCESS_MESSAGE = 4;
	/**
	 * 失败
	 */
	protected static final int FAILURE_MESSAGE = 1;
	/**
	 * 开始
	 */
	protected static final int START_MESSAGE = 2;
	/**
	 * 失败
	 */
	protected static final int FINISH_MESSAGE = 3;
	/**
	 * 进行中
	 */
	protected static final int PROGRESS_MESSAGE = 0;
	private Handler handler;

	public AsyncHttpResponseHandler() {
		if (Looper.myLooper() != null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					
					AsyncHttpResponseHandler.this.handleMessage(msg);
				}
			};
		}
	}

	/**
	 * 开始回调方法
	 * @param error
	 * @param content
	 */
	public void onStart() {
	}

	/**
	 * 完成回调方法
	 * @param error
	 * @param content
	 */
	public void onFinish() {
	}

	/**
	 * 成功回调方法
	 * @param error
	 * @param content
	 */
	public void onSuccess(String content) {

	}

	/**
	 * 进度更新
	 * @param error
	 * @param content
	 */
	public void onProgress(long totalSize, long currentSize, long speed) {

	}

	/**
	 * 成功回调方法
	 * @param error
	 * @param content
	 */
	public void onSuccess(int statusCode, Header[] headers, String content) {
		onSuccess(statusCode, content);
	}

	/**
	 * 成功回调方法
	 * @param error
	 * @param content
	 */
	public void onSuccess(int statusCode, String content) {
		onSuccess(content);
	}

	/**
	 * 失败回调方法
	 * @param error
	 * @param content
	 */
	public void onFailure(Throwable error) {

	}

	/**
	 * 失败回调方法
	 * @param error
	 * @param content
	 */
	public void onFailure(Throwable error, String content) {

		onFailure(error);
	}

	/**
	 * 发送成功信息
	 * @param statusCode
	 * @param headers
	 * @param responseBody
	 */
	protected void sendSuccessMessage(int statusCode, Header[] headers,
			String responseBody) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] {new Integer(statusCode), headers, responseBody }));
	}

	/**
	 * 发送失败信息
	 * @param e
	 * @param responseBody
	 */
	protected void sendFailureMessage(Throwable e, String responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody }));
	}

	/**
	 * 发送失败信息
	 * @param e
	 * @param responseBody
	 */
	protected void sendFailureMessage(Throwable e, byte[] responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody }));
	}

	/**
	 * 发送开始信息
	 */
	protected void sendStartMessage() {
		sendMessage(obtainMessage(START_MESSAGE, null));
	}

	/**
	 * 发送完成信息
	 */
	protected void sendFinishMessage() {
		sendMessage(obtainMessage(FINISH_MESSAGE, null));
	}

	/**
	 * 触发成功信息
	 * @param statusCode
	 * @param headers
	 * @param responseBody
	 */
	protected void handleSuccessMessage(int statusCode, Header[] headers,
			String responseBody) {
		onSuccess(statusCode, headers, responseBody);
	}

	/**
	 * 触发失败信息
	 * @param e
	 * @param responseBody
	 */
	protected void handleFailureMessage(Throwable e, String responseBody) {
		onFailure(e, responseBody);
	}
	/**
	 * 更新进度信息
	 * @param totalSize
	 * @param currentSize
	 * @param speed
	 */
	protected void handleProgressMessage(long totalSize, long currentSize,
			long speed) {
		onProgress(totalSize, currentSize, speed);
	}

	/**
	 * 处理消息
	 * @param msg
	 */
	protected void handleMessage(Message msg) {
		Object[] response;

		switch (msg.what) {
		case PROGRESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleProgressMessage((Long) response[0], (Long) response[1],
					(Long) response[2]);
			break;
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(Header[]) response[1], (String) response[2]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			handleFailureMessage((Throwable) response[0], (String) response[1]);
			break;
		case START_MESSAGE:
			onStart();
			break;
		case FINISH_MESSAGE:
			onFinish();
			break;

		}
	}

	/**
	 * 发送消息
	 * @param msg
	 */
	protected void sendMessage(Message msg) {
		if (handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}

	/**
	 * 创建消息
	 * @param responseMessage
	 * @param response
	 * @return
	 */
	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = Message.obtain();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}

	/**
	 * 发送响应信息进行处理
	 * @param response
	 */
	protected void sendResponseMessage(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		String responseBody = null;
		try {
			//HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				
				//entity = new BufferedHttpEntity();
				responseBody = EntityUtils.toString(temp,"utf-8");
				//Logger.i(this,"响应："+responseBody);
			}
		} catch (IOException e) {
			sendFailureMessage(e, (String) null);
		}

		if (status.getStatusCode() >= 300) {
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), status.getReasonPhrase()),
					responseBody);
		}else{
			sendSuccessMessage(status.getStatusCode(),response.getAllHeaders(), responseBody);
		}
	}
}
