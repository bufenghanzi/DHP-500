package com.mingseal.data.protocol;

import com.mingseal.data.param.CmdParam;
import com.mingseal.data.param.OrderParam;
import com.mingseal.utils.DataCheckout;

import android.util.Log;

/**
 * <p>
 * Title: Protocol_400_1
 * <p>
 * Description:通信C协议
 * <p>
 * Company: MingSeal .Ltd
 * 
 * @author lyq
 * @date 2015年10月23日
 */
public class Protocol_400_1 extends Protocol {

	final byte P_400_NULL = (byte) 0x00;
	final byte P_400_HEAD = (byte) 0x01; // 头标记
	final byte P_400_WRITE_S = (byte) 0x06;// 写单个寄存器
	final byte P_400_WRITE_M = (byte) 0x10;// 写多个寄存器
	final byte P_400_READ_S = (byte) 0x03;// 读单个寄存器
	final byte P_400_READ_WRITE = (byte) 0x17;// 读写多个寄存器
	final byte P_400_USER = (byte) 0x65;// 自定义读写寄存器
	final byte P_400_ERROR1 = (byte) 0x86;// 差错码
	final byte P_400_ERROR2 = (byte) 0x90;// 差错码
	final byte P_400_ERROR3 = (byte) 0x83;// 差错码
	final byte P_400_ERROR4 = (byte) 0x97;// 差错码
	final byte P_400_ERROR5 = (byte) 0xE5;// 差错码

	protected int m_nErrorCode; // 错误代码

