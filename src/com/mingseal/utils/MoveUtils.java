/**
 * 
 */
package com.mingseal.utils;

import com.mingseal.data.manager.MessageMgr;
import com.mingseal.data.param.OrderParam;
import com.mingseal.data.point.Point;

/**
 * 示教内部方法
 * @author 商炎炳
 *
 */
public class MoveUtils {
	/**
	 * <p>
	 * Title: Move
	 * <p>
	 * Description: 示教内部方法
	 * 
	 * @param moveDir
	 *            移动方向 0:正方向 1:反方向
	 * @param moveType
	 *            移动类型 0:连续 1:单步
	 * @param moveCoord
	 *            移动轴 0:x轴 1:y轴 2:z轴 3:u轴
	 * @param moveSpeed
	 *            移动速度 非询问命令均需要先发送询问下位机是否忙碌后在发送其他命令
	 */
	public static void move(int moveDir, int moveType, int moveCoord, int moveSpeed) {
		OrderParam.INSTANCE.setAllParamToZero();
		OrderParam.INSTANCE.setnMoveDir(moveDir);
		OrderParam.INSTANCE.setnMoveType(moveType);
		OrderParam.INSTANCE.setnMoveCoord(moveCoord);
		switch(moveType){
		case 0:
			OrderParam.INSTANCE.setnSpeed(moveSpeed);
		case 1:
			OrderParam.INSTANCE.setnPulse(moveSpeed);
		}
		MessageMgr.INSTANCE.moveCoord();
	}
	
	/**
	 * <p>
	 * Title: Stop
	 * <p>
	 * Description: 示教停止内部方法
	 * 
	 * @param moveCoord
	 *            移动轴 0:x轴 1:y轴 2:z轴 3:u轴 非询问命令均需要先发送询问下位机是否忙碌后在发送其他命令
	 */
	public static void stop(int moveCoord) {
		OrderParam.INSTANCE.setAllParamToZero();
		OrderParam.INSTANCE.setnMoveCoord(moveCoord);
		MessageMgr.INSTANCE.stopCoord();
	}
	
	/**
	 * 定位
	 * @param point
	 */
	public static void locationCoord(Point point){
		OrderParam.INSTANCE.setnXCoord(point.getX());
		OrderParam.INSTANCE.setnYCoord(point.getY());
		OrderParam.INSTANCE.setnZCoord(point.getZ());
		OrderParam.INSTANCE.setnUCoord(point.getU());
		OrderParam.INSTANCE.setnSpeed(200);
		MessageMgr.INSTANCE.setCurCoord();
	}
}
