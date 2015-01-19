package com.ncsoft.platform.weather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
	public static final String EXTRA_ITEM_POSITION = "extra_item_position"; 
	
	private int mPos = 0;
	private Forecast3DayModel mForecast3Day;
	private Forecast6DayModel mForecast6Day;
	
	public static ForecastFragment getInstance(int pos) {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_ITEM_POSITION, pos);
		
		ForecastFragment fragment = new ForecastFragment();
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Activity�� ����Ʈ�� ���� �б�
		//mPos = getActivity().getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0);
		
		// Fragment�� ��ť��Ʈ�� �����ϵ��� �ؼ� �ޱ� 
		mPos = (Integer) getArguments().getSerializable(EXTRA_ITEM_POSITION);
				
		getForecastData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_forecast, container, false);
		
		// ��������Ʈ���� ������ �⺻ ���� ���� ȭ�鿡 ��ġ�ϱ�
		return currentWeatherDataSetChange(v);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 1) {
				if(mForecast3Day != null && mForecast6Day != null) {
					// ���� �������� ���� - ���� ������ ȭ�鿡 ��ġ�ϱ�
					forecastWeatherDataSetChange();
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
	
	private View currentWeatherDataSetChange(View v) {
		
		TextView address = (TextView) v.findViewById(R.id.fragment_forecast_address);
		ImageView image = (ImageView) v.findViewById(R.id.fragment_forecast_image);
		TextView temperature = (TextView) v.findViewById(R.id.fragment_forecast_temperature);
		TextView skyname = (TextView) v.findViewById(R.id.fragment_forecast_skyname);
		TextView minmax = (TextView) v.findViewById(R.id.fragment_forecast_minmax);
		TextView precipitation = (TextView) v.findViewById(R.id.fragment_forecast_precipitation);
		TextView humidity = (TextView) v.findViewById(R.id.fragment_forecast_humidity);
		TextView surface = (TextView) v.findViewById(R.id.fragment_forecast_surface);
		TextView wdir = (TextView) v.findViewById(R.id.fragment_forecast_wdir);
		TextView wspd = (TextView) v.findViewById(R.id.fragment_forecast_wspd);
		
		WeatherManager weatherManager = WeatherManager.getInstance(getActivity());
		ArrayList<CurrentWeatherModel> weatherList = weatherManager.getCurrentWeatherList();
		CurrentWeatherModel current = weatherList.get(mPos);
		
		// ������
		getActivity().setTitle(current.getAddress());
		address.setText(current.getAddress());
		
		// �ϴû��� �̹���
		image.setImageResource(current.getSkyResourceID());
		
		// ���� ���
		String formatString = getResources().getString(R.string.current_temperature_format);
		temperature.setText(String.format(formatString, current.getTc()));
		// �ϴû��� �ؽ�Ʈ
		skyname.setText(current.getSkyName());
		// ���� �ְ� ���
		formatString = getResources().getString(R.string.minmax_temperature_format);
		minmax.setText(String.format(formatString, current.getTmin(), current.getTmax()));
		
		// ������
		precipitation.setText(current.getPrecipitation());
		// ����
		humidity.setText(current.getHumidity());
		// ���
		surface.setText(current.getSurface());
		// ǳ��
		wdir.setText(current.getWdir());
		// ǳ��
		wspd.setText(current.getWspd());
		
		
		// �ο� ������
		TextView currentweather = (TextView) v.findViewById(R.id.fragment_forecast_textview_currentweather);
		currentweather.setText(current.toString());
		
		return v;
	}
	
	private void forecastWeatherDataSetChange() {
		
		View v = getView();
		
		// 5�ϰ� ����
		// ��¥
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 2);
		Date nextDate = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd (EEE)", Locale.KOREAN);
							
		TextView dateText = (TextView) v.findViewById(R.id.fragment_forecast_2_date);
		dateText.setText(dateFormat.format(nextDate));
		
		dateText = (TextView) v.findViewById(R.id.fragment_forecast_3_date);
		cal.add(Calendar.DATE, 1);
		nextDate = cal.getTime();
		dateText.setText(dateFormat.format(nextDate));
		
		dateText = (TextView) v.findViewById(R.id.fragment_forecast_4_date);
		cal.add(Calendar.DATE, 1);
		nextDate = cal.getTime();
		dateText.setText(dateFormat.format(nextDate));
		
		dateText = (TextView) v.findViewById(R.id.fragment_forecast_5_date);
		cal.add(Calendar.DATE, 1);
		nextDate = cal.getTime();
		dateText.setText(dateFormat.format(nextDate));
		
		dateText = (TextView) v.findViewById(R.id.fragment_forecast_6_date);
		cal.add(Calendar.DATE, 1);
		nextDate = cal.getTime();
		dateText.setText(dateFormat.format(nextDate));
		
		// ���� ���� �ϴû���
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
		
		// ���� �ְ� ���
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
		
		// �ο� ������
		TextView forecast3day = (TextView) v.findViewById(R.id.fragment_forecast_textview_forecast3day);
		TextView forecast6day = (TextView) v.findViewById(R.id.fragment_forecast_textview_forecast6day);
		forecast3day.setText(mForecast3Day.toString());
		forecast6day.setText(mForecast6Day.toString());
	}
	
}
