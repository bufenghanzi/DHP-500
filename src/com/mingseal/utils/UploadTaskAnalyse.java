/**
 * 
 */
package com.mingseal.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mingseal.data.dao.GlueAloneDao;
import com.mingseal.data.dao.GlueClearDao;
import com.mingseal.data.dao.GlueFaceEndDao;
import com.mingseal.data.dao.GlueFaceStartDao;
import com.mingseal.data.dao.GlueInputDao;
import com.mingseal.data.dao.GlueLineEndDao;
import com.mingseal.data.dao.GlueLineMidDao;
import com.mingseal.data.dao.GlueLineStartDao;
import com.mingseal.data.dao.GlueOutputDao;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;

import android.content.Context;
import android.util.Log;

/**
 * @author 商炎炳
 * @description 上传解析数据成功之后需要把数据变成自己想要的List格式
 */
public class UploadTaskAnalyse {

	private static final String TAG = "UploadTaskAnalyse";
	/**
	 * 独立点的数据库操作
	 */
	private GlueAloneDao glueAloneDao;// 独立点的数据库操作
	/**
	 * 起始点的数据库操作
	 */
	private GlueLineStartDao glueLineStartDao;// 起始点的数据库操作
	/**
	 * 中间点的数据库操作
	 */
	private GlueLineMidDao glueLineMidDao;// 中间点的数据库操作
	/**
	 * 结束点的数据库操作
	 */
	private GlueLineEndDao glueLineEndDao;// 结束点的数据库操作
	/**
	 * 面起始点的数据库操作
	 */
	private GlueFaceStartDao glueFaceStartDao;// 面起始点的数据库操作
	/**
	 * 面结束点的数据库操作
	 */
	private GlueFaceEndDao glueFaceEndDao;// 面结束点的数据库操作
	/**
	 * 清胶点的数据库操作
	 */
	private GlueClearDao glueClearDao;// 清胶点的数据库操作
	/**
	 * 输入IO的数据库操作
	 */
	private GlueInputDao glueInputDao;// 输入IO的数据库操作
	/**
	 * 输出IO的数据库操作
	 */
	private GlueOutputDao glueOutputDao;// 输出IO的数据库操作

	/**
	 * 初始化各个和数据库操作的Dao
	 * 
	 * @param context
	 */
	public UploadTaskAnalyse(Context context) {
		glueAloneDao = new GlueAloneDao(context);
		glueLineStartDao = new GlueLineStartDao(context);
		glueLineMidDao = new GlueLineMidDao(context);
		glueLineEndDao = new GlueLineEndDao(context);
		glueFaceStartDao = new GlueFaceStartDao(context);
		glueFaceEndDao = new GlueFaceEndDao(context);
		glueClearDao = new GlueClearDao(context);
		glueInputDao = new GlueInputDao(context);
		glueOutputDao = new GlueOutputDao(context);
	}

