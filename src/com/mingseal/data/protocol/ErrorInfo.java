package com.mingseal.data.protocol;

/**
 * 错误信息枚举类
 * 
 * @author lyq
 */
public enum ErrorInfo {

	ERROR_ILLEGALFUNC(40101), // 非法功能
	ERROR_ILLEGALDATAADDR(40102), // 非法数据地址
	ERROR_ILLEGALDATA(40103), // 非法数据
	ERROR_FOLLOWERR(40104), // 从站设备故障
	ERROR_BUSY(40105), // 设备忙
	ERROR_TASK_NULL(40106), // 空任务
	ERROR_TASK_UNLOAD(40107), // 任务未加载
	ERROR_TASK_UNKNOWN(40108), // 非法任务号

	// 2013.11.15 errer add 故障04
	CEMG_ERROR(40109), // 1 //急停中
	LIMX_ERROR(40110), // 2 //X轴光电报警
	LIMY_ERROR(40111), // 3 //Y轴光电报警
	LIMZ_ERROR(40112), // 4 //Z轴光电报警
	LIMU_ERROR(40113), // 5 //U轴光电报警
	LIMIT_ERROR(40114), // 6 //行程超限报警
	DOWN_ERROR(40115), // 7 //任务下载失败
	UP_ERROR(40116), // 8 //任务上传失败
	SMU_ERROR(40117), // 9 //任务模拟失败
	COM_ERROR(40118), // 10 //示教指令错误
	FOLLOW_ERROR(40119), // 11 //循迹定位失败
	FLASH_ERROR(40120), // 12 //任务号不可用
	FPGA_INIT_ERROR(40121), // 13 //初始化失败
	APIVERSION_ERROR(40122), // 14 //API版本错误
	DOWN_PRO_ERROR(40123), // 15 //程序升级失败
	EEPROM_ERROR(40124), // 16 //系统损坏
	NOMISSION_ERROR(40125), // 17 //任务未加载
	ZBASE_HIGH_ERROR(40126), // 18 //(Z轴)基点抬起高度过高
	IN_OVERTIME_ERROR(40127); // 19 //等待输入超时

	private int error;

	private ErrorInfo(int errorInfo) {
		this.error = errorInfo;
	}

	public int getValue() {
		return error;
	}

}
