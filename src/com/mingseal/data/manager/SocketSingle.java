/**
 * 
 */
package com.mingseal.data.manager;

import java.net.Socket;

/**
 * @author 商炎炳
 *
 */
public enum SocketSingle {
	INSTANCE;
	private Socket mySocket;
	private SocketSingle(){
		mySocket = new Socket();
	}
	public Socket getMySocket() {
		return mySocket;
	}
	public void setMySocket(Socket mySocket) {
		this.mySocket = mySocket;
	}
}
