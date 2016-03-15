package com.mingseal.data.point.glueparam;

import java.util.Arrays;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶面起点参数类
 * 
 * @author lyq
 */
public class PointGlueFaceStartParam extends PointParam {

	private int outGlueTimePrev;// 出胶前延时
	private int outGlueTime;// 出胶(后)延时
	private int moveSpeed;// 轨迹速度
	private boolean isOutGlue;// 是否出胶
	private boolean[] gluePort;// 点胶口

	private int stopGlueTime;// 停胶延时
	// private int upHeight;// 抬起高度
	private boolean startDir;// 起始方向 true:x方向 false:y方向

	/**
	 * 点胶面起点参数私有初始化方法
	 * 
	 * @param moveSpeed
	 *            轨迹速度
	 * @param outGlueTimePrev
	 *            出胶前延时
	 * @param outGlueTime
	 *            出胶(后)延时
	 * @param isOutGlue
	 *            是否出胶
	 * @param stopGlueTime
	 *            停胶延时
	 * @param lineNum
	 *            直线条数
	 * @param startDir
	 *            起始方向 true:x方向 false:y方向
	 */
	private void pointGlueFaceStartInit(int moveSpeed, int outGlueTimePrev, int outGlueTime, boolean isOutGlue,
			int stopGlueTime, int lineNum, boolean startDir) {
		this.moveSpeed = moveSpeed;
		this.outGlueTimePrev = outGlueTimePrev;
		this.outGlueTime = outGlueTime;
		this.isOutGlue = isOutGlue;

		this.stopGlueTime = stopGlueTime;
		this.startDir = startDir;
	}

	/**
	 * 点胶面起点参数构造方法,默认值为
	 * 
	 * @moveSpeed 轨迹速度 1
	 * @outGlueTimePrev 出胶前延时 0
	 * @outGlueTime 出胶(后)延时 0
	 * @isOutGlue 是否出胶 true
	 * @stopGlueTime 停胶延时 0
	 * @lineNum 直线条数 5
	 * @startDir 起始方向 true:x方向
	 */
	public PointGlueFaceStartParam() {
		pointGlueFaceStartInit(1, 0, 0, true, 0, 5, true);
		super.setPointType(PointType.POINT_GLUE_FACE_START);
		this.gluePort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		this.gluePort[0] = true;
	}

	/**
	 * 点胶面起点初始化构造方法
	 * 
	 * @param moveSpeed
	 *            轨迹速度
	 * @param outGlueTimePrev
	 *            出胶前延时
	 * @param outGlueTime
	 *            出胶(后)延时
	 * @param isOutGlue
	 *            是否出胶
	 * @param stopGlueTime
	 *            停胶延时
	 * @param lineNum
	 *            直线条数
	 * @param startDir
	 *            起始方向 true:x方向 false:y方向
	 * @param gluePort
	 *            点胶口数据
	 */
	public PointGlueFaceStartParam(int moveSpeed, int outGlueTimePrev, int outGlueTime, boolean isOutGlue,
			int stopGlueTime, int lineNum, boolean startDir, boolean[] gluePort) {
		pointGlueFaceStartInit(moveSpeed, outGlueTimePrev, outGlueTime, isOutGlue, stopGlueTime, lineNum, startDir);
		super.setPointType(PointType.POINT_GLUE_FACE_START);
		this.gluePort = gluePort;
	}

	@Override
	public int get_id() {
		return super.get_id();
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
	 * @return 获取起始方向 true:x方向 false:y方向
	 */
	public boolean isStartDir() {
		return startDir;
	}

	/**
	 * 设置起始方向
	 * 
	 * @param startDir
	 *            起始方向 true:x方向 false:y方向
	 */
	public void setStartDir(boolean startDir) {
		this.startDir = startDir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(gluePort);
		result = prime * result + (isOutGlue ? 1231 : 1237);
		result = prime * result + moveSpeed;
		result = prime * result + outGlueTime;
		result = prime * result + outGlueTimePrev;
		result = prime * result + (startDir ? 1231 : 1237);
		result = prime * result + stopGlueTime;
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
		PointGlueFaceStartParam other = (PointGlueFaceStartParam) obj;
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
		if (startDir != other.startDir)
			return false;
		if (stopGlueTime != other.stopGlueTime)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PointGlueFaceStartParam [outGlueTimePrev=" + outGlueTimePrev + ", outGlueTime=" + outGlueTime
				+ ", moveSpeed=" + moveSpeed + ", isOutGlue=" + isOutGlue + ", gluePort=" + Arrays.toString(gluePort)
				+ ", stopGlueTime=" + stopGlueTime + ", startDir=" + startDir + ", get_id()=" + get_id() + "]";
	}

	/**
	 * @author 商炎炳
	 */
	public static final Parcelable.Creator<PointGlueFaceStartParam> CREATOR = new Creator<PointGlueFaceStartParam>() {

		@Override
		public PointGlueFaceStartParam createFromParcel(Parcel source) {
			PointGlueFaceStartParam point = new PointGlueFaceStartParam();
			point.outGlueTimePrev = source.readInt();
			point.outGlueTime = source.readInt();
			point.moveSpeed = source.readInt();
			point.stopGlueTime = source.readInt();
			point.isOutGlue = (source.readInt() == 0) ? false : true;
			point.startDir = (source.readInt() == 0) ? false : true;
			boolean[] val = null;
			val = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
			source.readBooleanArray(val);
			point.gluePort = val;
			point.set_id(source.readInt());

			return point;
		}

		@Override
		public PointGlueFaceStartParam[] newArray(int size) {
			return new PointGlueFaceStartParam[size];
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
		dest.writeInt(moveSpeed);
		dest.writeInt(stopGlueTime);
		dest.writeInt((boolean) isOutGlue ? 1 : 0);
		dest.writeInt((boolean) startDir ? 1 : 0);
		dest.writeBooleanArray(gluePort);
		dest.writeInt(get_id());
	}

}
