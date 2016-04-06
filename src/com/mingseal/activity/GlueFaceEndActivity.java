/**
 * 
 */
package com.mingseal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mingseal.adapter.PointGlueFaceEndAdapter;
import com.mingseal.adapter.PointGlueFaceStartAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.PointConfigParam.GlueFaceStart;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
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
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import static com.mingseal.data.param.PointConfigParam.GlueFaceEnd;

/**
 * @author 商炎炳
 * @description 面终点
 */
public class GlueFaceEndActivity extends Activity implements OnClickListener{

	private final static String TAG = "GlueFaceEndActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
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
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	private Handler handler;
	/* =================== begin =================== */
	private HashMap<Integer, PointGlueFaceEndParam> update_id;// 修改的方案号集合
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
	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private ImageView iv_moren;
	/**
	 * @Fields et_end_stopGlueTime: 停胶延时
	 */
	private EditText et_faceend_stopGlueTime;
	/**
	 * @Fields et_end_upHeight: 抬起高度
	 */
	private EditText et_faceend_upheight;
	/**
	 * @Fields et_end_lineNum: 直线条数
	 */
	private EditText et_faceend_lineNum;
	/**
	 * 是否暂停
	 */
	private ToggleButton switch_isPause;

	/* =================== end =================== */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_face_end);
		update_id = new HashMap<>();
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueFaceEndActivity.this,
				SettingParam.DefaultNum.ParamGlueFaceEndNumber);

		glueFaceEndDao = new GlueFaceEndDao(GlueFaceEndActivity.this);
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		if (pointEndLists == null || pointEndLists.isEmpty()) {
			pointEnd = new PointGlueFaceEndParam();
			pointEnd.set_id(param_id);
			glueFaceEndDao.insertGlueFaceEnd(pointEnd);
			// 插入主键id
		}
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		popupViews = new ArrayList<>();
		initPicker();
		// 初始化Handler,用来处理消息
		// handler = new Handler(GlueFaceEndActivity.this);
		// mFaceEndAdapter = new
		// PointGlueFaceEndAdapter(GlueFaceEndActivity.this);
		// mFaceEndAdapter.setGlueStartLists(pointEndLists);
		// faceEndSpinner.setAdapter(mFaceEndAdapter);
		// 如果为1的话，需要设置值
		// if (mType == 1) {
		// PointGlueFaceEndParam glueFaceEndParam =
		// glueFaceEndDao.getPointFaceEndParamByID(point.getPointParam().get_id());
		// param_id = glueFaceEndDao
		// .getFaceEndParamIDByParam(glueFaceEndParam);// 传过来的方案的参数序列主键。
		// SetDateAndRefreshUI(glueFaceEndParam);
		// // faceEndSpinner.setSelection(point.getPointParam().get_id() - 1);
		// // mFaceEndAdapter.notifyDataSetChanged();
		// } else {
		// // 不为1的话，需要选定默认的第一个方案
		// PointGlueFaceEndParam defaultParam = pointEndLists.get(0);
		// param_id = glueFaceEndDao.getFaceEndParamIDByParam(defaultParam);//
		// 默认的参数序列主键。
		// SetDateAndRefreshUI(defaultParam);
		// }
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
	 * @Title UpdateInfos
	 * @Description 更新上半部分界面
	 * @author wj
	 * @param glueFaceEndParam
	 */
	private void UpdateInfos(PointGlueFaceEndParam glueFaceEndParam) {
		if (glueFaceEndParam == null) {
			et_faceend_stopGlueTime.setText("");
			et_faceend_upheight.setText("");
			et_faceend_lineNum.setText("");

		} else {
			et_faceend_stopGlueTime.setText(glueFaceEndParam.getStopGlueTime()
					+ "");
			et_faceend_upheight.setText(glueFaceEndParam.getUpHeight() + "");
			et_faceend_lineNum.setText(glueFaceEndParam.getLineNum() + "");

			switch_isPause.setChecked(glueFaceEndParam.isPause());
		}
	}

	/**
	 * 加载自定义的组件，并设置NumberPicker的最大最小和默认值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(
				R.string.activity_glue_face_end));
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
					pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
					textView.setTextSize(30);
					if (p == 1) {// 方案列表第一位对应一号方案
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 2) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 3) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 4) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 5) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 6) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 7) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 8) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 9) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					} else if (p == 10) {
						for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
							if (p == pointGlueFaceEndParam.get_id()) {
								textView.setText(pointGlueFaceEndParam
										.toString());
							}
						}
					}
				}

				@Override
				public View setExtendView(View view) {
					if (view == null) {
						extendView = LayoutInflater.from(
								getApplicationContext()).inflate(
								R.layout.glue_face_end_extend_view, null);
						int size = pointEndLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
									if (p == pointGlueFaceEndParam.get_id()) {
										UpdateInfos(pointGlueFaceEndParam);
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
					et_faceend_stopGlueTime = (EditText) extendView
							.findViewById(R.id.et_faceend_stopGlueTime);
					et_faceend_upheight = (EditText) extendView
							.findViewById(R.id.et_faceend_upheight);
					et_faceend_lineNum = (EditText) extendView
							.findViewById(R.id.et_faceend_lineNum);
					switch_isPause = (ToggleButton) extendView
							.findViewById(R.id.switch_isPause);
					// 设置最大最小值
					et_faceend_stopGlueTime
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceEnd.StopGlueTimeMax,
									GlueFaceEnd.GlueFaceEndMin,
									et_faceend_stopGlueTime));
					et_faceend_stopGlueTime
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceEnd.StopGlueTimeMax,
									GlueFaceEnd.GlueFaceEndMin,
									et_faceend_stopGlueTime));
					et_faceend_stopGlueTime.setSelectAllOnFocus(true);

					et_faceend_upheight
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceEnd.UpHeightMax,
									GlueFaceEnd.GlueFaceEndMin,
									et_faceend_upheight));
					et_faceend_upheight
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceEnd.UpHeightMax,
									GlueFaceEnd.GlueFaceEndMin,
									et_faceend_upheight));
					et_faceend_upheight.setSelectAllOnFocus(true);

					et_faceend_lineNum
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueFaceEnd.LineNumMax,
									GlueFaceEnd.GlueFaceEndMin,
									et_faceend_lineNum));
					et_faceend_lineNum
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueFaceEnd.LineNumMax,
									GlueFaceEnd.GlueFaceEndMin,
									et_faceend_lineNum));
					et_faceend_lineNum.setSelectAllOnFocus(true);
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
											GlueFaceEndActivity.this,
											SettingParam.DefaultNum.ParamGlueFaceEndNumber,
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
		for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
			list.add(pointGlueFaceEndParam.get_id());
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
		// et_end_stopGlueTime = (EditText)
		// findViewById(R.id.et_faceend_stopGlueTime);
		// et_end_upHeight = (EditText) findViewById(R.id.et_faceend_upheight);
		// et_end_lineNum = (EditText) findViewById(R.id.et_faceend_lineNum);
		// isPauseSwitch = (Switch) findViewById(R.id.switch_isPause);
		//
		// rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		//
		// /* =================== begin =================== */
		// tv_num = (TextView) findViewById(R.id.item_num);
		// tv_lineNum = (TextView) findViewById(R.id.item_end_lineNum);
		// tv_stopGlue = (TextView) findViewById(R.id.item_end_stopGlueTime);
		// tv_upHeight = (TextView)findViewById(R.id.item_end_upHeight);
		// // 初始化界面组件
		// plan = (LinearLayout) findViewById(R.id.tv_plan);
		// /* =================== end =================== */
		// // 设置最大最小值
		// et_end_stopGlueTime.addTextChangedListener(new MaxMinEditWatcher(
		// GlueFaceEnd.StopGlueTimeMax, GlueFaceEnd.GlueFaceEndMin,
		// et_end_stopGlueTime));
		// et_end_stopGlueTime
		// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
		// GlueFaceEnd.StopGlueTimeMax,
		// GlueFaceEnd.GlueFaceEndMin, et_end_stopGlueTime));
		// et_end_stopGlueTime.setSelectAllOnFocus(true);
		//
		// et_end_upHeight.addTextChangedListener(new MaxMinEditWatcher(
		// GlueFaceEnd.UpHeightMax, GlueFaceEnd.GlueFaceEndMin,
		// et_end_upHeight));
		// et_end_upHeight.setOnFocusChangeListener(new
		// MaxMinFocusChangeListener(
		// GlueFaceEnd.UpHeightMax, GlueFaceEnd.GlueFaceEndMin,
		// et_end_upHeight));
		// et_end_upHeight.setSelectAllOnFocus(true);
		//
		// et_end_lineNum.addTextChangedListener(new MaxMinEditWatcher(
		// GlueFaceEnd.LineNumMax, GlueFaceEnd.GlueFaceEndMin,
		// et_end_lineNum));
		// et_end_lineNum.setOnFocusChangeListener(new
		// MaxMinFocusChangeListener(
		// GlueFaceEnd.LineNumMax, GlueFaceEnd.GlueFaceEndMin,
		// et_end_lineNum));
		// et_end_lineNum.setSelectAllOnFocus(true);
		//
		// tv_title.setText(getResources().getString(
		// R.string.activity_glue_face_end));
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
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
			list.add(pointGlueFaceEndParam.get_id());
		}
		System.out.println("存放主键id的集合---->" + list);
		System.out.println("当前选择的方案号---->" + currentTaskNum);
		System.out.println("list是否存在------------》"
				+ list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			// 已经保存在数据库中的数据
			for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
				if (currentTaskNum == pointGlueFaceEndParam.get_id()) {
					View extendView = popupListView.getItemViews()
							.get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueFaceEndParam);
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
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
			list.add(pointGlueFaceEndParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueFaceEndParam upfaceEndParam = getFaceEnd(extendView);
			if (pointEndLists.contains(upfaceEndParam)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号
				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != pointEndLists.get(
						pointEndLists.indexOf(upfaceEndParam)).get_id()) {
					ToastUtil.displayPromptInfo(GlueFaceEndActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {
				for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
					if (currentTaskNum == pointGlueFaceEndParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = glueFaceEndDao
							.upDateGlueFaceStart(upfaceEndParam);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upfaceEndParam.get_id(), upfaceEndParam);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upfaceEndParam.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = glueFaceEndDao
							.insertGlueFaceEnd(upfaceEndParam);
					firstExist = true;
					pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
					Log.i(TAG, "保存之后新方案-->" + pointEndLists.toString());
					ToastUtil.displayPromptInfo(GlueFaceEndActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {
						list.add(pointGlueFaceEndParam.get_id());
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

	/**
	 * @Title refreshTitle
	 * @Description 按下保存之后刷新title
	 * @author wj
	 */
	private void refreshTitle() {
		pointEndLists = glueFaceEndDao.findAllGlueFaceEndParams();
		// popupListView->pupupview->title
		for (PointGlueFaceEndParam pointGlueFaceEndParam : pointEndLists) {

			if (currentTaskNum == pointGlueFaceEndParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				TextView textViewItem = (TextView) titleViewItem
						.findViewById(R.id.title);
				TextView textViewExtend = (TextView) titleViewExtend
						.findViewById(R.id.title);
				textViewItem.setText(pointGlueFaceEndParam.toString());
				textViewExtend.setText(pointGlueFaceEndParam.toString());
			}
		}
	}

	/**
	 * @Title isEditClean
	 * @Description 判断输入框是否为空
	 * @author wj
	 * @param extendView
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditClean(View extendView) {
		et_faceend_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_faceend_stopGlueTime);
		et_faceend_upheight = (EditText) extendView
				.findViewById(R.id.et_faceend_upheight);
		et_faceend_lineNum = (EditText) extendView
				.findViewById(R.id.et_faceend_lineNum);
		if ("".equals(et_faceend_lineNum.getText().toString())) {
			return false;
		} else if ("".equals(et_faceend_stopGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_faceend_upheight.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * @Title initView
	 * @Description 初始化当前extendView视图
	 * @author wj
	 * @param extendView
	 */
	protected void initView(View extendView) {
		et_faceend_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_faceend_stopGlueTime);
		et_faceend_upheight = (EditText) extendView
				.findViewById(R.id.et_faceend_upheight);
		et_faceend_lineNum = (EditText) extendView
				.findViewById(R.id.et_faceend_lineNum);
		switch_isPause = (ToggleButton) extendView
				.findViewById(R.id.switch_isPause);
		// rl_moren = (RelativeLayout) extendView.findViewById(R.id.rl_moren);
		// iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
		// rl_save = (RelativeLayout) extendView.findViewById(R.id.rl_save);
		// iv_moren = (ImageView) extendView.findViewById(R.id.iv_moren);
	}

	/**
	 * @Title isEditNull
	 * @Description 判断输入框是否为空
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditNull() {
		if ("".equals(et_faceend_lineNum.getText().toString())) {
			return false;
		} else if ("".equals(et_faceend_stopGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_faceend_upheight.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * 将页面上的数据保存到一个PointGlueFaceEndParam对象中
	 * 
	 * @param extendView
	 * 
	 * @return PointGlueFaceEndParam
	 */
	private PointGlueFaceEndParam getFaceEnd(View extendView) {
		pointEnd = new PointGlueFaceEndParam();
		et_faceend_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_faceend_stopGlueTime);
		et_faceend_upheight = (EditText) extendView
				.findViewById(R.id.et_faceend_upheight);
		et_faceend_lineNum = (EditText) extendView
				.findViewById(R.id.et_faceend_lineNum);
		switch_isPause = (ToggleButton) extendView
				.findViewById(R.id.switch_isPause);
		try {
			stopGlueTimeInt = Integer.parseInt(et_faceend_stopGlueTime
					.getText().toString());
		} catch (NumberFormatException e) {
			stopGlueTimeInt = 0;
		}
		try {
			upHeightInt = Integer.parseInt(et_faceend_upheight.getText()
					.toString());
		} catch (NumberFormatException e) {
			upHeightInt = 0;
		}
		try {
			lineNumeInt = Integer.parseInt(et_faceend_lineNum.getText()
					.toString());
		} catch (NumberFormatException e) {
			lineNumeInt = 0;
		}
		pointEnd.setStopGlueTime(stopGlueTimeInt);
		pointEnd.setUpHeight(upHeightInt);
		pointEnd.setLineNum(lineNumeInt);
		pointEnd.setPause(switch_isPause.isChecked());
		pointEnd.set_id(currentTaskNum);

		return pointEnd;
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
		point.setPointParam(glueFaceEndDao.getPointFaceEndParamByID(mIndex));
		System.out.println("返回的Point为================》" + point);

		List<Map<Integer, PointGlueFaceEndParam>> list = new ArrayList<Map<Integer, PointGlueFaceEndParam>>();
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

}
