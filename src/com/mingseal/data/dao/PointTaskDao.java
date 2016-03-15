/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo.TablePointTask;
import com.mingseal.data.point.PointTask;
import com.mingseal.utils.DateUtil;
import com.mingseal.utils.ListResolving;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class PointTaskDao {
	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TablePointTask._ID, TablePointTask.TASK_NAME, TablePointTask.POINT_IDS };

	public PointTaskDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 增加一条任务
	 * 
	 * @param pointTask
	 * @return 刚插入的任务的主键
	 */
	public long insertTask(PointTask pointTask) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(TablePointTask.TASK_NAME, pointTask.getTaskName());
		values.put(TablePointTask.POINT_IDS, pointTask.getPointids().toString());
		values.put(TablePointTask.CREATE_TIME, DateUtil.getCurrentDate());
		values.put(TablePointTask.MODIFY_TIME, DateUtil.getCurrentDate());
		long rowID = db.insert(TablePointTask.TASK_TABLE, TablePointTask._ID, values);
		db.close();

		return rowID;
	}

	/**
	 * 删除一个任务
	 * 
	 * @param pointTask
	 */
	public void deleteTask(PointTask pointTask) {
		db = dbHelper.getWritableDatabase();
		db.delete(TablePointTask.TASK_TABLE, TablePointTask._ID + "=?",
				new String[] { String.valueOf(pointTask.getId()) });
		db.close();
	}

	/**
	 * 找到所有任务的集合(Pointids还没获取到)
	 * 
	 * @return 所有任务的集合
	 */
	public List<PointTask> findALLTaskLists() {
		db = dbHelper.getReadableDatabase();
		List<PointTask> pointTasks = new ArrayList<PointTask>();
		PointTask task = null;
		Cursor cursor = db.query(TablePointTask.TASK_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				task = new PointTask();
				task.setId(cursor.getInt(cursor.getColumnIndex(TablePointTask._ID)));
				task.setTaskName(cursor.getString(cursor.getColumnIndex(TablePointTask.TASK_NAME)));
				task.setPointids(
						ListResolving.listParse(cursor.getString(cursor.getColumnIndex(TablePointTask.POINT_IDS))));
				pointTasks.add(task);
			}
		}
		cursor.close();
		db.close();
		return pointTasks;
	}

	/**
	 * 更新一个任务
	 * 
	 * @param pointTask
	 * @return
	 */
	public int updateTask(PointTask pointTask) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(TablePointTask.TASK_NAME, pointTask.getTaskName());
		values.put(TablePointTask.POINT_IDS, pointTask.getPointids().toString());
		values.put(TablePointTask.MODIFY_TIME, DateUtil.getCurrentDate());

		int rowids = db.update(TablePointTask.TASK_TABLE, values, TablePointTask._ID + "=?",
				new String[] { String.valueOf(pointTask.getId()) });

		db.close();
		return rowids;
	}
	
	/**
	 * 找到所有任务的任务名集合
	 * 
	 * @return 所有任务的任务名集合
	 */
	public List<String> getALLTaskNames() {
		db = dbHelper.getReadableDatabase();
		List<String> taskNames = new ArrayList<>();
		String name = "";
		Cursor cursor = db.query(TablePointTask.TASK_TABLE, columns, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				name = cursor.getString(cursor.getColumnIndex(TablePointTask.TASK_NAME));
				taskNames.add(name);
			}
		}
		cursor.close();
		db.close();
		return taskNames;
	}

}
