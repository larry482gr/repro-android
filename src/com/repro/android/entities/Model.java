package com.repro.android.entities;

import com.repro.android.ReproAndroid;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.database.DatabaseSingleton;
import com.repro.android.utilities.Constants;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Model {
	protected SQLiteDatabase db;
	
	public Model(Context context) {
		this.db = DatabaseSingleton.getInstance(context).getDb();
	}
	
	protected int getLastId(String table) {
		String[] columns = new String[] { DatabaseConstants.REMOTE_ID };
		String order = DatabaseConstants.REMOTE_ID + " DESC";
		String limit = "LIMIT 0, 1";
		int lastId = 0;
		
		Cursor cursor = db.query(table, columns, null, null, null, null, order + " " + limit);
		if(cursor.moveToFirst()) {
			lastId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.REMOTE_ID));
		}
		
		return lastId;
	}
	
	protected Cursor findAll(String table, String order) {
		String selection = DatabaseConstants.LANG_ID + " = ?";
		String[] selectionArgs = new String[] { ReproAndroid.prefs.getString(Constants.LANGUAGE_ID, "1") }; 
		order = DatabaseConstants.REMOTE_ID + " " + order;
		Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, order);
		
		return cursor;
	}
	
	protected Cursor findAllIn(Cursor in, String foreignKey, String table, String order) {
		Cursor cursor = null;
		
		if(in.moveToFirst()) {
			String inIds = "";
			
			do {
				inIds = inIds.concat(in.getString(in.getColumnIndex(DatabaseConstants.REMOTE_ID)) + ",");
			} while(in.moveToNext());
			
			inIds = inIds.substring(0, inIds.length()-1);
			String selection = foreignKey + " IN (" + inIds + ")";
			order = DatabaseConstants.REMOTE_ID + " " + order;
			cursor = db.query(table, null, selection, null, null, null, order);
		}
		
		return cursor;
	}
	
	protected Cursor find(String table, int id) {
		String selection = DatabaseConstants._ID + " = ?";
		String[] selectionArgs = new String[] { Integer.toString(id) };
		Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, null);
		
		return cursor;
	}
}