/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueOutputAdapter;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.point.IOPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
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
import static com.mingseal.data.param.PointConfigParam.GlueOutput;

/**
 * @author 商炎炳
 *
 */
public class GlueOutputActivity extends Activity implements OnClickListener {
	private final static String TAG = "GlueOutputActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * @Fields et_output_goTimePrev: 动作前延时
	 */
	private EditText et_output_goTimePrev;
	/**
	 * @Fields et_output_goTimeNext: 动作后延时
	 */
	private EditText et_output_goTimeNext;
	/**
	 * IO口
	 */
	private Switch[] ioSwitch;

	/**
	 * 输出IOSpinner
	 */
	private Spinner outputSpinner;

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

	private GlueOutputDao outputDao;
	private List<PointGlueOutputIOParam> outputIOLists;
	private PointGlueOutputIOParam outputIO;
	private PointGlueOutputAdapter mOutputAdapter;
	private boolean[] ioBoolean;
	private int param_id = 1;/// 选取的是几号方案
	/**
	 * @Fields goTimePrevInt: 动作前延时的int值
	 */
	private int goTimePrevInt = 0;
	/**
	 * @Fields goTimeNextInt: 动作后延时的int值
	 */
	private int goTimeNextInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glue_output);

		intent = getIntent();
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		outputDao = new GlueOutputDao(this);
		outputIOLists = outputDao.findAllGlueOutputParams();
		if (outputIOLists == null || outputIOLists.isEmpty()) {
			outputIO = new PointGlueOutputIOParam();
			outputDao.insertGlueOutput(outputIO);
			outputIOLists = outputDao.findAllGlueOutputParams();
		}

		mOutputAdapter = new PointGlueOutputAdapter(this);
		mOutputAdapter.setOutputIOParams(outputIOLists);
		outputSpinner.setAdapter(mOutputAdapter);
		// 如果为1的话，需要设置值
		if (mType == 1) {
			outputSpinner.setSelection(point.getPointParam().get_id() - 1);
			mOutputAdapter.notifyDataSetChanged();
		}
		// 初始化数组
		ioBoolean = new boolean[IOPort.IO_NO_ALL.ordinal()];

		outputSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointGlueOutputIOParam param = mOutputAdapter.getItem(position);
				et_output_goTimePrev.setText(param.getGoTimePrev() + "");
				et_output_goTimeNext.setText(param.getGoTimeNext() + "");

				ioSwitch[0].setChecked(param.getInputPort()[0]);
				ioSwitch[1].setChecked(param.getInputPort()[1]);
				ioSwitch[2].setChecked(param.getInputPort()[2]);
				ioSwitch[3].setChecked(param.getInputPort()[3]);

				param_id = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 加载组件，并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);

		et_output_goTimePrev = (EditText) findViewById(R.id.et_output_goTimePrev);
		et_output_goTimeNext = (EditText) findViewById(R.id.et_output_goTimeNext);
		ioSwitch = new Switch[IOPort.IO_NO_ALL.ordinal()];
		ioSwitch[0] = (Switch) findViewById(R.id.switch_glueport1);
		ioSwitch[1] = (Switch) findViewById(R.id.switch_glueport2);
		ioSwitch[2] = (Switch) findViewById(R.id.switch_glueport3);
		ioSwitch[3] = (Switch) findViewById(R.id.switch_glueport4);
		outputSpinner = (Spinner) findViewById(R.id.spinner_output);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置动作前延时的最大最小值
		et_output_goTimePrev.addTextChangedListener(
				new MaxMinEditWatcher(GlueOutput.GoTimePrevMax, GlueOutput.GlueOutputMin, et_output_goTimePrev));
		et_output_goTimePrev.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueOutput.GoTimePrevMax,
				GlueOutput.GlueOutputMin, et_output_goTimePrev));
		et_output_goTimePrev.setSelectAllOnFocus(true);

		// 设置动作后延时的最大最小值
		et_output_goTimeNext.addTextChangedListener(
				new MaxMinEditWatcher(GlueOutput.GoTimeNextMax, GlueOutput.GlueOutputMin, et_output_goTimeNext));
		et_output_goTimeNext.setOnFocusChangeListener(new MaxMinFocusChangeListener(GlueOutput.GoTimeNextMax,
				GlueOutput.GlueOutputMin, et_output_goTimeNext));
		et_output_goTimeNext.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_output));
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
		if ("".equals(et_output_goTimeNext.getText().toString())) {
			return false;
		} else if ("".equals(et_output_goTimePrev.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到PointGlueOutputIOParam对象中
	 * 
	 * @return PointGlueOutputIOParam
	 */
	private PointGlueOutputIOParam getOutputParam() {
		outputIO = new PointGlueOutputIOParam();

		try {
			goTimePrevInt = Integer.parseInt(et_output_goTimePrev.getText().toString());
		} catch (NumberFormatException e) {
			goTimePrevInt = 0;
		}
		try {
			goTimeNextInt = Integer.parseInt(et_output_goTimeNext.getText().toString());
		} catch (NumberFormatException e) {
			goTimeNextInt = 0;
		}
		outputIO.setGoTimePrev(goTimePrevInt);
		outputIO.setGoTimeNext(goTimeNextInt);
		ioBoolean[0] = ioSwitch[0].isChecked();
		ioBoolean[1] = ioSwitch[1].isChecked();
		ioBoolean[2] = ioSwitch[2].isChecked();
		ioBoolean[3] = ioSwitch[3].isChecked();
		outputIO.setInputPort(ioBoolean);
		outputIO.set_id(param_id);

		return outputIO;
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
				outputIO = getOutputParam();
				if (outputIOLists.contains(outputIO)) {
					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.task_is_exist_yes));
				} else {
					outputDao.insertGlueOutput(outputIO);

					outputIOLists = outputDao.findAllGlueOutputParams();

					mOutputAdapter.setOutputIOParams(outputIOLists);
					mOutputAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}
			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {

				outputIO = getOutputParam();
				if (!outputIOLists.contains(outputIO)) {
					long rowID = outputDao.insertGlueOutput(outputIO);
					outputIO.set_id((int) rowID);
				} else {
					int id = outputIOLists.indexOf(outputIO);
					outputIO.set_id(outputIOLists.get(id).get_id());
				}
				point.setPointParam(outputIO);

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
