package com.repro.android.entities;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.repro.android.ReproAndroid;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.utilities.Constants;

public class MembersModel extends Model {
	private final static String TAG = MembersModel.class.getCanonicalName();
	
	public MembersModel(Context context) {
		super(context);
	}
	
	public int getGroupsLastId() {
		return getLastId(DatabaseConstants.TABLE_MEMBERS_GROUPS);
	}
	
	public int getMembersLastId() {
		return getLastId(DatabaseConstants.TABLE_MEMBERS);
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
		
		long result = db.insert(DatabaseConstants.TABLE_MEMBERS_GROUPS, null, values);
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
	
	public Cursor findAllMembersGroups() {
		return findAll(DatabaseConstants.TABLE_MEMBERS_GROUPS, "ASC");
	}
	
	public Cursor findMembersGroup(int groupId) {
		return find(DatabaseConstants.TABLE_MEMBERS_GROUPS, groupId);
	}
	
	public Cursor findAllMembers(Cursor groups) {
		return findAllIn(groups, DatabaseConstants.GROUP_ID, DatabaseConstants.TABLE_MEMBERS, "ASC");
	}
	
	public Cursor findMember(int memberId) {
		return find(DatabaseConstants.TABLE_MEMBERS, memberId);
	}
}