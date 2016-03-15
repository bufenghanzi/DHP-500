package com.mingseal.data.param.robot;

import com.mingseal.data.protocol.Protocol_400_1;

/**
 * 机器参数类
 * 
 * @author lyq
 */
public enum RobotParam {

	INSTANCE;

	private final int MAX_JOURNEY = 3000;// 最大行程
	private final int MAX_SPEED = 3000;// 最大速度
	private final double MAX_DIFFERENTIATE = 1.0;// 最大分辨率

	// private int m_mainPanelType; // 主控板
	// private int m_softWareVersion; // 软件版本
	// private int m_machineStreamNum; // 机器流水号
	// private int m_drivePanelType; // 驱动板
	// private int m_year; // 年
	// private int m_month; // 月
	// private int m_day; // 日
	private int m_xJourney; // x轴最大行程(单位：mm)
	private int m_yJourney; // y轴最大行程
	private int m_zJourney; // z轴最大行程
	private int m_uJourney; // u轴最大行程
	// private int m_xySpeed; // xy轴最大运行速度(单位：mm/s)
	private int m_xSpeed;// x轴最大运行速度
	private int m_ySpeed;// y轴最大运行速度
	private int m_zSpeed; // z轴最大运行速度
	private int m_uSpeed; // u轴最大运行速度
	private double m_xDifferentiate; // x轴分辨率(单位：mm/p)
	private double m_yDifferentiate; // y轴分辨率
	private double m_zDifferentiate; // z轴分辨率
	private double m_uDifferentiate; // u轴分辨率
	private int m_nAxisNum; // 机器人运动轴数
	// private int m_RobotTypeNum; // 机器人型号序号//2015/01/22
	// private int m_defaultResetSpeed; // 默认复位速度//2015/01/22
	// private int m_resetBackDistance; // 复位回退距离//2015/01/22
	// private int m_specialConfigExplain; // 特殊配置说明//2015/01/22
	// private int m_reservedInt01; // 预留1//2015/01/22
	// private int m_reservedInt02; // 预留2//2015/01/22
	private long m_BsDis;// 回退距离
	private int m_nAccelerateMax;// 最大加速度
	private RobotType m_nRobotType; // 机器人型号名
	private RobotSeries m_enRobotSeries; // 机器人系列
	private String strRobotName;

	public int m_softVersion;// 机器版本 2015.7.2

	private RobotParam() {
		m_xJourney = 200;
		m_yJourney = 200;
		m_zJourney = 80;
		m_uJourney = 200;
		m_xSpeed = 200;
		m_ySpeed = 200;
		m_zSpeed = 200;
		m_uSpeed = 200;
		m_xDifferentiate = 0.0045;
		m_yDifferentiate = 0.0045;
		m_zDifferentiate = 0.0035;
		m_uDifferentiate = 1.00;
		m_nAxisNum = 4;
		m_nAccelerateMax = 10000;
		m_nRobotType = RobotType.DJ0000;
		m_enRobotSeries = RobotSeries.Series_400;
		m_BsDis = 0;
	}

	public boolean InitRobot(byte[] buff) {
		m_xDifferentiate = ((double) Protocol_400_1.READ4BYTES_R(buff, 17) / 10000000);
		m_xJourney = Protocol_400_1.READ2BYTES_R(buff, 21);
		m_yDifferentiate = ((double) Protocol_400_1.READ4BYTES_R(buff, 23) / 10000000);
		m_yJourney = Protocol_400_1.READ2BYTES_R(buff, 27);
		m_zDifferentiate = ((double) Protocol_400_1.READ4BYTES_R(buff, 29) / 10000000);
		m_zJourney = Protocol_400_1.READ2BYTES_R(buff, 33);
		m_uDifferentiate = ((double) Protocol_400_1.READ4BYTES_R(buff, 35) / 10000000);
		m_uJourney = Protocol_400_1.READ2BYTES_R(buff, 39);

		m_xSpeed = Protocol_400_1.READ2BYTES_R(buff, 41);
		m_ySpeed = Protocol_400_1.READ2BYTES_R(buff, 41);
		m_zSpeed = Protocol_400_1.READ2BYTES_R(buff, 43);
		m_uSpeed = Protocol_400_1.READ2BYTES_R(buff, 45);

		m_nAxisNum = Protocol_400_1.READ2BYTES_R(buff, 47);
		if (m_nAxisNum == 0) {
			m_nAxisNum = 4;
		}
		String cs = new String(buff, 53, 10);
		if (cs.charAt(0) != 'D') {
			return false;
		}
		if (cs.substring(0, 1).compareTo("DH") == 0) {
			m_nRobotType = RobotType.DH200C;
		}
		if (cs.indexOf("DS200C") != -1) {
			m_nRobotType = RobotType.DS200C;
		}
		if (cs.indexOf("DS300C") != -1) {
			m_nRobotType = RobotType.DS300C;
		}
		if (cs.indexOf("DS400C") != -1) {
			m_nRobotType = RobotType.DS400C;
		}
		if (cs.indexOf("DS500C") != -1) {
			m_nRobotType = RobotType.DS500C;
		}

		StringBuffer cb = new StringBuffer(cs);
		for (int i = 0; i < cb.length(); i++) {
			if (cb.charAt(i) == 0) {
				cb.setCharAt(i, ' ');
			}
		}
		strRobotName = cb.toString();

		m_nAccelerateMax = 10000;/// *READ2BYTES_R(_buf, 69)*/不再关联最大加速度
		m_BsDis = Protocol_400_1.READ2BYTES_R(buff, 71);
		m_softVersion = Protocol_400_1.READ2BYTES_R(buff, 73);// 机器人版本 0:老版本
																// 1:新版本(包含清胶点)
		return true;
	}

