/**
 * 
 */
package com.mingseal.activity;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.mingseal.adapter.TaskListBaseAdapter;
import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.dao.PointDao;
import com.mingseal.data.dao.PointTaskDao;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointTask;
import com.mingseal.dhp.R;
import com.mingseal.utils.CustomProgressDialog;
import com.mingseal.utils.CustomUploadDialog;
import com.mingseal.utils.DateUtil;
import com.mingseal.utils.FileDatabase;
import com.mingseal.utils.ParamsSetting;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.UploadTaskAnalyse;
import com.mingseal.view.TrackView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 商炎炳
 * @description 普通用户登录主界面
 */
public class TaskListUserActivity extends Activity implements OnClickListener {

	private final static String TAG = "TaskListUserActivity";
	/**
	 * 从TaskListActivity开始的requestCode
	 */
	public final static int TASK_USER_RequestCode = 0x00000001;
	/**
	 * 从TaskListActivity结束的resultCode
	 */
	public final static int TASK_USER_ResultCode = 0x00000002;
	/**
	 * 保存任务的key
	 */
	public final static String TASK_KEY = "com.mingseal.tasklistuseractivity.task.key";
	public final static String TASK_POINT_LIST = "com.mingseal.tasklistuseractivity.task.point.list";

	/**
	 * 任务点的个数最大值
	 */
	public final static int TASK_MAX_SIZE = 5000;
	/**
	 * 任务点的个数，0代表超过最大值，取内存中的数，1代表没有超过，取Bundle中的数
	 */
	public final static String TASK_NUMBER_TYPE = "com.mingseal.tasklistactivity.number.key";
	/**
	 * 任务列表
	 */
	private ListView lv_task;
	/**
	 * 搜索框
	 */
	private EditText et_search;
	/**
	 * 自定义的绘图区间
	 */
	private TrackView view_track;
	/**
	 * 下载
	 */
	private RelativeLayout rl_download;
	/**
	 * 导出
	 */
	private RelativeLayout rl_export;
	/**
	 * 删除
	 */
	private RelativeLayout rl_delete;
	/**
	 * 上传
	 */
	private RelativeLayout rl_uploading;
	/**
	 * 设置
	 */
	private RelativeLayout rl_setting;
	/**
	 * 任务时长
	 */
	private TextView tv_totalTime;
	/**
	 * 对话框中的自定义View
	 */
	private View customView;
	/**
	 * 对话框中的自定义View中的Edittext
	 */
	private EditText et_title;
	/**
	 * 对话框中的自定义上传的View中的上传任务号
	 */
	private EditText et_upload_number;
	/**
	 * 对话框中的自定义上传的View中的保存任务名
	 */
	private EditText et_upload_name;
	/**
	 * 判断dialog需不需要关闭
	 */
	private Field field;

	private TaskListBaseAdapter mTaskAdapter;
	private List<PointTask> taskLists;
	private PointTask task;

	/**
	 * 和ListView中的position相对应
	 */
	private int pselect = 0;
	private PointTaskDao taskDao;
	private PointDao pointDao;
	/**
	 * 从Task中获取的Point的集合
	 */
	private List<Point> points;

	private Intent intent;

