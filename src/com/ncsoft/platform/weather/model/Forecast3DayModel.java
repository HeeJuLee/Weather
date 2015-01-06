package com.ncsoft.platform.weather.model;

import java.util.Iterator;
import java.util.List;

import com.ncsoft.platform.weather.model.Forecast6DayModel.Common;
import com.ncsoft.platform.weather.model.Forecast6DayModel.Forecast6days;
import com.ncsoft.platform.weather.model.Forecast6DayModel.Grid;
import com.ncsoft.platform.weather.model.Forecast6DayModel.Result;
import com.ncsoft.platform.weather.model.Forecast6DayModel.Weather;

/* 
 * �ܱ⿹�� (3�ð� ����, 4�ð�~3�Ͽ���)
 */
public class Forecast3DayModel {

	private Result result;		
	private Common common;
	private Weather weather;
	
	public Result getResult() {
		return result;
	}
	public Common getCommon() {
		return common;
	}
	public Weather getWeather() {
		return weather;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		List<Forecast3days> forecast3days = weather.getForecast3days();
		
		Iterator<Forecast3days> iterator = forecast3days.iterator();
		while(iterator.hasNext()) {
			Forecast3days forecast3day = iterator.next();
			
			sb.append("\nForecast3Days");
			sb.append("\n�����õ�: " + forecast3day.getGrid().getCity());			
			sb.append("\n�ñ���: " + forecast3day.getGrid().getCounty());
			sb.append("\n���鵿: " + forecast3day.getGrid().getVillage());
			sb.append("\n����: " + forecast3day.getGrid().getLatitude());
			sb.append("\n�浵: " + forecast3day.getGrid().getLongitude());
			sb.append("\n���: ");
			sb.append("\n ���� ����: " + forecast3day.getFcstdaily().getTemperature().getTmin2day());
			sb.append("\n ���� �ְ�: " + forecast3day.getFcstdaily().getTemperature().getTmax2day());
			sb.append("\n �� ����: " + forecast3day.getFcstdaily().getTemperature().getTmin3day());
			sb.append("\n �� �ְ�: " + forecast3day.getFcstdaily().getTemperature().getTmax3day());
		}
		
		return sb.toString();
	}
	// ��û���
	public class Result {
		private String message;
		private int code;
		private String requestUrl;
		
		public String getMessage() {
			return message;
		}
		public int getCode() {
			return code;
		}
		public String getRequestUrl() {
			return requestUrl;
		}
	}
	// �������
	public class Common {
		private String alertYn;
		private String stormYn;
		
		public String getAlertYn() {
			return alertYn;
		}
		public String getStormYn() {
			return stormYn;
		}
	}
	//��������
	public class Weather {
		List<Forecast3days> forecast3days;

		public List<Forecast3days> getForecast3days() {
			return forecast3days;
		}		
	}
	// �ܱ⿹��
	public class Forecast3days {
		private Grid grid;
		private Fcstdaily fcstdaily;
		
		public Grid getGrid() {
			return grid;
		}
		public Fcstdaily getFcstdaily() {
			return fcstdaily;
		}
	}
	// ��������
	public class Grid {
		private String city;
		private String county;
		private String village;
		private String latitude;
		private String longitude;
		
		public String getCity() {
			return city;
		}
		public String getCounty() {
			return county;
		}
		public String getVillage() {
			return village;
		}
		public String getLatitude() {
			return latitude;
		}
		public String getLongitude() {
			return longitude;
		}
	}
	// 24 �ð� ����
	public class Fcstdaily {
		private Temperature temperature;
		
		public Temperature getTemperature() {
			return temperature;
		}
	}
	// �������
	public class Temperature {
		private String tmin2day;
		private String tmax2day;
		private String tmin3day;
		private String tmax3day;
		
		public String getTmin2day() {
			return tmin2day;
		}
		public String getTmax2day() {
			return tmax2day;
		}
		public String getTmin3day() {
			return tmin3day;
		}
		public String getTmax3day() {
			return tmax3day;
		}
	}
}
