package com.ncsoft.platform.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class WeatherListActivity extends FragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		
		FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = new WeatherListFragment();
            manager.beginTransaction()
                .add(R.id.container, fragment)
                .commit();
        } 
	}
}
