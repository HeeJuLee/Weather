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
			// 设置?��?��从httpUrlConnection读入，默认情?��下是true; 
			conn.setDoInput(true);
			// 设置?��?��?��httpUrlConnection输出，因为这个是post请求，参?��要放?��   
			// http正文?��，因此�?��设为true, 默�?�情?��下是false; 
			conn.setDoOutput(true);
			// 设定请求?��?��法为"POST"，默认是GET 
			conn.setRequestMethod("POST");
			//设置超时
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(4000);
			// Post 请求不能使用缓存 
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			// 设定传�??��?��容类?��?��?��序列?��?��java对象   
			// (如果不�?��?�项,?��传�?�序?��?��对象�??当WEB?��?��默�?�的不是这种类型?��?��?��?��java.io.EOFException)  
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 连接，从上述�???��中url.openConnection()?��此的?���?必须要在connect之前完成�??
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
