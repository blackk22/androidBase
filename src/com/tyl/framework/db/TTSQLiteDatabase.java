package com.tyl.framework.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.tyl.framework.db.entity.DBMasterEntity;
import com.tyl.framework.db.entity.FTArrayList;
import com.tyl.framework.db.entity.FTHashMap;
import com.tyl.framework.db.entity.MapArrayList;
import com.tyl.framework.db.util.DBUtils;
import com.tyl.framework.db.util.SqlBuilderFactory;
import com.tyl.framework.db.util.sql.SqlBuilder;
import com.tyl.framework.exception.DBException;
import com.tyl.framework.exception.DBNotOpenException;
import com.tyl.framework.log.L;

/**
 * @Title FTSQLiteDatabase
 * @Package com.firstte.util.db
 * @Description 数据库管理类，通过此类进行数据库的操作
 * @version V1.0
 */
public class TTSQLiteDatabase {
	private final static String TAG="FTSQLiteDatabase";
	// 数据库默认设置
	private final static String DB_NAME = "ft_android.db"; // 默认数据库名字
	private final static int DB_VERSION = 1;// 默认数据库版本
	// 当前SQL指令
	private String queryStr = "";
	// 错误信息
	private String error = "";
	// 当前查询Cursor
	//private Cursor queryCursor = null;
	// 是否已经连接数据库
	private Boolean isConnect = false;
	// 执行oepn打开数据库时，保存返回的数据库对象
	private SQLiteDatabase mSQLiteDatabase = null;
	private DBHelper mDatabaseHelper = null;
	
	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param params
	 *            数据参数信息
	 */
	public TTSQLiteDatabase(Context context, DBParams params,DBUpdateListener dbUpdateListener) {
		this.mDatabaseHelper = new DBHelper(context, params.getDbName(),
				null, params.getDbVersion(),dbUpdateListener);
	}


	/**
	 * 打开数据库如果是 isWrite为true,则磁盘满时抛出错误
	 * 
	 * @param isWrite
	 * @return
	 */
	public SQLiteDatabase openDatabase(Boolean isWrite) {

		if (isWrite) {
			mSQLiteDatabase = openWritable();
		} else {
			mSQLiteDatabase = openReadable();
		}
		return mSQLiteDatabase;

	}

	/**
	 * 以读写方式打开数据库，一旦数据库的磁盘空间满了，数据库就不能以只能读而不能写抛出错误。
	 * 
	 * @param dbUpdateListener
	 * @return
	 */
	public SQLiteDatabase openWritable() {
		try {
			mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
			isConnect = true;
			// 注销数据库连接配置信息
			// 暂时不写
		} catch (Exception e) {
			isConnect = false;
		}

		return mSQLiteDatabase;
	}

