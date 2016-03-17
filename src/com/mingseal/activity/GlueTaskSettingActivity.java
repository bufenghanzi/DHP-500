/**
 * 
 */
package com.mingseal.activity;

import java.util.ArrayList;

import com.mingseal.data.param.SettingParam;
import com.mingseal.dhp.R;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @title 任务设置属性
 * @author 商炎炳
 *
 */
public class GlueTaskSettingActivity extends Activity implements OnClickListener{

	private String TAG = "GlueTaskSettingActivity";
	/**
	 * x轴步距
	 */
	private NumberPicker num_xDistance;
	/**
	 * y轴步距
	 */
	private NumberPicker num_yDistance;
	/**
	 * z轴步距
	 */
	private NumberPicker num_zDistance;
	/**
	 * 高速度
	 */
	private NumberPicker num_highSpeed;
	/**
	 * 中速度
	 */
	private NumberPicker num_mediumSpeed;
	/**
	 * 低速度
	 */
	private NumberPicker num_lowSpeed;
	/**
	 * 循迹速度
	 */
	private NumberPicker num_trackSpeed;
	/**
	 * 循迹定位
	 */
	private Switch sw_location;
	/**
	 * 返回
	 */
	private RelativeLayout rl_back;
	/**
	 * 完成
	 */
	private RelativeLayout rl_complete;
	/**
	 * 标题
	 */
	private TextView tv_title;

	private SettingParam setting;// 任务设置参数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);

		setting = SharePreferenceUtils.readFromSharedPreference(this);
//		System.out.println("任务设置-------->"+setting);
		initView();

	}

	/**
	 * 加载组件
	 */
	private void initView() {
		num_xDistance = (NumberPicker) findViewById(R.id.num_xDistance);
		num_yDistance = (NumberPicker) findViewById(R.id.num_yDistance);
		num_zDistance = (NumberPicker) findViewById(R.id.num_zDistance);
		num_highSpeed = (NumberPicker) findViewById(R.id.num_highSpeed);
		num_mediumSpeed = (NumberPicker) findViewById(R.id.num_mediumSpeed);
		num_lowSpeed = (NumberPicker) findViewById(R.id.num_lowSpeed);
		num_trackSpeed = (NumberPicker) findViewById(R.id.num_trackSpeed);
		sw_location = (Switch) findViewById(R.id.switch_trackLocation);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_task_set));
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		//设置NumberPicker的最大最小值
		//1-10(1)
		num_xDistance.setMaxValue(10);
		num_xDistance.setMinValue(1);
		num_xDistance.setValue(setting.getxStepDistance());
		//1-10(1)
		num_yDistance.setMaxValue(10);
		num_yDistance.setMinValue(1);
		num_yDistance.setValue(setting.getyStepDistance());
		//1-10(1)
		num_zDistance.setMaxValue(10);
		num_zDistance.setMinValue(1);
		num_zDistance.setValue(setting.getzStepDistance());
		//31-50(33)
		num_highSpeed.setMaxValue(50);
		num_highSpeed.setMinValue(31);
		num_highSpeed.setValue(setting.getHighSpeed());
		//11-30(13)
		num_mediumSpeed.setMaxValue(30);
		num_mediumSpeed.setMinValue(11);
		num_mediumSpeed.setValue(setting.getMediumSpeed());
		//2-10(3)
		num_lowSpeed.setMaxValue(10);
		num_lowSpeed.setMinValue(2);
		num_lowSpeed.setValue(setting.getLowSpeed());
		//1-100(50)
		num_trackSpeed.setMaxValue(100);
		num_trackSpeed.setMinValue(1);
		num_trackSpeed.setValue(setting.getTrackSpeed());
		sw_location.setChecked(setting.isTrackLocation());
		
		rl_back.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

	}
	
	/** 将页面上的数据保存到SettingParam对象中
	 * @return SettingParam
	 */
	private SettingParam getParam(){
		setting = new SettingParam();
		
		setting.setxStepDistance(num_xDistance.getValue());
		setting.setyStepDistance(num_yDistance.getValue());
		setting.setzStepDistance(num_zDistance.getValue());
		setting.setHighSpeed(num_highSpeed.getValue());
		setting.setMediumSpeed(num_mediumSpeed.getValue());
		setting.setLowSpeed(num_lowSpeed.getValue());
		setting.setTrackSpeed(num_trackSpeed.getValue());
		setting.setTrackLocation(sw_location.isChecked());
		
		return setting;
	}
	
	/**
	 * 保存页面中的信息并返回到之前的TaskActivity
	 */
	private void saveBackActivity() {
		
		setting = getParam();
		SharePreferenceUtils.saveToSharedPreferences(this, setting);
		setResult(TaskActivity.resultSettingCode);
		finish();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
	}
	
	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueTaskSettingActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_save));
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setPositiveButton(getResources().getString(R.string.need_yes), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				saveBackActivity();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.is_need_no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				GlueTaskSettingActivity.this.finish();
			}
		});
		builder.setNeutralButton(getResources().getString(R.string.is_need_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			showBackDialog();
			break;
		case R.id.rl_complete:
			saveBackActivity();
			break;
		}
	}

}
