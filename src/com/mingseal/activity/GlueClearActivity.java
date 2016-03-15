package com.mingseal.activity;

import java.util.List;

import com.mingseal.adapter.PointGlueClearAdapter;
import com.mingseal.data.dao.GlueClearDao;
import com.mingseal.data.param.PointConfigParam.GlueClear;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author 商炎炳
 *
 */
public class GlueClearActivity extends Activity implements OnClickListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_clear);

		intent = getIntent();
		point = intent.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);

		initPicker();

		glueClearDao = new GlueClearDao(GlueClearActivity.this);
		pointClearLists = glueClearDao.findAllGlueClearParams();
		if (pointClearLists == null || pointClearLists.isEmpty()) {
			pointClear = new PointGlueClearParam();
			glueClearDao.insertGlueClear(pointClear);
			// 重新获取一下数据
			pointClearLists = glueClearDao.findAllGlueClearParams();
		}

		mClearAdapter = new PointGlueClearAdapter(GlueClearActivity.this);
		mClearAdapter.setGlueClearLists(pointClearLists);
		clearSpinner.setAdapter(mClearAdapter);
		// 如果为1的话，需要设置值
		if (mType == 1) {
			clearSpinner.setSelection(point.getPointParam().get_id() - 1);
			mClearAdapter.notifyDataSetChanged();
		}
		clearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				PointGlueClearParam point = mClearAdapter.getItem(position);

				et_clear_clearGlue.setText(point.getClearGlueTime() + "");

				param_id = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_clear_clearGlue = (EditText) findViewById(R.id.et_clear_clearGlue);
		clearSpinner = (Spinner) findViewById(R.id.spinner_clear);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		// 设置清胶延时的最大最小值
		et_clear_clearGlue.addTextChangedListener(
				new MaxMinEditWatcher(GlueClear.ClearGlueTimeMax, GlueClear.GlueClearMin, et_clear_clearGlue));
		et_clear_clearGlue.setOnFocusChangeListener(
				new MaxMinFocusChangeListener(GlueClear.ClearGlueTimeMax, GlueClear.GlueClearMin, et_clear_clearGlue));
		et_clear_clearGlue.setSelectAllOnFocus(true);

		tv_title.setText(getResources().getString(R.string.activity_glue_cleario));
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
			clearGlueint = Integer.parseInt(et_clear_clearGlue.getText().toString());
		} catch (NumberFormatException e) {
			clearGlueint = 0;
		}
		pointClear.setClearGlueTime(clearGlueint);
		pointClear.set_id(param_id);

		return pointClear;
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
				pointClear = getClear();
				if (pointClearLists.contains(pointClear)) {
					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.task_is_exist_yes));
				} else {
					glueClearDao.insertGlueClear(pointClear);

					pointClearLists = glueClearDao.findAllGlueClearParams();

					mClearAdapter.setGlueClearLists(pointClearLists);
					mClearAdapter.notifyDataSetChanged();

					ToastUtil.displayPromptInfo(this, getResources().getString(R.string.save_success));
				}
			} else {
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_is_null));
			}
			break;
		case R.id.rl_complete:// 完成按钮的响应事件
			isNull = isEditNull();
			if (isNull) {
				pointClear = getClear();
				if (pointClearLists.contains(pointClear)) {
					int id = pointClearLists.indexOf(pointClear);
					pointClear.set_id(pointClearLists.get(id).get_id());
				} else {
					long rowID = glueClearDao.insertGlueClear(pointClear);
					pointClear.set_id((int) rowID);
				}

				point.setPointParam(pointClear);

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
