package com.repro.android.adapters;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.repro.android.R;
import com.repro.android.database.DatabaseConstants;

public class MembersAdapter extends CursorAdapter {
	private static final String TAG = "NewsAdapter";
	private Activity mContext;
	private LayoutInflater inflater;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;

	public MembersAdapter(Context context, Cursor membersCursor, int flags) {
		super(context, membersCursor, flags);
		this.mContext = (Activity) context;
		this.inflater = LayoutInflater.from(context);
		Log.d(TAG, "Total Members: " + membersCursor.getCount());
		
		// Image loader options
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_empty)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.bitmapConfig(Bitmap.Config.ARGB_8888)
			.build();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		View view = null;
		
		view = inflater.inflate(R.layout.member_item, parent, false);
		holder = new ViewHolder();
		holder.memberImage = (ImageView) view.findViewById(R.id.member_image);
		holder.memberName = (TextView) view.findViewById(R.id.member_name);
		holder.memberCV = (TextView) view.findViewById(R.id.member_cv);
		holder.memberEmail = (TextView) view.findViewById(R.id.member_email);
		
		view.setTag(holder);
		
		return view;
	}

	@Override
	public void bindView(final View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final int memberId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants._ID));
		String image = cursor.getString(cursor.getColumnIndex(DatabaseConstants.PICTURE));
		String imageUri = mContext.getResources().getString(R.string.members_images) + image;
		Log.i(TAG, "Member name: " + cursor.getString(cursor.getColumnIndex(DatabaseConstants.NAME)));
		Log.i(TAG, "Member image: " + imageUri);
		
		holder.memberName.setText(cursor.getString(cursor.getColumnIndex(DatabaseConstants.NAME)));
		/* Implement appropriate listeners for CV and contact.
			holder.memberCV.setOnClickListener(null);
			holder.memberEmail.setOnClickListener(null);
		*/
		ImageLoader.getInstance().displayImage(imageUri, holder.memberImage, options, animateFirstListener);
		
		/*
		holder.articleClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = mContext.getFragmentManager();
				fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
					.replace(R.id.container, ArticleFragment.newInstance(articleId))
					.addToBackStack(null)
					.commit();
			}
		};
		
		view.setOnClickListener(holder.articleClick);
		*/
	}
	
	private static class ViewHolder {
		ImageView memberImage;
		TextView memberName;
		TextView memberCV;
		TextView memberEmail;
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 800);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}