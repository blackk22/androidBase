package com.tyl.framework.exception;

/**
 * @Title DBException
 * @package com.firstte.exception
 * @Description 数据库字段错误时抛出
 * @version V1.0
 */
public class DBFieldException extends FTException {

	private static final long serialVersionUID = -6328047121656335941L;

	public DBFieldException() {
	}

	public DBFieldException(String detailMessage) {
		super(detailMessage);
	}

}
