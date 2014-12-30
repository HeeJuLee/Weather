package com.ncsoft.platform.weather.data;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.ncsoft.platform.weather.R;
import com.ncsoft.platform.weather.xmlparser.DailyWeatherXmlParser;
import com.ncsoft.platform.weather.xmlparser.DailyWeatherXmlParser.DailyWeatherData;
import com.ncsoft.platform.weather.xmlparser.DailyWeatherXmlParser.DailyWeatherXmlHeader;
import com.ncsoft.platform.weather.xmlparser.WeeklyWeatherXmlParser;
import com.ncsoft.platform.weather.xmlparser.WeeklyWeatherXmlParser.WeeklyWeatherXmlData;

public class FutureWeatherData {
	private static final String TAG = "WWW";

	private boolean isDayWeatherExceed = false;
	
	private DailyWeatherXmlHeader mDailyXmlParsedHeader;
	private List<DailyWeatherData> mDailyXmlParsedList;
	private List<WeeklyWeatherXmlData> mWeeklyXmlParsedList;
	private ArrayList<WeatherData> mWeatherList = new ArrayList<WeatherData>();
	private Date mLastDate = new Date(); // last retrieved date
	private String mWeeklyUrl;
	private String mDailyUrl;
	private String mCityCode;
	
	public class WeatherData {
		public String mDate; 
		public int mMinTemp;
		public int mMaxTemp;
		public String mWeather;
		public int mWeatherResource;
		
		public WeatherData() {
			mDate = "";
			mWeather = "";
			mWeatherResource = 0;
		}
	}
	
	protected FutureWeatherData() {
	}
	
	protected void setDailyOption(String dailyUrl){
		mDailyUrl = dailyUrl;
	}
	
	protected void setWeeklyOption(String weeklyUrl, String cityCode){
		mWeeklyUrl = weeklyUrl;
		mCityCode = cityCode;
	}
	
	protected void update() {
		WeeklyWeatherXmlParser weeklyParser = new WeeklyWeatherXmlParser();
    	DailyWeatherXmlParser dailyParser = new DailyWeatherXmlParser();
    	
    	Log.d(TAG, "URL: " + mDailyUrl);
    	Log.d(TAG, "URL: " + mWeeklyUrl + " Citycode:" + mCityCode); 
    	
		try {
			mWeeklyXmlParsedList = weeklyParser.parseFromUrl(mWeeklyUrl, mCityCode);
			mDailyXmlParsedList = dailyParser.parseFromUrl(mDailyUrl);
			mDailyXmlParsedHeader = dailyParser.getHeader();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		mWeatherList.clear();
		
    	for(int i = 0; i < 6; i++) {
    		if(i == 0) {
    			WeatherData weatherData = getFirstWeatherData();
    			mWeatherList.add(weatherData);
        	}
        	else {
        		WeatherData weatherData = getNextWeatherData();
        		mWeatherList.add(weatherData);
        	}
    	}
	}
	
	protected WeatherData getWeatherData(int index) {
		//Log.d(TAG, "getWeatherData() FutureWeatherData is " + this);
		return mWeatherList.get(index);
	}
	
	private WeatherData getFirstWeatherData() {
		WeatherData weatherData = new WeatherData();
		boolean isDay0valid;
		isDayWeatherExceed = false;
		isDay0valid = getDayWeatherByDayAfter(0, weatherData, mLastDate);

		if (isDay0valid == false) {
			getDayWeatherByDayAfter(1, weatherData, mLastDate);
			isDayWeatherExceed = true;
		}
		
		return weatherData;
	}

	private Date increamentDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(cal.DATE, 1);
		return cal.getTime();
	}
	
	private WeatherData getNextWeatherData() {
		WeatherData weatherData = new WeatherData();
		
		if (isDayWeatherExceed == false) {
			getDayWeatherByDayAfter(1, weatherData, mLastDate);
			isDayWeatherExceed = true;
		} else {
			mLastDate = increamentDay(mLastDate);
			getWeeklyWeatherData(weatherData, mLastDate);
		}
		
		return weatherData;
	}
	
	private int determineResource(ArrayList<Integer> skyList, ArrayList<Integer> ptyList){
		int ptyForToday = 0;
		int skyForToday = 0;
		
		/* pty 
		 * 0 : 없음
		 * 1 : 비
		 * 2 : 비/눈
		 * 3 : 눈/비
		 * 4 : 눈
		 * 
		 * sky
		 * 1 : 맑음
		 * 2 : 구름조금
		 * 3 : 구름많음
		 * 4 : 흐림
		 */
		
		Collections.sort(ptyList);
		ptyForToday = ptyList.get(ptyList.size() - 1);
		
		if(ptyForToday == 0){
			int i;
			double sum = 0;
			for(i=0; i<skyList.size(); i++) {
				sum += skyList.get(i);
				Collections.sort(skyList);
				skyForToday = skyList.get(ptyList.size() - 1);
			}
			//Log.d(TAG, "sum:" + sum + " i:" + i);
			skyForToday = (int)Math.round(sum / i);
		}
		
		switch(ptyForToday){
	    	case 0: 
	    		switch(skyForToday){
	    			case 1: return R.drawable.sunny;
	    			case 2: return R.drawable.sunny_cloudy;
	    			case 3: return R.drawable.cloudy_sunny;
	    			case 4: return R.drawable.cloudy;
	    			default: return 0;
	    		}
	    	case 1: return R.drawable.rain;
	    	case 2: return R.drawable.rain;
	    	case 3: return R.drawable.snow;
	    	case 4: return R.drawable.snow;
	    	default: return 0;
		}
	}
    
