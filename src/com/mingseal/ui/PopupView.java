package com.mingseal.ui;

import java.util.ArrayList;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author wj 独立点自定义视图
 */
public abstract class PopupView implements OnClickListener {
	Context context;
	LayoutInflater layoutInflater;
	int resId;
	View view;
	View extendPopupView;
	View extendView;
	/**
	 * @param context
	 * @param resId
	 *            整体的布局文件id
	 */
	public PopupView(Context context, int resId) {
		layoutInflater = LayoutInflater.from(context);
		this.resId = resId;
		// 映射出layout：popup_view_item
		//用于getview
		view = layoutInflater.inflate(resId, null);
		//用于移动
		extendPopupView = layoutInflater.inflate(resId, null);
		setViewsElements(view);
		setViewsElements(extendPopupView);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		extendView = setExtendView(extendView);
		extendView.setLayoutParams(layoutParams);
		initViewAndListener(extendView);
		
	}

	/**
	 * @Title  initViewAndListener
	 * @Description 初始化视图及监听
	 * @author wj
	 * @param extendView
	 */
	public abstract void initViewAndListener(View extendView);
	
//	public abstract PointGlueAloneParam getGlueAlone();

	public View getPopupView() {
		return view;
	}

	public View getExtendPopupView() {
		return extendPopupView;
	}

	public View getExtendView() {
		return extendView;
	}

	/**
	 * @Title setViewsElements
	 * @Description 设置标题栏
	 * @author wj
	 * @param view
	 */
	public abstract void setViewsElements(View view);

	/**
	 * 设置内容
	 * 
	 * @param view
	 * @return 要想实现popuview 必须实现此方法但是不知道何时实现所以交给子类。
	 */
	public abstract View setExtendView(View view);
}
