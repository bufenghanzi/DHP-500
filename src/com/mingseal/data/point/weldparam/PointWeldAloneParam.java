package com.mingseal.data.point.weldparam;

import com.mingseal.data.point.GWOutPort;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

/**
 * 焊锡作业点
 * @author lyq
 */
public class PointWeldAloneParam extends PointParam {
	
	private int preHeatTime;					//预热时间(单位:毫秒ms)
	private int sendSnSpeedFir;			//一次送锡速度(单位:毫米/秒 mm/s)
	private int sendSnSumFir;				//一次送锡量(单位:丝米dmm)
	private int sendSnSpeedSec;			//二次送锡速度(单位:毫米/秒 mm/s)
	private int sendSnSumSec;			//二次送锡量(单位:丝米dmm)
	private int stopSnStimeSec;			//二次停锡时间
	private int sendSnSpeedThird;		//三次送锡速度(单位:毫米/秒 mm/s)
	private int sendSnSumThird;			//三次送锡量(单位:丝米dmm)
	private int stopSnTimeThird;			//三次停锡时间
	private int sendSnSpeedFourth;	//四次送锡速度(单位:毫米/秒 mm/s)
	private int sendSnSumFourth;		//四次送锡量(单位:丝米dmm)
	private int stopSnTimeFourth;		//四次停锡时间
	private int dipDistance;					//倾斜距离(单位:mm)
	private int stopSnTime;					//停锡延时(单位:mm)
	private int upHeight;						//抬起高度(单位:mm)
	private boolean isSn;						//是否出锡
	private boolean isSlow;					//焊点结束后是否自动暂停标志		0: 不暂停		1: 暂停
	private boolean isOut;					//焊点结束退出时是否抬起停顿标志	0: 不抬起停顿	1:抬起停顿
	private boolean isStp;						//焊点结束后是否有输出标志			0: 有输出		1:无输出
	private boolean isSus;					//下降焊锡前是否减速标志			0: 不减速		1:减速
	private boolean[] snPort;				//焊锡口
	private boolean isPause;				//是否暂停
	
	/**
	 * @return 获取预热时间(单位:毫秒ms)
	 */
	public int getPreHeatTime() {
		return preHeatTime;
	}
	
	/**
	 * 设置预热时间(单位:毫秒ms)
	 * @param preHeatTime 预热时间
	 */
	public void setPreHeatTime(int preHeatTime) {
		this.preHeatTime = preHeatTime;
	}
	
	/**
	 * @return 获取一次送锡速度(单位:毫米/秒 mm/s)
	 */
	public int getSendSnSpeedFir() {
		return sendSnSpeedFir;
	}
	
	/**
	 * 设置一次送锡速度
	 * @param sendSnSpeedFir 一次送锡速度
	 */
	public void setSendSnSpeedFir(int sendSnSpeedFir) {
		this.sendSnSpeedFir = sendSnSpeedFir;
	}
	
	/**
	 * @return 获取一次送锡量(单位:丝米dmm)
	 */
	public int getSendSnSumFir() {
		return sendSnSumFir;
	}
	
	/**
	 * 设置一次送锡量(单位:丝米dmm)
	 * @param sendSnSumFir 一次送锡量(单位:丝米dmm)
	 */
	public void setSendSnSumFir(int sendSnSumFir) {
		this.sendSnSumFir = sendSnSumFir;
	}
	
	/**
	 * @return 获取二次送锡速度(单位:毫米/秒 mm/s)
	 */
	public int getSendSnSpeedSec() {
		return sendSnSpeedSec;
	}
	
	/**
	 * 设置二次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSpeedSec 二次送锡速度
	 */
	public void setSendSnSpeedSec(int sendSnSpeedSec) {
		this.sendSnSpeedSec = sendSnSpeedSec;
	}
	
	/**
	 * @return 获取二次送锡量(单位:丝米dmm)
	 */
	public int getSendSnSumSec() {
		return sendSnSumSec;
	}
	
	/**
	 * 设置二次送锡量(单位:丝米dmm)
	 * @param sendSnSumSec 二次送锡量
	 */
	public void setSendSnSumSec(int sendSnSumSec) {
		this.sendSnSumSec = sendSnSumSec;
	}
	
	/**
	 * @return 获取二次停锡时间
	 */
	public int getStopSnStimeSec() {
		return stopSnStimeSec;
	}
	
