package com.mingseal.data.param;

/**
 * @ClassName: TaskParam
 * @Description: 任务参数
 * @author lyq
 * @date 2015年6月15日 下午4:09:10
 * 
 */
public enum TaskParam {
	
	INSTANCE;

	private String strRobotName; // 机器名
	private String strTaskName; // 任务名
	private int nTaskNum; // 任务号
	private int nAccelerateTime; // 加速时间
	private int nDecelerateTime; // 减速时间
	private int nAccelerate; // 加速度
	public static  String Accelerate; // 加速度
	private int nDecelerate; // 减速度
	public String Decelerate; // 减速度
	private int nXYNullSpeed; // xy轴空走速度
	public String XYNullSpeed; // xy轴空走速度
	private int nYNullSpeed; // y轴空走速度
	private int nZNullSpeed; // z轴空走速度
	public String ZNullSpeed; // z轴空走速度
	private int nUNullSpeed; // u轴空走速度
	private int nBackSnSpeedFir; // 一次回锡速度
	private int nBackSnSumFir; // 一次回锡量
	private int nBackSnSpeedSec; // 二次回锡速度
	private int nBackSnSumSec; // 二次回锡量
	private int nSnHeight; // 出锡高度
	private int nWorkMode; // 工作模式(0:气缸，1:电机)
	private int nStartX; // 起点x轴坐标(脉冲数)
	private int nStartY; // 起点y轴坐标(脉冲数)
	private int nStartZ; // 起点z轴坐标(脉冲数)
	private int nStartU; // 起点u轴坐标(脉冲数)
	private int nStartSpeed; // 起点速度
	private int nTurnSpeed; // 拐点速度
	public static String TurnSpeed; // 拐点速度
	private int nDemoSpeed; // 模拟速度
	private int nSpeedCurve; // 任务速度曲线(0:梯形曲线，1:S形曲线)
	
	private int nZeroCheck; // 零点校正
	private int nBaseUnit; // 基点调整单位
	private int nBaseHeight; // 基点调整高度
	private int nAutoRunTime; // 自动运行延时时间
	private int nTurnAccelerateMax; // 拐点最大加速度
	public static String TurnAccelerateMax; // 拐点最大加速度
	private boolean bTaskBack; // 任务还原
	private boolean bTaskDelete; // 任务删除
	private int nYCheckDis; // Y轴定位校正距离
	private int nRunNum; // 任务运行记数
	private boolean bRunNumZero; // 任务运行记数清零
	private int nPauseType; // 暂停类型
	private boolean bBackDefault; // 恢复出厂默认值
	private int nNoHardOutGlueTime; // 硬化防止出胶时间
	private int nNoHardOutGlueInterval; // 硬化防止间隔时间

	/**
	 * 任务参数构造函数
	 */
	private TaskParam() {
		nTaskNum = 1;
		nAccelerateTime = 100;
		nDecelerateTime = 100;
		nAccelerate = 1000;
		nDecelerate = 1000;
		nXYNullSpeed = 200;
		nZNullSpeed = 200;
		nUNullSpeed = 150;
		nBackSnSpeedFir = 50;
		nBackSnSumFir = 0;
		nBackSnSpeedSec = 50;
		nBackSnSumSec = 0;
		nSnHeight = 8;
		nWorkMode = 0; // 工作模式（气缸 =0） 被取消 直接赋值1 2013.10.23(最新版本不是这样)
		nStartX = 0;
		nStartY = 0;
		nStartZ = 0;
		nStartU = 0;
		nStartSpeed = 100;
		nTurnSpeed = 50;
		nDemoSpeed = 30;
		nSpeedCurve = 0;
		
		nZeroCheck = 0;
		nBaseUnit = 5;
		nBaseHeight = 0;
		nAutoRunTime = 0;
		nTurnAccelerateMax = 10000;
		bTaskBack = false;
		bTaskDelete = false;
		nYCheckDis = 0;
		nRunNum = 0;
		bRunNumZero = false;
		nPauseType = 0;
		bBackDefault = false;
		nNoHardOutGlueTime = 0;
		nNoHardOutGlueInterval = 0;
	}
	
