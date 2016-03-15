package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡面起始点参数类
 * @author lyq
 */
public class PointWeldFaceStartParam extends PointParam {
	
	private int preHeatTime;		//预热时间(单位：毫秒ms)
	private int moveSpeed;		//轨迹速度(单位：毫米/秒mm/s)
	private boolean isSn;			//是否连续出锡
	private boolean[] snPort;	//焊锡口
	
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
	 * @return 获取是否连续出锡
	 */
	public boolean isSn() {
		return isSn;
	}
	
	/**
	 * 设置是否连续出锡
	 * @param isSn 是否连续出锡
	 */
	public void setSn(boolean isSn) {
		this.isSn = isSn;
	}
	
	/**
	 * @return 获取焊锡口数据
	 */
	public boolean[] getSnPort() {
		return snPort;
	}
	
	/**
	 * 设置焊锡口数据
	 * @param snPort 焊锡口
	 */
	public void setSnPort(boolean[] snPort) {
		this.snPort = snPort;
	}
	
	/**
	 * 焊锡线起始点参数私有初始化方法
	 * @param preHeatTime 预热时间(单位：毫秒ms)
	 * @param moveSpeed 轨迹速度(单位：毫米/秒mm/s)
	 * @param isSn 是否连续出锡
	 */
	private void pointWeldFaceStartParamInit(int preHeatTime, int moveSpeed,
			boolean isSn){
		this.preHeatTime = preHeatTime;
		this.moveSpeed = moveSpeed;
		this.isSn = isSn;
	}
	
	/**
	 * 焊锡面起始点参数构造方法,默认值为:
	 * @preHeatTime 预热时间 0 (单位：毫秒ms)
	 * @moveSpeed 轨迹速度 0 (单位：毫米/秒mm/s)
	 * @isSn 是否连续出锡 是
	 */
	public PointWeldFaceStartParam(){
		pointWeldFaceStartParamInit(0, 0, true);
		super.setPointType(PointType.POINT_WELD_FACE_START);
		this.snPort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		this.snPort[0] = true;
	}
	
	/**
	 * 焊锡面起始点参数构造方法
	 * @param preHeatTime 预热时间(单位：毫秒ms)
	 * @param moveSpeed 轨迹速度(单位：毫米/秒mm/s)
	 * @param isSn 是否连续出锡
	 * @param snPort 焊锡口
	 */
	public PointWeldFaceStartParam(int preHeatTime, int moveSpeed,
			boolean isSn, boolean[] snPort){
		pointWeldFaceStartParamInit(preHeatTime, moveSpeed, isSn);
		super.setPointType(PointType.POINT_WELD_FACE_START);
		this.snPort = snPort;
	}
	
}
