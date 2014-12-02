package com.repro.android;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.repro.android.R;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.entities.MembersModel;
import com.repro.android.utilities.ImageUtilities;

public class MemberFragment extends Fragment {
	private final String TAG = "MemberFragment";
	private static final String MEMBER_ID = "member_id";
	private MembersModel membersModel;
	private DisplayImageOptions options;
	
	public static MemberFragment newInstance(int memberId) {
		MemberFragment fragment = new MemberFragment();
		Bundle args = new Bundle();
		args.putInt(MEMBER_ID, memberId);
		fragment.setArguments(args);
		return fragment;
	}
	
	public MemberFragment() {
		membersModel = new MembersModel(getActivity());
		
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.member_layout, container, false);
        
        ImageView memberImage = (ImageView) view.findViewById(R.id.member_layout_image);
        TextView memberName = (TextView) view.findViewById(R.id.member_layout_name);
        TextView memberContact = (TextView) view.findViewById(R.id.member_layout_contact);
        TextView memberCV = (TextView) view.findViewById(R.id.member_layout_cv);
        
		int memberId = getArguments().getInt(MEMBER_ID);
        Cursor memberCursor = membersModel.findMember(memberId);
        memberCursor.moveToFirst();
        
        memberName.setText(memberCursor.getString(memberCursor.getColumnIndex(DatabaseConstants.NAME)));
        memberContact.setText(Html.fromHtml("<a href='mailto:" + memberCursor.getString(memberCursor.getColumnIndex(DatabaseConstants.EMAIL)) + "'>" + 
        									getResources().getString(R.string.member_contact) + "</a>", null, null));
        final String emailAddress = memberCursor.getString(memberCursor.getColumnIndex(DatabaseConstants.EMAIL));
        memberContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri data = Uri.parse("mailto:" + emailAddress);
				intent.setData(data);
				startActivity(intent);
			}
		});
        memberCV.setText(memberCursor.getString(memberCursor.getColumnIndex(DatabaseConstants.CV)));
        
        String image = memberCursor.getString(memberCursor.getColumnIndex(DatabaseConstants.PICTURE));
		String imageUri = getActivity().getResources().getString(R.string.members_images) + image;
        ImageLoader.getInstance().displayImage(imageUri, memberImage, options, ImageUtilities.getAnimateFirstDisplayListenerInstance());
        
		return view;
    }
}