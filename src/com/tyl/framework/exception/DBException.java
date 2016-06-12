package com.tyl.framework.exception;

/**
 * @Title DBException
 * @package com.firstte.exception
 * @Description 数据库错误时抛出
 * @version V1.0
 */
public class DBException extends FTException {
	private static final long serialVersionUID = 1L;

	public DBException() {
		super();
	}

	public DBException(String detailMessage) {
		super(detailMessage);
	}

}
