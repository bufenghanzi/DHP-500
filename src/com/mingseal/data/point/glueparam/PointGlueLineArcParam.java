package com.mingseal.data.point.glueparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;

/**
 * 点胶圆弧点参数
 * @author lyq
 *
 */
public class PointGlueLineArcParam extends PointParam {
	
	/**
	 * 点胶线圆弧点参数构造方法
	 */
	public PointGlueLineArcParam(){
		super.setPointType(PointType.POINT_GLUE_LINE_ARC);
	}

	@Override
	public int describeContents() {
		return super.describeContents();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}
	
	
}
