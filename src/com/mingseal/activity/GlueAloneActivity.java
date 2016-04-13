package com.mingseal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mingseal.data.dao.GlueAloneDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.ui.PopupListView;
import com.mingseal.ui.PopupListView.OnClickPositionChanged;
import com.mingseal.ui.PopupListView.OnZoomInChanged;
import com.mingseal.ui.PopupView;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import static com.mingseal.data.param.PointConfigParam.GlueAlone;

/**
 * @author 商炎炳
 * @description 点胶独立点
 */
public class GlueAloneActivity extends Activity implements OnClickListener {

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

	private ToggleButton isOutGlueSwitch;// 是否出胶
	private ToggleButton isPause;// 是否暂停
	private ToggleButton[] isGluePort;// 点胶口

	private RelativeLayout rl_back;// 返回上级的按钮

	private List<PointGlueAloneParam> glueAloneLists;// 保存的方案,用来维护从数据库中读出来的方案列表的编号
	private PointGlueAloneParam glueAlone;

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
	 * @Fields isNull: 判断数据库中是否有次方案，默认没有
	 */
	private boolean flag = false;
	PopupListView popupListView;
	ArrayList<PopupView> popupViews;
	int actionBarHeight;
	int p = 0;
	View extendView;
	private TextView mMorenTextView;
	/**
	 * 当前任务号
	 */
	private int currentTaskNum;
	private int currentClickNum;// 当前点击的序号
	// Content View Elements

	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private RelativeLayout rl_save;
	private ImageView iv_moren;
	private int dianjiao;
	private int defaultNum = 1;// 默认号
	private boolean isOk;
	private boolean isExist = false;// 是否存在
	private boolean firstExist = false;// 是否存在
	private int mIndex;// 对应方案号
	private HashMap<Integer, PointGlueAloneParam> update_id;// 修改的方案号集合
	String[] GluePort;
	private TextView title_et_dianjiao;
	private TextView title_et_tingjiao;
	private TextView title_et_upHeight;
	private TextView title_et_isOutGlue;
	private TextView title_et_isPause;
	private TextView title_et_glue_port;
	    private TextView title_dianjiao;
	    private TextView activity_ms;
	    private TextView activity_fenghao;
	    private TextView title_alone_stopGlueTime;
	    private TextView activity_second_ms;
	    private TextView activity_second_fenghao;
	    private TextView title_alone_upHeight;
	    private TextView activity_mm;
	    private TextView activity_third_fenghao;
	    private TextView title_activity_glue_alone_isOutGlue;
	    private TextView activity_four_fenghao;
	    private TextView title_activity_glue_alone_isPause;
	    private TextView activity_five_fenghao;
	    private TextView activity_glue_port;
	// End Of Content View Elements

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_glue_alone);
		update_id = new HashMap<>();
		intent = getIntent();
		// point携带的参数方案[_id=1, pointType=POINT_GLUE_FACE_START]
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		Log.d(TAG, point.toString() + " FLAG:" + mFlag);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueAloneActivity.this,
				SettingParam.DefaultNum.ParamGlueAloneNumber);
		glueAloneDao = new GlueAloneDao(GlueAloneActivity.this);
		glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		if (glueAloneLists == null || glueAloneLists.isEmpty()) {
			glueAlone = new PointGlueAloneParam();
			// 插入主键id
			glueAlone.set_id(param_id);
			glueAloneDao.insertGlueAlone(glueAlone);
		}
		glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		// 初始化
		gluePortBoolean = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		GluePort = new String[5];
		popupViews = new ArrayList<>();
		initPicker();
	}

	/**
	 * @Title initPicker
	 * @Description 初始化视图界面，创建10个popupView
	 * @author wj
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_alone));
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

				private int mDotGlueTime;

				@Override
				public void setViewsElements(View view) {
					// TextView textView = (TextView) view
					// .findViewById(R.id.title);
					ImageView title_num = (ImageView) view
							.findViewById(R.id.title_num);

					glueAloneLists = glueAloneDao.findAllGlueAloneParams();
					if (p == 1) {// 方案列表第一位对应一号方案
						title_num.setImageResource(R.drawable.green1);
						setTitleInfos(glueAloneLists, view, p);

					} else if (p == 2) {
						title_num.setImageResource(R.drawable.green2);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 3) {
						title_num.setImageResource(R.drawable.green3);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 4) {
						title_num.setImageResource(R.drawable.green4);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 5) {
						title_num.setImageResource(R.drawable.green5);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 6) {
						title_num.setImageResource(R.drawable.green6);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 7) {
						title_num.setImageResource(R.drawable.green7);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 8) {
						title_num.setImageResource(R.drawable.green8);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 9) {
						title_num.setImageResource(R.drawable.green9);
						setTitleInfos(glueAloneLists, view, p);
					} else if (p == 10) {
						title_num.setImageResource(R.drawable.green10);
						setTitleInfos(glueAloneLists, view, p);
					}
				}
				@Override
				public View setExtendView(View view) {
					if (view == null) {
						extendView = LayoutInflater.from(
								getApplicationContext()).inflate(
								R.layout.extend_view, null);
						int size = glueAloneLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
									if (p == pointGlueAloneParam.get_id()) {
										UpdateInfos(pointGlueAloneParam);
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
					et_alone_dianjiao = (EditText) extendView
							.findViewById(R.id.et_alone_dianjiao);
					isOutGlueSwitch = (ToggleButton) extendView
							.findViewById(R.id.switch_chujiao);
					et_alone_tingjiao = (EditText) extendView
							.findViewById(R.id.et_alone_tingjiao);
					isPause = (ToggleButton) extendView
							.findViewById(R.id.switch_tingjiao);
					et_alone_upHeight = (EditText) extendView
							.findViewById(R.id.et_alone_upheight);

					isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL
							.ordinal()];
					isGluePort[0] = (ToggleButton) extendView
							.findViewById(R.id.switch_dianjiaokou1);
					isGluePort[1] = (ToggleButton) extendView
							.findViewById(R.id.switch_dianjiaokou2);
					isGluePort[2] = (ToggleButton) extendView
							.findViewById(R.id.switch_dianjiaokou3);
					isGluePort[3] = (ToggleButton) extendView
							.findViewById(R.id.switch_dianjiaokou4);
					isGluePort[4] = (ToggleButton) extendView
							.findViewById(R.id.switch_dianjiaokou5);
					// 设置最大最小值

					// 设置最大最小值
					et_alone_dianjiao
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueAlone.DotGlueTimeMAX,
									GlueAlone.GlueAloneMIN, et_alone_dianjiao));
					et_alone_tingjiao
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueAlone.StopGlueTimeMAX,
									GlueAlone.GlueAloneMIN, et_alone_tingjiao));
					et_alone_upHeight
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueAlone.UpHeightMAX,
									GlueAlone.GlueAloneMIN, et_alone_upHeight));

					et_alone_dianjiao
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueAlone.DotGlueTimeMAX,
									GlueAlone.GlueAloneMIN, et_alone_dianjiao));
					et_alone_tingjiao
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueAlone.StopGlueTimeMAX,
									GlueAlone.GlueAloneMIN, et_alone_tingjiao));
					et_alone_upHeight
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueAlone.UpHeightMAX,
									GlueAlone.GlueAloneMIN, et_alone_upHeight));

					rl_moren = (RelativeLayout) extendView
							.findViewById(R.id.rl_moren);
					iv_add = (ImageView) extendView.findViewById(R.id.iv_add);
					rl_save = (RelativeLayout) extendView
							.findViewById(R.id.rl_save);// 保存按钮
					iv_moren = (ImageView) extendView
							.findViewById(R.id.iv_moren);// 默认按钮
					rl_moren.setOnClickListener(this);
					rl_save.setOnClickListener(this);
					et_alone_dianjiao.setSelectAllOnFocus(true);
					et_alone_tingjiao.setSelectAllOnFocus(true);
					et_alone_upHeight.setSelectAllOnFocus(true);
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
											GlueAloneActivity.this,
											SettingParam.DefaultNum.ParamGlueAloneNumber,
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
		for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
			list.add(pointGlueAloneParam.get_id());
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

	protected void setTitleInfos(List<PointGlueAloneParam> glueAloneLists,
			View view, int p) {
		// TODO Auto-generated method stub
		title_et_dianjiao = (TextView) view
				.findViewById(R.id.title_et_dianjiao);
		title_et_tingjiao = (TextView) view
				.findViewById(R.id.title_et_tingjiao);
		title_et_upHeight = (TextView) view
				.findViewById(R.id.title_et_upHeight);
		title_et_isOutGlue = (TextView) view
				.findViewById(R.id.title_et_isOutGlue);
		title_et_isPause = (TextView) view.findViewById(R.id.title_et_isPause);
		title_et_glue_port = (TextView) view
				.findViewById(R.id.title_et_glue_port);
		/*=================== begin ===================*/
		title_dianjiao = (TextView) view.findViewById(R.id.title_dianjiao);
        activity_ms = (TextView) view.findViewById(R.id.activity_ms);
        activity_fenghao = (TextView) view.findViewById(R.id.activity_fenghao);
        title_alone_stopGlueTime = (TextView) view.findViewById(R.id.title_alone_stopGlueTime);
        activity_second_ms = (TextView) view.findViewById(R.id.activity_second_ms);
        activity_second_fenghao = (TextView) view.findViewById(R.id.activity_second_fenghao);
        title_alone_upHeight = (TextView) view.findViewById(R.id.title_alone_upHeight);
        activity_mm = (TextView) view.findViewById(R.id.activity_mm);
        activity_third_fenghao = (TextView) view.findViewById(R.id.activity_third_fenghao);
        title_activity_glue_alone_isOutGlue = (TextView) view.findViewById(R.id.title_activity_glue_alone_isOutGlue);
        activity_four_fenghao = (TextView) view.findViewById(R.id.activity_four_fenghao);
        title_activity_glue_alone_isPause = (TextView) view.findViewById(R.id.title_activity_glue_alone_isPause);
        activity_five_fenghao = (TextView) view.findViewById(R.id.activity_five_fenghao);
        activity_glue_port = (TextView) view.findViewById(R.id.activity_glue_port);
		/*===================  end  ===================*/
		for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
			if (p == pointGlueAloneParam.get_id()) {
				title_dianjiao.setText(getResources().getString(R.string.activity_glue_alone_dianjiaoyanshi)+" ");
				activity_ms.setText(getResources().getString(R.string.activity_ms));
				activity_fenghao.setText(getResources().getString(R.string.activity_fenghao)+" ");
				title_alone_stopGlueTime.setText(getResources().getString(R.string.activity_glue_alone_stopGlueTime)+" ");
				activity_second_ms.setText(getResources().getString(R.string.activity_ms));
				activity_second_fenghao.setText(getResources().getString(R.string.activity_fenghao)+" ");
				title_alone_upHeight.setText(getResources().getString(R.string.activity_glue_alone_upHeight)+" ");
				activity_mm.setText(getResources().getString(R.string.activity_mm));
				activity_third_fenghao.setText(getResources().getString(R.string.activity_fenghao)+" ");
				title_activity_glue_alone_isOutGlue.setText(getResources().getString(R.string.activity_glue_alone_isOutGlue)+" ");
				activity_four_fenghao.setText(getResources().getString(R.string.activity_fenghao)+" ");
				title_activity_glue_alone_isPause.setText(getResources().getString(R.string.activity_glue_alone_isPause)+" ");
				activity_five_fenghao.setText(getResources().getString(R.string.activity_fenghao)+" ");
				activity_glue_port.setText(getResources().getString(R.string.activity_glue_port)+" ");
				for (int j = 0; j < 5; j++) {
					if (pointGlueAloneParam.getGluePort()[j]) {
						GluePort[j] = "开";
					} else {
						GluePort[j] = "关";
					}
				}
			
			title_et_dianjiao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			title_et_dianjiao.getPaint().setAntiAlias(true); // 抗锯齿
			title_et_tingjiao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			title_et_tingjiao.getPaint().setAntiAlias(true); // 抗锯齿
			title_et_upHeight.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			title_et_upHeight.getPaint().setAntiAlias(true); // 抗锯齿
			title_et_isOutGlue.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			title_et_isOutGlue.getPaint().setAntiAlias(true); // 抗锯齿
			title_et_isPause.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			title_et_isPause.getPaint().setAntiAlias(true); // 抗锯齿
			title_et_glue_port.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
			title_et_glue_port.getPaint().setAntiAlias(true); // 抗锯齿

			title_et_dianjiao
					.setText(pointGlueAloneParam.getDotGlueTime() + "");
			title_et_tingjiao.setText(pointGlueAloneParam.getStopGlueTime()
					+ "");
			title_et_upHeight.setText(pointGlueAloneParam.getUpHeight() + "");
			if (pointGlueAloneParam.isOutGlue()) {
				title_et_isOutGlue.setText("是");
			} else {
				title_et_isOutGlue.setText("否");
			}
			if (pointGlueAloneParam.isPause()) {
				title_et_isPause.setText("是");
			} else {
				title_et_isPause.setText("否");
			}
			title_et_glue_port.setText(GluePort[0] + GluePort[1] + GluePort[2]
					+ GluePort[3] + GluePort[4]);
			}
		}
	}

	/**
	 * @Title SetDateAndRefreshUI
	 * @Description 打开extendview的时候设置界面内容，显示最新的方案数据而不是没有保存的数据,没有得到保存的方案
	 * @author wj
	 */
	protected void SetDateAndRefreshUI() {
		glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
			list.add(pointGlueAloneParam.get_id());
		}
		System.out.println("存放主键id的集合---->" + list);
		System.out.println("当前选择的方案号---->" + currentTaskNum);
		System.out.println("list是否存在------------》"
				+ list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			// 已经保存在数据库中的数据
			for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
				if (currentTaskNum == pointGlueAloneParam.get_id()) {
					View extendView = popupListView.getItemViews()
							.get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueAloneParam);
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

	/**
	 * @Title UpdateInfos
	 * @Description 更新extendView数据（保存的数据）
	 * @author wj
	 * @param pointGlueAloneParam
	 */
	private void UpdateInfos(PointGlueAloneParam pointGlueAloneParam) {
		if (pointGlueAloneParam == null) {
			et_alone_dianjiao.setText("");
			et_alone_tingjiao.setText("");
			et_alone_upHeight.setText("");

		} else {
			et_alone_dianjiao
					.setText(pointGlueAloneParam.getDotGlueTime() + "");
			et_alone_tingjiao.setText(pointGlueAloneParam.getStopGlueTime()
					+ "");
			et_alone_upHeight.setText(pointGlueAloneParam.getUpHeight() + "");

			isOutGlueSwitch.setChecked(pointGlueAloneParam.isOutGlue());
			isPause.setChecked(pointGlueAloneParam.isPause());

			isGluePort[0].setChecked(pointGlueAloneParam.getGluePort()[0]);
			isGluePort[1].setChecked(pointGlueAloneParam.getGluePort()[1]);
			isGluePort[2].setChecked(pointGlueAloneParam.getGluePort()[2]);
			isGluePort[3].setChecked(pointGlueAloneParam.getGluePort()[3]);
			isGluePort[4].setChecked(pointGlueAloneParam.getGluePort()[4]);
		}
	}

	/**
	 * @Title initView
	 * @Description 初始化当前extendView视图
	 * @author wj
	 * @param extendView
	 */
	private void initView(View extendView) {
		et_alone_dianjiao = (EditText) extendView
				.findViewById(R.id.et_alone_dianjiao);
		isOutGlueSwitch = (ToggleButton) extendView
				.findViewById(R.id.switch_chujiao);
		et_alone_tingjiao = (EditText) extendView
				.findViewById(R.id.et_alone_tingjiao);
		isPause = (ToggleButton) extendView.findViewById(R.id.switch_tingjiao);
		et_alone_upHeight = (EditText) extendView
				.findViewById(R.id.et_alone_upheight);

		isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou1);
		isGluePort[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou2);
		isGluePort[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou3);
		isGluePort[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou4);
		isGluePort[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou5);
	}

	/**
	 * @Title save
	 * @Description 保存信息到PointGlueAloneParam的一个对象中，并更新数据库数据
	 * @author wj
	 */
	protected void save() {
		View extendView = popupListView.getItemViews().get(currentClickNum)
				.getExtendView();
		glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
			list.add(pointGlueAloneParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueAloneParam upglueAlone = getGlueAlone(extendView);
			if (glueAloneLists.contains(upglueAlone)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号
				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != glueAloneLists.get(
						glueAloneLists.indexOf(upglueAlone)).get_id()) {
					ToastUtil.displayPromptInfo(GlueAloneActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {

				for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
					if (currentTaskNum == pointGlueAloneParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = glueAloneDao.upDateGlueAlone(upglueAlone);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upglueAlone.get_id(), upglueAlone);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upglueAlone.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = glueAloneDao.insertGlueAlone(upglueAlone);
					firstExist = true;
					glueAloneLists = glueAloneDao.findAllGlueAloneParams();
					Log.i(TAG, "保存之后新方案-->" + glueAloneLists.toString());
					ToastUtil.displayPromptInfo(GlueAloneActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {
						list.add(pointGlueAloneParam.get_id());
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
		glueAloneLists = glueAloneDao.findAllGlueAloneParams();
		// popupListView->pupupview->title
		for (PointGlueAloneParam pointGlueAloneParam : glueAloneLists) {

			if (currentTaskNum == pointGlueAloneParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				setTitleInfos(glueAloneLists, titleViewItem,currentTaskNum);
				setTitleInfos(glueAloneLists, titleViewExtend,currentTaskNum);
				// TextView textViewItem = (TextView) titleViewItem
				// .findViewById(R.id.title);
				// TextView textViewExtend = (TextView) titleViewExtend
				// .findViewById(R.id.title);
				// textViewExtend.setText(pointGlueAloneParam.toString());
				// for (int j = 0; j < 5; j++) {
				// if (pointGlueAloneParam.getGluePort()[j]) {
				// GluePort[j] = "开";
				// } else {
				// GluePort[j] = "关";
				// }
				// }
				// textViewItem.setText("点胶延时："
				// + pointGlueAloneParam.getDotGlueTime() + "ms,"
				// + "停胶延时：" + pointGlueAloneParam.getStopGlueTime()
				// + "ms," + "抬起高度：" + pointGlueAloneParam.getUpHeight()
				// + "mm," + "是否出胶：" + pointGlueAloneParam.isOutGlue()
				// + "," + "是否暂停：" + pointGlueAloneParam.isPause() + ","
				// + "点胶口：" + GluePort[0] + GluePort[1] + GluePort[2]
				// + GluePort[3] + GluePort[4]);
				// textViewExtend.setText("点胶延时："
				// + pointGlueAloneParam.getDotGlueTime() + "ms,"
				// + "停胶延时：" + pointGlueAloneParam.getStopGlueTime()
				// + "ms," + "抬起高度：" + pointGlueAloneParam.getUpHeight()
				// + "mm," + "是否出胶：" + pointGlueAloneParam.isOutGlue()
				// + "," + "是否暂停：" + pointGlueAloneParam.isPause() + ","
				// + "点胶口：" + GluePort[0] + GluePort[1] + GluePort[2]
				// + GluePort[3] + GluePort[4]);
			}
		}
	}

	/**
	 * @Title getGlueAlone
	 * @Description 将页面上显示的数据保存到PointGlueAloneParam的一个对象中
	 * @author wj
	 * @param extendView
	 *            具体内容
	 * @param Id
	 *            方案主键
	 * @return
	 */
	private PointGlueAloneParam getGlueAlone(View extendView) {
		glueAlone = new PointGlueAloneParam();
		et_alone_dianjiao = (EditText) extendView
				.findViewById(R.id.et_alone_dianjiao);
		isOutGlueSwitch = (ToggleButton) extendView
				.findViewById(R.id.switch_chujiao);
		et_alone_tingjiao = (EditText) extendView
				.findViewById(R.id.et_alone_tingjiao);
		isPause = (ToggleButton) extendView.findViewById(R.id.switch_tingjiao);
		et_alone_upHeight = (EditText) extendView
				.findViewById(R.id.et_alone_upheight);

		isGluePort = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		isGluePort[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou1);
		isGluePort[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou2);
		isGluePort[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou3);
		isGluePort[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou4);
		isGluePort[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_dianjiaokou5);

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

		glueAlone.set_id(currentTaskNum);// 主键与列表的方案号绑定

		return glueAlone;
	}

	/**
	 * @Title isEditClean
	 * @Description 判断输入框是否为空
	 * @author wj
	 * @param extendView
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditClean(View extendView) {
		et_alone_dianjiao = (EditText) extendView
				.findViewById(R.id.et_alone_dianjiao);
		et_alone_tingjiao = (EditText) extendView
				.findViewById(R.id.et_alone_tingjiao);
		et_alone_upHeight = (EditText) extendView
				.findViewById(R.id.et_alone_upheight);

		if ("".equals(et_alone_dianjiao.getText().toString())) {
			return false;
		} else if ("".equals(et_alone_tingjiao.getText().toString())) {
			return false;
		} else if ("".equals(et_alone_upHeight.getText().toString())) {
			return false;
		}
		return true;
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

	/**
	 * @Title complete
	 * @Description 最终完成返回
	 * @author wj
	 */
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
		point.setPointParam(glueAloneDao.getPointGlueAloneParamById(mIndex));
		System.out.println("返回的Point为================》" + point);

		List<Map<Integer, PointGlueAloneParam>> list = new ArrayList<Map<Integer, PointGlueAloneParam>>();
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
