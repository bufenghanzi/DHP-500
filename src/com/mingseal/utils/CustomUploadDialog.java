/**
 * 
 */
package com.mingseal.utils;

import com.mingseal.dhp.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author 商炎炳
 * @description 自定义的对话框,不显示进度
 */
public class CustomUploadDialog extends Dialog {

	private Context context = null;
	private static CustomUploadDialog customProgressDialog = null;
	private static ProgressBar progressBar = null;

	public CustomUploadDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomUploadDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 创建自定义的Dialog
	 * 
	 * @param context
	 * @return
	 */
	public static CustomUploadDialog createDialog(Context context) {
		customProgressDialog = new CustomUploadDialog(context, R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.custom_upload_dialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		progressBar = (ProgressBar) customProgressDialog.findViewById(R.id.upLoadProgressBar);

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
	public CustomUploadDialog setTitle(String title) {
		return customProgressDialog;
	}

	/**
	 * 设置提示文本
	 * 
	 * @param message
	 * @return
	 */
	public CustomUploadDialog setMessage(String message) {
		TextView title = (TextView) customProgressDialog.findViewById(R.id.tv_loadingmsg);
		if (title != null) {
			title.setText(message);
		}

		return customProgressDialog;
	}

}
