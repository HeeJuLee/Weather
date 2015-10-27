package com.ncsoft.platform.weather.manager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ncsoft.platform.weather.R;
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
	private ArrayList<String> mAddress;
	private ArrayList<CurrentWeatherModel> mCurrentWeatherList;	
	
	private WeatherManager(Context context) {
		mContext = context;
		mAddress = new ArrayList<String>();
		mAddress.add(context.getResources().getString(R.string.default_address));
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
			getCurrentWeatherList(false);
		
		return mCurrentWeatherList;
	}
	
	public ArrayList<CurrentWeatherModel> getCurrentWeatherList(boolean reload) {
		if(mCurrentWeatherList == null || reload == true)
		{
			mCurrentWeatherList = new ArrayList<CurrentWeatherModel>();
			
			GeocodeManager gm = new GeocodeManager();
			
			for(int i = 0; i < mAddress.size(); i++) {
				
				// 지역별 날씨 데이터 읽어오기
				gm.getLocationInfo(mAddress.get(i));				
				CurrentWeatherModel current = getCurrentWeather(gm.getLatitude(), gm.getLongitude());
				
				current.setAddress(mAddress.get(i));				
				mCurrentWeatherList.add(current);
			}
		}
		return mCurrentWeatherList;
	}
	
	public ArrayList<CurrentWeatherModel> addCurrentWeather(String address) {
		mAddress.add(address);
		
		if(mCurrentWeatherList == null) {
			getCurrentWeatherList();
		}
		else {
			GeocodeManager gm = new GeocodeManager();
			gm.getLocationInfo(address);
			
			CurrentWeatherModel current = getCurrentWeather(gm.getLatitude(), gm.getLongitude());
			current.setAddress(address);
			
			mCurrentWeatherList.add(current);
		}
		return mCurrentWeatherList;
	}
	
	public CurrentWeatherModel getCurrentWeather(double latitude, double longitude) {
		
		CurrentWeatherModel current = getWeatherFromSKPlanet(CURRENT_WEATHER_API, latitude, longitude, CurrentWeatherModel.class);
		
		Log.d(TAG, current.toString());
		
		return current;
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
		    	Log.d(TAG, result.toString());
		    	
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
		
		String result = FileUtils.getStringFromAssets(mContext, "current_weather_response.json");
		
		return gson.fromJson(result, CurrentWeatherModel.class);
	}
	
	protected Forecast6DayModel forecast6DayModelFromJson() {
		
		Gson gson = new Gson(); 
		
		String result = FileUtils.getStringFromAssets(mContext, "forecast_6day_response.json");
		
		return gson.fromJson(result, Forecast6DayModel.class);
	}
	
}
