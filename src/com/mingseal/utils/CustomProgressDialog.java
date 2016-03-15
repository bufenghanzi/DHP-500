/**
 * 
 */
package com.mingseal.utils;

import com.mingseal.dhp.R;
import com.mingseal.view.RoundProgressBar;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author 商炎炳
 * @description 自定义的圆形对话框
 */
public class CustomProgressDialog extends Dialog {

	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;
	private static RoundProgressBar progressBar = null;

	public CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 创建自定义的Dialog
	 * 
	 * @param context
	 * @return
	 */
	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.custom_progress_dialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		progressBar = (RoundProgressBar) customProgressDialog.findViewById(R.id.roundProgressBar);

		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}

	}

	/**
	 * 标题
	 * 
	 * @param title
	 * @return
	 */
	public CustomProgressDialog setTitle(String title) {
		return customProgressDialog;
	}

	/**
	 * 设置提示文本
	 * 
	 * @param message
	 * @return
	 */
	public CustomProgressDialog setMessage(String message) {
		TextView title = (TextView) customProgressDialog.findViewById(R.id.tv_loadingmsg);
		if (title != null) {
			title.setText(message);
		}

		return customProgressDialog;
	}

	/**
	 * 设置ProgressBar进度的最大值
	 * 
	 * @param max
	 */
	public void setMax(int max) {
		progressBar.setMax(max);
	}

	/**
	 * 设置Progress的当前进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		progressBar.setProgress(progress);
	}
	
}
