package com.mingseal.data.point.glueparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶面结束点参数类
 * 
 * @author lyq
 *
 */
public class PointGlueFaceEndParam extends PointParam {

	private int stopGlueTime;// 停胶延时
	private int upHeight;// 抬起高度
	private int lineNum;// 直线条数
	// private boolean startDir;// 起始方向 true:x方向 false:y方向
	private boolean isPause;// 是否暂停

	/**
	 * 点胶面结束点私有初始化方法
	 * 
	 * @param stopGlueTime
	 *            停胶延时
	 * @param upHeight
	 *            抬起高度
	 * @param lineNum
	 *            直线条数
	 * @param isPause
	 *            是否暂停
	 */
	private void pointGlueFaceEndInit(int stopGlueTime, int upHeight, int lineNum, boolean isPause) {
		this.stopGlueTime = stopGlueTime;
		this.upHeight = upHeight;
		this.lineNum = lineNum;
		this.isPause = isPause;
	}

	/**
	 * 点胶面结束点构造方法,默认值为
	 * 
	 * @stopGlueTime 停胶延时 0
	 * @upHeight 抬起高度 0
	 * @lineNum 直线条数 5
	 * @isPause 是否暂停 是
	 */
	public PointGlueFaceEndParam() {
		pointGlueFaceEndInit(0, 0, 5, false);
		super.setPointType(PointType.POINT_GLUE_FACE_END);
	}

	/**
	 * 点胶面结束点初始化构造方法
	 * 
	 * @param stopGlueTime
	 *            停胶延时
	 * @param upHeight
	 *            抬起高度
	 * @param lineNum
	 *            直线条数
	 * @param isPause
	 *            是否暂停
	 */
	public PointGlueFaceEndParam(int stopGlueTime, int upHeight, int lineNum, boolean isPause) {
		pointGlueFaceEndInit(stopGlueTime, upHeight, lineNum, isPause);
		super.setPointType(PointType.POINT_GLUE_FACE_END);
	}

	@Override
	public int get_id() {
		return super.get_id();
	}

	/**
	 * @return 获取停胶延时
	 */
	public int getStopGlueTime() {
		return stopGlueTime;
	}

	/**
	 * 设置停胶延时
	 * 
	 * @param stopGlueTime
	 *            停胶延时
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
	 * @return 获取直线条数
	 */
	public int getLineNum() {
		return lineNum;
	}

	/**
	 * 设置直线条数
	 * 
	 * @param lineNum
	 *            直线条数
	 */
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	/**
	 * @return 获取是否暂停
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isPause ? 1231 : 1237);
		result = prime * result + lineNum;
		result = prime * result + stopGlueTime;
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
		PointGlueFaceEndParam other = (PointGlueFaceEndParam) obj;
		if (isPause != other.isPause)
			return false;
		if (lineNum != other.lineNum)
			return false;
		if (stopGlueTime != other.stopGlueTime)
			return false;
		if (upHeight != other.upHeight)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PointGlueFaceEndParam [stopGlueTime=" + stopGlueTime + ", upHeight=" + upHeight + ", lineNum=" + lineNum
				+ ", isPause=" + isPause + ", get_id()=" + get_id() + "]";
	}

	/**
	 * @author 商炎炳
	 */
	public static final Parcelable.Creator<PointGlueFaceEndParam> CREATOR = new Creator<PointGlueFaceEndParam>() {

		@Override
		public PointGlueFaceEndParam[] newArray(int size) {
			return new PointGlueFaceEndParam[size];
		}

		@Override
		public PointGlueFaceEndParam createFromParcel(Parcel source) {
			PointGlueFaceEndParam point = new PointGlueFaceEndParam();
			point.stopGlueTime = source.readInt();
			point.upHeight = source.readInt();
			point.lineNum = source.readInt();
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
		dest.writeInt(stopGlueTime);
		dest.writeInt(upHeight);
		dest.writeInt(lineNum);
		dest.writeInt((boolean) isPause ? 1 : 0);
		dest.writeInt(get_id());
	}

}