	/**
	 * 测试 FTSQLiteDatabase是否可用
	 * 
	 * @return
	 */
	public Boolean testSQLiteDatabase() {
		if (isConnect) {
			if (mSQLiteDatabase.isOpen()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 以读写方式打开数据库，如果数据库的磁盘空间满了，就会打开失败，当打开失败后会继续尝试以只读方式打开数据库。如果该问题成功解决，
	 * 则只读数据库对象就会关闭，然后返回一个可读写的数据库对象。
	 * 
	 * @param dbUpdateListener
	 * @return
	 */
	public SQLiteDatabase openReadable() {
		try {
			mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
			isConnect = true;
			// 注销数据库连接配置信息
			// 暂时不写
		} catch (Exception e) {
			// TODO: handle exception
			isConnect = false;
		}

		return mSQLiteDatabase;
	}

	/**
	 * 执行查询，主要是SELECT, SHOW 等指令 返回数据集
	 * 
	 * @param sql
	 *            sql语句
	 * @param selectionArgs
	 * @return
	 */
	public ArrayList<FTHashMap<String>> query(String sql, String[] selectionArgs) {
		L.i(TTSQLiteDatabase.this, sql);
		ArrayList<FTHashMap<String>> data=null;
		if (testSQLiteDatabase()) {
			if (sql != null && !sql.equalsIgnoreCase("")) {
				this.queryStr = sql;
			}
			
			Cursor queryCursor = null;
			queryCursor = mSQLiteDatabase.rawQuery(sql, selectionArgs);
			if (queryCursor != null) {
				data= getQueryCursorData(queryCursor);
				free(queryCursor);
			} else {
				L.e(TTSQLiteDatabase.this, "执行" + sql + "错误");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
		}
		return data;
	}

	/**
	 * 执行查询，主要是SELECT, SHOW 等指令 返回数据集
	 * 
	 * @param clazz
	 * @param distinct
	 *            限制重复，如过为true则限制,false则不用管
	 * @param where
	 *            where语句
	 * @param groupBy
	 *            groupBy语句
	 * @param having
	 *            having语句
	 * @param orderBy
	 *            orderBy语句
	 * @param limit
	 *            limit语句
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> query(Class<?> clazz, boolean distinct, String where,
			String groupBy, String having, String orderBy, String limit) {

		if (testSQLiteDatabase()) {
			List<T> list = null;
			SqlBuilder getSqlBuilder = SqlBuilderFactory.getInstance()
					.getSqlBuilder(SqlBuilderFactory.SELECT);
			getSqlBuilder.setClazz(clazz);
			getSqlBuilder.setCondition(distinct, where, groupBy, having,
					orderBy, limit);
			try {
				String sqlString = getSqlBuilder.getSqlStatement();
				L.i(TTSQLiteDatabase.this, "执行" + sqlString);
				Cursor queryCursor = null;
				queryCursor = mSQLiteDatabase.rawQuery(sqlString, null);
				list = (List<T>) DBUtils.getListEntity(clazz,queryCursor);
				free(queryCursor);
			} catch (IllegalArgumentException e) {
				
				L.e(TTSQLiteDatabase.this, e.getMessage());
				e.printStackTrace();

			} catch (DBException e) {
				
				L.e(TTSQLiteDatabase.this, e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				
				L.e(TTSQLiteDatabase.this, e.getMessage());
				e.printStackTrace();
			}
			return list;
		} else {
			L.d("firste", "query-----数据库没有打开");
			return null;
		}

	}
	
	/**
	 * 查询数据返回游标
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public  Cursor  queryWithCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {

		Cursor queryCursor = null;
		if (testSQLiteDatabase()) {
			queryCursor = mSQLiteDatabase.query(table, columns, selection,selectionArgs, groupBy, having, orderBy);
		} 
		return queryCursor;
		

	}

	/**
	 * 查询记录
	 * 
	 * @param table
	 *            表名
	 * @param columns
	 *            需要查询的列
	 * @param selection
	 *            格式化的作为 SQL WHERE子句(不含WHERE本身)。 传递null返回给定表的所有行。
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy语句
	 * @param having
	 *            having语句
	 * @param orderBy
	 *            orderBy语句
	 * @return
	 */
	public ArrayList<FTHashMap<String>> query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		ArrayList<FTHashMap<String>> data=null;
		if (testSQLiteDatabase()) {
			Cursor queryCursor = null;
			queryCursor = mSQLiteDatabase.query(table, columns, selection,
					selectionArgs, groupBy, having, orderBy);
			if (queryCursor != null) {
				data= getQueryCursorData(queryCursor);
				free(queryCursor);
			} else {
				L.e(TTSQLiteDatabase.this, "查询" + table + "错误");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
		}
		return data;
	}

	/**
	 * 查询记录
	 * 
	 * @param distinct
	 *            限制重复，如过为true则限制,false则不用管
	 * @param table
	 *            表名
	 * @param columns
	 *            需要查询的列
	 * @param selection
	 *            格式化的作为 SQL WHERE子句(不含WHERE本身)。 传递null返回给定表的所有行。
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy语句
	 * @param having
	 *            having语句
	 * @param orderBy
	 *            orderBy语句
	 * @param limit
	 *            limit语句
	 * @return
	 */
	public ArrayList<FTHashMap<String>> query(String table, boolean distinct,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		ArrayList<FTHashMap<String>> data=null;
		if (testSQLiteDatabase()) {
			Cursor queryCursor = null;
			queryCursor = mSQLiteDatabase.query(distinct, table, columns,
					selection, selectionArgs, groupBy, having, orderBy, limit);
			if (queryCursor != null) {
				data= getQueryCursorData(queryCursor);
				free(queryCursor);
			} else {
				L.e(TTSQLiteDatabase.this, "查询" + table + "错误");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
		}
		return data;
	}

	/**
	 * 查询记录
	 * 
	 * @param table
	 *            表名
	 * @param columns
	 *            需要查询的列
	 * @param selection
	 *            格式化的作为 SQL WHERE子句(不含WHERE本身)。 传递null返回给定表的所有行。
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy语句
	 * @param having
	 *            having语句
	 * @param orderBy
	 *            orderBy语句
	 * @param limit
	 *            limit语句
	 * @return
	 */
	public ArrayList<FTHashMap<String>> query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		ArrayList<FTHashMap<String>> data=null;
		if (testSQLiteDatabase()) {
			Cursor queryCursor = null;
			queryCursor = mSQLiteDatabase.query(table, columns, selection,
					selectionArgs, groupBy, having, orderBy, limit);
			if (queryCursor != null) {
				data= getQueryCursorData(queryCursor);
				free(queryCursor);
			} else {
				L.e(TTSQLiteDatabase.this, "查询" + table + "错误");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
		}
		return data;
	}

	/**
	 * 查询记录
	 * 
	 * @param cursorFactory
	 * @param distinct
	 *            限制重复，如过为true则限制,false则不用管
	 * @param table
	 *            表名
	 * @param columns
	 *            需要查询的列
	 * @param selection
	 *            格式化的作为 SQL WHERE子句(不含WHERE本身)。 传递null返回给定表的所有行。
	 * @param selectionArgs
	 * @param groupBy
	 *            groupBy语句
	 * @param having
	 *            having语句
	 * @param orderBy
	 *            orderBy语句
	 * @param limit
	 *            limit语句
	 * @return
	 */
	public ArrayList<FTHashMap<String>> queryWithFactory(
			CursorFactory cursorFactory, boolean distinct, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		ArrayList<FTHashMap<String>> data=null;
		if (testSQLiteDatabase()) {
			Cursor queryCursor = null;
			queryCursor = mSQLiteDatabase.queryWithFactory(cursorFactory,
					distinct, table, columns, selection, selectionArgs,
					groupBy, having, orderBy, limit);
			if (queryCursor != null) {
				data= getQueryCursorData(queryCursor);
				free(queryCursor);
			} else {
				L.e(TTSQLiteDatabase.this, "查询" + table + "错误");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
		}
		return data;

	}

	/**
	 * INSERT, UPDATE 以及DELETE
	 * 
	 * @param sql
	 *            语句
	 * @param bindArgs
	 * @throws DBNotOpenException
	 */
	public void execute(String sql, String[] bindArgs)
			throws DBNotOpenException {
		L.i(TTSQLiteDatabase.this, "准备执行SQL[" + sql + "]语句");
		if (testSQLiteDatabase()) {
			if (sql != null && !sql.equalsIgnoreCase("")) {
				this.queryStr = sql;
				if (bindArgs != null) {
					mSQLiteDatabase.execSQL(sql, bindArgs);
				} else {
					mSQLiteDatabase.execSQL(sql);
				}

			}

		} else {
			throw new DBNotOpenException("数据库未打开！");
		}

	}

	/**
	 * 执行INSERT, UPDATE 以及DELETE操作
	 * 
	 * @param getSqlBuilder
	 *            Sql语句构建器
	 * @return
	 */
	public Boolean execute(SqlBuilder getSqlBuilder) {
		Boolean isSuccess = false;
		String sqlString;
		try {
			sqlString = getSqlBuilder.getSqlStatement();
			execute(sqlString, null);
			isSuccess = true;
		} catch (IllegalArgumentException e) {
			
			isSuccess = false;
			e.printStackTrace();

		} catch (DBException e) {
			
			isSuccess = false;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			isSuccess = false;
			e.printStackTrace();
		} catch (DBNotOpenException e) {
			
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

	/**
	 * 获得所有的查询数据集中的数据
	 * 
	 * @return
	 */
	public MapArrayList<String> getQueryCursorData(Cursor queryCursor) {
		MapArrayList<String> arrayList = null;
		if (queryCursor != null) {
			try {
				arrayList = new MapArrayList<String>();
				queryCursor.moveToFirst();
				while (queryCursor.moveToNext()) {
					arrayList.add(DBUtils.getRowData(queryCursor));
				}
			} catch (Exception e) {
				e.printStackTrace();
				L.e(TTSQLiteDatabase.this, "当前数据集获取失败！");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "当前数据集不存在！");
		}
		return arrayList;
	}

	/**
	 * 取得数据库的表信息
	 * 
	 * @return
	 */
	public ArrayList<DBMasterEntity> getTables() {
		ArrayList<DBMasterEntity> tadbMasterArrayList = new ArrayList<DBMasterEntity>();
		String sql = "select * from sqlite_master where type='table' order by name";
		L.i(TTSQLiteDatabase.this, sql);
		if (testSQLiteDatabase()) {
			if (sql != null && !sql.equalsIgnoreCase("")) {
				this.queryStr = sql;
				
				Cursor queryCursor = null;
				queryCursor = mSQLiteDatabase
						.rawQuery(
								"select * from sqlite_master where type='table' order by name",
								null);

				if (queryCursor != null) {
					while (queryCursor.moveToNext()) {
						if (queryCursor != null
								&& queryCursor.getColumnCount() > 0) {
							DBMasterEntity tadbMasterEntity = new DBMasterEntity();
							tadbMasterEntity.setType(queryCursor.getString(0));
							tadbMasterEntity.setName(queryCursor.getString(1));
							tadbMasterEntity.setTbl_name(queryCursor.getString(2));
							tadbMasterEntity.setRootpage(queryCursor.getInt(3));
							tadbMasterEntity.setSql(queryCursor.getString(4));
							tadbMasterArrayList.add(tadbMasterEntity);
						}
					}
				} else {
					L.e(TTSQLiteDatabase.this, "数据库未打开！");
				}
				free(queryCursor);
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
		}
		return tadbMasterArrayList;
	}

	/**
	 * 判断是否存在某个表,为true则存在，否则不存在
	 * 
	 * @param clazz
	 * @return true则存在，否则不存在
	 */
	public boolean hasTable(Class<?> clazz) {
		String tableName = DBUtils.getTableName(clazz);
		return hasTable(tableName);
	}

	/**
	 * 判断是否存在某个表,为true则存在，否则不存在
	 * 
	 * @param tableName
	 *            需要判断的表名
	 * @return true则存在，否则不存在
	 */
	public boolean hasTable(String tableName) {
		boolean flag=false;
		if (tableName != null && !tableName.equalsIgnoreCase("")) {
			if (testSQLiteDatabase()) {
				tableName = tableName.trim();
				String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+ tableName + "' ";
				if (sql != null && !sql.equalsIgnoreCase("")) {
					this.queryStr = sql;
				}
				
				Cursor queryCursor = null;
				queryCursor = mSQLiteDatabase.rawQuery(sql, null);
				if (queryCursor.moveToNext()) {
					int count = queryCursor.getInt(0);
					if (count > 0) {
						flag=true;
					}
				}
				free(queryCursor);
			} else {
				L.e(TTSQLiteDatabase.this, "数据库未打开！");
			}
		} else {
			L.e(TTSQLiteDatabase.this, "判断数据表名不能为空！");
		}
		return flag;
	}

	/**
	 * 创建表
	 * 
	 * @param clazz
	 * @return 为true创建成功，为false创建失败
	 */
	public Boolean creatTable(Class<?> clazz) {
		Boolean isSuccess = false;
		if (testSQLiteDatabase()) {
			try {
				String sqlString = DBUtils.creatTableSql(clazz);
				execute(sqlString, null);
				isSuccess = true;
			} catch (DBException e) {
				
				isSuccess = false;
				//e.printStackTrace();
				L.e(TTSQLiteDatabase.this, e.getMessage());
			} catch (DBNotOpenException e) {
				
				isSuccess = false;
				//e.printStackTrace();
				L.e(TTSQLiteDatabase.this, e.getMessage());
			}
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
			return false;
		}
		return isSuccess;
	}

	public Boolean dropTable(Class<?> clazz) {
		String tableName = DBUtils.getTableName(clazz);
		return dropTable(tableName);
	}

	/**
	 * 删除表
	 * 
	 * @param tableName
	 * @return 为true创建成功，为false创建失败
	 */
	public Boolean dropTable(String tableName) {
		Boolean isSuccess = false;
		if (tableName != null && !tableName.equalsIgnoreCase("")) {
			if (testSQLiteDatabase()) {
				try {
					String sqlString = "DROP TABLE " + tableName;
					execute(sqlString, null);
					isSuccess = true;
				} catch (Exception e) {
					isSuccess = false;
					e.printStackTrace();
					L.e(TTSQLiteDatabase.this, e.getMessage());
				}
			} else {
				L.e(TTSQLiteDatabase.this, "数据库未打开！");
				return false;
			}
		} else {
			L.e(TTSQLiteDatabase.this, "删除数据表名不能为空！");
		}
		return isSuccess;
	}

	/**
	 * 更新表用于对实体修改时，改变表 暂时不写
	 * 
	 * @param tableName
	 * @return
	 */
	public Boolean alterTable(String tableName) {
		return false;
	}

	/**
	 * 数据库错误信息 并显示当前的SQL语句
	 * 
	 * @return
	 */
	public String error() {
		if (this.queryStr != null && !queryStr.equalsIgnoreCase("")) {
			error = error + "\n [ SQL语句 ] : " + queryStr;
		}
		L.e(TTSQLiteDatabase.this, error);
		return error;
	}

	/**
	 * 插入记录
	 * 
	 * @param entity
	 *            插入的实体
	 * @return
	 */
	public Boolean insert(Object entity) {
		return insert(entity, null);
	}

	/**
	 * 插入记录
	 * 
	 * @param table
	 *            需要插入到的表
	 * @param nullColumnHack
	 *            不允许为空的行
	 * @param values
	 *            插入的值
	 * @return
	 */
	public Boolean insert(String table, String nullColumnHack,
			ContentValues values) {
		if (testSQLiteDatabase()) {
			return mSQLiteDatabase.insert(table, nullColumnHack, values) > 0;
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
			return false;
		}
	}

	/**
	 * 插入记录
	 * 
	 * @param table
	 *            需要插入到的表
	 * @param nullColumnHack
	 *            不允许为空的行
	 * @param values
	 *            插入的值
	 * @return
	 */
	public Boolean insertOrThrow(String table, String nullColumnHack,
			ContentValues values) {
		if (testSQLiteDatabase()) {
			return mSQLiteDatabase.insertOrThrow(table, nullColumnHack, values) > 0;
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
			return false;
		}
	}

	/**
	 * 插入记录
	 * 
	 * @param entity
	 *            传入数据实体
	 * @param updateFields
	 *            插入到的字段,可设置为空
	 * @return 返回true执行成功，否则执行失败
	 */
	public Boolean insert(Object entity, FTArrayList updateFields) {

		SqlBuilder getSqlBuilder = SqlBuilderFactory.getInstance()
				.getSqlBuilder(SqlBuilderFactory.INSERT);
		getSqlBuilder.setEntity(entity);
		getSqlBuilder.setUpdateFields(updateFields);
		return execute(getSqlBuilder);
	}

	/**
	 * 删除记录
	 * 
	 * @param table
	 *            被删除的表名
	 * @param whereClause
	 *            设置的WHERE子句时，删除指定的数据 ,如果null会删除所有的行。
	 * @param whereArgs
	 * 
	 * @return 返回true执行成功，否则执行失败
	 */
	public Boolean delete(String table, String whereClause, String[] whereArgs) {
		if (testSQLiteDatabase()) {
			return mSQLiteDatabase.delete(table, whereClause, whereArgs) > 0;

		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
			return false;
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param clazz
	 * @param where
	 *            where语句
	 * @return 返回true执行成功，否则执行失败
	 */
	public Boolean delete(Class<?> clazz, String where) {
		if (testSQLiteDatabase()) {
			SqlBuilder getSqlBuilder = SqlBuilderFactory.getInstance()
					.getSqlBuilder(SqlBuilderFactory.DELETE);
			getSqlBuilder.setClazz(clazz);
			getSqlBuilder.setCondition(false, where, null, null, null, null);
			return execute(getSqlBuilder);
		} else {
			return false;
		}

	}

	/**
	 * 删除记录
	 * 
	 * @param entity
	 * @return 返回true执行成功，否则执行失败
	 */
	public Boolean delete(Object entity) {
		if (testSQLiteDatabase()) {
			SqlBuilder getSqlBuilder = SqlBuilderFactory.getInstance()
					.getSqlBuilder(SqlBuilderFactory.DELETE);
			getSqlBuilder.setEntity(entity);
			return execute(getSqlBuilder);
		} else {
			return false;
		}

	}

	/**
	 * 更新记录
	 * 
	 * @param table
	 *            表名字
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 返回true执行成功，否则执行失败
	 */
	public Boolean update(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		if (testSQLiteDatabase()) {
			return mSQLiteDatabase
					.update(table, values, whereClause, whereArgs) > 0;
		} else {
			L.e(TTSQLiteDatabase.this, "数据库未打开！");
			return false;
		}
	}

	/**
	 * 更新记录 这种更新方式只有才主键不是自增的情况下可用
	 * 
	 * @param entity
	 *            更新的数据
	 * @return 返回true执行成功，否则执行失败
	 */
	public Boolean update(Object entity) {
		return update(entity, null);
	}

	/**
	 * 更新记录
	 * 
	 * @param entity
	 *            更新的数据
	 * @param where
	 *            where语句
	 * @return
	 */
	public Boolean update(Object entity, String where) {
		if (testSQLiteDatabase()) {
			SqlBuilder getSqlBuilder = SqlBuilderFactory.getInstance()
					.getSqlBuilder(SqlBuilderFactory.UPDATE);
			getSqlBuilder.setEntity(entity);
			getSqlBuilder.setCondition(false, where, null, null, null, null);
			return execute(getSqlBuilder);
		} else {
			return false;
		}

	}

	/**
	 * 获取最近一次查询的sql语句
	 * 
	 * @return sql 语句
	 */
	public String getLastSql() {
		return queryStr;
	}

	/**
	 * 获得当前查询数据集合
	 * 
	 * @return
	 */
	/*
	public Cursor getQueryCursor() {
		return queryCursor;
	}
	 */
	/**
	 * 关闭数据库
	 */
	public void close() {
		mSQLiteDatabase.close();
	}

	/**
	 * 释放查询结果
	 */
	public void free(Cursor queryCursor) {
		if (queryCursor != null) {
			try {
				queryCursor.close();
			} catch (Exception e) {
				L.e(TAG, e);
			}
		}

	}

	/**
	 * 数据库配置参数
	 */
	public static class DBParams {
		/**
		 * 数据库名称
		 */
		private String dbName = DB_NAME;
		
		/**
		 * 数据库版本
		 */
		private int dbVersion = DB_VERSION;

		public DBParams() {
			
		}

		public DBParams(String dbName, int dbVersion) {
			this.dbName = dbName;
			this.dbVersion = dbVersion;
		}

		public String getDbName() {
			return dbName;
		}

		public void setDbName(String dbName) {
			this.dbName = dbName;
		}

		public int getDbVersion() {
			return dbVersion;
		}

		public void setDbVersion(int dbVersion) {
			this.dbVersion = dbVersion;
		}
	}

	/**
	 * Interface 数据库升级回调
	 */
	public interface DBUpdateListener {
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}
}
