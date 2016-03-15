package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡线起始点参数类
 * @author lyq
 */
public class PointWeldLineStartParam extends PointParam{
	
	private int sendSnSpeedFir; //一次送锡速度(单位:毫米/秒 mm/s)
	private int sendSnSumFir; //一次送锡量(单位:丝米dm)
	private int sendSnSpeedSec;//二次送锡速度(单位:毫米/秒 mm/s)
	private int sendSnSumSec;//二次送锡量(单位:丝米dm)
//	private int sendSnSpeedThird;
//	private int sendSnSumThird;
//	private int sendSnSpeedFourth;
//	private int sendSnSumFourth;
	
	private int snHeight; //送锡高度
	private int preSendSnSum; //预送锡量
	private int preSendSnSpeed; //预送锡速度
	private boolean isSn; //是否出锡
	private int moveSpeed; //轨迹速度(单位:毫米/秒 mm/s)
	private int preHeatTime; //预热时间
	private boolean[] snPort; //焊锡口
	
	/**
	 * @return 获取一次送锡速度(单位:毫米/秒 mm/s)
	 */
	public int getSendSnSpeedFir() {
		return sendSnSpeedFir;
	}
	
	/**
	 * 设置一次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSpeedFir 一次送锡速度
	 */
	public void setSendSnSpeedFir(int sendSnSpeedFir) {
		this.sendSnSpeedFir = sendSnSpeedFir;
	}
	
	/**
	 * @return 获取一次送锡量(单位:丝米dm)
	 */
	public int getSendSnSumFir() {
		return sendSnSumFir;
	}
	
	/**
	 * 设置一次送锡量(单位:丝米dm)
	 * @param sendSnSumFir 一次送锡量
	 */
	public void setSendSnSumFir(int sendSnSumFir) {
		this.sendSnSumFir = sendSnSumFir;
	}
	
	/**
	 * @return 获取二次送锡速度(单位:毫米/秒 mm/s)
	 */
	public int getSendSnSpeedSec() {
		return sendSnSpeedSec;
	}
	
	/**
	 * 设置二次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSpeedSec 二次送锡速度
	 */
	public void setSendSnSpeedSec(int sendSnSpeedSec) {
		this.sendSnSpeedSec = sendSnSpeedSec;
	}
	
	/**
	 * @return获取二次送锡量(单位:丝米dm)
	 */
	public int getSendSnSumSec() {
		return sendSnSumSec;
	}
	
	/**
	 * 设置二次送锡量(单位:丝米dm)
	 * @param sendSnSUmSec 二次送锡量
	 */
	public void setSendSnSumSec(int sendSnSUmSec) {
		this.sendSnSumSec = sendSnSUmSec;
	}
	
	/**
	 * @return 获取送锡高度
	 */
	public int getSnHeight() {
		return snHeight;
	}
	
	/**
	 * 设置送锡高度
	 * @param snHeight 送锡高度
	 */
	public void setSnHeight(int snHeight) {
		this.snHeight = snHeight;
	}
	
	/**
	 * @return 获取预送锡量
	 */
	public int getPreSendSnSum() {
		return preSendSnSum;
	}
	
	/**
	 * 获取预送锡量
	 * @param preSendSnSum 预送锡量
	 */
	public void setPreSendSnSum(int preSendSnSum) {
		this.preSendSnSum = preSendSnSum;
	}
	
	/**
	 * @return 获取预送锡速度
	 */
	public int getPreSendSnSpeed() {
		return preSendSnSpeed;
	}
	
	/**
	 * 设置预送锡速度
	 * @param preSendSnSpeed 预送锡速度
	 */
	public void setPreSendSnSpeed(int preSendSnSpeed) {
		this.preSendSnSpeed = preSendSnSpeed;
	}
	
	/**
	 * @return 获取是否出锡
	 */
	public boolean isSn() {
		return isSn;
	}
	
