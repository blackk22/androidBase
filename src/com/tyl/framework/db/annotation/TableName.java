package com.tyl.framework.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title TATableName 设置表名
 * @Package com.firstte.util.db.annotation
 * @Description 设置表名字
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableName {
	/**
	 * 设置表名字
	 * 
	 * @return
	 */
	String name() default "";
}