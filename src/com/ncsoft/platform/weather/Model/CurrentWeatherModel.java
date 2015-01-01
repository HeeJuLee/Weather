package com.ncsoft.platform.weather.Model;

import java.util.Iterator;
import java.util.List;

import android.util.Log;

/* 
 * 현재날씨(분별)
 */
@SuppressWarnings("unused")
public class CurrentWeatherModel {
	
	private static final String TAG = "CurrentWeatherModel";

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
		
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			sb.append("\n관측소명: " + minutely.getStation().getName());			
			sb.append("\n관측소 지점번호(stnid): " + minutely.getStation().getId());
			sb.append("\n관측소 위도: " + minutely.getStation().getLatitude());
			sb.append("\n관측소 경도: " + minutely.getStation().getLongitude());
			sb.append("\n풍향: " + minutely.getWind().getWdir());
			sb.append("\n풍속: " + minutely.getWind().getWspd());
			// 강우량 or 적설량
			sb.append("\n" + minutely.getPrecipitation().getKorType() + ": " + minutely.getPrecipitation().getSinceOnTime());
			sb.append("\n하늘상태: " + minutely.getSky().getName());
			sb.append("\n하늘상태코드: " + minutely.getSky().getCode());
			sb.append("\n현재기온: " + minutely.getTemperature().getTc());
			sb.append("\n오늘 최고기온: " + minutely.getTemperature().getTmax());
			sb.append("\n오늘 최저기온: " + minutely.getTemperature().getTmin());
			sb.append("\n습도: " + minutely.getHumidity());
			sb.append("\n관측시간: " + minutely.getTimeObservation());
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
		private List<Minutely> minutely;

		public List<Minutely> getMinutely() {
			return minutely;
		}
	}
	// 현재날씨(분단위)
	public class Minutely {
		private Station station;
		private Wind wind; 
		private Precipitation precipitation;
		private Sky sky;
		private Rain rain;
		private Temperature temperature;
		private String humidity;
		private Pressure pressure;
		private int lightning;
		private String timeObservation;
		
		public Station getStation() {
			return station;
		}
		public Wind getWind() {
			return wind;
		}
		public Precipitation getPrecipitation() {
			return precipitation;
		}
		public Sky getSky() {
			return sky;
		}
		public Rain getRain() {
			return rain;
		}
		public Temperature getTemperature() {
			return temperature;
		}
		public String getHumidity() {
			return humidity;
		}
		public Pressure getPressure() {
			return pressure;
		}
		public int getLightning() {
			return lightning;
		}		
		public String getTimeObservation() {
			return timeObservation;
		}
	}
	// 관측소정보
	public class Station {
		private String name;
		private int id;
		private String type;
		private double latitude;
		private double longitude;
		
		public String getName() {
			return name;
		}
		public int getId() {
			return id;
		}
		public String getType() {
			return type;
		}
		public double getLatitude() {
			return latitude;
		}
		public double getLongitude() {
			return longitude;
		}
	}
	// 바람정보
	public class Wind	{
		private double wdir;
		private double wspd;
		
		public double getWdir() {
			return wdir;
		}
		public double getWspd() {
			return wspd;
		}
	}
	// 강수정보
	public class Precipitation {
		public static final int PRECIPITATION_NONE = 0;
		public static final int PRECIPITATION_RAIN = 1;
		public static final int PRECIPITATION_RAIN_SNOW = 2;
		public static final int PRECIPITATION_SNOW = 3;
		
		private int type;
		private double sinceOnTime;
		
		public int getType() {
			return type;
		}
		public double getSinceOnTime() {
			return sinceOnTime;
		}
		
		public String getKorType() {
			String korType = "강우량(mm)";
			switch(type) {
				case Precipitation.PRECIPITATION_NONE:
				case Precipitation.PRECIPITATION_RAIN:
				case Precipitation.PRECIPITATION_RAIN_SNOW:
				default:
					korType = "강우량(mm)";
					break;
				case Precipitation.PRECIPITATION_SNOW:
					korType = "적설량(cm)";
					break;
			}
			return korType;
		}
	}
	// 하늘상태정보
	public class Sky {
		private String name;
		private String code;
		
		public String getName() {
			return name;
		}
		public String getCode() {
			return code;
		}
		public String getKorName() {
			String korName;
			if(name.equalsIgnoreCase("SKY_A01"))
				korName = "맑음";
			else if(name.equalsIgnoreCase("SKY_A02"))
				korName = "구름조금";
			else if(name.equalsIgnoreCase("SKY_A03"))
				korName = "구름많음";
			else if(name.equalsIgnoreCase("SKY_A04"))
				korName = "구름많고 비";
			else if(name.equalsIgnoreCase("SKY_A05"))
				korName = "구름많고 눈";
			else if(name.equalsIgnoreCase("SKY_A06"))
				korName = "구름많고 비 또는 눈";
			else if(name.equalsIgnoreCase("SKY_A07"))
				korName = "흐림";
			else if(name.equalsIgnoreCase("SKY_A08"))
				korName = "흐리고 비";
			else if(name.equalsIgnoreCase("SKY_A09"))
				korName = "흐리고 눈";
			else if(name.equalsIgnoreCase("SKY_A10"))
				korName = "흐리고 비 또는 눈";
			else if(name.equalsIgnoreCase("SKY_A11"))
				korName = "흐리고 낙뢰";
			else if(name.equalsIgnoreCase("SKY_A12"))
				korName = "뇌우, 비";
			else if(name.equalsIgnoreCase("SKY_A13"))
				korName = "뇌우, 눈";
			else if(name.equalsIgnoreCase("SKY_A14"))
				korName = "뇌우, 비 또는 눈";
			else
				korName = "맑음";

			return korName;
		}
	}
	// 강우정보
	public class Rain {
		private double sinceOntime;
		private double sinceMidnight;
		private double last10min;
		private double last15min;
		private double last30min;
		private double last1hour;
		private double last6hour;
		private double last12hour;
		private double last24hour;
		
		public double getSinceOntime() {
			return sinceOntime;
		}
		public double getSinceMidnight() {
			return sinceMidnight;
		}
		public double getLast10min() {
			return last10min;
		}
		public double getLast15min() {
			return last15min;
		}
		public double getLast30min() {
			return last30min;
		}
		public double getLast1hour() {
			return last1hour;
		}
		public double getLast6hour() {
			return last6hour;
		}
		public double getLast12hour() {
			return last12hour;
		}
		public double getLast24hour() {
			return last24hour;
		}
	}
	// 기온정보
	public class Temperature {
		private double tc;
		private double tmax;
		private double tmin;
		
		public double getTc() {
			return tc;
		}
		public double getTmax() {
			return tmax;
		}
		public double getTmin() {
			return tmin;
		}
	}
	// 기압정보
	public class Pressure {
		private double surface;
		private double seaLevel;
		
		public double getSurface() {
			return surface;
		}
		public double getSeaLevel() {
			return seaLevel;
		}
	}
}
