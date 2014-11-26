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

import android.content.Context;
import android.util.Log;

import com.repro.android.R;
import com.repro.android.entities.ArticlesModel;

public class NewsAsyncTask extends HttpJsonAsyncTask {
	private String TAG = "NewsAsyncTask";
	private ArticlesModel articlesModel;
	
    public NewsAsyncTask(Context context) {
    	super(context);
    	articlesModel = new ArticlesModel(mContext);
	}

	@Override
    protected JSONArray doInBackground(String... params) {
		JSONArray json_response = null;
		
		String news_url = mContext.getResources().getString(R.string.news_script);
		String action = params[0];
		
		String url = super.domain_url + news_url;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", action));
			
			int lastId = articlesModel.getLastId();
			nameValuePairs.add(new BasicNameValuePair("last_id", Integer.toString(lastId)));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// response from the php script
			HttpResponse http_response = httpclient.execute(httppost);
			HttpEntity entity = null;
			Log.e(TAG, "HTTP response code: " + Integer.toString(http_response.getStatusLine().getStatusCode()));
			if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				entity = http_response.getEntity();
				
				String response = null;
				try {
					response = EntityUtils.toString(entity);
					json_response = new JSONArray(response);
				} catch (Exception e) {
					Log.e(TAG, "Parse JSON Exception");
					Log.e(TAG, "Exception Message:\n" + e.getLocalizedMessage());
					Log.e(TAG, "Response was:\n" + String.valueOf(response));
					Log.e(TAG, "JSON Response was:\n" + String.valueOf(response));
				}
			}
		} catch (ClientProtocolException e) {
			Log.i(TAG, "ClientProtocolException");
		} catch (IOException e) {
			Log.i(TAG, "IOException");
		}
		
		return json_response;
    }
	
	@Override
	protected void onPostExecute(JSONArray json_response) {
		try {
			Log.i(TAG, "Response JSON Objects: " + json_response.length());
			createArticles(json_response);
		} catch(NullPointerException e) {
			Log.d(TAG, "NullPointerException (line 105): Server response was null!");
		}
	}

	private void createArticles(JSONArray json_response) {
		for (int i = 0; i < json_response.length(); i++) {
			try {
				articlesModel.createArticle(json_response.getJSONObject(i));
			} catch (JSONException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
		}
	}
}