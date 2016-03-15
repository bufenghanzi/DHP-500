/**
 * @Title PointConfigParam.java
 * @Package com.mingseal.data.param
 * @Description TODO
 * @author 商炎炳
 * @date 2016年1月29日 下午12:46:52
 * @version V1.0
 */
package com.mingseal.data.param;

/**
 * @ClassName PointConfigParam
 * @Description 任务点默认值
 * @author 商炎炳
 * @date 2016年1月29日 下午12:46:52
 *
 */
public class PointConfigParam {
	/**
	 * @ClassName GlueAlone
	 * @Description 独立点的默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午12:49:10
	 *
	 */
	public static class GlueAlone {
		/**
		 * @Fields DotGlueTimeMAX: 点胶延时最大值
		 */
		public static final int DotGlueTimeMAX = 6000;
		/**
		 * @Fields StopGlueTimeMAX: 停胶延时最大值
		 */
		public static final int StopGlueTimeMAX = 6000;
		/**
		 * @Fields UpHeightMAX: 抬起高度最大值
		 */
		public static final int UpHeightMAX = 3000;
		/**
		 * @Fields GlueAloneMIN: 点胶延时,停胶延时,抬起高度最小值
		 */
		public static final int GlueAloneMIN = 0;
	}

	/**
	 * @ClassName GlueLineStart
	 * @Description 线起始点默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午12:59:30
	 *
	 */
	public static class GlueLineStart {
		/**
		 * @Fields OutGlueTimePrevMax: 出胶前延时最大值
		 */
		public static final int OutGlueTimePrevMax = 6000;
		/**
		 * @Fields OutGlueTimeMax: 出胶后延时最大值
		 */
		public static final int OutGlueTimeMax = 6000;
		/**
		 * @Fields StopGlueTimePrevMax: 停胶前延时最大值
		 */
		public static final int StopGlueTimePrevMax = 6000;
		/**
		 * @Fields StopGlueTimeMax: 停胶后延时最大值
		 */
		public static final int StopGlueTimeMax = 6000;
		/**
		 * @Fields MoveSpeedMax: 轨迹速度最大值
		 */
		public static final int MoveSpeedMax = 3000;
		/**
		 * @Fields MoveSpeedMin: 轨迹速度最小值
		 */
		public static final int MoveSpeedMin = 1;
		/**
		 * @Fields UpHeightMax: 抬起高度最大值
		 */
		public static final int UpHeightMax = 3000;
		/**
		 * @Fields GlueLineStartMin: 出胶前延时,出胶后延时,停胶前延时,停胶后延时,抬起高度最小值
		 */
		public static final int GlueLineStartMin = 0;
	}

	/**
	 * @ClassName GlueLineMid
	 * @Description 线中间点默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午1:10:36
	 *
	 */
	public static class GlueLineMid {
		/**
		 * @Fields MoveSpeedMax: 轨迹速度最大值
		 */
		public static final int MoveSpeedMax = 3000;

		/**
		 * @Fields GlueLineMidMin: 轨迹速度最小值
		 */
		public static final int GlueLineMidMin = 1;
	}

	/**
	 * @ClassName GlueLineEnd
	 * @Description 线结束点默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午1:50:41
	 *
	 */
	public static class GlueLineEnd {
		/**
		 * @Fields StopGlueTimePrevMax: 停胶前延时最大值
		 */
		public static final int StopGlueTimePrevMax = 6000;
		/**
		 * @Fields StopGlueTimeMax: 停胶后延时最大值
		 */
		public static final int StopGlueTimeMax = 6000;
		/**
		 * @Fields UpHeightMax: 抬起高度最大值
		 */
		public static final int UpHeightMax = 3000;
		/**
		 * @Fields BreakGlueLenMax: 提前停胶距离最大值
		 */
		public static final int BreakGlueLenMax = 3000;
		/**
		 * @Fields DrawDistance: 拉丝距离最大值
		 */
		public static final int DrawDistance = 3000;
		/**
		 * @Fields DrawSpeed: 拉丝速度最大值
		 */
		public static final int DrawSpeed = 3000;
		/**
		 * @Fields GlueLineEndMin: 停胶前延时,停胶后延时,抬起高度,提前停胶距离,拉丝距离,拉丝速度最小值
		 */
		public static final int GlueLineEndMin = 0;

	}

