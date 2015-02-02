/**
 * @author Lazaros Kazantzis
 * @version 1.1
 */
package com.repro.android.database;

import android.content.Context;

/**
 * The Class DatabaseSingleton.
 */
public class DatabaseSingleton {
	
	/** The instance. */
	private static DatabaseHandler instance;
	
	/**
	 * Gets the single instance of DatabaseSingleton.
	 *
	 * @param context
	 *            the context
	 * @return single instance of DatabaseSingleton
	 */
	public static DatabaseHandler getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseHandler(context);
		}
		return instance;
	}

}
