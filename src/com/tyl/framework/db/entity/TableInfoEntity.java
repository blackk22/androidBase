package com.tyl.framework.db.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title TableInfoEntity
 * @Package com.firstte.core.util.sqlite
 * @Description TableInfoEntity 数据库的表信息
 * @version V1.0
 */
public class TableInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 488168612576359150L;
	/**
	 * 表名称
	 */
	private String tableName = "";
	/**
	 * 类名称
	 */
	private String className = "";
	/**
	 * 外键
	 */
	private PKProperyEntity pkProperyEntity = null;

	/**
	 * 字段信息列表
	 */
	ArrayList<PropertyEntity> propertieArrayList = new ArrayList<PropertyEntity>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public ArrayList<PropertyEntity> getPropertieArrayList() {
		return propertieArrayList;
	}

	public void setPropertieArrayList(List<PropertyEntity> propertyList) {
		this.propertieArrayList = (ArrayList<PropertyEntity>) propertyList;
	}

	public PKProperyEntity getPkProperyEntity() {
		return pkProperyEntity;
	}

	public void setPkProperyEntity(PKProperyEntity pkProperyEntity) {
		this.pkProperyEntity = pkProperyEntity;
	}

}
