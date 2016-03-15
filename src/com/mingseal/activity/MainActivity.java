package com.mingseal.activity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.mingseal.communicate.NetManager;
import com.mingseal.communicate.SocketInputThread;
import com.mingseal.communicate.SocketThreadManager;
import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.CmdParam;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.dhp.R;
import com.mingseal.listener.MaxMinEditWatcher;
import com.mingseal.listener.MaxMinFocusChangeListener;
import com.mingseal.utils.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button btnReset;
	private Button btnXRight;
	private Button btnXLeft;
	private Button btnYBackWard;
	private Button btnYForWard;
	private Button btnZUp;
	private Button btnZDown;
	private Button btnGetParam;
	private Button btnLocate;
	private Button btnDownload;
	private Button btnUpload;
	private TextView txtCoord;
	private TextView txtSpeed;
	private SeekBar speedBar;
	private RevHandler handler;
	private Button btnGetNumber;
	private EditText et_number;
	private String coord = null;
//	private AsyncConnection myConnection = null;
	private CmdParam[] cmd = new CmdParam[2];
	private int m_nRobotSeries = 0;
	private int debugCount = 0;
	private List<Point> pointList;
	private TextView txtUpload;
	private String uploadInfo = null;
	protected int count = 0;
	protected long intClickFirst;
	protected long intClickSecond;
	private static final String TAG = "MainActivity";

//	public AsyncConnection getMyConnection() {
//		return myConnection;
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		TestMoveListener testMove = new TestMoveListener();
		btnReset = (Button) findViewById(R.id.button_reset);
		btnXRight = (Button) findViewById(R.id.x_right);
		btnXRight.setOnTouchListener(testMove);
		btnXLeft = (Button) findViewById(R.id.x_left);
		btnXLeft.setOnTouchListener(testMove);
		btnYBackWard = (Button) findViewById(R.id.y_backward);
		btnYBackWard.setOnTouchListener(testMove);
		btnYForWard = (Button) findViewById(R.id.y_forward);
		btnYForWard.setOnTouchListener(testMove);
		btnZUp = (Button) findViewById(R.id.z_up);
		btnZUp.setOnTouchListener(testMove);
		btnZDown = (Button) findViewById(R.id.z_down);
		btnZDown.setOnTouchListener(testMove);
		btnGetParam = (Button) findViewById(R.id.get_param);
		txtCoord = (TextView) findViewById(R.id.txt_test);
		txtSpeed = (TextView) findViewById(R.id.speed);
		speedBar = (SeekBar) findViewById(R.id.speedBar);
		speedBar.setMax(50);
		speedBar.setOnSeekBarChangeListener(new SpeedBar());
		btnLocate = (Button) findViewById(R.id.locate);
		btnDownload = (Button)findViewById(R.id.download);
		btnUpload = (Button)findViewById(R.id.uploadTask);
		txtUpload = (TextView)findViewById(R.id.txt_uploadInfo);
		et_number = (EditText) findViewById(R.id.et_getNumber);
		btnGetNumber = (Button) findViewById(R.id.bt_getNumber);
		
		et_number.addTextChangedListener(new MaxMinEditWatcher(10000, 100, et_number));
		et_number.setOnFocusChangeListener(new MaxMinFocusChangeListener(10000, 100, et_number));
		
		MessageMgr.INSTANCE.cmdDelayFlag = CmdParam.Cmd_Null;
		pointList = new ArrayList<>();
		handler = new RevHandler();// 建立RevHandler对象,用于接收返回的数据
		//					线程管理单例初始化
		//===========================
		SocketThreadManager.sharedInstance().setInputThreadHandler(handler);
		NetManager.instance().init(this);
		//===========================
		
//		myConnection = new AsyncConnection("192.168.16.254", 8080, 3000, handler);// 建立asynctask对象
//		myConnection.execute();// asynctask开始运行

