/**
 * 
 */
package com.mingseal.activity;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.mingseal.application.UserApplication;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.DownloadParam;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.param.TaskParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointTask;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.utils.DateUtil;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 商炎炳
 *
 */
public class GlueDownloadActivity extends Activity implements OnClickListener {

	public static final String DOWNLOAD_PARAM = "downloading_param";
	private static final String TAG = "GlueDownloadActivity";
	private TextView tv_title;
	/**
	 * 返回
	 */
	private RelativeLayout rl_back;
	/**
	 * 下载
	 */
	private RelativeLayout rl_download;
	/**
	 * 取消
	 */
	private RelativeLayout rl_cancel;
	/**
	 * @Fields rl_title_wifi_connecting: wifi连接情况
	 */
	private RelativeLayout rl_title_wifi_connecting;// wifi连接情况

	// /**
	// * 任务号
	// */
	// private NumberPicker num_number;
	// /**
	// * 加速度
	// */
	// private NumberPicker num_accelerate_time;
	// /**
	// * 减速度
	// */
	// private NumberPicker num_decelerate_time;
	// /**
	// * XY轴空走速度
	// */
	// private NumberPicker num_xy_move;
	// /**
	// * Z轴空走速度
	// */
	// private NumberPicker num_z_move;
	// /**
	// * 拐点速度
	// */
	// private NumberPicker num_inflexion_time;
	// /**
	// * 拐点最大加速度
	// */
	// private NumberPicker num_max_accelerate_time;
	/**
	 * @Fields et_number: 任务号
	 */
	private EditText et_number;
	/**
	 * @Fields et_accelerate_time: 加速度
	 */
	private EditText et_accelerate_time;
	/**
	 * @Fields et_decelerate_time: 减速度
	 */
	private EditText et_decelerate_time;
	/**
	 * @Fields et_xy_move: XY轴空走速度
	 */
	private EditText et_xy_move;
	/**
	 * @Fields et_z_move: Z轴空走速度
	 */
	private EditText et_z_move;
	/**
	 * @Fields et_inflexion_time: 拐点速度
	 */
	private EditText et_inflexion_time;
	/**
	 * @Fields et_max_accelerate_time: 拐点最大加速度
	 */
	private EditText et_max_accelerate_time;
	/**
	 * 对话框中的自定义View
	 */
	private View customView;
	/**
	 * 判断dialog需不需要关闭
	 */
	private Field field;

	private DownloadParam downParam;
	private Intent intent;

