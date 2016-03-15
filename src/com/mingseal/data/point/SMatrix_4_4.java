package com.mingseal.data.point;


public class SMatrix_4_4 {
	double a11, a12, a13, a14;
	double a21, a22, a23, a24;
	double a31, a32, a33, a34;
	double a41, a42, a43, a44;

	public SMatrix_4_4() {
		super();
	}

	public SMatrix_4_4(double a11, double a12, double a13, double a14, double a21, double a22, double a23, double a24,
			double a31, double a32, double a33, double a34, double a41, double a42, double a43, double a44) {
		super();
		this.a11 = a11;
		this.a12 = a12;
		this.a13 = a13;
		this.a14 = a14;
		this.a21 = a21;
		this.a22 = a22;
		this.a23 = a23;
		this.a24 = a24;
		this.a31 = a31;
		this.a32 = a32;
		this.a33 = a33;
		this.a34 = a34;
		this.a41 = a41;
		this.a42 = a42;
		this.a43 = a43;
		this.a44 = a44;
	}

	/**
	 * 矩阵相乘
	 * 
	 * @param _n
	 * @param _m
	 * @return
	 */
	public SMatrix_4_4 operator_multiply(SMatrix_4_4 _n, SMatrix_4_4 _m) {

		SMatrix_4_4 m = new SMatrix_4_4();

		m.a11 = _n.a11 * _m.a11 + _n.a12 * _m.a21 + _n.a13 * _m.a31 + _n.a14 * _m.a41;
		m.a12 = _n.a11 * _m.a12 + _n.a12 * _m.a22 + _n.a13 * _m.a32 + _n.a14 * _m.a42;
		m.a13 = _n.a11 * _m.a13 + _n.a12 * _m.a23 + _n.a13 * _m.a33 + _n.a14 * _m.a43;
		m.a14 = _n.a11 * _m.a14 + _n.a12 * _m.a24 + _n.a13 * _m.a34 + _n.a14 * _m.a44;

		m.a21 = _n.a21 * _m.a11 + _n.a22 * _m.a21 + _n.a23 * _m.a31 + _n.a24 * _m.a41;
		m.a22 = _n.a21 * _m.a12 + _n.a22 * _m.a22 + _n.a23 * _m.a32 + _n.a24 * _m.a42;
		m.a23 = _n.a21 * _m.a13 + _n.a22 * _m.a23 + _n.a23 * _m.a33 + _n.a24 * _m.a43;
		m.a24 = _n.a21 * _m.a14 + _n.a22 * _m.a24 + _n.a23 * _m.a34 + _n.a24 * _m.a44;

		m.a31 = _n.a31 * _m.a11 + _n.a32 * _m.a21 + _n.a33 * _m.a31 + _n.a34 * _m.a41;
		m.a32 = _n.a31 * _m.a12 + _n.a32 * _m.a22 + _n.a33 * _m.a32 + _n.a34 * _m.a42;
		m.a33 = _n.a31 * _m.a13 + _n.a32 * _m.a23 + _n.a33 * _m.a33 + _n.a34 * _m.a43;
		m.a34 = _n.a31 * _m.a14 + _n.a32 * _m.a24 + _n.a33 * _m.a34 + _n.a34 * _m.a44;

		m.a41 = _n.a41 * _m.a11 + _n.a42 * _m.a21 + _n.a43 * _m.a31 + _n.a44 * _m.a41;
		m.a42 = _n.a41 * _m.a12 + _n.a42 * _m.a22 + _n.a43 * _m.a32 + _n.a44 * _m.a42;
		m.a43 = _n.a41 * _m.a13 + _n.a42 * _m.a23 + _n.a43 * _m.a33 + _n.a44 * _m.a43;
		m.a44 = _n.a41 * _m.a14 + _n.a42 * _m.a24 + _n.a43 * _m.a34 + _n.a44 * _m.a44;

		return m;
	}

	/**
	 * 矩阵*数字
	 * 
	 * @param _m
	 * @param num
	 * @return
	 */
	public SMatrix_4_4 operator_multiply(SMatrix_4_4 _m, double num) {

		SMatrix_4_4 m = new SMatrix_4_4();
		m.a11 = _m.a11 * num;
		m.a12 = _m.a12 * num;
		m.a13 = _m.a13 * num;
		m.a14 = _m.a14 * num;

		m.a21 = _m.a21 * num;
		m.a22 = _m.a22 * num;
		m.a23 = _m.a23 * num;
		m.a24 = _m.a24 * num;

		m.a31 = _m.a31 * num;
		m.a32 = _m.a32 * num;
		m.a33 = _m.a33 * num;
		m.a34 = _m.a34 * num;

		m.a41 = _m.a41 * num;
		m.a42 = _m.a42 * num;
		m.a43 = _m.a43 * num;
		m.a44 = _m.a44 * num;

		return m;
	}

