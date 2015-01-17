package com.ncsoft.platform.weather.model;

import java.util.Iterator;
import java.util.List;

import com.ncsoft.platform.weather.R;

/* 
 * ���糯��(�к�)
 */
public class CurrentWeatherModel {

	private String mAddress;
	
	private Result result;		
	private Common common;		
	private Weather weather;
		
	public String getAddress() {
		return mAddress;
	}
	public void setAddress(String address) {
		mAddress = address;
	}
	public Result getResult() {
		return result;
	}
	public Common getCommon() {
		return common;
	}
	public Weather getWeather() {
		return weather;
	}
	
	public String getStation() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getStation().getName();
		}
		return "";
	}
	
	public int getTc() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return Math.round(Float.parseFloat(minutely.getTemperature().getTc()));
		}
		return -999;
	}
	
	public int getTmax() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return Math.round(Float.parseFloat(minutely.getTemperature().getTmax()));
		}
		return -999;
	}
	
	public int getTmin() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return Math.round(Float.parseFloat(minutely.getTemperature().getTmin()));
		}
		return -999;
	}
	
	public double getLatitude() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return Double.parseDouble(minutely.getStation().getLatitude());
		}
		return 0;
	}
	
	public double getLongitude() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return Double.parseDouble(minutely.getStation().getLongitude());
		}
		return 0;
	}
	
	public String getSkyName() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getSky().getName();
		}
		return "";
	}
	
	public String getWdir() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getWind().getWdir();
		}
		return "";
	}
	
	public String getWspd() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getWind().getWspd();
		}
		return "";
	}
	
	public String getPrecipitation() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getPrecipitation().getSinceOntime();
		}
		return "";
	}
	
	public String getHumidity() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getHumidity();
		}
		return "";
	}
	
	public String getSurface() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			return minutely.getPressure().getSurface();
		}
		return "";
	}

	/*
	�ϴû����ڵ��
	- SKY_A01: ����
	- SKY_A02: ��������
	- SKY_A03: ��������
	- SKY_A04: �������� ��
	- SKY_A05: �������� ��
	- SKY_A06: �������� �� �Ǵ� ��
	- SKY_A07: �帲
	- SKY_A08: �帮�� ��
	- SKY_A09: �帮�� ��
	- SKY_A10:  �帮�� �� �Ǵ� ��
	- SKY_A11: �帮�� ����
	- SKY_A12: ����, ��
	- SKY_A13: ����, ��
	- SKY_A14: ����, �� �Ǵ� ��
	*/
	public int getSkyResourceID() {
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			String code = minutely.getSky().getCode();
			if(code.length() == 7) {
				switch(Integer.parseInt(code.substring(5))) {
					case 1: return R.drawable.weather01;
					case 2: return R.drawable.weather02;
					case 3: return R.drawable.weather03;
					case 4: return R.drawable.weather12;
					case 5: return R.drawable.weather13;
					case 6: return R.drawable.weather14;
					case 7: return R.drawable.weather18;
					case 8: return R.drawable.weather21;
					case 9: return R.drawable.weather32;
					case 10: return R.drawable.weather04;
					case 11: return R.drawable.weather29;
					case 12: return R.drawable.weather26;
					case 13: return R.drawable.weather27;
					case 14: return R.drawable.weather28;
					default: return R.drawable.weather38;			
				}		
			}
		}
		return R.drawable.weather38;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		List<Minutely> minutelys = weather.getMinutely();
		
		Iterator<Minutely> iterator = minutelys.iterator();
		while(iterator.hasNext()) {
			Minutely minutely = iterator.next();
			
			sb.append("\nCurrentWeather");
			sb.append("\n�����Ҹ�: " + minutely.getStation().getName());			
			sb.append("\n������ ������ȣ(stnid): " + minutely.getStation().getId());
			sb.append("\n������ ����: " + minutely.getStation().getLatitude());
			sb.append("\n������ �浵: " + minutely.getStation().getLongitude());
			sb.append("\nǳ��: " + minutely.getWind().getWdir());
			sb.append("\nǳ��: " + minutely.getWind().getWspd());
			// ���췮 or ������
			sb.append("\n" + minutely.getPrecipitation().getKorType() + ": " + minutely.getPrecipitation().getSinceOntime());
			sb.append("\n�ϴû���: " + minutely.getSky().getName());
			sb.append("\n�ϴû����ڵ�: " + minutely.getSky().getCode());
			sb.append("\n������: " + minutely.getTemperature().getTc());
			sb.append("\n���� �ְ���: " + minutely.getTemperature().getTmax());
			sb.append("\n���� �������: " + minutely.getTemperature().getTmin());
			sb.append("\n����: " + minutely.getHumidity());
			sb.append("\n���: " + minutely.getPressure().getSurface());
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
		private String lightning;
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
		public String getLightning() {
			return lightning;
		}		
		public String getTimeObservation() {
			return timeObservation;
		}
	}
	// ����������
	public class Station {
		private String name;
		private String id;
		private String type;
		private String latitude;
		private String longitude;
		
		public String getName() {
			return name;
		}
		public String getId() {
			return id;
		}
		public String getType() {
			return type;
		}
		public String getLatitude() {
			return latitude;
		}
		public String getLongitude() {
			return longitude;
		}
	}
	// �ٶ�����
	public class Wind	{
		private String wdir;
		private String wspd;
		
		public String getWdir() {
			return wdir;
		}
		public String getWspd() {
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
		private String sinceOntime;
		
		public int getType() {
			return type;
		}
		public String getSinceOntime() {
			return sinceOntime;
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
		private String sinceOntime;
		private String sinceMidnight;
		private String last10min;
		private String last15min;
		private String last30min;
		private String last1hour;
		private String last6hour;
		private String last12hour;
		private String last24hour;
		
		public String getSinceOntime() {
			return sinceOntime;
		}
		public String getSinceMidnight() {
			return sinceMidnight;
		}
		public String getLast10min() {
			return last10min;
		}
		public String getLast15min() {
			return last15min;
		}
		public String getLast30min() {
			return last30min;
		}
		public String getLast1hour() {
			return last1hour;
		}
		public String getLast6hour() {
			return last6hour;
		}
		public String getLast12hour() {
			return last12hour;
		}
		public String getLast24hour() {
			return last24hour;
		}
	}
	// �������
	public class Temperature {
		private String tc;
		private String tmax;
		private String tmin;
		
		public String getTc() {
			return tc;
		}
		public String getTmax() {
			return tmax;
		}
		public String getTmin() {
			return tmin;
		}
	}
	// �������
	public class Pressure {
		private String surface;
		private String seaLevel;
		
		public String getSurface() {
			return surface;
		}
		public String getSeaLevel() {
			return seaLevel;
		}
	}
}

		
		