/**
 * 
 */
package com.mingseal.utils;

import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.Rect_ltrb;
import com.mingseal.data.point.SMatrix1_4;
import com.mingseal.data.point.SMatrix_4_4;

/**
 * @author 商炎炳
 *
 */
public class CommonArithmetic {

	/**
	 * 取得_num的绝对值
	 * 
	 * @param _num
	 * @return _num的绝对值
	 */
	private static double getAbsoluteValue(double _num) {
		if (_num < 0) {
			_num = -_num;
		}
		return _num;
	}

	/**
	 * 判断点与线段的关系
	 * 
	 * @param _p1
	 *            线段起点
	 * @param _p2
	 *            线段终点
	 * @param _p
	 *            点
	 * @return 0：点在线段所在直线上，>0：点在线段左侧，<0：点在线段右侧
	 */
	private double isPtInLine(Point _p1, Point _p2, Point _p) {

		SMatrix1_4 _m1 = new SMatrix1_4(_p1);
		SMatrix1_4 _m2 = new SMatrix1_4(_p2);
		SMatrix1_4 _m = new SMatrix1_4(_p);
		return (double) (_m2.getX() - _m1.getX()) * (_m.getY() - _m1.getY())
				- (double) (_m2.getY() - _m1.getY()) * (_m.getX() - _m1.getX());
	}

	/** 
	* @Title: getTurnV 
	* @Description: 拐点矢量乘积
	* @param _pt 点集信息
	* @param _idx 拐点索引
	* @return 矢量乘积
	*/
	public static double getTurnV(Point[] _pt, int _idx) {
		SMatrix1_4 n1, n2;
		SMatrix1_4 m1 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx - 1].getX()),
				RobotParam.INSTANCE.YPulse2Journey(_pt[_idx - 1].getY()),
				RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx - 1].getZ()));
		SMatrix1_4 m2 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx].getX()),
				RobotParam.INSTANCE.YPulse2Journey(_pt[_idx].getY()),
				RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx].getZ()));
		SMatrix1_4 m3 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx + 1].getX()),
				RobotParam.INSTANCE.YPulse2Journey(_pt[_idx + 1].getY()),
				RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx + 1].getZ()));
		PointType type = _pt[_idx - 1].getPointParam().getPointType();
		if (type == PointType.POINT_GLUE_LINE_ARC || type == PointType.POINT_WELD_LINE_ARC) {
			SMatrix1_4 m11 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx - 2].getX()),
					RobotParam.INSTANCE.YPulse2Journey(_pt[_idx - 2].getY()),
					RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx - 2].getZ()));
			SMatrix1_4 m22 = m1;
			SMatrix1_4 m33 = m2;
			n1 = getArcV(m11, m22, m33, false);
		} else {
			n1 = SMatrix1_4.operator_minus(m2, m1);
			SMatrix1_4.operator_normalize3(n1);
		}
		type = _pt[_idx + 1].getPointParam().getPointType();
		if (type == PointType.POINT_GLUE_LINE_ARC || type == PointType.POINT_WELD_LINE_ARC) {
			SMatrix1_4 m11 = m2;
			SMatrix1_4 m22 = m3;
			SMatrix1_4 m33 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx + 2].getX()),
					RobotParam.INSTANCE.YPulse2Journey(_pt[_idx + 2].getY()),
					RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx + 2].getZ()));
			n2 = getArcV(m11, m22, m33, true);
		} else {
			n2 = SMatrix1_4.operator_minus(m3, m2);
			SMatrix1_4.operator_normalize3(n2);
		}
		SMatrix1_4 n12 = SMatrix1_4.operator_minus(n1, n2);
		double number = SMatrix1_4.operator_dot3(n12, n12);
		return number;
	}

	/**
	 * <p>
	 * Title: GetArcV
	 * <p>
	 * Description: 计算圆弧端点矢量
	 * 
	 * @param _m1
	 *            圆弧起始点
	 * @param _m2
	 *            圆弧中间点
	 * @param _m3
	 *            圆弧结束点
	 * @param _bStart
	 *            是否起始点矢量, true:起始点矢量,false:结束点矢量
	 * @return 圆弧端点矢量
	 */
	public static SMatrix1_4 getArcV(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, boolean _bStart) {
		// 矢量线 _m1->_m2, _m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1, _m2, _m3三点平面法向量 n(nx, ny, nz)
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		// 目标矢量方向
		SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
		// 计算旋转矩阵
		SMatrix_4_4 mr = getRotateMatrix(n, nz);
		_m1 = SMatrix1_4.operator_multify(_m1, mr);
		_m2 = SMatrix1_4.operator_multify(_m2, mr);
		_m3 = SMatrix1_4.operator_multify(_m3, mr);

		SMatrix1_4 n0 = new SMatrix1_4();
		// 求圆心坐标
		SMatrix1_4 c1 = getCenterPt(_m1, _m2, _m3);
		if (_bStart) {
			SMatrix1_4 m1_c1 = SMatrix1_4.operator_minus(c1, _m1);
			n0 = SMatrix1_4.operator_cross3(m1_c1, nz);
		} else {
			SMatrix1_4 m3_c1 = SMatrix1_4.operator_minus(c1, _m3);
			n0 = SMatrix1_4.operator_cross3(m3_c1, nz);
		}
		mr = getRotateMatrix(nz, n);
		n0 = SMatrix1_4.operator_multify(n0, mr);
		SMatrix1_4.operator_normalize3(n0);

		return n0;
	}

	/**
	 * 计算圆弧长度
	 * 
	 * @param _m1
	 *            圆弧起点
	 * @param _m2
	 *            圆弧中间点
	 * @param _m3
	 *            圆弧终点
	 * @return 圆弧长度
	 */
	public static double getArcLength(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3) {
		// 求圆心
		SMatrix1_4 m0 = getCenterPt(_m1, _m2, _m3);
		// 求半径
		SMatrix1_4 m01 = SMatrix1_4.operator_minus(_m1, m0);
		double r = SMatrix1_4.operator_mod3(m01);
		// 求弦长
		SMatrix1_4 m13 = SMatrix1_4.operator_minus(_m1, _m3);
		double dis = SMatrix1_4.operator_mod3(m13);
		// 余弦定理求圆心角
		float alpha = (float) Math.acos((2 * r * r - dis * dis) / (2 * r * r));
		// 判断优劣弧
		float angle = getAngle(_m1, _m2, _m3);
		if (angle < 90) {
			alpha = (float) (Math.PI + Math.PI - alpha);
		}
		return alpha * r;
	}

	/**
	 * 计算绕任意轴旋转的旋转矩阵
	 * 
	 * @param _m1
	 *            原矢量
	 * @param _m2
	 *            目标矢量
	 * @return 旋转矩阵
	 */
	private static SMatrix_4_4 getRotateMatrix(SMatrix1_4 _m1, SMatrix1_4 _m2) {

		// 旋转轴矢量（原矢量×目标矢量）
		SMatrix1_4 m = SMatrix1_4.operator_cross3(_m1, _m2);
		m = SMatrix1_4.operator_normalize3(m);
		// 计算（原矢量）旋转至（目标矢量）的旋转角度
		double _m1_2 = SMatrix1_4.operator_dot3(_m1, _m2);
		double _m1_0 = SMatrix1_4.operator_mod3(_m1);
		double _m2_0 = SMatrix1_4.operator_mod3(_m2);
		double angle = Math.acos(_m1_2 / (_m1_0 * _m2_0));
//		double angle = Math.acos(_m1_2 / (_m1_0 * _m2_0))*180/Math.PI;
		

		if (m.getX() == 0 && m.getY() == 0 && m.getZ() == 0) {// 平行矢量
			if (angle == 0) {
				// 同向
			} else {
				if (_m2.getX() != 0) {
					m = new SMatrix1_4(-(_m2.getY() + _m2.getZ()) / _m2.getX(), 1, 1);
				} else if (_m2.getY() != 0) {
					m = new SMatrix1_4(1, -(_m2.getX() + _m2.getZ()) / _m2.getY(), 1);
				} else if (_m2.getZ() != 0) {
					m = new SMatrix1_4(1, 1, -(_m2.getX() + _m2.getY()) / _m2.getZ());
				}
				m = SMatrix1_4.operator_normalize3(m);
			}
		}
		double fCos = Math.cos(angle);
		double fSin = Math.sin(angle);
//		double fCos = Math.cos(angle*Math.PI/180);
//		double fSin = Math.sin(angle*Math.PI/180);
		double a11 = fCos + (1 - fCos) * (m.getX() * m.getX());
		double a12 = (1 - fCos) * m.getX() * m.getY() + m.getZ() * fSin;
		double a13 = (1 - fCos) * m.getX() * m.getZ() - m.getY() * fSin;
		double a14 = 0;
		double a21 = (1 - fCos) * m.getX() * m.getY() - m.getZ() * fSin;
		double a22 = fCos + (1 - fCos) * (m.getY() * m.getY());
		double a23 = (1 - fCos) * m.getY() * m.getZ() + m.getX() * fSin;
		double a24 = 0;
		double a31 = (1 - fCos) * m.getX() * m.getZ() + m.getY() * fSin;
		double a32 = (1 - fCos) * m.getY() * m.getZ() - m.getX() * fSin;
		double a33 = fCos + (1 - fCos) * (m.getZ() * m.getZ());
		double a34 = 0;
		double a41 = 0;
		double a42 = 0;
		double a43 = 0;
		double a44 = 1;
		
		// 右手坐标系下任意旋转
		SMatrix_4_4 mr = new SMatrix_4_4(a11,a12,a13,a14,
										 a21,a22,a23,a24,
										 a31,a32,a33,a34,
										 a41,a42,a43,a44);

		return mr;

	}

	/**
	 * 坐标插值计算
	 * 
	 * @param _m1
	 *            直线起始点
	 * @param _m2
	 *            直线结束点
	 * @param _len
	 *            距离起始点长度
	 * @return 直线插入点
	 */
	public static SMatrix1_4 insertLinePoint(SMatrix1_4 _m1, SMatrix1_4 _m2, float _len) {
		// 矢量线 _m1->_m2
		SMatrix1_4 m12 = SMatrix1_4.operator_minus(_m2, _m1);
		float scale = (float) (_len / SMatrix1_4.operator_mod3(m12));
		SMatrix1_4 mResult = new SMatrix1_4(_m1.getX() + m12.getX() * scale, _m1.getY() + m12.getY() * scale,
				_m1.getZ() + m12.getZ() * scale, _m1.getU());

		return mResult;
	}

	/**
	 * 坐标插值计算
	 * 
	 * @param _m1
	 *            圆弧起始点
	 * @param _m2
	 *            圆弧中间点
	 * @param _m3
	 *            圆弧结束点
	 * @param _len
	 *            距离圆弧起始点弧长
	 * @return 圆弧插入点
	 */
	public static SMatrix1_4 insertArcPoint(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, float _len) {
		SMatrix1_4 mResult = new SMatrix1_4();
		// 矢量线 _m1->_m2，_m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		if (n.getX() != 0 || n.getY() != 0 || n.getZ() != 0) {// 三点不共线
			// 目标矢量方向
			SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
			// 计算旋转矩阵
			SMatrix_4_4 mr = getRotateMatrix(n, nz);
			_m1 = SMatrix1_4.operator_multify(_m1, mr);
			_m2 = SMatrix1_4.operator_multify(_m2, mr);
			_m3 = SMatrix1_4.operator_multify(_m3, mr);

			// 经过旋转后转换成二维坐标系中计算
			// 求圆心
			SMatrix1_4 m0 = getCenterPt(_m1, _m2, _m3);
			// 求半径
			SMatrix1_4 m01 = SMatrix1_4.operator_minus(_m1, m0);
			double r = SMatrix1_4.operator_mod3(m01);
			// 求方向角弧度
			double arf1 = getArc(m0, _m1);
			// 弧长转弧度
			double len = _len / r;
			if ((SMatrix1_4.operator_cross2(SMatrix1_4.operator_minus(_m2, _m1),
					SMatrix1_4.operator_minus(_m3, _m2)) < 0)) {
				len = -len;
			}
			SMatrix1_4 m = new SMatrix1_4();
			m.setX(m0.getX() + r * Math.cos(arf1 + len));
			m.setY(m0.getY() + r * Math.sin(arf1 + len));
			m.setZ(m0.getZ());

			mr = getRotateMatrix(nz, n);
			mResult = SMatrix1_4.operator_multify(m, mr);
		} else {
			mResult = insertLinePoint(_m1, _m2, _len);
		}
		return mResult;
	}

	/**
	 * <p>
	 * Title: getAngleTurnAngle
	 * <p>
	 * Description: 计算拐点角度
	 * 
	 * @param _pt
	 *            点集信息
	 * @param _idx
	 *            拐点索引
	 * @return
	 */
	public static int getTurnAngle(Point[] _pt, int _idx) {
		SMatrix1_4 m1 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx - 1].getX()),
				RobotParam.INSTANCE.YPulse2Journey(_pt[_idx - 1].getY()),
				RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx - 1].getZ()));
		SMatrix1_4 m2 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx].getX()),
				RobotParam.INSTANCE.YPulse2Journey(_pt[_idx].getY()),
				RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx].getZ()));
		SMatrix1_4 m3 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx + 1].getX()),
				RobotParam.INSTANCE.YPulse2Journey(_pt[_idx + 1].getY()),
				RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx + 1].getZ()));
		PointType type = _pt[_idx - 1].getPointParam().getPointType();
		if (type == PointType.POINT_GLUE_LINE_ARC || type == PointType.POINT_WELD_LINE_ARC) {
			SMatrix1_4 m11 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx - 2].getX()),
					RobotParam.INSTANCE.YPulse2Journey(_pt[_idx - 2].getY()),
					RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx - 2].getZ()));
