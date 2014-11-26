package com.repro.android.entities;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.repro.android.ReproAndroid;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.database.DatabaseSingleton;
import com.repro.android.utilities.Constants;

public class MembersModel {
	private final static String TAG = "MembersModel";
	private SQLiteDatabase db;
	
	public MembersModel(Context context) {
		this.db = DatabaseSingleton.getInstance(context).getDb();
	}

	public long createGroup(JSONObject group) {
		ContentValues values = new ContentValues();
		
		try {
			values.put(DatabaseConstants.REMOTE_ID, group.getInt("id"));
			values.put(DatabaseConstants.LABEL, group.getString(DatabaseConstants.LABEL));
			values.put(DatabaseConstants.LANG_ID, group.getString(DatabaseConstants.LANG_ID));
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
		
		long result = db.insert(DatabaseConstants.TABLE_GROUPS, null, values);
		Log.i(TAG, "Groups added(label): " + values.getAsString(DatabaseConstants.LABEL));
		return result;
	}
	
	public long createMember(JSONObject member) {
		ContentValues values = new ContentValues();
		
		try {
			values.put(DatabaseConstants.REMOTE_ID, member.getInt("id"));
			values.put(DatabaseConstants.GROUP_ID, member.getString(DatabaseConstants.GROUP_ID));
			values.put(DatabaseConstants.NAME, member.getString(DatabaseConstants.NAME));
			values.put(DatabaseConstants.EMAIL, member.getString(DatabaseConstants.EMAIL));
			values.put(DatabaseConstants.PICTURE, member.getString(DatabaseConstants.PICTURE));
			values.put(DatabaseConstants.CV, member.getString(DatabaseConstants.CV));
			values.put(DatabaseConstants.PUBS, member.getString(DatabaseConstants.PUBS));
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
		
		long result = db.insert(DatabaseConstants.TABLE_MEMBERS, null, values);
		Log.i(TAG, "Member added(name): " + values.getAsString(DatabaseConstants.NAME));
		return result;
	}
	
	public Cursor findGroups() {
		String selection = DatabaseConstants.LANG_ID + " = ?";
		String[] selectionArgs = new String[] { ReproAndroid.prefs.getString(Constants.LANGUAGE_ID, "1") }; 
		String order = DatabaseConstants.REMOTE_ID + " ASC";
		Cursor cursor = db.query(DatabaseConstants.TABLE_GROUPS, null, selection, selectionArgs, null, null, order);
		
		return cursor;
	}
	
	public Cursor findGroup(int groupId) {
		String selection = DatabaseConstants._ID + " = ?";
		String[] selectionArgs = new String[] { Integer.toString(groupId) };
		Cursor cursor = db.query(DatabaseConstants.TABLE_GROUPS, null, selection, selectionArgs, null, null, null);
		
		return cursor;
	}
	
	public Cursor findMembers(Cursor groups) {
		Cursor members = null;
		
		if(groups.moveToFirst()) {
			String groupIds = "";
			
			do {
				groupIds = groupIds.concat(groups.getString(groups.getColumnIndex(DatabaseConstants.REMOTE_ID)) + ",");
			} while(groups.moveToNext());
			
			groupIds = groupIds.substring(0, groupIds.length()-1);
			String selection = DatabaseConstants.GROUP_ID + " IN (" + groupIds + ")";
			String order = DatabaseConstants.REMOTE_ID + " ASC";
			members = db.query(DatabaseConstants.TABLE_MEMBERS, null, selection, null, null, null, order);
		}
		
		return members;
	}
	
	public Cursor findMember(int memberId) {
		String selection = DatabaseConstants._ID + " = ?";
		String[] selectionArgs = new String[] { Integer.toString(memberId) };
		Cursor cursor = db.query(DatabaseConstants.TABLE_MEMBERS, null, selection, selectionArgs, null, null, null);
		
		return cursor;
	}
}