package com.tyl.framework.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title TATableName 设置实体属性不被识别为表字段
 * @Package com.firstte.util.db.annotation
 * @Description 设置实体属性不被识别为表字段
 * @version V1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transient {

}
