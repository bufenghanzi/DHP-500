package com.mingseal.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.manager.SocketSingle;
import com.mingseal.data.param.CmdParam;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * <p>Title: AsyncConnection
 * <p>Description: Socket异步读写类
 * <p>Company: MingSeal .Ltd
 * @author lyq
 * @date 2015年11月5日
 */
public class AsyncConnection extends AsyncTask<String, String, Exception>{
	
	private String url;
	private int port;
	private int timeout;
	
	private BufferedInputStream bis;
	private BufferedOutputStream os;
	private Socket socket;
	private boolean interrupted = false;
	private int count = 0;
	private byte[] inDatas = null;
	private byte[] subStream = null;
	private Handler handler;
//	private Timer timer;
	
	private String TAG = getClass().getName();
	
	
	/**
	 * <p>Title: 
	 * <p>Description: 构造方法
	 * @param url ip地址
	 * @param port 端口号
	 * @param timeout 超时时限
	 */
	public AsyncConnection(String url, int port, int timeout, Handler handler) {
		this.url = url;
		this.port = port;
		this.timeout = timeout;
		this.handler = handler;
	}
	
	@Override
	protected Exception doInBackground(String... params) {
		Exception error = null;
		Log.d(TAG, "Opening socket connection.");
		try {
			Log.d(TAG, "Opening socket connection.");
			socket = SocketSingle.INSTANCE.getMySocket();
			socket.connect(new InetSocketAddress(url, port), timeout);
			bis = new BufferedInputStream(socket.getInputStream());
			os = new BufferedOutputStream(socket.getOutputStream());
			while(!interrupted){
				if(MessageMgr.INSTANCE.cmdDelayFlag == CmdParam.Cmd_Device
/*						|| MessageMgr.INSTANCE.cmdFlag == CmdParam.Cmd_UpLoad*/){
					while(true){
						count = bis.available();
						if(count != 79){
							Thread.sleep(10);
						}
						else{
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
			}
		}
		catch(UnknownHostException ex){
			Log.e(TAG, "doInBackground(): " + ex.toString());
			error = interrupted? null : ex;
		}
		catch (IOException ex){
			Log.e(TAG, "doInBackground(): " + ex.toString());
			error = interrupted? null : ex;
		}
		catch (Exception ex) {
			Log.e(TAG, "doInBackground(): " + ex.toString());
			error = interrupted? null : ex;
		}finally {
			try {
				os.close();
				bis.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return error;
	}
	
	
	/**
	 * <p>Title: write
	 * <p>Description: socket数据写入方法
	 * @param data 数据
	 * @param len 数据长度
	 */
	public void write(byte[] data, int len){
		Log.d(TAG, "write(): data = " + data);
		try {
			os.write(data, 0, len);
			os.flush();
		} catch (IOException ex) {
			Log.e(TAG, "write(): data = " + ex.toString());
		} catch (NullPointerException ex) {
			Log.e(TAG, "write(): " + ex.toString());
		}
	}

	/**
	 * <p>Title: disconnect
	 * <p>Description: 断开连接
	 */
	public void disconnect(){
		try {
			Log.d(TAG, "Closing the socket connection.");
			interrupted = true;
			if(os != null & bis != null){
				os.close();
				bis.close();
			}
			if(socket != null){
				socket.close();
			}
		} catch (IOException ex) {
			Log.e(TAG, "disconnect(): " +  ex.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		Log.d(TAG, "onCancelled");
	}
}
