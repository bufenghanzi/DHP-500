/**
 * 
 */
package com.mingseal.activity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;
import com.mingseal.dhp.R;
import com.mingseal.utils.FloatUtil;
import com.mingseal.utils.MoveUtils;
import com.mingseal.utils.PointCopyTools;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.WifiConnectTools;
import com.mingseal.view.CustomView;
import com.mingseal.view.MyCircleView;
import com.mingseal.view.MyCircleView.Dir;
import com.mingseal.view.MyCircleView.onActivityCallBackListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 商炎炳
 *
 */
public class GlueViewActivity extends Activity implements OnClickListener {

	private static final String TAG = "GlueViewActivity";

	/**
	 * 绘图区域
	 */
	private CustomView customView;
	/**
	 * x编辑框
	 */
	private EditText et_x;
	/**
	 * y编辑框
	 */
	private EditText et_y;
	/**
	 * z编辑框
	 */
	private EditText et_z;
	/**
	 * u编辑框
	 */
	private EditText et_u;
//	/**
//	 * 控制x,y变化的导航杆
//	 */
//	private MyCircleView myCircleUp;
//	/**
//	 * 移动点的导航杆
//	 */
//	private MyCircleView myCircleDown;
	/**
	 * 标题栏
	 */
	private TextView tv_title;
	/**
	 * 返回
	 */
	private RelativeLayout rl_back;
	private RelativeLayout rl_title_speed;// 标题栏的速度布局
	private RelativeLayout rl_title_moshi;// 标题栏的模式布局
	private RelativeLayout rl_title_wifi_connecting;//wifi连接情况
	private ImageView image_speed;// 标题栏的速度
	private ImageView image_moshi;// 标题栏的模式
	/**
	 * 速度
	 */
	private RelativeLayout rl_sudu;
	/**
	 * 模式
	 */
	private RelativeLayout rl_moshi;
	/**
	 * 完成
	 */
	private RelativeLayout rl_complete;
	/**
	 * @Fields tv_speed: 速度文本
	 */
	private TextView tv_speed;
	/**
	 * @Fields tv_moshi: 模式文本
	 */
	private TextView tv_moshi;

	private List<Point> pointListsCur = new ArrayList<>();// 接收从TaskActivity传递过来的Point列表
	private List<Point> pointListsFirst = new ArrayList<>();// 用于比较有没有变化
	/**
	 * 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	 */
	private String numberType;// 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	private Point point;

	private int speedFlag = 0;// 判断点击了几次，三次一循环，分别为高速33,中速13,低速3
	private int speed;
	private int[] speedXYZ;// 单步的话保存x,y,z的坐标
	private SettingParam settingParam;// 任务设置参数
	/**
	 * 连续0(默认),单步是1
	 */
	private int modeFlag = 0;// 连续单步判断标志
	/**
	 * 上次选中的点的position(判断是否需要定位)
	 */
	private int selectAssignPosition = -1;// 单选框上次选中的ID(判断是否需要定位)
	/**
	 * @Fields m_nAxisNum : 机器轴数
	 */
	private int m_nAxisNum;
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
	 * 从TaskActivity传过来的Intent
	 */
	private Intent intent;

	private RevHandler handler;
	private UserApplication userApplication;
	/**
	 * @Fields isChange: 判断List是否改变,true为没有变化,false表示有变化
	 */
	private boolean isChange = true;

