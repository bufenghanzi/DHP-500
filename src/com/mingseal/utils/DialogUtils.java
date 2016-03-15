/**
 * 
 */
package com.mingseal.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * @author 商炎炳
 *
 */
public class DialogUtils {

	/**
	 * 对话框的显示
	 * 
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void showCustomDialog(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}
}
