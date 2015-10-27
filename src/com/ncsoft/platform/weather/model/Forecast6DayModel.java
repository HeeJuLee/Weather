package com.ncsoft.platform.weather.model;

import java.util.Iterator;
import java.util.List;

import com.ncsoft.platform.weather.R;

/* 
 * 중기예보 (12시간 간격, 3일~10일예보)
 */
public class Forecast6DayModel {
	
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
		
		List<Forecast6days> forecast6days = weather.getForecast6days();
		
		Iterator<Forecast6days> iterator = forecast6days.iterator();
		while(iterator.hasNext()) {
			Forecast6days forecast6day = iterator.next();
			
			sb.append("\nForecast6Days");
			sb.append("\n광역시도: " + forecast6day.getGrid().getCity());			
			sb.append("\n시군구: " + forecast6day.getGrid().getCounty());
			sb.append("\n읍면동: " + forecast6day.getGrid().getVillage());
			sb.append("\n기준지역: " + forecast6day.getLocation().getName());
			sb.append("\n하늘상태/기온: ");
			sb.append("\n +2일 오전: " + forecast6day.getSky().getAmCode2day() + ", " + forecast6day.getSky().getAmName2day());
			sb.append("\n +2일 오후: " + forecast6day.getSky().getPmCode2day() + ", " + forecast6day.getSky().getPmName2day());
			sb.append("\n +2일 최대: " + forecast6day.getTemperature().getTmax2day() + ", 최저: " + forecast6day.getTemperature().getTmin2day());
			sb.append("\n +3일 오전: " + forecast6day.getSky().getAmCode3day() + ", " +	forecast6day.getSky().getAmName3day());
			sb.append("\n +3일 오후: " + forecast6day.getSky().getPmCode3day() + ", " +	forecast6day.getSky().getPmName3day());
			sb.append("\n +3일 최대: " + forecast6day.getTemperature().getTmax3day() + ", 최저: " + forecast6day.getTemperature().getTmin3day());			
			sb.append("\n +4일 오전: " + forecast6day.getSky().getAmCode4day() + ", " +	forecast6day.getSky().getAmName4day());
			sb.append("\n +4일 오후: " + forecast6day.getSky().getPmCode4day() + ", " +	forecast6day.getSky().getPmName4day());
			sb.append("\n +4일 최대: " + forecast6day.getTemperature().getTmax4day() + ", 최저: " + forecast6day.getTemperature().getTmin4day());			
			sb.append("\n +5일 오전: " + forecast6day.getSky().getAmCode5day() + ", " +	forecast6day.getSky().getAmName5day());
			sb.append("\n +5일 오후: " + forecast6day.getSky().getPmCode5day() + ", " +	forecast6day.getSky().getPmName5day());
			sb.append("\n +5일 최대: " + forecast6day.getTemperature().getTmax5day() + ", 최저: " + forecast6day.getTemperature().getTmin5day());			
			sb.append("\n +6일 오전: " + forecast6day.getSky().getAmCode6day() + ", " +	forecast6day.getSky().getAmName6day());
			sb.append("\n +6일 오후: " + forecast6day.getSky().getPmCode6day() + ", " +	forecast6day.getSky().getPmName6day());
			sb.append("\n +6일 최대: " + forecast6day.getTemperature().getTmax6day() + ", 최저: " + forecast6day.getTemperature().getTmin6day());
			sb.append("\n +7일 오전: " + forecast6day.getSky().getAmCode7day() + ", " +	forecast6day.getSky().getAmName7day());
			sb.append("\n +7일 오후: " + forecast6day.getSky().getPmCode7day() + ", " +	forecast6day.getSky().getPmName7day());
			sb.append("\n +7일 최대: " + forecast6day.getTemperature().getTmax7day() + ", 최저: " + forecast6day.getTemperature().getTmin7day());			
			sb.append("\n +8일 오전: " + forecast6day.getSky().getAmCode8day() + ", " +	forecast6day.getSky().getAmName8day());
			sb.append("\n +8일 오후: " + forecast6day.getSky().getPmCode8day() + ", " +	forecast6day.getSky().getPmName8day());
			sb.append("\n +8일 최대: " + forecast6day.getTemperature().getTmax8day() + ", 최저: " + forecast6day.getTemperature().getTmin8day());
			sb.append("\n +9일 오전: " + forecast6day.getSky().getAmCode9day() + ", " +	forecast6day.getSky().getAmName9day());
			sb.append("\n +9일 오후: " + forecast6day.getSky().getPmCode9day() + ", " +	forecast6day.getSky().getPmName9day());
			sb.append("\n +9일 최대: " + forecast6day.getTemperature().getTmax9day() + ", 최저: " + forecast6day.getTemperature().getTmin9day());
			sb.append("\n +10일 오전: " + forecast6day.getSky().getAmCode10day() + ", " + forecast6day.getSky().getAmName10day());
			sb.append("\n +10일 오후: " + forecast6day.getSky().getPmCode10day() + ", " + forecast6day.getSky().getPmName10day());
			sb.append("\n +10일 최대: " + forecast6day.getTemperature().getTmax10day() + ", 최저: " + forecast6day.getTemperature().getTmin10day());
			sb.append("\n관측시간: " + forecast6day.getTimeRelease());
		}
		
