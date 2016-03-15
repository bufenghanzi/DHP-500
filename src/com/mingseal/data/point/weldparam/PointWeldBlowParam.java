package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡吹锡点参数类
 * @author lyq
 */
public class PointWeldBlowParam extends PointParam {
	
	private int blowSnTime;//吹锡时间(单位:毫秒ms)
	
	/**
	 * @return 获取吹锡时间
	 */
	public int getBlowSnTime() {
		return blowSnTime;
	}

	/**
	 * 设置吹锡时间
	 * @param blowSnTime 吹锡时间
	 */
	public void setBlowSnTime(int blowSnTime) {
		this.blowSnTime = blowSnTime;
	}

	/**
	 * 焊锡吹锡点参数构造方法,默认值:
	 * @blowSnTime 吹锡时间 0
	 */
	public PointWeldBlowParam(){
		this.blowSnTime = 0;
		super.setPointType(PointType.POINT_WELD_BLOW);
	}
}
