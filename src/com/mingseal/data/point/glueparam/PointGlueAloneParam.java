package com.mingseal.data.point.glueparam;

import java.util.Arrays;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶独立点参数类
 * 
 * @author tiptr
 */
public class PointGlueAloneParam extends PointParam {

	private int dotGlueTime;// 点胶延时
	private int stopGlueTime;// 停胶延时
	private int upHeight;// 抬起高度
	private boolean isOutGlue;// 是否出胶
	private boolean isPause;// 是否暂停
	private boolean[] gluePort;// 点胶口
	
	//待定
	public int nDipDistanceZ = 0;//Z轴方向倾斜距离
	public int nDipDistanceY = 0;//Y轴方向倾斜距离
	public int nDipSpeed = 0;//斜插速度

	/**
	 * 点胶独立点参数私有初始化方法
	 * 
	 * @param dotGlueTime
	 *            点胶延时
	 * @param stopGlueTime
	 *            停胶延时
	 * @param upHeight
	 *            抬起高度
	 * @param isOutGlue
	 *            是否出胶
	 * @param isPause
	 *            是否暂停
	 */
	private void pointGlueAloneInit(int dotGlueTime, int stopGlueTime, int upHeight, boolean isOutGlue,
			boolean isPause) {
		this.dotGlueTime = dotGlueTime;
		this.stopGlueTime = stopGlueTime;
		this.upHeight = upHeight;
		this.isOutGlue = isOutGlue;
		this.isPause = isPause;
	}

	/**
	 * 点胶独立点参数构造方法,默认值为
	 * 
	 * @dotGlueTime 点胶延时 0
	 * @stopGlueTime 停胶延时 0
	 * @upHeight 抬起高度 0
	 * @isOutGlue 是否出胶 是
	 * @isPause 是否暂停 否
	 * @gluePort 10000000000000000000
	 */
	public PointGlueAloneParam() {
		pointGlueAloneInit(0, 0, 0, true, false);
		super.setPointType(PointType.POINT_GLUE_ALONE);
		this.gluePort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		this.gluePort[0] = true;
	}

	/**
	 * 点胶独立点初始化构造方法
	 * 
	 * @param dotGlueTime
	 *            点胶延时
	 * @param stopGlueTime
	 *            停胶延时
	 * @param upHeight
	 *            抬起高度
	 * @param isOutGlue
	 *            是否出胶
	 * @param isPause
	 *            是否暂停
	 * @param gluePort
	 *            点胶口数据
	 */
	public PointGlueAloneParam(int dotGlueTime, int stopGlueTime, int upHeight, boolean isOutGlue, boolean isPause,
			boolean[] gluePort) {
		pointGlueAloneInit(dotGlueTime, stopGlueTime, upHeight, isOutGlue, isPause);
		super.setPointType(PointType.POINT_GLUE_ALONE);
		this.gluePort = gluePort;
	}

	@Override
	public int get_id() {
		return super.get_id();
	}

	@Override
	public void set_id(int _id) {
		super.set_id(_id);
	}

	@Override
	public void setStrParamName(String strParamName) {
		strParamName = "#点胶独立点" + strParamName;
		super.setStrParamName(strParamName);
	}

	/**
	 * @return 获取点胶延时
	 */
	public int getDotGlueTime() {
		return dotGlueTime;
	}

	/**
	 * 设置点胶延时
	 * 
	 * @param dotGlueTime
	 *            点胶延时
	 */
	public void setDotGlueTime(int dotGlueTime) {
		this.dotGlueTime = dotGlueTime;
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
	 * <p>Title: setGluePort
	 * <p>Description: 设置指定位置点胶口数据
	 * @param location 指定的位置
	 * @param value 点胶口数据
	 */
	public void setGluePort(int location, boolean value){
		this.gluePort[location] = value;
	}

	@Override
	public String toString() {
		return "PointGlueAloneParam [dotGlueTime=" + dotGlueTime + ", stopGlueTime=" + stopGlueTime + ", upHeight="
				+ upHeight + ", isOutGlue=" + isOutGlue + ", isPause=" + isPause + ", gluePort="
				+ Arrays.toString(gluePort) + ", get_id()=" + get_id() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dotGlueTime;
		result = prime * result + Arrays.hashCode(gluePort);
		result = prime * result + (isOutGlue ? 1231 : 1237);
		result = prime * result + (isPause ? 1231 : 1237);
		result = prime * result + stopGlueTime;
		result = prime * result + upHeight;
		return result;
	}

	/*
	 * 复写equal方法，用于判断List集合的contain方法，不写的话，contain方法判断的是地址
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointGlueAloneParam other = (PointGlueAloneParam) obj;
		if (dotGlueTime != other.dotGlueTime)
			return false;
		if (!Arrays.equals(gluePort, other.gluePort))
			return false;
		if (isOutGlue != other.isOutGlue)
			return false;
		if (isPause != other.isPause)
			return false;
		if (stopGlueTime != other.stopGlueTime)
			return false;
		if (upHeight != other.upHeight)
			return false;
		return true;
	}
	
	

	public static final Parcelable.Creator<PointGlueAloneParam> CREATOR = new Creator<PointGlueAloneParam>() {

		@Override
		public PointGlueAloneParam createFromParcel(Parcel source) {

			PointGlueAloneParam point = new PointGlueAloneParam();
			point.dotGlueTime = source.readInt();
			point.stopGlueTime = source.readInt();
			point.upHeight = source.readInt();
			point.isOutGlue = (source.readInt() == 0) ? false : true;
			point.isPause = (source.readInt() == 0) ? false : true;
			boolean[] val = null;
			val = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
			source.readBooleanArray(val);
			point.gluePort = val;
			point.set_id(source.readInt());
			return point;
		}

		@Override
		public PointGlueAloneParam[] newArray(int size) {
			return new PointGlueAloneParam[size];
		}
	};

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(dotGlueTime);
		dest.writeInt(stopGlueTime);
		dest.writeInt(upHeight);
		dest.writeInt((boolean) isOutGlue ? 1 : 0);
		dest.writeInt((boolean) isPause ? 1 : 0);
		dest.writeBooleanArray(gluePort);
		dest.writeInt(get_id());
	}

}