package com.mingseal.data.manager;

import java.util.BitSet;

import com.mingseal.utils.BitSetConvert;

import android.renderscript.Byte2;

/** 
* @ClassName: PointInfo400 
* @Description: PointInfo单字节补齐结构体在java中实现,长度为6的byte型数组
* @author 李英骑
* @date 2015年10月29日 上午8:34:38  
* @Companly Mingseal.Ltd
*/
public class PointInfo400 {

	private byte[] pointInfo = null;

//	private byte[] portInfo = null;
//	private BitSet IOPort = null;

	private byte byte0 = 0;
	private byte byte1 = 0;
	private byte byte2 = 0;// le
	
	private int io = 0;
	
	/** 
	* @Title: analysePointInfo 
	* @Description: 解析byte数组中点相关数据
	* @param src 要解析的src数据数组
	* @return PointInfo400 引用
	*/
	public PointInfo400 analysePointInfo(byte[] src){
		PointInfo400 dst =new PointInfo400();
		dst.setPointInfo(src);
		return dst;
	}
	
	/** 
	* @Title: setPointInfo 
	* @Description: 解析点信息
	* @param pointInfo 数据数组
	*/
	public void setPointInfo(byte[] pointInfo){
		byte0 = this.pointInfo[0] = pointInfo[0];
		byte1 = this.pointInfo[1] = pointInfo[1];
		byte2 = this.pointInfo[2] = pointInfo[2];
		this.pointInfo[3] = pointInfo[3];
		this.pointInfo[4] = pointInfo[4];
		this.pointInfo[5] = pointInfo[5];
		/**
		 *
		 *pointInfo[3] = (byte) (io & 0x000000ff);
		 *pointInfo[4] = (byte) (io & 0x0000ff00);
		 *pointInfo[5] = (byte) (io & 0x00ff0000);
		 */
		int temp = this.pointInfo[3] & 0x000000ff;
		temp = (temp << 8) | (this.pointInfo[4] & 0x0000ff00);
		temp = (temp << 8) | (this.pointInfo[5] & 0x00ff0000);
		for(int i = 0; i < 24; i++){
			io = io | (temp & (0x01 << (23 - i)));
		}
	}

	/** 
	* @Title: setPointInfo 
	* @Description: 解析点信息
	* @param pointInfo 数据数组
	* @param primaryOffset 主偏移量
	*/
	public void setPointInfo(byte[] pointInfo, int primaryOffset){
		byte0 = this.pointInfo[0] = pointInfo[0 + primaryOffset];
		byte1 = this.pointInfo[1] = pointInfo[1 + primaryOffset];
		byte2 = this.pointInfo[2] = pointInfo[2 + primaryOffset];
		this.pointInfo[3] = pointInfo[3 + primaryOffset];
		this.pointInfo[4] = pointInfo[4 + primaryOffset];
		this.pointInfo[5] = pointInfo[5 + primaryOffset];
		/**
		 *
		 *pointInfo[3] = (byte) (io & 0x000000ff);
		 *pointInfo[4] = (byte) (io & 0x0000ff00);
		 *pointInfo[5] = (byte) (io & 0x00ff0000);
		 */
		int temp = this.pointInfo[3] & 0x000000ff;
		temp = (temp << 8) | (this.pointInfo[4] & 0x0000ff00);
		temp = (temp << 8) | (this.pointInfo[5] & 0x00ff0000);
		for(int i = 0; i < 24; i++){
			io = io | (temp & (0x01 << (23 - i)));
		}
	}

	/** 
	* <p>Title: PointInfo400构造函数</p> 
	* <p>Description: 初始化相关数据</p>  
	*/
	public PointInfo400() {
		pointInfo = new byte[6];
		setAllValueDefault();
	}
	
