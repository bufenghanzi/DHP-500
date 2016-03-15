/**
 * 
 */
package com.mingseal.data.param;

import com.mingseal.data.point.SMatrix1_4;

/**
 * @title 阵列参数
 * 
 * @author 商炎炳
 *
 */
public class ArrayParam {
	private int row;// 行数
	private int col;// 列数
	private SMatrix1_4 mx;// x方向偏移
	private SMatrix1_4 my;// y方向偏移
	private SMatrix1_4 me;// 终点坐标
	private boolean bStartDirY;// 是否起始y方向
	private boolean bSType;// 是否S型阵列
	private boolean bSort;// 是否优化阵列

	/**
	 * 初始化了三个Smatrix1_4
	 */
	public ArrayParam() {
		super();
		this.mx = new SMatrix1_4();
		this.my = new SMatrix1_4();
		this.me = new SMatrix1_4();
	}

	/**
	 * @param row
	 *            行数
	 * @param col
	 *            列数
	 * @param mx
	 *            x方向偏移
	 * @param my
	 *            y方向偏移
	 * @param me
	 *            终点坐标
	 * @param bStartDirY
	 *            是否起始y方向
	 * @param bSType
	 *            是否S型阵列
	 * @param bSort
	 *            是否优化阵列
	 */
	public ArrayParam(int row, int col, SMatrix1_4 mx, SMatrix1_4 my, SMatrix1_4 me, boolean bStartDirY, boolean bSType,
			boolean bSort) {
		super();
		this.row = row;
		this.col = col;
		this.mx = mx;
		this.my = my;
		this.me = me;
		this.bStartDirY = bStartDirY;
		this.bSType = bSType;
		this.bSort = bSort;
	}

	/**
	 * @return 行数
	 */
	public int getRow() {
		return row;
	}

	/**
	 * 设置行数
	 * 
	 * @param row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return 列数
	 */
	public int getCol() {
		return col;
	}

	/**
	 * 设置列数
	 * 
	 * @param col
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * @return x方向偏移
	 */
	public SMatrix1_4 getMx() {
		return mx;
	}

	/**
	 * 设置x方向偏移
	 * 
	 * @param mx
	 */
	public void setMx(SMatrix1_4 mx) {
		this.mx = mx;
	}

	/**
	 * @return y方向偏移
	 */
	public SMatrix1_4 getMy() {
		return my;
	}

	/**
	 * 设置y方向偏移
	 * 
	 * @param my
	 */
	public void setMy(SMatrix1_4 my) {
		this.my = my;
	}

	/**
	 * @return 终点坐标
	 */
	public SMatrix1_4 getMe() {
		return me;
	}

	/**
	 * 设置终点坐标
	 * 
	 * @param me
	 */
	public void setMe(SMatrix1_4 me) {
		this.me = me;
	}

	/**
	 * @return 是否起始y方向(true 为是)
	 */
	public boolean isbStartDirY() {
		return bStartDirY;
	}

	/**
	 * 设置起始y方向
	 * 
	 * @param bStartDirY
	 */
	public void setbStartDirY(boolean bStartDirY) {
		this.bStartDirY = bStartDirY;
	}

	/**
	 * @return 是否S型阵列
	 */
	public boolean isbSType() {
		return bSType;
	}

	/**
	 * 设置是否为S型阵列
	 * 
	 * @param bSType
	 */
	public void setbSType(boolean bSType) {
		this.bSType = bSType;
	}

	/**
	 * @return 是否优化阵列
	 */
	public boolean isbSort() {
		return bSort;
	}

	/**
	 * 设置优化阵列
	 * 
	 * @param bSort
	 */
	public void setbSort(boolean bSort) {
		this.bSort = bSort;
	}

	@Override
	public String toString() {
		return "ArrayParam [row=" + row + ", col=" + col + ", mx=" + mx + ", my=" + my + ", me=" + me + ", bStartDirY="
				+ bStartDirY + ", bSType=" + bSType + ", bSort=" + bSort + "]";
	}

}
