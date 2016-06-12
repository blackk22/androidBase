package com.tyl.framework.db.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.tyl.framework.db.entity.PKProperyEntity;
import com.tyl.framework.db.entity.PropertyEntity;
import com.tyl.framework.db.entity.TableInfoEntity;
import com.tyl.framework.exception.DBException;

/**
 * @Title TableInfofactory
 * @Package com.firstte.util.db.util.sql
 * @Description 数据库表工厂类
 * @version V1.0
 */
public class TableInfofactory {
	/**
	 * 表名为键，表信息为值的HashMap
	 */
	private static final HashMap<String, TableInfoEntity> tableInfoEntityMap = new HashMap<String, TableInfoEntity>();

	private TableInfofactory() {

	}

	private static TableInfofactory instance;

	/**
	 * 获得数据库表工厂
	 * 
	 * @return 数据库表工厂
	 */
	public static TableInfofactory getInstance() {
		if (instance == null) {
			instance = new TableInfofactory();
		}
		return instance;
	}

	/**
	 * 获得表信息
	 * 
	 * @param clazz
	 *            实体类型
	 * @return 表信息
	 * @throws DBException
	 */
	public TableInfoEntity getTableInfoEntity(Class<?> clazz)
			throws DBException {
		if (clazz == null){
			throw new DBException("表信息获取失败，应为class为null");
		}
		TableInfoEntity tableInfoEntity = tableInfoEntityMap.get(clazz.getName());
		
		if (tableInfoEntity == null) {
			
			tableInfoEntity = new TableInfoEntity();
			//表名称
			tableInfoEntity.setTableName(DBUtils.getTableName(clazz));
			//类名称
			tableInfoEntity.setClassName(clazz.getName());
			//主键列
			Field idField = DBUtils.getPrimaryKeyField(clazz);
			
			if (idField != null) {
				PKProperyEntity pkProperyEntity = new PKProperyEntity();
				pkProperyEntity.setColumnName(DBUtils.getColumnByField(idField));
				
				pkProperyEntity.setName(idField.getName());
				pkProperyEntity.setType(idField.getType());
				pkProperyEntity.setAutoIncrement(DBUtils.isAutoIncrement(idField));
				tableInfoEntity.setPkProperyEntity(pkProperyEntity);
			} else {
				tableInfoEntity.setPkProperyEntity(null);
			}
			
			List<PropertyEntity> propertyList = DBUtils
					.getPropertyList(clazz);
			if (propertyList != null) {
				tableInfoEntity.setPropertieArrayList(propertyList);
			}

			tableInfoEntityMap.put(clazz.getName(), tableInfoEntity);
		}
		if (tableInfoEntity == null
				|| tableInfoEntity.getPropertieArrayList() == null
				|| tableInfoEntity.getPropertieArrayList().size() == 0) {
			throw new DBException("不能创建+" + clazz + "的表信息");
		}
		return tableInfoEntity;
	}
}
