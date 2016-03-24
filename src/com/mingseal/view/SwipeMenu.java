package com.mingseal.view;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wj
 * 存放items的类
 */
public class SwipeMenu {

	private Context mContext;
	private List<SwipeMenuItem> mItems;

	public SwipeMenu(Context context) {
		mContext = context;
		mItems = new ArrayList<SwipeMenuItem>();
	}

	public Context getContext() {
		return mContext;
	}

	/**
	 * @Title  addMenuItem
	 * @Description 添加事件
	 * @author wj
	 * @param item
	 */
	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}


}
