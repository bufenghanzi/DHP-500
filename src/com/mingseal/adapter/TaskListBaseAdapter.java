/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.PointTask;
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
public class TaskListBaseAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;

	private List<PointTask> mOriginalValues;// 所有的Item
	private List<PointTask> mObjects;// 过滤后的Item
	private PointTask pointTask;
	private int selectItem = 0;

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public TaskListBaseAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.mOriginalValues = new ArrayList<PointTask>();
		mObjects = mOriginalValues;
	}

	/**
	 * Activity赋初值
	 * 
	 * @param mOriginalValues
	 */
	public void setTaskList(List<PointTask> mOriginalValues) {
		this.mOriginalValues = mOriginalValues;
		this.mObjects = mOriginalValues;
	}

	/**
	 * Activity设置选中行
	 * 
	 * @param selectItem
	 */
	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	/**
	 * Activity获取选中的值
	 * 
	 * @return
	 */
	public int getSelectItem() {
		return selectItem;
	}

	@Override
	public int getCount() {
		if (mObjects == null) {
			return 0;
		} else {
			return mObjects.size();
		}
	}

	@Override
	public PointTask getItem(int position) {
		return mObjects.get(position);
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
			convertView = mInflater.inflate(R.layout.item_task_list, null);
			holder.tv_task = (TextView) convertView.findViewById(R.id.item_task);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mObjects != null && mObjects.size() != 0) {

			holder.tv_task.setText(getItem(position).getTaskName());
		}
		if (position == selectItem) {
			convertView.setBackground(context.getResources().getDrawable(R.drawable.task_list_pressed_bg));
		} else {
			convertView.setBackground(context.getResources().getDrawable(R.drawable.task_list_normal_bg));
		}
		return convertView;
	}

	/**
	 * 匹配过滤搜索内容
	 * 
	 * @param prefix
	 *            搜索框输入内容
	 */
	public void performFiltering(CharSequence prefix) {
		if (prefix == null || prefix.length() == 0) {// 搜索框内容为空的时候显示所有的历史记录
			mObjects = mOriginalValues;
		} else {
			String prefixString = prefix.toString();
			int count = mOriginalValues.size();
			ArrayList<PointTask> newValues = new ArrayList<PointTask>(count);
			for (PointTask point : mOriginalValues) {
				String value = point.getTaskName();
				if (value.contains(prefixString)) {
					newValues.add(point);
				}
			}
			/*=================== begin ===================*/
			if (newValues.isEmpty()) {
				mObjects=mOriginalValues;
			}else {
				mObjects = newValues;
			}
			/*===================  end  ===================*/
		}
		selectItem = 0;// 默认选中搜索数据的第一条数据
		notifyDataSetChanged();
	}

	static class ViewHolder {
		private TextView tv_task;// 任务名称
	}

}
