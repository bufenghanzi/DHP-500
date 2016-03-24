/**
 * 
 */
package com.mingseal.activity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.mingseal.adapter.TestArrayAdapter;
import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.ArrayParam;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.SMatrix1_4;
import com.mingseal.dhp.R;
import com.mingseal.utils.ArrayArithmetic;
import com.mingseal.utils.FloatUtil;
import com.mingseal.utils.MoveUtils;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.WifiConnectTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 阵列
 * 
 * @author 商炎炳
 *
 */
public class GlueArrayActivity extends Activity implements OnClickListener {

//	private MyCircleView myCircleUp;
//	private MyCircleView myCircleDown;

	private TextView tv_title;
	private RelativeLayout rl_back;
	private RelativeLayout rl_title_speed;// 标题栏的速度布局
	private RelativeLayout rl_title_moshi;// 标题栏的模式布局
	private RelativeLayout rl_title_wifi_connecting;// wifi连接情况
	private ImageView image_speed;// 标题栏的速度
	private ImageView image_moshi;// 标题栏的模式

	/**
	 * 速度按钮
	 */
	private RelativeLayout rl_speed;
	/**
	 * 模式
	 */
	private RelativeLayout rl_moshi;
	/**
	 * 完成
	 */
	private RelativeLayout rl_complete;
	/**
	 * @Fields tv_speed: 速度的文本
	 */
	private TextView tv_speed;
	/**
	 * @Fields tv_moshi: 模式的文本
	 */
	private TextView tv_moshi;
	/**
	 * 行数
	 */
	private EditText rowEdit;
	/**
	 * 列数
	 */
	private EditText columnEdit;
	/**
	 * 阵列方式
	 */
	private Spinner arraySpinner;

	/**
	 * X轴方向偏移x
	 */
	private EditText et_x_offset_x;
	/**
	 * X轴方向偏移y
	 */
	private EditText et_x_offset_y;
	/**
	 * X轴方向偏移z
	 */
	private EditText et_x_offset_z;
	/**
	 * Y轴方向偏移x
	 */
	private EditText et_y_offset_x;
	/**
	 * Y轴方向偏移y
	 */
	private EditText et_y_offset_y;
	/**
	 * Y轴方向偏移z
	 */
	private EditText et_y_offset_z;
	/**
	 * 终点x坐标
	 */
	private EditText et_end_x;
	/**
	 * 终点y坐标
	 */
	private EditText et_end_y;
	/**
	 * 终点z坐标
	 */
	private EditText et_end_z;

	private LinearLayout linear_x;// X轴方向偏移的LineayLayout
	private LinearLayout linear_y;// Y轴方向偏移的LineayLayout
	private LinearLayout linear_e;// 终点坐标偏移的LineayLayout
	/**
	 * @Fields but_x_plus: X+
	 */
	private Button but_x_plus;
	/**
	 * @Fields but_x_minus: X-
	 */
	private Button but_x_minus;
	/**
	 * @Fields but_y_plus: Y+
	 */
	private Button but_y_plus;
	/**
	 * @Fields but_y_minus: Y-
	 */
	private Button but_y_minus;
	/**
	 * @Fields but_z_plus: Z+
	 */
	private Button but_z_plus;
	/**
	 * @Fields but_z_minus: Z-
	 */
	private Button but_z_minus;
	/**
	 * @Fields but_u_plus: U+
	 */
	private Button but_u_plus;
	/**
	 * @Fields but_u_minus: U-
	 */
	private Button but_u_minus;

	private final static int KEY_X_X = 0;
	private final static int KEY_X_Y = 1;
	private final static int KEY_X_Z = 2;
	private final static int KEY_Y_X = 3;
	private final static int KEY_Y_Y = 4;
	private final static int KEY_Y_Z = 5;
	private final static int KEY_E_X = 6;
	private final static int KEY_E_Y = 7;
	private final static int KEY_E_Z = 8;
	private final static int ROW = 9;
	private final static int COLUMN = 10;

	private ArrayAdapter adapterSpinner;
	private List<Point> points;// 接收从TaskActivity传递过来的数据
	/**
	 * 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	 */
	private String numberType;// 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	private Point point;

