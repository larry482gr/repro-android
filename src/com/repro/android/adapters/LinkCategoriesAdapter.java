package com.repro.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.repro.android.R;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.entities.LinksModel;

public class LinkCategoriesAdapter extends CursorAdapter {
	private static final String TAG = LinkCategoriesAdapter.class.getCanonicalName();
	private Activity mContext;
	private LayoutInflater inflater;

	public LinkCategoriesAdapter(Context context, Cursor linkCategoriesCursor, int flags) {
		super(context, linkCategoriesCursor, flags);
		this.mContext = (Activity) context;
		this.inflater = LayoutInflater.from(context);
		Log.d(TAG, "Total Link Categories: " + linkCategoriesCursor.getCount());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		View view = null;
		
		view = inflater.inflate(R.layout.link_category_item, parent, false);
		holder = new ViewHolder();
		holder.categoryLabel = (TextView) view.findViewById(R.id.link_cat_label);
		
		view.setTag(holder);
		
		return view;
	}

	@Override
	public void bindView(final View view, final Context context, final Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final int categoryId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants._ID));
		
		holder.categoryLabel.setText((cursor.getPosition() + 1) + ". " + cursor.getString(cursor.getColumnIndex(DatabaseConstants.LABEL)));
		
		holder.categoryClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinksModel linksModel = new LinksModel(context);
				Cursor category = linksModel.findLinkCategory(categoryId);
				Cursor links = linksModel.findAllLinks(category);
				if(category.moveToFirst()) {
					Log.d(TAG, "==========" + category.getString(category.getColumnIndex(DatabaseConstants.LABEL)) + "==========");
				}
				
				if(links.moveToFirst()) {
					int i = 1;
					do {
						Log.d(TAG, i + ". " + links.getString(links.getColumnIndex(DatabaseConstants.LINK_LABEL)) + 
								" --> " + links.getString(links.getColumnIndex(DatabaseConstants.LINK_URL)));
						i++;
					} while(links.moveToNext());
				}
				
				Log.d(TAG, "=================================");
				/*
				FragmentManager fragmentManager = mContext.getFragmentManager();
				fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
					.replace(R.id.container, LinkCategoryFragment.newInstance(categoryId))
					.addToBackStack(null)
					.commit();
				*/
			}
		};
		
		view.setOnClickListener(holder.categoryClick);
	}
	
	private static class ViewHolder {
		TextView categoryLabel;
		OnClickListener categoryClick;
	}
}