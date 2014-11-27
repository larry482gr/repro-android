package com.repro.android;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.repro.android.asynctasks.MembersAsyncTask;
import com.repro.android.asynctasks.NewsAsyncTask;
import com.repro.android.dialogs.Dialogs;
import com.repro.android.fragments.NavigationDrawerFragment;
import com.repro.android.fragments.PlaceholderFragment;
import com.repro.android.utilities.Constants;
import com.repro.android.utilities.NetworkUtilities;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private Context mContext;

	private String TAG = "MainActivity";

	private CharSequence previousActionBarTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		mContext = this;
	}

	@Override
	public void onResume() {
		super.onResume();
		if(ReproAndroid.prefs.getBoolean(Constants.JUST_LAUNCHED, true)) {
			preloadContent();
			ReproAndroid.prefs.edit().putBoolean(Constants.JUST_LAUNCHED, false).commit();
		}
	}
	
	private void preloadContent() {
		if(NetworkUtilities.isConnectedToInternet(this)) {
			// Download Latest Articles
			NewsAsyncTask news = new NewsAsyncTask(this);
			news.execute(new String[] { "all" });
			
			// Download Members
			MembersAsyncTask members = new MembersAsyncTask(this);
			members.execute(new String[] { "all" });
		}
		else {
			final Handler mHandler = new Handler(Looper.getMainLooper());
			
			String title = getResources().getString(R.string.no_connection_msg);
			String[] options = new String[] { getResources().getString(R.string.enable_wifi), getResources().getString(R.string.enable_mobile) };
			OnClickListener connect = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final ProgressDialog enablingConnection = new ProgressDialog(mContext);
					if(which == 0) {
						NetworkUtilities.enableWifi(mContext);
						enablingConnection.setMessage(mContext.getResources().getString(R.string.enabling_wifi));
					}
					else if(which == 1) {
						NetworkUtilities.enableMobileData(mContext);
						enablingConnection.setMessage(mContext.getResources().getString(R.string.enabling_mobile));
					}
					enablingConnection.show();
					
					for(int i = 1; i < 6; i++) {
						mHandler.postDelayed(new Runnable() {
							public void run() {
								if(NetworkUtilities.isConnectedToInternet(mContext)) {
									enablingConnection.dismiss();
									preloadContent();
								}
						    }
						}, i*2000);
					}
					
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if(!NetworkUtilities.isConnectedToInternet(mContext)) {
								enablingConnection.dismiss();
								String toastMessage = mContext.getResources().getString(R.string.connection_failure) + "\n" + 
													mContext.getResources().getString(R.string.check_network);
								Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
							}
					    }
					}, 11000);
					
				}
			};
			Builder alertDialog = Dialogs.optionDialog(mContext, title, options, connect);
			alertDialog.create().show();
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
			.beginTransaction()
			.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
			.addToBackStack(null)
			.commit();
	}

	public void onSectionAttached(int number) {
		previousActionBarTitle = getActionBar().getTitle();
		String[] menu_items = getResources().getStringArray(R.array.menu_items);
		mTitle = menu_items[number-1];
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if(id == R.id.action_language) {
			showLanguageAlert();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showLanguageAlert() {
		String title = getResources().getString(R.string.lang_select);
		String[] options = getResources().getStringArray(R.array.language_items);
		OnClickListener connect = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Configuration config = getBaseContext().getResources().getConfiguration();
				Locale locale = null;
				String langId = null;
				
				if(which == 0) {
					locale = Locale.ENGLISH;
					langId = Constants.ENGLISH_ID;
				}
				else if(which == 1) {
					locale = Locale.getDefault();
					langId = Constants.GREEK_ID;
				}
				
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
				ReproAndroid.prefs.edit().putString(Constants.LOCALE, locale.toString()).commit();
				ReproAndroid.prefs.edit().putString(Constants.LANGUAGE_ID, langId).commit();
				
				recreate();
			}
		};
		Builder alertDialog = Dialogs.optionDialog(mContext, title, options, connect);
		alertDialog.create().show();
	}
}