	/**
	 * 矩阵相加
	 * 
	 * @param _m
	 * @param _m1
	 * @return
	 */
	public SMatrix_4_4 operator_plus(SMatrix_4_4 _m, SMatrix_4_4 _m1) {
		SMatrix_4_4 m = new SMatrix_4_4();

		m.a11 = _m.a11 + _m1.a11;
		m.a12 = _m.a12 + _m1.a12;
		m.a13 = _m.a13 + _m1.a13;
		m.a14 = _m.a14 + _m1.a14;

		m.a21 = _m.a21 + _m1.a21;
		m.a22 = _m.a22 + _m1.a22;
		m.a23 = _m.a23 + _m1.a23;
		m.a24 = _m.a24 + _m1.a24;

		m.a31 = _m.a31 + _m1.a31;
		m.a32 = _m.a32 + _m1.a32;
		m.a33 = _m.a33 + _m1.a33;
		m.a34 = _m.a34 + _m1.a34;

		m.a41 = _m.a41 + _m1.a41;
		m.a42 = _m.a42 + _m1.a42;
		m.a43 = _m.a43 + _m1.a43;
		m.a44 = _m.a44 + _m1.a44;

		return m;
	}

	/**
	 * 矩阵自减
	 * 
	 * @param _m
	 * @return
	 */
	public SMatrix_4_4 operator_minus(SMatrix_4_4 _m) {
		SMatrix_4_4 m = new SMatrix_4_4();
		m.a11 = -_m.a11;
		m.a12 = -_m.a12;
		m.a13 = -_m.a13;
		m.a14 = -_m.a14;

		m.a21 = -_m.a21;
		m.a22 = -_m.a22;
		m.a23 = -_m.a23;
		m.a24 = -_m.a24;

		m.a31 = -_m.a31;
		m.a32 = -_m.a32;
		m.a33 = -_m.a33;
		m.a34 = -_m.a34;

		m.a41 = -_m.a41;
		m.a42 = -_m.a42;
		m.a43 = -_m.a43;
		m.a44 = -_m.a44;

		return m;
	}

	/**
	 * 矩阵是否相等
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public boolean operator_equal(SMatrix_4_4 _m, SMatrix_4_4 _n) {
		return _m.a11 == _n.a11 && _m.a12 == _n.a12 && _m.a13 == _n.a13 && _m.a14 == _n.a14 && _m.a21 == _n.a21
				&& _m.a22 == _n.a22 && _m.a23 == _n.a23 && _m.a24 == _n.a24 && _m.a31 == _n.a31 && _m.a32 == _n.a32
				&& _m.a33 == _n.a33 && _m.a34 == _n.a34 && _m.a41 == _n.a41 && _m.a42 == _n.a42 && _m.a43 == _n.a43
				&& _m.a44 == _n.a44;

	}

	/**
	 * 矩阵除法
	 * 
	 * @param _m
	 * @param _num
	 * @return
	 */
	public SMatrix_4_4 operator_division(SMatrix_4_4 _m, double _num) {

		return operator_multiply(_m, 1 / _num);

	}

	/**
	 * 矩阵相减
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public SMatrix_4_4 operator_minus(SMatrix_4_4 _m, SMatrix_4_4 _n) {
		SMatrix_4_4 m = new SMatrix_4_4();

		m.a11 = _m.a11 - _n.a11;
		m.a12 = _m.a12 - _n.a12;
		m.a13 = _m.a13 - _n.a13;
		m.a14 = _m.a14 - _n.a14;

		m.a21 = _m.a21 - _n.a21;
		m.a22 = _m.a22 - _n.a22;
		m.a23 = _m.a23 - _n.a23;
		m.a24 = _m.a24 - _n.a24;

		m.a31 = _m.a31 - _n.a31;
		m.a32 = _m.a32 - _n.a32;
		m.a33 = _m.a33 - _n.a33;
		m.a34 = _m.a34 - _n.a34;

		m.a41 = _m.a41 - _n.a41;
		m.a42 = _m.a42 - _n.a42;
		m.a43 = _m.a43 - _n.a43;
		m.a44 = _m.a44 - _n.a44;

		return m;

	}

	/**
	 * 判断矩阵是否不相等
	 * 
	 * @param _m
	 * @param _n
	 * @return
	 */
	public boolean operator_not_equal(SMatrix_4_4 _m, SMatrix_4_4 _n) {
		return !(operator_equal(_m, _n));
	}

}