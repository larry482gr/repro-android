package com.repro.android.entities;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.repro.android.database.DatabaseConstants;

public class ArticlesModel extends Model {
	private final static String TAG = ArticlesModel.class.getCanonicalName();
	
	public ArticlesModel(Context context) {
		super(context);
	}

	public int getArticlesLastId() {
		return getLastId(DatabaseConstants.TABLE_NEWS);
	}
	
	public Cursor findAllArticles() {
		return findAll(DatabaseConstants.TABLE_NEWS, "DESC");
	}
	
	public Cursor findArticle(int articleId) {
		return find(DatabaseConstants.TABLE_NEWS, articleId);
	}
	
	public long createArticle(JSONObject article) {
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