	/**
	 * 设置是否出锡
	 * @param isSn 是否出锡
	 */
	public void setSn(boolean isSn) {
		this.isSn = isSn;
	}
	
	/**
	 * @return 获取轨迹速度(单位:毫米/秒 mm/s)
	 */
	public int getMoveSpeed() {
		return moveSpeed;
	}
	
	/**
	 * 设置轨迹速度(单位:毫米/秒 mm/s)
	 * @param moveSpeed 轨迹速度
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	/**
	 * @return 获取预热时间
	 */
	public int getPreHeatTime() {
		return preHeatTime;
	}
	
	/**
	 * 设置预热时间
	 * @param preHeatTime 预热时间
	 */
	public void setPreHeatTime(int preHeatTime) {
		this.preHeatTime = preHeatTime;
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
	 * @param sendSnSpeedFir 一次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSumFir 一次送锡量(单位:丝米dm)
	 * @param sendSnSpeedSec 二次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSumSec 二次送锡量(单位:丝米dm)
	 * @param snHeight 送锡高度
	 * @param preSendSnSum 预送锡量
	 * @param preSendSnSpeed 预送锡速度
	 * @param isSn 是否出锡
	 * @param moveSpeed 轨迹速度(单位:毫米/秒 mm/s)
	 * @param preHeatTime 预热时间
	 */
	private void pointWeldLineStartParamInit(int sendSnSpeedFir, int sendSnSumFir, int sendSnSpeedSec, int sendSnSumSec,
			int snHeight, int preSendSnSum, int preSendSnSpeed, boolean isSn, int moveSpeed, int preHeatTime){
		this.sendSnSpeedFir = sendSnSpeedFir;
		this.sendSnSumFir = sendSnSumFir;
		this.sendSnSpeedSec = sendSnSpeedSec;
		this.sendSnSumSec = sendSnSumSec;
		this.snHeight = snHeight;
		this.preSendSnSum = preSendSnSum;
		this.preSendSnSpeed = preSendSnSpeed;
		this.isSn = isSn;
		this.moveSpeed = moveSpeed;
		this.preHeatTime = preHeatTime;
	}
	
	/**
	 * 焊锡线起始点参数构造方法,默认值为:
	 *  @sendSnSpeedFir 一次送锡速度 0(单位:毫米/秒 mm/s)
	 *  @sendSnSumFir 一次送锡量(单位:丝米dm) 0
	 *  @sendSnSpeedSec 二次送锡速度 0(单位:毫米/秒 mm/s)
	 *  @sendSnSumSec 二次送锡量 0(单位:丝米dm)
	 *  @snHeight 送锡高度 0
	 *  @preSendSnSum 预送锡量 0
	 *  @preSendSnSpeed 预送锡速度 0
	 *  @isSn 是否出锡 true
	 *  @moveSpeed 轨迹速度 0(单位:毫米/秒 mm/s)
	 *  @preHeatTime 预热时间 0
	 */
	public PointWeldLineStartParam(){
		pointWeldLineStartParamInit(0, 0, 0, 0, 0, 0, 0, true, 0, 0);
		super.setPointType(PointType.POINT_WELD_LINE_START);
		this.snPort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		this.snPort[0] = true;
	}
	
	public PointWeldLineStartParam(int sendSnSpeedFir, int sendSnSumFir, int sendSnSpeedSec, int sendSnSumSec,
			int snHeight, int preSendSnSum, int preSendSnSpeed, boolean isSn, int moveSpeed, int preHeatTime, boolean[] snPort){
		pointWeldLineStartParamInit(sendSnSpeedFir, sendSnSumFir, sendSnSpeedSec, sendSnSumSec, snHeight, preSendSnSum, preSendSnSpeed, isSn, moveSpeed, preHeatTime);
		super.setPointType(PointType.POINT_WELD_LINE_START);
		this.snPort = snPort;
	}
	
}
