/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo.TableOutputIO;
import com.mingseal.data.point.glueparam.PointGlueOutputIOParam;
import com.mingseal.utils.ArraysComprehension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class GlueOutputDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;

	String[] columns = { TableOutputIO._ID, TableOutputIO.GO_TIME_PREV, TableOutputIO.GO_TIME_NEXT,
			TableOutputIO.INPUT_PORT };

	public GlueOutputDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 增加一条输出IO的数据
	 * 
	 * @param param
	 * @return 刚增加的这条数据的主键
	 */
	public long insertGlueOutput(PointGlueOutputIOParam param) {
		db = dbHelper.getWritableDatabase();

		values = new ContentValues();
		values.put(TableOutputIO.GO_TIME_PREV, param.getGoTimePrev());
		values.put(TableOutputIO.GO_TIME_NEXT, param.getGoTimeNext());
		values.put(TableOutputIO.INPUT_PORT, Arrays.toString(param.getInputPort()));

		long rowID = db.insert(TableOutputIO.OUTPUT_IO_TABLE, TableOutputIO._ID, values);

		db.close();

		return rowID;
	}

	/**
	 * 取得所有输出IO的数据
	 * 
	 * @return List<PointGlueOutputIOParam>
	 */
	public List<PointGlueOutputIOParam> findAllGlueOutputParams() {
		db = dbHelper.getReadableDatabase();
		List<PointGlueOutputIOParam> outputIOParams = null;
		PointGlueOutputIOParam output = null;

		Cursor cursor = db.query(TableOutputIO.OUTPUT_IO_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			outputIOParams = new ArrayList<PointGlueOutputIOParam>();
			while (cursor.moveToNext()) {
				output = new PointGlueOutputIOParam();
				output.set_id(cursor.getInt(cursor.getColumnIndex(TableOutputIO._ID)));
				output.setGoTimePrev(cursor.getInt(cursor.getColumnIndex(TableOutputIO.GO_TIME_PREV)));
				output.setGoTimeNext(cursor.getInt(cursor.getColumnIndex(TableOutputIO.GO_TIME_NEXT)));
				output.setInputPort(ArraysComprehension
						.boooleanParse(cursor.getString(cursor.getColumnIndex(TableOutputIO.INPUT_PORT))));

				outputIOParams.add(output);
			}
		}

		cursor.close();
		db.close();
		return outputIOParams;

	}

	/**
	 * 通过List<Integer> 列表来查找对应的PointGlueOutputIOParam集合
	 * 
	 * @param ids
	 * @return List<PointGlueOutputIOParam>
	 */
	public List<PointGlueOutputIOParam> getGlueOutputIOParamsByIDs(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<PointGlueOutputIOParam> params = new ArrayList<>();
		PointGlueOutputIOParam param = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TableOutputIO.OUTPUT_IO_TABLE, columns, TableOutputIO._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						param = new PointGlueOutputIOParam();
						param.set_id(cursor.getInt(cursor.getColumnIndex(TableOutputIO._ID)));
						param.setGoTimePrev(cursor.getInt(cursor.getColumnIndex(TableOutputIO.GO_TIME_PREV)));
						param.setGoTimeNext(cursor.getInt(cursor.getColumnIndex(TableOutputIO.GO_TIME_NEXT)));
						param.setInputPort(ArraysComprehension
								.boooleanParse(cursor.getString(cursor.getColumnIndex(TableOutputIO.INPUT_PORT))));

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
	 * 通过参数寻找到当前方案的主键
	 * 
	 * @param pointGlueOutPutIOParam
	 * @return 当前方案的主键
	 */
	public int getOutputParamIDByParam(PointGlueOutputIOParam pointGlueOutPutIOParam) {
		int id = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableOutputIO.OUTPUT_IO_TABLE, columns,
				TableOutputIO.GO_TIME_PREV + "=? and " + TableOutputIO.GO_TIME_NEXT + "=? and "
						+ TableOutputIO.INPUT_PORT + "=?",
				new String[] { String.valueOf(pointGlueOutPutIOParam.getGoTimePrev()),
						String.valueOf(pointGlueOutPutIOParam.getGoTimeNext()),
						Arrays.toString(pointGlueOutPutIOParam.getInputPort()) },
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				id = cursor.getInt(cursor.getColumnIndex(TableOutputIO._ID));
			}
		}
		db.close();
		return id;
	}

	/**
	 * @Title getOutPutPointByID
	 * @Description 通过主键寻找到当前输出口的参数方案
	 * @param id
	 *            主键
	 * @return PointGlueOutputIOParam
	 */
	public PointGlueOutputIOParam getOutPutPointByID(int id) {
		db = dbHelper.getReadableDatabase();
		PointGlueOutputIOParam param = null;
		try {
			db.beginTransaction();
			Cursor cursor = db.query(TableOutputIO.OUTPUT_IO_TABLE, columns, TableOutputIO._ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					param = new PointGlueOutputIOParam();
					param.set_id(cursor.getInt(cursor.getColumnIndex(TableOutputIO._ID)));
					param.setGoTimePrev(cursor.getInt(cursor.getColumnIndex(TableOutputIO.GO_TIME_PREV)));
					param.setGoTimeNext(cursor.getInt(cursor.getColumnIndex(TableOutputIO.GO_TIME_NEXT)));
					param.setInputPort(ArraysComprehension
							.boooleanParse(cursor.getString(cursor.getColumnIndex(TableOutputIO.INPUT_PORT))));

				}
			}
			cursor.close();
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return param;
	}

}
