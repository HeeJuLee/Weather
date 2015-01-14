package com.ncsoft.platform.weather;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncsoft.platform.weather.manager.WeatherManager;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;
import com.ncsoft.platform.weather.model.Forecast3DayModel;
import com.ncsoft.platform.weather.model.Forecast6DayModel;

public class ForecastFragment extends Fragment {
	public static final String EXTRA_POSITION = "extra_position"; 
	
	private int mPos = 0;
	private Forecast3DayModel mForecast3Day;
	private Forecast6DayModel mForecast6Day;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPos = getActivity().getIntent().getIntExtra(EXTRA_POSITION, 0);
				
		getForecastData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_forecast, container, false);
		
		ImageView image = (ImageView) v.findViewById(R.id.fragment_forecast_image);
		TextView temperature = (TextView) v.findViewById(R.id.fragment_forecast_temperature);
		TextView skyname = (TextView) v.findViewById(R.id.fragment_forecast_skyname);
		TextView minmax = (TextView) v.findViewById(R.id.fragment_forecast_minmax);
		
		WeatherManager weatherManager = WeatherManager.getInstance(getActivity());
		ArrayList<CurrentWeatherModel> weatherList = weatherManager.getCurrentWeatherList();
		CurrentWeatherModel current = weatherList.get(mPos);
		
		image.setImageResource(current.getSkyResourceID());
		
		String formatString = getResources().getString(R.string.current_temperature_format);
		temperature.setText(String.format(formatString, current.getTc()));
		skyname.setText(current.getSkyName());
		formatString = getResources().getString(R.string.minmax_temperature_format);
		minmax.setText(String.format(formatString, current.getTmin(), current.getTmax()));
		
		TextView currentweather = (TextView) v.findViewById(R.id.fragment_forecast_textview_currentweather);
		currentweather.setText(current.toString());

		return v;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 1) {
				if(mForecast3Day != null && mForecast6Day != null) {
					View v = getView();
					
					TextView forecast3day = (TextView) v.findViewById(R.id.fragment_forecast_textview_forecast3day);
					TextView forecast6day = (TextView) v.findViewById(R.id.fragment_forecast_textview_forecast6day);
					forecast3day.setText(mForecast3Day.toString());
					forecast6day.setText(mForecast6Day.toString());
				}
				else
					Toast.makeText(getActivity(), R.string.forecast_is_null, Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getActivity(), R.string.forecast_load_exception, Toast.LENGTH_SHORT).show();
		}
	};
	
	private void getForecastData() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					WeatherManager weatherManager = WeatherManager.getInstance(getActivity());
					ArrayList<CurrentWeatherModel> currentWeatherList = weatherManager.getCurrentWeatherList();
					CurrentWeatherModel currentWeather = currentWeatherList.get(mPos);
					double latitude = currentWeather.getLatitude();
					double longitude = currentWeather.getLongitude();
							
					mForecast3Day = weatherManager.getForecast3DayWeather(latitude, longitude);
					mForecast6Day = weatherManager.getForecast6DayWeather(latitude, longitude);
					
					mHandler.sendEmptyMessage(1);
				} catch(Exception e) {
				    e.printStackTrace();
				    mHandler.sendEmptyMessage(0);
				}
			}
		};
		thread.start();
	}
}
