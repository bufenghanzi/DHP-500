/**
 * 
 */
package com.mingseal.activity;

import java.nio.ByteBuffer;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.dhp.R;
import com.mingseal.utils.CameraInterface;
import com.mingseal.utils.CameraInterface.CamOpenOverCallback;
import com.mingseal.utils.DisplayUtil;
import com.mingseal.utils.FloatUtil;
import com.mingseal.utils.MoveUtils;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.WifiConnectTools;
import com.mingseal.view.CameraSurfaceView;
import com.mingseal.view.MyCircleView;
import com.mingseal.view.MyCircleView.Dir;
import com.mingseal.view.MyCircleView.onActivityCallBackListener;
import com.mingseal.view.VerticalSeekBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * @author 商炎炳
 *
 */
public class GlueCameraActivity extends Activity implements CamOpenOverCallback, OnClickListener {

	private static final String TAG = "GlueCameraActivity";
	/**
	 * 预览图片视图
	 */
	private CameraSurfaceView surfaceView = null;
	private FrameLayout previewFrame;
	/**
	 * 垂直SeekBar
	 */
	private VerticalSeekBar vertical_bar;
	/**
	 * 拍照按钮
	 */
	private ImageButton takeBtn;
	private RelativeLayout rl_back;
	private RelativeLayout rl_title_speed;// 标题栏的速度布局
	private RelativeLayout rl_title_moshi;// 标题栏的模式布局
	private RelativeLayout rl_title_wifi_connecting;// wifi连接情况
	private ImageView image_speed;// 标题栏的速度
	private ImageView image_moshi;// 标题栏的模式
	/**
	 * 速度
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
	private TextView tv_title;

//	/**
//	 * x,y的控制
//	 */
//	private MyCircleView myCircleUp;
//	/**
//	 * z,u的控制
//	 */
//	private MyCircleView myCircleDown;
	/**
	 * X的Edittext
	 */
	private EditText et_x;
	/**
	 * Y的Edittext
	 */
	private EditText et_y;
	/**
	 * Z的Edittext
	 */
	private EditText et_z;
	/**
	 * U的Edittext
	 */
	private EditText et_u;
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
	/**
	 * @Fields m_nAxisNum : 机器轴数
	 */
	private int m_nAxisNum;

	float previewRate = -1f;

	private int speedFlag = 0;// 判断点击了几次，三次一循环，分别为高速33,中速13,低速3
	private int speed;
	private int[] speedXYZ;// 单步的话保存x,y,z的坐标
	private SettingParam settingParam;// 任务设置参数
	/**
	 * 连续0(默认),单步是1
	 */
	private int modeFlag = 0;// 连续单步判断标志

	private double position_x = 0;
	private double position_y = 0;
	private double position_z = 0;
	private double position_u = 0;

	private final static int KEY_X = 0;
	private final static int KEY_Y = 1;
	private final static int KEY_Z = 2;
	private final static int KEY_U = 3;

	private Intent intent;// 接收从之前传过来的Intent
	private Point point;// 任务点
	private RevHandler handler;
	private UserApplication userApplication;
	private ImageView iv_wifi_connecting;//wifi连接情况

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		userApplication = (UserApplication) getApplication();
		Thread openThread = new Thread() {
			@Override
			public void run() {
				CameraInterface.getInstance(GlueCameraActivity.this).doOpenCamera(GlueCameraActivity.this);
			}
		};
		openThread.start();
		setContentView(R.layout.activity_glue_camera);
		intent = getIntent();
		point = intent.getParcelableExtra(TaskActivity.ARRAY_KEY);
		Log.d(TAG, "任务点：之前：" + point.toString());
		initUI();
		initViewParams();
		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
		// 进入照相界面，先定位到选中点的位置
		MoveUtils.locationCoord(point);
		m_nAxisNum = RobotParam.INSTANCE.getM_nAxisNum();
		if (m_nAxisNum == 3) {
			// 三轴机器，u不允许点击
			et_u.setEnabled(false);
			but_u_plus.setVisibility(View.INVISIBLE);
			but_u_minus.setVisibility(View.INVISIBLE);
//			myCircleDown.setRow("");
		} else {
			et_u.setEnabled(true);
			but_u_plus.setVisibility(View.VISIBLE);
			but_u_minus.setVisibility(View.VISIBLE);
		}

