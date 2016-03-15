/**
 * 
 */
package com.mingseal.view;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.param.robot.RobotParam;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.SMatrix1_4;
import com.mingseal.utils.CommonArithmetic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;

/**
 * 视图界面里的视图
 * 
 * @author 商炎炳
 *
 */
public class CustomView extends View {
	private static String TAG = "CustomView";
	private int backgroundColor = Color.rgb(255, 255, 255);// 背景色
	private int lineColor = Color.argb(100, 255, 0, 0);// 绘制线的颜色
	private int assignColor = Color.rgb(0, 255, 255);// 指定点的颜色
	private int basePointColor = Color.rgb(0, 0, 0);// 起始点的颜色
	private int alonePointColor = Color.rgb(255, 242, 0);// 独立点的颜色
	private int pointColor = Color.argb(180, 255, 0, 0);// 绘制点的颜色
	private int arcColor = Color.argb(150, 255, 0, 0);// 绘制弧线的颜色
	private int wrongColor = Color.argb(255, 25, 226, 91);// 绘制错误线的颜色
	private int wrongFaceColor = Color.argb(100, 25, 226, 91);// 绘制错误线的颜色
	private int borderColor = Color.rgb(180, 180, 180);//边框颜色
	private int radius = 5;// 半径
	private int fold;// 放大或缩小的倍数
	private Paint paint;

	private Point point_first, point_second, point_third;
	private int left, top, right, bottom;

	private float scaleXY = 1.0f;// 放大缩放比例
	private float distanceX = 0;// 向x平移的量
	private float distanceY = 0;// 向y平移的量
	private GestureDetector mGestureDetector;
	private ScaleGestureDetector mScaleDetector;

	private List<Point> points;
	private Point point;
	private int assignPosition = 0;// 指定的任务点的编号

	/**
	 * Activity设置任务点
	 * 
	 * @param points
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
		invalidate();
	}

	/**
	 * 设置当前选中的是哪一个点
	 * 
	 * @param _assignPosition
	 *            1为下一个点,-1为上一个点
	 */
	public void setAssignPosition(int _assignPosition) {
		if (points.size() > 0) {
			assignPosition = assignPosition + _assignPosition;
			if (assignPosition < 0) {
				assignPosition = 0;
			} else if (assignPosition >= points.size()) {
				assignPosition = points.size() - 1;
			}
			invalidate();
		}
	}

	/**
	 * 获得当前任务点的position
	 * 
	 * @return 当前任务点的position
	 */
	public int getAssignPosition() {
		return assignPosition;
	}

	/**
	 * 获得当前任务点
	 * 
	 * @return 当前任务点
	 */
	public Point getPointByAssignPosition() {
		return points.get(assignPosition);
	}

	/**
	 * 设置缩放比例
	 * 
	 * @param _scaleXY
	 */
	public void setScaleXY(float _scaleXY) {
		scaleXY = scaleXY * _scaleXY;
		if (scaleXY > 2) {
			scaleXY = 2;
		} else if (scaleXY < 1) {
			scaleXY = 1;
		}

		Log.d(TAG, "scaleXY:" + scaleXY + ",_scaleXY:" + _scaleXY);

		invalidate();
	}

	/**
	 * 同时设置X,Y的偏移量
	 * 
	 * @param _distanceX
	 * @param _distanceY
	 */
	public void setDistance(float _distanceX, float _distanceY) {
		distanceX = distanceX + _distanceX;
		distanceY = distanceY + _distanceY;
		//设置区间
//		if (distanceX > getWidth() / scaleXY / 2) {
//			distanceX = getWidth() / scaleXY / 2;
//		} else if (distanceY > getHeight() / scaleXY / 2) {
//			distanceY = getHeight() / scaleXY / 2;
//		} else if (distanceX < -getWidth() / scaleXY / 3) {// 往左不超过1/3
//			distanceX = -getWidth() / scaleXY / 3;
//		} else if (distanceY < -getHeight() / scaleXY / 4) {// 往上不超过1/4
//			distanceY = -getHeight() / scaleXY / 4;
//		}
		Log.i(TAG, "width:" + getWidth() + "-height:" + getHeight());
		Log.d(TAG, "this.distanceX:" + distanceX + ",distanceX:" + _distanceX + ",this.distanceY:" + distanceY
				+ ",distanceY:" + _distanceY);
		invalidate();
	}

