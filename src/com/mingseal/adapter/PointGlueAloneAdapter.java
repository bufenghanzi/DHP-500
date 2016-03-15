package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.dhp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 独立点方案列表适配器
 * 
 * @author 商炎炳
 *
 */
public class PointGlueAloneAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueAloneParam> glueAloneLists;// 独立点数据集合
	private PointGlueAloneParam glueAlone;// 独立点
	private ViewHolder holder;

	public PointGlueAloneAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueAloneLists = new ArrayList<PointGlueAloneParam>();
	}

	/**
	 * Activity设置初值
	 * 
	 * @param glueAloneLists
	 */
	public void setGlueAloneLists(List<PointGlueAloneParam> glueAloneLists) {
		this.glueAloneLists = glueAloneLists;
	}

	@Override
	public int getCount() {
		return glueAloneLists.size();
	}

	@Override
	public PointGlueAloneParam getItem(int position) {
		return glueAloneLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_alone, null);
			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_dotGlue = (TextView) convertView.findViewById(R.id.item_alone_dotglue);
			holder.tv_stopGlue = (TextView) convertView.findViewById(R.id.item_alone_stopglue);
			holder.tv_upHeight = (TextView) convertView.findViewById(R.id.item_alone_upheight);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (glueAloneLists != null && glueAloneLists.size() != 0) {
			glueAlone = getItem(position);// 调整为getItem()试试
			holder.tv_num.setText(glueAlone.get_id() + "");
			holder.tv_dotGlue.setText(glueAlone.getDotGlueTime() + "");
			holder.tv_stopGlue.setText(glueAlone.getStopGlueTime() + "");
			holder.tv_upHeight.setText(glueAlone.getUpHeight() + "");
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_dotGlue;// 点胶延时
		private TextView tv_stopGlue;// 停胶延时
		private TextView tv_upHeight;// 抬起高度
	}

}
