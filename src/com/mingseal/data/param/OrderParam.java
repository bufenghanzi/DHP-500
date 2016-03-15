package com.mingseal.data.param;

/**
 * @ClassName: OrderParam
 * @Description: 命令参数
 * @author lyq
 * @date 2015年6月15日 下午4:08:46
 * 
 */
public enum OrderParam {

	INSTANCE;
	
	private int nSpeed; // 移动速度：mm/s
	private int nXCoord; // x坐标
	private int nYCoord; // y坐标
	private int nZCoord; // z坐标
	private int nUCoord; // u坐标
	private int nXCoord1; // x坐标
	private int nYCoord1; // y坐标
	private int nZCoord1; // z坐标
	private int nUCoord1; // u坐标
	private int nXCoord2; // x坐标
	private int nYCoord2; // y坐标
	private int nZCoord2; // z坐标
	private int nUCoord2; // u坐标
	private int nPulse; // 单步脉冲数
	private int nDataLen; // 任务数据长度：字节数
	private int nTaskNum; // 任务号
	private int nMoveDir; // 运动方向：0-正、1-负
	private int nMoveType; // 运动方式：0-连续、1-单步
	private int nMoveCoord; // 运动坐标：0-x轴，1-y轴，2-z轴，3-u轴
	private int nHeight; // 循迹高度
	private int nType; // 循迹类型
	private int nFlag; // 是否空走标记
	private int nIOPortStatue; // 4路输出口状态 2014/12/29
	private int nIOflag; // 待定 2014/12/30

	private int nZeroCheck;			// 零点校正
	private int nAccelerate;		// 加速度
	private int nDecelerate;		// 减速度
	private int nBaseUnit;			// 基点调整单位
	private int nBaseHeight;		// 基点调整高度
	private int nXYNullSpeed;		// xy轴空走速度
	private int nZNullSpeed;		// z轴空走速度
	private int nUNullSpeed;		// u轴空走速度
	private int nAutoRunTime;		// 自动运行延时时间
	private int nTurnAccelerateMax;	// 拐点最大加速度
	private boolean bTaskBack;			// 任务还原
	private boolean bTaskDelete;		// 任务删除
	private int nYCheckDis;			// Y轴定位校正距离
	private int nRunNum;			// 任务运行记数
	private boolean bRunNumZero;		// 任务运行记数清零
	private int nPauseType;			// 暂停类型
	private boolean bBackDefault;		// 恢复出厂默认值
	private int nNoHardOutGlueTime;	// 硬化防止出胶时间
	private int nNoHardOutGlueInterval;	// 硬化防止间隔时间
	
	/**
	 * <p>Title: 
	 * <p>Description: 默认构造函数
	 */
	private  OrderParam(){
		this.setAllParamToZero();
	}

	public int getnSpeed() {
		return nSpeed;
	}

	public void setnSpeed(int nSpeed) {
		this.nSpeed = nSpeed;
	}

	public int getnXCoord() {
		return nXCoord;
	}

	public void setnXCoord(int nXCoord) {
		this.nXCoord = nXCoord;
	}

	public int getnYCoord() {
		return nYCoord;
	}

	public void setnYCoord(int nYCoord) {
		this.nYCoord = nYCoord;
	}

	public int getnZCoord() {
		return nZCoord;
	}

	public void setnZCoord(int nZCoord) {
		this.nZCoord = nZCoord;
	}

	public int getnUCoord() {
		return nUCoord;
	}

	public void setnUCoord(int nUCoord) {
		this.nUCoord = nUCoord;
	}

	public int getnXCoord1() {
		return nXCoord1;
	}

	public void setnXCoord1(int nXCoord1) {
		this.nXCoord1 = nXCoord1;
	}

	public int getnYCoord1() {
		return nYCoord1;
	}

	public void setnYCoord1(int nYCoord1) {
		this.nYCoord1 = nYCoord1;
	}

	public int getnZCoord1() {
		return nZCoord1;
	}

	public void setnZCoord1(int nZCoord1) {
		this.nZCoord1 = nZCoord1;
	}

	public int getnUCoord1() {
		return nUCoord1;
	}

	public void setnUCoord1(int nUCoord1) {
		this.nUCoord1 = nUCoord1;
	}

	public int getnXCoord2() {
		return nXCoord2;
	}

	public void setnXCoord2(int nXCoord2) {
		this.nXCoord2 = nXCoord2;
	}

	public int getnYCoord2() {
		return nYCoord2;
	}

	public void setnYCoord2(int nYCoord2) {
		this.nYCoord2 = nYCoord2;
	}

	public int getnZCoord2() {
		return nZCoord2;
	}

	public void setnZCoord2(int nZCoord2) {
		this.nZCoord2 = nZCoord2;
	}

	public int getnUCoord2() {
		return nUCoord2;
	}

