/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 线起始点方案适配器
 * 
 * @author 商炎炳
 * 
 */
public class PointGlueLineStartAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueLineStartParam> glueStartLists;// 线起点数据集合
	private PointGlueLineStartParam glueStart;// 起始点
	private ViewHolder holder;

	public PointGlueLineStartAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueStartLists = new ArrayList<PointGlueLineStartParam>();
	}

	/**
	 * Activity设置初值
	 * 
	 * @param glueStartLists
	 */
	public void setGlueStartLists(List<PointGlueLineStartParam> glueStartLists) {
		this.glueStartLists = glueStartLists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return glueStartLists.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public PointGlueLineStartParam getItem(int position) {
		return glueStartLists.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_line_start, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_outGlue = (TextView) convertView.findViewById(R.id.item_line_outglue);
			holder.tv_moveSpeed = (TextView) convertView.findViewById(R.id.item_line_movespeed);
			holder.tv_upHeight = (TextView) convertView.findViewById(R.id.item_line_upheight);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (glueStartLists != null && glueStartLists.size() != 0) {
			glueStart = getItem(position);
			holder.tv_num.setText(glueStart.get_id() + "");
			holder.tv_outGlue.setText(glueStart.getOutGlueTimePrev() + "");
			holder.tv_moveSpeed.setText(glueStart.getMoveSpeed() + "");
			holder.tv_upHeight.setText(glueStart.getUpHeight() + "");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_outGlue;// 出胶前延时
		private TextView tv_moveSpeed;// 轨迹速度
		private TextView tv_upHeight;// 抬起高度
	}

}
