/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueFaceEndAdapter;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
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
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import static com.mingseal.data.param.PointConfigParam.GlueFaceEnd;

/**
 * @author 商炎炳
 * @description 面终点
 */
public class GlueFaceEndActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueFaceEndActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * @Fields et_end_stopGlueTime: 停胶延时
	 */
	private EditText et_end_stopGlueTime;
	/**
	 * @Fields et_end_upHeight: 抬起高度
	 */
	private EditText et_end_upHeight;
	/**
	 * @Fields et_end_lineNum: 直线条数
	 */
	private EditText et_end_lineNum;
	/**
	 * 起始方向true:x方向 false:y方向
	 */
	// private Switch startDirSwitch;
	/**
	 * 是否暂停
	 */
	private Switch isPauseSwitch;
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

	/**
	 * 面结束点Spinner
	 */
	private Spinner faceEndSpinner;

	/**
	 * 装载任务方案的适配器
	 */
	private PointGlueFaceEndAdapter mFaceEndAdapter;

	/**
	 * PointGlueFaceEndParam List集合
	 */
	private List<PointGlueFaceEndParam> pointEndLists;

	private PointGlueFaceEndParam pointEnd;

	private GlueFaceEndDao glueFaceEndDao;

	private Intent intent;
	private Point point;// 从taskActivity中传值传过来的point
	/**
	 * 将方案中的id保存下来
	 */
	private int param_id = 1;
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据
	/**
	 * @Fields stopGlueTimeInt: 停胶延时int值
	 */
	private int stopGlueTimeInt = 0;
	/**
	 * @Fields upHeightInt: 抬起高度int值
	 */
	private int upHeightInt = 0;
	/**
	 * @Fields lineNumeInt: 直线条数int值
	 */
	private int lineNumeInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_face_end);
		intent = getIntent();
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		glueFaceEndDao = new GlueFaceEndDao(GlueFaceEndActivity.this);
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		if (pointEndLists == null || pointEndLists.isEmpty()) {
			pointEnd = new PointGlueFaceEndParam();
			glueFaceEndDao.insertGlueFaceEnd(pointEnd);
			// 重新获取一下数据
			pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		}

		mFaceEndAdapter = new PointGlueFaceEndAdapter(GlueFaceEndActivity.this);
		mFaceEndAdapter.setGlueStartLists(pointEndLists);
		faceEndSpinner.setAdapter(mFaceEndAdapter);
		// 如果为1的话，需要设置值
		if (mType == 1) {
			faceEndSpinner.setSelection(point.getPointParam().get_id() - 1);
			mFaceEndAdapter.notifyDataSetChanged();
		}
		faceEndSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointGlueFaceEndParam point = mFaceEndAdapter.getItem(position);

				et_end_stopGlueTime.setText(point.getStopGlueTime() + "");
				et_end_upHeight.setText(point.getUpHeight() + "");
				et_end_lineNum.setText(point.getLineNum() + "");

				isPauseSwitch.setChecked(point.isPause());

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
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_end_stopGlueTime = (EditText) findViewById(R.id.et_faceend_stopGlueTime);
		et_end_upHeight = (EditText) findViewById(R.id.et_faceend_upheight);
		et_end_lineNum = (EditText) findViewById(R.id.et_faceend_lineNum);
		isPauseSwitch = (Switch) findViewById(R.id.switch_isPause);

		faceEndSpinner = (Spinner) findViewById(R.id.spinner_face_end);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置最大最小值
		et_end_stopGlueTime.addTextChangedListener(
				new MaxMinEditWatcher(GlueFaceEnd.StopGlueTimeMax, GlueFaceEnd.GlueFaceEndMin, et_end_stopGlueTime));
		et_end_stopGlueTime.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueFaceEnd.StopGlueTimeMax,
				GlueFaceEnd.GlueFaceEndMin, et_end_stopGlueTime));
		et_end_stopGlueTime.setSelectAllOnFocus(true);

		et_end_upHeight.addTextChangedListener(
				new MaxMinEditWatcher(GlueFaceEnd.UpHeightMax, GlueFaceEnd.GlueFaceEndMin, et_end_upHeight));
		et_end_upHeight.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueFaceEnd.UpHeightMax, GlueFaceEnd.GlueFaceEndMin, et_end_upHeight));
		et_end_upHeight.setSelectAllOnFocus(true);

		et_end_lineNum.addTextChangedListener(
				new MaxMinEditWatcher(GlueFaceEnd.LineNumMax, GlueFaceEnd.GlueFaceEndMin, et_end_lineNum));
		et_end_lineNum.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueFaceEnd.LineNumMax, GlueFaceEnd.GlueFaceEndMin, et_end_lineNum));
		et_end_lineNum.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_face_end));
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
		if ("".equals(et_end_lineNum.getText().toString())) {
			return false;
		} else if ("".equals(et_end_stopGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_end_upHeight.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到一个PointGlueFaceEndParam对象中
	 * 
	 * @return PointGlueFaceEndParam
	 */
	private PointGlueFaceEndParam getFaceEnd() {
		pointEnd = new PointGlueFaceEndParam();
		try {
			stopGlueTimeInt = Integer.parseInt(et_end_stopGlueTime.getText().toString());
		} catch (NumberFormatException e) {
			stopGlueTimeInt = 0;
		}
		try {
			upHeightInt = Integer.parseInt(et_end_upHeight.getText().toString());
		} catch (NumberFormatException e) {
			upHeightInt = 0;
		}
		try {
			lineNumeInt = Integer.parseInt(et_end_lineNum.getText().toString());
		} catch (NumberFormatException e) {
			lineNumeInt = 0;
		}
		pointEnd.setStopGlueTime(stopGlueTimeInt);
		pointEnd.setUpHeight(upHeightInt);
		pointEnd.setLineNum(lineNumeInt);
		pointEnd.setPause(isPauseSwitch.isChecked());
		pointEnd.set_id(param_id);

		return pointEnd;
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
				pointEnd = getFaceEnd();
				if (pointEndLists.contains(pointEnd)) {
					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.task_is_exist_yes));
				} else {
					glueFaceEndDao.insertGlueFaceEnd(pointEnd);

					pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();

					mFaceEndAdapter.setGlueStartLists(pointEndLists);
					mFaceEndAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}
			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {
				pointEnd = getFaceEnd();
				if (pointEndLists.contains(pointEnd)) {
					int id = pointEndLists.indexOf(pointEnd);
					pointEnd.set_id(pointEndLists.get(id).get_id());
				} else {
					long rowID = glueFaceEndDao.insertGlueFaceEnd(pointEnd);
					pointEnd.set_id((int) rowID);
				}

				point.setPointParam(pointEnd);

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
