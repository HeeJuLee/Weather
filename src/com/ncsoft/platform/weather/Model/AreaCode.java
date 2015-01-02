package com.ncsoft.platform.weather.model;

public class AreaCode {
	
	private String mLevel1;
	private String mLevel2;
	private String mLevel3;
	private double mLatitude;
	private double mLongitude;
	private int mDongCode;
	private int mNx;				
	private int mNy;
	
	public String getLevel1() {
		return mLevel1;
	}
	
	public void setLevel1(String level1) {
		mLevel1 = level1;
	}
	
	public String getLevel2() {
		return mLevel2;
	}
	
	public void setLevel2(String level2) {
		mLevel2 = level2;
	}
	
	public String getLevel3() {
		return mLevel3;
	}
	
	public void setLevel3(String level3) {
		mLevel3 = level3;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}
	
	public int getDongCode() {
		return mDongCode;
	}
	
	public void setDongCode(int dongCode) {
		mDongCode = dongCode;
	}
	
	public int getNx() {
		return mNx;
	}
	
	public void setNx(int nx) {
		mNx = nx;
	}
	
	public int getNy() {
		return mNy;
	}
	
	public void setNy(int ny) {
		mNy = ny;
	}
}
