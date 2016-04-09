/**
 * 
 */
package com.mingseal.application;

import java.util.HashMap;
import java.util.List;

import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointTask;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
import com.mingseal.data.user.User;

import android.app.Application;
import android.os.Handler;

/**
 * @author 商炎炳
 * @description 保存全局变量
 */
public class UserApplication extends Application {
	private User user;
	private List<Point> points;// Point的List集合
	// private List<Integer> pointIDs;// 任务中任务点的主键集合
	private PointTask pointTask;// 任务
	private HashMap<Integer, PointGlueAloneParam> aloneParamMaps;// 独立点Map集合
	private HashMap<Integer, PointGlueLineStartParam> lineStartParamMaps;// 线起始点Map集合
	private HashMap<Integer, PointGlueLineMidParam> lineMidParamMaps;// 线中间点Map集合
	private HashMap<Integer, PointGlueLineEndParam> lineEndParamMaps;// 线结束点Map集合
	private HashMap<Integer, PointGlueFaceStartParam> faceStartParamMaps;// 面起始点Map集合
	private HashMap<Integer, PointGlueFaceEndParam> faceEndParamMaps;// 面结束点Map集合
	private HashMap<Integer, PointGlueClearParam> clearParamMaps;// 清胶点Map集合
	private HashMap<Integer, PointGlueInputIOParam> inputParamMaps;// 输入IO点Map集合
	private HashMap<Integer, PointGlueOutputIOParam> outputParamMaps;// 输出IO点Map集合
	private boolean isWifiConnecting = false;// wifi连接情况
	private static Handler mHandler;

	public static Handler getHandler() {
		return mHandler;
	}

	/**
	 * @return 全局User对象
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置全局User对象
	 * 
	 * @param user
	 *            用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return 全局Point的List集合
	 */
	public List<Point> getPoints() {
		return points;
	}

	/**
	 * 设置全局的Point的List集合
	 * 
	 * @param points
	 *            Point的List集合
	 */
	public void setPoints(List<Point> points) {
		this.points = points;
	}

	/**
	 * @return 任务
	 */
	public PointTask getPointTask() {
		return pointTask;
	}

	/**
	 * 设置任务
	 * 
	 * @param pointTask
	 */
	public void setPointTask(PointTask pointTask) {
		this.pointTask = pointTask;
	}

	/**
	 * @return 独立点参数的Map集合
	 */
	public HashMap<Integer, PointGlueAloneParam> getAloneParamMaps() {
		return aloneParamMaps;
	}

	/**
	 * 设置独立点参数的Map集合
	 * 
	 * @param aloneParamMaps
	 *            独立点参数Map
	 */
	public void setAloneParamMaps(
			HashMap<Integer, PointGlueAloneParam> aloneParamMaps) {
		this.aloneParamMaps = aloneParamMaps;
	}

	/**
	 * @return 线结束点参数的Map集合
	 */
	public HashMap<Integer, PointGlueLineEndParam> getLineEndParamMaps() {
		return lineEndParamMaps;
	}

	/**
	 * 设置线结束点参数的Map集合
	 * 
	 * @param lineEndParamMaps
	 *            线结束点参数的Map
	 */
	public void setLineEndParamMaps(
			HashMap<Integer, PointGlueLineEndParam> lineEndParamMaps) {
		this.lineEndParamMaps = lineEndParamMaps;
	}

	/**
	 * @return 面结束点参数的Map集合
	 */
	public HashMap<Integer, PointGlueFaceEndParam> getFaceEndParamMaps() {
		return faceEndParamMaps;
	}

	/**
	 * 设置面结束点参数的Map集合
	 * 
	 * @param faceEndParamMaps
	 *            面结束点参数的Map集合
	 */
	public void setFaceEndParamMaps(
			HashMap<Integer, PointGlueFaceEndParam> faceEndParamMaps) {
		this.faceEndParamMaps = faceEndParamMaps;
	}

	/**
	 * @return 线起始点参数的Map集合
	 */
	public HashMap<Integer, PointGlueLineStartParam> getLineStartParamMaps() {
		return lineStartParamMaps;
	}

	/**
	 * 
	 * 设置线起始点参数的Map集合
	 * 
	 * @param lineStartParamMaps
	 *            线起始点参数的Map集合
	 */
	public void setLineStartParamMaps(
			HashMap<Integer, PointGlueLineStartParam> lineStartParamMaps) {
		this.lineStartParamMaps = lineStartParamMaps;
	}

	/**
	 * @return 线中间点参数的Map集合
	 */
	public HashMap<Integer, PointGlueLineMidParam> getLineMidParamMaps() {
		return lineMidParamMaps;
	}

	/**
	 * 设置线中间点参数的Map集合
	 * 
	 * @param lineMidParamMaps
	 *            线中间点参数的Map集合
	 */
	public void setLineMidParamMaps(
			HashMap<Integer, PointGlueLineMidParam> lineMidParamMaps) {
		this.lineMidParamMaps = lineMidParamMaps;
	}

