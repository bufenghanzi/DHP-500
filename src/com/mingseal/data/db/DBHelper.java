/**
 * 
 */
package com.mingseal.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author 商炎炳
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库名称
	 * @param factory
	 *            游标工厂
	 * @param version
	 *            数据库版本
	 */
	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DBHelper(Context context) {
		this(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBInfo.TableUser.CREATE_USER_TABLE);
		db.execSQL(DBInfo.TableAlone.CREATE_ALONE_TABLE);
		db.execSQL(DBInfo.TableFaceStart.CREATE_FACE_START_TABLE);
		db.execSQL(DBInfo.TablePoint.CREATE_POINT_TABLE);
		db.execSQL(DBInfo.TableFaceEnd.CREATE_FACE_END_TABLE);
		db.execSQL(DBInfo.TableClear.CREATE_CLEAR_TABLE);
		db.execSQL(DBInfo.TableLineStart.CREATE_LINE_START_TABLE);
		db.execSQL(DBInfo.TableLineMid.CREATE_LINE_Mid_TABLE);
		db.execSQL(DBInfo.TableLineEnd.CREATE_LINE_END_TABLE);
		db.execSQL(DBInfo.TableOutputIO.CREATE_OUTPUT_IO_TABLE);
		db.execSQL(DBInfo.TableInputIO.CREATE_INPUT_IO_TABLE);
		db.execSQL(DBInfo.TablePointTask.CREATE_TASK_TABLE);
	}

	/*
	 * VERSION改变会更新所有的表
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.
	 * sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBInfo.TableUser.DROP_USER_TABLE);
		db.execSQL(DBInfo.TableAlone.DROP_ALONE_TABLE);
		db.execSQL(DBInfo.TableFaceStart.DROP_FACE_START_TABLE);
		db.execSQL(DBInfo.TablePoint.DROP_POINT_TABLE);
		db.execSQL(DBInfo.TableFaceEnd.DROP_FACE_END_TABLE);
		db.execSQL(DBInfo.TableClear.DROP_CLEAR_TABLE);
		db.execSQL(DBInfo.TableLineStart.DROP_LINE_START_TABLE);
		db.execSQL(DBInfo.TableLineMid.DROP_LINE_MID_TABLE);
		db.execSQL(DBInfo.TableLineEnd.DROP_LINE_END_TABLE);
		db.execSQL(DBInfo.TableOutputIO.DROP_OUTPUT_IO_TABLE);
		db.execSQL(DBInfo.TableInputIO.DROP_INPUT_IO_TABLE);
		db.execSQL(DBInfo.TablePointTask.DROP_POINT_TABLE);
		onCreate(db);
	}

}
