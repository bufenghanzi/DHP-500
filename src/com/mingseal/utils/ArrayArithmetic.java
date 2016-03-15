/**
 * 
 */
package com.mingseal.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mingseal.data.param.ArrayParam;
import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.SMatrix1_4;

import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

/**
 * @author 商炎炳
 *
 */
public class ArrayArithmetic {
	private static String TAG = "ArrayArithmetic";

	
	/**
	 * @title 阵列点或者面
	 * 
	 * @param _para
	 *            阵列参数
	 * @param _pointMgr
	 *            点集管理
	 */
	public static List<Point> arrayPoint(ArrayParam _para, List<Point> _pointMgr) {

		List<Point> points = new ArrayList<Point>();
		// for(Point point:_pointMgr){
		// points.add(point);
		// }
		SMatrix1_4 mx0 = new SMatrix1_4(0, 0, 0, 0);// x方向单位偏移量
		SMatrix1_4 my0 = new SMatrix1_4(0, 0, 0, 0);// y方向单位偏移量
		SMatrix1_4 mxy0 = new SMatrix1_4(0, 0, 0, 0);// xy方向综合单位偏移量

		if (_para.getCol() > 1) {
			mx0 = SMatrix1_4.operator_division(_para.getMx(), _para.getCol() - 1);
		}
		if (_para.getRow() > 1) {
			my0 = SMatrix1_4.operator_division(_para.getMy(), _para.getRow() - 1);
		}
		double dx = 0, dy = 0, dz = 0, du = 0;// 终点偏移量
		if (_para.getMe().getX() != 0 || _para.getMe().getY() != 0 || _para.getMe().getZ() != 0
				|| _para.getMe().getU() != 0) {
			dx = _para.getMe().getX() - _para.getMx().getX() - _para.getMy().getX();
			dy = _para.getMe().getY() - _para.getMx().getY() - _para.getMy().getY();
			dz = _para.getMe().getZ() - _para.getMx().getZ() - _para.getMy().getZ();
			du = _para.getMe().getU() - _para.getMx().getU() - _para.getMy().getU();
		}

		double xDir = _para.getMx().getX();
		double yDir = _para.getMy().getY();
		boolean bSort = _para.isbSort();
		int _start = 0;
		int _end = _pointMgr.size();
		double x, y, z, u;// double
		int xi, yi, zi, ui;// int
		Point point = new Point(PointType.POINT_NULL);
		Point pointCur = new Point(PointType.POINT_NULL);// 判断当前点是不是基准点
		PointParam pointParam = new PointParam();

		if (_para.isbStartDirY()) {
			for (int j = 0; j < _para.getCol(); j++) {
				for (int i = 0; i < _para.getRow(); i++) {

					// if (bSort && i == 0) {
					// if (_para.isbSType() && ((j & 0x1) == 1 ? true : false))
					// {
					// if (xDir >= 0) {
					// if (yDir >= 0) {
					// Collections.sort(points.subList(_start, _end),
					// compareY2X1);
					// } else {
					// Collections.sort(points.subList(_start, _end),
					// compareY1X1);
					// }
					// }else{
					// if(yDir >=0){
					// Collections.sort(points.subList(_start, _end),
					// compareY2X2);
					// }else{
					// Collections.sort(points.subList(_start, _end),
					// compareY1X2);
					// }
					// }
					// } else {
					// if (xDir >= 0) {
					// if (yDir < 0) {
					// Collections.sort(points.subList(_start, _end),
					// compareY2X1);
					// } else {
					// Collections.sort(points.subList(_start, _end),
					// compareY1X1);
					// }
					// }else{
					// if(yDir <0){
					// Collections.sort(points.subList(_start, _end),
					// compareY2X2);
					// }else{
					// Collections.sort(points.subList(_start, _end),
					// compareY1X2);
					// }
					// }
					// }
					// for (Point point1 : points) {
					// Log.d(TAG, "设置：point-->" + point1.toString());
					// }
					// }

					if (i == 0 && j == 0) {
						continue;
					}

					if (_para.isbSType() && (j & 0x1) == 1 ? true : false) {
						mxy0 = SMatrix1_4.operator_plus(SMatrix1_4.operator_multify(my0, _para.getRow() - 1 - i),
								SMatrix1_4.operator_multify(mx0, j));
					} else {
						mxy0 = SMatrix1_4.operator_plus(SMatrix1_4.operator_multify(my0, i),
								SMatrix1_4.operator_multify(mx0, j));
					}

					if (_para.getRow() > 1 && _para.getCol() > 1) {
						if (_para.isbSType() && ((j & 0x1) == 1 ? true : false)) {
							x = mxy0.getX() + dx * ((_para.getRow() - 1 - i) * j)
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							y = mxy0.getY() + dy * ((_para.getRow() - 1 - i) * j)
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							z = mxy0.getZ() + dz * ((_para.getRow() - 1 - i) * j)
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							u = mxy0.getU() + du * ((_para.getRow() - 1 - i) * j)
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							mxy0.setX(x);
							mxy0.setY(y);
							mxy0.setZ(z);
							mxy0.setU(u);
						} else {
							x = mxy0.getX() + dx * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							y = mxy0.getY() + dy * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							z = mxy0.getZ() + dz * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							u = mxy0.getU() + du * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							mxy0.setX(x);
							mxy0.setY(y);
							mxy0.setZ(z);
							mxy0.setU(u);
						}
					}

					for (int k = 0; k < _pointMgr.size(); k++) {
						point = new Point(PointType.POINT_NULL);
						pointCur = _pointMgr.get(k);
						switch (pointCur.getPointParam().getPointType()) {
						case POINT_GLUE_BASE:

							break;

						default:
							xi = (int) (pointCur.getX() + mxy0.getX());
							yi = (int) (pointCur.getY() + mxy0.getY());
							zi = (int) (pointCur.getZ() + mxy0.getZ());
							ui = (int) (pointCur.getU() + mxy0.getU());

							point.setX(RobotParam.INSTANCE.VerifyXPulse(xi));
							point.setY(RobotParam.INSTANCE.VerifyYPulse(yi));
							point.setZ(RobotParam.INSTANCE.VerifyZPulse(zi));
							point.setU(RobotParam.INSTANCE.VerifyUPulse(ui));

							pointParam = new PointParam();
							pointParam.set_id(pointCur.getPointParam().get_id());
							pointParam.setPointType(pointCur.getPointParam().getPointType());
							point.setPointParam(pointParam);
							points.add(point);
							break;
						}

					}

				}
			}

			// if(bSort){
			// if(xDir >=0){
			// if(yDir<0){
			// Collections.sort(points.subList(_start, _end),compareY2X1);
			// }else{
			// Collections.sort(points.subList(_start, _end),compareY1X1);
			// }
			// }else{
			// if(yDir<0){
			// Collections.sort(points.subList(_start, _end),compareY2X2);
			// }else{
			// Collections.sort(points.subList(_start, _end),compareY1X2);
			// }
			// }
			// }
			// Log.d(TAG, points.toString());

		} else {
			for (int i = 0; i < _para.getRow(); i++) {
				for (int j = 0; j < _para.getCol(); j++) {
					if (i == 0 && j == 0) {
						continue;
					}
					if (_para.isbSType() && ((i & 0x1) == 1 ? true : false)) {
						mxy0 = SMatrix1_4.operator_plus(SMatrix1_4.operator_multify(my0, i),
								SMatrix1_4.operator_multify(mx0, _para.getCol() - 1 - j));
					} else {
						mxy0 = SMatrix1_4.operator_plus(SMatrix1_4.operator_multify(my0, i),
								SMatrix1_4.operator_multify(mx0, j));
					}

					if (_para.getRow() > 1 && _para.getCol() > 1) {
						if (_para.isbSType() && ((i & 0x1) == 1 ? true : false)) {
							x = mxy0.getX() + dx * (i * (_para.getCol() - 1 - j))
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							y = mxy0.getY() + dy * (i * (_para.getCol() - 1 - j))
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							z = mxy0.getZ() + dz * (i * (_para.getCol() - 1 - j))
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							u = mxy0.getU() + du * (i * (_para.getCol() - 1 - j))
									/ ((_para.getRow() - 1) * (_para.getCol() - 1));
							mxy0.setX(x);
							mxy0.setY(y);
							mxy0.setZ(z);
							mxy0.setU(u);
						} else {
							x = mxy0.getX() + dx * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							y = mxy0.getY() + dy * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							z = mxy0.getZ() + dz * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							u = mxy0.getU() + du * (i * j) / ((_para.getRow() - 1) * (_para.getCol() - 1));
							mxy0.setX(x);
							mxy0.setY(y);
							mxy0.setZ(z);
							mxy0.setU(u);
						}
					}
					for (int k = 0; k < _pointMgr.size(); k++) {
						point = new Point(PointType.POINT_NULL);
						pointCur = _pointMgr.get(k);
						switch (pointCur.getPointParam().getPointType()) {
						case POINT_GLUE_BASE:

							break;

						default:
							xi = (int) (pointCur.getX() + mxy0.getX());
							yi = (int) (pointCur.getY() + mxy0.getY());
							zi = (int) (pointCur.getZ() + mxy0.getZ());
							ui = (int) (pointCur.getU() + mxy0.getU());

							point.setX(RobotParam.INSTANCE.VerifyXPulse(xi));
							point.setY(RobotParam.INSTANCE.VerifyYPulse(yi));
							point.setZ(RobotParam.INSTANCE.VerifyZPulse(zi));
							point.setU(RobotParam.INSTANCE.VerifyUPulse(ui));

							pointParam = new PointParam();
							pointParam.set_id(pointCur.getPointParam().get_id());
							pointParam.setPointType(pointCur.getPointParam().getPointType());
							point.setPointParam(pointParam);
							points.add(point);
							break;
						}

					}
				}
			}
		}

		return points;

	}

}