	/** 
	* @Title: setAllValueDefault 
	* @Description: 设置所有数据为默认值
	*/
	public void setAllValueDefault(){
		byte0 = 0;
		byte1 = 0;
		byte2 = 0;
		io = 0;
		for(int i = 0; i < 6; i++){
			pointInfo[i] = 0;
		}
	}
	
//	/**
//	 * <p>Title: setAllPortFalse
//	 * <p>Description: 设置所有点胶口均为false
//	 */
//	private void setAllPortFalse(){
//		for(int i = 0; i < IOPort.length(); i++){
//			IOPort.set(i, false);
//		}
//	}

	/** 
	* @Title: setPointType 
	* @Description: 设置点类型
	* @param pointType
	*/
	public void setPointType(byte pointType) {
		this.byte0 = (byte) (this.byte0 | (pointType & 0x0f));
	}
	
	
	/** 
	* @Title: getPointType 
	* @Description: 获取点类型
	* @return 点类型
	*/
	public int getPointType(){
		int pointType = (int)(this.byte0 & 0x0f);
		return pointType;
	}

	/** 
	* @Title: setIoFlag 
	* @Description: 设置输入输出标记(0:输出, 1:输入)
	* @param ioFlag
	*/
	public void setIoFlag(byte ioFlag) {
		this.byte0 = (byte) (this.byte0 | ((byte)(ioFlag << 5)));
	}
	
	/** 
	* @Title: getIoFlag 
	* @Description: 获取输入输出标记(0:输出,1:输入)
	* @return 输入输出标记
	*/
	public int getIoFlag(){
		int ioFlag = (int)((this.byte0 & 0x20)>>5);
		return ioFlag;
	}

	/** 
	* @Title: setFlag 
	* @Description: 设置点标记(0:普通点, 1:插入点, 2:变化点)
	* @param flag
	*/
	public void setFlag(byte flag) {
		this.byte0 = (byte) (this.byte0 | ((byte) ((flag & 0x03) << 6)));
	}
	
	/** 
	* @Title: getFlag 
	* @Description: 获取点标记(0:普通点,1:插入点,2:变化点)
	* @return 点标记
	*/
	public int getFlag(){
		int flag = (int)(this.byte0 & 0xC0);
		flag = (flag>>>6)&0x03;
		
		return flag;
	}
	
	// ------------------------------------
	/** 
	* @Title: setTimeMode 
	* @Description: 设置延时模式(0:联动, 1:定时)
	* @param timeMode
	*/
	public void setTimeMode(byte timeMode) {
		this.byte1 = (byte) (this.byte1 | (byte)(timeMode & 0x01));
	}
	
	/** 
	* @Title: getTimeMode 
	* @Description: 获取延时模式(0:联动 1:定时) 
	* @return 延时模式
	*/
	public int getTimeMode(){
		int timeMode = (int)(this.byte1 & 0x01);
		return timeMode;
	}
	
	/** 
	* @Title: setIfDrop 
	* @Description: 点胶(焊)使能(0:不出胶(锡) 1:出胶(锡))
	* @param ifDrop
	*/
	public void setIfDrop(byte ifDrop) {
		this.byte1 = (byte) (this.byte1 | (byte)(ifDrop << 1));
	}
	
	/** 
	* @Title: getIfDrop 
	* @Description: 获取点胶(焊)使能 0:不出胶(锡) 1:出胶(锡) 
	* @return 点胶(焊)使能
	*/
	public int getIfDrop(){
		int ifDrop = (int)(this.byte1 & 0x02);
		return ifDrop;
	}

	/** 
	* @Title: setSurfaceDir 
	* @Description: 面点胶(焊)起始方向(0:x轴, 1:y轴)
	* @param surfaceDir
	*/
	public void setSurfaceDir(byte surfaceDir) {
		this.byte1 = (byte) (this.byte1 | (byte) (surfaceDir << 2));
	}
	
	/** 
	* @Title: getSurfaceDir 
	* @Description: 获取面点胶(焊)起始方向：0:X轴; 1:Y轴
	* @return 面点胶(焊)起始方向
	*/
	public int getSurfaceDir(){
		int surfaceDir = (int)(this.byte1 & 0x04);
		return surfaceDir;
	}

