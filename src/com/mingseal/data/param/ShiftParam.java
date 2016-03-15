package com.mingseal.data.param;

/** 
* @ClassName: ShiftParam 
* @Description: 偏移参数
* @author lyq
* @date 2015年6月15日 下午4:07:28 
*  
*/
public class ShiftParam {

	private int x; // x轴坐标(脉冲数)
	private int y; // y轴坐标(脉冲数)
	private int z; // z轴坐标(脉冲数)
	private int u; // u轴坐标(脉冲数)

	private int nPreheatTime; // 预热时间(单位:毫秒ms)
	private int nSendSnSpeedFir; // 一次送锡速度(单位:毫米/秒mm/s)
	private int nSendSnSumFir; // 一次送锡量(单位:丝米dmm)
	private int nSendSnSpeedSec; // 二次送锡速度(单位:毫米/秒mm/s)
	private int nSendSnSumSec; // 二次送锡量(单位:丝米dmm)
	private float nRadius; // 圆角半径(单位:mm)
	private int nStopSnTime; // 停锡延时(单位:毫秒ms)
	private int nUpHeight; // 抬起高度(单位:mm)
	private int nMoveSpeed; // 轨迹速度(单位:毫米/秒mm/s)
	private int nBlowSnTime; // 吹锡时间(单位:毫秒ms)
	private int nDipDistance; // 倾斜距离(单位:mm)

	private int nDotGlueTime; // 点胶延时(单位:毫秒ms)
	private int nOutGlueTime; // 出胶延时(单位:毫秒ms)
	private int nStopGlueTime; // 停胶延时(单位:毫秒ms)
	private int nBreakGlueLen; // 断胶长度(单位:毫米mm)
	private int nDrawDistance; // 拉丝距离(单位:毫米mm)
	private int nDrawSpeed; // 拉丝速度(单位:毫米/秒mm/s)

	private int bModeX; // x轴坐标(偏移模式)
	private int bModeY; // y轴坐标(偏移模式)
	private int bModeZ; // z轴坐标(偏移模式)
	private int bModeU; // u轴坐标(偏移模式)

	private int bModePreheatTime; // 预热时间(偏移模式)
	private int bModeSendSnSpeedFir; // 一次送锡速度(偏移模式)
	private int bModeSendSnSumFir; // 一次送锡量(偏移模式)
	private int bModeSendSnSpeedSec; // 二次送锡速度(偏移模式)
	private int bModeSendSnSumSec; // 二次送锡量(偏移模式)
	private float bModeRadius; // 圆角半径(偏移模式)
	private int bModeStopSnTime; // 停锡延时(偏移模式)
	private int bModeUpHeight; // 抬起高度(偏移模式)
	private int bModeMoveSpeed; // 轨迹速度(偏移模式)
	private int bModeBlowSnTime; // 吹锡时间(偏移模式)
	private int bModeDipDistance; // 倾斜距离(偏移模式)

	private int bModeDotGlueTime; // 点胶延时(偏移模式)
	private int bModeOutGlueTime; // 出胶延时(偏移模式)
	private int bModeStopGlueTime; // 停胶延时(偏移模式)
	private int bModeBreakGlueLen; // 断胶长度(偏移模式)
	private int bModeDrawDistance; // 拉丝距离(偏移模式)
	private int bModeDrawSpeed; // 拉丝速度(偏移模式)
}
