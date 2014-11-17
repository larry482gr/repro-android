/**
 * @author Lazaros Kazantzis
 * @version 1.0
 */
package com.repro.android.database;

/**
 * The Class DatabaseConstants.
 */
public class DatabaseConstants {
	
	public final static int DATABASE_VERSION = 1;
	public final static String REPRO_DATABASE = "repro_db";
	public final static String TABLE_NEWS = "news";
	public final static String _ID = "_id";
	public final static String REMOTE_ID = "remote_id";
	public final static String TITLE = "title";
	public final static String SHORT_DESC = "short_desc";
	public final static String LONG_DESC = "long_desc";
	public final static String SOURCE_LABEL = "source_label";
	public final static String SOURCE_LINK = "source_link";
	public final static String PICTURE = "picture";
	public final static String LANG_ID = "lang_id";
	public final static String PUBLISHED_AT = "published_at";
	
	/** The Constant CREATE_NEWS_TABLE. */
	public final static String CREATE_NEWS_TABLE = "CREATE TABLE " + TABLE_NEWS + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + REMOTE_ID + " INTEGER," + TITLE + " TEXT," + SHORT_DESC + " TEXT," + 
			LONG_DESC + " TEXT," + SOURCE_LABEL + " TEXT," + SOURCE_LINK + " TEXT," + PICTURE + " TEXT," + 
			LANG_ID + " TEXT, " + PUBLISHED_AT + " TEXT" + "); " + 
			"CREATE INDEX title_index ON " + TABLE_NEWS + "(" + TITLE + ");";
}