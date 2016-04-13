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
import android.graphics.Paint;
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
	private TextView title_clearGlue;
    private TextView title_et_clear_clearGlue;
    private TextView activity_ms;
    private TextView activity_fenghao;
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
			PopupView popupView = new PopupView(this, R.layout.popup_view_item_glue_clear) {

				@Override
				public void setViewsElements(View view) {
					pointClearLists = glueClearDao.findAllGlueClearParams();
					ImageView title_num = (ImageView) view
							.findViewById(R.id.title_num);
					if (p == 1) {// 方案列表第一位对应一号方案
						title_num.setImageResource(R.drawable.green1);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 2) {
						title_num.setImageResource(R.drawable.green2);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 3) {
						title_num.setImageResource(R.drawable.green3);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 4) {
						title_num.setImageResource(R.drawable.green4);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 5) {
						title_num.setImageResource(R.drawable.green5);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 6) {
						title_num.setImageResource(R.drawable.green6);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 7) {
						title_num.setImageResource(R.drawable.green7);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 8) {
						title_num.setImageResource(R.drawable.green8);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 9) {
						title_num.setImageResource(R.drawable.green9);
						setTitleInfos(pointClearLists, view, p);
					} else if (p == 10) {
						title_num.setImageResource(R.drawable.green10);
						setTitleInfos(pointClearLists, view, p);
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

	}
	protected void setTitleInfos(List<PointGlueClearParam> pointClearLists,
			View view, int p) {
		title_clearGlue = (TextView) view.findViewById(R.id.title_clearGlue);
        title_et_clear_clearGlue = (TextView) view.findViewById(R.id.title_et_clear_clearGlue);
        activity_ms = (TextView) view.findViewById(R.id.activity_ms);
        activity_fenghao = (TextView) view.findViewById(R.id.activity_fenghao);
        
        for (PointGlueClearParam pointGlueClearParam : pointClearLists) {
			if (p == pointGlueClearParam.get_id()) {
				activity_ms.setText(getResources().getString(
						R.string.activity_ms));
				activity_fenghao.setText(getResources().getString(
						R.string.activity_fenghao)
						+ " ");
				title_clearGlue.setText(getResources().getString(
						R.string.activity_glue_clearGlueTime)
						+ " ");
				
				title_et_clear_clearGlue.getPaint().setFlags(
						Paint.UNDERLINE_TEXT_FLAG); // 下划线
				title_et_clear_clearGlue.getPaint()
						.setAntiAlias(true); // 抗锯齿

				title_et_clear_clearGlue.setText(pointGlueClearParam
						.getClearGlueTime() + "");
			}
		}
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
				setTitleInfos(pointClearLists, titleViewItem, currentTaskNum);
				setTitleInfos(pointClearLists, titleViewExtend, currentTaskNum);
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
