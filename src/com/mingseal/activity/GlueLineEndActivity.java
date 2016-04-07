/**
 * 
 */
package com.mingseal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mingseal.adapter.PointGlueLineEndAdapter;
import com.mingseal.adapter.PointGlueLineStartAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueLineEndDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.PointConfigParam.GlueLineMid;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.listener.TextEditWatcher;
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
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.mingseal.data.param.PointConfigParam.GlueLineEnd;

/**
 * @author 商炎炳
 * 
 */
public class GlueLineEndActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueLineEndActivity";
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
	private Intent intent;
	private Point point;// 从taskActivity中传值传过来的point
	private int mFlag;// 0代表增加数据，1代表更新数据
	private int mType;// 1表示要更新数据

	private GlueLineEndDao glueEndDao;
	private List<PointGlueLineEndParam> glueEndLists;
	private PointGlueLineEndParam glueEnd;
	private int param_id = 1;// 选取的是几号方案

	/**
	 * @Fields stopPrevInt: 停胶前延时的int值
	 */
	private int stopTimePrevInt = 0;
	/**
	 * @Fields stopTimeInt: 停胶后延时的int值
	 */
	private int stopTimeInt = 0;
	/**
	 * @Fields upHeightInt: 抬起高度的int值
	 */
	private int upHeightInt = 0;
	/**
	 * @Fields breakGlueLenInt: 提前停胶距离的int值
	 */
	private int breakGlueLenInt = 0;
	/**
	 * @Fields drawDistanceInt: 拉丝距离的int值
	 */
	private int drawDistanceInt = 0;
	/**
	 * @Fields drawSpeedInt: 拉丝速度的int值
	 */
	private int drawSpeedInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	/* =================== begin =================== */
	private HashMap<Integer, PointGlueLineEndParam> update_id;// 修改的方案号集合
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
	/**
	 * @Fields et_lineend_stopPrev: 停胶前延时
	 */
	private EditText et_lineend_stopGlueTimePrev;
	/**
	 * @Fields et_lineend_stop: 停胶后延时
	 */
	private EditText et_lineend_stopGlueTime;
	/**
	 * @Fields et_lineend_upHeight: 抬起高度
	 */
	private EditText et_lineend_upHeight;
	/**
	 * @Fields et_lineend_breakGlueLen: 提前停胶距离
	 */
	private EditText et_lineend_breakGlueLen;
	/**
	 * @Fields et_lineend_drawDistance: 拉丝距离
	 */
	private EditText et_lineend_drawDistance;
	/**
	 * @Fields et_lineend_drawSpeed: 拉丝速度
	 */
	private EditText et_lineend_drawSpeed;
	/**
	 * 是否暂停
	 */
	private ToggleButton switch_isPause;
	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private ImageView iv_moren;
	String[] GluePort;

	/* =================== end =================== */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_line_end);
		update_id = new HashMap<>();
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueLineEndActivity.this,
				SettingParam.DefaultNum.ParamGlueLineEndNumber);

		glueEndDao = new GlueLineEndDao(this);
		glueEndLists = glueEndDao.findAllGlueLineEndParams();
		if (glueEndLists == null || glueEndLists.isEmpty()) {
			glueEnd = new PointGlueLineEndParam();
			glueEnd.set_id(param_id);
			glueEndDao.insertGlueLineEnd(glueEnd);
		}
		glueEndLists = glueEndDao.findAllGlueLineEndParams();
		popupViews = new ArrayList<>();
		GluePort = new String[5];
		initPicker();

	}

	/**
	 * @Title UpdateInfos
	 * @Description 更新extendView数据（保存的数据）
	 * @author wj
	 * @param glueLineEndParam
	 */
	private void UpdateInfos(PointGlueLineEndParam glueLineEndParam) {
		if (glueLineEndParam == null) {
			et_lineend_stopGlueTimePrev.setText("");
			et_lineend_stopGlueTime.setText("");
			et_lineend_upHeight.setText("");
			et_lineend_breakGlueLen.setText("");
			et_lineend_drawDistance.setText("");
			et_lineend_drawSpeed.setText("");

		} else {
			et_lineend_stopGlueTimePrev.setText(glueLineEndParam
					.getStopGlueTimePrev() + "");
			et_lineend_stopGlueTime.setText(glueLineEndParam.getStopGlueTime()
					+ "");
			et_lineend_upHeight.setText(glueLineEndParam.getUpHeight() + "");
			et_lineend_breakGlueLen.setText(glueLineEndParam.getBreakGlueLen()
					+ "");
			et_lineend_drawDistance.setText(glueLineEndParam.getDrawDistance()
					+ "");
			et_lineend_drawSpeed.setText(glueLineEndParam.getDrawSpeed() + "");
			switch_isPause.setChecked(glueLineEndParam.isPause());
		}
	}

	/**
	 * 加载页面组件并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(
				R.string.activity_glue_line_end));
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
					glueEndLists = glueEndDao.findAllGlueLineEndParams();
					textView.setTextSize(25);
					ImageView title_num = (ImageView) view
							.findViewById(R.id.title_num);
					if (p == 1) {// 方案列表第一位对应一号方案
						title_num.setImageResource(R.drawable.green1);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 2) {
						title_num.setImageResource(R.drawable.green2);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 3) {
						title_num.setImageResource(R.drawable.green3);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 4) {
						title_num.setImageResource(R.drawable.green4);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 5) {
						title_num.setImageResource(R.drawable.green5);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 6) {
						title_num.setImageResource(R.drawable.green6);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 7) {
						title_num.setImageResource(R.drawable.green7);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 8) {
						title_num.setImageResource(R.drawable.green8);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 9) {
						title_num.setImageResource(R.drawable.green9);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
										.toString());
							}
						}
					} else if (p == 10) {
						title_num.setImageResource(R.drawable.green10);
						for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
							if (p == pointGlueLineEndParam.get_id()) {
								textView.setText(pointGlueLineEndParam
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
								R.layout.glue_end_extend_view, null);
						int size = glueEndLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
									if (p == pointGlueLineEndParam.get_id()) {
										UpdateInfos(pointGlueLineEndParam);
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

					et_lineend_stopGlueTimePrev = (EditText) extendView
							.findViewById(R.id.et_lineend_stopGlueTimePrev);
					et_lineend_stopGlueTime = (EditText) extendView
							.findViewById(R.id.et_lineend_stopGlueTime);
					et_lineend_upHeight = (EditText) extendView
							.findViewById(R.id.et_lineend_upHeight);
					et_lineend_breakGlueLen = (EditText) extendView
							.findViewById(R.id.et_lineend_breakGlueLen);
					et_lineend_drawDistance = (EditText) extendView
							.findViewById(R.id.et_lineend_drawDistance);
					et_lineend_drawSpeed = (EditText) extendView
							.findViewById(R.id.et_lineend_drawSpeed);
					switch_isPause = (ToggleButton) extendView
							.findViewById(R.id.switch_isPause);

					// 设置停胶前延时的最大最小值
					et_lineend_stopGlueTimePrev
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineEnd.StopGlueTimePrevMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_stopGlueTimePrev));
					et_lineend_stopGlueTimePrev
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineEnd.StopGlueTimePrevMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_stopGlueTimePrev));
					et_lineend_stopGlueTimePrev.setSelectAllOnFocus(true);

					// 设置停胶后延时的最大最小值
					et_lineend_stopGlueTime
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineEnd.StopGlueTimeMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_stopGlueTime));
					et_lineend_stopGlueTime
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineEnd.StopGlueTimeMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_stopGlueTime));
					et_lineend_stopGlueTime.setSelectAllOnFocus(true);

					// 设置抬起高度的最大最小值
					et_lineend_upHeight
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineEnd.UpHeightMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_upHeight));
					et_lineend_upHeight
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineEnd.UpHeightMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_upHeight));
					et_lineend_upHeight.setSelectAllOnFocus(true);

					// 设置提前停胶距离的最大最小值
					et_lineend_breakGlueLen
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineEnd.BreakGlueLenMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_breakGlueLen));
					et_lineend_breakGlueLen
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineEnd.BreakGlueLenMax,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_breakGlueLen));
					et_lineend_breakGlueLen.setSelectAllOnFocus(true);

					// 设置拉丝距离的最大最小值
					et_lineend_drawDistance
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineEnd.DrawDistance,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_drawDistance));
					et_lineend_drawDistance
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineEnd.DrawDistance,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_drawDistance));
					et_lineend_drawDistance.setSelectAllOnFocus(true);

					// 设置拉丝速度的最大最小值
					et_lineend_drawSpeed
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineEnd.DrawSpeed,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_drawSpeed));
					et_lineend_drawSpeed
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineEnd.DrawSpeed,
									GlueLineEnd.GlueLineEndMin,
									et_lineend_drawSpeed));
					et_lineend_drawSpeed.setSelectAllOnFocus(true);

					rl_moren = (RelativeLayout) extendView.findViewById(R.id.rl_moren);
					iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
					rl_save = (RelativeLayout) extendView.findViewById(R.id.rl_save);// 保存按钮
					iv_moren = (ImageView) extendView.findViewById(R.id.iv_moren);// 默认按钮
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
											GlueLineEndActivity.this,
											SettingParam.DefaultNum.ParamGlueLineEndNumber,
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
		for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
			list.add(pointGlueLineEndParam.get_id());
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
	}
	/**
	 * @Title  SetDateAndRefreshUI
	 * @Description 打开extendview的时候设置界面内容，显示最新的方案数据而不是没有保存的数据,没有得到保存的方案
	 * @author wj
	 */
	protected void SetDateAndRefreshUI() {
		glueEndLists= glueEndDao.findAllGlueLineEndParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
			list.add(pointGlueLineEndParam.get_id());
		}
		System.out.println("存放主键id的集合---->"+list);
		System.out.println("当前选择的方案号---->"+currentTaskNum);
		System.out.println("list是否存在------------》"+list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			//已经保存在数据库中的数据
			for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
				if (currentTaskNum==pointGlueLineEndParam.get_id()) {
					View extendView = popupListView.getItemViews().get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueLineEndParam);
				}
			}
		}
		else {
			//对所有数据进行置空
			View allextendView = popupListView.getItemViews().get(currentClickNum).getExtendView();
			initView(allextendView);
			UpdateInfos(null);
		}
	}

	protected void save() {
		View extendView = popupListView.getItemViews().get(currentClickNum)
				.getExtendView();
		glueEndLists = glueEndDao.findAllGlueLineEndParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
			list.add(pointGlueLineEndParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueLineEndParam upLineEndParam = getLineEnd(extendView);
			if (glueEndLists.contains(upLineEndParam)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号
				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != glueEndLists.get(
						glueEndLists.indexOf(upLineEndParam)).get_id()) {
					ToastUtil.displayPromptInfo(GlueLineEndActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {
				for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
					if (currentTaskNum == pointGlueLineEndParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = glueEndDao.upDateGlueLineEnd(upLineEndParam);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upLineEndParam.get_id(), upLineEndParam);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upLineEndParam.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = glueEndDao.insertGlueLineEnd(upLineEndParam);
					firstExist = true;
					glueEndLists = glueEndDao.findAllGlueLineEndParams();
					Log.i(TAG, "保存之后新方案-->" + glueEndLists.toString());
					ToastUtil.displayPromptInfo(GlueLineEndActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {
						list.add(pointGlueLineEndParam.get_id());
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
	 * @Title  refreshTitle
	 * @Description 按下保存之后刷新title
	 * @author wj
	 */
	private void refreshTitle() {
		glueEndLists = glueEndDao.findAllGlueLineEndParams();
		// popupListView->pupupview->title
		for (PointGlueLineEndParam pointGlueLineEndParam : glueEndLists) {

			if (currentTaskNum == pointGlueLineEndParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				TextView textViewItem = (TextView) titleViewItem
						.findViewById(R.id.title);
				TextView textViewExtend = (TextView) titleViewExtend
						.findViewById(R.id.title);
				textViewItem.setText(pointGlueLineEndParam.toString());
				textViewExtend.setText(pointGlueLineEndParam.toString());
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
		et_lineend_stopGlueTimePrev = (EditText) extendView
				.findViewById(R.id.et_lineend_stopGlueTimePrev);
		et_lineend_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_lineend_stopGlueTime);
		et_lineend_upHeight = (EditText) extendView
				.findViewById(R.id.et_lineend_upHeight);
		et_lineend_breakGlueLen = (EditText) extendView
				.findViewById(R.id.et_lineend_breakGlueLen);
		et_lineend_drawDistance = (EditText) extendView
				.findViewById(R.id.et_lineend_drawDistance);
		et_lineend_drawSpeed = (EditText) extendView
				.findViewById(R.id.et_lineend_drawSpeed);

		if ("".equals(et_lineend_breakGlueLen.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_drawDistance.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_drawSpeed.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_stopGlueTime.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_stopGlueTimePrev.getText().toString())) {
			return false;
		} else if ("".equals(et_lineend_upHeight.getText().toString())) {
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
		et_lineend_stopGlueTimePrev = (EditText) extendView
				.findViewById(R.id.et_lineend_stopGlueTimePrev);
		et_lineend_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_lineend_stopGlueTime);
		et_lineend_upHeight = (EditText) extendView
				.findViewById(R.id.et_lineend_upHeight);
		et_lineend_breakGlueLen = (EditText) extendView
				.findViewById(R.id.et_lineend_breakGlueLen);
		et_lineend_drawDistance = (EditText) extendView
				.findViewById(R.id.et_lineend_drawDistance);
		et_lineend_drawSpeed = (EditText) extendView
				.findViewById(R.id.et_lineend_drawSpeed);
		switch_isPause = (ToggleButton) extendView
				.findViewById(R.id.switch_isPause);
		// rl_moren = (RelativeLayout) findViewById(R.id.rl_moren);
		// iv_add = (ImageView) findViewById(R.id.iv_add);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// iv_moren = (ImageView) findViewById(R.id.iv_moren);
	}

	/**
	 * 将页面上的数据保存到PointGlueLineEndParam对象中
	 * 
	 * @param extendView
	 * 
	 * @return PointGlueLineEndParam
	 */
	private PointGlueLineEndParam getLineEnd(View extendView) {
		glueEnd = new PointGlueLineEndParam();
		et_lineend_stopGlueTimePrev = (EditText) extendView
				.findViewById(R.id.et_lineend_stopGlueTimePrev);
		et_lineend_stopGlueTime = (EditText) extendView
				.findViewById(R.id.et_lineend_stopGlueTime);
		et_lineend_upHeight = (EditText) extendView
				.findViewById(R.id.et_lineend_upHeight);
		et_lineend_breakGlueLen = (EditText) extendView
				.findViewById(R.id.et_lineend_breakGlueLen);
		et_lineend_drawDistance = (EditText) extendView
				.findViewById(R.id.et_lineend_drawDistance);
		et_lineend_drawSpeed = (EditText) extendView
				.findViewById(R.id.et_lineend_drawSpeed);
		switch_isPause = (ToggleButton) extendView
				.findViewById(R.id.switch_isPause);
		try {
			stopTimePrevInt = Integer.parseInt(et_lineend_stopGlueTimePrev
					.getText().toString());
		} catch (NumberFormatException e) {
			stopTimePrevInt = 0;
		}
		try {
			stopTimeInt = Integer.parseInt(et_lineend_stopGlueTime.getText()
					.toString());
		} catch (NumberFormatException e) {
			stopTimeInt = 0;
		}
		try {
			upHeightInt = Integer.parseInt(et_lineend_upHeight.getText()
					.toString());
		} catch (NumberFormatException e) {
			upHeightInt = 0;
		}
		try {
			breakGlueLenInt = Integer.parseInt(et_lineend_breakGlueLen
					.getText().toString());
		} catch (NumberFormatException e) {
			breakGlueLenInt = 0;
		}
		try {
			drawDistanceInt = Integer.parseInt(et_lineend_drawDistance
					.getText().toString());
		} catch (NumberFormatException e) {
			drawDistanceInt = 0;
		}
		try {
			drawSpeedInt = Integer.parseInt(et_lineend_drawSpeed.getText()
					.toString());
		} catch (NumberFormatException e) {
			drawSpeedInt = 0;
		}

		glueEnd.setStopGlueTimePrev(stopTimePrevInt);
		glueEnd.setStopGlueTime(stopTimeInt);
		glueEnd.setUpHeight(upHeightInt);
		glueEnd.setBreakGlueLen(breakGlueLenInt);
		glueEnd.setDrawDistance(drawDistanceInt);
		glueEnd.setDrawSpeed(drawSpeedInt);
		glueEnd.setPause(switch_isPause.isChecked());

		glueEnd.set_id(currentTaskNum);
		return glueEnd;
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
		 ArrayList<? extends PopupView> itemPopuViews = popupListView.getItemViews();
		 for (PopupView popupView : itemPopuViews) {
			 ImageView iv_selected= (ImageView) popupView.getPopupView().findViewById(R.id.iv_selected);
			 if (iv_selected.getVisibility()==View.VISIBLE) {
				mIndex = itemPopuViews.indexOf(popupView)+1;
			}
		}
		 System.out.println("返回的方案号为================》"+mIndex);
		 point.setPointParam(glueEndDao.getPointGlueLineEndParamByID(mIndex));
		 System.out.println("返回的Point为================》"+point);

		 List<Map<Integer, PointGlueLineEndParam>> list = new ArrayList<Map<Integer, PointGlueLineEndParam>>();  
		 list.add(update_id); 
			Log.i(TAG, point.toString());
			Bundle extras = new Bundle();
			extras.putParcelable(MyPopWindowClickListener.POPWINDOW_KEY, point);
			extras.putInt(MyPopWindowClickListener.FLAG_KEY, mFlag);
			//须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的 
			ArrayList bundlelist = new ArrayList(); 
			bundlelist.add(list);
			extras.putParcelableArrayList(MyPopWindowClickListener.TYPE_UPDATE, bundlelist);
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
