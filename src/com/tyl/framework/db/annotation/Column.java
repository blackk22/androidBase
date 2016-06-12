package com.tyl.framework.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title TAColumn 字段配置
 * @Package com.firstte.util.db.annotation
 * @Description 数据字段注解
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column {
	/**
	 * 设置字段名
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 字段默认值
	 * 
	 * @return
	 */
	public String defaultValue() default "";
}
