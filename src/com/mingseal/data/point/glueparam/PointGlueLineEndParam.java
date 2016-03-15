package com.mingseal.data.point.glueparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶线结束点参数类
 * 
 * @author lyq
 */
public class PointGlueLineEndParam extends PointParam {

	private int stopGlueTimePrev; // 停胶前延时
	private int stopGlueTime; // 停胶(后)延时
	private int upHeight; // 抬起高度
	private int breakGlueLen; // 提前停胶距离
	private int drawDistance; // 拉丝距离
	private int drawSpeed; // 拉丝速度
	private boolean isPause; // 是否暂停

	/**
	 * 点胶线结束点参数构造方法,默认值为
	 * 
	 * @stopGlueTimePrev 停胶前延时 0
	 * @stopGlueTime 停胶(后)延时 0
	 * @upHeight 抬起高度 0
	 * @breakGlueLen 断胶长度 0
	 * @drawDistance 拉丝距离 0
	 * @drawSpeed 拉丝速度 0
	 * @isPause 是否暂停 否
	 */
	public PointGlueLineEndParam() {
		pointGlueLineEndInit(0, 0, 0, 0, 0, 0, false);
		super.setPointType(PointType.POINT_GLUE_LINE_END);
	}

	/**
	 * 点胶线结束点初始化构造方法
	 * 
	 * @param stopGlueTimePrev
	 *            停胶前延时
	 * @param stopGlueTime
	 *            停胶(后)延时
	 * @param upHeight
	 *            抬起高度
	 * @param breakGlueLen
	 *            断胶长度
	 * @param drawDistance
	 *            拉丝距离
	 * @param drawSpeed
	 *            拉丝速度
	 * @param isPause
	 *            是否暂停
	 */
	public PointGlueLineEndParam(int stopGlueTimePrev, int stopGlueTime, int upHeight, int breakGlueLen,
			int drawDistance, int drawSpeed, boolean isPause) {
		pointGlueLineEndInit(stopGlueTimePrev, stopGlueTime, upHeight, breakGlueLen, drawDistance, drawSpeed, isPause);
		super.setPointType(PointType.POINT_GLUE_LINE_END);

	}

	/**
	 * 点胶线结束点参数私有初始化方法
	 * 
	 * @param stopGlueTimePrev
	 *            停胶前延时
	 * @param stopGlueTime
	 *            停胶(后)延时
	 * @param upHeight
	 *            抬起高度
	 * @param breakGlueLen
	 *            断胶长度
	 * @param drawDistance
	 *            拉丝距离
	 * @param drawSpeed
	 *            拉丝速度
	 * @param isPause
	 *            是否暂停
	 */
	private void pointGlueLineEndInit(int stopGlueTimePrev, int stopGlueTime, int upHeight, int breakGlueLen,
			int drawDistance, int drawSpeed, boolean isPause) {
		this.stopGlueTimePrev = stopGlueTimePrev;
		this.stopGlueTime = stopGlueTime;
		this.upHeight = upHeight;
		this.breakGlueLen = breakGlueLen;
		this.drawDistance = drawDistance;
		this.drawSpeed = drawSpeed;
		this.isPause = isPause;
	}

	/**
	 * @return 停胶前延时
	 */
	public int getStopGlueTimePrev() {
		return stopGlueTimePrev;
	}

	/**
	 * 设置停胶前延时
	 * 
	 * @param stopGlueTimePrev
	 *            停胶(后)延时
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
	 * 设置停胶(后)延时
	 * 
	 * @param stopGlueTime
	 *            停胶(后)延时
	 */
	public void setStopGlueTime(int stopGlueTime) {
		this.stopGlueTime = stopGlueTime;
	}

	/**
	 * @return 抬起高度
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
	 * @return 获取断胶长度
	 */
	public int getBreakGlueLen() {
		return breakGlueLen;
	}

	/**
	 * 设置断胶长度
	 * 
	 * @param breakGlueLen
	 *            断胶长度
	 */
	public void setBreakGlueLen(int breakGlueLen) {
		this.breakGlueLen = breakGlueLen;
	}

	/**
	 * @return 拉丝距离
	 */
	public int getDrawDistance() {
		return drawDistance;
	}

	/**
	 * 设置拉丝距离
	 * 
	 * @param drawDistance
	 *            拉丝距离
	 */
	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}

	/**
	 * @return 获取拉丝速度
	 */
	public int getDrawSpeed() {
		return drawSpeed;
	}

	/**
	 * 设置拉丝速度
	 * 
	 * @param drawSpeed
	 *            拉丝速度
	 */
	public void setDrawSpeed(int drawSpeed) {
		this.drawSpeed = drawSpeed;
	}

	/**
	 * @return 是否暂停
	 */
	public boolean isPause() {
		return isPause;
	}

	/**
	 * 设置是否暂停
	 * 
	 * @param isPause
	 *            是否暂停
	 */
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	@Override
	public String toString() {
		return "PointGlueLineEndParam [stopGlueTimePrev=" + stopGlueTimePrev + ", stopGlueTime=" + stopGlueTime
				+ ", upHeight=" + upHeight + ", breakGlueLen=" + breakGlueLen + ", drawDistance=" + drawDistance
				+ ", drawSpeed=" + drawSpeed + ", isPause=" + isPause + ", get_id()=" + get_id() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + breakGlueLen;
		result = prime * result + drawDistance;
		result = prime * result + drawSpeed;
		result = prime * result + (isPause ? 1231 : 1237);
		result = prime * result + stopGlueTime;
		result = prime * result + stopGlueTimePrev;
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
		PointGlueLineEndParam other = (PointGlueLineEndParam) obj;
		if (breakGlueLen != other.breakGlueLen)
			return false;
		if (drawDistance != other.drawDistance)
			return false;
		if (drawSpeed != other.drawSpeed)
			return false;
		if (isPause != other.isPause)
			return false;
		if (stopGlueTime != other.stopGlueTime)
			return false;
		if (stopGlueTimePrev != other.stopGlueTimePrev)
			return false;
		if (upHeight != other.upHeight)
			return false;
		return true;
	}

	public static final Parcelable.Creator<PointGlueLineEndParam> CREATOR = new Creator<PointGlueLineEndParam>() {

		@Override
		public PointGlueLineEndParam[] newArray(int size) {
			return new PointGlueLineEndParam[size];
		}

		@Override
		public PointGlueLineEndParam createFromParcel(Parcel source) {
			PointGlueLineEndParam point = new PointGlueLineEndParam();
			point.stopGlueTimePrev = source.readInt();
			point.stopGlueTime = source.readInt();
			point.upHeight = source.readInt();
			point.breakGlueLen = source.readInt();
			point.drawDistance = source.readInt();
			point.drawSpeed = source.readInt();
			point.isPause = (source.readInt() == 0) ? false : true;
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
		dest.writeInt(stopGlueTimePrev);
		dest.writeInt(stopGlueTime);
		dest.writeInt(upHeight);
		dest.writeInt(breakGlueLen);
		dest.writeInt(drawDistance);
		dest.writeInt(drawSpeed);
		dest.writeInt((boolean) isPause ? 1 : 0);
		dest.writeInt(get_id());
	}

}
