package com.mingseal.communicate;

import android.os.Handler;


public class SocketThreadManager
{
	
	private static SocketThreadManager s_SocketManager = null;
	
	private SocketInputThread mInputThread = null;
	
	private SocketOutputThread mOutThread = null;
	
	 private SocketHeartThread mHeartThread = null;
//	private Handler handler;

	
	// 获取单例
	public static SocketThreadManager sharedInstance()
	{
		if (s_SocketManager == null)
		{
			s_SocketManager = new SocketThreadManager();
			s_SocketManager.startThreads();
		}
		return s_SocketManager;
	}
	
	// 单例，不允许在外部构建对象
	private SocketThreadManager()
	{
		mInputThread = new SocketInputThread();
		mOutThread = new SocketOutputThread();
//		mHeartThread=new SocketHeartThread();
	}
	
	/**
	 * 启动线程
	 */
	
	private void startThreads()
	{
		mInputThread.start();
		mInputThread.setStart(true);
		mOutThread.start();
		mOutThread.setStart(true);
//		mHeartThread.start();
//		mHeartThread.setStart(true);
//		TCPClient.instance().reConnect();
	}
	
	public void setInputThreadHandler(Handler handler){
		mInputThread.setSocketInputThreadHandler(handler);
	}
	
	/**
	 * stop线程
	 */
	public void stopThreads()
	{
//		mHeartThread.setStart(false);
		mInputThread.setStart(false);
		mOutThread.setStart(false);
		
	}
	
	/**
	 * @Title  releaseInstance
	 * @Description 释放单例
	 * @author wj
	 */
	public static void releaseInstance()
	{
		if (s_SocketManager != null)
		{
			s_SocketManager.stopThreads();
			s_SocketManager = null;
		}
//		if(TCPClient.instance().isConnect()){
//			TCPClient.instance().closeTCPSocket();
//		}
	}
	
//	public void sendMsg(byte [] buffer, Handler handler)
//	{
//		MsgEntity entity = new MsgEntity(buffer, handler);
//		mOutThread.addMsgToSendList(entity);
//	}
	public void sendMsg(byte[] buffer) {
		MsgEntity entity = new MsgEntity(buffer);
		mOutThread.addMsgToSendList(entity);
	}
}