	private RevHandler handler;
	/**
	 * 保存全局变量
	 */
	private UserApplication userApplication;// 保存全局变量
	/**
	 * 有进度条的对话框
	 */
	private CustomProgressDialog progressDialog = null;
	/**
	 * 没有进度条的对话框
	 */
	private CustomUploadDialog uploadDialog = null;
	/**
	 * 用于upload上传保存的point列表
	 */
	private List<Point> pointUploads;
	/**
	 * @Fields iv_connect_tip: 连接信息
	 */
	private ImageView iv_connect_tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_list_user);

		// initTaskList();
		// 初始化
		userApplication = (UserApplication) getApplication();
		SharePreferenceUtils.setSharedPreference(this);

		initView();

		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);

		taskDao = new PointTaskDao(this);
		pointDao = new PointDao(this);
		taskLists = taskDao.findALLTaskLists();
		// Log.d(TAG, taskLists.toString());
		mTaskAdapter = new TaskListBaseAdapter(this);
		mTaskAdapter.setTaskList(taskLists);
		lv_task.setAdapter(mTaskAdapter);
		if (taskLists.size() == 0) {
			showAndHideLayout(false);
		} else {
			invalidateCustomView(mTaskAdapter.getItem(pselect), pointDao);
		}
		// ListView的点击事件
		lv_task.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				pselect = position;
				mTaskAdapter.setSelectItem(position);
				mTaskAdapter.notifyDataSetInvalidated();

				Log.d(TAG, mTaskAdapter.getItem(position).toString());

				invalidateCustomView(mTaskAdapter.getItem(position), pointDao);
			}
		});
		// 长按事件修改任务名
		lv_task.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				pselect = position;
				mTaskAdapter.setSelectItem(position);
				mTaskAdapter.notifyDataSetInvalidated();

				Log.d(TAG, mTaskAdapter.getItem(position).toString());

				invalidateCustomView(mTaskAdapter.getItem(position), pointDao);
				showModifyDialog();

				return false;
			}
		});
		
		//搜索
		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mTaskAdapter.performFiltering(s);
				// 搜索的时候实时更新绘图界面
				invalidateCustomView(mTaskAdapter.getItem(mTaskAdapter.getSelectItem()), pointDao);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Activity结束需要关闭进度条对话框
		stopProgressDialog();
		stopUploadDialog();
		Log.e(TAG, "TaskListUserActivity-->onDestroy");
		SocketThreadManager.releaseInstance();
	}

	/**
	 * 绘制左边的自定义视图
	 * 
	 * @param pointTask
	 * @param pointDao
	 */
	private void invalidateCustomView(PointTask pointTask, PointDao pointDao) {
		new InvalidateViewAsynctask().execute(pointTask, pointDao);

		tv_totalTime.setText(pointTask.getId() * 10 + "");
	}

	/**
	 * 画图操作放到异步线程中去
	 *
	 */
	private class InvalidateViewAsynctask extends AsyncTask<Object, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			// 设置任务点
			view_track.setPointTask((PointTask) params[0], (PointDao) params[1]);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// 重绘
				view_track.invalidate();
			}
		}

	}

	/**
	 * 加载自定义组件
	 */
	private void initView() {
		lv_task = (ListView) findViewById(R.id.lv_task);
		et_search = (EditText) findViewById(R.id.et_Search);
		view_track = (TrackView) findViewById(R.id.view_track);
		tv_totalTime = (TextView) findViewById(R.id.tv_totaltime);

		rl_download = (RelativeLayout) findViewById(R.id.rl_xiazai);
		rl_export = (RelativeLayout) findViewById(R.id.rl_daochu);
		rl_delete = (RelativeLayout) findViewById(R.id.rl_shanchu);
		rl_uploading = (RelativeLayout) findViewById(R.id.rl_shangchuan);
		rl_setting = (RelativeLayout) findViewById(R.id.rl_shezhi);

		rl_download.setOnClickListener(this);
		rl_export.setOnClickListener(this);
		rl_delete.setOnClickListener(this);
		rl_uploading.setOnClickListener(this);
		rl_setting.setOnClickListener(this);

		view_track.setCircle(50);
		view_track.setRadius(5);
		
		iv_connect_tip = (ImageView) findViewById(R.id.iv_connect_tip);
	}

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		if (_requestCode == TASK_USER_RequestCode) {
			if(_resultCode == TaskActivity.resultDownLoadCode){
				Log.d(TAG, "从DownloadActivity返回结果");
			}
		}
	}

	/**
	 * true显示所有,false隐藏不该显示的
	 * 
	 * @param showFlag
	 */
	private void showAndHideLayout(boolean showFlag) {
		rl_delete.setEnabled(showFlag);
		rl_download.setEnabled(showFlag);
	}


	/**
	 * 新建对话框,修改任务名之后跳转到新建点界面
	 */
	private void showModifyDialog() {
		AlertDialog.Builder builderModify = new AlertDialog.Builder(TaskListUserActivity.this);
		builderModify.setTitle("修改任务名");
		customView = View.inflate(TaskListUserActivity.this, R.layout.custom_dialog_edittext, null);
		builderModify.setView(customView);
		et_title = (EditText) customView.findViewById(R.id.et_title);
		Log.d(TAG, "showModifyDialog中的：" + taskLists.get(pselect).toString());
		et_title.setText(taskLists.get(pselect).getTaskName());
		et_title.setSelectAllOnFocus(true);
		builderModify.setNegativeButton("取消", new DialogInterface.OnClickListener() {

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
		builderModify.setPositiveButton("修改", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				}
				if ("".equals(et_title.getText().toString())) {
					ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务名不能为空！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				} else {
					try {
						field.set(dialog, true);// true表示要关闭
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
					task = taskLists.get(pselect);
					task.setTaskName(et_title.getText().toString());
					taskDao.updateTask(task);
					taskLists = taskDao.findALLTaskLists();
					mTaskAdapter.setTaskList(taskLists);
					mTaskAdapter.notifyDataSetChanged();
				}

			}
		});
		builderModify.show();
	}

	/**
	 * 新建对话框，确认是否删除
	 */
	private void showDeleteDialog() {
		AlertDialog.Builder buildDelete = new AlertDialog.Builder(TaskListUserActivity.this);
		buildDelete.setTitle("删除任务");
		buildDelete.setMessage("是否删除任务：" + taskLists.get(pselect).getTaskName() + " ?");
		buildDelete.setNegativeButton("取消", null);
		buildDelete.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// taskLists.remove(pselect);
				PointTask subTask = taskLists.get(pselect);
				taskDao.deleteTask(subTask);
				// 删除任务时,将任务点也跟着删除
				pointDao.deletePointsByIds(subTask.getPointids());
				taskLists = taskDao.findALLTaskLists();

				mTaskAdapter.setTaskList(taskLists);
				Log.d(TAG, "taskLists.size():" + taskLists.size() + ",pselect:" + pselect);
				if (taskLists.size() == 0) {
					showAndHideLayout(false);
				} else {
					showAndHideLayout(true);
				}
				// 超过范围，要减少pselect的值
				if (pselect >= taskLists.size()) {
					pselect = pselect - 1;
				}
				mTaskAdapter.setSelectItem(pselect);
				if (pselect < 0) {
					invalidateCustomView(new PointTask(), pointDao);
				} else {
					invalidateCustomView(mTaskAdapter.getItem(pselect), pointDao);
				}
				mTaskAdapter.notifyDataSetChanged();

			}
		});
		buildDelete.show();
	}
	
	/**
	 * 新建对话框，选中上传任务号
	 */
	private void showUploadDialog() {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = null;
		View view = LayoutInflater.from(TaskListUserActivity.this).inflate(R.layout.custom_dialog_upload, null);
		builder = new AlertDialog.Builder(TaskListUserActivity.this);
		builder.setView(view);
		builder.setTitle("上传任务");
		et_upload_number = (EditText) view.findViewById(R.id.upload_task_number);
		et_upload_name = (EditText) view.findViewById(R.id.upload_task_name);

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

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
		builder.setPositiveButton("上传", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				}
				if ("".equals(et_upload_name.getText().toString())
						|| "".equals(et_upload_number.getText().toString())) {
					ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务号和任务名都不能为空！");

					try {
						field.set(dialog, false);// true表示要关闭
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					try {
						field.set(dialog, true);// true表示要关闭
					} catch (Exception e) {
						e.printStackTrace();
					}
					int taskNum = Integer.parseInt(et_upload_number.getText().toString());
					OrderParam.INSTANCE.setAllParamToZero();
					OrderParam.INSTANCE.setnTaskNum(taskNum);
					pointUploads = new ArrayList<>();
					Log.d(TAG, "上传之前:" + DateUtil.getCurrentTime());
					startUpLoadDialog();
					MessageMgr.INSTANCE.taskUpload(pointUploads);
				}
			}
		});
		dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * 解析下载成功的任务
	 * 
	 * @param pointUploads
	 */
	private void analyseTaskSuccess(List<Point> pointUploads) {
		//上传成功里面的Point的List数组
		List<Point> points = new ArrayList<>();
		UploadTaskAnalyse uploadAnalyse =new UploadTaskAnalyse(TaskListUserActivity.this);
		Log.d(TAG, "解析之前："+DateUtil.getCurrentTime());
		points = uploadAnalyse.analyseTaskSuccess(pointUploads);
		Log.d(TAG, "解析之后："+DateUtil.getCurrentTime());
		//往界面上添加（暂时先删了）
		//先往数据库里面添加，然后再将Point的id保存出来
		List<Integer> ids = pointDao.insertPoints(points);
		task = new PointTask();
		task.setPointids(ids);
		task.setTaskName(et_upload_name.getText().toString());
		int id = (int) taskDao.insertTask(task);
		task.setId(id);
		taskLists.add(task);
		mTaskAdapter.setTaskList(taskLists);
		// 设置刚添加的被选中
		pselect = taskLists.size() - 1;
		mTaskAdapter.setSelectItem(pselect);
		// 滚动到最底部
		lv_task.smoothScrollToPosition(pselect);
		showAndHideLayout(true);
		mTaskAdapter.notifyDataSetChanged();
		invalidateCustomView(taskLists.get(pselect), pointDao);
		///////////////////////
		//全部更新完之后关闭进度框
		stopUploadDialog();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_xiazai:// 下载

			//任务点的主键集合
			List<Integer> ids = taskLists.get(pselect).getPointids();
			if(ids.isEmpty()){
				ToastUtil.displayPromptInfo(this, "任务点不能为空");
			}else{
				CheckUpHeightAsynctask check = new CheckUpHeightAsynctask();
				check.execute(ids);
			}

			break;
		case R.id.rl_daochu:// 导出
			FileDatabase.exportToFile(this);
			break;
		case R.id.rl_shanchu:// 删除

			showDeleteDialog();

			break;
		case R.id.rl_shangchuan:// 上传
			showUploadDialog();
			break;
		case R.id.rl_shezhi:// 设置
			intent = new Intent(this, MainUserSettingActivity.class);
			startActivityForResult(intent, TASK_USER_RequestCode);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			break;

		}
	}

	/**
	 * <p>
	 * Title: DisPlayInfoAfterGetMsg
	 * <p>
	 * Description: 显示收到的数据信息
	 * 
	 * @param revBuffer
	 */
	private void disPlayInfoAfterGetMsg(byte[] revBuffer) {
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
		case 0:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "校验失败");
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			if (revBuffer[2] == 0x4A) {// 获取下位机参数成功
				ToastUtil.displayPromptInfo(TaskListUserActivity.this, "获取参数成功!");
				iv_connect_tip.setImageDrawable(getResources().getDrawable(R.drawable.icon_wifi_connect));
				userApplication.setWifiConnecting(true);
				Log.d(TAG,
						RobotParam.INSTANCE.GetXJourney() + ",分辨率：x" + RobotParam.INSTANCE.GetXDifferentiate() + ",y:"
								+ RobotParam.INSTANCE.GetYDifferentiate() + ",z:"
								+ RobotParam.INSTANCE.GetZDifferentiate());
			}
		}
			break;
		case 8421:
			// 任务上传分包数据,不作处理
			break;
		case 1248:
			// 下载成功
			if ((revBuffer[3] & 0x00ff) == 0xe6) {
				if (!pointUploads.isEmpty() && pointUploads.size() > 0) {
					Log.d(TAG, "上传的列表长度:" + pointUploads.size() + "--" + et_upload_name.getText().toString());
					// for(Point point:pointUploads){
					// Log.d(TAG, point.toString());
					// }
					analyseTaskSuccess(pointUploads);
				}
				ToastUtil.displayPromptInfo(TaskListUserActivity.this, "上传完成");
			}
			break;
		case 1249:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "上传失败");
			break;
		case 40101:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务下载失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务上传失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(TaskListUserActivity.this, "未知错误");
			break;
		}
	}

	/**
	 * <p>
	 * Title: RevHandler
	 * <p>
	 * Description: 数据接收Handler
	 * <p>
	 * Company: MingSeal .Ltd
	 * 
	 * @author lyq
	 * @date 2015年11月6日
	 */
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
			}else if(msg.what == SocketInputThread.SocketInputUPLOADWhat){
				//获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer) msg.obj;
				byte[] buffer;
				buffer = temp.array();
				disPlayInfoAfterGetMsg(buffer);
			}
		}
	}
	
	/**
	 * 打开进度条对话框
	 */
	private void startProgressDialog(){
		if(progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在设置抬起高度...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * 关闭进度条对话框
	 */
	private void stopProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/**
	 * 打开没有进度条的对话框
	 */
	private void startUpLoadDialog(){
		if(uploadDialog == null){
			uploadDialog = CustomUploadDialog.createDialog(this);
			uploadDialog.setMessage("正在上传中..");
			uploadDialog.setCanceledOnTouchOutside(false);
		}
		uploadDialog.show();
	}
	
	/**
	 * 关闭没有进度条的对话框
	 */
	private void stopUploadDialog(){
		if(uploadDialog!=null){
			uploadDialog.dismiss();
			uploadDialog =null;
		}
	}
	
	/**
	 * 检查抬起高度，往GlueDownloadActivity中传因为要从数据库中读取数据，所以比较耗时，把它放到异步线程中
	 *
	 */
	private class CheckUpHeightAsynctask extends AsyncTask<List<Integer>, Integer, Boolean>{

		private ParamsSetting paramSetting;
		@Override
		protected void onPreExecute() {
			startProgressDialog();
			paramSetting = new ParamsSetting(TaskListUserActivity.this);
		}
		
		@Override
		protected Boolean doInBackground(List<Integer>... params) {
			int max = params[0].size();
			progressDialog.setMax(max);
			publishProgress(0);
			points = pointDao.findALLPointsByIdLists(params[0]);
			publishProgress(max/3*2);
			paramSetting.setParamsToApplication(userApplication, points);
			publishProgress(max);
			
			return true;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			stopProgressDialog();
			if(result){
				Bundle extras = new Bundle();
				if(points.size()>TaskActivity.MAX_SIZE){
					extras.putString(TaskActivity.KEY_NUMBER, "0");
					userApplication.setPoints(points);
				}else{
					extras.putString(TaskActivity.KEY_NUMBER, "1");
					extras.putParcelableArrayList(TaskActivity.ARRAY_KEY, (ArrayList<? extends Parcelable>) points);
				}
				Intent _intent = new Intent(TaskListUserActivity.this, GlueDownloadActivity.class);
				extras.putString(TaskActivity.DOWNLOAD_NUMBER_KEY, taskLists.get(pselect).getTaskName());
				_intent.putExtras(extras);
				startActivityForResult(_intent, TASK_USER_RequestCode);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			}
		}
		
	}

}
