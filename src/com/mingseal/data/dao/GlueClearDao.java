/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo.TableClear;
import com.mingseal.data.point.glueparam.PointGlueClearParam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 * @description 清胶点Dao
 */
public class GlueClearDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;

	String[] columns = { TableClear._ID, TableClear.CLEAR_GLUE_TIME };

	public GlueClearDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 插入一条清胶点的数据
	 * 
	 * @param pointGlueClearParam
	 * @return 刚插入清胶点的id
	 */
	public long insertGlueClear(PointGlueClearParam pointGlueClearParam) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();

		values.put(TableClear.CLEAR_GLUE_TIME, pointGlueClearParam.getClearGlueTime());

		long rowID = db.insert(TableClear.CLEAR_TABLE, TableClear._ID, values);

		db.close();
		return rowID;
	}

	/**
	 * 取得所有清胶点的方案
	 * 
	 * @return List<PointGlueClearParam>
	 */
	public List<PointGlueClearParam> findAllGlueClearParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueClearParam> clearLists = null;
		PointGlueClearParam clear = null;

		Cursor cursor = db.query(TableClear.CLEAR_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			clearLists = new ArrayList<PointGlueClearParam>();
			while (cursor.moveToNext()) {
				clear = new PointGlueClearParam();
				clear.set_id(cursor.getInt(cursor.getColumnIndex(TableClear._ID)));
				clear.setClearGlueTime(cursor.getInt(cursor.getColumnIndex(TableClear.CLEAR_GLUE_TIME)));

				clearLists.add(clear);
			}
		}
		cursor.close();
		db.close();

		return clearLists;
	}

	/**
	 * 通过List<Integer>列表来查找对应的PointGlueClearParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueClearParam>
	 */
	public List<PointGlueClearParam> getGlueClearParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueClearParam> params = new ArrayList<>();
		PointGlueClearParam param = null;

		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableClear.CLEAR_TABLE, columns, TableClear._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueClearParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableClear._ID)));
						param.setClearGlueTime(cursor.getInt(cursor.getColumnIndex(TableClear.CLEAR_GLUE_TIME)));

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
}
