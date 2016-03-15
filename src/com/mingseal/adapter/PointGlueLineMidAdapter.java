/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 中间点的方案适配器
 * 
 * @author 商炎炳
 *
 */
public class PointGlueLineMidAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueLineMidParam> glueMidLists;// 线起点数据集合
	private PointGlueLineMidParam glueMid;// 起始点
	private ViewHolder holder;

	public PointGlueLineMidAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueMidLists = new ArrayList<PointGlueLineMidParam>();
	}

	/**
	 * Activity设置初值
	 * 
	 * @param glueMidLists
	 */
	public void setGlueMidLists(List<PointGlueLineMidParam> glueMidLists) {
		this.glueMidLists = glueMidLists;
	}

	@Override
	public int getCount() {
		return glueMidLists.size();
	}

	@Override
	public PointGlueLineMidParam getItem(int position) {
		return glueMidLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_line_mid, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_radius = (TextView) convertView.findViewById(R.id.item_mid_radius);
			holder.tv_stopPrev = (TextView) convertView.findViewById(R.id.item_mid_stopDisPrev);
			holder.tv_stopNext = (TextView) convertView.findViewById(R.id.item_mid_stopDisNext);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (glueMidLists != null && glueMidLists.size() != 0) {
			glueMid = getItem(position);
			holder.tv_num.setText(glueMid.get_id() + "");
			holder.tv_radius.setText(glueMid.getRadius() + "");
			holder.tv_stopPrev.setText(glueMid.getStopGlueDisPrev() + "");
			holder.tv_stopNext.setText(glueMid.getStopGLueDisNext() + "");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_radius;// 圆角半径
		private TextView tv_stopPrev;// 停胶前延时
		private TextView tv_stopNext;// 停胶后延时
	}

}
