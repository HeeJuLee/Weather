package com.ncsoft.platform.weather;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

		convertView.setTag(getItem(position));
		
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