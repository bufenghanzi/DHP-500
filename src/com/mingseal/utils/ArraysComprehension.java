/**
 * 
 */
package com.mingseal.utils;

/**
 * com.mingseal.data.dao里面的数组解析
 * 
 * @author 商炎炳
 *
 */
public class ArraysComprehension {
	/**
	 * 将字符串解析成boolean数组 如果有问题，可以先把所有的逗号先给替换，再去换boolean 字符串格式
	 * 
	 * @param value
	 *            [false, false, true, false, true, false, false, false, false,
	 *            false, false, false, false, false, false, false, false, false,
	 *            false, false]
	 * @return boolean数组
	 */
	public static boolean[] boooleanParse(String value) {
		value = value.replace("[", "").replace("]", "").replace(" ", "");
		String[] StrSplite = value.split(",");
		boolean[] aloneBoolean = new boolean[StrSplite.length];
		for (int i = 0; i < StrSplite.length; i++) {
			aloneBoolean[i] = Boolean.parseBoolean(StrSplite[i]);
		}

		return aloneBoolean;
	}
}