	public int getMAX_JOURNEY() {
		return MAX_JOURNEY;
	}

	public int getMAX_SPEED() {
		return MAX_SPEED;
	}

	public double getMAX_DIFFERENTIATE() {
		return MAX_DIFFERENTIATE;
	}

	// public int getM_mainPanelType() {
	// return m_mainPanelType;
	// }
	//
	// public int getM_softWareVersion() {
	// return m_softWareVersion;
	// }
	//
	// public int getM_machineStreamNum() {
	// return m_machineStreamNum;
	// }
	//
	// public int getM_drivePanelType() {
	// return m_drivePanelType;
	// }
	//
	// public int getM_year() {
	// return m_year;
	// }
	//
	// public int getM_month() {
	// return m_month;
	// }
	//
	// public int getM_day() {
	// return m_day;
	// }

	/**
	 * @return X轴最大行程
	 */
	public int GetXJourney() {
		return (m_xJourney > 0 && m_xJourney <= MAX_JOURNEY) ? m_xJourney : MAX_JOURNEY;
	}

	/**
	 * @return Y轴最大行程
	 */
	public int GetYJourney() {
		return (m_yJourney > 0 && m_yJourney <= MAX_JOURNEY) ? m_yJourney : MAX_JOURNEY;
	}

	/**
	 * @return Z轴最大行程
	 */
	public int GetZJourney() {
		return (m_zJourney > 0 && m_zJourney <= MAX_JOURNEY) ? m_zJourney : MAX_JOURNEY;
	}

	/**
	 * @return U轴最大行程
	 */
	public int GetUJourney() {
		return (m_uJourney > 0 && m_uJourney <= MAX_JOURNEY) ? m_uJourney : MAX_JOURNEY;
	}

	/**
	 * <p>
	 * Title: VerifyXJourney
	 * <p>
	 * Description: 检查x轴行程范围(单位:mm)
	 * 
	 * @param xJourney
	 *            x轴行程值
	 * @return 检查后x轴行程值
	 */
	public int VerifyXJourney(int xJourney) {
		if (xJourney < 0)
			return 0;
		if (xJourney > GetXJourney())
			return GetXJourney();
		return xJourney;
	}

	/**
	 * <p>
	 * Title: VerifyYJourney
	 * <p>
	 * Description: 检查y轴行程范围(单位:mm)
	 * 
	 * @param yJourney
	 *            y轴行程值
	 * @return 检查后y轴行程值
	 */
	public int VerifyYJourney(int yJourney) {
		if (yJourney < 0)
			return 0;
		if (yJourney > GetYJourney())
			return GetYJourney();
		return yJourney;
	}

	/**
	 * <p>
	 * Title: VerifyZJourney
	 * <p>
	 * Description: 检查z轴行程范围(单位:mm)
	 * 
	 * @param zJourney
	 *            y轴行程值
	 * @return 检查后y轴行程值
	 */
	public int VerifyZJourney(int zJourney) {
		if (zJourney < 0)
			return 0;
		if (zJourney > GetZJourney())
			return GetZJourney();
		return zJourney;
	}

