package com.mingseal.data.point.glueparam;

import java.util.Arrays;

import com.mingseal.data.point.IOPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶输出IO参数类
 * 
 * @author lyq
 */
public class PointGlueOutputIOParam extends PointParam {
	private int goTimePrev; // 动作前延时(单位:毫秒ms)
	private int goTimeNext; // 动作后延时(单位:毫秒ms)
	private boolean[] inputPort; // 输入口

	/**
	 * 点胶输出IO初始化构造方法,默认值为:
	 * 
	 * @goTimePrev 动作前延时 0
	 * @goTimeNext 动作后延时 0
	 * @inputPort 输入口数据 000000000000
	 */
	public PointGlueOutputIOParam() {
		pointGlueOutputIOParamInit(0, 0);
		super.setPointType(PointType.POINT_GLUE_OUTPUT);
		this.inputPort = new boolean[IOPort.IO_NO_ALL.ordinal()];
	}

	/**
	 * 点胶输出IO初始化构造方法
	 * 
	 * @param goTimePrev
	 *            动作前延时
	 * @param goTimeNext
	 *            动作后延时
	 * @param inputPort
	 *            输入口数据
	 */
	public PointGlueOutputIOParam(int goTimePrev, int goTimeNext, boolean[] inputPort) {
		pointGlueOutputIOParamInit(goTimePrev, goTimeNext);
		super.setPointType(PointType.POINT_GLUE_OUTPUT);
		this.inputPort = inputPort;
	}

	/**
	 * 点胶输出IO参数私有初始化方法
	 * 
	 * @param goTimePrev
	 *            动作前延时
	 * @param goTimeNext
	 *            动作后延时
	 */
	private void pointGlueOutputIOParamInit(int goTimePrev, int goTimeNext) {
		this.goTimePrev = goTimePrev;
		this.goTimeNext = goTimeNext;
	}

	/**
	 * @return 获取动作前延时
	 */
	public int getGoTimePrev() {
		return goTimePrev;
	}

	/**
	 * 设置动作前延时
	 * 
	 * @param goTimePrev
	 *            动作前延时
	 */
	public void setGoTimePrev(int goTimePrev) {
		this.goTimePrev = goTimePrev;
	}

	/**
	 * @return 获取动作后延时
	 */
	public int getGoTimeNext() {
		return goTimeNext;
	}

	/**
	 * 设置动作后延时
	 * 
	 * @param goTimeNext
	 *            动作后延时
	 */
	public void setGoTimeNext(int goTimeNext) {
		this.goTimeNext = goTimeNext;
	}

	/**
	 * @return 获取输出口数据
	 */
	public boolean[] getInputPort() {
		return inputPort;
	}

	/**
	 * 设置输出口数据
	 * 
	 * @param inputPort
	 *            输出口数据
	 */
	public void setInputPort(boolean[] inputPort) {
		this.inputPort = inputPort;
	}

	@Override
	public String toString() {
		return "PointGlueOutputIOParam [goTimePrev=" + goTimePrev + ", goTimeNext=" + goTimeNext + ", inputPort="
				+ Arrays.toString(inputPort) + ", get_id()=" + get_id() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + goTimeNext;
		result = prime * result + goTimePrev;
		result = prime * result + Arrays.hashCode(inputPort);
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
		PointGlueOutputIOParam other = (PointGlueOutputIOParam) obj;
		if (goTimeNext != other.goTimeNext)
			return false;
		if (goTimePrev != other.goTimePrev)
			return false;
		if (!Arrays.equals(inputPort, other.inputPort))
			return false;
		return true;
	}

	public static final Parcelable.Creator<PointGlueOutputIOParam> CREATOR = new Creator<PointGlueOutputIOParam>() {

		@Override
		public PointGlueOutputIOParam[] newArray(int size) {
			return new PointGlueOutputIOParam[size];
		}

		@Override
		public PointGlueOutputIOParam createFromParcel(Parcel source) {
			PointGlueOutputIOParam param = new PointGlueOutputIOParam();
			param.goTimePrev = source.readInt();
			param.goTimeNext = source.readInt();
			boolean[] val = null;
			val = new boolean[IOPort.IO_NO_ALL.ordinal()];
			source.readBooleanArray(val);
			param.inputPort = val;
			param.set_id(source.readInt());

			return param;
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(goTimePrev);
		dest.writeInt(goTimeNext);
		dest.writeBooleanArray(inputPort);
		dest.writeInt(get_id());
	}

}
