package com.mingseal.activity;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.mingseal.adapter.TaskMainBaseAdapter;
import com.mingseal.adapter.TaskMainBaseAdapter.onMyCheckboxChangedListener;
import com.mingseal.adapter.TaskMainBaseAdapter.onMyRadioButtonChangedListener;
import com.mingseal.application.UserApplication;
import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.dao.GlueAloneDao;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.dao.GlueFaceStartDao;
import com.mingseal.data.dao.GlueLineEndDao;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.dao.GlueLineStartDao;
import com.mingseal.data.dao.PointDao;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.CmdParam;
import com.mingseal.data.param.DownloadParam;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.param.SettingParam;
import com.mingseal.data.param.TaskParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointTask;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.SMatrix1_4;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.protocol.Protocol_400_1;
import com.mingseal.dhp.R;
import com.mingseal.listener.MyPopWindowClickListener;
import com.mingseal.utils.AsyncConnection;
import com.mingseal.utils.CommonArithmetic;
import com.mingseal.utils.CustomProgressDialog;
import com.mingseal.utils.CustomUploadDialog;
import com.mingseal.utils.DateUtil;
import com.mingseal.utils.FileDatabase;
import com.mingseal.utils.MoveUtils;
import com.mingseal.utils.ParamsSetting;
import com.mingseal.utils.PointCopyTools;
import com.mingseal.utils.SharePreferenceUtils;
import com.mingseal.utils.ToastUtil;
import com.mingseal.utils.WifiConnectTools;
import com.mingseal.view.MyCircleView;
import com.mingseal.view.MyCircleView.Dir;
import com.mingseal.view.MyCircleView.onActivityCallBackListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * @author 商炎炳
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class TaskActivity extends Activity implements OnClickListener {

	private static final String TAG = "TaskActivity";
	/**
	 * 判断Point的List数组的长度,大于这个数时保存在内存中,小于的话用Bundle保存
	 */
	public final static int MAX_SIZE = 3000;

	public final static int requestCode = 0x00000101;
	public final static int resultCode = 0x00000102;
	/**
	 * 视图GlueViewActivity返回的resultCode
	 */
	public final static int resultViewCode = 0x00000103;
	/**
	 * 阵列GlueArrayActivity返回的resultCode
	 */
	public final static int resultArrayCode = 0x00000104;
	/**
	 * 偏移GlueOffsetActivity返回的resultCode
	 */
	public final static int resultOffsetCode = 0x00000105;

	/**
	 * 任务设置GlueTaskSettingActivity返回的resultCode
	 */
	public final static int resultSettingCode = 0x00000106;
	/**
	 * 下载GlueDownloadActivity返回的resultCode
	 */
	public final static int resultDownLoadCode = 0x00000107;
	/**
	 * GlueCameraActivity返回的resultCode
	 */
	public final static int resultCameraCode = 0x00000108;

	/**
	 * 传递时保存的number key,用于判断是大数据还是平常的数据(0代表大数据,1代表平常数据)
	 */
	public final static String KEY_NUMBER = "com.mingseal.taskactivity.number.key";
	/**
	 * 视图保存的key
	 */
	public final static String VIEW_KEY = "com.mingseal.taskactivity.view.key";
	/**
	 * 阵列保存的key
	 */
	public final static String ARRAY_KEY = "com.mingseal.taskactivity.array.key";
	/**
	 * 偏移保存的key
	 */
	public final static String OFFSET_KEY = "com.mingseal.taskactivity.offset.key";
	/**
	 * 保存下载的任务号
	 */
	public final static String DOWNLOAD_NUMBER_KEY = "com.mingseal.taskactivity.download.number.key";
	/**
	 * 下载保存的List<Point> key
	 */
	public final static String DOWNLOAD_KEY = "com.mingseal.taskactivity.downloading.key";
	/**
	 * 下载保存的DownloadParam key
	 */
	public final static String DOWNLOAD_PARAM_KEY = "com.mingseal.taskactivity.downloading.param.key";
	/**
	 * GlueCameraActivity保存的key
	 */
	public final static String CAMERA_KEY = "com.mingseal.taskactivity.camera.key";
	private TextView tv_title;// 标题栏
	private RelativeLayout rl_back;// 返回
	private RelativeLayout rl_title_speed;// 标题栏的速度布局
	private RelativeLayout rl_title_moshi;// 标题栏的模式布局
	private RelativeLayout rl_title_wifi_connecting;// wifi连接情况
	private ImageView iv_wifi_connecting;//wifi连接情况
	private ImageView image_speed;// 标题栏的速度
	private ImageView image_moshi;// 标题栏的模式

	private RelativeLayout rl_sudu;// 速度按钮
	private RelativeLayout rl_dingwei;// 定位按钮
	private RelativeLayout rl_pianyi;// 偏移按钮
	private RelativeLayout rl_moni;// 模拟按钮
	private RelativeLayout rl_shijue;// 视觉按钮

	private RelativeLayout rl_moshi;// 模式按钮
	private RelativeLayout rl_fuwei;// 复位按钮
	private RelativeLayout rl_zhenlie;// 阵列按钮
	private RelativeLayout rl_zhantie;// 粘贴按钮
	private RelativeLayout rl_shitu;// 视图按钮

	private RelativeLayout rl_xiazai;// 下载按钮
	private RelativeLayout rl_shanchu;// 删除按钮
	private RelativeLayout rl_quanxuan;// 全选按钮
	private RelativeLayout rl_shezhi;// 任务设置按钮
	private RelativeLayout rl_fangan;// 方案按钮

	private TextView tv_speed;// 速度按钮对应的文本
	private TextView tv_moshi;// 速度按钮对应的文本
	private Switch singleSwitch;// switch按钮
	private Button but_xuhao;// 序号按钮
	
	/************************ add begin ************************/
	private int orderLength = 0;
	private byte[] buffer;
	private final int ORDER_BUFFER_LENTH = 100;
	private Protocol_400_1 protocol = null;
	/************************ end ******************************/

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
	private ListView mList;
	private LinearLayout mHeaderView;
	private LinearLayout lin_add;// add按钮
	private LinearLayout lin_pop;// popwindow
	private TextView headUTv;
	private TextView tv_selectAll;//全选文本
	public MyPopWindowClickListener popMenu;// 弹出选择框
	public PopupWindow mPopupWindow; // 弹出框

	private TaskMainBaseAdapter mAdapter;

	/**
	 * 该列表会进行增删改查的操作
	 */
	private List<Point> mPointsCur = new ArrayList<Point>();
	/**
	 * 用于存放刚开始保存的数据列表，程序刚进来时保存，退出activity时会删除
	 */
	private List<Point> mPointStorages = new ArrayList<Point>();
	private Point point;
	/**
	 * @Fields mIsFirst: 是否第一次初始化弹出框
	 */
	private boolean mIsFirst = true; // 是否第一次初始化弹出框
	
	/**
	 * @Fields isSelectAll: 是否全选,true为全选,false为全不选
	 */
	private boolean isSelectAll = true;

	private int speedFlag = 0;// 判断点击了几次，三次一循环，分别为高速33,中速13,低速3
	private int speed;
	private int[] speedXYZ;// 单步的话保存x,y,z的坐标

	/**
	 * 单选框选中的ID
	 */
	private int selectRadioIDCur;// 单选框选中的ID
	/**
	 * 单选框上次选中的ID(判断是否需要定位)
	 */
	private int selectRadioIDPrev = -1;// 单选框上次选中的ID(判断是否需要定位)

	// private String selectCheckboxID = "";// checkbox选中的ID
	/**
	 * checkbox选中的ID
	 */
	private List<Integer> selectCheckboxCur;
	/**
	 * 偏移时保存的selectCheckboxID
	 */
	private List<Integer> offsetCheckBox;

	// private String offsetCheckBoxID = "";// 偏移时保存的selectCheckboxID
	/**
	 * checkbox选中ID的最后一位(阵列回调时添加数据)
	 */
	private int sCheckViewIDLast;// checkbox选中ID的最后一位

	private PointDao pointDao;

	/**
	 * 0代表是从TaskActivity中传值过去的，要加一条数据，1代表是从TaskMainBaseAdapter传值过去的，要更新数据
	 */
	private int mFlag = 0;

	/**
	 * 接收从TaskListActivity传过来的Intent
	 */
	private Intent intent;// 接收从之前页面传过来的Intent
	/**
	 * 接收从TaskListActivity传过来的PointTask
	 */
	private PointTask task;// 接收从之前页面传过来的PointTask
	private String task_type;// 接收从之前页面传过来的type
	/**
	 * Point表的主键集合
	 */
	private List<Integer> rowids;// 插入Point表的主键集合
	/**
	 * @Fields m_nAxisNum : 机器轴数
	 */
	private int m_nAxisNum;

	/**
	 * 点击阵列的Intent
	 */
	private Intent arrayIntent;

	private RevHandler handler;

	private SettingParam settingParam;// 任务设置参数
	/**
	 * 连续0(默认),单步是1
	 */
	private int modeFlagCur = 0;// 连续单步判断标志
	/**
	 * 用来改变TaskMainBaseAdapter中的checkBox选中框
	 */
	private LinkedHashMap<Integer, Boolean> isSelected;

	/**
	 * 独立点的数据库操作
	 */
	private GlueAloneDao glueAloneDao;// 独立点的数据库操作
	/**
	 * 起始点的数据库操作
	 */
	private GlueLineStartDao glueLineStartDao;// 起始点的数据库操作
	/**
	 * 中间点的数据库操作
	 */
	private GlueLineMidDao glueLineMidDao;// 中间点的数据库操作
	/**
	 * 结束点的数据库操作
	 */
	private GlueLineEndDao glueLineEndDao;// 结束点的数据库操作
	/**
	 * 面起始点的数据库操作
	 */
	private GlueFaceStartDao glueFaceStartDao;// 面起始点的数据库操作
	/**
	 * 面结束点的数据库操作
	 */
	private GlueFaceEndDao glueFaceEndDao;// 面结束点的数据库操作
	private UserApplication userApplication;// 保存全局的变量
	private CustomProgressDialog progressDialog = null;
	/**
	 * 没有进度条的对话框
	 */
	private CustomUploadDialog uploadDialog = null;
	/**
	 * @Fields isChange: 判断List是否改变,true为没有变化,false表示有变化
	 */
	private boolean isChange = true;
	private  boolean prepareReset=false;//判断是否是按下复位按键

	/**
	 * 判断是否是第一次打开popwindow
	 * 
	 * @return
	 */
	public boolean ismIsFirst() {
		return mIsFirst;
	}

	/**
	 * 设置打开popwindow的开关
	 * 
	 * @param mIsFirst
	 */
	public void setmIsFirst(boolean mIsFirst) {
		this.mIsFirst = mIsFirst;
	}

	/**
	 * 取得选中的那一行的id
	 * 
	 * @return
	 */
	public int getSelectRadioID() {
		return selectRadioIDCur;
	}

	/**
	 * 设置当前选中的radioButton的id
	 * 
	 * @param selectRadioID
	 */
	public void setSelectRadioID(int selectRadioID) {
		this.selectRadioIDCur = selectRadioID;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_main);
		/************************ add begin ************************/
		protocol = new Protocol_400_1();
		/************************ end ******************************/

		// 初始化
		userApplication = (UserApplication) getApplication();
		intent = getIntent();
		task_type = intent.getStringExtra(TaskListActivity.TASK_NUMBER_TYPE);
		if ("0".equals(task_type)) {
			// 取内存中的数
			task = userApplication.getPointTask();
		} else {
			task = intent.getParcelableExtra(TaskListActivity.TASK_KEY);
		}
		Log.d(TAG, "接收到的task:" + task.toString());

		settingParam = SharePreferenceUtils.readFromSharedPreference(this);

		mList = (ListView) findViewById(R.id.lv_show);
		mHeaderView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_task_main_listview_header,
				null);
		headUTv = (TextView) mHeaderView.findViewById(R.id.tv_u);
		mList.addHeaderView(mHeaderView);

		pointDao = new PointDao(TaskActivity.this);
		mAdapter = new TaskMainBaseAdapter(this, TaskActivity.this);
		new GetPointsAsynctask().execute(task.getPointids());

		handler = new RevHandler();
		// 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);

		initComponent();
		initDao();
		tv_title.setText(task.getTaskName());

		singleSwitch.setOnCheckedChangeListener(new myCheckedChangeListener());

		m_nAxisNum = RobotParam.INSTANCE.getM_nAxisNum();
		mAdapter.setM_nAxisNum(m_nAxisNum);
		// 三轴的话，将U轴隐藏掉
		if (m_nAxisNum == 3) {
			headUTv.setVisibility(View.GONE);
			but_u_minus.setEnabled(false);
			but_u_plus.setEnabled(false);
//			myCircleDown.setRow("");
		} else {
			headUTv.setVisibility(View.VISIBLE);
		}
		// MessageMgr.INSTANCE.setUserApplication(userApplication);
		// 初始化
		selectCheckboxCur = new ArrayList<Integer>();
		offsetCheckBox = new ArrayList<Integer>();

		// 监听checkbox的选中事件
		mAdapter.setOnCheckboxChanged(new onMyCheckboxChangedListener() {

			@Override
			public void setCheckBoxSelected(LinkedHashMap<Integer, Boolean> isSelected) {
				Log.d(TAG, "HashMap<Integer, Boolean> isSelected-->" + isSelected);
				// selectCheckboxID = "";
				selectCheckboxCur = new ArrayList<Integer>();
				Iterator<Entry<Integer, Boolean>> iterator = isSelected.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<Integer, Boolean> entry = iterator.next();
					if (entry.getValue()) {// 为true的话取出key值
						Log.d(TAG, "entry.getKey():" + entry.getKey());
						// selectCheckboxID = selectCheckboxID + entry.getKey()
						// + ",";
						selectCheckboxCur.add(entry.getKey());
					}
					mAdapter.setSelectCheckIDS(selectCheckboxCur);
					mAdapter.notifyDataSetChanged();
				}
			}
		});

		// 监听radiobutton的选中事件
		mAdapter.setOnRadioButtonChanged(new onMyRadioButtonChangedListener() {

			@Override
			public void setSelectID(int selectID) {
				mAdapter.setSelectID(selectID);// 选中位置
				selectRadioIDCur = selectID;
				Log.d(TAG, "mRobots.get(selectID):" + mPointsCur.get(selectID).toString());
				mAdapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		// handler = new RevHandler();
		// // 线程管理单例初始化
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// Activity结束需要关闭进度条对话框
		stopProgressDialog();
		// Activity结束需要关闭没有进度条对话框
		stopUploadDialog();
		super.onDestroy();
	}

	/**
	 * 加载组件
	 */
	private void initComponent() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		rl_title_speed = (RelativeLayout) findViewById(R.id.rl_title_speed);
		rl_title_moshi = (RelativeLayout) findViewById(R.id.rl_title_moshi);
		rl_title_wifi_connecting = (RelativeLayout) findViewById(R.id.rl_title_wifi_connecting);
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
		lin_add = (LinearLayout) findViewById(R.id.lin_add);
		// 弹出对话框
		lin_add.setOnClickListener(this);

		rl_sudu = (RelativeLayout) findViewById(R.id.rl_sudu);
		rl_dingwei = (RelativeLayout) findViewById(R.id.rl_dingwei);
		rl_pianyi = (RelativeLayout) findViewById(R.id.rl_pianyi);
		rl_moni = (RelativeLayout) findViewById(R.id.rl_moni);
		rl_shijue = (RelativeLayout) findViewById(R.id.rl_shijue);

		rl_moshi = (RelativeLayout) findViewById(R.id.rl_moshi);
		rl_fuwei = (RelativeLayout) findViewById(R.id.rl_fuwei);
		rl_zhenlie = (RelativeLayout) findViewById(R.id.rl_zhenlie);
		rl_zhantie = (RelativeLayout) findViewById(R.id.rl_zhantie);
		rl_shitu = (RelativeLayout) findViewById(R.id.rl_shitu);

		rl_xiazai = (RelativeLayout) findViewById(R.id.rl_xiazai);
		rl_shanchu = (RelativeLayout) findViewById(R.id.rl_shanchu);
		rl_quanxuan = (RelativeLayout) findViewById(R.id.rl_xiugai);
		rl_shezhi = (RelativeLayout) findViewById(R.id.rl_shezhi);
		rl_fangan = (RelativeLayout) findViewById(R.id.rl_fangan);

		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		tv_speed = (TextView) rl_sudu.findViewById(R.id.tv_speed);
		tv_moshi = (TextView) findViewById(R.id.tv_moshi);

		rl_sudu.setOnClickListener(this);
		rl_dingwei.setOnClickListener(this);
		rl_pianyi.setOnClickListener(this);
		rl_moni.setOnClickListener(this);
		rl_shijue.setOnClickListener(this);

		rl_moshi.setOnClickListener(this);
		rl_fuwei.setOnClickListener(this);
		rl_zhenlie.setOnClickListener(this);
		rl_zhantie.setOnClickListener(this);
		rl_shitu.setOnClickListener(this);

		rl_xiazai.setOnClickListener(this);
		rl_shanchu.setOnClickListener(this);
		rl_quanxuan.setOnClickListener(this);
		rl_shezhi.setOnClickListener(this);
		rl_fangan.setOnClickListener(this);

		rl_back.setOnClickListener(this);
		
		tv_selectAll = (TextView) findViewById(R.id.tv_selectall);

		rl_shanchu.setEnabled(false);// 刚开始设置不能点击
		rl_zhenlie.setEnabled(false);
		rl_pianyi.setEnabled(false);
		rl_zhantie.setEnabled(false);
		rl_quanxuan.setEnabled(false);

		but_xuhao = (Button) findViewById(R.id.but_xuhao);

		singleSwitch = (Switch) findViewById(R.id.sw_single);
		
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

		speed = settingParam.getHighSpeed();
		speedXYZ = new int[3];
		speedXYZ[0] = 4 * settingParam.getxStepDistance();
		speedXYZ[1] = 4 * settingParam.getyStepDistance();
		speedXYZ[2] = 4 * settingParam.getzStepDistance();
	}

	/**
	 * 加载自定义的默认初始数据
	 */
	private void initListData() {
		mPointsCur = new ArrayList<Point>();
		Point point;
		for (int i = 0; i < 20; i++) {
			point = new Point(12, 58, 120, 34, PointType.POINT_GLUE_BASE);
			mPointsCur.add(point);
		}
	}

	/**
	 * 加载自定义的Dao
	 */
	private void initDao() {
		glueAloneDao = new GlueAloneDao(this);
		glueLineStartDao = new GlueLineStartDao(this);
		glueLineMidDao = new GlueLineMidDao(this);
		glueLineEndDao = new GlueLineEndDao(this);
		glueFaceStartDao = new GlueFaceStartDao(this);
		glueFaceEndDao = new GlueFaceEndDao(this);
	}

	/**
	 * @ClassName MoveListener
	 * @Description 触摸移动x,y,z,u
	 * @author 商炎炳
	 * @date 2016年1月28日 上午9:48:47
	 *
	 */
	private class MoveListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (selectRadioIDCur >= mPointsCur.size()) {
				selectRadioIDCur = mPointsCur.size() - 1;
				mAdapter.setSelectID(selectRadioIDCur);// 选中位置
			}
			if (selectRadioIDPrev == selectRadioIDCur && selectRadioIDCur != -1) {
				if (modeFlagCur == 0) {
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
				} else if (modeFlagCur == 1) {
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
			} else if (selectRadioIDPrev != selectRadioIDCur && selectRadioIDCur != -1) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// 按下定位
					MoveUtils.locationCoord(mPointsCur.get(selectRadioIDCur));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					// 抬起设置上次选中的单选框
					selectRadioIDPrev = selectRadioIDCur;
				}
			}
			mAdapter.notifyDataSetChanged();
			return false;
		}
		
	}
	
	/**
	 * 自定义Switch的事件监听
	 *
	 */
	private class myCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			if (isChecked) {
				// 选中为多，要把序号隐藏
				but_xuhao.setVisibility(View.GONE);
				rl_shanchu.setEnabled(true);
				rl_zhenlie.setEnabled(true);
				rl_pianyi.setEnabled(true);
				rl_zhantie.setEnabled(true);
				rl_quanxuan.setEnabled(true);
				rl_dingwei.setEnabled(false);
				rl_shijue.setEnabled(false);
				rl_fangan.setEnabled(false);
			} else {
				but_xuhao.setVisibility(View.VISIBLE);
				rl_shanchu.setEnabled(false);
				rl_zhenlie.setEnabled(false);
				rl_pianyi.setEnabled(false);
				rl_zhantie.setEnabled(false);
				rl_quanxuan.setEnabled(false);
				rl_dingwei.setEnabled(true);
				rl_shijue.setEnabled(true);
				rl_fangan.setEnabled(true);
				// 多选框回到单选框，selectID如果比list集合大的话，需要修改
				if (selectRadioIDCur >= mPointsCur.size()) {
					selectRadioIDCur = mPointsCur.size() - 1;
					mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				}
			}
			mAdapter.setSingleOrMultify(isChecked);
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获得某一个点的点参数的点类型
	 * 
	 * @param point
	 * @return point.getPointParam().getPointType()
	 */
	private PointType getPointType(Point point) {
		return point.getPointParam().getPointType();

	}

	/**
	 * 检查任务点的合法性
	 * 
	 * @param points
	 * @return true为合法的,false表示有不合法的点存在
	 */
	private boolean checkForValidity(List<Point> points) {
		if (points.size() == 0 || points == null) {
			ToastUtil.displayPromptInfo(this, getResources().getString(R.string.not_null));
			return false;
		}

		for (int i = 0; i < points.size(); i++) {
			if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_FACE_START)) {// 面起点
				if ((i + 1) >= points.size()) {
					ToastUtil.displayPromptInfo(this, "不能以面起点结尾");
					selectRadioIDCur = i;
					return false;
				} else {
					if (!getPointType(points.get(i + 1)).equals(PointType.POINT_GLUE_FACE_END)) {
						ToastUtil.displayPromptInfo(this, "面起点后面必须跟着面终点");
						selectRadioIDCur = i + 1;
						return false;
					}
				}
			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_FACE_END)) {// 面终点
				if (i <= 0) {
					ToastUtil.displayPromptInfo(this, "不能以面终点开始");
					selectRadioIDCur = i;
					return false;
				} else {
					if (!getPointType(points.get(i - 1)).equals(PointType.POINT_GLUE_FACE_START)) {
						ToastUtil.displayPromptInfo(this, "面终点前面必须跟着面起点");
						selectRadioIDCur = i - 1;
						return false;
					}
				}

			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_CLEAR)) {// 清胶点
				if ((i + 1) >= points.size()) {
					ToastUtil.displayPromptInfo(this, "不能以清胶点结尾");
					selectRadioIDCur = i;
					return false;
				} else {
					if (!getPointType(points.get(i + 1)).equals(PointType.POINT_GLUE_CLEARIO)) {
						ToastUtil.displayPromptInfo(this, "清胶点后面必须跟着清胶");
						selectRadioIDCur = i + 1;
						return false;
					}
				}
			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_CLEARIO)) {// 清胶
				if (i <= 0) {
					ToastUtil.displayPromptInfo(this, "不能以清胶开始");
					selectRadioIDCur = i;
					return false;
				} else {
					if (!getPointType(points.get(i - 1)).equals(PointType.POINT_GLUE_CLEAR)) {
						ToastUtil.displayPromptInfo(this, "清胶前面必须跟着清胶点");
						selectRadioIDCur = i - 1;
						return false;
					}
				}
			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_LINE_START)) {// 起始点
				if ((i + 1) >= points.size()) {
					ToastUtil.displayPromptInfo(this, "不能以起始点结尾");
					selectRadioIDCur = i;
					return false;
				} else {
					boolean isLineEnd = false;
					for (int j = i + 1; j < points.size(); j++) {
						if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_END)) {
							isLineEnd = true;
							break;
						} else if (!(getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_MID)
								|| getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_ARC))) {
							// 不是中间点或者是圆弧点
							ToastUtil.displayPromptInfo(this, "起始点后面不能跟除中间点和圆弧点之外其他的点");
							selectRadioIDCur = j;
							return false;
						}
					}
					if (!isLineEnd) {
						ToastUtil.displayPromptInfo(this, "起始点必须和结束点相对应出现");
						selectRadioIDCur = i;
						return false;
					}
				}
			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_LINE_END)) {// 结束点
				if (i <= 0) {
					ToastUtil.displayPromptInfo(this, "不能以结束点开始");
					selectRadioIDCur = i;
					return false;
				} else {
					boolean isLineStart = false;
					for (int j = i - 1; j >= 0; j--) {
						if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_START)) {
							isLineStart = true;
							break;
						} else if (!(getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_ARC)
								|| getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_MID))) {
							// 不是中间点或者是圆弧点
							ToastUtil.displayPromptInfo(this, "结束点前面不能跟除中间点和圆弧点之外其他的点");
							selectRadioIDCur = j;
							return false;
						}
					}
					if (!isLineStart) {
						ToastUtil.displayPromptInfo(this, "起始点必须和结束点相对应出现");
						selectRadioIDCur = i;
						return false;
					}
				}
			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_LINE_MID)) {// 中间点
				if (i <= 0) {
					ToastUtil.displayPromptInfo(this, "不能以中间点开始");
					selectRadioIDCur = i;
					return false;
				} else if ((i + 1) >= points.size()) {
					ToastUtil.displayPromptInfo(this, "不能以中间点结尾");
					selectRadioIDCur = i;
					return false;
				} else {
					boolean isLineStart = false;
					boolean isLineEnd = false;
					for (int j = i - 1; j >= 0; j--) {
						if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_START)) {
							isLineStart = true;
							break;
						}
					}
					for (int j = i + 1; j < points.size(); j++) {
						if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_END)) {
							isLineEnd = true;
							break;
						}
					}
					if (!(isLineStart && isLineEnd)) {
						ToastUtil.displayPromptInfo(this, "中间点必须在起始点和结束点之间");
						selectRadioIDCur = i;
						return false;
					}
				}
			} else if (getPointType(points.get(i)).equals(PointType.POINT_GLUE_LINE_ARC)) {// 圆弧点
				if (i <= 0) {
					ToastUtil.displayPromptInfo(this, "不能以圆弧点开始");
					selectRadioIDCur = i;
					return false;
				} else if ((i + 1) >= points.size()) {
					ToastUtil.displayPromptInfo(this, "不能以圆弧点结尾");
					selectRadioIDCur = i;
					return false;
				} else {
					boolean isLineStart = false;
					boolean isLineEnd = false;
					for (int j = i - 1; j >= 0; j--) {
						if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_START)) {
							isLineStart = true;
							break;
						} 
//						else if (!getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_MID)) {
//							// 不是中间点
//							ToastUtil.displayPromptInfo(this, "圆弧点前面不能跟除中间点之外其他的点");
//							selectRadioIDCur = j;
//							return false;
//						}

					}
					for (int j = i + 1; j < points.size(); j++) {
						if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_END)) {
							isLineEnd = true;
							break;
						}
