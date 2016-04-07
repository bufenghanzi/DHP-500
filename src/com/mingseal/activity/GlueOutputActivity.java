/**
 * 
 */
package com.mingseal.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mingseal.adapter.PointGlueInputAdapter;
import com.mingseal.adapter.PointGlueOutputAdapter;
import com.mingseal.communicate.Const;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.PointConfigParam.GlueInput;
import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.IOPort;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
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
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import static com.mingseal.data.param.PointConfigParam.GlueOutput;

/**
 * @author 商炎炳
 * 
 */
public class GlueOutputActivity extends Activity implements OnClickListener {
	private final static String TAG = "GlueOutputActivity";
	/**
	 * 标题栏的标题
	 */
	private TextView tv_title;
	/**
	 * IO口
	 */
	private ToggleButton[] ioSwitch;

	/**
	 * 输出IOSpinner
	 */
	private Spinner outputSpinner;

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

	private GlueOutputDao outputDao;
	private List<PointGlueOutputIOParam> outputIOLists;
	private PointGlueOutputIOParam outputIO;
	private PointGlueOutputAdapter mOutputAdapter;
	private boolean[] ioBoolean;
	private int param_id = 1;// / 选取的是几号方案
	/**
	 * @Fields goTimePrevInt: 动作前延时的int值
	 */
	private int goTimePrevInt = 0;
	/**
	 * @Fields goTimeNextInt: 动作后延时的int值
	 */
	private int goTimeNextInt = 0;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private Handler handler;
	private boolean flag = false;// 可以与用户交互，初始化完成标志
	/* =================== begin =================== */
	private HashMap<Integer, PointGlueOutputIOParam> update_id;// 修改的方案号集合
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
	 * @Fields et_output_goTimePrev: 动作前延时
	 */
	private EditText et_output_goTimePrev;
	/**
	 * @Fields et_output_goTimeNext: 动作后延时
	 */
	private EditText et_output_goTimeNext;
	private RelativeLayout rl_moren;
	private ImageView iv_add;
	private ImageView iv_moren;