	public void setnUCoord2(int nUCoord2) {
		this.nUCoord2 = nUCoord2;
	}

	public int getnPulse() {
		return nPulse;
	}

	public void setnPulse(int nPulse) {
		this.nPulse = nPulse;
	}

	public int getnDataLen() {
		return nDataLen;
	}

	public void setnDataLen(int nDataLen) {
		this.nDataLen = nDataLen;
	}

	public int getnTaskNum() {
		return nTaskNum;
	}

	public void setnTaskNum(int nTaskNum) {
		this.nTaskNum = nTaskNum;
	}

	public int getnMoveDir() {
		return nMoveDir;
	}

	public void setnMoveDir(int nMoveDir) {
		this.nMoveDir = nMoveDir;
	}

	public int getnMoveType() {
		return nMoveType;
	}

	public void setnMoveType(int nMoveType) {
		this.nMoveType = nMoveType;
	}

	public int getnMoveCoord() {
		return nMoveCoord;
	}

	public void setnMoveCoord(int nMoveCoord) {
		this.nMoveCoord = nMoveCoord;
	}

	public int getnHeight() {
		return nHeight;
	}

	public void setnHeight(int nHeight) {
		this.nHeight = nHeight;
	}

	public int getnType() {
		return nType;
	}

	public void setnType(int nType) {
		this.nType = nType;
	}

	public int getnFlag() {
		return nFlag;
	}

	public void setnFlag(int nFlag) {
		this.nFlag = nFlag;
	}

	public int getnIOPortStatue() {
		return nIOPortStatue;
	}

	public void setnIOPortStatue(int nIOPortStatue) {
		this.nIOPortStatue = nIOPortStatue;
	}

	public int getnIOflag() {
		return nIOflag;
	}

	public void setnIOflag(int nIOflag) {
		this.nIOflag = nIOflag;
	}
	//-----------------------------------------------
	public int getnZeroCheck() {
		return nZeroCheck;
	}

	public void setnZeroCheck(int nZeroCheck) {
		this.nZeroCheck = nZeroCheck;
	}

	public int getnAccelerate() {
		return nAccelerate;
	}

	public void setnAccelerate(int nAccelerate) {
		this.nAccelerate = nAccelerate;
	}

	public int getnDecelerate() {
		return nDecelerate;
	}

	public void setnDecelerate(int nDecelerate) {
		this.nDecelerate = nDecelerate;
	}

	public int getnBaseUnit() {
		return nBaseUnit;
	}

	public void setnBaseUnit(int nBaseUnit) {
		this.nBaseUnit = nBaseUnit;
	}

	public int getnBaseHeight() {
		return nBaseHeight;
	}

	public void setnBaseHeight(int nBaseHeight) {
		this.nBaseHeight = nBaseHeight;
	}

	public int getnXYNullSpeed() {
		return nXYNullSpeed;
	}

	public void setnXYNullSpeed(int nXYNullSpeed) {
		this.nXYNullSpeed = nXYNullSpeed;
	}

	public int getnZNullSpeed() {
		return nZNullSpeed;
	}

	public void setnZNullSpeed(int nZNullSpeed) {
		this.nZNullSpeed = nZNullSpeed;
	}

	public int getnUNullSpeed() {
		return nUNullSpeed;
	}

	public void setnUNullSpeed(int nUNullSpeed) {
		this.nUNullSpeed = nUNullSpeed;
	}

	public int getnAutoRunTime() {
		return nAutoRunTime;
	}

	public void setnAutoRunTime(int nAutoRunTime) {
		this.nAutoRunTime = nAutoRunTime;
	}

	public int getnTurnAccelerateMax() {
		return nTurnAccelerateMax;
	}

	public void setnTurnAccelerateMax(int nTurnAccelerateMax) {
		this.nTurnAccelerateMax = nTurnAccelerateMax;
	}

	public boolean isbTaskBack() {
		return bTaskBack;
	}

	public void setbTaskBack(boolean bTaskBack) {
		this.bTaskBack = bTaskBack;
	}

	public boolean isbTaskDelete() {
		return bTaskDelete;
	}

	public void setbTaskDelete(boolean bTaskDelete) {
		this.bTaskDelete = bTaskDelete;
	}

	public int getnYCheckDis() {
		return nYCheckDis;
	}

	public void setnYCheckDis(int nYCheckDis) {
		this.nYCheckDis = nYCheckDis;
	}

	public int getnRunNum() {
		return nRunNum;
	}

	public void setnRunNum(int nRunNum) {
		this.nRunNum = nRunNum;
	}

	public boolean isbRunNumZero() {
		return bRunNumZero;
	}

	public void setbRunNumZero(boolean bRunNumZero) {
		this.bRunNumZero = bRunNumZero;
	}

	public int getnPauseType() {
		return nPauseType;
	}

