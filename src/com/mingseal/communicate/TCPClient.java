package com.mingseal.communicate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;

import com.mingseal.application.UserApplication;

import android.os.Handler;
import android.util.Log;

/**
 * NIO TCP 客户端
 * 
 */
public class TCPClient {
	private static final String TAG = "TCPClient";
	// 信道选择器
	private Selector selector;

	// 与服务器通信的信道
	SocketChannel socketChannel;

	// 要连接的服务器Ip地址
	private String hostIp;

	// 要连接的远程服务器在监听的端口
	private int hostListenningPort;

	private static TCPClient s_Tcp = null;

	public boolean isInitialized = false;

	private Handler handler;

	/**
	 * @Title instance
	 * @Description dcl
	 * @author wj
	 * @return
	 */
	public static TCPClient instance() {
		if (s_Tcp == null) {
			synchronized (TCPClient.class) {
				if (s_Tcp == null) {
					s_Tcp = new TCPClient(UserApplication.getHandler(),
							Const.SOCKET_SERVER, Const.SOCKET_PORT);
				}
			}
		}
		return s_Tcp;
	}

	/**
	 * 构造函数
	 * 
	 * @param handler
	 * 
	 * @param HostIp
	 * @param HostListenningPort
	 * @throws IOException
	 */
	public TCPClient(Handler handler, String HostIp, int HostListenningPort) {
		this.hostIp = HostIp;
		this.hostListenningPort = HostListenningPort;
		this.handler = handler;
		try {
			initialize();
			this.isInitialized = true;
		} catch (Exception e) {
			this.isInitialized = false;
			e.printStackTrace();
		}
	}

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	public void initialize() {
		boolean done = false;

		try {
//			new Timer().schedule(tt, 10000);
			// 打开监听信道并设置为非阻塞模式,将该通道的serversocket绑定到port端口
			socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,
					hostListenningPort));
			if (socketChannel != null) {
				socketChannel.socket().setTcpNoDelay(false);
				socketChannel.socket().setKeepAlive(true);
				// 设置 读socket的timeout时间,60s，传输大文件需要设置得大一些
				socketChannel.socket().setSoTimeout(Const.SOCKET_READ_TIMOUT);
				socketChannel.configureBlocking(false);

				// 打开并注册选择器到信道
				selector = Selector.open();
				if (selector != null) {
					socketChannel.register(selector, SelectionKey.OP_READ);
					// 连接成功
					handler.sendEmptyMessage(-1);
					done = true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (!done && selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!done) {
				try {
					socketChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	TimerTask tt = new TimerTask() {
		@Override
		public void run() {
			if (socketChannel == null || !socketChannel.isConnected()) {
				try {
					throw new SocketTimeoutException("连接超时");
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					 handler.sendEmptyMessage(-6); // 连接超时
				}
			}
		}

	};

	static void blockUntil(SelectionKey key, long timeout) throws IOException {

		int nkeys = 0;
		if (timeout > 0) {
			nkeys = key.selector().select(timeout);

		} else if (timeout == 0) {
			nkeys = key.selector().selectNow();
		}

		if (nkeys == 0) {
			throw new SocketTimeoutException();
		}
	}

	/**
	 * 发送字符串到服务器
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMsg(String message) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("utf-8"));

		if (socketChannel == null) {
			throw new IOException();
		}
		socketChannel.write(writeBuffer);
	}

	/**
	 * 发送数据
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	public void sendMsg(byte[] bytes) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap(bytes);

		if (socketChannel == null) {
			throw new IOException();
		}
		Log.d(TAG, "" + writeBuffer);
		socketChannel.write(writeBuffer);
	}

	/**
	 * 
	 * @return
	 */
	public synchronized Selector getSelector() {
		return this.selector;
	}

	/**
	 * Socket连接是否是正常的
	 * 
	 * @return
	 */
	public boolean isConnect() {
		boolean isConnect = false;
		if (this.isInitialized) {
			isConnect = this.socketChannel.isConnected();
		}
		return isConnect;
	}

	/**
	 * 关闭socket 重新连接
	 * 
	 * @return
	 */
	public boolean reConnect() {
		closeTCPSocket();

		try {
			initialize();
			isInitialized = true;
		} catch (Exception e) {
			isInitialized = false;
			e.printStackTrace();
		}
		return isInitialized;
	}

	/**
	 * 服务器是否关闭，通过发送一个socket信息
	 * 
	 * @return
	 */
	public boolean canConnectToServer() {
		try {
			if (socketChannel != null) {
				socketChannel.socket().sendUrgentData(0xff);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "========>心跳停止！");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "========>心跳停止！");
			return false;
		}
		return true;
	}

	/**
	 * 关闭socket
	 */
	public void closeTCPSocket() {
		try {
			if (socketChannel != null) {
				socketChannel.close();
			}

		} catch (IOException e) {

		}
		try {
			if (selector != null) {
				selector.close();
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 每次读完数据后，需要重新注册selector，读取数据
	 */
	public synchronized void repareRead() {
		if (socketChannel != null) {
			try {
				selector = Selector.open();
				socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (ClosedChannelException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