	public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 */
	private void init(Context context) {
		paint = new Paint();
		points = new ArrayList<Point>();

		mGestureDetector = new GestureDetector(context, new myGestureListener());
		mScaleDetector = new ScaleGestureDetector(context, new myScaleGestureListener());

		fold = 50;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		Log.d(TAG, "measuredHeight:" + measuredHeight + ",measuredWidth:" + measuredWidth);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	/**
	 * 测量宽度
	 * 
	 * @param measureSpec
	 * @return
	 */
	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		int result = 0;

		if (specMode == MeasureSpec.AT_MOST) {
			result = getWidth();
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	/**
	 * 测量高度
	 * 
	 * @param measureSpec
	 * @return
	 */
	private int measureHeight(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		int result = 0;

		if (specMode == MeasureSpec.AT_MOST) {

			result = getHeight();
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	/**
	 * 两个参数进行比较
	 * 
	 * @param num1
	 * @param num2
	 * @return value[0]是大的数，values[1]是小的数
	 */
	private float[] compareMaxValue(float num1, float num2) {
		float[] value = new float[2];
		if (num1 > num2) {
			value[0] = num1;
			value[1] = num2;
		} else {
			value[0] = num2;
			value[1] = num1;
		}
		return value;
	}

	/**
	 * 获得某一个点的点参数的点类型
	 * 
	 * @param point
	 * @return point.getPointParam().getPointType()
	 */
	private PointType getPointType(Point point) {
		return point.getPointParam().getPointType();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		clearCanvas(canvas);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);
		canvas.scale(scaleXY, scaleXY);
		canvas.translate(distanceX, distanceY);
		
		double xDiff = RobotParam.INSTANCE.GetXDifferentiate();
		float xMax = (float) ((double)(RobotParam.INSTANCE.GetXJourney()/xDiff)/fold);
		//画框
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(borderColor);
		canvas.drawRect(new RectF(0, 0, xMax, xMax), paint);
		
		// 画出所有点
		for (Point point : points) {

			paint.setColor(pointColor);
			paint.setStyle(Paint.Style.FILL);
			// 绘图区域在900*900的画布中，取中间的800*800为显示绘图区域
			canvas.drawCircle(point.getFoldX(fold), point.getFoldY(fold), radius, paint);
		}
		boolean faceStartFlag = false;
		// 先画出所有的线
		for (int i = 0; i < points.size() - 1; i++) {

			point_first = points.get(i);
			point_second = points.get(i + 1);

			if (getPointType(point_first).equals(PointType.POINT_GLUE_BASE)) {
				// 基准点，单独画
				paint.setColor(basePointColor);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(point_first.getFoldX(fold), point_first.getFoldY(fold), radius, paint);
			} else if (getPointType(point_first).equals(PointType.POINT_GLUE_ALONE)) {
				// 如果是独立点，不需要画线段
				paint.setColor(pointColor);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawCircle(point_first.getFoldX(fold), point_first.getFoldY(fold), radius, paint);
			} else if (getPointType(point_first).equals(PointType.POINT_GLUE_FACE_START)) {
				// 面结束点紧跟面起点
				if (getPointType(point_second).equals(PointType.POINT_GLUE_FACE_END)) {

					paint.setColor(lineColor);

					faceStartFlag = false;
					paint.setStyle(Paint.Style.FILL);

					left = (int) compareMaxValue(point_first.getFoldX(fold), point_second.getFoldX(fold))[1];
					top = (int) compareMaxValue(point_first.getFoldY(fold), point_second.getFoldY(fold))[1];
					right = (int) compareMaxValue(point_first.getFoldX(fold), point_second.getFoldX(fold))[0];
					bottom = (int) compareMaxValue(point_first.getFoldY(fold), point_second.getFoldY(fold))[0];

					RectF rectF = new RectF(left, top, right, bottom);
					// 在目标图像的顶部绘制源图像
					// paint.setXfermode(new
					// PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
					canvas.drawRect(rectF, paint);
					// paint.setXfermode(null);
					i = i + 1;
				}

			} else if (getPointType(point_first).equals(PointType.POINT_GLUE_LINE_START)) {
				for (int j = i + 1; j < points.size(); j++) {
					if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_ARC)) {
						// 圆弧点，画弧
						// 圆弧点
						paint.setColor(arcColor);
						paint.setStyle(Paint.Style.STROKE);
						point_third = points.get(i + 2);

						SMatrix1_4 mp1 = new SMatrix1_4(points.get(j - 1));
						SMatrix1_4 mp2 = new SMatrix1_4(points.get(j));
						SMatrix1_4 mp3 = new SMatrix1_4(points.get(j + 1));
						SMatrix1_4 mp = CommonArithmetic.getCenterPt(mp1, mp2, mp3);
						SMatrix1_4 m = SMatrix1_4.operator_minus(mp, mp1);
						Log.d(TAG, "SMatrix1_4:mp->" + mp.toString() + ",mp2:" + mp2.toString() + ",mp3:"
								+ mp3.toString());

						double r = SMatrix1_4.operator_mod3(m);

						double arcLeft = (mp.getX() - r) / fold;// 放大倍数显示在预览上
						double arcTop = (mp.getY() - r) / fold;
						double arcRight = (mp.getX() + r) / fold;
						double arcBottom = (mp.getY() + r) / fold;

						double start = CommonArithmetic.getAngle(mp, mp1);
						double mid = CommonArithmetic.getAngle(mp, mp2);
						double end = CommonArithmetic.getAngle(mp, mp3);

						Log.d(TAG, "r:" + r + ",arcLeft:" + arcLeft + ",arcTop:" + arcTop + ",arcRight:" + arcRight
								+ ",arcBottom:" + arcBottom);
						double startAngle, sweepAngle;

						if (start < mid && mid < end) {
							startAngle = start;
							sweepAngle = end - start;
						} else if (start < end && end < mid) {
							startAngle = end;
							sweepAngle = 360 - (end - start);
						} else if (mid < start && start < end) {
							startAngle = end;
							sweepAngle = 360 - (end - start);
						} else if (mid < end && end < start) {
							startAngle = start;
							sweepAngle = 360 - (start - end);
						} else if (end < mid && mid < start) {
							startAngle = end;
							sweepAngle = start - end;
						} else {
							startAngle = start;
							sweepAngle = 360 - (start - end);
						}

						RectF oval = new RectF((float) arcLeft, (float) arcTop, (float) arcRight, (float) arcBottom);
						canvas.drawArc(oval, (float) startAngle, (float) sweepAngle, false, paint);

						j = j + 1;
						i = j;
					} else if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_END)) {
						paint.setColor(lineColor);
						canvas.drawLine(points.get(j - 1).getFoldX(fold), points.get(j - 1).getFoldY(fold),
								points.get(j).getFoldX(fold), points.get(j).getFoldY(fold), paint);
						i = j;
						break;
					} else if (getPointType(points.get(j)).equals(PointType.POINT_GLUE_LINE_MID)) {
						paint.setColor(lineColor);
						canvas.drawLine(points.get(j - 1).getFoldX(fold), points.get(j - 1).getFoldY(fold),
								points.get(j).getFoldX(fold), points.get(j).getFoldY(fold), paint);
						i = j;
					}

				}
			}
		}

