package com.tyl.framework.util.reflect;

import java.lang.reflect.Field;
import java.util.Date;

import com.tyl.framework.annotation.AField;
import com.tyl.framework.annotation.ATransparent;

/**
 * @Title ReflectUtils
 * @Package com.firstte.common
 * @Description 反射的一些工具
 * @version V1.0
 */
public class ReflectUtils {
	/**
	 * 检测实体属性是否已经被标注为 不被识别
	 * 
	 * @param field
	 *            字段
	 * @return
	 */
	public static boolean isTransient(Field field) {
		return field.getAnnotation(ATransparent.class) != null;
	}

	/**
	 * 是否为基本的数据类型
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isBaseDateType(Field field) {
		Class<?> clazz = field.getType();
		return clazz.equals(String.class) || clazz.equals(Integer.class)
				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.equals(java.util.Date.class)
				|| clazz.equals(java.sql.Date.class) || clazz.isPrimitive();
	}

	/**
	 * 获得配置名
	 * 
	 * @param field
	 * @return
	 */
	public static String getFieldName(Field field) {
		AField column = field.getAnnotation(AField.class);
		if (column != null && column.name().trim().length() != 0) {
			return column.name();
		}
		return field.getName();
	}
}
