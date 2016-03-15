/**
 * 
 */
package com.mingseal.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 商炎炳
 *
 */
public class ListResolving {

	/**
	 * 字符串解析成List集合
	 * 
	 * @param values
	 * @return List<Integer>
	 */
	public static List<Integer> listParse(String values) {
		// [1, 2, 4, 5, 10, 19, 3]
		values = values.replace("[", "").replace("]", "").replace(" ", "");
		List<Integer> resolvingLists = new ArrayList<Integer>();
		if ("".equals(values)) {

		} else {
			String[] strSplite = values.split(",");
			for (int i = 0; i < strSplite.length; i++) {
				resolvingLists.add(Integer.parseInt(strSplite[i]));
			}
		}

		return resolvingLists;
	}
}
