package com.repro.android.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

public class NewsAsyncTask extends AsyncTask<String, Void, String> {
	private String TAG = "NewsAsyncTask";
	private Context mContext;
	
    public NewsAsyncTask(Context context) {
		this.mContext = context;
	}

	@Override
    protected String doInBackground(String... params) {
        
		return null;
    }
	
	@Override
	protected void onPostExecute(String result) {
		
	}
}