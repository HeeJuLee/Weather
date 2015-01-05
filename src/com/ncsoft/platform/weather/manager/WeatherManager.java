package com.ncsoft.platform.weather.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;
import com.ncsoft.platform.weather.model.Forecast6DayModel;
import com.ncsoft.platform.weather.util.FileUtils;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.ResponseMessage;

public class WeatherManager {
	private static final String TAG = "WeatherDataManager";
	private static WeatherManager mWeatherManager;
	
	private Context mContext;
	private ArrayList<CurrentWeatherModel> mCurrentWeatherList;

	private WeatherManager(Context context) {
		mContext = context;
	}
	
	public static WeatherManager getInstance(Context context) {
		if(mWeatherManager == null) {
        	mWeatherManager = new WeatherManager(context);
        	
        	APIRequest.setAppKey("12f6c0f5-f4f9-3268-af97-f3c9a65a7f36");
        }
		
        return mWeatherManager;
    }
	
	public ArrayList<CurrentWeatherModel> getCurrentWeatherList() {
		if(mCurrentWeatherList == null)
			loadCurrentWeatherList();
		
		return mCurrentWeatherList;
	}
	
	public ArrayList<CurrentWeatherModel> getCurrentWeatherList(boolean reload) {
		if(mCurrentWeatherList == null || reload == true)
			loadCurrentWeatherList();
		
		return mCurrentWeatherList;
	}
	
	private void loadCurrentWeatherList() {
		mCurrentWeatherList = new ArrayList<CurrentWeatherModel>();
		
		String[] address = {"용인시 마북동", "분당구 삼평동", "전주시 진북동", "강남구 신사동" };
		
		GeocodeManager gm = new GeocodeManager();
		
		for(int i = 0; i < address.length; i++) {
			gm.getLocationInfo(address[i]);
			
			CurrentWeatherModel current = getCurrentWeather(gm.getLatitude(), gm.getLongitude());
			mCurrentWeatherList.add(current);
			
			Log.d(TAG, current.toString());
		}
	}
	
	private CurrentWeatherModel getCurrentWeather(double latitude, double longitude) {
		try {
			String URL = "http://apis.skplanetx.com/weather/current/minutely";
							 
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("version", "1");
			param.put("lat", latitude);
			param.put("lon", longitude);
			 
			RequestBundle req = new RequestBundle();
			req.setUrl(URL);
			req.setParameters(param);
			req.setResponseType(CONTENT_TYPE.JSON);
			 
			APIRequest api = new APIRequest();
		    ResponseMessage result = api.request(req);
		    
		    if(result.getStatusCode().equalsIgnoreCase("200")) {
		    	Gson gson = new Gson(); 
				
		    	return gson.fromJson(result.toString(), CurrentWeatherModel.class);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Forecast6DayModel getForecast6Days(double latitude, double longitude) {
		try {
			String URL = "http://apis.skplanetx.com/weather/forecast/6days";
							 
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("version", "1");
			param.put("lat", latitude);
			param.put("lon", longitude);
			 
			RequestBundle req = new RequestBundle();
			req.setUrl(URL);
			req.setParameters(param);
			req.setResponseType(CONTENT_TYPE.JSON);
			 
			APIRequest api = new APIRequest();
		    ResponseMessage result = api.request(req);
		    
		    if(result.getStatusCode().equalsIgnoreCase("200")) {
		    	Gson gson = new Gson(); 
				
		    	return gson.fromJson(result.toString(), Forecast6DayModel.class);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	private CurrentWeatherModel getCurrentWeatherFromJson() {
		Gson gson = new Gson(); 
		
		String result = FileUtils.getFileFromAssets(mContext, "current_weather_response.json");
		
		return gson.fromJson(result, CurrentWeatherModel.class);
	}
	
	
	private Forecast6DayModel getForecastWeekFromJson() {
		Gson gson = new Gson(); 
		
		String result = FileUtils.getFileFromAssets(mContext, "forecast_week_response.json");
		
		return gson.fromJson(result, Forecast6DayModel.class);
	}

	
	
}
