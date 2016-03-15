package com.mingseal.data.protocol;

import com.mingseal.data.param.CmdParam;

/** 
* @ClassName: Protocol 
* @Description: 通信协议接口类
* @author lyq
* @date 2015年6月16日 上午9:03:26 
*  
*/
public abstract class Protocol {
	
	/**
	 * <p>Title: Index
	 * <p>Description: 命令参数长度
	 * <p>Company: MingSeal .Ltd
	 * @author lyq
	 * @date 2015年10月26日
	 */
	class Index {
		private int value;
		
		/**
		 * <p>Title: getValue
		 * <p>Description: 获取索引值
		 * @return
		 */
		public int getValue() {
			return value;
		}
		
		/**
		 * <p>Title: setValue
		 * <p>Description: 设置索引值
		 * @param value
		 */
		public void setValue(int value) {
			this.value = value;
		}
		
		/**
		 * <p>Title: ValueAdd
		 * <p>Description: 索引值累加
		 */
		public void ValueAdd(){
			value++;
		}
		
		/**
		 * <p>Title: 
		 * <p>Description: 索引初始化构造函数
		 * @param value
		 */
		public Index(int value){
			this.value = value;
		}
	}
	
	/**
	 * <p>Title: CreaterOrder
	 * <p>Description: 创建命令
	 * @param buf 数据数组
	 * @param cmd 命令类型
	 * @param para 命令参数
	 * @return 数组长度
	 */
	abstract public int CreaterOrder(
			byte[] buf,
			CmdParam cmd
			);
	
	/**
	 * <p>Title: GetRetLen 
	 * <p>Description: 获取相应命令数据长度
	 * @param cmd 命令类型
	 * @return 数据长度
	 */
	abstract int GetRetLen(
			CmdParam cmd
			);
	
	/**
	 * <p>Title: CheckData
	 * <p>Description: 校验数据
	 * @param buf 数据数组
	 * @param cmd 命令类型
	 * @return 0:错误 1:正确 其他:错误码
	 */
	abstract public int CheckData(
			byte[] buf,
			CmdParam cmd
			);
	
//	abstract public int LastErrorCode();
	
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
	abstract protected int FillHead(
			byte[] buf,
			byte head,
			byte wr,
			int len,
			CmdParam cmd,
			Index index
			);
	
	/**
	 * <p>Title: FillData
	 * <p>Description: 填充数据
	 * @param buf 数据存储数组
	 * @param cmd 命令类型
	 * @param para 命令参数
	 * @param index 索引
	 * @return
	 */
	abstract protected int FillData(
			byte[] buf,
			CmdParam cmd,
			Index index
			);
	
	/**
	 * <p>Title: FillTail
	 * <p>Description: 填充尾部命令
	 * @param buf 数据存储数组
	 * @param tail
	 * @param index
	 * @return
	 * @deprecated C协议未用到此方法
	 */
	abstract protected int FillTail(
			byte[] buf,
			byte tail,
			Index index
			);
	
	/**
	 * <p>Title: READ1BYTE
	 * <p>Description: 从byte数组读取一个字节(高位在高地址,低位在低地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ1BYTE(byte[] buff, int offset){
		return ((int)(buff[offset] & 0x00ff));
	}
	
	/** 
	* @Title: READ1BYTE 
	* @Description: 从byte数组读取一个字节(高位在高地址,低位在低地址)
	* @param buff byte数组
	* @param primaryOffset 主偏移量
	* @param secondaryOffset 副偏移量
	* @return
	*/
	public static int READ1BYTE(byte[] buff, int primaryOffset, int secondaryOffset){
		return ((int)(buff[primaryOffset + secondaryOffset] & 0x00ff));
	}
	
	/**
	 * <p>Title: READ2BYTES
	 * <p>Description: 从byte数组读取两个字节(高位在高地址,低位在低地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ2BYTES(byte[] buff, int offset){
		int temp = 0;
		int off0 = buff[offset] & 0x00ff;
		int off1 = buff[offset + 1] & 0x00ff;
		temp = off1;
		temp = temp << 8;
		temp = temp | off0;
		return temp;
	}
	
	/** 
	* @Title: READ2BYTES 
	* @Description: 从byte数组读取两个字节(高位在高地址,低位在低地址)
	* @param buff byte数组
	* @param primaryOffset 主偏移量
	* @param secondaryOffset 副偏移量
	* @return
	*/
	public static int READ2BYTES(byte[] buf, int primaryOffset, int secondaryOffset){
		int temp = 0;
		int off0 = buf[primaryOffset + secondaryOffset] & 0x00ff;
		int off1 = buf[primaryOffset + secondaryOffset + 1] & 0x00ff;
		temp = off1;
		temp = temp << 8;
		temp = temp | off0;
		return temp;
	}
	
