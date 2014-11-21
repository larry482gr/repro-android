package com.repro.android;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.repro.android.adapters.NewsAdapter;
import com.repro.android.entities.ArticlesModel;
import com.repro.android.entities.StaticContent;
import com.repro.android.utilities.HtmlTagHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	private static int tabId;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PlaceholderFragment newInstance(int sectionNumber) {
		PlaceholderFragment fragment = new PlaceholderFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tabId = getArguments().getInt(ARG_SECTION_NUMBER);
		int fragment = getFragment(tabId);
		View rootView = inflater.inflate(fragment, container, false);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initView(getView(), tabId);
	}

	private int getFragment(int tabId) {
		int fragmentId = -1;
		switch(tabId) {
			case 2:
				fragmentId = R.layout.fragment_research_program;
				break;
			case 3:
				fragmentId = R.layout.fragment_news_list;
				break;
			default:
				fragmentId = R.layout.fragment_main;
				break;
		}
		
		return fragmentId;
	}
	
	private void initView(View rootView, int tabId) {
		switch(tabId) {
			case 2:
				initResearchProgramFragment(rootView);
				break;
			case 3:
				initNewsFragment(rootView);
				break;
			default:
				initMainFragment(rootView, tabId);
				break;
		}
	}

	private void initResearchProgramFragment(View rootView) {
		Configuration config = rootView.getContext().getResources().getConfiguration();
        Locale locale = config.locale;
        
		TextView researchProgram = (TextView) rootView.findViewById(R.id.research_program);
		HtmlTagHandler htmlTagHandler = new HtmlTagHandler();
		
		if(locale.toString().equals("el_GR")) {
			// researchProgram.loadDataWithBaseURL(null, StaticContent.RESEARCH_PROGRAM_TITLE_GR+StaticContent.RESEARCH_PROGRAM_CONTENT_GR, "text/html","utf-8", null);
			researchProgram.setText(Html.fromHtml(StaticContent.RESEARCH_PROGRAM_TITLE_GR + StaticContent.RESEARCH_PROGRAM_CONTENT_GR, null, htmlTagHandler));
		}
		else {
			// researchProgram.loadDataWithBaseURL(null, StaticContent.RESEARCH_PROGRAM_TITLE_EN+StaticContent.RESEARCH_PROGRAM_CONTENT_EN, "text/html","utf-8", null);
			researchProgram.setText(Html.fromHtml(StaticContent.RESEARCH_PROGRAM_TITLE_EN + StaticContent.RESEARCH_PROGRAM_CONTENT_EN, null, htmlTagHandler));
		}
		
        // Toast.makeText(getActivity(), locale.toString(), Toast.LENGTH_LONG).show();
		
	}
	
	private void initNewsFragment(final View rootView) {
		Handler mHandler = new Handler(Looper.getMainLooper());
		
		mHandler.postDelayed(new Runnable(){
			public void run() {
				ListView newsList = (ListView) rootView.findViewById(R.id.news_list);
				ArticlesModel articlesModel = new ArticlesModel(getActivity());
				Cursor articlesCursor = articlesModel.findArticles();
				NewsAdapter mAdapter = new NewsAdapter(getActivity(), articlesCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
				newsList.setAdapter(mAdapter);
		    }
		}, 400);
	}

	private void initMainFragment(View rootView, int tabId) {
		TextView fragment_text = (TextView) rootView.findViewById(R.id.section_label);
		String[] menuItems = getResources().getStringArray(R.array.menu_items);
		fragment_text.setText(menuItems[tabId-1]);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
}