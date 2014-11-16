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

		AlertDialog.Builder confirmDialog = new AlertDialog.Builder(context);

		if (title != null)
			confirmDialog.setTitle(title);
		if (message != null)
			confirmDialog.setMessage(message);
		if (positive_text != null)
			confirmDialog.setPositiveButton(positive_text, positive_listener);
		if (negative_text != null)
			confirmDialog.setNegativeButton(negative_text, negative_listener);

		return confirmDialog;
	}
	
	public static Builder optionDialog(Context context, String title, String[] options, OnClickListener onClickListener) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle(title).setItems(options, onClickListener);
	    return alertDialog;
	}
}