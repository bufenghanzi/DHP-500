/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 面结束点方案适配器
 * 
 * @author 商炎炳
 * 
 */
public class PointGlueFaceEndAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueFaceEndParam> glueEndLists;// 面结束数据集合
	private PointGlueFaceEndParam glueEnd;// 面结束点
	private ViewHolder holder;

	public PointGlueFaceEndAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueEndLists = new ArrayList<PointGlueFaceEndParam>();
	}

	/**
	 * Activity设置初值
	 * 
	 * @param glueEndLists
	 */
	public void setGlueStartLists(List<PointGlueFaceEndParam> glueEndLists) {
		this.glueEndLists = glueEndLists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return glueEndLists.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public PointGlueFaceEndParam getItem(int position) {
		return glueEndLists.get(position);
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
			convertView = mInflater.inflate(R.layout.item_glue_face_end, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_lineNum = (TextView) convertView.findViewById(R.id.item_end_lineNum);
			holder.tv_stopGlue = (TextView) convertView.findViewById(R.id.item_end_stopGlueTime);
			holder.tv_upHeight = (TextView) convertView.findViewById(R.id.item_end_upHeight);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (glueEndLists != null && glueEndLists.size() != 0) {
			glueEnd = getItem(position);
			holder.tv_num.setText(glueEnd.get_id() + "");
			holder.tv_lineNum.setText(glueEnd.getLineNum() + "");
			holder.tv_stopGlue.setText(glueEnd.getStopGlueTime() + "");
			holder.tv_upHeight.setText(glueEnd.getUpHeight() + "");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_stopGlue;// 停胶延时
		private TextView tv_upHeight;// 抬起高度
		private TextView tv_lineNum;// 直线条数
	}

}
