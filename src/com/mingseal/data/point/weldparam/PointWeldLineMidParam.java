package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡线中间点参数类
 * @author lyq
 */
public class PointWeldLineMidParam extends PointParam{
	
	private int moveSpeed; //轨迹速度(单位：毫米/秒mm/s)
	private int sendSnSpeed; //送锡速度(单位：毫米/秒mm/s)
	private int stopSnTime; //停锡延时(单位：毫秒ms)
	private int preHeatTime; //预热时间(单位：毫秒ms)
	
	/**
	 * @return 获取轨迹速度(单位：毫米/秒mm/s)
	 */
	public int getMoveSpeed() {
		return moveSpeed;
	}
	
	/**
	 * 设置轨迹速度(单位：毫米/秒mm/s)
	 * @param moveSpeed 轨迹速度
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	/**
	 * @return 获取送锡速度(单位：毫米/秒mm/s)
	 */
	public int getSendSnSpeed() {
		return sendSnSpeed;
	}
	
	/**
	 * 设置送锡速度(单位：毫米/秒mm/s)
	 * @param sendSnSpeed 送锡速度
	 */
	public void setSendSnSpeed(int sendSnSpeed) {
		this.sendSnSpeed = sendSnSpeed;
	}
	
	/**
	 * @return 获取停锡延时(单位：毫秒ms)
	 */
	public int getStopSnTime() {
		return stopSnTime;
	}
	
	/**
	 * 设置停锡延时(单位：毫秒ms)
	 * @param stopSnTime 停锡延时
	 */
	public void setStopSnTime(int stopSnTime) {
		this.stopSnTime = stopSnTime;
	}
	
	/**
	 * @return 获取预热时间(单位：毫秒ms)
	 */
	public int getPreHeatTime() {
		return preHeatTime;
	}
	
	/**
	 * 设置预热时间(单位：毫秒ms)
	 * @param preHeatTime 预热时间
	 */
	public void setPreHeatTime(int preHeatTime) {
		this.preHeatTime = preHeatTime;
	}
	
	/**
	 * 焊锡线中间点参数私有初始化方法
	 * @param moveSpeed 轨迹速度(单位：毫米/秒mm/s)
	 * @param sendSnSpeed 送锡速度(单位：毫米/秒mm/s)
	 * @param stopSnTime 停锡延时(单位：毫秒ms)
	 * @param preHeatTime 预热时间(单位：毫秒ms)
	 */
	private void pointWeldLineMidParamInit(int moveSpeed, int sendSnSpeed, 
			int stopSnTime, int preHeatTime){
		this.moveSpeed = moveSpeed;
		this.sendSnSpeed = sendSnSpeed;
		this.stopSnTime = stopSnTime;
		this.preHeatTime = preHeatTime;
	}
	
	/**
	 * 焊锡线中间点参数构造方法,默认值为:
	 *  @moveSpeed 轨迹速度 0 (单位：毫米/秒mm/s)
	 *  @sendSnSpeed 送锡速度 10 (单位：毫米/秒mm/s)
	 *  @stopSnTime 停锡延时 0 (单位：毫秒ms)
	 *  @preHeatTime 预热时间 0 (单位：毫秒ms)
	 */
	public PointWeldLineMidParam(){
		pointWeldLineMidParamInit(0, 10, 0, 0);
		super.setPointType(PointType.POINT_WELD_LINE_MID);
	}
	
	/**
	 * 焊锡线中间点参数含参构造方法
	 * @param moveSpeed 轨迹速度(单位：毫米/秒mm/s)
	 * @param sendSnSpeed 送锡速度(单位：毫米/秒mm/s)
	 * @param stopSnTime 停锡延时(单位：毫秒ms)
	 * @param preHeatTime 预热时间(单位：毫秒ms)
	 */
	public PointWeldLineMidParam(int moveSpeed, int sendSnSpeed,
			int stopSnTime, int preHeatTime){
		pointWeldLineMidParamInit(moveSpeed, sendSnSpeed, stopSnTime, preHeatTime);
		super.setPointType(PointType.POINT_WELD_LINE_MID);
	}

}
