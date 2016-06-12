package com.tyl.framework.db.entity;

/**
 * @Title PKProperyEntity
 * @Package com.firstte.core.util.sqlite
 * @Description PKProperyEntity 数据库的外键字段
 * @version V1.0
 */
public class PKProperyEntity extends PropertyEntity {

	public PKProperyEntity() {

	}

	public PKProperyEntity(String name, Class<?> type, Object defaultValue,
			boolean primaryKey, boolean isAllowNull, boolean autoIncrement,
			String columnName) {
		super(name, type, defaultValue, primaryKey, isAllowNull, autoIncrement,
				columnName);
	}

}