	private boolean getDayWeatherByDayAfter(int dayAfter, WeatherData weatherData, Date date) {
		boolean isFound = false;

		ArrayList<Integer> skyList = new ArrayList<Integer>();
		ArrayList<Integer> ptyList = new ArrayList<Integer>();

		for (int i = 0; i < mDailyXmlParsedList.size(); i++) {
			DailyWeatherData data = (DailyWeatherData) mDailyXmlParsedList.get(i);
			if (data.mDay == dayAfter) {
				if (isFound == false) {
					weatherData.mMinTemp = Math.round(data.mMinTemp);
					weatherData.mMaxTemp = Math.round(data.mMaxTemp);
					isFound = true;
				}
				skyList.add(new Integer(data.mSky));
				ptyList.add(new Integer(data.mPty));
			}
		}

		//skip no max/min temp happening after 3p.m 
		//if (minTemp == -999 && maxTemp == -999)
		//	return false;
		
		if (isFound == false)
			return false;

		weatherData.mWeatherResource = determineResource(skyList, ptyList);

		for (int i = 0; i < mDailyXmlParsedList.size(); i++) {
			DailyWeatherData data = (DailyWeatherData) mDailyXmlParsedList.get(i);
			if (data.mDay == dayAfter) {
				weatherData.mWeather += String.format("%1$d시|%2$s|%3$dº|%4$d%%|%5$.1fm/s\n", data.mHour, data.mWfKor, Math.round(data.mTemp), data.mReh, data.mWs);
			}
		}

		Calendar cal = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			Date headerDate = sdf.parse(mDailyXmlParsedHeader.mDate);
			cal.setTime(headerDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.set(Calendar.HOUR_OF_DAY, 0); //initialize for time comparation
		cal.set(Calendar.MINUTE, 0); //initialize for time comparation
		cal.set(Calendar.SECOND, 0); //initialize for time comparation
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d E");
		cal.add(Calendar.DATE, dayAfter);
		weatherData.mDate = dateFormat.format(cal.getTime());
		
		date.setTime(cal.getTime().getTime());
				
		return isFound;
	}

	private void getWeeklyWeatherData(WeatherData weatherData, Date date) {
		if (mWeeklyXmlParsedList.isEmpty())
			return;
		
		for(int i=0; i<mWeeklyXmlParsedList.size(); i++){
			WeeklyWeatherXmlData data = (WeeklyWeatherXmlData) mWeeklyXmlParsedList.get(i);
			if(data.mDate.equals(date)){
				
				
				weatherData.mMinTemp = data.mMinTemp;
				weatherData.mMaxTemp = data.mMaxTemp;
				weatherData.mWeather = data.mWeather;
				weatherData.mWeatherResource = getIconResource(weatherData.mWeather);

				SimpleDateFormat newSdf = new SimpleDateFormat("M/d E");
				weatherData.mDate = newSdf.format(date);
			}
		}
	}
	
	private int getIconResource(String weatherStr) {
		if(weatherStr.equals("맑음")){
			return R.drawable.sunny;
		}
		else if(weatherStr.equals("구름 조금")){
			return R.drawable.sunny_cloudy;
		}
		else if(weatherStr.equals("구름조금")){
			return R.drawable.sunny_cloudy;
		}
		else if(weatherStr.equals("구름 많음")){
			return R.drawable.cloudy_sunny;
		}
		else if(weatherStr.equals("구름많음")){
			return R.drawable.cloudy_sunny;
		}
		else if(weatherStr.equals("흐림")){
			return R.drawable.cloudy;
		}
		else if(weatherStr.equals("비")){
			return R.drawable.rain;
		}
		else if(weatherStr.equals("눈/비")){
			return R.drawable.snow;
		}
		else if(weatherStr.equals("눈")){
			return R.drawable.more_snow;
		}
		else if(weatherStr.equals("흐리고 비")){
			return R.drawable.rain;
		}
		else if(weatherStr.equals("구름많고 비/눈")){
			return R.drawable.snow;
		}
		else if(weatherStr.equals("구름많고 비")){
			return R.drawable.rain;
		}
		else if(weatherStr.equals("구름많고 눈")){
			return R.drawable.snow;
		}
		else {
			return 0;
		}
    }
}
