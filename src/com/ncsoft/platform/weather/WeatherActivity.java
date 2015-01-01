package com.ncsoft.platform.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ncsoft.platform.weather.Model.CurrentWeatherModel;
import com.ncsoft.platform.weather.Model.ForecastWeekModel;
import com.ncsoft.platform.weather.Model.WeatherManager;
import com.skp.openplatform.android.sdk.common.ResponseMessage;


public class WeatherActivity extends Activity {
	
	private static final String TAG = "WeatherActivity";
    
	private TextView mTestView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		mTestView = (TextView) findViewById(R.id.test_view);
		
		WorkerThread thread = new WorkerThread(new WeatherHandler());
		thread.start();
		
	}
		
		
	
	@SuppressLint("HandlerLeak")
	class WeatherHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

        	
            super.handleMessage(msg);
        }
	}
	
	class WorkerThread extends Thread {
		Handler mHandler;
		
		public WorkerThread(Handler handler) {
			mHandler = handler;
		}
		
		@Override
		public void run() {
			try {
				WeatherManager wm = WeatherManager.getInstance();
								
				CurrentWeatherModel currentWeather = wm.getCurrentWeatherFromJson(getApplicationContext());
				ForecastWeekModel forecastWeek = wm.getForecastWeekFromJson(getApplicationContext());
				
				Log.d(TAG, currentWeather.toString());
				Log.d(TAG, forecastWeek.toString());
				
				/*
				APIRequest.setAppKey("12f6c0f5-f4f9-3268-af97-f3c9a65a7f36");
				
				String URL = "http://apis.skplanetx.com/weather/current/minutely";
								 
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("version", "1");
				param.put("lat", "37.566535");
				param.put("lon", "126.977969");
				 
				RequestBundle req = new RequestBundle();
				req.setUrl(URL);
				req.setParameters(param);
				req.setResponseType(CONTENT_TYPE.JSON);
				 
				APIRequest api = new APIRequest();
			    ResponseMessage result = api.request(req);
			    
				
				Message.obtain(mHandler, 1, result).sendToTarget();
				*/
			} catch(Exception e) {
			    e.printStackTrace();
			    
			    mHandler.sendEmptyMessage(0);
			}
		}

	}
}