//			SMatrix1_4 m22 = m1;
//			SMatrix1_4 m33 = m2;
			SMatrix1_4 m22 = new SMatrix1_4(m1.getX(), m1.getY(), m1.getZ(), m1.getU());
			SMatrix1_4 m33 = new SMatrix1_4(m2.getX(),m2.getY(),m2.getZ(),m2.getU());
			SMatrix1_4 n = getArcV(m11, m22, m33, false);
			m1 = SMatrix1_4.operator_minus(m2, n);
		}
		type = _pt[_idx + 1].getPointParam().getPointType();
		if (type == PointType.POINT_GLUE_LINE_ARC || type == PointType.POINT_WELD_LINE_ARC) {
			SMatrix1_4 m11 = new SMatrix1_4(m2.getX(),m2.getY(),m2.getZ(),m2.getU());
			SMatrix1_4 m22 = new SMatrix1_4(m3.getX(),m3.getY(),m3.getZ(),m3.getU());
			SMatrix1_4 m33 = new SMatrix1_4(RobotParam.INSTANCE.XPulse2Journey(_pt[_idx + 2].getX()),
					RobotParam.INSTANCE.YPulse2Journey(_pt[_idx + 2].getY()),
					RobotParam.INSTANCE.ZPulse2Journey(_pt[_idx + 2].getZ()));
			SMatrix1_4 n = getArcV(m11, m22, m33, true);
			m3 = SMatrix1_4.operator_plus(m2, n);
		}
		float a = getAngle(m1, m2, m3);
		return (int)(a+0.5f);
	}

	/**
	 * 计算水平面矢量线方向角度
	 * 
	 * @param _p1
	 *            起点
	 * @param _p2
	 *            终点
	 * @return 方向角度[0, 360)
	 */
	public static double getAngle(Point _p1, Point _p2) {
		SMatrix1_4 _m1 = new SMatrix1_4(_p1);
		SMatrix1_4 _m2 = new SMatrix1_4(_p2);
		double dx = _m2.getX() - _m1.getX();
		double dy = _m2.getY() - _m1.getY();
		double angle = 0.0f;
		if (dx > 0 && dy >= 0) { // [0, 90)
			angle = Math.atan(dy / dx) / Math.PI * 180;
		} else if (dx == 0 && dy > 0) { // 90
			angle = 90;
		} else if (dx < 0 && dy >= 0) { // (90, 180]
			angle = 180 + Math.atan(dy / dx) / Math.PI * 180;
		} else if (dx < 0 && dy < 0) { // (180, 270)
			angle = 180 + Math.atan(dy / dx) / Math.PI * 180;
		} else if (dx == 0 && dy < 0) { // 270
			angle = 270;
		} else if (dx > 0 && dy < 0) { // (270, 360)
			angle = 360 + Math.atan(dy / dx) / Math.PI * 180;
		}
		return angle;
	}

	/**
	 * 计算水平面矢量线方向角度
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            终点
	 * @return 方向角度[0, 360)
	 */
	public static double getAngle(SMatrix1_4 _m1, SMatrix1_4 _m2) {
		double dx = _m2.getX() - _m1.getX();
		double dy = _m2.getY() - _m1.getY();
		double angle = 0.0f;
		if (dx > 0 && dy >= 0) { // [0, 90)
			angle = Math.atan(dy / dx) / Math.PI * 180;
		} else if (dx == 0 && dy > 0) { // 90
			angle = 90;
		} else if (dx < 0 && dy >= 0) { // (90, 180]
			angle = 180 + Math.atan(dy / dx) / Math.PI * 180;
		} else if (dx < 0 && dy < 0) { // (180, 270)
			angle = 180 + Math.atan(dy / dx) / Math.PI * 180;
		} else if (dx == 0 && dy < 0) { // 270
			angle = 270;
		} else if (dx > 0 && dy < 0) { // (270, 360)
			angle = 360 + Math.atan(dy / dx) / Math.PI * 180;
		}
		return angle;
	}

	/**
	 * 计算两条直线夹角
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            拐点
	 * @param _m3
	 *            终点
	 * @return 直线夹角[0, 180]
	 */
	public static float getAngle(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3) {
		// SMatrix1_4 SMatrix1_4 = new SMatrix1_4();
		SMatrix1_4 m12 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m23 = SMatrix1_4.operator_minus(_m3, _m2);

		double dot3 = SMatrix1_4.operator_dot3(m12, m23);
		double mod312 = SMatrix1_4.operator_mod3(m12);
		double mod323 = SMatrix1_4.operator_mod3(m23);
//		float angle = (float) Math.acos(
//				SMatrix1_4.operator_dot3(m12, m23) / (SMatrix1_4.operator_mod3(m12)) * SMatrix1_4.operator_mod3(m23));

		float angle = (float) Math.acos(dot3/(mod312*mod323));
		return (float) (180 - angle * 180 / Math.PI);
	}

	/**
	 * 计算水平面矢量线方向角弧度
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            终点
	 * @return 方向角弧度[0, 2*Math.PI)
	 */
	public static double getArc(SMatrix1_4 _m1, SMatrix1_4 _m2) {
		double dx = _m2.getX() - _m1.getX();
		double dy = _m2.getY() - _m1.getY();
		double arc = 0.0;
		if (dx > 0 && dy >= 0) { // [0, 90)
			arc = Math.atan(dy / dx);
		} else if (dx == 0 && dy > 0) { // 90
			arc = Math.PI / 2;
		} else if (dx < 0 && dy >= 0) { // (90, 180]
			arc = Math.PI + Math.atan(dy / dx);
		} else if (dx < 0 && dy < 0) { // (180, 270)
			arc = Math.PI + Math.atan(dy / dx);
		} else if (dx == 0 && dy < 0) { // 270
			arc = 3 * Math.PI / 2;
		} else if (dx > 0 && dy < 0) { // (270, 360)
			arc = 2 * Math.PI + Math.atan(dy / dx);
		}
		return arc;
	}

	/**
	 * 三点算圆心
	 * 
	 * @param _p1
	 * @param _p2
	 * @param _p3
	 * @return
	 */
	public static Point getCenterPt(Point _p1, Point _p2, Point _p3) {
		Point mResult = new Point(PointType.POINT_NULL);

		SMatrix1_4 _m1 = new SMatrix1_4(_p1);
		SMatrix1_4 _m2 = new SMatrix1_4(_p2);
		SMatrix1_4 _m3 = new SMatrix1_4(_p3);
		// System.out.println(_m1.toString() + "," + _m2.toString() + "," +
		// _m3.toString());
		//
		// System.out.println(_p1.toString() + ",," + _p2.toString() + ",," +
		// _p3.toString());

		SMatrix1_4 m12 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m13 = SMatrix1_4.operator_minus(_m3, _m1);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = new SMatrix1_4(m12.getY() * m13.getZ() - m12.getZ() * m13.getY(), // nx
				m12.getZ() * m13.getX() - m12.getX() * m13.getZ(), // ny
				m12.getX() * m13.getY() - m12.getY() * m13.getX(), // nz
				1);

		double b1 = ((_m2.getX() + _m1.getX()) * m12.getX() + (_m2.getY() + _m1.getY()) * m12.getY()
				+ (_m2.getZ() + _m1.getZ()) * m12.getZ()) * 0.5;
		double b2 = ((_m3.getX() + _m1.getX()) * m13.getX() + (_m3.getY() + _m1.getY()) * m13.getY()
				+ (_m3.getZ() + _m1.getZ()) * m13.getZ()) * 0.5;
		double b3 = _m1.getX() * n.getX() + _m1.getY() * n.getY() + _m1.getZ() * n.getZ();
		double det = n.getX() * n.getX() + n.getY() * n.getY() + n.getZ() * n.getZ();

		if (det != 0.0) {
			double detx = n.getX() * b3 + (m13.getY() * n.getZ() - m13.getZ() * n.getY()) * b1
					+ (m12.getZ() * n.getY() - m12.getY() * n.getZ()) * b2;
			double dety = n.getY() * b3 + (m13.getZ() * n.getX() - m13.getX() * n.getZ()) * b1
					+ (m12.getX() * n.getZ() - m12.getZ() * n.getX()) * b2;
			double detz = n.getZ() * b3 + (m13.getX() * n.getY() - m13.getY() * n.getX()) * b1
					+ (m12.getY() * n.getX() - m12.getX() * n.getY()) * b2;
			// System.out.println("detx / det:"+detx / det+"dety / det:"+dety /
			// det);
			mResult.setX(RobotParam.INSTANCE.XCenterJourney2Pulse(detx / det));
			mResult.setY(RobotParam.INSTANCE.YCenterJourney2Pulse(dety / det));
			mResult.setZ(RobotParam.INSTANCE.ZCenterJourney2Pulse(detz / det));
		}

		return mResult;

	}

	/**
	 * 三点算圆心
	 * 
	 * @param _m1
	 * @param _m2
	 * @param _m3
	 * @return
	 */
	public static SMatrix1_4 getCenterPt(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3) {
		SMatrix1_4 mResult = new SMatrix1_4();

		SMatrix1_4 m12 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m13 = SMatrix1_4.operator_minus(_m3, _m1);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = new SMatrix1_4(m12.getY() * m13.getZ() - m12.getZ() * m13.getY(), // nx
				m12.getZ() * m13.getX() - m12.getX() * m13.getZ(), // ny
				m12.getX() * m13.getY() - m12.getY() * m13.getX(), // nz
				1);

		double b1 = ((_m2.getX() + _m1.getX()) * m12.getX() + (_m2.getY() + _m1.getY()) * m12.getY()
				+ (_m2.getZ() + _m1.getZ()) * m12.getZ()) * 0.5;
		double b2 = ((_m3.getX() + _m1.getX()) * m13.getX() + (_m3.getY() + _m1.getY()) * m13.getY()
				+ (_m3.getZ() + _m1.getZ()) * m13.getZ()) * 0.5;
		double b3 = _m1.getX() * n.getX() + _m1.getY() * n.getY() + _m1.getZ() * n.getZ();
		double det = n.getX() * n.getX() + n.getY() * n.getY() + n.getZ() * n.getZ();

		if (det != 0.0) {
			double detx = n.getX() * b3 + (m13.getY() * n.getZ() - m13.getZ() * n.getY()) * b1
					+ (m12.getZ() * n.getY() - m12.getY() * n.getZ()) * b2;
			double dety = n.getY() * b3 + (m13.getZ() * n.getX() - m13.getX() * n.getZ()) * b1
					+ (m12.getX() * n.getZ() - m12.getZ() * n.getX()) * b2;
			double detz = n.getZ() * b3 + (m13.getX() * n.getY() - m13.getY() * n.getX()) * b1
					+ (m12.getY() * n.getX() - m12.getX() * n.getY()) * b2;
			// System.out.println("detx / det:"+detx / det+"dety / det:"+dety /
			// det);
			mResult.setX(detx / det);
			mResult.setY(dety / det);
			mResult.setZ(detz / det);
		}

		return mResult;

	}

	/**
	 * 判断点是否在线段内（RECT_ltrb类型是long，而SMatrix_1_4类型为double以后要注意看有没有问题）
	 * 
	 * @param _m
	 *            点
	 * @param _m1
	 *            线段起点
	 * @param _m2
	 *            线段终点
	 * @return
	 */
	public static boolean isPtOnLine(SMatrix1_4 _m, SMatrix1_4 _m1, SMatrix1_4 _m2) {
		double left, right, top, bottom;
		if (_m1.getX() < _m2.getX()) {
			left = _m1.getX();
			right = _m2.getX();
		} else {
			left = _m2.getX();
			right = _m1.getX();
		}
		if (_m1.getY() < _m2.getY()) {
			top = _m1.getY();
			bottom = _m2.getY();
		} else {
			top = _m2.getY();
			bottom = _m1.getY();
		}
		return _m.getX() >= left && _m.getX() <= right && _m.getY() >= top && _m.getY() <= bottom;
	}

	/**
	 * 计算与两直线相切的圆弧点
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            拐点
	 * @param _m3
	 *            终点
	 * @param _len
	 *            拐点到切点长度
	 * @param _m
	 *            圆弧起点、圆弧中间点、圆弧终点
	 * @return
	 */
	public static boolean getArcPt(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, float _len, SMatrix1_4[] _m) {
		// SMatrix1_4 SMatrix1_4 = new SMatrix1_4();
		// 矢量线 _m1->_m2，_m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		// 目标矢量方向
		SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
		// 计算旋转矩阵
		SMatrix_4_4 mr = getRotateMatrix(n, nz);
		_m1 = SMatrix1_4.operator_multify(_m1, mr);
		_m2 = SMatrix1_4.operator_multify(_m2, mr);
		_m3 = SMatrix1_4.operator_multify(_m3, mr);

		SMatrix1_4 m1 = insertLinePoint(_m2, _m1, _len);// 圆弧起点
		SMatrix1_4 m2 = new SMatrix1_4();
		SMatrix1_4 m3 = insertLinePoint(_m2, _m3, _len); // 圆弧终点

		// 求圆心坐标
		SMatrix1_4 m0 = new SMatrix1_4();
		if (_m2.getX() == _m1.getX()) {
			if (m3.getX() == _m2.getX()) {
				SMatrix1_4 m_1_3 = SMatrix1_4.operator_plus(m1, m3);
				m0 = SMatrix1_4.operator_division(m_1_3, 2);
			} else {
				m0.setY(m1.getY());
				m0.setX(m3.getX() - (m3.getY() - _m2.getY()) * (m0.getY() - m3.getY()) / (m3.getX() - _m2.getX()));
			}
		} else if (_m2.getX() == _m3.getX()) {
			if (m1.getX() == _m2.getX()) {
				SMatrix1_4 m_1_3 = SMatrix1_4.operator_plus(m1, m3);
				m0 = SMatrix1_4.operator_division(m_1_3, 2);
			} else {
				m0.setY(m3.getY());
				m0.setX(m1.getX() - (m1.getY() - _m2.getY()) * (m0.getY() - m1.getY()) / (m1.getX() - _m2.getX()));
			}
		} else {
			double k1 = (_m1.getY() - _m2.getY()) / (_m1.getX() - _m2.getX());
			double k2 = (_m3.getY() - _m2.getY()) / (_m3.getX() - _m2.getX());
			if (k1 == k2) {
				SMatrix1_4 m_1_3 = SMatrix1_4.operator_plus(m1, m3);
				m0 = SMatrix1_4.operator_division(m_1_3, 2);
			} else {
				m0.setX((k1 * k2 * (m3.getY() - m1.getY()) + k1 * m3.getX() - k2 * m1.getX()) / (k1 - k2));
				if (getAbsoluteValue(k1) > 1e-8) {
					m0.setY(m1.getY() - (m0.getX() - m1.getX()) / k1);
				} else if (getAbsoluteValue(k2) > 1e-8) {
					m0.setY(m3.getY() - (m0.getX() - m3.getX()) / k2);
				} else {
					return false;
				}
			}
		}
		m0.setZ(_m1.getZ());
		// 求圆弧中间点
		SMatrix1_4 m01 = SMatrix1_4.operator_minus(m1, m0);
		double r = SMatrix1_4.operator_mod3(m01);
		// 求方向角弧度
		double arc1 = getArc(m0, m1);
		double arc3 = getArc(m0, m3);
		// 求方向矢量
		SMatrix1_4 m_2_1 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_3_2 = SMatrix1_4.operator_minus(_m3, _m2);
		double v = SMatrix1_4.operator_cross2(m_2_1, m_3_2);
		if (v > 0 && arc1 > arc3) {
			arc3 += Math.PI + Math.PI;
		} else if (v < 0 && arc1 < arc3) {
			arc1 += Math.PI + Math.PI;
		}
		// 求圆弧中间点
		double arc2 = (arc1 + arc3) / 2;
		m2.setX(m0.getX() + r * Math.cos(arc2));
		m2.setY(m0.getY() + r * Math.sin(arc2));
		m2.setZ(m0.getZ());
		boolean nRet = true;
		if (!isPtOnLine(m1, _m1, _m2) || !isPtOnLine(m3, _m2, _m3)) {
			nRet = false;
		}
		mr = getRotateMatrix(nz, n);
		m1 = SMatrix1_4.operator_multify(m1, mr);
		m2 = SMatrix1_4.operator_multify(m2, mr);
		m3 = SMatrix1_4.operator_multify(m3, mr);

		_m[0] = m1;
		_m[1] = m2;
		_m[2] = m3;

		return nRet;
	}

	/**
	 * 计算与直线和圆弧相切的圆弧点
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            拐点(圆弧起始点)
	 * @param _m3
	 *            圆弧中间点
	 * @param _m4
	 *            圆弧结束点
	 * @param _len1
	 *            拐点到直线切点长度
	 * @param _len2
	 *            拐点到圆弧切点长度
	 * @param _m
	 *            圆弧起点、圆弧中间点、圆弧终点(近似拟合)
	 * @return
	 */
	public static boolean getArcPt(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, SMatrix1_4 _m4, float _len1, float _len2,
			SMatrix1_4[] _m) {
		// SMatrix1_4 SMatrix1_4 = new SMatrix1_4();
		// 矢量线 _m1->_m2，_m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		// 目标矢量方向
		SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
		// 计算旋转矩阵
		SMatrix_4_4 mr = getRotateMatrix(n, nz);
		_m1 = SMatrix1_4.operator_multify(_m1, mr);
		_m2 = SMatrix1_4.operator_multify(_m2, mr);
		_m3 = SMatrix1_4.operator_multify(_m3, mr);
		_m4 = SMatrix1_4.operator_multify(_m4, mr);

		SMatrix1_4 m1 = insertLinePoint(_m2, _m1, _len1);// 圆弧起点
		SMatrix1_4 m2 = new SMatrix1_4();// 圆弧中间点
		SMatrix1_4 m3 = insertLinePoint(_m2, _m3, _len2); // 圆弧终点

		// 求圆心坐标
		SMatrix1_4 c1 = getCenterPt(_m2, _m3, _m4);
		SMatrix1_4 m0 = new SMatrix1_4();
		if (_m2.getX() == _m1.getX() && c1.getX() == m3.getX()) {
			m0.setX(m3.getX());
			m0.setY(m1.getY());

		} else if (_m2.getX() == _m1.getX()) {
			if (m3.getY() == c1.getY()) {
				SMatrix1_4 m_1_3 = SMatrix1_4.operator_plus(m1, m3);
				m0 = SMatrix1_4.operator_division(m_1_3, 2);
			} else {
				m0.setY(m1.getY());
				m0.setX(m3.getX() + (m3.getX() - c1.getX()) * (m0.getY() - m3.getY()) / (m3.getY() - c1.getY()));
			}
		} else if (c1.getX() == m3.getX()) {
			if (m1.getY() == _m2.getY()) {
				SMatrix1_4 m_1_3 = SMatrix1_4.operator_plus(m1, m3);
				m0 = SMatrix1_4.operator_division(m_1_3, 2);
			} else {
				m0.setX(m3.getX());
				m0.setY(m1.getY() - (m1.getX() - _m2.getX()) * (m0.getX() - m1.getX()) / (m1.getY() - _m2.getY()));
			}
		} else {
			double k1 = (_m1.getY() - _m2.getY()) / (_m1.getX() - _m2.getX());
			double k2 = (m3.getY() - c1.getY()) / (m3.getX() - c1.getX());
			if (k1 * k2 == -1) {
				SMatrix1_4 m_1_3 = SMatrix1_4.operator_plus(m1, m3);
				m0 = SMatrix1_4.operator_division(m_1_3, 2);
			} else {
				m0.setX((k1 * (m1.getY() - m3.getY()) + m1.getX() + k1 * k2 * m3.getX()) / (k1 * k2 + 1));
				m0.setY(m3.getY() + (m0.getX() - m3.getX()) * k2);
			}
		}
		m0.setZ(_m1.getZ());
		// 求圆弧中间点
		SMatrix1_4 m01 = SMatrix1_4.operator_minus(m1, m0);
		double r = SMatrix1_4.operator_mod3(m01);
		// 求方向角弧度
		double arc1 = getArc(m0, m1);
		double arc3 = getArc(m0, m3);
		// 求方向矢量
		SMatrix1_4 m_2_1 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_3_2 = SMatrix1_4.operator_minus(_m3, _m2);
		double v = SMatrix1_4.operator_cross2(m_2_1, m_3_2);
		if (v > 0 && arc1 > arc3) {
			arc3 += Math.PI + Math.PI;
		} else if (v < 0 && arc1 < arc3) {
			arc1 += Math.PI + Math.PI;
		}
		// 求圆弧中间点
		double arc2 = (arc1 + arc3) / 2;
		m2.setX(m0.getX() + r * Math.cos(arc2));
		m2.setY(m0.getY() + r * Math.sin(arc2));
		m2.setZ(m0.getZ());
		boolean nRet = true;
		if (!isPtOnLine(m1, _m1, _m2)) {
			nRet = false;
		} else {
			double arcS = getArc(c1, _m2);
			double arcE = getArc(c1, _m4);
			if (isPtInLine(_m2, _m3, _m4) < 0) // 顺时针弧
			{
				double temp = arcS;
				arcS = arcE;
				arcE = temp;
			}
			double arc = getArc(c1, m3);
			if (!isArcOnArc(arcS, arcE, arc)) {
				nRet = false;
			}
		}
		mr = getRotateMatrix(nz, n);
		m1 = SMatrix1_4.operator_multify(m1, mr);
		m2 = SMatrix1_4.operator_multify(m2, mr);
		m3 = SMatrix1_4.operator_multify(m3, mr);

		_m[0] = m1;
		_m[1] = m2;
		_m[2] = m3;

		return nRet;
	}

	/**
	 * 计算与两直线相切的圆弧点
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            拐点
	 * @param _m3
	 *            终点
	 * @param _r
	 *            圆半径
	 * @param _m
	 *            圆弧起点、圆弧中间点、圆弧终点
	 * @return
	 */
	public static boolean getArcPtByR(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, float _r, SMatrix1_4 _m[]) {
		// 矢量线 _m1->_m2，_m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		// 目标矢量方向
		SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
		// 计算旋转矩阵
		SMatrix_4_4 mr = getRotateMatrix(n, nz);
		_m1 = SMatrix1_4.operator_multify(_m1, mr);
		_m2 = SMatrix1_4.operator_multify(_m2, mr);
		_m3 = SMatrix1_4.operator_multify(_m3, mr);
		float angle = getAngle(_m1, _m2, _m3);
		float len = (float) (_r / Math.tan(angle / 2 * Math.PI / 180));
		boolean bRet = getArcPt(_m1, _m2, _m3, len, _m);
		mr = getRotateMatrix(nz, n);
		_m[0] = SMatrix1_4.operator_multify(_m[0], mr);
		_m[1] = SMatrix1_4.operator_multify(_m[1], mr);
		_m[2] = SMatrix1_4.operator_multify(_m[2], mr);
		return bRet;

	}

	/**
	 * 计算与直线和圆弧相切的圆弧点
	 * 
	 * @param _m1
	 *            起点
	 * @param _m2
	 *            拐点(圆弧起始点)
	 * @param _m3
	 *            圆弧中间点
	 * @param _m4
	 *            圆弧结束点
	 * @param _r
	 *            圆半径
	 * @param _m
	 *            圆弧起点、圆弧中间点、圆弧终点
	 * @return
	 */
	public static boolean getArcPtByR(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, SMatrix1_4 _m4, float _r,
			SMatrix1_4 _m[]) {
		// 矢量线 _m1->_m2，_m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		// 目标矢量方向
		SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
		// 计算旋转矩阵
		SMatrix_4_4 mr = getRotateMatrix(n, nz);
		_m1 = SMatrix1_4.operator_multify(_m1, mr);
		_m2 = SMatrix1_4.operator_multify(_m2, mr);
		_m3 = SMatrix1_4.operator_multify(_m3, mr);
		_m4 = SMatrix1_4.operator_multify(_m4, mr);
		SMatrix1_4 m0 = new SMatrix1_4(); // 圆弧圆点
		SMatrix1_4 m1; // 圆弧起点
		SMatrix1_4 m2 = new SMatrix1_4(); // 圆弧中间点
		SMatrix1_4 m3; // 圆弧终点
		m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		SMatrix1_4 m_3_4 = SMatrix1_4.operator_minus(_m4, _m3);
		// 求圆心坐标
		SMatrix1_4 c1 = getCenterPt(_m2, _m3, _m4);
		SMatrix1_4 mc1_2 = SMatrix1_4.operator_minus(_m2, c1);
		double r1 = SMatrix1_4.operator_mod3(mc1_2);
		// 求方向矢量
		double v = SMatrix1_4.operator_cross2(m_1_2, m_2_3);
		double v1 = SMatrix1_4.operator_cross2(m_2_3, m_3_4);

		if (m_1_2.getX() == 0) {
			if (m_2_3.getX() > 0) {
				m0.setX(_m2.getX() + _r);
			} else {
				m0.setX(_m2.getX() - _r);
			}
			if (v * v1 < 0) {
				if (m_2_3.getX() * v > 0) {
					m0.setY(c1.getY()
							+ Math.sqrt((r1 + _r) * (r1 + _r) - (m0.getX() - c1.getX()) * (m0.getX() - c1.getX())));
				} else {
					m0.setY(c1.getY()
							- Math.sqrt((r1 + _r) * (r1 + _r) - (m0.getX() - c1.getX()) * (m0.getX() - c1.getX())));
				}
			} else {
				if (m_2_3.getX() * (_m2.getX() - c1.getX()) <= 0) {
					if (r1 + getAbsoluteValue(_m2.getX() - c1.getX()) < _r + _r) {
						return false;
					}
				} else {
					if (r1 - getAbsoluteValue(_m2.getX() - c1.getX()) < _r + _r) {
						return false;
					}
				}
				if (m_2_3.getX() * v < 0) {
					m0.setY(c1.getY()
							+ Math.sqrt((r1 - _r) * (r1 - _r) - (m0.getX() - c1.getX()) * (m0.getX() - c1.getX())));
				} else {
					m0.setY(c1.getY()
							- Math.sqrt((r1 - _r) * (r1 - _r) - (m0.getX() - c1.getX()) * (m0.getX() - c1.getX())));
				}
			}
		} else {

			float k1 = (float) (m_1_2.getY() / m_1_2.getX());
			double t, arc;
			if (k1 == 0) {
				if (m_2_3.getY() < 0) {
					t = _m1.getY() - c1.getY() - _r;
				} else {
					t = _m1.getY() - c1.getY() + _r;
				}
				if (v * v1 < 0) {
					if (getAbsoluteValue(t) > getAbsoluteValue(r1 + _r)) {
						return false;
					}
					if (m_2_3.getY() * v > 0) {
						arc = -Math.acos(t / (r1 + _r));
					} else {
						arc = Math.acos(t / (r1 + _r));
					}
				} else {
					if (getAbsoluteValue(t) > getAbsoluteValue(r1 - _r)) {
						return false;
					}
					if (m_2_3.getY() * v < 0) {
						arc = -Math.acos(t / (r1 - _r));
					} else {
						arc = Math.acos(t / (r1 - _r));
					}
				}
			} else {
				double k = Math.sqrt(1 + k1 * k1);
				if (v * v1 < 0) {
					if ((k1 < 0 && v * m_1_2.getY() < 0) || (k1 > 0 && v * m_1_2.getY() > 0)) {
						t = (-_r * k + c1.getY() - (_m1.getY() - k1 * _m1.getX()) - k1 * c1.getX()) / (r1 + _r);
					} else {
						t = (_r * k + c1.getY() - (_m1.getY() - k1 * _m1.getX()) - k1 * c1.getX()) / (r1 + _r);
					}
					if (getAbsoluteValue(t) > k) {
						return false;
					}
					if (m_1_2.getY() > 0) {
						if (k1 < 0) {
							arc = Math.PI - Math.asin(-t / k) - Math.asin(1 / k);
						} else {
							arc = Math.PI - Math.asin(t / k) + Math.asin(1 / k);
						}
					} else {
						if (k1 < 0) {
							arc = Math.asin(-t / k) - Math.asin(1 / k);
						} else {
							arc = Math.asin(t / k) + Math.asin(1 / k);
						}
					}
				} else {
					if ((k1 < 0 && v * m_1_2.getY() < 0) || (k1 > 0 && v * m_1_2.getY() > 0)) {
						t = (-_r * k + c1.getY() - (_m1.getY() - k1 * _m1.getX()) - k1 * c1.getX()) / (r1 - _r);
					} else {
						t = (_r * k + c1.getY() - (_m1.getY() - k1 * _m1.getX()) - k1 * c1.getX()) / (r1 - _r);
					}
					if (getAbsoluteValue(t) > k) {
						return false;
					}
					if (m_1_2.getY() < 0) {
						if (k1 < 0) {
							arc = Math.PI - Math.asin(-t / k) - Math.asin(1 / k);
						} else {
							arc = Math.PI - Math.asin(t / k) + Math.asin(1 / k);
						}
					} else {
						if (k1 < 0) {
							arc = Math.asin(-t / k) - Math.asin(1 / k);
						} else {
							arc = Math.asin(t / k) + Math.asin(1 / k);
						}
					}
				}
			}
			if (v * v1 < 0) {
				m0 = new SMatrix1_4((r1 + _r) * Math.sin(arc) + c1.getX(), (r1 + _r) * Math.cos(arc) + c1.getY());
			} else {
				m0 = new SMatrix1_4((r1 - _r) * Math.sin(arc) + c1.getX(), (r1 - _r) * Math.cos(arc) + c1.getY());
			}

		}

		m0.setZ(_m1.getZ());
		// 求圆弧起点
		m1 = getProjection(_m1, _m2, m0, true);
		// 求圆弧切点
		SMatrix1_4 mc10 = SMatrix1_4.operator_minus(m0, c1);
		SMatrix1_4 m0_2 = SMatrix1_4.operator_minus(_m2, m0);
		m3 = insertArcPoint(_m2, _m3, _m4,
				(float) (r1 * Math.acos((SMatrix1_4.operator_dot3(mc10, mc10) + SMatrix1_4.operator_dot3(mc1_2, mc1_2)
						- SMatrix1_4.operator_dot3(m0_2, m0_2))
						/ (2 * SMatrix1_4.operator_mod3(mc10) * SMatrix1_4.operator_mod3(mc1_2)))));

		// 求圆弧中间点
		SMatrix1_4 m01 = SMatrix1_4.operator_minus(m1, m0);
		double r = SMatrix1_4.operator_mod3(m01);
		// 求方向角弧度
		double arc1 = getArc(m0, m1);
		double arc3 = getArc(m0, m3);
		if (v > 0 && arc1 > arc3) {
			arc3 += Math.PI + Math.PI;
		} else if (v < 0 && arc1 < arc3) {
			arc1 += Math.PI + Math.PI;
		}
		// 求圆弧中间点
		double arc2 = (arc1 + arc3) / 2;
		m2.setX(m0.getX() + r * Math.cos(arc2));
		m2.setY(m0.getY() + r * Math.sin(arc2));
		m2.setZ(m0.getZ());

		boolean nRet = true;
		if (!isPtOnLine(m1, _m1, _m2)) {
			nRet = false;
		} else {
			double arcS = getArc(c1, _m2);
			double arcE = getArc(c1, _m4);
			if (isPtInLine(_m2, _m3, _m4) < 0) // 顺时针弧
			{
				double temp = arcS;
				arcS = arcE;
				arcE = temp;
			}
			double arc = getArc(c1, m3);
			if (!isArcOnArc(arcS, arcE, arc)) {
				nRet = false;
			}
		}
		mr = getRotateMatrix(nz, n);
		m1 = SMatrix1_4.operator_multify(m1, mr);
		m2 = SMatrix1_4.operator_multify(m2, mr);
		m3 = SMatrix1_4.operator_multify(m3, mr);

		_m[0] = m1;
		_m[1] = m2;
		_m[2] = m3;
		return nRet;

	}

	/**
	 * 计算与两圆弧相切的圆弧点
	 * 
	 * @param _m1
	 *            圆弧起始点
	 * @param _m2
	 *            圆弧中间点
	 * @param _m3
	 *            拐点(圆弧起始点)
	 * @param _m4
	 *            圆弧中间点
	 * @param _m5
	 *            圆弧结束点
	 * @param _r
	 *            圆半径
	 * @param _m
	 *            圆弧起点、圆弧中间点、圆弧终点
	 * @return
	 */
	public static boolean getArcPtByR(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, SMatrix1_4 _m4, SMatrix1_4 _m5, float _r,
			SMatrix1_4[] _m) {
		// 矢量线 _m1->_m2，_m2->_m3
		SMatrix1_4 m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		SMatrix1_4 m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		// _m1，_m2，_m3 三点平面法向量 n（nx，ny，nz）
		SMatrix1_4 n = SMatrix1_4.operator_cross3(m_1_2, m_2_3);
		// 目标矢量方向
		SMatrix1_4 nz = new SMatrix1_4(0, 0, 1, 1);
		// 计算旋转矩阵
		SMatrix_4_4 mr = getRotateMatrix(n, nz);
		_m1 = SMatrix1_4.operator_multify(_m1, mr);
		_m2 = SMatrix1_4.operator_multify(_m2, mr);
		_m3 = SMatrix1_4.operator_multify(_m3, mr);
		_m4 = SMatrix1_4.operator_multify(_m4, mr);
		_m5 = SMatrix1_4.operator_multify(_m5, mr);
		SMatrix1_4 m0 = new SMatrix1_4(); // 圆弧圆点
		SMatrix1_4 m1; // 圆弧起点
		SMatrix1_4 m2 = new SMatrix1_4(); // 圆弧中间点
		SMatrix1_4 m3; // 圆弧终点

		m_1_2 = SMatrix1_4.operator_minus(_m2, _m1);
		m_2_3 = SMatrix1_4.operator_minus(_m3, _m2);
		SMatrix1_4 m_3_4 = SMatrix1_4.operator_minus(_m4, _m3);
		SMatrix1_4 m_4_5 = SMatrix1_4.operator_minus(_m5, _m4);

		// 求圆心坐标
		SMatrix1_4 c1 = getCenterPt(_m1, _m2, _m3);
		SMatrix1_4 mc1_3 = SMatrix1_4.operator_minus(_m3, c1);
		double r1 = SMatrix1_4.operator_mod3(mc1_3);
		SMatrix1_4 c2 = getCenterPt(_m3, _m4, _m5);
		SMatrix1_4 mc2_3 = SMatrix1_4.operator_minus(_m3, c2);
		double r2 = SMatrix1_4.operator_mod3(mc2_3);
		if (SMatrix1_4.operator_equal(c1, c2)) {
			return false;
		}
		// 求方向矢量
		double v = SMatrix1_4.operator_cross2(m_2_3, m_3_4);
		double v1 = SMatrix1_4.operator_cross2(m_1_2, m_2_3);
		double v2 = SMatrix1_4.operator_cross2(m_3_4, m_4_5);
		SMatrix1_4 mc1c2 = SMatrix1_4.operator_minus(c2, c1);
		double k = SMatrix1_4.operator_mod2(mc1c2);
		if (v * v1 < 0 && v * v2 < 0) {
			double t = ((r2 + _r + r1 + _r) * (r2 - r1) - k * k) / (2 * (r1 + _r) * k);
			if (getAbsoluteValue(t) > 1.0 || getAbsoluteValue(mc1c2.getY()) > k) {
				return false;
			}
			double arc;
			if (v > 0) {
				arc = Math.asin(-t);
			} else {
				arc = Math.PI + Math.asin(t);
			}
			if (mc1c2.getX() > 0) {
				arc = arc - Math.asin(mc1c2.getY() / k);
			} else {
				arc = arc - Math.PI + Math.asin(mc1c2.getY() / k);
			}
			m0 = new SMatrix1_4((r1 + _r) * Math.sin(arc) + c1.getX(), (r1 + _r) * Math.cos(arc) + c1.getY());
		} else if (v * v1 < 0 && v * v2 > 0) {
			double t = ((r2 + r1) * (r2 - _r - r1 - _r) - k * k) / (2 * (r1 + _r) * k);
			if (getAbsoluteValue(t) > 1.0 || getAbsoluteValue(mc1c2.getY()) > k) {
				return false;
			}
			double arc;
			if (v < 0) {
				arc = Math.asin(-t);
			} else {
				arc = Math.PI + Math.asin(t);
			}
			if (mc1c2.getX() > 0) {
				arc = arc - Math.asin(mc1c2.getY() / k);
			} else {
				arc = arc - Math.PI + Math.asin(mc1c2.getY() / k);
			}
			m0 = new SMatrix1_4((r1 + _r) * Math.sin(arc) + c1.getX(), (r1 + _r) * Math.cos(arc) + c1.getY());
		} else if (v * v1 > 0 && v * v2 < 0) {
			double t = ((r2 + r1) * (r2 + _r - r1 + _r) - k * k) / (2 * (r1 - _r) * k);
			if (getAbsoluteValue(t) > 1.0 || getAbsoluteValue(mc1c2.getY()) > k) {
				return false;
			}
			double arc;
			if (v < 0) {
				arc = Math.asin(-t);
			} else {
				arc = Math.PI + Math.asin(t);
			}
			if (mc1c2.getX() > 0) {
				arc = arc - Math.asin(mc1c2.getY() / k);
			} else {
				arc = arc - Math.PI + Math.asin(mc1c2.getY() / k);
			}
			m0 = new SMatrix1_4((r1 - _r) * Math.sin(arc) + c1.getX(), (r1 - _r) * Math.cos(arc) + c1.getY());
		} else if (v * v1 > 0 && v * v2 > 0) {
			double t = ((r2 - _r + r1 - _r) * (r2 - r1) - k * k) / (2 * (r1 - _r) * k);
			if (getAbsoluteValue(t) > 1.0 || getAbsoluteValue(mc1c2.getY()) > k) {
				return false;
			}
			double arc;
			if (v > 0) {
				arc = Math.asin(-t);
			} else {
				arc = Math.PI + Math.asin(t);
			}
			if (mc1c2.getX() > 0) {
				arc = arc - Math.asin(mc1c2.getY() / k);
			} else {
				arc = arc - Math.PI + Math.asin(mc1c2.getY() / k);
			}
			m0 = new SMatrix1_4((r1 - _r) * Math.sin(arc) + c1.getX(), (r1 - _r) * Math.cos(arc) + c1.getY());
		}

		m0.setZ(_m1.getZ());
		// 求圆弧起点
		SMatrix1_4 m0c1 = SMatrix1_4.operator_minus(c1, m0);
		SMatrix1_4 m0_3 = SMatrix1_4.operator_minus(_m3, m0);
		m1 = insertArcPoint(_m3, _m2, _m1,
				(float) (r1 * Math.acos((SMatrix1_4.operator_dot3(m0c1, m0c1) + SMatrix1_4.operator_dot3(mc1_3, mc1_3)
						- SMatrix1_4.operator_dot3(m0_3, m0_3))
						/ (2 * SMatrix1_4.operator_mod3(m0c1) * SMatrix1_4.operator_mod3(mc1_3)))));

		// 求圆弧终点
		SMatrix1_4 m0c2 = SMatrix1_4.operator_minus(c2, m0);
		m3 = insertArcPoint(_m3, _m4, _m5,
				(float) (r2 * Math.acos((SMatrix1_4.operator_dot3(m0c2, m0c2) + SMatrix1_4.operator_dot3(mc2_3, mc2_3)
						- SMatrix1_4.operator_dot3(m0_3, m0_3))
						/ (2 * SMatrix1_4.operator_mod3(m0c2) * SMatrix1_4.operator_mod3(mc2_3)))));

		// 求圆弧中间点
		SMatrix1_4 m01 = SMatrix1_4.operator_minus(m1, m0);
		double r = SMatrix1_4.operator_mod3(m01);
		// 求方向角弧度
		double arc1 = getArc(m0, m1);
		double arc3 = getArc(m0, m3);
		if (v > 0 && arc1 > arc3) {
			arc3 += Math.PI + Math.PI;
		} else if (v < 0 && arc1 < arc3) {
			arc1 += Math.PI + Math.PI;
		}
		// 求圆弧中间点
		double arc2 = (arc1 + arc3) / 2;
		m2.setX(m0.getX() + r * Math.cos(arc2));
		m2.setY(m0.getY() + r * Math.sin(arc2));
		m2.setZ(m0.getZ());

		boolean nRet = true;
		double arcS = getArc(c1, _m1);
		double arcE = getArc(c1, _m3);
		if (isPtInLine(_m1, _m2, _m3) < 0) // 顺时针弧
		{
			double temp = arcS;
			arcS = arcE;
			arcE = temp;
		}
		double arc = getArc(c1, m1);
		if (!isArcOnArc(arcS, arcE, arc)) {
			nRet = false;
		} else {
			arcS = getArc(c2, _m3);
			arcE = getArc(c2, _m5);
			if (isPtInLine(_m3, _m4, _m5) < 0) // 顺时针弧
			{
				double temp = arcS;
				arcS = arcE;
				arcE = temp;
			}
			arc = getArc(c2, m3);
			if (!isArcOnArc(arcS, arcE, arc)) {
				nRet = false;
			}
		}
		mr = getRotateMatrix(nz, n);
		m1 = SMatrix1_4.operator_multify(m1, mr);

		m2 = SMatrix1_4.operator_multify(m2, mr);
		m3 = SMatrix1_4.operator_multify(m3, mr);

		_m[0] = m1;
		_m[1] = m2;
		_m[2] = m3;
		return nRet;
	}

	/**
	 * @param _m1
	 *            圆弧起点
	 * @param _m2
	 *            圆弧终点
	 * @param _bulge
	 *            圆弧凸度(2*弓高/弦长)
	 * @return 圆弧中间点
	 */
	public static SMatrix1_4 getArcMidPt(SMatrix1_4 _m1, SMatrix1_4 _m2, double _bulge) {
		double x1 = _m1.getX();
		double y1 = _m1.getY();
		double x2 = _m2.getX();
		double y2 = _m2.getY();

		double l = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));// 弦长
		double arc = 4.0 * Math.atan(getAbsoluteValue(_bulge));// 圆弧角
		if (arc > Math.PI) {
			arc = 2 * Math.PI - arc;
		}
		double r = l / (2 * Math.sin(arc * 0.5)); // 半径
		double c = r * Math.cos(arc * 0.5); // 圆心到弦的距离
		/* 求弦的直线 */
		double A, B, c1;
		double c2 = 0.0;
		double k;
		double xArc;
		double yArc;
		int BL = 0;
		if (getAbsoluteValue(x1 - x2) < 0.0001) {
			c1 = x1;
			double sign = _bulge > 0 ? 1 : -1;
			double a = (y1 - y2) / 2;
			if (a > 0) {
				BL = 1;
			} else {
				BL = -1;
			}
			c2 = c1 - BL * sign * (r - Math.sqrt(r * r - a * a));
			xArc = c2;
			yArc = (y1 + y2) / 2;
		} else if (getAbsoluteValue(y1 - y2) < 0.0001) {
			double sign = _bulge > 0 ? 1 : -1;
			double b = (x1 - x2) / 2;
			if (b > 0) {
				BL = 1;
			} else {
				BL = -1;
			}
			c2 = y1 + BL * sign * (r - Math.sqrt(r * r - b * b));
			xArc = (x1 + x2) / 2;
			yArc = c2;
		} else {
			k = (y1 - y2) / (x1 - x2);
			double b = y1 - k * x1;
			A = k;
			B = -1;
			c1 = b;
			double sign = _bulge > 0 ? 1 : -1;
			if (A > 0 && y1 < y2) // 弦过一三象限
			{
				c2 = c1 - sign * Math.sqrt(A * A + B * B) * (r - c);
			} else if (A > 0 && y1 > y2) // 弦过一三象限
			{
				c2 = c1 + sign * Math.sqrt(A * A + B * B) * (r - c);
			} else if (A < 0 && y1 < y2) // 弦过二四象限
			{
				c2 = c1 + sign * Math.sqrt(A * A + B * B) * (r - c);
			} else if (A < 0 && y1 > y2) // 弦过二四象限
			{
				c2 = c1 - sign * Math.sqrt(A * A + B * B) * (r - c);
			}

			/* 求中垂线 */
			double Ac;
			double Cc;
			Ac = (-1) / A;
			Cc = (y1 + y2) / 2 - Ac * (x1 + x2) / 2;
			// 求圆弧点, AX-Y+c2 = 0 和 AcX-Y+Cc = 0的交点
			xArc = (Cc - c2) / (A - Ac);
			yArc = Ac * xArc + Cc;
		}
		SMatrix1_4 m = new SMatrix1_4(xArc, yArc);
		return m;
	}

	/**
	 * 判断线段与矩形框是否相交（RECT_ltrb类型是long，而SMatrix_1_4类型为double以后要注意看有没有问题）
	 * 
	 * @param _m1
	 *            线段起点
	 * @param _m2
	 *            线段终点
	 * @param _rc
	 *            矩形框（左小右大，顶小底大）
	 * @return TRUE：是，false：否
	 */
	public static boolean isLineInRect(SMatrix1_4 _m1, SMatrix1_4 _m2, Rect_ltrb _rc) {
		Rect_ltrb rc = new Rect_ltrb();
		if (_m1.getX() > _m2.getX()) {
			rc.setLeft(_m2.getX());
			rc.setRight(_m1.getX());
		} else {
			rc.setLeft(_m1.getX());
			rc.setRight(_m2.getX());
		}
		if (_m1.getY() > _m2.getY()) {
			rc.setTop(_m2.getY());
			rc.setBottom(_m1.getY());
		} else {
			rc.setTop(_m1.getY());
			rc.setBottom(_m2.getY());
		}
		if (isRectInRect(rc, _rc)) {
			SMatrix1_4 pt1 = new SMatrix1_4(_rc.getLeft(), _rc.getTop());
			SMatrix1_4 pt2 = new SMatrix1_4(_rc.getRight(), _rc.getBottom());
			SMatrix1_4 pt3 = new SMatrix1_4(_rc.getLeft(), _rc.getBottom());
			SMatrix1_4 pt4 = new SMatrix1_4(_rc.getRight(), _rc.getTop());

			double p1 = isPtInLine(_m1, _m2, pt1);
			double p2 = isPtInLine(_m1, _m2, pt2);
			double p3 = isPtInLine(_m1, _m2, pt3);
			double p4 = isPtInLine(_m1, _m2, pt4);

			if ((p1 == 0 || p2 == 0 || p3 == 0 || p4 == 0) || (p1 * p2 < 0) || (p3 * p4 < 0)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断点与线段的关系
	 * 
	 * @param _m1
	 *            线段起点
	 * @param _m2
	 *            线段终点
	 * @param _m
	 *            点
	 * @return 0：点在线段所在直线上，>0：点在线段左侧，<0：点在线段右侧
	 */
	private static double isPtInLine(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m) {
		return (double) (_m2.getX() - _m1.getX()) * (_m.getY() - _m1.getY())
				- (double) (_m2.getY() - _m1.getY()) * (_m.getX() - _m1.getX());
	}

	/**
	 * 判断矩形框与矩形框是否相交
	 * 
	 * @param _rc1
	 *            矩形框（左小右大，顶小底大）
	 * @param _rc2
	 *            矩形框（左小右大，顶小底大）
	 * @return TRUE：是，FALSE：否
	 */
	private static boolean isRectInRect(Rect_ltrb _rc1, Rect_ltrb _rc2) {
		return _rc1.getRight() >= _rc2.getLeft() && _rc1.getLeft() <= _rc2.getRight()
				&& _rc1.getBottom() >= _rc2.getTop() && _rc1.getTop() <= _rc2.getBottom();
	}

	/**
	 * 判断点是否在矩形框内
	 * 
	 * @param _m
	 *            点
	 * @param _rc
	 *            矩形框（左小右大，顶小底大）
	 * @return TRUE：是，FALSE：否
	 */
	private boolean isPtInRect(SMatrix1_4 _m, Rect_ltrb _rc) {
		return _m.getX() >= _rc.getLeft() && _m.getX() <= _rc.getRight() && _m.getY() >= _rc.getTop()
				&& _m.getY() <= _rc.getBottom();
	}

	/**
	 * 判断圆弧与矩形框是否相交
	 * 
	 * @param _m1
	 *            圆弧起点
	 * @param _m2
	 *            圆弧中间点
	 * @param _m3
	 *            圆弧终点
	 * @param _rc
	 *            矩形框（左小右大，顶小底大）
	 * @return TRUE：是，false：否
	 */
	public boolean isArcInRect(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m3, Rect_ltrb _rc) {
		// SMatrix1_4 SMatrix1_4 = new SMatrix1_4();

		if (isPtInRect(_m1, _rc) || isPtInRect(_m2, _rc) || isPtInRect(_m3, _rc)) {
			return true;
		}

		SMatrix1_4 m0 = getCenterPt(_m1, _m2, _m3);
		double r = SMatrix1_4.operator_mod3(SMatrix1_4.operator_minus(m0, _m1));
		Rect_ltrb rc = new Rect_ltrb(m0.getX() - r, m0.getY() - r, m0.getX() + r, m0.getY() + r);
		if (isRectInRect(rc, _rc)) {
			double arcS = getArc(m0, _m1);
			double arcE = getArc(m0, _m3);
			if (isPtInLine(_m1, _m2, _m3) < 0) {// 顺时针弧
				double temp = arcS;
				arcS = arcE;
				arcE = temp;
			}
			SMatrix1_4 mlt = new SMatrix1_4(_rc.getLeft(), _rc.getTop());
			SMatrix1_4 mlb = new SMatrix1_4(_rc.getLeft(), _rc.getBottom());
			SMatrix1_4 mrt = new SMatrix1_4(_rc.getRight(), _rc.getTop());
			SMatrix1_4 mrb = new SMatrix1_4(_rc.getRight(), _rc.getBottom());

			double dl = getDistance(mlt, mlb, m0, false);
			double dr = getDistance(mrt, mrb, m0, false);
			double dt = getDistance(mlt, mrt, m0, false);
			double db = getDistance(mlb, mrb, m0, false);

			if (dl <= r) {// 矩形框左边与弧相交
				double dy = Math.sqrt(r * r - (_rc.getLeft() - m0.getX()) * (_rc.getLeft() - m0.getX()));
				SMatrix1_4 m = new SMatrix1_4(_rc.getLeft(), m0.getY() + dy);
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
				m.setY(m0.getY() - dy);
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
			}
			if (dt <= r) {// 矩形框顶边与弧相交
				double dx = Math.sqrt(r * r - (_rc.getTop() - m0.getY()) * (_rc.getTop() - m0.getY()));
				SMatrix1_4 m = new SMatrix1_4(m0.getX() + dx, _rc.getTop());
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
				m.setX(m0.getX() - dx);
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
			}
			if (dr <= r) // 矩形框右边与弧相交
			{
				double dy = Math.sqrt(r * r - (_rc.getRight() - m0.getX()) * (_rc.getRight() - m0.getX()));
				SMatrix1_4 m = new SMatrix1_4(_rc.getRight(), m0.getY() + dy);
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
				m.setY(m0.getY() - dy);
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
			}
			if (db <= r) // 矩形框底边与弧相交
			{
				double dx = Math.sqrt(r * r - (_rc.getBottom() - m0.getY()) * (_rc.getBottom() - m0.getY()));
				SMatrix1_4 m = new SMatrix1_4(m0.getX() + dx, _rc.getBottom());
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
				m.setX(m0.getX() - dx);
				if (isPtInRect(m, _rc)) {
					double arc = getArc(m0, m);
					if (isArcOnArc(arcS, arcE, arc)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 计算点与线段最短距离
	 * 
	 * @param _m1
	 *            线段起点
	 * @param _m2
	 *            线段终点
	 * @param _m
	 *            点
	 * @param _bLine
	 *            是否直线投影
	 * @return 点与线段最短距离
	 */
	private double getDistance(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m, boolean _bLine) {
		// SMatrix1_4 SMatrix1_4 = new SMatrix1_4();
		SMatrix1_4 m = SMatrix1_4.operator_minus(getProjection(_m1, _m2, _m, _bLine), _m);

		return SMatrix1_4.operator_mod3(m);
	}

	/**
	 * 计算点在线段的投影点
	 * 
	 * @param _m1
	 *            线段起点
	 * @param _m2
	 *            线段终点
	 * @param _m
	 *            点
	 * @param _bLine
	 *            是否直线投影
	 * @return 点在线段的投影点
	 */
	private static SMatrix1_4 getProjection(SMatrix1_4 _m1, SMatrix1_4 _m2, SMatrix1_4 _m, boolean _bLine) {
		SMatrix1_4 m10 = SMatrix1_4.operator_minus(_m, _m1);
		SMatrix1_4 m12 = SMatrix1_4.operator_minus(_m2, _m1);

		double f = SMatrix1_4.operator_dot3(m10, m12);
		double d = SMatrix1_4.operator_dot3(m12, m12);

		if (!_bLine) {
			if (f < 0) {
				return _m1;
			}
			if (f > d) {
				return _m2;
			}
		}
		SMatrix1_4 m = SMatrix1_4.operator_multify(m12, f);
		m = SMatrix1_4.operator_division(m, d);
		// _m1 + m12 * f / d;
		return SMatrix1_4.operator_plus(_m1, m);
	}

	/**
	 * 判断弧度是否在弧段内，按逆时针方向
	 * 
	 * @param _arcS
	 *            弧段起始弧度
	 * @param _arcE
	 *            弧段终止弧度
	 * @param _arc
	 * @return
	 */
	public static boolean isArcOnArc(double _arcS, double _arcE, double _arc) {
		if (_arcS < _arcE) {
			if (_arc >= _arcS && _arc <= _arcE) {
				return true;
			}
		} else {
			if (_arc >= _arcS || _arc <= _arcE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Title: Equal
	 * <p>
	 * Description: 判断值是否一致,误差不得大于2
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equal(int a, int b) {
		return Math.abs(a - b) > 2 ? false : true;
	}
}
