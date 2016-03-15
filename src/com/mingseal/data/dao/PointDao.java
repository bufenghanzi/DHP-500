/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo.TablePoint;
import com.mingseal.data.point.Point;
import com.mingseal.data.point.PointParam;
import com.mingseal.data.point.PointType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class PointDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TablePoint._ID, TablePoint.POINT_X, TablePoint.POINT_Y, TablePoint.POINT_Z, TablePoint.POINT_U,
			TablePoint.POINT_PARAM_ID, TablePoint.POINT_TYPE };

	public PointDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 插入一条point数据
	 * 
	 * @param point
	 * @return
	 */
	public long insertPoint(Point point) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(TablePoint.POINT_X, point.getX());
		values.put(TablePoint.POINT_Y, point.getY());
		values.put(TablePoint.POINT_Z, point.getZ());
		values.put(TablePoint.POINT_U, point.getU());
		values.put(TablePoint.POINT_PARAM_ID, point.getPointParam().get_id());
		values.put(TablePoint.POINT_TYPE, point.getPointParam().getPointType().toString());

		long rowID = db.insert(TablePoint.POINT_TABLE, TablePoint._ID, values);

		// 释放资源
		db.close();
		return rowID;
	}

	/**
	 * 插入一条List<Point> 集合
	 * 
	 * @param pointLists
	 * @return 刚插入的List<Point>的主键集合
	 */
	public List<Integer> insertPoints(List<Point> pointLists) {
		db = dbHelper.getWritableDatabase();
		List<Integer> rowids = new ArrayList<Integer>();
		int rowid;
		try {
			db.beginTransaction();

			for (Point point : pointLists) {
				values = new ContentValues();
				values.put(TablePoint.POINT_X, point.getX());
				values.put(TablePoint.POINT_Y, point.getY());
				values.put(TablePoint.POINT_Z, point.getZ());
				values.put(TablePoint.POINT_U, point.getU());
				values.put(TablePoint.POINT_PARAM_ID, point.getPointParam().get_id());
				values.put(TablePoint.POINT_TYPE, point.getPointParam().getPointType().toString());

				rowid = (int) db.insert(TablePoint.POINT_TABLE, TablePoint._ID, values);
				rowids.add(rowid);

			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			// 释放资源
			db.close();
		}
		return rowids;
	}

	/**
	 * 找到所有的Point集合
	 * 
	 * @return
	 */
	public List<Point> findAllPointLists() {
		db = dbHelper.getReadableDatabase();
		List<Point> pointList = new ArrayList<Point>();
		Point point = null;
		Cursor cursor = db.query(TablePoint.POINT_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				point = new Point(PointType.POINT_NULL);
				point.setId(cursor.getInt(cursor.getColumnIndex(TablePoint._ID)));
				point.setX(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_X)));
				point.setY(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_Y)));
				point.setZ(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_Z)));
				point.setU(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_U)));
				PointParam pointParam = new PointParam();
				pointParam.set_id(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_PARAM_ID)));
				pointParam.setPointType(PointType.valueOf(PointType.class,
						cursor.getString(cursor.getColumnIndex(TablePoint.POINT_TYPE))));
				point.setPointParam(pointParam);
				pointList.add(point);
			}
		}
		cursor.close();
		db.close();
		return pointList;
	}

	/**
	 * 删除一条Point数据
	 * 
	 * @param point
	 */
	public void deletePoint(Point point) {
		db = dbHelper.getWritableDatabase();

		db.delete(TablePoint.POINT_TABLE, TablePoint._ID + "=?", new String[] { String.valueOf(point.getId()) });

		db.close();
	}

	/**
	 * 删除PointList集合
	 * 
	 * @param pointLists
	 */
	public void deletePoints(List<Point> pointLists) {
		db = dbHelper.getWritableDatabase();

		try {
			db.beginTransaction();
			for (Point point : pointLists) {
				db.delete(TablePoint.POINT_TABLE, TablePoint._ID + "=?",
						new String[] { String.valueOf(point.getId()) });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {

		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 删除任务时,删除该任务下的所有任务点
	 * 
	 * @param pointIds
	 *            List<Integer>
	 */
	public void deletePointsByIds(List<Integer> pointIds) {
		db = dbHelper.getWritableDatabase();

		try {
			db.beginTransaction();
			for (int id : pointIds) {
				db.delete(TablePoint.POINT_TABLE, TablePoint._ID + "=?", new String[] { String.valueOf(id) });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {

		} finally {
			db.endTransaction();
			db.close();
		}
	}

	/**
	 * 通过List<Integer> 列表来查找到对应的Point集合
	 * 
	 * @param ids
	 * @return List<Point>
	 */
	public List<Point> findALLPointsByIdLists(List<Integer> ids) {
		db = dbHelper.getReadableDatabase();
		List<Point> pointList = new ArrayList<Point>();
		Point point = null;
		try {
			db.beginTransaction();
			for (Integer id : ids) {
				Cursor cursor = db.query(TablePoint.POINT_TABLE, columns, TablePoint._ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					while (cursor.moveToNext()) {
						point = new Point(PointType.POINT_NULL);
						point.setId(cursor.getInt(cursor.getColumnIndex(TablePoint._ID)));
						point.setX(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_X)));
						point.setY(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_Y)));
						point.setZ(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_Z)));
						point.setU(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_U)));
						PointParam pointParam = new PointParam();
						pointParam.set_id(cursor.getInt(cursor.getColumnIndex(TablePoint.POINT_PARAM_ID)));
						pointParam.setPointType(PointType.valueOf(PointType.class,
								cursor.getString(cursor.getColumnIndex(TablePoint.POINT_TYPE))));
						point.setPointParam(pointParam);
						pointList.add(point);
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
		return pointList;
	}

}
