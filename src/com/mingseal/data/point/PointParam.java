package com.mingseal.data.point;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 点参数基类
 * 
 * @author lyq
 */
public class PointParam implements Parcelable {

	private String strParamName;// 参数名(备用)
	private int _id;// 参数序列

	private PointType pointType;// 点类型

	public PointParam() {
		super();
	}

	private PointParam(Parcel in) {
		pointType = PointType.values()[in.readInt()];
		_id = in.readInt();
	}

	/**
	 * @return 获取参数序列
	 */
	public int get_id() {
		return _id;
	}

	/**
	 * 设置参数序列
	 * 
	 * @param _id
	 */
	public void set_id(int _id) {
		this._id = _id;
	}

	/**
	 * @return 获取参数名
	 */
	public String getStrParamName() {
		return strParamName;
	}

	/**
	 * 设置参数名
	 * 
	 * @param strParamName
	 *            参数名
	 */
	public void setStrParamName(String strParamName) {
		this.strParamName = strParamName;
	}

	/**
	 * @return 获取点类型
	 */
	public PointType getPointType() {
		return pointType;
	}

	/**
	 * 设置点类型
	 * 
	 * @param pointType
	 *            点类型
	 */
	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}

	@Override
	public String toString() {
		return "PointParam [_id=" + _id + ", pointType=" + pointType + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _id;
		result = prime * result + ((pointType == null) ? 0 : pointType.hashCode());
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
		PointParam other = (PointParam) obj;
		if (_id != other._id)
			return false;
		if (pointType != other.pointType)
			return false;
		return true;
	}



	/**
	 * 
	 * @author 商炎炳
	 */
	public static final Parcelable.Creator<PointParam> CREATOR = new Creator<PointParam>() {

		@Override
		public PointParam createFromParcel(Parcel source) {
			return new PointParam(source);
		}

		@Override
		public PointParam[] newArray(int size) {
			return new PointParam[size];
		}

	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pointType.ordinal());
		dest.writeInt(_id);
	}

}
