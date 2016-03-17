package com.mingseal.communicate;

import com.mingseal.dhp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断网络连接状态
 * 
 */
public class NetManager {
	static NetManager s_m = null;

	private Context context;

	private NetManager() {

	}

	public void init(Context ctx) {
		context = ctx;
	}

	/**
	 * @Title  instance
	 * @Description 单例DCL
	 * @author wj
	 * @return
	 */
	public static NetManager instance() {
		if (s_m == null) {
			synchronized(NetManager.class){
				if (s_m==null) {
					s_m = new NetManager();
				}
			}
		}
		return s_m;
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		if (context == null) {
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @return
	 */
	public boolean isWifiConnected() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @return
	 */
	public boolean isMobileConnected() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * @Title  getConnectedType
	 * @Description 获取当前网络连接的类型信息
	 * @author wj
	 * @return
	 */
	public int getConnectedType() {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
	
}
