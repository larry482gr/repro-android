package com.repro.android.entities;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Article {
	private static final String TAG = "Article";
	
	private long id;
	private String title;
	private String shortDesc;
	private String longDesc;
	private String sourceLabel;
	private String sourceLink;
	private String picture;
	private int langId;
	private String publishedAt;
	

	public Article(JSONObject jsonObject) {
		try {
			this.id = jsonObject.getLong("id");
			this.title = jsonObject.getString("title");
			this.shortDesc = jsonObject.getString("short_desc");
			this.longDesc = jsonObject.getString("long_desc");
			this.sourceLabel = jsonObject.getString("source_label");
			this.sourceLink = jsonObject.getString("source_link");
			this.picture = jsonObject.getString("picture");
			this.langId = jsonObject.getInt("lang_id");
			this.publishedAt = jsonObject.getString("published_at");
		} catch (JSONException e) {
			Log.d(TAG, e.getLocalizedMessage());
		}
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getShortDesc() {
		return shortDesc;
	}


	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}


	public String getLongDesc() {
		return longDesc;
	}


	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}


	public String getSourceLabel() {
		return sourceLabel;
	}


	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}


	public String getSourceLink() {
		return sourceLink;
	}


	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}


	public String getPicture() {
		return picture;
	}


	public void setPicture(String picture) {
		this.picture = picture;
	}


	public int getLangId() {
		return langId;
	}


	public void setLangId(int langId) {
		this.langId = langId;
	}


	public String getPublishedAt() {
		return publishedAt;
	}


	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}
}
