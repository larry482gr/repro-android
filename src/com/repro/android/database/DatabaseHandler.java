/**
 * @author Lazaros Kazantzis
 * @version 1.1
 */
package com.repro.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The Class DatabaseHandler.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
	
	/** The logcat tag. */
	@SuppressWarnings("unused")
	private String TAG = "DatabaseHandler";
	
	/** The values. */
	private ContentValues values;
	
	/**
	 * Instantiates a new database handler.
	 *
	 * @param context
	 *            the context
	 */
	public DatabaseHandler(Context context) {
		super(context, DatabaseConstants.REPRO_DATABASE, null, DatabaseConstants.DATABASE_VERSION);
		values = new ContentValues();
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseConstants.CREATE_NEWS_TABLE);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_NEWS);
		
		onCreate(db);
	}
	
	/**
	 * Gets the db.
	 *
	 * @return the db
	 */
	public SQLiteDatabase getDb() {
		return this.getWritableDatabase();
	}
	
	/**
	 * Delete all records.
	 *
	 * @param table
	 *            the table
	 */
	public void deleteAllRecords(String table) {
		SQLiteDatabase db = getDb();
		db.delete(table, null, null);
	}
	
	// TODO Model Methods or even better create a DAO Factory.
	
}