	/** 
	* @Title: setSurfaceChangeDrop 
	* @Description: 面点胶(焊)转折处出胶(锡)使能(0:不出胶(锡),1:出胶(锡))
	* @param surfaceChagneDrop
	*/
	public void setSurfaceChangeDrop(byte surfaceChagneDrop) {
		this.byte1 = (byte) (this.byte1 | (byte)(surfaceChagneDrop << 3));
	}
	
	/** 
	* @Title: getSurfaceChangeDrop 
	* @Description: 获取面点胶(焊)转折处出胶(锡)使能
	* @return 面点胶(焊)转折处出胶(锡)使能 0:不出胶(锡);1:出胶(锡)
	*/
	public int getSurfaceChangeDrop(){
		int surfaceChangeDrop = (int)(this.byte1 & 0x08);
		return surfaceChangeDrop;
	}
	
	/** 
	* @Title: setArcIfU 
	* @Description: 1:圆弧点U轴随动 0:圆弧点U轴不动
	* @param arcIfU
	*/
	public void setArcIfU(byte arcIfU) {
		this.byte1 = (byte) (this.byte1 | (byte)(arcIfU << 4));
	}
	
	/** 
	* @Title: getArcIfU 
	* @Description: 获取圆弧点U轴是否随动
	* @return 圆弧点U轴是否随动 1:圆弧点U轴随动 0:圆弧点U轴不动
	*/
	public int getArcIfU(){
		int arcIfU = (int)(this.byte1 & 0x10);
		return arcIfU;
	}

	/** 
	* @Title: setIfChangeSpeed 
	* @Description: 速度可变使能(0:不可变, 1:可变)
	* @param ifChangeSpeed
	*/
	public void setIfChangeSpeed(byte ifChangeSpeed) {
		this.byte1 = (byte) (this.byte1 | (byte)(ifChangeSpeed << 5));
	}
	
	/** 
	* @Title: getIfChangeSpeed 
	* @Description: 获取速度可变使能
	* @return 速度可变使能 0:不可变; 1:可变
	*/
	public int getIfChangeSpeed(){
		int ifChangeSpeed = (int)(this.byte1 & 0x20);
		return ifChangeSpeed;
	}
	
	/** 
	* @Title: setIfUexcusion 
	* @Description: 阵列点中是否有U轴偏移(0:没有, 1:有)
	* @param ifUexCusion
	*/
	public void setIfUexcusion(byte ifUexCusion) {
		this.byte1 = (byte) (this.byte1 | (byte)(ifUexCusion << 6));
	}
	
	/** 
	* @Title: getIfUexcusion 
	* @Description: 获取阵列点中是否有U轴偏移
	* @return 阵列点中是否有U轴偏移 0:没有;1:有
	*/
	public int getIfUexcusion(){
		int ifUexcusion = (int)(this.byte1 & 0x40);
		return ifUexcusion;
	}
	
	/** 
	* @Title: setIfPause 
	* @Description: 是否暂停(0:没有, 1:有) 
	* @param ifPause
	*/
	public void setIfPause(byte ifPause) {
		this.byte1 = (byte) (this.byte1 | (byte)(ifPause << 7));
	}
	
	/** 
	* @Title: getIfPause 
	* @Description: 获取是否暂停
	* @return 0:没有;1:有
	*/
	public boolean getIfPause(){
		boolean ifPause = (this.byte1 & 0x80) > 0 ? true : false;
		return ifPause;
	}
	
	/** 
	* @Title: setLen 
	* @Description: 记录总字节数
	* @param len 总字节数
	*/
	public void setLen(byte len) {
		this.byte2 = (byte) (len & 0xff);
	}
	
	/** 
	* @Title: getLen 
	* @Description: 获取总字节数
	* @return 总字节数
	*/
	public int getLen(){
		int len = (int)this.byte2;
		return len;
	}
	
//	/**
//	 * <p>Title: setIOPort
//	 * <p>Description: 设置出胶(锡)口/输入口 (0:关, 1:开)
//	 * @param fromIndex 起始位置
//	 * @param toIndex 结束位置
//	 * @param state 开关状态
//	 */
//	public void setIOPort(int fromIndex, int toIndex, boolean state) {
//		IOPort.set(fromIndex, toIndex, state);
//	}
	