	/**
	 * @return 面起始点参数的Map集合
	 */
	public HashMap<Integer, PointGlueFaceStartParam> getFaceStartParamMaps() {
		return faceStartParamMaps;
	}

	/**
	 * 设置面起始点参数的Map集合
	 * 
	 * @param faceStartParamMaps
	 *            面起始点参数的Map集合
	 */
	public void setFaceStartParamMaps(
			HashMap<Integer, PointGlueFaceStartParam> faceStartParamMaps) {
		this.faceStartParamMaps = faceStartParamMaps;
	}

	/**
	 * @return 清胶点参数的Map集合
	 */
	public HashMap<Integer, PointGlueClearParam> getClearParamMaps() {
		return clearParamMaps;
	}

	/**
	 * 设置清胶点参数的Map集合
	 * 
	 * @param clearParamMaps
	 *            清胶点参数的Map集合
	 */
	public void setClearParamMaps(
			HashMap<Integer, PointGlueClearParam> clearParamMaps) {
		this.clearParamMaps = clearParamMaps;
	}

	/**
	 * @return 输入IO点参数的Map集合
	 */
	public HashMap<Integer, PointGlueInputIOParam> getInputParamMaps() {
		return inputParamMaps;
	}

	/**
	 * 设置输入IO点参数的Map集合
	 * 
	 * @param inputParamMaps
	 *            输入IO点参数的Map集合
	 */
	public void setInputParamMaps(
			HashMap<Integer, PointGlueInputIOParam> inputParamMaps) {
		this.inputParamMaps = inputParamMaps;
	}

	/**
	 * @return 输出IO点参数的Map集合
	 */
	public HashMap<Integer, PointGlueOutputIOParam> getOutputParamMaps() {
		return outputParamMaps;
	}

	/**
	 * 设置输出IO点参数的Map集合
	 * 
	 * @param outputParamMaps
	 *            输出IO点参数的Map集合
	 */
	public void setOutputParamMaps(
			HashMap<Integer, PointGlueOutputIOParam> outputParamMaps) {
		this.outputParamMaps = outputParamMaps;
	}

	/**
	 * ParamsSetting中设置独立点，起始点，中间点，结束点，面起点，面终点，清胶点，输入IO，输出IO的参数方案
	 * 
	 * @param aloneParamMaps
	 *            独立点参数方案
	 * @param lineStartParamMaps
	 *            线起始点参数方案
	 * @param lineMidParamMaps
	 *            线中间点参数方案
	 * @param lineEndParamMaps
	 *            线结束点参数方案
	 * @param faceStartParamMaps
	 *            面起点参数方案
	 * @param faceEndParamMaps
	 *            面结束点参数方案
	 * @param clearParamMaps
	 *            清胶点参数方案
	 * @param inputParamMaps
	 *            输入IO点参数方案
	 * @param outputParamMaps
	 *            输出IO点参数方案
	 */
	public void setParamMaps(
			HashMap<Integer, PointGlueAloneParam> aloneParamMaps,
			HashMap<Integer, PointGlueLineStartParam> lineStartParamMaps,
			HashMap<Integer, PointGlueLineMidParam> lineMidParamMaps,
			HashMap<Integer, PointGlueLineEndParam> lineEndParamMaps,
			HashMap<Integer, PointGlueFaceStartParam> faceStartParamMaps,
			HashMap<Integer, PointGlueFaceEndParam> faceEndParamMaps,
			HashMap<Integer, PointGlueClearParam> clearParamMaps,
			HashMap<Integer, PointGlueInputIOParam> inputParamMaps,
			HashMap<Integer, PointGlueOutputIOParam> outputParamMaps) {
		this.aloneParamMaps = aloneParamMaps;
		this.lineStartParamMaps = lineStartParamMaps;
		this.lineMidParamMaps = lineMidParamMaps;
		this.lineEndParamMaps = lineEndParamMaps;
		this.faceStartParamMaps = faceStartParamMaps;
		this.faceEndParamMaps = faceEndParamMaps;
		this.clearParamMaps = clearParamMaps;
		this.inputParamMaps = inputParamMaps;
		this.outputParamMaps = outputParamMaps;
	}

	/**
	 * @Title isWifiConnecting
	 * @Description 获取wifi连接情况
	 * @return true为连接成功(获取参数成功),false为连接失败(没有获取到机器参数)
	 */
	public boolean isWifiConnecting() {
		return isWifiConnecting;
	}

	/**
	 * @Title setWifiConnecting
	 * @Description 设置wifi连接情况
	 * @param isWifiConnecting
	 *            true为连接成功(获取参数成功),false为连接失败(没有获取到机器参数)
	 */
	public void setWifiConnecting(boolean isWifiConnecting) {
		this.isWifiConnecting = isWifiConnecting;
	}

	@Override
	public void onCreate() {
		// 定义一个handler
		mHandler = new Handler();
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
