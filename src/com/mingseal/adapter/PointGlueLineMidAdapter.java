///**
// * 
// */
//package com.mingseal.adapter;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import com.mingseal.communicate.Const;
//import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
//import com.mingseal.dhp.R;
//import com.mingseal.ui.SwipeLayout;
//import com.mingseal.ui.SwipeLayout.SwipeListener;
//import com.mingseal.utils.ToastUtil;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//
///**
// * 中间点的方案适配器
// * 
// * @author 商炎炳
// * 
// */
//public class PointGlueLineMidAdapter extends BaseAdapter {
//
//	private Context context;
//	private LayoutInflater mInflater;
//	private List<PointGlueLineMidParam> glueMidLists;// 线起点数据集合
//	private PointGlueLineMidParam glueMid;// 起始点
//	private ViewHolder holder;
//	HashSet<Integer> mRemoved = new HashSet<Integer>();
//	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();
//	private Handler handler;
//
//	public PointGlueLineMidAdapter(Context context, Handler handler) {
//		super();
//		this.context = context;
//		this.handler = handler;
//		this.mInflater = LayoutInflater.from(context);
//		this.glueMidLists = new ArrayList<PointGlueLineMidParam>();
//	}
//
//	/**
//	 * Activity设置初值
//	 * 
//	 * @param glueMidLists
//	 */
//	public void setGlueMidLists(List<PointGlueLineMidParam> glueMidLists) {
//		this.glueMidLists = glueMidLists;
//	}
//
//	@Override
//	public int getCount() {
//		return glueMidLists.size();
//	}
//
//	@Override
//	public PointGlueLineMidParam getItem(int position) {
//		return glueMidLists.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.item_glue_line_mid, null);
//
//			holder.tv_num = (TextView) convertView.findViewById(R.id.item_num);
//			holder.tv_radius = (TextView) convertView
//					.findViewById(R.id.item_mid_radius);
//			holder.tv_stopPrev = (TextView) convertView
//					.findViewById(R.id.item_mid_stopDisPrev);
//			holder.tv_stopNext = (TextView) convertView
//					.findViewById(R.id.item_mid_stopDisNext);
//			holder.mButtonTOP = (Button) convertView.findViewById(R.id.bt_top);
//			holder.mButtonDel = (Button) convertView
//					.findViewById(R.id.bt_delete);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		SwipeLayout view = (SwipeLayout) convertView;
//		view.close(false, false);
//		view.getFrontView().setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
////				ToastUtil.displayPromptInfo(context, "item click: " + position);
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				// 设置选中索引
//				data.putInt("selIndex", position);
//				msg.setData(data);
//				msg.what = Const.POINTGLUELINEMID_CLICK;
//				// 发出消息
//				handler.sendMessage(msg);
//			}
//		});
//		view.setSwipeListener(mSwipeListener);
//		if (glueMidLists != null && glueMidLists.size() != 0) {
//			glueMid = getItem(position);
//			holder.tv_num.setText(String.valueOf(position+1) + "");
//			holder.tv_radius.setText(glueMid.getRadius() + "");
//			holder.tv_stopPrev.setText(glueMid.getStopGlueDisPrev() + "");
//			holder.tv_stopNext.setText(glueMid.getStopGLueDisNext() + "");
//		}
//		holder.mButtonTOP.setTag(position);
//		holder.mButtonTOP.setOnClickListener(onActionClick);
//
//		holder.mButtonDel.setTag(position);
//		holder.mButtonDel.setOnClickListener(onActionClick);
//
//		return convertView;
//	}
//	OnClickListener onActionClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Integer position = (Integer) v.getTag();
//			int id = v.getId();
//			if (id == R.id.bt_top) {
//				closeAllLayout();
////				ToastUtil.displayPromptInfo(context, "position: " + position+ " top");
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				// 设置选中索引
//				data.putInt("top_Index", position);
//				msg.setData(data);
//				msg.what = Const.POINTGLUELINEMID_TOP;
//				// 发出消息
//				handler.sendMessage(msg);
//			} else if (id == R.id.bt_delete) {
//				closeAllLayout();
////				ToastUtil.displayPromptInfo(context, "position: " + position+ " del");
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				// 设置选中索引
//				data.putInt("del_Index", position);
//				msg.setData(data);
//				msg.what = Const.POINTGLUELINEMID_DEL;
//				// 发出消息
//				handler.sendMessage(msg);
//			}
//		}
//	};
//	SwipeListener mSwipeListener = new SwipeListener() {
//		@Override
//		public void onOpen(SwipeLayout swipeLayout) {
//			// ToastUtil.displayPromptInfo(context, "onOpen");
//			mUnClosedLayouts.add(swipeLayout);
//		}
//
//		@Override
//		public void onClose(SwipeLayout swipeLayout) {
//			// ToastUtil.displayPromptInfo(context, "onClose");
//			mUnClosedLayouts.remove(swipeLayout);
//		}
//
//		@Override
//		public void onStartClose(SwipeLayout swipeLayout) {
//			// ToastUtil.displayPromptInfo(context, "onStartClose");
//		}
//
//		@Override
//		public void onStartOpen(SwipeLayout swipeLayout) {
//			// ToastUtil.displayPromptInfo(context, "onStartOpen");
//			closeAllLayout();
//			mUnClosedLayouts.add(swipeLayout);
//		}
//
//	};
//
//	private class ViewHolder {
//		private TextView tv_num;// 方案号
//		private TextView tv_radius;// 圆角半径
//		private TextView tv_stopPrev;// 停胶前延时
//		private TextView tv_stopNext;// 停胶后延时
//		private Button mButtonTOP;// 置顶
//		public Button mButtonDel;// 删除
//	}
//	protected void closeAllLayout() {
//		if (mUnClosedLayouts.size() == 0)
//			return;
//
//		for (SwipeLayout l : mUnClosedLayouts) {
//			l.close(true, false);
//		}
//		mUnClosedLayouts.clear();
//	}
//
//	public int getUnClosedCount() {
//		return mUnClosedLayouts.size();
//	}
//
//}
