package com.mingseal.communicate;

import android.R.integer;

public class Const {

	public final static String SOCKET_SERVER = "192.168.11.254";

	public final static int SOCKET_PORT = 8080;

	// 默认timeout 时间 60s
	public final static int SOCKET_TIMOUT = 60 * 1000;
	//60s超时
	public final static int SOCKET_READ_TIMOUT = 60 * 1000;

	// 如果没有连接无服务器。读线程的sleep时间
	public final static int SOCKET_SLEEP_SECOND = 3;

	 //心跳包发送间隔时间
	 public final static int SOCKET_HEART_SECOND =3 ;
	 //搜索框最大值
	 public final static int SEARCH_MAX=11000;
	 //搜索框最小值
	 public final static int SEARCH_MIN=1;
	//
	// public final static String BC = "BC";
	/*=================== begin ===================*/
	/**
	 * 独立点
	 */
	public final static int POINTGLUEALONE_CLICK       = 100;
	
	/**
	 *清胶点 
	 */
	public final static int POINTGLUECLEAR_CLICK       = 101;
	/**
	 * 面终点
	 */
	public final static int POINTGLUEFACEEND_CLICK     = 102;
	/**
	 *面起点 
	 */
	public final static int POINTGLUEFACESTART_CLICK   = 103;
	/**
	 *输入io 
	 */
	public final static int POINTGLUEINPUT_CLICK       = 104;
	/**
	 * 结束点
	 */
	public final static int POINTGLUELINEEND_CLICK     = 105;
	/**
	 * 中间点
	 */
	public final static int POINTGLUELINEMID_CLICK     = 106;
	/**
	 * 起始点
	 */
	public final static int POINTGLUELINESTART_CLICK     = 107;
	/**
	 * 输出io
	 */
	public final static int POINTGLUEOUTPUT_CLICK     = 108;
	/*===================  方案置顶  ===================*/
	
	public final static int POINTGLUEALONE_TOP       = 109;
	public final static int POINTGLUECLEAR_TOP       = 110;
	public final static int POINTGLUEFACEEND_TOP     = 111;
	public final static int POINTGLUEFACESTART_TOP   = 112;
	public final static int POINTGLUEINPUT_TOP       = 113;
	public final static int POINTGLUELINEEND_TOP     = 114;
	public final static int POINTGLUELINEMID_TOP     = 115;
	public final static int POINTGLUELINESTART_TOP     = 116;
	public final static int POINTGLUEOUTPUT_TOP     = 117;
	/*===================  方案删除  ===================*/
	public final static int POINTGLUEALONE_DEL       = 118;
	public final static int POINTGLUECLEAR_DEL       = 119;
	public final static int POINTGLUEFACEEND_DEL     = 120;
	public final static int POINTGLUEFACESTART_DEL   = 121;
	public final static int POINTGLUEINPUT_DEL       = 122;
	public final static int POINTGLUELINEEND_DEL     = 123;
	public final static int POINTGLUELINEMID_DEL     = 124;
	public final static int POINTGLUELINESTART_DEL     = 125;
	public final static int POINTGLUEOUTPUT_DEL     = 126;
	/*===================  end  ===================*/

}
