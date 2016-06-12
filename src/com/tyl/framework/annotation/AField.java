package com.tyl.framework.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Title AField 字段配置
 * @Package com.firstte.annotation
 * @Description 不配置的时候默认为字符串空，对应默认值为字符串空
 * @version V1.0
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface AField {
	/**
	 * 设置配置的名
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * 设置配置的默认值
	 * 
	 * @return
	 */
	public String defaultValue() default "";

}