	/**
	 * 设置二次停锡时间
	 * @param stopSnStimeSec 二次停锡时间
	 */
	public void setStopSnStimeSec(int stopSnStimeSec) {
		this.stopSnStimeSec = stopSnStimeSec;
	}
	
	/**
	 * @return 获取三次送锡速度(单位:毫米/秒 mm/s)
	 */
	public int getSendSnSpeedThird() {
		return sendSnSpeedThird;
	}
	
	/**
	 * 设置三次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSpeedThird 三次送锡速度
	 */
	public void setSendSnSpeedThird(int sendSnSpeedThird) {
		this.sendSnSpeedThird = sendSnSpeedThird;
	}
	
	/**
	 * @return 获取三次送锡量(单位:丝米dmm)
	 */
	public int getSendSnSumThird() {
		return sendSnSumThird;
	}
	
	/**
	 * 设置三次送锡量(单位:丝米dmm)
	 * @param sendSnSumThird 三次送锡量
	 */
	public void setSendSnSumThird(int sendSnSumThird) {
		this.sendSnSumThird = sendSnSumThird;
	}
	
	/**
	 * @return 获取三次停锡时间
	 */
	public int getStopSnTimeThird() {
		return stopSnTimeThird;
	}
	
	/**
	 * 设置三次停锡时间
	 * @param stopSnTimeThird 三次停锡时间
	 */
	public void setStopSnTimeThird(int stopSnTimeThird) {
		this.stopSnTimeThird = stopSnTimeThird;
	}
	
	/**
	 * @return 获取四次送锡速度(单位:毫米/秒 mm/s)
	 */
	public int getSendSnSpeedFourth() {
		return sendSnSpeedFourth;
	}
	
	/**
	 * 设置四次送锡速度(单位:毫米/秒 mm/s)
	 * @param sendSnSpeedFourth 四次送锡速度
	 */
	public void setSendSnSpeedFourth(int sendSnSpeedFourth) {
		this.sendSnSpeedFourth = sendSnSpeedFourth;
	}
	
	/**
	 * @return 获取四次送锡量(单位:丝米dmm)
	 */
	public int getSendSnSumFourth() {
		return sendSnSumFourth;
	}
	
	/**
	 * 设置四次送锡量(单位:丝米dmm)
	 * @param sendSnSumFourth 四次送锡量
	 */
	public void setSendSnSumFourth(int sendSnSumFourth) {
		this.sendSnSumFourth = sendSnSumFourth;
	}
	
	/**
	 * @return 获取四次停锡时间
	 */
	public int getStopSnTimeFourth() {
		return stopSnTimeFourth;
	}
	
	/**
	 * 设置四次停锡时间
	 * @param stopSnTimeFourth 四次停锡时间
	 */
	public void setStopSnTimeFourth(int stopSnTimeFourth) {
		this.stopSnTimeFourth = stopSnTimeFourth;
	}
	
	/**
	 * @return 获取倾斜距离(单位:mm)
	 */
	public int getDipDistance() {
		return dipDistance;
	}
	
	/**
	 * 设置倾斜距离(单位:mm)
	 * @param dipDistance 倾斜距离
	 */
	public void setDipDistance(int dipDistance) {
		this.dipDistance = dipDistance;
	}
	
	/**
	 * @return 获取停锡延时(单位:mm)
	 */
	public int getStopSnTime() {
		return stopSnTime;
	}
	
	/**
	 * 设置停锡延时(单位:mm)
	 * @param stopSnTime 停锡延时
	 */
	public void setStopSnTime(int stopSnTime) {
		this.stopSnTime = stopSnTime;
	}
	
	/**
	 * @return 获取抬起高度(单位:mm)
	 */
	public int getUpHeight() {
		return upHeight;
	}
	
	/**
	 * 设置抬起高度(单位:mm)
	 * @param upHeight 抬起高度
	 */
	public void setUpHeight(int upHeight) {
		this.upHeight = upHeight;
	}
	
	/**
	 * @return 获取是否出锡
	 */
	public boolean isSn() {
		return isSn;
	}
	
	/**
	 * 设置是否出锡
	 * @param isSn 是否出锡
	 */
	public void setSn(boolean isSn) {
		this.isSn = isSn;
	}
	
