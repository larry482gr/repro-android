package com.repro.android.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.repro.android.R;

abstract class HttpTextAsyncTask extends AsyncTask<String, Void, String> {
	private String TAG = "HttpTextAsyncTask";
	protected Context mContext;
	protected String domain_url; // , param_separator, param_operator, action_param, id_param;
	
	public HttpTextAsyncTask(Context context) {
		this.mContext = context;
		this.domain_url = context.getResources().getString(R.string.domain_url);
		
		/**
		 * TODO if a get request is implemented
		 * this.param_separator = context.getResources().getString(R.string.param_separator);
		 * this.param_operator = context.getResources().getString(R.string.param_operator);
		 * this.action_param = context.getResources().getString(R.string.param_action);
		 * this.id_param = context.getResources().getString(R.string.param_action);
		 */
	}
}
