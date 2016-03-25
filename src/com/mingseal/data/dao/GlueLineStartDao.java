/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.db.DBInfo.TableLineStart;
import com.mingseal.data.point.glueparam.PointGlueLineStartParam;
import com.mingseal.utils.ArraysComprehension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class GlueLineStartDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TableLineStart._ID, TableLineStart.OUT_GLUE_TIME_PREV, TableLineStart.OUT_GLUE_TIME,
			TableLineStart.TIME_MODE, TableLineStart.MOVE_SPEED, TableLineStart.IS_OUT_GLUE,
//			TableLineStart.STOP_GLUE_TIME_PREV, TableLineStart.STOP_GLUE_TIME, TableLineStart.UP_HEIGHT,
			TableLineStart.GLUE_PORT };

	public GlueLineStartDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 增加一条线起始点的数据
	 * 
	 * @param pointGlueLineStartParam
	 * @return 刚增加的这条数据的主键
	 */
	public long insertGlueLineStart(PointGlueLineStartParam pointGlueLineStartParam) {
		db = dbHelper.getWritableDatabase();

		values = new ContentValues();
		values.put(TableLineStart.OUT_GLUE_TIME_PREV, pointGlueLineStartParam.getOutGlueTimePrev());
		values.put(TableLineStart.OUT_GLUE_TIME, pointGlueLineStartParam.getOutGlueTime());
		values.put(TableLineStart.TIME_MODE, (boolean) pointGlueLineStartParam.isTimeMode() ? 1 : 0);
		values.put(TableLineStart.MOVE_SPEED, pointGlueLineStartParam.getMoveSpeed());
		values.put(TableLineStart.IS_OUT_GLUE, (boolean) pointGlueLineStartParam.isOutGlue() ? 1 : 0);
//		values.put(TableLineStart.STOP_GLUE_TIME_PREV, pointGlueLineStartParam.getStopGlueTimePrev());
//		values.put(TableLineStart.STOP_GLUE_TIME, pointGlueLineStartParam.getStopGlueTime());
//		values.put(TableLineStart.UP_HEIGHT, pointGlueLineStartParam.getUpHeight());
		values.put(TableLineStart.GLUE_PORT, Arrays.toString(pointGlueLineStartParam.getGluePort()));

		long rowID = db.insert(TableLineStart.LINE_START_TABLE, TableLineStart._ID, values);

		// 释放资源
		db.close();

		return rowID;
	}

	/**
	 * 取得所有线起始点的数据
	 * 
	 * @return
	 */
	public List<PointGlueLineStartParam> findAllGlueLineStartParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineStartParam> startLists = null;
		PointGlueLineStartParam start = null;

		Cursor cursor = db.query(TableLineStart.LINE_START_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			startLists = new ArrayList<PointGlueLineStartParam>();
			while (cursor.moveToNext()) {
				start = new PointGlueLineStartParam();
				start.set_id(cursor.getInt(cursor.getColumnIndex(TableLineStart._ID)));
				start.setOutGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineStart.OUT_GLUE_TIME_PREV)));
				start.setOutGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.OUT_GLUE_TIME)));
				start.setTimeMode(cursor.getInt(cursor.getColumnIndex(TableLineStart.TIME_MODE)) == 0 ? false : true);
				start.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableLineStart.MOVE_SPEED)));
				start.setOutGlue(cursor.getInt(cursor.getColumnIndex(TableLineStart.IS_OUT_GLUE)) == 0 ? false : true);
//				start.setStopGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME_PREV)));
//				start.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME)));
//				start.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineStart.UP_HEIGHT)));
				start.setGluePort(ArraysComprehension
						.boooleanParse(cursor.getString(cursor.getColumnIndex(TableLineStart.GLUE_PORT))));

				startLists.add(start);
			}
		}

		cursor.close();
		db.close();
		return startLists;
	}

	/**
	 * 通过主键id找到PointGlueLineStartParam参数
	 * 
	 * @param id
	 * @return PointGlueLineStartParam
	 */
	public PointGlueLineStartParam getPointGlueLineStartParamByID(int id) {
		PointGlueLineStartParam param = new PointGlueLineStartParam();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableLineStart.LINE_START_TABLE, columns, TableLineStart._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param.setOutGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineStart.OUT_GLUE_TIME_PREV)));
					param.setOutGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.OUT_GLUE_TIME)));
					param.setTimeMode(
							cursor.getInt(cursor.getColumnIndex(TableLineStart.TIME_MODE)) == 0 ? false : true);
					param.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableLineStart.MOVE_SPEED)));
					param.setOutGlue(
							cursor.getInt(cursor.getColumnIndex(TableLineStart.IS_OUT_GLUE)) == 0 ? false : true);
