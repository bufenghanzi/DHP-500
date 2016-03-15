package com.mingseal.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.CmdParam;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class SocketThread implements Runnable {
	private Socket s;
	//定义向UI线程发送消息的Handler对象
	public Handler handler;
	//定义接受UI线程的Handler对象
	public Handler revHandler;
	
	private int count = 0;
	private byte[] inDatas = null;
//	该线程处理Socket所对应的输入输出流
	BufferedReader br = null;
	BufferedInputStream bis = null;
	OutputStream os = null;
	
	public SocketThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		s = new Socket();
		try {
			s.connect(new InetSocketAddress("192.168.16.254", 8080));
//			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bis = new BufferedInputStream(s.getInputStream());
			os = s.getOutputStream();
			//启动一条子线程来读取服务器相应的数据
			new Thread(){

				@Override
				public void run() {
//					String content = null;
					// 不断的读取Socket输入流的内容
					while(true){
//						=================
//						int count = 0;
//						byte[] inDatas = null;
//						try {
//							while(count == 0){
//								count = bis.available();
//							}
//							inDatas = new byte[count];
//							bis.read(inDatas);
//						}catch (IOException e) {
//							e.printStackTrace();
//						}finally {
//							count = 0;
//							Message msg = new Message();
//							msg.what = 0x123;
//							msg.obj = inDatas;
//							handler.sendMessage(msg);
//						}
//						===================
						try {
							if(MessageMgr.INSTANCE.cmdDelayFlag == CmdParam.Cmd_Device
/*						|| MessageMgr.INSTANCE.cmdFlag == CmdParam.Cmd_UpLoad*/){
								while(true){
									count = bis.available();
									if(count != 79){
										Thread.sleep(10);
									}else{
										inDatas = new byte[79];
										bis.read(inDatas);
										MessageMgr.INSTANCE.cmdDelayFlag = CmdParam.Cmd_Null;
										Message msg = new Message();
										msg.what = 0x123;
										msg.obj = inDatas;
										handler.sendMessage(msg);
										break;
									}
								}
							}else{
								count = bis.available();
								if(count > 0){
									inDatas = new byte[count];
									bis.read(inDatas);
									Message msg = new Message();
									msg.what = 0x123;
									msg.obj = inDatas;
									handler.sendMessage(msg);
								}
							}
						} catch (IOException e) {
							Log.e("SocketThread", "IOException:" + e.toString());
							e.printStackTrace();
						} catch (InterruptedException e) {
							Log.e("SocketThread", "InterruptedException:" + e.toString());
							e.printStackTrace();
						}
					}
				}
				
			}.start();
			//为当前线程初始化Looper
			Looper.prepare();
			//创建revHandler对象
			revHandler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
//					super.handleMessage(msg);
					//接收到UI线程中用户输入的数据
					if(msg.what == 0x345){
						try {
							os.write((byte[]) msg.obj, 0, msg.arg1);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			//启动Looper
			Looper.loop();
		}catch(SocketTimeoutException e){
			Message msg = new Message();
			msg.what = 0x001;
			handler.sendMessage(msg);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
