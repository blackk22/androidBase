package com.tyl.framework.exception;

import android.database.sqlite.SQLiteException;

/**
 * An exception that indicates there was an error with SQLite asset retrieval or
 * parsing.
 */
public class SQLiteAssetException extends SQLiteException {

	private static final long serialVersionUID = 1L;

	public SQLiteAssetException() {
	}

	public SQLiteAssetException(String error) {
		super(error);
	}
	
}