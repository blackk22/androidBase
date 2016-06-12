package com.tyl.framework.exception;

/**
 * @Title FTException
 * @package com.firstte.core.exception
 * @Description FTException 所有异常的基类
 * @version V1.0
 */
public class FTException extends Exception {
	private static final long serialVersionUID = 1L;

	public FTException() {
		super();
	}

	public FTException(String detailMessage) {
		super(detailMessage);
	}
}