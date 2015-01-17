package com.ncsoft.platform.weather;

import java.util.ArrayList;

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
		
		// 하늘상태 이미지
		image.setImageResource(current.getSkyResourceID());
		
		// 현재 기온
		String formatString = getResources().getString(R.string.current_temperature_format);
		temperature.setText(String.format(formatString, current.getTc()));
		// 하늘상태 텍스트
		skyname.setText(current.getSkyName());
		// 최저 최고 기온
		formatString = getResources().getString(R.string.minmax_temperature_format);
		minmax.setText(String.format(formatString, current.getTmin(), current.getTmax()));
		
		// 로우 데이터
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
					
					// 로우 데이터
					TextView forecast3day = (TextView) v.findViewById(R.id.fragment_forecast_textview_forecast3day);
					TextView forecast6day = (TextView) v.findViewById(R.id.fragment_forecast_textview_forecast6day);
					forecast3day.setText(mForecast3Day.toString());
					forecast6day.setText(mForecast6Day.toString());
					
					// 5일간 오전 오후 
					ImageView amImage = (ImageView) v.findViewById(R.id.fragment_forecast_2_am);
					ImageView pmImage = (ImageView) v.findViewById(R.id.fragment_forecast_2_pm);
					amImage.setImageResource(mForecast6Day.getSkyResourceID(2, true));
					pmImage.setImageResource(mForecast6Day.getSkyResourceID(2, false));
					
					amImage = (ImageView) v.findViewById(R.id.fragment_forecast_3_am);
					pmImage = (ImageView) v.findViewById(R.id.fragment_forecast_3_pm);
					amImage.setImageResource(mForecast6Day.getSkyResourceID(3, true));
					pmImage.setImageResource(mForecast6Day.getSkyResourceID(3, false));
					
					amImage = (ImageView) v.findViewById(R.id.fragment_forecast_4_am);
					pmImage = (ImageView) v.findViewById(R.id.fragment_forecast_4_pm);
					amImage.setImageResource(mForecast6Day.getSkyResourceID(4, true));
					pmImage.setImageResource(mForecast6Day.getSkyResourceID(4, false));
					
					amImage = (ImageView) v.findViewById(R.id.fragment_forecast_5_am);
					pmImage = (ImageView) v.findViewById(R.id.fragment_forecast_5_pm);
					amImage.setImageResource(mForecast6Day.getSkyResourceID(5, true));
					pmImage.setImageResource(mForecast6Day.getSkyResourceID(5, false));
				
					amImage = (ImageView) v.findViewById(R.id.fragment_forecast_6_am);
					pmImage = (ImageView) v.findViewById(R.id.fragment_forecast_6_pm);
					amImage.setImageResource(mForecast6Day.getSkyResourceID(6, true));
					pmImage.setImageResource(mForecast6Day.getSkyResourceID(6, false));
					
					// 5일간 최저 최고 기온
					String formatString = getResources().getString(R.string.forecast_minmax_format);
					TextView min = (TextView) v.findViewById(R.id.fragment_forecast_2_min);
					TextView max = (TextView) v.findViewById(R.id.fragment_forecast_2_max);
					min.setText(String.format(formatString, mForecast6Day.getTmin(2)));
					max.setText(String.format(formatString, mForecast6Day.getTmax(2)));
					
					min = (TextView) v.findViewById(R.id.fragment_forecast_3_min);
					max = (TextView) v.findViewById(R.id.fragment_forecast_3_max);
					min.setText(String.format(formatString, mForecast6Day.getTmin(3)));
					max.setText(String.format(formatString, mForecast6Day.getTmax(3)));
					
					min = (TextView) v.findViewById(R.id.fragment_forecast_4_min);
					max = (TextView) v.findViewById(R.id.fragment_forecast_4_max);
					min.setText(String.format(formatString, mForecast6Day.getTmin(4)));
					max.setText(String.format(formatString, mForecast6Day.getTmax(4)));
					
					min = (TextView) v.findViewById(R.id.fragment_forecast_5_min);
					max = (TextView) v.findViewById(R.id.fragment_forecast_5_max);
					min.setText(String.format(formatString, mForecast6Day.getTmin(5)));
					max.setText(String.format(formatString, mForecast6Day.getTmax(5)));
					
					min = (TextView) v.findViewById(R.id.fragment_forecast_6_min);
					max = (TextView) v.findViewById(R.id.fragment_forecast_6_max);
					min.setText(String.format(formatString, mForecast6Day.getTmin(6)));
					max.setText(String.format(formatString, mForecast6Day.getTmax(6)));
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
