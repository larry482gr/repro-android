package com.repro.android.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetworkUtilities {
	private static final String TAG = "NetworkUtilities";

	/**
	 * Checks if is connected to internet.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if is connected to internet
	 */
	public static boolean isConnectedToInternet(Context mContext) {
		return (isWifiConnected(mContext) || isMobileDataConnected(mContext));
	}
	
	/**
	 * Enable wifi.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if successful
	 */
	public static boolean enableWifi(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE); 
		return wifiManager.setWifiEnabled(true);
	}
	
	/**
	 * Disable wifi.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if successful
	 */
	public static boolean disableWifi(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE); 
		return wifiManager.setWifiEnabled(false);
	}
	
	/**
	 * Enable mobile data.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if successful
	 */
	public static boolean enableMobileData(Context mContext) {
		boolean result = false;
		ConnectivityManager dataManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Method dataMtd = null;
		
		try {
			dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
		} catch (NoSuchMethodException e) {
			Log.d(TAG, e.getMessage());
		}
		
		dataMtd.setAccessible(true);
		
		try {
			result = dataMtd.invoke(dataManager, true) != null;
		} catch (IllegalAccessException e) {
			Log.d(TAG, e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.d(TAG, e.getMessage());
		} catch (InvocationTargetException e) {
			Log.d(TAG, e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Disable mobile data.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if successful
	 */
	public static boolean disableMobileData(Context mContext) {
		boolean result = false;
		ConnectivityManager dataManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		Method dataMtd = null;
		
		try {
			dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
		} catch (NoSuchMethodException e) {
			Log.d(TAG, e.getMessage());
		}
		
		dataMtd.setAccessible(true);
		
		try {
			result = dataMtd.invoke(dataManager, false) != null;
		} catch (IllegalAccessException e) {
			Log.d(TAG, e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.d(TAG, e.getMessage());
		} catch (InvocationTargetException e) {
			Log.d(TAG, e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Checks if is wifi connected.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if is wifi connected
	 */
	public static boolean isWifiConnected(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if is mobile data connected.
	 *
	 * @param mContext
	 *            the m context
	 * @return true, if is mobile data connected
	 */
	public static boolean isMobileDataConnected(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}
		return false;
	}
}
