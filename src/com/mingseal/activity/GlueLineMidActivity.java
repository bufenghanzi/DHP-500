/**
 * 
 */
package com.mingseal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.PointConfigParam.GlueAlone;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
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
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ToggleButton;
import static com.mingseal.data.param.PointConfigParam.GlueLineMid;

/**
 * @author 商炎炳
 * 
 */
public class GlueLineMidActivity extends Activity implements OnClickListener {

	private final static String TAG = "GlueLineMidActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;

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
	private boolean flag = false;// 可以与用户交互，初始化完成标志

	/* =================== begin =================== */
	private HashMap<Integer, PointGlueLineMidParam> update_id;// 修改的方案号集合
	private int defaultNum = 1;// 默认号
	ArrayList<PopupView> popupViews;
	private TextView mMorenTextView;
	PopupListView popupListView;
	int p = 0;
	View extendView;
	private ToggleButton switch_isOutGlue;
	private TextView tv_moveSpeed;
	private EditText et_linemid_moveSpeed;
	private EditText et_radius;
	/**
	 * 断胶前距离
	 */
	private EditText et_stopDisPrev;
	/**
	 * 断胶后距离
	 */
	private EditText et_stopDisNext;
	private ToggleButton switch_glueport1;
	private ToggleButton switch_glueport2;
	private ToggleButton switch_glueport3;
	private ToggleButton switch_glueport4;
	private ToggleButton switch_glueport5;
	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private ImageView iv_moren;
	/**
	 * 点胶口
	 */
	private ToggleButton[] isGluePort;
	private boolean isOk;
	private boolean isExist = false;// 是否存在
	private boolean firstExist = false;// 是否存在
	/**
	 * 当前任务号
	 */
	private int currentTaskNum;
	private int currentClickNum;// 当前点击的序号
	private int mIndex;// 对应方案号
	String[] GluePort;
	  private TextView title_moveSpeed;
	    private TextView title_et_linemid_moveSpeed;
	    private TextView activity_mm_s;
	    private TextView activity_fenghao;
	    private TextView title_activity_glue_radius;
	    private TextView title_et_radius;
	    private TextView activity_mm;
	    private TextView activity_second_fenghao;
	    private TextView title_stopDisPrev;
	    private TextView title_et_stopDisPrev;
	    private TextView activity_second_mm;
	    private TextView activity_third_fenghao;
	    private TextView title_stopDisNext;
	    private TextView title_et_stopDisNext;
	    private TextView activity_third_mm;
	    private TextView activity_four_fenghao;
	    private TextView title_activity_glue_port;
	    private TextView title_et_activity_glue_port;
	/* =================== end =================== */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_line_mid);
		update_id = new HashMap<>();
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueLineMidActivity.this,
				SettingParam.DefaultNum.ParamGlueLineMidNumber);
		glueMidDao = new GlueLineMidDao(GlueLineMidActivity.this);
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		if (glueMidLists == null || glueMidLists.isEmpty()) {
			glueMid = new PointGlueLineMidParam();
			glueMid.set_id(param_id);
			glueMidDao.insertGlueLineMid(glueMid);
			// 插入主键id
		}
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		// 初始化数组
		glueBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		popupViews = new ArrayList<>();
		GluePort = new String[5];
		initPicker();
		Log.d(TAG, glueMidLists.toString());
	}

	/**
	 * @Title UpdateInfos
	 * @Description 更新extendView数据（保存的数据）
	 * @author wj
	 * @param pointGlueAloneParam
	 */
	private void UpdateInfos(PointGlueLineMidParam glueLineMidParam) {
		if (glueLineMidParam == null) {
			et_linemid_moveSpeed.setText("");
			et_radius.setText("");
			et_stopDisPrev.setText("");
			et_stopDisNext.setText("");

		} else {
			et_linemid_moveSpeed.setText(glueLineMidParam.getMoveSpeed() + "");
			et_radius.setText(glueLineMidParam.getRadius() + "");
			et_stopDisPrev.setText(glueLineMidParam.getStopGlueDisPrev() + "");
			et_stopDisNext.setText(glueLineMidParam.getStopGLueDisNext() + "");

			switch_isOutGlue.setChecked(glueLineMidParam.isOutGlue());

			isGluePort[0].setChecked(glueLineMidParam.getGluePort()[0]);
			isGluePort[1].setChecked(glueLineMidParam.getGluePort()[1]);
			isGluePort[2].setChecked(glueLineMidParam.getGluePort()[2]);
			isGluePort[3].setChecked(glueLineMidParam.getGluePort()[3]);
			isGluePort[4].setChecked(glueLineMidParam.getGluePort()[4]);
		}
	}

	/**
	 * 加载自定义组件并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(
				R.string.activity_glue_line_mid));
		mMorenTextView = (TextView) findViewById(R.id.morenfangan);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		mMorenTextView.setText("当前默认方案号(" + defaultNum + ")");
		// 初始化popuplistview区域
		popupListView = (PopupListView) findViewById(R.id.popupListView);
		popupListView.init(null);

		// 初始化创建10个popupView
		for (int i = 0; i < 10; i++) {
			p = i + 1;
			PopupView popupView = new PopupView(this, R.layout.popup_view_item_glue_mid) {

				@Override
				public void setViewsElements(View view) {
//					TextView textView = (TextView) view
//							.findViewById(R.id.title);
					glueMidLists = glueMidDao.findAllGlueLineMidParams();
					ImageView title_num = (ImageView) view
							.findViewById(R.id.title_num);
					if (p == 1) {// 方案列表第一位对应一号方案
						title_num.setImageResource(R.drawable.green1);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 2) {
						title_num.setImageResource(R.drawable.green2);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 3) {
						title_num.setImageResource(R.drawable.green3);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 4) {
						title_num.setImageResource(R.drawable.green4);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 5) {
						title_num.setImageResource(R.drawable.green5);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 6) {
						title_num.setImageResource(R.drawable.green6);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 7) {
						title_num.setImageResource(R.drawable.green7);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 8) {
						title_num.setImageResource(R.drawable.green8);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 9) {
						title_num.setImageResource(R.drawable.green9);
						setTitleInfos(glueMidLists, view, p);
					} else if (p == 10) {
						title_num.setImageResource(R.drawable.green10);
						setTitleInfos(glueMidLists, view, p);
					}
				}

				@Override
				public View setExtendView(View view) {
					if (view == null) {
						extendView = LayoutInflater.from(
								getApplicationContext()).inflate(
								R.layout.glue_mid_extend_view, null);
						int size = glueMidLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
									if (p == pointGlueLineMidParam.get_id()) {
										UpdateInfos(pointGlueLineMidParam);
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
					et_linemid_moveSpeed = (EditText) extendView
							.findViewById(R.id.et_linemid_moveSpeed);
					switch_isOutGlue = (ToggleButton) extendView
							.findViewById(R.id.switch_isOutGlue);
					et_radius = (EditText) extendView
							.findViewById(R.id.et_radius);
					et_stopDisPrev = (EditText) extendView
							.findViewById(R.id.et_stopDisPrev);

					et_stopDisNext = (EditText) extendView
							.findViewById(R.id.et_stopDisNext);
					isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL
							.ordinal()];
					isGluePort[0] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport1);
					isGluePort[1] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport2);
					isGluePort[2] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport3);
					isGluePort[3] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport4);
					isGluePort[4] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport5);
					// 轨迹速度设置最大最小值
					et_linemid_moveSpeed
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueLineMid.MoveSpeedMax,
									GlueLineMid.GlueLineMidMin,
									et_linemid_moveSpeed));
					et_linemid_moveSpeed
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueLineMid.MoveSpeedMax,
									GlueLineMid.GlueLineMidMin,
									et_linemid_moveSpeed));
					et_linemid_moveSpeed.setSelectAllOnFocus(true);
					TextEditWatcher teWatcher = new TextEditWatcher();
					et_stopDisPrev.addTextChangedListener(teWatcher);
					et_stopDisNext.addTextChangedListener(teWatcher);
					et_radius.addTextChangedListener(teWatcher);

					rl_moren = (RelativeLayout) extendView
							.findViewById(R.id.rl_moren);
					iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
					rl_save = (RelativeLayout) extendView
							.findViewById(R.id.rl_save);// 保存按钮
					iv_moren = (ImageView) extendView
							.findViewById(R.id.iv_moren);// 默认按钮
					rl_moren.setOnClickListener(this);
					rl_save.setOnClickListener(this);
					// 点击全选
					et_linemid_moveSpeed.setSelectAllOnFocus(true);
					et_stopDisPrev.setSelectAllOnFocus(true);
					et_radius.setSelectAllOnFocus(true);
					et_stopDisNext.setSelectAllOnFocus(true);
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
											GlueLineMidActivity.this,
											SettingParam.DefaultNum.ParamGlueLineMidNumber,
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
		for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
			list.add(pointGlueLineMidParam.get_id());
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

	protected void setTitleInfos(List<PointGlueLineMidParam> glueMidLists,
			View view, int p) {
		 title_moveSpeed = (TextView) view.findViewById(R.id.title_moveSpeed);
	        title_et_linemid_moveSpeed = (TextView) view.findViewById(R.id.title_et_linemid_moveSpeed);
	        activity_mm_s = (TextView) view.findViewById(R.id.activity_mm_s);
	        activity_fenghao = (TextView) view.findViewById(R.id.activity_fenghao);
	        title_activity_glue_radius = (TextView) view.findViewById(R.id.title_activity_glue_radius);
	        title_et_radius = (TextView) view.findViewById(R.id.title_et_radius);
	        activity_mm = (TextView) view.findViewById(R.id.activity_mm);
	        activity_second_fenghao = (TextView) view.findViewById(R.id.activity_second_fenghao);
	        title_stopDisPrev = (TextView) view.findViewById(R.id.title_stopDisPrev);
	        title_et_stopDisPrev = (TextView) view.findViewById(R.id.title_et_stopDisPrev);
	        activity_second_mm = (TextView) view.findViewById(R.id.activity_second_mm);
	        activity_third_fenghao = (TextView) view.findViewById(R.id.activity_third_fenghao);
	        title_stopDisNext = (TextView) view.findViewById(R.id.title_stopDisNext);
	        title_et_stopDisNext = (TextView) view.findViewById(R.id.title_et_stopDisNext);
	        activity_third_mm = (TextView) view.findViewById(R.id.activity_third_mm);
	        activity_four_fenghao = (TextView) view.findViewById(R.id.activity_four_fenghao);
	        title_activity_glue_port = (TextView) view.findViewById(R.id.title_activity_glue_port);
	        title_et_activity_glue_port = (TextView) view.findViewById(R.id.title_et_activity_glue_port);
	        for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
				if (p == pointGlueLineMidParam.get_id()) {
					title_moveSpeed.setText(getResources().getString(
							R.string.activity_glue_moveSpeed)
							+ " ");
					activity_mm_s.setText(getResources().getString(
							R.string.activity_mm_s));
					activity_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					title_activity_glue_radius.setText(getResources().getString(
							R.string.activity_glue_radius)
							+ " ");
					activity_mm.setText(getResources().getString(
							R.string.activity_mm));
					activity_second_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					title_stopDisPrev.setText(getResources().getString(
							R.string.activity_glue_stopGlueDisPrev)
							+ " ");
					activity_second_mm.setText(getResources().getString(
							R.string.activity_mm));
					activity_third_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					title_stopDisNext.setText(getResources()
							.getString(R.string.activity_glue_stopGlueDisNext)
							+ " ");
					activity_third_mm.setText(getResources().getString(R.string.activity_mm));
					activity_four_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					title_activity_glue_port.setText(getResources().getString(
							R.string.activity_glue_port)
							+ " ");
					activity_four_fenghao.setText(getResources().getString(
							R.string.activity_fenghao)
							+ " ");
					
					for (int j = 0; j < 5; j++) {
						if (pointGlueLineMidParam.getGluePort()[j]) {
							GluePort[j] = "开";
						} else {
							GluePort[j] = "关";
						}
					}

					title_et_linemid_moveSpeed.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_linemid_moveSpeed.getPaint()
							.setAntiAlias(true); // 抗锯齿
					title_et_radius.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_radius.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_stopDisPrev.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_stopDisPrev.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_stopDisNext.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_stopDisNext.getPaint().setAntiAlias(true); // 抗锯齿
					title_et_activity_glue_port.getPaint().setFlags(
							Paint.UNDERLINE_TEXT_FLAG); // 下划线
					title_et_activity_glue_port.getPaint().setAntiAlias(true); // 抗锯齿
					
					title_et_linemid_moveSpeed.setText(pointGlueLineMidParam.getMoveSpeed()+"");
					title_et_radius.setText(pointGlueLineMidParam.getRadius()+"");
					title_et_stopDisPrev.setText(pointGlueLineMidParam.getStopGlueDisPrev()+"");
					title_et_stopDisNext.setText(pointGlueLineMidParam.getStopGLueDisNext()+"");
//					if (pointGlueLineMidParam.isOutGlue()) {
//						title_et_stopDisNext.setText("是");
//					} else {
//						title_et_stopDisNext.setText("否");
//					}
					title_et_activity_glue_port.setText(GluePort[0] + GluePort[1]
							+ GluePort[2] + GluePort[3] + GluePort[4]);
				}
			}
	}

	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 打开extendview的时候设置界面内容，显示最新的方案数据而不是没有保存的数据,没有得到保存的方案
	 * @author wj
	 */
	protected void SetDateAndRefreshUI() {
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
			list.add(pointGlueLineMidParam.get_id());
		}
		System.out.println("存放主键id的集合---->" + list);
		System.out.println("当前选择的方案号---->" + currentTaskNum);
		System.out.println("list是否存在------------》"
				+ list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			// 已经保存在数据库中的数据
			for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
				if (currentTaskNum == pointGlueLineMidParam.get_id()) {
					View extendView = popupListView.getItemViews()
							.get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueLineMidParam);
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
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
			list.add(pointGlueLineMidParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueLineMidParam upLineMidParam = getLineMid(extendView);
			if (glueMidLists.contains(upLineMidParam)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号
				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != glueMidLists.get(
						glueMidLists.indexOf(upLineMidParam)).get_id()) {
					ToastUtil.displayPromptInfo(GlueLineMidActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {

				for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
					if (currentTaskNum == pointGlueLineMidParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = glueMidDao.upDateGlueLineMid(upLineMidParam);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upLineMidParam.get_id(), upLineMidParam);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upLineMidParam.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = glueMidDao.insertGlueLineMid(upLineMidParam);
					firstExist = true;
					glueMidLists = glueMidDao.findAllGlueLineMidParams();
					Log.i(TAG, "保存之后新方案-->" + glueMidLists.toString());
					ToastUtil.displayPromptInfo(GlueLineMidActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {
						list.add(pointGlueLineMidParam.get_id());
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
		glueMidLists = glueMidDao.findAllGlueLineMidParams();
		// popupListView->pupupview->title
		for (PointGlueLineMidParam pointGlueLineMidParam : glueMidLists) {

			if (currentTaskNum == pointGlueLineMidParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				setTitleInfos(glueMidLists, titleViewItem, currentTaskNum);
				setTitleInfos(glueMidLists, titleViewExtend, currentTaskNum);
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
		et_linemid_moveSpeed = (EditText) extendView
				.findViewById(R.id.et_linemid_moveSpeed);
		et_radius = (EditText) extendView.findViewById(R.id.et_radius);
		et_stopDisPrev = (EditText) extendView
				.findViewById(R.id.et_stopDisPrev);
		et_stopDisNext = (EditText) extendView
				.findViewById(R.id.et_stopDisNext);
		if ("".equals(et_linemid_moveSpeed.getText().toString())) {
			return false;
		} else if ("".equals(et_radius.getText().toString())) {
			return false;
		} else if ("".equals(et_stopDisPrev.getText().toString())) {
			return false;
		} else if ("".equals(et_stopDisNext.getText().toString())) {
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
		et_linemid_moveSpeed = (EditText) extendView
				.findViewById(R.id.et_linemid_moveSpeed);
		switch_isOutGlue = (ToggleButton) extendView
				.findViewById(R.id.switch_isOutGlue);
		et_radius = (EditText) extendView.findViewById(R.id.et_radius);
		et_stopDisPrev = (EditText) extendView
				.findViewById(R.id.et_stopDisPrev);

		et_stopDisNext = (EditText) extendView
				.findViewById(R.id.et_stopDisNext);
		isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport1);
		isGluePort[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport2);
		isGluePort[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport3);
		isGluePort[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport4);
		isGluePort[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport5);
	}

	/**
	 * 将页面上的数据保存到PointGlueLineMidParam对象中
	 * 
	 * @param extendView
	 * 
	 * @return PointGlueLineMidParam
	 */
	private PointGlueLineMidParam getLineMid(View extendView) {
		glueMid = new PointGlueLineMidParam();
		et_linemid_moveSpeed = (EditText) extendView
				.findViewById(R.id.et_linemid_moveSpeed);
		switch_isOutGlue = (ToggleButton) extendView
				.findViewById(R.id.switch_isOutGlue);
		et_radius = (EditText) extendView.findViewById(R.id.et_radius);
		et_stopDisPrev = (EditText) extendView
				.findViewById(R.id.et_stopDisPrev);

		et_stopDisNext = (EditText) extendView
				.findViewById(R.id.et_stopDisNext);
		isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport1);
		isGluePort[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport2);
		isGluePort[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport3);
		isGluePort[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport4);
		isGluePort[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport5);
		try {
			moveSpeedInt = Integer.parseInt(et_linemid_moveSpeed.getText()
					.toString());
			if (moveSpeedInt == 0) {
				moveSpeedInt = 1;
			}
		} catch (NumberFormatException e) {
			moveSpeedInt = 1;
		}

		glueMid.setMoveSpeed(moveSpeedInt);
		glueMid.setRadius(Float.parseFloat(et_radius.getText().toString()));
		glueMid.setStopGlueDisPrev(Float.parseFloat(et_stopDisPrev.getText()
				.toString()));
		glueMid.setStopGLueDisNext(Float.parseFloat(et_stopDisNext.getText()
				.toString()));
		glueMid.setOutGlue(switch_isOutGlue.isChecked());

		glueBoolean[0] = isGluePort[0].isChecked();
		glueBoolean[1] = isGluePort[1].isChecked();
		glueBoolean[2] = isGluePort[2].isChecked();
		glueBoolean[3] = isGluePort[3].isChecked();
		glueBoolean[4] = isGluePort[4].isChecked();
		glueMid.setGluePort(glueBoolean);
		glueMid.set_id(currentTaskNum);

		return glueMid;
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
		point.setPointParam(glueMidDao.getPointGlueLineMidParam(mIndex));
		System.out.println("返回的Point为================》" + point);

		List<Map<Integer, PointGlueLineMidParam>> list = new ArrayList<Map<Integer, PointGlueLineMidParam>>();
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