	/** 
	* @Title: setIOPort 
	* @Description: 打开第index出胶(锡)口/输入口
	* @param index 开关位置
	*/
	public void setIOPort(int index){
		byte temp = 1;
		io = io | ((temp & 0x01) << (23 - index));
	}
	
	/** 
	* @Title: setIOPort 
	* @Description: 设置指定位置的io口状态
	* @param index 指定位置
	* @param state 状态
	*/
	public void setIOPort(int index, boolean state){
		byte temp = (byte) (state ? 1 : 0);
		io = io | ((temp & 0x01) << (35 - index));
	}
	
	/** 
	* @Title: 设置输入口 
	* @Description: 设置指定位置的io口状态
	* @param index 指定位置
	* @param state 状态
	*/
	public void setInputIOPort(int index, boolean state){
		byte temp = (byte) (state ? 1 : 0);
		io = io | ((temp & 0x01) << (27 - index));
	}
	
	/** 
	* @Title: 设置输出口 
	* @Description: 设置指定位置的io口状态
	* @param index 指定位置
	* @param state 状态
	*/
	public void setOutputIOPort(int index, boolean state){
		byte temp = (byte) (state ? 1 : 0);
		io = io | ((temp & 0x01) << (23 - index));
	}

	/** 
	* @Title: reverseBytePerBit 
	* @Description: 字节顺序位数逆序排列
	* @param sGlue
	*/
	public boolean[] reverseBytePerBit(boolean [] sGlue){
		boolean midValue;
		for(int i = 0; i < 24; i+=8){
			for(int j = 0; j < 4; j++){
				midValue = sGlue[j + i];
				sGlue[j + i] = sGlue[i + 7 - j];
				sGlue[i + 7 - j] = midValue;
			}
		}
		return sGlue;
	}
	/** 
	* @Title: getIOPort 
	* @Description: 获取输出口状态
	* @return 输出口状态
	*/
	public boolean[] getIOPort(){
		boolean[] ioPort = new boolean[24];
		for(int i = 0; i < 24; i++){
			ioPort[i] = (io & (0x01 << (23 - i))) > 0 ? true : false;
		}
		return ioPort;
	}
	
	/**
	 * @description int转变成长度为12的boolean数组,获取输入输出IO口的状态,返回的数组长度为12
	 * @param res
	 * @return 输入,输出IO口的状态
	 */
	public boolean[] getOutputPort(int res) {
		boolean[] ioPort = new boolean[12];
		for (int i = 0; i < 12; i++) {
			ioPort[i] = false;
		}
		ioPort[0] = (res & 0x01) == 1 ? true : false;
		ioPort[1] = ((res >> 1) & 0x01) == 1 ? true : false;
		ioPort[2] = ((res >> 2) & 0x01) == 1 ? true : false;
		ioPort[3] = ((res >> 3) & 0x01) == 1 ? true : false;
		return ioPort;
	}
	
	/** 
	* @Title: setIOPortClose 
	* @Description: 关闭所有出胶(锡)口/输入口
	*/
	public void setIOPortClose() {
		io = 0;
		updateByteData();
	}

	/** 
	* @Title: updateByteData 
	* @Description: 刷新数据
	*/
	private void updateByteData() {
		// 刷新数据
		pointInfo[0] = byte0;
		pointInfo[1] = byte1;
		pointInfo[2] = byte2;
		pointInfo[3] = (byte) ((io & 0x00ff0000)>>>16);
		pointInfo[4] = (byte) ((io & 0x0000ff00)>>>8);
		pointInfo[5] = (byte) (io & 0x000000ff);
	}
	
	/** 
	* @Title: getPointInfo 
	* @Description: 获取数据 
	* @return 点信息byte数组
	*/
	public byte[] getPointInfo() {
		updateByteData();
		return pointInfo;
	}
}
