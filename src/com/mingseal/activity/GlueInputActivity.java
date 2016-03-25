/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueClearAdapter;
import com.mingseal.adapter.PointGlueInputAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueInputDao;
import com.mingseal.data.param.PointConfigParam.GlueInput;
import com.mingseal.data.point.IOPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author 商炎炳
 * 
 */
public class GlueInputActivity extends Activity implements OnClickListener, Callback {
	private final static String TAG = "GlueInputActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * @Fields et_input_goTimePrev: 动作前延时
	 */
	private EditText et_input_goTimePrev;
	/**
	 * @Fields et_input_goTimeNext: 动作后延时
	 */
	private EditText et_input_goTimeNext;
	/**
	 * IO口
	 */
	private Switch[] ioSwitch;

	/**
	 * 输出IOSpinner
	 */
	private Spinner inputSpinner;

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

	private GlueInputDao inputDao;
	private List<PointGlueInputIOParam> inputIOLists;
	private PointGlueInputIOParam inputIO;
	private PointGlueInputAdapter mInputAdapter;
	private boolean[] ioBoolean;
	private int param_id = 1;// / 选取的是几号方案
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
	private TextView tv_num;
	private TextView tv_goTimePrev;
	private TextView tv_goTimeNext;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glue_input);

		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		inputDao = new GlueInputDao(this);
		inputIOLists = inputDao.findAllGlueInputParams();
		if (inputIOLists == null || inputIOLists.isEmpty()) {
			inputIO = new PointGlueInputIOParam();
			inputDao.insertGlueInput(inputIO);
			// 插入主键id
			inputIO.set_id(param_id);
		}
		inputIOLists = inputDao.findAllGlueInputParams();
		// 初始化Handler,用来处理消息
		handler = new Handler(GlueInputActivity.this);
		if (mType == 1) {
			PointGlueInputIOParam glueInputIOParam = inputDao
					.getInputPointByID(point.getPointParam().get_id());
			param_id = inputDao.getInputParamIDByParam(glueInputIOParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(glueInputIOParam);
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueInputIOParam defaultParam = inputIOLists.get(0);
			param_id = inputDao.getInputParamIDByParam(defaultParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultParam);
		}
//		mInputAdapter = new PointGlueInputAdapter(this);
//		mInputAdapter.setInputIOParams(inputIOLists);
//		inputSpinner.setAdapter(mInputAdapter);
//		// 如果为1的话，需要设置值
//		if (mType == 1) {
//			inputSpinner.setSelection(point.getPointParam().get_id() - 1);
//			mInputAdapter.notifyDataSetChanged();
//		}
		// 初始化数组
		ioBoolean = new boolean[IOPort.IO_NO_ALL.ordinal()];

//		inputSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				PointGlueInputIOParam param = mInputAdapter.getItem(position);
//				et_input_goTimePrev.setText(param.getGoTimePrev() + "");
//				et_input_goTimeNext.setText(param.getGoTimeNext() + "");
//
//				ioSwitch[0].setChecked(param.getInputPort()[0]);
//				ioSwitch[1].setChecked(param.getInputPort()[1]);
//				ioSwitch[2].setChecked(param.getInputPort()[2]);
//				ioSwitch[3].setChecked(param.getInputPort()[3]);
//
//				param_id = position + 1;
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
	}

	private void SetDateAndRefreshUI(PointGlueInputIOParam glueInputIOParam) {

		tv_num.setText(String.valueOf(inputIOLists.indexOf(glueInputIOParam) + 1) + "");
		tv_goTimePrev.setText(glueInputIOParam.getGoTimePrev() + "");
		tv_goTimeNext.setText(glueInputIOParam.getGoTimeNext() + "");
		UpdateInfos(glueInputIOParam);
	}

	private void UpdateInfos(PointGlueInputIOParam glueInputIOParam) {
		et_input_goTimePrev.setText(glueInputIOParam.getGoTimePrev() + "");
		et_input_goTimeNext.setText(glueInputIOParam.getGoTimeNext() + "");

		ioSwitch[0].setChecked(glueInputIOParam.getInputPort()[0]);
		ioSwitch[1].setChecked(glueInputIOParam.getInputPort()[1]);
		ioSwitch[2].setChecked(glueInputIOParam.getInputPort()[2]);
		ioSwitch[3].setChecked(glueInputIOParam.getInputPort()[3]);
		
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

		mInputAdapter = new PointGlueInputAdapter(GlueInputActivity.this,
				handler);
		mInputAdapter.setInputIOParams(inputIOLists);
		listView.setAdapter(mInputAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	protected void popupWindwShowing() {
		selectPopupWindow.showAsDropDown(plan, 0, -3);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	/**
	 * 加载组件，并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);

		et_input_goTimePrev = (EditText) findViewById(R.id.et_input_goTimePrev);
		et_input_goTimeNext = (EditText) findViewById(R.id.et_input_goTimeNext);
		ioSwitch = new Switch[IOPort.IO_NO_ALL.ordinal()];
		ioSwitch[0] = (Switch) findViewById(R.id.switch_glueport1);
		ioSwitch[1] = (Switch) findViewById(R.id.switch_glueport2);
		ioSwitch[2] = (Switch) findViewById(R.id.switch_glueport3);
		ioSwitch[3] = (Switch) findViewById(R.id.switch_glueport4);
		// inputSpinner = (Spinner) findViewById(R.id.spinner_input);
		/* =================== begin =================== */
		tv_num = (TextView) findViewById(R.id.item_num);
		tv_goTimePrev = (TextView) findViewById(R.id.item_goTimePrev);
		tv_goTimeNext = (TextView) findViewById(R.id.item_goTimeNext);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		/* =================== end =================== */

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置动作前延时的最大最小值
		et_input_goTimePrev.addTextChangedListener(new MaxMinEditWatcher(
				GlueInput.GoTimePrevMax, GlueInput.GlueInputMin,
				et_input_goTimePrev));
		et_input_goTimePrev
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueInput.GoTimePrevMax, GlueInput.GlueInputMin,
						et_input_goTimePrev));
		et_input_goTimePrev.setSelectAllOnFocus(true);

		// 设置动作后延时的最大最小值
		et_input_goTimeNext.addTextChangedListener(new MaxMinEditWatcher(
				GlueInput.GoTimeNextMax, GlueInput.GlueInputMin,
				et_input_goTimeNext));
		et_input_goTimeNext
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueInput.GoTimeNextMax, GlueInput.GlueInputMin,
						et_input_goTimeNext));
		et_input_goTimeNext.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_input));
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
		if ("".equals(et_input_goTimeNext.getText().toString())) {
			return false;
		} else if ("".equals(et_input_goTimePrev.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到PointGlueOutputIOParam对象中
	 * 
	 * @return PointGlueInputIOParam
	 */
	private PointGlueInputIOParam getOutputParam() {
		inputIO = new PointGlueInputIOParam();

		try {
			goTimePrevInt = Integer.parseInt(et_input_goTimePrev.getText()
					.toString());
		} catch (NumberFormatException e) {
			goTimePrevInt = 0;
		}
		try {
			goTimeNextInt = Integer.parseInt(et_input_goTimeNext.getText()
					.toString());
		} catch (NumberFormatException e) {
			goTimeNextInt = 0;
		}
		inputIO.setGoTimePrev(goTimePrevInt);
		inputIO.setGoTimeNext(goTimeNextInt);
		ioBoolean[0] = ioSwitch[0].isChecked();
		ioBoolean[1] = ioSwitch[1].isChecked();
		ioBoolean[2] = ioSwitch[2].isChecked();
		ioBoolean[3] = ioSwitch[3].isChecked();
		inputIO.setInputPort(ioBoolean);
//		inputIO.set_id(param_id);

		return inputIO;
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
				inputIO = getOutputParam();
				if (inputIOLists.contains(inputIO)) {
					ToastUtil.displayPromptInfo(this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowid = inputDao.insertGlueInput(inputIO);
					inputIO.set_id((int)rowid);

					inputIOLists = inputDao.findAllGlueInputParams();

					mInputAdapter.setInputIOParams(inputIOLists);
					mInputAdapter.notifyDataSetChanged();

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
				inputIO = getOutputParam();
				if (!inputIOLists.contains(inputIO)) {
					long rowID = inputDao.insertGlueInput(inputIO);
					inputIO.set_id((int) rowID);
				} else {
					int id = inputIOLists.indexOf(inputIO);
					// 如果方案里有的话,只需要设置一下id就行
					param_id = inputDao.getInputParamIDByParam(inputIO);// 默认的参数序列主键。
					inputIO.set_id(param_id);
				}
				point.setPointParam(inputIO);

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

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {
		case Const.POINTGLUEINPUT_CLICK:
			inputIOLists = inputDao.findAllGlueInputParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = inputIOLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + inputIOLists.get(position).get_id());
			PointGlueInputIOParam glueInputIOParam = inputIOLists.get(position);
			SetDateAndRefreshUI(glueInputIOParam);
			dismiss();
			// 更新界面
//			UpdateInfos(glueFaceStartParam);
			break;
		case Const.POINTGLUEINPUT_TOP:
			// 置顶
			inputIOLists = inputDao.findAllGlueInputParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueInputIOParam pointGlueInputIOParam : inputIOLists) {
				// 1为成功删除，0为未成功删除
				int result = inputDao.deleteParam(pointGlueInputIOParam);
				if (result == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueInputIOParam topParam = inputIOLists
					.get(top_position);// 需要置顶的数据
			inputIOLists.remove(top_position);// 移除该数据
			inputIOLists.add(0, topParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueInputIOParam pointGlueInputIOParam : inputIOLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = inputDao.insertGlueInput(pointGlueInputIOParam);
				// 重新分配主键id
				pointGlueInputIOParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mInputAdapter.setInputIOParams(inputIOLists);
			mInputAdapter.notifyDataSetChanged();
			break;
		case Const.POINTGLUEINPUT_DEL:// 删除方案
			inputIOLists = inputDao.findAllGlueInputParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
//			System.out.println("删除的主键param_id：" + param_id);
//			System.out.println("删除的位置del_position：" + del_position);
//			System.out.println("删除之前的glueAloneLists的大小："
//					+ glueStartLists.size());
//			System.out.println("删除之前的方案主键:"
//					+ glueStartLists.get(del_position).get_id());

			// 删除到最后一个
			if (inputIOLists.size() == 1 && del_position == 0) {
				PointGlueInputIOParam lastParam = new PointGlueInputIOParam();
				inputDao.deleteParam(inputIOLists.get(0));// 删除当前方案
				inputDao.insertGlueInput(lastParam);// 默认方案
				lastParam.set_id(inputIOLists.get(0).get_id() + 1);// 设置主键
				inputIOLists = inputDao.findAllGlueInputParams();
				mInputAdapter.setInputIOParams(inputIOLists);
				mInputAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastParam);
			} else {
				inputDao.deleteParam(inputIOLists.get(del_position));
				inputIOLists.remove(del_position);
				mInputAdapter.setInputIOParams(inputIOLists);
				mInputAdapter.notifyDataSetChanged();
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(inputIOLists.get(0));
			break;
		}
		return false;
	}
}
