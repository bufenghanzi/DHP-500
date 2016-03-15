/**
 * @Title PointCopyTools.java
 * @Package com.mingseal.utils
 * @Description 复制Point点类
 * @author 商炎炳
 * @date 2016年1月22日 上午8:12:29
 * @version V1.0
 */
package com.mingseal.utils;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;

/**
 * @ClassName PointCopyTools
 * @Description 复制Point点类
 * @author 商炎炳
 * @date 2016年1月22日 上午8:12:29
 *
 */
public class PointCopyTools {
	private static Point pointCur;
	/**
	 * @Fields pointListsCur: 处理之后的List
	 */
	private static List<Point> pointListsCur;

	/**
	 * 复制List类到另一个List,是将里面的数据复制过去,不复制地址
	 * 
	 * @Title processCopyPoints
	 * @Description 复制List类到另一个List,是将里面的数据复制过去,不复制地址
	 * @param pointListsFirst
	 *            要复制的List类
	 * @return 和传进来的List一样的数据,但地址不相同
	 */
	public static List<Point> processCopyPoints(List<Point> pointListsFirst) {
		pointListsCur = new ArrayList<>();
		for (Point point : pointListsFirst) {
			pointCur = new Point(PointType.POINT_NULL);

			pointCur.setId(point.getId());
			pointCur.setX(point.getX());
			pointCur.setY(point.getY());
			pointCur.setZ(point.getZ());
			pointCur.setU(point.getU());
			pointCur.setPointParam(point.getPointParam());

			pointListsCur.add(pointCur);
		}
		return pointListsCur;

	}

	/**
	 * 比较两个List类是否相等
	 * 
	 * @Title comparePoints
	 * @Description 比较两个List是否相等
	 * @param pFirst
	 *            第一个List<Point>类
	 * @param pCur
	 *            第二个List<Point>类
	 * @return true表示相等,false表示不想等
	 */
	public static boolean comparePoints(List<Point> pFirst, List<Point> pCur) {
		if (pFirst.size() != pCur.size()) {
			return false;
		}

		for (int i = 0; i < pFirst.size(); i++) {
			if (!pFirst.get(i).equals(pCur.get(i))) {
				return false;
			}
		}
		return true;
	}
}