	public void setnPauseType(int nPauseType) {
		this.nPauseType = nPauseType;
	}

	public boolean isbBackDefault() {
		return bBackDefault;
	}

	public void setbBackDefault(boolean bBackDefault) {
		this.bBackDefault = bBackDefault;
	}

	public int getnNoHardOutGlueTime() {
		return nNoHardOutGlueTime;
	}

	public void setnNoHardOutGlueTime(int nNoHardOutGlueTime) {
		this.nNoHardOutGlueTime = nNoHardOutGlueTime;
	}

	public int getnNoHardOutGlueInterval() {
		return nNoHardOutGlueInterval;
	}

	public void setnNoHardOutGlueInterval(int nNoHardOutGlueInterval) {
		this.nNoHardOutGlueInterval = nNoHardOutGlueInterval;
	}
	
	/**
	 * 设置默认值
	 */
	public void setAllParamToZero(){
		this.nSpeed = 0; // 移动速度：mm/s
		this.nXCoord = 0; // x坐标
		this.nYCoord = 0; // y坐标
		this.nZCoord = 0; // z坐标
		this.nUCoord = 0; // u坐标
		this.nXCoord1 = 0; // x坐标
		this.nYCoord1 = 0; // y坐标
		this.nZCoord1 = 0; // z坐标
		this.nUCoord1 = 0; // u坐标
		this.nXCoord2 = 0; // x坐标
		this.nYCoord2 = 0; // y坐标
		this.nZCoord2 = 0; // z坐标
		this.nUCoord2 = 0; // u坐标
		this.nPulse = 0; // 单步脉冲数
		this.nDataLen = 0; // 任务数据长度：字节数
		this.nTaskNum = 0; // 任务号
		this.nMoveDir = 0; // 运动方向：0-正、1-负
		this.nMoveType = 0; // 运动方式：0-连续、1-单步
		this.nMoveCoord = 0; // 运动坐标：0-x轴，1-y轴，2-z轴，3-u轴
		this.nHeight = 0; // 循迹高度
		this.nType = 0; // 循迹类型
		this.nFlag = 0; // 是否空走标记
		this.nIOPortStatue = 0; // 4路输出口状态 2014/12/29
		this.nIOflag = 0; // 待定 2014/12/30

		this.nZeroCheck = 0;			// 零点校正
		this.nAccelerate = 0;		// 加速度
		this.nDecelerate = 0;		// 减速度
		this.nBaseUnit = 0;			// 基点调整单位
		this.nBaseHeight = 0;		// 基点调整高度
		this.nXYNullSpeed = 0;		// xy轴空走速度
		this.nZNullSpeed = 0;		// z轴空走速度
		this.nUNullSpeed = 0;		// u轴空走速度
		this.nAutoRunTime = 0;		// 自动运行延时时间
		this.nTurnAccelerateMax = 0;	// 拐点最大加速度
		this.bTaskBack = false;			// 任务还原
		this.bTaskDelete = false;		// 任务删除
		this.nYCheckDis = 0;			// Y轴定位校正距离
		this.nRunNum = 0;			// 任务运行记数
		this.bRunNumZero = false;		// 任务运行记数清零
		int nPauseType = 0;			// 暂停类型
		this.bBackDefault = false;		// 恢复出厂默认值
		this.nNoHardOutGlueTime = 0;	// 硬化防止出胶时间
		this.nNoHardOutGlueInterval = 0;	// 硬化防止间隔时间
	}

}

//	private int m_mainPanelType; // 主控板
//	private int m_softWareVertsion; // 软件版本号
//	private int m_machineStreamNum; // 机器流水号
//	private int m_drivePanelType; // 驱动板
//	private int m_year; // 年
//	private int m_month; // 月
//	private int m_day; // 日
//	private int m_xJourney; // x轴最大行程(单位mm)
//	private int m_yJourney; // y轴最大行程
//	private int m_zJourney; // z轴最大行程
//	private int m_uJourney; // u轴最大行程
//	private int m_xySpeed; // xy轴最大运行速度
//	private int m_zSpeed; // z轴最大运行速度
//	private int m_uSpeed; // u轴最大运行速度
//	private double m_xDifferentiate; // x轴分辨率(单位mm)
//	private double m_yDifferentiate; // y轴分辨率
//	private double m_zDifferentiate; // z轴分辨率
//	private double m_uDifferentiate; // u轴分辨率
//	private int m_nAxisNum; // 机器人运动轴数
//	private int m_nRobotType; // 机器人型号名
//	private int m_RobotTypeNum; // 机器人型号序号
//	private int m_defaultResetSpeed; // 默认复位速度
//	private int m_resetBackDistance; // 复位回退距离
//	private int m_specialConfigExplain; // 特殊配置说明
//	private int m_reservedInt01; // 预留1
//	private int m_reservedInt02; // 预留2