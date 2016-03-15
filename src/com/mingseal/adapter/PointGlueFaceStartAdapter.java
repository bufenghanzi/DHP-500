/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 面起始点方案适配器
 * 
 * @author 商炎炳
 * 
 */
public class PointGlueFaceStartAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueFaceStartParam> glueStartLists;// 面起点数据集合
	private PointGlueFaceStartParam glueStart;// 面起点
	private ViewHolder holder;

	public PointGlueFaceStartAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueStartLists = new ArrayList<PointGlueFaceStartParam>();
	}

	/**
	 * Activity设置初值
	 * 
	 * @param glueStartLists
	 */
	public void setGlueStartLists(List<PointGlueFaceStartParam> glueStartLists) {
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
	public PointGlueFaceStartParam getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.item_glue_face_start, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_prev = (TextView) convertView.findViewById(R.id.item_alone_dotglue);
			holder.tv_stopGlue = (TextView) convertView.findViewById(R.id.item_alone_stopglue);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (glueStartLists != null && glueStartLists.size() != 0) {
			glueStart = getItem(position);
			holder.tv_num.setText(glueStart.get_id() + "");
			holder.tv_prev.setText(glueStart.getMoveSpeed() + "");
			holder.tv_stopGlue.setText(glueStart.getStopGlueTime() + "");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_prev;// 轨迹速度
		private TextView tv_stopGlue;// 停胶延时
	}

}
