/**
 * 
 */
package com.mingseal.data.dao;

import java.util.ArrayList;
import java.util.List;

import com.mingseal.data.db.DBHelper;
import com.mingseal.data.db.DBInfo.TableUser;
import com.mingseal.data.user.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author 商炎炳
 *
 */
public class UserDao {

	private DBHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private ContentValues values = null;
	String[] columns = { TableUser._ID, TableUser.USERNAME, TableUser.PASSWORD, TableUser.TYPE };

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public UserDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 插入一条用户数据
	 * 
	 * @param user
	 * @return 刚插入的数据的主键
	 */
	public long insertUser(User user) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(TableUser.USERNAME, user.getUsername());
		values.put(TableUser.PASSWORD, user.getPassword());
		values.put(TableUser.TYPE, user.getType());

		long rowID = db.insert(TableUser.USER_TABLE, TableUser._ID, values);
		// 关闭资源
		db.close();
		return rowID;
	}

	/**
	 * 插入一个用户List数据
	 * 
	 * @param users
	 *            用户集合
	 * @return rowIds 刚插入的用户集合的主键集合
	 */
	public List<Integer> insertUsers(List<User> users) {
		db = dbHelper.getWritableDatabase();
		List<Integer> rowIds = new ArrayList<Integer>();
		int rowId;
		try {
			db.beginTransaction();
			for (User user : users) {
				values = new ContentValues();
				values.put(TableUser.USERNAME, user.getUsername());
				values.put(TableUser.PASSWORD, user.getPassword());
				values.put(TableUser.TYPE, user.getType());
				rowId = (int) db.insert(TableUser.USER_TABLE, TableUser._ID, values);
				rowIds.add(rowId);
			}

			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return rowIds;
	}

	/**
	 * 找出所有的User
	 * 
	 * @return User的List集合
	 */
	public List<User> findAllUserLists() {
		db = dbHelper.getReadableDatabase();
		List<User> users = new ArrayList<User>();
		User user = null;
		Cursor cursor = db.query(TableUser.USER_TABLE, columns, null, null, null, null, null);
		try {
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					user = new User();
					user.setId(cursor.getInt(cursor.getColumnIndex(TableUser._ID)));
					user.setUsername(cursor.getString(cursor.getColumnIndex(TableUser.USERNAME)));
					user.setPassword(cursor.getString(cursor.getColumnIndex(TableUser.PASSWORD)));
					user.setType(cursor.getString(cursor.getColumnIndex(TableUser.TYPE)));
					users.add(user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}
		return users;

	}

	/**
	 * 检查数据库中是否存在该数据
	 * 
	 * @param user
	 * @return -1表示不存在,>0表示存在
	 */
	public int checkUserExist(User user) {
		// boolean isExist = false;

		int primaryId = -1;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableUser.USER_TABLE, columns,
				TableUser.USERNAME + "=?" + " AND " + TableUser.PASSWORD + "=?" + " AND " + TableUser.TYPE + "=?",
				new String[] { user.getUsername(), user.getPassword(), user.getType() }, null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					primaryId = cursor.getInt(cursor.getColumnIndex(TableUser._ID));
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

		return primaryId;

	}

	/**
	 * 更新用户信息,只更改密码和类型(一般情况都不允许更改)
	 * 
	 * @param user
	 * @return 刚更新的用户信息的id
	 */
	public int updateUser(User user) {
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();
		values.put(TableUser.PASSWORD, user.getPassword());
		values.put(TableUser.TYPE, user.getType());
		int rowid = db.update(TableUser.USER_TABLE, values, TableUser._ID + "=?",
				new String[] { String.valueOf(user.getId()) });

		db.close();
		return rowid;
	}

	/**
	 * 通过用户名查找数据库中是否存在该数据
	 * 
	 * @param username
	 * @return true存在,false不存在
	 */
	public boolean checkUserByUsername(String username) {
		boolean isExist = false;
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TableUser.USER_TABLE, columns, TableUser.USERNAME + "=?", new String[] { username },
				null, null, null);

		try {
			db.beginTransaction();
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					isExist = true;
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

		return isExist;
	}

}
