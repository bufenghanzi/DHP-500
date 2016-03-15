package com.mingseal.data.point.glueparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点胶清胶点
 * 
 * @author lyq
 *
 */
public class PointGlueClearParam extends PointParam {

	private int clearGlueTime; // 清胶延时(单位:毫秒ms)

	/**
	 * 点胶清胶点参数构造方法,默认值为
	 * 
	 * @clearGlueTime 清胶延时 0
	 */
	public PointGlueClearParam() {
		pointGlueClearInit(0);
		super.setPointType(PointType.POINT_GLUE_CLEAR);
	}

	/**
	 * 点胶清胶点初始化构造方法
	 * 
	 * @param clearGlueTime
	 *            清胶延时
	 */
	public PointGlueClearParam(int clearGlueTime) {
		pointGlueClearInit(clearGlueTime);
		super.setPointType(PointType.POINT_GLUE_CLEAR);
	}

	/**
	 * @return 获取清胶延时
	 */
	public int getClearGlueTime() {
		return clearGlueTime;
	}

	/**
	 * 设置清胶延时
	 * 
	 * @param clearGlueTime
	 *            清胶延时
	 */
	public void setClearGlueTime(int clearGlueTime) {
		this.clearGlueTime = clearGlueTime;
	}

	/**
	 * 点胶清胶点参数私有初始化方法
	 * 
	 * @param clearGlueTime
	 *            清胶延时
	 */
	private void pointGlueClearInit(int clearGlueTime) {
		this.clearGlueTime = clearGlueTime;
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
		PointGlueClearParam other = (PointGlueClearParam) obj;
		if (clearGlueTime != other.clearGlueTime)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PointGlueClearParam [clearGlueTime=" + clearGlueTime + ", get_id()=" + get_id() + "]";
	}

	public static final Parcelable.Creator<PointGlueClearParam> CREATOR = new Creator<PointGlueClearParam>() {

		@Override
		public PointGlueClearParam[] newArray(int size) {
			return new PointGlueClearParam[size];
		}

		@Override
		public PointGlueClearParam createFromParcel(Parcel source) {
			PointGlueClearParam point = new PointGlueClearParam();

			point.set_id(source.readInt());
			point.clearGlueTime = source.readInt();

			return point;
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(get_id());
		dest.writeInt(clearGlueTime);
	}

}