	/**
	 * @return  焊点结束后是否自动暂停标志		0: 不暂停		1: 暂停
	 * @deprecated 新增加的参数,不通用
	 */
	public boolean isSlow() {
		return isSlow;
	}
	
	/**
	 * 焊点结束后是否自动暂停标志		0: 不暂停		1: 暂停
	 * @param isSlow
	 * @deprecated 新增加的参数,不通用
	 */
	public void setSlow(boolean isSlow) {
		this.isSlow = isSlow;
	}
	
	/**
	 * @return 焊点结束退出时是否抬起停顿标志	0: 不抬起停顿	1:抬起停顿
	 * @deprecated 新增加的参数,不通用
	 */
	public boolean isOut() {
		return isOut;
	}
	
	/**
	 * 焊点结束退出时是否抬起停顿标志	0: 不抬起停顿	1:抬起停顿
	 * @param isOut
	 * @deprecated 新增加的参数,不通用
	 */
	public void setOut(boolean isOut) {
		this.isOut = isOut;
	}
	
	/**
	 * @return 焊点结束后是否有输出标志			0: 有输出		1:无输出
	 * @deprecated 新增加的参数,不通用
	 */
	public boolean isStp() {
		return isStp;
	}
	
	/**
	 * 焊点结束后是否有输出标志			0: 有输出		1:无输出
	 * @param isStp
	 * @deprecated 新增加的参数,不通用
	 */
	public void setStp(boolean isStp) {
		this.isStp = isStp;
	}
	
	/**
	 * @return 下降焊锡前是否减速标志			0: 不减速		1:减速
	 * @deprecated 新增加的参数,不通用
	 */
	public boolean isSus() {
		return isSus;
	}
	
	/**
	 * 下降焊锡前是否减速标志			0: 不减速		1:减速
	 * @param isSus
	 * @deprecated 新增加的参数,不通用
	 */
	public void setSus(boolean isSus) {
		this.isSus = isSus;
	}
	
	/**
	 * @return 获取焊锡口数据
	 */
	public boolean[] getSnPort() {
		return snPort;
	}
	
	/**
	 * 设置焊锡口数据
	 * @param snPort 焊锡口
	 */
	public void setSnPort(boolean[] snPort) {
		this.snPort = snPort;
	}
	
	/**
	 * @return 获取是否暂停
	 */
	public boolean isPause() {
		return isPause;
	}
	
	/**
	 * 设置是否暂停
	 * @param isPause 是否暂停
	 */
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
	
	/**
	 * 焊锡作业点默认构造函数,默认值为:
	 * @preHeatTime 预热时间 0
	 * @sendSnSpeedFir 一次送锡速度0
	 * @sendSnSumFir 一次送锡量0
	 * @sendSnSpeedSec 二次送锡速度 0
	 * @sendSnSumSec 二次送锡量 0
	 * @stopSnStimeSec 二次停锡时间 0
	 * @sendSnSpeedThird 三次送锡速度 0
	 * @sendSnSumThird 三次送锡量 0
	 * @stopSnTimeThird 三次停锡时间 0
	 * @sendSnSpeedFourth 四次送锡速度 0
	 * @sendSnSumFourth 四次送锡量 0
	 * @stopSnTimeFourth 四次停锡时间 0
	 * @dipDistance 倾斜距离 0
	 * @stopSnTime 停锡延时 0
	 * @upHeight 抬起高度 0
	 * @isSn 是否出锡 是
	 * @isPause 是否暂停 否
	 * @snPort 10000000000000000000
	 */
	public PointWeldAloneParam(){
		this.preHeatTime = 0;
		this.sendSnSpeedFir = 0;
		this.sendSnSumFir = 0;
		this.sendSnSpeedSec = 0;
		this.sendSnSumSec = 0;
		this.stopSnStimeSec = 0;
		this.sendSnSpeedThird = 0;
		this.sendSnSumThird = 0;
		this.stopSnTimeThird = 0;
		this.sendSnSpeedFourth = 0;
		this.sendSnSumFourth = 0;
		this.stopSnTimeFourth = 0;
		this.dipDistance = 0;
		this.stopSnTime = 0;
		this.upHeight = 0;
		this.isSn = true;
		this.isPause = false;
		super.setPointType(PointType.POINT_WELD_ALONE);
		this.snPort = new boolean[GWOutPort.USER_O_NO_ALL.ordinal()];
		this.snPort[0] = true;
	}
	
	
}