	/**
	 * <p>Title: setAllParamBacktoDefault
	 * <p>Description: 恢复任务参数默认值
	 */
	public void setAllParamBacktoDefault(){
		nTaskNum = 1;
		nAccelerateTime = 100;
		nDecelerateTime = 100;
		nAccelerate = 1000;
		nDecelerate = 1000;
		nXYNullSpeed = 200;
		nZNullSpeed = 200;
		nUNullSpeed = 150;
		nBackSnSpeedFir = 50;
		nBackSnSumFir = 0;
		nBackSnSpeedSec = 50;
		nBackSnSumSec = 0;
		nSnHeight = 8;
		nWorkMode = 0; // 工作模式（气缸 =0） 被取消 直接赋值1 2013.10.23(最新版本不是这样)
		nStartX = 0;
		nStartY = 0;
		nStartZ = 0;
		nStartU = 0;
		nStartSpeed = 100;
		nTurnSpeed = 50;
		nDemoSpeed = 30;
		nSpeedCurve = 0;
		
		nZeroCheck = 0;
		nBaseUnit = 5;
		nBaseHeight = 0;
		nAutoRunTime = 0;
		nTurnAccelerateMax = 10000;
		bTaskBack = false;
		bTaskDelete = false;
		nYCheckDis = 0;
		nRunNum = 0;
		bRunNumZero = false;
		nPauseType = 0;
		bBackDefault = false;
		nNoHardOutGlueTime = 0;
		nNoHardOutGlueInterval = 0;
	}
	
	/**
	 * @return 获取机器名
	 */
	public String getStrRobotName() {
		return strRobotName;
	}

	/**
	 * 设置机器名
	 * @param strRobotName 机器名
	 */
	public void setStrRobotName(String strRobotName) {
		this.strRobotName = strRobotName;
	}

	/**
	 * @return 获取任务名
	 */
	public String getStrTaskName() {
		return strTaskName;
	}

	/**
	 * 设置任务名
	 * @param strTaskName 任务名
	 */
	public void setStrTaskName(String strTaskName) {
		this.strTaskName = strTaskName;
	}

	/**
	 * @return 获取任务号
	 */
	public int getnTaskNum() {
		return nTaskNum;
	}

	/**
	 * 设置任务号
	 * @param nTaskNum 任务号
	 */
	public void setnTaskNum(int nTaskNum) {
		this.nTaskNum = nTaskNum;
	}

	/**
	 * @return 获取加速时间
	 */
	public int getnAccelerateTime() {
		return nAccelerateTime;
	}

	/**
	 * 设置加速时间
	 * @param nAccelerateTime 加速时间
	 */
	public void setnAccelerateTime(int nAccelerateTime) {
		this.nAccelerateTime = nAccelerateTime;
	}

	/**
	 * @return 获取减速时间
	 */
	public int getnDecelerateTime() {
		return nDecelerateTime;
	}

	/**
	 * 设置减速时间
	 * @param nDecelerateTime 减速时间
	 */
	public void setnDecelerateTime(int nDecelerateTime) {
		this.nDecelerateTime = nDecelerateTime;
	}

	/**
	 * @return 获取加速度
	 */
	public int getnAccelerate() {
		return nAccelerate;
	}

	/**
	 * 设置加速度
	 * @param nAccelerate 加速度
	 */
	public void setnAccelerate(int nAccelerate) {
		this.nAccelerate = nAccelerate;
	}

	/**
	 * @return 获取减速度
	 */
	public int getnDecelerate() {
		return nDecelerate;
	}

	/**
	 * 设置减速度
	 * @param nDecelerate 减速度
	 */
	public void setnDecelerate(int nDecelerate) {
		this.nDecelerate = nDecelerate;
	}

	/**
	 * @return 获取xy轴空走速度
	 */
	public int getnXYNullSpeed() {
		return nXYNullSpeed;
	}

