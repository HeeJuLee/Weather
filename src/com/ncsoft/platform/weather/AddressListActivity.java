package com.ncsoft.platform.weather;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.ncsoft.platform.weather.util.FileUtils;

public class AddressListActivity extends Activity {
	public static final String SELECT_ADDRESS = "select_address"; 

	AddressListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_addresslist);

		ListView listView = (ListView) findViewById(R.id.fragment_address_listview);
		EditText editText = (EditText) findViewById(R.id.fragment_address_search);
		
		ArrayList<String> arrayList = FileUtils.getStringArrayFromAssets(this, "address_list.txt");

		mAdapter = new AddressListAdapter(this, arrayList);
		listView.setAdapter(mAdapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				
				Intent intent = new Intent();
				intent.putExtra(AddressListActivity.SELECT_ADDRESS, (String) view.getTag());
				
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		
		
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mAdapter.filter(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}			
		});
		
		
		editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER)
					return true;
				return false;
			}
		});
	}
}

