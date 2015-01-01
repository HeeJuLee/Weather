package com.ncsoft.platform.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ncsoft.platform.weather.Model.CurrentWeatherModel;
import com.ncsoft.platform.weather.Model.CurrentWeatherModel.Minutely;
import com.ncsoft.platform.weather.Model.CurrentWeatherModel.Precipitation;
import com.skp.openplatform.android.sdk.api.APIRequest;
import com.skp.openplatform.android.sdk.common.PlanetXSDKConstants.CONTENT_TYPE;
import com.skp.openplatform.android.sdk.common.PlanetXSDKException;
import com.skp.openplatform.android.sdk.common.RequestBundle;
import com.skp.openplatform.android.sdk.common.ResponseMessage;


public class WeatherActivity extends Activity {
	
	private static final String TAG = "WeatherActivity";
    
	private TextView mTestView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		mTestView = (TextView) findViewById(R.id.test_view);
		
		//WorkerThread thread = new WorkerThread(new WeatherHandler());
		//thread.start();
		
		CurrentWeatherModel currentWeather = getCurrentWeatherFromJson();
		//debugPrintCurrentWeather(currentWeather);
		
		Log.d(TAG, currentWeather.toString());
	}
		
	private CurrentWeatherModel getCurrentWeatherFromJson() {
		Gson gson = new Gson(); 
		
		String result = getFileFromAssets(this, "current_weather_response.json");
		
		return gson.fromJson(result, CurrentWeatherModel.class);
	}	

	private String getFileFromAssets(Context context, String fileName) {
        
		StringBuilder s = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
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
	private void writeToFile(Context context, String content, String filename) throws IOException {
		Writer writer = null;
		try {
			OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(content);
		} finally {
			if(writer != null)
				writer.close();
		}
		
	}
	
	
	@SuppressLint("HandlerLeak")
	class WeatherHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

       		ResponseMessage result = (ResponseMessage) msg.obj;
       		
       		Toast.makeText(getApplicationContext(), "¼º°ø", Toast.LENGTH_SHORT).show();
       		        		
       		mTestView.setText(result.getStatusCode() + ", " + result.toString());
        	
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
			} catch(PlanetXSDKException e) {
			    e.printStackTrace();
			    
			    mHandler.sendEmptyMessage(0);
			}
		}

	}
}
