package com.mingseal.data.point;

import java.util.HashMap;

import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueBaseParam;
import com.mingseal.data.point.glueparam.PointGlueClearIOParam;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueLineArcParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
import com.mingseal.data.point.weldparam.PointWeldAloneParam;
import com.mingseal.data.point.weldparam.PointWeldBaseParam;
import com.mingseal.data.point.weldparam.PointWeldBlowParam;
import com.mingseal.data.point.weldparam.PointWeldFaceEndParam;
import com.mingseal.data.point.weldparam.PointWeldFaceStartParam;
import com.mingseal.data.point.weldparam.PointWeldLineArcParam;
import com.mingseal.data.point.weldparam.PointWeldLineEndParam;
import com.mingseal.data.point.weldparam.PointWeldLineMidParam;
import com.mingseal.data.point.weldparam.PointWeldLineStartParam;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.SparseArrayCompat;

/**
 * 点类
 * 
 * @author lyq
 */
public class Point implements Parcelable {
	private int id;// 主键
	private int x;
	private int y;
	private int z;
	private int u;

	private PointParam pointParam; // 点参数
	
	/*=================== begin ===================*/
	static HashMap<PointType,PointParam>	cachesPointParam	= new HashMap<PointType,PointParam>();
	/*===================  add  ===================*/

	/**
	 * 只用于初始化一个Point，不能用作点类型的添加
	 */
	/*
	 * public Point() { super(); }
	 */

	/**
	 * 点类构造函数,坐标默认为原点
	 * 
	 * @param pointType
	 *            点类型
	 */
	public Point(PointType pointType) {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.u = 0;
		ParamFactory(pointType);
	}

	/**
	 * 点类构造函数
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param u
	 * @param pointType
	 *            点类型
	 */
	public Point(int x, int y, int z, int u, PointType pointType) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		ParamFactory(pointType);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getU() {
		return u;
	}

	public void setU(int u) {
		this.u = u;
	}

	/**
	 * 获取倍数之后的X
	 * 
	 * @param fold
	 * @return 获取倍数之后的X
	 */
	public int getFoldX(int fold) {
		return this.x / fold;
	}

	/**
	 * 获取倍数之后的Y
	 * 
	 * @param fold
	 * @return 获取倍数之后的Y
	 */
	public int getFoldY(int fold) {
		return this.y / fold;
	}

	/**
	 * 获取倍数之后的Z
	 * 
	 * @param fold
	 * @return 获取倍数之后的Z
	 */
	public int getFoldZ(int fold) {
		return this.z / fold;
	}

	/**
	 * 获取倍数之后的U
	 * 
	 * @param fold
	 * @return 获取倍数之后的U
	 */
	public int getFoldU(int fold) {
		return this.u / fold;
	}

	/**
	 * @return 获取点参数
	 */
	public PointParam getPointParam() {
		return pointParam;
	}

	/**
	 * 设置点参数
	 * 
	 * @param pointParam
	 *            点参数
	 */
	public void setPointParam(PointParam pointParam) {
		this.pointParam = pointParam;
	}

	@Override
	public String toString() {
		return "Point [id=" + id + ", x=" + x + ", y=" + y + ", z=" + z + ", u=" + u + ", pointParam=" + pointParam
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pointParam == null) ? 0 : pointParam.hashCode());
		result = prime * result + u;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (pointParam == null) {
			if (other.pointParam != null)
				return false;
		} else if (!pointParam.equals(other.pointParam))
			return false;
		if (u != other.u)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	/**
	 * 参数工厂
	 * 
	 * @param pointType
	 *            点类型
	 */
	private void ParamFactory(PointType pointType) {
		/*=================== begin ===================*/
		// 如果缓存里面有对应的fragment,就直接取出返回
		PointParam tempPointParam = cachesPointParam.get(pointType);
		if (tempPointParam != null) {
			this.pointParam = tempPointParam;
		}
		/*===================  add  ===================*/
		switch (pointType) {
		case POINT_GLUE_BASE:
			this.pointParam = new PointGlueBaseParam();
			break;
		case POINT_GLUE_ALONE:
			this.pointParam = new PointGlueAloneParam();
			break;
		case POINT_GLUE_LINE_START:
			this.pointParam = new PointGlueLineStartParam();
			break;
		case POINT_GLUE_LINE_MID:
			this.pointParam = new PointGlueLineMidParam();
			break;
		case POINT_GLUE_LINE_ARC:
			this.pointParam = new PointGlueLineArcParam();
			break;
		case POINT_GLUE_LINE_END:
			this.pointParam = new PointGlueLineEndParam();
			break;
		case POINT_GLUE_FACE_START:
			this.pointParam = new PointGlueFaceStartParam();
			break;
		case POINT_GLUE_FACE_END:
			this.pointParam = new PointGlueFaceEndParam();
			break;
		case POINT_GLUE_INPUT:
			this.pointParam = new PointGlueInputIOParam();
			break;
		case POINT_GLUE_OUTPUT:
			this.pointParam = new PointGlueOutputIOParam();
			break;
		case POINT_GLUE_CLEAR:
			this.pointParam = new PointGlueClearParam();
			break;
		case POINT_GLUE_CLEARIO:
			this.pointParam = new PointGlueClearIOParam();
			break;

		case POINT_WELD_BASE:
			this.pointParam = new PointWeldBaseParam();
			break;
		case POINT_WELD_ALONE:
			this.pointParam = new PointWeldAloneParam();
			break;
		// case POINT_WELD_WORK:
		// this.pointParam = new PointWeldWork();
		// break;
		case POINT_WELD_LINE_START:
			this.pointParam = new PointWeldLineStartParam();
			break;
		case POINT_WELD_LINE_MID:
			this.pointParam = new PointWeldLineMidParam();
			break;
		case POINT_WELD_LINE_ARC:
			this.pointParam = new PointWeldLineArcParam();
			break;
		case POINT_WELD_LINE_END:
			this.pointParam = new PointWeldLineEndParam();
			break;
		case POINT_WELD_FACE_START:
			this.pointParam = new PointWeldFaceStartParam();
			break;
		case POINT_WELD_FACE_END:
			this.pointParam = new PointWeldFaceEndParam();
			break;
		case POINT_WELD_BLOW:
			this.pointParam = new PointWeldBlowParam();
			break;
		default:
			this.pointParam = new PointParam();
			break;
		}
		/*=================== begin ===================*/
		// 保存对应的fragment
		cachesPointParam.put(pointType, pointParam);
		/*===================  add  ===================*/
	}

	/**
	 * 
	 * @author 商炎炳
	 */
	public static final Parcelable.Creator<Point> CREATOR = new Creator<Point>() {

		@Override
		public Point[] newArray(int size) {
			return new Point[size];
		}

		@Override
		public Point createFromParcel(Parcel source) {
			Point point = new Point(PointType.POINT_GLUE_ALONE);
			point.x = source.readInt();
			point.y = source.readInt();
			point.z = source.readInt();
			point.u = source.readInt();
			point.pointParam = source.readParcelable(PointParam.class.getClassLoader());

			return point;
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(z);
		dest.writeInt(u);
		dest.writeParcelable(pointParam, flags);
	}

}
