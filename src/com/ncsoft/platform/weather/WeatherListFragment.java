package com.ncsoft.platform.weather;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncsoft.platform.weather.manager.WeatherManager;
import com.ncsoft.platform.weather.model.CurrentWeatherModel;

public class WeatherListFragment extends ListFragment {

	private static final String TAG = "WeatherListFragment";
	private static final int WEATHERLIST_FAIL = 0;
	private static final int WEATHERLIST_INIT = 1;
	private static final int WEATHERLIST_RELOAD = 2;
	private static final int WEATHERLIST_ADD= 3;
	
	private ArrayList<CurrentWeatherModel> mCurrentWeatherList;
	private String mSelectAddress;
	private WeatherListAdapter mWeatherListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActivity().setTitle(R.string.weather_list);
		setHasOptionsMenu(true);
		
		WorkerThread thread = new WorkerThread(new WeatherHandler(), WEATHERLIST_INIT);
		thread.start();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.weather, menu);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//CurrentWeatherModel current = (CurrentWeatherModel) getListView().getItemAtPosition(position);
		
		// 인텐트 생성해서 디테일 액티비티로 엑스트라 전송
		Intent intent = new Intent(getActivity(), ForecastActivity.class);
		intent.putExtra(ForecastFragment.EXTRA_POSITION, position);
		startActivity(intent);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_item_add:
			Intent intent = new Intent(getActivity(), AddressListActivity.class);
			startActivityForResult(intent, 10);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK) {
			if(requestCode == 10) {
				Toast.makeText(getActivity(), data.getStringExtra(AddressListActivity.SELECT_ADDRESS), Toast.LENGTH_SHORT).show();
				
				mSelectAddress = data.getStringExtra(AddressListActivity.SELECT_ADDRESS);
				
				WorkerThread thread = new WorkerThread(new WeatherHandler(), WEATHERLIST_ADD);
				thread.start();
			}
		}
	}
	
	private class WeatherListAdapter extends ArrayAdapter<CurrentWeatherModel> {
		
		public WeatherListAdapter(ArrayList<CurrentWeatherModel> weatherList) {
			super(getActivity(), android.R.layout.simple_list_item_1, weatherList);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_weather_item, null);
			}
			
			if(mCurrentWeatherList != null) {
				ImageView image = (ImageView) convertView.findViewById(R.id.list_weather_item_image);
				TextView address = (TextView) convertView.findViewById(R.id.list_weather_item_address);
				TextView temperature = (TextView) convertView.findViewById(R.id.list_weather_item_temperature);
				TextView skyname = (TextView) convertView.findViewById(R.id.list_weather_item_skyname);
				TextView minmax = (TextView) convertView.findViewById(R.id.list_weather_item_min_max);

				CurrentWeatherModel current = getItem(position);
				//address.setText(current.getAddress() + "(" + current.getStation() + ")");
				address.setText(current.getAddress());
				image.setImageResource(current.getSkyResourceID());
				
				String format = getResources().getString(R.string.current_temperature_format);
				temperature.setText(String.format(format, current.getTc()));

				skyname.setText(current.getSkyName());
				
				format = getResources().getString(R.string.minmax_temperature_format);
				minmax.setText(String.format(format, current.getTmin(), current.getTmax()));
			}
			
			return convertView;
		}
	}
		
	@SuppressLint("HandlerLeak")
	private class WeatherHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			switch(msg.what) {
				case WEATHERLIST_INIT:
					mWeatherListAdapter = new WeatherListAdapter(mCurrentWeatherList);
					setListAdapter(mWeatherListAdapter);
					break;
				case WEATHERLIST_RELOAD:
					break;
				case WEATHERLIST_ADD:
					mWeatherListAdapter.notifyDataSetChanged();
					break;
				default:
					Toast.makeText(getActivity(), "FAIL: Weather is not loaded", Toast.LENGTH_SHORT).show();
			}
			
		    super.handleMessage(msg);
		}
	}

	private class WorkerThread extends Thread {
		Handler mHandler;
		int mActionType = 0;

		public WorkerThread(Handler handler, int type) {
			mHandler = handler;
			mActionType = type;
		}

		@Override
		public void run() {
			try {
				WeatherManager weatherManager = WeatherManager.getInstance(getActivity());
				
				switch(mActionType) {
					case WEATHERLIST_INIT:
						mCurrentWeatherList = weatherManager.getCurrentWeatherList();
						break;
					case WEATHERLIST_RELOAD:
						mCurrentWeatherList = weatherManager.getCurrentWeatherList(true);
						break;
					case WEATHERLIST_ADD:
						mCurrentWeatherList = weatherManager.addCurrentWeather(mSelectAddress);
						break;
					default:
						break;
				}
				
				mHandler.sendEmptyMessage(mActionType);				
			} catch(Exception e) {
			    e.printStackTrace();
			    mHandler.sendEmptyMessage(WEATHERLIST_FAIL);
			}
		}
	}
}


