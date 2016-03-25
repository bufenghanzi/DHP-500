/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.mingseal.communicate.Const;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.dhp.R;
import com.mingseal.ui.SwipeLayout;
import com.mingseal.ui.SwipeLayout.SwipeListener;
import com.mingseal.utils.ToastUtil;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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
	HashSet<Integer> mRemoved = new HashSet<Integer>();
	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();
	private Handler handler;
	public PointGlueLineStartAdapter(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_line_start, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_outGlue = (TextView) convertView.findViewById(R.id.item_line_outglue);
			holder.tv_moveSpeed = (TextView) convertView.findViewById(R.id.item_line_movespeed);
			holder.mButtonTOP = (Button) convertView.findViewById(R.id.bt_top);
			holder.mButtonDel = (Button) convertView
					.findViewById(R.id.bt_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SwipeLayout view = (SwipeLayout) convertView;
		view.close(false, false);
		view.getFrontView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ToastUtil.displayPromptInfo(context, "item click: " + position);
				Message msg = new Message();
				Bundle data = new Bundle();
				// 设置选中索引
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = Const.POINTGLUELINESTART_CLICK;
				// 发出消息
				handler.sendMessage(msg);
			}
		});
		view.setSwipeListener(mSwipeListener);
		if (glueStartLists != null && glueStartLists.size() != 0) {
			glueStart = getItem(position);
			holder.tv_num.setText(String.valueOf(position+1) + "");
			holder.tv_outGlue.setText(glueStart.getOutGlueTimePrev() + "");
			holder.tv_moveSpeed.setText(glueStart.getMoveSpeed() + "");
//			holder.tv_upHeight.setText(glueStart.getUpHeight() + "");
		}

		holder.mButtonTOP.setTag(position);
		holder.mButtonTOP.setOnClickListener(onActionClick);

		holder.mButtonDel.setTag(position);
		holder.mButtonDel.setOnClickListener(onActionClick);
		return convertView;
	}
	OnClickListener onActionClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Integer position = (Integer) v.getTag();
			int id = v.getId();
			if (id == R.id.bt_top) {
				closeAllLayout();
//				ToastUtil.displayPromptInfo(context, "position: " + position+ " top");
				Message msg = new Message();
				Bundle data = new Bundle();
				// 设置选中索引
				data.putInt("top_Index", position);
				msg.setData(data);
				msg.what = Const.POINTGLUELINESTART_TOP;
				// 发出消息
				handler.sendMessage(msg);
			} else if (id == R.id.bt_delete) {
				closeAllLayout();
//				ToastUtil.displayPromptInfo(context, "position: " + position+ " del");
				Message msg = new Message();
				Bundle data = new Bundle();
				// 设置选中索引
				data.putInt("del_Index", position);
				msg.setData(data);
				msg.what = Const.POINTGLUELINESTART_DEL;
				// 发出消息
				handler.sendMessage(msg);
			}
		}
	};
	SwipeListener mSwipeListener = new SwipeListener() {
		@Override
		public void onOpen(SwipeLayout swipeLayout) {
			// ToastUtil.displayPromptInfo(context, "onOpen");
			mUnClosedLayouts.add(swipeLayout);
		}

		@Override
		public void onClose(SwipeLayout swipeLayout) {
			// ToastUtil.displayPromptInfo(context, "onClose");
			mUnClosedLayouts.remove(swipeLayout);
		}

		@Override
		public void onStartClose(SwipeLayout swipeLayout) {
			// ToastUtil.displayPromptInfo(context, "onStartClose");
		}

		@Override
		public void onStartOpen(SwipeLayout swipeLayout) {
			// ToastUtil.displayPromptInfo(context, "onStartOpen");
			closeAllLayout();
			mUnClosedLayouts.add(swipeLayout);
		}

	};

	private class ViewHolder {
		private TextView tv_num;// 方案号
		private TextView tv_outGlue;// 出胶前延时
		private TextView tv_moveSpeed;// 轨迹速度
//		private TextView tv_upHeight;// 抬起高度
		private Button mButtonTOP;// 置顶
		public Button mButtonDel;// 删除
	}
	protected void closeAllLayout() {
		if (mUnClosedLayouts.size() == 0)
			return;

		for (SwipeLayout l : mUnClosedLayouts) {
			l.close(true, false);
		}
		mUnClosedLayouts.clear();
	}

	public int getUnClosedCount() {
		return mUnClosedLayouts.size();
	}

}
