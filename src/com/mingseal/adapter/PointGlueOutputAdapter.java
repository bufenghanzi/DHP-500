/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
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
public class PointGlueOutputAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater mInflater;
	private List<PointGlueOutputIOParam> outputIOParams;// 输出IO点集合
	private PointGlueOutputIOParam outputIO;// 输出IO点

	public PointGlueOutputAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.outputIOParams = new ArrayList<PointGlueOutputIOParam>();
	}

	/**
	 * Activity赋初值
	 * 
	 * @param outputIOParams
	 */
	public void setOutputIOParams(List<PointGlueOutputIOParam> outputIOParams) {
		this.outputIOParams = outputIOParams;
	}

	@Override
	public int getCount() {
		return outputIOParams.size();
	}

	@Override
	public PointGlueOutputIOParam getItem(int position) {
		return outputIOParams.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_output, null);
			
			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_goTimePrev = (TextView) convertView.findViewById(R.id.item_goTimePrev);
			holder.tv_goTimeNext = (TextView) convertView.findViewById(R.id.item_goTimeNext);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(outputIOParams!=null&&outputIOParams.size()!=0){
			outputIO = getItem(position);
			
			holder.tv_num.setText(outputIO.get_id()+"");
			holder.tv_goTimePrev.setText(outputIO.getGoTimePrev()+"");
			holder.tv_goTimeNext.setText(outputIO.getGoTimeNext()+"");
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_goTimePrev;// 动作前延时
		private TextView tv_goTimeNext;// 动作后延时
	}

}