	/**
	 * 解析下载成功的任务（*重写HashCode方法*）
	 * 
	 * @param pointUploads
	 * @return 解析之后的List数据
	 */
	public List<Point> analyseTaskSuccess(List<Point> pointUploads) {
		// 上传成功里面的Point的List数组
		List<Point> points = new ArrayList<>();
		// 用于上传成功之后数据的PointParam的解析
		PointParam pointParam = null;
		// 独立点参数
		PointGlueAloneParam aloneParam = null;
		// 起始点参数
		PointGlueLineStartParam lineStartParam = null;
		// 中间点参数
		PointGlueLineMidParam lineMidParam = null;
		// 结束点参数
		PointGlueLineEndParam lineEndParam = null;
		// 面起始点参数
		PointGlueFaceStartParam faceStartParam = null;
		// 面结束点参数
		PointGlueFaceEndParam faceEndParam = null;
		// 输入IO参数
		PointGlueInputIOParam inputParam = null;
		// 输出IO参数
		PointGlueOutputIOParam outputParam = null;
		// 各个点胶口的初始化，因为下载是有24个的，实际上保存的时候是不需要这么多的
		boolean[] ports = null;
		// 独立点HashMap集合
		HashMap<PointGlueAloneParam, Integer> aloneParamMaps = new HashMap<>();
		// 起始点HashMap集合
		HashMap<PointGlueLineStartParam, Integer> lineStartParamMaps = new HashMap<>();
		// 中间点HashMap集合
		HashMap<PointGlueLineMidParam, Integer> lineMidParamMaps = new HashMap<>();
		// 结束点HashMap集合
		HashMap<PointGlueLineEndParam, Integer> lineEndParamMaps = new HashMap<>();
		// 面起始点HashMap集合
		HashMap<PointGlueFaceStartParam, Integer> faceStartParamMaps = new HashMap<>();
		// 面结束点HashMap集合
		HashMap<PointGlueFaceEndParam, Integer> faceEndParamMaps = new HashMap<>();
		// 输入IO点HashMap集合
		HashMap<PointGlueInputIOParam, Integer> inputParamMaps = new HashMap<>();
		// 输出IO点HashMap集合
		HashMap<PointGlueOutputIOParam, Integer> outputParamMaps = new HashMap<>();
		for (Point point : pointUploads) {
			if (point.getPointParam().getPointType() == PointType.POINT_GLUE_BASE) {
				// 基准点解析
				points.add(point);
			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_ALONE) {
				// 独立点解析
				aloneParam = (PointGlueAloneParam) point.getPointParam();
				ports = new boolean[20];
				for (int i = 0; i < 20; i++) {
					ports[i] = aloneParam.getGluePort()[i];
				}
				aloneParam.setGluePort(ports);

				// 先判断Map里面有没有，有的话，直接添加，无需查询数据库
				pointParam = new PointParam();
				if (aloneParamMaps.containsKey(aloneParam)) {
					pointParam.set_id(aloneParamMaps.get(aloneParam));
				} else {
					int _id = glueAloneDao.getAloneParamIdByParam(aloneParam);
					pointParam.set_id(_id);
					aloneParamMaps.put(aloneParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_ALONE);
				point.setPointParam(pointParam);
				points.add(point);

			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_START) {
				// 起始点解析
				lineStartParam = (PointGlueLineStartParam) point.getPointParam();
				ports = new boolean[20];
				for (int i = 0; i < 20; i++) {
					ports[i] = lineStartParam.getGluePort()[i];
				}
				lineStartParam.setGluePort(ports);
				// 先判断Map里面有没有，有的话，直接添加，无需查询数据库
				pointParam = new PointParam();
				if (lineStartParamMaps.containsKey(lineStartParam)) {
					pointParam.set_id(lineStartParamMaps.get(lineStartParam));
				} else {
					int _id = glueLineStartDao.getLineStartParamIDByParam(lineStartParam);
					pointParam.set_id(_id);
					lineStartParamMaps.put(lineStartParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_LINE_START);
				point.setPointParam(pointParam);
				points.add(point);

			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_MID) {
				// 中间点解析
				lineMidParam = (PointGlueLineMidParam) point.getPointParam();
				ports = new boolean[20];
				for (int i = 0; i < 20; i++) {
					ports[i] = lineMidParam.getGluePort()[i];
				}
				lineMidParam.setGluePort(ports);
				pointParam = new PointParam();
				if (lineMidParamMaps.containsKey(lineMidParam)) {
					pointParam.set_id(lineMidParamMaps.get(lineMidParam));
				} else {
					int _id = glueLineMidDao.getLineMidParamIDByParam(lineMidParam);
					pointParam.set_id(_id);
					lineMidParamMaps.put(lineMidParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_LINE_MID);
				point.setPointParam(pointParam);
				points.add(point);

			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_ARC) {
				// 圆弧点解析
				points.add(point);
			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_LINE_END) {
				// 结束点解析
				lineEndParam = (PointGlueLineEndParam) point.getPointParam();
				pointParam = new PointParam();
				if (lineEndParamMaps.containsKey(lineEndParam)) {
					pointParam.set_id(lineEndParamMaps.get(lineEndParam));
				} else {
					int _id = glueLineEndDao.getLineEndParamIDByParam(lineEndParam);
					pointParam.set_id(_id);
					lineEndParamMaps.put(lineEndParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_LINE_END);
				point.setPointParam(pointParam);
				points.add(point);

			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_FACE_START) {
				// 面起始点解析
				faceStartParam = (PointGlueFaceStartParam) point.getPointParam();
				ports = new boolean[20];
				for (int i = 0; i < 20; i++) {
					ports[i] = faceStartParam.getGluePort()[i];
				}
				faceStartParam.setGluePort(ports);
				pointParam = new PointParam();
				if (faceStartParamMaps.containsKey(faceStartParam)) {
					pointParam.set_id(faceStartParamMaps.get(faceStartParam));
				} else {
					int _id = glueFaceStartDao.getFaceStartParamIDByParam(faceStartParam);
					pointParam.set_id(_id);
					faceStartParamMaps.put(faceStartParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_FACE_START);
				point.setPointParam(pointParam);
				points.add(point);

			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_FACE_END) {
				// 面结束点
				faceEndParam = (PointGlueFaceEndParam) point.getPointParam();
				pointParam = new PointParam();
				if (faceEndParamMaps.containsKey(faceEndParam)) {
					pointParam.set_id(faceEndParamMaps.get(faceEndParam));
				} else {
					int _id = glueFaceEndDao.getFaceEndParamIDByParam(faceEndParam);
					pointParam.set_id(_id);
					faceEndParamMaps.put(faceEndParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_FACE_END);
				point.setPointParam(pointParam);
				points.add(point);
			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_INPUT) {
				// 输入IO
				inputParam = (PointGlueInputIOParam) point.getPointParam();
				pointParam = new PointParam();

				if (inputParamMaps.containsKey(inputParam)) {
					pointParam.set_id(inputParamMaps.get(inputParam));
				} else {
					int _id = glueInputDao.getInputParamIDByParam(inputParam);
					pointParam.set_id(_id);
					inputParamMaps.put(inputParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_INPUT);
				point.setPointParam(pointParam);
				points.add(point);

			} else if (point.getPointParam().getPointType() == PointType.POINT_GLUE_OUTPUT) {
				// 输出IO
				outputParam = (PointGlueOutputIOParam) point.getPointParam();
				pointParam = new PointParam();

				if (outputParamMaps.containsKey(outputParam)) {
					pointParam.set_id(outputParamMaps.get(outputParam));
				} else {
					int _id = glueOutputDao.getOutputParamIDByParam(outputParam);
					pointParam.set_id(_id);
					outputParamMaps.put(outputParam, _id);
				}

				pointParam.setPointType(PointType.POINT_GLUE_OUTPUT);
				point.setPointParam(pointParam);
				points.add(point);

			}
		}
		return points;
	}
}