	/**
	 * 设置xy轴空走速度
	 * @param nXYNullSpeed xy轴空走速度
	 */
	public void setnXYNullSpeed(int nXYNullSpeed) {
		this.nXYNullSpeed = nXYNullSpeed;
	}

	/**
	 * @return 获取Y轴空走速度
	 */
	public int getnYNullSpeed() {
		return nYNullSpeed;
	}

	/**
	 * 设置Y轴空走速度
	 * @param nYNullSpeed Y轴空走速度
	 */
	public void setnYNullSpeed(int nYNullSpeed) {
		this.nYNullSpeed = nYNullSpeed;
	}

	/**
	 * @return 获取Z轴空走速度
	 */
	public int getnZNullSpeed() {
		return nZNullSpeed;
	}

	/**
	 * 设置Z轴空走速度
	 * @param nZNullSpeed Z轴空走速度
	 */
	public void setnZNullSpeed(int nZNullSpeed) {
		this.nZNullSpeed = nZNullSpeed;
	}

	/**
	 * @return 获取U轴空走速度
	 */
	public int getnUNullSpeed() {
		return nUNullSpeed;
	}

	/**
	 * 设置U轴空走速度
	 * @param nUNullSpeed U轴空走速度
	 */
	public void setnUNullSpeed(int nUNullSpeed) {
		this.nUNullSpeed = nUNullSpeed;
	}

	/**
	 * @return 获取一次回锡速度
	 */
	public int getnBackSnSpeedFir() {
		return nBackSnSpeedFir;
	}

	/**
	 * 设置一次回锡速度
	 * @param nBackSnSpeedFir 一次回锡速度
	 */
	public void setnBackSnSpeedFir(int nBackSnSpeedFir) {
		this.nBackSnSpeedFir = nBackSnSpeedFir;
	}

	/**
	 * @return 获取一次回锡量
	 */
	public int getnBackSnSumFir() {
		return nBackSnSumFir;
	}

	/**
	 * 设置一次回锡量
	 * @param nBackSnSumFir 一次回锡量
	 */
	public void setnBackSnSumFir(int nBackSnSumFir) {
		this.nBackSnSumFir = nBackSnSumFir;
	}

	/**
	 * @return 获取二次回锡速度
	 */
	public int getnBackSnSpeedSec() {
		return nBackSnSpeedSec;
	}

	/**
	 * 设置二次回锡速度
	 * @param nBackSnSpeedSec 二次回锡速度
	 */
	public void setnBackSnSpeedSec(int nBackSnSpeedSec) {
		this.nBackSnSpeedSec = nBackSnSpeedSec;
	}

	/**
	 * @return 获取二次回锡量
	 */
	public int getnBackSnSumSec() {
		return nBackSnSumSec;
	}

	/**
	 * 设置二次回锡量
	 * @param nBackSnSumSec 二次回锡量
	 */
	public void setnBackSnSumSec(int nBackSnSumSec) {
		this.nBackSnSumSec = nBackSnSumSec;
	}

	/**
	 * @return 获取出锡高度
	 */
	public int getnSnHeight() {
		return nSnHeight;
	}

	/**
	 * 设置出锡高度
	 * @param nSnHeight 出锡高度
	 */
	public void setnSnHeight(int nSnHeight) {
		this.nSnHeight = nSnHeight;
	}

	/**
	 * @return 工作模式(0:气缸，1:电机)
	 */
	public int getnWorkMode() {
		return nWorkMode;
	}

	/**
	 * 设置工作模式(0:气缸，1:电机)
	 * @param nWorkMode 工作模式
	 */
	public void setnWorkMode(int nWorkMode) {
		this.nWorkMode = nWorkMode;
	}

	/**
	 * @return 获取起点x轴坐标(脉冲数)
	 */
	public int getnStartX() {
		return nStartX;
	}

