package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡面结束点参数类
 * @author lyq
 */
public class PointWeldFaceEndParam extends PointParam {
	
	private int stopSnTime; //停锡延时(单位：毫秒ms)
	private int upHeight; //抬起高度(单位：mm)
	private int lineNum; //直线条数
	private boolean startDir; //起始方向 true:x方向 false:y方向
	private boolean isPause; //是否暂停
	
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
	 * @return 获取直线条数
	 */
	public int getLineNum() {
		return lineNum;
	}
	
	/**
	 * 设置直线条数
	 * @param lineNum 直线条数
	 */
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	
	/**
	 * @return 获取起始方向 true:x方向 false:y方向
	 */
	public boolean isStartDir() {
		return startDir;
	}
	
	/**
	 * 设置起始方向 true:x方向 false:y方向
	 * @param startDir 起始方向
	 */
	public void setStartDir(boolean startDir) {
		this.startDir = startDir;
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
	 * 焊锡面结束点参数私有初始化方法
	 * @param stopSnTime 停锡延时(单位：毫秒ms)
	 * @param upHeight 抬起高度(单位：mm)
	 * @param lineNum 直线条数
	 * @param startDir 起始方向 true:x方向 false:y方向
	 * @param isPause 是否暂停
	 */
	private void pointWeldFaceEndParamInit(int stopSnTime, int upHeight, int lineNum,
			boolean startDir, boolean isPause){
		this.stopSnTime = stopSnTime;
		this.upHeight = upHeight;
		this.lineNum = lineNum;
		this.startDir = startDir;
		this.isPause = isPause;
	}
	
	/**
	 * 焊锡面结束点参数构造方法,默认值为:
	 * @stopSnTime 停锡延时 0 (单位：毫秒ms)
	 * @upHeight 抬起高度 0 (单位：mm)
	 * @lineNum 直线条数 5
	 * @startDir 起始方向 true:x方向
	 * @isPause 是否暂停 否
	 */
	public PointWeldFaceEndParam(){
		pointWeldFaceEndParamInit(0, 0, 5, true, false);
		super.setPointType(PointType.POINT_WELD_FACE_END);
	}
	
	/**
	 * 焊锡面结束点参数构造方法
	 * @param stopSnTime 停锡延时(单位：毫秒ms)
	 * @param upHeight 抬起高度(单位：mm)
	 * @param lineNum 直线条数
	 * @param startDir 起始方向 true:x方向 false:y方向
	 * @param isPause 是否暂停
	 */
	public PointWeldFaceEndParam(int stopSnTime, int upHeight, int lineNum,
			boolean startDir, boolean isPause){
		pointWeldFaceEndParamInit(stopSnTime, upHeight, lineNum, startDir, isPause);
		super.setPointType(PointType.POINT_WELD_FACE_END);
	}
	
}
