package com.ncsoft.platform.weather.manager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;
import com.ncsoft.platform.weather.model.Forecast3DayModel;
import com.ncsoft.platform.weather.model.Forecast6DayModel;
import com.ncsoft.platform.weather.util.FileUtils;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.ResponseMessage;

public class WeatherManager {
	private static final String TAG = "WeatherDataManager";
	private static final String CURRENT_WEATHER_API = "http://apis.skplanetx.com/weather/current/minutely";
	private static final String FORECAST_3DAY_API = "http://apis.skplanetx.com/weather/forecast/3days";
	private static final String FORECAST_6DAY_API = "http://apis.skplanetx.com/weather/forecast/6days";
	
	private static WeatherManager mWeatherManager;
	
	private Context mContext;
	private ArrayList<CurrentWeatherModel> mCurrentWeatherList;

	private String[] mAddress = {"용인시 마북동", "분당구 삼평동", "전주시 진북동", "강남구 신사동" };
	
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
				
		GeocodeManager gm = new GeocodeManager();
		
		for(int i = 0; i < mAddress.length; i++) {
			gm.getLocationInfo(mAddress[i]);
			
			CurrentWeatherModel current = getWeatherFromSKPlanet(CURRENT_WEATHER_API, gm.getLatitude(), gm.getLongitude(), CurrentWeatherModel.class);
			mCurrentWeatherList.add(current);
			
			Log.d(TAG, current.toString());
		}
	}
	
	public Forecast3DayModel getForecast3DayWeather(double latitude, double longitude) {
		
		Forecast3DayModel forecast = getWeatherFromSKPlanet(FORECAST_3DAY_API, latitude, longitude, Forecast3DayModel.class);
			
		Log.d(TAG, forecast.toString());
		
		return forecast;
	}	
	
	public Forecast6DayModel getForecast6DayWeather(double latitude, double longitude) {
			
		Forecast6DayModel forecast = getWeatherFromSKPlanet(FORECAST_6DAY_API, latitude, longitude, Forecast6DayModel.class);
			
		Log.d(TAG, forecast.toString());
		
		return forecast;
	}
	
	private <T> T getWeatherFromSKPlanet(String url, double latitude, double longitude, Type typeOfT) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("version", "1");
			param.put("lat", latitude);
			param.put("lon", longitude);
			 
			RequestBundle req = new RequestBundle();
			req.setUrl(url);
			req.setParameters(param);
			req.setResponseType(CONTENT_TYPE.JSON);
			 
			APIRequest api = new APIRequest();
		    ResponseMessage result = api.request(req);
		    
		    if(result.getStatusCode().equalsIgnoreCase("200")) {
		    	Gson gson = new Gson(); 
				
		    	return gson.fromJson(result.toString(), typeOfT);
		    }
		} catch(PlanetXSDKException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected CurrentWeatherModel currentWeatherModelFromJson() {
		Gson gson = new Gson(); 
		
		String result = FileUtils.getFileFromAssets(mContext, "current_weather_response.json");
		
		return gson.fromJson(result, CurrentWeatherModel.class);
	}
	
	protected Forecast6DayModel forecast6DayModelFromJson() {
		Gson gson = new Gson(); 
		
		String result = FileUtils.getFileFromAssets(mContext, "forecast_6day_response.json");
		
		return gson.fromJson(result, Forecast6DayModel.class);
	}
	
}
