package com.tyl.framework.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import android.os.Message;

import com.tyl.framework.exception.FileAlreadyExistException;
import com.tyl.framework.log.L;
/**
 * @Title FileHttpResponseHandler
 * @Package com.firstte.util.http
 * @Description FileHttpResponseHandler是AsyncHttpClient的请求响应，响应的内容为文件，
 * 对AsyncHttpResponseHandler扩充，不支持断传。下载失败要求重新下载
 * @version V1.0
 */
public class DirectFileHttpResponseHandler extends AsyncHttpResponseHandler {
	/**
	 * 超时
	 */
	public final static int TIME_OUT = 30000;
	/**
	 * 缓冲大小
	 */
	private final static int BUFFER_SIZE = 1024 * 100;

	private static final String TAG = "DirectFileHttpResponseHandler";
	private static final String TEMP_SUFFIX = ".download";
	private File file;
	private File tempFile;
	private File baseDirFile;
	private RandomAccessFile outputStream;
	private long downloadSize;
	private long totalSize;
	private long networkSpeed;
	private long previousTime;
	private long totalTime;
	private boolean interrupt = false;
	private boolean timerInterrupt = false;
	private String url;
	private Timer timer = new Timer();
	private static final int TIMERSLEEPTIME = 100;

	/**
	 * 
	 * @param url
	 * @param rootFile
	 * @param fileName
	 */
	public DirectFileHttpResponseHandler(String url, String rootFile, String fileName) {
		this.url = url;
		this.baseDirFile = new File(rootFile);
		this.file = new File(rootFile, fileName);
		this.tempFile = new File(rootFile, fileName + TEMP_SUFFIX);
		init();
	}

	public DirectFileHttpResponseHandler(String rootFile, String fileName) {
		this.baseDirFile = new File(rootFile);
		this.file = new File(rootFile, fileName);
		this.tempFile = new File(rootFile, fileName + TEMP_SUFFIX);
		init();
	}

	public DirectFileHttpResponseHandler(String filePath) {
		super();
		this.file = new File(filePath);
		this.baseDirFile = new File(this.file.getParent());
		this.tempFile = new File(filePath + TEMP_SUFFIX);
		init();
	}

	private void init() {
		if (!this.baseDirFile.exists()) {
			this.baseDirFile.mkdirs();
		}
	}

	/**
	 * 周期发送下载进度
	 * 
	 */
	private void startTimer() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				while (!timerInterrupt) {
					sendProgressMessage(totalSize, getDownloadSize(),
							networkSpeed);
					try {
						Thread.sleep(TIMERSLEEPTIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}, 0, 1500);
	}

	private void stopTimer() {
		timerInterrupt = true;
	}

