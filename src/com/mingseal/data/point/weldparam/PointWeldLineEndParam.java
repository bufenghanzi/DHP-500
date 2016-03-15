package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡线结束点参数类
 * @author lyq
 */
public class PointWeldLineEndParam extends PointParam{
	
	private int stopSnTime; //停锡延时(单位：毫秒ms)
	private int upHeight; //抬起高度(单位：mm)
	private boolean isPause; //是否暂停
	
	/**
	 * @return 获取停锡延时(单位：毫秒ms)
	 */
	public int getStopSnTime() {
		return stopSnTime;
	}
	
	/**
	 * 停锡延时(单位：毫秒ms)
	 * @param stopSnTime 停锡延时
	 */
	public void setStopSnTime(int stopSnTime) {
		this.stopSnTime = stopSnTime;
	}
	
	/**
	 * @return 获取抬起高度(单位：mm)
	 */
	public int getUpHeight() {
		return upHeight;
	}
	
	/**
	 * 设置抬起高度(单位：mm)
	 * @param upHeight 抬起高度
	 */
	public void setUpHeight(int upHeight) {
		this.upHeight = upHeight;
	}
	
	/**
	 * @return 获取是否暂停
	 */
	public boolean isPause() {
		return isPause;
	}
	
	/**
	 * 设置是否暂停
	 * @param isPause 是否暂停
	 */
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
	
	/**
	 * 点胶线起始点参数私有初始化方法
	 * @param stopSnTime 停锡延时(单位：毫秒ms)
	 * @param upHeight 抬起高度(单位：mm)
	 * @param isPause 是否暂停
	 */
	private void pointWeldLineEndParamInit(int stopSnTime, int upHeight, boolean isPause){
		this.stopSnTime = stopSnTime;
		this.upHeight = upHeight;
		this.isPause = isPause;
	}
	
	/**
	 * 焊锡线起始点参数构造方法,默认值为
	 *  @stopSnTime 停锡延时 0(单位：毫秒ms)
	 *  @upHeight 抬起高度 0(单位：mm)
	 *  @isPause 是否暂停 否
	 */
	public PointWeldLineEndParam(){
		pointWeldLineEndParamInit(0, 0, false);
		super.setPointType(PointType.POINT_WELD_LINE_END);
	}
	
	/**
	 * 焊锡线结束点初始化构造方法
	 * @param stopSnTime 停锡延时(单位：毫秒ms)
	 * @param upHeight 抬起高度(单位：mm)
	 * @param isPause 是否暂停
	 */
	public PointWeldLineEndParam(int stopSnTime,
			int upHeight, boolean isPause){
		pointWeldLineEndParamInit(stopSnTime, upHeight, isPause);
		super.setPointType(PointType.POINT_WELD_LINE_END);
	}
	
}