	/**
	 * @ClassName GlueClear
	 * @Description 清胶点默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午3:25:26
	 *
	 */
	public static class GlueClear {
		/**
		 * @Fields ClearGlueTimeMax: 清胶延时最大值
		 */
		public static final int ClearGlueTimeMax = 6000;
		/**
		 * @Fields GlueClearMin: 清胶延时最小值
		 */
		public static final int GlueClearMin = 0;
	}

	/**
	 * @ClassName GlueInput
	 * @Description 输入IO默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午3:36:02
	 *
	 */
	public static class GlueInput {
		/**
		 * @Fields GoTimePrevMax: 动作前延时最大值
		 */
		public static final int GoTimePrevMax = 6000;
		/**
		 * @Fields GoTimeNextMax: 动作后延时最大值
		 */
		public static final int GoTimeNextMax = 6000;
		/**
		 * @Fields GlueInputMin: 动作前延时,动作后延时最小值
		 */
		public static final int GlueInputMin = 0;
	}

	/**
	 * @ClassName GlueOutput
	 * @Description 输出IO默认参数值
	 * @author 商炎炳
	 * @date 2016年1月29日 下午3:40:27
	 *
	 */
	public static class GlueOutput {
		/**
		 * @Fields GoTimePrevMax: 动作前延时最大值
		 */
		public static final int GoTimePrevMax = 6000;
		/**
		 * @Fields GoTimeNextMax: 动作后延时最大值
		 */
		public static final int GoTimeNextMax = 6000;
		/**
		 * @Fields GlueInputMin: 动作前延时,动作后延时最小值
		 */
		public static final int GlueOutputMin = 0;

	}
	
	/**
	 * @ClassName GlueFaceEnd
	 * @Description 面终点默认参数值
	 * @author 商炎炳
	 * @date 2016年2月1日 下午2:52:00
	 *
	 */
	public static class GlueFaceEnd{
		/**
		 * @Fields StopGlueTimeMax: 停胶延时最大值
		 */
		public static final int StopGlueTimeMax = 6000;
		/**
		 * @Fields UpHeightMax: 抬起高度最大值
		 */
		public static final int UpHeightMax = 3000;
		/**
		 * @Fields LineNumMax: 直线条数最大值
		 */
		public static final int LineNumMax = 6000;
		/**
		 * @Fields GlueFaceEndMin: 面终点停胶延时,抬起高度,直线条数最小值
		 */
		public static final int GlueFaceEndMin = 0;
	}
	
	/**
	 * @ClassName GlueFaceStart
	 * @Description 面起点默认参数值
	 * @author 商炎炳
	 * @date 2016年2月2日 上午8:15:22
	 *
	 */
	public static class GlueFaceStart{
		/**
		 * @Fields OutGlueTimePrevMax: 出胶前延时最大值
		 */
		public static final int OutGlueTimePrevMax = 6000;
		/**
		 * @Fields OutGlueTimeMax: 出胶后延时最大值
		 */
		public static final int OutGlueTimeMax = 6000;
		/**
		 * @Fields moveSpeedMax: 轨迹速度最大值
		 */
		public static final int MoveSpeedMax = 3000;
		/**
		 * @Fields moveSpeedMin: 轨迹速度最小值
		 */
		public static final int MoveSpeedMin = 1;
		/**
		 * @Fields stopGlueTimeMax: 停胶延时最大值
		 */
		public static final int StopGlueTimeMax = 6000;
		/**
		 * @Fields GlueFaceStartMin: 出胶前延时,出胶后延时,停胶延时最小值
		 */
		public static final int GlueFaceStartMin = 0;
		
	}
}
