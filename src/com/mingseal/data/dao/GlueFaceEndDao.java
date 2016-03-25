/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo;
import com.mingseal.data.db.DBInfo.TableFaceEnd;
import com.mingseal.data.db.DBInfo.TableFaceStart;
import com.mingseal.data.point.glueparam.PointGlueFaceEndParam;
import com.mingseal.data.point.glueparam.PointGlueFaceStartParam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳 @
 */
public class GlueFaceEndDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;

	String[] columns = { TableFaceEnd._ID, TableFaceEnd.STOP_GLUE_TIME, TableFaceEnd.UP_HEIGHT, TableFaceEnd.LINE_NUM,
			TableFaceEnd.IS_PAUSE };

	public GlueFaceEndDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 插入一条面结束点的数据
	 * 
	 * @param pointGlueFaceEndParam
	 * @return 刚插入结束点的id
	 */
	public long insertGlueFaceEnd(PointGlueFaceEndParam pointGlueFaceEndParam) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();

		values.put(TableFaceEnd.STOP_GLUE_TIME, pointGlueFaceEndParam.getStopGlueTime());
		values.put(TableFaceEnd.UP_HEIGHT, pointGlueFaceEndParam.getUpHeight());
		values.put(TableFaceEnd.LINE_NUM, pointGlueFaceEndParam.getLineNum());
		values.put(TableFaceEnd.IS_PAUSE, (boolean) pointGlueFaceEndParam.isPause() ? 1 : 0);

		long rowID = db.insert(TableFaceEnd.FACE_END_TABLE, TableFaceEnd._ID, values);

		db.close();
		return rowID;
	}

	/**
	 * 取得所有的面结束点的方案
	 * 
	 * @return
	 */
	public List<PointGlueFaceEndParam> findAllGlueFaceEndParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueFaceEndParam> endLists = null;
		PointGlueFaceEndParam end = null;

		Cursor cursor = db.query(TableFaceEnd.FACE_END_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			endLists = new ArrayList<PointGlueFaceEndParam>();
			while (cursor.moveToNext()) {
				end = new PointGlueFaceEndParam();
				end.set_id(cursor.getInt(cursor.getColumnIndex(TableFaceEnd._ID)));
				end.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.STOP_GLUE_TIME)));
				end.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.UP_HEIGHT)));
				end.setLineNum(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.LINE_NUM)));
				end.setPause(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.IS_PAUSE)) == 0 ? false : true);

				endLists.add(end);
			}
		}
		cursor.close();
		db.close();
		return endLists;
	}

	/**
	 * 通过id找到面结束点参数
	 * 
	 * @param id
	 * @return PointGlueFaceEndParam
	 */
	public PointGlueFaceEndParam getPointFaceEndParamByID(int id) {
		PointGlueFaceEndParam param = new PointGlueFaceEndParam();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableFaceEnd.FACE_END_TABLE, columns, TableFaceEnd._ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.STOP_GLUE_TIME)));
					param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.UP_HEIGHT)));
					param.setLineNum(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.LINE_NUM)));
					param.setPause(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.IS_PAUSE)) == 0 ? false : true);

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
	 * 通过List<Integer> 列表来查找对应的PointGlueFaceEndParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueFaceEndParam>
	 */
	public List<PointGlueFaceEndParam> getGlueFaceEndParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueFaceEndParam> params = new ArrayList<>();
		PointGlueFaceEndParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableFaceEnd.FACE_END_TABLE, columns, TableFaceEnd._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueFaceEndParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableFaceEnd._ID)));
						param.setStopGlueTime(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.STOP_GLUE_TIME)));
						param.setUpHeight(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.UP_HEIGHT)));
						param.setLineNum(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.LINE_NUM)));
						param.setPause(cursor.getInt(cursor.getColumnIndex(TableFaceEnd.IS_PAUSE)) == 0 ? false : true);
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
	 * 通过参数方案寻找到当前方案的主键
	 * 
	 * @param pointGlueFaceEndParam
	 * @return 当前方案的主键
	 */
	public int getFaceEndParamIDByParam(PointGlueFaceEndParam pointGlueFaceEndParam) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableFaceEnd.FACE_END_TABLE, columns,
				TableFaceEnd.STOP_GLUE_TIME + "=? and " + TableFaceEnd.UP_HEIGHT + "=? and " + TableFaceEnd.LINE_NUM
						+ "=? and " + TableFaceEnd.IS_PAUSE + "=?",
				new String[] { String.valueOf(pointGlueFaceEndParam.getStopGlueTime()),
						String.valueOf(pointGlueFaceEndParam.getUpHeight()),
						String.valueOf(pointGlueFaceEndParam.getLineNum()),
						String.valueOf(pointGlueFaceEndParam.isPause() ? 1 : 0), },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableFaceEnd._ID));
			}
		}
		db.close();
		if (-1 == id) {
			id = (int) insertGlueFaceEnd(pointGlueFaceEndParam);
		}
		return id;
	}
	/**
	 * @Title  deletepointGlueFaceEndParam
	 * @Description 删除某一行数据
	 * @author wj
	 * @param pointGlueAloneParam
	 * @return 1为成功删除，0为未成功删除
	 */
	public Integer deleteParam(PointGlueFaceEndParam pointGlueFaceEndParam) {
		db = dbHelper.getWritableDatabase();
		int rowID = db.delete(DBInfo.TableFaceEnd.FACE_END_TABLE, TableFaceEnd._ID + "=?",
				new String[] { String.valueOf(pointGlueFaceEndParam.get_id()) });

		db.close();
		return rowID;
	}
}
