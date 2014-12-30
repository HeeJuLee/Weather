package com.ncsoft.platform.weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ncsoft.platform.weather.data.WeatherDataManager;
import com.ncsoft.platform.weather.location.LocationFinder;

public class TestActivity extends Activity implements AdapterView.OnItemClickListener {
	private static final String TAG = "WWW";
	
	WeatherDataManager mWeatherDataManager;
	
	private static HandlerThread sWorkerThread;
	private static Handler sWorkerQueue;
	private Handler mHandler;

	static {
		sWorkerThread = new HandlerThread("TestActivity-worker");
		sWorkerThread.start();
		sWorkerQueue = new Handler(sWorkerThread.getLooper());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Activity created");
		super.onCreate(savedInstanceState);
		
		mWeatherDataManager = WeatherDataManager.getInstance(this);
		
		setContentView(R.layout.test_activity);
		
		if(mWeatherDataManager.isLoaded()) {
			buildLayout();
		} else {
			Log.d(TAG, "Evacuation plan");
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
		    		Log.d(TAG, "LATE buildLayout()");
		    		buildLayout();
				}
			};
			//When widget gets alive from OOM, onCreate() is called but all instance inside of it are pre-initialized state.
			//so weatherData from weather data manager is null. So we need to handle this case here starting from last known location. 
			final Context context = this;
			sWorkerQueue.post(
				new Runnable(){
					public void run() {
						Location location = LocationFinder.getLastKnownLocation(context);
						mWeatherDataManager.setLocation(location);
						Message.obtain(mHandler, 0, 0).sendToTarget();
					}
				}
			);
		}
	}
	
	private void showDetailWeather(int position) {
		ListView detailView = (ListView) findViewById(R.id.detail_weather);
		TextView simpleView = (TextView) findViewById(R.id.simple_weather);
		
		String weather = mWeatherDataManager.getFutureWeatherData(position).mWeather;
		
		//header for each column
		if(!weather.contains("|")) {
			//3-days after forecasting is only for short sky weather information
			simpleView.setText(weather);
			detailView.setVisibility(View.GONE);
			simpleView.setVisibility(View.VISIBLE);
		} else {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> item;
			StringTokenizer st = new StringTokenizer(weather, "\n");
			String token;
			
			/* put dummy data first */
			for(int i=3; i<=24; i+=3) {
				item = new HashMap<String, Object>();
				item.put("col1", (i - 3) + "~" + i + "시");
				item.put("col2", "-");
				item.put("col3", "-");
				item.put("col4", "-");
				item.put("col5", "-");
				list.add(item);
			}
			
			/* and then, put real data in replacement manner */ 
			while(st.hasMoreElements()) {
				token = st.nextToken();
				item = new HashMap<String, Object>();
				StringTokenizer st2 = new StringTokenizer(token, "|");
				String time = st2.nextToken();
				int nTime = 0;
				try{
					nTime = Integer.parseInt(time.substring(0, time.length()-1));
				}catch(NumberFormatException e){}
				if(nTime < 3) break;
				
				item.put("col1", (nTime - 3) + "~" + nTime + "시");
				item.put("col2", st2.nextToken());
				item.put("col3", st2.nextToken());
				item.put("col4", st2.nextToken());
				item.put("col5", st2.nextToken());
				
				list.remove(nTime/3 - 1);
				list.add(nTime/3 - 1, item);
			}
			
			item = new HashMap<String, Object>();
			item.put("col1", "시간");
			item.put("col2", "날씨");
			item.put("col3", "기온");
			item.put("col4", "습도");
			item.put("col5", "풍량");
			list.add(0, item);
			
			SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.day_item, 
					new String[]{ "col1", "col2", "col3", "col4", "col5" }, 
					new int[] 	{ R.id.time, R.id.weather, R.id.temp, R.id.reh, R.id.ws } );
			detailView.setAdapter(adapter);
			
			detailView.setVisibility(View.VISIBLE);
			simpleView.setVisibility(View.GONE);
		}		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
		showDetailWeather(position);
		for(int i=0; i<parent.getChildCount(); i++)  {
			if(i != position)
				parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
		}
		Log.d(TAG, "parent.getChildCount()" + parent.getChildCount());
		parent.getChildAt(position).setBackgroundColor(Color.rgb(0x39,0x87,0xa3));
	}
	
	private void buildLayout() {
		Intent intent = getIntent();
		final int position = intent.getIntExtra("Position", 0);
		
		//City name
		String cityname = intent.getStringExtra("Cityname");
		TextView cityNameView = (TextView) findViewById(R.id.city_name);
		cityNameView.setText(cityname);
		
		//Current temperature
		TextView curTempView = (TextView) findViewById(R.id.current_temp);
		String formatStr = getResources().getString(R.string.current_temp_format_string);
		curTempView.setText(String.format(formatStr, mWeatherDataManager.getCurrentTemp()));
		
		ImageView skyView = (ImageView) findViewById(R.id.city_weather);
		skyView.setImageResource(mWeatherDataManager.getCurrentResourceID());
		
		//Date grid view
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> item;
		for(int i=0; i<6; i++) {
			item = new HashMap<String, Object>();
			item.put("col1", mWeatherDataManager.getFutureWeatherData(i).mDate);
			list.add(item);
		}
		
		GridView dateListView = (GridView) findViewById(R.id.date_list);
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.widget_date_item, 
									new String[]{"col1"}, new int[] { R.id.date_text});
		dateListView.setAdapter(adapter);
		
		//Weather grid view
		/*mList = new ArrayList<HashMap<String, Object>>();
		for(int i=0; i<6; i++) {
			item = new HashMap<String, Object>();
			
			int maxTemp = mWeatherDataManager.getFutureWeatherData(i).mMaxTemp;
			int minTemp = mWeatherDataManager.getFutureWeatherData(i).mMinTemp;
			String maxTempStr = (maxTemp != -999)? maxTemp + "º": "-"; 
			String minTempStr = (minTemp != -999)? minTemp + "º": "-";
			
			item.put("col2", maxTempStr);
			item.put("col3", minTempStr);
			item.put("col4", mWeatherDataManager.getFutureWeatherData(i).mWeatherResource);
			mList.add(item);
		}
		adapter = new SimpleAdapter(this, mList, R.layout.large_widget_item, 
		new String[]{"col2", "col3", "col4"}, new int[] {  R.id.grid_maxTemp, R.id.grid_minTemp, R.id.grid_weather_icon });*/
		
		GridView weatherGridView = (GridView) findViewById(R.id.weather_content);
		weatherGridView.setAdapter(new WeatherDetailAdapter(this, position));
		weatherGridView.setOnItemClickListener(this);
		
		//show detail info according to position
		showDetailWeather(position);
	}
	
	class WeatherDetailAdapter extends BaseAdapter  {

	    private LayoutInflater mInflater;
		private int mPosition;

	    public WeatherDetailAdapter(Context context, int position) {
	        mInflater = LayoutInflater.from(context);
	        mPosition = position;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	TextView text;
	    	ImageView image;

	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.large_widget_item, null);
	        }
	        
            int maxTemp = mWeatherDataManager.getFutureWeatherData(position).mMaxTemp;
			int minTemp = mWeatherDataManager.getFutureWeatherData(position).mMinTemp;
			String maxTempStr = (maxTemp != -999)? maxTemp + "º": "-"; 
			String minTempStr = (minTemp != -999)? minTemp + "º": "-";
			int resourceId = mWeatherDataManager.getFutureWeatherData(position).mWeatherResource;
			
            text = (TextView) convertView.findViewById(R.id.grid_maxTemp);
            text.setText(maxTempStr);
            
            text = (TextView) convertView.findViewById(R.id.grid_minTemp);
            text.setText(minTempStr);
            
            image = (ImageView) convertView.findViewById(R.id.grid_weather_icon);
            image.setImageResource(resourceId);
            
            if(position == mPosition)
            	convertView.setBackgroundColor(Color.rgb(0x39,0x87,0xa3));
            
	        return convertView;
	    }


		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
}

