package com.ncsoft.platform.weather.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.content.Context;

import com.google.gson.Gson;

public class WeatherManager {
	
	private static WeatherManager mWeatherManager;
	
	private WeatherManager() {
	}
	
	public static WeatherManager getInstance() {
        if(mWeatherManager == null) {
        	mWeatherManager = new WeatherManager();
        }
        return mWeatherManager;
    }

	public CurrentWeatherModel getCurrentWeatherFromJson(Context context) {
		Gson gson = new Gson(); 
		
		String result = getFileFromAssets(context, "current_weather_response.json");
		
		return gson.fromJson(result, CurrentWeatherModel.class);
	}
	
	public ForecastWeekModel getForecastWeekFromJson(Context context) {
		Gson gson = new Gson(); 
		
		String result = getFileFromAssets(context, "forecast_week_response.json");
		
		return gson.fromJson(result, ForecastWeekModel.class);
	}

	public String getFileFromAssets(Context context, String fileName) {
        
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
	
	public void writeToFile(Context context, String content, String filename) throws IOException {
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
}
