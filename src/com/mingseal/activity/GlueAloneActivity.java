package com.mingseal.activity;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.adapter.PointGlueAloneAdapter;
import com.mingseal.data.dao.GlueAloneDao;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.SharedPreferencesCompat.EditorCompat;
import android.util.Log;
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
import static com.mingseal.data.param.PointConfigParam.GlueAlone;

/**
 * @author 商炎炳
 * @description 点胶独立点
 */
public class GlueAloneActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueAloneActivity";
	private TextView tv_title;// 标题栏的标题

	private Spinner taskSpinner;// 方案列表

	// private NumberPicker dianjiaoPicker;// 点胶延时
	// private NumberPicker tingjiaoPicker;// 停胶延时
	// private NumberPicker gaoduPicker;// 抬起高度
	/**
	 * @Fields et_alone_dianjiao: 点胶延时
	 */
	private EditText et_alone_dianjiao;
	/**
	 * @Fields et_alone_tingjiao: 停胶延时
	 */
	private EditText et_alone_tingjiao;
	/**
	 * @Fields et_alone_upHeight: 抬起高度
	 */
	private EditText et_alone_upHeight;

	private Switch isOutGlueSwitch;// 是否出胶
	private Switch isPause;// 是否暂停
	private Switch[] isGluePort;// 点胶口

	private RelativeLayout rl_save;// 保存方案的按钮
	private RelativeLayout rl_complete;// 完成的按钮
	private RelativeLayout rl_back;// 返回上级的按钮

	private List<PointGlueAloneParam> glueAloneLists;// 保存的方案
	private PointGlueAloneParam glueAlone;

	private PointGlueAloneAdapter mAloneAdapter;

	private Point point;// 从taskActivity中传值传过来的point
	private Intent intent;
	private boolean[] gluePortBoolean;
	private GlueAloneDao glueAloneDao;

	private int param_id = 1;
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据
	private int dotGlueTime = 0;
	private int stopGlueTime = 0;
	private int upHeight = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_alone);
		intent = getIntent();

		// point = (Point)
		// intent.getSerializableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		point = (Point) intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		Log.d(TAG, point.toString() + " FLAG:" + mFlag);

		initPicker();

		tv_title.setText(getResources().getString(R.string.activity_glue_alone));
		rl_back.setOnClickListener(this);
		rl_save.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

		glueAloneDao = new GlueAloneDao(GlueAloneActivity.this);
		// initData();
		glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		if (glueAloneLists == null || glueAloneLists.isEmpty()) {
			glueAlone = new PointGlueAloneParam();
			glueAloneDao.insertGlueAlone(glueAlone);
		}

		glueAloneLists = glueAloneDao.findAllGlueAloneParams();

		mAloneAdapter = new PointGlueAloneAdapter(GlueAloneActivity.this);
		mAloneAdapter.setGlueAloneLists(glueAloneLists);
		taskSpinner.setAdapter(mAloneAdapter);
		// 如果为1的话，需要设置值
		if (mType == 1) {
			taskSpinner.setSelection(point.getPointParam().get_id() - 1);
			mAloneAdapter.notifyDataSetChanged();
		}
		// 初始化
		gluePortBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];

		taskSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG, "position: " + position + ",id: " + id);

				PointGlueAloneParam pointGlueAloneParam = mAloneAdapter.getItem(position);
				et_alone_dianjiao.setText(pointGlueAloneParam.getDotGlueTime() + "");
				et_alone_tingjiao.setText(pointGlueAloneParam.getStopGlueTime() + "");
				et_alone_upHeight.setText(pointGlueAloneParam.getUpHeight() + "");
				isOutGlueSwitch.setChecked(pointGlueAloneParam.isOutGlue());
				isPause.setChecked(pointGlueAloneParam.isPause());

				isGluePort[0].setChecked(pointGlueAloneParam.getGluePort()[0]);
				isGluePort[1].setChecked(pointGlueAloneParam.getGluePort()[1]);
				isGluePort[2].setChecked(pointGlueAloneParam.getGluePort()[2]);
				isGluePort[3].setChecked(pointGlueAloneParam.getGluePort()[3]);
				isGluePort[4].setChecked(pointGlueAloneParam.getGluePort()[4]);

				param_id = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * 加载一些自定义的数据
	 */
	private void initData() {
		glueAloneLists = new ArrayList<PointGlueAloneParam>();
		glueAlone = new PointGlueAloneParam();
		glueAloneLists.add(glueAlone);
		for (int i = 0; i < 2; i++) {
			glueAlone = new PointGlueAloneParam();
			glueAlone.setDotGlueTime(200 + i);
			glueAlone.setStopGlueTime(20 + i);
			glueAlone.setUpHeight(400 + i * 2);
			glueAlone.setPause(false);

			glueAloneLists.add(glueAlone);
		}

	}

	/**
	 * 加载组件并设置NumberPicker最大最小值
	 */
	private void initPicker() {
		et_alone_dianjiao = (EditText) findViewById(R.id.et_alone_dianjiao);
		et_alone_tingjiao = (EditText) findViewById(R.id.et_alone_tingjiao);
		et_alone_upHeight = (EditText) findViewById(R.id.et_alone_upheight);
		isOutGlueSwitch = (Switch) findViewById(R.id.switch_chujiao);
		tv_title = (TextView) findViewById(R.id.tv_title);
		taskSpinner = (Spinner) findViewById(R.id.spinner_alone);
		isPause = (Switch) findViewById(R.id.switch_tingjiao);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);

		isGluePort = new Switch[GWOutPort.USER_O_NO_ALL.ordinal()];// 初始化20个点胶口
		isGluePort[0] = (Switch) findViewById(R.id.switch_dianjiaokou1);
		isGluePort[1] = (Switch) findViewById(R.id.switch_dianjiaokou2);
		isGluePort[2] = (Switch) findViewById(R.id.switch_dianjiaokou3);
		isGluePort[3] = (Switch) findViewById(R.id.switch_dianjiaokou4);
		isGluePort[4] = (Switch) findViewById(R.id.switch_dianjiaokou5);

		// 设置最大最小值
		et_alone_dianjiao.addTextChangedListener(
				new MaxMinEditWatcher(GlueAlone.DotGlueTimeMAX, GlueAlone.GlueAloneMIN, et_alone_dianjiao));
		et_alone_tingjiao.addTextChangedListener(
				new MaxMinEditWatcher(GlueAlone.StopGlueTimeMAX, GlueAlone.GlueAloneMIN, et_alone_tingjiao));
		et_alone_upHeight.addTextChangedListener(
				new MaxMinEditWatcher(GlueAlone.UpHeightMAX, GlueAlone.GlueAloneMIN, et_alone_upHeight));

		et_alone_dianjiao.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueAlone.DotGlueTimeMAX, GlueAlone.GlueAloneMIN, et_alone_dianjiao));
		et_alone_tingjiao.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueAlone.StopGlueTimeMAX, GlueAlone.GlueAloneMIN, et_alone_tingjiao));
		et_alone_upHeight.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueAlone.UpHeightMAX, GlueAlone.GlueAloneMIN, et_alone_upHeight));

		et_alone_dianjiao.setSelectAllOnFocus(true);
		et_alone_tingjiao.setSelectAllOnFocus(true);
		et_alone_upHeight.setSelectAllOnFocus(true);
	}

	/**
	 * @Title isEditNull
	 * @Description 判断输入框是否为空
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditNull() {
		if ("".equals(et_alone_dianjiao.getText().toString())) {
			return false;
		} else if ("".equals(et_alone_tingjiao.getText().toString())) {
			return false;
		} else if ("".equals(et_alone_upHeight.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上显示的数据保存到PointGlueAloneParam的一个对象中
	 * 
	 * @return
	 */
	private PointGlueAloneParam getGlueAlone() {
		glueAlone = new PointGlueAloneParam();

		try {
			dotGlueTime = Integer.parseInt(et_alone_dianjiao.getText().toString());
		} catch (NumberFormatException e) {
			dotGlueTime = 0;
		}
		try {
			stopGlueTime = Integer.parseInt(et_alone_tingjiao.getText().toString());
		} catch (NumberFormatException e) {
			stopGlueTime = 0;
		}
		try {
			upHeight = Integer.parseInt(et_alone_upHeight.getText().toString());
		} catch (NumberFormatException e) {
			upHeight = 0;
		}

		glueAlone.setDotGlueTime(dotGlueTime);
		glueAlone.setStopGlueTime(stopGlueTime);
		glueAlone.setUpHeight(upHeight);
		glueAlone.setOutGlue(isOutGlueSwitch.isChecked());
		glueAlone.setPause(isPause.isChecked());

		gluePortBoolean[0] = isGluePort[0].isChecked();
		gluePortBoolean[1] = isGluePort[1].isChecked();
		gluePortBoolean[2] = isGluePort[2].isChecked();
		gluePortBoolean[3] = isGluePort[3].isChecked();
		gluePortBoolean[4] = isGluePort[4].isChecked();

		// 设置数组的方法
		glueAlone.setGluePort(gluePortBoolean);

		glueAlone.set_id(param_id);

		return glueAlone;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_save:// 保存新方案
			isNull = isEditNull();
			if (isNull) {
				// Log.i(TAG, "保存新方案z之前-->"+glueAloneLists.toString());
				glueAlone = getGlueAlone();
				// Log.i(TAG, "保存新方案-->"+glueAloneLists.toString());

				// Log.d(TAG, "集合中是否存在： "+glueAloneLists.contains(glueAlone));

				if (glueAloneLists.contains(glueAlone)) {
					ToastUtil.displayPromptInfo(GlueAloneActivity.this,
							getResources().getString(R.string.task_is_exist_yes));
				} else {
					glueAloneDao.insertGlueAlone(glueAlone);
					// glueAloneLists.add(glueAlone);//加到方案里面去

					glueAloneLists = glueAloneDao.findAllGlueAloneParams();

					mAloneAdapter.setGlueAloneLists(glueAloneLists);
					mAloneAdapter.notifyDataSetChanged();

					Log.i(TAG, "保存之后新方案-->" + glueAloneLists.toString());
					ToastUtil.displayPromptInfo(GlueAloneActivity.this,
							getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}

			break;
		case R.id.rl_complete:// 完成按钮响应事件
			isNull = isEditNull();
			if (isNull) {
				glueAlone = getGlueAlone();
				if (glueAloneLists.contains(glueAlone)) {
					int id = glueAloneLists.indexOf(glueAlone);
					glueAlone.set_id(glueAloneLists.get(id).get_id());
				} else {
					// 方案中不存在的话就保存
					long rowID = glueAloneDao.insertGlueAlone(glueAlone);
					glueAlone.set_id((int) rowID);
				}

				point.setPointParam(glueAlone);

				Log.i(TAG, point.toString());
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
		case R.id.rl_back:// 返回按钮的响应事件
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);

			break;

		default:
			break;
		}
	}
}