	public File getFile() {
		return file;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * 随机流对象
	 * @author LaoYing
	 *
	 */
	private class ProgressReportingRandomAccessFile extends RandomAccessFile {
		private int progress = 0;

		public ProgressReportingRandomAccessFile(File file, String mode)
				throws FileNotFoundException {
			super(file, mode);
		}

		@Override
		public void write(byte[] buffer, int offset, int count)
				throws IOException {

			super.write(buffer, offset, count);
			progress += count;
			totalTime = System.currentTimeMillis() - previousTime;
			downloadSize = progress;
			if (totalTime > 0) {
				networkSpeed = (long) ((progress / totalTime) / 1.024);
			}

		}
	}//end
	

	public boolean isInterrupt() {

		return interrupt;
	}

	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

	public long getDownloadSize() {

		return downloadSize;
	}

	public long getTotalSize() {

		return totalSize;
	}

	public double getDownloadSpeed() {

		return this.networkSpeed;
	}


	public long getTotalTime() {

		return this.totalTime;
	}

	public void onSuccess(byte[] binaryData) {
		onSuccess(new String(binaryData));
	}

	public void onSuccess(int statusCode, byte[] binaryData) {
		onSuccess(binaryData);
	}

	public void onFailure(Throwable error, byte[] binaryData) {
		onFailure(error);
	}

	protected void sendSuccessMessage(int statusCode, byte[] responseBody) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] { statusCode,
				responseBody }));
	}

	@Override
	protected void sendFailureMessage(Throwable e, byte[] responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e,
				responseBody }));
	}

	protected void sendProgressMessage(long totalSize, long currentSize,
			long speed) {
		sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[] { totalSize,
				currentSize, speed }));
	}

	protected void handleSuccessMessage(int statusCode, byte[] responseBody) {
		onSuccess(statusCode, responseBody);
	}

	protected void handleFailureMessage(Throwable e, byte[] responseBody) {
		onFailure(e, responseBody);
	}

	@Override
	protected void handleMessage(Message msg) {
		Object[] response;
		switch (msg.what) {
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			handleSuccessMessage(((Integer) response[0]).intValue(),
					(byte[]) response[1]);
			break;
		default:
			super.handleMessage(msg);
			break;
		}
	}

	@Override
	protected void sendResponseMessage(HttpResponse response) {

		Throwable error = null;
		byte[] responseBody = null;
		long result = -1;
		int statusCode = 0;
		// previousTime = System.currentTimeMillis();
		try {
			statusCode = response.getStatusLine().getStatusCode();
			long contentLenght = response.getEntity().getContentLength();
			L.v(TAG, "contentLenght: " + contentLenght);
			L.v(TAG, "response: " + response.toString());
			// -1的解决方式ContentLength 在手机访问的时候出现了问题，返回为-1
			if (contentLenght == -1) {
				contentLenght = response.getEntity().getContent().available();
			}
			L.v(TAG, "contentLenght: " + contentLenght);
			totalSize = contentLenght;

		    L.v(TAG, "totalSize: " + totalSize);

			if (file.exists() && totalSize == file.length()) {
				L.v(TAG, "Output file already exists. Skipping download.");
				throw new FileAlreadyExistException("Output file already exists. Skipping download.");
			}
			outputStream = new ProgressReportingRandomAccessFile(tempFile, "rw");
			InputStream input = response.getEntity().getContent();
			startTimer();
			//开始写入数据
			int bytesCopied = copy(input, outputStream);
			
			if (bytesCopied!= totalSize&& totalSize != -1 && !interrupt) {
				throw new IOException("Download incomplete: " + bytesCopied+ " != " + totalSize);
			}
			L.v(TAG, "Download completed successfully."+url);
			result = bytesCopied;
		} catch (FileNotFoundException e) {
			sendFailureMessage(e, responseBody);
			error = e;
		} catch (FileAlreadyExistException e) {
			// sendSuccessMessage(statusCode, e.getMessage().getBytes());
			error = e;
		} catch (IllegalStateException e) {
			error = e;
			// sendFailureMessage(e, responseBody);
		} catch (IOException e) {
			// sendFailureMessage(e, responseBody);
			error = e;
		}finally{
			// 停止打印
			stopTimer();
			// 保证timer被关闭
			try {
				Thread.sleep(TIMERSLEEPTIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (result == -1 || interrupt || error != null) {
				if (error != null) {
					L.v(TAG, "Download failed." + error.getMessage());
					if (error instanceof FileAlreadyExistException) {
						L.d("sendResponseMessage", "文件已经存在:"+file.getAbsolutePath());
						sendSuccessMessage(statusCode, "下载成功！".getBytes());

					} else {
						sendFailureMessage(error, responseBody);
					}
				}
				return;
			}
			tempFile.renameTo(file);
			L.d("sendResponseMessage", statusCode+"==下载成功:"+file.getAbsolutePath());
			sendSuccessMessage(statusCode, "下载成功！".getBytes());
		}
		
	}

	public int copy(InputStream input, RandomAccessFile out) throws IOException {

		if (input == null || out == null) {
			return -1;
		}

		byte[] buffer = new byte[BUFFER_SIZE];

		BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
		L.v(TAG, "length" + out.length());
		int count = 0, n = 0;
		long errorBlockTimePreviousTime = -1, expireTime = 0;
		try {
			out.seek(0);
			previousTime = System.currentTimeMillis();
			while (!interrupt) {
				n = in.read(buffer, 0, BUFFER_SIZE);
				if (n == -1) {
					break;
				}
				out.write(buffer, 0, n);
				count += n;
				if (networkSpeed == 0) {
					if (errorBlockTimePreviousTime > 0) {
						expireTime = System.currentTimeMillis()- errorBlockTimePreviousTime;
						if (expireTime > TIME_OUT) {
							throw new ConnectTimeoutException("connection time out.");
						}
					} else {
						errorBlockTimePreviousTime = System.currentTimeMillis();
					}
				} else {
					expireTime = 0;
					errorBlockTimePreviousTime = -1;
				}
			}
		} finally {

			try {
				out.close();
				// 无法关闭 inputstram
				// input.close();
				// in.close();
			} catch (IOException e) {
				L.e(TAG, e);
			}
		}
		return count;

	}

	/**
	 * 获得下载文件的抽象标示File
	 * @return
	 */
	public File getTempFile() {
		return tempFile;
	}

}