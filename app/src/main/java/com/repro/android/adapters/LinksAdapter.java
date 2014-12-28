package com.repro.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.repro.android.R;
import com.repro.android.database.DatabaseConstants;

public class LinksAdapter extends CursorAdapter {
    private static final String TAG = LinksAdapter.class.getCanonicalName();
    private Activity mContext;
    private LayoutInflater inflater;
    private static int headerHeight;

    public LinksAdapter(Context context, Cursor linksCursor, int flags) {
        super(context, linksCursor, flags);
        this.mContext = (Activity) context;
        this.inflater = LayoutInflater.from(context);
        Log.d(TAG, "Total Links: " + linksCursor.getCount());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder;
        View view;

        view = inflater.inflate(R.layout.link_item, parent, false);
        holder = new ViewHolder();
        holder.linkHeader = (TextView) view.findViewById(R.id.link_header);
        holder.linkLabel = (TextView) view.findViewById(R.id.link_label);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        // final int linkId = cursor.getInt(cursor.getColumnIndex(DatabaseConstants._ID));
        final String header = cursor.getString(cursor.getColumnIndex(DatabaseConstants.HEADER));
        if(header.trim().length() > 0) {
            holder.linkHeader.setVisibility(View.VISIBLE);
            holder.linkHeader.setText(header);
        }
        else {
            holder.linkHeader.setVisibility(View.GONE);
        }

        final String prependText = cursor.getString(cursor.getColumnIndex(DatabaseConstants.PREPEND_TEXT));
        final String appendText = cursor.getString(cursor.getColumnIndex(DatabaseConstants.APPEND_TEXT));
        final String linkLabel = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LINK_LABEL));
        final String linkUrl = cursor.getString(cursor.getColumnIndex(DatabaseConstants.LINK_URL));
        final boolean isGroup = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.IS_GROUP)) == 1 ? true : false;

        String label;
        if(prependText.trim().length() > 0) {
            label = prependText + "&nbsp;";
            if(isGroup) {
                label += "<br/>";
            }
        }
        else {
            label = "";
        }

        final String link = "<a href \"" + linkUrl + "\">" + linkLabel + "</a>";
        label += isGroup ? "&nbsp;&nbsp;&nbsp;&nbsp;" + link + "&nbsp;" : link + "&nbsp;";
        label += appendText;

        holder.linkLabel.setText(Html.fromHtml(label));

        if(linkUrl.length() > 0) {
            setLink(holder, linkUrl);
        }
    }

    private static class ViewHolder {
        TextView linkHeader;
        TextView linkLabel;
        View.OnClickListener linkClick;
    }

    private void setLink(ViewHolder viewHolder, final String linkUrl) {
        viewHolder.linkClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
                mContext.startActivity(intent);
            }
        };

        viewHolder.linkLabel.setOnClickListener(viewHolder.linkClick);
    }
}