package com.ncsoft.platform.weather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.NameValuePair;

import android.util.Log;

public class CustomHttpURLConnection {
	private static String TAG = "CustomHttpUrlConnection";
	private static HttpURLConnection conn;
	
	public CustomHttpURLConnection() {
	}

	public static String GetFromWebByHttpUrlConnection(String strUrl,
			NameValuePair... nameValuePairs) {
		String result="";
		try {
			URL url = new URL(strUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(4000);
			conn.setRequestProperty("accept", "*/*");
//			int resCode=conn.getResponseCode();
			conn.connect();
			InputStream stream=conn.getInputStream();
			InputStreamReader inReader=new InputStreamReader(stream);
			BufferedReader buffer=new BufferedReader(inReader);
			String strLine=null;
			while((strLine=buffer.readLine())!=null)
			{
				result+=strLine;
			}
			inReader.close();
			conn.disconnect();
			return result;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "getFromWebByHttpUrlCOnnection:"+e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "getFromWebByHttpUrlCOnnection:"+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static String PostFromWebByHttpURLConnection(String strUrl,
			NameValuePair... nameValuePairs) {
		String result="";
		try {
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url
					.openConnection();
			// è®¾ç½®?˜¯?¦ä»httpUrlConnectionè¯»å…¥ï¼Œé»˜è®¤æƒ…?†µä¸‹æ˜¯true; 
			conn.setDoInput(true);
			// è®¾ç½®?˜¯?¦?‘httpUrlConnectionè¾“å‡ºï¼Œå› ä¸ºè¿™ä¸ªæ˜¯postè¯·æ±‚ï¼Œå‚?•°è¦æ”¾?œ¨   
			// httpæ­£æ–‡?†…ï¼Œå› æ­¤ï¿½?ï¿½ï¿½è®¾ä¸ºtrue, é»˜è?¤æƒ…?†µä¸‹æ˜¯false; 
			conn.setDoOutput(true);
			// è®¾å®šè¯·æ±‚?š„?–¹æ³•ä¸º"POST"ï¼Œé»˜è®¤æ˜¯GET 
			conn.setRequestMethod("POST");
			//è®¾ç½®è¶…æ—¶
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(4000);
			// Post è¯·æ±‚ä¸èƒ½ä½¿ç”¨ç¼“å­˜ 
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			// è®¾å®šä¼ ï¿½??š„?†…å®¹ç±»?‹?˜¯?¯åºåˆ—?Œ–?š„javaå¯¹è±¡   
			// (å¦‚æœä¸è?¾æ?¤é¡¹,?œ¨ä¼ é?åº?ˆ—?Œ–å¯¹è±¡ï¿??å½“WEB?œ?Š¡é»˜è?¤çš„ä¸æ˜¯è¿™ç§ç±»å‹?—¶?¯?ƒ½?Š›java.io.EOFException)  
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// è¿æ¥ï¼Œä»ä¸Šè¿°ï¿???¡ä¸­url.openConnection()?‡³æ­¤çš„?…ç½?å¿…é¡»è¦åœ¨connectä¹‹å‰å®Œæˆï¿??
//			urlConn.connect();
			
			InputStream in = conn.getInputStream();
			InputStreamReader inStream=new InputStreamReader(in);
			BufferedReader buffer=new BufferedReader(inStream);
			String strLine=null;
			while((strLine=buffer.readLine())!=null)
			{
				result+=strLine;
			}
			return result;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
