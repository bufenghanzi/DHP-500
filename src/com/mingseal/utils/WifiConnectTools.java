/**
 * @Title WifiConnectTools.java
 * @Package com.mingseal.utils
 * @Description TODO
 * @author 商炎炳
 * @date 2016年1月25日 下午4:18:20
 * @version V1.0
 */
package com.mingseal.utils;

import com.mingseal.application.UserApplication;
import com.mingseal.dhp.R;

import android.view.View;
import android.widget.ImageView;

/**
 * @ClassName WifiConnectTools
 * @Description 判断wifi是否连接
 * @author 商炎炳
 * @date 2016年1月25日 下午4:18:20
 *
 */
public class WifiConnectTools {
	/**
	 * 成功连上,显示蓝色图标,未连上显示红色图标
	 * 
	 * @Title processWifiConnect
	 * @Description 显示隐藏wifi连接情况
	 * @param userApplication
	 *            保存的全局变量
	 * @param iv_title_wifi_connecting
	 *            wifi连接图像
	 */
	public static void processWifiConnect(UserApplication userApplication, ImageView iv_title_wifi_connecting) {
		iv_title_wifi_connecting.setVisibility(View.VISIBLE);
		if (userApplication.isWifiConnecting()) {
			// 显示wifi连接
			iv_title_wifi_connecting.setImageResource(R.drawable.icon_wifi_connect);
		} else {
			iv_title_wifi_connecting.setImageResource(R.drawable.icon_wifi_disconnect);
		}
	}
}
