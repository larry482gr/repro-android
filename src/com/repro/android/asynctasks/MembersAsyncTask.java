package com.repro.android.asynctasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.repro.android.R;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.entities.MembersModel;

public class MembersAsyncTask extends HttpTextAsyncTask {
	private String TAG = "MembersAsyncTask";
	private MembersModel membersModel;
	
    public MembersAsyncTask(Context context) {
    	super(context);
    	membersModel = new MembersModel(mContext);
	}

	@Override
    protected String doInBackground(String... params) {
		String response = null;
		
		String members_url = mContext.getResources().getString(R.string.members_script);
		String action = params[0];
		
		String url = super.domain_url + members_url;
		Log.i(TAG, "URL: " + url);
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", action));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// response from the php script
			HttpResponse http_response = httpclient.execute(httppost);
			HttpEntity entity = null;
			Log.e(TAG, "HTTP response code: " + Integer.toString(http_response.getStatusLine().getStatusCode()));
			if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				entity = http_response.getEntity();
				
				try {
					response = EntityUtils.toString(entity);
				} catch (Exception e) {
					Log.e(TAG, "Parse JSON Exception");
					Log.e(TAG, "Exception Message:\n" + e.getLocalizedMessage());
					Log.e(TAG, "Response was:\n" + String.valueOf(response));
				}
			}
		} catch (ClientProtocolException e) {
			Log.i(TAG, "ClientProtocolException");
		} catch (IOException e) {
			Log.i(TAG, "IOException");
		}
		
		return response;
    }
	
	@Override
	protected void onPostExecute(String response) {
		Log.i(TAG, response.toString());
		JSONObject json_response = null;
		try {
			json_response = new JSONObject(response);
		} catch (JSONException e) {
			Log.i(TAG, e.getLocalizedMessage());
		}
		
		JSONArray groups = null;
		JSONArray members = null;
		try {
			groups = json_response.getJSONArray("groups");
			members = json_response.getJSONArray("members");
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
		
		Log.d(TAG, "Groups\n" + groups.toString());
		Log.d(TAG, "Members\n" + members.toString());
		
		createGroups(groups);
		createMembers(members);
		
		Cursor groupsCursor	= membersModel.findGroups();
		Cursor membersCursor = membersModel.findMembers(groupsCursor);
		
		if(membersCursor.moveToFirst()) {
			do {
				Log.d(TAG, "============NEW MEMBER START============");
				Log.i(TAG, membersCursor.getString(membersCursor.getColumnIndex(DatabaseConstants.REMOTE_ID)));
				Log.i(TAG, membersCursor.getString(membersCursor.getColumnIndex(DatabaseConstants.GROUP_ID)));
				Log.i(TAG, membersCursor.getString(membersCursor.getColumnIndex(DatabaseConstants.NAME)));
				Log.i(TAG, membersCursor.getString(membersCursor.getColumnIndex(DatabaseConstants.EMAIL)));
				Log.i(TAG, membersCursor.getString(membersCursor.getColumnIndex(DatabaseConstants.PICTURE)));
				Log.d(TAG, "============NEW MEMBER END============");
			} while(membersCursor.moveToNext());
		}
	}

	private void createGroups(JSONArray groups) {
		for (int i = 0; i < groups.length(); i++) {
			try {
				membersModel.createGroup(groups.getJSONObject(i));
			} catch (JSONException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
		}
	}

	private void createMembers(JSONArray members) {
		for (int i = 0; i < members.length(); i++) {
			try {
				membersModel.createMember(members.getJSONObject(i));
			} catch (JSONException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
		}
	}
}