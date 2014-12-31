package com.ncsoft.platform.weather.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.ncsoft.platform.weather.R;

public class CustomHttpClient {
	private static String TAG = "CustomHttpClient";
	private static final String CHARSET_UTF8 = HTTP.UTF_8;
	private static final String CHARSET_GB2312 = "GB2312";
	private static HttpClient customerHttpClient;

	private CustomHttpClient() {

	}

	/**
	 * HttpClient post?–¹æ³?
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public static String PostFromWebByHttpClient(Context context, String url,
			NameValuePair... nameValuePairs) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (nameValuePairs != null) {
				for (int i = 0; i < nameValuePairs.length; i++) {
					params.add(nameValuePairs[i]);
				}
			}
			UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,
					CHARSET_UTF8);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(urlEncoded);
			HttpClient client = getHttpClient(context);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("è¯·æ±‚å¤±è´¥");
			}
			HttpEntity resEntity = response.getEntity();
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			/*
			throw new RuntimeException(context.getResources().getString(
					R.string.httpError), e);
			*/
			Log.w(TAG, e.getMessage());
			return null;
		} 
	}

	public static String getFromWebByHttpClient(Context context, String url,
			NameValuePair... nameValuePairs) throws Exception{
		try {
			// http?œ°??
			// String httpUrl =
			// "http://192.168.1.110:8080/httpget.jsp?par=HttpClient_android_Get";
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			if (nameValuePairs != null && nameValuePairs.length > 0) {
				sb.append("?");
				for (int i = 0; i < nameValuePairs.length; i++) {
					if (i > 0) {
						sb.append("&");
					}
					sb.append(String.format("%s=%s",
							nameValuePairs[i].getName(),
							nameValuePairs[i].getValue()));
				}
			}
			Log.i(TAG, sb.toString());
			// HttpGetè¿æ¥å¯¹è±¡
			HttpGet httpRequest = new HttpGet(sb.toString());
			// ?–å¾—HttpClientå¯¹è±¡
			HttpClient httpclient = getHttpClient(context);
			// è¯·æ±‚HttpClientï¼Œå–å¾—HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// è¯·æ±‚?ˆ?ŠŸ
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				/*
				if (context instanceof MainActivity) {
					((MainActivity) context).showToast("?Š è½½å¤±è´¥ï¼Œä¸‹æ‹‰?ˆ·?–°è¯•è¯•ï¼?");
				}else if (context instanceof CitySelectionActivity) {
					((MainActivity) context).showToast("?Š è½½å¤±è´¥ï¼Œ?‡?–°?‰æ‹©è¯•è¯•ï¼?");
				}

				throw new RuntimeException(context.getResources().getString(
						R.string.httpError));
				 */
				return "-1";
			}
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new RuntimeException(context.getResources().getString(
//					R.string.httpError),e);
			return "-1";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new RuntimeException(context.getResources().getString(
//					R.string.httpError),e);
			return "-1";
		} 	
	}

	/**
	 * ?ˆ›å»ºhttpClientå®ä¾‹
	 * 
	 * @return
	 * @throws Exception
	 */
	private static synchronized HttpClient getHttpClient(Context context) {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// è®¾ç½®ï¿??ï¿½ï¿½?Ÿº?œ¬?‚?•°
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams
					.setUserAgent(
							params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
									+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// è¶…æ—¶è®¾ç½®
			/* ä»è¿?¥æ± ä¸­?–è¿æ¥?š„è¶…æ—¶?—¶?—´
			 * */
			ConnManagerParams.setTimeout(params, 1000);
			/* è¿æ¥è¶…æ—¶ */
			int ConnectionTimeOut = 3000;
			if (!HttpUtils.isWifiDataEnable(context)) {
				ConnectionTimeOut = 8000;
			}
			HttpConnectionParams
					.setConnectionTimeout(params, ConnectionTimeOut);
			/* è¯·æ±‚è¶…æ—¶ */
			HttpConnectionParams.setSoTimeout(params, 4000);
			// è®¾ç½®?ˆ‘ä»¬çš„HttpClient?”¯?ŒHTTP?’ŒHTTPSä¸¤ç§æ¨¡å¼
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// ä½¿ç”¨çº¿ç¨‹å®‰å…¨?š„è¿æ¥ç®¡ç†?¥?ˆ›å»ºHttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}
}