	/**
	 * <p>
	 * Title: VerifyUJourney
	 * <p>
	 * Description: 检查u轴行程范围(单位:mm)
	 * 
	 * @param uJourney
	 *            u轴行程值
	 * @return 检查后u轴行程值
	 */
	public int VerifyUJourney(int uJourney) {
		if (uJourney < 0)
			return 0;
		if (uJourney > GetUJourney())
			return GetUJourney();
		return uJourney;
	}
	

	/**
	 * <p>
	 * Title: GetXSpeed
	 * <p>
	 * Description: 获取x轴最大运行速度
	 * 
	 * @return
	 */
	public int GetXSpeed() {
		return (m_xSpeed > 0 && m_xSpeed <= MAX_SPEED) ? m_xSpeed : MAX_SPEED;
	}

	/**
	 * <p>
	 * Title: GetYSpeed
	 * <p>
	 * Description: 获取y轴运行速度
	 * 
	 * @return
	 */
	public int GetYSpeed() {
		return (m_ySpeed > 0 && m_ySpeed <= MAX_SPEED) ? m_ySpeed : MAX_SPEED;
	}

	/**
	 * <p>
	 * Title: GetZSpeed
	 * <p>
	 * Description: 获取z轴运行速度
	 * 
	 * @return
	 */
	public int GetZSpeed() {
		return (m_zSpeed > 0 && m_zSpeed <= MAX_SPEED) ? m_zSpeed : MAX_SPEED;
	}

	/**
	 * <p>
	 * Title: GetUSpeed
	 * <p>
	 * Description: 获取u轴运行速度
	 * 
	 * @return
	 */
	public int GetUSpeed() {
		return (m_uSpeed > 0 && m_uSpeed <= MAX_SPEED) ? m_uSpeed : MAX_SPEED;
	}

	/**
	 * <p>
	 * Title: VerifyXSpeed
	 * <p>
	 * Description: 检查速度范围
	 * 
	 * @param xSpeed
	 *            x轴速度值
	 * @return
	 */
	public int VerifyXSpeed(int xSpeed) {
		if (xSpeed < 0)
			return 0;
		if (xSpeed > GetXSpeed())
			return GetXSpeed();
		return xSpeed;
	}

	/**
	 * <p>
	 * Title: VerifyYSpeed
	 * <p>
	 * Description: 检查速度范围
	 * 
	 * @param xSpeed
	 *            y轴速度值
	 * @return
	 */
	public int VerifyYSpeed(int ySpeed) {
		if (ySpeed < 0)
			return 0;
		if (ySpeed > GetYSpeed())
			return GetYSpeed();
		return ySpeed;
	}

	/**
	 * <p>
	 * Title: VerifyZSpeed
	 * <p>
	 * Description: 检查速度范围
	 * 
	 * @param xSpeed
	 *            z轴速度值
	 * @return
	 */
	public int VerifyZSpeed(int zSpeed) {
		if (zSpeed < 0)
			return 0;
		if (zSpeed > GetZSpeed())
			return GetZSpeed();
		return zSpeed;
	}

	/**
	 * <p>
	 * Title: VerifyUSpeed
	 * <p>
	 * Description: 检查速度范围
	 * 
	 * @param xSpeed
	 *            u轴速度值
	 * @return
	 */
	public int VerifyUSpeed(int uSpeed) {
		if (uSpeed < 0)
			return 0;
		if (uSpeed > GetUSpeed())
			return GetUSpeed();
		return uSpeed;
	}

	// public int getM_xySpeed() {
	// return m_xySpeed;
	// }

	/**
	 * <p>
	 * Title: GetXDifferentiate
	 * <p>
	 * Description: 获取X轴分辨率
	 * 
	 * @return
	 */
	public double GetXDifferentiate() {
		return (m_xDifferentiate > 0 && m_xDifferentiate <= MAX_DIFFERENTIATE) ? m_xDifferentiate : MAX_DIFFERENTIATE;
//		return 0.0045;
	}

	/**
	 * <p>
	 * Title: GetYDifferentiate
	 * <p>
	 * Description: 获取Y轴分辨率
	 * 
	 * @return
	 */
	public double GetYDifferentiate() {
		return (m_yDifferentiate > 0 && m_yDifferentiate <= MAX_DIFFERENTIATE) ? m_yDifferentiate : MAX_DIFFERENTIATE;
//		return 0.0045;
	}

	/**
	 * <p>
	 * Title: GetZDifferentiate
	 * <p>
	 * Description: 获取Z轴分辨率
	 * 
	 * @return
	 */
	public double GetZDifferentiate() {
		return (m_zDifferentiate > 0 && m_zDifferentiate <= MAX_DIFFERENTIATE) ? m_zDifferentiate : MAX_DIFFERENTIATE;
	}

