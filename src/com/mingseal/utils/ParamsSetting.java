/**
 * 
 */
package com.mingseal.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mingseal.application.UserApplication;
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
import com.mingseal.data.point.PointType;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueClearParam;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueInputIOParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;

import android.content.Context;

/**
 * @author 商炎炳
 * @description 各参数方案的设置
 */
public class ParamsSetting {

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

	private Context context;

	/**
	 * 初始化各个和数据库操作的Dao
	 * 
	 * @param context
	 */
	public ParamsSetting(Context context) {
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
	 * 将Point的List集合中每个点对应的参数类型方案设置到全局变量中
	 * <p>
	 * 最好是放到异步线程里面,因为要从数据库读取数据
	 * 
	 * @param userApplication
	 *            全局变量
	 * @param points
	 *            point的List集合
	 */
	public void setParamsToApplication(UserApplication userApplication, List<Point> points) {
		Point point;
		// 类型
		PointType pointType = PointType.POINT_NULL;
		// Point的任务参数序列
		int id = -1;
		// 保存独立点参数方案主键的List
		List<Integer> aloneIDs = new ArrayList<>();
		// 保存线起始点参数方案主键的List
		List<Integer> lineStartIDs = new ArrayList<>();
		// 保存线中间点参数方案主键的List
		List<Integer> lineMidIDs = new ArrayList<>();
		// 保存线结束点参数方案主键的List
		List<Integer> lineEndIDs = new ArrayList<>();
		// 保存面起始点参数方案主键的List
		List<Integer> faceStartIDs = new ArrayList<>();
		// 保存面结束点参数方案主键的List
		List<Integer> faceEndIDs = new ArrayList<>();
		// 保存清胶点参数方案主键的List
		List<Integer> clearIDs = new ArrayList<>();
		// 保存输入IO参数方案主键的List
		List<Integer> inputIDs = new ArrayList<>();
		// 保存输出IO参数方案主键的List
		List<Integer> outputIDs = new ArrayList<>();
		for (int i = 0; i < points.size(); i++) {
			point = points.get(i);
			pointType = point.getPointParam().getPointType();
			id = point.getPointParam().get_id();
			if (pointType.equals(PointType.POINT_GLUE_ALONE)) {
				// 如果等于独立点
				if (!aloneIDs.contains(id)) {
					aloneIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_LINE_START)) {
				// 线起始点
				if (!lineStartIDs.contains(id)) {
					lineStartIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_LINE_MID)) {
				// 线中间点
				if (!lineMidIDs.contains(id)) {
					lineMidIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_LINE_END)) {
				// 如果等于线结束点
				if (!lineEndIDs.contains(id)) {
					lineEndIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_FACE_START)) {
				// 如果为面起始点
				if (!faceStartIDs.contains(id)) {
					faceStartIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_FACE_END)) {
				// 如果等于面结束点
				if (!faceEndIDs.contains(id)) {
					faceEndIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_CLEAR)) {
				// 如果等于清胶点
				if (!clearIDs.contains(id)) {
					clearIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_INPUT)) {
				// 如果等于输入IO
				if (!inputIDs.contains(id)) {
					inputIDs.add(id);
				}
			} else if (pointType.equals(PointType.POINT_GLUE_OUTPUT)) {
				// 如果等于输出IO
				if (!outputIDs.contains(id)) {
					outputIDs.add(id);
				}
			}
		}
		// 存放独立点的参数方案
		HashMap<Integer, PointGlueAloneParam> aloneMaps = new HashMap<>();
		// 存放线起始点的参数方案
		HashMap<Integer, PointGlueLineStartParam> lineStartMaps = new HashMap<>();
		// 存放线中间点的参数方案
		HashMap<Integer, PointGlueLineMidParam> lineMidMaps = new HashMap<>();
		// 存放线结束点的参数方案
		HashMap<Integer, PointGlueLineEndParam> lineEndMaps = new HashMap<>();
		// 存放面起始点的参数方案
		HashMap<Integer, PointGlueFaceStartParam> faceStartMaps = new HashMap<>();
		// 存放面结束点的参数方案
		HashMap<Integer, PointGlueFaceEndParam> faceEndMaps = new HashMap<>();
		// 存放清胶点的参数方案
		HashMap<Integer, PointGlueClearParam> clearMaps = new HashMap<>();
		// 存放输入IO的参数方案
		HashMap<Integer, PointGlueInputIOParam> inputMaps = new HashMap<>();
		// 存放输出IO的参数方案
		HashMap<Integer, PointGlueOutputIOParam> outputMaps = new HashMap<>();

		// 获取所有独立点的参数方案
		if (!aloneIDs.isEmpty()) {
			List<PointGlueAloneParam> aloneParams = glueAloneDao.getGlueAloneParamsByIDs(aloneIDs);
			// 将方案和对应方案主键放到一个HashMap中
			for (int i = 0; i < aloneIDs.size(); i++) {
				aloneMaps.put(aloneIDs.get(i), aloneParams.get(i));
			}
		}

		// 获取所有线起始点的参数方案
		if (!lineStartIDs.isEmpty()) {
			List<PointGlueLineStartParam> lineStartParams = glueLineStartDao
					.getPointGlueLineStartParamsByIDs(lineStartIDs);
			for (int i = 0; i < lineStartIDs.size(); i++) {
				lineStartMaps.put(lineStartIDs.get(i), lineStartParams.get(i));
			}
		}

		// 获取所有线中间点的参数方案
		if (!lineMidIDs.isEmpty()) {
			List<PointGlueLineMidParam> lineMidParams = glueLineMidDao.getPointGlueLineMidParamsByIDs(lineMidIDs);

			for (int i = 0; i < lineMidIDs.size(); i++) {
				lineMidMaps.put(lineMidIDs.get(i), lineMidParams.get(i));
			}
		}

		// 获取所有线结束点的参数方案
		if (!lineEndIDs.isEmpty()) {
			List<PointGlueLineEndParam> lineEndParams = glueLineEndDao.getPointGlueLineEndParamsByIDs(lineEndIDs);

			for (int i = 0; i < lineEndIDs.size(); i++) {
				lineEndMaps.put(lineEndIDs.get(i), lineEndParams.get(i));
			}
		}

		// 获取所有面起始点的参数方案
		if (!faceStartIDs.isEmpty()) {
			List<PointGlueFaceStartParam> faceStartParams = glueFaceStartDao.getPointFaceStartParamsByIDs(faceStartIDs);
			for (int i = 0; i < faceStartIDs.size(); i++) {
				faceStartMaps.put(faceStartIDs.get(i), faceStartParams.get(i));
			}
		}

		// 获取所有面结束点的参数方案
		if (!faceEndIDs.isEmpty()) {
			List<PointGlueFaceEndParam> faceEndParams = glueFaceEndDao.getGlueFaceEndParamsByIDs(faceEndIDs);
			for (int i = 0; i < faceEndIDs.size(); i++) {
				faceEndMaps.put(faceEndIDs.get(i), faceEndParams.get(i));
			}
		}

		// 获取所有清胶点的参数方案
		if (!clearIDs.isEmpty()) {
			List<PointGlueClearParam> clearParams = glueClearDao.getGlueClearParamsByIDs(clearIDs);
			for (int i = 0; i < clearIDs.size(); i++) {
				clearMaps.put(clearIDs.get(i), clearParams.get(i));
			}
		}

		// 获取所有输入IO点的参数方案
		if (!inputIDs.isEmpty()) {
			List<PointGlueInputIOParam> inputParams = glueInputDao.getGlueInputParamsByIDs(inputIDs);
			for (int i = 0; i < inputIDs.size(); i++) {
				inputMaps.put(inputIDs.get(i), inputParams.get(i));
			}
		}

		// 获取所有输出IO点的参数方案
		if (!outputIDs.isEmpty()) {
			List<PointGlueOutputIOParam> outputParams = glueOutputDao.getGlueOutputIOParamsByIDs(outputIDs);
			for (int i = 0; i < outputIDs.size(); i++) {
				outputMaps.put(outputIDs.get(i), outputParams.get(i));
			}
		}

		// 设置到全局变量中
		userApplication.setParamMaps(aloneMaps, lineStartMaps, lineMidMaps, lineEndMaps, faceStartMaps, faceEndMaps,
				clearMaps, inputMaps, outputMaps);
	}
}
