package com.repro.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.repro.android.adapters.NewsAdapter;

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
		initView(getView(), tabId);
	}

	private int getFragment(int tabId) {
		int fragmentId = -1;
		switch(tabId) {
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
			case 3:
				initNewsFragment(rootView);
				break;
			default:
				initMainFragment(rootView, tabId);
				break;
		}
	}

	private void initMainFragment(View rootView, int tabId) {
		TextView fragment_text = (TextView) rootView.findViewById(R.id.section_label);
		String[] menuItems = getResources().getStringArray(R.array.menu_items);
		fragment_text.setText(menuItems[tabId-1]);
	}

	private void initNewsFragment(View rootView) {
		ListView newsList = (ListView) rootView.findViewById(R.id.news_list);
		NewsAdapter mAdapter = new NewsAdapter(this.getActivity(), R.layout.article_item, MainActivity.articles);
		newsList.setAdapter(mAdapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
}