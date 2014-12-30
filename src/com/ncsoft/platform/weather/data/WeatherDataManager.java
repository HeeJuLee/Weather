package com.ncsoft.platform.weather.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.ncsoft.platform.weather.data.FutureWeatherData.WeatherData;
import com.ncsoft.platform.weather.location.AddressMap;
import com.ncsoft.platform.weather.location.AddressMap.AddressInfo;

public class WeatherDataManager {
	private static String TAG = "WWW";
	
	private static WeatherDataManager mWeatherDataContainer = null;

	private static Context mContext;
	
	private FutureWeatherData mFutureWeatherData;
	private CurrentWeatherData mCurrentWeatherData;
	
	private WeatherDataManager(){
	}
	
	public static WeatherDataManager getInstance(Context context) {		
		if(mWeatherDataContainer == null) {
			Log.d(TAG, "new WeatherDataManager");
			mWeatherDataContainer = new WeatherDataManager();
			mContext = context;
		}
		return mWeatherDataContainer;
	}
	
	public boolean isLoaded() {
		if(mFutureWeatherData == null || mCurrentWeatherData == null)
			return false;
		else
			return true;
	}
	
	public void setLocation(Location location) {
		Log.d(TAG, "setLocation() Latitude:" + location.getLatitude() + " Longitue:" + location.getLongitude());
		
		final AddressInfo ai = AddressMap.getInstance().queryCloseCity(location);
		
		String cityCode = AddressMap.getInstance().getWeekCityCode(ai.mCityname);
		int weekStnId = AddressMap.getInstance().getWeekStationId(ai.mCityname);
		int curStnId = AddressMap.getInstance().getCurStationId(ai.mCityname);
		
		int gridx = (int)(location.getLongitude() * 17.8480008 -2206);
		int gridy = (int)(location.getLatitude()  * 22.23306308 - 708.4424054);
		
		String DailyUrlFormat = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=%1$d&gridy=%2$d";
		String weeklyUrlFormat = "http://www.kma.go.kr/weather/forecast/mid-term-xml.jsp?stnId=%1$d";
		String curWeatherURL = "http://www.kma.go.kr/XML/weather/sfc_web_map.xml";
		
		synchronized (this) {
			mFutureWeatherData = new FutureWeatherData();
			mCurrentWeatherData = new CurrentWeatherData();

			mFutureWeatherData.setDailyOption(String.format(DailyUrlFormat, gridx, gridy));
			mFutureWeatherData.setWeeklyOption(String.format(weeklyUrlFormat, weekStnId), cityCode);
			mCurrentWeatherData.setOption(curWeatherURL, curStnId);
			
			mCurrentWeatherData.update();
			mFutureWeatherData.update();
		}
	}
	
	public void updateCurrentWeather(Runnable callback){
		synchronized (this) {
			mCurrentWeatherData.update();
		}
	}
	
	public void updateFutureWeather(){
		synchronized (this) {
			if(mFutureWeatherData != null)
				mFutureWeatherData.update();
		}
	}
	
	public int getCurrentTemp() {
		synchronized (this) {
			if(mCurrentWeatherData == null) {
				return -999;
			} else {
				return mCurrentWeatherData.getCurrentTemp();
			}
		}
	}
	
	public int getCurrentResourceID() {
		synchronized (this) {
			if(mCurrentWeatherData == null) {
				return 0;
			} else {
				return mCurrentWeatherData.getResourceID();
			}
		}
	}
	
	public String getCurrentWeather() {
		synchronized (this) {
			if(mCurrentWeatherData == null) {
				return "";
			} else {
				return mCurrentWeatherData.getWeatherString();
			}
		}
	}
	
	public WeatherData getFutureWeatherData(int position) {
		synchronized (this) {
			if(mFutureWeatherData == null) {
				return null;
			} else { 
				return mFutureWeatherData.getWeatherData(position);
			}
		}
	}
}
