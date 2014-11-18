package com.repro.android;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.entities.ArticlesModel;
import com.repro.android.utilities.HtmlTagHandler;

public class ArticleFragment extends Fragment {
	private final String TAG = "ArticleFragment";
	private static final String ARTICLE_ID = "article_id";
	private ArticlesModel articlesModel;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	private HtmlTagHandler htmlTagHandler = new HtmlTagHandler();
	
	public static ArticleFragment newInstance(int articleId) {
		ArticleFragment fragment = new ArticleFragment();
		Bundle args = new Bundle();
		args.putInt(ARTICLE_ID, articleId);
		fragment.setArguments(args);
		return fragment;
	}
	
	public ArticleFragment() {
		articlesModel = new ArticlesModel(getActivity());
		
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
        final View view = inflater.inflate(R.layout.article_layout, container, false);
        
        ImageView articleImage = (ImageView) view.findViewById(R.id.article_layout_image);
        TextView articleTitle = (TextView) view.findViewById(R.id.article_layout_title);
        TextView articleShortDesc = (TextView) view.findViewById(R.id.article_layout_short_desc);
        TextView articleLongDesc = (TextView) view.findViewById(R.id.article_layout_long_desc);
        
		int articleId = getArguments().getInt(ARTICLE_ID);
        Cursor articleCursor = articlesModel.findArticle(articleId);
        articleCursor.moveToFirst();
        
        articleTitle.setText(articleCursor.getString(articleCursor.getColumnIndex(DatabaseConstants.TITLE)));
        articleShortDesc.setText(Html.fromHtml(articleCursor.getString(articleCursor.getColumnIndex(DatabaseConstants.SHORT_DESC)) + "<br/>", null, htmlTagHandler));
        articleLongDesc.setText(Html.fromHtml(articleCursor.getString(articleCursor.getColumnIndex(DatabaseConstants.LONG_DESC)), null, htmlTagHandler));
        
        String image = articleCursor.getString(articleCursor.getColumnIndex(DatabaseConstants.PICTURE));
		String imageUri = getActivity().getResources().getString(R.string.news_images) + image;
        ImageLoader.getInstance().displayImage(imageUri, articleImage, options, animateFirstListener);
        
		return view;
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