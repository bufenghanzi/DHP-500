package com.mingseal.data.param.robot;

/**
 * 机器系列枚举类
 * @author lyq
 */
public enum RobotSeries {
	Series_DJ(0x1),
	Series_DH(0x2),
	Series_LA(0x4),
	Series_100(0x100),
	Series_200(0x200),
	Series_400(0x400),
	Series_A(0x10000),
	Series_B(0x20000),
	Series_C(0x20000),
	Series_Unknown(0);
	
	private int robotSeriesNum;
	
	private RobotSeries(int robotSeriesNum){
		this.robotSeriesNum = robotSeriesNum;
	}
	
	/**
	 * @return 获取机器系列
	 */
	public int getValue(){
		return robotSeriesNum;
	}
}
