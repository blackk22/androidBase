package com.tyl.framework.exception;

/**
 * @Title FileAlreadyExistException
 * @package com.firstte.exception
 * @Description FileAlreadyExistException 数据库表中字段存在抛出异常
 * @version V1.0
 */
public class FileAlreadyExistException extends FTException {
	private static final long serialVersionUID = 1L;

	public FileAlreadyExistException(String message) {

		super(message);
	}

}