		return sb.toString();
	}
	
	public int getTmax(int day) {
		List<Forecast6days> forecast6days = weather.getForecast6days();
		
		Iterator<Forecast6days> iterator = forecast6days.iterator();
		while(iterator.hasNext()) {
			Forecast6days forecast6day = iterator.next();
			
			switch(day) {
				case 2: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmax2day()));
				case 3: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmax3day()));
				case 4: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmax4day()));
				case 5: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmax5day()));
				case 6: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmax6day()));
				default: break;
			}
		}
		return -999;
	}
	public int getTmin(int day) {
		List<Forecast6days> forecast6days = weather.getForecast6days();
		
		Iterator<Forecast6days> iterator = forecast6days.iterator();
		while(iterator.hasNext()) {
			Forecast6days forecast6day = iterator.next();
			
			switch(day) {
				case 2: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmin2day()));
				case 3: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmin3day()));
				case 4: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmin4day()));
				case 5: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmin5day()));
				case 6: return Math.round(Float.parseFloat(forecast6day.getTemperature().getTmin6day()));
				default: break;
			}
		}
		return -999;
	}
	/*
	하늘상태코드명
	- SKY_W00: 상태없음
	- SKY_W01: 맑음
	- SKY_W02: 구름조금
	- SKY_W03: 구름많음
	- SKY_W04: 흐림
	- SKY_W07: 흐리고 비
	- SKY_W09: 구름많고 비
	- SKY_W10: 소나기
	- SKY_W11: 비 또는 눈
	- SKY_W12: 구름많고 눈
	- SKY_W13: 흐리고 눈
	*/
	public int getSkyResourceID(int day, boolean am) {
		List<Forecast6days> forecast6days = weather.getForecast6days();
		
		Iterator<Forecast6days> iterator = forecast6days.iterator();
		while(iterator.hasNext()) {
			Forecast6days forecast6day = iterator.next();
			
			String code = "SKY_W00";
			if(am) {
				switch(day) {
					case 2: code = forecast6day.getSky().getAmCode2day(); break;
					case 3: code = forecast6day.getSky().getAmCode3day(); break;
					case 4: code = forecast6day.getSky().getAmCode4day(); break;
					case 5: code = forecast6day.getSky().getAmCode5day(); break;
					case 6: code = forecast6day.getSky().getAmCode6day(); break;
					default: break;
				}
			} else {
				switch(day) {
					case 2: code = forecast6day.getSky().getPmCode2day(); break;
					case 3: code = forecast6day.getSky().getPmCode3day(); break;
					case 4: code = forecast6day.getSky().getPmCode4day(); break;
					case 5: code = forecast6day.getSky().getPmCode5day(); break;
					case 6: code = forecast6day.getSky().getPmCode6day(); break;
					default: break;
				}
			}
			
			switch(Integer.parseInt(code.substring(5))) {
				case 1: return R.drawable.weather01;
				case 2: return R.drawable.weather02;
				case 3: return R.drawable.weather03;
				case 4: return R.drawable.weather18;
				case 7: return R.drawable.weather21;
				case 9: return R.drawable.weather12;
				case 10: return R.drawable.weather21;
				case 11: return R.drawable.weather04;
				case 12: return R.drawable.weather13;
				case 13: return R.drawable.weather32;
				default: return R.drawable.weather38;	
			}
		}
		return R.drawable.weather38;
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
		List<Forecast6days> forecast6days;

		public List<Forecast6days> getForecast6days() {
			return forecast6days;
		}		
	}
	// 중기예보
	public class Forecast6days {
		private Grid grid;
		private Location location;
		private Sky sky;
		private Temperature temperature;
		private String timeRelease;
		
		public Grid getGrid() {
			return grid;
		}
		public Location getLocation() {
			return location;
		}
		public Sky getSky() {
			return sky;
		}
		public Temperature getTemperature() {
			return temperature;
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
		
		public String getCity() {
			return city;
		}
		public String getCounty() {
			return county;
		}
		public String getVillage() {
			return village;
		}
	}
	// 기준지역
	public class Location {
		private String name;

		public String getName() {
			return name;
		}
	}
	// 하늘상태
	public class Sky {
		private String amCode2day;
		private String amName2day;
		private String amCode3day;
		private String amName3day;
		private String amCode4day;
		private String amName4day;
		private String amCode5day;
		private String amName5day;
		private String amCode6day;
		private String amName6day;
		private String amCode7day;
		private String amName7day;
		private String amCode8day;
		private String amName8day;
		private String amCode9day;
		private String amName9day;
		private String amCode10day;
		private String amName10day;
		private String pmCode2day;
		private String pmName2day;
		private String pmCode3day;
		private String pmName3day;
		private String pmCode4day;
		private String pmName4day;
		private String pmCode5day;
		private String pmName5day;
		private String pmCode6day;
		private String pmName6day;
		private String pmCode7day;
		private String pmName7day;
		private String pmCode8day;
		private String pmName8day;
		private String pmCode9day;
		private String pmName9day;
		private String pmCode10day;
		private String pmName10day;
				
		public String getAmCode2day() {
			return amCode2day;
		}
		public String getAmName2day() {
			return amName2day;
		}
		public String getAmCode3day() {
			return amCode3day;
		}
		public String getAmName3day() {
			return amName3day;
		}
		public String getAmCode4day() {
			return amCode4day;
		}
		public String getAmName4day() {
			return amName4day;
		}
		public String getAmCode5day() {
			return amCode5day;
		}
		public String getAmName5day() {
			return amName5day;
		}
		public String getAmCode6day() {
			return amCode6day;
		}
		public String getAmName6day() {
			return amName6day;
		}
		public String getAmCode7day() {
			return amCode7day;
		}
		public String getAmName7day() {
			return amName7day;
		}
		public String getAmCode8day() {
			return amCode8day;
		}
		public String getAmName8day() {
			return amName8day;
		}
		public String getAmCode9day() {
			return amCode9day;
		}
		public String getAmName9day() {
			return amName9day;
		}
		public String getAmCode10day() {
			return amCode10day;
		}
		public String getAmName10day() {
			return amName10day;
		}
		public String getPmCode2day() {
			return pmCode2day;
		}
		public String getPmName2day() {
			return pmName2day;
		}
		public String getPmCode3day() {
			return pmCode3day;
		}
		public String getPmName3day() {
			return pmName3day;
		}
		public String getPmCode4day() {
			return pmCode4day;
		}
		public String getPmName4day() {
			return pmName4day;
		}
		public String getPmCode5day() {
			return pmCode5day;
		}
		public String getPmName5day() {
			return pmName5day;
		}
		public String getPmCode6day() {
			return pmCode6day;
		}
		public String getPmName6day() {
			return pmName6day;
		}
		public String getPmCode7day() {
			return pmCode7day;
		}
		public String getPmName7day() {
			return pmName7day;
		}
		public String getPmCode8day() {
			return pmCode8day;
		}
		public String getPmName8day() {
			return pmName8day;
		}
		public String getPmCode9day() {
			return pmCode9day;
		}
		public String getPmName9day() {
			return pmName9day;
		}
		public String getPmCode10day() {
			return pmCode10day;
		}
		public String getPmName10day() {
			return pmName10day;
		}
	}
	// 기온정보
	public class Temperature {
		private String tmin2day;
		private String tmax2day;
		private String tmin3day;
		private String tmax3day;
		private String tmin4day;
		private String tmax4day;
		private String tmin5day;
		private String tmax5day;
		private String tmin6day;
		private String tmax6day;
		private String tmin7day;
		private String tmax7day;
		private String tmin8day;
		private String tmax8day;
		private String tmin9day;
		private String tmax9day;
		private String tmin10day;
		private String tmax10day;
		
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
		public String getTmin4day() {
			return tmin4day;
		}
		public String getTmax4day() {
			return tmax4day;
		}
		public String getTmin5day() {
			return tmin5day;
		}
		public String getTmax5day() {
			return tmax5day;
		}
		public String getTmin6day() {
			return tmin6day;
		}
		public String getTmax6day() {
			return tmax6day;
		}
		public String getTmin7day() {
			return tmin7day;
		}
		public String getTmax7day() {
			return tmax7day;
		}
		public String getTmin8day() {
			return tmin8day;
		}
		public String getTmax8day() {
			return tmax8day;
		}
		public String getTmin9day() {
			return tmin9day;
		}
		public String getTmax9day() {
			return tmax9day;
		}
		public String getTmin10day() {
			return tmin10day;
		}
		public String getTmax10day() {
			return tmax10day;
		}
	}
}
