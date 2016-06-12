package com.tyl.framework.data.city;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class ProvinceCityDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "city";
	private static final int DATABASE_VERSION = 1;
	
	public ProvinceCityDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
		
		// you can use an alternate constructor to specify a database location
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

        // call this method to force a database overwrite every time the version number increments:
        //setForcedUpgrade();

		// call this method to force a database overwrite if the version number
		// is below a certain threshold:
		//setForcedUpgrade(2);
	}
	
	/**
	 * 获得所有省份、直辖市个数
	 * @return
	 */
	public int getProvinceCount() {

		int count=0;
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {"id", "name" };
		String sqlTables = "gb2260";
		String sqlSelection=" id%10000=0";
		String sArgs[]={};
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, sArgs, null, null, null);
		c.moveToFirst();
		count=c.getCount();
		c.close();
		db.close();
		return count;

	}

	/**
	 * 获得所有省份、直辖市
	 * @return
	 */
	public List<ProvinceCityInfo> getProvinceList() {

		List<ProvinceCityInfo> list=new ArrayList<ProvinceCityInfo>();
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {"id", "name" };
		String sqlTables = "gb2260";
		String sqlSelection=" id%10000=0";
		String sArgs[]={};
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, sArgs, null, null, null);
		c.moveToFirst();
		for(int i=0,len=c.getCount();i<len;i++){
			list.add(new ProvinceCityInfo(c.getString(0), c.getString(1)));
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}
	
	/**
	 * 获得所有省份、直辖市
	 * @return
	 */
	public String[][] getProvinceArray() {

		String provinces[][];
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {"id", "name" };
		String sqlTables = "gb2260";
		String sqlSelection=" id%10000=0";
		String sArgs[]={};
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, sArgs, null, null, null);
		c.moveToFirst();
		int count=c.getCount();
		provinces=new String[count][2];
		for(int i=0;i<count;i++){
			provinces[i][0]=c.getString(0);
        	provinces[i][1]=c.getString(1);
			c.moveToNext();
		}
		c.close();
		db.close();
		return provinces;

	}

	/**
	 * 获得对应省份的城市的个数
	 * @param province 省份的id
	 * @return
	 */
	public int getCityCount(String province) {

		int count=0;
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String sqlSelection=" id % 100 = 0 and id-?<10000 and id-?>0 ";
		String[] sqlSelect = { "id", "name"};
		String[] sArgs={province,province};
		String sqlTables = "gb2260";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, sArgs, null, null, null);
		c.moveToFirst();
		count=c.getCount();
		c.close();
		db.close();
		return count;

	}
	
	/**
	 * 获得对应省份的城市，如果是直辖市就返回空
	 * @param province 省份的id
	 * @return
	 */
	public List<ProvinceCityInfo> getCityList(String province) {

		List<ProvinceCityInfo> list=new ArrayList<ProvinceCityInfo>();
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String sqlSelection=" id % 100 = 0 and id-?<10000 and id-?>0 ";
		String[] sqlSelect = { "id", "name"};
		String[] sArgs={province,province};
		String sqlTables = "gb2260";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, sArgs, null, null, null);

		c.moveToFirst();
		for(int i=0,len=c.getCount();i<len;i++){
			list.add(new ProvinceCityInfo(c.getString(0), c.getString(1)));
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;

	}
	
	/**
	 * 获得对应省份的城市，如果是直辖市就返回空
	 * @param province 省份的id
	 * @return
	 */
	public String[][] getCityArray(String province) {

		String cities[][];
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String sqlSelection=" id % 100 = 0 and id-?<10000 and id-?>0 ";
		String[] sqlSelect = { "id", "name"};
		String[] sArgs={province,province};
		String sqlTables = "gb2260";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, sArgs, null, null, null);
		c.moveToFirst();
		int count=c.getCount();
		cities=new String[count][2];
		for(int i=0;i<count;i++){
			cities[i][0]=c.getString(0);
			cities[i][1]=c.getString(1);
			c.moveToNext();
		}
		c.close();
		db.close();
		return cities;

	}
}