	/* =================== end =================== */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_glue_output);
		update_id = new HashMap<>();
		intent = getIntent();
		point = intent
				.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
		mFlag = intent.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
		mType = intent.getIntExtra(MyPopWindowClickListener.TYPE_KEY, 0);
		defaultNum = SharePreferenceUtils.getParamNumberFromPref(
				GlueOutputActivity.this,
				SettingParam.DefaultNum.ParamGlueOutputNumber);
		outputDao = new GlueOutputDao(this);
		outputIOLists = outputDao.findAllGlueOutputParams();
		if (outputIOLists == null || outputIOLists.isEmpty()) {
			outputIO = new PointGlueOutputIOParam();
			outputIO.set_id(param_id);
			outputDao.insertGlueOutput(outputIO);
			// 插入主键id
		}
		outputIOLists = outputDao.findAllGlueOutputParams();
		// 初始化数组
		ioBoolean = new boolean[IOPort.IO_NO_ALL.ordinal()];
		popupViews = new ArrayList<>();
		initPicker();

		// // 初始化Handler,用来处理消息
		// handler = new Handler(GlueOutputActivity.this);
		// if (mType == 1) {
		// PointGlueOutputIOParam glueOutputIOParam = outputDao
		// .getOutPutPointByID(point.getPointParam().get_id());
		// param_id = outputDao.getOutputParamIDByParam(glueOutputIOParam);//
		// 传过来的方案的参数序列主键。
		// SetDateAndRefreshUI(glueOutputIOParam);
		// } else {
		// // 不为1的话，需要选定默认的第一个方案
		// PointGlueOutputIOParam defaultParam = outputIOLists.get(0);
		// param_id = outputDao.getOutputParamIDByParam(defaultParam);//
		// 默认的参数序列主键。
		// SetDateAndRefreshUI(defaultParam);
		// }
		// mOutputAdapter = new PointGlueOutputAdapter(this);
		// mOutputAdapter.setOutputIOParams(outputIOLists);
		// outputSpinner.setAdapter(mOutputAdapter);
		// // 如果为1的话，需要设置值
		// if (mType == 1) {
		// outputSpinner.setSelection(point.getPointParam().get_id() - 1);
		// mOutputAdapter.notifyDataSetChanged();
		// }

		// outputSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> parent, View view,
		// int position, long id) {
		// PointGlueOutputIOParam param = mOutputAdapter.getItem(position);
		// et_output_goTimePrev.setText(param.getGoTimePrev() + "");
		// et_output_goTimeNext.setText(param.getGoTimeNext() + "");
		//
		// ioSwitch[0].setChecked(param.getInputPort()[0]);
		// ioSwitch[1].setChecked(param.getInputPort()[1]);
		// ioSwitch[2].setChecked(param.getInputPort()[2]);
		// ioSwitch[3].setChecked(param.getInputPort()[3]);
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

	private void UpdateInfos(PointGlueOutputIOParam glueOutputIOParam) {
		if (glueOutputIOParam == null) {
			et_output_goTimePrev.setText("");
			et_output_goTimeNext.setText("");

		} else {
			et_output_goTimePrev
					.setText(glueOutputIOParam.getGoTimePrev() + "");
			et_output_goTimeNext
					.setText(glueOutputIOParam.getGoTimeNext() + "");

			ioSwitch[0].setChecked(glueOutputIOParam.getInputPort()[0]);
			ioSwitch[1].setChecked(glueOutputIOParam.getInputPort()[1]);
			ioSwitch[2].setChecked(glueOutputIOParam.getInputPort()[2]);
			ioSwitch[3].setChecked(glueOutputIOParam.getInputPort()[3]);
			ioSwitch[4].setChecked(glueOutputIOParam.getInputPort()[4]);
			ioSwitch[5].setChecked(glueOutputIOParam.getInputPort()[5]);
			ioSwitch[6].setChecked(glueOutputIOParam.getInputPort()[6]);
			ioSwitch[7].setChecked(glueOutputIOParam.getInputPort()[7]);
			ioSwitch[8].setChecked(glueOutputIOParam.getInputPort()[8]);
			ioSwitch[9].setChecked(glueOutputIOParam.getInputPort()[9]);
			ioSwitch[10].setChecked(glueOutputIOParam.getInputPort()[10]);
			ioSwitch[11].setChecked(glueOutputIOParam.getInputPort()[11]);
		}
	}

	/**
	 * 加载组件，并设置NumberPicker的最大最小值
	 */
	private void initPicker() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources()
				.getString(R.string.activity_glue_output));
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
					outputIOLists = outputDao.findAllGlueOutputParams();
					textView.setTextSize(30);
					if (p == 1) {// 方案列表第一位对应一号方案
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 2) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 3) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 4) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 5) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 6) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 7) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 8) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 9) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
										.toString());
							}
						}
					} else if (p == 10) {
						for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
							if (p == pointGlueOutputIOParam.get_id()) {
								textView.setText(pointGlueOutputIOParam
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
								R.layout.glue_output_extend_view, null);
						int size = outputIOLists.size();
						while (size > 0) {
							size--;
							if (p == 1) {// 方案列表第一位对应一号方案
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 2) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 3) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 4) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 5) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 6) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 7) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 8) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 9) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
									}
								}
							} else if (p == 10) {
								initView(extendView);
								for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
									if (p == pointGlueOutputIOParam.get_id()) {
										UpdateInfos(pointGlueOutputIOParam);
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
					et_output_goTimePrev = (EditText) extendView
							.findViewById(R.id.et_output_goTimePrev);
					et_output_goTimeNext = (EditText) extendView
							.findViewById(R.id.et_output_goTimeNext);
					ioSwitch = new ToggleButton[GWOutPort.USER_O_NO_ALL
							.ordinal()];
					ioSwitch[0] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport1);
					ioSwitch[1] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport2);
					ioSwitch[2] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport3);
					ioSwitch[3] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport4);
					ioSwitch[4] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport5);
					ioSwitch[5] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport6);
					ioSwitch[6] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport7);
					ioSwitch[7] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport8);
					ioSwitch[8] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport9);
					ioSwitch[9] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport10);
					ioSwitch[10] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport11);
					ioSwitch[11] = (ToggleButton) extendView
							.findViewById(R.id.switch_glueport12);

					// 设置动作前延时的最大最小值
					et_output_goTimePrev
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueOutput.GoTimePrevMax,
									GlueOutput.GlueOutputMin,
									et_output_goTimePrev));
					et_output_goTimePrev
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueOutput.GoTimePrevMax,
									GlueOutput.GlueOutputMin,
									et_output_goTimePrev));
					et_output_goTimePrev.setSelectAllOnFocus(true);

					// 设置动作后延时的最大最小值
					et_output_goTimeNext
							.addTextChangedListener(new MaxMinEditWatcher(
									GlueOutput.GoTimeNextMax,
									GlueOutput.GlueOutputMin,
									et_output_goTimeNext));
					et_output_goTimeNext
							.setOnFocusChangeListener(new MaxMinFocusChangeListener(
									GlueOutput.GoTimeNextMax,
									GlueOutput.GlueOutputMin,
									et_output_goTimeNext));
					et_output_goTimeNext.setSelectAllOnFocus(true);
					// et_input_goTimePrev = (EditText) extendView
					// .findViewById(R.id.et_input_goTimePrev);
					// et_input_goTimeNext = (EditText) extendView
					// .findViewById(R.id.et_input_goTimeNext);
					//
					// ioSwitch = new ToggleButton[GWOutPort.USER_O_NO_ALL
					// .ordinal()];
					// ioSwitch[0] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport1);
					// ioSwitch[1] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport2);
					// ioSwitch[2] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport3);
					// ioSwitch[3] = (ToggleButton) extendView
					// .findViewById(R.id.switch_glueport4);
					//
					// // 设置动作前延时的最大最小值
					// et_input_goTimePrev
					// .addTextChangedListener(new MaxMinEditWatcher(
					// GlueInput.GoTimePrevMax,
					// GlueInput.GlueInputMin, et_input_goTimePrev));
					// et_input_goTimePrev
					// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
					// GlueInput.GoTimePrevMax,
					// GlueInput.GlueInputMin, et_input_goTimePrev));
					// et_input_goTimePrev.setSelectAllOnFocus(true);
					//
					// // 设置动作后延时的最大最小值
					// et_input_goTimeNext
					// .addTextChangedListener(new MaxMinEditWatcher(
					// GlueInput.GoTimeNextMax,
					// GlueInput.GlueInputMin, et_input_goTimeNext));
					// et_input_goTimeNext
					// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
					// GlueInput.GoTimeNextMax,
					// GlueInput.GlueInputMin, et_input_goTimeNext));
					// et_input_goTimeNext.setSelectAllOnFocus(true);
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
											GlueOutputActivity.this,
											SettingParam.DefaultNum.ParamGlueOutputNumber,
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
		for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
			list.add(pointGlueOutputIOParam.get_id());
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
		//
		// et_output_goTimePrev = (EditText)
		// findViewById(R.id.et_output_goTimePrev);
		// et_output_goTimeNext = (EditText)
		// findViewById(R.id.et_output_goTimeNext);
		// ioSwitch = new Switch[IOPort.IO_NO_ALL.ordinal()];
		// ioSwitch[0] = (Switch) findViewById(R.id.switch_glueport1);
		// ioSwitch[1] = (Switch) findViewById(R.id.switch_glueport2);
		// ioSwitch[2] = (Switch) findViewById(R.id.switch_glueport3);
		// ioSwitch[3] = (Switch) findViewById(R.id.switch_glueport4);
		// /* =================== begin =================== */
		// ioSwitch[4] = (Switch) findViewById(R.id.switch_glueport5);
		// ioSwitch[5] = (Switch) findViewById(R.id.switch_glueport6);
		// ioSwitch[6] = (Switch) findViewById(R.id.switch_glueport7);
		// ioSwitch[7] = (Switch) findViewById(R.id.switch_glueport8);
		// ioSwitch[8] = (Switch) findViewById(R.id.switch_glueport9);
		// ioSwitch[9] = (Switch) findViewById(R.id.switch_glueport10);
		// ioSwitch[10] = (Switch) findViewById(R.id.switch_glueport11);
		// ioSwitch[11] = (Switch) findViewById(R.id.switch_glueport12);
		// tv_num = (TextView) findViewById(R.id.item_num);
		// tv_goTimePrev = (TextView) findViewById(R.id.item_goTimePrev);
		// tv_goTimeNext = (TextView) findViewById(R.id.item_goTimeNext);
		// // 初始化界面组件
		// plan = (LinearLayout) findViewById(R.id.tv_plan);
		// /* =================== end =================== */
		// rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		//
		// // 设置动作前延时的最大最小值
		// et_output_goTimePrev.addTextChangedListener(new MaxMinEditWatcher(
		// GlueOutput.GoTimePrevMax, GlueOutput.GlueOutputMin,
		// et_output_goTimePrev));
		// et_output_goTimePrev
		// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
		// GlueOutput.GoTimePrevMax, GlueOutput.GlueOutputMin,
		// et_output_goTimePrev));
		// et_output_goTimePrev.setSelectAllOnFocus(true);
		//
		// // 设置动作后延时的最大最小值
		// et_output_goTimeNext.addTextChangedListener(new MaxMinEditWatcher(
		// GlueOutput.GoTimeNextMax, GlueOutput.GlueOutputMin,
		// et_output_goTimeNext));
		// et_output_goTimeNext
		// .setOnFocusChangeListener(new MaxMinFocusChangeListener(
		// GlueOutput.GoTimeNextMax, GlueOutput.GlueOutputMin,
		// et_output_goTimeNext));
		// et_output_goTimeNext.setSelectAllOnFocus(true);
		//
		// tv_title.setText(getResources()
		// .getString(R.string.activity_glue_output));
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
		outputIOLists = outputDao.findAllGlueOutputParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
			list.add(pointGlueOutputIOParam.get_id());
		}
		System.out.println("存放主键id的集合---->" + list);
		System.out.println("当前选择的方案号---->" + currentTaskNum);
		System.out.println("list是否存在------------》"
				+ list.contains(currentTaskNum));
		if (list.contains(currentTaskNum)) {
			// 已经保存在数据库中的数据
			for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
				if (currentTaskNum == pointGlueOutputIOParam.get_id()) {
					View extendView = popupListView.getItemViews()
							.get(currentClickNum).getExtendView();
					initView(extendView);
					UpdateInfos(pointGlueOutputIOParam);
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
		outputIOLists = outputDao.findAllGlueOutputParams();
		ArrayList<Integer> list = new ArrayList<>();
		for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
			list.add(pointGlueOutputIOParam.get_id());
		}
		// 判空
		isOk = isEditClean(extendView);
		if (isOk) {

			PointGlueOutputIOParam upOutputIOParam = getOutputParam(extendView);
			if (outputIOLists.contains(upOutputIOParam)) {
				// 默认已经存在的方案但是不能创建方案只能改变默认方案号

				if (list.contains(currentTaskNum)) {
					isExist = true;
				}
				// 保存的方案已经存在但不是当前编辑的方案
				if (currentTaskNum != outputIOLists.get(
						outputIOLists.indexOf(upOutputIOParam)).get_id()) {
					ToastUtil.displayPromptInfo(GlueOutputActivity.this,
							getResources()
									.getString(R.string.task_is_exist_yes));
				}
			} else {
				for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
					if (currentTaskNum == pointGlueOutputIOParam.get_id()) {// 说明之前插入过
						flag = true;
					}
				}
				if (flag) {
					// 更新数据
					int rowid = outputDao.upDateGlueOutput(upOutputIOParam);
					// System.out.println("影响的行数"+rowid);
					update_id.put(upOutputIOParam.get_id(), upOutputIOParam);
					// mPMap.map.put(upglueAlone.get_id(), upglueAlone);
					System.out.println("修改的方案号为：" + upOutputIOParam.get_id());
					// System.out.println(glueAloneDao.getPointGlueAloneParamById(currentTaskNum).toString());
				} else {
					// 插入一条数据
					long rowid = outputDao.insertGlueOutput(upOutputIOParam);
					firstExist = true;
					outputIOLists = outputDao.findAllGlueOutputParams();
					Log.i(TAG, "保存之后新方案-->" + outputIOLists.toString());
					ToastUtil.displayPromptInfo(GlueOutputActivity.this,
							getResources().getString(R.string.save_success));
					list.clear();
					for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
						list.add(pointGlueOutputIOParam.get_id());
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
		outputIOLists = outputDao.findAllGlueOutputParams();
		// popupListView->pupupview->title
		for (PointGlueOutputIOParam pointGlueOutputIOParam : outputIOLists) {
			if (currentTaskNum == pointGlueOutputIOParam.get_id()) {
				// 需要设置两个view，因为view内容相同但是parent不同
				View titleViewItem = popupListView.getItemViews()
						.get(currentClickNum).getPopupView();
				View titleViewExtend = popupListView.getItemViews()
						.get(currentClickNum).getExtendPopupView();
				TextView textViewItem = (TextView) titleViewItem
						.findViewById(R.id.title);
				TextView textViewExtend = (TextView) titleViewExtend
						.findViewById(R.id.title);
				textViewItem.setText(pointGlueOutputIOParam.toString());
				textViewExtend.setText(pointGlueOutputIOParam.toString());
			}
		}
	}
	private boolean isEditClean(View extendView) {
		et_output_goTimePrev = (EditText) extendView
				.findViewById(R.id.et_output_goTimePrev);
		et_output_goTimeNext = (EditText) extendView
				.findViewById(R.id.et_output_goTimeNext);
		if ("".equals(et_output_goTimeNext.getText().toString())) {
			return false;
		} else if ("".equals(et_output_goTimePrev.getText().toString())) {
			return false;
		}
		return true;
	}

	protected void initView(View extendView) {
		et_output_goTimePrev = (EditText) extendView
				.findViewById(R.id.et_output_goTimePrev);
		et_output_goTimeNext = (EditText) extendView
				.findViewById(R.id.et_output_goTimeNext);
		ioSwitch = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		ioSwitch[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport1);
		ioSwitch[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport2);
		ioSwitch[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport3);
		ioSwitch[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport4);
		ioSwitch[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport5);
		ioSwitch[5] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport6);
		ioSwitch[6] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport7);
		ioSwitch[7] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport8);
		ioSwitch[8] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport9);
		ioSwitch[9] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport10);
		ioSwitch[10] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport11);
		ioSwitch[11] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport12);
		// rl_moren = (RelativeLayout) findViewById(R.id.rl_moren);
		// iv_add = (ImageView) findViewById(R.id.iv_add);
		// rl_save = (RelativeLayout) findViewById(R.id.rl_save);
		// iv_moren = (ImageView) findViewById(R.id.iv_moren);
	}

	/**
	 * 将页面上的数据保存到PointGlueOutputIOParam对象中
	 * 
	 * @param extendView
	 * 
	 * @return PointGlueOutputIOParam
	 */
	private PointGlueOutputIOParam getOutputParam(View extendView) {
		outputIO = new PointGlueOutputIOParam();
		et_output_goTimePrev = (EditText) extendView
				.findViewById(R.id.et_output_goTimePrev);
		et_output_goTimeNext = (EditText) extendView
				.findViewById(R.id.et_output_goTimeNext);
		ioSwitch = new ToggleButton[GWOutPort.USER_O_NO_ALL.ordinal()];
		ioSwitch[0] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport1);
		ioSwitch[1] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport2);
		ioSwitch[2] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport3);
		ioSwitch[3] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport4);
		ioSwitch[4] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport5);
		ioSwitch[5] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport6);
		ioSwitch[6] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport7);
		ioSwitch[7] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport8);
		ioSwitch[8] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport9);
		ioSwitch[9] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport10);
		ioSwitch[10] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport11);
		ioSwitch[11] = (ToggleButton) extendView
				.findViewById(R.id.switch_glueport12);
		try {
			goTimePrevInt = Integer.parseInt(et_output_goTimePrev.getText()
					.toString());
		} catch (NumberFormatException e) {
			goTimePrevInt = 0;
		}
		try {
			goTimeNextInt = Integer.parseInt(et_output_goTimeNext.getText()
					.toString());
		} catch (NumberFormatException e) {
			goTimeNextInt = 0;
		}
		outputIO.setGoTimePrev(goTimePrevInt);
		outputIO.setGoTimeNext(goTimeNextInt);
		ioBoolean[0] = ioSwitch[0].isChecked();
		ioBoolean[1] = ioSwitch[1].isChecked();
		ioBoolean[2] = ioSwitch[2].isChecked();
		ioBoolean[3] = ioSwitch[3].isChecked();
		ioBoolean[4] = ioSwitch[4].isChecked();
		ioBoolean[5] = ioSwitch[5].isChecked();
		ioBoolean[6] = ioSwitch[6].isChecked();
		ioBoolean[7] = ioSwitch[7].isChecked();
		ioBoolean[8] = ioSwitch[8].isChecked();
		ioBoolean[9] = ioSwitch[9].isChecked();
		ioBoolean[10] = ioSwitch[10].isChecked();
		ioBoolean[11] = ioSwitch[11].isChecked();
		outputIO.setInputPort(ioBoolean);
		outputIO.set_id(currentTaskNum);
		return outputIO;
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
		point.setPointParam(outputDao.getOutPutPointByID(mIndex));
		System.out.println("返回的Point为================》" + point);

		List<Map<Integer, PointGlueOutputIOParam>> list = new ArrayList<Map<Integer, PointGlueOutputIOParam>>();
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