		vertical_bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				CameraInterface.getInstance(GlueCameraActivity.this).setZoom(progress);
			}
		});


	}

	/**
	 * 加载自定义UI
	 */
	private void initUI() {
		previewFrame = (FrameLayout) findViewById(R.id.camera_preview);
		surfaceView = (CameraSurfaceView) findViewById(R.id.camera);
		takeBtn = (ImageButton) findViewById(R.id.imageButtonTake);

		vertical_bar = (VerticalSeekBar) findViewById(R.id.vertical_bar);
		vertical_bar.setMax(99);
		vertical_bar.setProgress(0);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_visual_aid));

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_speed = (RelativeLayout) findViewById(R.id.rl_sudu);
		tv_speed = (TextView) rl_speed.findViewById(R.id.tv_camera_speed);
		rl_moshi = (RelativeLayout) findViewById(R.id.rl_moshi);
		tv_moshi = (TextView) rl_moshi.findViewById(R.id.tv_camera_moshi);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);

		takeBtn.setOnClickListener(this);
		rl_back.setOnClickListener(this);
		rl_speed.setOnClickListener(this);
		rl_moshi.setOnClickListener(this);
		rl_complete.setOnClickListener(this);

		et_x = (EditText) findViewById(R.id.et_x);
		et_y = (EditText) findViewById(R.id.et_y);
		et_z = (EditText) findViewById(R.id.et_z);
		et_u = (EditText) findViewById(R.id.et_u);
		
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

		et_x.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_x, KEY_X));
		et_y.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_y, KEY_Y));
		et_z.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_z, KEY_Z));
		et_u.setOnFocusChangeListener(new OnKeyFocusChangeListener(et_u, KEY_U));

		et_x.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(point.getX())));
		et_y.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(point.getY())));
		et_z.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(point.getZ())));
		et_u.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.UPulse2Journey(point.getU())));

		rl_title_speed = (RelativeLayout) findViewById(R.id.rl_title_speed);
		rl_title_moshi = (RelativeLayout) findViewById(R.id.rl_title_moshi);
		// 让RelativeLayout显示
		rl_title_speed.setVisibility(View.VISIBLE);
		rl_title_moshi.setVisibility(View.VISIBLE);
		iv_wifi_connecting  =(ImageView) findViewById(R.id.iv_title_wifi_connecting);
		WifiConnectTools.processWifiConnect(userApplication, iv_wifi_connecting);
		image_speed = (ImageView) findViewById(R.id.iv_title_speed);
		image_moshi = (ImageView) findViewById(R.id.iv_title_moshi);
		// 设置初值
		image_speed.setBackgroundResource(R.drawable.icon_speed_high);
		image_moshi.setBackgroundResource(R.drawable.icon_step_serious);

		settingParam = SharePreferenceUtils.readFromSharedPreference(this);
		speed = settingParam.getHighSpeed();
		speedXYZ = new int[3];
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();
	}

	/**
	 * 设置SurfaceView属性
	 */
	private void initViewParams() {
		LayoutParams params = surfaceView.getLayoutParams();
		android.graphics.Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;
		previewRate = DisplayUtil.getScreenRate(this); // 默认全屏的比例预览

		surfaceView.setLayoutParams(params);
	}
	
	/**
	 * @ClassName MoveListener
	 * @Description x,y,z,u移动
	 * @author 商炎炳
	 * @date 2016年1月28日 下午2:39:53
	 *
	 */
	private class MoveListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (modeFlag == 0) {
				// 连续
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
			} else if (modeFlag == 1) {
				// 单步
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
		
			return false;
		}
		
	}

	/**
	 * 失去焦点事件,当超过数值或者小于0时,设置正确的数值
	 *
	 */
	private class OnKeyFocusChangeListener implements OnFocusChangeListener {

		private EditText et;
		private double value;
		private int key;

		/**
		 * 当失去焦点时,如果为空的话，要设置为0
		 * 
		 * @param et
		 *            Edittext
		 */
		public OnKeyFocusChangeListener(EditText et, int key) {
			this.et = et;
			this.key = key;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if (et.getText().toString().equals("")) {
					et.setText("0");
				}
				((EditText) v).setCursorVisible(false);
				value = Double.parseDouble(et.getText().toString());
				if (key == KEY_X) {
					if (value > RobotParam.INSTANCE.GetXJourney()) {
						value = RobotParam.INSTANCE.GetXJourney();
					} else if (value < 0) {
						value = 0;
					}
				} else if (key == KEY_Y) {
					if (value > RobotParam.INSTANCE.GetYJourney()) {
						value = RobotParam.INSTANCE.GetYJourney();
					} else if (value < 0) {
						value = 0;
					}
				} else if (key == KEY_Z) {
					if (value > RobotParam.INSTANCE.GetZJourney()) {
						value = RobotParam.INSTANCE.GetZJourney();
					} else if (value < 0) {
						value = 0;
					}
				} else if (key == KEY_U) {
					if (value > RobotParam.INSTANCE.GetUJourney()) {
						value = RobotParam.INSTANCE.GetUJourney();
					} else if (value < 0) {
						value = 0;
					}
				}
				et.setText(value + "");
			}
		}

	}

	@Override
	public void cameraHasOpened() {
		// TODO Auto-generated method stub
		SurfaceHolder holder = surfaceView.getSurfaceHolder();
		CameraInterface.getInstance(GlueCameraActivity.this).doStartPreview(holder, previewRate);
	}

	/**
	 * 检查所有输入框是否为空
	 * 
	 * @return true代表正常,false代表有异常
	 */
	private boolean checkAllComponents() {
		String str = "";
		if (et_x.getText().toString().equals("")) {
			str += "x" + getResources().getString(R.string.not_null);
		} else if (et_y.getText().toString().equals("")) {
			str += "y" + getResources().getString(R.string.not_null);
		} else if (et_z.getText().toString().equals("")) {
			str += "z" + getResources().getString(R.string.not_null);
		} else if (et_u.getText().toString().equals("")) {
			str += "u" + getResources().getString(R.string.not_null);
		}

		if (str.equals("")) {
			return true;
		} else {
			ToastUtil.displayPromptInfo(this, str);
			return false;
		}
	}

	private void saveBackActivity() {
		if (checkAllComponents()) {
			position_x = Double.parseDouble(et_x.getText().toString());
			position_y = Double.parseDouble(et_y.getText().toString());
			position_z = Double.parseDouble(et_z.getText().toString());
			position_u = Double.parseDouble(et_u.getText().toString());

			point.setX(RobotParam.INSTANCE.XJourney2Pulse(position_x));
			point.setY(RobotParam.INSTANCE.YJourney2Pulse(position_y));
			point.setZ(RobotParam.INSTANCE.ZJourney2Pulse(position_z));
			point.setU(RobotParam.INSTANCE.UJourney2Pulse(position_u));
			Log.d(TAG, "任务点：" + point.toString());

			Bundle extras = new Bundle();
			extras.putParcelable(TaskActivity.CAMERA_KEY, point);

			intent.putExtras(extras);
			setResult(TaskActivity.resultCameraCode, intent);
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
		}
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueCameraActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_save));
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setPositiveButton(getResources().getString(R.string.need_yes), new DialogInterface.OnClickListener() {
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
				GlueCameraActivity.this.finish();
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
		case R.id.imageButtonTake:
			Log.d(TAG, "点击ImageButton");
			CameraInterface.getInstance(GlueCameraActivity.this).doTakePicture();
			break;
		case R.id.rl_back:
			showBackDialog();
			break;
		case R.id.rl_sudu:
			speedFlag++;
			if (speedFlag % 3 == 1) {
				saveMediumSpeed(settingParam);
			} else if (speedFlag % 3 == 2) {
				saveLowSpeed(settingParam);
			} else if (speedFlag % 3 == 0) {
				saveHighSpeed(settingParam);
			}
			break;
		case R.id.rl_moshi:
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
		case R.id.rl_complete:
			saveBackActivity();
			break;
		}
	}

	private void DisPlayInfoAfterGetMsg(byte[] revBuffer) {
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
		case 0:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "校验失败");
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);

				et_x.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX())));
				et_y.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.YPulse2Journey(coordPoint.getY())));
				et_z.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.ZPulse2Journey(coordPoint.getZ())));
				et_u.setText(FloatUtil.getFloatToString(RobotParam.INSTANCE.UPulse2Journey(coordPoint.getU())));

			}
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "任务上传失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "任务下载失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(GlueCameraActivity.this, "未知错误");
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

}
