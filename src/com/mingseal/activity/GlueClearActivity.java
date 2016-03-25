package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueClearAdapter;
import com.mingseal.adapter.PointGlueFaceStartAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueClearDao;
import com.mingseal.data.param.PointConfigParam.GlueClear;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author 商炎炳
 * 
 */
public class GlueClearActivity extends Activity implements OnClickListener,
		Callback {

	/**
	 * 标题栏
	 */
	private TextView tv_title;
	/**
	 * @Fields et_clear_clearGlue: 清胶延时
	 */
	private EditText et_clear_clearGlue;
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
	 * 清胶点Spinner
	 */
	private Spinner clearSpinner;
	private Intent intent;
	private Point point;// 从taskActivity中传值传过来的point
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据

	/**
	 * 将方案中的id保存下来
	 */
	private int param_id = 1;

	private GlueClearDao glueClearDao;
	private List<PointGlueClearParam> pointClearLists = null;
	private PointGlueClearParam pointClear = null;

	private PointGlueClearAdapter mClearAdapter;
	/**
	 * @Fields clearGlueint: 清胶延时取得值
	 */
	private int clearGlueint = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private Handler handler;
	private TextView tv_num;
	private TextView tv_clearTime;
	private LinearLayout plan;
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
		setContentView(R.layout.activity_glue_clear);

		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		glueClearDao = new GlueClearDao(GlueClearActivity.this);
		pointClearLists = glueClearDao.findAllGlueClearParams();
		if (pointClearLists == null || pointClearLists.isEmpty()) {
			pointClear = new PointGlueClearParam();
			glueClearDao.insertGlueClear(pointClear);
			// 重新获取一下数据
			// 插入主键id
			pointClear.set_id(param_id);
		}
		pointClearLists = glueClearDao.findAllGlueClearParams();
		// 初始化Handler,用来处理消息
		handler = new Handler(GlueClearActivity.this);
		if (mType == 1) {
			PointGlueClearParam glueClearParam = glueClearDao
					.getPointGlueClearParamByID(point.getPointParam().get_id());
			param_id = glueClearDao.getGlueClearParamIDByParam(glueClearParam);// 传过来的方案的参数序列主键。
			SetDateAndRefreshUI(glueClearParam);
		} else {
			// 不为1的话，需要选定默认的第一个方案
			PointGlueClearParam defaultParam = pointClearLists.get(0);
			param_id = glueClearDao.getGlueClearParamIDByParam(defaultParam);// 默认的参数序列主键。
			SetDateAndRefreshUI(defaultParam);
		}
		// mClearAdapter = new PointGlueClearAdapter(GlueClearActivity.this);
		// mClearAdapter.setGlueClearLists(pointClearLists);
		// clearSpinner.setAdapter(mClearAdapter);
		// // 如果为1的话，需要设置值
		// if (mType == 1) {
		// clearSpinner.setSelection(point.getPointParam().get_id() - 1);
		// mClearAdapter.notifyDataSetChanged();
		// }
		// clearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// PointGlueClearParam point = mClearAdapter.getItem(position);
		//
		// et_clear_clearGlue.setText(point.getClearGlueTime() + "");
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

	private void SetDateAndRefreshUI(PointGlueClearParam glueClearParam) {
		tv_num.setText(String.valueOf(pointClearLists.indexOf(glueClearParam) + 1) + "");
		tv_clearTime.setText(glueClearParam.getClearGlueTime() + "");
		UpdateInfos(glueClearParam);
	}

	private void UpdateInfos(PointGlueClearParam glueClearParam) {
		et_clear_clearGlue.setText(glueClearParam.getClearGlueTime() + "");
	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_clear_clearGlue = (EditText) findViewById(R.id.et_clear_clearGlue);
		/* =================== begin =================== */
		tv_num = (TextView) findViewById(R.id.item_num);
		tv_clearTime = (TextView) findViewById(R.id.item_clear);
		// 初始化界面组件
		plan = (LinearLayout) findViewById(R.id.tv_plan);
		/* =================== end =================== */
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置清胶延时的最大最小值
		et_clear_clearGlue.addTextChangedListener(new MaxMinEditWatcher(
				GlueClear.ClearGlueTimeMax, GlueClear.GlueClearMin,
				et_clear_clearGlue));
		et_clear_clearGlue
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(
						GlueClear.ClearGlueTimeMax, GlueClear.GlueClearMin,
						et_clear_clearGlue));
		et_clear_clearGlue.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(
				R.string.activity_glue_cleario));
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
		if ("".equals(et_clear_clearGlue.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到PointGlueClearParam
	 * 
	 * @return PointGlueClearParam
	 */
	private PointGlueClearParam getClear() {
		pointClear = new PointGlueClearParam();
		try {
			clearGlueint = Integer.parseInt(et_clear_clearGlue.getText()
					.toString());
		} catch (NumberFormatException e) {
			clearGlueint = 0;
		}
		pointClear.setClearGlueTime(clearGlueint);
//		pointClear.set_id(param_id);

		return pointClear;
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

		mClearAdapter = new PointGlueClearAdapter(GlueClearActivity.this,
				handler);
		mClearAdapter.setGlueClearLists(pointClearLists);
		listView.setAdapter(mClearAdapter);

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
				pointClear = getClear();
				if (pointClearLists.contains(pointClear)) {
					ToastUtil.displayPromptInfo(this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				} else {
					long rowid = glueClearDao.insertGlueClear(pointClear);
					pointClear.set_id((int)rowid);

					pointClearLists = glueClearDao.findAllGlueClearParams();

					mClearAdapter.setGlueClearLists(pointClearLists);
					mClearAdapter.notifyDataSetChanged();

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
				pointClear = getClear();
				if (pointClearLists.contains(pointClear)) {
					int id = pointClearLists.indexOf(pointClear);
					// 如果方案里有的话,只需要设置一下id就行
					param_id = glueClearDao.getGlueClearParamIDByParam(pointClear);// 默认的参数序列主键。
					pointClear.set_id(param_id);
				} else {
					long rowID = glueClearDao.insertGlueClear(pointClear);
					pointClear.set_id((int) rowID);
				}

				point.setPointParam(pointClear);

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
		case Const.POINTGLUECLEAR_CLICK:
			pointClearLists = glueClearDao.findAllGlueClearParams();
			// 选中下拉项，下拉框消失
			int position = data.getInt("selIndex");
			param_id = pointClearLists.get(position).get_id();// 参数序列id等于主键
			System.out.println("点击的position:" + position);
			System.out
					.println("点击的主键:" + pointClearLists.get(position).get_id());
			PointGlueClearParam glueClearParam = pointClearLists.get(position);
			SetDateAndRefreshUI(glueClearParam);
			dismiss();
			// 更新界面
//			UpdateInfos(glueFaceStartParam);
			break;
		case Const.POINTGLUECLEAR_TOP:
			// 置顶
			pointClearLists = glueClearDao.findAllGlueClearParams();
			int top_position = data.getInt("top_Index");
			// 清空数据库，准备重新排序
			for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
				// 1为成功删除，0为未成功删除
				int result = glueClearDao.deleteParam(pointGlueClearParam);
				if (result == 0) {
					// 未成功
					System.out.println("删除未成功！");
				} else {
					System.out.println("删除成功！");
				}
			}
			// 重新排序
			PointGlueClearParam topParam = pointClearLists
					.get(top_position);// 需要置顶的数据
			pointClearLists.remove(top_position);// 移除该数据
			pointClearLists.add(0, topParam);// 置顶
			// 将重新排序的list插入数据库
			for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
				// 因为重新排序了，所以要更改参数方案的参数序列。
				long rowID = glueClearDao.insertGlueClear(pointGlueClearParam);
				// 重新分配主键id
				pointGlueClearParam.set_id((int) rowID);
				System.out.println("插入成功！");
			}
			// 刷新ui
			mClearAdapter.setGlueClearLists(pointClearLists);
			mClearAdapter.notifyDataSetChanged();
			break;
		case Const.POINTGLUECLEAR_DEL:// 删除方案
			pointClearLists = glueClearDao.findAllGlueClearParams();
			// 选中下拉项，下拉框消失
			int del_position = data.getInt("del_Index");
//			System.out.println("删除的主键param_id：" + param_id);
//			System.out.println("删除的位置del_position：" + del_position);
//			System.out.println("删除之前的glueAloneLists的大小："
//					+ glueStartLists.size());
//			System.out.println("删除之前的方案主键:"
//					+ glueStartLists.get(del_position).get_id());

			// 删除到最后一个
			if (pointClearLists.size() == 1 && del_position == 0) {
				PointGlueClearParam lastParam = new PointGlueClearParam();
				glueClearDao.deleteParam(pointClearLists.get(0));// 删除当前方案
				glueClearDao.insertGlueClear(lastParam);// 默认方案
				lastParam.set_id(pointClearLists.get(0).get_id() + 1);// 设置主键
				pointClearLists = glueClearDao.findAllGlueClearParams();
				mClearAdapter.setGlueClearLists(pointClearLists);
				mClearAdapter.notifyDataSetChanged();
				SetDateAndRefreshUI(lastParam);
			} else {
				glueClearDao.deleteParam(pointClearLists.get(del_position));
				pointClearLists.remove(del_position);
				mClearAdapter.setGlueClearLists(pointClearLists);
				mClearAdapter.notifyDataSetChanged();
			}
			// 删除后上半部分默认选中第一条方案
			UpdateInfos(pointClearLists.get(0));
			break;
		}
		return false;
	}
}
