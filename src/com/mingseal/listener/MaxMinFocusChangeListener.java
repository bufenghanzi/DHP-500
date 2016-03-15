/**
 * @Title MaxMinFocusChangeListener.java
 * @Package com.mingseal.listener
 * @Description TODO
 * @author 商炎炳
 * @date 2016年1月27日 上午10:50:28
 * @version V1.0
 */
package com.mingseal.listener;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * @ClassName MaxMinFocusChangeListener
 * @Description 最大最小值的输入框判断
 * @author 商炎炳
 * @date 2016年1月27日 上午10:50:28
 *
 */
public class MaxMinFocusChangeListener implements OnFocusChangeListener {

	/**
	 * @Fields maxValue: 最大值
	 */
	private int maxValue;
	/**
	 * @Fields minValue: 最小值
	 */
	private int minValue;
	/**
	 * @Fields etNumber: 当前Edittext控件
	 */
	private EditText etNumber;
	/**
	 * @Fields value: 获取当前输入框的值
	 */
	private int value;

	/**
	 * @Title MaxMinFocusChangeListener
	 * @Description if(>=maxValue)取maxValue;else(<=minValue)取minValue
	 * @param maxValue
	 *            允许输入的最大值
	 * @param minValue
	 *            允许输入的最小值
	 * @param etNumber
	 *            当前Edittext控件
	 */
	public MaxMinFocusChangeListener(int maxValue, int minValue, EditText etNumber) {
		super();
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.etNumber = etNumber;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			if (etNumber.getText().toString().equals("")) {
				etNumber.setText(minValue + "");
			}

			value = Integer.parseInt(etNumber.getText().toString());
			if (value <= minValue) {
				etNumber.setText(minValue + "");
			} else if (value >= maxValue) {
				etNumber.setText(maxValue + "");
			} else {
				etNumber.setText(value + "");
			}
		}
	}

}