	private ArrayParam arrayParam;
	private SMatrix1_4 sMatrix_x;
	private SMatrix1_4 sMatrix_y;
	private SMatrix1_4 sMatrix_e;

	/**
	 * 从TaskActivity传过来的Intent
	 */
	private Intent intent;
	private static String TAG = "GlueArrayActivity";

	private String prompt = "";// 提示信息
	/**
	 * 判断当前的焦点在哪个输入框(0是x轴偏移,1是y轴偏移,2是终点坐标)
	 */
	private int selectFocus = 0;// 判断当前的焦点在哪个输入框(0是x轴偏移,1是y轴偏移,2是终点坐标)
	private int selectLastFocus = -1;// 用于判断焦点框是否变化,如果变化了的话,就需要先定位

	private int speedFlag = 0;// 判断点击了几次，三次一循环，分别为高速33,中速13,低速3
	private int speed;
	private int[] speedXYZ;// 单步的话保存x,y,z的坐标
	private SettingParam settingParam;// 任务设置参数
	/**
	 * @Fields m_nAxisNum : 机器轴数
	 */
	private int m_nAxisNum;
	/**
	 * 连续0(默认),单步是1
	 */
	private int modeFlag = 0;// 连续单步判断标志
	private RevHandler handler;
	private UserApplication userApplication;
	private ImageView iv_wifi_connecting;//wifi连接情况

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_array);

		userApplication = (UserApplication) getApplication();
		intent = getIntent();
		numberType = intent.getStringExtra(TaskActivity.KEY_NUMBER);
		if ("0".equals(numberType)) {
			points = userApplication.getPoints();
		} else if ("1".equals(numberType)) {
			points = intent.getParcelableArrayListExtra(TaskActivity.ARRAY_KEY);
		}
		// Log.d(TAG, "numberType:"+numberType+",长度:"+points.size());
		settingParam = SharePreferenceUtils.readFromSharedPreference(this);
		initView();
		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
		m_nAxisNum = RobotParam.INSTANCE.getM_nAxisNum();
		if (m_nAxisNum == 3) {
//			myCircleDown.setRow("");
			but_u_minus.setVisibility(View.INVISIBLE);
			but_u_plus.setVisibility(View.INVISIBLE);
		}else if(m_nAxisNum ==4){
			but_u_minus.setVisibility(View.VISIBLE);
			but_u_plus.setVisibility(View.VISIBLE);
		}

		// 初始化
		arrayParam = new ArrayParam();
		 //使用自定义的ArrayAdapter  
		adapterSpinner = new TestArrayAdapter(GlueArrayActivity.this,getResources().getStringArray(R.array.arrayMethods));  
