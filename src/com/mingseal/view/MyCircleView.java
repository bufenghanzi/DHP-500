package com.mingseal.view;

import com.mingseal.utils.DateUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 自定义圆形的方向布局
 * 
 * @author 商炎炳
 *
 */
public class MyCircleView extends View {

	private int circleWidth = 80; // 圆环直径
	private int circleColor = Color.argb(50, 255, 0, 0);
	private int innerCircleColor = Color.rgb(254, 0, 46);
	private int backgroundColor = Color.rgb(255, 255, 255);
	private Paint paint;
	private int center = 0;
	private int innerRadius = 0;
	private float innerCircleRadius = 0;
	private float smallCircle = 5;
	public Dir dir = Dir.UP;

	private String row = "";// 横坐标的值
	private String column = "";// 纵坐标的值

	private float speed;// 设置返回时X的坐标

	private float initialX;// x方法的值

	public static String PUSH_DOWN = "push_down";
	public static String PUSH_UP = "push_up";
	public String VIEW_PUSH;

	private onActivityCallBackListener circleListener;

	public MyCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyCircleView(Context context) {
		super(context);
		init(context);
	}

	public MyCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * @param context
	 */
	private void init(Context context) {
		paint = new Paint();
	}

	public float getSpeed() {
		return speed;
	}

	/**
	 * 设置速度
	 * 
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Dir getDir() {
		return dir;
	}

	/**
	 * 设置方向
	 * 
	 * @param dir
	 *            枚举类 UP, DOWN, LEFT, RIGHT, CENTER, UNDEFINE
	 */
	public void setDir(Dir dir) {
		this.dir = dir;
	}

	/**
	 * 设置横着显示的坐标
	 * 
	 * @param row
	 */
	public void setRow(String row) {
		this.row = row;
	}

	/**
	 * 设置竖着显示的坐标
	 * 
	 * @param column
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);

		setMeasuredDimension(measuredWidth, measuredHeight);

		// center = getWidth() / 2;
		center = getHeight() / 2;
		innerRadius = (center - circleWidth / 2 - 10);// 圆环
		innerCircleRadius = center / 4;
		// System.out.println("width:"+measuredWidth+",height:"+measuredHeight+",getWidth():"+getWidth()+",height:"+getHeight()+
		// ",center:" + center + ",innerRadius:" + innerRadius +
		// ",innerCircleRadius:" + innerCircleRadius);
		this.setOnTouchListener(new myTouchListener());
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

			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	/**
	 * 开始绘制
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		initBackGround(canvas);
		drawDirTriangle(canvas, dir);

	}

	/**
	 * 绘制方向小箭头
	 * 
	 * @param canvas
	 */
	private void drawDirTriangle(Canvas canvas, Dir dir) {
		paint.setColor(innerCircleColor);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.FILL);

		switch (dir) {
		case UP:
			drawUpTriangle(canvas);
			break;
		case DOWN:
			drawDownTriangle(canvas);
			break;
		case LEFT:
			drawLeftTriangle(canvas);
			break;
		case RIGHT:
			drawRightTriangle(canvas);
			break;
		case CENTER:
			invalidate();
			break;
		default:
			break;
		}

		paint.setColor(backgroundColor);

