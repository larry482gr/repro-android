package com.repro.android;

import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.repro.android.utilities.Constants;

public class ReproAndroid extends Application {
	private final String TAG = "ReproAndroid";
	public static SharedPreferences prefs;
	
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String language = prefs.getString(Constants.LOCALE, Constants.DEFAULT_LOCALE);
		Configuration config = getBaseContext().getResources().getConfiguration();
		Locale locale = null;
		if(language.equals("en")) {
			locale = Locale.ENGLISH;
			prefs.edit().putString(Constants.LANGUAGE_ID, Constants.ENGLISH_ID).commit();
		}
		else {
			locale = Locale.getDefault();
			prefs.edit().putString(Constants.LANGUAGE_ID, Constants.GREEK_ID).commit();
		}
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			.diskCacheSize(50 * 1024 * 1024) // 50 Mb
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.writeDebugLogs() // Remove for release app
			.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
