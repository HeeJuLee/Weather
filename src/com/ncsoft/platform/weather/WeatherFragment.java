package com.ncsoft.platform.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_weather, container, false);
		
		TextView testView = (TextView) view.findViewById(R.id.test_view);
		testView.setText(new String("test ¿‘¥œ¥Ÿ").toString());
		

		return view;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
}
