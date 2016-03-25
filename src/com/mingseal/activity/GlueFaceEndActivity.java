/**
 * 
 */
package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueFaceEndAdapter;
import com.mingseal.adapter.PointGlueFaceStartAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
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
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import static com.mingseal.data.param.PointConfigParam.GlueFaceEnd;

/**
 * @author 商炎炳
 * @description 面终点
 */
public class GlueFaceEndActivity extends Activity implements OnClickListener, Callback {

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
	private TextView tv_num;
	private TextView tv_lineNum;
	private TextView tv_stopGlue;
	private TextView tv_upHeight;
	private LinearLayout plan;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	private ListView listView;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_face_end);
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		glueFaceEndDao = new GlueFaceEndDao(GlueFaceEndActivity.this);
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		if (pointEndLists == null || pointEndLists.isEmpty()) {
			pointEnd = new PointGlueFaceEndParam();
			glueFaceEndDao.insertGlueFaceEnd(pointEnd);
			// 插入主键id
			pointEnd.set_id(param_id);
		}
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		// 初始化Handler,用来处理消息
		handler = new Handler(GlueFaceEndActivity.this);
		// mFaceEndAdapter = new
		// PointGlueFaceEndAdapter(GlueFaceEndActivity.this);
		// mFaceEndAdapter.setGlueStartLists(pointEndLists);
		// faceEndSpinner.setAdapter(mFaceEndAdapter);
		// 如果为1的话，需要设置值
		if (mType == 1) {
			PointGlueFaceEndParam glueFaceEndParam = glueFaceEndDao.getPointFaceEndParamByID(point.getPointParam().get_id());
			param_id = glueFaceEndDao
					.getFaceEndParamIDByParam(glueFaceEndParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(glueFaceEndParam);
			// faceEndSpinner.setSelection(point.getPointParam().get_id() - 1);
			// mFaceEndAdapter.notifyDataSetChanged();
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueFaceEndParam defaultParam = pointEndLists.get(0);
			param_id = glueFaceEndDao.getFaceEndParamIDByParam(defaultParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultParam);
		}
		// faceEndSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// PointGlueFaceEndParam point = mFaceEndAdapter.getItem(position);
		//
		// et_end_stopGlueTime.setText(point.getStopGlueTime() + "");
		// et_end_upHeight.setText(point.getUpHeight() + "");
		// et_end_lineNum.setText(point.getLineNum() + "");
		//
		// isPauseSwitch.setChecked(point.isPause());
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

		mFaceEndAdapter = new PointGlueFaceEndAdapter(GlueFaceEndActivity.this,
				handler);
		mFaceEndAdapter.setGlueStartLists(pointEndLists);
		listView.setAdapter(mFaceEndAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	protected void popupWindwShowing() {
		selectPopupWindow.showAsDropDown(plan, 0, -3);
	}

	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 更新整体ui界面
	 * @author wj
	 * @param glueFaceEndParam
	 */
	private void SetDateAndRefreshUI(PointGlueFaceEndParam glueFaceEndParam) {
		// 返回该方案在list中的位置
		tv_num.setText(String.valueOf(pointEndLists.indexOf(glueFaceEndParam) + 1)
				+ "");
		tv_lineNum.setText(glueFaceEndParam.getLineNum() + "");
		tv_stopGlue.setText(glueFaceEndParam.getStopGlueTime() + "");
		tv_upHeight.setText(glueFaceEndParam.getUpHeight()+"");
		UpdateInfos(glueFaceEndParam);
	}

	/**
	 * @Title UpdateInfos
	 * @Description 更新上半部分界面
	 * @author wj
	 * @param glueFaceEndParam
	 */
	private void UpdateInfos(PointGlueFaceEndParam glueFaceEndParam) {
		et_end_stopGlueTime.setText(glueFaceEndParam.getStopGlueTime() + "");
		et_end_upHeight.setText(glueFaceEndParam.getUpHeight() + "");
		et_end_lineNum.setText(glueFaceEndParam.getLineNum() + "");

		isPauseSwitch.setChecked(glueFaceEndParam.isPause());
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

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		/* =================== begin =================== */
		tv_num = (TextView) findViewById(R.id.item_num);
		tv_lineNum = (TextView) findViewById(R.id.item_end_lineNum);
		tv_stopGlue = (TextView) findViewById(R.id.item_end_stopGlueTime);
		tv_upHeight = (TextView)findViewById(R.id.item_end_upHeight);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		/* =================== end =================== */
		// 设置最大最小值
		et_end_stopGlueTime.addTextChangedListener(new MaxMinEditWatcher(
				GlueFaceEnd.StopGlueTimeMax, GlueFaceEnd.GlueFaceEndMin,
				et_end_stopGlueTime));
		et_end_stopGlueTime
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueFaceEnd.StopGlueTimeMax,
						GlueFaceEnd.GlueFaceEndMin, et_end_stopGlueTime));
		et_end_stopGlueTime.setSelectAllOnFocus(true);

		et_end_upHeight.addTextChangedListener(new MaxMinEditWatcher(
				GlueFaceEnd.UpHeightMax, GlueFaceEnd.GlueFaceEndMin,
				et_end_upHeight));
		et_end_upHeight.setOnFocusChangeListener(new MaxMinFocusChangeListener(
				GlueFaceEnd.UpHeightMax, GlueFaceEnd.GlueFaceEndMin,
				et_end_upHeight));
		et_end_upHeight.setSelectAllOnFocus(true);

		et_end_lineNum.addTextChangedListener(new MaxMinEditWatcher(
				GlueFaceEnd.LineNumMax, GlueFaceEnd.GlueFaceEndMin,
				et_end_lineNum));
		et_end_lineNum.setOnFocusChangeListener(new MaxMinFocusChangeListener(
				GlueFaceEnd.LineNumMax, GlueFaceEnd.GlueFaceEndMin,
				et_end_lineNum));
		et_end_lineNum.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(
				R.string.activity_glue_face_end));
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
			stopGlueTimeInt = Integer.parseInt(et_end_stopGlueTime.getText()
					.toString());
		} catch (NumberFormatException e) {
			stopGlueTimeInt = 0;
		}
		try {
			upHeightInt = Integer
					.parseInt(et_end_upHeight.getText().toString());
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
//		pointEnd.set_id(param_id);

		return pointEnd;
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
				pointEnd = getFaceEnd();
				if (pointEndLists.contains(pointEnd)) {
					ToastUtil.displayPromptInfo(this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowid = glueFaceEndDao.insertGlueFaceEnd(pointEnd);
					pointEnd.set_id((int)rowid);
					pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();

					mFaceEndAdapter.setGlueStartLists(pointEndLists);
					mFaceEndAdapter.notifyDataSetChanged();

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
				pointEnd = getFaceEnd();
				if (pointEndLists.contains(pointEnd)) {
					int id = pointEndLists.indexOf(pointEnd);
					// 如果方案里有的话,只需要设置一下id就行
					param_id = glueFaceEndDao.getFaceEndParamIDByParam(pointEnd);// 默认的参数序列主键。
					pointEnd.set_id(param_id);
				} else {
					long rowID = glueFaceEndDao.insertGlueFaceEnd(pointEnd);
					pointEnd.set_id((int) rowID);
				}

				point.setPointParam(pointEnd);

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
		case Const.POINTGLUEFACEEND_CLICK:
			pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = pointEndLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + pointEndLists.get(position).get_id());
			PointGlueFaceEndParam glueFaceEndParam = pointEndLists.get(position);
			SetDateAndRefreshUI(glueFaceEndParam);
			dismiss();
			// 更新界面
//			UpdateInfos(glueFaceStartParam);
			break;
		case Const.POINTGLUEFACEEND_TOP:
			// 置顶
			pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
				// 1为成功删除，0为未成功删除
				int result = glueFaceEndDao.deleteParam(pointGlueFaceEndParam);
				if (result == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueFaceEndParam topGluefaceFaceEndParam = pointEndLists.get(top_position);// 需要置顶的数据
			pointEndLists.remove(top_position);// 移除该数据
			pointEndLists.add(0, topGluefaceFaceEndParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = glueFaceEndDao.insertGlueFaceEnd(pointGlueFaceEndParam);
				// 重新分配主键id
				pointGlueFaceEndParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mFaceEndAdapter.setGlueStartLists(pointEndLists);
			mFaceEndAdapter.notifyDataSetChanged();
			break;
		case Const.POINTGLUEFACEEND_DEL:// 删除方案
			pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
//			System.out.println("删除的主键param_id：" + param_id);
//			System.out.println("删除的位置del_position：" + del_position);
//			System.out.println("删除之前的glueAloneLists的大小："
//					+ glueStartLists.size());
//			System.out.println("删除之前的方案主键:"
//					+ glueStartLists.get(del_position).get_id());

			// 删除到最后一个
			if (pointEndLists.size() == 1 && del_position == 0) {
				PointGlueFaceEndParam lastParam = new PointGlueFaceEndParam();
				glueFaceEndDao.deleteParam(pointEndLists.get(0));// 删除当前方案
				glueFaceEndDao.insertGlueFaceEnd(lastParam);// 默认方案
				lastParam.set_id(pointEndLists.get(0).get_id() + 1);// 设置主键
				pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
				mFaceEndAdapter.setGlueStartLists(pointEndLists);
				mFaceEndAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastParam);
			} else {
				glueFaceEndDao.deleteParam(pointEndLists.get(del_position));
				pointEndLists.remove(del_position);
				mFaceEndAdapter.setGlueStartLists(pointEndLists);
				mFaceEndAdapter.notifyDataSetChanged();
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(pointEndLists.get(0));
			break;
		}
		return false;
	}
}