	private List<Point> points;// 接收从TaskActivity传递过来的Point列表
	private String taskName;// 任务名
	/**
	 * 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	 */
	private String numberType;// 接收从TaskActivity传递过来的类型(0代表大数据,1代表小数据)
	private UserApplication userApplication;
	private RevHandler handler;
	private EditText et_title;
	/**
	 * @Fields isNull: 判断编辑输入框是否为空,false表示为空,true表示不为空
	 */
	private boolean isNull = false;
	private boolean isLow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download);
		userApplication = (UserApplication) getApplication();
		MessageMgr.INSTANCE.setUserApplication(userApplication);
		intent = getIntent();
		numberType = intent.getStringExtra(TaskActivity.KEY_NUMBER);
		if ("0".equals(numberType)) {
			points = userApplication.getPoints();
		} else if ("1".equals(numberType)) {
			points = intent.getParcelableArrayListExtra(TaskActivity.ARRAY_KEY);
		}
		taskName = intent.getStringExtra(TaskActivity.DOWNLOAD_NUMBER_KEY);
		initView();
		downParam = new DownloadParam();
		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
	}

	/**
	 * 加载自定义组件
	 */
	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.activity_download_task));
		rl_title_wifi_connecting = (RelativeLayout) findViewById(R.id.rl_title_wifi_connecting);
		processWifiConnect(rl_title_wifi_connecting);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_download = (RelativeLayout) findViewById(R.id.rl_download);
		rl_cancel = (RelativeLayout) findViewById(R.id.rl_cancel);
		et_number = (EditText) findViewById(R.id.et_download_tasknumber);
		et_accelerate_time = (EditText) findViewById(R.id.et_download_accelerate_time);
		et_decelerate_time = (EditText) findViewById(R.id.et_download_decelerate_time);
		et_xy_move = (EditText) findViewById(R.id.et_download_xy_move);
		et_z_move = (EditText) findViewById(R.id.et_download_z_move);
		et_inflexion_time = (EditText) findViewById(R.id.et_download_inflexion_time);
		et_max_accelerate_time = (EditText) findViewById(R.id.et_download_max_accelerate_move);
		// num_number = (NumberPicker) findViewById(R.id.num_task_number);
		// num_accelerate_time = (NumberPicker)
		// findViewById(R.id.num_accelerate_time);
		// num_xy_move = (NumberPicker) findViewById(R.id.num_xy_move);
		// num_z_move = (NumberPicker) findViewById(R.id.num_z_move);
		// num_inflexion_time = (NumberPicker)
		// findViewById(R.id.num_inflexion_time);
		// num_decelerate_time = (NumberPicker)
		// findViewById(R.id.num_decelerate_time);
		// num_max_accelerate_time = (NumberPicker)
		// findViewById(R.id.num_max_accelerate_move);

		// 设置NumberPick的初始值
		// num_number.setMaxValue(120);
		// num_number.setMinValue(1);
		// num_number.setValue(SharePreferenceUtils.getTaskNumberFromPref(this));

		et_number.addTextChangedListener(new MaxMinEditWatcher(120, 1, et_number));
		et_number.setOnFocusChangeListener(new MaxMinFocusChangeListener(120, 1, et_number));
		et_number.setSelectAllOnFocus(true);
		et_number.setText(SharePreferenceUtils.getTaskNumberFromPref(this) + "");

		// num_accelerate_time.setMaxValue(10000);
		// num_accelerate_time.setMinValue(100);
		// num_accelerate_time.setValue(1000);
		et_accelerate_time.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_accelerate_time));
		et_accelerate_time.setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_accelerate_time));
		et_accelerate_time.setSelectAllOnFocus(true);
		et_accelerate_time.setText(TaskParam.INSTANCE.getnAccelerate()+"");

		// num_decelerate_time.setMaxValue(10000);
		// num_decelerate_time.setMinValue(100);
		// num_decelerate_time.setValue(1000);

		et_decelerate_time.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_decelerate_time));
		et_decelerate_time.setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_decelerate_time));
		et_decelerate_time.setSelectAllOnFocus(true);
		et_decelerate_time.setText(TaskParam.INSTANCE.getnDecelerate() + "");
		// num_xy_move.setMaxValue(800);
		// num_xy_move.setMinValue(1);
		// num_xy_move.setValue(200);

		et_xy_move.addTextChangedListener(new MaxMinEditWatcher(800, 1, et_xy_move));
		et_xy_move.setOnFocusChangeListener(new MaxMinFocusChangeListener(800, 1, et_xy_move));
		et_xy_move.setSelectAllOnFocus(true);
		et_xy_move.setText(TaskParam.INSTANCE.getnXYNullSpeed() + "");

		// num_z_move.setMaxValue(400);
		// num_z_move.setMinValue(1);
		// num_z_move.setValue(200);
		et_z_move.addTextChangedListener(new MaxMinEditWatcher(400, 1, et_z_move));
		et_z_move.setOnFocusChangeListener(new MaxMinFocusChangeListener(400, 1, et_z_move));
		et_z_move.setSelectAllOnFocus(true);
		et_z_move.setText(TaskParam.INSTANCE.getnZNullSpeed() + "");

		et_inflexion_time.addTextChangedListener(new MaxMinEditWatcher(800, 1, et_inflexion_time));
		et_inflexion_time.setOnFocusChangeListener(new MaxMinFocusChangeListener(800, 1, et_inflexion_time));
		et_inflexion_time.setSelectAllOnFocus(true);
		et_inflexion_time.setText(TaskParam.INSTANCE.getnTurnSpeed() + "");

		et_max_accelerate_time.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_max_accelerate_time));
		et_max_accelerate_time
				.setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_max_accelerate_time));
		et_max_accelerate_time.setSelectAllOnFocus(true);
		et_max_accelerate_time.setText(TaskParam.INSTANCE.getnTurnAccelerateMax() + "");

		// num_inflexion_time.setMaxValue(800);
		// num_inflexion_time.setMinValue(1);
		// num_inflexion_time.setValue(50);

		// num_max_accelerate_time.setMaxValue(10000);
		// num_max_accelerate_time.setMinValue(100);
		// num_max_accelerate_time.setValue(10000);

		rl_back.setOnClickListener(this);
		rl_download.setOnClickListener(this);
		rl_cancel.setOnClickListener(this);
	}

	/**
	 * @Title processWifiConnect
	 * @Description 显示隐藏wifi连接情况
	 * @param rl_title_wifi_connecting
	 */
	private void processWifiConnect(RelativeLayout rl_title_wifi_connecting) {
		if (userApplication.isWifiConnecting()) {
			// 显示wifi连接
			rl_title_wifi_connecting.setVisibility(View.VISIBLE);
		} else {
			rl_title_wifi_connecting.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * @Title isEditNull
	 * @Description 判断输入框是否为空
	 * @return false表示为空,true表示都有数据
	 */
	private boolean isEditNull() {
		if ("".equals(et_number.getText().toString())) {
			return false;
		} else if ("".equals(et_accelerate_time.getText().toString())) {
			return false;
		} else if ("".equals(et_decelerate_time.getText().toString())) {
			return false;
		} else if ("".equals(et_xy_move.getText().toString())) {
			return false;
		} else if ("".equals(et_z_move.getText().toString())) {
			return false;
		} else if ("".equals(et_inflexion_time.getText().toString())) {
			return false;
		} else if ("".equals(et_max_accelerate_time.getText().toString())) {
			return false;
		}
		return true;
	}

	/**
	 * @Title isEditLow
	 * @Description 判断输入框的内容是不是小于最小值
	 * @return false表示小于最小值，true表示正常
	 */
	private boolean isEditLow() {
		if (Integer.parseInt(et_number.getText().toString()) < 1) {
			return false;
		} else if (Integer.parseInt(et_accelerate_time.getText().toString()) < 100) {
			return false;
		} else if (Integer.parseInt(et_decelerate_time.getText().toString()) < 100) {
			return false;
		} else if (Integer.parseInt(et_xy_move.getText().toString()) < 1) {
			return false;
		} else if (Integer.parseInt(et_z_move.getText().toString()) < 1) {
			return false;
		} else if (Integer.parseInt(et_inflexion_time.getText().toString()) < 1) {
			return false;
		} else if (Integer.parseInt(et_max_accelerate_time.getText().toString()) < 100) {
			return false;
		}
		return true;

	}

	/**
	 * 保存页面中的信息并下载任务
	 */
	private void saveBackActivity() {
		setResult(TaskActivity.resultDownLoadCode, intent);
		SharePreferenceUtils.saveTaskNumberAndDatesToPref(this, Integer.parseInt(et_number.getText().toString()));
		TaskParam.INSTANCE.setStrTaskName(taskName);
		TaskParam.INSTANCE.setnStartX(points.get(0).getX());
		TaskParam.INSTANCE.setnStartY(points.get(0).getY());
		TaskParam.INSTANCE.setnStartZ(points.get(0).getZ());
		TaskParam.INSTANCE.setnStartU(points.get(0).getU());
		TaskParam.INSTANCE.setnTaskNum(Integer.parseInt(et_number.getText().toString()));
		TaskParam.INSTANCE.setnAccelerate(Integer.parseInt(et_accelerate_time.getText().toString()));// 设置加速度
		TaskParam.INSTANCE.setnDecelerate(Integer.parseInt(et_decelerate_time.getText().toString()));// 设置减速度
		TaskParam.INSTANCE.setnTurnSpeed(Integer.parseInt(et_inflexion_time.getText().toString()));// 拐点速度
		TaskParam.INSTANCE.setnXYNullSpeed(Integer.parseInt(et_xy_move.getText().toString()));// 设置XY轴空走速度
		TaskParam.INSTANCE.setnZNullSpeed(Integer.parseInt(et_z_move.getText().toString()));// 设置Z轴空走速度
		TaskParam.INSTANCE.setnTurnAccelerateMax(Integer.parseInt(et_max_accelerate_time.getText().toString()));// 设置拐点最大加速度
		OrderParam.INSTANCE.setnTaskNum(Integer.parseInt(et_number.getText().toString()));
		MessageMgr.INSTANCE.isTaskExist();
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(GlueDownloadActivity.this);
		builder.setMessage(getResources().getString(R.string.is_need_download));
		builder.setTitle(getResources().getString(R.string.tip));
		builder.setPositiveButton(getResources().getString(R.string.is_need_yes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// dialog.dismiss();
						try {
							field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
						} catch (NoSuchFieldException e1) {
							e1.printStackTrace();
						}
						isNull = isEditNull();
						if (isNull) {
							isLow = isEditLow();
							if (isLow) {
								try {
									field.set(dialog, true);// true表示要关闭
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								}
								saveBackActivity();
							} else {
								ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "有数据小于最小值");

								try {
									field.set(dialog, true);// true表示要关闭
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								}
							}
						} else {
							ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "有数据为空");

							try {
								field.set(dialog, true);// true表示要关闭
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							}
						}

					}
				});
		builder.setNegativeButton(getResources().getString(R.string.is_need_no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// true表示要关闭
				} catch (Exception e) {
					e.printStackTrace();
				}

				GlueDownloadActivity.this.finish();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			// 返回
			showBackDialog();
			break;
		case R.id.rl_download:
			// 下载
			isNull = isEditNull();
			if (isNull) {
				isLow = isEditLow();
				if (isLow) {
					saveBackActivity();
				} else {
					ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "有数据小于最小值");
				}
			} else {
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, getResources().getString(R.string.data_is_null));
			}
			break;
		case R.id.rl_cancel:
			// 取消
			GlueDownloadActivity.this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			break;

		}
	}

	/**
	 * @Title: displayTaskNumExist
	 * @Description: 跳出对话框，弹出提示，任务是否存在
	 */
	private void displayTaskNumExist() {
		AlertDialog.Builder buildAdd = new AlertDialog.Builder(GlueDownloadActivity.this);
		buildAdd.setTitle("任务下载");
		customView = View.inflate(GlueDownloadActivity.this, R.layout.custom_dialog_edittext, null);
		buildAdd.setView(customView);
		et_title = (EditText) customView.findViewById(R.id.et_title);
		et_title.setText("是否覆盖" + Integer.parseInt(et_number.getText().toString()) + "号任务?");
		et_title.setFocusable(false);
		buildAdd.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// true表示要关闭
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		buildAdd.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// true表示要关闭
					MessageMgr.INSTANCE.taskDownload(points);
				} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e1) {
					e1.printStackTrace();
				}

			}
		});
		buildAdd.show();
	}

	private class ManagingMessage extends AsyncTask<byte[], Void, Integer> {

		@Override
		protected Integer doInBackground(byte[]... params) {
			Log.d(TAG, "下载1：" + DateUtil.getCurrentTime());
			int i = MessageMgr.INSTANCE.managingMessage(params[0]);
			Log.d(TAG, "下载2：" + DateUtil.getCurrentTime());
			return i;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.d(TAG, "下载3：" + DateUtil.getCurrentTime() + ",result=" + result);
			switch (result) {
			case 0:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "校验失败");
				break;
			case 1: {
				// 结果等于1，表示正在下载
				// ToastUtil.showToast(GlueDownloadActivity.this, "正在下载...");
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			}
				break;
			case 40101:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "非法功能");
				break;
			case 40102:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "非法数据地址");
				break;
			case 40103:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "非法数据");
				break;
			case 40105:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "设备忙");
				break;
			case 40109:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "急停中");
				break;
			case 40110:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "X轴光电报警");
				break;
			case 40111:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "Y轴光电报警");
				break;
			case 40112:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "Z轴光电报警");
				break;
			case 40113:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "U轴光电报警");
				break;
			case 40114:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "行程超限报警");
				break;
			case 40115:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务上传失败");
				break;
			case 40116:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务下载失败");
				break;
			case 40117:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务模拟失败");
				break;
			case 40118:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "示教指令错误");
				break;
			case 40119:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "循迹定位失败");
				break;
			case 40120:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务号不可用");
				break;
			case 40121:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "初始化失败");
				break;
			case 40122:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "API版本错误");
				break;
			case 40123:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "程序升级失败");
				break;
			case 40124:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "系统损坏");
				break;
			case 40125:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务未加载");
				break;
			case 40126:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "(Z轴)基点抬起高度过高");
				break;
			case 40127:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "等待输入超时");
				break;
			default:
				ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "未知错误");
				break;
			}
		}

	}

	/**
	 * @Title: disPlayInfoAfterGetMsg
	 * @Description: 获取下位机返回的指令，进行解析
	 * @param revBuffer
	 */
	private void disPlayInfoAfterGetMsg(byte[] revBuffer) {
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
		case 0:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "校验失败");
			if (((revBuffer[3] & 0x00ff) == 0x31) && ((revBuffer[2] & 0x00ff) == 0x79)) {
				if ((revBuffer[5] & 0x00ff) == 0) {
					Log.d(TAG, "任务不存在");
					// 任务不存在的话，就可以直接下载
					MessageMgr.INSTANCE.taskDownload(points);
				}
			}
			break;
		case 1: {
			// int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] &
			// 0x00ff);
			// if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
			// Point coordPoint =
			// MessageMgr.INSTANCE.analyseCurCoord(revBuffer);
			//
			// } else if (revBuffer[2] == 0x4A) {// 获取下位机参数成功
			// ToastUtil.showToast(GlueDownloadActivity.this, "获取参数成功!");
			// }
			// for(int i=0;i<revBuffer.length;i++){
			// Log.d(TAG, ""+revBuffer[i]);
			// }
			if (revBuffer[3] == 0x30) {
				Log.e(TAG, "询问");
			} else if (revBuffer[3] == 0x52) {
				Log.e(TAG, "下载预处理");
				ToastUtil.displayPromptInfo(this, "正在下载..");
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			}
			if ((revBuffer[3] & 0x00ff) == 0x31) {
				if ((revBuffer[5] & 0x00ff) == 1) {
					Log.d(TAG, "任务存在");
					// 任务存在的话，需要给个提示框
					displayTaskNumExist();
				}
			}
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务上传失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务下载失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(GlueDownloadActivity.this, "未知错误");
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
				disPlayInfoAfterGetMsg(buffer);
				// new ManagingMessage().execute(buffer);
			}
		}
	}

}
