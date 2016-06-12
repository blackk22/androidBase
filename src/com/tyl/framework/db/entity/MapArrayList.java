package com.tyl.framework.db.entity;

import java.util.ArrayList;


/**
 * @Title MapArrayList 
 * @Package com.firstte.util.db.entity
 * @Description MapArrayList FTHashMap集合扩充
 * @version V1.0
 */
public class MapArrayList<T extends Object> extends ArrayList<FTHashMap<T>> {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(FTHashMap<T> taHashMap) {
		if (taHashMap != null) {
			return super.add(taHashMap);
		} else {
			return false;
		}
	}
}
