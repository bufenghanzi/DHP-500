/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.db.DBInfo.TableFaceEnd;
import com.mingseal.data.db.DBInfo.TableLineEnd;
import com.mingseal.data.db.DBInfo.TableLineMid;
import com.mingseal.data.point.glueparam.PointGlueLineEndParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class GlueLineEndDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TableLineEnd._ID, TableLineEnd.STOP_GLUE_TIME_PREV, TableLineEnd.STOP_GLUE_TIME,
			TableLineEnd.UP_HEIGHT, TableLineEnd.BREAK_GLUE_LEN, TableLineEnd.DRAW_DISTANCE, TableLineEnd.DRAW_SPEED,
			TableLineEnd.IS_PAUSE };

	public GlueLineEndDao(Context context) {
		dbHelper = new DBHelper(context);
	}
	/**
	 * @Title  upDateGlueAlone
	 * @Description 更新一条独立点数据
	 * @author wj
	 * @param pointGlueAloneParam
	 * @return  影响的行数，0表示错误
	 */
	public int upDateGlueLineEnd(PointGlueLineEndParam pointGlueLineEndParam){
		int rowid = 0;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableLineEnd.STOP_GLUE_TIME_PREV, pointGlueLineEndParam.getStopGlueTimePrev());
			values.put(TableLineEnd.STOP_GLUE_TIME, pointGlueLineEndParam.getStopGlueTime());
			values.put(TableLineEnd.UP_HEIGHT, pointGlueLineEndParam.getUpHeight());
			values.put(TableLineEnd.BREAK_GLUE_LEN, pointGlueLineEndParam.getBreakGlueLen());
			values.put(TableLineEnd.DRAW_DISTANCE, pointGlueLineEndParam.getDrawDistance());
			values.put(TableLineEnd.DRAW_SPEED, pointGlueLineEndParam.getDrawSpeed());
			values.put(TableLineEnd.IS_PAUSE, (boolean) pointGlueLineEndParam.isPause() ? 1 : 0);
			rowid = db.update(DBInfo.TableLineEnd.LINE_END_TABLE, values,TableLineEnd._ID +"=?", new String[]{String.valueOf(pointGlueLineEndParam.get_id())});
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
	 * 增加一条线结束点的数据
	 * 
	 * @param pointGlueLineEndParam
	 * @return PointGlueLineEndParam
	 */
	public long insertGlueLineEnd(PointGlueLineEndParam pointGlueLineEndParam) {
		long rowID = 0;
		db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableLineEnd._ID, pointGlueLineEndParam.get_id());
			values.put(TableLineEnd.STOP_GLUE_TIME_PREV, pointGlueLineEndParam.getStopGlueTimePrev());
			values.put(TableLineEnd.STOP_GLUE_TIME, pointGlueLineEndParam.getStopGlueTime());
			values.put(TableLineEnd.UP_HEIGHT, pointGlueLineEndParam.getUpHeight());
			values.put(TableLineEnd.BREAK_GLUE_LEN, pointGlueLineEndParam.getBreakGlueLen());
			values.put(TableLineEnd.DRAW_DISTANCE, pointGlueLineEndParam.getDrawDistance());
			values.put(TableLineEnd.DRAW_SPEED, pointGlueLineEndParam.getDrawSpeed());
			values.put(TableLineEnd.IS_PAUSE, (boolean) pointGlueLineEndParam.isPause() ? 1 : 0);
			rowID = db.insert(TableLineEnd.LINE_END_TABLE, TableLineEnd._ID, values);
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
	 * 取得所有线结束点的数据
	 * 
	 * @return List<PointGlueLineEndParam>
	 */
	public List<PointGlueLineEndParam> findAllGlueLineEndParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineEndParam> endLists = null;
		PointGlueLineEndParam end = null;

		Cursor cursor = db.query(TableLineEnd.LINE_END_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			endLists = new ArrayList<PointGlueLineEndParam>();
			while (cursor.moveToNext()) {
				end = new PointGlueLineEndParam();
				end.set_id(cursor.getInt(cursor.getColumnIndex(TableLineEnd._ID)));
				end.setStopGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineEnd.STOP_GLUE_TIME_PREV)));
				end.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineEnd.STOP_GLUE_TIME)));
				end.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineEnd.UP_HEIGHT)));
				end.setBreakGlueLen(cursor.getInt(cursor.getColumnIndex(TableLineEnd.BREAK_GLUE_LEN)));
				end.setDrawDistance(cursor.getInt(cursor.getColumnIndex(TableLineEnd.DRAW_DISTANCE)));
				end.setDrawSpeed(cursor.getInt(cursor.getColumnIndex(TableLineEnd.DRAW_SPEED)));
				end.setPause(cursor.getInt(cursor.getColumnIndex(TableLineEnd.IS_PAUSE)) == 0 ? false : true);

				endLists.add(end);

			}
		}
		cursor.close();
		db.close();

		return endLists;
	}

	/**
	 * 通过id得到PointGlueLineEndParam参数
	 * 
	 * @param id
	 * @return PointGlueLineEndParam
	 */
	public PointGlueLineEndParam getPointGlueLineEndParamByID(int id) {
		PointGlueLineEndParam param = new PointGlueLineEndParam();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableLineEnd.LINE_END_TABLE, columns, TableLineEnd._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param.set_id(cursor.getInt(cursor.getColumnIndex(TableLineEnd._ID)));
					param.setStopGlueTimePrev(cursor.getInt(cursor.getColumnIndex(TableLineEnd.STOP_GLUE_TIME_PREV)));
					param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineEnd.STOP_GLUE_TIME)));
					param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineEnd.UP_HEIGHT)));
					param.setBreakGlueLen(cursor.getInt(cursor.getColumnIndex(TableLineEnd.BREAK_GLUE_LEN)));
					param.setDrawDistance(cursor.getInt(cursor.getColumnIndex(TableLineEnd.DRAW_DISTANCE)));
					param.setDrawSpeed(cursor.getInt(cursor.getColumnIndex(TableLineEnd.DRAW_SPEED)));
					param.setPause(cursor.getInt(cursor.getColumnIndex(TableLineEnd.IS_PAUSE)) == 0 ? false : true);

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
	 * 通过List<Integer> 列表来查找对应的 PointGlueLineEndParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueLineEndParam>
	 */
	public List<PointGlueLineEndParam> getPointGlueLineEndParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineEndParam> params = new ArrayList<>();
		PointGlueLineEndParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableLineEnd.LINE_END_TABLE, columns, TableLineEnd._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueLineEndParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableLineEnd._ID)));
						param.setStopGlueTimePrev(
								cursor.getInt(cursor.getColumnIndex(TableLineEnd.STOP_GLUE_TIME_PREV)));
						param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableLineEnd.STOP_GLUE_TIME)));
						param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableLineEnd.UP_HEIGHT)));
						param.setBreakGlueLen(cursor.getInt(cursor.getColumnIndex(TableLineEnd.BREAK_GLUE_LEN)));
						param.setDrawDistance(cursor.getInt(cursor.getColumnIndex(TableLineEnd.DRAW_DISTANCE)));
						param.setDrawSpeed(cursor.getInt(cursor.getColumnIndex(TableLineEnd.DRAW_SPEED)));
						param.setPause(cursor.getInt(cursor.getColumnIndex(TableLineEnd.IS_PAUSE)) == 0 ? false : true);
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
	 * 通过参数方案寻找到当前方案的主键(Messagemgr放进去的停前延时为‘停胶后延时’,所有'停胶前延时'是没有数据的)
	 * 
	 * @param pointGlueLineEndParam
	 * @return 当前方案的主键
	 */
	public int getLineEndParamIDByParam(PointGlueLineEndParam pointGlueLineEndParam) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableLineEnd.LINE_END_TABLE, columns,
				TableLineEnd.STOP_GLUE_TIME_PREV + "=? and " + TableLineEnd.STOP_GLUE_TIME + "=? and "
						+ TableLineEnd.UP_HEIGHT + "=? and " + TableLineEnd.BREAK_GLUE_LEN + "=? and "
						+ TableLineEnd.DRAW_DISTANCE + "=? and " + TableLineEnd.DRAW_SPEED + "=? and "
						+ TableLineEnd.IS_PAUSE + "=?",
				new String[] { String.valueOf(pointGlueLineEndParam.getStopGlueTimePrev()),
						String.valueOf(pointGlueLineEndParam.getStopGlueTime()),
						String.valueOf(pointGlueLineEndParam.getUpHeight()),
						String.valueOf(pointGlueLineEndParam.getBreakGlueLen()),
						String.valueOf(pointGlueLineEndParam.getDrawDistance()),
						String.valueOf(pointGlueLineEndParam.getDrawSpeed()),
						String.valueOf(pointGlueLineEndParam.isPause() ? 1 : 0), },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableLineEnd._ID));
			}
		}
		db.close();
		if (-1 == id) {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TableLineEnd.LINE_END_TABLE, columns,
					TableLineEnd.STOP_GLUE_TIME + "=? and " + TableLineEnd.UP_HEIGHT + "=? and "
							+ TableLineEnd.BREAK_GLUE_LEN + "=? and " + TableLineEnd.DRAW_DISTANCE + "=? and "
							+ TableLineEnd.DRAW_SPEED + "=? and " + TableLineEnd.IS_PAUSE + "=?",
					new String[] { String.valueOf(pointGlueLineEndParam.getStopGlueTime()),
							String.valueOf(pointGlueLineEndParam.getUpHeight()),
							String.valueOf(pointGlueLineEndParam.getBreakGlueLen()),
							String.valueOf(pointGlueLineEndParam.getDrawDistance()),
							String.valueOf(pointGlueLineEndParam.getDrawSpeed()),
							String.valueOf(pointGlueLineEndParam.isPause() ? 1 : 0), },
					null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex(TableLineEnd._ID));
				}
			}
			db.close();
			if(-1 == id){
				id = (int) insertGlueLineEnd(pointGlueLineEndParam);
			}
		}
		return id;
	}

	public int deleteParam(PointGlueLineEndParam param) {
		db = dbHelper.getWritableDatabase();
		int rowID = db.delete(DBInfo.TableLineEnd.LINE_END_TABLE, TableLineEnd._ID + "=?",
				new String[] { String.valueOf(param.get_id()) });

		db.close();
		return rowID;
	}
}
