package com.mingseal.data.param;

/** 
* @ClassName: CmdParam 
* @Description: 命令枚举类型
* @author lyq
* @date 2015年6月17日 上午11:17:24 
*  
*/
public enum CmdParam {
	Cmd_Null						((byte)0x00), 
	Cmd_Ask						((byte)0x01), // 询问状态
	Cmd_Reset					((byte)0x02), // 复位
	Cmd_Move					((byte)0x03), // 示教
	Cmd_Stop						((byte)0x04), // 停止
	Cmd_Demo					((byte)0x05), // 模拟
	Cmd_Locate				((byte)0x06), // 定位
	Cmd_Track					((byte)0x07), // 循迹定位
	Cmd_TrackFail			((byte)0x08), // 循迹定位失败
	Cmd_TrackFinish		((byte)0x09),	// 循迹定位完成
	Cmd_DeleteTask		((byte)0x0A), // 删除任务
	Cmd_RunTask				((byte)0x0B), // 运行任务
	Cmd_SelTask				((byte)0x0C), // 选择任务
	Cmd_UpdateDSP		((byte)0x0D), // 升级下位机DSP
	
	Cmd_Device				((byte)0x0E), // 获取设备类型
	Cmd_Coord					((byte)0x0F), // 获取坐标
	
	Cmd_Read_Funclist 	((byte)0x10),	// 读取功能列表
	Cmd_Write_Funclist	((byte)0x11),	// 写入功能列表
	
	Cmd_Aacceleration	((byte)0x12), // 设置加速度
	Cmd_XYSpeed				((byte)0x13), // 设置xy空走速度
	Cmd_ZSpeed				((byte)0x14), // 设置z空走速度
	Cmd_SaveParam		((byte)0x15), // 保存参数设置
	Cmd_YMinDistance	((byte)0x16), // 获得y最小步距
	Cmd_ZMinDistance	((byte)0x17), // 获得z最小步距
	Cmd_XMinDistance	((byte)0x18), // 获得x最小步距
	Cmd_ZUp						((byte)0x19), // z轴抬起
	
	Cmd_PreDownLoad	((byte)0x1A), // 任务预下载
	Cmd_DownLoad		((byte)0x1B), // 任务下载
	Cmd_DownRetry		((byte)0x1C), // 任务下载重发
	Cmd_PreUpLoad		((byte)0x1D), // 任务上传预命令
	Cmd_UpLoad				((byte)0x1E), // 任务上传命令
	Cmd_UpLoadParam	((byte)0x1F), // 任务参数上传命令
	Cmd_UpLoadFail		((byte)0x20), // 任务上传失败
	Cmd_UpLoadRetry	((byte)0x21), // 任务上传重发数据包
	Cmd_IsTaskExist		((byte)0x22), // 任务是否已存在
	Cmd_UpLoadFinish	((byte)0x23),//	任务上传结束
	Cmd_All						((byte)0x24);
	
	byte cmd;
	
	private CmdParam(byte cmd){
		this.cmd = cmd;
	}
	
	public byte getValue(){
		return cmd;
	}
	
	public static CmdParam ret8ByteDataCmd(byte[] buffer, int value){
		switch(value){
		case 0x7930:
			return CmdParam.Cmd_Ask;
		case 0x7931:
			return CmdParam.Cmd_IsTaskExist;
		case 0x7932:
			return CmdParam.Cmd_Reset;
		case 0x7934:
			return CmdParam.Cmd_Move;
		case 0x7938:
			return CmdParam.Cmd_Stop;
		case 0x793A:
			return CmdParam.Cmd_Demo;
		case 0x793F:
			return CmdParam.Cmd_Locate;
		case 0x7952:
			return CmdParam.Cmd_DownLoad;
		case 0x7956:
			return CmdParam.Cmd_DeleteTask;
		case 0x7958:{
			int temp = (int)((buffer[4] & 0x00ff << 8) | (buffer[5] & 0x00ff));
			if(temp == 0x0003){
				return CmdParam.Cmd_Track;
			}else if(temp == 0x00B5){
				return CmdParam.Cmd_TrackFinish;
			}
		}
		case 0x795B:
			return CmdParam.Cmd_RunTask;
		case 0x79E4:
			return CmdParam.Cmd_UpLoad;
		case 0x79E5:
			return CmdParam.Cmd_UpLoadRetry;
		case 0x79E6:
			return CmdParam.Cmd_UpLoadFail;
		case 0x7A47:
			return CmdParam.Cmd_Write_Funclist;	
		}
		return Cmd_Null;
	}
	
}
