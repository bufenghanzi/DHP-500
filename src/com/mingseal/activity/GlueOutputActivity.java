/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueInputAdapter;
import com.mingseal.adapter.PointGlueOutputAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.point.IOPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
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
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import static com.mingseal.data.param.PointConfigParam.GlueOutput;

/**
 * @author 商炎炳
 * 
 */
public class GlueOutputActivity extends Activity implements OnClickListener, Callback {
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
			// 插入主键id
			outputIO.set_id(param_id);
		}
		outputIOLists = outputDao.findAllGlueOutputParams();

		// 初始化Handler,用来处理消息
		handler = new Handler(GlueOutputActivity.this);
		if (mType == 1) {
			PointGlueOutputIOParam glueOutputIOParam = outputDao
					.getOutPutPointByID(point.getPointParam().get_id());
			param_id = outputDao.getOutputParamIDByParam(glueOutputIOParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(glueOutputIOParam);
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueOutputIOParam defaultParam = outputIOLists.get(0);
			param_id = outputDao.getOutputParamIDByParam(defaultParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultParam);
		}
//		mOutputAdapter = new PointGlueOutputAdapter(this);
//		mOutputAdapter.setOutputIOParams(outputIOLists);
//		outputSpinner.setAdapter(mOutputAdapter);
//		// 如果为1的话，需要设置值
//		if (mType == 1) {
//			outputSpinner.setSelection(point.getPointParam().get_id() - 1);
//			mOutputAdapter.notifyDataSetChanged();
//		}
		// 初始化数组
		ioBoolean = new boolean[IOPort.IO_NO_ALL.ordinal()];

//		outputSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				PointGlueOutputIOParam param = mOutputAdapter.getItem(position);
//				et_output_goTimePrev.setText(param.getGoTimePrev() + "");
//				et_output_goTimeNext.setText(param.getGoTimeNext() + "");
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

	private void SetDateAndRefreshUI(PointGlueOutputIOParam glueOutputIOParam) {
		// TODO Auto-generated method stub
		tv_num.setText(String.valueOf(outputIOLists.indexOf(glueOutputIOParam) + 1) + "");
		tv_goTimePrev.setText(glueOutputIOParam.getGoTimePrev()+"");
		tv_goTimeNext.setText(glueOutputIOParam.getGoTimeNext()+"");
		UpdateInfos(glueOutputIOParam);
	}

	private void UpdateInfos(PointGlueOutputIOParam glueOutputIOParam) {
		// TODO Auto-generated method stub
		et_output_goTimePrev.setText(glueOutputIOParam.getGoTimePrev() + "");
		et_output_goTimeNext.setText(glueOutputIOParam.getGoTimeNext() + "");

		ioSwitch[0].setChecked(glueOutputIOParam.getInputPort()[0]);
		ioSwitch[1].setChecked(glueOutputIOParam.getInputPort()[1]);
		ioSwitch[2].setChecked(glueOutputIOParam.getInputPort()[2]);
		ioSwitch[3].setChecked(glueOutputIOParam.getInputPort()[3]);
		ioSwitch[4].setChecked(glueOutputIOParam.getInputPort()[4]);
		ioSwitch[5].setChecked(glueOutputIOParam.getInputPort()[5]);
		ioSwitch[6].setChecked(glueOutputIOParam.getInputPort()[6]);
		ioSwitch[7].setChecked(glueOutputIOParam.getInputPort()[7]);
		ioSwitch[8].setChecked(glueOutputIOParam.getInputPort()[8]);
		ioSwitch[9].setChecked(glueOutputIOParam.getInputPort()[9]);
		ioSwitch[10].setChecked(glueOutputIOParam.getInputPort()[10]);
		ioSwitch[11].setChecked(glueOutputIOParam.getInputPort()[11]);
		
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

		mOutputAdapter = new PointGlueOutputAdapter(GlueOutputActivity.this,
				handler);
		mOutputAdapter.setOutputIOParams(outputIOLists);
		listView.setAdapter(mOutputAdapter);

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

		et_output_goTimePrev = (EditText) findViewById(R.id.et_output_goTimePrev);
		et_output_goTimeNext = (EditText) findViewById(R.id.et_output_goTimeNext);
		ioSwitch = new Switch[IOPort.IO_NO_ALL.ordinal()];
		ioSwitch[0] = (Switch) findViewById(R.id.switch_glueport1);
		ioSwitch[1] = (Switch) findViewById(R.id.switch_glueport2);
		ioSwitch[2] = (Switch) findViewById(R.id.switch_glueport3);
		ioSwitch[3] = (Switch) findViewById(R.id.switch_glueport4);
		/* =================== begin =================== */
		ioSwitch[4] = (Switch) findViewById(R.id.switch_glueport5);
		ioSwitch[5] = (Switch) findViewById(R.id.switch_glueport6);
		ioSwitch[6] = (Switch) findViewById(R.id.switch_glueport7);
		ioSwitch[7] = (Switch) findViewById(R.id.switch_glueport8);
		ioSwitch[8] = (Switch) findViewById(R.id.switch_glueport9);
		ioSwitch[9] = (Switch) findViewById(R.id.switch_glueport10);
		ioSwitch[10] = (Switch) findViewById(R.id.switch_glueport11);
		ioSwitch[11] = (Switch) findViewById(R.id.switch_glueport12);
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
		et_output_goTimePrev.addTextChangedListener(new MaxMinEditWatcher(
				GlueOutput.GoTimePrevMax, GlueOutput.GlueOutputMin,
				et_output_goTimePrev));
		et_output_goTimePrev
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueOutput.GoTimePrevMax, GlueOutput.GlueOutputMin,
						et_output_goTimePrev));
		et_output_goTimePrev.setSelectAllOnFocus(true);

		// 设置动作后延时的最大最小值
		et_output_goTimeNext.addTextChangedListener(new MaxMinEditWatcher(
				GlueOutput.GoTimeNextMax, GlueOutput.GlueOutputMin,
				et_output_goTimeNext));
		et_output_goTimeNext
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueOutput.GoTimeNextMax, GlueOutput.GlueOutputMin,
						et_output_goTimeNext));
		et_output_goTimeNext.setSelectAllOnFocus(true);

		tv_title.setText(getResources()
				.getString(R.string.activity_glue_output));
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
			goTimePrevInt = Integer.parseInt(et_output_goTimePrev.getText()
					.toString());
		} catch (NumberFormatException e) {
			goTimePrevInt = 0;
		}
		try {
			goTimeNextInt = Integer.parseInt(et_output_goTimeNext.getText()
					.toString());
		} catch (NumberFormatException e) {
			goTimeNextInt = 0;
		}
		outputIO.setGoTimePrev(goTimePrevInt);
		outputIO.setGoTimeNext(goTimeNextInt);
		ioBoolean[0] = ioSwitch[0].isChecked();
		ioBoolean[1] = ioSwitch[1].isChecked();
		ioBoolean[2] = ioSwitch[2].isChecked();
		ioBoolean[3] = ioSwitch[3].isChecked();
		ioBoolean[4] = ioSwitch[4].isChecked();
		ioBoolean[5] = ioSwitch[5].isChecked();
		ioBoolean[6] = ioSwitch[6].isChecked();
		ioBoolean[7] = ioSwitch[7].isChecked();
		ioBoolean[8] = ioSwitch[8].isChecked();
		ioBoolean[9] = ioSwitch[9].isChecked();
		ioBoolean[10] = ioSwitch[10].isChecked();
		ioBoolean[11] = ioSwitch[11].isChecked();
		ioBoolean[12] = ioSwitch[12].isChecked();
		outputIO.setInputPort(ioBoolean);
