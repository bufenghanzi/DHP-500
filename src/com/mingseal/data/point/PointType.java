package com.mingseal.data.point;

/**
 * 点类型枚举类
 * @author lyq
 */
public enum PointType{
	POINT_NULL(0,""),
	POINT_GLUE_BASE(0x1,"基准点"),
	POINT_GLUE_ALONE(0x2,"独立点"),
	POINT_GLUE_LINE_START(0x3,"起始点"),
	POINT_GLUE_LINE_ARC(0x4,"圆弧点"),
	POINT_GLUE_LINE_MID(0x5,"中间点"),
	POINT_GLUE_LINE_END(0x6,"结束点"),
	POINT_GLUE_FACE_START(0x7,"面起点"),
	POINT_GLUE_FACE_END(0x8,"面终点"),
	POINT_GLUE_INPUT(0x9,"输入IO"),
	POINT_GLUE_OUTPUT(0xA,"输出IO"),
	POINT_GLUE_CLEAR(0xB,"清胶点"),
	POINT_GLUE_CLEARIO(0xC,"清胶"),
	
	POINT_WELD_BASE(0x100,"基准点"),
	POINT_WELD_ALONE(0x101,"独立点"),
	POINT_WELD_LINE_START(0x102,"起始点"),
	POINT_WELD_LINE_ARC(0x103,"圆弧点"),
	POINT_WELD_LINE_MID(0x104,"中间点"),
	POINT_WELD_LINE_END(0x105,"结束点"),
	POINT_WELD_FACE_START(0x106,"面起点"),
	POINT_WELD_FACE_END(0x107,"面终点"),
	POINT_WELD_BLOW(0x109,"吹锡点"),
	POINT_WELD_WORK(0x10A,"作业点"),
	POINT_TYPE_ALL(0x10B,"所有点");
	
	private int index;
	private String type;
	
	PointType(int _num,String _type){
		index = _num;
		type = _type;
	}
	
	/**
	 * 获取点类型
	 * 
	 * @return 点类型
	 */
	public int getValue() {
		return index;
	}
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 通过value值取得对应的type中文名称
	 * 
	 * @param _value
	 * @return
	 */
	public static String getTypeName(int _value) {
		for (PointType pointType : PointType.values()) {
			if (pointType.getValue() == _value) {
				return pointType.getType();
			}
		}
		return String.valueOf(_value);
	}
	
}