	/**
	 * 设置起点x轴坐标(脉冲数)
	 * @param nStartX 起点x轴坐标
	 */
	public void setnStartX(int nStartX) {
		this.nStartX = nStartX;
	}

	/**
	 * @return 获取起点y轴坐标(脉冲数)
	 */
	public int getnStartY() {
		return nStartY;
	}

	/**
	 * 设置起点y轴坐标(脉冲数)
	 * @param nStartY 起点y轴坐标
	 */
	public void setnStartY(int nStartY) {
		this.nStartY = nStartY;
	}

	/**
	 * @return 获取起点z轴坐标(脉冲数)
	 */
	public int getnStartZ() {
		return nStartZ;
	}

	/**
	 * 设置起点z轴坐标(脉冲数)
	 * @param nStartZ 起点y轴坐标
	 */
	public void setnStartZ(int nStartZ) {
		this.nStartZ = nStartZ;
	}

	/**
	 * @return 获取起点u轴坐标(脉冲数)
	 */
	public int getnStartU() {
		return nStartU;
	}

	/**
	 * 设置起点u轴坐标(脉冲数)
	 * @param nStartU 起点u轴坐标
	 */
	public void setnStartU(int nStartU) {
		this.nStartU = nStartU;
	}

	/**
	 * @return 获取起点速度
	 */
	public int getnStartSpeed() {
		return nStartSpeed;
	}

	/**
	 * 设置起点速度
	 * @param nStartSpeed 起点速度
	 */
	public void setnStartSpeed(int nStartSpeed) {
		this.nStartSpeed = nStartSpeed;
	}

	/**
	 * @return 获取拐点速度
	 */
	public int getnTurnSpeed() {
		return nTurnSpeed;
	}

	/**
	 * 设置拐点速度
	 * @param nTurnSpeed 拐点速度
	 */
	public void setnTurnSpeed(int nTurnSpeed) {
		this.nTurnSpeed = nTurnSpeed;
	}

	/**
	 * @return 模拟速度
	 */
	public int getnDemoSpeed() {
		return nDemoSpeed;
	}

	/**
	 * 设置模拟速度
	 * @param nDemoSpeed 模拟速度
	 */
	public void setnDemoSpeed(int nDemoSpeed) {
		this.nDemoSpeed = nDemoSpeed;
	}

	/**
	 * @return 获取任务速度曲线(0:梯形曲线，1:S形曲线)
	 */
	public int getnSpeedCurve() {
		return nSpeedCurve;
	}

	/**
	 * 设置任务速度曲线(0:梯形曲线，1:S形曲线)
	 * @param nSpeedCurve 任务速度曲线
	 */
	public void setnSpeedCurve(int nSpeedCurve) {
		this.nSpeedCurve = nSpeedCurve;
	}

	/**
	 * @return 获取零点校正
	 */
	public int getnZeroCheck() {
		return nZeroCheck;
	}

	/**
	 * 设置零点校正
	 * @param nZeroCheck 零点校正
	 */
	public void setnZeroCheck(int nZeroCheck) {
		this.nZeroCheck = nZeroCheck;
	}

	/**
	 * @return 获取基点调整单位
	 */
	public int getnBaseUnit() {
		return nBaseUnit;
	}

	/**
	 * 设置基点调整单位
	 * @param nBaseUnit 基点调整单位
	 */
	public void setnBaseUnit(int nBaseUnit) {
		this.nBaseUnit = nBaseUnit;
	}

	/**
	 * @return 获取基点调整高度
	 */
	public int getnBaseHeight() {
		return nBaseHeight;
	}

	/**
	 * 设置基点调整高度
	 * @param nBaseHeight 基点调整高度
	 */
	public void setnBaseHeight(int nBaseHeight) {
		this.nBaseHeight = nBaseHeight;
	}

	/**
	 * @return 获取自动运行延时时间
	 */
	public int getnAutoRunTime() {
		return nAutoRunTime;
	}

