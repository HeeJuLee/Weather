package com.ncsoft.platform.weather.util;

import java.io.UnsupportedEncodingException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtils {
	// ç½‘ç»œè¿žæŽ¥?ƒ¨?ˆ†
	public static String postByHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs) {
		return CustomHttpURLConnection.PostFromWebByHttpURLConnection(strUrl,
				nameValuePairs);
	}

	public static String getByHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs) {
		return CustomHttpURLConnection.GetFromWebByHttpUrlConnection(strUrl,
				nameValuePairs);
	}

	public static String postByHttpClient(Context context,String strUrl,
			NameValuePair... nameValuePairs) {
		return CustomHttpClient.PostFromWebByHttpClient(context,strUrl, nameValuePairs);
	}

	public static String getByHttpClient(Context context,String strUrl,
			NameValuePair... nameValuePairs) throws Exception {
		if (!isNetworkConnAvail(context)) {
			return "-1";
		}
		return CustomHttpClient.getFromWebByHttpClient(context,strUrl, nameValuePairs);
	}

//	 ------------------------------------------------------------------------------------------
//	 ç½‘ç»œè¿žæŽ¥?ˆ¤?–­
//	 ?ˆ¤?–­?˜¯?¦?œ‰ç½‘ï¿½?
//	public static boolean isNetworkAvailable(Context context) {
//		return NetWorkHelper.isNetworkAvailable(context);
//	}

	public static boolean isNetworkConnAvail(Context context) {

	    ConnectivityManager connMgr = (ConnectivityManager) 
	        context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null)
	        return networkInfo.isConnected();

	    return false;
	}
	// ?ˆ¤?–­mobileç½‘ç»œ?˜¯?¦?¯?”¨
	public static boolean isMobileDataEnable(Context context) {
		String TAG = "httpUtils.isMobileDataEnable()";
		try {
			return NetWorkHelper.isMobileDataEnable(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// ?ˆ¤?–­wifiç½‘ç»œ?˜¯?¦?¯?”¨
	public static boolean isWifiDataEnable(Context context) {
		String TAG = "httpUtils.isWifiDataEnable()";
		try {
			return NetWorkHelper.isWifiDataEnable(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	/**
     * ç¼–ç æµ‹è¯•
     */
    public static void testCharset(String datastr) {
        try {
            String temp = new String(datastr.getBytes(), "GBK");
            Log.v("TestCharset", "****** getBytes() -> GBK ******/n" + temp);
            temp = new String(datastr.getBytes("GBK"), "UTF-8");
            Log.v("TestCharset", "****** GBK -> UTF-8 *******/n" + temp);
            temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
            Log.v("TestCharset", "****** GBK -> ISO-8859-1 *******/n" + temp);
            temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
            Log.v("TestCharset", "****** ISO-8859-1 -> UTF-8 *******/n" + temp);
            temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
            Log.v("TestCharset", "****** ISO-8859-1 -> GBK *******/n" + temp);
            temp = new String(datastr.getBytes("UTF-8"), "GBK");
            Log.v("TestCharset", "****** UTF-8 -> GBK *******/n" + temp);
            temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
            Log.v("TestCharset", "****** UTF-8 -> ISO-8859-1 *******/n" + temp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
