package com.mingseal.data.point.glueparam;

import java.util.Arrays;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶线起始点参数类
 * 
 * @author lyq
 */
public class PointGlueLineStartParam extends PointParam {

	private int outGlueTimePrev; // 出胶前延时
	private int outGlueTime; // 出胶(后)延时
	private boolean timeMode;
	/**
	 * 延时模式 true:联动（ETimeNode.TIME_MODE_GANGED_TIME） 延时模式
	 * false:定时（ETimeMode.TIME_MODE_FIXED_TIME）
	 */
	private int moveSpeed; // 轨迹速度
	private boolean isOutGlue; // 是否出胶
	private boolean[] gluePort; // 点胶口

	private int stopGlueTime; // 停胶(后)延时
	private int upHeight; // 抬起高度
	private int breakGlueLen; //断胶长度(单位: 毫米mm)
	private int drawDistance; // 拉丝距离(单位: 毫米mm)
	private int drawSpeed; //拉丝速度(单位: 毫米/秒mm/s)
	private int stopGlueTimePrev; // 停胶前延时

	/**
	 * 点胶线起始点参数私有初始化方法(未设置停胶前延时 stopGlueTimePrev ,先前代码未使用)
	 * 
	 * @param outGlueTimePrev
	 *            出胶前延时
	 * @param outGlueTime
	 *            出胶(后)延时
	 * @param timeMode
	 *            延时模式 true:联动（ETimeNode.TIME_MODE_GANGED_TIME）
	 *            false:定时（ETimeMode.TIME_MODE_FIXED_TIME）
	 * @param moveSpeed
	 *            轨迹速度
	 * @param isOutGlue
	 *            是否出胶
	 * @param stopGlueTime
	 *            停胶(后)延时
	 * @param upHeight
	 *            抬起高度
	 */
	private void pointGlueLineStartInit(int outGlueTimePrev, int outGlueTime, boolean timeMode, int moveSpeed,
			boolean isOutGlue, int stopGlueTime, int upHeight) {
		this.outGlueTimePrev = outGlueTimePrev;
		this.outGlueTime = outGlueTime;
		this.timeMode = timeMode;
		this.moveSpeed = moveSpeed;
		this.isOutGlue = isOutGlue;
		this.stopGlueTime = stopGlueTime;
		this.upHeight = upHeight;
	}

	/**
	 * 点胶线起始点参数构造方法,默认值为
	 * 
	 * @outGlueTimePrev 出胶前延时 0
	 * @outGlueTime 出胶(后)延时0
	 * @timeMode 延时模式 false true:联动（ETimeNode.TIME_MODE_GANGED_TIME）
	 *           false:定时（ETimeMode.TIME_MODE_FIXED_TIME）
	 * @moveSpeed 轨迹速度 1
	 * @isOutGlue 是否出胶 是
	 * @stopGlueTime 停胶(后)延时 0
	 * @upHeight 抬起高度 0
	 */
	public PointGlueLineStartParam() {
		pointGlueLineStartInit(0, 0, false, 1, true, 0, 0);
		super.setPointType(PointType.POINT_GLUE_LINE_START);
		this.gluePort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		this.gluePort[0] = true;
	}

	/**
	 * 点胶线起始点初始化构造方法(未设置停胶前延时 stopGlueTimePrev ,先前代码未使用)
	 * 
	 * @param outGlueTimePrev
	 *            出胶前延时
	 * @param outGlueTime
	 *            出胶(后)延时
	 * @param timeMode
	 *            延时模式 true:联动（ETimeNode.TIME_MODE_GANGED_TIME）
	 *            false:定时（ETimeMode.TIME_MODE_FIXED_TIME）
	 * @param moveSpeed
	 *            轨迹速度
	 * @param isOutGlue
	 *            是否出胶
	 * @param stopGlueTime
	 *            停胶(后)延时
	 * @param upHeight
	 *            抬起高度
	 * @param gluePort
	 *            点胶口数据
	 */
	public PointGlueLineStartParam(int outGlueTimePrev, int outGlueTime, boolean timeMode, int moveSpeed,
			boolean isOutGlue, int stopGlueTime, int upHeight, boolean[] gluePort) {
		pointGlueLineStartInit(outGlueTimePrev, outGlueTime, timeMode, moveSpeed, isOutGlue, stopGlueTime, upHeight);
		super.setPointType(PointType.POINT_GLUE_LINE_START);
		this.gluePort = gluePort;
	}

