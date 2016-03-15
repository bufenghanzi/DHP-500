/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
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
public class PointGlueLineEndAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueLineEndParam> glueEndLists;// 线结束点集合
	private PointGlueLineEndParam glueEnd;// 线结束点

	public PointGlueLineEndAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.glueEndLists = new ArrayList<PointGlueLineEndParam>();
	}

	/**
	 * Activity赋值
	 * 
	 * @param glueEndLists
	 */
	public void setGlueEndLists(List<PointGlueLineEndParam> glueEndLists) {
		this.glueEndLists = glueEndLists;
	}

	@Override
	public int getCount() {
		return glueEndLists.size();
	}

	@Override
	public PointGlueLineEndParam getItem(int position) {
		return glueEndLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_line_end, null);
			
			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_stopPrevTime = (TextView) convertView.findViewById(R.id.item_line_stopGlueTimePrev);
			holder.tv_stopTime = (TextView) convertView.findViewById(R.id.item_line_stopGlueTime);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(glueEndLists!=null&&glueEndLists.size()!=0){
			glueEnd = getItem(position);
			
			holder.tv_num.setText(glueEnd.get_id()+"");
			holder.tv_stopPrevTime.setText(glueEnd.getStopGlueTimePrev()+"");
			holder.tv_stopTime.setText(glueEnd.getStopGlueTime()+"");
			
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_stopPrevTime;// 停胶前延时
		private TextView tv_stopTime;// 停胶后延时
	}

}
