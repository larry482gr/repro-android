package com.repro.android.utilities.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.ParseException;

public class HTTPUtilities {
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
}