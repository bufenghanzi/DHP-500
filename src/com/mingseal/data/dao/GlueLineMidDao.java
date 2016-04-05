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
import com.mingseal.data.db.DBInfo.TableLineMid;
import com.mingseal.data.db.DBInfo.TableLineStart;
import com.mingseal.data.point.glueparam.PointGlueAloneParam;
import com.mingseal.data.point.glueparam.PointGlueLineMidParam;
import com.mingseal.utils.ArraysComprehension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class GlueLineMidDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TableLineMid._ID, TableLineMid.MOVE_SPEED, TableLineMid.RADIUS,
			TableLineMid.STOP_GLUE_DIS_PREV, TableLineMid.STOP_GLUE_DIS_NEXT, TableLineMid.IS_OUT_GLUE,
			TableLineMid.GLUE_PORT };

	public GlueLineMidDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * @Title  upDateGlueAlone
	 * @Description 更新一条独立点数据
	 * @author wj
	 * @param pointGlueAloneParam
	 * @return  影响的行数，0表示错误
	 */
	public int upDateGlueLineMid(PointGlueLineMidParam pointGlueLineMidParam){
		int rowid = 0;
		try {
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableLineMid.MOVE_SPEED, pointGlueLineMidParam.getMoveSpeed());
			values.put(TableLineMid.RADIUS, pointGlueLineMidParam.getRadius());
			values.put(TableLineMid.STOP_GLUE_DIS_PREV, pointGlueLineMidParam.getStopGlueDisPrev());
			values.put(TableLineMid.STOP_GLUE_DIS_NEXT, pointGlueLineMidParam.getStopGLueDisNext());
			values.put(TableLineMid.IS_OUT_GLUE, (boolean) pointGlueLineMidParam.isOutGlue() ? 1 : 0);
			values.put(TableLineMid.GLUE_PORT, Arrays.toString(pointGlueLineMidParam.getGluePort()));
//			values.put(TableAlone.DOT_GLUE_TIME, pointGlueAloneParam.getDotGlueTime());
//			values.put(TableAlone.STOP_GLUE_TIME, pointGlueAloneParam.getStopGlueTime());
//			values.put(TableAlone.UP_HEIGHT, pointGlueAloneParam.getUpHeight());
//			values.put(TableAlone.IS_OUT_GLUE, (boolean) pointGlueAloneParam.isOutGlue() ? 1 : 0);
//			values.put(TableAlone.IS_PAUSE, (boolean) pointGlueAloneParam.isPause() ? 1 : 0);
//			values.put(TableAlone.GLUE_PORT, Arrays.toString(pointGlueAloneParam.getGluePort()));
			rowid = db.update(DBInfo.TableLineMid.LINE_MID_TABLE, values,TableLineMid._ID +"=?", new String[]{String.valueOf(pointGlueLineMidParam.get_id())});
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
	 * 增加一条线中间点的数据
	 * 
	 * @param pointGlueLineMidParam
	 * @return 刚增加的这条数据的主键
	 */
	public long insertGlueLineMid(PointGlueLineMidParam pointGlueLineMidParam) {
		long rowID = 0;
		db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();
			values = new ContentValues();
			values.put(TableLineMid._ID, pointGlueLineMidParam.get_id());
			values.put(TableLineMid.MOVE_SPEED, pointGlueLineMidParam.getMoveSpeed());
			values.put(TableLineMid.RADIUS, pointGlueLineMidParam.getRadius());
			values.put(TableLineMid.STOP_GLUE_DIS_PREV, pointGlueLineMidParam.getStopGlueDisPrev());
			values.put(TableLineMid.STOP_GLUE_DIS_NEXT, pointGlueLineMidParam.getStopGLueDisNext());
			values.put(TableLineMid.IS_OUT_GLUE, (boolean) pointGlueLineMidParam.isOutGlue() ? 1 : 0);
			values.put(TableLineMid.GLUE_PORT, Arrays.toString(pointGlueLineMidParam.getGluePort()));
			rowID = db.insert(TableLineMid.LINE_MID_TABLE, TableLineMid._ID, values);
			
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
	 * 取得所有线中间点的数据
	 * 
	 * @return
	 */
	public List<PointGlueLineMidParam> findAllGlueLineMidParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineMidParam> midLists = null;
		PointGlueLineMidParam mid = null;

		Cursor cursor = db.query(TableLineMid.LINE_MID_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			midLists = new ArrayList<PointGlueLineMidParam>();
			while (cursor.moveToNext()) {
				mid = new PointGlueLineMidParam();

				mid.set_id(cursor.getInt(cursor.getColumnIndex(TableLineMid._ID)));
				mid.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableLineMid.MOVE_SPEED)));
				mid.setRadius(cursor.getFloat(cursor.getColumnIndex(TableLineMid.RADIUS)));
				mid.setStopGlueDisPrev(cursor.getFloat(cursor.getColumnIndex(TableLineMid.STOP_GLUE_DIS_PREV)));
				mid.setStopGLueDisNext(cursor.getFloat(cursor.getColumnIndex(TableLineMid.STOP_GLUE_DIS_NEXT)));
				mid.setOutGlue(cursor.getInt(cursor.getColumnIndex(TableLineMid.IS_OUT_GLUE)) == 0 ? false : true);
				mid.setGluePort(ArraysComprehension
						.boooleanParse(cursor.getString(cursor.getColumnIndex(TableLineMid.GLUE_PORT))));

				midLists.add(mid);
			}
		}
		cursor.close();
		db.close();

		return midLists;
	}

	/**
	 * 通过主键找到PointGlueLineMidParam参数
	 * 
	 * @param id
	 * @return PointGlueLineMidParam
	 */
	public PointGlueLineMidParam getPointGlueLineMidParam(int id) {
		PointGlueLineMidParam param = new PointGlueLineMidParam();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableLineMid.LINE_MID_TABLE, columns, TableLineMid._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param.set_id(cursor.getInt(cursor.getColumnIndex(TableLineMid._ID)));
					param.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableLineMid.MOVE_SPEED)));
					param.setRadius(cursor.getFloat(cursor.getColumnIndex(TableLineMid.RADIUS)));
					param.setStopGlueDisPrev(cursor.getFloat(cursor.getColumnIndex(TableLineMid.STOP_GLUE_DIS_PREV)));
					param.setStopGLueDisNext(cursor.getFloat(cursor.getColumnIndex(TableLineMid.STOP_GLUE_DIS_NEXT)));
					param.setOutGlue(
							cursor.getInt(cursor.getColumnIndex(TableLineMid.IS_OUT_GLUE)) == 0 ? false : true);
					param.setGluePort(ArraysComprehension
							.boooleanParse(cursor.getString(cursor.getColumnIndex(TableLineMid.GLUE_PORT))));
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
	 * 通过List<Integer> 列表来查找对应的 PointGlueLineMidParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueLineMidParam>
	 */
	public List<PointGlueLineMidParam> getPointGlueLineMidParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueLineMidParam> params = new ArrayList<>();
		PointGlueLineMidParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableLineMid.LINE_MID_TABLE, columns, TableLineMid._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueLineMidParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableLineMid._ID)));
						param.setMoveSpeed(cursor.getInt(cursor.getColumnIndex(TableLineMid.MOVE_SPEED)));
						param.setRadius(cursor.getFloat(cursor.getColumnIndex(TableLineMid.RADIUS)));
						param.setStopGlueDisPrev(
								cursor.getFloat(cursor.getColumnIndex(TableLineMid.STOP_GLUE_DIS_PREV)));
						param.setStopGLueDisNext(
								cursor.getFloat(cursor.getColumnIndex(TableLineMid.STOP_GLUE_DIS_NEXT)));
						param.setOutGlue(
								cursor.getInt(cursor.getColumnIndex(TableLineMid.IS_OUT_GLUE)) == 0 ? false : true);
						param.setGluePort(ArraysComprehension
								.boooleanParse(cursor.getString(cursor.getColumnIndex(TableLineMid.GLUE_PORT))));
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
	 * 通过参数方案找到当前方案的主键(是否出胶是没有数据的)
	 * 
	 * @param pointGlueLineMidParam
	 * @return 当前方案的主键
	 */
	public int getLineMidParamIDByParam(PointGlueLineMidParam pointGlueLineMidParam) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableLineMid.LINE_MID_TABLE, columns,
				TableLineMid.MOVE_SPEED + "=? and " + TableLineMid.RADIUS + "=? and " + TableLineMid.STOP_GLUE_DIS_PREV
						+ "=? and " + TableLineMid.STOP_GLUE_DIS_NEXT + "=? and " + TableLineMid.IS_OUT_GLUE + "=? and "
						+ TableLineMid.GLUE_PORT + "=?",
				new String[] { String.valueOf(pointGlueLineMidParam.getMoveSpeed()),
						String.valueOf(pointGlueLineMidParam.getRadius()),
						String.valueOf(pointGlueLineMidParam.getStopGlueDisPrev()),
						String.valueOf(pointGlueLineMidParam.getStopGLueDisNext()),
						String.valueOf(pointGlueLineMidParam.isOutGlue() ? 1 : 0),
						Arrays.toString(pointGlueLineMidParam.getGluePort()) },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableLineMid._ID));
			}
		}
		db.close();
		if (-1 == id) {
			db = dbHelper.getReadableDatabase();
			cursor = db.query(TableLineMid.LINE_MID_TABLE, columns,
					TableLineMid.MOVE_SPEED + "=? and " + TableLineMid.RADIUS + "=? and "
							+ TableLineMid.STOP_GLUE_DIS_PREV + "=? and " + TableLineMid.STOP_GLUE_DIS_NEXT + "=? and "
							+ TableLineMid.GLUE_PORT + "=?",
					new String[] { String.valueOf(pointGlueLineMidParam.getMoveSpeed()),
							String.valueOf(pointGlueLineMidParam.getRadius()),
							String.valueOf(pointGlueLineMidParam.getStopGlueDisPrev()),
							String.valueOf(pointGlueLineMidParam.getStopGLueDisNext()),
							Arrays.toString(pointGlueLineMidParam.getGluePort()) },
					null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					id = cursor.getInt(cursor.getColumnIndex(TableLineMid._ID));
				}
			}
			db.close();
			if (-1 == id) {
				id = (int) insertGlueLineMid(pointGlueLineMidParam);
			}
		}
		return id;

	}

	/**
	 * @Title  deleteParam
	 * @Description 
	 * @author wj
	 * @param pointGlueLineMidParam
	 * @return
	 */
	public int deleteParam(PointGlueLineMidParam pointGlueLineMidParam) {
		db = dbHelper.getWritableDatabase();
		int rowID = db.delete(DBInfo.TableLineMid.LINE_MID_TABLE, TableLineMid._ID + "=?",
				new String[] { String.valueOf(pointGlueLineMidParam.get_id()) });

		db.close();
		return rowID;
	}

}