	@Override
	public int CreaterOrder(byte[] buf, CmdParam cmd) {

		Index index = new Index(0);

		switch (cmd) {
		case Cmd_Ask:// 询问状态
			/**
			 * <p>Title: FillHead
			 * <p>Description: 填充头部命令
			 * @param buf 数据存储数组
			 * @param head 头标记
			 * @param wr 读写标记(单/多寄存器)
			 * @param len 长度
			 * @param cmd 命令类型
			 * @param index 索引
			 * @return
			 */
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			/**
			 * <p>
			 * Title: FillCommand
			 * <p>
			 * Description: 填充命令数据
			 * 
			 * @param buf
			 *            数据数组
			 * @param wr
			 *            读写标记
			 * @param cmd
			 *            命令类型
			 * @param registerNum
			 *            寄存器数量
			 * @param index
			 *            内存填写位置
			 * @return
			 */
			FillCommand(buf, P_400_WRITE_S, 0x7930, (byte) 0x00, index);
			/**
			 * <p>Title: FillData
			 * <p>Description: 填充数据
			 * @param buf 数据存储数组
			 * @param cmd 命令类型
			 * @param index 索引
			 * @return
			 */
			FillData(buf, cmd, index);
			break;
		case Cmd_Reset:// 复位
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x7932, (byte) 0x01, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Move:// 示教运动
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_M, 0x7934, (byte) 0x03, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Stop:// 停止运动
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x7938, (byte) OrderParam.INSTANCE.getnMoveCoord(), index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Demo:// 任务模拟
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_M, 0x793A, (byte) 0x04, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Locate:// 定位
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_M, 0x793F, (byte) 0x09, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Track:// 循迹定位
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_M, 0x7958, (byte) 0x03, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_TrackFinish:// 循迹定位完成
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_NULL, 0x7958, (byte) 0xB5, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_DownLoad:// 任务下载
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_M, 0x7952, (byte) 0x03, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_DeleteTask:// 删除任务
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x7956, (byte) OrderParam.INSTANCE.getnTaskNum(), index);
			FillData(buf, cmd, index);
			break;
		case Cmd_RunTask:// 运行任务
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x795B, (byte) OrderParam.INSTANCE.getnTaskNum(), index);
			FillData(buf, cmd, index);
			break;
		case Cmd_SelTask:// 选择任务
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x7ADC, (byte) OrderParam.INSTANCE.getnTaskNum(), index);
			FillData(buf, cmd, index);
			break;
		case Cmd_IsTaskExist:// 任务是否已存在
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x7931, (byte) OrderParam.INSTANCE.getnTaskNum(), index);
			FillData(buf, cmd, index);
			break;
		case Cmd_UpdateDSP:// 升级下位机DSP
			FillHead(buf, P_400_HEAD, P_400_USER, 0, cmd, index);
			FillCommand(buf, P_400_USER, 0x05, (byte) 0x00, index);
			FillData(buf, cmd, index);
			break;

		case Cmd_Device:// 读取机器类型
			FillHead(buf, P_400_HEAD, P_400_READ_S, 0, cmd, index);
			FillCommand(buf, P_400_READ_S, 0x5220, (byte) 0x25, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Coord:// 读取当前坐标
			FillHead(buf, P_400_HEAD, P_400_READ_S, 0, cmd, index);
			FillCommand(buf, P_400_READ_S, 0x5245, (byte) 0x0D, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_PreUpLoad:// 任务上传预处理命令
			FillHead(buf, P_400_HEAD, P_400_READ_WRITE, 0, cmd, index);
			FillCommand(buf, P_400_READ_S, 0x795E, (byte) 0x03, index);
			FillCommand(buf, P_400_WRITE_M, 0x795D, (byte) 0x01, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_UpLoad:// 任务上传命令
			FillHead(buf, P_400_HEAD, P_400_READ_S, 0, cmd, index);
			FillCommand(buf, P_400_READ_S, 0x79E4, (byte) 0x00, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_UpLoadRetry:// 任务上传重发命令?
			FillHead(buf, P_400_HEAD, P_400_READ_S, 0, cmd, index);
			FillCommand(buf, P_400_READ_S, 0x79E5, (byte) 0x00, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_UpLoadFail:// 任务上传失败命令
			FillHead(buf, P_400_HEAD, P_400_WRITE_S, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_S, 0x79E6, (byte) OrderParam.INSTANCE.getnFlag(), index);
			FillData(buf, cmd, index);
			break;

		case Cmd_Read_Funclist:// 读取功能列表
			FillHead(buf, P_400_HEAD, P_400_READ_S, 0, cmd, index);
			FillCommand(buf, P_400_READ_S, 0x7A47, (byte) 0x17, index);
			FillData(buf, cmd, index);
			break;
		case Cmd_Write_Funclist:// 写入功能列表
			FillHead(buf, P_400_HEAD, P_400_WRITE_M, 0, cmd, index);
			FillCommand(buf, P_400_WRITE_M, 0x7A47, (byte) 0x17, index);
			FillData(buf, cmd, index);
			break;
		default:
			break;
		}
		int indexValue = index.getValue();
		index = null;
		return indexValue;
	}

	/*
	 * (non-Javadoc) <p>Title: GetRetLen <p>Description: 预设命令发送后的返回数据长度
	 * 
	 * @param cmd 命令类型
	 * 
	 * @return 收到回复数据的长度
	 * 
	 * @see
	 * com.mingseal.data.protocol.Protocol#GetRetLen(com.mingseal.data.param.
	 * CmdParam)
	 */
	@Override
	public int GetRetLen(CmdParam cmd) {
		switch (cmd) {
		case Cmd_Ask: // 询问状态
		case Cmd_Reset: // 复位
		case Cmd_Move: // 示教运动
		case Cmd_Stop: // 停止运动
		case Cmd_Demo: // 任务模拟
		case Cmd_Locate: // 定位
		case Cmd_Track: // 循迹定位
		case Cmd_DownLoad: // 任务下载
		case Cmd_DeleteTask:// 删除任务
		case Cmd_RunTask: // 运行任务
		case Cmd_SelTask: // 选择任务
		case Cmd_IsTaskExist: // 任务是否已存在
		case Cmd_UpLoad: // 任务上传命令
		case Cmd_UpLoadRetry: // 任务上传重发命令
		case Cmd_UpLoadFail: // 任务上传失败命令
			return 8;
		case Cmd_Device: // 读取机器类型
			return 79;
		case Cmd_Coord:
			return 31;
		case Cmd_PreUpLoad: // 任务上传预处理命令
			return 11;
		case Cmd_UpdateDSP: // 升级DSP
			return 9;
		case Cmd_TrackFinish: // 循迹定位完成
			return 0;
		case Cmd_Read_Funclist: // 读取功能列表
			return 51;
		case Cmd_Write_Funclist:// 写入功能列表
			return 10;
		default: // 没有对应命令
			return 8;
		}
	}

	/*
	 * (non-Javadoc) <p>Title: CheckData <p>Description: 校验返回数据是否正确
	 * 
	 * @param buf 返回的数据
	 * 
	 * @param cmd 命令类型
	 * 
	 * @return 校验是否成功
	 * 
	 * @see com.mingseal.data.protocol.Protocol#CheckData(byte[],
	 * com.mingseal.data.param.CmdParam)
	 */
	@Override
	public int CheckData(byte[] buf, CmdParam cmd) {
		m_nErrorCode = 0;
		int crc = 0;
		switch (cmd) {
		case Cmd_Ask:// 询问状态
		case Cmd_Reset:// 复位
		case Cmd_Stop:// 停止运动
		case Cmd_DeleteTask:// 删除任务
		case Cmd_RunTask:// 运行任务
		case Cmd_SelTask:// 选择任务
		case Cmd_IsTaskExist:// 任务是否已存在
		case Cmd_UpLoadFail: // 任务上传失败命令
			if (buf[0] == P_400_HEAD) {
				if (buf[1] == P_400_WRITE_S) {
					crc = DataCheckout.byteToCRC16(buf, 0, GetRetLen(cmd) - 2);
					if (buf[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
							&& buf[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
						if (cmd.getValue() == CmdParam.Cmd_IsTaskExist.getValue()) {
							if (buf[5] == 1) {
								return 1;
							}
						} else {
							return 1;
						}
					}
				} /*else if (buf[1] == P_400_ERROR1) {
					 crc = DataCheckout.byteToCRC16(buf, 0, (GetRetLen(cmd) -
					 2));
					 byte[] temp = new byte[50];
					 System.arraycopy(buf, 0, temp, 0,
					 buf.length);//让数组范围扩大,防止校验数据时超出范围
					 if ((int) temp[GetRetLen(cmd) - 2] == (byte) (crc &
					 0x00ff)
					 && (int) temp[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
					 temp = null;
					 Error(buf[2], buf[3]);
					 return m_nErrorCode;
					 }else{
					 temp = null;
					 }
				}*/
			}
			break;
		case Cmd_Move:// 示教运动
		case Cmd_Demo:// 任务模拟
		case Cmd_Locate:// 定位
		case Cmd_Track:// 循迹定位
		case Cmd_DownLoad:// 任务下载
		case Cmd_Write_Funclist:// 写入功能列表
			if (buf[0] == P_400_HEAD) {
				if (buf[1] == P_400_WRITE_M) {
					crc = DataCheckout.byteToCRC16(buf, 0, GetRetLen(cmd) - 2);
					byte c2 = (byte) (crc & 0x00ff);
					byte c1 = (byte) (crc >> 8);
					if (buf[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
							&& buf[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
						return 1;
					}
				} /*else if (buf[1] == P_400_ERROR2) {
					 crc = DataCheckout.byteToCRC16(buf, 0, 3);
					 if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte)
					 (crc >> 8)) {
					 Error(buf[2], 0);
					 return m_nErrorCode;
					 }
				}*/
			}
			break;
		case Cmd_Device:// 读取机器类型
		case Cmd_Coord:// 读取当前坐标
		case Cmd_Read_Funclist: // 读取功能列表
//		case Cmd_UpLoad: // 任务上传命令
//		case Cmd_UpLoadRetry: // 任务上传重发命令
			if (buf[0] == P_400_HEAD) {
				if (buf[1] == P_400_READ_S) {
					crc = DataCheckout.byteToCRC16(buf, 0, GetRetLen(cmd) - 2);
					if (buf[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
							&& buf[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
						return 1;
					}
				} /*else if (buf[1] == P_400_ERROR3) {
					 crc = DataCheckout.byteToCRC16(buf, 0, 3);
					 if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte)
					 (crc >> 8)) {
					 Error(buf[2], 0);
					 return m_nErrorCode;
					 }
				}*/
			}
			break;
		case Cmd_UpLoad: // 任务上传命令
		case Cmd_UpLoadRetry: // 任务上传重发命令
			if (buf[0] == P_400_HEAD) {
				if (buf[1] == P_400_READ_S) {
					crc = DataCheckout.byteToCRC16(buf, 0, GetRetLen(cmd) - 2);
					int temp1 = (int)buf[GetRetLen(cmd) - 2] & 0xff;
					int temp2 = (int)buf[GetRetLen(cmd) - 1] & 0xff;
					if (buf[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
							&& buf[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
						return 1;
					}
				} /*else if (buf[1] == P_400_ERROR3) {
					 crc = DataCheckout.byteToCRC16(buf, 0, 3);
					 if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte)
					 (crc >> 8)) {
					 Error(buf[2], 0);
					 return m_nErrorCode;
					 }
				}*/
			}
			break;
		case Cmd_PreUpLoad:// 任务上传命令
			if (buf[0] == P_400_HEAD) {
				if (buf[1] == P_400_READ_WRITE) {
					crc = DataCheckout.byteToCRC16(buf, 0, GetRetLen(cmd) - 2);
					if (buf[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
							&& buf[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
						if ((buf[4] & 0x01) == 0) {
							Error(ErrorInfo.ERROR_TASK_NULL.getValue(), 0);
							return m_nErrorCode;
						} else if ((buf[4] & 0x4) != 0) {
							Error(ErrorInfo.ERROR_TASK_UNKNOWN.getValue(), 0);
							return m_nErrorCode;
						} else {
							return 1;
						}
					}
				} /*else if (buf[1] == P_400_ERROR4) {
					 crc = DataCheckout.byteToCRC16(buf, 0, 3);
					 if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte)
					 (crc >> 8)) {
					 Error(buf[2], 0);
					 return m_nErrorCode;
					 }
				}*/
			}
			break;
		case Cmd_UpdateDSP:// 升级DSP
			if (buf[0] == P_400_HEAD) {
				if (buf[1] == P_400_USER) {
					crc = DataCheckout.byteToCRC16(buf, 0, GetRetLen(cmd) - 2);
					if (buf[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
							&& buf[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
						return 1;
					}
				} /*else if (buf[1] == P_400_ERROR5) {
					 crc = DataCheckout.byteToCRC16(buf, 0, 3);
					 if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte)
					 (crc >> 8)) {
					 Error(buf[2], 0);
					 return m_nErrorCode;
					 }
				}*/
			}
			break;
		default: {// 没有对应命令

			if (buf[1] == P_400_ERROR1) {
				byte[] temp = new byte[50];
				System.arraycopy(buf, 0, temp, 0, buf.length);// 让数组范围扩大,防止校验数据时超出范围
				crc = DataCheckout.byteToCRC16(temp, 0, (GetRetLen(cmd) - 2));
				if ((int) temp[GetRetLen(cmd) - 2] == (byte) (crc & 0x00ff)
						&& (int) temp[GetRetLen(cmd) - 1] == (byte) (crc >> 8)) {
					temp = null;
					Error(buf[2], buf[3]);
					return m_nErrorCode;
				} else {
					temp = null;
				}
			} else if (buf[1] == P_400_ERROR2) {
				crc = DataCheckout.byteToCRC16(buf, 0, 3);
				if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte) (crc >> 8)) {
					Error(buf[2], 0);
					return m_nErrorCode;
				}
			} else if (buf[1] == P_400_ERROR3) {
				crc = DataCheckout.byteToCRC16(buf, 0, 3);
				if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte) (crc >> 8)) {
					Error(buf[2], 0);
					return m_nErrorCode;
				}
			} else if (buf[1] == P_400_ERROR4) {
				crc = DataCheckout.byteToCRC16(buf, 0, 3);
				if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte) (crc >> 8)) {
					Error(buf[2], 0);
					return m_nErrorCode;
				}
			} else if (buf[1] == P_400_ERROR5) {
				crc = DataCheckout.byteToCRC16(buf, 0, 3);
				if (buf[3] == (byte) (crc & 0x00ff) && buf[4] == (byte) (crc >> 8)) {
					Error(buf[2], 0);
					return m_nErrorCode;
				}
			}
		}
			break;
		}
		// return false;
		return 0;
	}

	// @Override
	// public int LastErrorCode() {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	@Override
	public int FillHead(byte[] buf, byte head, byte wr, int len, CmdParam cmd, Index index) {
		int current = 0;
		buf[index.getValue() + current++] = head;
		buf[index.getValue() + current++] = wr;
		index.setValue(current + index.getValue());
		return current;
	}

	@Override
	public int FillData(byte[] buf, CmdParam cmd, Index index) {
		/* 断言index值是否大于等于6 */
		int current = 0;
		switch (cmd) {
		case Cmd_Ask:// 询问状态
		case Cmd_Reset:// 复位
		case Cmd_Stop:// 停止运动
		case Cmd_Device:// 读取机器类型
		case Cmd_Coord:// 读取当前坐标
		case Cmd_DeleteTask:// 删除任务
		case Cmd_RunTask:// 运行任务
		case Cmd_SelTask:// 选择任务
		case Cmd_IsTaskExist:// 任务是否已存在
		case Cmd_TrackFinish: // 循迹定位完成
			break;
		case Cmd_Move:// 示教运动
			int tmp = ((OrderParam.INSTANCE.getnMoveDir() & 0x01) | ((OrderParam.INSTANCE.getnMoveType() & 0x01) << 1)
					| ((OrderParam.INSTANCE.getnMoveCoord() & 0x03) << 2));
			//往寄存器值内写数据，字段占6个字节
			buf[index.getValue() + current++] = (byte) ((tmp & 0x0000ff00) >>> 8);// 高位
			buf[index.getValue() + current++] = (byte) ((tmp & 0x000000ff));// 低位
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnPulse() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnPulse() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnSpeed() & 0x000000ff);
			break;
		case Cmd_Demo:// 任务模拟
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnDataLen() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnSpeed() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnSpeed() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnSpeed() & 0x000000ff);
			break;
		case Cmd_Locate:// 定位
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnXCoord() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnXCoord() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnXCoord() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnXCoord() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnYCoord() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnYCoord() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnYCoord() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnYCoord() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnZCoord() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnZCoord() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnZCoord() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnZCoord() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnUCoord() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnUCoord() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnUCoord() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnUCoord() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnSpeed() & 0x000000ff);
			break;
		case Cmd_Track:
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnDataLen() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnSpeed() & 0x000000ff);
			break;
		case Cmd_DownLoad:
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x0000ff00) >>> 8);
//			short temp = (short) (OrderParam.INSTANCE.getnDataLen() & 0x000000ff);
//			buf[index.getValue() + current++] = (byte) temp;
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnDataLen() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnTaskNum() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnTaskNum() & 0x000000ff);
			break;
		case Cmd_PreUpLoad:
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnTaskNum() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnTaskNum() & 0x000000ff);
			break;
		case Cmd_Read_Funclist:
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnTaskNum() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnTaskNum() & 0x000000ff);
			break;
		case Cmd_Write_Funclist: {
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnTaskNum() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnTaskNum() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnZeroCheck() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnZeroCheck() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnAccelerate() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnAccelerate() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDecelerate() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnDecelerate() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnBaseHeight() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnBaseUnit() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnXYNullSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnXYNullSpeed() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnZNullSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnZNullSpeed() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnUNullSpeed() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnUNullSpeed() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnAutoRunTime() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnAutoRunTime() & 0x000000ff);
			buf[index.getValue()
					+ current++] = (byte) ((OrderParam.INSTANCE.getnTurnAccelerateMax() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnTurnAccelerateMax() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) (((OrderParam.INSTANCE.isbTaskDelete()) ? 1 : 0) & 0x000000ff);
			buf[index.getValue() + current++] = (byte) (((OrderParam.INSTANCE.isbTaskBack()) ? 1 : 0) & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnYCheckDis() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnYCheckDis() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnRunNum() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnRunNum() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnRunNum() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnRunNum() & 0x000000ff);
			buf[index.getValue() + current++] = (byte) (((OrderParam.INSTANCE.isbBackDefault()) ? 1 : 0) & 0x000000ff);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnPauseType() & 0x000000ff);
			buf[index.getValue()
					+ current++] = (byte) ((OrderParam.INSTANCE.getnNoHardOutGlueTime() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnNoHardOutGlueTime() & 0x000000ff);
			buf[index.getValue()
					+ current++] = (byte) ((OrderParam.INSTANCE.getnNoHardOutGlueInterval() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnNoHardOutGlueInterval() & 0x000000ff);
			buf[index.getValue()
					+ current++] = (byte) ((((OrderParam.INSTANCE.isbRunNumZero()) ? 1 : 0) & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (((OrderParam.INSTANCE.isbRunNumZero()) ? 1 : 0) & 0x000000ff);
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
			buf[index.getValue() + current++] = (byte) 0;
		}
			break;
		case Cmd_UpdateDSP:
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0xff000000) >>> 24);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x00ff0000) >>> 16);
			buf[index.getValue() + current++] = (byte) ((OrderParam.INSTANCE.getnDataLen() & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (OrderParam.INSTANCE.getnDataLen() & 0x000000ff);
			break;
		default:
			break;
		}
		// CRC字段占2个字节，低字节在前
		int temp = DataCheckout.byteToCRC16(buf, 0, index.getValue() + current);
		buf[index.getValue() + current++] = (byte) (temp & 0x000000ff);
		buf[index.getValue() + current++] = (byte) ((temp & 0x0000ff00) >>> 8);
		index.setValue(index.getValue() + current);
		return current;
	}

	@Override
	public int FillTail(byte[] buf, byte tail, Index index) {
		// index.setValue(0);
		return 0;
	}

	/**
	 * <p>
	 * Title: FillCommand
	 * <p>
	 * Description: 填充命令数据
	 * 
	 * @param buf
	 *            数据数组
	 * @param wr
	 *            读写标记，功能码
	 * @param cmd
	 *            命令类型
	 * @param registerNum
	 *            寄存器数量，2个字节
	 * @param index
	 *            内存填写位置
	 * @return
	 */
	protected int FillCommand(byte[] buf, byte wr, int cmd, byte registerNum, Index index) {
		int current = 0;
		if (wr == P_400_USER) {
			buf[index.getValue() + current++] = (byte) (cmd & 0x000000ff);
		} else {
			/*------------------- 起始地址/寄存器地址，占两个字节 -------------------*/
			buf[index.getValue() + current++] = (byte) ((cmd & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (cmd & 0x000000ff);
			/*------------------- 寄存器数量/寄存器值，占2个字节 -------------------*/
			buf[index.getValue() + current++] = (byte) ((registerNum & 0x0000ff00) >>> 8);
			buf[index.getValue() + current++] = (byte) (registerNum & 0x000000ff);
			if (wr == P_400_WRITE_M) {
				/*------------------- 公式：寄存器数量*每个寄存器所占字节数=寄存器所占字节数 -------------------*/
				registerNum *= 2;
				buf[index.getValue() + current++] = (byte) (registerNum & 0x000000ff);
			}
		}
		index.setValue(index.getValue() + current);
		return current;
	}

	void Error(int no, int no2) {
		switch (no) {
		case 1:
			m_nErrorCode = ErrorInfo.ERROR_ILLEGALFUNC.getValue();
			break;
		case 2:
			m_nErrorCode = ErrorInfo.ERROR_ILLEGALDATAADDR.getValue();
			break;
		case 3:
			m_nErrorCode = ErrorInfo.ERROR_ILLEGALDATA.getValue();
			break;
		case 4:
			// m_nErrorCode = ErrorInfo.ERROR_FOLLOWERR.getValue();
			m_nErrorCode = ErrorInfo.CEMG_ERROR.getValue() + no2 - 1;// 错误号和枚举号相同
			break;
		case 6:
			m_nErrorCode = ErrorInfo.ERROR_BUSY.getValue();
			break;
		default:
			m_nErrorCode = no;
			break;
		}
	}

}