//		MessageMgr.INSTANCE.setmConnection(myConnection);// 设置Asynctask对象引用
//		MessageMgr.INSTANCE.setHandler(handler);
//		MessageMgr.INSTANCE.startThread();
		btnReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				MessageMgr.INSTANCE.resetCoord();
				count++;
				if(count == 1){
					intClickFirst = System.currentTimeMillis();
					Log.d(TAG, "第一次点击："+intClickFirst);
				}else if(count ==2){
					intClickSecond = System.currentTimeMillis();
					Log.d(TAG, "第二次点击："+(intClickSecond-intClickFirst));
					if(intClickSecond - intClickFirst<1000){
						ToastUtil.displayPromptInfo(MainActivity.this, "连续点击两次事件");
					}
					count = 0;
					intClickFirst = 0;
					intClickSecond = 0;
				}
				
			}
		});
		/*btnReset.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_DOWN == event.getAction()){
					count++;
					if(count == 1){
						intClickFirst = System.currentTimeMillis();
					}else if(count ==2){
						intClickSecond = System.currentTimeMillis();
						if(intClickSecond - intClickFirst<1000){
							ToastUtil.displayPromptInfo(MainActivity.this, "连续点击两次事件");
						}
						count = 0;
						intClickFirst = 0;
						intClickSecond = 0;
					}
				}
				return true;
			}
		});*/

		btnGetParam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MessageMgr.INSTANCE.getMachineParam();
			}
		});

		btnLocate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//定位
				OrderParam.INSTANCE.setnXCoord(RobotParam.INSTANCE.XJourney2Pulse(200));
				OrderParam.INSTANCE.setnYCoord(RobotParam.INSTANCE.YJourney2Pulse(200));
				OrderParam.INSTANCE.setnZCoord(RobotParam.INSTANCE.ZJourney2Pulse(50));
				OrderParam.INSTANCE.setnSpeed(400);
				MessageMgr.INSTANCE.setCurCoord();
			}
		});
		
		btnDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//任务下载
				String str= "5, 0, 97, 98, 98, 98, 98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 61, 9, 0, 0, -49, 8, 0, 0, -60, 8, 0, 0, 0, 0, 0, 0, 50, 0, 50, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -24, 3, -24, 3, 50, 0, 5, 0, -56, 0, -56, 0, -106, 0, 0, 0, 16, 39, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 34, 7, 0, 0, 61, 9, 0, 0, -49, 8, 0, 0, -60, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 1, 0, 2, 0, 2, 0, 36, 21, 0, 0, -113, 22, 0, 0, -76, 19, 0, 0, 52, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 41, 0, 0, 0, -93, 0, -78, 92, 1, 0, -126, 0, 48, 0, 0, 0, -79, 34, 0, 0, 28, 27, 0, 0, -59, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 41, 0, 0, 0, 0, 0, -1, 8, 61, 0, 2, 0, 59, 2, 0, 0, 2, 0, 0, 0, 0, 0, 66, 0, 36, 0, 0, 0, -128, 33, 0, 0, 97, 26, 0, 0, -19, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 90, 0, -128, -124, 30, 0, 68, 0, 32, 0, 0, 0, -128, 33, 0, 0, 97, 26, 0, 0, -19, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 59, 2, 0, 0, -41, 0";
				byte[] buf = boooleanParse(str);
				ArrayList<Point> mylist = new ArrayList<>();
				MessageMgr.INSTANCE.analyseTask400(buf, buf.length, mylist);
				int pointNum = mylist.size();
				Point point = mylist.get(0);
				
				Log.d("sss", mylist.toString());
				
