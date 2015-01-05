package com.ncsoft.platform.weather;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncsoft.platform.weather.manager.WeatherManager;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;

public class WeatherListFragment extends ListFragment {

	private static final String TAG = "WeatherListFragment";
	private ArrayList<CurrentWeatherModel> mCurrentWeatherList;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActivity().setTitle(R.string.weather_list);
		
		WorkerThread thread = new WorkerThread(new WeatherHandler());
		thread.start();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//CurrentWeatherModel current = (CurrentWeatherModel) getListView().getItemAtPosition(position);
		
		// 인텐트 생성해서 디테일 액티비티로 엑스트라 전송
		Intent intent = new Intent(getActivity(), ForecastActivity.class);
		intent.putExtra(ForecastFragment.EXTRA_POSITION, position);
		startActivity(intent);
	}
	
	private class WeatherAdapter extends ArrayAdapter<CurrentWeatherModel> {
		
		public WeatherAdapter(ArrayList<CurrentWeatherModel> weatherList) {
			super(getActivity(), android.R.layout.simple_list_item_1, weatherList);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_weather_item, null);
			}
			
			if(mCurrentWeatherList != null) {
				CurrentWeatherModel current = getItem(position);
				
				TextView textView = (TextView) convertView.findViewById(R.id.list_weather_item_textview);
				textView.setText(current.toString());
			}
			
			return convertView;
			//return super.getView(position, convertView, parent);
		}
	}
	
	
	@SuppressLint("HandlerLeak")
	private class WeatherHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 1)
				setListAdapter(new WeatherAdapter(mCurrentWeatherList));				
			else
				Toast.makeText(getActivity(), "FAIL: Weather is not loaded", Toast.LENGTH_SHORT).show();
			
		    super.handleMessage(msg);
		}
	}

	private class WorkerThread extends Thread {
		Handler mHandler;

		public WorkerThread(Handler handler) {
			mHandler = handler;
		}

		@Override
		public void run() {
			try {
				WeatherManager weatherManager = WeatherManager.getInstance(getActivity());
				mCurrentWeatherList = weatherManager.getCurrentWeatherList();
				
				mHandler.sendEmptyMessage(1);
				//Message.obtain(mHandler, 1, result).sendToTarget();				
			} catch(Exception e) {
			    e.printStackTrace();
			    mHandler.sendEmptyMessage(0);
			}

		}
	}
}


