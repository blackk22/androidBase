package com.tyl.framework.db.util.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.http.NameValuePair;

import com.tyl.framework.db.annotation.PrimaryKey;
import com.tyl.framework.db.entity.FTArrayList;
import com.tyl.framework.db.util.DBUtils;
import com.tyl.framework.exception.DBException;
import com.tyl.framework.util.text.StringUtils;

/**
 * @Title TASqlBuilder
 * @Package com.firstte.util.db.util.sql
 * @Description 更新sql语句构建器类
 * @version V1.0
 */
public class UpdateSqlBuilder extends SqlBuilder {

	@Override
	public void onPreGetStatement() throws DBException,
			IllegalArgumentException, IllegalAccessException {
		if (getUpdateFields() == null) {
			setUpdateFields(getFieldsAndValue(entity));
		}
		super.onPreGetStatement();
	}

	/**
	 * 构建sql语句
	 */
	@Override
	public String buildSql() throws DBException, IllegalArgumentException,
			IllegalAccessException {
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("UPDATE ");
		stringBuilder.append(tableName).append(" SET ");
		
		FTArrayList needUpdate = getUpdateFields();
		for (int i = 0; i < needUpdate.size(); i++) {
			NameValuePair nameValuePair = needUpdate.get(i);
			stringBuilder
					.append(nameValuePair.getName())
					.append(" = ")
					.append(StringUtils.isNumeric(nameValuePair.getValue()
							.toString()) ? nameValuePair.getValue() : "'"
							+ nameValuePair.getValue() + "'");
			if (i + 1 < needUpdate.size()) {
				stringBuilder.append(", ");
			}
		}
		if (!StringUtils.isEmpty(this.where)) {
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
					Annotation annotation = field
							.getAnnotation(PrimaryKey.class);
					if (annotation != null) {
						String columnName = DBUtils.getColumnByField(field);
						whereArrayList.add((columnName != null && !columnName
								.equals("")) ? columnName : field.getName(),
								field.get(entity).toString());
					}

				}
			}
		}
		if (whereArrayList.isEmpty()) {
			throw new DBException("不能创建Where条件，语句");
		}
		return whereArrayList;
	}

	/**
	 * 从实体加载,更新的数据
	 * 
	 * @return
	 * @throws DBException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static FTArrayList getFieldsAndValue(Object entity)
			throws DBException, IllegalArgumentException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		FTArrayList arrayList = new FTArrayList();
		if (entity == null) {
			throw new DBException("没有加载实体类！");
		}
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {

			if (!DBUtils.isTransient(field)) {
				if (DBUtils.isBaseDateType(field)) {
					PrimaryKey annotation = field
							.getAnnotation(PrimaryKey.class);
					if (annotation == null || !annotation.autoIncrement()) {
						String columnName = DBUtils.getColumnByField(field);
						field.setAccessible(true);
						arrayList
								.add((columnName != null && !columnName
										.equals("")) ? columnName : field
										.getName(),
										field.get(entity) == null ? null
												: field.get(entity).toString());
					}
				}
			}
		}
		return arrayList;
	}
}
