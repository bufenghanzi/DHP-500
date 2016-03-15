/**
 * 
 */
package com.mingseal.data.point;

/**
 * @author 商炎炳
 *
 */
public class SMatrix1_4 {
	private double x;
	private double y;
	private double z;
	private double u;

	/**
	 * 将Point转换成double保存到一个对象中
	 * 
	 * @param point
	 */
	public SMatrix1_4(Point point) {
		this.x = point.getX();
		this.y = point.getY();
		this.z = point.getZ();
		this.u = point.getU();
	}

	/**
	 * 无参构造函数
	 * <p>
	 * x = 0.0 , y = 0.0 , z = 0.0 , u = 1.0
	 */
	public SMatrix1_4() {
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
		this.u = 1.0;
	}

	public SMatrix1_4(double x, double y, double z, double u) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 *            u = 1.0
	 */
	public SMatrix1_4(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = 1.0;
	}

	/**
	 * @param x
	 * @param y
	 *            z = 0.0 u = 1.0
	 */
	public SMatrix1_4(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.z = 0.0;
		this.u = 1.0;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getU() {
		return u;
	}

	public void setU(double u) {
		this.u = u;
	}

	/**
	 * 获取倍数之后的X
	 * 
	 * @param fold
	 * @return 获取倍数之后的X
	 */
	public float getFoldX(int fold) {
		return (float) (this.x / fold);
	}

	/**
	 * 获取倍数之后的Y
	 * 
	 * @param fold
	 * @return 获取倍数之后的Y
	 */
	public float getFoldY(int fold) {
		return (float) (this.y / fold);
	}

	/**
	 * 获取倍数之后的Z
	 * 
	 * @param fold
	 * @return 获取倍数之后的Z
	 */
	public float getFoldZ(int fold) {
		return (float) (this.z / fold);
	}

	/**
	 * 获取倍数之后的U
	 * 
	 * @param fold
	 * @return 获取倍数之后的U
	 */
	public float getFoldU(int fold) {
		return (float) (this.u / fold);
	}

	@Override
	public String toString() {
		return "SMatrix1_4 [x=" + x + ", y=" + y + ", z=" + z + ", u=" + u + "]";
	}

	/**
	 * 1*4矩阵_m(-)1*4矩阵_n
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static SMatrix1_4 operator_minus(SMatrix1_4 _m, SMatrix1_4 _n) {
		SMatrix1_4 m = new SMatrix1_4();

		m.x = _m.x - _n.x;
		m.y = _m.y - _n.y;
		m.z = _m.z - _n.z;
		m.u = _m.u - _n.u;

		return m;
	}

	/**
	 * 1*4矩阵自减，取他的负值
	 * 
	 * @param _m
	 * @return
	 */
	public static SMatrix1_4 operator_minus(SMatrix1_4 _m) {
		SMatrix1_4 m = new SMatrix1_4();

		m.x = -_m.x;
		m.y = -_m.y;
		m.z = -_m.z;
		m.u = -_m.u;

		return m;
	}

	/**
	 * x,y取模
	 * 
	 * @param _m
	 * @return
	 */
	public static double operator_mod2(SMatrix1_4 _m) {
		return Math.sqrt(_m.x * _m.x + _m.y * _m.y);
	}

	/**
	 * x,y,z取模
	 * 
	 * @param _m
	 * @return
	 */
	public static double operator_mod3(SMatrix1_4 _m) {
		return Math.sqrt(_m.x * _m.x + _m.y * _m.y + _m.z * _m.z);
	}

	/**
	 * x,y点乘
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static double operator_dot2(SMatrix1_4 _m, SMatrix1_4 _n) {
		return _m.x * _n.x + _m.y * _n.y;
	}

	/**
	 * x,y,z点乘
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static double operator_dot3(SMatrix1_4 _m, SMatrix1_4 _n) {
		return _m.x * _n.x + _m.y * _n.y + _m.z * _n.z;
	}

	/**
	 * 1*4矩阵（*）double类型数
	 * 
	 * @param _m
	 * @param _num
	 * @return
	 */
	public static SMatrix1_4 operator_multify(SMatrix1_4 _m, double _num) {
		SMatrix1_4 m = new SMatrix1_4();

		m.x = _m.x * _num;
		m.y = _m.y * _num;
		m.z = _m.z * _num;
		m.u = _m.u * _num;

		return m;
	}

	/**
	 * 1*4矩阵（/）double数
	 * 
	 * @param _m
	 * @param _num
	 * @return
	 */
	public static SMatrix1_4 operator_division(SMatrix1_4 _m, double _num) {
		return operator_multify(_m, (1.0 / _num));
	}

	/**
	 * 1*4矩阵（+）1*4矩阵
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static SMatrix1_4 operator_plus(SMatrix1_4 _m, SMatrix1_4 _n) {
		SMatrix1_4 m = new SMatrix1_4();

		m.x = _m.x + _n.x;
		m.y = _m.y + _n.y;
		m.z = _m.z + _n.z;
		m.u = _m.u + _n.u;

		return m;
	}

	/**
	 * x,y,z叉乘
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static SMatrix1_4 operator_cross3(SMatrix1_4 _m, SMatrix1_4 _n) {
		SMatrix1_4 m = new SMatrix1_4();
		m.x = _m.y * _n.z - _m.z * _n.y;
		m.y = _m.z * _n.x - _m.x * _n.z;
		m.z = _m.x * _n.y - _m.y * _n.x;
		m.u = 1.0;

		return m;
	}

	/**
	 * x,y单元化
	 * 
	 * @param _m
	 */
	public static void operator_normalize2(SMatrix1_4 _m) {
		double nm = operator_mod2(_m);
		if (nm != 0) {
			_m.x /= nm;
			_m.y /= nm;
		}

	}

	/**
	 * x,y,z单元化
	 * 
	 * @param _m
	 */
	public static SMatrix1_4 operator_normalize3(SMatrix1_4 _m) {
		double nm = operator_mod3(_m);
		if (nm != 0) {
			_m.x /= nm;
			_m.y /= nm;
			_m.z /= nm;
		}

		return _m;
	}

	/**
	 * 1*4矩阵 （*） 4*4的矩阵
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static SMatrix1_4 operator_multify(SMatrix1_4 _m, SMatrix_4_4 _n) {
		SMatrix1_4 m = new SMatrix1_4();

		m.x = _m.x * _n.a11 + _m.y * _n.a21 + _m.z * _n.a31 + _m.u * _n.a41;
		m.y = _m.x * _n.a12 + _m.y * _n.a22 + _m.z * _n.a32 + _m.u * _n.a42;
		m.z = _m.x * _n.a13 + _m.y * _n.a23 + _m.z * _n.a33 + _m.u * _n.a43;
		m.u = _m.x * _n.a14 + _m.y * _n.a24 + _m.z * _n.a34 + _m.u * _n.a44;

		return m;
	}

	/**
	 * x,y叉乘
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static double operator_cross2(SMatrix1_4 _m, SMatrix1_4 _n) {
		return _m.x * _n.y - _m.y * _n.x;
	}

	/**
	 * 判断两个1*4矩阵是否相等
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static boolean operator_equal(SMatrix1_4 _m, SMatrix1_4 _n) {
		return _m.x == _n.x && _m.y == _n.y && _m.z == _n.z && _m.u == _n.u;
	}

	/**
	 * 判断两个矩阵是否不相等
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public static boolean operator_not_equal(SMatrix1_4 _m, SMatrix1_4 _n) {
		return !(operator_equal(_m, _n));
	}

}
