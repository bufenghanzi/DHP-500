package com.mingseal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueClearDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.PointConfigParam.GlueClear;
import com.mingseal.data.param.PointConfigParam.GlueFaceStart;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.ui.PopupListView;
import com.mingseal.ui.PopupView;
import com.mingseal.ui.PopupListView.OnClickPositionChanged;
import com.mingseal.ui.PopupListView.OnZoomInChanged;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * @author 商炎炳
 * 
 */
public class GlueClearActivity extends Activity implements OnClickListener{
	private static final String TAG = "GlueClearActivity";
	/**
	 * 标题栏
	 */
	private TextView tv_title;
	/**
	 * 返回上级菜单
	 */
	private RelativeLayout rl_back;

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

	/**
	 * @Fields clearGlueint: 清胶延时取得值
	 */
	private int clearGlueint = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private Handler handler;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	/* =================== begin =================== */
	private HashMap<Integer, PointGlueClearParam> update_id;// 修改的方案号集合
	private int defaultNum = 1;// 默认号
	ArrayList<PopupView> popupViews;
	private TextView mMorenTextView;
	PopupListView popupListView;
	int p = 0;
	View extendView;

	private boolean isOk;
	private boolean isExist = false;// 是否存在
	private boolean firstExist = false;// 是否存在
	/**
	 * 当前任务号
	 */
	private int currentTaskNum;
	private int currentClickNum;// 当前点击的序号
	private int mIndex;// 对应方案号
	private EditText et_clear_clearGlue;
	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private RelativeLayout rl_save;
	private ImageView iv_moren;

