package com.repro.android.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.repro.android.R;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.fragments.MemberFragment;
import com.repro.android.utilities.ImageUtilities;

public class MembersAdapter extends CursorAdapter {
	private static final String TAG = "MembersAdapter";
	private Activity mContext;
	private LayoutInflater inflater;
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
		
		view.setTag(holder);
		
		return view;
	}

	@Override
	public void bindView(final View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		
		final int memberId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants._ID));
		String image = cursor.getString(cursor.getColumnIndex(DatabaseConstants.PICTURE));
		String imageUri = mContext.getResources().getString(R.string.members_images) + image;
		
		holder.memberName.setText(cursor.getString(cursor.getColumnIndex(DatabaseConstants.NAME)));
		ImageLoader.getInstance().displayImage(imageUri, holder.memberImage, options, ImageUtilities.getAnimateFirstDisplayListenerInstance());
		
		holder.memberClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = mContext.getFragmentManager();
				fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, R.animator.slide_in_right, R.animator.slide_out_left)
					.replace(R.id.container, MemberFragment.newInstance(memberId))
					.addToBackStack(null)
					.commit();
			}
		};
		
		view.setOnClickListener(holder.memberClick);
	}
	
	private static class ViewHolder {
		ImageView memberImage;
		TextView memberName;
		OnClickListener memberClick;
	}
}