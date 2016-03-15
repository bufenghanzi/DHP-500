/**
 * 
 */
package com.mingseal.data.param;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 商炎炳
 * @description 下载参数
 */
public class DownloadParam implements Parcelable {
	private int number;// 任务号
	private int accelerate_time;// 加速度
	private int decelerate_speed;// 减速度
	private int xy_move;// XY轴空走速度
	private int z_move;// Z轴空走速度
	private int inflexion_time;// 拐点速度
	private int inflexion_max_speed;// 拐点最大加速度

	public DownloadParam() {
		super();
	}

	/**
	 * @param number
	 *            任务号
	 * @param accelerate_time
	 *            加速度
	 * @param decelerate_speed
	 *            减速度
	 * @param xy_move
	 *            XY轴空走速度
	 * @param z_move
	 *            Z轴空走速度
	 * @param inflexion_time
	 *            拐点减速速度
	 * @param inflexion_max_speed
	 *            拐点最大加速度
	 */
	public DownloadParam(int number, int accelerate_time, int decelerate_speed, int xy_move, int z_move,
			int inflexion_time, int inflexion_max_speed) {
		super();
		this.number = number;
		this.accelerate_time = accelerate_time;
		this.decelerate_speed = decelerate_speed;
		this.xy_move = xy_move;
		this.z_move = z_move;
		this.inflexion_time = inflexion_time;
		this.inflexion_max_speed = inflexion_max_speed;
	}

	/**
	 * @return 任务号
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * 设置任务号
	 * 
	 * @param number
	 *            任务号
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return 加速时间
	 */
	public int getAccelerate_time() {
		return accelerate_time;
	}

	/**
	 * 设置加速时间
	 * 
	 * @param accelerate_time
	 */
	public void setAccelerate_time(int accelerate_time) {
		this.accelerate_time = accelerate_time;
	}

	/**
	 * @return XY轴空走速度
	 */
	public int getXy_move() {
		return xy_move;
	}

	/**
	 * 设置XY轴空走速度
	 * 
	 * @param xy_move
	 *            XY轴空走速度
	 */
	public void setXy_move(int xy_move) {
		this.xy_move = xy_move;
	}

	/**
	 * @return Z轴空走速度
	 */
	public int getZ_move() {
		return z_move;
	}

	/**
	 * 设置Z轴空走速度
	 * 
	 * @param z_move
	 *            Z轴空走速度
	 */
	public void setZ_move(int z_move) {
		this.z_move = z_move;
	}

	/**
	 * @return 拐点减速速度
	 */
	public int getInflexion_time() {
		return inflexion_time;
	}

	/**
	 * 设置拐点速度
	 * 
	 * @param inflexion_time
	 *            拐点减速速度
	 */
	public void setInflexion_time(int inflexion_time) {
		this.inflexion_time = inflexion_time;
	}

	/**
	 * @return 减速度
	 */
	public int getDecelerate_speed() {
		return decelerate_speed;
	}

	/**
	 * 设置减速度
	 * 
	 * @param decelerate_speed
	 *            减速度
	 */
	public void setDecelerate_speed(int decelerate_speed) {
		this.decelerate_speed = decelerate_speed;
	}

	/**
	 * @return 拐点最大加速度
	 */
	public int getInflexion_max_speed() {
		return inflexion_max_speed;
	}

	/**
	 * 设置拐点最大加速度
	 * 
	 * @param inflexion_max_speed
	 *            拐点最大加速度
	 */
	public void setInflexion_max_speed(int inflexion_max_speed) {
		this.inflexion_max_speed = inflexion_max_speed;
	}

	@Override
	public String toString() {
		return "DownloadParam [number=" + number + ", accelerate_time=" + accelerate_time + ", decelerate_speed="
				+ decelerate_speed + ", xy_move=" + xy_move + ", z_move=" + z_move + ", inflexion_time="
				+ inflexion_time + ", inflexion_max_speed=" + inflexion_max_speed + "]";
	}

	public static final Parcelable.Creator<DownloadParam> CREATOR = new Creator<DownloadParam>() {

		@Override
		public DownloadParam createFromParcel(Parcel source) {
			DownloadParam param = new DownloadParam();

			param.number = source.readInt();
			param.accelerate_time = source.readInt();
			param.xy_move = source.readInt();
			param.z_move = source.readInt();
			param.inflexion_time = source.readInt();
			param.decelerate_speed = source.readInt();
			param.inflexion_max_speed = source.readInt();

			return param;
		}

		@Override
		public DownloadParam[] newArray(int size) {
			return new DownloadParam[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(number);
		dest.writeInt(accelerate_time);
		dest.writeInt(xy_move);
		dest.writeInt(z_move);
		dest.writeInt(inflexion_time);
		dest.writeInt(decelerate_speed);
		dest.writeInt(inflexion_max_speed);

	}

}
