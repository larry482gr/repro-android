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
import com.repro.android.entities.Article;
import com.repro.android.entities.ArticlesModel;

public class NewsAsyncTask extends HTTPAsyncTask {
	private String TAG = "NewsAsyncTask";
	private String id_param;
	private ArticlesModel articlesModel;
	// private ArrayList<Article> articles;
	// private ProgressDialog dialog;
	
    public NewsAsyncTask(Context context) {
    	super(context);
    	// this.dialog = new ProgressDialog(context);
    	// this.articles = articles;
    	articlesModel = new ArticlesModel(mContext);
	}
    
    @Override
    protected void onPreExecute() {
        // this.dialog.setMessage("Loading...");
        // this.dialog.show();
    }

	@Override
    protected JSONArray doInBackground(String... params) {
		JSONArray json_response = null;
		
		String news_url = mContext.getResources().getString(R.string.news_script);
		String action = params[0];
		id_param = params.length == 1 ? null : params[1];
		
		String url = super.domain_url + news_url;
		Log.i(TAG, "URL: " + url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("action", action));
			if(null != id_param) {
				nameValuePairs.add(new BasicNameValuePair("id", id_param));
			}
			
			int lastId = articlesModel.getLastId();
			nameValuePairs.add(new BasicNameValuePair("last_id", Integer.toString(lastId)));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// response from the php script
			HttpResponse http_response = httpclient.execute(httppost);
			HttpEntity entity = null;
			Log.e(TAG, "HTTP response code: " + Integer.toString(http_response.getStatusLine().getStatusCode()));
			if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				entity = http_response.getEntity();
				Log.i(TAG, "Entity: " + entity.toString());
				// response = Utilities._getResponseBody(entity);
				
				String response = null;
				try {
					response = EntityUtils.toString(entity);
					json_response = new JSONArray(response);
				} catch (Exception e) {
					Log.d(TAG, "Parse JSON Exception");
					Log.e(TAG, "Exception Message:\n" + e.getLocalizedMessage());
					Log.i(TAG, "Response was:\n" + response);
				}
			}
		} catch (ClientProtocolException e) {
			Log.i(TAG, "ClientProtocolException");
		} catch (IOException e) {
			Log.i(TAG, "IOException");
		}
		
		Log.d(TAG, String.valueOf(json_response));
		return json_response;
    }
	
	@Override
	protected void onPostExecute(JSONArray json_response) {
		try {
			Log.i(TAG, "Response JSON Objects: " + json_response.length());
			createArticles(json_response);
		} catch(NullPointerException e) {
			Log.d(TAG, "NullPointerException (line 107): Server response was null!");
		}
	}

	private void createArticles(JSONArray json_response) {
		// ListView newsList = (ListView) newsView.findViewById(R.id.news_list);
		for (int i = 0; i < json_response.length(); i++) {
			try {
				articlesModel.createArticle(json_response.getJSONObject(i));
			} catch (JSONException e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
		}
		/*
		if (dialog.isShowing()) {
            dialog.dismiss();
        }
        */
	}
}