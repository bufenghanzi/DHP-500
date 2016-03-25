/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueLineMidAdapter;
import com.mingseal.adapter.PointGlueLineStartAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.listener.TextEditWatcher;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import static com.mingseal.data.param.PointConfigParam.GlueLineMid;

/**
 * @author 商炎炳
 * 
 */
public class GlueLineMidActivity extends Activity implements OnClickListener,
		Callback {

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
	private int param_id = 1;// / 选取的是几号方案
	/**
	 * @Fields moveSpeedInt: 轨迹速度的int值
	 */
	private int moveSpeedInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private TextView tv_num;
	private TextView tv_radius;
	private TextView tv_stopPrev;
	private TextView tv_stopNext;
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
		setContentView(R.layout.activity_glue_line_mid);

		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
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
			// 插入主键id
			glueMid.set_id(param_id);
		}
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		Log.d(TAG, glueMidLists.toString());
		// 初始化Handler,用来处理消息
		handler = new Handler(GlueLineMidActivity.this);
		if (mType == 1) {
			PointGlueLineMidParam glueLineMidParam = glueMidDao
					.getPointGlueLineMidParam(point.getPointParam().get_id());
			param_id = glueMidDao.getLineMidParamIDByParam(glueLineMidParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(glueLineMidParam);
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueLineMidParam defaultParam = glueMidLists.get(0);
			param_id = glueMidDao.getLineMidParamIDByParam(defaultParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultParam);
		}
		// mMidAdapter = new PointGlueLineMidAdapter(GlueLineMidActivity.this);
		// mMidAdapter.setGlueMidLists(glueMidLists);
		// lineMidSpinner.setAdapter(mMidAdapter);
		//
		// // 如果为1的话，需要设置值
		// if (mType == 1) {
		// lineMidSpinner.setSelection(point.getPointParam().get_id() - 1);
		// mMidAdapter.notifyDataSetChanged();
		// }
		// 初始化数组
		glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		//
		// lineMidSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// PointGlueLineMidParam point = mMidAdapter.getItem(position);
		// et_mid_moveSpeed.setText(point.getMoveSpeed() + "");
		// radiusEdit.setText(point.getRadius() + "");
		// stopDisPrevEdit.setText(point.getStopGlueDisPrev() + "");
		// stopDisNextEdit.setText(point.getStopGLueDisNext() + "");
		// isOutGlueSwitch.setChecked(point.isOutGlue());
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

	private void SetDateAndRefreshUI(PointGlueLineMidParam glueLineMidParam) {
		tv_num.setText(String.valueOf(glueMidLists.indexOf(glueLineMidParam) + 1)
				+ "");
		tv_radius.setText(glueLineMidParam.getRadius() + "");
		tv_stopPrev.setText(glueLineMidParam.getStopGlueDisPrev() + "");
		tv_stopNext.setText(glueLineMidParam.getStopGLueDisNext() + "");
		UpdateInfos(glueLineMidParam);
	}

	private void UpdateInfos(PointGlueLineMidParam glueLineMidParam) {
		et_mid_moveSpeed.setText(glueLineMidParam.getMoveSpeed() + "");
		radiusEdit.setText(glueLineMidParam.getRadius() + "");
		stopDisPrevEdit.setText(glueLineMidParam.getStopGlueDisPrev() + "");
		stopDisNextEdit.setText(glueLineMidParam.getStopGLueDisNext() + "");
		isOutGlueSwitch.setChecked(glueLineMidParam.isOutGlue());

		isGluePort[0].setChecked(glueLineMidParam.getGluePort()[0]);
		isGluePort[1].setChecked(glueLineMidParam.getGluePort()[1]);
		isGluePort[2].setChecked(glueLineMidParam.getGluePort()[2]);
		isGluePort[3].setChecked(glueLineMidParam.getGluePort()[3]);
		isGluePort[4].setChecked(glueLineMidParam.getGluePort()[4]);
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

	protected void popupWindwShowing() {
		selectPopupWindow.showAsDropDown(plan, 0, -3);
	}

	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		mMidAdapter = new PointGlueLineMidAdapter(GlueLineMidActivity.this, handler);
		mMidAdapter.setGlueMidLists(glueMidLists);
		listView.setAdapter(mMidAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
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

		/* =================== begin =================== */
		tv_num = (TextView) findViewById(R.id.item_num);
		tv_radius = (TextView) findViewById(R.id.item_mid_radius);
		tv_stopPrev = (TextView) findViewById(R.id.item_mid_stopDisPrev);
		tv_stopNext = (TextView) findViewById(R.id.item_mid_stopDisNext);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		/* =================== end =================== */

		isGluePort = new Switch[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (Switch) findViewById(R.id.switch_glueport1);
		isGluePort[1] = (Switch) findViewById(R.id.switch_glueport2);
		isGluePort[2] = (Switch) findViewById(R.id.switch_glueport3);
		isGluePort[3] = (Switch) findViewById(R.id.switch_glueport4);
		isGluePort[4] = (Switch) findViewById(R.id.switch_glueport5);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 轨迹速度设置最大最小值
		et_mid_moveSpeed.addTextChangedListener(new MaxMinEditWatcher(
				GlueLineMid.MoveSpeedMax, GlueLineMid.GlueLineMidMin,
				et_mid_moveSpeed));
		et_mid_moveSpeed
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueLineMid.MoveSpeedMax, GlueLineMid.GlueLineMidMin,
						et_mid_moveSpeed));
		et_mid_moveSpeed.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(
				R.string.activity_glue_line_mid));
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
			moveSpeedInt = Integer.parseInt(et_mid_moveSpeed.getText()
					.toString());
			if (moveSpeedInt == 0) {
				moveSpeedInt = 1;
			}
		} catch (NumberFormatException e) {
			moveSpeedInt = 1;
		}

		glueMid.setMoveSpeed(moveSpeedInt);
		glueMid.setRadius(Float.parseFloat(radiusEdit.getText().toString()));
		glueMid.setStopGlueDisPrev(Float.parseFloat(stopDisPrevEdit.getText()
				.toString()));
		glueMid.setStopGLueDisNext(Float.parseFloat(stopDisNextEdit.getText()
				.toString()));
		glueMid.setOutGlue(isOutGlueSwitch.isChecked());

		glueBoolean[0] = isGluePort[0].isChecked();
		glueBoolean[1] = isGluePort[1].isChecked();
		glueBoolean[2] = isGluePort[2].isChecked();
		glueBoolean[3] = isGluePort[3].isChecked();
		glueBoolean[4] = isGluePort[4].isChecked();
		glueMid.setGluePort(glueBoolean);
		// glueMid.set_id(param_id);

		return glueMid;
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
				glueMid = getLineMid();
				if (glueMidLists.contains(glueMid)) {
					ToastUtil.displayPromptInfo(this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowid = glueMidDao.insertGlueLineMid(glueMid);
					glueMid.set_id((int)rowid);

					glueMidLists = glueMidDao.findAllGlueLineMidParams();

					mMidAdapter.setGlueMidLists(glueMidLists);
					mMidAdapter.notifyDataSetChanged();

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
				glueMid = getLineMid();
				if (!glueMidLists.contains(glueMid)) {
					long rowID = glueMidDao.insertGlueLineMid(glueMid);
					glueMid.set_id((int) rowID);
				} else {
					int id = glueMidLists.indexOf(glueMid);
					// 如果方案里有的话,只需要设置一下id就行
					param_id = glueMidDao.getLineMidParamIDByParam(glueMid);// 默认的参数序列主键。
					glueMid.set_id(param_id);
				}

				point.setPointParam(glueMid);

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
		case Const.POINTGLUELINEMID_CLICK:
			glueMidLists = glueMidDao.findAllGlueLineMidParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = glueMidLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + glueMidLists.get(position).get_id());
			PointGlueLineMidParam glueLineMidParam = glueMidLists.get(position);
			SetDateAndRefreshUI(glueLineMidParam);
			dismiss();
			// 更新界面
//			UpdateInfos(glueFaceStartParam);
			break;
		case Const.POINTGLUELINEMID_TOP:
			// 置顶
			glueMidLists = glueMidDao.findAllGlueLineMidParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
				// 1为成功删除，0为未成功删除
				int result = glueMidDao.deleteParam(pointGlueLineMidParam);
				if (result == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueLineMidParam topgluelLineMidParam = glueMidLists.get(top_position);// 需要置顶的数据
			glueMidLists.remove(top_position);// 移除该数据
			glueMidLists.add(0, topgluelLineMidParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = glueMidDao.insertGlueLineMid(pointGlueLineMidParam);
				// 重新分配主键id
				pointGlueLineMidParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mMidAdapter.setGlueMidLists(glueMidLists);
			mMidAdapter.notifyDataSetChanged();
			break;
		case Const.POINTGLUELINEMID_DEL:// 删除方案
			glueMidLists = glueMidDao.findAllGlueLineMidParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
//			System.out.println("删除的主键param_id：" + param_id);
//			System.out.println("删除的位置del_position：" + del_position);
//			System.out.println("删除之前的glueAloneLists的大小："
//					+ glueStartLists.size());
//			System.out.println("删除之前的方案主键:"
//					+ glueStartLists.get(del_position).get_id());

			// 删除到最后一个
			if (glueMidLists.size() == 1 && del_position == 0) {
				PointGlueLineMidParam lastParam = new PointGlueLineMidParam();
				glueMidDao.deleteParam(glueMidLists.get(0));// 删除当前方案
				glueMidDao.insertGlueLineMid(lastParam);// 默认方案
				lastParam.set_id(glueMidLists.get(0).get_id() + 1);// 设置主键
				glueMidLists = glueMidDao.findAllGlueLineMidParams();
				mMidAdapter.setGlueMidLists(glueMidLists);
				mMidAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastParam);
			} else {
				glueMidDao.deleteParam(glueMidLists.get(del_position));
				glueMidLists.remove(del_position);
				mMidAdapter.setGlueMidLists(glueMidLists);
				mMidAdapter.notifyDataSetChanged();
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(glueMidLists.get(0));
			break;
		}
		return false;
	}
}
