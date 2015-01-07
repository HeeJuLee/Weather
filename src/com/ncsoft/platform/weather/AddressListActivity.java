package com.ncsoft.platform.weather;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ncsoft.platform.weather.util.FileUtils;

public class AddressListActivity extends Activity {
	public static final String SELECT_ADDRESS = "select_address"; 

	EditText mEditText;
	AddressListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_address);

		ListView list = (ListView) findViewById(R.id.fragment_address_listview);
		ArrayList<String> arrayList = FileUtils.getStringArrayFromAssets(this, "address_list.txt");
		mAdapter = new AddressListAdapter(this, arrayList);
		list.setAdapter(mAdapter);
		
		mEditText = (EditText) findViewById(R.id.fragment_address_search);
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String searchString = mEditText.getText().toString();
				mAdapter.filter(searchString);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}			
		});
		
		mEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER)
					return true;
				return false;
			}
		});
	}
	private ArrayList<String> getAddressList() {
		
		return FileUtils.getStringArrayFromAssets(this, "address_list.txt");
	}
	
	public class AddressListAdapter extends BaseAdapter {
		Context mContext;
		LayoutInflater mInflater;
		ArrayList<String> mArrayList;
		ArrayList<String> mOriginList;
		
		public AddressListAdapter(Context context, ArrayList<String> arrayList) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mOriginList = arrayList;
			mArrayList = new ArrayList<String>();
			mArrayList.addAll(arrayList);
		}
		
		@Override
		public int getCount() {
			return mArrayList.size();
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public String getItem(int position) {
			return mArrayList.get(position);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.list_address_item, null);
			}
			
			TextView textView = (TextView) convertView.findViewById(R.id.list_address_item_textview);
			textView.setText(getItem(position));

			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra(AddressListActivity.SELECT_ADDRESS, getItem(position));
					
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
			return convertView;
		}
		
		public void filter(String searchStr) {
			
			mArrayList.clear();
			
			if(searchStr.length() == 0)
				mArrayList.addAll(mOriginList);
			else {
				searchStr = searchStr.toLowerCase(Locale.getDefault());
				
				for(String str: mOriginList) {
					str = str.toLowerCase(Locale.getDefault());
					if(str.contains(searchStr))
						mArrayList.add(str);
				}
			}
			
			notifyDataSetChanged();
		}
	}
}