		if (points.size() > 0) {
			// 绘制特殊点
			paint.setColor(assignColor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(getPointByAssignPosition().getFoldX(fold), getPointByAssignPosition().getFoldY(fold),
					radius - 1, paint);
		}
		

	}

	/**
	 * 清空画布
	 * 
	 * @param canvas
	 */
	private void clearCanvas(Canvas canvas) {
		canvas.drawColor(backgroundColor);
	}

	/**
	 * 拖动
	 *
	 */
	private class myGestureListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			float x = e2.getX() - e1.getX();
			float y = e2.getY() - e1.getY();

			if (getAbsoluteValue(x) > getAbsoluteValue(y)) {
				// 只向x轴偏移
				setDistance(x, 0);
			} else if (getAbsoluteValue(x) < getAbsoluteValue(y)) {
				// 只向y轴偏移
				setDistance(0, y);
			}

			return true;
		}

	}

	/**
	 * 放大缩小
	 *
	 */
	private class myScaleGestureListener implements OnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			float scale = detector.getScaleFactor();
			setScaleXY(scale);

			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		mScaleDetector.onTouchEvent(event);

		return true;
	}

	/**
	 * 取得float的绝对值
	 * 
	 * @param _value
	 * @return 绝对值
	 */
	private float getAbsoluteValue(float _value) {
		if (_value < 0) {
			_value = -_value;
		}
		return _value;
	}

}
