package com.repro.android.entities;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.repro.android.database.DatabaseConstants;

public class LinksModel extends Model {
	private final static String TAG = LinksModel.class.getCanonicalName();
	
	public LinksModel(Context context) {
		super(context);
	}
	
	public int getCategoriesLastId() {
		return getLastId(DatabaseConstants.TABLE_LINKS_CATEGORIES);
	}
	
	public int getLinksLastId() {
		return getLastId(DatabaseConstants.TABLE_LINKS);
	}

	public long createCategory(JSONObject category) {
		ContentValues values = new ContentValues();
		
		try {
			values.put(DatabaseConstants.REMOTE_ID, category.getInt("id"));
			values.put(DatabaseConstants.LABEL, category.getString(DatabaseConstants.LABEL));
			values.put(DatabaseConstants.LANG_ID, category.getString(DatabaseConstants.LANG_ID));
			values.put(DatabaseConstants.POSITION, category.getString(DatabaseConstants.POSITION));
			values.put(DatabaseConstants.IS_ACTIVE, category.getString(DatabaseConstants.IS_ACTIVE));
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
		
		long result = db.insert(DatabaseConstants.TABLE_LINKS_CATEGORIES, null, values);
		Log.i(TAG, "Category added (label): " + values.getAsString(DatabaseConstants.LABEL));
		return result;
	}
	
	public long createLink(JSONObject link) {
		ContentValues values = new ContentValues();
		
		try {
			values.put(DatabaseConstants.REMOTE_ID, link.getInt("id"));
			values.put(DatabaseConstants.CATEGORY_ID, link.getString(DatabaseConstants.CATEGORY_ID));
			values.put(DatabaseConstants.HEADER, link.getString(DatabaseConstants.HEADER));
			values.put(DatabaseConstants.PREPEND_TEXT, link.getString(DatabaseConstants.PREPEND_TEXT));
			values.put(DatabaseConstants.LINK_URL, link.getString(DatabaseConstants.LINK_URL));
			values.put(DatabaseConstants.LINK_LABEL, link.getString(DatabaseConstants.LINK_LABEL));
			values.put(DatabaseConstants.APPEND_TEXT, link.getString(DatabaseConstants.APPEND_TEXT));
			values.put(DatabaseConstants.IS_GROUP, link.getString(DatabaseConstants.IS_GROUP));
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
		
		long result = db.insert(DatabaseConstants.TABLE_LINKS, null, values);
		Log.i(TAG, "Link added(label): " + values.getAsString(DatabaseConstants.LINK_LABEL));
		return result;
	}
	
	public Cursor findAllLinkCategories() {
		return findAll(DatabaseConstants.TABLE_LINKS_CATEGORIES, "ASC");
	}
	
	public Cursor findLinkCategory(int categoryId) {
		return find(DatabaseConstants.TABLE_LINKS_CATEGORIES, categoryId);
	}
	
	public Cursor findAllLinks(Cursor categories) {
		return findAllIn(categories, DatabaseConstants.CATEGORY_ID, DatabaseConstants.TABLE_LINKS, "ASC");
	}
	
	public Cursor findLink(int linkId) {
		return find(DatabaseConstants.TABLE_LINKS, linkId);
	}
}