/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueLineEndAdapter;
import com.mingseal.data.dao.GlueLineEndDao;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.mingseal.data.param.PointConfigParam.GlueLineEnd;

/**
 * @author 商炎炳
 *
 */
public class GlueLineEndActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueLineEndActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * @Fields et_lineend_stopPrev: 停胶前延时
	 */
	private EditText et_lineend_stopPrev;
	/**
	 * @Fields et_lineend_stop: 停胶后延时
	 */
	private EditText et_lineend_stop;
	/**
	 * @Fields et_lineend_upHeight: 抬起高度
	 */
	private EditText et_lineend_upHeight;
	/**
	 * @Fields et_lineend_breakGlueLen: 提前停胶距离
	 */
	private EditText et_lineend_breakGlueLen;
	/**
	 * @Fields et_lineend_drawDistance: 拉丝距离
	 */
	private EditText et_lineend_drawDistance;
	/**
	 * @Fields et_lineend_drawSpeed: 拉丝速度
	 */
	private EditText et_lineend_drawSpeed;
	/**
	 * 是否暂停
	 */
	private Switch isPauseSwitch;

	/**
	 * 线结束点Spinner
	 */
	private Spinner lineEndSpinner;

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
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据

	private GlueLineEndDao glueEndDao;
	private List<PointGlueLineEndParam> glueEndLists;
	private PointGlueLineEndParam glueEnd;
	private PointGlueLineEndAdapter mEndAdapter;
	private int param_id = 1;// 选取的是几号方案

	/**
	 * @Fields stopPrevInt: 停胶前延时的int值
	 */
	private int stopTimePrevInt = 0;
	/**
	 * @Fields stopTimeInt: 停胶后延时的int值
	 */
	private int stopTimeInt = 0;
	/**
	 * @Fields upHeightInt: 抬起高度的int值
	 */
	private int upHeightInt = 0;
	/**
	 * @Fields breakGlueLenInt: 提前停胶距离的int值
	 */
	private int breakGlueLenInt = 0;
	/**
	 * @Fields drawDistanceInt: 拉丝距离的int值
	 */
	private int drawDistanceInt = 0;
	/**
	 * @Fields drawSpeedInt: 拉丝速度的int值
	 */
	private int drawSpeedInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_line_end);

		intent = getIntent();
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		glueEndDao = new GlueLineEndDao(this);
		glueEndLists = glueEndDao.findAllGlueLineEndParams();
		if (glueEndLists == null || glueEndLists.isEmpty()) {
			glueEnd = new PointGlueLineEndParam();
			glueEndDao.insertGlueLineEnd(glueEnd);
			glueEndLists = glueEndDao.findAllGlueLineEndParams();
		}

		mEndAdapter = new PointGlueLineEndAdapter(this);
		mEndAdapter.setGlueEndLists(glueEndLists);
		lineEndSpinner.setAdapter(mEndAdapter);
		// 如果为1的话，需要设置值
		if (mType == 1) {
			lineEndSpinner.setSelection(point.getPointParam().get_id() - 1);
			mEndAdapter.notifyDataSetChanged();
		}
		lineEndSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointGlueLineEndParam point = mEndAdapter.getItem(position);

				et_lineend_stopPrev.setText(point.getStopGlueTimePrev() + "");
				et_lineend_stop.setText(point.getStopGlueTime() + "");
				et_lineend_upHeight.setText(point.getUpHeight() + "");
				et_lineend_breakGlueLen.setText(point.getBreakGlueLen() + "");
				et_lineend_drawDistance.setText(point.getDrawDistance() + "");
				et_lineend_drawSpeed.setText(point.getDrawSpeed() + "");
				isPauseSwitch.setChecked(point.isPause());

				param_id = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * 加载页面组件并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		et_lineend_stopPrev = (EditText) findViewById(R.id.et_lineend_stopGlueTimePrev);
		et_lineend_stop = (EditText) findViewById(R.id.et_lineend_stopGlueTime);
		et_lineend_upHeight = (EditText) findViewById(R.id.et_lineend_upHeight);
		et_lineend_breakGlueLen = (EditText) findViewById(R.id.et_lineend_breakGlueLen);
		et_lineend_drawDistance = (EditText) findViewById(R.id.et_lineend_drawDistance);
		et_lineend_drawSpeed = (EditText) findViewById(R.id.et_lineend_drawSpeed);
		isPauseSwitch = (Switch) findViewById(R.id.switch_isPause);
		lineEndSpinner = (Spinner) findViewById(R.id.spinner_line_end);

		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置停胶前延时的最大最小值
		et_lineend_stopPrev.addTextChangedListener(new MaxMinEditWatcher(GlueLineEnd.StopGlueTimePrevMax,
				GlueLineEnd.GlueLineEndMin, et_lineend_stopPrev));
		et_lineend_stopPrev.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueLineEnd.StopGlueTimePrevMax,
				GlueLineEnd.GlueLineEndMin, et_lineend_stopPrev));
		et_lineend_stopPrev.setSelectAllOnFocus(true);

		// 设置停胶后延时的最大最小值
		et_lineend_stop.addTextChangedListener(
				new MaxMinEditWatcher(GlueLineEnd.StopGlueTimeMax, GlueLineEnd.GlueLineEndMin, et_lineend_stop));
		et_lineend_stop.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueLineEnd.StopGlueTimeMax,
				GlueLineEnd.GlueLineEndMin, et_lineend_stop));
		et_lineend_stop.setSelectAllOnFocus(true);

		// 设置抬起高度的最大最小值
		et_lineend_upHeight.addTextChangedListener(
				new MaxMinEditWatcher(GlueLineEnd.UpHeightMax, GlueLineEnd.GlueLineEndMin, et_lineend_upHeight));
		et_lineend_upHeight.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueLineEnd.UpHeightMax,
				GlueLineEnd.GlueLineEndMin, et_lineend_upHeight));
		et_lineend_upHeight.setSelectAllOnFocus(true);

		// 设置提前停胶距离的最大最小值
		et_lineend_breakGlueLen.addTextChangedListener(new MaxMinEditWatcher(GlueLineEnd.BreakGlueLenMax,
				GlueLineEnd.GlueLineEndMin, et_lineend_breakGlueLen));
		et_lineend_breakGlueLen.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueLineEnd.BreakGlueLenMax,
				GlueLineEnd.GlueLineEndMin, et_lineend_breakGlueLen));
		et_lineend_breakGlueLen.setSelectAllOnFocus(true);

		// 设置拉丝距离的最大最小值
		et_lineend_drawDistance.addTextChangedListener(
				new MaxMinEditWatcher(GlueLineEnd.DrawDistance, GlueLineEnd.GlueLineEndMin, et_lineend_drawDistance));
		et_lineend_drawDistance.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueLineEnd.DrawDistance,
				GlueLineEnd.GlueLineEndMin, et_lineend_drawDistance));
		et_lineend_drawDistance.setSelectAllOnFocus(true);

		// 设置拉丝速度的最大最小值
		et_lineend_drawSpeed.addTextChangedListener(
				new MaxMinEditWatcher(GlueLineEnd.DrawSpeed, GlueLineEnd.GlueLineEndMin, et_lineend_drawSpeed));
		et_lineend_drawSpeed.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueLineEnd.DrawSpeed, GlueLineEnd.GlueLineEndMin, et_lineend_drawSpeed));
		et_lineend_drawSpeed.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_line_end));
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
		if ("".equals(et_lineend_breakGlueLen.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_drawDistance.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_drawSpeed.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_stop.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_stopPrev.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_upHeight.getText().toString())) {
			return false;
		}

		return true;
	}

	/**
	 * 将页面上的数据保存到PointGlueLineEndParam对象中
	 * 
	 * @return PointGlueLineEndParam
	 */
	private PointGlueLineEndParam getLineEnd() {
		glueEnd = new PointGlueLineEndParam();

		try {
			stopTimePrevInt = Integer.parseInt(et_lineend_stopPrev.getText().toString());
		} catch (NumberFormatException e) {
			stopTimePrevInt = 0;
		}
		try {
			stopTimeInt = Integer.parseInt(et_lineend_stop.getText().toString());
		} catch (NumberFormatException e) {
			stopTimeInt = 0;
		}
		try {
			upHeightInt = Integer.parseInt(et_lineend_upHeight.getText().toString());
		} catch (NumberFormatException e) {
			upHeightInt = 0;
		}
		try {
			breakGlueLenInt = Integer.parseInt(et_lineend_breakGlueLen.getText().toString());
		} catch (NumberFormatException e) {
			breakGlueLenInt = 0;
		}
		try {
			drawDistanceInt = Integer.parseInt(et_lineend_drawDistance.getText().toString());
		} catch (NumberFormatException e) {
			drawDistanceInt = 0;
		}
		try {
			drawSpeedInt = Integer.parseInt(et_lineend_drawSpeed.getText().toString());
		} catch (NumberFormatException e) {
			drawSpeedInt = 0;
		}

		glueEnd.setStopGlueTimePrev(stopTimePrevInt);
		glueEnd.setStopGlueTime(stopTimeInt);
		glueEnd.setUpHeight(upHeightInt);
		glueEnd.setBreakGlueLen(breakGlueLenInt);
		glueEnd.setDrawDistance(drawDistanceInt);
		glueEnd.setDrawSpeed(drawSpeedInt);
		glueEnd.setPause(isPauseSwitch.isChecked());

		glueEnd.set_id(param_id);
		return glueEnd;
	}

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
				glueEnd = getLineEnd();
				if (glueEndLists.contains(glueEnd)) {
					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.task_is_exist_yes));
				} else {
					glueEndDao.insertGlueLineEnd(glueEnd);
					glueEndLists = glueEndDao.findAllGlueLineEndParams();

					mEndAdapter.setGlueEndLists(glueEndLists);
					mEndAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}

			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {
				glueEnd = getLineEnd();
				if (!glueEndLists.contains(glueEnd)) {
					long rowID = glueEndDao.insertGlueLineEnd(glueEnd);
					glueEnd.set_id((int) rowID);
				} else {
					int id = glueEndLists.indexOf(glueEnd);
					glueEnd.set_id(glueEndLists.get(id).get_id());
				}
				point.setPointParam(glueEnd);

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
