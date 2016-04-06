/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.db.DBInfo.TableAlone;
import com.mingseal.data.db.DBInfo.TableFaceStart;
import com.mingseal.data.db.DBInfo.TableLineEnd;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.utils.ArraysComprehension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class GlueFaceStartDao {

	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TableFaceStart._ID, TableFaceStart.OUT_GLUE_TIME_PREV, TableFaceStart.OUT_GLUE_TIME,
			TableFaceStart.MOVE_SPEED, TableFaceStart.IS_OUT_GLUE, TableFaceStart.STOP_GLUE_TIME,
			TableFaceStart.START_DIR, TableFaceStart.GLUE_PORT };

	public GlueFaceStartDao(Context context) {
		dbHelper = new DBHelper(context);
	}
	/**
	 * @Title  upDateGlueLineMid
	 * @Description 更新一条独立点数据
	 * @author wj
	 * @param pointGlueFaceStartParam
	 * @return  影响的行数，0表示错误
	 */
	public int upDateGlueFaceStart(PointGlueFaceStartParam pointGlueFaceStartParam){
		int rowid = 0;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableFaceStart.OUT_GLUE_TIME_PREV, pointGlueFaceStartParam.getOutGlueTimePrev());
			values.put(TableFaceStart.OUT_GLUE_TIME, pointGlueFaceStartParam.getOutGlueTime());
			values.put(TableFaceStart.MOVE_SPEED, pointGlueFaceStartParam.getMoveSpeed());
			values.put(TableFaceStart.IS_OUT_GLUE, (boolean) pointGlueFaceStartParam.isOutGlue() ? 1 : 0);
			values.put(TableFaceStart.STOP_GLUE_TIME, pointGlueFaceStartParam.getStopGlueTime());
			values.put(TableFaceStart.START_DIR, (boolean) pointGlueFaceStartParam.isStartDir() ? 1 : 0);
			values.put(TableFaceStart.GLUE_PORT, Arrays.toString(pointGlueFaceStartParam.getGluePort()));
			rowid = db.update(DBInfo.TableFaceStart.FACE_START_TABLE, values,TableFaceStart._ID +"=?", new String[]{String.valueOf(pointGlueFaceStartParam.get_id())});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return rowid; 
	}
	/**
	 * 增加一条面起始点的数据
	 * 
	 * @param pointGlueFaceStartParam
	 * @return
	 */
	public long insertGlueFaceStart(PointGlueFaceStartParam pointGlueFaceStartParam) {
		long rowID = 0;
		db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableFaceStart._ID, pointGlueFaceStartParam.get_id());
			values.put(TableFaceStart.OUT_GLUE_TIME_PREV, pointGlueFaceStartParam.getOutGlueTimePrev());
			values.put(TableFaceStart.OUT_GLUE_TIME, pointGlueFaceStartParam.getOutGlueTime());
			values.put(TableFaceStart.MOVE_SPEED, pointGlueFaceStartParam.getMoveSpeed());
			values.put(TableFaceStart.IS_OUT_GLUE, (boolean) pointGlueFaceStartParam.isOutGlue() ? 1 : 0);
			values.put(TableFaceStart.STOP_GLUE_TIME, pointGlueFaceStartParam.getStopGlueTime());
			values.put(TableFaceStart.START_DIR, (boolean) pointGlueFaceStartParam.isStartDir() ? 1 : 0);
			values.put(TableFaceStart.GLUE_PORT, Arrays.toString(pointGlueFaceStartParam.getGluePort()));
			rowID = db.insert(TableFaceStart.FACE_START_TABLE, TableFaceStart._ID, values);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.endTransaction();
			// 释放资源
			db.close();
		}
		return rowID;

	}

	/**
	 * 取得所有面起始点的数据
	 * 
	 * @return
	 */
	public List<PointGlueFaceStartParam> findAllGlueFaceStartParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueFaceStartParam> startLists = null;
		PointGlueFaceStartParam start = null;

		Cursor cursor = db.query(TableFaceStart.FACE_START_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			startLists = new ArrayList<PointGlueFaceStartParam>();
			while (cursor.moveToNext()) {
				start = new PointGlueFaceStartParam();
				start.set_id(cursor.getInt(cursor.getColumnIndex(TableFaceStart._ID)));
				start.setOutGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableFaceStart.OUT_GLUE_TIME_PREV)));
				start.setOutGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceStart.OUT_GLUE_TIME)));
				start.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableFaceStart.MOVE_SPEED)));
				start.setOutGlue(cursor.getInt(cursor.getColumnIndex(TableFaceStart.IS_OUT_GLUE)) == 0 ? false : true);
				start.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceStart.STOP_GLUE_TIME)));
				start.setStartDir(cursor.getInt(cursor.getColumnIndex(TableFaceStart.START_DIR)) == 0 ? false : true);
				start.setGluePort(ArraysComprehension
						.boooleanParse(cursor.getString(cursor.getColumnIndex(TableFaceStart.GLUE_PORT))));

				startLists.add(start);
			}
		}
		cursor.close();
		db.close();
		return startLists;
	}

	/**
	 * 通过id找到面起点的参数
	 * 
	 * @param id
	 * @return PointGlueFaceStartParam
	 */
	public PointGlueFaceStartParam getPointFaceStartParamByID(int id) {
		PointGlueFaceStartParam param = new PointGlueFaceStartParam();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableFaceStart.FACE_START_TABLE, columns, TableFaceStart._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param.set_id(cursor.getInt(cursor.getColumnIndex(TableFaceStart._ID)));
					param.setOutGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableFaceStart.OUT_GLUE_TIME_PREV)));
					param.setOutGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceStart.OUT_GLUE_TIME)));
					param.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableFaceStart.MOVE_SPEED)));
					param.setOutGlue(
							cursor.getInt(cursor.getColumnIndex(TableFaceStart.IS_OUT_GLUE)) == 0 ? false : true);
					param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceStart.STOP_GLUE_TIME)));
					param.setStartDir(
							cursor.getInt(cursor.getColumnIndex(TableFaceStart.START_DIR)) == 0 ? false : true);
					param.setGluePort(ArraysComprehension
							.boooleanParse(cursor.getString(cursor.getColumnIndex(TableFaceStart.GLUE_PORT))));

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
	 * 通过List<Integer> 列表来查找对应的 PointGlueFaceStartParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueFaceStartParam>
	 */
	public List<PointGlueFaceStartParam> getPointFaceStartParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueFaceStartParam> params = new ArrayList<>();
		PointGlueFaceStartParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableFaceStart.FACE_START_TABLE, columns, TableFaceStart._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueFaceStartParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableFaceStart._ID)));
						param.setOutGlueTimePrev(
								cursor.getInt(cursor.getColumnIndex(TableFaceStart.OUT_GLUE_TIME_PREV)));
						param.setOutGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceStart.OUT_GLUE_TIME)));
						param.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableFaceStart.MOVE_SPEED)));
						param.setOutGlue(
								cursor.getInt(cursor.getColumnIndex(TableFaceStart.IS_OUT_GLUE)) == 0 ? false : true);
						param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceStart.STOP_GLUE_TIME)));
						param.setStartDir(
								cursor.getInt(cursor.getColumnIndex(TableFaceStart.START_DIR)) == 0 ? false : true);
						param.setGluePort(ArraysComprehension
								.boooleanParse(cursor.getString(cursor.getColumnIndex(TableFaceStart.GLUE_PORT))));
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
	 * 通过参数方案寻找到当前方案的主键(没有停胶延时)
	 * 
	 * @param pointGlueFaceStartParam
	 * @return 当前方案的主键
	 */
	public int getFaceStartParamIDByParam(PointGlueFaceStartParam pointGlueFaceStartParam) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableFaceStart.FACE_START_TABLE, columns,
				TableFaceStart.OUT_GLUE_TIME_PREV + "=? and " + TableFaceStart.OUT_GLUE_TIME + "=? and "
						+ TableFaceStart.MOVE_SPEED + "=? and " + TableFaceStart.IS_OUT_GLUE + "=? and "
						+ TableFaceStart.STOP_GLUE_TIME + "=? and " + TableFaceStart.START_DIR + "=? and "
						+ TableFaceStart.GLUE_PORT + "=?",
				new String[] { String.valueOf(pointGlueFaceStartParam.getOutGlueTimePrev()),
						String.valueOf(pointGlueFaceStartParam.getOutGlueTime()),
						String.valueOf(pointGlueFaceStartParam.getMoveSpeed()),
						String.valueOf(pointGlueFaceStartParam.isOutGlue() ? 1 : 0),
						String.valueOf(pointGlueFaceStartParam.getStopGlueTime()),
						String.valueOf(pointGlueFaceStartParam.isStartDir() ? 1 : 0),
						Arrays.toString(pointGlueFaceStartParam.getGluePort()) },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableFaceStart._ID));
			}
		}
		db.close();
		if (-1 == id) {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TableFaceStart.FACE_START_TABLE, columns,
					TableFaceStart.OUT_GLUE_TIME_PREV + "=? and " + TableFaceStart.OUT_GLUE_TIME + "=? and "
							+ TableFaceStart.MOVE_SPEED + "=? and " + TableFaceStart.IS_OUT_GLUE + "=? and "
							+ TableFaceStart.START_DIR + "=? and " + TableFaceStart.GLUE_PORT + "=?",
					new String[] { String.valueOf(pointGlueFaceStartParam.getOutGlueTimePrev()),
							String.valueOf(pointGlueFaceStartParam.getOutGlueTime()),
							String.valueOf(pointGlueFaceStartParam.getMoveSpeed()),
							String.valueOf(pointGlueFaceStartParam.isOutGlue() ? 1 : 0),
							String.valueOf(pointGlueFaceStartParam.isStartDir() ? 1 : 0),
							Arrays.toString(pointGlueFaceStartParam.getGluePort()) },
					null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex(TableFaceStart._ID));
				}
			}
			db.close();
			if (-1 == id) {
				id = (int) insertGlueFaceStart(pointGlueFaceStartParam);
			}

		}
		return id;
	}
	
	/**
	 * @Title  deleteGlueAlone
	 * @Description 删除某一行数据
	 * @author wj
	 * @param pointGlueAloneParam
	 * @return 1为成功删除，0为未成功删除
	 */
	public Integer deleteParam(PointGlueFaceStartParam pointGlueFaceStartParam) {
		db = dbHelper.getWritableDatabase();
		int rowID = db.delete(DBInfo.TableFaceStart.FACE_START_TABLE, TableFaceStart._ID + "=?",
				new String[] { String.valueOf(pointGlueFaceStartParam.get_id()) });

		db.close();
		return rowID;
	}

}
