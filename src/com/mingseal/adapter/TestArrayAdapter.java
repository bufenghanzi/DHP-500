package com.mingseal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author wj
 * 自定义阵列spinner适配器，修改文字大小。
 */
public class TestArrayAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] mStringArray;

	public TestArrayAdapter(Context context, String[] stringArray) {
		super(context, android.R.layout.simple_spinner_item, stringArray);
		mContext = context;
		mStringArray = stringArray;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		//修改Spinner展开后的字体颜色 
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);
		}

		//此处text1是Spinner默认的用来显示文字的TextView 
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(mStringArray[position]);
		tv.setTextSize(23f);//字体大小
		tv.setTextColor(Color.RED);//颜色
		tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);//设置居中
		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 修改Spinner选择后结果的字体颜色
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
		}

		//此处text1是Spinner默认的用来显示文字的TextView
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(mStringArray[position]);
		tv.setTextSize(23f);
		tv.setTextColor(Color.BLUE);
		tv.setGravity(android.view.Gravity.CENTER_HORIZONTAL);//设置居中
		return convertView;
	}
}