//		// 将数组内容与ArrayAdapter连接起来
//		adapterSpinner = ArrayAdapter.createFromResource(this, R.array.arrayMethods,
//				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到Spinner中
		arraySpinner.setAdapter(adapterSpinner);
		// 添加事件监听
		arraySpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
		arraySpinner.setVisibility(View.VISIBLE);

		et_x_offset_x.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_x_offset_x, KEY_X_X));
		et_x_offset_y.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_x_offset_y, KEY_X_Y));
		et_x_offset_z.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_x_offset_z, KEY_X_Z));
		et_y_offset_x.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_y_offset_x, KEY_Y_X));
		et_y_offset_y.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_y_offset_y, KEY_Y_Y));
		et_y_offset_z.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_y_offset_z, KEY_Y_Z));
		et_end_x.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_end_x, KEY_E_X));
		et_end_y.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_end_y, KEY_E_Y));
		et_end_z.setOnEditorActionListener(new OnKeyEditorEnterListener(arrayParam, et_end_z, KEY_E_Z));

		et_x_offset_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_x_offset_x, KEY_X_X));
		et_x_offset_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_x_offset_y, KEY_X_Y));
		et_x_offset_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_x_offset_z, KEY_X_Z));
		et_y_offset_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_y_offset_x, KEY_Y_X));
		et_y_offset_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_y_offset_y, KEY_Y_Y));
		et_y_offset_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_y_offset_z, KEY_Y_Z));
		et_end_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_end_x, KEY_E_X));
		et_end_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_end_y, KEY_E_Y));
		et_end_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, et_end_z, KEY_E_Z));
		rowEdit.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, rowEdit, ROW));
		columnEdit.setOnFocusChangeListener(new OnKeyFocusChangeListener(arrayParam, columnEdit, COLUMN));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 判断Edittext输入框里面是否有数据,如果没有数据则置为0
	 * 
	 * @param edittext
	 * @return double数据
	 */
	private double getStringToDouble(EditText edittext) {
		String str = edittext.getText().toString();
		if (str.equals("")) {
			str = "0";
			edittext.setText(str);
		}
		double result = Double.parseDouble(str);
		return result;
	}

	/**
	 * 加载自定义控件
	 */
	private void initView() {
		but_x_plus = (Button) findViewById(R.id.nav_x_plus);
		but_x_minus = (Button) findViewById(R.id.nav_x_minus);
		but_y_plus = (Button) findViewById(R.id.nav_y_plus);
		but_y_minus = (Button) findViewById(R.id.nav_y_minus);
		but_z_plus = (Button) findViewById(R.id.nav_z_plus);
		but_z_minus = (Button) findViewById(R.id.nav_z_minus);
		but_u_plus = (Button) findViewById(R.id.nav_u_plus);
		but_u_minus = (Button) findViewById(R.id.nav_u_minus);
		
		MoveListener moveListener = new MoveListener();
		but_x_plus.setOnTouchListener(moveListener);
		but_x_minus.setOnTouchListener(moveListener);
		but_y_plus.setOnTouchListener(moveListener);
		but_y_minus.setOnTouchListener(moveListener);
		but_z_plus.setOnTouchListener(moveListener);
		but_z_minus.setOnTouchListener(moveListener);
		but_u_plus.setOnTouchListener(moveListener);
		but_u_minus.setOnTouchListener(moveListener);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_array));

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_title_speed = (RelativeLayout) findViewById(R.id.rl_title_speed);
		rl_title_moshi = (RelativeLayout) findViewById(R.id.rl_title_moshi);
		iv_wifi_connecting  =(ImageView) findViewById(R.id.iv_title_wifi_connecting);
		WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
		// 让RelativeLayout显示
		rl_title_speed.setVisibility(View.VISIBLE);
		rl_title_moshi.setVisibility(View.VISIBLE);
		image_speed = (ImageView) findViewById(R.id.iv_title_speed);
		image_moshi = (ImageView) findViewById(R.id.iv_title_moshi);
		// 设置初值
		image_speed.setBackgroundResource(R.drawable.icon_speed_high);
		image_moshi.setBackgroundResource(R.drawable.icon_step_serious);

		rowEdit = (EditText) findViewById(R.id.et_row);
		columnEdit = (EditText) findViewById(R.id.et_column);
		arraySpinner = (Spinner) findViewById(R.id.sp_array);
		rl_speed = (RelativeLayout) findViewById(R.id.rl_sudu);
		rl_moshi = (RelativeLayout) findViewById(R.id.rl_moshi);
		tv_speed = (TextView) rl_speed.findViewById(R.id.tv_array_speed);
		tv_moshi = (TextView) rl_moshi.findViewById(R.id.tv_array_moshi);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		et_x_offset_x = (EditText) findViewById(R.id.et_x_offset_x);
		et_x_offset_y = (EditText) findViewById(R.id.et_x_offset_y);
		et_x_offset_z = (EditText) findViewById(R.id.et_x_offset_z);
		et_y_offset_x = (EditText) findViewById(R.id.et_y_offset_x);
		et_y_offset_y = (EditText) findViewById(R.id.et_y_offset_y);
		et_y_offset_z = (EditText) findViewById(R.id.et_y_offset_z);
		et_end_x = (EditText) findViewById(R.id.et_end_x);
		et_end_y = (EditText) findViewById(R.id.et_end_y);
		et_end_z = (EditText) findViewById(R.id.et_end_z);

		// 点击全选
		rowEdit.setSelectAllOnFocus(true);
		columnEdit.setSelectAllOnFocus(true);
		et_x_offset_x.setSelectAllOnFocus(true);
		et_x_offset_y.setSelectAllOnFocus(true);
		et_x_offset_z.setSelectAllOnFocus(true);
		et_y_offset_x.setSelectAllOnFocus(true);
		et_y_offset_y.setSelectAllOnFocus(true);
		et_y_offset_z.setSelectAllOnFocus(true);
		et_end_x.setSelectAllOnFocus(true);
		et_end_y.setSelectAllOnFocus(true);
		et_end_z.setSelectAllOnFocus(true);

		linear_x = (LinearLayout) findViewById(R.id.array_lin_x);
		linear_y = (LinearLayout) findViewById(R.id.array_lin_y);
		linear_e = (LinearLayout) findViewById(R.id.array_lin_e);
		linear_x.setOnClickListener(this);
		linear_y.setOnClickListener(this);
		linear_e.setOnClickListener(this);

		linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
		linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
		linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));

		rl_back.setOnClickListener(this);
		rl_speed.setOnClickListener(this);
		rl_moshi.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

		speed = settingParam.getHighSpeed();
		speedXYZ = new int[3];
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();

	}
	
	/**
	 * @ClassName MoveListener
	 * @Description x,y,z,u移动
	 * @author 商炎炳
	 * @date 2016年1月28日 下午2:36:18
	 *
	 */
	private class MoveListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(selectLastFocus == selectFocus){
				//直接移动
				if(modeFlag == 0){
					//连续
					switch (v.getId()) {
					case R.id.nav_x_plus:// x+
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 0, 0, speed);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(0);
						}
						break;
					case R.id.nav_x_minus:// x-
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 0, 0, speed);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(0);
						}
						break;
					case R.id.nav_y_plus:// y+
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 0, 1, speed);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(1);
						}
						break;
					case R.id.nav_y_minus:// y-
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 0, 1, speed);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(1);
						}
						break;
					case R.id.nav_z_plus:// z+
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 0, 2, speed);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(2);
						}
						break;
					case R.id.nav_z_minus:// z-
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 0, 2, speed);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(2);
						}
						break;
					case R.id.nav_u_plus:// u+
						if (m_nAxisNum == 4) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(0, 0, 3, speed);
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(3);
							}
						}
						break;
					case R.id.nav_u_minus:// u-
						if (m_nAxisNum == 4) {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(1, 0, 3, speed);
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(3);
							}
						}
						break;
					}
				}else if(modeFlag ==1 ){
					//单步
					switch (v.getId()) {
					case R.id.nav_x_plus:// x+
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 1, 0, speedXYZ[0]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(0);
						}
						break;
					case R.id.nav_x_minus:// x-
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 1, 0, speedXYZ[0]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(0);
						}
						break;
					case R.id.nav_y_plus:// y+
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 1, 1, speedXYZ[1]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(1);
						}
						break;
					case R.id.nav_y_minus:// y-
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 1, 1, speedXYZ[1]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(1);
						}
						break;
					case R.id.nav_z_plus:// z+
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(0, 1, 2, speedXYZ[2]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(2);
						}
						break;
					case R.id.nav_z_minus:// z-
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							MoveUtils.move(1, 1, 2, speedXYZ[2]);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							MoveUtils.stop(2);
						}
					case R.id.nav_u_plus:// u+
						if(m_nAxisNum == 4){
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(0, 1, 3, speedXYZ[0]);
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(3);
							}
						}
						break;
					case R.id.nav_u_minus:// u-
						if(m_nAxisNum == 4){
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								MoveUtils.move(1, 1, 3, speedXYZ[0]);
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								MoveUtils.stop(3);
							}
						}
						break;
					}
				}
			}else{
				//定位
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					point = new Point(PointType.POINT_NULL);
					if (selectFocus == 0) {
						point.setX(RobotParam.INSTANCE.XJourney2Pulse(getStringToDouble(et_x_offset_x)));
						point.setY(RobotParam.INSTANCE.YJourney2Pulse(getStringToDouble(et_x_offset_y)));
						point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(getStringToDouble(et_x_offset_z)));
					} else if (selectFocus == 1) {
						point.setX(RobotParam.INSTANCE.XJourney2Pulse(getStringToDouble(et_y_offset_x)));
						point.setY(RobotParam.INSTANCE.YJourney2Pulse(getStringToDouble(et_y_offset_y)));
						point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(getStringToDouble(et_y_offset_z)));
					} else if (selectFocus == 2) {
						point.setX(RobotParam.INSTANCE.XJourney2Pulse(getStringToDouble(et_end_x)));
						point.setY(RobotParam.INSTANCE.YJourney2Pulse(getStringToDouble(et_end_y)));
						point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(getStringToDouble(et_end_z)));
					}
					MoveUtils.locationCoord(point);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					// 抬起的时候把设置上次选中的单选框
					selectLastFocus = selectFocus;
				}
			}
			return false;
		}
		
	}

	/**
	 * 自定义OnFocusChangeListene,失去焦点时，保存当前的内容
	 *
	 */
	private class OnKeyFocusChangeListener implements OnFocusChangeListener {

		private ArrayParam arrayParam;
		private EditText et;
		private int key;// 判断是哪一个
		private double value;

		/**
		 * 自定义OnFocusChangeListene,失去焦点时，保存当前的内容
		 * 
		 * @param arrayParam
		 *            阵列参数
		 * @param et
		 *            Edittext
		 * @param key
		 *            判断是哪一个
		 */
		public OnKeyFocusChangeListener(ArrayParam arrayParam, EditText et, int key) {
			super();
			this.arrayParam = arrayParam;
			this.et = et;
			this.key = key;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				// ((EditText) v).setCursorVisible(false);
				value = Double.parseDouble(et.getText().toString());
				switch (key) {
				case KEY_X_X:
					arrayParam.getMx().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
					break;
				case KEY_X_Y:
					arrayParam.getMx().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
					break;
				case KEY_X_Z:
					arrayParam.getMx().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
					break;
				case KEY_Y_X:
					arrayParam.getMy().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
					break;
				case KEY_Y_Y:
					arrayParam.getMy().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
					break;
				case KEY_Y_Z:
					arrayParam.getMy().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
					break;
				case KEY_E_X:
					arrayParam.getMe().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
					break;
				case KEY_E_Y:
					arrayParam.getMe().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
					break;
				case KEY_E_Z:
					arrayParam.getMe().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
					break;
				case ROW:
					arrayParam.setRow((int) value);
					break;
				case COLUMN:
					arrayParam.setCol((int) value);
					break;
				}
			}
		}

	}

	/**
	 * 自定义的OnEditorActionListener,软键盘输入回车，将数据保存到List集合中
	 *
	 */
	private class OnKeyEditorEnterListener implements OnEditorActionListener {

		private ArrayParam arrayParam;
		private EditText et;
		private int key;// 判断是哪一个
		private double value;

		/**
		 * 软键盘输入回车，将数据保存到ArrayParam中
		 * 
		 * @param arrayParam
		 *            阵列参数
		 * @param et
		 *            Edittext
		 * @param key
		 *            判断是哪一个Edittext
		 */
		public OnKeyEditorEnterListener(ArrayParam arrayParam, EditText et, int key) {
			this.arrayParam = arrayParam;
			this.et = et;
			this.key = key;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				value = Double.parseDouble(et.getText().toString());
				switch (key) {
				case KEY_X_X:
					arrayParam.getMx().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
					break;
				case KEY_X_Y:
					arrayParam.getMx().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
					break;
				case KEY_X_Z:
					arrayParam.getMx().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
					break;
				case KEY_Y_X:
					arrayParam.getMy().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
					break;
				case KEY_Y_Y:
					arrayParam.getMy().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
					break;
				case KEY_Y_Z:
					arrayParam.getMy().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
					break;
				case KEY_E_X:
					arrayParam.getMe().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
					break;
				case KEY_E_Y:
					arrayParam.getMe().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
					break;
				case KEY_E_Z:
					arrayParam.getMe().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
					break;
				}
				et.setText(value + "");
			}
			return false;
		}

	}

	private class SpinnerXMLSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			String bStart = adapterSpinner.getItem(position).toString();
			if (bStart.equals("Z")) {
				arrayParam.setbStartDirY(false);
				arrayParam.setbSType(false);
			} else if (bStart.equals("S")) {
				arrayParam.setbStartDirY(false);
				arrayParam.setbSType(true);

			} else if (bStart.equals("W")) {
				arrayParam.setbStartDirY(true);
				arrayParam.setbSType(false);

			} else if (bStart.equals("U")) {
				arrayParam.setbStartDirY(true);
				arrayParam.setbSType(true);
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	/**
	 * 找到所有组件的属性值
	 * 
	 * @return false代表有错误信息,true代表一起正常
	 */
	private boolean checkAllComponents() {

		double value;
		if (rowEdit.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.row_not_null);
			return false;
		} else if (Integer.parseInt(rowEdit.getText().toString()) < 1) {
			prompt = getResources().getString(R.string.row_not_less_1);
			return false;
		} else {
			arrayParam.setRow(Integer.parseInt(rowEdit.getText().toString()));
		}
		if (columnEdit.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.column_not_null);
			return false;
		} else if (Integer.parseInt(columnEdit.getText().toString()) < 1) {
			prompt = getResources().getString(R.string.column_not_less_1);
			return false;
		} else {
			arrayParam.setCol(Integer.parseInt(columnEdit.getText().toString()));
		}
		if (et_x_offset_x.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.x_offset_x_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_x_offset_x.getText().toString());
			arrayParam.getMx().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
		}
		if (et_x_offset_y.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.x_offset_y_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_x_offset_y.getText().toString());
			arrayParam.getMx().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
		}
		if (et_x_offset_z.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.x_offset_z_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_x_offset_z.getText().toString());
			arrayParam.getMx().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
			//默认是为1的
			arrayParam.getMx().setU(0);
		}
		if (et_y_offset_x.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.y_offset_x_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_y_offset_x.getText().toString());
			arrayParam.getMy().setX(RobotParam.INSTANCE.XJourney2Pulse(value));
		}
		if (et_y_offset_y.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.y_offset_y_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_y_offset_y.getText().toString());
			arrayParam.getMy().setY(RobotParam.INSTANCE.YJourney2Pulse(value));
		}
		if (et_y_offset_z.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.y_offset_z_not_null);
			return false;
		} else {
			value = Double.parseDouble(et_y_offset_z.getText().toString());
			arrayParam.getMy().setZ(RobotParam.INSTANCE.ZJourney2Pulse(value));
			//默认是为1
			arrayParam.getMy().setU(0);
		}
		if (et_end_x.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.et_end_x_not_null);
			return false;
		} else {
			arrayParam.getMe().setX(Double.parseDouble(et_end_x.getText().toString()));
		}
		if (et_end_y.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.et_end_y_not_null);
			return false;
		} else {
			arrayParam.getMe().setY(Double.parseDouble(et_end_y.getText().toString()));
		}
		if (et_end_z.getText().toString().equals("")) {
			prompt = getResources().getString(R.string.et_end_z_not_null);
			return false;
		} else {
			arrayParam.getMe().setZ(Double.parseDouble(et_end_z.getText().toString()));
			// U默认是设为1的
			arrayParam.getMe().setU(0);
		}
		return true;
	}

	/**
	 * 保存并返回TaskActivity
	 */
	private void saveBackActivity() {
		if (checkAllComponents()) {
			// arrayParam.setbSort(true);
			List<Point> pLists = ArrayArithmetic.arrayPoint(arrayParam, points);
			Log.d(TAG, pLists.toString());
			Bundle extras = new Bundle();
			if (pLists.size() > TaskActivity.MAX_SIZE) {
				extras.putString(TaskActivity.KEY_NUMBER, "0");
				userApplication.setPoints(pLists);
			} else {
				extras.putString(TaskActivity.KEY_NUMBER, "1");
				extras.putParcelableArrayList(TaskActivity.ARRAY_KEY, (ArrayList<? extends Parcelable>) pLists);
			}
			intent.putExtras(extras);
			setResult(TaskActivity.resultArrayCode, intent);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
		} else {
			ToastUtil.displayPromptInfo(this, prompt);
		}
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueArrayActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_array));
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setPositiveButton(getResources().getString(R.string.is_need_yes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						saveBackActivity();
					}

				});
		builder.setNegativeButton(getResources().getString(R.string.is_need_no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				GlueArrayActivity.this.finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			}
		});
		builder.setNeutralButton(getResources().getString(R.string.is_need_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	private void DisPlayInfoAfterGetMsg(byte[] revBuffer) {
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
		case 0:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "校验失败");
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);

				if (selectFocus == 0) {
					et_x_offset_x
							.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX())));
					et_x_offset_y
							.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(coordPoint.getY())));
					et_x_offset_z
							.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(coordPoint.getZ())));
				} else if (selectFocus == 1) {
					et_y_offset_x
							.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX())));
					et_y_offset_y
							.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(coordPoint.getY())));
					et_y_offset_z
							.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(coordPoint.getZ())));
				} else if (selectFocus == 2) {
					et_end_x.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX())));
					et_end_y.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(coordPoint.getY())));
					et_end_z.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(coordPoint.getZ())));
				}

			}
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务上传失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务下载失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(GlueArrayActivity.this, "未知错误");
			break;
		}
	}

	private class RevHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// 如果消息来自子线程
			if (msg.what == SocketInputThread.SocketInputWhat) {
				// 获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer) msg.obj;
				byte[] buffer;
				buffer = temp.array();
				// byte[] revBuffer = (byte[]) msg.obj;
				DisPlayInfoAfterGetMsg(buffer);
			}
		}
	}

	/**
	 * @Title saveMediumSpeed
	 * @Description 单步和连续的速度都改成中速的，且将文本提示设置成中速
	 * @param settingParam
	 */
	private void saveMediumSpeed(SettingParam settingParam) {
		speed = settingParam.getMediumSpeed();
		speedXYZ[0] = 2 * settingParam.getxStepDistance();
		speedXYZ[1] = 2 * settingParam.getyStepDistance();
		speedXYZ[2] = 2 * settingParam.getzStepDistance();
		ToastUtil.displayPromptInfo(this, getResources().getString(R.string.activity_medium));
		image_speed.setBackgroundResource(R.drawable.icon_speed_medium);
		tv_speed.setText(getResources().getString(R.string.activity_medium));
	}

	/**
	 * @Title saveLowSpeed
	 * @Description 单步和连续的速度都改成低速的，且将文本提示设置成低速
	 * @param settingParam
	 */
	private void saveLowSpeed(SettingParam settingParam) {
		speed = settingParam.getLowSpeed();
		speedXYZ[0] = 1 * settingParam.getxStepDistance();
		speedXYZ[1] = 1 * settingParam.getyStepDistance();
		speedXYZ[2] = 1 * settingParam.getzStepDistance();
		ToastUtil.displayPromptInfo(this, getResources().getString(R.string.activity_low));
		image_speed.setBackgroundResource(R.drawable.icon_speed_low);
		tv_speed.setText(getResources().getString(R.string.activity_low));
	}

	/**
	 * @Title saveHighSpeed
	 * @Description 单步和连续的速度都改成高速的，且将文本提示设置成高速
	 * @param settingParam
	 */
	private void saveHighSpeed(SettingParam settingParam) {
		speed = settingParam.getHighSpeed();
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();
		ToastUtil.displayPromptInfo(this, getResources().getString(R.string.activity_high));
		image_speed.setBackgroundResource(R.drawable.icon_speed_high);
		tv_speed.setText(getResources().getString(R.string.activity_high));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.array_lin_x:
			selectFocus = 0;
			linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
			linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));

			break;
		case R.id.array_lin_y:
			selectFocus = 1;
			linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));
			linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));

			break;
		case R.id.array_lin_e:
			selectFocus = 2;
			linear_x.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_y.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_bg));
			linear_e.setBackground(getResources().getDrawable(R.drawable.task_linearlayout_pressed_bg));

			break;
		case R.id.rl_back:// 返回
			showBackDialog();

			break;
		case R.id.rl_sudu:// 速度
			speedFlag++;
			if (speedFlag % 3 == 1) {
				saveMediumSpeed(settingParam);
			} else if (speedFlag % 3 == 2) {
				saveLowSpeed(settingParam);
			} else if (speedFlag % 3 == 0) {
				saveHighSpeed(settingParam);
			}
			break;
		case R.id.rl_moshi:// 模式
			if (modeFlag == 0) {
				modeFlag = 1;// 点击一次变为单步
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.step_single));
				image_moshi.setBackgroundResource(R.drawable.icon_step_single);
				tv_moshi.setText(getResources().getString(R.string.step_single));
			} else {
				modeFlag = 0;// 默认为0
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.step_serious));
				image_moshi.setBackgroundResource(R.drawable.icon_step_serious);
				tv_moshi.setText(getResources().getString(R.string.step_serious));
			}
			break;
		case R.id.rl_complete:// 完成
			showBackDialog();
			break;
		}
	}
}
