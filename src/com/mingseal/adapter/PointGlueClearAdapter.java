/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 清胶点方案适配器
 * 
 * @author 商炎炳
 * 
 */
public class PointGlueClearAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueClearParam> glueClearLists;// 清胶点数据集合
	private PointGlueClearParam glueClear;// 清胶点
	private ViewHolder holder;

	public PointGlueClearAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueClearLists = new ArrayList<PointGlueClearParam>();
	}

	/**
	 * Activity与Adapter赋值
	 * 
	 * @param glueClearLists
	 *            PointGlueClearParam集合
	 */
	public void setGlueClearLists(List<PointGlueClearParam> glueClearLists) {
		this.glueClearLists = glueClearLists;
	}

	@Override
	public int getCount() {
		return glueClearLists.size();
	}

	@Override
	public PointGlueClearParam getItem(int position) {
		return glueClearLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_clear, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_clearTime = (TextView) convertView.findViewById(R.id.item_clear);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (glueClearLists != null && glueClearLists.size() != 0) {
			glueClear = getItem(position);
			holder.tv_num.setText(glueClear.get_id() + "");
			holder.tv_clearTime.setText(glueClear.getClearGlueTime() + "");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_clearTime;// 清胶延时
	}

}
