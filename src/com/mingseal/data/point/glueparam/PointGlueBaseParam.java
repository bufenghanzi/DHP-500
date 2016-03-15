package com.mingseal.data.point.glueparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 点胶基准点
 * @author lyq
 */
public class PointGlueBaseParam extends PointParam{
	

	/**
	 * 点胶基准点参数构造方法
	 */
	public PointGlueBaseParam(){
		super.setPointType(PointType.POINT_GLUE_BASE);
	}
	
//	public static final Parcelable.Creator<PointGlueBaseParam> CREATOR = new Creator<PointGlueBaseParam>() {
//		
//		@Override
//		public PointGlueBaseParam[] newArray(int size) {
//			return new PointGlueBaseParam[size];
//		}
//		
//		@Override
//		public PointGlueBaseParam createFromParcel(Parcel source) {
//			PointGlueBaseParam point = new PointGlueBaseParam();
//			return point;
//		}
//	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}
}
