package com.repro.android.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.repro.android.R;
import com.repro.android.entities.Article;

public class NewsAdapter extends ArrayAdapter<Article> {
	private static final String TAG = "NewsAdapter";
	private ArrayList<Article> articles;
	private LayoutInflater inflater;

	public NewsAdapter(Context context, int resource, ArrayList<Article> articles) {
		super(context, resource, articles);
		this.articles = articles;
		this.inflater = LayoutInflater.from(context);
		Log.d(TAG, "Total Articles: " + articles.size());
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.article_item, parent, false);
		}
		
		Article article = getItem(position);
		
		/*
		 *	ImageView articleImage = convertView.findViewById(R.id.article_image);
		 *	// TODO Set article image.
		 */
		
		TextView articleTitle = (TextView) convertView.findViewById(R.id.article_title);
		Log.d(TAG, article.getTitle());
		articleTitle.setText(article.getTitle());
		
		TextView articleShortDesc = (TextView) convertView.findViewById(R.id.article_short_desc);
		Log.d(TAG, article.getShortDesc());
		articleShortDesc.setText(article.getShortDesc());
		
		return convertView;
	}
}