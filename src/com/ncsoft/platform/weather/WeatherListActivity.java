package com.ncsoft.platform.weather;

import android.support.v4.app.Fragment;

public class WeatherListActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		return new WeatherListFragment();
	}
	
	@Override
	protected int getContainerLayoutResId() {
		return R.layout.activity_masterdetail;
	}
}
