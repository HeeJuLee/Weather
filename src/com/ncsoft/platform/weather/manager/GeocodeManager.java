package com.ncsoft.platform.weather.manager;

import java.util.List;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class GeocodeManager {

	private static final String TAG = "GeocodeManager";
	
	private double mLatitude;
	private double mLongitude;
	
	public double getLatitude() {
		return mLatitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void getLocationInfo(String address) {

		// 구글 지오코딩 API - 주소로 위경도를 읽어와서 JSON 파싱
		StringBuilder sb = new StringBuilder();
		sb.append("http://maps.google.com/maps/api/geocode/json?sensor=true&language=ko&address=")
			.append(address.replace(" ", "%20"));

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(sb.toString());
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String result = client.execute(httpget, responseHandler);
			
	        Log.d(TAG, "\n\n=== Google Geocode - setLocationInfo");
            Log.d(TAG, result);
            
			JSONObject jsonObject = new JSONObject(result);			
			mLatitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
			mLongitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location").getDouble("lng");			
			
            Log.d(TAG, "address: " + address + ", latitde: " + mLatitude + ", longtitude: " + mLongitude);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
	
	private void searchLocationGoogle(String address) {
		
		try {
			HttpClient Client = new DefaultHttpClient();
				
    		StringBuilder sb = new StringBuilder();
    		sb.append("http://maps.google.com/maps/api/geocode/json?sensor=true&language=ko&address=").append(address);
    		
            HttpGet httpget = new HttpGet(sb.toString());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String SetServerString = Client.execute(httpget, responseHandler);
            
            Log.d(TAG, "\n========================");
            Log.d(TAG, "\nGoogle Geocode-2");
            Log.d(TAG, SetServerString);
            
		} catch(Exception e) {
		    e.printStackTrace();
		}
	}
	
	private void searchLocation(Context context, String searchStr) {
		Geocoder gc = new Geocoder(context, Locale.getDefault());
		
		List<Address> list = null;
		
		try {
			list = gc.getFromLocationName(searchStr, 10);
			if(list != null) {
				for(int i = 0; i < list.size(); i++) {
					Address address = list.get(i);
					Log.d(TAG, "\n주소: " + address.getAddressLine(i));
					Log.d(TAG, "\n위도: " + address.getLatitude());
					Log.d(TAG, "\n경도: " + address.getLongitude());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void searchLocation(Context context, double latitude, double longitude) {
		Geocoder gc = new Geocoder(context, Locale.getDefault());
		
		List<Address> list = null;
		
		try {
			list = gc.getFromLocation(latitude, longitude, 10);
			if(list != null) {
				for(int i = 0; i < list.size(); i++) {
					Address address = list.get(i);
					Log.d(TAG, "\n주소: " + address.getAddressLine(i));
					Log.d(TAG, "\n위도: " + address.getLatitude());
					Log.d(TAG, "\n경도: " + address.getLongitude());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
