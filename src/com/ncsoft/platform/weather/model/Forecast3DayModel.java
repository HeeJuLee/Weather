package com.ncsoft.platform.weather.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
	
/* 
 * 단기예보 (3시간 간격, 4시간~3일예보)
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
			sb.append("\n광역시도: " + forecast3day.getGrid().getCity());			
			sb.append("\n시군구: " + forecast3day.getGrid().getCounty());
			sb.append("\n읍면동: " + forecast3day.getGrid().getVillage());
			sb.append("\n위도: " + forecast3day.getGrid().getLatitude());
			sb.append("\n경도: " + forecast3day.getGrid().getLongitude());
			sb.append("\n기온: ");
			sb.append("\n 내일 최저: " + forecast3day.getFcstdaily().getTemperature().getTmin2day());
			sb.append("\n 내일 최고: " + forecast3day.getFcstdaily().getTemperature().getTmax2day());
			sb.append("\n 모레 최저: " + forecast3day.getFcstdaily().getTemperature().getTmin3day());
			sb.append("\n 모레 최고: " + forecast3day.getFcstdaily().getTemperature().getTmax3day());		
			sb.append("\n관측시간: " + forecast3day.getTimeRelease());
		}
		
		return sb.toString();
	}
	// 요청결과
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
	// 공통사항
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
	//날씨정보
	public class Weather {
		List<Forecast3days> forecast3days;

		public List<Forecast3days> getForecast3days() {
			return forecast3days;
		}		
	}
	// 단기예보
	public class Forecast3days {
		private Grid grid;
		private Fcst3hour fcst3hour;
		private Fcstdaily fcstdaily;
		private String timeRelease;
		
		public Grid getGrid() {
			return grid;
		}
		public Fcst3hour getFcst3hour() {
			return fcst3hour;
		}
		public Fcstdaily getFcstdaily() {
			return fcstdaily;
		}
		public String getTimeRelease() {
			return timeRelease;
		}
	}
	// 격자정보
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
	// 3시간 예보
	public class Fcst3hour {
		private Sky sky;
		private Temperature temperature;
		
		public class Sky {
			
		}
		public class Temperature {
			
		}
	}
	// 24 시간 예보
	public class Fcstdaily {
		private Temperature temperature;
		
		public Temperature getTemperature() {
			return temperature;
		}
		// 기온정보
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
}
