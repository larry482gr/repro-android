package com.repro.android.entities;

import org.json.JSONException;
import org.json.JSONObject;

import com.repro.android.ReproAndroid;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.database.DatabaseSingleton;
import com.repro.android.utilities.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ArticlesModel {
	private final static String TAG = "NewsModel";
	private SQLiteDatabase db;
	
	public ArticlesModel(Context context) {
		this.db = DatabaseSingleton.getInstance(context).getDb();
	}

	public int getLastId() {
		String[] columns = new String[] { DatabaseConstants.REMOTE_ID };
		String order = DatabaseConstants.REMOTE_ID + " DESC";
		String limit = "LIMIT 0, 1";
		int lastId = 0;
		
		Cursor cursor = db.query(DatabaseConstants.TABLE_NEWS, columns, null, null, null, null, order + " " + limit);
		if(cursor.moveToFirst()) {
			lastId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.REMOTE_ID));
		}
		
		return lastId;
	}
	
	public Cursor findArticles() {
		String selection = DatabaseConstants.LANG_ID + " = ?";
		String[] selectionArgs = new String[] { ReproAndroid.prefs.getString(Constants.LANGUAGE_ID, "1") }; 
		String order = DatabaseConstants.REMOTE_ID + " DESC";
		Cursor cursor = db.query(DatabaseConstants.TABLE_NEWS, null, selection, selectionArgs, null, null, order);
		
		return cursor;
	}
	
	public long createArticle(JSONObject article) {
		// SQLiteDatabase db = dbHandler.getDb();
		ContentValues values = new ContentValues();
		
		try {
			values.put(DatabaseConstants.REMOTE_ID, article.getInt("id"));
			values.put(DatabaseConstants.TITLE, article.getString(DatabaseConstants.TITLE));
			values.put(DatabaseConstants.SHORT_DESC, article.getString(DatabaseConstants.SHORT_DESC));
			values.put(DatabaseConstants.LONG_DESC, article.getString(DatabaseConstants.LONG_DESC));
			values.put(DatabaseConstants.SOURCE_LABEL, article.getString(DatabaseConstants.SOURCE_LABEL));
			values.put(DatabaseConstants.SOURCE_LINK, article.getString(DatabaseConstants.SOURCE_LINK));
			values.put(DatabaseConstants.PICTURE, article.getString(DatabaseConstants.PICTURE));
			values.put(DatabaseConstants.LANG_ID, article.getInt(DatabaseConstants.LANG_ID));
			values.put(DatabaseConstants.PUBLISHED_AT, article.getString(DatabaseConstants.PUBLISHED_AT));
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
		
		long result = db.insert(DatabaseConstants.TABLE_NEWS, null, values);
		Log.i(TAG, "Article added(id): " + values.getAsString(DatabaseConstants.REMOTE_ID));
		return result;
	}
}