//						else if (!getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_MID)) {
//							// 不是中间点
//							ToastUtil.displayPromptInfo(this, "圆弧点后面不能跟除中间点之外其他的点");
//							selectRadioIDCur = j;
//							return false;
//						}
					}
					if (!(isLineStart && isLineEnd)) {
						ToastUtil.displayPromptInfo(this, "圆弧点必须在起始点和结束点之间");
						selectRadioIDCur = i;
						return false;
					}
					if (!checkArcPoint(points.get(i - 1), points.get(i), points.get(i + 1))) {
						selectRadioIDCur = i;
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 下载之前要判断独立点,线结束点,面结束点的抬起高度(放到异步线程里去，这里不需要了！！！)
	 * 
	 * @param points
	 * @return false(抬起高度过高),true(一切正常)
	 */
	private boolean checkUpHeightValidity(List<Point> points) {
		// 独立点参数
		PointGlueAloneParam pointGlueAloneParam = null;
		// 线结束点参数
		PointGlueLineEndParam pointGlueLineEndParam = null;
		// 面结束点参数
		PointGlueFaceEndParam pointGlueFaceEndParam = null;
		Point point;
		// 类型
		PointType pointType = PointType.POINT_NULL;
		// Point的任务参数序列
		int id = -1;
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			pointType = getPointType(point);
			id = point.getPointParam().get_id();
			if (pointType.equals(PointType.POINT_GLUE_ALONE)) {
				// 如果等于独立点
				pointGlueAloneParam = glueAloneDao.getPointGlueAloneParamById(id);
				if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < pointGlueAloneParam.getUpHeight()) {
					// Z轴行程小于抬起高度
					ToastUtil.displayPromptInfo(this, "独立点的抬起高度过高");
					selectRadioIDCur = i;
					return false;
				}
			} else if (pointType.equals(PointType.POINT_GLUE_LINE_END)) {
				// 如果为线结束点
				pointGlueLineEndParam = glueLineEndDao.getPointGlueLineEndParamByID(id);
				if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < pointGlueLineEndParam.getUpHeight()) {
					// Z轴行程小于抬起高度
					ToastUtil.displayPromptInfo(this, "线结束点的抬起高度过高");
					selectRadioIDCur = i;
					return false;
				}
			} else if (pointType.equals(PointType.POINT_GLUE_FACE_END)) {
				// 如果为面结束点
				pointGlueFaceEndParam = glueFaceEndDao.getPointFaceEndParamByID(id);
				if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < pointGlueFaceEndParam.getUpHeight()) {
					// Z轴行程小于抬起高度
					ToastUtil.displayPromptInfo(this, "面结束点的抬起高度过高");
					selectRadioIDCur = i;
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 保存页面中的信息并返回到之前的TaskListActivity
	 */
	private void saveBackActivity() {
		// 要先判断页面中的List是否满足点的条件
		if (checkForValidity(mPointsCur)) {
			// 初始化异步线程
			CheckBackUpHeightAsynctask check = new CheckBackUpHeightAsynctask();
			check.execute(mPointsCur);
		} else {
			singleSwitch.setChecked(false);// 设置为单选框显示
			mAdapter.setSelectID(selectRadioIDCur);// 选中位置
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 点击方案选项，跳转到对应的Activity,打开相对应的任务参数
	 */
	private void processTaskActivity() {
		if (mPointsCur.size()==0) {
			ToastUtil.displayPromptInfo(this, "请先增加一个任务点");
		}else {
			PointType type = mPointsCur.get(selectRadioIDCur).getPointParam().getPointType();
			Intent intent = null;
			switch (type) {
			case POINT_GLUE_ALONE:
				intent = new Intent(this, GlueAloneActivity.class);
				break;
			case POINT_GLUE_LINE_START:
				intent = new Intent(this, GlueLineStartActivity.class);
				break;
			case POINT_GLUE_LINE_MID:
				intent = new Intent(this, GlueLineMidActivity.class);
				break;
			case POINT_GLUE_LINE_END:
				intent = new Intent(this, GlueLineEndActivity.class);
				break;
			case POINT_GLUE_FACE_START:
				intent = new Intent(this, GlueFaceStartActivity.class);
				break;
			case POINT_GLUE_FACE_END:
				intent = new Intent(this, GlueFaceEndActivity.class);
				break;
			case POINT_GLUE_CLEAR:
				intent = new Intent(this, GlueClearActivity.class);
				break;
			case POINT_GLUE_INPUT:
				intent = new Intent(this, GlueInputActivity.class);
				break;
			case POINT_GLUE_OUTPUT:
				intent = new Intent(this, GlueOutputActivity.class);
				break;
				
			default:
				// 其他点没有参数方案
				intent = null;
				ToastUtil.displayPromptInfo(this, "没有参数方案");
				break;
			}
			if (intent != null) {
				Bundle extras = new Bundle();
				extras.putParcelable(MyPopWindowClickListener.POPWINDOW_KEY, mPointsCur.get(selectRadioIDCur));
				extras.putInt(MyPopWindowClickListener.FLAG_KEY, 1);// 1代表更新数据
				extras.putInt(MyPopWindowClickListener.TYPE_KEY, 1);// 1代表要显示方案
				intent.putExtras(extras);
				startActivityForResult(intent, requestCode);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			}
		}
	}

	/**
	 * 点击返回按钮响应事件
	 */
	private void showBackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
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
				TaskActivity.this.finish();
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
	 * 将数据List<Point> 传递到下一个activity中(GlueArrayActivity,GlueOffsetActivity,
	 * GlueViewActivity)
	 * 
	 * @param _intent
	 *            要传递的intent
	 * @param _points
	 *            要传递的数据List
	 */
	private void save2Activity(Intent _intent, List<Point> _points) {
		if (checkForValidity(mPointsCur)) {
			Bundle extras = new Bundle();
			if (_points.size() > MAX_SIZE) {
				extras.putString(KEY_NUMBER, "0");
				userApplication.setPoints(_points);
			} else {
				extras.putString(KEY_NUMBER, "1");
				extras.putParcelableArrayList(TaskActivity.ARRAY_KEY, (ArrayList<? extends Parcelable>) _points);
			}
			_intent.putExtras(extras);
			startActivityForResult(_intent, requestCode);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			isSelectAll = true;//设置全选
		} else {
			selectCheckboxCur.clear();
			mAdapter.setData(mPointsCur);
			singleSwitch.setChecked(false);// 有错的话设置为单选框显示
			mList.smoothScrollToPosition(selectRadioIDCur);// Listview滚动到指定地方
			mAdapter.setSelectID(selectRadioIDCur);// 选中位置
			mAdapter.notifyDataSetChanged();
		}
	}

	/*
	 * Activity传值的回调方法
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		if (_requestCode == requestCode) {
			if (_resultCode == resultCode) {
				// point = (Point)
				// _data.getSerializableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
				mFlag = _data.getIntExtra(MyPopWindowClickListener.FLAG_KEY, 0);
				point = (Point) _data.getParcelableExtra(MyPopWindowClickListener.POPWINDOW_KEY);
				Log.d(TAG + ":onActivityResult", "selectRadioID:" + selectRadioIDCur + " " + point.toString());
				if (mFlag == 0) {
					if (mPointsCur.size() != 0) {
						selectRadioIDCur = selectRadioIDCur + 1;
					} else {
						selectRadioIDCur = 0;
					}
					mPointsCur.add(selectRadioIDCur, point);
					mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				} else if (mFlag == 1) {
					mPointsCur.remove(selectRadioIDCur);
					mPointsCur.add(selectRadioIDCur, point);
				}
				selectCheckboxCur.clear();
				singleSwitch.setChecked(false);

				Log.d(TAG + ":onActivityResult-->", mPointsCur.toString());
			} else if (_resultCode == resultViewCode) {
				// 视图保存回来的点
				String type = _data.getStringExtra(KEY_NUMBER);
				if ("0".equals(type)) {
					// 大数据
					mPointsCur = userApplication.getPoints();
				} else if ("1".equals(type)) {
					mPointsCur = _data.getParcelableArrayListExtra(VIEW_KEY);
				}
			} else if (_resultCode == resultArrayCode) {
				List<Point> pLists = new ArrayList<>();
				String array_type = _data.getStringExtra(KEY_NUMBER);
				if ("0".equals(array_type)) {
					// 大数据,从内存读取
					pLists = userApplication.getPoints();
				} else if ("1".equals(array_type)) {
					pLists = _data.getParcelableArrayListExtra(ARRAY_KEY);
				}
				Log.d(TAG, "阵列保存回来的点:" + pLists.toString());
				mPointsCur.addAll(sCheckViewIDLast + 1, pLists);
			} else if (_resultCode == resultOffsetCode) {
				// 偏移
				String type = _data.getStringExtra(KEY_NUMBER);
				List<Point> pLists = new ArrayList<>();
				if ("0".equals(type)) {
					pLists = userApplication.getPoints();
				} else {
					pLists = _data.getParcelableArrayListExtra(OFFSET_KEY);
				}
				Log.i(TAG, "偏移：" + offsetCheckBox.toString());
				for (int i = 0; i < offsetCheckBox.size(); i++) {
					mPointsCur.remove((int) offsetCheckBox.get(i));
					mPointsCur.add((int) offsetCheckBox.get(i), pLists.get(i));
				}
				offsetCheckBox.clear();

			} else if (_resultCode == resultSettingCode) {
				// 任务参数设置
				settingParam = SharePreferenceUtils.readFromSharedPreference(this);
			} else if (_resultCode == resultDownLoadCode) {
				// ToastUtil.showToast(this, "下载完成");
			} else if (_resultCode == resultCameraCode) {
				// 视觉
				point = _data.getParcelableExtra(CAMERA_KEY);
				mPointsCur.remove(selectRadioIDCur);
				mPointsCur.add(selectRadioIDCur, point);
			}

		}
		// 返回时需要将上次选中id置为初始值
		selectRadioIDPrev = -1;
		mAdapter.setData(mPointsCur);
		mAdapter.notifyDataSetChanged();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			// 触摸其它地方，让popwindow消失
			mPopupWindow.dismiss();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isChange  = PointCopyTools.comparePoints(mPointStorages, mPointsCur);
			if(isChange){
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_not_changed));
				TaskActivity.this.finish();
			}else{
				showBackDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
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

	/*
	 * 点击事件 (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_back:// 点击头文件的返回按钮
			isChange  = PointCopyTools.comparePoints(mPointStorages, mPointsCur);
			if(isChange){
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.data_not_changed));
				TaskActivity.this.finish();
			}else{
				showBackDialog();
			}
			break;

		case R.id.lin_add:// 点击+弹出选择框
			if (mIsFirst) {
				mIsFirst = false;
				// int height = v.getHeight();
				popMenu = new MyPopWindowClickListener(TaskActivity.this);
				mPopupWindow = popMenu.getMenu();
			}
			popMenu.setPointLists(mPointsCur, selectRadioIDCur, 0, mAdapter);
			// mPopupWindow.setFocusable(true);
			mPopupWindow.setOutsideTouchable(true); // 设置点击屏幕其它地方弹出框消失
			/*=================== begin ===================*/
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			/*===================  add  ===================*/
			if (mPopupWindow == null) {
				return;
			}
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				return;
			}
			// 设置popwindow的显示和消失动画
			mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 改变后面的数值就能上下平移
			mPopupWindow.showAtLocation(lin_add, Gravity.LEFT, 0, 0);
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
		case R.id.rl_dingwei:// 定位
			Log.d(TAG, "--------->定位");
			if (!mPointsCur.isEmpty()) {
				OrderParam.INSTANCE.setnXCoord(mPointsCur.get(selectRadioIDCur).getX());
				OrderParam.INSTANCE.setnYCoord(mPointsCur.get(selectRadioIDCur).getY());
				OrderParam.INSTANCE.setnZCoord(mPointsCur.get(selectRadioIDCur).getZ());
				OrderParam.INSTANCE.setnSpeed(200);
				MessageMgr.INSTANCE.setCurCoord();
			} else {
				ToastUtil.displayPromptInfo(this, "列表为空,不能定位！");
			}
			break;
		case R.id.rl_pianyi:// 偏移
			Log.d(TAG, "--------->偏移" + selectCheckboxCur.toString());
			if (selectCheckboxCur.size() != 0 && selectCheckboxCur != null) {
				List<Point> pointArrays = new ArrayList<Point>();
				for (int i = 0; i < selectCheckboxCur.size(); i++) {
					point = mPointsCur.get((int) selectCheckboxCur.get(i));
					pointArrays.add(point);// 要偏移的先加到一个List里
				}
				arrayIntent = new Intent(this, GlueOffsetActivity.class);
				save2Activity(arrayIntent, pointArrays);
				offsetCheckBox.clear();
				// 将选中的ID保存到一个自定义的string里去，然后再删除
				for (Integer number : selectCheckboxCur) {
					offsetCheckBox.add(number);
				}
				selectCheckboxCur.clear();

			} else {
				ToastUtil.displayPromptInfo(TaskActivity.this, "请选择要偏移的点");
			}

			break;
		case R.id.rl_moni:// 模拟
			Log.d(TAG, "--------->模拟");
			if (checkForValidity(mPointsCur)) {
				CheckSimulationUpHeightAsynctask check = new CheckSimulationUpHeightAsynctask();
				check.execute(mPointsCur);
			} else {
				selectCheckboxCur.clear();
				mAdapter.setData(mPointsCur);
				singleSwitch.setChecked(false);// 有错的话设置为单选框显示
				mList.smoothScrollToPosition(selectRadioIDCur);// Listview滚动到指定地方
				mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				mAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.rl_shijue:// 视觉
			Log.d(TAG, "--------->视觉");
			if (!mPointsCur.isEmpty()) {
				arrayIntent = new Intent(this, GlueCameraActivity.class);
				Bundle extras = new Bundle();
				extras.putParcelable(TaskActivity.ARRAY_KEY, mPointsCur.get(selectRadioIDCur));
				arrayIntent.putExtras(extras);
				startActivityForResult(arrayIntent, requestCode);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			} else {
				ToastUtil.displayPromptInfo(this, "请先增加一个任务点！");
			}
			break;
		case R.id.rl_moshi:// 模式
			Log.d(TAG, "--------->模式");
			if (modeFlagCur == 0) {
				modeFlagCur = 1;// 点击一次变为单步
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.step_single));
				image_moshi.setBackgroundResource(R.drawable.icon_step_single);
				tv_moshi.setText(getResources().getString(R.string.step_single));
			} else {
				modeFlagCur = 0;// 默认为0
				ToastUtil.displayPromptInfo(this, getResources().getString(R.string.step_serious));
				image_moshi.setBackgroundResource(R.drawable.icon_step_serious);
				tv_moshi.setText(getResources().getString(R.string.step_serious));
			}
			break;
		case R.id.rl_fuwei:// 复位
			prepareReset=true;
			MessageMgr.INSTANCE.resetCoord();

			break;
		case R.id.rl_zhenlie:// 阵列
			Log.d(TAG, "--------->阵列");

			if (selectCheckboxCur.size() != 0 && selectCheckboxCur != null) {
				List<Point> pointArrays = new ArrayList<Point>();
				for (int i = 0; i < selectCheckboxCur.size(); i++) {
					point = mPointsCur.get((int) selectCheckboxCur.get(i));
					pointArrays.add(point);
				}
				sCheckViewIDLast = selectCheckboxCur.get(selectCheckboxCur.size() - 1);
				selectCheckboxCur.clear();

				arrayIntent = new Intent(this, GlueArrayActivity.class);
				save2Activity(arrayIntent, pointArrays);

			} else {
				ToastUtil.displayPromptInfo(TaskActivity.this, "请选择要阵列的选项");
			}

			break;
		case R.id.rl_zhantie:// 粘贴
			if (selectCheckboxCur.size() != 0 && selectCheckboxCur != null) {
				List<Point> pointArrays = new ArrayList<Point>();
				for (int i = 0; i < selectCheckboxCur.size(); i++) {
					int id = (int) selectCheckboxCur.get(i);
					// Point point = mPoints.get(id);
					point = new Point(PointType.POINT_NULL);
					point.setX(mPointsCur.get(id).getX());
					point.setY(mPointsCur.get(id).getY());
					point.setZ(mPointsCur.get(id).getZ());
					point.setU(mPointsCur.get(id).getU());
					point.setPointParam(mPointsCur.get(id).getPointParam());
					if (!point.getPointParam().getPointType().equals(PointType.POINT_GLUE_BASE)) {
						pointArrays.add(point);
					}
				}
				selectCheckboxCur.clear();
				mPointsCur.addAll(pointArrays);
				mAdapter.setData(mPointsCur);
				mAdapter.notifyDataSetChanged();
			} else {
				ToastUtil.displayPromptInfo(TaskActivity.this, "请选择要粘贴的选项");
			}
			break;
		case R.id.rl_shitu:// 视图
			Log.d(TAG, "--------->视图");
			arrayIntent = new Intent(this, GlueViewActivity.class);
			Log.d(TAG, "视图：" + mPointsCur.toString());
			save2Activity(arrayIntent, mPointsCur);
			break;
		case R.id.rl_xiazai:// 下载

			// arrayIntent = new Intent(this, GlueDownloadActivity.class);
			// save2DownLoadActivity(arrayIntent, mPoints);
			if (checkForValidity(mPointsCur)) {
				CheckUpHeightAsynctask check = new CheckUpHeightAsynctask();
				check.execute(mPointsCur);
			} else {
				selectCheckboxCur.clear();
				mAdapter.setData(mPointsCur);
				singleSwitch.setChecked(false);// 有错的话设置为单选框显示
				mList.smoothScrollToPosition(selectRadioIDCur);// Listview滚动到指定地方
				mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				mAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.rl_shanchu:// 删除
			if (selectCheckboxCur.size() != 0 && selectCheckboxCur != null) {
				Log.i(TAG, "数组：" + selectCheckboxCur.toString());
				for (int i = selectCheckboxCur.size() - 1; i >= 0; i--) {
					Log.d(TAG, "Integer.parseInt(checkBox[i])" + selectCheckboxCur.get(i));
					mPointsCur.remove((int) selectCheckboxCur.get(i));
				}
				selectCheckboxCur.clear();
				if (selectRadioIDCur >= mPointsCur.size()) {
					selectRadioIDCur = mPointsCur.size() - 1;
					mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				}
				mAdapter.setData(mPointsCur);
				mAdapter.notifyDataSetChanged();
			} else {
				ToastUtil.displayPromptInfo(TaskActivity.this, "请选择要删除的选项");
			}

			break;
		case R.id.rl_xiugai:// 全选
			if(isSelectAll){
//				tv_selectAll.setText(getResources().getString(R.string.activity_glue_quanbuxuan));
				selectCheckboxCur.clear();
				isSelected = new LinkedHashMap<Integer, Boolean>();
				for (int i = 0; i < mPointsCur.size(); i++) {
					// 保存在内存中的
					selectCheckboxCur.add(i);
					// TaskMainAdapter中的选择框全设置为true
					isSelected.put(i, true);
				}
				isSelectAll = false;
			}else{
//				tv_selectAll.setText(getResources().getString(R.string.activity_glue_quanxuan));
				selectCheckboxCur.clear();
				isSelectAll = true;
				isSelected = new LinkedHashMap<Integer, Boolean>();
				for (int i = 0; i < mPointsCur.size(); i++) {
					// TaskMainAdapter中的选择框全设置为false
					isSelected.put(i, false);
				}
				isSelectAll = true;
			}
			mAdapter.setIsSelected(isSelected);
			mAdapter.notifyDataSetChanged();
			break;
		case R.id.rl_shezhi:// 任务设置
			Log.d(TAG, "--------->设置");
			arrayIntent = new Intent(this, GlueTaskSettingActivity.class);
			startActivityForResult(arrayIntent, requestCode);
			break;
		case R.id.rl_fangan:// 方案
			Log.d(TAG, "--------->方案");
			processTaskActivity();
			// Log.d(TAG, mPoints.get(selectRadioID).toString());

			break;

		default:
			break;
		}

	}

	/**
	 * 打开进度条对话框
	 */
	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在检查抬起高度...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度条对话框
	 */
	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * 打开没有进度条的对话框
	 */
	private void startUpLoadDialog() {
		if (uploadDialog == null) {
			uploadDialog = CustomUploadDialog.createDialog(this);
			uploadDialog.setMessage("正在上传中..");
			uploadDialog.setCanceledOnTouchOutside(false);
		}
		uploadDialog.show();
	}

	/**
	 * 关闭没有进度条的对话框
	 */
	private void stopUploadDialog() {
		if (uploadDialog != null) {
			uploadDialog.dismiss();
			uploadDialog = null;
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
	private void DisPlayInfoAfterGetMsg(byte[] revBuffer) {
		
		switch (MessageMgr.INSTANCE.managingMessage(revBuffer)) {
		
		case 0:
			ToastUtil.displayPromptInfo(TaskActivity.this, "校验失败");
			sendResetCommand();
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);
				 Log.d(TAG, "解析坐标值->:"+coordPoint.toString());
				mPointsCur.get(selectRadioIDCur).setX(coordPoint.getX());
				mPointsCur.get(selectRadioIDCur).setY(coordPoint.getY());
				mPointsCur.get(selectRadioIDCur).setZ(coordPoint.getZ());
				mPointsCur.get(selectRadioIDCur).setU(coordPoint.getU());
				mAdapter.setData(mPointsCur);
				mAdapter.notifyDataSetChanged();

			} 
			else if (revBuffer[2] == 0x4A) {// 获取下位机参数成功
				ToastUtil.displayPromptInfo(TaskActivity.this, "获取参数成功!111");
			}
			sendResetCommand();
		}
			break;
		case 40101:
			ToastUtil.displayPromptInfo(TaskActivity.this, "非法功能");
			sendResetCommand();
			break;
		case 40102:
			ToastUtil.displayPromptInfo(TaskActivity.this, "非法数据地址");
			sendResetCommand();
			break;
		case 40103:
			ToastUtil.displayPromptInfo(TaskActivity.this, "非法数据");
			sendResetCommand();
			
			break;
		case 40105:
			ToastUtil.displayPromptInfo(TaskActivity.this, "设备忙");
//			if(prepareReset){
//				/************************ add begin ************************/
//				buffer = new byte[ORDER_BUFFER_LENTH];
//				orderLength = protocol.CreaterOrder(buffer, CmdParam.Cmd_Reset);
//				MessageMgr.INSTANCE.writeData(buffer, orderLength);
//				/************************ end ******************************/
//				prepareReset=false;
//			}
			
			break;
		case 40109:
			ToastUtil.displayPromptInfo(TaskActivity.this, "急停中");
//			if(prepareReset){
//				/************************ add begin ************************/
//				buffer = new byte[ORDER_BUFFER_LENTH];
//				orderLength = protocol.CreaterOrder(buffer, CmdParam.Cmd_Reset);
//				MessageMgr.INSTANCE.writeData(buffer, orderLength);
//				/************************ end ******************************/
//				prepareReset=false;
//			}
			
			break;
		case 40110:
			ToastUtil.displayPromptInfo(TaskActivity.this, "X轴光电报警");
			sendResetCommand();
			
			break;
		case 40111:
			ToastUtil.displayPromptInfo(TaskActivity.this, "Y轴光电报警");
			sendResetCommand();
			
			break;
		case 40112:
			ToastUtil.displayPromptInfo(TaskActivity.this, "Z轴光电报警");
			sendResetCommand();
			break;
		case 40113:
			ToastUtil.displayPromptInfo(TaskActivity.this, "U轴光电报警");
			sendResetCommand();
			break;
		case 40114:
			ToastUtil.displayPromptInfo(TaskActivity.this, "行程超限报警");
			sendResetCommand();
			break;
		case 40115:
			ToastUtil.displayPromptInfo(TaskActivity.this, "任务上传失败");
			sendResetCommand();
			break;
		case 40116:
			ToastUtil.displayPromptInfo(TaskActivity.this, "任务下载失败");
			sendResetCommand();
			break;
		case 40117:
			ToastUtil.displayPromptInfo(TaskActivity.this, "任务模拟失败");
			sendResetCommand();
			break;
		case 40118:
			ToastUtil.displayPromptInfo(TaskActivity.this, "示教指令错误");
			sendResetCommand();
			break;
		case 40119:
			ToastUtil.displayPromptInfo(TaskActivity.this, "循迹定位失败");
			sendResetCommand();
			break;
		case 40120:
			ToastUtil.displayPromptInfo(TaskActivity.this, "任务号不可用");
			sendResetCommand();
			break;
		case 40121:
			ToastUtil.displayPromptInfo(TaskActivity.this, "初始化失败");
			sendResetCommand();
			break;
		case 40122:
			ToastUtil.displayPromptInfo(TaskActivity.this, "API版本错误");
			sendResetCommand();
			break;
		case 40123:
			ToastUtil.displayPromptInfo(TaskActivity.this, "程序升级失败");
			sendResetCommand();
			break;
		case 40124:
			ToastUtil.displayPromptInfo(TaskActivity.this, "系统损坏");
			sendResetCommand();
			break;
		case 40125:
			ToastUtil.displayPromptInfo(TaskActivity.this, "任务未加载");
			sendResetCommand();
			break;
		case 40126:
			ToastUtil.displayPromptInfo(TaskActivity.this, "(Z轴)基点抬起高度过高");
			sendResetCommand();
			break;
		case 40127:
			ToastUtil.displayPromptInfo(TaskActivity.this, "等待输入超时");
			sendResetCommand();
			break;
		default:
			ToastUtil.displayPromptInfo(TaskActivity.this, "未知错误");
			sendResetCommand();
			break;
		}
	}

	/**
	 * @Title  sendResetCommand
	 * @Description 发送复位命令，跟新ui
	 * @author wj
	 */
	private void sendResetCommand() {
		if(prepareReset){
			/************************ add begin ************************/
			buffer = new byte[ORDER_BUFFER_LENTH];
			orderLength = protocol.CreaterOrder(buffer, CmdParam.Cmd_Reset);
			MessageMgr.INSTANCE.writeData(buffer, orderLength);
			/************************ end ******************************/
			prepareReset=false;
//			mPointsCur.get(selectRadioIDCur).setX(0);
//			mPointsCur.get(selectRadioIDCur).setY(0);
//			mPointsCur.get(selectRadioIDCur).setZ(0);
//			mPointsCur.get(selectRadioIDCur).setU(0);
//			mAdapter.setData(mPointsCur);
//			mAdapter.notifyDataSetChanged();
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
				DisPlayInfoAfterGetMsg(buffer);
			}
		}
	}

	/**
	 * 异步线程获取Point点(因为要从数据库中获取数据,费时操作需要异步线程)
	 *
	 */
	private class GetPointsAsynctask extends AsyncTask<Object, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Object... params) {
			mPointsCur = pointDao.findALLPointsByIdLists((List<Integer>) params[0]);

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
//				for (Point point : mPointsCur) {
//					mPointStorages.add(point);
//				}
				mPointStorages = PointCopyTools.processCopyPoints(mPointsCur);
				mAdapter.setData(mPointsCur);
				mList.setAdapter(mAdapter);
			}
		}

	}

	/**
	 * 检查抬起高度，往TaskListActivity中传因为要从数据库中读取数据，所以比较耗时，把它放到异步线程中
	 *
	 */
	private class CheckBackUpHeightAsynctask extends AsyncTask<List<Point>, Integer, Integer[]> {

		@Override
		protected void onPreExecute() {
			startProgressDialog();
		}

		@Override
		protected Integer[] doInBackground(List<Point>... params) {
			// result[0] = 1代表正常，result[1] = 0代表有错，
			// result[1]=0代表正常,result[1]=1代表抬起高度,result[1]=2代表面间距,result[1]=3代表圆角半径
			Integer[] result = new Integer[2];
			result[0] = 1;
			result[1] = 0;
			// 独立点参数
			PointGlueAloneParam pointGlueAloneParam = null;
			// 线结束点参数
			PointGlueLineEndParam pointGlueLineEndParam = null;
			// 面结束点参数
			PointGlueFaceEndParam pointGlueFaceEndParam = null;
			Point point;
			// 类型
			PointType pointType = PointType.POINT_NULL;
			// Point的任务参数序列
			int id = -1;
			// 保存独立点参数方案主键的List
			List<Integer> aloneIDs = new ArrayList<>();
			// 保存线结束点参数方案主键的List
			List<Integer> lineEndIDs = new ArrayList<>();
			// 保存线中间点参数方案主键的List
			List<Integer> lineMidIDs = new ArrayList<>();
			// 保存面起始点参数方案主键的List
			List<Integer> faceStartIDs = new ArrayList<>();
			// 保存面结束点参数方案主键的List
			List<Integer> faceEndIDs = new ArrayList<>();
			progressDialog.setMax(params[0].size());
			for (int i = 0; i < params[0].size(); i++) {
				point = params[0].get(i);
				pointType = getPointType(point);
				id = point.getPointParam().get_id();
				if (pointType.equals(PointType.POINT_GLUE_ALONE)) {
					// 如果等于独立点
					if (!aloneIDs.contains(id)) {
						aloneIDs.add(id);
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_MID)) {
					// 线中间点
					if (!lineMidIDs.contains(id)) {
						lineMidIDs.add(id);
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_END)) {
					// 如果等于线结束点
					if (!lineEndIDs.contains(id)) {
						lineEndIDs.add(id);
					}
				} else if (pointType.equals(PointType.POINT_GLUE_FACE_START)) {
					// 如果为面起始点
					if (!faceStartIDs.contains(id)) {
						faceStartIDs.add(id);
					}
				} else if (pointType.equals(PointType.POINT_GLUE_FACE_END)) {
					// 如果等于面结束点
					if (!faceEndIDs.contains(id)) {
						faceEndIDs.add(id);
					}
				}
			}
			// 获取所有独立点的参数方案
			List<PointGlueAloneParam> aloneParams = glueAloneDao.getGlueAloneParamsByIDs(aloneIDs);
			// 获取所有线结束点的参数方案
			List<PointGlueLineEndParam> lineEndParams = glueLineEndDao.getPointGlueLineEndParamsByIDs(lineEndIDs);
			// 获取所有线中间点的参数方案
			List<PointGlueLineMidParam> lineMidParams = glueLineMidDao.getPointGlueLineMidParamsByIDs(lineMidIDs);
			// 获取所有面起始点的参数方案
			List<PointGlueFaceStartParam> faceStartParams = glueFaceStartDao.getPointFaceStartParamsByIDs(faceStartIDs);
			// 获取所有面结束点的参数方案
			List<PointGlueFaceEndParam> faceEndParams = glueFaceEndDao.getGlueFaceEndParamsByIDs(faceEndIDs);
			// 将方案和对应方案主键放到一个HashMap中
			HashMap<Integer, PointGlueAloneParam> aloneMaps = new HashMap<>();
			for (int i = 0; i < aloneIDs.size(); i++) {
				aloneMaps.put(aloneIDs.get(i), aloneParams.get(i));
			}
			HashMap<Integer, PointGlueLineEndParam> lineEndMaps = new HashMap<>();
			for (int i = 0; i < lineEndIDs.size(); i++) {
				lineEndMaps.put(lineEndIDs.get(i), lineEndParams.get(i));
			}
			HashMap<Integer, PointGlueFaceEndParam> faceEndMaps = new HashMap<>();
			for (int i = 0; i < faceEndIDs.size(); i++) {
				faceEndMaps.put(faceEndIDs.get(i), faceEndParams.get(i));
			}
			HashMap<Integer, PointGlueLineMidParam> lineMidMaps = new HashMap<>();
			for (int i = 0; i < lineMidIDs.size(); i++) {
				lineMidMaps.put(lineMidIDs.get(i), lineMidParams.get(i));
			}
			HashMap<Integer, PointGlueFaceStartParam> faceStartMaps = new HashMap<>();
			for (int i = 0; i < faceStartIDs.size(); i++) {
				faceStartMaps.put(faceStartIDs.get(i), faceStartParams.get(i));
			}
			// 代替之前的每个数据都从数据库里面读取
			for (int i = 0; i < params[0].size(); i++) {
				publishProgress(i);
				point = params[0].get(i);
				pointType = getPointType(point);
				id = point.getPointParam().get_id();
				if (pointType.equals(PointType.POINT_GLUE_ALONE)) {
					// 如果等于独立点
					pointGlueAloneParam = aloneMaps.get(id);
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < pointGlueAloneParam.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_END)) {
					// 如果为线结束点
					pointGlueLineEndParam = lineEndMaps.get(id);
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < pointGlueLineEndParam.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
				} else if (pointType.equals(PointType.POINT_GLUE_FACE_END)) {
					// 如果为面结束点
					pointGlueFaceEndParam = faceEndMaps.get(id);
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < pointGlueFaceEndParam.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
					Point _point_1 = params[0].get(i - 1);// 前面一个点
					int _id_1 = _point_1.getPointParam().get_id();// 面终点前面一个点的面起点的方案id
					if (faceStartMaps.get(_id_1).isStartDir()) {// x方向
						int lineNum = pointGlueFaceEndParam.getLineNum();
						if (RobotParam.INSTANCE
								.YPulse2Journey(Math.abs((_point_1.getY() - point.getY()) / (lineNum - 1))) < 1.0f) {
							// 面直线间距不小于1mm
							selectRadioIDCur = i;
							result[0] = 0;
							result[1] = 2;
							return result;
						}
					} else {
						int lineNum = pointGlueFaceEndParam.getLineNum();
						if (RobotParam.INSTANCE
								.XPulse2Journey(Math.abs((_point_1.getX() - point.getX()) / (lineNum - 1))) < 1.0f) {
							// 面直线间距不小于1mm
							selectRadioIDCur = i;
							result[0] = 0;
							result[1] = 2;
							return result;
						}
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_MID)) {
					// 中间点
					float radius = lineMidMaps.get(id).getRadius();

					if (radius > 0) {
						if (!checkRadiusPoint(params[0], i, radius)) {
							// 圆角半径过大
							result[0] = 0;
							result[1] = 3;
							return result;
						}
					}
				}
			}
			// 说明抬起高度都低于Z轴
			pointDao.deletePoints(mPointStorages);
			rowids = pointDao.insertPoints(mPointsCur);
			mPointsCur = pointDao.findALLPointsByIdLists(rowids);
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Integer[] result) {
			// result[0] = 1代表正常，result[1] = 0代表有错，
			// result[1]=0代表正常,result[1]=1代表抬起高度,result[1]=2代表面间距,result[1]=3代表圆角半径
			stopProgressDialog();
			if (result[0] == 1) {

				List<Integer> ids = new ArrayList<Integer>();
				for (Point point : mPointsCur) {
					ids.add(point.getId());
				}

				Bundle extras = new Bundle();
				task.setPointids(ids);
				extras.putParcelable(TaskListActivity.TASK_KEY, task);

				intent.putExtras(extras);
				setResult(TaskListActivity.TASK_ResultCode, intent);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
			} else {
				if (result[1] == 1) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "抬起高度过高");
				} else if (result[1] == 2) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "面直线间距不得小于1mm");
				} else if (result[1] == 3) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "圆角半径过大");
				}
				selectCheckboxCur.clear();
				mAdapter.setData(mPointsCur);
				singleSwitch.setChecked(false);// 有错的话设置为单选框显示
				mList.smoothScrollToPosition(selectRadioIDCur);// Listview滚动到指定地方
				mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				mAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 模拟任务因为要从数据库中读取数据，所以比较耗时，把它放到异步线程中
	 *
	 */
	private class CheckSimulationUpHeightAsynctask extends AsyncTask<List<Point>, Integer, Integer[]> {

		private ParamsSetting paramSetting;

		@Override
		protected void onPreExecute() {
			startUpLoadDialog();
			paramSetting = new ParamsSetting(TaskActivity.this);
		}

		@Override
		protected Integer[] doInBackground(List<Point>... params) {
			Integer[] result = new Integer[2];
			result[0] = 1;
			result[1] = 0;
			paramSetting.setParamsToApplication(userApplication, params[0]);
			Point point;
			// 类型
			PointType pointType = PointType.POINT_NULL;
			// Point的任务参数序列
			int id = -1;
			// 代替之前的每个数据都从数据库里面读取
			for (int i = 0; i < params[0].size(); i++) {
				publishProgress(i);
				point = params[0].get(i);
				pointType = getPointType(point);
				id = point.getPointParam().get_id();
				if (pointType.equals(PointType.POINT_GLUE_ALONE)) {
					// 如果等于独立点
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < userApplication.getAloneParamMaps().get(id)
							.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_END)) {
					// 如果为线结束点
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < userApplication.getLineEndParamMaps().get(id)
							.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
				} else if (pointType.equals(PointType.POINT_GLUE_FACE_END)) {
					// 如果为面结束点
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < userApplication.getFaceEndParamMaps().get(id)
							.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
					Point _point_1 = params[0].get(i - 1);// 前面一个点
					int _id_1 = _point_1.getPointParam().get_id();// 面终点前面一个点的面起点的方案id
					if (userApplication.getFaceStartParamMaps().get(_id_1).isStartDir()) {// x方向
						int lineNum = userApplication.getFaceEndParamMaps().get(id).getLineNum();
						if (RobotParam.INSTANCE
								.YPulse2Journey(Math.abs((_point_1.getY() - point.getY()) / (lineNum - 1))) < 1.0f) {
							// 面直线间距不小于1mm
							selectRadioIDCur = i;
							result[0] = 0;
							result[1] = 2;
							return result;
						}
					} else {
						int lineNum = userApplication.getFaceEndParamMaps().get(id).getLineNum();
						if (RobotParam.INSTANCE
								.XPulse2Journey(Math.abs((_point_1.getX() - point.getX()) / (lineNum - 1))) < 1.0f) {
							// 面直线间距不小于1mm
							selectRadioIDCur = i;
							result[0] = 0;
							result[1] = 2;
							return result;
						}
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_MID)) {
					// 中间点
					float radius = userApplication.getLineMidParamMaps().get(id).getRadius();
					if (radius > 0) {
						if (!checkRadiusPoint(params[0], i, radius)) {
							// 圆角半径过大
							result[0] = 0;
							result[1] = 3;
							return result;
						}
					}
				}
			}

			return result;

		}

		@Override
		protected void onPostExecute(Integer[] result) {
			stopUploadDialog();
			// result[0] = 1代表正常，result[0] = 0代表有错，
			// result[1]=0代表正常,result[1]=1代表抬起高度,result[1]=2代表面间距,result[1]=3代表圆角半径
			if (result[0] == 1) {
				MessageMgr.INSTANCE.setUserApplication(userApplication);
				TaskParam.INSTANCE.setAllParamBacktoDefault();
				// TaskParam.INSTANCE.setnStartX(mPoints.get(0).getX());
				// TaskParam.INSTANCE.setnStartY(mPoints.get(0).getY());
				// TaskParam.INSTANCE.setnStartZ(mPoints.get(0).getZ());
				// TaskParam.INSTANCE.setnStartU(mPoints.get(0).getU());
				TaskParam.INSTANCE.setStrTaskName("");
				MessageMgr.INSTANCE.taskDownloadDemo(mPointsCur);
			} else {
				if (result[1] == 1) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "抬起高度过高");
				} else if (result[1] == 2) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "面直线间距不得小于1mm");
				} else if (result[1] == 3) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "圆角半径过大");
				}
				selectCheckboxCur.clear();
				mAdapter.setData(mPointsCur);
				singleSwitch.setChecked(false);// 有错的话设置为单选框显示
				mList.smoothScrollToPosition(selectRadioIDCur);// Listview滚动到指定地方
				mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				mAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 检查抬起高度，往GlueDownloadActivity中传因为要从数据库中读取数据，所以比较耗时，把它放到异步线程中
	 *
	 */
	private class CheckUpHeightAsynctask extends AsyncTask<List<Point>, Integer, Integer[]> {

		private ParamsSetting paramSetting;

		@Override
		protected void onPreExecute() {
			startProgressDialog();
			paramSetting = new ParamsSetting(TaskActivity.this);
		}

		@Override
		protected Integer[] doInBackground(List<Point>... params) {
			// result[0] = 1代表正常，result[0] = 0代表有错，
			// result[1]=0代表正常,result[1]=1代表抬起高度,result[1]=2代表面间距,result[1]=3代表圆角半径
			Integer[] result = new Integer[2];
			result[0] = 1;
			result[1] = 0;
			int max = params[0].size();
			progressDialog.setMax(max);
			publishProgress(0);
			paramSetting.setParamsToApplication(userApplication, params[0]);

			Point point;
			// 类型
			PointType pointType = PointType.POINT_NULL;
			// Point的任务参数序列
			int id = -1;
			// 代替之前的每个数据都从数据库里面读取
			for (int i = 0; i < params[0].size(); i++) {
				publishProgress(i);
				point = params[0].get(i);
				pointType = getPointType(point);
				id = point.getPointParam().get_id();
				if (pointType.equals(PointType.POINT_GLUE_ALONE)) {
					// 如果等于独立点
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < userApplication.getAloneParamMaps().get(id)
							.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_END)) {
					// 如果为线结束点
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < userApplication.getLineEndParamMaps().get(id)
							.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
				} else if (pointType.equals(PointType.POINT_GLUE_FACE_END)) {
					// 如果为面结束点
					if (RobotParam.INSTANCE.ZPulse2Journey(point.getZ()) < userApplication.getFaceEndParamMaps().get(id)
							.getUpHeight()) {
						// Z轴行程小于抬起高度
						selectRadioIDCur = i;
						result[0] = 0;
						result[1] = 1;
						return result;
					}
					Point _point_1 = params[0].get(i - 1);// 前面一个点
					int _id_1 = _point_1.getPointParam().get_id();// 面终点前面一个点的面起点的方案id
					if (userApplication.getFaceStartParamMaps().get(_id_1).isStartDir()) {// x方向
						int lineNum = userApplication.getFaceEndParamMaps().get(id).getLineNum();
						if (RobotParam.INSTANCE
								.YPulse2Journey(Math.abs((_point_1.getY() - point.getY()) / (lineNum - 1))) < 1.0f) {
							// 面直线间距不小于1mm
							selectRadioIDCur = i;
							result[0] = 0;
							result[1] = 2;
							return result;
						}
					} else {
						int lineNum = userApplication.getFaceEndParamMaps().get(id).getLineNum();
						if (RobotParam.INSTANCE
								.XPulse2Journey(Math.abs((_point_1.getX() - point.getX()) / (lineNum - 1))) < 1.0f) {
							// 面直线间距不小于1mm
							selectRadioIDCur = i;
							result[0] = 0;
							result[1] = 2;
							return result;
						}
					}
				} else if (pointType.equals(PointType.POINT_GLUE_LINE_MID)) {
					// 中间点
					float radius = userApplication.getLineMidParamMaps().get(id).getRadius();
					if (radius > 0) {
						if (!checkRadiusPoint(params[0], i, radius)) {
							// 圆角半径过大
							result[0] = 0;
							result[1] = 3;
							return result;
						}
					}
				}
			}

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Integer[] result) {
			// result[0] = 1代表正常，result[1] = 0代表有错，
			// result[1]=0代表正常,result[1]=1代表抬起高度,result[1]=2代表面间距,result[1]=3代表圆角半径
			stopProgressDialog();
			if (result[0] == 1) {
				Bundle extras = new Bundle();
				if (mPointsCur.size() > MAX_SIZE) {
					extras.putString(KEY_NUMBER, "0");
					userApplication.setPoints(mPointsCur);
				} else {
					extras.putString(KEY_NUMBER, "1");
					extras.putParcelableArrayList(TaskActivity.ARRAY_KEY, (ArrayList<? extends Parcelable>) mPointsCur);
				}
				Intent _intent = new Intent(TaskActivity.this, GlueDownloadActivity.class);
				extras.putString(TaskActivity.DOWNLOAD_NUMBER_KEY, task.getTaskName());
				_intent.putExtras(extras);
				startActivityForResult(_intent, requestCode);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
			} else {
				if (result[1] == 1) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "抬起高度过高");
				} else if (result[1] == 2) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "面直线间距不得小于1mm");
				} else if (result[1] == 3) {
					ToastUtil.displayPromptInfo(TaskActivity.this, "圆角半径过大");
				}
				selectCheckboxCur.clear();
				mAdapter.setData(mPointsCur);
				singleSwitch.setChecked(false);// 有错的话设置为单选框显示
				mList.smoothScrollToPosition(selectRadioIDCur);// Listview滚动到指定地方
				mAdapter.setSelectID(selectRadioIDCur);// 选中位置
				mAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * @description 检查中间点的圆弧半径
	 * @param list
	 *            Point的List表
	 * @param _idx
	 * @param radius
	 * @return false-圆角半径过大,true-正常
	 */
	private boolean checkRadiusPoint(List<Point> list, int _idx, float radius) {
		SMatrix1_4[] m = new SMatrix1_4[3];
		if (getPointType(list.get(_idx - 1)) == PointType.POINT_GLUE_LINE_ARC) {
			if (getPointType(list.get(_idx + 1)) == PointType.POINT_GLUE_LINE_ARC) {
				SMatrix1_4 _m1 = new SMatrix1_4(list.get(_idx - 2).getX(), list.get(_idx - 2).getY(),
						list.get(_idx - 2).getZ());
				SMatrix1_4 _m2 = new SMatrix1_4(list.get(_idx - 1).getX(), list.get(_idx - 1).getY(),
						list.get(_idx - 1).getZ());
				SMatrix1_4 _m3 = new SMatrix1_4(list.get(_idx).getX(), list.get(_idx).getY(), list.get(_idx).getZ());
				SMatrix1_4 _m4 = new SMatrix1_4(list.get(_idx + 1).getX(), list.get(_idx + 1).getY(),
						list.get(_idx + 1).getZ());
				SMatrix1_4 _m5 = new SMatrix1_4(list.get(_idx + 2).getX(), list.get(_idx + 2).getY(),
						list.get(_idx + 2).getZ());
				float _r = RobotParam.INSTANCE.XJourney2Pulse(radius);
				if (!CommonArithmetic.getArcPtByR(_m1, _m2, _m3, _m4, _m5, _r, m)) {
					// 圆角半径过大
					selectRadioIDCur = _idx;
					return false;
				}
			} else {
				SMatrix1_4 _m1 = new SMatrix1_4(list.get(_idx + 1).getX(), list.get(_idx + 1).getY(),
						list.get(_idx + 1).getZ());
				SMatrix1_4 _m2 = new SMatrix1_4(list.get(_idx).getX(), list.get(_idx).getY(), list.get(_idx).getZ());
				SMatrix1_4 _m3 = new SMatrix1_4(list.get(_idx - 1).getX(), list.get(_idx - 1).getY(),
						list.get(_idx - 1).getZ());
				SMatrix1_4 _m4 = new SMatrix1_4(list.get(_idx - 2).getX(), list.get(_idx - 2).getY(),
						list.get(_idx - 2).getZ());
				float _r = RobotParam.INSTANCE.XJourney2Pulse(radius);
				if (!CommonArithmetic.getArcPtByR(_m1, _m2, _m3, _m4, _r, m)) {
					// 圆角半径过大
					selectRadioIDCur = _idx;
					return false;
				}
			}
		} else {
			if (getPointType(list.get(_idx + 1)) == PointType.POINT_GLUE_LINE_ARC) {
				SMatrix1_4 _m1 = new SMatrix1_4(list.get(_idx - 1).getX(), list.get(_idx - 1).getY(),
						list.get(_idx - 1).getZ());
				SMatrix1_4 _m2 = new SMatrix1_4(list.get(_idx).getX(), list.get(_idx).getY(), list.get(_idx).getZ());
				SMatrix1_4 _m3 = new SMatrix1_4(list.get(_idx + 1).getX(), list.get(_idx + 1).getY(),
						list.get(_idx + 1).getZ());
				SMatrix1_4 _m4 = new SMatrix1_4(list.get(_idx + 2).getX(), list.get(_idx + 2).getY(),
						list.get(_idx + 2).getZ());
				float _r = RobotParam.INSTANCE.XJourney2Pulse(radius);
				if (!CommonArithmetic.getArcPtByR(_m1, _m2, _m3, _m4, _r, m)) {
					// 圆角半径过大
					selectRadioIDCur = _idx;
					return false;
				}
			} else {
				SMatrix1_4 m1 = new SMatrix1_4(list.get(_idx - 1).getX(), list.get(_idx - 1).getY(),
						list.get(_idx - 1).getZ());
				SMatrix1_4 m2 = new SMatrix1_4(list.get(_idx).getX(), list.get(_idx).getY(), list.get(_idx).getZ());
				SMatrix1_4 m3 = new SMatrix1_4(list.get(_idx + 1).getX(), list.get(_idx + 1).getY(),
						list.get(_idx + 1).getZ());
				float nRadius = RobotParam.INSTANCE.XJourney2Pulse(radius);
				float angle = CommonArithmetic.getAngle(m1, m2, m3);
				SMatrix1_4 m2_1 = SMatrix1_4.operator_minus(m2, m1);
				SMatrix1_4 m3_2 = SMatrix1_4.operator_minus(m3, m2);
				// double 强转成float
				float len12 = (float) SMatrix1_4.operator_mod3(m2_1);
				float len23 = (float) SMatrix1_4.operator_mod3(m3_2);
				float lenMin = len12 < len23 ? len12 : len23;
				float rMax = (float) (lenMin * Math.tan(angle * Math.PI / 360));
				if (nRadius > rMax) {
					// 圆角半径过大
					selectRadioIDCur = _idx;
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 检查圆弧点共线问题
	 * 
	 * @param _pt1
	 *            起始点
	 * @param _pt2
	 *            圆弧点
	 * @param _pt3
	 *            结束点
	 * @return
	 */
	private boolean checkArcPoint(Point _pt1, Point _pt2, Point _pt3) {
		if (_pt1.getX() == _pt2.getX() && _pt1.getY() == _pt2.getY() && _pt1.getZ() == _pt2.getZ()
				&& _pt1.getU() == _pt2.getU()) {
			// 前一个点-圆弧点重合
			ToastUtil.displayPromptInfo(this, _pt1,"-圆弧点重合！");
			return false;
		}
		if (_pt3.getX() == _pt2.getX() && _pt3.getY() == _pt2.getY() && _pt3.getZ() == _pt2.getZ()
				&& _pt3.getU() == _pt2.getU()) {
			// 后一个点-结束点重合
			ToastUtil.displayPromptInfo(this, _pt3,"-圆弧点重合！");
			return false;
		}
		if (_pt3.getX() == _pt1.getX() && _pt3.getY() == _pt1.getY() && _pt3.getZ() == _pt1.getZ()
				&& _pt3.getU() == _pt1.getU()) {
			// 前一个点-后一个点重合
			ToastUtil.displayPromptInfo(this, _pt1,_pt3,"重合！");
			return false;
		}
		SMatrix1_4 m1 = new SMatrix1_4(_pt1.getX(), _pt1.getY(), _pt1.getZ());
		SMatrix1_4 m2 = new SMatrix1_4(_pt2.getX(), _pt2.getY(), _pt2.getZ());
		SMatrix1_4 m3 = new SMatrix1_4(_pt3.getX(), _pt3.getY(), _pt3.getZ());
		SMatrix1_4 m2_1 = SMatrix1_4.operator_minus(m2, m1);
		SMatrix1_4 m3_2 = SMatrix1_4.operator_minus(m3, m2);
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m2_1, m3_2);
		if (n.getX() == 0 && n.getY() == 0 && n.getZ() == 0) {
			//
			ToastUtil.displayPromptInfo(this, _pt1,_pt3,"-圆弧点三点共线！");
			return false;
		}
		return true;
	}

}
