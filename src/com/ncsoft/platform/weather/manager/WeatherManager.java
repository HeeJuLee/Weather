package com.ncsoft.platform.weather.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;
import com.ncsoft.platform.weather.model.ForecastWeekModel;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.ResponseMessage;

public class WeatherManager {
	private static final String TAG = "WeatherDataManager";
	private static WeatherManager mWeatherManager;
	
	private CurrentWeatherModel mCurrentWeather;
	private ForecastWeekModel mForecastWeek;
	
	private Context mContext;

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

	public void getWeatherData(String address) {
		
		GeocodeManager gm = new GeocodeManager();
		gm.getLocationInfo(address);
		
		getWeatherData(gm.getLatitude(), gm.getLongitude());
	}
	
	public void getWeatherData(double latitude, double longitude) {
		
		getCurrentWeather(latitude, longitude);
		getForecastWeek(latitude, longitude);
		
        Log.d(TAG, "\n\n=== WeatherPlanet - getCurrentWeather");
        Log.d(TAG, mCurrentWeather.toString());
        Log.d(TAG, "\n\n=== WeatherPlanet - getForecastWeek");
        Log.d(TAG, mForecastWeek.toString());

		/*
		mCurrentWeather = getCurrentWeatherFromJson();
		Log.d(TAG, mCurrentWeather.toString());
		
		mForecastWeek = getForecastWeekFromJson();
		Log.d(TAG, mForecastWeek.toString());
		*/
	}
	
	private void getCurrentWeather(double latitude, double longitude) {
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
				
		    	mCurrentWeather = gson.fromJson(result.toString(), CurrentWeatherModel.class);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getForecastWeek(double latitude, double longitude) {
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
				
		    	mForecastWeek = gson.fromJson(result.toString(), ForecastWeekModel.class);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private CurrentWeatherModel getCurrentWeatherFromJson() {
		Gson gson = new Gson(); 
		
		String result = getFileFromAssets("current_weather_response.json");
		
		return gson.fromJson(result, CurrentWeatherModel.class);
	}
	
	
	private ForecastWeekModel getForecastWeekFromJson() {
		Gson gson = new Gson(); 
		
		String result = getFileFromAssets("forecast_week_response.json");
		
		return gson.fromJson(result, ForecastWeekModel.class);
	}

	
	public String getFileFromAssets(String fileName) {
		StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(mContext.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } 

	
	public void writeToFile(String content, String filename) throws IOException {
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(content);
		} finally {
			if(writer != null)
				writer.close();
		}
		
	}
}