//				pointList = new ArrayList<Point>();
//				
//				Point p2 = new Point(RobotParam.INSTANCE.XJourney2Pulse(150), 
//						RobotParam.INSTANCE.YJourney2Pulse(150), 
//						RobotParam.INSTANCE.ZJourney2Pulse(0),
//						RobotParam.INSTANCE.UJourney2Pulse(0),
//						PointType.POINT_GLUE_ALONE);
//				PointGlueAloneParam param2 = new PointGlueAloneParam();
//				param2.setOutGlue(true);
//				param2.setUpHeight(0);
//				p2.setPointParam(param2);
//				pointList.add(p2);
//				
//				/*Point p3 = new Point(RobotParam.INSTANCE.XJourney2Pulse(100), 
//						RobotParam.INSTANCE.YJourney2Pulse(100), 
//						RobotParam.INSTANCE.ZJourney2Pulse(0),
//						RobotParam.INSTANCE.UJourney2Pulse(0),
//						PointType.POINT_GLUE_ALONE);
//				PointGlueAloneParam param3 = (PointGlueAloneParam)p3.getPointParam();
//				param3.setOutGlue(true);
//				param3.setUpHeight(0);
//				pointList.add(p3);
//				pointList.add(p3);
//				pointList.add(p3);*/
//				
//				TaskParam.INSTANCE.setStrTaskName("helloworld");
//				TaskParam.INSTANCE.setnStartX(0);
//				TaskParam.INSTANCE.setnStartY(0);
//				TaskParam.INSTANCE.setnStartZ(0);
//				TaskParam.INSTANCE.setnStartU(0);
//
//				
//				
//				MessageMgr.INSTANCE.taskDownload(pointList);
				
			}
		});
//		btnDownload.setClickable(false);
		
		btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				TaskParam.INSTANCE.setAllParamBacktoDefault();