	/**
	 * 设置自动运行延时时间
	 * @param nAutoRunTime 自动运行延时时间
	 */
	public void setnAutoRunTime(int nAutoRunTime) {
		this.nAutoRunTime = nAutoRunTime;
	}

	/**
	 * @return 获取拐点最大加速度
	 */
	public int getnTurnAccelerateMax() {
		return nTurnAccelerateMax;
	}

	/**
	 * 设置拐点最大加速度
	 * @param nTurnAccelerateMax 拐点最大加速度
	 */
	public void setnTurnAccelerateMax(int nTurnAccelerateMax) {
		this.nTurnAccelerateMax = nTurnAccelerateMax;
	}

	/**
	 * @return 获取任务是否还原
	 */
	public boolean isbTaskBack() {
		return bTaskBack;
	}

	/**
	 * 设置任务是否还原
	 * @param bTaskBack 任务是否还原
	 */
	public void setbTaskBack(boolean bTaskBack) {
		this.bTaskBack = bTaskBack;
	}

	/**
	 * @return 获取任务是否删除
	 */
	public boolean isbTaskDelete() {
		return bTaskDelete;
	}

	/**
	 * 设置任务是否删除
	 * @param bTaskDelete 任务是否删除
	 */
	public void setbTaskDelete(boolean bTaskDelete) {
		this.bTaskDelete = bTaskDelete;
	}

	/**
	 * @return 获取Y轴定位校正距离
	 */
	public int getnYCheckDis() {
		return nYCheckDis;
	}

	/**
	 * 设置Y轴定位校正距离
	 * @param nYCheckDis Y轴定位校正距离
	 */
	public void setnYCheckDis(int nYCheckDis) {
		this.nYCheckDis = nYCheckDis;
	}

	/**
	 * @return 获取任务运行计数
	 */
	public int getnRunNum() {
		return nRunNum;
	}

	/**
	 * 设置任务运行计数
	 * @param nRunNum 任务运行计数
	 */
	public void setnRunNum(int nRunNum) {
		this.nRunNum = nRunNum;
	}

	/**
	 * @return 获取任务运行计数是否清零
	 */
	public boolean isbRunNumZero() {
		return bRunNumZero;
	}

	/**
	 * 设置任务运行计数是否清零
	 * @param bRunNumZero 任务运行计数是否清零
	 */
	public void setbRunNumZero(boolean bRunNumZero) {
		this.bRunNumZero = bRunNumZero;
	}

	/**
	 * @return 获取暂停类型
	 */
	public int getnPauseType() {
		return nPauseType;
	}

	/**
	 * 设置暂停类型
	 * @param nPauseType 暂停类型
	 */
	public void setnPauseType(int nPauseType) {
		this.nPauseType = nPauseType;
	}

	/**
	 * @return 获取是否恢复出厂默认值
	 */
	public boolean isbBackDefault() {
		return bBackDefault;
	}

	/**
	 * 设置是否恢复出厂默认值
	 * @param bBackDefault 是否恢复出厂默认值
	 */
	public void setbBackDefault(boolean bBackDefault) {
		this.bBackDefault = bBackDefault;
	}

	/**
	 * @return 获取硬化防止出胶时间
	 */
	public int getnNoHardOutGlueTime() {
		return nNoHardOutGlueTime;
	}

	/**
	 * 设置硬化防止出胶时间
	 * @param nNoHardOutGlueTime 硬化防止出胶时间
	 */
	public void setnNoHardOutGlueTime(int nNoHardOutGlueTime) {
		this.nNoHardOutGlueTime = nNoHardOutGlueTime;
	}

	/**
	 * @return 获取硬化防止间隔时间
	 */
	public int getnNoHardOutGlueInterval() {
		return nNoHardOutGlueInterval;
	}

	/**
	 * 设置硬化防止间隔时间
	 * @param nNoHardOutGlueInterval 硬化防止间隔时间
	 */
	public void setnNoHardOutGlueInterval(int nNoHardOutGlueInterval) {
		this.nNoHardOutGlueInterval = nNoHardOutGlueInterval;
	}

}