//					param.setStopGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME_PREV)));
//					param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME)));
//					param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineStart.UP_HEIGHT)));
					param.setGluePort(ArraysComprehension
							.boooleanParse(cursor.getString(cursor.getColumnIndex(TableLineStart.GLUE_PORT))));

				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.endTransaction();
			db.close();
		}
		return param;
	}

	/**
	 * 通过List<Integer> 列表来查找对应的 PointGlueLineStartParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueLineStartParam>
	 */
	public List<PointGlueLineStartParam> getPointGlueLineStartParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineStartParam> params = new ArrayList<>();
		PointGlueLineStartParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableLineStart.LINE_START_TABLE, columns, TableLineStart._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueLineStartParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableLineStart._ID)));
						param.setOutGlueTimePrev(
								cursor.getInt(cursor.getColumnIndex(TableLineStart.OUT_GLUE_TIME_PREV)));
						param.setOutGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.OUT_GLUE_TIME)));
						param.setTimeMode(
								cursor.getInt(cursor.getColumnIndex(TableLineStart.TIME_MODE)) == 0 ? false : true);
						param.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableLineStart.MOVE_SPEED)));
						param.setOutGlue(
								cursor.getInt(cursor.getColumnIndex(TableLineStart.IS_OUT_GLUE)) == 0 ? false : true);
//						param.setStopGlueTimePrev(
//								cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME_PREV)));
//						param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineStart.STOP_GLUE_TIME)));
//						param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineStart.UP_HEIGHT)));
						param.setGluePort(ArraysComprehension
								.boooleanParse(cursor.getString(cursor.getColumnIndex(TableLineStart.GLUE_PORT))));
						params.add(param);
					}
				}
				cursor.close();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return params;
	}

	/**
	 * 通过参数方案找到当前方案的主键(是否出胶,停胶前延时,停胶后延时,抬起高度起始点是没有数据的)
	 * 
	 * @param pointGlueLineStartParam
	 * @return 当前方案的主键
	 */
	public int getLineStartParamIDByParam(PointGlueLineStartParam pointGlueLineStartParam) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableLineStart.LINE_START_TABLE, columns,
				TableLineStart.OUT_GLUE_TIME_PREV + "=? and " + TableLineStart.OUT_GLUE_TIME + "=? and "
						+ TableLineStart.TIME_MODE + "=? and " + TableLineStart.MOVE_SPEED + "=? and "
						+ TableLineStart.IS_OUT_GLUE + "=? and " + TableLineStart.STOP_GLUE_TIME_PREV + "=? and "
						+ TableLineStart.STOP_GLUE_TIME + "=? and " + TableLineStart.UP_HEIGHT + "=? and "
						+ TableLineStart.GLUE_PORT + "=?",
				new String[] { String.valueOf(pointGlueLineStartParam.getOutGlueTimePrev()),
						String.valueOf(pointGlueLineStartParam.getOutGlueTime()),
						String.valueOf(pointGlueLineStartParam.isTimeMode() ? 1 : 0),
						String.valueOf(pointGlueLineStartParam.getMoveSpeed()),
						String.valueOf(pointGlueLineStartParam.isOutGlue() ? 1 : 0),
//						String.valueOf(pointGlueLineStartParam.getStopGlueTimePrev()),
						String.valueOf(pointGlueLineStartParam.getOutGlueTime()),
//						String.valueOf(pointGlueLineStartParam.getUpHeight()),
						Arrays.toString(pointGlueLineStartParam.getGluePort()) },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableLineStart._ID));
			}
		}
		db.close();
		if (-1 == id) {
			//先查询除了是否出胶,停胶前延时,停胶后延时,抬起高度之外有没有相对应的方案参数
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TableLineStart.LINE_START_TABLE, columns,
					TableLineStart.OUT_GLUE_TIME_PREV + "=? and " + TableLineStart.OUT_GLUE_TIME + "=? and "
							+ TableLineStart.TIME_MODE + "=? and " + TableLineStart.MOVE_SPEED + "=? and "
							+ TableLineStart.GLUE_PORT + "=?",
					new String[] { String.valueOf(pointGlueLineStartParam.getOutGlueTimePrev()),
							String.valueOf(pointGlueLineStartParam.getOutGlueTime()),
							String.valueOf(pointGlueLineStartParam.isTimeMode() ? 1 : 0),
							String.valueOf(pointGlueLineStartParam.getMoveSpeed()),
							Arrays.toString(pointGlueLineStartParam.getGluePort()) },
					null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex(TableLineStart._ID));
				}
			}
			db.close();
			
			if(-1 == id){
				id = (int) insertGlueLineStart(pointGlueLineStartParam);
			}
		}
		return id;

	}

	/**
	 * @Title  deleteParam
	 * @Description 删除某一行数据
	 * @author wj
	 * @param pointGlueLineStartParam
	 * @return
	 */
	public int deleteParam(PointGlueLineStartParam pointGlueLineStartParam) {
		db = dbHelper.getWritableDatabase();
		int rowID = db.delete(DBInfo.TableLineStart.LINE_START_TABLE, TableLineStart._ID + "=?",
				new String[] { String.valueOf(pointGlueLineStartParam.get_id()) });

		db.close();
		return rowID;
	}

}
