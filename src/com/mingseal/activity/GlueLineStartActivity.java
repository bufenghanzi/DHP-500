/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueFaceEndAdapter;
import com.mingseal.adapter.PointGlueLineStartAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueLineStartDao;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import static com.mingseal.data.param.PointConfigParam.GlueLineStart;

/**
 * @author 商炎炳
 * 
 */
public class GlueLineStartActivity extends Activity implements OnClickListener,
		Callback {

	private final static String TAG = "GlueLineStartActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;

	/**
	 * @Fields et_start_outGlueTimePrev: 提前出胶时间
	 */
	private EditText et_start_outGlueTimePrev;
	/**
	 * @Fields et_start_outGlueTime: 滞后出胶时间
	 */
	private EditText et_start_outGlueTime;
	/**
	 * @Fields et_start_moveSpeed: 轨迹速度
	 */
	private EditText et_start_moveSpeed;
	// /**
	// * @Fields et_start_stopGlueTimePrev: 停胶前延时
	// */
	// private EditText et_start_stopGlueTimePrev;
	// /**
	// * @Fields et_start_stopGlueTime: 停胶后延时
	// */
	// private EditText et_start_stopGlueTime;
	// /**
	// * @Fields et_start_upHeight: 抬起高度
	// */
	// private EditText et_start_upHeight;

	/**
	 * 是否出胶
	 */
	private Switch isOutGlueSwitch;
	/**
	 * 延时模式 true:联动(ETimeNode.TIME_MODE_GANGED_TIME) 延时模式
	 * false:定时(ETimeMode.TIME_MODE_FIXED_TIME)
	 */
	private Switch timeModeSwitch;

	/**
	 * 点胶口
	 */
	private Switch[] isGluePort;

	/**
	 * 线起始点Spinner
	 */
	private Spinner lineStartSpinner;

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

	private GlueLineStartDao glueStartDao;
	private List<PointGlueLineStartParam> glueStartLists;
	private PointGlueLineStartParam glueStart;
	private PointGlueLineStartAdapter mStartAdapter;
	private boolean[] glueBoolean;
	private int param_id = 1;// / 选取的是几号方案
	/**
	 * @Fields outGlueTimePrevInt: 出胶前延时的int值
	 */
	private int outGlueTimePrevInt = 0;
	/**
	 * @Fields outGlueTimeInt: 出胶后延时的int值
	 */
	private int outGlueTimeInt = 0;
	/**
	 * @Fields moveSpeedInt: 轨迹速度的int值
	 */
	private int moveSpeedInt = 0;
	/**
	 * @Fields stopGlueTimePrevInt: 停胶前延时的int值
	 */
	private int stopGlueTimePrevInt = 0;
	/**
	 * @Fields stopGlueTimeInt: 停胶后延时的int值
	 */
	private int stopGlueTimeInt = 0;
	/**
	 * @Fields upHeightInt: 抬起高度的int值
	 */
	private int upHeightInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private TextView tv_num;
	private TextView tv_outGlue;
	private TextView tv_moveSpeed;
	private LinearLayout plan;
	private Handler handler;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_line_start);

		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		Log.d(TAG, point.toString());
		initPicker();

		glueStartDao = new GlueLineStartDao(GlueLineStartActivity.this);
		glueStartLists = glueStartDao.findAllGlueLineStartParams();
		if (glueStartLists == null || glueStartLists.isEmpty()) {
			glueStart = new PointGlueLineStartParam();
			glueStartDao.insertGlueLineStart(glueStart);
			// 插入主键id
			glueStart.set_id(param_id);
		}
		glueStartLists = glueStartDao.findAllGlueLineStartParams();
		// 初始化Handler,用来处理消息
		handler = new Handler(GlueLineStartActivity.this);
		if (mType == 1) {
			PointGlueLineStartParam glueLineStartParam = glueStartDao.getPointGlueLineStartParamByID(point.getPointParam().get_id());
			param_id = glueStartDao.getLineStartParamIDByParam(glueLineStartParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(glueLineStartParam);
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueLineStartParam defaultParam = glueStartLists.get(0);
			param_id = glueStartDao.getLineStartParamIDByParam(defaultParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultParam);
		}
		// mStartAdapter = new PointGlueLineStartAdapter(
		// GlueLineStartActivity.this);
		// mStartAdapter.setGlueStartLists(glueStartLists);
		// lineStartSpinner.setAdapter(mStartAdapter);
		//
		// // 如果为1的话，需要设置值
		// if (mType == 1) {
		// lineStartSpinner.setSelection(point.getPointParam().get_id() - 1);
		// mStartAdapter.notifyDataSetChanged();
		// }
		// 初始化数组
		glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];

		// lineStartSpinner
		// .setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent,
		// View view, int position, long id) {
		// PointGlueLineStartParam point = mStartAdapter
		// .getItem(position);
		// et_start_outGlueTimePrev.setText(point
		// .getOutGlueTimePrev() + "");
		// et_start_outGlueTime.setText(point.getOutGlueTime()
		// + "");
		// et_start_moveSpeed.setText(point.getMoveSpeed() + "");
		// // et_start_stopGlueTimePrev.setText(point.getStopGlueTimePrev()
		// // + "");
		// // et_start_stopGlueTime.setText(point.getStopGlueTime()
		// // + "");
		// // et_start_upHeight.setText(point.getUpHeight() + "");
		// isOutGlueSwitch.setChecked(point.isOutGlue());
		// timeModeSwitch.setChecked(point.isTimeMode());
		//
		// isGluePort[0].setChecked(point.getGluePort()[0]);
		// isGluePort[1].setChecked(point.getGluePort()[1]);
		// isGluePort[2].setChecked(point.getGluePort()[2]);
		// isGluePort[3].setChecked(point.getGluePort()[3]);
		// isGluePort[4].setChecked(point.getGluePort()[4]);
		//
		// param_id = position + 1;
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> parent) {
		//
		// }
		// });

	}

	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 更新整体ui界面
	 * @author wj
	 * @param glueLineStartParam
	 */
	private void SetDateAndRefreshUI(PointGlueLineStartParam glueLineStartParam) {
		tv_num.setText(String.valueOf(glueStartLists
				.indexOf(glueLineStartParam) + 1) + "");
		tv_outGlue.setText(glueLineStartParam.getOutGlueTimePrev() + "");
		tv_moveSpeed.setText(glueLineStartParam.getMoveSpeed() + "");
		UpdateInfos(glueLineStartParam);
	}

	/**
	 * @Title UpdateInfos
	 * @Description 更新上半部分界面
	 * @author wj
	 * @param glueLineStartParam
	 */
	private void UpdateInfos(PointGlueLineStartParam glueLineStartParam) {
		et_start_outGlueTimePrev.setText(glueLineStartParam
				.getOutGlueTimePrev() + "");
		et_start_outGlueTime.setText(glueLineStartParam.getOutGlueTime() + "");
		et_start_moveSpeed.setText(glueLineStartParam.getMoveSpeed() + "");
		isOutGlueSwitch.setChecked(glueLineStartParam.isOutGlue());
		timeModeSwitch.setChecked(glueLineStartParam.isTimeMode());

		isGluePort[0].setChecked(glueLineStartParam.getGluePort()[0]);
		isGluePort[1].setChecked(glueLineStartParam.getGluePort()[1]);
		isGluePort[2].setChecked(glueLineStartParam.getGluePort()[2]);
		isGluePort[3].setChecked(glueLineStartParam.getGluePort()[3]);
		isGluePort[4].setChecked(glueLineStartParam.getGluePort()[4]);
	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (!flag) {
			initWedget();
			flag = true;
		}

	}

	private void initWedget() {
		int width = plan.getWidth();
		pwidth = width;

		plan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					// 显示PopupWindow窗口
					popupWindwShowing();
				}
			}
		});

		// 初始化PopupWindow
		initPopuWindow();
	}

	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		mStartAdapter = new PointGlueLineStartAdapter(GlueLineStartActivity.this,
				handler);
		mStartAdapter.setGlueStartLists(glueStartLists);
		listView.setAdapter(mStartAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	protected void popupWindwShowing() {
		selectPopupWindow.showAsDropDown(plan, 0, -3);
	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);

		et_start_outGlueTimePrev = (EditText) findViewById(R.id.et_linestart_outGlueTimePrev);
		et_start_outGlueTime = (EditText) findViewById(R.id.et_linestart_outGlueTime);
		et_start_moveSpeed = (EditText) findViewById(R.id.et_linestart_moveSpeed);
		// et_start_stopGlueTimePrev = (EditText)
		// findViewById(R.id.et_linestart_stopGlueTimePrev);
		// et_start_stopGlueTime = (EditText)
		// findViewById(R.id.et_linestart_stopGlueTime);
		// et_start_upHeight = (EditText)
		// findViewById(R.id.et_linestart_upHeight);

		/* =================== begin =================== */
		tv_num = (TextView) findViewById(R.id.item_num);
		tv_outGlue = (TextView) findViewById(R.id.item_line_outglue);
		tv_moveSpeed = (TextView) findViewById(R.id.item_line_movespeed);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		/* =================== end =================== */
		isOutGlueSwitch = (Switch) findViewById(R.id.switch_isOutGlue);
		timeModeSwitch = (Switch) findViewById(R.id.switch_timeMode);

		isGluePort = new Switch[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (Switch) findViewById(R.id.switch_glueport1);
		isGluePort[1] = (Switch) findViewById(R.id.switch_glueport2);
		isGluePort[2] = (Switch) findViewById(R.id.switch_glueport3);
		isGluePort[3] = (Switch) findViewById(R.id.switch_glueport4);
		isGluePort[4] = (Switch) findViewById(R.id.switch_glueport5);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置出胶前延时的默认值和最大最小值
		et_start_outGlueTimePrev.addTextChangedListener(new MaxMinEditWatcher(
				GlueLineStart.OutGlueTimePrevMax,
				GlueLineStart.GlueLineStartMin, et_start_outGlueTimePrev));
		et_start_outGlueTimePrev
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueLineStart.OutGlueTimePrevMax,
						GlueLineStart.GlueLineStartMin,
						et_start_outGlueTimePrev));
		et_start_outGlueTimePrev.setSelectAllOnFocus(true);

		// 设置出胶后延时的默认值和最大最小值
		et_start_outGlueTime.addTextChangedListener(new MaxMinEditWatcher(
				GlueLineStart.OutGlueTimeMax, GlueLineStart.GlueLineStartMin,
				et_start_outGlueTime));
		et_start_outGlueTime
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueLineStart.OutGlueTimeMax,
						GlueLineStart.GlueLineStartMin, et_start_outGlueTime));
		et_start_outGlueTime.setSelectAllOnFocus(true);

		// 设置轨迹速度的默认值和最大最小值
		et_start_moveSpeed.addTextChangedListener(new MaxMinEditWatcher(
				GlueLineStart.MoveSpeedMax, GlueLineStart.MoveSpeedMin,
				et_start_moveSpeed));
		et_start_moveSpeed
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueLineStart.MoveSpeedMax, GlueLineStart.MoveSpeedMin,
						et_start_moveSpeed));
		et_start_moveSpeed.setSelectAllOnFocus(true);

		// 设置停胶前延时的默认值和最大最小值
		// et_start_stopGlueTimePrev.addTextChangedListener(new
		// MaxMinEditWatcher(GlueLineStart.StopGlueTimePrevMax,
		// GlueLineStart.GlueLineStartMin, et_start_stopGlueTimePrev));
		// et_start_stopGlueTimePrev.setOnFocusChangeListener(new
		// MaxMinFocusChangeListener(
		// GlueLineStart.StopGlueTimePrevMax, GlueLineStart.GlueLineStartMin,
		// et_start_stopGlueTimePrev));
		// et_start_stopGlueTimePrev.setSelectAllOnFocus(true);

		// 设置停胶后延时的默认值和最大最小值
		// et_start_stopGlueTime.addTextChangedListener(new
		// MaxMinEditWatcher(GlueLineStart.StopGlueTimeMax,
		// GlueLineStart.GlueLineStartMin, et_start_stopGlueTime));
		// et_start_stopGlueTime.setOnFocusChangeListener(new
		// MaxMinFocusChangeListener(GlueLineStart.StopGlueTimeMax,
		// GlueLineStart.GlueLineStartMin, et_start_stopGlueTime));
		// et_start_stopGlueTime.setSelectAllOnFocus(true);

		// 设置抬起高度的默认值和最大最小值
		// et_start_upHeight.addTextChangedListener(
		// new MaxMinEditWatcher(GlueLineStart.UpHeightMax,
		// GlueLineStart.GlueLineStartMin, et_start_upHeight));
		// et_start_upHeight.setOnFocusChangeListener(new
		// MaxMinFocusChangeListener(GlueLineStart.UpHeightMax,
		// GlueLineStart.GlueLineStartMin, et_start_upHeight));
		// et_start_upHeight.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(
				R.string.activity_glue_line_start));
		rl_back.setOnClickListener(this);
		rl_save.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

	}

	/**
	 * 将页面上的数据保存到一个PointGlueLineStartParam对象中
	 * 
	 * @return PointGlueLineStartParam
	 */
	private PointGlueLineStartParam getLineStart() {
		glueStart = new PointGlueLineStartParam();
		try {
			outGlueTimePrevInt = Integer.parseInt(et_start_outGlueTimePrev
					.getText().toString());
		} catch (NumberFormatException e) {
			outGlueTimePrevInt = 0;
		}
		try {
			outGlueTimeInt = Integer.parseInt(et_start_outGlueTime.getText()
					.toString());
		} catch (NumberFormatException e) {
			outGlueTimeInt = 0;
		}
		try {
			moveSpeedInt = Integer.parseInt(et_start_moveSpeed.getText()
					.toString());
			if (moveSpeedInt == 0) {
				moveSpeedInt = 1;
			}
		} catch (NumberFormatException e) {
			moveSpeedInt = 1;
		}
		// try {
		// stopGlueTimePrevInt =
		// Integer.parseInt(et_start_stopGlueTimePrev.getText().toString());
		// } catch (NumberFormatException e) {
		// stopGlueTimePrevInt = 0;
		// }
		// try {
		// stopGlueTimeInt =
		// Integer.parseInt(et_start_stopGlueTime.getText().toString());
		// } catch (NumberFormatException e) {
		// stopGlueTimeInt = 0;
		// }
		// try {
		// upHeightInt =
		// Integer.parseInt(et_start_upHeight.getText().toString());
		// } catch (NumberFormatException e) {
		// upHeightInt = 0;
		// }
		glueStart.setOutGlueTimePrev(outGlueTimePrevInt);
		glueStart.setOutGlueTime(outGlueTimeInt);
		glueStart.setMoveSpeed(moveSpeedInt);
		// glueStart.setStopGlueTimePrev(stopGlueTimePrevInt);
		// glueStart.setStopGlueTime(stopGlueTimeInt);
		// glueStart.setUpHeight(upHeightInt);
		glueStart.setOutGlue(isOutGlueSwitch.isChecked());
		glueStart.setTimeMode(timeModeSwitch.isChecked());

		glueBoolean[0] = isGluePort[0].isChecked();
		glueBoolean[1] = isGluePort[1].isChecked();
		glueBoolean[2] = isGluePort[2].isChecked();
		glueBoolean[3] = isGluePort[3].isChecked();
		glueBoolean[4] = isGluePort[4].isChecked();
		glueStart.setGluePort(glueBoolean);
		// glueStart.set_id(param_id);

		return glueStart;
	}

	/**
	 * @Title isEditNull
	 * @Description 判断输入框是否为空
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditNull() {
		if ("".equals(et_start_outGlueTimePrev.getText().toString())) {
			return false;
		} else if ("".equals(et_start_outGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_start_moveSpeed.getText().toString())) {
			return false;
		}
		// else if ("".equals(et_start_stopGlueTimePrev.getText().toString())) {
		// return false;
		// } else if ("".equals(et_start_stopGlueTime.getText().toString())) {
		// return false;
		// } else if ("".equals(et_start_upHeight.getText().toString())) {
		// return false;
		// }
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_back:// 返回按钮的响应事件
			finish();
			overridePendingTransition(R.anim.in_from_left,
					R.anim.out_from_right);
			break;
		case R.id.rl_save:// 保存新方案
			isNull = isEditNull();
			if (isNull) {
				glueStart = getLineStart();
				if (glueStartLists.contains(glueStart)) {
					ToastUtil.displayPromptInfo(this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowid = glueStartDao.insertGlueLineStart(glueStart);
					glueStart.set_id((int)rowid);
					glueStartLists = glueStartDao.findAllGlueLineStartParams();

					mStartAdapter.setGlueStartLists(glueStartLists);
					mStartAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this,
							getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this,
						getResources().getString(R.string.data_is_null));
			}
			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {
				glueStart = getLineStart();
				if (glueStartLists.contains(glueStart)) {
					int id = glueStartLists.indexOf(glueStart);
					// 如果方案里有的话,只需要设置一下id就行
					param_id = glueStartDao.getLineStartParamIDByParam(glueStart);// 默认的参数序列主键。
					glueStart.set_id(param_id);
				} else {
					long rowID = glueStartDao.insertGlueLineStart(glueStart);
					glueStart.set_id((int) rowID);
				}

				point.setPointParam(glueStart);

				Bundle extras = new Bundle();
				extras.putParcelable(MyPopWindowClickListener.POPWINDOW_KEY,
						point);
				extras.putInt(MyPopWindowClickListener.FLAG_KEY, mFlag);

				intent.putExtras(extras);
				setResult(TaskActivity.resultCode, intent);

				finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_from_right);
			} else {
				ToastUtil.displayPromptInfo(this,
						getResources().getString(R.string.data_is_null));
			}

			break;

		}

	}
	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {
		case Const.POINTGLUELINESTART_CLICK:
			glueStartLists = glueStartDao.findAllGlueLineStartParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = glueStartLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + glueStartLists.get(position).get_id());
			PointGlueLineStartParam glueLineStartParam = glueStartLists.get(position);
			SetDateAndRefreshUI(glueLineStartParam);
			dismiss();
			// 更新界面
//			UpdateInfos(glueFaceStartParam);
			break;
		case Const.POINTGLUELINESTART_TOP:
			// 置顶
			glueStartLists = glueStartDao.findAllGlueLineStartParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
				// 1为成功删除，0为未成功删除
				int result = glueStartDao.deleteParam(pointGlueLineStartParam);
				if (result == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueLineStartParam topGlueLineStartParam = glueStartLists.get(top_position);// 需要置顶的数据
			glueStartLists.remove(top_position);// 移除该数据
			glueStartLists.add(0, topGlueLineStartParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueLineStartParam pointGlueLineStartParam : glueStartLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = glueStartDao.insertGlueLineStart(pointGlueLineStartParam);
				// 重新分配主键id
				pointGlueLineStartParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mStartAdapter.setGlueStartLists(glueStartLists);
			mStartAdapter.notifyDataSetChanged();
			break;
		case Const.POINTGLUELINESTART_DEL:// 删除方案
			glueStartLists = glueStartDao.findAllGlueLineStartParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
//			System.out.println("删除的主键param_id：" + param_id);
//			System.out.println("删除的位置del_position：" + del_position);
//			System.out.println("删除之前的glueAloneLists的大小："
//					+ glueStartLists.size());
//			System.out.println("删除之前的方案主键:"
//					+ glueStartLists.get(del_position).get_id());

			// 删除到最后一个
			if (glueStartLists.size() == 1 && del_position == 0) {
				PointGlueLineStartParam lastParam = new PointGlueLineStartParam();
				glueStartDao.deleteParam(glueStartLists.get(0));// 删除当前方案
				glueStartDao.insertGlueLineStart(lastParam);// 默认方案
				lastParam.set_id(glueStartLists.get(0).get_id() + 1);// 设置主键
				glueStartLists = glueStartDao.findAllGlueLineStartParams();
				mStartAdapter.setGlueStartLists(glueStartLists);
				mStartAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastParam);
			} else {
				glueStartDao.deleteParam(glueStartLists.get(del_position));
				glueStartLists.remove(del_position);
				mStartAdapter.setGlueStartLists(glueStartLists);
				mStartAdapter.notifyDataSetChanged();
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(glueStartLists.get(0));
			break;
		}
		return false;
	}
}
