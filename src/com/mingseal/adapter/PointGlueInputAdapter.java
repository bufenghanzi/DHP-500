/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author 商炎炳
 *
 */
public class PointGlueInputAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueInputIOParam> inputIOParams;// 输出IO点集合
	private PointGlueInputIOParam inputIO;// 输出IO点

	public PointGlueInputAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.inputIOParams = new ArrayList<PointGlueInputIOParam>();
	}

	/**
	 * Activity赋初值
	 * 
	 * @param inputIOParams
	 */
	public void setInputIOParams(List<PointGlueInputIOParam> inputIOParams) {
		this.inputIOParams = inputIOParams;
	}

	@Override
	public int getCount() {
		return inputIOParams.size();
	}

	@Override
	public PointGlueInputIOParam getItem(int position) {
		return inputIOParams.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_input, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_goTimePrev = (TextView) convertView.findViewById(R.id.item_goTimePrev);
			holder.tv_goTimeNext = (TextView) convertView.findViewById(R.id.item_goTimeNext);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (inputIOParams != null && inputIOParams.size() != 0) {
			inputIO = getItem(position);

			holder.tv_num.setText(inputIO.get_id() + "");
			holder.tv_goTimePrev.setText(inputIO.getGoTimePrev() + "");
			holder.tv_goTimeNext.setText(inputIO.getGoTimeNext() + "");
		}

		return convertView;
	}

	private static class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_goTimePrev;// 动作前延时
		private TextView tv_goTimeNext;// 动作后延时
	}

}
