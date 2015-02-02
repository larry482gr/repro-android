package com.repro.android.utilities;

import android.net.ParseException;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class HTTPUtilities {
    private final static String TAG = HTTPUtilities.class.getCanonicalName();
	/**
	 * get response body.
	 *
	 * @param entity
	 *            the entity
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParseException
	 *             the parse exception
	 */
	public static String getResponseBody(final HttpEntity entity) throws IOException, ParseException {

		if (entity == null)
			throw new IllegalArgumentException("HTTP entity may not be null");

		InputStream instream = (InputStream) entity.getContent();

		if (instream == null)
			return "";

		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
		}

		String charset = getContentCharSet(entity);

		if (charset == null)
			charset = HTTP.DEFAULT_CONTENT_CHARSET;

		Reader reader = new InputStreamReader(instream, charset);
		StringBuilder buffer = new StringBuilder();

		try {
			char[] tmp = new char[1024];
			int l;

			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}

		return buffer.toString();
	}
	
	/**
	 * Gets the content char set.
	 *
	 * @param entity
	 *            the entity
	 * @return the content char set
	 * @throws ParseException
	 *             the parse exception
	 */
	public static String getContentCharSet(final HttpEntity entity)
			throws ParseException {

		if (entity == null)
			throw new IllegalArgumentException("HTTP entity may not be null");

		String charset = null;

		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}

		return charset;
	}
	
	public static String stripHtml(String html) {
	    return Html.fromHtml(html).toString();
	}

    public static void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
    }

    private static void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Log.d(TAG, span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }
}