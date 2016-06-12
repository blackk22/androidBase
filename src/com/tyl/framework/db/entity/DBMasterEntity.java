package com.tyl.framework.db.entity;


/**
 * @Title DBMasterEntity 对应sqlite_master表的实体类
 * @Package com.firstte.util.db.entity
 * @Description 对应sqlite_master表的实体类
 * @version V1.0
 */
public class DBMasterEntity extends BaseEntity {
	private static final long serialVersionUID = 4511697615195446516L;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 表名称
	 */
	private String tbl_name;
	/**
	 * sql语句
	 */
	private String sql;
	/**
	 * 根页面
	 */
	private int rootpage;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTbl_name() {
		return tbl_name;
	}

	public void setTbl_name(String tbl_name) {
		this.tbl_name = tbl_name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getRootpage() {
		return rootpage;
	}

	public void setRootpage(int rootpage) {
		this.rootpage = rootpage;
	}

}
