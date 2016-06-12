package com.tyl.framework.db.util.sql;

import java.lang.reflect.Field;

import com.tyl.framework.db.entity.FTArrayList;
import com.tyl.framework.db.util.DBUtils;
import com.tyl.framework.exception.DBException;

/**
 * @Title DeleteSqlBuilder
 * @Package com.firstte.util.db.util.sql
 * @Description 删除sql语句构建器类
 * @version V1.0
 */
public class DeleteSqlBuilder extends SqlBuilder {
	
	@Override
	public String buildSql() throws DBException, IllegalArgumentException,IllegalAccessException {
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("DELETE FROM ");
		stringBuilder.append(tableName);
		if (entity == null) {
			stringBuilder.append(buildConditionString());
		} else {
			stringBuilder.append(buildWhere(buildWhere(this.entity)));
		}

		return stringBuilder.toString();
	}

	/**
	 * 创建Where语句
	 * 
	 * @param entity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws DBException
	 */
	public FTArrayList buildWhere(Object entity)
			throws IllegalArgumentException, IllegalAccessException,
			DBException {
		Class<?> clazz = entity.getClass();
		FTArrayList whereArrayList = new FTArrayList();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (!DBUtils.isTransient(field)) {
				if (DBUtils.isBaseDateType(field)) {
					// 如果ID不是自动增加的
					if (!DBUtils.isAutoIncrement(field)) {
						String columnName = DBUtils.getColumnByField(field);
						if (null != field.get(entity)
								&& field.get(entity).toString().length() > 0) {
							whereArrayList.add(
									(columnName != null && !columnName
											.equals("")) ? columnName : field
											.getName(), field.get(entity)
											.toString());
						}
					}
				}
			}
		}
		if (whereArrayList.isEmpty()) {
			throw new DBException("不能创建Where条件，语句");
		}
		return whereArrayList;
	}
}