	/**
	 * @return 获取出胶前延时
	 */
	public int getOutGlueTimePrev() {
		return outGlueTimePrev;
	}

	/**
	 * 设置出胶前延时
	 * 
	 * @param outGlueTimePrev
	 *            出胶前延时
	 */
	public void setOutGlueTimePrev(int outGlueTimePrev) {
		this.outGlueTimePrev = outGlueTimePrev;
	}

	/**
	 * @return 获取出胶(后)延时
	 */
	public int getOutGlueTime() {
		return outGlueTime;
	}

	/**
	 * 设置出胶后延时
	 * 
	 * @param outGlueTime
	 *            出胶(后)延时
	 */
	public void setOutGlueTime(int outGlueTime) {
		this.outGlueTime = outGlueTime;
	}

	/**
	 * 获取延时模式
	 * 
	 * @true:联动（ETimeNode.TIME_MODE_GANGED_TIME）
	 * @false:定时（ETimeMode.TIME_MODE_FIXED_TIME）
	 * @return 延时模式
	 */
	public boolean isTimeMode() {
		return timeMode;
	}

	/**
	 * 设置延时模式
	 * 
	 * @true:联动（ETimeNode.TIME_MODE_GANGED_TIME）
	 * @false:定时（ETimeMode.TIME_MODE_FIXED_TIME）
	 * @param timeMode
	 *            延时模式
	 */
	public void setTimeMode(boolean timeMode) {
		this.timeMode = timeMode;
	}

	/**
	 * @return 获取轨迹速度
	 */
	public int getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * 设置轨迹速度
	 * 
	 * @param moveSpeed
	 *            轨迹速度
	 */
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * @return 获取是否出胶
	 */
	public boolean isOutGlue() {
		return isOutGlue;
	}

	/**
	 * 设置是否出胶
	 * 
	 * @param isOutGlue
	 *            是否出胶
	 */
	public void setOutGlue(boolean isOutGlue) {
		this.isOutGlue = isOutGlue;
	}

	/**
	 * @return 获取点胶口数据
	 */
	public boolean[] getGluePort() {
		return gluePort;
	}

	/**
	 * 设置点胶口数据
	 * 
	 * @param gluePort
	 *            点胶口数据
	 */
	public void setGluePort(boolean[] gluePort) {
		this.gluePort = gluePort;
	}

	/**
	 * @return 获取停胶前延时
	 */
	public int getStopGlueTimePrev() {
		return stopGlueTimePrev;
	}

	/**
	 * 设置停胶前延时
	 * 
	 * @param stopGlueTimePrev
	 *            停胶前延时
	 */
	public void setStopGlueTimePrev(int stopGlueTimePrev) {
		this.stopGlueTimePrev = stopGlueTimePrev;
	}

	/**
	 * @return 获取停胶(后)延时
	 */
	public int getStopGlueTime() {
		return stopGlueTime;
	}

	/**
	 * 设置停胶后延时
	 * 
	 * @param stopGlueTime
	 *            停胶后延时
	 */
	public void setStopGlueTime(int stopGlueTime) {
		this.stopGlueTime = stopGlueTime;
	}

	/**
	 * @return 获取抬起高度
	 */
	public int getUpHeight() {
		return upHeight;
	}

	/**
	 * 设置抬起高度
	 * 
	 * @param upHeight
	 *            抬起高度
	 */
	public void setUpHeight(int upHeight) {
		this.upHeight = upHeight;
	}
	
	/**
	 * <p>Title: getBreakGlueLen
	 * <p>Description: 获取断胶长度
	 * @return 断胶长度
	 */
	public int getBreakGlueLen() {
		return breakGlueLen;
	}

	/**
	 * <p>Title: setBreakGlueLen
	 * <p>Description: 设置断胶长度
	 * @param breakGlueLen 断胶长度
	 */
	public void setBreakGlueLen(int breakGlueLen) {
		this.breakGlueLen = breakGlueLen;
	}

