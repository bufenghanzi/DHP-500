package com.mingseal.utils;

import java.lang.reflect.Field;

import com.mingseal.dhp.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	private static Toast mToast = null;

	/**
	 * @Title displayPromptInfo
	 * @Description 显示自定义Toast
	 * @param context
	 * @param message
	 *            显示的文本
	 */
	public static void displayPromptInfo(Context context, String message) {
		// 多个同时显示，会只显示当前的那个，而不用去等待前面一个显示完成
		View view = LayoutInflater.from(context).inflate(R.layout.activity_toast, null);
		TextView messageTv = (TextView) view.findViewById(R.id.tv_toast);

		if (mToast == null) {
			mToast = new Toast(context);
			mToast.setView(view);
			messageTv.setText(message);
			mToast.setDuration(800);
		} else {
			mToast.setView(view);
			messageTv.setText(message);
		}
		mToast.setGravity(Gravity.TOP, 0, 300);
		mToast.show();
	}

	/**
	 * @Title getField
	 * @Description 反射字段
	 * @param object
	 *            要反射的对象
	 * @param fieldName
	 *            要反射的字段名称
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	private static Object getField(Object object, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = object.getClass().getDeclaredField(fieldName);
		if (field != null) {
			field.setAccessible(true);
			return field.get(object);
		}
		return null;
	}

}
