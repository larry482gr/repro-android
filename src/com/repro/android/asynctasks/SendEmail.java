package com.repro.android.asynctasks;

import java.io.IOException;
import java.net.URLEncoder;
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

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.repro.android.R;

public class SendEmail extends HttpTextAsyncTask {
	private String TAG = "SendEmail";
	
	public SendEmail(Context context) {
		super(context);
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;
		String name = params[0];
		String email = params[1];
		String subject = params[2];
		String message = params[3];
		String isOpinion = params[4];
		
		String contact_url = mContext.getResources().getString(R.string.contact_script);
		String url = super.domain_url + contact_url;
		// Log.i(TAG, "URL: " + url);
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", URLEncoder.encode(name, "UTF-8")));
			nameValuePairs.add(new BasicNameValuePair("email", URLEncoder.encode(email, "UTF-8")));
			nameValuePairs.add(new BasicNameValuePair("subject", URLEncoder.encode(subject, "UTF-8")));
			nameValuePairs.add(new BasicNameValuePair("message", URLEncoder.encode(message, "UTF-8")));
			nameValuePairs.add(new BasicNameValuePair("opinion_form", URLEncoder.encode(isOpinion, "UTF-8")));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// response from the php script
			HttpResponse http_response = httpclient.execute(httppost);
			HttpEntity entity = null;
			Log.e(TAG, "HTTP response code: " + Integer.toString(http_response.getStatusLine().getStatusCode()));
			if (http_response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				entity = http_response.getEntity();
				
				serverResponse = EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			Log.i(TAG, "ClientProtocolException");
		} catch (IOException e) {
			Log.i(TAG, "IOException");
		}
		
		return serverResponse;
	}
	
	@Override
	public void onPostExecute(String serverResponse) {
		if(serverResponse.equals("fill_all")) {
			Toast.makeText(mContext, "Fill all fields", Toast.LENGTH_LONG).show();
		} else if(serverResponse.equals("success")) {
			Toast.makeText(mContext, "Email successfully sent!", Toast.LENGTH_LONG).show();
		}
	}

}