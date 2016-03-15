/**
 * 
 */
package com.mingseal.data.param;

/**
 * @author 商炎炳
 * 
 * @title 任务设置参数
 *
 */
public class SettingParam {

	public static class Setting {
		public static final String SettingName = "setting_pref";
		/**
		 * x轴步距(1-10)
		 */
		public static final String XStepDistance = "xStepDistance";
		/**
		 * y轴步距(1-10)
		 */
		public static final String YStepDistance = "yStepDistance";
		/**
		 * z轴步距(1-10)
		 */
		public static final String ZStepDistance = "xStepDistance";
		/**
		 * 高速(mm/s)
		 */
		public static final String HighSpeed = "highSpeed";
		/**
		 * 中速(mm/s)
		 */
		public static final String MediumSpeed = "mediumSpeed";
		/**
		 * 低速(mm/s)
		 */
		public static final String LowSpeed = "lowSpeed";
		/**
		 * 循迹速度(mm/s)
		 */
		public static final String TrackSpeed = "trackSpeed";
		/**
		 * 循迹定位
		 */
		public static final String TrackLocation = "trackLocation";
	}
	
	public static class Task{
		/**
		 * @Fields TaskName: 保存任务名的string
		 */
		public static final String TaskName = "setting_task_pref";
		/**
		 * @Fields TaskNumber: 任务编号
		 */
		public static final String TaskNumber = "taskNumber";
		/**
		 * @Fields TaskGlueAloneNumber: 独立点上次保存的编号
		 */
		public static final String TaskGlueAloneNumber = "taskGlueAloneNumber";
		/**
		 * @Fields TaskGlueClearNumber: 清胶点上次保存的编号
		 */
		public static final String TaskGlueClearNumber = "taskGlueClearNumber";
		/**
		 * @Fields TaskGlueFaceEndNumber: 面结束点上次保存的编号
		 */
		public static final String TaskGlueFaceEndNumber = "taskGlueFaceEndNumber";
		/**
		 * @Fields TaskGlueFaceStartNumber: 面起始点上次保存的编号
		 */
		public static final String TaskGlueFaceStartNumber = "taskGlueFaceStartNumber";
		/**
		 * @Fields TaskGlueInputNumber: 输入IO上次保存的编号
		 */
		public static final String TaskGlueInputNumber = "taskGlueInputNumber";
		/**
		 * @Fields TaskGlueOutputNumber: 输出IO上次保存的编号
		 */
		public static final String TaskGlueOutputNumber = "taskGlueOutputNumber";
		/**
		 * @Fields TaskGlueLineEndNumber: 线结束点上次保存的编号
		 */
		public static final String TaskGlueLineEndNumber = "taskGlueLineEndNumber";
		/**
		 * @Fields TaskGlueLineMidNumber: 线中间点上次保存的编号
		 */
		public static final String TaskGlueLineMidNumber = "taskGlueLineMidNumber";
		/**
		 * @Fields TaskGlueLineStartNumber: 线起始点上次保存的编号
		 */
		public static final String TaskGlueLineStartNumber = "taskGlueLineStartNumber";
	}

	private int xStepDistance;// x轴步距(1-10)
	private int yStepDistance;// y轴步距(1-10)
	private int zStepDistance;// z轴步距(1-10)
	private int highSpeed;// 高速(mm/s)
	private int mediumSpeed;// 中速(mm/s)
	private int lowSpeed;// 低速(mm/s)
	private int trackSpeed;// 循迹速度(mm/s)
	private boolean trackLocation;// 循迹定位

	public SettingParam() {
		super();
	}

	/**
	 * @Title: SettingParam
	 * @Description: 所有参数的构造函数
	 * @param xStepDistance
	 *            x轴步距(1-10)
	 * @param yStepDistance
	 *            y轴步距(1-10)
	 * @param zStepDistance
	 *            z轴步距(1-10)
	 * @param highSpeed
	 *            高速(mm/s)
	 * @param mediumSpeed
	 *            中速(mm/s)
	 * @param lowSpeed
	 *            低速(mm/s)
	 * @param trackSpeed
	 *            循迹速度(mm/s)
	 * @param trackLocation
	 *            循迹定位
	 */
	public SettingParam(int xStepDistance, int yStepDistance, int zStepDistance, int highSpeed, int mediumSpeed,
			int lowSpeed, int trackSpeed, boolean trackLocation) {
		super();
		this.xStepDistance = xStepDistance;
		this.yStepDistance = yStepDistance;
		this.zStepDistance = zStepDistance;
		this.highSpeed = highSpeed;
		this.mediumSpeed = mediumSpeed;
		this.lowSpeed = lowSpeed;
		this.trackSpeed = trackSpeed;
		this.trackLocation = trackLocation;
	}

	/**
	 * @return x轴步距
	 */
	public int getxStepDistance() {
		return xStepDistance;
	}

	/**
	 * 设置x轴步距
	 * 
	 * @param xStepDistance
	 */
	public void setxStepDistance(int xStepDistance) {
		this.xStepDistance = xStepDistance;
	}

	/**
	 * @return y轴步距
	 */
	public int getyStepDistance() {
		return yStepDistance;
	}

	/**
	 * 设置y轴步距
	 * 
	 * @param yStepDistance
	 */
	public void setyStepDistance(int yStepDistance) {
		this.yStepDistance = yStepDistance;
	}

	/**
	 * @return z轴步距
	 */
	public int getzStepDistance() {
		return zStepDistance;
	}

	/**
	 * 设置z轴步距
	 * 
	 * @param zStepDistance
	 */
	public void setzStepDistance(int zStepDistance) {
		this.zStepDistance = zStepDistance;
	}

	/**
	 * @return 高速
	 */
	public int getHighSpeed() {
		return highSpeed;
	}

	/**
	 * 设置高速的值
	 * 
	 * @param highSpeed
	 */
	public void setHighSpeed(int highSpeed) {
		this.highSpeed = highSpeed;
	}

	/**
	 * @return 中速
	 */
	public int getMediumSpeed() {
		return mediumSpeed;
	}

	/**
	 * 设置中速的值
	 * 
	 * @param mediumSpeed
	 */
	public void setMediumSpeed(int mediumSpeed) {
		this.mediumSpeed = mediumSpeed;
	}

	/**
	 * @return 低速
	 */
	public int getLowSpeed() {
		return lowSpeed;
	}

	/**
	 * 设置低速的值
	 * 
	 * @param lowSpeed
	 */
	public void setLowSpeed(int lowSpeed) {
		this.lowSpeed = lowSpeed;
	}

	/**
	 * @return 循迹速度
	 */
	public int getTrackSpeed() {
		return trackSpeed;
	}

	/**
	 * 设置循迹速度
	 * 
	 * @param trackSpeed
	 */
	public void setTrackSpeed(int trackSpeed) {
		this.trackSpeed = trackSpeed;
	}

	/**
	 * @return 循迹定位(true为是,false为否)
	 */
	public boolean isTrackLocation() {
		return trackLocation;
	}

	/**
	 * 设置循迹定位(true为是,false为否)
	 * 
	 * @param trackLocation
	 */
	public void setTrackLocation(boolean trackLocation) {
		this.trackLocation = trackLocation;
	}

	@Override
	public String toString() {
		return "SettingParam [xStepDistance=" + xStepDistance + ", yStepDistance=" + yStepDistance + ", zStepDistance="
				+ zStepDistance + ", highSpeed=" + highSpeed + ", mediumSpeed=" + mediumSpeed + ", lowSpeed=" + lowSpeed
				+ ", trackSpeed=" + trackSpeed + ", trackLocation=" + trackLocation + "]";
	}

}
