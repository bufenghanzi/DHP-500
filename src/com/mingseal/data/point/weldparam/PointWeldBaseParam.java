package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡基准点参数类
 * @author lyq
 */
public class PointWeldBaseParam extends PointParam {

	/**
	 * 焊锡基准点参数构造方法
	 */
	public PointWeldBaseParam(){
		super.setPointType(PointType.POINT_WELD_BASE);
	}
}
