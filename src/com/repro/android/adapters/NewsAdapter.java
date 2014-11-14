package com.repro.android.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.repro.android.R;
import com.repro.android.entities.Article;
import com.repro.android.utilities.HTTPUtilities;

public class NewsAdapter extends ArrayAdapter<Article> {
	private static final String TAG = "NewsAdapter";
	private Context mContext;
	private ArrayList<Article> articles;
	private LayoutInflater inflater;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;

	public NewsAdapter(Context context, int resource, ArrayList<Article> articles) {
		super(context, resource, articles);
		this.mContext = context;
		this.articles = articles;
		this.inflater = LayoutInflater.from(context);
		Log.d(TAG, "Total Articles: " + articles.size());
		
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
	public int getCount() {
		return articles.size();
	}

	@Override
	public Article getItem(int arg0) {
		return articles.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.article_item, parent, false);
			holder = new ViewHolder();
			holder.articleImage = (ImageView) view.findViewById(R.id.article_image);
			holder.articleTitle = (TextView) view.findViewById(R.id.article_title);
			holder.articleShortDesc = (TextView) view.findViewById(R.id.article_short_desc);
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}
		
		Article article = getItem(position);
		String image = article.getPicture();
		String imageUri = mContext.getResources().getString(R.string.news_images) + image;
		Log.i(TAG, "Article title: " + article.getTitle());
		Log.i(TAG, "Article image: " + imageUri);
		
		holder.articleTitle.setText(article.getTitle());
		holder.articleShortDesc.setText(HTTPUtilities.stripHtml(article.getLongDesc()));
		ImageLoader.getInstance().displayImage(imageUri, holder.articleImage, options, animateFirstListener);
		return view;
	}
	
	private static class ViewHolder {
		ImageView articleImage;
		TextView articleTitle;
		TextView articleShortDesc;
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