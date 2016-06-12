package com.tyl.framework.exception;

/**
 * @Title DBNotOpenException
 * @package com.firstte.exception
 * @Description DBNotOpenException 数据库不能打抛出异常
 * @version V1.0
 */
public class DBNotOpenException extends FTException {
	private static final long serialVersionUID = 1L;

	public DBNotOpenException() {
		super();
	}

	public DBNotOpenException(String detailMessage) {
		super(detailMessage);
	}

}
