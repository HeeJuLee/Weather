package com.ncsoft.platform.weather;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ncsoft.platform.weather.manager.WeatherManager;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;

public class ForecastFragment extends Fragment {
	public static final String EXTRA_POSITION = "com.ncsoft.platform.weather.extra_position"; 
	
	int mCurrentPos = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mCurrentPos = getActivity().getIntent().getIntExtra(EXTRA_POSITION, 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_forecast, container, false);
		
		TextView textView = (TextView) v.findViewById(R.id.fragment_forecast_textview);
		
		WeatherManager weatherManager = WeatherManager.getInstance(getActivity());
		ArrayList<CurrentWeatherModel> weatherList = weatherManager.getCurrentWeatherList();
		CurrentWeatherModel current = weatherList.get(mCurrentPos);
				
		textView.setText(current.toString());
		
		return v;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
}
