/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueLineMidAdapter;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.listener.TextEditWatcher;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import static com.mingseal.data.param.PointConfigParam.GlueLineMid;

/**
 * @author 商炎炳
 *
 */
public class GlueLineMidActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueLineMidActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * @Fields et_mid_moveSpeed: 移动速度
	 */
	private EditText et_mid_moveSpeed;

	/**
	 * 圆角半径
	 */
	private EditText radiusEdit;
	/**
	 * 断胶前距离
	 */
	private EditText stopDisPrevEdit;
	/**
	 * 断胶后距离
	 */
	private EditText stopDisNextEdit;
	/**
	 * 是否出胶
	 */
	private Switch isOutGlueSwitch;
	/**
	 * 点胶口
	 */
	private Switch[] isGluePort;

	/**
	 * 中间点的方案Spinner
	 */
	private Spinner lineMidSpinner;

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

	private GlueLineMidDao glueMidDao;
	private PointGlueLineMidAdapter mMidAdapter;// 线中间点的适配器
	private List<PointGlueLineMidParam> glueMidLists;
	private PointGlueLineMidParam glueMid;
	private boolean[] glueBoolean;
	private int param_id = 1;/// 选取的是几号方案
	/**
	 * @Fields moveSpeedInt: 轨迹速度的int值
	 */
	private int moveSpeedInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_line_mid);

		intent = getIntent();
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		TextEditWatcher teWatcher = new TextEditWatcher();
		stopDisPrevEdit.addTextChangedListener(teWatcher);
		stopDisNextEdit.addTextChangedListener(teWatcher);
		radiusEdit.addTextChangedListener(teWatcher);

		glueMidDao = new GlueLineMidDao(GlueLineMidActivity.this);
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		if (glueMidLists == null || glueMidLists.isEmpty()) {
			glueMid = new PointGlueLineMidParam();
			glueMidDao.insertGlueLineMid(glueMid);
			glueMidLists = glueMidDao.findAllGlueLineMidParams();

		}
		Log.d(TAG, glueMidLists.toString());

		mMidAdapter = new PointGlueLineMidAdapter(GlueLineMidActivity.this);
		mMidAdapter.setGlueMidLists(glueMidLists);
		lineMidSpinner.setAdapter(mMidAdapter);

		// 如果为1的话，需要设置值
		if (mType == 1) {
			lineMidSpinner.setSelection(point.getPointParam().get_id() - 1);
			mMidAdapter.notifyDataSetChanged();
		}
		// 初始化数组
		glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];

		lineMidSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointGlueLineMidParam point = mMidAdapter.getItem(position);
				et_mid_moveSpeed.setText(point.getMoveSpeed() + "");
				radiusEdit.setText(point.getRadius() + "");
				stopDisPrevEdit.setText(point.getStopGlueDisPrev() + "");
				stopDisNextEdit.setText(point.getStopGLueDisNext() + "");
				isOutGlueSwitch.setChecked(point.isOutGlue());

				isGluePort[0].setChecked(point.getGluePort()[0]);
				isGluePort[1].setChecked(point.getGluePort()[1]);
				isGluePort[2].setChecked(point.getGluePort()[2]);
				isGluePort[3].setChecked(point.getGluePort()[3]);
				isGluePort[4].setChecked(point.getGluePort()[4]);

				param_id = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 加载自定义组件并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_mid_moveSpeed = (EditText) findViewById(R.id.et_linemid_moveSpeed);
		radiusEdit = (EditText) findViewById(R.id.et_radius);
		stopDisPrevEdit = (EditText) findViewById(R.id.et_stopDisPrev);
		stopDisNextEdit = (EditText) findViewById(R.id.et_stopDisNext);
		isOutGlueSwitch = (Switch) findViewById(R.id.switch_isOutGlue);

		isGluePort = new Switch[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (Switch) findViewById(R.id.switch_glueport1);
		isGluePort[1] = (Switch) findViewById(R.id.switch_glueport2);
		isGluePort[2] = (Switch) findViewById(R.id.switch_glueport3);
		isGluePort[3] = (Switch) findViewById(R.id.switch_glueport4);
		isGluePort[4] = (Switch) findViewById(R.id.switch_glueport5);

		lineMidSpinner = (Spinner) findViewById(R.id.spinner_line_mid);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 轨迹速度设置最大最小值
		et_mid_moveSpeed.addTextChangedListener(
				new MaxMinEditWatcher(GlueLineMid.MoveSpeedMax, GlueLineMid.GlueLineMidMin, et_mid_moveSpeed));
		et_mid_moveSpeed.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueLineMid.MoveSpeedMax, GlueLineMid.GlueLineMidMin, et_mid_moveSpeed));
		et_mid_moveSpeed.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_line_mid));
		rl_back.setOnClickListener(this);
		rl_save.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

		// 点击全选
		stopDisPrevEdit.setSelectAllOnFocus(true);
		stopDisNextEdit.setSelectAllOnFocus(true);
		radiusEdit.setSelectAllOnFocus(true);
	}

	/**
	 * @Title isEditNull
	 * @Description 判断输入框是否为空
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditNull() {
		if ("".equals(et_mid_moveSpeed.getText().toString())) {
			return false;
		} else if ("".equals(radiusEdit.getText().toString())) {
			return false;
		} else if ("".equals(stopDisNextEdit.getText().toString())) {
			return false;
		} else if ("".equals(stopDisPrevEdit.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到PointGlueLineMidParam对象中
	 * 
	 * @return PointGlueLineMidParam
	 */
	private PointGlueLineMidParam getLineMid() {
		glueMid = new PointGlueLineMidParam();

		try {
			moveSpeedInt = Integer.parseInt(et_mid_moveSpeed.getText().toString());
			if (moveSpeedInt == 0) {
				moveSpeedInt = 1;
			}
		} catch (NumberFormatException e) {
			moveSpeedInt = 1;
		}

		glueMid.setMoveSpeed(moveSpeedInt);
		glueMid.setRadius(Float.parseFloat(radiusEdit.getText().toString()));
		glueMid.setStopGlueDisPrev(Float.parseFloat(stopDisPrevEdit.getText().toString()));
		glueMid.setStopGLueDisNext(Float.parseFloat(stopDisNextEdit.getText().toString()));
		glueMid.setOutGlue(isOutGlueSwitch.isChecked());

		glueBoolean[0] = isGluePort[0].isChecked();
		glueBoolean[1] = isGluePort[1].isChecked();
		glueBoolean[2] = isGluePort[2].isChecked();
		glueBoolean[3] = isGluePort[3].isChecked();
		glueBoolean[4] = isGluePort[4].isChecked();
		glueMid.setGluePort(glueBoolean);
		glueMid.set_id(param_id);

		return glueMid;
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
				glueMid = getLineMid();
				if (glueMidLists.contains(glueMid)) {
					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.task_is_exist_yes));
				} else {
					glueMidDao.insertGlueLineMid(glueMid);

					glueMidLists = glueMidDao.findAllGlueLineMidParams();

					mMidAdapter.setGlueMidLists(glueMidLists);
					mMidAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}

			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {
				glueMid = getLineMid();
				if (!glueMidLists.contains(glueMid)) {
					long rowID = glueMidDao.insertGlueLineMid(glueMid);
					glueMid.set_id((int) rowID);
				} else {
					int id = glueMidLists.indexOf(glueMid);
					glueMid.set_id(glueMidLists.get(id).get_id());
				}

				point.setPointParam(glueMid);

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
