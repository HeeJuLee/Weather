package com.ncsoft.platform.weather;

import android.support.v4.app.Fragment;

public class ForecastActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		int pos = (Integer) getIntent().getSerializableExtra(ForecastFragment.EXTRA_ITEM_POSITION);
		
		return ForecastFragment.getInstance(pos);
	}
}