	/**
	 * <p>Title: READ3BYTES
	 * <p>Description: 从byte数组读取两个字节(高位在高地址,低位在低地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ3BYTES(byte[] buff, int offset){
		int temp = 0;
		int off0 = buff[offset] & 0x00ff;
		int off1 = buff[offset + 1] & 0x00ff;
		int off2 = buff[offset + 2] & 0x00ff;
		temp = off2;
		temp = temp << 8;
		temp = temp | off1;
		temp = temp << 8;
		temp = temp | off0;
		return temp;
	}
	
	/**
	 * <p>Title: READ4BYTES
	 * <p>Description: 从byte数组读取四个字节(高位在高地址,低位在低地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ4BYTES(byte[] buf, int offset){
		int temp = 0;
		int off0 = buf[offset] & 0x00ff;
		int off1 = buf[offset + 1] & 0x00ff;
		int off2 = buf[offset + 2] & 0x00ff;
		int off3 = buf[offset + 3] & 0x00ff;
		temp = off3;
		temp = temp << 8;
		temp = temp | off2;
		temp = temp << 8;
		temp = temp | off1;
		temp = temp << 8;
		temp = temp | off0;
		return temp;
//		return ((int)(buff[offset]) | ((int)(buff[offset + 1]) << 8) | ((int)(buff[offset + 2]) << 16) | ((int)(buff[offset + 3]) << 24));
	}
	
	/** 
	* @Title: READ4BYTES 
	* @Description: 从byte数组读取四个字节(高位在高地址,低位在低地址)
	* @param buff byte数组
	* @param primaryOffset 主偏移量
	* @param secondaryOffset 副偏移量
	* @return
	*/
	public static int READ4BYTES(byte[] buff,int primaryOffset, int secondaryOffset){
		int temp = 0;
		int off0 = buff[primaryOffset + secondaryOffset] & 0x00ff;
		int off1 = buff[primaryOffset + secondaryOffset + 1] & 0x00ff;
		int off2 = buff[primaryOffset + secondaryOffset + 2] & 0x00ff;
		int off3 = buff[primaryOffset + secondaryOffset + 3] & 0x00ff;
		temp = off3;
		temp = temp << 8;
		temp = temp | off2;
		temp = temp << 8;
		temp = temp | off1;
		temp = temp << 8;
		temp = temp | off0;
		return temp;
//		return ((int)(buff[offset]) | ((int)(buff[offset + 1]) << 8) | ((int)(buff[offset + 2]) << 16) | ((int)(buff[offset + 3]) << 24));
	}
	
	/**
	 * <p>Title: READ2BYTES_R
	 * <p>Description: 从byte数组读取两个字节(高位在低地址,低位在高地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ2BYTES_R(byte[] buff, int offset){
		int temp = 0;
		int off0 = buff[offset] & 0x00ff;
		int off1 = buff[offset + 1] & 0x00ff;
		temp = off0;
		temp = temp << 8;
		temp = temp | off1;
		return temp;
	}
	
	/**
	 * <p>Title: READ3BYTES_R
	 * <p>Description: 从byte数组读取三个字节(高位在低地址,低位在高地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ3BYTES_R(byte[] buff, int offset){
		int temp = 0;
		int off0 = buff[offset] & 0x00ff;
		int off1 = buff[offset + 1] & 0x00ff;
		int off2 = buff[offset + 2] & 0x00ff;
		temp = off0;
		temp = temp << 8;
		temp = temp | off1;
		temp = temp << 8;
		temp = temp | off2;
		return temp;
	}
	
	/**
	 * <p>Title: READ4BYTES_R
	 * <p>Description: 从byte数组读取四个字节(高位在低地址,低位在高地址)
	 * @param buff byte数组
	 * @param offset 偏移数
	 * @return
	 */
	public static int READ4BYTES_R(byte[] buff, int offset){
		int temp = 0;
		int off0 = buff[offset] & 0x00ff;
		int off1 = buff[offset + 1] & 0x00ff;
		int off2 = buff[offset + 2] & 0x00ff;
		int off3 = buff[offset + 3] & 0x00ff;
		temp = off0;
		temp = temp << 8;
		temp = temp | off1;
		temp = temp << 8;
		temp = temp | off2;
		temp = temp << 8;
		temp = temp | off3;
		return temp;
	}
}
