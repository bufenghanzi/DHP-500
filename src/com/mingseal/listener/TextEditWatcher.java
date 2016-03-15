/**
 * 
 */
package com.mingseal.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author 商炎炳
 * @description Edittext只允许输入两位小数
 */
public class TextEditWatcher implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		String temp = s.toString();// 只允许输入两位小数
		int posDot = temp.indexOf(".");
		if (posDot <= 0) {
			return;
		}
		if (s.length() - posDot - 1 > 2) {
			s.delete(posDot + 3, posDot + 4);
		}
	}

}