	/**
	 * <p>
	 * Title: GetUDifferentiate
	 * <p>
	 * Description: 获取U轴分辨率
	 * 
	 * @return
	 */
	public double GetUDifferentiate() {
		return (m_uDifferentiate > 0 && m_uDifferentiate <= MAX_DIFFERENTIATE) ? m_uDifferentiate : MAX_DIFFERENTIATE;
	}

	/**
	 * <p>
	 * Title: VerifyXPulse
	 * <p>
	 * Description: 检查x轴行程脉冲范围
	 * 
	 * @param xPulse
	 * @return
	 */
	public int VerifyXPulse(int xPulse) {
		if (xPulse < 0)
			return 0;
		if (xPulse > (GetXJourney() / GetXDifferentiate() + 0.5))
			return (int) (GetXJourney() / GetXDifferentiate() + 0.5);
		return xPulse;
	}

	/**
	 * <p>
	 * Title: VerifyYPulse
	 * <p>
	 * Description: 检查y轴行程脉冲范围
	 * 
	 * @param yPulse
	 * @return
	 */
	public int VerifyYPulse(int yPulse) {
		if (yPulse < 0)
			return 0;
		if (yPulse > (GetYJourney() / GetYDifferentiate() + 0.5))
			return (int) (GetYJourney() / GetYDifferentiate() + 0.5);
		return yPulse;
	}

	/**
	 * <p>
	 * Title: VerifyZPulse
	 * <p>
	 * Description: 检查z轴行程脉冲范围
	 * 
	 * @param zPulse
	 * @return
	 */
	public int VerifyZPulse(int zPulse) {
		if (zPulse < 0)
			return 0;
		if (zPulse > (GetZJourney() / GetZDifferentiate() + 0.5))
			return (int) (GetZJourney() / GetZDifferentiate() + 0.5);
		return zPulse;
	}

	/**
	 * <p>
	 * Title: VerifyUPulse
	 * <p>
	 * Description: 检查u轴行程脉冲范围
	 * 
	 * @param uPulse
	 * @return
	 */
	public int VerifyUPulse(int uPulse) {
		if (uPulse < -(GetUJourney() / GetUDifferentiate() + 0.5))
			return (int) -(GetUJourney() / GetUDifferentiate() + 0.5);
		if (uPulse > (GetUJourney() / GetUDifferentiate() + 0.5))
			return (int) (GetUJourney() / GetUDifferentiate() + 0.5);
		return uPulse;
	}

	/**
	 * <p>
	 * Title: XJourney2Pulse
	 * <p>
	 * Description: x轴行程转脉冲
	 * 
	 * @param xJourney
	 *            x轴行程
	 * @return
	 */
	public int XJourney2Pulse(double xJourney) {
		int pulse = (int) (xJourney / GetXDifferentiate() + 0.5);
		pulse = VerifyXPulse(pulse);
		return pulse;
	}

	/**
	 * 只用于设置圆心时，x轴行程转脉冲
	 * 
	 * @param xJourney
	 * @return
	 */
	public int XCenterJourney2Pulse(double xJourney) {
		int pulse = (int) (xJourney / GetXDifferentiate() + 0.5);
		return pulse;
	}

	/**
	 * <p>
	 * Title: YJourney2Pulse
	 * <p>
	 * Description: y轴行程转脉冲
	 * 
	 * @param yJourney
	 *            y轴行程
	 * @return
	 */
	public int YJourney2Pulse(double yJourney) {
		int pulse = (int) (yJourney / GetYDifferentiate() + 0.5);
		pulse = VerifyYPulse(pulse);
		return pulse;
	}
	
	/**
	 * 只用于设置圆心时，y轴行程转脉冲
	 * 
	 * @param xJourney
	 * @return
	 */
	public int YCenterJourney2Pulse(double yJourney) {
		int pulse = (int) (yJourney / GetXDifferentiate() + 0.5);
		return pulse;
	}

	/**
	 * <p>
	 * Title: ZJourney2Pulse
	 * <p>
	 * Description: z轴行程转脉冲
	 * 
	 * @param zJourney
	 *            z轴行程
	 * @return
	 */
	public int ZJourney2Pulse(double zJourney) {
		int pulse = (int) (zJourney / GetZDifferentiate() + 0.5);
		pulse = VerifyZPulse(pulse);
		return pulse;
	}
	
