package com.mingseal.activity;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.adapter.PointGlueAloneAdapter;
import com.mingseal.communicate.Const;
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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import static com.mingseal.data.param.PointConfigParam.GlueAlone;

/**
 * @author 商炎炳
 * @description 点胶独立点
 */
public class GlueAloneActivity extends Activity implements OnClickListener,
		Callback {

	private final static String TAG = "GlueAloneActivity";
	private TextView tv_title;// 标题栏的标题

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

	private List<PointGlueAloneParam> glueAloneLists;// 保存的方案,用来维护从数据库中读出来的方案列表的编号
	private PointGlueAloneParam glueAlone;

	private PointGlueAloneAdapter mAloneAdapter;

	private Point point;// 从taskActivity中传值传过来的point
	private Intent intent;
	private boolean[] gluePortBoolean;
	private GlueAloneDao glueAloneDao;

	private int param_id = 1;//
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据
	private int dotGlueTime = 0;
	private int stopGlueTime = 0;
	private int upHeight = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	private Handler handler;
	private LinearLayout plan;
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	private ListView listView;
	private TextView item_num;
	private TextView item_alone_dotglue;
	private TextView item_alone_stopglue;
	private TextView item_alone_upheight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_alone);
		intent = getIntent();

		// point = (Point)
		// intent.getSerializableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		// point携带的参数方案[_id=1, pointType=POINT_GLUE_FACE_START]
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);

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
			// 插入主键id
			glueAlone.set_id(param_id);
		}

		glueAloneLists = glueAloneDao.findAllGlueAloneParams();

		// 初始化Handler,用来处理消息
		handler = new Handler(GlueAloneActivity.this);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		// 如果为1的话，需要设置值，准备跟新
		if (mType == 1) {
			System.out.println("传进来的主键point.getPointParam().get_id():"
					+ point.getPointParam().get_id());
			System.out.println("point.getPointParam():"
					+ glueAloneDao.getPointGlueAloneParamById(point
							.getPointParam().get_id()));
			System.out.println("point:" + point);
			PointGlueAloneParam GlueAloneParam = glueAloneDao
					.getPointGlueAloneParamById(point.getPointParam().get_id());
			param_id = glueAloneDao.getAloneParamIdByParam(GlueAloneParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(GlueAloneParam);
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueAloneParam defaultAloneParam = glueAloneLists.get(0);
			param_id = glueAloneDao.getAloneParamIdByParam(defaultAloneParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultAloneParam);
		}
		// 初始化
		gluePortBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		// mAloneAdapter = new PointGlueAloneAdapter(GlueAloneActivity.this);
		// mAloneAdapter.setGlueAloneLists(glueAloneLists);
		// taskSpinner.setAdapter(mAloneAdapter);
		// /*=================== begin ===================*/
		// CreateSwipMenu();
		// //关联
		// taskSpinner.setMenuCreator(mCreator);
		// /*=================== end ===================*/
		// 如果为1的话，需要设置值
		// if (mType == 1) {
		// PointGlueAloneParam GlueAloneParam = (PointGlueAloneParam) point
		// .getPointParam();
		// System.out.println(GlueAloneParam);
		// plan.setText(GlueAloneParam.get_id() + " " + "点胶延时：" + " "
		// + GlueAloneParam.getDotGlueTime() + "ms" + "停胶延时：" + " "
		// + GlueAloneParam.getStopGlueTime() + "ms" + "抬起高度：" + " "
		// + GlueAloneParam.getUpHeight() + " " + "mm");
		// // taskSpinner.setSelection(point.getPointParam().get_id() - 1);
		// // mAloneAdapter.notifyDataSetChanged();
		// }
		// taskSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// Log.d(TAG, "position: " + position + ",id: " + id);
		//
		// PointGlueAloneParam pointGlueAloneParam = mAloneAdapter
		// .getItem(position);
		// et_alone_dianjiao.setText(pointGlueAloneParam.getDotGlueTime()
		// + "");
		// et_alone_tingjiao.setText(pointGlueAloneParam.getStopGlueTime()
		// + "");
		// et_alone_upHeight.setText(pointGlueAloneParam.getUpHeight()
		// + "");
		// isOutGlueSwitch.setChecked(pointGlueAloneParam.isOutGlue());
		// isPause.setChecked(pointGlueAloneParam.isPause());
		//
		// isGluePort[0].setChecked(pointGlueAloneParam.getGluePort()[0]);
		// isGluePort[1].setChecked(pointGlueAloneParam.getGluePort()[1]);
		// isGluePort[2].setChecked(pointGlueAloneParam.getGluePort()[2]);
		// isGluePort[3].setChecked(pointGlueAloneParam.getGluePort()[3]);
		// isGluePort[4].setChecked(pointGlueAloneParam.getGluePort()[4]);
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
		// // 初始化Handler,用来处理消息
		// handler = new Handler(GlueAloneActivity.this);
		// // 初始化界面组件
		// parent = (LinearLayout) findViewById(R.id.parent);
		// plan = (LinearLayout) findViewById(R.id.tv_plan);
		// // 如果为1的话，需要设置值，准备跟新
		// if (mType == 1) {
		// System.out.println("point.getPointParam():"+point.getPointParam());
		// System.out.println("point:"+point);
		// System.out.println("(PointGlueAloneParam)point.getPointParam():"+(PointGlueAloneParam)point.getPointParam());
		// PointGlueAloneParam GlueAloneParam = (PointGlueAloneParam)
		// point.getPointParam();
		// SetDateAndRefreshUI(GlueAloneParam);
		// }else {
		// //不为1的话，需要选定默认的第一个方案
		// PointGlueAloneParam defaultAloneParam = glueAloneLists.get(0);
		// SetDateAndRefreshUI(defaultAloneParam);
		// }
		// 获取下拉框依附的组件宽度
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

	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 更新整体ui界面
	 * @author wj
	 * @param GlueAloneParam
	 */
	private void SetDateAndRefreshUI(PointGlueAloneParam GlueAloneParam) {
		// 返回该方案在list中的位置
		item_num.setText(String.valueOf(glueAloneLists.indexOf(GlueAloneParam) + 1)
				+ "");
		item_alone_dotglue.setText(GlueAloneParam.getDotGlueTime() + "");
		item_alone_stopglue.setText(GlueAloneParam.getStopGlueTime() + "");
		item_alone_upheight.setText(GlueAloneParam.getUpHeight() + "");
		// taskSpinner.setSelection(point.getPointParam().get_id() - 1);
		// mAloneAdapter.notifyDataSetChanged();
		UpdateInfos(GlueAloneParam);
	}

	private void initPopuWindow() {

		// glueAloneDao = new GlueAloneDao(GlueAloneActivity.this);
		// // initData();
		// glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		// if (glueAloneLists == null || glueAloneLists.isEmpty()) {
		// glueAlone = new PointGlueAloneParam();
		// glueAloneDao.insertGlueAlone(glueAlone);
		// }
		//
		// glueAloneLists = glueAloneDao.findAllGlueAloneParams();

		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		mAloneAdapter = new PointGlueAloneAdapter(GlueAloneActivity.this,
				handler);
		mAloneAdapter.setGlueAloneLists(glueAloneLists);
		listView.setAdapter(mAloneAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 显示PopupWindow窗口
	 */
	protected void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		selectPopupWindow.showAsDropDown(plan, 0, -3);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
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
		isPause = (Switch) findViewById(R.id.switch_tingjiao);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		/* =================== begin =================== */
		item_num = (TextView) findViewById(R.id.item_num);
		item_alone_dotglue = (TextView) findViewById(R.id.item_alone_dotglue);
		item_alone_stopglue = (TextView) findViewById(R.id.item_alone_stopglue);
		item_alone_upheight = (TextView) findViewById(R.id.item_alone_upheight);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		/* =================== end =================== */

		isGluePort = new Switch[GWOutPort.USER_O_NO_ALL.ordinal()];// 初始化20个点胶口
		isGluePort[0] = (Switch) findViewById(R.id.switch_dianjiaokou1);
		isGluePort[1] = (Switch) findViewById(R.id.switch_dianjiaokou2);
		isGluePort[2] = (Switch) findViewById(R.id.switch_dianjiaokou3);
		isGluePort[3] = (Switch) findViewById(R.id.switch_dianjiaokou4);
		isGluePort[4] = (Switch) findViewById(R.id.switch_dianjiaokou5);

		// 设置最大最小值
		et_alone_dianjiao.addTextChangedListener(new MaxMinEditWatcher(
				GlueAlone.DotGlueTimeMAX, GlueAlone.GlueAloneMIN,
				et_alone_dianjiao));
		et_alone_tingjiao.addTextChangedListener(new MaxMinEditWatcher(
				GlueAlone.StopGlueTimeMAX, GlueAlone.GlueAloneMIN,
				et_alone_tingjiao));
		et_alone_upHeight.addTextChangedListener(new MaxMinEditWatcher(
				GlueAlone.UpHeightMAX, GlueAlone.GlueAloneMIN,
				et_alone_upHeight));

		et_alone_dianjiao
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueAlone.DotGlueTimeMAX, GlueAlone.GlueAloneMIN,
						et_alone_dianjiao));
		et_alone_tingjiao
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueAlone.StopGlueTimeMAX, GlueAlone.GlueAloneMIN,
						et_alone_tingjiao));
		et_alone_upHeight
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueAlone.UpHeightMAX, GlueAlone.GlueAloneMIN,
						et_alone_upHeight));

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
			dotGlueTime = Integer.parseInt(et_alone_dianjiao.getText()
					.toString());
		} catch (NumberFormatException e) {
			dotGlueTime = 0;
		}
		try {
			stopGlueTime = Integer.parseInt(et_alone_tingjiao.getText()
					.toString());
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

		// glueAlone.set_id(param_id);先不设id，等检查是否存在之后，获取数据库的id再赋值

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
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowID = glueAloneDao.insertGlueAlone(glueAlone);
					glueAlone.set_id((int) rowID);
					// glueAloneLists.add(glueAlone);//加到方案里面去

					glueAloneLists = glueAloneDao.findAllGlueAloneParams();

					mAloneAdapter.setGlueAloneLists(glueAloneLists);
					mAloneAdapter.notifyDataSetChanged();

					Log.i(TAG, "保存之后新方案-->" + glueAloneLists.toString());
					ToastUtil.displayPromptInfo(GlueAloneActivity.this,
							getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this,
						getResources().getString(R.string.data_is_null));
			}

			break;
		case R.id.rl_complete:// 完成按钮响应事件
			isNull = isEditNull();
			if (isNull) {
				glueAlone = getGlueAlone();
				if (glueAloneLists.contains(glueAlone)) {
					int id = glueAloneLists.indexOf(glueAlone);
					// glueAlone.set_id(glueAloneLists.get(id).get_id());
					// ToastUtil.displayPromptInfo(GlueAloneActivity.this,
					// getResources()
					// .getString(R.string.task_is_exist_yes));
//					System.out.println("完成按钮响应已经存在" + id);
					param_id = glueAloneDao.getAloneParamIdByParam(glueAlone);// 默认的参数序列主键。
					glueAlone.set_id(param_id);
				} else {
					// 方案中不存在的话就保存
					long rowID = glueAloneDao.insertGlueAlone(glueAlone);
					glueAlone.set_id((int) rowID);
				}
				point.setPointParam(glueAlone);
//				System.out.println("完成按钮响应point.setPointParam(glueAlone)"
//						+ glueAlone);

//				Log.i(TAG, point.toString());
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
		case R.id.rl_back:// 返回按钮的响应事件
			finish();
			overridePendingTransition(R.anim.in_from_left,
					R.anim.out_from_right);

			break;

		default:
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		Bundle data = msg.getData();
		switch (msg.what) {
		case Const.POINTGLUEALONE_CLICK:
			glueAloneLists = glueAloneDao.findAllGlueAloneParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = glueAloneLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + glueAloneLists.get(position).get_id());
			PointGlueAloneParam glueAlone = glueAloneLists.get(position);
//			item_num.setText(String.valueOf(glueAloneLists.indexOf(glueAlone) + 1)
//					+ "");
//			item_alone_dotglue.setText(glueAlone.getDotGlueTime() + "");
//			item_alone_stopglue.setText(glueAlone.getStopGlueTime() + "");
//			item_alone_upheight.setText(glueAlone.getUpHeight() + "");
			SetDateAndRefreshUI(glueAlone);
			dismiss();
//			// 更新界面
//			UpdateInfos(glueAlone);
			break;
		case Const.POINTGLUEALONE_TOP:
			// 置顶
			glueAloneLists = glueAloneDao.findAllGlueAloneParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
				// 1为成功删除，0为未成功删除
				int deleteGlueAlone = glueAloneDao
						.deleteGlueAlone(pointGlueAloneParam);
				if (deleteGlueAlone == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueAloneParam topGlueAloneParam = glueAloneLists
					.get(top_position);// 需要置顶的数据
			glueAloneLists.remove(top_position);// 移除该数据
			glueAloneLists.add(0, topGlueAloneParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = glueAloneDao.insertGlueAlone(pointGlueAloneParam);
				// 重新分配主键id
				pointGlueAloneParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mAloneAdapter.setGlueAloneLists(glueAloneLists);
			mAloneAdapter.notifyDataSetChanged();
			// param_id = position + 1;
			// PointGlueAloneParam glueAlone = glueAloneLists.get(position);
			// item_num.setText(glueAlone.get_id() + "");
			// item_alone_dotglue.setText(glueAlone.getDotGlueTime() + "");
			// item_alone_stopglue.setText(glueAlone.getStopGlueTime() + "");
			// item_alone_upheight.setText(glueAlone.getUpHeight() + "");
			// // 更新界面
			// UpdateInfos(glueAlone);
			break;
		case Const.POINTGLUEALONE_DEL:// 删除方案
			glueAloneLists = glueAloneDao.findAllGlueAloneParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
			System.out.println("删除的主键param_id：" + param_id);
			System.out.println("删除的位置del_position：" + del_position);
			System.out.println("删除之前的glueAloneLists的大小："
					+ glueAloneLists.size());
			System.out.println("删除之前的方案主键:"
					+ glueAloneLists.get(del_position).get_id());

			// 删除到最后一个
			if (glueAloneLists.size() == 1 && del_position == 0) {
				PointGlueAloneParam lastglueAlone = new PointGlueAloneParam();
				glueAloneDao.deleteGlueAlone(glueAloneLists.get(0));// 删除当前方案
				glueAloneDao.insertGlueAlone(lastglueAlone);// 默认方案
				lastglueAlone.set_id(glueAloneLists.get(0).get_id() + 1);// 设置主键
				glueAloneLists = glueAloneDao.findAllGlueAloneParams();
				mAloneAdapter.setGlueAloneLists(glueAloneLists);
				mAloneAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastglueAlone);
			} else {
				glueAloneDao.deleteGlueAlone(glueAloneLists.get(del_position));
				glueAloneLists.remove(del_position);
				mAloneAdapter.setGlueAloneLists(glueAloneLists);
				mAloneAdapter.notifyDataSetChanged();
				// //清除数据库里的数据然后再重新插入。
				// for (PointGlueAloneParam pointGlueAloneParam :
				// glueAloneLists) {
				// //1为成功删除，0为未成功删除
				// int deleteGlueAlone =
				// glueAloneDao.deleteGlueAlone(pointGlueAloneParam);
				// if (deleteGlueAlone==0) {
				// //未成功
				// System.out.println("未成功！");
				// }else {
				// System.out.println("成功！");
				// }
				// }
				// // glueAloneDao.delsqlite_sequence();
				// for (PointGlueAloneParam pointGlueAloneParam :
				// glueAloneLists) {
				// glueAloneDao.insertGlueAlone(pointGlueAloneParam);
				// System.out.println("插入成功！");
				// }
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(glueAloneLists.get(0));
			// mAloneAdapter.setGlueAloneLists(glueAloneLists);
			break;
		}
		return false;
	}

	/**
	 * 更新上半部分界面
	 * 
	 * @param glueAlone
	 */
	private void UpdateInfos(PointGlueAloneParam glueAlone) {
		et_alone_dianjiao.setText(glueAlone.getDotGlueTime() + "");
		et_alone_tingjiao.setText(glueAlone.getStopGlueTime() + "");
		et_alone_upHeight.setText(glueAlone.getUpHeight() + "");
		isOutGlueSwitch.setChecked(glueAlone.isOutGlue());
		isPause.setChecked(glueAlone.isPause());

		isGluePort[0].setChecked(glueAlone.getGluePort()[0]);
		isGluePort[1].setChecked(glueAlone.getGluePort()[1]);
		isGluePort[2].setChecked(glueAlone.getGluePort()[2]);
		isGluePort[3].setChecked(glueAlone.getGluePort()[3]);
		isGluePort[4].setChecked(glueAlone.getGluePort()[4]);
	}
}
