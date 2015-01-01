package com.ncsoft.platform.weather.Model;

import java.util.Iterator;
import java.util.List;

import android.util.Log;

/* 
 * ���糯��(�к�)
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
			
			sb.append("\n�����Ҹ�: " + minutely.getStation().getName());			
			sb.append("\n������ ������ȣ(stnid): " + minutely.getStation().getId());
			sb.append("\n������ ����: " + minutely.getStation().getLatitude());
			sb.append("\n������ �浵: " + minutely.getStation().getLongitude());
			sb.append("\nǳ��: " + minutely.getWind().getWdir());
			sb.append("\nǳ��: " + minutely.getWind().getWspd());
			// ���췮 or ������
			sb.append("\n" + minutely.getPrecipitation().getKorType() + ": " + minutely.getPrecipitation().getSinceOnTime());
			sb.append("\n�ϴû���: " + minutely.getSky().getName());
			sb.append("\n�ϴû����ڵ�: " + minutely.getSky().getCode());
			sb.append("\n������: " + minutely.getTemperature().getTc());
			sb.append("\n���� �ְ���: " + minutely.getTemperature().getTmax());
			sb.append("\n���� �������: " + minutely.getTemperature().getTmin());
			sb.append("\n����: " + minutely.getHumidity());
			sb.append("\n�����ð�: " + minutely.getTimeObservation());
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
		private List<Minutely> minutely;

		public List<Minutely> getMinutely() {
			return minutely;
		}
	}
	// ���糯��(�д���)
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
	// ����������
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
	// �ٶ�����
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
	// ��������
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
			String korType = "���췮(mm)";
			switch(type) {
				case Precipitation.PRECIPITATION_NONE:
				case Precipitation.PRECIPITATION_RAIN:
				case Precipitation.PRECIPITATION_RAIN_SNOW:
				default:
					korType = "���췮(mm)";
					break;
				case Precipitation.PRECIPITATION_SNOW:
					korType = "������(cm)";
					break;
			}
			return korType;
		}
	}
	// �ϴû�������
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
				korName = "����";
			else if(name.equalsIgnoreCase("SKY_A02"))
				korName = "��������";
			else if(name.equalsIgnoreCase("SKY_A03"))
				korName = "��������";
			else if(name.equalsIgnoreCase("SKY_A04"))
				korName = "�������� ��";
			else if(name.equalsIgnoreCase("SKY_A05"))
				korName = "�������� ��";
			else if(name.equalsIgnoreCase("SKY_A06"))
				korName = "�������� �� �Ǵ� ��";
			else if(name.equalsIgnoreCase("SKY_A07"))
				korName = "�帲";
			else if(name.equalsIgnoreCase("SKY_A08"))
				korName = "�帮�� ��";
			else if(name.equalsIgnoreCase("SKY_A09"))
				korName = "�帮�� ��";
			else if(name.equalsIgnoreCase("SKY_A10"))
				korName = "�帮�� �� �Ǵ� ��";
			else if(name.equalsIgnoreCase("SKY_A11"))
				korName = "�帮�� ����";
			else if(name.equalsIgnoreCase("SKY_A12"))
				korName = "����, ��";
			else if(name.equalsIgnoreCase("SKY_A13"))
				korName = "����, ��";
			else if(name.equalsIgnoreCase("SKY_A14"))
				korName = "����, �� �Ǵ� ��";
			else
				korName = "����";

			return korName;
		}
	}
	// ��������
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
	// �������
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
	// �������
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