	/**
	 * @Fields iv_wifi_connecting: wifi连接情况
	 */
	private ImageView iv_wifi_connecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_view);
		userApplication = (UserApplication) getApplication();
		intent = getIntent();
		numberType = intent.getStringExtra(TaskActivity.KEY_NUMBER);
		if ("0".equals(numberType)) {
			pointListsCur = userApplication.getPoints();
		} else if ("1".equals(numberType)) {
			pointListsCur = intent.getParcelableArrayListExtra(TaskActivity.ARRAY_KEY);
		}

		//复制List
		pointListsFirst = PointCopyTools.processCopyPoints(pointListsCur);

		settingParam = SharePreferenceUtils.readFromSharedPreference(this);
		initView();
		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
		// initPoint();

		customView.setPoints(pointListsCur);

		setCoords();
		m_nAxisNum = RobotParam.INSTANCE.getM_nAxisNum();
		if(m_nAxisNum == 3){
			et_u.setEnabled(false);
//			myCircleDown.setRow("");
		}else{
			et_u.setEnabled(true);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 加载组件
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_glue_shitu));
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
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
		rl_sudu = (RelativeLayout) findViewById(R.id.rl_sudu);
		tv_speed = (TextView) rl_sudu.findViewById(R.id.tv_view_speed);
		rl_sudu.setOnClickListener(this);
		rl_moshi = (RelativeLayout) findViewById(R.id.rl_moshi);
		tv_moshi = (TextView) findViewById(R.id.tv_view_moshi);
		rl_moshi.setOnClickListener(this);
		rl_complete = (RelativeLayout) findViewById(R.id.rl_complete);
		rl_complete.setOnClickListener(this);
		customView = (CustomView) findViewById(R.id.customView);
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
		
		but_u_plus.setText("↓");
		but_u_minus.setText("↑");
		MoveListener moveListener = new MoveListener();
		//添加触摸事件
		but_x_plus.setOnTouchListener(moveListener);
		but_x_minus.setOnTouchListener(moveListener);
		but_y_plus.setOnTouchListener(moveListener);
		but_y_minus.setOnTouchListener(moveListener);
		but_z_plus.setOnTouchListener(moveListener);
		but_z_minus.setOnTouchListener(moveListener);
		but_u_plus.setOnTouchListener(moveListener);
		but_u_minus.setOnTouchListener(moveListener);
		
		speed = settingParam.getHighSpeed();
		speedXYZ = new int[3];
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();

	}
	
	/**
	 * 设置X,Y,Z,U的坐标值
	 */
	private void setCoords() {
		et_x.setText(FloatUtil.getFloatToString(
				RobotParam.INSTANCE.XPulse2Journey(customView.getPointByAssignPosition().getX())) + "");
		et_y.setText(FloatUtil.getFloatToString(
				RobotParam.INSTANCE.YPulse2Journey(customView.getPointByAssignPosition().getY())) + "");
		et_z.setText(FloatUtil.getFloatToString(
				RobotParam.INSTANCE.ZPulse2Journey(customView.getPointByAssignPosition().getZ())) + "");
		et_u.setText(FloatUtil.getFloatToString(
				RobotParam.INSTANCE.UPulse2Journey(customView.getPointByAssignPosition().getU())) + "");
	}

	/**
	 * 加载自定义的任务点
	 */
	private void initPoint() {
		pointListsCur = new ArrayList<Point>();
		for (int i = 1; i < 10; i++) {
			point = new Point(PointType.POINT_NULL);
			point.setX(i * 20);
			point.setY(i * 10);
			pointListsCur.add(point);
		}
		point = new Point(150, 30, 10, 20, PointType.POINT_NULL);
		pointListsCur.add(point);
		point = new Point(30, 130, 3, 6, PointType.POINT_NULL);
		pointListsCur.add(point);
		point = new Point(80, 80, 7, 8, PointType.POINT_NULL);
		pointListsCur.add(point);

	}
	
	/**
	 * @ClassName MoveListener
	 * @Description x,y,z,u移动
	 * @author 商炎炳
	 * @date 2016年1月28日 下午3:29:26
	 *
	 */
	private class MoveListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.nav_u_plus) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					customView.setAssignPosition(1);
					setCoords();
				}
			} else if (v.getId() == R.id.nav_u_minus) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					customView.setAssignPosition(-1);
					setCoords();
				}

			} else {
				// 不为u轴控制,需要判断是否是当前点
				if (selectAssignPosition == customView.getAssignPosition()) {
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
						}
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						MoveUtils.locationCoord(pointListsCur.get(customView.getAssignPosition()));
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						// 抬起的时候把设置上次选中的单选框
						selectAssignPosition = customView.getAssignPosition();
					}
				}
			}
			return false;
		}

	}

	/**
	 * 保存页面中的信息并返回到之前的TaskActivity
	 */
	private void saveBackActivity() {
		Bundle extras = new Bundle();
		if ("0".equals(numberType)) {
			extras.putString(TaskActivity.KEY_NUMBER, "0");
			userApplication.setPoints(pointListsCur);
		} else {
			extras.putString(TaskActivity.KEY_NUMBER, "1");
			extras.putParcelableArrayList(TaskActivity.VIEW_KEY, (ArrayList<? extends Parcelable>) pointListsCur);
		}

		intent.putExtras(extras);
		setResult(TaskActivity.resultViewCode, intent);
		finish();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueViewActivity.this);
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
				GlueViewActivity.this.finish();
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
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "校验失败");
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);
				pointListsCur.get(customView.getAssignPosition()).setX(coordPoint.getX());
				pointListsCur.get(customView.getAssignPosition()).setY(coordPoint.getY());
				pointListsCur.get(customView.getAssignPosition()).setZ(coordPoint.getZ());
				pointListsCur.get(customView.getAssignPosition()).setU(coordPoint.getU());
				customView.setPoints(pointListsCur);// 重绘图
				setCoords();

			} else if (revBuffer[2] == 0x4A) {// 获取下位机参数成功
				ToastUtil.displayPromptInfo(GlueViewActivity.this, "获取参数成功!");
			}
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "任务上传失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "任务下载失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(GlueViewActivity.this, "未知错误");
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
		case R.id.rl_back:// 返回

			isChange = PointCopyTools.comparePoints(pointListsFirst, pointListsCur);
			if(isChange){
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_not_changed));
				GlueViewActivity.this.finish();
			}else{
				showBackDialog();
			}
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
			isChange = PointCopyTools.comparePoints(pointListsFirst, pointListsCur);
			if(isChange){
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_not_changed));
				GlueViewActivity.this.finish();
			}else{
				showBackDialog();
			}
			break;

		}
	}

}