	/**
	 * 只用于设置圆心时，z轴行程转脉冲
	 * 
	 * @param zJourney
	 * @return
	 */
	public int ZCenterJourney2Pulse(double zJourney) {
		int pulse = (int) (zJourney / GetXDifferentiate() + 0.5);
		return pulse;
	}

	/**
	 * <p>
	 * Title: UJourney2Pulse
	 * <p>
	 * Description: u轴行程转脉冲
	 * 
	 * @param uJourney
	 *            u轴行程
	 * @return
	 */
	public int UJourney2Pulse(double uJourney) {
		int pulse = (int) (uJourney / GetUDifferentiate() + 0.5);
		pulse = VerifyUPulse(pulse);
		return pulse;
	}
	
	/**
	 * 只用于设置圆心时，u轴行程转脉冲
	 * 
	 * @param uJourney
	 * @return
	 */
	public int UCenterJourney2Pulse(double uJourney) {
		int pulse = (int) (uJourney / GetXDifferentiate() + 0.5);
		return pulse;
	}

	/**
	 * <p>
	 * Title: XPulse2Journey
	 * <p>
	 * Description: x轴脉冲转行程
	 * 
	 * @param xPulse
	 *            x轴脉冲
	 * @return
	 */
	public double XPulse2Journey(int xPulse) {
		xPulse = VerifyXPulse(xPulse);
		return xPulse * GetXDifferentiate();
	}

	/**
	 * 只用于圆心的x轴脉冲判断
	 * 
	 * @param xPluse
	 * @return
	 */
	public double XCenterPulse2Journey(int xPluse) {
		return xPluse * GetXDifferentiate();
	}

	/**
	 * <p>
	 * Title: YPulse2Journey
	 * <p>
	 * Description: y轴脉冲转行程
	 * 
	 * @param yPulse
	 *            y轴脉冲
	 * @return
	 */
	public double YPulse2Journey(int yPulse) {
		yPulse = VerifyYPulse(yPulse);
		return yPulse * GetYDifferentiate();
	}

	/**
	 * 只用于圆心的y轴脉冲判断
	 * 
	 * @param yPluse
	 * @return
	 */
	public double YCenterPulse2Journey(int yPluse) {
		return yPluse * GetYDifferentiate();
	}

	/**
	 * <p>
	 * Title: ZPulse2Journey
	 * <p>
	 * Description: z轴脉冲转行程
	 * 
	 * @param zPulse
	 *            z轴脉冲
	 * @return
	 */
	public double ZPulse2Journey(int zPulse) {
		zPulse = VerifyZPulse(zPulse);
		return zPulse * GetZDifferentiate();
	}
	
	/**
	 * 只用于圆心的z轴脉冲判断
	 * 
	 * @param zPluse
	 * @return
	 */
	public double ZCenterPulse2Journey(int zPluse) {
		return zPluse * GetZDifferentiate();
	}

	/**
	 * <p>
	 * Title: UPulse2Journey
	 * <p>
	 * Description: u轴脉冲转行程
	 * 
	 * @param uPulse
	 *            u轴脉冲
	 * @return
	 */
	public double UPulse2Journey(int uPulse) {
		uPulse = VerifyUPulse(uPulse);
		return uPulse * GetUDifferentiate();
	}
	/**
	 * 只用于圆心的u轴脉冲判断
	 * 
	 * @param uPluse
	 * @return
	 */
	public double UCenterPulse2Journey(int uPluse) {
		return uPluse * GetUDifferentiate();
	}

	/**
	 * @Title getM_nAxisNum
	 * @Description 获取机器轴数
	 * @return 机器轴数
	 */
	public int getM_nAxisNum() {
		return m_nAxisNum;
	}

	public long getM_BsDis() {
		return m_BsDis;
	}

	public int getM_nAccelerateMax() {
		return m_nAccelerateMax;
	}

	// public int getM_RobotTypeNum() {
	// return m_RobotTypeNum;
	// }
	//
	// public int getM_defaultResetSpeed() {
	// return m_defaultResetSpeed;
	// }
	//
	// public int getM_resetBackDistance() {
	// return m_resetBackDistance;
	// }
	//
	// public int getM_specialConfigExplain() {
	// return m_specialConfigExplain;
	// }
	//
	// public int getM_reservedInt01() {
	// return m_reservedInt01;
	// }
	//
	// public int getM_reservedInt02() {
	// return m_reservedInt02;
	// }

	public RobotType getM_nRobotType() {
		return m_nRobotType;
	}

	public RobotSeries getM_enRobotSeries() {
		return m_enRobotSeries;
	}

	public String getStrRobotName() {
		return strRobotName;
	}

	// function

}