	/**
	 * <p>Title: getDrawDistance
	 * <p>Description: 获取拉丝距离
	 * @return 拉丝距离
	 */
	public int getDrawDistance() {
		return drawDistance;
	}

	/**
	 * <p>Title: setDrawDistance
	 * <p>Description: 设置拉丝距离
	 * @param drawDistance 拉丝距离
	 */
	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}

	/**
	 * <p>Title: getDrawSpeed
	 * <p>Description: 获取拉丝速度
	 * @return 拉丝速度
	 */
	public int getDrawSpeed() {
		return drawSpeed;
	}

	/**
	 * <p>Title: setDrawSpeed
	 * <p>Description: 设置拉丝速度
	 * @param drawSpeed 拉丝速度
	 */
	public void setDrawSpeed(int drawSpeed) {
		this.drawSpeed = drawSpeed;
	}

	@Override
	public String toString() {
		return "PointGlueLineStartParam [outGlueTimePrev=" + outGlueTimePrev + ", outGlueTime=" + outGlueTime
				+ ", timeMode=" + timeMode + ", moveSpeed=" + moveSpeed + ", isOutGlue=" + isOutGlue + ", gluePort="
				+ Arrays.toString(gluePort) + ", stopGlueTimePrev=" + stopGlueTimePrev + ", stopGlueTime="
				+ stopGlueTime + ", upHeight=" + upHeight + ", get_id()=" + get_id() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + breakGlueLen;
		result = prime * result + drawDistance;
		result = prime * result + drawSpeed;
		result = prime * result + Arrays.hashCode(gluePort);
		result = prime * result + (isOutGlue ? 1231 : 1237);
		result = prime * result + moveSpeed;
		result = prime * result + outGlueTime;
		result = prime * result + outGlueTimePrev;
		result = prime * result + stopGlueTime;
		result = prime * result + stopGlueTimePrev;
		result = prime * result + (timeMode ? 1231 : 1237);
		result = prime * result + upHeight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointGlueLineStartParam other = (PointGlueLineStartParam) obj;
		if (breakGlueLen != other.breakGlueLen)
			return false;
		if (drawDistance != other.drawDistance)
			return false;
		if (drawSpeed != other.drawSpeed)
			return false;
		if (!Arrays.equals(gluePort, other.gluePort))
			return false;
		if (isOutGlue != other.isOutGlue)
			return false;
		if (moveSpeed != other.moveSpeed)
			return false;
		if (outGlueTime != other.outGlueTime)
			return false;
		if (outGlueTimePrev != other.outGlueTimePrev)
			return false;
		if (stopGlueTime != other.stopGlueTime)
			return false;
		if (stopGlueTimePrev != other.stopGlueTimePrev)
			return false;
		if (timeMode != other.timeMode)
			return false;
		if (upHeight != other.upHeight)
			return false;
		return true;
	}

	public static final Parcelable.Creator<PointGlueLineStartParam> CREATOR = new Creator<PointGlueLineStartParam>() {

		@Override
		public PointGlueLineStartParam[] newArray(int size) {
			return new PointGlueLineStartParam[size];
		}

		@Override
		public PointGlueLineStartParam createFromParcel(Parcel source) {
			PointGlueLineStartParam point = new PointGlueLineStartParam();
			point.outGlueTimePrev = source.readInt();
			point.outGlueTime = source.readInt();
			point.timeMode = (source.readInt() == 0) ? false : true;
			point.moveSpeed = source.readInt();
			point.isOutGlue = (source.readInt() == 0) ? false : true;
			boolean[] val = null;
			val = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
			source.readBooleanArray(val);
			point.gluePort = val;
			point.stopGlueTimePrev = source.readInt();
			point.stopGlueTime = source.readInt();
			point.upHeight = source.readInt();
			point.set_id(source.readInt());

			return point;
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(outGlueTimePrev);
		dest.writeInt(outGlueTime);
		dest.writeInt((boolean) timeMode ? 1 : 0);
		dest.writeInt(moveSpeed);
		dest.writeInt((boolean) isOutGlue ? 1 : 0);
		dest.writeBooleanArray(gluePort);
		dest.writeInt(stopGlueTimePrev);
		dest.writeInt(stopGlueTime);
		dest.writeInt(upHeight);
		dest.writeInt(get_id());
	}

}
