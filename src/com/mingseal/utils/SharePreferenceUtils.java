/**
 * 
 */
package com.mingseal.utils;

import com.mingseal.data.param.SettingParam;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author 商炎炳
 *
 */
public class SharePreferenceUtils {

	/**
	 * 这个要放在第一个Activity中 ,设置SharePreference的初始数据
	 * 
	 * @param context
	 */
	public static void setSharedPreference(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SettingParam.Setting.SettingName, Context.MODE_PRIVATE);
		// 因为是第一次，所以需要给它设个初值并保存(如果里面有值的话，还是保存原来的值)
		int xStepDistance = sp.getInt(SettingParam.Setting.XStepDistance, 1);
		int yStepDistance = sp.getInt(SettingParam.Setting.YStepDistance, 1);
		int zStepDistance = sp.getInt(SettingParam.Setting.ZStepDistance, 1);
		int lowSpeed = sp.getInt(SettingParam.Setting.LowSpeed, 3);
		int mediumSpeed = sp.getInt(SettingParam.Setting.MediumSpeed, 13);
		int highSpeed = sp.getInt(SettingParam.Setting.HighSpeed, 33);
		int trackSpeed = sp.getInt(SettingParam.Setting.TrackSpeed, 50);
		boolean trackLocation = sp.getBoolean(SettingParam.Setting.TrackLocation, false);

		SharedPreferences.Editor editor = sp.edit();

		editor.putInt(SettingParam.Setting.XStepDistance, xStepDistance);
		editor.putInt(SettingParam.Setting.YStepDistance, yStepDistance);
		editor.putInt(SettingParam.Setting.ZStepDistance, zStepDistance);
		editor.putInt(SettingParam.Setting.LowSpeed, lowSpeed);
		editor.putInt(SettingParam.Setting.MediumSpeed, mediumSpeed);
		editor.putInt(SettingParam.Setting.HighSpeed, highSpeed);
		editor.putInt(SettingParam.Setting.TrackSpeed, trackSpeed);
		editor.putBoolean(SettingParam.Setting.TrackLocation, trackLocation);
		editor.commit();

	}

	/**
	 * 从SharePreference中读取数据，并保存到SettingParam中
	 * 
	 * @param context
	 * @return 任务设置参数
	 */
	public static SettingParam readFromSharedPreference(Context context) {
		SettingParam settingParam = new SettingParam();
		SharedPreferences sp = context.getSharedPreferences(SettingParam.Setting.SettingName, Context.MODE_PRIVATE);
		int xStepDistance = sp.getInt(SettingParam.Setting.XStepDistance, 1);
		int yStepDistance = sp.getInt(SettingParam.Setting.YStepDistance, 1);
		int zStepDistance = sp.getInt(SettingParam.Setting.ZStepDistance, 1);
		int lowSpeed = sp.getInt(SettingParam.Setting.LowSpeed, 3);
		int mediumSpeed = sp.getInt(SettingParam.Setting.MediumSpeed, 13);
		int highSpeed = sp.getInt(SettingParam.Setting.HighSpeed, 33);
		int trackSpeed = sp.getInt(SettingParam.Setting.TrackSpeed, 50);
		boolean trackLocation = sp.getBoolean(SettingParam.Setting.TrackLocation, false);

		settingParam.setxStepDistance(xStepDistance);
		settingParam.setyStepDistance(yStepDistance);
		settingParam.setzStepDistance(zStepDistance);
		settingParam.setHighSpeed(highSpeed);
		settingParam.setMediumSpeed(mediumSpeed);
		settingParam.setLowSpeed(lowSpeed);
		settingParam.setTrackSpeed(trackSpeed);
		settingParam.setTrackLocation(trackLocation);

		return settingParam;

	}

	/**
	 * 将SettingParam的数据保存到SharedPreferences中
	 * 
	 * @param context
	 *            上下文
	 * @param setting
	 *            SettingParam
	 */
	public static void saveToSharedPreferences(Context context, SettingParam setting) {
		SharedPreferences sp = context.getSharedPreferences(SettingParam.Setting.SettingName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		editor.putInt(SettingParam.Setting.XStepDistance, setting.getxStepDistance());
		editor.putInt(SettingParam.Setting.YStepDistance, setting.getyStepDistance());
		editor.putInt(SettingParam.Setting.ZStepDistance, setting.getzStepDistance());
		editor.putInt(SettingParam.Setting.LowSpeed, setting.getLowSpeed());
		editor.putInt(SettingParam.Setting.MediumSpeed, setting.getMediumSpeed());
		editor.putInt(SettingParam.Setting.HighSpeed, setting.getHighSpeed());
		editor.putInt(SettingParam.Setting.TrackSpeed, setting.getTrackSpeed());
		editor.putBoolean(SettingParam.Setting.TrackLocation, setting.isTrackLocation());

		editor.commit();
	}

	/**
	 * 将任务号保存到SharePreferences
	 * 
	 * @Title saveTaskNumberToPref
	 * @Description 将任务号保存到SharePreferences
	 * @param context
	 * @param number
	 *            任务号
	 */
	public static void saveTaskNumberToPref(Context context, int number) {
		SharedPreferences sp = context.getSharedPreferences(SettingParam.Task.TaskName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(SettingParam.Task.TaskNumber, number);
		editor.commit();
	}

	/**
	 * 从SharePreferences中获取任务号
	 * 
	 * @Title getTaskNumberFromPref
	 * @Description 从SharePreferences中获取任务号
	 * @param context
	 * @return 任务号
	 */
	public static int getTaskNumberFromPref(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SettingParam.Task.TaskName, Context.MODE_PRIVATE);
		int number = sp.getInt(SettingParam.Task.TaskNumber, 1);
		return number;
	}

	/**
	 * 保存上一次使用的参数序列号
	 * 
	 * @Title saveParamNumberToPref
	 * @Description 保存某个方案的参数序列号
	 * @param context
	 * @param key
	 *            保存方案的参数序列号的key
	 * @param number
	 *            对应某个方案的参数序列号
	 */
	public static void saveParamNumberToPref(Context context, String key, int number) {

		SharedPreferences sp = context.getSharedPreferences(SettingParam.Task.TaskName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, number);
		editor.commit();

	}

	/**
	 * 从SharePreferences中读取上次保存的参数序列号
	 * 
	 * @Title getParamNumberFromPref
	 * @Description 从SharePreferences中读取上次保存的参数序列号
	 * @param context
	 * @param key
	 *            保存方案的参数序列号的key
	 * @return 序列号
	 */
	public static int getParamNumberFromPref(Context context, String key) {

		SharedPreferences sp = context.getSharedPreferences(SettingParam.Task.TaskName, Context.MODE_PRIVATE);
		int number = sp.getInt(key, 1);
		return number;

	}

}
