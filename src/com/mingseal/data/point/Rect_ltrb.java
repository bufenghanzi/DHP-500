/**
 * 
 */
package com.mingseal.data.point;

/**
 * @author 商炎炳
 * @description 矩形框
 */
public class Rect_ltrb {
	private double left;
	private double top;
	private double right;
	private double bottom;

	public Rect_ltrb() {
		super();
	}

	public Rect_ltrb(double left, double top, double right, double bottom) {
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public double getLeft() {
		return left;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getTop() {
		return top;
	}

	public void setTop(double top) {
		this.top = top;
	}

	public double getRight() {
		return right;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public double getBottom() {
		return bottom;
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

}
