package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡圆弧点参数类
 * @author lyq
 */
public class PointWeldLineArcParam extends PointParam {
	
	/**
	 * 焊锡圆弧点构造函数
	 */
	public PointWeldLineArcParam(){
		super.setPointType(PointType.POINT_WELD_LINE_ARC);
	}
}
