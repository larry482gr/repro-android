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

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.repro.android.R;

public class NewsAsyncTask extends HTTPAsyncTask {
	private String TAG = "NewsAsyncTask";
	private String id_param;
	private RelativeLayout newsView;
	private ProgressDialog dialog;
	
    public NewsAsyncTask(Context context, View rootView) {
    	super(context);
    	this.newsView = (RelativeLayout) rootView;
    	this.dialog = new ProgressDialog(context);
	}
    
    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Loading...");
        this.dialog.show();
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
			if(null != id_param)
				nameValuePairs.add(new BasicNameValuePair("id", id_param));
			
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
		Log.i(TAG, "Response JSON Objects: " + json_response.length());
		populateNewsList(json_response);
	}

	private void populateNewsList(JSONArray json_response) {
		ListView newsList = (ListView) newsView.findViewById(R.id.news_list);
		List<String> articlesTitles = new ArrayList<String>();
		for (int i = 0; i < json_response.length(); i++) {
			try {
				articlesTitles.add(json_response.getJSONObject(i).getString("title"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (dialog.isShowing()) {
            dialog.dismiss();
        }
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, articlesTitles);
		newsList.setAdapter(mAdapter);

	}
}