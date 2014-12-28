package com.repro.android;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.repro.android.adapters.LinksAdapter;
import com.repro.android.database.DatabaseConstants;
import com.repro.android.entities.LinksModel;

public class LinksFragment extends Fragment {
    private final String TAG = LinksFragment.class.getCanonicalName();
    private static final String CATEGORY_ID = "category_id";
    private LinksModel linksModel;

    public static LinksFragment newInstance(int categoryId) {
        LinksFragment fragment = new LinksFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    public LinksFragment() {
        linksModel = new LinksModel(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_links, container, false);

        TextView categoryLabel = (TextView) view.findViewById(R.id.category_label);
        ListView linksList = (ListView) view.findViewById(R.id.links_list);

        int categoryId = getArguments().getInt(CATEGORY_ID);
        Cursor categoryCursor = linksModel.findLinkCategory(categoryId);
        Cursor linksCursor = linksModel.findAllLinks(categoryCursor);
        categoryCursor.moveToFirst();
        linksCursor.moveToFirst();

        if(categoryCursor.moveToFirst()) {
            categoryLabel.setText(categoryCursor.getString(categoryCursor.getColumnIndex(DatabaseConstants.LABEL)));
        }

        LinksAdapter mAdapter = new LinksAdapter(getActivity(), linksCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        linksList.setAdapter(mAdapter);

        return view;
    }
}