	/* =================== end =================== */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_clear);
		update_id = new HashMap<>();
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueClearActivity.this,
				SettingParam.DefaultNum.ParamGlueClearNumber);

		glueClearDao = new GlueClearDao(GlueClearActivity.this);
		pointClearLists = glueClearDao.findAllGlueClearParams();
		if (pointClearLists == null || pointClearLists.isEmpty()) {
			pointClear = new PointGlueClearParam();
			pointClear.set_id(param_id);
			glueClearDao.insertGlueClear(pointClear);
			// 插入主键id
		}
		pointClearLists = glueClearDao.findAllGlueClearParams();
		popupViews = new ArrayList<>();
		initPicker();

	}

	/**
	 * @Title UpdateInfos
	 * @Description 更新上半部分界面
	 * @author wj
	 * @param glueFaceStartParam
	 */
	private void UpdateInfos(PointGlueClearParam glueClearParam) {
		if (glueClearParam == null) {
			et_clear_clearGlue.setText("");
		} else {
			et_clear_clearGlue.setText(glueClearParam.getClearGlueTime() + "");
		}
	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(
				R.string.activity_glue_cleario));
		mMorenTextView = (TextView) findViewById(R.id.morenfangan);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mMorenTextView.setText("当前默认方案号(" + defaultNum + ")");
		// 初始化popuplistview区域
		popupListView = (PopupListView) findViewById(R.id.popupListView);
		popupListView.init(null);

		// 初始化创建10个popupView
		for (int i = 0; i < 10; i++) {
			p = i + 1;
			PopupView popupView = new PopupView(this, R.layout.popup_view_item) {

				@Override
				public void setViewsElements(View view) {
					TextView textView = (TextView) view
							.findViewById(R.id.title);
					pointClearLists = glueClearDao.findAllGlueClearParams();
					textView.setTextSize(30);
					ImageView title_num = (ImageView) view
							.findViewById(R.id.title_num);
					if (p == 1) {// 方案列表第一位对应一号方案
						title_num.setImageResource(R.drawable.green1);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 2) {
						title_num.setImageResource(R.drawable.green2);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 3) {
						title_num.setImageResource(R.drawable.green3);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 4) {
						title_num.setImageResource(R.drawable.green4);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 5) {
						title_num.setImageResource(R.drawable.green5);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 6) {
						title_num.setImageResource(R.drawable.green6);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 7) {
						title_num.setImageResource(R.drawable.green7);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 8) {
						title_num.setImageResource(R.drawable.green8);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 9) {
						title_num.setImageResource(R.drawable.green9);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					} else if (p == 10) {
						title_num.setImageResource(R.drawable.green10);
						for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
							if (p == pointGlueClearParam.get_id()) {
								textView.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
							}
						}
					}
				}

				@Override
				public View setExtendView(View view) {
					if (view == null) {
						extendView = LayoutInflater.from(
								getApplicationContext()).inflate(
								R.layout.glue_clear_extend_view, null);
						int size = pointClearLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
									if (p == pointGlueClearParam.get_id()) {
										UpdateInfos(pointGlueClearParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
									if (p == pointGlueClearParam.get_id()) {
										UpdateInfos(pointGlueClearParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
									if (p == pointGlueFaceStartParam.get_id()) {
										UpdateInfos(pointGlueFaceStartParam);
									}
								}
							}
						}
						extendView.setBackgroundColor(Color.WHITE);
					} else {
						extendView = view;
					}
					return extendView;
				}

				@Override
				public void initViewAndListener(View extendView) {
					et_clear_clearGlue = (EditText) extendView
							.findViewById(R.id.et_clear_clearGlue);
					// 设置清胶延时的最大最小值
					et_clear_clearGlue
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueClear.ClearGlueTimeMax,
									GlueClear.GlueClearMin, et_clear_clearGlue));
					et_clear_clearGlue
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueClear.ClearGlueTimeMax,
									GlueClear.GlueClearMin, et_clear_clearGlue));
					et_clear_clearGlue.setSelectAllOnFocus(true);
					// et_facestart_outGlueTimePrev = (EditText) extendView
					// .findViewById(R.id.et_facestart_outGlueTimePrev);
					// et_facestart_movespeed = (EditText) extendView
					// .findViewById(R.id.et_facestart_movespeed);
					// et_facestart_outGlueTime = (EditText) extendView
					// .findViewById(R.id.et_facestart_outGlueTime);
					// et_facestart_stopGlueTime = (EditText) extendView
					// .findViewById(R.id.et_facestart_stopGlueTime);
					// switch_isOutGlue = (ToggleButton) extendView
					// .findViewById(R.id.switch_isOutGlue);
					// switch_startDir = (ToggleButton) extendView
					// .findViewById(R.id.switch_startDir);
					//
					// isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL
					// .ordinal()];
					// isGluePort[0] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport1);
					// isGluePort[1] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport2);
					// isGluePort[2] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport3);
					// isGluePort[3] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport4);
					// isGluePort[4] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport5);
					//
					// // 设置出胶前延时的默认值和最大最小值(要重新设置)
					// et_facestart_outGlueTimePrev
					// .addTextChangedListener(new MaxMinEditWatcher(
					// GlueFaceStart.OutGlueTimePrevMax,
					// GlueFaceStart.GlueFaceStartMin,
					// et_facestart_outGlueTimePrev));
					// et_facestart_outGlueTimePrev
					// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
					// GlueFaceStart.OutGlueTimePrevMax,
					// GlueFaceStart.GlueFaceStartMin,
					// et_facestart_outGlueTimePrev));
					// et_facestart_outGlueTimePrev.setSelectAllOnFocus(true);
					//
					// // 设置出胶后延时的默认值和最大最小值(要重新设置)
					// et_facestart_outGlueTime
					// .addTextChangedListener(new MaxMinEditWatcher(
					// GlueFaceStart.OutGlueTimeMax,
					// GlueFaceStart.GlueFaceStartMin,
					// et_facestart_outGlueTime));
					// et_facestart_outGlueTime
					// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
					// GlueFaceStart.OutGlueTimeMax,
					// GlueFaceStart.GlueFaceStartMin,
					// et_facestart_outGlueTime));
					// et_facestart_outGlueTime.setSelectAllOnFocus(true);
					//
					// // 设置轨迹速度的默认值和最大最小值(要重新设置)
					// et_facestart_movespeed
					// .addTextChangedListener(new MaxMinEditWatcher(
					// GlueFaceStart.MoveSpeedMax,
					// GlueFaceStart.MoveSpeedMin,
					// et_facestart_movespeed));
					// et_facestart_movespeed
					// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
					// GlueFaceStart.MoveSpeedMax,
					// GlueFaceStart.MoveSpeedMin,
					// et_facestart_movespeed));
					// et_facestart_movespeed.setSelectAllOnFocus(true);
					//
					// // 设置停胶延时的默认值和最大最小值(要重新设置)
					// et_facestart_stopGlueTime
					// .addTextChangedListener(new MaxMinEditWatcher(
					// GlueFaceStart.StopGlueTimeMax,
					// GlueFaceStart.GlueFaceStartMin,
					// et_facestart_stopGlueTime));
					// et_facestart_stopGlueTime
					// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
					// GlueFaceStart.StopGlueTimeMax,
					// GlueFaceStart.GlueFaceStartMin,
					// et_facestart_stopGlueTime));
					// et_facestart_stopGlueTime.setSelectAllOnFocus(true);
					rl_moren = (RelativeLayout) extendView
							.findViewById(R.id.rl_moren);
					iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
					rl_save = (RelativeLayout) extendView
							.findViewById(R.id.rl_save);// 保存按钮
					iv_moren = (ImageView) extendView
							.findViewById(R.id.iv_moren);// 默认按钮
					rl_moren.setOnClickListener(this);
					rl_save.setOnClickListener(this);
				}

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.rl_moren:// 设为默认
						// 判断界面
						save();
						if ((isOk && isExist) || firstExist) {// 不为空且已经存在或者不存在且插入新的
							// 刷新ui
							mMorenTextView.setText("当前默认方案号(" + currentTaskNum
									+ ")");
							// 默认号存到sp
							SharePreferenceUtils
									.saveParamNumberToPref(
											GlueClearActivity.this,
											SettingParam.DefaultNum.ParamGlueClearNumber,
											currentTaskNum);
						}
						isExist = false;
						firstExist = false;
						// 更新数据
						break;
					case R.id.rl_save:// 保存
						save();
						// 数据库保存数据
						break;

					default:
						break;
					}
				}
			};
			popupViews.add(popupView);
		}
		popupListView.setItemViews(popupViews);
		if (mType != 1) {
			popupListView.setPosition(defaultNum - 1);// 第一次默认选中第一个item，后面根据方案号(新建点)
		} else {
			// 显示point的参数方案
			// PointGlueAloneParam glueAloneParam= (PointGlueAloneParam)
			// point.getPointParam();
			// System.out.println("传进来的方案号为----------》"+glueAloneParam.get_id());
			popupListView.setPosition(point.getPointParam().get_id() - 1);
		}
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
			list.add(pointGlueClearParam.get_id());
		}
		popupListView.setSelectedEnable(list);
		popupListView.setOnClickPositionChanged(new OnClickPositionChanged() {
			@Override
			public void getCurrentPositon(int position) {
				currentTaskNum = position + 1;
				currentClickNum = position;
			}
		});
		popupListView.setOnZoomInListener(new OnZoomInChanged() {

			@Override
			public void getZoomState(Boolean isZoomIn) {
				if (isZoomIn) {
					// 设置界面
					SetDateAndRefreshUI();
				}
			}
		});
		rl_back.setOnClickListener(this);
		// tv_title = (TextView) findViewById(R.id.tv_title);
		// et_clear_clearGlue = (EditText)
		// findViewById(R.id.et_clear_clearGlue);
		// /* =================== begin =================== */
		// tv_num = (TextView) findViewById(R.id.item_num);
		// tv_clearTime = (TextView) findViewById(R.id.item_clear);
		// // 初始化界面组件
		// plan = (LinearLayout) findViewById(R.id.tv_plan);
		// /* =================== end =================== */
		// rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		//
		// // 设置清胶延时的最大最小值
		// et_clear_clearGlue.addTextChangedListener(new MaxMinEditWatcher(
		// GlueClear.ClearGlueTimeMax, GlueClear.GlueClearMin,
		// et_clear_clearGlue));
		// et_clear_clearGlue
		// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
		// GlueClear.ClearGlueTimeMax, GlueClear.GlueClearMin,
		// et_clear_clearGlue));
		// et_clear_clearGlue.setSelectAllOnFocus(true);
		//
		// tv_title.setText(getResources().getString(
		// R.string.activity_glue_cleario));
		// rl_back.setOnClickListener(this);
		// rl_save.setOnClickListener(this);
		// rl_complete.setOnClickListener(this);

	}
	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 打开extendview的时候设置界面内容，显示最新的方案数据而不是没有保存的数据,没有得到保存的方案
	 * @author wj
	 */
	protected void SetDateAndRefreshUI() {
		pointClearLists = glueClearDao.findAllGlueClearParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
			list.add(pointGlueClearParam.get_id());
		}
		System.out.println("存放主键id的集合---->" + list);
		System.out.println("当前选择的方案号---->" + currentTaskNum);
		System.out.println("list是否存在------------》"
				+ list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			// 已经保存在数据库中的数据
			for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
				if (currentTaskNum == pointGlueFaceStartParam.get_id()) {
					View extendView = popupListView.getItemViews()
							.get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueFaceStartParam);
				}
			}
		} else {
			// 对所有数据进行置空
			View allextendView = popupListView.getItemViews()
					.get(currentClickNum).getExtendView();
			initView(allextendView);
			UpdateInfos(null);
		}
	}

	protected void save() {
		View extendView = popupListView.getItemViews().get(currentClickNum)
				.getExtendView();
		pointClearLists = glueClearDao.findAllGlueClearParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
			list.add(pointGlueClearParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueClearParam upclearParam = getClear(extendView);
			if (pointClearLists.contains(upclearParam)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号
				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != pointClearLists.get(
						pointClearLists.indexOf(upclearParam)).get_id()) {
					ToastUtil.displayPromptInfo(GlueClearActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {
				for (PointGlueClearParam pointGlueFaceStartParam : pointClearLists) {
					if (currentTaskNum == pointGlueFaceStartParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = glueClearDao
							.upDateGlueClear(upclearParam);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upclearParam.get_id(), upclearParam);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upclearParam.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = glueClearDao
							.insertGlueClear(upclearParam);
					firstExist = true;
					pointClearLists = glueClearDao
							.findAllGlueClearParams();
					Log.i(TAG, "保存之后新方案-->" + pointClearLists.toString());
					ToastUtil.displayPromptInfo(GlueClearActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
						list.add(pointGlueClearParam.get_id());
					}
					popupListView.setSelectedEnable(list);
				}
			}
			if (popupListView.isItemZoomIn()) {
				popupListView.zoomOut();
			}
			// 更新title
			refreshTitle();
			flag = false;
		} else {
			ToastUtil.displayPromptInfo(this,
					getResources().getString(R.string.data_is_null));
		}
	}

	private void refreshTitle() {
		pointClearLists = glueClearDao.findAllGlueClearParams();
		// popupListView->pupupview->title
		for (PointGlueClearParam pointGlueClearParam : pointClearLists) {

			if (currentTaskNum == pointGlueClearParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				TextView textViewItem = (TextView) titleViewItem
						.findViewById(R.id.title);
				TextView textViewExtend = (TextView) titleViewExtend
						.findViewById(R.id.title);
				textViewItem.setText(pointGlueClearParam.toString());
				textViewExtend.setText(pointGlueClearParam.toString());
				textViewItem.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
				textViewExtend.setText("清胶延时："+pointGlueClearParam.getClearGlueTime()+"ms");
			}
		}
	}

	private boolean isEditClean(View extendView) {
		et_clear_clearGlue = (EditText) extendView
				.findViewById(R.id.et_clear_clearGlue);
		if ("".equals(et_clear_clearGlue.getText().toString())) {
			return false;
		}
		return true;
	}

	protected void initView(View extendView) {
		et_clear_clearGlue = (EditText) extendView
				.findViewById(R.id.et_clear_clearGlue);
		// rl_moren = (RelativeLayout) findViewById(R.id.rl_moren);
		// iv_add = (ImageView) findViewById(R.id.iv_add);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// iv_moren = (ImageView) findViewById(R.id.iv_moren);
	}
	/**
	 * 将页面上的数据保存到PointGlueClearParam
	 * @param extendView
	 * 
	 * @return PointGlueClearParam
	 */
	private PointGlueClearParam getClear(View extendView) {
		pointClear = new PointGlueClearParam();
		et_clear_clearGlue = (EditText) extendView
				.findViewById(R.id.et_clear_clearGlue);
		try {
			clearGlueint = Integer.parseInt(et_clear_clearGlue.getText()
					.toString());
		} catch (NumberFormatException e) {
			clearGlueint = 0;
		}
		pointClear.setClearGlueTime(clearGlueint);
		 pointClear.set_id(currentTaskNum);

		return pointClear;
	}
	@Override
	public void onBackPressed() {
		// 不想保存只想回退，不保存数据
		if (popupListView.isItemZoomIn()) {
			popupListView.zoomOut();
		} else {
			complete();
			super.onBackPressed();
			overridePendingTransition(R.anim.in_from_left,
					R.anim.out_from_right);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:// 返回按钮的响应事件
			if (popupListView.isItemZoomIn()) {
				popupListView.zoomOut();
			} else {
				complete();
				super.onBackPressed();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_from_right);
			}
			break;
		default:
			break;
		}
	}
	private void complete() {
		ArrayList<? extends PopupView> itemPopuViews = popupListView
				.getItemViews();
		for (PopupView popupView : itemPopuViews) {
			ImageView iv_selected = (ImageView) popupView.getPopupView()
					.findViewById(R.id.iv_selected);
			if (iv_selected.getVisibility() == View.VISIBLE) {
				mIndex = itemPopuViews.indexOf(popupView) + 1;
			}
		}
		System.out.println("返回的方案号为================》" + mIndex);
		point.setPointParam(glueClearDao.getPointGlueClearParamByID(mIndex));
		System.out.println("返回的Point为================》" + point);

		List<Map<Integer, PointGlueClearParam>> list = new ArrayList<Map<Integer, PointGlueClearParam>>();
		list.add(update_id);
		Log.i(TAG, point.toString());
		Bundle extras = new Bundle();
		extras.putParcelable(MyPopWindowClickListener.POPWINDOW_KEY, point);
		extras.putInt(MyPopWindowClickListener.FLAG_KEY, mFlag);
		// 须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的
		ArrayList bundlelist = new ArrayList();
		bundlelist.add(list);
		extras.putParcelableArrayList(MyPopWindowClickListener.TYPE_UPDATE,
				bundlelist);
		intent.putExtras(extras);

		setResult(TaskActivity.resultCode, intent);
	}
}
