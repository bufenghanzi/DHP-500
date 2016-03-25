/**
 * 
 */
package com.mingseal.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.mingseal.communicate.Const;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
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
	private Handler handler;
	HashSet<Integer> mRemoved = new HashSet<Integer>();
	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

	public PointGlueFaceEndAdapter(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_glue_face_end, null);

			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
			holder.tv_lineNum = (TextView) convertView
					.findViewById(R.id.item_end_lineNum);
			holder.tv_stopGlue = (TextView) convertView
					.findViewById(R.id.item_end_stopGlueTime);
			holder.tv_upHeight = (TextView) convertView
					.findViewById(R.id.item_end_upHeight);
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
				msg.what = Const.POINTGLUEFACEEND_CLICK;
				// 发出消息
				handler.sendMessage(msg);
			}
		});
		view.setSwipeListener(mSwipeListener);
		if (glueEndLists != null && glueEndLists.size() != 0) {
			glueEnd = getItem(position);
			holder.tv_num.setText(String.valueOf(position+1));
			holder.tv_lineNum.setText(glueEnd.getLineNum() + "");
			holder.tv_stopGlue.setText(glueEnd.getStopGlueTime() + "");
			holder.tv_upHeight.setText(glueEnd.getUpHeight() + "");
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
				msg.what = Const.POINTGLUEFACEEND_TOP;
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
				msg.what = Const.POINTGLUEFACEEND_DEL;
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
		private TextView tv_stopGlue;// 停胶延时
		private TextView tv_upHeight;// 抬起高度
		private TextView tv_lineNum;// 直线条数
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
