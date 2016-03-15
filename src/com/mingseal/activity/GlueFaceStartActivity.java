/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueFaceStartAdapter;
import com.mingseal.data.dao.GlueFaceStartDao;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import static com.mingseal.data.param.PointConfigParam.GlueFaceStart;

/**
 * @author 商炎炳
 *
 */
public class GlueFaceStartActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueFaceStartActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;

	/**
	 * @Fields et_start_outGlueTimePrev: 出胶前延时
	 */
	private EditText et_start_outGlueTimePrev;
	/**
	 * @Fields et_start_outGlueTime: 出胶后延时
	 */
	private EditText et_start_outGlueTime;
	/**
	 * @Fields et_start_moveSpeed: 轨迹速度
	 */
	private EditText et_start_moveSpeed;
	/**
	 * @Fields et_start_stopGlueTimePrev: 停胶延时
	 */
	private EditText et_start_stopGlueTimePrev;
	/**
	 * 抬起高度2015/12/15注销，不需要抬起高度
	 */
	// private NumberPicker upHeightPicker;

	/**
	 * 是否出胶
	 */
	private Switch isOutGlueSwitch;
	/**
	 * 起始方向 true:x方向 false:y方向
	 */
	private Switch startDirSwitch;

	/**
	 * 点胶口
	 */
	private Switch[] isGluePort;

	/**
	 * 面起始点Spinner
	 */
	private Spinner faceStartSpinner;

	/**
	 * 返回上级菜单
	 */
	private RelativeLayout rl_back;

	/**
	 * 保存方案按钮
	 */
	private RelativeLayout rl_save;
	/**
	 * 完成按钮
	 */
	private RelativeLayout rl_complete;
	private Intent intent;
	private Point point;// 从taskActivity中传值传过来的point
	private GlueFaceStartDao glueFaceStartDao;
	private List<PointGlueFaceStartParam> glueStartLists;
	private PointGlueFaceStartParam glueStart;
	private PointGlueFaceStartAdapter mStartAdapter;
	private boolean[] glueBoolean;
	private int param_id = 1;/// 选取的是几号方案
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据

	/**
	 * @Fields outGlueTimePrevInt: 出胶前延时的int值
	 */
	private int outGlueTimePrevInt = 0;
	/**
	 * @Fields outGlueTimeInt: 出胶后延时的int值
	 */
	private int outGlueTimeInt = 0;
	/**
	 * @Fields moveSpeedInt: 移动速度的int值
	 */
	private int moveSpeedInt = 0;
	/**
	 * @Fields stopGlueTimePrevInt: 停胶延时的int值
	 */
	private int stopGlueTimePrevInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_face_start);

		intent = getIntent();
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		Log.d(TAG, point.toString());

		initPicker();

		glueFaceStartDao = new GlueFaceStartDao(GlueFaceStartActivity.this);
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();
		if (glueStartLists == null || glueStartLists.isEmpty()) {
			glueStart = new PointGlueFaceStartParam();
			glueFaceStartDao.insertGlueFaceStart(glueStart);
		}
		// 重新获取一下数据
		glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();

		mStartAdapter = new PointGlueFaceStartAdapter(GlueFaceStartActivity.this);
		mStartAdapter.setGlueStartLists(glueStartLists);
		faceStartSpinner.setAdapter(mStartAdapter);

		// 如果为1的话，需要设置值
		if (mType == 1) {
			faceStartSpinner.setSelection(point.getPointParam().get_id() - 1);
			mStartAdapter.notifyDataSetChanged();
		}
		// 初始化数组
		glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		faceStartSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointGlueFaceStartParam pointGlueFaceStartParam = mStartAdapter.getItem(position);
				et_start_outGlueTimePrev.setText(pointGlueFaceStartParam.getOutGlueTimePrev() + "");
				et_start_outGlueTime.setText(pointGlueFaceStartParam.getOutGlueTime() + "");
				et_start_moveSpeed.setText(pointGlueFaceStartParam.getMoveSpeed() + "");
				et_start_stopGlueTimePrev.setText(pointGlueFaceStartParam.getStopGlueTime() + "");
				isOutGlueSwitch.setChecked(pointGlueFaceStartParam.isOutGlue());
				startDirSwitch.setChecked(pointGlueFaceStartParam.isStartDir());

				isGluePort[0].setChecked(pointGlueFaceStartParam.getGluePort()[0]);
				isGluePort[1].setChecked(pointGlueFaceStartParam.getGluePort()[1]);
				isGluePort[2].setChecked(pointGlueFaceStartParam.getGluePort()[2]);
				isGluePort[3].setChecked(pointGlueFaceStartParam.getGluePort()[3]);
				isGluePort[4].setChecked(pointGlueFaceStartParam.getGluePort()[4]);

				param_id = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		et_start_outGlueTimePrev = (EditText) findViewById(R.id.et_facestart_outGlueTimePrev);
		et_start_outGlueTime = (EditText) findViewById(R.id.et_facestart_outGlueTime);
		et_start_moveSpeed = (EditText) findViewById(R.id.et_facestart_movespeed);
		et_start_stopGlueTimePrev = (EditText) findViewById(R.id.et_facestart_stopGlueTime);

		isOutGlueSwitch = (Switch) findViewById(R.id.switch_isOutGlue);
		startDirSwitch = (Switch) findViewById(R.id.switch_startDir);
		isGluePort = new Switch[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (Switch) findViewById(R.id.switch_glueport1);
		isGluePort[1] = (Switch) findViewById(R.id.switch_glueport2);
		isGluePort[2] = (Switch) findViewById(R.id.switch_glueport3);
		isGluePort[3] = (Switch) findViewById(R.id.switch_glueport4);
		isGluePort[4] = (Switch) findViewById(R.id.switch_glueport5);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		tv_title = (TextView) findViewById(R.id.tv_title);

		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		faceStartSpinner = (Spinner) findViewById(R.id.spinner_face_start);

		// 设置出胶前延时的默认值和最大最小值(要重新设置)
		et_start_outGlueTimePrev.addTextChangedListener(new MaxMinEditWatcher(GlueFaceStart.OutGlueTimePrevMax,
				GlueFaceStart.GlueFaceStartMin, et_start_outGlueTimePrev));
		et_start_outGlueTimePrev.setOnFocusChangeListener(new MaxMinFocusChangeListener(
				GlueFaceStart.OutGlueTimePrevMax, GlueFaceStart.GlueFaceStartMin, et_start_outGlueTimePrev));
		et_start_outGlueTimePrev.setSelectAllOnFocus(true);

		// 设置出胶后延时的默认值和最大最小值(要重新设置)
		et_start_outGlueTime.addTextChangedListener(new MaxMinEditWatcher(GlueFaceStart.OutGlueTimeMax,
				GlueFaceStart.GlueFaceStartMin, et_start_outGlueTime));
		et_start_outGlueTime.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueFaceStart.OutGlueTimeMax,
				GlueFaceStart.GlueFaceStartMin, et_start_outGlueTime));
		et_start_outGlueTime.setSelectAllOnFocus(true);

		// 设置轨迹速度的默认值和最大最小值(要重新设置)
		et_start_moveSpeed.addTextChangedListener(
				new MaxMinEditWatcher(GlueFaceStart.MoveSpeedMax, GlueFaceStart.MoveSpeedMin, et_start_moveSpeed));
		et_start_moveSpeed.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueFaceStart.MoveSpeedMax,
				GlueFaceStart.MoveSpeedMin, et_start_moveSpeed));
		et_start_moveSpeed.setSelectAllOnFocus(true);

		// 设置停胶延时的默认值和最大最小值(要重新设置)
		et_start_stopGlueTimePrev.addTextChangedListener(new MaxMinEditWatcher(GlueFaceStart.StopGlueTimeMax,
				GlueFaceStart.GlueFaceStartMin, et_start_stopGlueTimePrev));
		et_start_stopGlueTimePrev.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueFaceStart.StopGlueTimeMax,
				GlueFaceStart.GlueFaceStartMin, et_start_stopGlueTimePrev));
		et_start_stopGlueTimePrev.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_face_start));
		rl_back.setOnClickListener(this);
		rl_save.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

	}

	/**
	 * @Title isEditNull
	 * @Description 判断输入框是否为空
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditNull() {
		if ("".equals(et_start_moveSpeed.getText().toString())) {
			return false;
		} else if ("".equals(et_start_outGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_start_outGlueTimePrev.getText().toString())) {
			return false;
		} else if ("".equals(et_start_stopGlueTimePrev.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到PointGlueFaceStartParam对象中
	 * 
	 * @return PointGlueFaceStartParam
	 */
	private PointGlueFaceStartParam getFaceStart() {
		glueStart = new PointGlueFaceStartParam();
		try {
			outGlueTimePrevInt = Integer.parseInt(et_start_outGlueTimePrev.getText().toString());
		} catch (NumberFormatException e) {
			outGlueTimePrevInt = 0;
		}
		try {
			outGlueTimeInt = Integer.parseInt(et_start_outGlueTime.getText().toString());
		} catch (NumberFormatException e) {
			outGlueTimeInt = 0;
		}
		try {
			moveSpeedInt = Integer.parseInt(et_start_moveSpeed.getText().toString());
			if (moveSpeedInt == 0) {
				moveSpeedInt = 1;
			}
		} catch (NumberFormatException e) {
			moveSpeedInt = 1;
		}
		try {
			stopGlueTimePrevInt = Integer.parseInt(et_start_stopGlueTimePrev.getText().toString());
		} catch (NumberFormatException e) {
			stopGlueTimePrevInt = 0;
		}

		glueStart.setOutGlueTimePrev(outGlueTimePrevInt);
		glueStart.setOutGlueTime(outGlueTimeInt);
		glueStart.setMoveSpeed(moveSpeedInt);
		glueStart.setStopGlueTime(stopGlueTimePrevInt);
		glueStart.setOutGlue(isOutGlueSwitch.isChecked());
		glueStart.setStartDir(startDirSwitch.isChecked());

		glueBoolean[0] = isGluePort[0].isChecked();
		glueBoolean[1] = isGluePort[1].isChecked();
		glueBoolean[2] = isGluePort[2].isChecked();
		glueBoolean[3] = isGluePort[3].isChecked();
		glueBoolean[4] = isGluePort[4].isChecked();

		glueStart.setGluePort(glueBoolean);
		glueStart.set_id(param_id);

		return glueStart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:// 返回按钮的响应事件
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			break;
		case R.id.rl_save:// 保存新方案
			isNull = isEditNull();
			if (isNull) {
				glueStart = getFaceStart();
				if (glueStartLists.contains(glueStart)) {
					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.task_is_exist_yes));
				} else {
					glueFaceStartDao.insertGlueFaceStart(glueStart);

					glueStartLists = glueFaceStartDao.findAllGlueFaceStartParams();

					mStartAdapter.setGlueStartLists(glueStartLists);
					mStartAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.save_success));

				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}

			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {
				glueStart = getFaceStart();
				if (glueStartLists.contains(glueStart)) {

					int id = glueStartLists.indexOf(glueStart);
					// 如果方案里有的话,只需要设置一下id就行
					glueStart.set_id(glueStartLists.get(id).get_id());
				} else {
					long rowID = glueFaceStartDao.insertGlueFaceStart(glueStart);
					glueStart.set_id((int) rowID);
				}

				point.setPointParam(glueStart);

				Bundle extras = new Bundle();
				extras.putParcelable(MyPopWindowClickListener.POPWINDOW_KEY, point);
				extras.putInt(MyPopWindowClickListener.FLAG_KEY, mFlag);

				intent.putExtras(extras);
				setResult(TaskActivity.resultCode, intent);

				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}
			break;

		}
	}
}