		canvas.drawCircle(center, center, smallCircle, paint);
		// canvas.drawText(text, center, center+40, paint);
		paint.setColor(innerCircleColor);
		paint.setTextSize(20);
		canvas.drawText(row, center + 55, center + 8, paint);// 绘制横坐标数值
		canvas.drawText(column, center - 6, center - 55, paint);// 绘制竖坐标数值

	}

	/**
	 * 绘制向右的小箭头
	 * 
	 * @param canvas
	 */
	private void drawRightTriangle(Canvas canvas) {
		Path path = new Path();
		path.moveTo(center, center);
		double sqrt2 = innerCircleRadius / Math.sqrt(2);
		double pow05 = innerCircleRadius * Math.sqrt(2);
		path.lineTo((float) (center + sqrt2), (float) (center - sqrt2));
		path.lineTo((float) (center + pow05), center);
		path.lineTo((float) (center + sqrt2), (float) (center + sqrt2));
		canvas.drawPath(path, paint);
		paint.setColor(backgroundColor);
		// canvas.drawLine(center, center, center + innerCircleRadius, center,
		// paint);

		drawOnclikColor(canvas, Dir.RIGHT);
	}

	/**
	 * 绘制向左的小箭头
	 * 
	 * @param canvas
	 */
	private void drawLeftTriangle(Canvas canvas) {
		Path path = new Path();
		path.moveTo(center, center);
		double sqrt2 = innerCircleRadius / Math.sqrt(2);
		double pow05 = innerCircleRadius * Math.sqrt(2);
		path.lineTo((float) (center - sqrt2), (float) (center - sqrt2));
		path.lineTo((float) (center - pow05), center);
		path.lineTo((float) (center - sqrt2), (float) (center + sqrt2));
		canvas.drawPath(path, paint);

		paint.setColor(backgroundColor);
		// canvas.drawLine(center, center, center - innerCircleRadius, center,
		// paint);

		drawOnclikColor(canvas, Dir.LEFT);

	}

	/**
	 * 绘制向下的小箭头
	 * 
	 * @param canvas
	 */
	private void drawDownTriangle(Canvas canvas) {
		Path path = new Path();
		path.moveTo(center, center);
		double sqrt2 = innerCircleRadius / Math.sqrt(2);
		double pow05 = innerCircleRadius * Math.sqrt(2);
		path.lineTo((float) (center - sqrt2), (float) (center + sqrt2));
		path.lineTo(center, (float) (center + pow05));
		path.lineTo((float) (center + sqrt2), (float) (center + sqrt2));
		canvas.drawPath(path, paint);

		paint.setColor(backgroundColor);
		// canvas.drawLine(center, center, center, center + innerCircleRadius,
		// paint);

		drawOnclikColor(canvas, Dir.DOWN);
	}

	/**
	 * 绘制向上的箭头
	 * 
	 * @param canvas
	 */
	private void drawUpTriangle(Canvas canvas) {
		Path path = new Path();
		path.moveTo(center, center);
		double sqrt2 = innerCircleRadius / Math.sqrt(2);
		double pow05 = innerCircleRadius * Math.sqrt(2);

		path.lineTo((float) (center - sqrt2), (float) (center - sqrt2));
		path.lineTo(center, (float) (center - pow05));
		path.lineTo((float) (center + sqrt2), (float) (center - sqrt2));
		canvas.drawPath(path, paint);

		paint.setColor(backgroundColor);
		// canvas.drawLine(center, center, center, center - innerCircleRadius,
		// paint);

		drawOnclikColor(canvas, Dir.UP);
	}

	/**
	 * 点击的时候绘制有颜色的扇形
	 * 
	 * @param canvas
	 * @param dir
	 */
	private void drawOnclikColor(Canvas canvas, Dir dir) {
		paint.setColor(innerCircleColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(circleWidth);
		switch (dir) {
		case UP:

			canvas.drawArc(
					new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center + innerRadius),
					225, 90, false, paint);
			paint.setColor(backgroundColor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("+", center - 10, center - innerCircleRadius * 2 - 15, paint);

			break;
		case DOWN:
			canvas.drawArc(
					new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center + innerRadius),
					45, 90, false, paint);
			paint.setColor(backgroundColor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("-", center - 10, center + innerCircleRadius * 3, paint);
			break;
		case LEFT:
			canvas.drawArc(
					new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center + innerRadius),
					135, 90, false, paint);
			paint.setColor(backgroundColor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("-", center - innerCircleRadius * 3 + 10, center + 8, paint);
			break;
		case RIGHT:
			canvas.drawArc(
					new RectF(center - innerRadius, center - innerRadius, center + innerRadius, center + innerRadius),
					-45, 90, false, paint);
			paint.setColor(backgroundColor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("+", center + innerCircleRadius * 2 + 20, center + 8, paint);
			break;

		default:
			break;
		}

		paint.setStyle(Paint.Style.FILL);
	}

	/**
	 * 绘制基本的背景， 这包括了三个步骤：1.清空画布 2.绘制外圈的圆 3.绘制内圈的圆
	 * 
	 * @param canvas
	 */
	private void initBackGround(Canvas canvas) {
		clearCanvas(canvas);
		drawBackCircle(canvas);
		drawInnerCircle(canvas);

	}

	/**
	 * 绘制中心白色小圆
	 * 
	 * @param canvas
	 */
	private void drawInnerCircle(Canvas canvas) {
		paint.setColor(innerCircleColor);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(1);
		canvas.drawCircle(center, center, innerCircleRadius, paint);
	}

	/**
	 * 绘制背景的圆圈和隔线
	 * 
	 * @param canvas
	 */
	private void drawBackCircle(Canvas canvas) {
		paint.setColor(circleColor);
		paint.setStrokeWidth(circleWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(center, center, innerRadius, paint); // 绘制圆圈

		paint.setColor(backgroundColor);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(4);
		canvas.drawLine(center, center, 0, 0, paint);
		canvas.drawLine(center, center, center * 2, 0, paint);
		canvas.drawLine(center, center, 0, center * 2, paint);
		canvas.drawLine(center, center, center * 2, center * 2, paint);

		paint.setColor(backgroundColor);
		paint.setTextSize(40);
		// 和绘制有颜色的外圈里面的值是一样的
		canvas.drawText("+", center + innerCircleRadius * 2 + 20, center + 8, paint);
		canvas.drawText("-", center - innerCircleRadius * 3 + 10, center + 8, paint);
		canvas.drawText("+", center - 10, center - innerCircleRadius * 2 - 15, paint);
		canvas.drawText("-", center - 10, center + innerCircleRadius * 3, paint);

	}

	/**
	 * 清空画布
	 * 
	 * @param canvas
	 */
	private void clearCanvas(Canvas canvas) {
		canvas.drawColor(backgroundColor);
	}

	public class myTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				System.out.println("ACTION_DOWN-->当前时间：" + DateUtil.getCurrentTime());
				VIEW_PUSH = PUSH_DOWN;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
//				System.out.println("ACTION_UP-->当前时间：" + DateUtil.getCurrentTime());
//				System.out.println("getSpeed()" + getSpeed());
				VIEW_PUSH = PUSH_UP;
			} else {
				VIEW_PUSH = "";
			}

			Dir tmp = Dir.UNDEFINE;
			if ((tmp = checkDir(event.getX(), event.getY())) != Dir.UNDEFINE) {
				dir = tmp;
				invalidate();
			}
			if (circleListener != null) {
				circleListener.setDir(dir, VIEW_PUSH);
//				circleListener.setSpeed(speed);

			}
			return true;// return false只会执行一遍，return true的话会一遍又一遍的在执行
		}

		/**
		 * 检测方向
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		private Dir checkDir(float x, float y) {
			Dir dir = Dir.UNDEFINE;

			if (Math.sqrt(Math.pow(y - center, 2) + Math.pow(x - center, 2)) < innerCircleRadius) {// 判断在中心圆圈内
				dir = Dir.CENTER;
				// System.out.println("----中央");
			} else if (y < x && y + x < 2 * center) {
				dir = Dir.UP;
				// System.out.println("----向上");
			} else if (y < x && y + x > 2 * center) {
				dir = Dir.RIGHT;
				// System.out.println("----向右");
			} else if (y > x && y + x < 2 * center) {
				dir = Dir.LEFT;
				// System.out.println("----向左");
			} else if (y > x && y + x > 2 * center) {
				dir = Dir.DOWN;
				// System.out.println("----向下");
			}

			return dir;
		}

	}

	/**
	 * 回调函数
	 * 
	 * @param l
	 */
	public void setOnActivityCallBackListener(onActivityCallBackListener l) {
		this.circleListener = l;
	}

	/**
	 * 回調接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface onActivityCallBackListener {
		/**
		 * @param dir
		 *            判断上下左右
		 * @param view_push
		 *            判断按下，抬起，移动
		 */
		void setDir(Dir dir, String view_push);

//		void setSpeed(float speed);

	}

	/**
	 * 关于方向的枚举
	 * 
	 * @author Administrator
	 * 
	 */
	public enum Dir {
		UP, DOWN, LEFT, RIGHT, CENTER, UNDEFINE
	}

}
