package com.mingseal.data.point.glueparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.os.Parcel;

/**
 * 点胶清胶IO
 * @author lyq
 *
 */
public class PointGlueClearIOParam extends PointParam{
	
	/**
	 * 点胶清胶点初始化构造方法
	 */
	public PointGlueClearIOParam(){
		super.setPointType(PointType.POINT_GLUE_CLEARIO);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}
	
	
}