//		outputIO.set_id(param_id);

		return outputIO;
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
				outputIO = getOutputParam();
				if (outputIOLists.contains(outputIO)) {
					ToastUtil.displayPromptInfo(this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowid = outputDao.insertGlueOutput(outputIO);
					outputIO.set_id((int)rowid);

					outputIOLists = outputDao.findAllGlueOutputParams();

					mOutputAdapter.setOutputIOParams(outputIOLists);
					mOutputAdapter.notifyDataSetChanged();

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

				outputIO = getOutputParam();
				if (!outputIOLists.contains(outputIO)) {
					long rowID = outputDao.insertGlueOutput(outputIO);
					outputIO.set_id((int) rowID);
				} else {
					int id = outputIOLists.indexOf(outputIO);
					param_id = outputDao.getOutputParamIDByParam(outputIO);// 默认的参数序列主键。
					outputIO.set_id(param_id);
				}
				point.setPointParam(outputIO);

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
		case Const.POINTGLUEOUTPUT_CLICK:
			outputIOLists = outputDao.findAllGlueOutputParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = outputIOLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + outputIOLists.get(position).get_id());
			PointGlueOutputIOParam glueOutputIOParam = outputIOLists.get(position);
			SetDateAndRefreshUI(glueOutputIOParam);
			dismiss();
			// 更新界面
//			UpdateInfos(glueFaceStartParam);
			break;
		case Const.POINTGLUEOUTPUT_TOP:
			// 置顶
			outputIOLists = outputDao.findAllGlueOutputParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
				// 1为成功删除，0为未成功删除
				int result = outputDao.deleteParam(pointGlueOutputIOParam);
				if (result == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueOutputIOParam topParam = outputIOLists
					.get(top_position);// 需要置顶的数据
			outputIOLists.remove(top_position);// 移除该数据
			outputIOLists.add(0, topParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = outputDao.insertGlueOutput(pointGlueOutputIOParam);
				// 重新分配主键id
				pointGlueOutputIOParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mOutputAdapter.setOutputIOParams(outputIOLists);
			mOutputAdapter.notifyDataSetChanged();
			break;
		case Const.POINTGLUEOUTPUT_DEL:// 删除方案
			outputIOLists = outputDao.findAllGlueOutputParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
//			System.out.println("删除的主键param_id：" + param_id);
//			System.out.println("删除的位置del_position：" + del_position);
//			System.out.println("删除之前的glueAloneLists的大小："
//					+ glueStartLists.size());
//			System.out.println("删除之前的方案主键:"
//					+ glueStartLists.get(del_position).get_id());

			// 删除到最后一个
			if (outputIOLists.size() == 1 && del_position == 0) {
				PointGlueOutputIOParam lastParam = new PointGlueOutputIOParam();
				outputDao.deleteParam(outputIOLists.get(0));// 删除当前方案
				outputDao.insertGlueOutput(lastParam);// 默认方案
				lastParam.set_id(outputIOLists.get(0).get_id() + 1);// 设置主键
				outputIOLists = outputDao.findAllGlueOutputParams();
				mOutputAdapter.setOutputIOParams(outputIOLists);
				mOutputAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastParam);
			} else {
				outputDao.deleteParam(outputIOLists.get(del_position));
				outputIOLists.remove(del_position);
				mOutputAdapter.setOutputIOParams(outputIOLists);
				mOutputAdapter.notifyDataSetChanged();
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(outputIOLists.get(0));
			break;
		}
		return false;
	}
}
