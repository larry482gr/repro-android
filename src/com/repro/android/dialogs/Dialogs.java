package com.repro.android.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class Dialogs {
	public static Builder confirmDialog(Context context, String title,
			String message, String positive_text,
			OnClickListener positive_listener, String negative_text,
			OnClickListener negative_listener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		if (title != null)
			builder.setTitle(title);
		if (message != null)
			builder.setMessage(message);
		if (positive_text != null)
			builder.setPositiveButton(positive_text, positive_listener);
		if (negative_text != null)
			builder.setNegativeButton(negative_text, negative_listener);

		return builder;
	}
	
	public static Builder optionDialog(Context context, String title, String[] options, OnClickListener onClickListener) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle(title).setItems(options, onClickListener);
	    return alertDialog;
	}
}