//				TaskParam.INSTANCE.setnTaskNum(1);
				OrderParam.INSTANCE.setnTaskNum(2);
				pointList = new ArrayList<>();
				MessageMgr.INSTANCE.taskUpload(pointList);
			}
		});
		
		btnGetNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int number = Integer.parseInt(et_number.getText().toString());
				OrderParam.INSTANCE.setnTaskNum(number);
				MessageMgr.INSTANCE.isTaskExist();
			}
		});
		

	
	}
	
	private static byte[] boooleanParse(String value) {
		value = value.replace(" ", "");
		String[] StrSplite = value.split(",");
		byte[] aloneBoolean = new byte[StrSplite.length];
		for (int i = 0; i < StrSplite.length; i++) {
			aloneBoolean[i] = Byte.parseByte(StrSplite[i]);
			
		}

		return aloneBoolean;
	}

	/**
	 * <p>
	 * Title: Move
	 * <p>
	 * Description: 示教内部方法
	 * 
	 * @param moveDir
	 *            移动方向 0:正方向 1:反方向
	 * @param moveType
	 *            移动类型 0:连续 1:单步
	 * @param moveCoord
	 *            移动轴 0:x轴 1:y轴 2:z轴 3:u轴
	 * @param moveSpeed
	 *            移动速度 非询问命令均需要先发送询问下位机是否忙碌后在发送其他命令
	 */
	private void Move(int moveDir, int moveType, int moveCoord, int moveSpeed) {
		OrderParam.INSTANCE.setAllParamToZero();
		OrderParam.INSTANCE.setnMoveDir(moveDir);
		OrderParam.INSTANCE.setnMoveType(moveType);
		OrderParam.INSTANCE.setnMoveCoord(moveCoord);
		OrderParam.INSTANCE.setnSpeed(speedBar.getProgress());
		MessageMgr.INSTANCE.moveCoord();
	}

	/**
	 * <p>
	 * Title: Stop
	 * <p>
	 * Description: 示教停止内部方法
	 * 
	 * @param moveCoord
	 *            移动轴 0:x轴 1:y轴 2:z轴 3:u轴 非询问命令均需要先发送询问下位机是否忙碌后在发送其他命令
	 */
	private void Stop(int moveCoord) {
		OrderParam.INSTANCE.setAllParamToZero();
		OrderParam.INSTANCE.setnMoveCoord(moveCoord);
		MessageMgr.INSTANCE.stopCoord();
	}

	/**
	 * <p>Title: DisPlayInfoAfterGetMsg
	 * <p>Description: 显示收到的数据信息
	 * @param revBuffer
	 */
	private void DisPlayInfoAfterGetMsg(byte[] revBuffer) {
		int errCode;
		errCode = MessageMgr.INSTANCE.managingMessage(revBuffer);
//		Log.e(TAG, "errorCode:"+errCode);
		switch (errCode) {
		case 0:
			ToastUtil.displayPromptInfo(MainActivity.this, "校验失败");
			int temp = (int)revBuffer[3] & 0xff;
			if(((revBuffer[3] & 0x00ff) == 0x31)&&((revBuffer[2]&0x00ff)==0x79)){
				if((revBuffer[5]&0x00ff)==0){
					Log.d(TAG, "任务不存在");
				}
			}
			break;
		case 1: {
			int cmdFlag = ((revBuffer[2] & 0x00ff) << 8) | (revBuffer[3] & 0x00ff);
			Log.d(TAG, "cmdFlag:"+cmdFlag);
			if (cmdFlag == 0x1a00) {// 若是获取坐标命令返回的数据,解析坐标值
				Point coordPoint = MessageMgr.INSTANCE.analyseCurCoord(revBuffer);
				Log.d(TAG, "返回的Point:"+coordPoint.toString()+","+RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX()));
				coord = "X:" + RobotParam.INSTANCE.XPulse2Journey(coordPoint.getX()) + "*Y:"
						+ RobotParam.INSTANCE.YPulse2Journey(coordPoint.getY()) + "*Z:"
						+ RobotParam.INSTANCE.ZPulse2Journey(coordPoint.getZ()) + "*U:"
						+ RobotParam.INSTANCE.UPulse2Journey(coordPoint.getU());
				txtCoord.setText(coord);
			} else if (revBuffer[2] == 0x4A) {// 获取下位机参数成功
				ToastUtil.displayPromptInfo(MainActivity.this, "获取参数成功!");
			}
			if((revBuffer[3] & 0x00ff) == 0x31){
				Log.d(TAG, "查询任务");
				if((revBuffer[5]&0x00ff)==1){
					Log.d(TAG, "任务存在");
				}
			}
		}
			break;
		case 8421:
			//任务上传分包数据,不作处理
			break;
		case 1248:{
			
//			uploadInfo = "点数据：\n"
//					+ "点类型：\n" + pointList.get(0).getPointParam().getPointType()
//					+ "\n：x" + pointList.get(0).getX()
//					+ "\n：y" + pointList.get(0).getY()
//					+ "\n：z" + pointList.get(0).getZ();
//			txtUpload.setText(uploadInfo);
			if(!pointList.isEmpty()&&pointList.size()>0){
				Log.d(TAG , "长度："+pointList.size());
//				Log.d(TAG , pointList.toString());
				for(int i=0;i<pointList.size();i++){
					Log.d(TAG , (i+1)+":"+pointList.get(i).toString());
				}
				txtUpload.setText("长度："+pointList.size());
			}
			ToastUtil.displayPromptInfo(MainActivity.this, "上传完成");
		}
			break;
		case 1249:
			ToastUtil.displayPromptInfo(MainActivity.this, "上传失败");
			break;
		case 40101:
			ToastUtil.displayPromptInfo(MainActivity.this, "非法功能");
			break;
		case 40102:
			ToastUtil.displayPromptInfo(MainActivity.this, "非法数据地址");
			break;
		case 40103:
			ToastUtil.displayPromptInfo(MainActivity.this, "非法数据");
			break;
		case 40105:
			ToastUtil.displayPromptInfo(MainActivity.this, "设备忙");
			break;
		case 40109:
			ToastUtil.displayPromptInfo(MainActivity.this, "急停中");
			break;
		case 40110:
			ToastUtil.displayPromptInfo(MainActivity.this, "X轴光电报警");
			break;
		case 40111:
			ToastUtil.displayPromptInfo(MainActivity.this, "Y轴光电报警");
			break;
		case 40112:
			ToastUtil.displayPromptInfo(MainActivity.this, "Z轴光电报警");
			break;
		case 40113:
			ToastUtil.displayPromptInfo(MainActivity.this, "U轴光电报警");
			break;
		case 40114:
			ToastUtil.displayPromptInfo(MainActivity.this, "行程超限报警");
			break;
		case 40115:
			ToastUtil.displayPromptInfo(MainActivity.this, "任务下载失败");
			break;
		case 40116:
			ToastUtil.displayPromptInfo(MainActivity.this, "任务上传失败");
			break;
		case 40117:
			ToastUtil.displayPromptInfo(MainActivity.this, "任务模拟失败");
			break;
		case 40118:
			ToastUtil.displayPromptInfo(MainActivity.this, "示教指令错误");
			break;
		case 40119:
			ToastUtil.displayPromptInfo(MainActivity.this, "循迹定位失败");
			break;
		case 40120:
			ToastUtil.displayPromptInfo(MainActivity.this, "任务号不可用");
			break;
		case 40121:
			ToastUtil.displayPromptInfo(MainActivity.this, "初始化失败");
			break;
		case 40122:
			ToastUtil.displayPromptInfo(MainActivity.this, "API版本错误");
			break;
		case 40123:
			ToastUtil.displayPromptInfo(MainActivity.this, "程序升级失败");
			break;
		case 40124:
			ToastUtil.displayPromptInfo(MainActivity.this, "系统损坏");
			break;
		case 40125:
			ToastUtil.displayPromptInfo(MainActivity.this, "任务未加载");
			break;
		case 40126:
			ToastUtil.displayPromptInfo(MainActivity.this, "(Z轴)基点抬起高度过高");
			break;
		case 40127:
			ToastUtil.displayPromptInfo(MainActivity.this, "等待输入超时");
			break;
		default:
			ToastUtil.displayPromptInfo(MainActivity.this, "未知错误");
			break;
		}
	}

	/**
	 * <p>
	 * Title: SpeedBar
	 * <p>
	 * Description: 设置速度
	 * <p>
	 * Company: MingSeal .Ltd
	 * 
	 * @author lyq
	 * @date 2015年11月6日
	 */
	private class SpeedBar implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			txtSpeed.setText("" + seekBar.getProgress());
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			txtSpeed.setText("" + seekBar.getProgress());

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			txtSpeed.setText("" + seekBar.getProgress());
		}

	}

	/**
	 * <p>
	 * Title: TestMoveListener
	 * <p>
	 * Description: 示教按键响应内部类
	 * <p>
	 * Company: MingSeal .Ltd
	 * 
	 * @author lyq
	 * @date 2015年11月6日
	 */
	private class TestMoveListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.x_right: {//x+
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Move(0, 0, 0, 50);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Stop(0);
				}
			}
				break;
			case R.id.x_left: {//x-
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Move(1, 0, 0, 50);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Stop(0);
				}
			}
				break;
			case R.id.y_backward: {//y-
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Move(1, 0, 1, 50);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Stop(1);
				}
			}
				break;
			case R.id.y_forward: {//y+
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Move(0, 0, 1, 50);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Stop(1);
				}
			}
				break;
			case R.id.z_up: {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Move(0, 0, 2, 30);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Stop(2);
				}
			}
				break;
			case R.id.z_down: {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Move(1, 0, 2, 30);
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Stop(2);
				}
			}
				break;
			}
			return false;
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
			if (msg.what == 0x123) {
				// 获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer)msg.obj;
				byte[] buffer;
				buffer = temp.array();
//				byte[] revBuffer = (byte[]) msg.obj;
				DisPlayInfoAfterGetMsg(buffer);
			}else if(msg.what ==SocketInputThread.SocketInputUPLOADWhat){
				// 获取下位机上传的数据
				ByteBuffer temp = (ByteBuffer)msg.obj;
				byte[] buffer;
				buffer = temp.array();
//				byte[] revBuffer = (byte[]) msg.obj;
				DisPlayInfoAfterGetMsg(buffer);
			}
		}
	}
}
